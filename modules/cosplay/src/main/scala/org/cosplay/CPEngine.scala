/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cosplay

import org.apache.logging.log4j.LogManager
import impl.emuterm.*
import impl.guilog.*
import impl.jlineterm.*
import CPColor.*
import CPLifecycle.State.*
import CPKeyboardKey.*
import impl.*

import java.io.InterruptedIOException
import scala.collection.mutable

/*
   _________            ______________
   __  ____/_______________  __ \__  /_____ _____  __
   _  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
   / /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
   \____/  \____//____/ /_/     /_/  \__,_/ _\__, /
                                            /____/

          2D ASCII GAME ENGINE FOR SCALA3
            (C) 2021 Rowan Games, Inc.
               ALl rights reserved.
*/

/**
  * Global syntax sugar for throwing [[CPException]].
  *
  * @param msg Exception message.
  * @param cause Optional cause.
  */
def E[T](msg: String, cause: Throwable = null): T = throw new CPException(msg, cause)

/**
  * CosPlay game engine.
  *
  * Game engine is mostly an internal object and it is only used at the beginning of the game. It provides
  * variety of utility and miscellaneous methods for games.
  *
  * Most CosPlay games follow this basic game organization:
  * {{{
  *import org.cosplay.CPColor.*
  *import org.cosplay.*
  *
  *object Game:
  *    def main(args: Array[String]): Unit =
  *        val gameInfo = CPGameInfo(
  *            name = "Game Name",
  *            devName = "Game Developer",
  *            initDim = CPDim(100, 50)
  *        )
  *
  *        // Initialize the engine.
  *        CPEngine.init(gameInfo, System.console() == null || args.contains("emuterm"))
  *
  *        // Create game scenes.
  *        val sc1 = new CPScene(...)
  *        val sc2 = new CPScene(...)
  *
  *        // Start the game & wait for exit.
  *        try CPEngine.startGame(sc1, sc2)
  *        finally CPEngine.dispose()
  *
  *        sys.exit(0)
  * }}}
  * Notes:
  *  - Game start in a standard Scala way. It is recommended to use `main(...)` function for better
  *    compatibility.
  *  - Create [[CPGameInfo]] object that describes the game and its properties.
  *  - Initialize game engine by calling [[CPEngine.init()]] method passing it game descriptor and terminal emulation
  *    flag.
  *  - Create all necessary scenes, scene objects and assets. You can organize these objects in any desirable way - CosPlay
  *    does not impose any restrictions or limitation on how it is should be done.
  *  - Once you have all scenes are constructed - you can start the game by calling one of the [[CPEngine.startGame()]] methods.
  *  - Make sure to call [[CPEngine.dispose()]] method upon exit from [[CPEngine.startGame()]] method.
  *
  * @example See all examples under `org.cosplay.examples` package. Each example has a complete demonstration of
  *     working with game engine including initialization and game start.
  */
object CPEngine:
    private enum State:
        case ENG_INIT, ENG_STARTED, ENG_STOPPED

    private val FPS = 30 // Target FPS.
    private val FRAME_NANOS = 1_000_000_000 / FPS
    private val FRAME_MICROS = 1_000_000 / FPS
    private val FRAME_MILLIS = 1_000 / FPS
    private val FPS_LIST_SIZE = 500
    private val FPS_1PCT_LIST_SIZE = FPS_LIST_SIZE / 100
    private val scenes = CPContainer[CPScene]()
    private var term: CPTerminal = _
    private var pause = false
    private var frameCnt = 0L // Overall game fame count.
    private var scFrameCnt = 0L // Current scene fame count (each scene starts at zero).
    private var dbgStopAtFrameCnt: Option[Long] = None
    private var dbgKbKey: Option[CPKeyboardKey] = None
    private var gameInfo: CPGameInfo = _
    private var isShowFps = false
    private var kbReader: NativeKbReader = _
    private var kbKey: CPKeyboardKey = _
    private final val kbMux = AnyRef
    private final val pauseMux = AnyRef
    private var engLog: Log4jWrapper = _
    private val statsReg = mutable.HashSet.empty[CPRenderStatsListener]
    private val inputReg = mutable.HashSet.empty[CPInput]
    private var savedEx: Throwable = _
    @volatile private var state = State.ENG_INIT
    @volatile private var playing = true

    require(FPS_1PCT_LIST_SIZE > 0)

    /**
      * Target FPS of the game engine. If the actual frame rate exceeds this value the game engine will
      * throttle the execution and release spare cycle to the user logic. Default value is `30`.
      */
    val fps: Int = FPS

    /**
      * Milliseconds per frame assuming target FPS.
      */
    val frameMillis: Long = FRAME_MILLIS

    /**
      * Nanoseconds per frame assuming target FPS.
      */
    val frameNanos: Long = FRAME_NANOS

    /**
      * Microseconds per frame assuming target FPS.
      */
    val frameMicros: Long = FRAME_MICROS

    /**
      * Log4J2 wrapper for the log. Mirrors all log output to log4j2 rolling file appended
      * under ${user.home}/.cosplay/log folder.
      *
      * @param impl
      */
    private class Log4jWrapper(val impl: CPLog) extends CPLog :
        private val log4j = LogManager.getLogger(impl.getCategory)

        def log(nthFrame: Int, lvl: CPLogLevel, obj: Any, ex: Exception = null): Unit =
            if frameCnt % nthFrame == 0 then
                if obj.toString != CPUtils.PING_MSG then
                    lvl match
                        case CPLogLevel.TRACE => log4j.trace(obj, ex)
                        case CPLogLevel.DEBUG => log4j.debug(obj, ex)
                        case CPLogLevel.INFO => log4j.info(obj, ex)
                        case CPLogLevel.WARN => log4j.warn(obj, ex)
                        case CPLogLevel.ERROR => log4j.error(obj, ex)
                        case CPLogLevel.FATAL => log4j.fatal(obj, ex)
                impl.log(nthFrame, lvl, obj, ex)

        def getLog(category: String): CPLog = new Log4jWrapper(impl.getLog(category))

        def getCategory: String = impl.getCategory

    /**
      *
      */
    private class NativeKbReader extends Thread :
        private final val EOF = -1
        private final val TIMEOUT = -2
        private final val ESC = 27
        private val mapping = mutable.HashMap.empty[Seq[Int], CPKeyboardKey]

        @volatile var st0p = false

        // Prep keyboard map.
        for (key <- CPKeyboardKey.values)
            key.rawCodes.foreach(s => mapping += s.map(_.toInt) -> key)

        private def read(timeout: Long): Int = term.nativeKbRead(timeout)

        override def run(): Unit =
            while (!st0p)
                var key = KEY_UNKNOWN

                //noinspection ScalaUnnecessaryParentheses
                try
                    read(0) match // Blocking wait (timeout = 0).
                        case ESC => read(1) match
                            case EOF | TIMEOUT => key = KEY_ESC
                            case c1 => mapping.get(Seq(ESC, c1)) match
                                case Some(k) => key = k
                                case None => read(1) match
                                    case EOF | TIMEOUT => ()
                                    case c2 => mapping.get(Seq(ESC, c1, c2)) match
                                        case Some(k) => key = k
                                        case None => read(1) match
                                            case EOF | TIMEOUT => ()
                                            case c3 => mapping.get(Seq(ESC, c1, c2, c3)) match
                                                case Some(k) => key = k
                                                case None => read(1) match
                                                    case EOF | TIMEOUT => ()
                                                    case c4 => mapping.get(Seq(ESC, c1, c2, c3, c4)) match
                                                        case Some(k) => key = k
                                                        case None => read(1) match
                                                            case EOF | TIMEOUT => ()
                                                            case c5 => mapping.get(Seq(ESC, c1, c2, c3, c4, c5)) match
                                                                case Some(k) => key = k
                                                                case None => ()
                        case EOF | TIMEOUT => ()
                        case code => key = mapping.getOrElse(Seq(code), KEY_UNKNOWN)
                catch
                    case _: (InterruptedIOException | InterruptedException) => ()
                    case e: Exception => E(s"Keyboard read error: $e", e)

                kbMux.synchronized {
                    key.clear() // Clear potential metadata from the key.
                    kbKey = key
                }
            end while

    /**
      * Tests whether or not game engine is initialized.
      */
    def isInit: Boolean = state == State.ENG_STARTED

    /**
      * Gets the game information supplied to [[init()]] method.
      */
    def getGameInfo: CPGameInfo =
        gameInfo

    /**
      * Initialized the game engine.
      *
      * @param gameInfo Game information.
      * @param emuTerm Whether or not to use built-in terminal emulator.
      */
    def init(gameInfo: CPGameInfo, emuTerm: Boolean): Unit =
        if state == State.ENG_STARTED then E("Engine is already started.")
        if state == State.ENG_STOPPED then E("Engine is stopped and cannot be restarted.")

        // Initialize JavaFX toolkit for audio.
        try com.sun.javafx.application.PlatformImpl.startup(() => ())
        catch case e: Exception => E(s"Failed to start JavaFX.", e)

        this.gameInfo = gameInfo

        // Used by Log4J configuration for log output sub-folder.
        System.setProperty(
            "COSPLAY_GAME_NAME",
            gameInfo.name.toLowerCase.replace(' ', '_').replaceAll("\\W+", "")
        )

        val termClsName = CPUtils.sysEnv("COSPLAY_TERM_CLASSNAME") match
            case Some(cls) => cls
            case None => if emuTerm then "org.cosplay.impl.emuterm.CPEmuTerminal" else "org.cosplay.impl.jlineterm.CPJLineTerminal"

        // Create new terminal.
        try term = Class.forName(termClsName).getDeclaredConstructor(classOf[CPGameInfo]).newInstance(gameInfo).asInstanceOf[CPTerminal]
        catch case e: Exception => E(s"Failed to create the terminal for class: $termClsName", e)

        engLog = new Log4jWrapper(term.getRootLog.getLog("cosplay"))

        // Set terminal window title.
        updateTitle(term.getDim)

        if term.isNative then
            // Keyboard reader thread.
            kbReader = NativeKbReader()
            kbReader.start()

        state = State.ENG_STARTED

        // For some reasons, GUI-based log (some Swing activity) is **required** to avoid
        // strange behavior of the native terminal in shaders. Specifically, fade-in
        // and fade-out shaders stutter in native terminal unless GUI-based subsystem
        // is initialized. This output is essential to avoid this behavior. It also marks
        // the log (both log4j and GUI-based logger) with the game information.
        asciiLogo()
        ackGameInfo()

    /**
      *
      */
    private def asciiLogo(): Unit =
        val verStr = s"ver: ${CPVersion.latest.semver}".padTo(13, ' ')
        val logo =
            s"""
                |_________            ______________
                |__  ____/_______________  __ \\__  /_____ _____  __
                |_  /    _  __ \\_  ___/_  /_/ /_  /_  __ `/_  / / /
                |/ /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
                |\\____/  \\____//____/ /_/     /_/  \\__,_/ _\\__, /
                |$verStr                            /____/
                |
                |         ${CPVersion.tagline}
                |           ${CPVersion.copyright}
                |              ALl rights reserved.
                |""".stripMargin
        engLog.info(s"\n$logo")

    /**
      *
      */
    private def ackGameInfo(): Unit =
        val tbl = new CPAsciiTable()
        tbl += ("Game ID", gameInfo.id)
        tbl += ("Game name", gameInfo.name)
        tbl += ("Game URL", gameInfo.gameUrl)
        tbl += ("Description", gameInfo.descrShort)
        tbl += ("Version", gameInfo.semVer)
        tbl += ("Developer", gameInfo.devName)
        tbl += ("Release Date", gameInfo.relDate)
        tbl += ("Release Notes", gameInfo.relUrl)
        tbl += ("License", gameInfo.license)
        tbl += ("License URL", gameInfo.licenseUrl)
        tbl += ("Game-pad", gameInfo.requireGamePad)
        tbl += ("24bit Colors", gameInfo.require24bitColor)
        tbl += ("1x1 Fonts", gameInfo.require1x1Font)
        tbl += ("1x2 Fonts", gameInfo.require1x2Font)
        tbl += ("Initial Size", gameInfo.initDim)
        tbl += ("Minimum Size", gameInfo.minDim match
            case Some(dim) => dim.toString
            case None => "n/a"
        )
        tbl.info(engLog, Option("Game initialized:"))

    /**
      *
      * @param dim
      */
    private def updateTitle(dim: CPDim): Unit =
        term.setTitle(s"CosPlay - ${gameInfo.name} v${gameInfo.semVer}, ${dim.width}x${dim.height}")

    /**
      *
      */
    private def checkState(): Unit =
        if state != State.ENG_STARTED then E(s"Engine is not started.")

    /**
      * Gets root log for the game engine.
      */
    def rootLog(): CPLog = engLog

    /**
      * Shows or hides the built-in FPS overlay in the right top corner. Can
      * also be turned on or off by pressing `Ctrl-q` in the game.
      *
      * @param show Show/hide flag.
      */
    def showFpsOverlay(show: Boolean): Unit = isShowFps = show

    /**
      * Opens GUI-based log window by bringing it upfront.
      * Can also be open by pressing `Ctrl-l` in the game.
      */
    def openLog(): Unit =
        rootLog().info(CPUtils.PING_MSG)

    /**
      * Disposes the game engine. This method must be called upon exit from the [[startGame()]] method.
      */
    def dispose(): Unit =
        checkState()
        if kbReader != null then
            kbReader.st0p = true
            kbReader.interrupt()
            kbReader.join()
        if term != null then term.dispose()
        state = State.ENG_STOPPED
        if savedEx != null then savedEx.printStackTrace()

    /**
      * Pauses the game.
      */
    def pauseGame(): Unit =
        checkState()
        pauseMux.synchronized {
            pause = true
        }
        CPGuiLog.onGamePauseResume(pause)
        engLog.info("Game paused.")

    /**
      * Tests whether or not the game is paused.
      */
    def isGamePaused: Boolean =
        checkState()
        pauseMux.synchronized(pause)

    /**
      * Debug steps through the game on frame at a time. Note that the game must be paused.
      *
      * @param kbKey Optional keyboard key event to emulate for this debug step.
      *     If provided, the real keyboard event, if any, will be ignored.
      */
    def debugStep(kbKey: Option[CPKeyboardKey]): Unit =
        checkState()
        pauseMux.synchronized {
            if !pause then E(s"Game must be paused for debugging.")
            dbgStopAtFrameCnt = Option(frameCnt + 1)
            dbgKbKey = kbKey
            pause = false
            pauseMux.notifyAll()
        }

    /**
      * Resumes the game.
      */
    def resumeGame(): Unit =
        checkState()
        pauseMux.synchronized {
            pause = false
            pauseMux.notifyAll()
        }
        CPGuiLog.onGamePauseResume(pause)
        engLog.info("Game resumed.")

    /**
      * Starts the game.
      *
      * @param startSceneId ID of the scene to start with.
      * @param scs Set of scene comprising the game. Note that scenes can be dynamically
      *     [[CPSceneObjectContext.addScene() added]] or [[CPSceneObjectContext.deleteScene() removed]].
      */
    def startGame(startSceneId: String, scs: CPScene*): Unit =
        checkState()
        scs.foreach(scenes.add)
        engLog.info("Game started.")
        gameLoop(scenes.grab(startSceneId))

    /**
      * Starts the game.
      *
      * @param startSceneId ID of the scene to start with.
      * @param scs Set of scene comprising the game. Note that scenes can be dynamically
      *     [[CPSceneObjectContext.addScene() added]] or [[CPSceneObjectContext.deleteScene() removed]].
      */
    def startGame(startSceneId: String, scs: List[CPScene]): Unit = startGame(startSceneId, scs:_*)

    /**
      * Starts the game. Games start with the first scene in the list.
      *
      * @param scenes Set of scene comprising the game. Note that scenes can be dynamically
      *     [[CPSceneObjectContext.addScene() added]] or [[CPSceneObjectContext.deleteScene() removed]].
      */
    def startGame(scenes: CPScene*): Unit = startGame(scenes.head.getId, scenes:_*)

    /**
      * Starts the game. Games start with the first scene in the list.
      *
      * @param scenes Set of scene comprising the game. Note that scenes can be dynamically
      *     [[CPSceneObjectContext.addScene() added]] or [[CPSceneObjectContext.deleteScene() removed]].
      */
    def startGame(scenes: List[CPScene]): Unit = startGame(scenes:_*)

    /**
      * Exits the game. Calling this method will exit the [[startGame()]] method.
      */
    def exitGame(): Unit =
        checkState()
        playing = false
        pauseMux.synchronized {
            if pause then
                pause = false
                pauseMux.notifyAll()
        }
        engLog.info("Game exited.")

    /**
      * Adds the rendering stats listener.
      *
      * @param f Listener to add.
      */
    def addStatsListener(f: CPRenderStatsListener): Unit =
        statsReg += f

    /**
      * Adds external input devices.
      *
      * @param in External input device to add.
      */
    def addInput(in: CPInput): Unit =
        inputReg += in

    /**
      * Removes external input devices.
      *
      * @param in External input device to remove.
      */
    def removeInput(in: CPInput): Unit =
        inputReg -= in

    /**
      * Removes the rendering stats listener.
      *
      * @param f Listener to remove.
      */
    def removeStatsListener(f: CPRenderStatsListener): Unit =
        statsReg -= f

    /**
      *
      * @param canv
      * @param stats
      * @param camRect
      */
    private def showFps(canv: CPCanvas, stats: CPRenderStats, camRect: CPRect): Unit =
        def leftPad(s: String): String = s"${' '.toString * (12 - s.length())}$s"

        def rightPad(s: String): String = s.padTo(7, ' ')

        val usrTime = rightPad(s"${stats.userTimeNs / 1_000_000}ms")
        val sysTime = rightPad(s"${stats.sysTimeNs / 1_000_000}ms")

        import CPStyledString.styleStr

        val w = camRect.width.min(canv.dim.width)
        val fg = C_BLACK
        val bg = C_WHITE
        val sep = styleStr("-----------+-------", bg.darker(0.3), bg)

        Seq(
            styleStr(s"${leftPad("CURR FPS|")}${rightPad(stats.fps.toString)}", fg, bg),
            styleStr(s"${leftPad("AVG FPS|")}${rightPad(stats.avgFps.toString)}", fg, bg),
            styleStr(s"${leftPad("AVG 1% FPS|")}${rightPad(stats.low1PctFps.toString)}", fg, bg),
            sep,
            styleStr(s"${leftPad("USR TIME|")}$usrTime", fg, bg),
            styleStr(s"${leftPad("SYS TIME|")}$sysTime", fg, bg),
            sep,
            styleStr(s"${leftPad("OBJ COUNT|")}${rightPad(stats.objCount.toString)}", fg, bg),
            styleStr(s"${leftPad("VIS COUNT|")}${rightPad(stats.visObjCount.toString)}", fg, bg)
        )
        .zipWithIndex
        .foreach((line, idx) => canv.drawPixels(camRect.x + w - line.length, camRect.y + idx, Int.MaxValue, line))

    /**
      * Adjusts given camera frame, if necessary.
      *
      * @param scr Scene's screen rectangle.
      * @param x Camera frame left top corner X-coordinate.
      * @param y Camera frame left top corner Y-coordinate.
      * @param termW Camera frame width.
      * @param termH Camera frame height.
      * @return Adjusted camera frame.
      */
    private def adjustCameraFrame(scr: CPRect, x: Int, y: Int, termW: Int, termH: Int): CPRect =
        require(scr.x == 0 && scr.y == 0)

        val frame = CPRect(x, y, termW, termH)
        if frame.contains(scr) then
            // If terminal is larger than entire scene return scene as a camera frame.
            scr
        else if scr.contains(frame) then
            // If camera frame is fully contained within the scene screen -  we are done!
            frame
        else
            var x2 = x.max(0)
            var y2 = y.max(0)

            if frame.xMax > scr.xMax then x2 -= frame.xMax - scr.xMax
            if frame.yMax > scr.yMax then y2 -= frame.yMax - scr.yMax

            x2 = x2.max(0)
            y2 = y2.max(0)

            scr.intersectWith(CPRect(x2, y2, termW, termH))

    /**
      *
      * @param startScene
      */
    private def gameLoop(startScene: CPScene): Unit =
        val startMs = System.currentTimeMillis()
        var startScMs = startMs
        var fps = 0
        var sc = startScene
        var lastKbEvt: Option[CPKeyboardEvent] = None
        var kbEvt: Option[CPKeyboardEvent] = None
        var lastTermDim = CPDim.ZERO
        val msgQ = mutable.HashMap.empty[String, mutable.Buffer[AnyRef]]
        val delayedQ = mutable.ArrayBuffer.empty[() => Unit]
        val gameCache = CPCache(delayedQ)
        val sceneCache = CPCache(delayedQ)
        val collidedBuf = mutable.ArrayBuffer.empty[CPSceneObject]
        var stopFrame = false
        var kbFocusOwner: Option[String] = None
        var camRect: CPRect = null
        var camX = 0
        var camY = 0
        var camPanX = 0f
        var camPanY = 0f
        var stats: Option[CPRenderStats] = None
        var fpsList: List[Int] = Nil
        var fpsCnt = 0
        var fpsSum = 0
        var low1FpsList: List[Int] = Nil
        var low1FpsCnt = 0
        var scLog = engLog.getLog(s"${startScene.getId}")
        var forceStatsUpdate = false
        var forceRedraw = false

        def lifecycleStart(x: CPLifecycle): Unit =
            x.getState match
                case LF_INIT =>
                    x.onStart()
                    x.onActivate()
                case LF_INACTIVE | LF_STARTED => x.onActivate()
                case LF_ACTIVE => ()
                case LF_STOPPED => assert(false)

        def lifecycleStop(x: CPLifecycle): Unit =
            try
                x.getState match
                    case LF_INIT => ()
                    case LF_ACTIVE | LF_STARTED =>
                        x.onDeactivate()
                        x.onStop()
                    case LF_INACTIVE => x.onStop()
                    case LF_STOPPED => ()
            catch case e: Exception => engLog.error(s"Failed to stop object: $x", e)

        def postQ(id: String, msgs: Seq[AnyRef]): Unit = msgQ.get(id) match
            case Some(buf) => buf ++= msgs
            case None => msgQ += id -> mutable.Buffer(msgs)

        if term.isNative && gameInfo.minDim.isDefined && term.getDim <@ gameInfo.minDim.get then
            throw E(s"Terminal window is too small (must be at least ${gameInfo.minDim.get}).")

        var lastScDim: CPDim = null
        var scr: CPScreen = null

        def logSceneSwitch(x: CPScene): Unit =
            val dimS = x.getDim match
                case Some(d) => d.toString
                case None => "[adaptive]"
            engLog.info(s"Switching to scene: ${x.getId} $dimS")

        try
            logSceneSwitch(sc) // Initial scene.

            // Main game loop.
            while (playing)
                val termDim = term.getDim

                // If necessary, dynamically adjust scene dimension to the terminal size.
                val scDim = sc.getDim.getOrElse(termDim)
                if scr == null || lastScDim != scDim then scr = CPScreen(scDim, sc.getBgPixel)
                lastScDim = scDim

                stopFrame = false

                val frameNs = System.nanoTime()
                val frameMs = frameNs / 1_000_000

                def waitForWakeup(): Unit =
                    while (pause)
                        try pauseMux.wait()
                        catch case _: InterruptedException => ()
                    forceStatsUpdate = true

                if dbgStopAtFrameCnt.isDefined && dbgStopAtFrameCnt.get == frameCnt then
                    pauseMux.synchronized {
                        dbgStopAtFrameCnt = None
                        pause = true
                        waitForWakeup()
                    }

                // Handle pause/resume of the game.
                if pause then pauseMux.synchronized(waitForWakeup())

                // Transition the state of the scene, if necessary.
                lifecycleStart(sc)

                // Visible scene objects sorted by layer.
                val objs = sc.objects.values.toSeq.sortBy(_.getZ)

                if objs.isEmpty then E(s"Scene has no objects: ${sc.getId}")

                // Transition objects states.
                objs.foreach(lifecycleStart)

                val termW = termDim.width
                val termH = termDim.height
                val redraw = forceRedraw || scFrameCnt == 0 || lastTermDim != termDim
                forceRedraw = false
                lastTermDim = termDim

                if redraw then // Update terminal window title.
                    updateTitle(termDim)

                val cam = sc.getCamera

                if redraw then
                    // When redraw - move camera instantly instead of panning.
                    if cam.getFocusTrackId.isDefined then
                        sc.objects.get(cam.getFocusTrackId.get) match
                            case Some(obj) =>
                                // Trying to keep tracking object in the center.
                                camX = obj.getX - termW / 2
                                camY = obj.getY - termH / 2
                            case None =>
                                camX = 0
                                camY = 0
                    else
                        // If no focus tracking - move camera frame to the top-left corner.
                        camX = 0
                        camY = 0

                    // When redrawing - reset camera panning.
                    camPanX = 0
                    camPanY = 0

                kbMux.synchronized {
                    // Native ANSI reader happens in its own thread...
                    if !term.isNative then kbKey = term.kbRead().orNull
                    if dbgKbKey.isDefined then // Override keyboard events when debugging.
                        kbKey = dbgKbKey.get
                        dbgKbKey = None
                    else if inputReg.nonEmpty then
                        val ctx = new CPInputContext:
                            override def getLog: CPLog = scLog
                            override def getGameCache: CPCache = gameCache
                            override def getSceneCache: CPCache = sceneCache
                            override def getRenderStats: Option[CPRenderStats] = stats
                            override def getFrameCount: Long = frameCnt
                            override def getSceneFrameCount: Long = scFrameCnt
                            override def getStartGameMs: Long = startMs
                            override def getStartSceneMs: Long = startScMs
                            override def getFrameMs: Long = frameMs

                        inputReg.flatMap(_.poll(ctx)).headOption match
                            case Some(key) => kbKey = key
                            case None => ()

                    if kbKey == null then
                        kbEvt = None
                    else
                        // Fix the ambiguity between 'Ctrl-h' and 'Backspace' as well as 'Ctrl-i' and 'Tab'.
                        // Prefer 'Backspace' and 'Tab' in both cases meaning that 'Ctrl-h' and 'Ctrl-i'
                        // will never be available to the user...
                        if kbKey == KEY_CTRL_H then kbKey = KEY_BACKSPACE
                        else if kbKey == KEY_CTRL_I then kbKey = KEY_TAB

                        lastKbEvt match
                            case Some(lastEvt) =>
                                kbEvt = Option(CPKeyboardEvent(
                                    kbKey,
                                    lastEvt.key == kbKey,
                                    frameCnt,
                                    frameNs,
                                    lastEvt.eventFrame,
                                    lastEvt.eventNs
                                ))
                            case None =>
                                kbEvt = Option(CPKeyboardEvent(
                                    kbKey,
                                    sameAsLast = false,
                                    frameCnt,
                                    frameNs,
                                    0L,
                                    0L
                                ))

                        lastKbEvt = kbEvt
                        kbKey = null
                }

                if camRect == null then camRect = new CPRect(camX, camY, termDim).intersectWith(new CPRect(0, 0, scDim))

                class CPSceneObjectContextImpl(canv: CPCanvas) extends CPSceneObjectContext :
                    private var myId: String = _
                    private var myObj: CPSceneObject = _
                    private var myLog: CPLog = _

                    private def collide(f: CPRect => Boolean, zs: Int*): Seq[CPSceneObject] =
                        collidedBuf.clear()
                        for (obj <- objs)
                            if obj.isVisible && (zs.isEmpty || zs.contains(obj.getZ)) then
                                obj.getCollisionRect match
                                    case Some(clsRect) if f(clsRect) => collidedBuf += obj
                                    case _ => ()
                        collidedBuf.toSeq

                    def setSceneObject(obj: CPSceneObject): Unit =
                        myId = obj.getId
                        myObj = obj
                        myLog = scLog.getLog(myId)

                    def doSwitchScene(id: String, delCur: Boolean): Unit =
                        val cloId = id
                        val cloDelCur = delCur

                        delayedQ += (() => {
                            if cloDelCur then
                                scenes.remove(cloId) match
                                    case Some(s) => lifecycleStop(s)
                                    case _ => ()
                            else
                                sc.onDeactivate()
                                sc.objects.values.foreach(_.onDeactivate())
                            sc = scenes.grab(cloId)
                            scLog = engLog.getLog(s"scene:${sc.getId}")
                            scr = null
                            sceneCache.reset()
                            msgQ.clear()
                            lastKbEvt = None
                            camRect = null
                            kbFocusOwner = None
                            scFrameCnt = 0
                            stopFrame = true
                            startScMs = System.currentTimeMillis()
                            logSceneSwitch(sc)
                        })

                    override def getId: String = myId
                    override def getLog: CPLog = myLog
                    override def getOwner: CPSceneObject = myObj
                    override def getCamera: CPCamera = sc.getCamera
                    override def getCameraFrame: CPRect = camRect
                    override def getCanvas: CPCanvas = canv
                    override def getFrameCount: Long = frameCnt
                    override def getSceneFrameCount: Long = scFrameCnt
                    override def getRenderStats: Option[CPRenderStats] = stats
                    override def getStartGameMs: Long = startMs
                    override def getStartSceneMs: Long = startScMs
                    override def getGameCache: CPCache = gameCache
                    override def getSceneCache: CPCache = sceneCache
                    override def getFrameMs: Long = frameMs
                    override def getKbEvent: Option[CPKeyboardEvent] =
                        if kbFocusOwner.isEmpty || kbFocusOwner.get == myId then
                            kbEvt
                        else
                            None
                    override def sendMessage(id: String, msgs: AnyRef*): Unit =
                        val cloId = id
                        val cloMsgs = msgs
                        delayedQ += (() => postQ(cloId, cloMsgs))
                    override def receiveMessage(): Seq[AnyRef] = msgQ.get(myId) match
                        case Some(buf) =>
                            val pckt = Seq.empty ++ buf // Copy.
                            msgQ.remove(myId)
                            pckt
                        case None => Seq.empty
                    override def exitGame(): Unit =
                        stopFrame = true
                        playing = false
                    override def acquireFocus(id: String): Unit =
                        if kbFocusOwner.isDefined && kbFocusOwner.get != id then
                            scLog.trace(s"Input focus is currently held by '${kbFocusOwner.get}', switching to '$id'.")
                        val cloId = id
                        delayedQ += (() => kbFocusOwner = Option(cloId))
                    override def acquireMyFocus(): Unit = acquireFocus(myId)
                    override def getFocusOwner: Option[String] = kbFocusOwner
                    override def releaseFocus(id: String): Unit =
                        if kbFocusOwner.isDefined && kbFocusOwner.get == id then
                            delayedQ += (() => kbFocusOwner = None)
                    override def releaseMyFocus(): Unit = releaseFocus(myId)
                    override def addObject(obj: CPSceneObject): Unit =
                        val cloObj = obj
                        delayedQ += (() => sc.objects.add(cloObj))
                    override def getObject(id: String): Option[CPSceneObject] = sc.objects.get(id)
                    override def grabObject(id: String): CPSceneObject = sc.objects.grab(id)
                    override def getObjectsForTags(tags: String*): Seq[CPSceneObject] = sc.objects.getForTags(tags: _*)
                    override def addScene(newSc: CPScene, switchTo: Boolean = false, delCur: Boolean = false): Unit = 
                        delayedQ += (() =>
                            engLog.info(s"Scene added: ${newSc.getId}")
                            scenes.add(newSc)
                        )
                        if switchTo then doSwitchScene(newSc.getId, delCur)
                    override def switchScene(id: String, delCur: Boolean = false): Unit = doSwitchScene(id, delCur)
                    override def deleteScene(id: String): Unit =
                        if sc.getId == id then E(s"Cannot remove current scene: ${sc.getId}")
                        else
                            val colId = id
                            delayedQ += (() => {
                                scenes.remove(colId) match
                                    case Some(s) =>
                                        engLog.info(s"Scene deleted: ${s.getId}")
                                        lifecycleStop(s)
                                    case _ => ()
                            })
                    override def deleteObject(id: String): Unit =
                        val colId = id
                        delayedQ += (() => {
                            sc.objects.remove(colId) match
                                case Some(obj) => lifecycleStop(obj)
                                case _ => ()
                        })
                    override def collisions(zs: Int*): Seq[CPSceneObject] =
                        if myObj.getCollisionRect.isEmpty then E(s"Current object does not provide collision shape: ${myObj.getId}")
                        else
                            val myClsRect = myObj.getCollisionRect.get
                            collide(r => r.overlaps(myClsRect), zs: _*)
                    override def collisions(x: Int, y: Int, zs: Int*): Seq[CPSceneObject] =
                        collide(r => r.contains(x, y), zs: _*)
                    override def collisions(rect: CPRect, zs: Int*): Seq[CPSceneObject] =
                        collide(r => r.overlaps(rect), zs: _*)

                val ctx = new CPSceneObjectContextImpl(scr.canvas())

                var visObjCnt = 0

                // Update all objects (including invisible and outside of the frame) in the scene.
                for (obj <- objs if !stopFrame)
                    ctx.setSceneObject(obj)
                    obj.update(ctx)

                // NOTE: Update camera panning after the objects were updated BUT before object are drawn.
                if !redraw then
                    // Calculate camera tracking movement only if focus tracking ID is provided.
                    // If not redrawing and no focus tracking or tracking is disabled - leave
                    // the camera frame as is.
                    if cam.getFocusTrackId.isDefined then
                        // NOTE: camera rect >= focus rect.
                        val insets = cam.getFocusFrameInsets
                        val hor = insets.horOffset <= termW
                        val vert = insets.verOffset <= termH
                        val focusRect = new CPRect(
                            if hor then camX + insets.left else camX,
                            if vert then camY + insets.top else camY,
                            if hor then termW - insets.horOffset else termW,
                            if vert then termH - insets.verOffset else termH
                        )
                        sc.objects.get(cam.getFocusTrackId.get) match
                            case Some(obj) =>
                                val objRect = obj.getRect
                                camPanX =
                                    if focusRect.containsHor(objRect) then
                                        if cam.isMinPanningX then 0 else camPanX
                                    else
                                        (objRect.centerX - focusRect.centerX).toFloat
                                camPanY =
                                    if focusRect.containsVer(objRect) then
                                        if cam.isMinPanningY then 0 else camPanY
                                    else
                                        (objRect.centerY - focusRect.centerY).toFloat
                            // If not redrawing and tracking object cannot be found - leave the camera frame as is.
                            case None => ()

                // Update panning.
                // NOTE: Update panning after the objects were updated BUT before object are rendered.
                if camPanX != 0 || camPanY != 0 then
                    val dx = cam.getPanningStepX * camPanX.sign
                    val dy = cam.getPanningStepY * camPanY.sign
                    val deltaX = (if dx.abs < camPanX.abs then dx else camPanX).round
                    val deltaY = (if dy.abs < camPanY.abs then dy else camPanY).round
                    camX += deltaX
                    camY += deltaY
                    camPanX -= deltaX
                    camPanY -= deltaY

                // NOTE: Create camera frame after the objects were updated BUT before object are drawn.
                camRect = adjustCameraFrame(scr.getRect, camX, camY, termW, termH)

                // Repaint visible and in-frame objects only.
                for (obj <- objs if !stopFrame && obj.isVisible && camRect.overlaps(obj.getRect))
                    ctx.setSceneObject(obj)
                    obj.render(ctx)
                    visObjCnt += 1

                // Shader pass for all objects (including invisible and outside of the frame).
                for (obj <- objs if !stopFrame)
                    val shaders = obj.getShaders
                    if shaders.nonEmpty then
                        val objRect = obj.getRect
                        val inCamera = camRect.overlaps(objRect)
                        for (shdr <- shaders if !stopFrame)
                            ctx.setSceneObject(obj)
                            shdr.render(ctx, objRect, inCamera)

                val startSysNs = System.nanoTime()

                // Perform all delayed operations for the next frame.
                for (f <- delayedQ if !stopFrame) f()
                // Clear delayed operations.
                delayedQ.clear()

                // Rendering...
                if !stopFrame then
                    // Update FPS screen overlay, if necessary.
                    if stats.nonEmpty && isShowFps then showFps(ctx.getCanvas, stats.get, camRect)
                    term.render(scr, camRect, redraw)
                    scr.clear()

                val endFrameNs = System.nanoTime()
                val durNs = endFrameNs - frameNs
                val sysNs = endFrameNs - startSysNs
                val usrNs = durNs - sysNs
                fps = if durNs > 0 then (1_000_000_000f / durNs).round else 9999

                val statsStart = System.nanoTime()
                fpsList :+= fps
                fpsSum += fps
                if fpsCnt > FPS_LIST_SIZE then
                    val oldestFps = fpsList.head
                    fpsSum -= oldestFps
                    fpsList = fpsList.tail
                    if low1FpsList.contains(oldestFps) then
                        low1FpsList = fpsList.sorted.take(FPS_1PCT_LIST_SIZE)
                        low1FpsCnt = FPS_1PCT_LIST_SIZE
                    end if
                else
                    fpsCnt += 1

                if low1FpsCnt < FPS_1PCT_LIST_SIZE then // Initial low1% list accumulation.
                    low1FpsList :+= fps
                    low1FpsCnt += 1
                else if fpsCnt <= FPS_LIST_SIZE then
                    low1FpsList = fpsList.sorted.take(FPS_1PCT_LIST_SIZE)
                    low1FpsCnt = low1FpsList.length

                // Built-in support for Ctrl-q and Ctrl-l.
                if kbEvt.isDefined then
                    if kbEvt.get.key == KEY_CTRL_Q then isShowFps = !isShowFps
                    else if kbEvt.get.key == KEY_CTRL_R then forceRedraw = true
                    else if kbEvt.get.key == KEY_CTRL_L then engLog.info(CPUtils.PING_MSG)
                end if

                if scFrameCnt == 0 || frameCnt % 3 == 0 || forceStatsUpdate then // Reduce flicker.
                    forceStatsUpdate = false

                    // Average FPS.
                    val avgFps = fpsSum / fpsCnt
                    val avgLow1Fps = low1FpsList.sum / low1FpsCnt

                    stats = Option(CPRenderStats(frameCnt, scFrameCnt, fps, avgFps, avgLow1Fps, usrNs, sysNs, objs.length, visObjCnt, kbEvt))

                    // Update GUI log if it is used.
                    if engLog.impl.isInstanceOf[CPGuiLog] then CPGuiLog.updateStats(stats.get)

                // Notify stats listeners, if any.
                stats match
                    case Some(s) => statsReg.foreach(_.onStats(s))
                    case None => ()

                val statsNs = System.nanoTime() - statsStart
                val waitMs = (FRAME_NANOS - (durNs + statsNs)) / 1_000_000

                // Enforce actual FPS.
                if waitMs > 0 then
                    try Thread.sleep(waitMs)
                    catch case _: InterruptedException => ()

                frameCnt += 1
                if !stopFrame then scFrameCnt += 1
            end while
        catch case e: Throwable => savedEx = e
        finally
            engLog.info("Game stopped.")

            // Stop all the scenes and their scene objects.
            for (sc <- scenes.values)
                // Stop scene object first.
                sc.objects.values.foreach(lifecycleStop)
                // Stop the scene itself.
                lifecycleStop(sc)
