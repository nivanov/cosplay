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
import org.cosplay.impl.emuterm.*
import org.cosplay.impl.guilog.*
import org.cosplay.impl.jlineterm.*
import org.cosplay.CPColor.*
import org.cosplay.CPLifecycle.State.*
import org.cosplay.CPKeyboardKey.*
import org.cosplay.impl.*

import java.io.*
import scala.annotation.targetName
import scala.collection.mutable
import com.sun.javafx.application.*
import scala.util.*

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
private[cosplay] def raise[T](msg: String, cause: Option[Throwable] = None): T = throw new CPException(msg, cause)

/**
  * Global syntax sugar for throwing [[CPException]].
  *
  * @param msg Exception message.
  */
private[cosplay] def raise[T](msg: String): T = throw new CPException(msg, None)

/**
  * A shortcut for:
  * {{{
  *     if !cond then throw new CPException(errMsg)
  * }}}
  *
  * @param cond Condition to check.
  * @param errMsg Optional error message to throw if condition is `false`. By default, 'Requirement failed."
  *     message will be used.
  */
@targetName("exclamationRightAngle")
def !>(cond: Boolean, errMsg: => String = "Requirement failed."): Unit = if !cond then raise(errMsg)

/** Sugar for typed `None` value. */
def none[T]: Option[T] = None

/** Sugar for typed `Nil` value. */
def nil[T]: List[T] = Nil

/**
  * Shortcut for `Option[x]` as `x.?`.
  */
extension[T](ref: T)
    /** Shortcut for `Option[x]` as `x.?`. */
    @targetName("asAnOption")
    def `?`: Option[T] = Option(ref)

/** Sugar for `scala.util.Try`. */
extension[T] (t: Try[T])
    def getOr(pf: PartialFunction[Throwable, T]): T = t.recover(pf).get
    def onFailure(pf: PartialFunction[Throwable, T]): Unit = t.recover(pf).get
    def getOrRethrow(): T = t.recover(e => throw e).get

/** Single element sequence sugar. */
extension[T](t: T)
    /** Shortcut for `Seq(t)` as `t.seq` */
    def seq: Seq[T] = Seq(t)
    /** Shortcut for `Set(t)` as `t.set` */
    def set: Set[T] = Set(t)

extension(s: String)
    def isVis(ch: Char): Boolean = ch == '\n' || ch == '\t' || !ch.isControl
    /** Length of the string taking into account printable characters only. */
    inline def visLength: Int = s.count(isVis)
    /** String with all non-printable characters removed. */
    inline def visOnly: String = s.filter(isVis)

extension[R, T](opt: Option[T])
    def onSome(f: T => R): Unit = opt match
        case Some(s) => f(s)
        case None => ()
    def onNone(f: () => R): Unit = opt match
        case Some(_) => ()
        case None => f()
    @targetName("optEqual")
    def ===(t: T): Boolean = opt match
        case Some(a) => a == t
        case None => false
    def mapOr(f: T => R, dflt: => R): R = opt.flatMap(f(_).?).getOrElse(dflt)
    def getOrThrow(err: => String | Exception): T = opt match
        case Some(t) => t
        case None => err match
            case s: String => raise(s)
            case e: Exception => throw e

extension(d: Int)
    // To bytes...
    def kb: Long = d * 1024
    def mb: Long = d * 1024 * 1024
    def gb: Long = d * 1024 * 1024 * 1024

    // To milliseconds...
    def ms: Long = d
    def secs: Long = d * 1000
    def mins: Long = d * 1000 * 60
    def hours: Long = d * 1000 * 60 * 60
    def days: Long = d * 1000 * 60 * 60 * 24
    def weeks: Long = d * 1000 * 60 * 60 * 24 * 7

/**
  * CosPlay game engine.
  *
  * Game engine is used mostly for game lifecycle like starting and stopping as well as debugging, logging,
  * and managing global utility and miscellaneous features.
  *
  * CosPlay games follow relatively straightforward game organization:
  *  - Initialize game engine by calling [[CPEngine.init()]] or [[CPEngine.initEff()]] method.
  *  - Start the game loop by calling [[CPEngine.startGame()]] or [[CPEngine.startGameEff()]] method.
  *  - Dispose the game engine calling [[CPEngine.dispose()]] or [[CPEngine.disposeEff()]] method.
  *
  * Note that these methods have two versions:
  *  - One that throws [[CPException]] exception in case of any errors for the classic exception-based
  *    error handling.
  *  - Another with `Eff` suffix in the name that returns [[scala.util.Try]]-monad allowing for composable effect-based error handling.
  *
  * You can use either approach but you can't mix them up. Effect-based approach, however, forces a more
  * rigorous error handling requiring to handle errors at each call.
  *
  * CosPlay game organization using classic exception-based error handling:
  * {{{
  * import org.cosplay.*
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
  * Here's the same game structure using effect-based error handling (calling `sys.exit()` with an
  * appropriate exit code at each step):
  * {{{
  * import org.cosplay.*
  *
  * object Game:
  *    def main(args: Array[String]): Unit =
  *        // Initialize the engine.
  *        CPEngine.initEff(
  *             CPGameInfo(name = "My Game"),
  *             System.console() == null || args.contains("emuterm")
  *        ).recover(_ => sys.exit(1)).get
  *
  *        // Create game scenes & their scene objects.
  *        val sc1 = new CPScene(...)
  *        val sc2 = new CPScene(...)
  *
  *        // Start the game & wait for exit.
  *        CPEngine.startGameEff(sc1, sc2).recover(_ => sys.exit(2)).get
  *
  *        // Dispose game engine.
  *        CPEngine.disposeEff().fold(_ => sys.exit(3), _ => sys.exit(0))
  * }}}
  * Notes:
  *  - Game start in a standard Scala way. It is recommended to use `main(...)` function for better
  *    compatibility.
  *  - Create [[CPGameInfo]] object that describes the game and its properties.
  *  - Initialize game engine by calling [[CPEngine.init()]]/[[CPEngine.initEff()]] method passing it game descriptor and terminal emulation
  *    flag.
  *  - Create all necessary scenes, scene objects and assets. You can organize these objects in any desirable way - CosPlay
  *    does not impose any restrictions or limitation on how it is should be done. Note also that scene and scene objects
  *    can be added, removed or modified later throughout the game lifecycle.
  *  - Once you have all initially required scenes constructed - you can start the game by calling one of the
  *    [[CPEngine.startGame()]]/[[CPEngine.startGameEff()]] methods.
  *  - Make sure to call [[CPEngine.dispose()]]/[[CPEngine.disposeEff()]] method upon exit from
  *    [[CPEngine.startGame()]]/[[CPEngine.startGameEff()]] method.
  *
  * ### Extensions and Shortcuts
  * Throughout the CosPlay code, exampled and built-in games there are several extensions and syntactic shortcuts
  * used to simplify frequently used code idioms:
  *
  * | Shortcut | Replaced By |
  * | -------- | ---- |
  * | `x.?` | `Option(x)` |
  * | `x.seq` | `Seq(x)` |
  * | `val x = nil[Int]` | `val x: List[Int] = Nil` |
  * | `val x = none[Int]` | `val x: Option[Int] = None` |
  *
  * These extensions are introduced in the global scope and can be used by any code that is using CosPlay.
  *
  * ### System Properties
  * CosPlay game engine supports the following system properties that control various aspects of its
  * operation. Note that these properties must be set before method [[CPEngine.init()]]/[[CPEngine.initEff()]] is called:
  *
  * | System Property | Value Type | Description  |
  * | ----------------| ---------- | ------------ |
  * | `COSPLAY_GAME_ID` | `String` | Internal unique game ID. |
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
  *  - 'CTRL+Q' - toggles in-game FPS overlay (see [[CPEngine.enableCtrlFps()]] method)
  *  - 'CTRL+L' - opens GUI-based loc viewer & debugger (see [[CPEngine.enableCtrlLog()]] method)
  *  - 'F12' - saves current frame screenshot as *.xp image to the current working folder.
  *
  * @example See all examples under `org.cosplay.examples` package. Each example has a complete demonstration of
  *     working with game engine including initialization and game start.
  * @note See developer guide at [[https://cosplayengine.com]]
  */
//noinspection ScalaWeakerAccess
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

    private var ctrlLogEnabled: Boolean = true
    private var ctrlFpsEnabled: Boolean = true
    private val scenes = new CPContainer[CPScene]()
    private var term: CPTerminal = _
    private var pause = false
    private var frameCnt = 0L // Overall game fame count.
    private var scFrameCnt = 0L // Current scene fame count (each scene starts at zero).
    private var dbgStopAtFrameCnt = none[Long]
    private var dbgKbKey = none[CPKeyboardKey]
    private var gameInfo: CPGameInfo = _
    private var isShowFps = false
    private var kbReader: NativeKbReader = _
    private var kbKey: CPKeyboardKey = _
    private val kbMux = AnyRef
    private val pauseMux = AnyRef
    private var engLog: CPLog = BufferedLog("").getLog("root")
    private val statsReg = mutable.HashSet.empty[CPRenderStatsListener]
    private val inputReg = mutable.HashSet.empty[CPInput]
    private val extDelayedQ = mutable.ArrayBuffer.empty[(String, Seq[AnyRef])]
    private var stats = none[CPRenderStats]
    private var homeRoot = none[String]
    private var tempRoot = none[String]

    @volatile private var state = State.ENG_INIT
    @volatile private var playing = true

    !>(FPS_1PCT_LIST_SIZE > 0)

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

    private object BufferedLog:
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

    private class NativeKbReader extends Thread:
        private val EOF = -1
        private val TIMEOUT = -2
        private val ESC = 27
        private val mapping = mutable.HashMap.empty[Seq[Int], CPKeyboardKey]
        private val isKbLog = CPUtils.sysEnvBool("COSPLAY_KB_LOG")

        @volatile var st0p = false

        // Prep keyboard map.
        for key <- CPKeyboardKey.values do key.rawCodes.foreach(s => mapping += s.map(_.toInt) -> key)

        private def read(timeout: Long): Int = term.nativeKbRead(timeout)
        private inline def logKb(key: CPKeyboardKey, chs: Int*): Unit =
            if isKbLog then rootLog().debug(s"Native kb reader: '${key.id}' [${chs.mkString(",")}]")
        override def run(): Unit =
            while !st0p do
                var key = KEY_UNKNOWN

                try
                    read(0) match // Blocking wait (timeout = 0).
                        case ESC => read(1) match
                            case EOF | TIMEOUT =>
                                key = KEY_ESC
                                logKb(key, ESC)
                            case c1 => mapping.get(Seq(ESC, c1)) match
                                case Some(k) =>
                                    key = k
                                    logKb(key, ESC, c1)
                                case None => read(1) match
                                    case EOF | TIMEOUT => ()
                                    case c2 => mapping.get(Seq(ESC, c1, c2)) match
                                        case Some(k) =>
                                            key = k
                                            logKb(key, ESC, c1, c2)
                                        case None => read(1) match
                                            case EOF | TIMEOUT => ()
                                            case c3 => mapping.get(Seq(ESC, c1, c2, c3)) match
                                                case Some(k) =>
                                                    key = k
                                                    logKb(key, ESC, c1, c2, c3)
                                                case None => read(1) match
                                                    case EOF | TIMEOUT => ()
                                                    case c4 => mapping.get(Seq(ESC, c1, c2, c3, c4)) match
                                                        case Some(k) =>
                                                            key = k
                                                            logKb(key, ESC, c1, c2, c3, c4)
                                                        case None => read(1) match
                                                            case EOF | TIMEOUT => ()
                                                            case c5 => mapping.get(Seq(ESC, c1, c2, c3, c4, c5)) match
                                                                case Some(k) =>
                                                                    key = k
                                                                    logKb(key, ESC, c1, c2, c3, c4, c5)
                                                                case None => ()
                        case EOF | TIMEOUT => ()
                        case code =>
                            key = mapping.getOrElse(Seq(code), KEY_UNKNOWN)
                            logKb(key, code)
                catch
                    case _: InterruptedIOException => ()
                    case _: InterruptedException => ()
                    case e: Exception => raise(s"Keyboard read error: $e", e.?)

                kbMux.synchronized {
                    key.clear() // Clear potential metadata from the key.
                    kbKey = key
                }
            end while

    /**
      * Returns whether or not game engine is initialized.
      */
    def isInit: Boolean = state == State.ENG_STARTED

    /**
      * Gets the game information supplied to [[init()]] method.
      */
    def getGameInfo: CPGameInfo =
        gameInfo

    /**
      * Creates file with given relative path in the engine's special, system-specific, game-specific root location. The actual
      * absolute path of the returned file is OS-dependent and shouldn't be relied on or used.
      *
      * @param path Relative path of the file. Path may include sub-directories and should use Unix
      *     style '/' for path separator.
      */
    def homeFile(path: String): Try[File] = Try {
        checkState()
        newFile(s"${homeRoot.get}/data/$path")
    }

    /**
      * Creates file in the engine's special, system-specific, temporary file location. The actual
      * absolute path of the returned file is OS-dependent and shouldn't be relied on or used. Returned
      * file wil be automatically deleted upon exiting the game engine.
      */
    def tempFile(): Try[File] = Try {
        checkState()
        val tempFile = newFile(s"${tempRoot.get}/${CPRand.guid}")
        tempFile.deleteOnExit()
        tempFile
    }

    private def mkDir(dir: File): File =
        if !dir.exists() && !dir.mkdirs() then raise(s"Failed to create folder: ${dir.getAbsolutePath}")
        dir

    private def newFile(path: String): File =
        val file = new File(path)
        val parent = file.getParentFile
        mkDir(parent)
        file

    /**
      * Initializes the game engine. Effect-based version of [[init()]] method allowing end-user to use pattern
      * matching for error handling. This method does not throw any exceptions.
      *
      * @param gameInfo Game information.
      * @param emuTerm Whether or not to use built-in terminal emulator. If not provided, the default
      *     value will be result of this expression: {{{System.console() == null}}}
      * @return Try-monad.
      * @see [[init()]]
      * @see [[startGameEff()]]
      * @see [[disposeEff()]]
      */
    def initEff(gameInfo: CPGameInfo, emuTerm: Boolean = System.console() == null): Try[Unit] =
        Try(init(gameInfo, emuTerm))

    /**
      * Initializes the game engine.
      *
      * @param gameInfo Game information.
      * @param emuTerm Whether or not to use built-in terminal emulator. If not provided, the default
      *     value will be result of this expression: {{{System.console() == null}}}
      * @see [[initEff()]]
      * @throws CPException Thrown in case of any errors.
      */
    def init(gameInfo: CPGameInfo, emuTerm: Boolean = System.console() == null): Unit =
        !>(state != State.ENG_STARTED, "Engine is already initialized.")
        !>(state != State.ENG_STOPPED, "Engine is stopped and cannot be restarted.")
        // Initialize JavaFX toolkit for audio.
        !>(
            Try {
                PlatformImpl.setImplicitExit(false)
                PlatformImpl.startup(() => {})
            }.isSuccess,
            s"Failed to start JavaFX - make sure your JDK/OS is compatible with JavaFX (https://openjfx.io)."
        )

        this.gameInfo = gameInfo
        // Internal game ID.
        val gameId = gameInfo.name.toLowerCase.replace(' ', '_').replaceAll("\\W+", "")
        // Used by Log4J configuration for log output sub-folder.
        System.setProperty("COSPLAY_GAME_ID", gameId)

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
        catch case e: Exception => throw if e.getCause != null then e.getCause else e

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

    private def ackGameInfo(): Unit =
        val tbl = new CPAsciiTable()
        tbl += ("Game ID", gameInfo.id)
        tbl += ("Game name", gameInfo.name)
        tbl += ("Version", gameInfo.semVer)
        tbl += ("Initial Size", gameInfo.initDim)
        tbl += ("Minimum Size", gameInfo.minDim.mapOr(_.toString, "n/a"))
        tbl.info(engLog, "Game initialized:".?)

    private def updateTitle(dim: CPDim): Unit =
        assert(dim != null, "Dimension is null.")
        term.setTitle(s"${gameInfo.name} v${gameInfo.semVer}, ${dim.w}x${dim.h}")
    private def checkState(): Unit =
        !>(state == State.ENG_STARTED, s"Engine is not started.")

    /**
      * Gets root log for the game engine.
      *
      * NOTE: unlike most other methods in the game engine, you can use this method before engine is initialized.
      * In such case the log entries will be buffered until the engine is initialized.
      */
    def rootLog(): CPLog = engLog

    /**
      * Enables or disables opening the GUI-based debugger log by pressing 'Ctrl-l'. Production games may want to
      * disable that capability. Default value is `true`.
      *
      * @param f Whether to enable or disable opening the GUI-based debugger log by pressing 'Ctrl-l'.
      */
    def enableCtrlLog(f: Boolean): Unit = ctrlLogEnabled = f

    /**
      * Checks whether or not opening the GUI-based debugger log by pressing 'Ctrl-l' is enabled.
      * Default value is `true`.
      */
    def isCtrlLog: Boolean = ctrlLogEnabled

    /**
      * Enables or disables opening built-in FPS window by pressing 'Ctrl-q'. Production games may want to
      * disable that capability. Default value is `true`.
      *
      * @param f Whether to enable or disable opening built-in FPS window by pressing 'Ctrl-q'.
      */
    def enableCtrlFps(f: Boolean): Unit = ctrlFpsEnabled = f

    /**
      * Checks whether or not opening built-in FPS window pressing 'Ctrl-q' is enabled.
      * Default value is `true`.
      */
    def isCtrlFps:Boolean = ctrlFpsEnabled

    /**
      * Shows or hides the built-in FPS overlay in the right top corner. Can
      * also be turned on or off by pressing `Ctrl-q` in the game.
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
      *
      * @param show Show/hide built-in FPS flag.
      * @see [[enableCtrlFps()]]
      * @see [[isCtrlFps]]
      */
    def showFpsOverlay(show: Boolean): Unit =
        checkState()
        isShowFps = show

    /**
      * Opens GUI-based log window by bringing it upfront.
      * Can also be open by pressing `Ctrl-l` in the game.
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
      *
      * @see [[enableCtrlLog()]]
      * @see [[isCtrlLog]]
      */
    def openLog(): Unit =
        rootLog().info(CPUtils.PING_MSG)

    private def stopInternals(): Unit =
        if kbReader != null then
            kbReader.st0p = true
            kbReader.interrupt()
            kbReader.join()
        if term != null then term.dispose()

    /**
      * Disposes the game engine. This method must be called upon exit from the [[startGame()]] method.
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
      *
      * @see [[disposeEff()]]
      */
    def dispose(): Unit =
        checkState()
        stopInternals()
        state = State.ENG_STOPPED

    /**
      * Disposes the game engine. This method must be called upon exit from the [[startGameEff()]] method.
      * Engine must be [[initEff() initialized]] before this call otherwise exception is thrown.
      * Effect-based version of [[dispose()]] method allowing end-user to use pattern matching for error handling.
      * This method does not throw any exceptions.
      *
      * @return Try-monad.
      * @see [[dispose()]]
      * @see [[startGame()]]
      * @see [[initEff()]]
      */
    def disposeEff(): Try[Unit] =
        Try {
            checkState()
            stopInternals()
        } match
            case s: Success[Unit] => state = State.ENG_STOPPED; s
            case f => f

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
        engLog.trace("Game paused.")

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
            !>(pause, s"Game must be paused for debugging.")
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
        engLog.trace("Game resumed.")

    /**
      * Starts the game. Games start with the first scene in the list.
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
      *
      * @param scs Non-empty set of scene comprising the game. Note that scenes can be dynamically
      *     [[CPSceneObjectContext.addScene() added]] or [[CPSceneObjectContext.deleteScene() removed]].
      * @see [[startGameEff()]]
      * @throws CPException Thrown in case of any errors.
      */
    def startGame(scs: CPScene*): Unit =
        !>(scs.nonEmpty, "At least one scene must be provided.")
        checkState()
        scs.foreach(scenes.add)
        engLog.trace("Game started.")
        gameLoop(scenes(scs.head.getId))

    /**
      * Starts the game. Games start with the given start scene.
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
      *
      * @param startSc ID of the scene to start the game with.
      * @param scs Non-empty set of scene comprising the game. Note that scenes can be dynamically
      * [[CPSceneObjectContext.addScene() added]] or [[CPSceneObjectContext.deleteScene() removed]].
      * @see [[startGameEff()]]
      * @throws CPException Thrown in case of any errors.
      */
    def startGame(startSc: String, scs: CPScene*): Unit =
        !>(scs.nonEmpty, "At least one scene must be provided.")
        checkState()
        scs.foreach(scenes.add)
        engLog.trace("Game started.")
        gameLoop(scenes(startSc))

    /**
      * This is effect-based version of [[startGame()]] method allowing end-user to use pattern matching
      * for error handling. Starts the game. Games start with the first scene in the list.
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
      * This method does not throw any exceptions.
      *
      * @param scs Non-empty set of scene comprising the game. Note that scenes can be dynamically
      * [[CPSceneObjectContext.addScene() added]] or [[CPSceneObjectContext.deleteScene() removed]].
      * @see [[startGame()]]
      * @see [[initEff()]]
      * @see [[disposeEff()]]
      */
    def startGameEff(scs: CPScene*): Try[Unit] = Try(startGame(scs: _*))

    /**
      * This is effect-based version of [[startGame()]] method allowing end-user to use pattern matching
      * for error handling. Starts the game. Games start with the given start scene ID.
      * Engine must be [[init() initialized]] before this call otherwise exception is thrown.
      * This method does not throw any exceptions.
      *
      * @param startSc ID of the scene to start the game with.
      * @param scs Non-empty set of scene comprising the game. Note that scenes can be dynamically
      * [[CPSceneObjectContext.addScene() added]] or [[CPSceneObjectContext.deleteScene() removed]].
      * @see [[startGame()]]
      * @see [[initEff()]]
      * @see [[disposeEff()]]
      */
    def startGameEff(startSc: String, scs: CPScene*): Try[Unit] = Try(startGame(startSc, scs: _*))

    /**
      * Exits the game. Calling this method will cause [[startGame()]] or [[startGameEff()]] method to exit on the next frame. Note that
      * this method can only be called from a thread different from the one used to call [[startGame()]] or [[startGameEff()]] method. If
      * the game wasn't started yet, this method is a no-op. Engine must be [[init() initialized]] before this
      * call otherwise exception is thrown.
      */
    def exitGame(): Unit =
        checkState()
        playing = false
        pauseMux.synchronized {
            if pause then
                pause = false
                pauseMux.notifyAll()
        }
        engLog.trace("Game exited.")

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
      * Sends direct message(s) to the scene object of the currently playing scene. Note that to
      * exchange data between scenes you should use [[CPSceneObjectContext.getGameCache game cache]].
      * Sent messages will be available to the recipient scene objects starting with the next frame.
      * Messages will be stored until they are retrieved or the scene is changed.
      *
      * Although this method looks identical to [[CPSceneObjectContext.sendMessage()]] method, it
      * allows to send messages to the scene objects from outside of the game loop.
      *
      * @param id Scene object ID from the current scene.
      * @param msgs Messages to send.
      * @see [[CPSceneObjectContext.receiveMessage()]]
      */
    def sendMessage(id: String, msgs: AnyRef*): Unit = extDelayedQ.synchronized(
        extDelayedQ += (id -> msgs)
    )

    private def gameLoop(startScene: CPScene): Unit =
        val startMs = System.currentTimeMillis()
        var startScMs = startMs
        var fps = 0
        var sc = startScene
        var lastKbEvt = none[CPKeyboardEvent]
        var kbEvt = none[CPKeyboardEvent]
        var lastTermDim = CPDim.ZERO
        val msgQ = mutable.HashMap.empty[String, mutable.Buffer[AnyRef]]
        val delayedQ0 = mutable.ArrayBuffer.empty[() => Unit]
        val delayedQ1 = mutable.ArrayBuffer.empty[() => Unit]
        val laterRuns = mutable.ArrayBuffer.empty[LaterRun]
        val nextFrameRuns = mutable.ArrayBuffer.empty[CPSceneObjectContext => Unit]
        val gameCache = CPCache(delayedQ1)
        val sceneCache = CPCache(delayedQ1)
        val collidedBuf = mutable.ArrayBuffer.empty[CPSceneObject]
        var stopFrame = false
        var kbFocusOwner = none[String]
        var camRect: CPRect = null
        var camX = 0
        var camY = 0
        var camPanX = 0f
        var camPanY = 0f
        var fpsList = nil[Int]
        var fpsCnt = 0
        var fpsSum = 0
        var low1FpsList = nil[Int]
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
            case None => msgQ += id -> mutable.Buffer(msgs: _*)

        if term.isNative && gameInfo.minDim.isDefined && term.getDim <@ gameInfo.minDim.get then
            raise(s"Terminal window is too small (must be at least ${gameInfo.minDim.get}).")

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
            tbl.trace(engLog, s"Switching to scene '${x.getId}' $dimS with scene objects:".?)

        try
            logSceneSwitch(sc) // Initial scene.

            // Main game loop.
            while playing do
                val termDim = term.getDim

                // If necessary, dynamically adjust scene dimension to the terminal size.
                val scDim = sc.getDim.getOrElse(termDim)
                if scr == null || lastScDim != scDim then scr = CPScreen(scDim, sc.getBgPixel)
                lastScDim = scDim

                stopFrame = false

                val frameNs = System.nanoTime()
                val frameMs = System.currentTimeMillis()

                def waitForWakeup(): Unit =
                    while pause do
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
                !>(objs.nonEmpty, s"Scene '${sc.getId}' has no objects.")
                // Transition objects states.
                objs.foreach(lifecycleStart)

                val termW = termDim.w
                val termH = termDim.h
                val redraw = scFrameCnt == 0 || lastTermDim != termDim
                val cam = sc.getCamera

                lastTermDim = termDim

                if redraw then
                    // Update terminal window title.
                    updateTitle(termDim)
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

                        // Complete later runs right away.
                        laterRuns.foreach(_.f(this))
                        laterRuns.clear()

                        delayedQ1 += (() =>
                            if cloDelCur then
                                scenes.remove(sc.getId) match
                                    case Some(s) => lifecycleStop(s)
                                    case _ => ()
                            else
                                sc.onDeactivateX()
                                sc.objects.values.foreach(_.onDeactivateX())
                            sc = scenes(cloId)
                            scLog = engLog.getLog(s"scene:${sc.getId}")
                            scr = null
                            sceneCache.reset()
                            msgQ.clear()
                            lastKbEvt = None
                            camRect = null
                            kbFocusOwner = None
                            scFrameCnt = 0
                            stopFrame = true
                            nextFrameRuns.clear()
                            startScMs = System.currentTimeMillis()
                            logSceneSwitch(sc)
                        )

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
                    override def consumeKbEvent(): Unit = kbEvt = None
                    override def sendMessage(id: String, msgs: AnyRef*): Unit =
                        val cloId = id
                        val cloMsgs = msgs
                        delayedQ0 += (() => postQ(cloId, cloMsgs))
                    override def receiveMessage(): Seq[AnyRef] = msgQ.get(myId) match
                        case Some(buf) =>
                            val msgs = Seq.empty ++ buf // Copy.
                            msgQ.remove(myId)
                            msgs
                        case None => Seq.empty
                    override def exitGame(): Unit =
                        stopFrame = true
                        playing = false
                    override def acquireFocus(id: String): Unit =
                        if kbFocusOwner.isDefined && kbFocusOwner.get != id then
                            scLog.trace(s"Input focus is currently held by '${kbFocusOwner.get}', switching to '$id'.")
                        val cloId = id
                        delayedQ0 += (() => kbFocusOwner = cloId.?)
                    override def getFocusOwner: Option[String] = kbFocusOwner
                    override def releaseFocus(id: String): Unit =
                        if kbFocusOwner.isDefined && kbFocusOwner.get == id then
                            delayedQ0 += (() => kbFocusOwner = None)
                    override def releaseMyFocus(): Unit = releaseFocus(myId)
                    override def addObject(obj: CPSceneObject, replace: Boolean = false): Unit =
                        if replace then deleteObject(obj.getId)
                        val cloObj = obj
                        delayedQ0 += (() =>
                            sc.objects.add(cloObj)
                            engLog.trace(s"Scene object added to '${sc.getId}' scene: ${cloObj.toExtStr}")
                        )
                    override def getObject(id: String): Option[CPSceneObject] = sc.objects.get(id)
                    override def getObjects: Iterable[CPSceneObject] = sc.objects.values
                    override def grabObject(id: String): CPSceneObject = sc.objects(id)
                    override def getObjectsForTags(tags: Set[String]): Seq[CPSceneObject] = sc.objects.getForTags(tags)
                    override def countObjectsForTags(tags: String*): Int = sc.objects.countForTags(tags.toSet)
                    override def addScene(newSc: CPScene, switchTo: Boolean = false, delCur: Boolean = false, replace: Boolean = false): Unit =
                        val newScId = newSc.getId
                        if newScId == sc.getId then raise(s"Cannot add a new scene with the same ID as the current one: $newScId")
                        if replace then deleteScene(newScId) // Adds delayed action.
                        val loNewSc = newSc
                        delayedQ1 += (() =>
                            // NOTE: scene lifecycle transitions when it becomes active.
                            scenes.add(loNewSc)
                            val verb = if replace then "replaced" else "added"
                            engLog.trace(s"Scene $verb: $newScId")
                        )
                        if switchTo then doSwitchScene(newScId, delCur)
                    override def switchScene(id: String, delCur: Boolean = false): Unit = doSwitchScene(id, delCur)
                    override def deleteScene(id: String): Unit =
                        if sc.getId == id then raise(s"Cannot delete current scene: ${sc.getId}")
                        else
                            val cloId = id
                            delayedQ1 += (() =>
                                scenes.remove(cloId) match
                                    case Some(s) =>
                                        engLog.trace(s"Scene deleted: ${s.getId}")
                                        lifecycleStop(s)
                                    case _ =>
                                        engLog.warn(s"Ignored an attempt to delete unknown scene: $cloId")
                            )
                    override def deleteObject(id: String): Unit =
                        val cloId = id
                        delayedQ0 += (() =>
                            sc.objects.remove(cloId) match
                                case Some(obj) =>
                                    if kbFocusOwner.isDefined && kbFocusOwner.get == cloId then kbFocusOwner = None
                                    engLog.trace(s"Scene object deleted from '${sc.getId}' scene: ${obj.toExtStr}")
                                    lifecycleStop(obj)
                                case _ =>
                                    engLog.warn(s"Ignored an attempt to delete unknown object from '${sc.getId}' scene: $cloId")
                        )
                    override def collisions(zs: Int*): Seq[CPSceneObject] =
                        if myObj.getCollisionRect.isEmpty then raise(s"Current object does not provide collision shape: ${myObj.getId}")
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

                // Process scene monitors.
                for obj <- objs if !stopFrame do
                    ctx.setSceneObject(obj)
                    obj.monitor(ctx)

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
                for f <- delayedQ0 if !stopFrame do f()
                delayedQ0.clear()
                for f <- delayedQ1 if !stopFrame do f()
                delayedQ1.clear()

                // Built-in support for 'Ctrl+q', 'Ctrl+l' and 'F12'.
                if kbEvt.isDefined then
                    if ctrlFpsEnabled && kbEvt.get.key == KEY_CTRL_Q then
                        isShowFps = !isShowFps
                    else if kbEvt.get.key == KEY_F12 then
                        val path = s"cosplay-screenshot-${CPRand.guid6}.xp"
                        ctx.getCanvas.capture().saveRexXp(path, sc.getBgColor)
                        engLog.trace(s"Screenshot saved: $path")
                    else if ctrlLogEnabled && kbEvt.get.key == KEY_CTRL_L then
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

                // Copy messages from the external queue to the internal.
                extDelayedQ.synchronized {
                    if extDelayedQ.nonEmpty then
                        extDelayedQ.foreach((id, msgs) => delayedQ0 += (() => postQ(id, msgs)))
                        extDelayedQ.clear()
                }

                val statsNs = System.nanoTime() - statsStart
                val waitMs = (FRAME_NANOS - (durNs + statsNs)) / 1_000_000

                // Enforce actual FPS.
                if waitMs > 0 then
                    try Thread.sleep(waitMs)
                    catch case _: InterruptedException => ()

                frameCnt += 1
                if !stopFrame then scFrameCnt += 1
            end while
        finally
            engLog.trace("Game stopped.")

            // Stop all the scenes and their scene objects.
            for sc <- scenes.values do
                // Stop scene object first.
                sc.objects.values.foreach(lifecycleStop)
                // Stop the scene itself.
                lifecycleStop(sc)
