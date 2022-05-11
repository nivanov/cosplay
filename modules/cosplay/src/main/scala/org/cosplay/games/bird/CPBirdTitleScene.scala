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

package org.cosplay.games.bird

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

import org.cosplay.*
import CPColor.*
import CPKeyboardKey.*
import CPPixel.*
import org.cosplay.CPArrayImage.*
import prefabs.scenes.*
import prefabs.shaders.*

object CPBirdTitleScene extends CPScene("title", None, GAME_BG_PX):
    private val logoImg = CPImage.loadRexXp("images/games/bird/bird_logo.xp").trimBg()
    private val fadeInShdr = CPSlideInShader.sigmoid(
        CPSlideDirection.CENTRIFUGAL,
        true,
        3000,
        GAME_BG_PX
    )
    private val CS = Seq(C1, C2, C3, C4, C5)
    private val starStreakShdr = CPStarStreakShader(
        true,
        GAME_BG_PX.bg.get,
        Seq(
            CPStarStreak('.', CS, 0.025, 30, (0f, .2f), 0),
            CPStarStreak(':', CS, 0.015, 25, (0f, .4f), 0),
            CPStarStreak('|', CS, 0.005, 50, (0f, .8f), 0)
        ),
        autoStart = true,
        skip = (zpx, _, _) ⇒ zpx.z == 1
    )

    // Add scene objects...
    addObjects(
        // Main logo.
        CPCenteredImageSprite(img = logoImg, 1),
        // Off screen sprite since shaders are applied to entire screen.
        new CPOffScreenSprite(shaders = Seq(fadeInShdr, starStreakShdr)),
        CPKeyboardSprite(KEY_LO_Q, _.exitGame()), // Exit on 'Q' press.
        CPKeyboardSprite(KEY_SPACE, _.switchScene("play"))// Transition to the next scene on 'Enter' press.
    )

    override def onActivate(): Unit =
        super.onActivate()

    override def onDeactivate(): Unit =
        super.onDeactivate()

