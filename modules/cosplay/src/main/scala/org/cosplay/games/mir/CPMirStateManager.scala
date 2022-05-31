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
import games.mir.os.fs.*
import org.apache.commons.lang3.SystemUtils

import java.io.*
import scala.collection.mutable
import scala.io.Source
import scala.util.Using

/**
  *
  * @param gameId
  * @param os
  * @param player
  * @param crew
  * @param bg
  * @param fg
  * @param crtVisual
  * @param crtAudio
  * @param crtOverscanProb
  * @param crtOverscanFactor
  * @param crtTearProb
  * @param timeMs
  */
@SerialVersionUID(1_0_0L)
case class CPMirState(
    gameId: String = CPRand.guid6,
    os: CPMirOs,
    player: CPMirPlayer,
    crew: Seq[CPMirPlayer],
    var logoImg: String,
    var bg: CPColor,
    var fg: CPColor,
    var crtVisual: Boolean,
    var crtAudio: Boolean,
    var crtOverscanProb: Float,
    var crtOverscanFactor: Float,
    var crtTearProb: Float,
    var timeMs: Long
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
    private val player = CPMirPlayer.newPlayer
    private var os: CPMirOs = _

    private var stateFound = false
    private var stateLoadFailed = false

    val state: CPMirState = init()
//        try
//            loadLatestState() match
//                case Some(v) ⇒
//                    loaded = true
//                    v
//                case None ⇒
//                    loaded = false
//                    init()
//        catch
//            case e: Exception ⇒
//                loadFailed = true
//                loaded = false
//                init()

    /**
      *
      * @return
      */
    private def init(): CPMirState =
        // Crew.
        val crew = mutable.ArrayBuffer(player) // Crew always includes the player.
        for (i ← 0 until NPC_CNT)
            var found = false
            while !found do
                val crewman = CPMirPlayer.newPlayer
                if !crew.exists(p ⇒ p != player && (p.username == player.username || p.lastName == player.lastName)) then
                    found = true
                    crew += crewman

        // Users.
        // NOTE: root password is not guessable in the game - but can be obtained.
        val rootUsr = CPMirUser(None, true, "root", CPRand.guid6)
        val usrs = mutable.ArrayBuffer(rootUsr)
        crew.foreach(p ⇒ usrs += CPMirUser(Option(p), false, p.username, CPRand.rand(p.passwords)))

        // File system.
        val rootDir = CPMirDirFile("", rootUsr, None)
        val fs = CPMirFileSystem(rootDir)

        // OS.
        os = CPMirOs(fs, usrs.toSeq)

        CPEngine.rootLog().info(s"New game state is initialized.")

        CPMirState(
            os = os,
            player = player,
            crew = crew.toSeq,
            bg = DFLT_BG,
            fg = DFLT_FG,
            logoImg = DFLT_LOGO_IMAGE,
            crtVisual = true,
            crtAudio = true,
            crtOverscanProb = .005f,
            crtOverscanFactor = if SystemUtils.IS_OS_MAC then .03f else .01f,
            crtTearProb = .03f,
            timeMs = 0L
        )

    /**
      *
      * @return
      */
    private def loadLatest(): Option[CPMirState] = None // TODO

    /**
      * Saves current state. Throws exception in case of any errors.
      *
      * @throws Exception Thrown in case of any errors.
      */
    def save(): Unit =
        val path = CPUtils.homeFile(s"$DIR/${state.gameId}_${state.timeMs}.mir")
        Using.resource(new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path)))) { _.writeObject(state) }
        CPEngine.rootLog().info(s"Game saved: $path")



