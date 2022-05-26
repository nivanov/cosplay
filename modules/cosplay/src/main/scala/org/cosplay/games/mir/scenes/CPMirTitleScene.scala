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
import sprites.*
import prefabs.images.ani.*
import prefabs.shaders.*
import prefabs.sprites.*

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
object CPMirTitleScene extends CPMirStarStreakSceneBase("title", "bg2.wav"):
    private val state = stateMgr.state
    private val clickSnd = CPSound(s"$SND_HOME/click.wav")
    private val logoImg = CPImage.loadRexXp(s"images/games/mir/${state.logoImg}").trimBg()
    private val spinGlobeImgs = CPSpinningGlobeAniImage.trimBg().split(47, 23).map(
        _.skin((px, _, _) ⇒ px.withFg(FG).withDarkerFg(0.85f))
    )
    private val spinGlobeAni = CPAnimation.filmStrip("ani", 99, true, false, spinGlobeImgs)
    private val spinGlobeSpr = CPAnimationSprite("spr", Seq(spinGlobeAni), 4, 1, 1, "ani")

    addObjects(
        // Main logo.
        CPCenteredImageSprite(img = logoImg, z = 2),
        // Spinning globe.
        spinGlobeSpr,
        // Add full-screen shaders - order is important.
        new CPOffScreenSprite(shaders = Seq(starStreakShdr, crtShdr, fadeInShdr, fadeOutShdr)),
        // Transition to the next scene on 'Enter' press.
        CPKeyboardSprite(KEY_SPACE, _ => clickSnd.play(0, _ ⇒ fadeOutShdr.start(_.switchScene("menu")))),
        // Sprite for ghost images.
        new CPMirGhostSprite(true)
    )
