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
import CPPixel.*
import CPColor.*
import CPArrayImage.*
import CPKeyboardKey.*
import games.mir.*
import sprites.*
import prefabs.sprites.*
import scala.util.*

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
  * Main menu scene.
  */
object CPMirMenuScene extends CPMirStarStreakSceneBase("menu", "bg1.wav"):
    private def mkMenuImage(): CPImage =
        val badgeMirXAdmin = if stateMgr.state.badgeMirXAdmin.isEarned then "(X)" else "( )"
        val badgeMashDev = if stateMgr.state.badgeMashDev.isEarned then "(X)" else "( )"
        val badgeCommSpec = if stateMgr.state.badgeCommSpec.isEarned then "(X)" else "( )"
        val badgeLifeSupportSpec = if stateMgr.state.badgeLifeSupportSpec.isEarned then "(X)" else "( )"
        val badgeOrbitalSpec = if stateMgr.state.badgeOrbitalSpec.isEarned then "(X)" else "( )"

        val pxs = markup.process(
            s"""
               | <@ Menu @>
               | ------
               |
               | <%[C]%> - Continue
               |
               | <%[N]%> - New Game
               | <%[S]%> - Save Game
               | <%[L]%> - Load Game
               |
               | <%[O]%> - Options
               | <%[T]%> - Tutorial
               | <%[Q]%> - Quit
               |
               |
               | <~Open menu in-game by pressing~> <%[F10]%>
               |
               |
               |
               | <@ Badges @>
               | --------
               | <%$badgeMirXAdmin%> ${stateMgr.state.badgeMirXAdmin.name}
               | <%$badgeMashDev%> ${stateMgr.state.badgeMashDev.name}
               | <%$badgeCommSpec%> ${stateMgr.state.badgeCommSpec.name}
               | <%$badgeLifeSupportSpec%> ${stateMgr.state.badgeLifeSupportSpec.name}
               | <%$badgeOrbitalSpec%> ${stateMgr.state.badgeOrbitalSpec.name}
               |
            """.stripMargin
        )
        CPArrayImage(pxs, BG_PX).trimBg(_ == BG_PX)

    private val menuSpr = new CPCenteredImageSprite(img = mkMenuImage(), z = 2)

    addObjects(
        // Sprite for ghost images.
        new CPMirGhostSprite(false),
        menuSpr,
        new CPKeyboardSprite((_, key) => key match
            case KEY_LO_Q => clickThenFade(_.exitGame())
            case KEY_LO_T => clickThenFade(_.switchScene("tutorial", false, ("next_scene", "menu")))
            case KEY_LO_O => clickThenFade(_.switchScene("options"))
            case KEY_LO_L => clickThenFade(_.switchScene("load"))
            case KEY_LO_N => clickThenFade(_.switchScene("new_game"))
            case KEY_LO_S => clickThenFade(_ => {
                Try(stateMgr.save()) match
                    case Success(_) => showConfirm(
                        s"Current game progress has been successfully saved.",
                        () => menuSpr.hide(),
                        () => menuSpr.show(),
                        "Save"
                    )
                    case Failure(e) => showError(
                        s"Failed to save game progress due to: <%${e.getMessage}%>",
                        () => menuSpr.hide(),
                        () => menuSpr.show()
                    )
            })
            case _ => ()
        ),
        // Add full-screen shaders - order is important.
        new CPOffScreenSprite(shaders = Seq(starStreakShdr, crtShdr, fadeInShdr, fadeOutShdr))
    )

    override def onActivate(): Unit =
        super.onActivate()
        menuSpr.setImage(mkMenuImage())
