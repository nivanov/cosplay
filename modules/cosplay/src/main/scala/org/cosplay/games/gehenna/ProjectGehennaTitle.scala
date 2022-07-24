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
import CPColor.*
import CPPixel.*
import CPKeyboardKey.*
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
    private val startImg = CPImage.loadRexCsv("images/games/gehenna/StartBtn.csv").trimBg()
    private val settingsImg = CPImage.loadRexCsv("images/games/gehenna/SettingsBtn.csv").trimBg()
    private val helpImg = CPImage.loadRexCsv("images/games/gehenna/HelpBtn.csv").trimBg()

    private val fadeInShdr = CPSlideInShader.sigmoid(
        CPSlideDirection.CENTRIFUGAL,
        true,
        3000,
        GAME_BG_PX,
        onFinish = _ => TextDripShader.start()
    )

    private def titleImage(): CPImage = FIG_OGRE.render(titleText, BLOOD_RED).trimBg()

    private val titleSpr = new CPImageSprite("title", 0, 0, 1, titleImage(), shaders = Seq(fadeInShdr, TextDripShader)):
        private val clickSound = CPSound("sounds/games/gehenna/click.wav")
        private val introSong = CPSound("sounds/games/gehenna/intro song.wav")

        override def update(ctx: CPSceneObjectContext): Unit =
            setX((ctx.getCanvas.w - this.getWidth) / 2)

            if !introSong.isPlaying then
                introSong.loop(3000)

    private val startSpr = new CPImageSprite("start", 0, 30, 1, startImg, shaders = Seq(fadeInShdr)):
        override def update(ctx: CPSceneObjectContext): Unit =
            setX((ctx.getCanvas.w - this.getWidth) / 2)

    private val helpSpr = new CPImageSprite("help", 0, 30, 1, helpImg, shaders = Seq(fadeInShdr)):
        override def update(ctx: CPSceneObjectContext): Unit =
            setX(((ctx.getCanvas.w - this.getWidth) / 2) - 30 - getWidth)

    private val settingsSpr = new CPImageSprite("settings", 0, 30, 1, settingsImg, shaders = Seq(fadeInShdr)):
        override def update(ctx: CPSceneObjectContext): Unit =
            setX(((ctx.getCanvas.w - this.getWidth) / 2) + 30)

    private val underLine = new CPCanvasSprite("underLine"):
        val btns: List[CPImageSprite] = List(helpSpr, startSpr, settingsSpr)
        var btnIndex = 1

        override def render(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas

            val btn = btns(btnIndex)

            canv.drawLine(btn.getX - 1, btn.getY - 1, btn.getX + btn.getWidth, btn.getY - 1, 1, '_'&C2)
            canv.drawLine(btn.getX - 1, btn.getY, btn.getX + btn.getWidth, btn.getY, 1, '_'&C2)

            ctx.getKbEvent match
                case Some(evt) =>
                    evt.key match
                        case KEY_LO_D | KEY_RIGHT => btnIndex += 1
                        case KEY_LO_A | KEY_LEFT => btnIndex -= 1
                        case _ => ()
                case None => ()

            if btnIndex < 0 then
                btnIndex = btns.length - 1
            else if btnIndex > btns.length - 1 then
                btnIndex = 0

    addObjects(
        titleSpr,
        startSpr,
        helpSpr,
        settingsSpr,
        underLine
    )
