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
import prefabs.shaders.*
import org.cosplay.CPFIGLetFont.*
import org.cosplay.CPCanvas.*
import org.cosplay.CPDim.*

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

object CPPongTitleScene extends CPScene("title", None, bgPx):
    private val logoImg = FIG_DOH.render("Pong", C_WHITE).skin(
        (px, _, _) => px.char match
            case ':' => px.withFg(C_GREY70)
            case _ => px
    ).trimBg()

    private val helpImg4 = FIG_DOOM.renderMulti("[Q] - Quit", C_WHITE).trimBg()
    private val helpImg3 = FIG_DOOM.renderMulti("[S] - Down", C_WHITE).trimBg()
    private val helpImg2 = FIG_DOOM.renderMulti("[W] - Up", C_WHITE).trimBg()
    private val helpImg1 = FIG_DOOM.renderMulti("[ENTER] - Start", C_WHITE).trimBg()

    private val logoW = logoImg.getWidth
    private val logoH = logoImg.getHeight
    private val logoSpr = new CPImageSprite("logoSpr", 0, 0, 0, logoImg, shaders = Seq(CPFadeInShader(true, 1500, bgPx))):
        override def update(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            setX((canv.dim.width - logoImg.getWidth) / 2)
            setY((canv.dim.height - logoImg.getHeight) / 20)


    private val helpSpr4 = new CPImageSprite("helpSpr4", 0, 0, 0, helpImg4, shaders = Seq(CPFadeInShader(true, 1500, bgPx))):
        override def update(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            setX((canv.dim.width - helpImg4.getWidth) / 2)
            setY((canv.dim.height - helpImg4.getHeight))

    private val helpSpr3 = new CPImageSprite("helpSpr3", 0, 0, 0, helpImg3, shaders = Seq(CPFadeInShader(true, 1500, bgPx))):
        override def update(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            setX((canv.dim.width - helpImg3.getWidth) / 2)
            setY((canv.dim.height - helpImg3.getHeight) / 4)

    private val helpSpr2 = new CPImageSprite("helpSpr2", 0, 0, 0, helpImg2, shaders = Seq(CPFadeInShader(true, 1500, bgPx))):
        override def update(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            setX((canv.dim.width - helpImg2.getWidth) / 2)
            setY((canv.dim.height - helpImg2.getHeight) / 8)

    private val helpSpr1 = new CPImageSprite("helpSpr1", 0, 0, 0, helpImg1, shaders = Seq(CPFadeInShader(true, 1500, bgPx))):
        override def update(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            setX((canv.dim.width - helpImg1.getWidth) / 2)
            setY((canv.dim.height - helpImg1.getHeight) / 12)


    // Add scene objects...
    addObject(logoSpr)
    addObject(helpSpr4)
    addObject(helpSpr3)
    addObject(helpSpr2)
    addObject(helpSpr1)
