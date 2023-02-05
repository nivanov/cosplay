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
import org.apache.commons.lang3.*
import impl.emuterm.*
import impl.guilog.*
import impl.jlineterm.*
import CPColor.*
import CPLifecycle.State.*
import CPKeyboardKey.*
import impl.*

import java.io.*
import scala.annotation.targetName
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
               All rights reserved.
*/

/**
  * Global syntax sugar for throwing [[CPException]].
  *
  * @param msg Exception message.
  * @param cause Optional cause.
  */
def E[T](msg: String, cause: Throwable = null): T = throw new CPException(msg, cause)

/**
  * Shortcut for `Option[x]` as `x.?`.
  */
extension[T](ref: T)
    @targetName("asAnOption")
    def `?`: Option[T] = Option(ref)

extension[R, T](opt: Option[T])
    @targetName("optEqual")
    def ===(t: T): Boolean = opt match
        case Some(a) => a == t
        case None => false
    def mapOr(f: T => R, dflt: => R): R = opt.flatMap(f(_).?).getOrElse(dflt)
    def getOrThrow[E <: Exception](e: => E): T = opt match
        case Some(t) => t
        case None => throw e

/**
  * Base trait for typed coordinates and dimensions.
  *
  * User code can choose to use these typed coordinates, color components
  * and dimensions in methods where there could be a confusion about parameters.
  * Alternatively, user code can use named parameters to disambiguate the formal
  * parameters at the call site.
  */
sealed trait Base(val d: Int)
given scala.Conversion[Base, Int] with
    /** Converting numeric base to integer. */
    def apply(b: Base): Int = b.d
/** Typed X-coordinate. */
case class X(x: Int) extends Base(x)
/** Typed Y-coordinate. */
case class Y(y: Int) extends Base(y)
/** Typed Z-coordinate. */
case class Z(z: Int) extends Base(z)
/** Typed width. */
case class W(w: Int) extends Base(w)
/** Typed height. */
case class H(h: Int) extends Base(h)
/** Typed red RGB component. */
case class R(r: Int) extends Base(r)
/** Typed green RGB component. */
case class G(g: Int) extends Base(g)
/** Typed blue RGB component. */
case class B(b: Int) extends Base(b)
extension(d: Int)
    def x: X = X(d)
    def y: Y = Y(d)
    def z: Z = Z(d)
    def w: W = W(d)
    def h: H = H(d)
    def r: R = R(d)
    def g: G = G(d)
    def b: B = B(d)

extension[T](seq: Seq[T])
    /**
      * Random element selector using [[CPRand]] class.
      */
    def rand: T = CPRand.rand(seq)

extension(d: Int)
    // To bytes...
    def kb: Long = d * 1024
    def mb: Long = d * 1024 * 1024
    def gb: Long = d * 1024 * 1024 * 1024

    // To milliseconds...
    def msec: Long = d
    def ms: Long = d
    def secs: Long = d * 1000
    def mins: Long = d * 1000 * 60
    def hours: Long = d * 1000 * 60 * 60
    def days: Long = d * 1000 * 60 * 60 * 24
    def weeks: Long = d * 1000 * 60 * 60 * 24 * 7

/**
  * CosPlay game engine.
  *
  * Game engine is mostly an internal object, and it is only used at the beginning of the game. It provides
  * variety of utility and miscellaneous methods for games.
  *
  * Most CosPlay games follow this basic game organization:
  * {{{
  * import org.cosplay.{given, *}
  *
  * object Game:
  *    def main(args: Array[String]): Unit =
  *        // Initialize the engine.
  *        CPEngine.init(
  *             CPGameInfo(name = "My Game"),
  *             System.console() == null || args.contains("emuterm")
  *        )
  *
  *        // Create game scenes & their scene objects.
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
  *  - Once you have all scenes constructed - you can start the game by calling one of the [[CPEngine.startGame()]] methods.
  *  - Make sure to call [[CPEngine.dispose()]] method upon exit from [[CPEngine.startGame()]] method.
  *
  * ### System Properties
  * CosPlay game engine supports the following system properties that control various aspects of its
  * operation. Note that these properties must be set before method [[CPEngine.init()]] is called:
  *
  * | System Property | Value Type | Description  |
  * | ----------------| ---------- | ------------ |
  * | `COSPLAY_EMUTERM_FONT_NAME` | `String` | Applies to the built-in terminal emulator only. Specifies the font name to use. |
  * | `COSPLAY_EMUTERM_FONT_SIZE` | `Int` | Applies to the built-in terminal emulator only. Specifies the font size to use. |
  * | `COSPLAY_EMUTERM_CH_WIDTH_OFFSET` | `Int` | Applies to the built-in terminal emulator only. Specifies character width offset. Can be positive or negative. Default is zero. |
  * | `COSPLAY_EMUTERM_CH_HEIGHT_OFFSET` | `Int` | Applies to the built-in terminal emulator only. Specifies character height offset. Can be positive or negative. Default is zero. |
  * | `COSPLAY_EMUTERM_ANTIALIAS` | | Applies to the built-in terminal emulator only. If system property is present - the font rendering will use antialiasing. By default, no antialiasing is used. |
  * | `COSPLAY_FORCE_8BIT_COLOR`| `Boolean` | Forces the automatic conversion from 24-bit color to 8-bit color. Only needed when running in the native terminal that does not support 24-bit color. Default value is `false`. |
  * | `COSPLAY_TERM_CLASSNAME`| `String` | Fully qualified class name for the custom terminal emulator implementation. Class must implement [[org.cosplay.CPTerminal]] trait. |
  *
  * ### Reserved Keyboard Keys
  * There are three reserved key strokes that are used by the game engine itself and therefore NOT available
  * to the game. These keystrokes are intercepted before frame update and not propagated to the scene object
  * context:
  *  - 'CTRL+Q' - toggles in-game FPS overlay
  *  - 'CTRL+L' - opens GUI-based loc viewer & debugger
  *  - 'F12' - saves current frame screenshot as *.xp image to the current working folder.
  *
  * @example See all examples under `org.cosplay.examples` package. Each example has a complete demonstration of
  *     working with game engine including initialization and game start.
  * @note See developer guide at [[https://cosplayengine.com]]
  */
object CPEngine:
    private enum State:
        case ENG_INIT, ENG_STARTED, ENG_STOPPED

    private case class LaterRun(tsMs: Long, f: CPSceneObjectContext => Unit)

    private val FPS = 30 // Target FPS - ASCII rendering does not benefit from higher frame rate.
    private val FRAME_NANOS = 1_000_000_000 / FPS
    private val FRAME_MICROS = 1_000_000 / FPS
    private val FRAME_MILLIS = 1_000 / FPS
    private val FPS_LIST_SIZE = 500
    private val FPS_1PCT_LIST_SIZE = FPS_LIST_SIZE / 100
    private val HOME_DIR = ".cosplay"

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
    private val kbMux = AnyRef
    private val pauseMux = AnyRef
    private var engLog: CPLog = BufferedLog("").getLog("root")
    private val statsReg = mutable.HashSet.empty[CPRenderStatsListener]
    private val inputReg = mutable.HashSet.empty[CPInput]
    private var savedEx: Throwable = _
    private var stats: Option[CPRenderStats] = None
    private var homeRoot: Option[String] = None
    private var tempRoot: Option[String] = None
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
      * under ${user.home}/.cosplay/${game.name}/log folder.
      *
      * @param impl Log implementation.
      */
    private class Log4jMirrorLog(impl: CPLog) extends CPLog:
        private val log4j = LogManager.getLogger(impl.getCategory)

        init()

        private def init(): Unit =
            val buf = BufferedLog.buf
            if buf.nonEmpty then
                buf.foreach(ent => log(
                    ent.nthFrame,
                    ent.lvl,
                    ent.obj,
                    ent.cat,
                    ent.ex
                ))
                buf.clear()
        
        def log(nthFrame: Int, lvl: CPLogLevel, obj: Any, cat: String, ex: Throwable): Unit =
            if frameCnt % nthFrame == 0 then
                if obj.toString != CPUtils.PING_MSG then
                    lvl match
                        case CPLogLevel.TRACE => log4j.trace(obj, ex)
                        case CPLogLevel.DEBUG => log4j.debug(obj, ex)
                        case CPLogLevel.INFO => log4j.info(obj, ex)
                        case CPLogLevel.WARN => log4j.warn(obj, ex)
                        case CPLogLevel.ERROR => log4j.error(obj, ex)
                        case CPLogLevel.FATAL => log4j.fatal(obj, ex)
                impl.log(nthFrame, lvl, obj, cat, ex)
        def getLog(category: String): CPLog = new Log4jMirrorLog(impl.getLog(category))
        def getCategory: String = impl.getCategory

    /**
      *
      */
    object BufferedLog:
        case class BufferedLogEntry(nthFrame: Int, lvl: CPLogLevel, obj: Any, cat: String, ex: Throwable)
        val buf: mutable.ArrayBuffer[BufferedLogEntry] = mutable.ArrayBuffer.empty[BufferedLogEntry]

    import BufferedLog.*

    /**
      *
      * @param cat Log category.
      */
    private class BufferedLog(cat: String) extends CPLog:
        def getLog(category: String): CPLog = new BufferedLog(s"$cat/$category")
        def getCategory: String = cat
        def log(nthFrame: Int, lvl: CPLogLevel, obj: Any, cat: String, ex: Throwable): Unit = buf += BufferedLogEntry(nthFrame, lvl, obj, cat, ex)

    /**
      *
      */
    private class NativeKbReader extends Thread:
        private val EOF = -1
        private val TIMEOUT = -2
        private val ESC = 27
        private val mapping = mutable.HashMap.empty[Seq[Int], CPKeyboardKey]

        @volatile var st0p = false

        // Prep keyboard map.
        for key <- CPKeyboardKey.values do key.rawCodes.foreach(s => mapping += s.map(_.toInt) -> key)

        private def read(timeout: Long): Int = term.nativeKbRead(timeout)

        override def run(): Unit =
            while (!st0p)
                var key = KEY_UNKNOWN

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
                    case _: InterruptedIOException => ()
                    case _: InterruptedException => ()
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
      * Creates file with given relative path in the engine's special, system-specific, root location. The actual
      * absolute path of the returned file is OS-dependent and shouldn't be relied on or used.
      *
      * @param path Relative path of the file. Path may include sub-directories and should use Unix
      *     style '/' for path separator.
      */
    def homeFile(path: String): File =
        checkState()
        newFile(s"${homeRoot.get}/data/$path")

    /**
      * Creates file in the engine's special, system-specific, temporary file location. The actual
      * absolute path of the returned file is OS-dependent and shouldn't be relied on or used. Returned
      * file wil be automatically deleted upon exiting the game engine.
      */
    def tempFile(): File =
        checkState()
        val tempFile = newFile(s"${tempRoot.get}/${CPRand.guid}")
        tempFile.deleteOnExit()
        tempFile

    private def mkDir(dir: File): File =
        if !dir.exists() && !dir.mkdirs() then throw E(s"Failed to create folder: ${dir.getAbsolutePath}")
        dir

    /**
      *
      * @param path
      */
    private def newFile(path: String): File =
        val file = new File(path)
        val parent = file.getParentFile
        mkDir(parent)
        file

    /**
      * Initializes the game engine.
      *
      * @param gameInfo Game information.
      * @param emuTerm Whether or not to use built-in terminal emulator. If not provided, the default
      *     value will be result of this expression: {{{System.console() == null}}}
      */
    def init(gameInfo: CPGameInfo, emuTerm: Boolean = System.console() == null): Unit =
        if state == State.ENG_STARTED then E("Engine is already initialized.")
        if state == State.ENG_STOPPED then E("Engine is stopped and cannot be restarted.")

        // Initialize JavaFX toolkit for audio.
        try com.sun.javafx.application.PlatformImpl.startup(() => ())
        catch case e: Exception => E(s"Failed to start JavaFX - make sure your JDK/OS is compatible with JavaFX (https://openjfx.io).", e)

        this.gameInfo = gameInfo
        // Internal game ID.
        val gameId = gameInfo.name.toLowerCase.replace(' ', '_').replaceAll("\\W+", "")

        // Used by Log4J configuration for log output sub-folder.
        System.setProperty("COSPLAY_GAME_NAME", gameId)

        homeRoot = s"${SystemUtils.getUserHome}/$HOME_DIR/$gameId".?
        tempRoot = s"${homeRoot.get}/temp".?

        // Make sure folders exist and temp folder is cleared up.
        mkDir(new File(homeRoot.get))
        val tempDir = mkDir(new File(tempRoot.get))
        tempDir.listFiles().foreach(_.delete())

        val termClsName = CPUtils.sysEnv("COSPLAY_TERM_CLASSNAME") match
            case Some(cls) => cls
            case None => if emuTerm then "org.cosplay.impl.emuterm.CPEmuTerminal" else "org.cosplay.impl.jlineterm.CPJLineTerminal"

        // Create new terminal.
        try term = Class.forName(termClsName).getDeclaredConstructor(classOf[CPGameInfo]).newInstance(gameInfo).asInstanceOf[CPTerminal]
        catch case e: Exception => E(s"Failed to create the terminal for class: $termClsName", e)

        // Set terminal window title.
        updateTitle(term.getDim)

        if term.isNative then
            // Keyboard reader thread.
            kbReader = NativeKbReader()
            kbReader.start()

        state = State.ENG_STARTED

        engLog = new Log4jMirrorLog(term.getRootLog.getLog("cosplay"))

        // For some reasons, GUI-based log (some Swing activity) is **required** to avoid
        // strange behavior of the native terminal in shaders. Specifically, fade-in
        // and fade-out shaders stutter in native terminal unless GUI-based subsystem
        // is initialized. This output is essential to avoid this behavior. It also marks
        // the log (both log4j and GUI-based logger) with the game information.
        asciiLogo()
        ackGameInfo()
        CPUtils.startPing(gameInfo)

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
                |              All rights reserved.
                |""".stripMargin
        engLog.info(s"\n$logo")

    /**
      *
      */
    private def ackGameInfo(): Unit =
        val tbl = new CPAsciiTable()
        tbl += ("Game ID", gameInfo.id)
        tbl += ("Game name", gameInfo.name)
        tbl += ("Version", gameInfo.semVer)
        tbl += ("Initial Size", gameInfo.initDim)
        tbl += ("Minimum Size", gameInfo.minDim.mapOr(_.toString, "n/a"))
        tbl.info(engLog, "Game initialized:".?)

    /**
      *
      * @param dim
      */
    private def updateTitle(dim: CPDim): Unit =
        assert(dim != null, "Dimension is null.")
        term.setTitle(s"CosPlay - ${gameInfo.name} v${gameInfo.semVer}, ${dim.w}x${dim.h}")

    /**
      *
      */
    private def checkState(): Unit =
        if state != State.ENG_STARTED then E(s"Engine is not started.")

    /**
      * Gets root log for the game engine.
      *
      * NOTE: unlike most other methods in the game engine, you can use this method before engine is initialized.
      * In such case the log entries will be buffered until the engine is initialized.
      */
    def rootLog(): CPLog = engLog

    /**
      * Shows or hides the built-in FPS overlay in the right top corner. Can
      * also be turned on or off by pressing `Ctrl-q` in the game.
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
      *
      * @param show Show/hide flag.
      */
    def showFpsOverlay(show: Boolean): Unit =
        checkState()
        isShowFps = show

    /**
      * Opens GUI-based log window by bringing it upfront.
      * Can also be open by pressing `Ctrl-l` in the game.
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
      */
    //noinspection ScalaWeakerAccess
    def openLog(): Unit =
        rootLog().info(CPUtils.PING_MSG)

    /**
      * Disposes the game engine. This method must be called upon exit from the [[startGame()]] method.
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
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
      * Shortcut for the following two calls:
      * {{{
      *     pauseGame()
      *     openLog()
      * }}}
      * This is a convenient call to programmatically start the debugging session.
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
      */
    def startDebug(): Unit =
        pauseGame()
        openLog()

    /**
      * Pauses the game.
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
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
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
      */
    def isGamePaused: Boolean =
        checkState()
        pauseMux.synchronized(pause)

    /**
      * Debug steps through the game on frame at a time. Note that the game must be paused.
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
      *
      * @param kbKey Optional keyboard key event to emulate for this debug step.
      *     If provided, the real keyboard event, if any, will be ignored.
      */
    def debugStep(kbKey: Option[CPKeyboardKey]): Unit =
        checkState()
        pauseMux.synchronized {
            if !pause then E(s"Game must be paused for debugging.")
            dbgStopAtFrameCnt = (frameCnt + 1).?
            dbgKbKey = kbKey
            pause = false
            pauseMux.notifyAll()
        }

    /**
      * Resumes the game.
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
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
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
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
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
      *
      * @param startSceneId ID of the scene to start with.
      * @param scs Set of scene comprising the game. Note that scenes can be dynamically
      *     [[CPSceneObjectContext.addScene() added]] or [[CPSceneObjectContext.deleteScene() removed]].
      */
    def startGame(startSceneId: String, scs: List[CPScene]): Unit = startGame(startSceneId, scs:_*)

    /**
      * Starts the game. Games start with the first scene in the list.
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
      *
      * @param scenes Set of scene comprising the game. Note that scenes can be dynamically
      *     [[CPSceneObjectContext.addScene() added]] or [[CPSceneObjectContext.deleteScene() removed]].
      */
    def startGame(scenes: CPScene*): Unit = startGame(scenes.head.getId, scenes:_*)

    /**
      * Starts the game. Games start with the first scene in the list.
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
      *
      * @param scenes Set of scene comprising the game. Note that scenes can be dynamically
      *     [[CPSceneObjectContext.addScene() added]] or [[CPSceneObjectContext.deleteScene() removed]].
      */
    def startGame(scenes: List[CPScene]): Unit = startGame(scenes:_*)

    /**
      * Exits the game. Calling this method will exit the [[startGame()]] method.
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
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
      * Gets current rendering statistics, if available.
      *
      * @see [[CPSceneObjectContext.getRenderStats]]
      */
    def getRenderStats: Option[CPRenderStats] = stats

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

        val w = camRect.w.min(canv.dim.w)
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
        val laterRuns = mutable.ArrayBuffer.empty[LaterRun]
        val nextFrameRuns = mutable.ArrayBuffer.empty[CPSceneObjectContext => Unit]
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
        var fpsList: List[Int] = Nil
        var fpsCnt = 0
        var fpsSum = 0
        var low1FpsList: List[Int] = Nil
        var low1FpsCnt = 0
        var scLog = engLog.getLog(s"${startScene.getId}")
        var forceStatsUpdate = false

        def lifecycleStart(x: CPLifecycle): Unit =
            x.getState match
                case LF_INIT =>
                    x.onStartX()
                    x.onActivateX()
                case LF_INACTIVE | LF_STARTED => x.onActivateX()
                case LF_ACTIVE => ()
                case LF_STOPPED => assert(false)

        def lifecycleStop(x: CPLifecycle): Unit =
            try
                x.getState match
                    case LF_INIT => ()
                    case LF_ACTIVE | LF_STARTED =>
                        x.onDeactivateX()
                        x.onStopX()
                    case LF_INACTIVE => x.onStopX()
                    case LF_STOPPED => ()
            catch case e: Exception => engLog.error(s"Failed to stop object: $x", e)

        def postQ(id: String, msgs: Seq[AnyRef]): Unit = msgQ.get(id) match
            case Some(b) => b ++= msgs
            case None => msgQ += id -> mutable.Buffer(msgs)

        if term.isNative && gameInfo.minDim.isDefined && term.getDim <@ gameInfo.minDim.get then
            throw E(s"Terminal window is too small (must be at least ${gameInfo.minDim.get}).")

        var lastScDim: CPDim = null
        var scr: CPScreen = null

        def logSceneSwitch(x: CPScene): Unit =
            val dimS = x.getDim.mapOr(_.toString, "[adaptive]")
            val tbl = CPAsciiTable("ID", "Tags", "Init Pos", "Z-index", "Visible", "Dim")
            x.objects.foreach(scObj => {
                tbl += (
                    scObj.getId,
                    scObj.getTags.mkString(","),
                    s"(${scObj.getX},${scObj.getY})",
                    scObj.getZ,
                    scObj.isVisible,
                    scObj.getDim
                )
            })
            tbl.info(engLog, s"Switching to scene '${x.getId}' $dimS with scene objects:".?)

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
                val frameMs = System.currentTimeMillis()

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
                pauseMux.synchronized {
                    if pause then waitForWakeup()
                }

                // Transition the state of the scene, if necessary.
                lifecycleStart(sc)

                // Visible scene objects sorted by layer.
                val objs = sc.objects.values.toSeq.sortBy(_.getZ)

                if objs.isEmpty then E(s"Scene '${sc.getId}' has no objects.")

                // Transition objects states.
                objs.foreach(lifecycleStart)

                val termW = termDim.w
                val termH = termDim.h
                val redraw = scFrameCnt == 0 || lastTermDim != termDim
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
                        else if kbKey == KEY_CTRL_M then kbKey = KEY_ENTER

                        lastKbEvt match
                            case Some(lastEvt) =>
                                kbEvt = CPKeyboardEvent(
                                    kbKey,
                                    lastEvt.key == kbKey,
                                    frameCnt,
                                    frameMs,
                                    lastEvt.eventFrame,
                                    lastEvt.eventMs
                                ).?
                            case None =>
                                kbEvt = CPKeyboardEvent(
                                    kbKey,
                                    sameAsLast = false,
                                    frameCnt,
                                    frameMs,
                                    0L,
                                    0L
                                ).?

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
                        for obj <- objs do
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
                                scenes.remove(sc.getId) match
                                    case Some(s) => lifecycleStop(s)
                                    case _ => ()
                            else
                                sc.onDeactivateX()
                                sc.objects.values.foreach(_.onDeactivateX())
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
                            laterRuns.clear()
                            nextFrameRuns.clear()
                            startScMs = System.currentTimeMillis()
                            logSceneSwitch(sc)
                        })

                    override def getId: String = myId
                    override def getLog: CPLog = myLog
                    override def getOwner: CPSceneObject = myObj
                    override def getCamera: CPCamera = sc.getCamera
                    override def isVisible: Boolean = myObj.isVisible
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
                    override def runLater(delayMs: Long, f: CPSceneObjectContext => Unit): Unit = laterRuns += LaterRun(frameMs + delayMs, f)
                    override def runNextFrame(f: CPSceneObjectContext => Unit): Unit = nextFrameRuns += f
                    override def getKbEvent: Option[CPKeyboardEvent] = if kbFocusOwner.isEmpty || kbFocusOwner.get == myId then kbEvt else None
                    override def sendMessage(id: String, msgs: AnyRef*): Unit =
                        val cloId = id
                        val cloMsgs = msgs
                        delayedQ += (() => postQ(cloId, cloMsgs))
                    override def receiveMessage(): Seq[AnyRef] = msgQ.get(myId) match
                        case Some(b) =>
                            val pckt = Seq.empty ++ b // Copy.
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
                        delayedQ += (() => kbFocusOwner = cloId.?)
                    override def getFocusOwner: Option[String] = kbFocusOwner
                    override def releaseFocus(id: String): Unit =
                        if kbFocusOwner.isDefined && kbFocusOwner.get == id then
                            delayedQ += (() => kbFocusOwner = None)
                    override def releaseMyFocus(): Unit = releaseFocus(myId)
                    override def addObject(obj: CPSceneObject): Unit =
                        val cloObj = obj
                        delayedQ += (() => {
                            sc.objects.add(cloObj)
                            engLog.info(s"Scene object added to '${sc.getId}' scene: ${cloObj.toExtStr}")
                        })
                    override def getObject(id: String): Option[CPSceneObject] = sc.objects.get(id)
                    override def grabObject(id: String): CPSceneObject = sc.objects.grab(id)
                    override def getObjectsForTags(tags: Seq[String]): Seq[CPSceneObject] = sc.objects.getForTags(tags)
                    override def countObjectsForTags(tags: Seq[String]): Int = sc.objects.countForTags(tags)
                    override def addScene(newSc: CPScene, switchTo: Boolean = false, delCur: Boolean = false): Unit = 
                        delayedQ += (() => {
                            engLog.info(s"Scene added: ${newSc.getId}")
                            scenes.add(newSc)
                        })
                        if switchTo then doSwitchScene(newSc.getId, delCur)
                    override def switchScene(id: String, delCur: Boolean = false): Unit = doSwitchScene(id, delCur)
                    override def deleteScene(id: String): Unit =
                        if sc.getId == id then E(s"Cannot remove current scene: ${sc.getId}")
                        else
                            val cloId = id
                            delayedQ += (() => {
                                scenes.remove(cloId) match
                                    case Some(s) =>
                                        engLog.info(s"Scene deleted: ${s.getId}")
                                        lifecycleStop(s)
                                    case _ =>
                                        engLog.warn(s"Ignored an attempt to delete unknown scene: $cloId")
                            })
                    override def deleteObject(id: String): Unit =
                        val cloId = id
                        delayedQ += (() => {
                            sc.objects.remove(cloId) match
                                case Some(obj) =>
                                    if kbFocusOwner.isDefined && kbFocusOwner.get == cloId then kbFocusOwner = None
                                    engLog.info(s"Scene object deleted from '${sc.getId}' scene: ${obj.toExtStr}")
                                    lifecycleStop(obj)
                                case _ =>
                                    engLog.warn(s"Ignored an attempt to delete unknown object from '${sc.getId}' scene: $cloId")
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

                val ctx = new CPSceneObjectContextImpl(scr.newCanvas())

                // Handle later runs.
                val toRun = laterRuns.filter(_.tsMs <= frameMs)
                if toRun.nonEmpty then
                    toRun.foreach(_.f(ctx))
                    laterRuns.filterInPlace(_.tsMs > frameMs)

                // Handle next frame runs.
                nextFrameRuns.foreach(_(ctx))
                nextFrameRuns.clear()

                // Update all objects (including invisible and outside the frame) in the scene.
                for obj <- objs if !stopFrame do
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

                var visObjCnt = 0

                // Repaint visible and in-frame objects only.
                for obj <- objs if !stopFrame && obj.isVisible && camRect.overlaps(obj.getRect) do
                    ctx.setSceneObject(obj)
                    obj.render(ctx)
                    visObjCnt += 1

                // Shader pass for all objects (including invisible and outside the frame).
                // First, process shaders for all visible objects, then for all invisible objects.
                for set <- Seq(objs.filter(_.isVisible), objs.filter(!_.isVisible)) if !stopFrame do
                    for obj <- set if !stopFrame do
                        val shaders = obj.getShaders
                        if shaders.nonEmpty then
                            val objRect = obj.getRect
                            val inCamera = camRect.overlaps(objRect)
                            for shdr <- shaders if !stopFrame do
                                ctx.setSceneObject(obj)
                                shdr.render(ctx, objRect, inCamera)

                val startSysNs = System.nanoTime()

                // Perform all delayed operations for the next frame.
                for f <- delayedQ if !stopFrame do f()
                // Clear delayed operations.
                delayedQ.clear()

                // Built-in support for 'CTRL+Q', 'CTRL+L' and 'F12'.
                if kbEvt.isDefined then
                    if kbEvt.get.key == KEY_CTRL_Q then
                        isShowFps = !isShowFps
                    else if kbEvt.get.key == KEY_F12 then
                        val path = s"cosplay-screenshot-${CPRand.guid6}.xp"
                        ctx.getCanvas.capture().saveRexXp(path, sc.getBgColor)
                        engLog.info(s"Screenshot saved: $path")
                    else if kbEvt.get.key == KEY_CTRL_L then
                        engLog.info(CPUtils.PING_MSG)
                end if

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

                if scFrameCnt == 0 || frameCnt % 3 == 0 || forceStatsUpdate then // Reduce flicker.
                    forceStatsUpdate = false

                    // Average FPS.
                    val avgFps = fpsSum / fpsCnt
                    val avgLow1Fps = low1FpsList.sum / low1FpsCnt

                    stats = CPRenderStats(frameCnt, scFrameCnt, fps, avgFps, avgLow1Fps, usrNs, sysNs, objs.length, visObjCnt, kbEvt).?

                    // Update GUI log.
                    CPGuiLog.updateStats(stats.get)

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
            for sc <- scenes.values do
                // Stop scene object first.
                sc.objects.values.foreach(lifecycleStop)
                // Stop the scene itself.
                lifecycleStop(sc)
