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

package org.cosplay.examples.shader

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
import CPPixel.*
import CPColor.*
import CPArrayImage.*
import CPKeyboardKey.*
import prefabs.scenes.*
import prefabs.shaders.*
import CPSlideDirection.*
import scala.collection.mutable

/**
  * Code demo for slide in and slide out shaders functionality.
  *
  * ### Running Example
  * One-time Git clone & build:
  * {{{
  *     $ git clone https://github.com/nivanov/cosplay.git
  *     $ cd cosplay
  *     $ mvn package
  * }}}
  * to run example:
  * {{{
  *     $ mvn -f modules/cosplay -P ex:slide_shader exec:java
  * }}}
  *
  * @see [[CPShader]]
  * @see [[CPSlideInShader]]
  * @see [[CPSlideOutShader]]
  * @note See developer guide at [[https://cosplayengine.com]]
  */
object CPSlideShaderExample:
    private val BLUE_BLACK = CPColor("0x00000F")
    private val BG_PX = ' '&&(BLUE_BLACK, BLUE_BLACK) // Background pixel.
    private val dim = CPDim(80, 40)
    private val cols = Seq(C_STEEL_BLUE1, C_LIME, C_ORANGE1)

    // In-code image creation & "painting".
    private val img = new CPArrayImage(
        prepSeq("""
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |******************************************************
            |"""),
        (_, _, _) => CPRand.randSymbol()&CPRand.rand(cols)
    )

    /**
      * Entry point for JVM runtime.
      *
      * @param args Ignored.
      */
    def main(args: Array[String]): Unit =
        // Initialize the engine.
        CPEngine.init(
            CPGameInfo(name = "Slide Shaders Demo", initDim = Option(dim)),
            System.console() == null || args.contains("emuterm")
        )

        val labelSpr = new CPImageSprite("label", 0, 0, 0, CPSystemFont.render("", C_ORANGE1, None)):
            override def update(ctx: CPSceneObjectContext): Unit =
                super.update(ctx)
                val canv = ctx.getCanvas
                // Center itself.
                setX((canv.dim.w - getImage.getWidth) / 2)
                setY(canv.dim.h - 3)

        var lastShdr: CPSlideOutShader = null
        val shdrs = mutable.Buffer.empty[CPSlideInShader | CPSlideOutShader]
        for (dir <- CPSlideDirection.values)
            val s1 = CPSlideInShader.sigmoid(dir, false, 1500, BG_PX, _ => (), lastShdr == null)
            val s2 = CPSlideOutShader.sigmoid(dir, false, 1500, BG_PX, _ => labelSpr.reset(), false)
            s1.setOnFinish(_ => {
                labelSpr.setImage(CPSystemFont.render(dir.toString, C_ORANGE1, None))
                s2.start()
            })
            if lastShdr != null then lastShdr.setOnFinish(_ => {
                labelSpr.setImage(CPSystemFont.render(dir.toString, C_ORANGE1, None))
                s1.start()
            })
            else
                labelSpr.setImage(CPSystemFont.render(dir.toString, C_ORANGE1, None))
            shdrs.append(s1)
            shdrs.append(s2)
            lastShdr = s2

        val imgSpr = new CPCenteredImageSprite(img = img, 0, shdrs.toSeq)
        val sc = new CPScene("scene", Option(dim), BG_PX, imgSpr, labelSpr, CPKeyboardSprite(KEY_LO_Q, _.exitGame()))

        // Start the game & wait for exit.
        try CPEngine.startGame(new CPFadeShimmerLogoScene("logo", Option(dim), BG_PX, cols, "scene"), sc)
        finally CPEngine.dispose()

        sys.exit(0)
