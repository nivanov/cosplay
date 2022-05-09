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

import org.cosplay.*
import CPArrayImage.*
import CPKeyboardKey.*
import CPPixel.*
import prefabs.images.ani.*
import prefabs.shaders.*

/**
  *
  */
object CPMirTitleScene extends CPScene("title", None, BG_PX):
    private val logoImg = CPImage.loadRexXp("images/games/mir/mir_logo.xp").trimBg()

    private val spinGlobeImgs = CPSpinningGlobeAniImage.trimBg().split(47, 23).map(
        _.skin((px, _, _) ⇒ px.withDarkerFg(0.85f))
    )
    private val spinGlobeAni = CPAnimation.filmStrip("ani", 99, true, false, spinGlobeImgs)
    private val spinGlobeSpr = CPAnimationSprite("spr", Seq(spinGlobeAni), 4, 1, 1, "ani")

    private val crtTurnOnSnd = CPSound("sounds/games/mir/crt_turn_on.wav")
    private val crtTearSnd = CPSound("sounds/games/mir/crt_tear.wav")
    private val crtKnockSnd = CPSound("sounds/games/mir/crt_knock.wav")
    private val crtNoiseSnd = CPSound("sounds/games/mir/crt_noise.wav")

    private val fadeInShdr = CPSlideInShader.sigmoid(CPSlideDirection.TOP_TO_BOTTOM, true, 3000, bgPx = BG_PX)
    private val fadeOutShdr = CPSlideOutShader(CPSlideDirection.TOP_TO_BOTTOM, true, 500, bgPx = BG_PX)
    private val crtShdr = new CPOldCRTShader(lineEffectProb = 1f, .03f, tearSnd = Option(crtTearSnd))
    private val colors = Seq(FG)
    private val starStreakShdr = CPStarStreakShader(
        true,
        BG,
        Seq(
            CPStarStreak('.', colors, 0.025, 30, (-.3f, 0f), 0),
            CPStarStreak('.', colors, 0.015, 25, (-.7f, 0f), 0),
            CPStarStreak('_', colors, 0.005, 50, (-1f, 0f), 0)
        ),
        skip = (zpx, _, _) ⇒ zpx.z >= 1
    )

    // Add scene objects...
    addObjects(
        // Main logo.
        CPCenteredImageSprite(img = logoImg, z = 2),
        // Spinning globe.
        spinGlobeSpr,
        // Add all screen shaders.
        new CPOffScreenSprite(shaders = Seq(fadeInShdr, fadeOutShdr, crtShdr, starStreakShdr)),
        // Exit on 'Q' press.
        CPKeyboardSprite(KEY_LO_Q, _.exitGame()),
        // Transition to the next scene on 'Enter' press.
        CPKeyboardSprite(KEY_SPACE, _ => fadeOutShdr.start(_.exitGame()))
    )

    override def onActivate(): Unit =
        starStreakShdr.start()
        crtShdr.start()
        crtTurnOnSnd.play()
        crtKnockSnd.play()
        crtNoiseSnd.loop(1000, _ ⇒ crtKnockSnd.play())

    override def onDeactivate(): Unit =
        starStreakShdr.stop()
        crtShdr.stop()
