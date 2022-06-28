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
  * Log levels.
  */
enum CPLogLevel:
    case TRACE, DEBUG, INFO, WARN, ERROR, FATAL

/**
  * Game logging interface.
  *
  * As CosPlay games are terminal-based they require a different approach to logging since CosPlay cannot
  * use the standard terminal output (it will conflict the game rendering). All the log that goes through this
  * interface always ends up in two places:
  *  - Built-in GUI-based log viewer that can be opened in any game by pressing `Ctrl-l` at any time.
  *  - In `*.txt` log files under `${USER_HOME}/.cosplay/log/xxx` directory, where `xxx` is the [[CPGameInfo.name name]]
  *    of the game.
  *
  * The instance of this logger is available to any scene object via [[CPSceneObjectContext.getLog]]. You can also
  * get a root logger at any time outside the frame update pass via [[CPEngine.rootLog()]] method. Note that
  * additionally to the standard familiar logging APIs this interface supports log throttling that's
  * important in games since most of the game logic gets "touched" up to 30 times a second and log throttling is
  * essential for not overflowing logging.
  *
  * Underneath the implementation is using latest [[https://logging.apache.org/log4j/2.x/ Log4j2]] library. You can
  * supply your own `log4j2.xml` file on the classpath of your game. Here's the default Log4j2 configuration:
  * {{{
  * <Configuration status="INFO" strict="true">
  *     <Appenders>
  *         <RollingFile
  *             name="file"
  *             fileName="${sys:user.home}/.cosplay/log/${sys:COSPLAY_GAME_NAME}/log.txt"
  *             filePattern="${sys:user.home}/.cosplay/log/${sys:COSPLAY_GAME_NAME}/$${date:yyyy-MM}/log-%d{MM-dd-yyyy}-%i.gz">
  *             <PatternLayout>
  *                 <Pattern>%d %p %c{1.} [%t] %m%n</Pattern>
  *             </PatternLayout>
  *             <Policies>
  *                 <TimeBasedTriggeringPolicy />
  *                 <SizeBasedTriggeringPolicy size="250 MB"/>
  *             </Policies>
  *         </RollingFile>
  *     </Appenders>
  *     <Loggers>
  *         <Root level="trace">
  *             <AppenderRef ref="file"/>
  *         </Root>
  *     </Loggers>
  * </Configuration>
  * }}}
  *
  * @see [[CPSceneObjectContext.getLog]]
  * @see [[CPEngine.rootLog()]]
  */
trait CPLog:
    import CPLogLevel.*

    private val flags = mutable.HashMap[CPLogLevel, Boolean](CPLogLevel.values.map(_ -> true).toSeq:_*)
    private var throttle = 1

    /**
      *
      * @param log
      */
    protected def inheritFrom(log: CPLog): Unit =
        for lvl <- CPLogLevel.values do if log.isEnabled(lvl) then enable(lvl) else disable(lvl)
        throttle = log.throttle

    /**
      * Gets current throttle value.
      *
      * Throttle value `N` simply means that actual logging will only happen for each `Nth` frame. Logging for all
      * other frames will be ignored. For example, setting throttling to `10` means that logging will happen
      * only every `10th` frame.
      */
    def getThrottle: Int = throttle

    /**
      * Sets the throttling value.
      *
      * Throttle value `N` simply means that actual logging will only happen for each `Nth` frame. Logging for all
      * other frames will be ignored. For example, setting throttling to `10` means that logging will happen
      * only every `10th` frame.
      *
      * @param nthFrame Throttle value.
      */
    def setThrottle(nthFrame: Int): Unit = throttle = nthFrame

    /**
      * Enables given log level for logging.
      *
      * @param lvls Log levels to enable.
      */
    def enable(lvls: CPLogLevel*): Unit = lvls.foreach(f => flags.put(f, true))

    /**
      * Disables given log level for logging.
      *
      * @param lvls Log levels to disable.
      */
    def disable(lvls: CPLogLevel*): Unit = lvls.foreach(f => flags.put(f, false))

    /**
      * Tests whether given log level is enabled.
      *
      * @param lvl Log level to check.
      */
    def isEnabled(lvl: CPLogLevel): Boolean = flags(lvl)

    /**
      * Logs given message with throttling. This method will use this log's category.
      *
      * @param nthFrame Throttle value. Throttle value `N` simply means that actual logging will only
      *     happen for each `Nth` frame. Logging for all other frames will be ignored.
      * @param lvl Log level.
      * @param obj Object to log.
      * @param ex Optional exception to log. Default value is `null`.
      */
    def log(nthFrame: Int, lvl: CPLogLevel, obj: Any, ex: Exception = null): Unit = log(nthFrame, lvl, obj, getCategory, ex)

    /**
      * Logs given message with throttling and explicit category.
      *
      * @param nthFrame Throttle value. Throttle value `N` simply means that actual logging will only
      *     happen for each `Nth` frame. Logging for all other frames will be ignored.
      * @param lvl Log level.
      * @param obj Object to log.
      * @param cat Explicit log category.
      * @param ex Exception to log. Can be `null`.
      */
    def log(nthFrame: Int, lvl: CPLogLevel, obj: Any, cat: String, ex: Exception): Unit

    /**
      * Gets a new logger for given category. New logger will inherit log levels and throttle value.
      *
      * @param category Category for new logger.
      */
    def getLog(category: String): CPLog

    /**
      * Gets category for this logger.
      */
    def getCategory: String

    /**
      * Logs rendering performance snapshot, if available from the [[CPEngine.getRenderStats game engine]].
      *
      * When debugging a performance issue with the game, it is often useful to periodically log
      * game engine performance metrics like actual FPS, LOW 1%, etc. to analyze them later in log files in correlation
      * with other game activity. This is a convenient method to quickly log a snapshot of the current game
      * engine performance numbers.
      */
    def snapshot(): Unit =
        CPEngine.getRenderStats match
            case Some(stats) =>
                val tbl = CPAsciiTable("Frm#", "ScFrm#", "FPS", "AvgFPS", "Low1%FPS", "UsrTime", "SysTime", "Obj#", "VisObj#")
                tbl += (
                    stats.frameCount,
                    stats.sceneFrameCount,
                    stats.fps,
                    stats.avgFps,
                    stats.low1PctFps,
                    s"${stats.userTimeNs / 1_000_000}ms",
                    s"${stats.sysTimeNs / 1_000_000}ms",
                    stats.objCount,
                    stats.visObjCount
                )
                tbl.trace(this, Option("Performance snapshot:"))

            case None => ()

    /**
      * Logs object with [[CPLogLevel.TRACE]] level.
      *
      * @param obj Object to log.
      * @param ex Optional exception to log. Default value is `null`.
      */
    def trace(obj: Any, ex: Exception = null): Unit = log(throttle, TRACE, obj, ex)

    /**
      * Logs object with [[CPLogLevel.TRACE]] level and throttling.
      *
      * @param nthFrame Throttle value. Throttle value `N` simply means that actual logging will only
      *     happen for each `Nth` frame. Logging for all other frames will be ignored.
      * @param obj Object to log.
      * @param ex Optional exception to log. Default value is `null`.
      */
    def tracex(nthFrame: Int, obj: Any, ex: Exception = null): Unit = log(nthFrame, TRACE, obj, ex)

    /**
      * Logs object with [[CPLogLevel.DEBUG]] level.
      *
      * @param obj Object to log.
      * @param ex Optional exception to log. Default value is `null`.
      */
    def debug(obj: Any, ex: Exception = null): Unit = log(throttle, DEBUG, obj, ex)

    /**
      * Logs object with [[CPLogLevel.DEBUG]] level and throttling.
      *
      * @param nthFrame Throttle value. Throttle value `N` simply means that actual logging will only
      *     happen for each `Nth` frame. Logging for all other frames will be ignored.
      * @param obj Object to log.
      * @param ex Optional exception to log. Default value is `null`.
      */
    def debugx(nthFrame: Int, obj: Any, ex: Exception = null): Unit = log(nthFrame, DEBUG, obj, ex)

    /**
      * Logs object with [[CPLogLevel.INFO]] level.
      *
      * @param obj Object to log.
      * @param ex Optional exception to log. Default value is `null`.
      */
    def info(obj: Any, ex: Exception = null): Unit = log(throttle, INFO, obj, ex)

    /**
      * Logs object with [[CPLogLevel.INFO]] level and throttling.
      *
      * @param nthFrame Throttle value. Throttle value `N` simply means that actual logging will only
      *     happen for each `Nth` frame. Logging for all other frames will be ignored.
      * @param obj Object to log.
      * @param ex Optional exception to log. Default value is `null`.
      */
    def infox(nthFrame: Int, obj: Any, ex: Exception = null): Unit = log(nthFrame, INFO, obj, ex)

    /**
      * Logs object with [[CPLogLevel.WARN]] level.
      *
      * @param obj Object to log.
      * @param ex Optional exception to log. Default value is `null`.
      */
    def warn(obj: Any, ex: Exception = null): Unit = log(throttle, WARN, obj, ex)

    /**
      * Logs object with [[CPLogLevel.WARN]] level and throttling.
      *
      * @param nthFrame Throttle value. Throttle value `N` simply means that actual logging will only
      *     happen for each `Nth` frame. Logging for all other frames will be ignored.
      * @param obj Object to log.
      * @param ex Optional exception to log. Default value is `null`.
      */
    def warnx(nthFrame: Int, obj: Any, ex: Exception = null): Unit = log(nthFrame, WARN, obj, ex)

    /**
      * Logs object with [[CPLogLevel.ERROR]] level.
      *
      * @param obj Object to log.
      * @param ex Optional exception to log. Default value is `null`.
      */
    def error(obj: Any, ex: Exception = null): Unit = log(throttle, ERROR, obj, ex)

    /**
      * Logs object with [[CPLogLevel.ERROR]] level and throttling.
      *
      * @param nthFrame Throttle value. Throttle value `N` simply means that actual logging will only
      *     happen for each `Nth` frame. Logging for all other frames will be ignored.
      * @param obj Object to log.
      * @param ex Optional exception to log. Default value is `null`.
      */
    def errorx(nthFrame: Int, obj: Any, ex: Exception = null): Unit = log(nthFrame, ERROR, obj, ex)

    /**
      * Logs object with [[CPLogLevel.FATAL]] level.
      *
      * @param obj Object to log.
      * @param ex Optional exception to log. Default value is `null`.
      */
    def fatal(obj: Any, ex: Exception = null): Unit = log(throttle, FATAL, obj, ex)

    /**
      * Logs object with [[CPLogLevel.FATAL]] level and throttling.
      *
      * @param nthFrame Throttle value. Throttle value `N` simply means that actual logging will only
      *     happen for each `Nth` frame. Logging for all other frames will be ignored.
      * @param obj Object to log.
      * @param ex Optional exception to log. Default value is `null`.
      */
    def fatalx(nthFrame: Int, obj: Any, ex: Exception = null): Unit = log(nthFrame, FATAL, obj, ex)
