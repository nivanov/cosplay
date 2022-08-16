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

import org.cosplay.*
import games.mir.*
import org.cosplay.games.mir.os.progs.mash.CPMirMashProgram
import org.cosplay.games.mir.station.{CPMirCrewMember, CPMirModuleDevice, CPMirStation}
import os.*
import progs.*

import scala.concurrent.*
import scala.concurrent.ExecutionContext.Implicits.global

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
object CPMirOs:
    val VERSION = "1.12.04"

    /**
      *
      * @param fs
      * @param usrs
      * @param player
      */
    def apply(
        fs: CPMirFileSystem,
        usrs: Seq[CPMirUser],
        player: CPMirCrewMember
    ): CPMirOs =
        require(usrs.exists(_.isRoot))

        val rootUsr = usrs.find(_.isRoot).get

        new CPMirOs:
            override def getFs: CPMirFileSystem = fs
            override def bootUp(con: CPMirConsole): Unit =
                stateMgr.state.osRebootCnt += 1

                CPMirRuntime(fs, con).exec(
                    None,
                    fs.file("/sbin/boot").get,
                    Seq.empty,
                    fs.file("/").get,
                    rootUsr,
                    Map.empty
                )

            override def getAllUsers: Seq[CPMirUser] = usrs
            override def getRootUser: CPMirUser = rootUsr
            override def shutDown(): Unit = ???
            override def restart(): Unit = ???

/**
  * MirX.
  */
trait CPMirOs extends Serializable:
    /**
      *
      */
    def getFs: CPMirFileSystem

    /**
      *
      */
    def getRootUser: CPMirUser

    /**
      *
      */
    def getAllUsers: Seq[CPMirUser]

    /**
      *
      */
    def restart(): Unit

    /**
      * @param con
      */
    def bootUp(con: CPMirConsole): Unit

    /**
      *
      */
    def shutDown(): Unit
