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

package org.cosplay.games.pong

import org.cosplay.CPColor.*
import org.cosplay.*
import org.cosplay.CPArrayImage.*
import prefabs.shaders.*
import org.cosplay.CPFIGLetFont.*
import org.cosplay.CPCanvas.*
import org.cosplay.CPDim.*
import CPPixel.*
import CPKeyboardKey.*
import prefabs.images.*
import prefabs.scenes.*
import org.cosplay.games.pong.shaders.*

/*
   _________            ______________
   __  ____/_______________  __ \__  /_____ _____  __
   _  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
   / /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
   \____/  \____//____/ /_/     /_/  \__,_/ _\__, /
                                            /____/

          2D ASCII JVM GAME ENGINE FOR SCALA3
              (C) 2021 Rowan Games, Inc.
                ALl rights reserved.
*/

object CPPongTitleScene extends CPScene("title", None, BG_PX):
    private val introSnd = CPSound(s"sounds/games/pong/intro.wav", 0.3f)
    private val logoImg = FIG_BIG_MONEY_NE.render("Pong", C_WHITE).skin(
        (px, _, _) => px.char match
            case '$' => px.withFg(C5)
            case _ => px.withFg(C4)
    ).trimBg()
    private val helpImg = CPArrayImage(
        prepSeq(
            """
              |       GET 10 POINTS TO WIN
              |
              |
              |       
              |           Up      Down
              |         .----.    .----.
              |         | W  |    | S  |
              |         `----'    `----'
              |           or        or
              |         .----.    .----.
              |         | Up |    | Dn |
              |         `----'    `----'
              |       
              |         [ENTER]   Play
              |         [Q]       Quit Any Time
              |
              |
              |
              |  Copyright (C) 2022 Rowan Games, Inc
            """),
        (ch, _, y) =>
            if y == 18 then ch&C3
            else
                ch match
                    case c if c.isLetter => c&C4
                    case '|' | '.' | '`' | '-' | '\'' => ch&C2
                    case _ => ch.toUpper&C1
    ).trimBg()

    private val sparkleShdr = CPPongTitleSparkleShader()
    private val fadeInShdr = CPFadeInShader(true, 1000, BG_PX, onFinish = _ ⇒ sparkleShdr.start())

    // Add scene objects...
    addObjects(
        CPImageSprite(xf = c => (c.w - logoImg.w) / 2, c => Math.max(0, c.h / 2 - logoImg.h - 1), 0, logoImg),
        CPImageSprite(xf = c => (c.w - helpImg.w) / 2, c => Math.max(0, c.h / 2 + 1), 0, helpImg),
        new CPOffScreenSprite(shaders = Seq(fadeInShdr, sparkleShdr)),
        CPKeyboardSprite(KEY_LO_Q, _.exitGame()), // Exit on 'Q' press.
        CPKeyboardSprite(KEY_ENTER, _.switchScene("play"))// Transition to the next scene on 'Enter' press.
    )

    override def onActivate(): Unit =
        super.onActivate()
        introSnd.loopAll(2000) // Start background audio.

    override def onDeactivate(): Unit =
        super.onDeactivate()
        introSnd.stop(400) // Stop background audio.
