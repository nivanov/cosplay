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
  * MirX.
  *
  * @param fsOpt
  * @param usrs
  * @param player
  */
@SerialVersionUID(1_0_0L)
class CPMirOs(
    fsOpt: Option[CPMirFileSystem],
    usrs: Seq[CPMirUser],
    player: CPMirCrewMember,
    station: CPMirStation) extends Serializable:
    require(usrs.exists(_.isRoot))

    private val rootUsr = usrs.find(_.isRoot).get
    private var rt: CPMirRuntime = _
    private val fs = fsOpt.getOrElse(installFs())

    /**
      * Initializes brand new file system.
      */
    private def installFs(): CPMirFileSystem =
        val root = CPMirDirectoryFile.mkRoot(rootUsr)

        // Install directory structure.
        val bin = root.addDirFile("bin", rootUsr)
        val sbin = root.addDirFile("sbin", rootUsr)
        val dev = root.addDirFile("dev", rootUsr)
        val home = root.addDirFile("home", rootUsr)
        root.addDirFile("tmp", rootUsr)
        val etc = root.addDirFile("etc", rootUsr)
        val lib = root.addDirFile("lib", rootUsr)
        val usr = root.addDirFile("usr", rootUsr)
        val usrBin = usr.addDirFile("bin", rootUsr)

        // Add homes for all non-root users.
        usrs.foreach(usr =>
            if !usr.isRoot then
                val usrHome = home.addDirFile(usr.getUsername, usr)
                usrHome.addDirFile("inbox", usr)
                usrHome.addDirFile("outbox", usr)
        )

        val plyInbox = root.dirFile(s"/home/${player.username}/inbox")
        val plyOutbox = root.dirFile(s"/home/${player.username}/outbox")

        usr.addDirFile("crew", rootUsr)

        // Install executables.
        sbin.addExecFile("boot", rootUsr, new CPMirBootProgram)
        sbin.addExecFile("ls", rootUsr, new CPMirLsProgram)
        sbin.addExecFile("login", rootUsr, new CPMirLoginProgram)
        sbin.addExecFile("mash", rootUsr, new CPMirMashProgram)

        // Install devices
        dev.addDeviceFile("radio", rootUsr, null /* TODO */)
        dev.addDeviceFile("stela1", rootUsr, null /* TODO */)
        dev.addDeviceFile("stela2", rootUsr, null /* TODO */)
        dev.addDeviceFile("travers", rootUsr, null /* TODO */)
        dev.addDeviceFile("lira", rootUsr, null /* TODO */)
        dev.addDeviceFile("alt", rootUsr, null /* TODO */)
        dev.addDeviceFile("uhf", rootUsr, null /* TODO */)
        dev.addDeviceFile("thrust", rootUsr, null /* TODO */)

        def addDeviceFile(modAbbr: String, modDev: CPMirModuleDevice): Unit =
            dev.addDeviceFile(s"$modAbbr-${modDev.getAbbreviation}", rootUsr, modDev.getDriver)
            ()

        for mod <- station.allModules do
            val modAbbr = mod.getAbbreviation
            addDeviceFile(modAbbr, mod.getOxygenDetectorDevice)
            addDeviceFile(modAbbr, mod.getAirPressureDevice)
            addDeviceFile(modAbbr, mod.getFireDetectorDevice)
            addDeviceFile(modAbbr, mod.getFireSuppressionDevice)
            addDeviceFile(modAbbr, mod.getPowerSupplyDevice)

        val passwd = usrs.map(usr =>
            val info = if usr.isRoot then "" else usr.getPlayer.get.nameCamelCase
            s"${usr.getUsername}:${usr.getId}:$info:/home/${usr.getUsername}"
        )
        etc.addRegFile("passwd", rootUsr, true, false, passwd)

        new CPMirFileSystem(root)

    /**
      *
      */
    def getFs: CPMirFileSystem = fs

    /**
      *
      */
    def getRootUser: CPMirUser = rootUsr

    /**
      *
      */
    def getAllUsers: Seq[CPMirUser] = usrs

    /**
      *
      */
    def restart(): Unit = ???

    /**
      *
      */
    def boot(con: CPMirConsole): Unit =
        stateMgr.state.osRebootCnt += 1

        rt = CPMirRuntime(fs, con)

        rt.exec(
            None,
            fs.file("/sbin/boot").get,
            Seq.empty,
            fs.file("/").get,
            rootUsr,
            Map.empty
        )

    /**
      *
      */
    def shutdown(): Unit = ???
