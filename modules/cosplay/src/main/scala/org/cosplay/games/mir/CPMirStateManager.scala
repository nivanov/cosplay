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
import impl.*
import games.mir.os.*
import progs.*
import progs.mash.*
import games.mir.station.*
import org.apache.commons.lang3.SystemUtils

import java.io.*
import scala.collection.mutable
import scala.io.Source
import scala.util.Using

@SerialVersionUID(1_0_0L)
case class CPMirState(
    gameId: String,
    os: CPMirOs,
    var osRebootCnt: Int,
    station: CPMirStation,
    player: CPMirUser,
    crew: Seq[CPMirCrewMember],
    var logoImg: String,
    var bg: CPColor,
    var fg: CPColor,
    var crtVisual: Boolean,
    var crtAudio: Boolean,
    var crtOverscanProb: Float,
    var crtOverscanFactor: Float,
    var crtTearProb: Float,
    var elapsedTimeMs: Long,
    var lastLoginTstamp: Long,
    badgeMirXAdmin: CPMirPlayerBadge,
    badgeMashDev: CPMirPlayerBadge,
    badgeCommSpec: CPMirPlayerBadge,
    badgeLifeSupportSpec: CPMirPlayerBadge,
    badgeOrbitalSpec: CPMirPlayerBadge
) extends Serializable

/**
  *
  */
object CPMirStateManager:
    final val FG_GREEN = CPColor("0x00AF00")
    final val BG_GREEN = CPColor("0x010101")
    final val FG_YELLOW = CPColor("0xE6C906")
    final val BG_YELLOW = CPColor("0x010101")
    final val FG_WHITE = CPColor("0x999999")
    final val BG_WHITE = CPColor("0x010101")

    final val LOGO_GREEN = "mir_logo_green.xp"
    final val LOGO_YELLOW = "mir_logo_yellow.xp"
    final val LOGO_WHITE = "mir_logo_white.xp"

    final val DFLT_BG = BG_YELLOW
    final val DFLT_FG = FG_YELLOW
    final val DFLT_LOGO_IMAGE = LOGO_YELLOW

    private final val DIR = "mir/saved"

import CPMirStateManager.*

/**
  *
  */
class CPMirStateManager:
    // Player protagonist.
    private var player = _
    private var os: CPMirOs = _
    private var station: CPMirStation = _

    private var stateFound = false
    private var stateLoadFailed = false

    val state: CPMirState = init()
//        try
//            loadLatestState() match
//                case Some(v) =>
//                    loaded = true
//                    v
//                case None =>
//                    loaded = false
//                    init()
//        catch
//            case e: Exception =>
//                loadFailed = true
//                loaded = false
//                init()

    /**
      *
      */
    private def init(): CPMirState =
        // Station sim.
        station = CPMirStation()

        // Users.
        // NOTE: root password is not guessable in the game - but can be obtained.
        val crew = station.getCrew
        player = crew.head
        val rootUsr = CPMirUser.mkRoot()
        val usrs = mutable.ArrayBuffer(rootUsr)
        crew.tail.foreach(p => usrs += CPMirUser(p.username, CPRand.rand(p.passwords), Option(p)))

        // Init file system.
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

        // TODO: add content to 'inbox' and 'outbox'.

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

        for mod <- station.allModules do
            val modAbbr = mod.abbreviation
            addDeviceFile(modAbbr, mod.oxygenDetectorDevice)
            addDeviceFile(modAbbr, mod.airPressureDevice)
            addDeviceFile(modAbbr, mod.fireDetectorDevice)
            addDeviceFile(modAbbr, mod.fireSuppressionDevice)
            addDeviceFile(modAbbr, mod.powerSupplyDevice)

        val passwd = usrs.map(usr =>
            val info = if usr.isRoot then "" else usr.getPlayer.get.nameCamelCase
            s"${usr.getUsername}:${usr.getId}:$info:/home/${usr.getUsername}"
        ).toSeq
        etc.addRegFile("passwd", rootUsr, true, false, passwd)

        // OS.
        os = CPMirOs(new CPMirFileSystem(root), usrs.toSeq, player)

        CPEngine.rootLog().info(s"New game state is initialized.")

        CPMirState(
            gameId = CPRand.guid6,
            os = os,
            station = station,
            osRebootCnt = 0,
            player = usrs.find(_.getUsername == player.username).get,
            crew = crew.toSeq,
            bg = DFLT_BG,
            fg = DFLT_FG,
            logoImg = DFLT_LOGO_IMAGE,
            crtVisual = true,
            crtAudio = true,
            crtOverscanProb = .005f,
            crtOverscanFactor = if SystemUtils.IS_OS_MAC then .03f else .01f,
            crtTearProb = .03f,
            elapsedTimeMs = 0L,
            lastLoginTstamp = CPMirClock.randSysTime(),
            badgeMirXAdmin = CPMirPlayerBadge("MirX Administrator"),
            badgeMashDev = CPMirPlayerBadge("Mash Developer"),
            badgeCommSpec = CPMirPlayerBadge("Communication Specialist"),
            badgeLifeSupportSpec = CPMirPlayerBadge("Life Support Specialist"),
            badgeOrbitalSpec = CPMirPlayerBadge("Orbital Control Specialist")
        )

    /**
      *
      */
    private def loadLatest(): Option[CPMirState] = None // TODO

    /**
      * Saves current state. Throws exception in case of any errors.
      *
      * @throws Exception Thrown in case of any errors.
      */
    def save(): Unit =
        val path = CPEngine.homeFile(s"$DIR/${state.gameId}_${state.elapsedTimeMs}.mir")
        Using.resource(
            new ObjectOutputStream(
                new BufferedOutputStream(
                    new FileOutputStream(path)
                )
            )
        ) {
            _.writeObject(state)
        }
        CPEngine.rootLog().info(s"Game saved: $path")




