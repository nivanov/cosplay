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

/**
  *
  */
object CPMirClock:
    private final val CRASH_TIME = SimpleDateFormat("yyyy MM dd HH:mm z")
        .parse("1997 June 25 09:18 UTC")
        .getTime

    private var elapsedMs = 0L
    private var time = 0L

    /**
      *
      * @param time
      */
    def setElapsedTime(time: Long): Unit = this.time = CRASH_TIME + time

    /**
      *
      * @param deltaMs
      */
    def addTime(deltaMs: Long): Unit = time += deltaMs

    /**
      * Gets current station time in milliseconds.
      */
    inline def now(): Long = time

