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

package org.cosplay.games.mir.scenes

import org.cosplay.*
import games.mir.*
import prefabs.sprites.*
import sprites.*
import CPArrayImage.*
import CPPixel.*
import CPKeyboardKey.*

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
object CPMirNewGameScene extends CPMirStarStreakSceneBase("new_game", "bg1.wav"):
    private val player = stateMgr.state.player
    private val name = player.nameCamelCase
    private val username = player.username
    private val role = player.roleLowerCase
    private val txtPxs = markup.process(
        s"""
          |  <@ Escape From Mir @>
          |  -----------------
          |
          |  Based on real events (*)
          |
          |  The year is <%1997%>. It’s been 11 years since the launch of the Russian's
          |  <%Mir%> space station - the world’s first permanent human habitat in orbit
          |  above the planet Earth. Over the decade the Mir station has been
          |  assembled from 7 separate modules and is now manned by an international
          |  crew of 3 astronauts.
          |
          |  On the morning of <%June 24%> the unmanned resupply vessel <%Progress%>-34 that
          |  arrived just a few months earlier crashed into Mir space station during
          |  routine training re-docking procedure causing critical station damage.
          |  Status of the remaining crew is unknown, air is leaking, structural and
          |  orbit control damage alarm is on, the power supply subsystem is offline.
          |
          |  Your name is <%$name%> (@<%$username%>), $role. You regain
          |  consciousness in the air locked "Core Module" of the station. Through
          |  the zero-gravity mayhem of the crash you see a working computer terminal
          |  that is... rebooting.
          |
          |  <~You must escape from Mir to survive.~>
          |
          |  * - https://en.wikipedia.org/wiki/Mir
          |
          |
          |
          |  <%[SPACE]%>  Continue
          |
        """.stripMargin
    )
    private val img = CPArrayImage(txtPxs, BG_PX).trimBg(_ == BG_PX)

    addObjects(
        new CPKeyboardSprite((ctx, key) ⇒ key match
            case KEY_SPACE ⇒ clickThenFade(_.switchScene("tutorial", false, ("next_scene", "main")))
            case _ ⇒ ()
        ),
        // Sprite for ghost images.
        new CPMirGhostSprite(false),
        new CPCenteredImageSprite(img = img, z = 2),
        // Add full-screen shaders - order is important.
        new CPOffScreenSprite(shaders = Seq(starStreakShdr, crtShdr, fadeInShdr, fadeOutShdr))
    )


