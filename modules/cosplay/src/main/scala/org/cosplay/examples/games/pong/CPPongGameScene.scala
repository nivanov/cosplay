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

package org.cosplay.examples.games.pong

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

object CPPongGameScene extends CPScene("game", None, CPPixel('.', C_GRAY2, C_GRAY1)):
    private var playerScore = 0
    private var enemyScore = 0

    private val playerScoreImg = FIG_BIG.render(playerScore.toString, C_WHITE).skin(
        (px, _, _) => px.char match
            case ':' => px.withFg(C_GREY70)
            case _ => px
    ).trimBg()

    private val enemyScoreImg = FIG_BIG.render(enemyScore.toString, C_WHITE).skin(
        (px, _, _) => px.char match
            case ':' => px.withFg(C_GREY70)
            case _ => px
    ).trimBg()

    private val playerScoreSpr = new CPImageSprite("playerScoreSpr", 0, 0, 0, playerScoreImg, shaders = Seq(CPFadeInShader(true, 1500, bgPx))):
        override def update(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            setX((canv.dim.width - playerScoreImg.getWidth) / 20)
            setY((canv.dim.height - playerScoreImg.getHeight) / 20)

    private val enemyScoreSpr = new CPImageSprite("enemyScoreSpr", 0, 0, 0, enemyScoreImg, shaders = Seq(CPFadeInShader(true, 1500, bgPx))):
        override def update(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            setX((canv.dim.width - enemyScoreImg.getWidth))
            setY((canv.dim.height - enemyScoreImg.getHeight) / 20)



    addObject(CPKeyboardSprite(KEY_LO_Q, _.exitGame()))
    addObject(playerScoreSpr)
    addObject(enemyScoreSpr)
