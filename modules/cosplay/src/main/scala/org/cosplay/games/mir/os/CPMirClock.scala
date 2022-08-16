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

package org.cosplay.games.mir.os

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

import java.text.*
import org.cosplay.*
import games.mir.*
import java.util.Date

/**
  *
  */
object CPMirClock:
    private final val DATETIME_ZONE_FMT = SimpleDateFormat("yyyy MMMM dd HH:mm z")
    private final val DATETIME_FMT = SimpleDateFormat("yyyy MMMM dd HH:mm:ss")
    private final val TIME_FMT = SimpleDateFormat("HH:mm:ss")
    private final val CRASH_TIME_MS = DATETIME_ZONE_FMT.parse("1997 June 25 09:18 UTC").getTime
    private final val OS_BUILD_TIME_MS = DATETIME_ZONE_FMT.parse("1990 Jan 1 00:01 UTC").getTime
    private final val OS_CREW_ARRIVE_TIME_MS = DATETIME_ZONE_FMT.parse("1995 Jan 1 00:01 UTC").getTime
    private final val YEAR_IN_MS = 365 * 24 * 60 * 60 * 1000L

    private var time = 0L

    /**
      *
      * @param timeMs
      */
    def setElapsedTimeMs(timeMs: Long): Unit = time = CRASH_TIME_MS + timeMs

    /**
      *
      * @param deltaMs
      */
    def addTime(deltaMs: Long): Unit = time += deltaMs

    /**
      * Gets current station time in milliseconds.
      */
    inline def now(): Long = time

    /**
      *
      */
    def formatTimeDate(): String = DATETIME_FMT.format(new Date(now()))

    /**
      *
      */
    def formatTime(): String = ???

    /**
      *
      */
    def formatDate(): String = ???

    def randLastLoginBeforeCrash(): Long = CRASH_TIME_MS - CPRand.randLong(5.hours, 24.hours)

    /**
      *
      */
    def randSysTime(): Long = OS_BUILD_TIME_MS + CPRand.randLong(0L, YEAR_IN_MS)

    /**
      *
      */
    def randCrewTime(): Long = OS_CREW_ARRIVE_TIME_MS + CPRand.randLong(0L, 2 * YEAR_IN_MS)

