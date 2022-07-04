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

package org.cosplay.games.projectGehenna

import org.cosplay.*
import org.cosplay.CPFIGLetFont.*

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
    private val finalTitleText = "Project Gehenna"
    private var currTitleText = ""

    private def titleImage(): CPImage = FIG_OGRE.render(currTitleText, C1).trimBg()

    private val titleSpr = new CPImageSprite("title", 0, 0, 1, titleImage()):
        private var index = 1
        private val len = finalTitleText.length
        private val freq = CPEngine.fps / 4

        private val waitMs = 580
        private var lastMs = 0f

        private val clickSound = CPSound("sounds/games/gehenna/click.wav")
        private val introSong = CPSound("sounds/games/gehenna/intro song.wav")

        override def update(ctx: CPSceneObjectContext): Unit =
            setX((ctx.getCanvas.w - 74) / 2)

            if ctx.getFrameMs >= 2250 && !introSong.isPlaying then
                introSong.loop(100)

//            if index <= len && ctx.getFrameCount % freq == 0 then
            if index <= len && lastMs + waitMs <= ctx.getFrameMs then
                currTitleText = finalTitleText.substring(0, index)
                index += 1
                lastMs = ctx.getFrameMs
                this.setVisible(!this.isVisible)
                setImage(titleImage())
                //clickSound.play(0)

    addObjects(
        titleSpr
    )
