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
import CPKeyboardKey.*
import scenes.*
import scenes.sprites.*
import prefabs.sprites.*
import CPMirStateManager.*
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
  * 
  */
object CPMirOptionsScene extends CPMirStarStreakSceneBase("options", "bg1.wav"):
    private var savedCrtVisual: Boolean = _
    private var savedCrtAudio: Boolean = _
    private var savedFg: CPColor = _
    private var savedBg: CPColor = _
    private val UNSEL = '+'

    private def mkImage(): CPImage =
        val state = stateMgr.state
        val crtVisual = if state.crtVisual then UNSEL else " "
        val crtAudio = if state.crtAudio then UNSEL else " "
        val colGreen = if state.fg == CPMirStateManager.FG_GREEN then UNSEL else " "
        val colYellow = if state.fg == CPMirStateManager.FG_YELLOW then UNSEL else " "
        val colWhite = if state.fg == CPMirStateManager.FG_WHITE then UNSEL else " "
        val txtPxs = markup.process(
            s"""
               | <@ Options @>
               | ---------
               | 
               | <%[V]%> - Visual CRT Effect [<~$crtVisual~>]
               | <%[A]%> - Audio CRT Effect  [<~$crtAudio~>]
               | <%[C]%> - CRT Color:
               |           Green   [<~$colGreen~>]
               |           Yellow  [<~$colYellow~>]
               |           White   [<~$colWhite~>]
               |
               | (color changes require restart)
               |
               |
               |
               |
               | <%[SPACE]%>  Accept Changes
               | <%[X]%>      Discard Changes
            """.stripMargin
        )
        CPArrayImage(txtPxs, BG_PX).trimBg(_ == BG_PX)

    private val imgSpr = new CPCenteredImageSprite(img = mkImage(), z = 2)

    /**
      *
      */
    private def update(): Unit = click(() => imgSpr.setImage(mkImage()))

    addObjects(
        new CPKeyboardSprite((ctx, key) => {
            val state = stateMgr.state
            key match
                case KEY_LO_V =>
                    state.crtVisual = !state.crtVisual
                    update()
                case KEY_LO_A =>
                    state.crtAudio = !state.crtAudio
                    update()
                case KEY_LO_C =>
                    if state.fg == FG_GREEN then state.fg = FG_YELLOW
                    else if state.fg == FG_YELLOW then state.fg = FG_WHITE
                    else state.fg = FG_GREEN
                    update()
                case KEY_LO_X =>
                    state.crtVisual = savedCrtVisual
                    state.crtAudio = savedCrtAudio
                    state.fg = savedFg
                    state.bg = savedBg
                    clickThenFade(_.switchScene("menu"))
                case KEY_SPACE =>
                    clickThenFade(ctx => Try(stateMgr.save()) match
                        case Success(_) => ctx.switchScene("menu")
                        case Failure(e) => showError(
                            s"Failed to save game options due to: <%${e.getMessage}%>",
                            () => imgSpr.hide(),
                            () => imgSpr.show()
                        )
                    )
                case _ => ()
        }),
        // Sprite for ghost images.
        new CPMirGhostSprite(false),
        imgSpr,
        // Add full-screen shaders - order is important.
        new CPOffScreenSprite(shaders = Seq(starStreakShdr, crtShdr, fadeInShdr, fadeOutShdr))
    )

    override def onActivate(): Unit =
        super.onActivate()
        val state = stateMgr.state
        savedCrtVisual = state.crtVisual
        savedCrtAudio = state.crtAudio
        savedFg = state.fg
        savedBg = state.bg
        imgSpr.setImage(mkImage())


