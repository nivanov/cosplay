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
                All rights reserved.
*/

import org.cosplay.*
import org.cosplay.CPColor.*
import org.cosplay.CPKeyboardKey.*
import org.cosplay.CPPixel.*
import org.cosplay.CPArrayImage.*
import org.cosplay.prefabs.scenes.*
import org.cosplay.prefabs.shaders.*
import org.cosplay.prefabs.sprites.*

object CPBirdTitleScene extends CPScene("title", None, GAME_BG_PX):
    private val bgSnd = CPSound("sounds/games/bird/bg.wav")
    private val logoImg = CPImage.loadRexXp("images/games/bird/bird_logo.xp").trimBg()
    private val fadeInShdr = CPSlideInShader.sigmoid(
        CPSlideDirection.CENTRIFUGAL,
        true,
        3000.ms,
        GAME_BG_PX
    )
    private val CS = Seq(C1, C2, C3, C4, C5)
    private val starStreakShdr = CPStarStreakShader(
        true,
        GAME_BG_PX.bg.get,
        Seq(
            CPStarStreak('.', CS, ratio = 0.025, steps = 30, speed = (0f, .2f), z = 0),
            CPStarStreak(':', CS, 0.015, 25, (0f, .4f), 0),
            CPStarStreak('|', CS, 0.005, 50, (0f, .8f), 0)
        ),
        autoStart = true,
        skip = (zpx, _, _) => zpx.z == 1
    )
    private val borderShdr = CPBorderShader(true, 5, true, -.03f, true)
    private val blinkShdr = new CPShader():
        override def render(ctx: CPSceneObjectContext, objRect: CPRect, inCamera: Boolean): Unit =
            if ctx.isVisible && inCamera then
                val rect = ctx.getCameraFrame
                val canv = ctx.getCanvas
                rect.loop((x, y) => {
                    val zpx = canv.getZPixel(x, y)
                    val px = zpx.px
                    if px.char == '@' && CPRand.randFloat() < 0.06f then canv.drawPixel(px.withChar('-'), x, y, zpx.z)
                })

    // Add scene objects...
    addObjects(
        // Main logo.
        CPCenteredImageSprite(img = logoImg, 1, shaders = blinkShdr.seq),
        // Off screen sprite since shaders are applied to entire screen.
        new CPOffScreenSprite(shaders = Seq(fadeInShdr, starStreakShdr, borderShdr)),
        // Exit on 'Q' press.
        CPKeyboardSprite(KEY_LO_Q, _.exitGame()),
        // Toggle audio on 'CTRL+A' press.
        CPKeyboardSprite(KEY_CTRL_A, _ => toggleAudio()),
        // Transition to the next scene on 'Space' press.
        CPKeyboardSprite(KEY_SPACE, _.switchScene("play"))
    )

    private def startBgAudio(): Unit = bgSnd.loop(2000.ms)
    private def stopBgAudio(): Unit = bgSnd.stop(2000.ms)

    /**
      * Toggles audio on and off.
      */
    private def toggleAudio(): Unit =
        if audioOn then
            stopBgAudio()
            audioOn = false
        else
            startBgAudio()
            audioOn = true

    override def onActivate(): Unit = if audioOn then startBgAudio()
    override def onDeactivate(): Unit = if audioOn then stopBgAudio()

