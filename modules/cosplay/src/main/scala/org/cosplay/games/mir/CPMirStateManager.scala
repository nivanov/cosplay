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
import games.mir.os.*
import games.mir.os.fs.*

import scala.collection.mutable

/**
  *
  * @param os
  * @param player
  * @param crew
  * @param props
  * @param bg
  * @param fg
  * @param crtEffect
  * @param elapsedSec
  */
@SerialVersionUID(1_0_0L)
case class CPMirState(
    os: CPMirOs,
    player: CPMirPlayer,
    crew: Seq[CPMirPlayer],
    props: mutable.Map[String, AnyRef],
    var bg: CPColor,
    var fg: CPColor,
    var crtEffect: Boolean,
    var elapsedSec: Long
) extends Serializable

/**
  *
  */
object CPMirStateManager:
    private final val DFLT_BG = CPColor("0x000300")
    private final val DFLT_FG = CPColor("0x00AF00")

import CPMirStateManager.*

/**
  *
  */
class CPMirStateManager:
    // Player protagonist.
    private val player = CPMirPlayer.newPlayer
    private var os: CPMirOs = _

    private var loaded = false
    private var loadFailed = false

    val state: CPMirState =
        try
            loadLatestState() match
                case Some(v) ⇒
                    loaded = true
                    v
                case None ⇒
                    loaded = false
                    init()
        catch
            case e: Exception ⇒
                loadFailed = true
                loaded = false
                init()

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

        CPMirState(
            os = os,
            player = player,
            crew = crew.toSeq,
            props = mutable.HashMap.empty,
            bg = DFLT_BG,
            fg = DFLT_FG,
            crtEffect = true,
            elapsedSec = 0L
        )

    /**
      *
      * @return
      */
    private def loadLatestState(): Option[CPMirState] = None // TODO

    /**
      *
      * @return
      */
    def isLatestStateLoaded: Boolean = loaded

    /**
      *
      * @return
      */
    def isLatestStateLoadFailed: Boolean = loadFailed

    /**
      *
      */
    def saveSnapshot(): Unit = ???

