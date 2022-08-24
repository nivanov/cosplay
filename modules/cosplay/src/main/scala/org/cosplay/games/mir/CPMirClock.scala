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

package org.cosplay.games.mir

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

import org.cosplay.*
import mir.*

import java.text.*
import java.util.Date

/**
  *
  */
object CPMirClock:
    private final val DATETIME_ZONE_FMT = SimpleDateFormat("yyyy MMMM dd HH:mm z")
    private final val DATETIME_FMT = SimpleDateFormat("yyyy MMMM dd HH:mm:ss")
    private final val DATE_FMT = SimpleDateFormat("yyyy MMMM dd")
    private final val TIME_FMT = SimpleDateFormat("HH:mm:ss")
    private final val CRASH_TIME_MS = DATETIME_ZONE_FMT.parse("1997 June 25 09:18 UTC").getTime
    private final val OS_BUILD_TIME_MS = DATETIME_ZONE_FMT.parse("1990 Jan 1 00:01 UTC").getTime
    private final val OS_CREW_ARRIVE_TIME_MS = DATETIME_ZONE_FMT.parse("1995 Jan 1 00:01 UTC").getTime
    private final val YEAR_IN_MS = 365 * 24 * 60 * 60 * 1000L

    private var startMs: Long = -1L
    private var initMs: Long = -1L

    /**
      *
      * @param elapsedMs
      */
    def init(elapsedMs: Long): Unit =
        startMs = CRASH_TIME_MS + elapsedMs
        initMs = System.currentTimeMillis()

    /**
      *
      */
    def getElapsedTime: Long = now() - CRASH_TIME_MS

    /**
      * Gets current station time in milliseconds.
      */
    def now(): Long = startMs + (System.currentTimeMillis() - initMs)

    /**
      *
      */
    def formatNowTimeDate(): String = DATETIME_FMT.format(new Date(now()))

    /**
      *
      */
    def formatNowTime(): String = TIME_FMT.format(new Date(now()))

    /**
      *
      */
    def formatNowDate(): String = DATE_FMT.format(new Date(now()))

    /**
      * @param ms
      */
    def formatTimeDate(ms: Long): String = DATETIME_FMT.format(new Date(ms))

    /**
      * @param ms
      */
    def formatTime(ms: Long): String = TIME_FMT.format(new Date(ms))

    /**
      * @param ms
      */
    def formatDate(ms: Long): String = DATE_FMT.format(new Date(ms))

    /**
      *
      */
    def randLastLoginBeforeCrash(): Long = CRASH_TIME_MS - CPRand.randLong(5.hours, 24.hours)

    /**
      *
      */
    def randSysTime(): Long = OS_BUILD_TIME_MS + CPRand.randLong(0L, YEAR_IN_MS)

    /**
      *
      */
    def randCrewTime(): Long = OS_CREW_ARRIVE_TIME_MS + CPRand.randLong(0L, 2 * YEAR_IN_MS)

