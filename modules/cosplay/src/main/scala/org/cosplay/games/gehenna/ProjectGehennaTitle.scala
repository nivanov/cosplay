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

package org.cosplay.games.gehenna

import org.cosplay.*
import org.cosplay.CPFIGLetFont.*
import org.cosplay.games.gehenna.shaders.TextDripShader
import org.cosplay.prefabs.shaders.*

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


object ProjectGehennaTitle extends CPScene("title", None, GAME_BG_PX):
    private val titleText = "Project Gehenna"

    private val fadeInShdr = CPSlideInShader.sigmoid(
        CPSlideDirection.CENTRIFUGAL,
        true,
        3000,
        GAME_BG_PX,
        onFinish = _ => TextDripShader.start()
    )

    private def titleImage(): CPImage = FIG_OGRE.render(titleText, C1).trimBg()

    private val titleSpr = new CPImageSprite("title", 0, 0, 1, titleImage(), shaders = Seq(fadeInShdr, TextDripShader)):
        private val clickSound = CPSound("sounds/games/gehenna/click.wav")
        private val introSong = CPSound("sounds/games/gehenna/intro song.wav")

        override def update(ctx: CPSceneObjectContext): Unit =
            setX((ctx.getCanvas.w - this.getWidth) / 2)

            if !introSong.isPlaying then
                introSong.loop(3000)

    addObjects(
        titleSpr
    )
