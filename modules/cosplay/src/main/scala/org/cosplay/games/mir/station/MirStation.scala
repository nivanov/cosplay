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

package org.cosplay.games.mir.station

import java.text.SimpleDateFormat
import org.cosplay.games.mir.*
import os.MirUser

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
  *
  */
class MirStation extends Serializable:
    private var modules: Seq[MirModule] = _
    private var crew: Seq[MirCrewMember] = _

    init()

    /**
      *
      */
    private def init(): Unit =
        val fmt = SimpleDateFormat("dd MMMM yyyy")
        modules = Seq(
            MirModule(
                "Core Module", "cor",
                fmt.parse("19 Feb 1986"),
                MirModuleDevice(null /* TODO */, "pwr"),
                MirModuleDevice(null /* TODO */, "oxy"),
                MirModuleDevice(null /* TODO */, "fdr"),
                MirModuleDevice(null /* TODO */, "fsp"),
                MirModuleDevice(null /* TODO */, "aps")
            ),
            MirModule(
                "Kvant-1",
                "kv1",
                fmt.parse("31 Mar 1987"),
                MirModuleDevice(null /* TODO */ , "pwr"),
                MirModuleDevice(null /* TODO */ , "oxy"),
                MirModuleDevice(null /* TODO */ , "fdr"),
                MirModuleDevice(null /* TODO */ , "fsp"),
                MirModuleDevice(null /* TODO */ , "aps")
            ),
            MirModule(
                "Kvant-2",
                "kv2",
                fmt.parse("26 Nov 1989"),
                MirModuleDevice(null /* TODO */ , "pwr"),
                MirModuleDevice(null /* TODO */ , "oxy"),
                MirModuleDevice(null /* TODO */ , "fdr"),
                MirModuleDevice(null /* TODO */ , "fsp"),
                MirModuleDevice(null /* TODO */ , "aps")
            ),
            MirModule(
                "Kristal",
                "krs",
                fmt.parse("31 May 1990"),
                MirModuleDevice(null /* TODO */ , "pwr"),
                MirModuleDevice(null /* TODO */ , "oxy"),
                MirModuleDevice(null /* TODO */ , "fdr"),
                MirModuleDevice(null /* TODO */ , "fsp"),
                MirModuleDevice(null /* TODO */ , "aps")
            ),
            MirModule(
                "Spektr",
                "spk",
                fmt.parse("20 May 1995"),
                MirModuleDevice(null /* TODO */ , "pwr"),
                MirModuleDevice(null /* TODO */ , "oxy"),
                MirModuleDevice(null /* TODO */ , "fdr"),
                MirModuleDevice(null /* TODO */ , "fsp"),
                MirModuleDevice(null /* TODO */ , "aps")
            ),
            MirModule(
                "Docking Module",
                "dck",
                fmt.parse("15 Nov 1995"),
                MirModuleDevice(null /* TODO */ , "pwr"),
                MirModuleDevice(null /* TODO */ , "oxy"),
                MirModuleDevice(null /* TODO */ , "fdr"),
                MirModuleDevice(null /* TODO */ , "fsp"),
                MirModuleDevice(null /* TODO */ , "aps")
            ),
            MirModule(
                "Priroda",
                "prd",
                fmt.parse("26 Apr 1996"),
                MirModuleDevice(null /* TODO */ , "pwr"),
                MirModuleDevice(null /* TODO */ , "oxy"),
                MirModuleDevice(null /* TODO */ , "fdr"),
                MirModuleDevice(null /* TODO */ , "fsp"),
                MirModuleDevice(null /* TODO */ , "aps")
            )
        )
        val arr = mutable.ArrayBuffer.empty[MirCrewMember]
        for _ <- 0 until NPC_CNT + 1 do
            var found = false
            while !found do
                val crewman = MirCrewMember.newPlayer
                if !arr.exists(p => p.username == crewman.username || p.lastName == crewman.lastName) then
                    found = true
                    arr += crewman
        crew = arr.toSeq

    /**
      *
      */
    def allModules: Seq[MirModule] = modules

    /**
      * Gets all crew members including NPCs and the player.
      */
    def getCrew: Seq[MirCrewMember] = crew
