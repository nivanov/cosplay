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
case class MirState(
    gameId: String,
    os: MirOs,
    var osRebootCnt: Int,
    station: MirStation,
    player: MirUser,
    crew: Seq[MirCrewMember],
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
    badgeMirXAdmin: MirPlayerBadge,
    badgeMashDev: MirPlayerBadge,
    badgeCommSpec: MirPlayerBadge,
    badgeLifeSupportSpec: MirPlayerBadge,
    badgeOrbitalSpec: MirPlayerBadge
) extends Serializable

/**
  *
  */
object MirStateManager:
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

import MirStateManager.*

/**
  *
  */
class MirStateManager:
    // Player protagonist.
    private var player: MirUser = _
    private var os: MirOs = _
    private var station: MirStation = _

    private var stateFound = false
    private var stateLoadFailed = false

    val state: MirState = init()
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
    private def init(): MirState =
        MirClock.init(0L)

        // Station sim.
        station = MirStation()

        // Users.
        // NOTE: root password is not guessable in the game - but can be obtained.
        val crew = station.getCrew
        val head = crew.head
        player = MirUser(head.username, CPRand.rand(head.passwords), Option(head))
        val playerCrew = player.getCrewMember.get
        val rootUsr = MirUser.mkRoot()
        val usrs = mutable.ArrayBuffer(player, rootUsr)
        crew.tail.foreach(p => usrs += MirUser(p.username, CPRand.rand(p.passwords), Option(p)))

        // Init file system.
        val root = MirDirectoryFile.mkRoot(rootUsr)

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

        val plyInbox = root.dirFile(s"/home/${playerCrew.username}/inbox")
        val plyOutbox = root.dirFile(s"/home/${playerCrew.username}/outbox")
        val crewDir = usr.addDirFile("crew", rootUsr)

        // TODO: add content to:
        // TODO: - 'inbox' folder
        // TODO: - 'outbox' folder
        // TODO: - 'crew' folder.

        // Install executables.
        sbin.addExecFile("boot", rootUsr, new MirBootProgram)
        sbin.addExecFile("ls", rootUsr, new MirLsProgram)
        sbin.addExecFile("login", rootUsr, new MirLoginProgram)
        sbin.addExecFile("mash", rootUsr, new MirMashProgram)

        // Install devices
        dev.addDeviceFile("radio", rootUsr, null /* TODO */)
        dev.addDeviceFile("stela1", rootUsr, null /* TODO */)
        dev.addDeviceFile("stela2", rootUsr, null /* TODO */)
        dev.addDeviceFile("travers", rootUsr, null /* TODO */)
        dev.addDeviceFile("lira", rootUsr, null /* TODO */)
        dev.addDeviceFile("alt", rootUsr, null /* TODO */)
        dev.addDeviceFile("uhf", rootUsr, null /* TODO */)
        dev.addDeviceFile("thrust", rootUsr, null /* TODO */)

        def addDeviceFile(modAbbr: String, modDev: MirModuleDevice): Unit =
            dev.addDeviceFile(s"$modAbbr-${modDev.getAbbreviation}", rootUsr, modDev.getDriver)

        for mod <- station.allModules do
            val modAbbr = mod.abbreviation
            addDeviceFile(modAbbr, mod.oxygenDetectorDevice)
            addDeviceFile(modAbbr, mod.airPressureDevice)
            addDeviceFile(modAbbr, mod.fireDetectorDevice)
            addDeviceFile(modAbbr, mod.fireSuppressionDevice)
            addDeviceFile(modAbbr, mod.powerSupplyDevice)

        val passwd = usrs.map(usr =>
            val info = if usr.isRoot then "" else usr.getCrewMember.get.nameCamelCase
            s"${usr.getUsername}:${usr.getUid}:$info:${usr.getHomeDirectory}"
        ).toSeq
        etc.addRegFile("passwd", rootUsr, true, false, passwd)

        // OS.
        os = MirOs(new MirFileSystem(root), usrs.toSeq, playerCrew)

        CPEngine.rootLog().info(s"New game state is initialized.")

        MirState(
            gameId = CPRand.guid6,
            os = os,
            station = station,
            osRebootCnt = 0,
            player = usrs.find(_.getUsername == playerCrew.username).get,
            crew = crew,
            bg = DFLT_BG,
            fg = DFLT_FG,
            logoImg = DFLT_LOGO_IMAGE,
            crtVisual = true,
            crtAudio = true,
            crtOverscanProb = .005f,
            crtOverscanFactor = if SystemUtils.IS_OS_MAC then .03f else .01f,
            crtTearProb = .03f,
            elapsedTimeMs = 0L,
            lastLoginTstamp = MirClock.randSysTime(),
            badgeMirXAdmin = MirPlayerBadge("MirX Administrator"),
            badgeMashDev = MirPlayerBadge("Mash Developer"),
            badgeCommSpec = MirPlayerBadge("Communication Specialist"),
            badgeLifeSupportSpec = MirPlayerBadge("Life Support Specialist"),
            badgeOrbitalSpec = MirPlayerBadge("Orbital Control Specialist")
        )

    /**
      *
      */
    private def loadLatest(): Option[MirState] = None // TODO

    /**
      * Saves current state. Throws exception in case of any errors.
      *
      * @throws Exception Thrown in case of any errors.
      */
    def save(): Unit =
        // Save current timestamp.
        state.elapsedTimeMs = MirClock.getElapsedTime

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




