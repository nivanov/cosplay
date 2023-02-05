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

package org.cosplay.examples.canvas

import org.cosplay.{given, *}
import scala.language.implicitConversions
import CPColor.*
import CPCanvas.ArtLineStyle.*
import CPArrayImage.*
import CPPixel.*
import CPKeyboardKey.*
import CPStyledString.*
import CPPixel.*
import org.cosplay.prefabs.scenes.CPFadeShimmerLogoScene
import org.cosplay.prefabs.shaders.CPFadeInShader

/*
   _________            ______________
   __  ____/_______________  __ \__  /_____ _____  __
   _  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
   / /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
   \____/  \____//____/ /_/     /_/  \__,_/ _\__, /
                                            /____/

          2D ASCII GAME ENGINE FOR SCALA3
            (C) 2021 Rowan Games, Inc.
               All rights reserved.
*/

/**
  * Code example for canvas drawing functionality.
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
  *     $ mvn -f modules/cosplay -P ex:canvas exec:java
  * }}}
  *
  * @see [[CPCanvas]]
  * @note See developer guide at [[https://cosplayengine.com]]
  */
object CPCanvasExample:
    /**
      * Entry point for JVM runtime.
      *
      * @param args Ignored.
      */
    def main(args: Array[String]): Unit =
        val alienImg = new CPArrayImage(
            // 12x9
            prepSeq("""
              |    .  .
              |     \/
              |    (@@)
              | g/\_)(_/\e
              |g/\(=--=)/\e
              |    //\\
              |   _|  |_
            """),
            (ch, _, _) => ch&C_LIME
        ).trimBg()

        val bgPx = '.'&&(C_GRAY2, C_GRAY1)
        val COLORS = CS_X11_GREENS ++ CS_X11_CYANS ++ CS_X11_REDS ++ CS_X11_BLUES
        val fgf = (_: Char) => COLORS.rand
        val pxs = CPPixel.seq("~!@#$%^&*()[]:;'<>?", fgf, _ => None)

        val dim = CPDim(100.w, 40.h)

        // Demo sprite that illustrates working with 'canvas'.
        val drawSpr = new CPCanvasSprite():
            override def render(ctx: CPSceneObjectContext): Unit =
                val canv = ctx.getCanvas

                // Color shortcuts.
                val c1 = C_STEEL_BLUE1
                val c2 = C_INDIAN_RED
                val c3 = C_LIGHT_STEEL_BLUE

                // Draw image.
                canv.drawImage(alienImg, 2.x, 2.y, 0.z)
                canv.drawString(5.x, (alienImg.getHeight + 4).y, 0.z, "Image", c1)

                // Draw a basic polyline shape.
                canv.drawPolyline(Seq(
                    15 -> 2,
                    25 -> 3,
                    30 -> 15,
                    22 -> 17,
                    18 -> 5,
                    15 -> 2,
                ), 100, '*'&c3)

                // Fill this shape with random color on each frame.
                canv.fill(17, 3, _.char == '*', (_, _) => pxs.rand)
                canv.drawString(18.x, 19.y, 0.z, "Filled shape", c1)

                // Draw ASCII-art style polyline shape.
                canv.drawArtPolyline(Seq(
                    32 -> 2,
                    42 -> 3,
                    47 -> 15,
                    39 -> 17,
                    35 -> 5,
                    32 -> 2,
                ), 100, _.px.withFg(c3), ART_SMOOTH)

                // Fill this ASCII-art shape with dark grey 'x'.
                canv.fill(35.x, 4.y, _.px != bgPx, (_, _) => 'x'&C_GRAY3)
                canv.drawString(34.x, 19.y, 0.z, "Filled smooth shape", c1)

                // Draw antialias circle.
                val circRect = canv.drawCircle(75.x, 12.y, 10, 0.z, 2f, 1f, 0.5f, true, 'x'&c2)
                canv.antialias(circRect, _.char != 'x')
                // Draw a center point (z-index 1).
                canv.drawPixel('X'&c1, 75, 12, z = 1)
                // Draw a moving radius arm.
                canv.drawArtVector(75.x, 12.y, ctx.getFrameCount.toFloat % 360, 9, 0.z, 2f, 1f, _.px.withFg(COLORS.rand), ART_SMOOTH)
                canv.drawString(68.x, 24.y, 0.z, "Antialias circle", c1)

                // Flickering colors without styled string...
                val flickImg = new CPArrayImage(
                    "Flickering String Example".map(_&COLORS.rand)
                )
                canv.drawImage(flickImg, 2.x, 24.y, 0.z)

                // Draw a panel with titled border.
                canv.fillRect(3.x, 27.y, 40.x, 37.y, 0.z, Seq(' '&C_BLACK))
                canv.drawSimpleBorder(
                    2.x, 26.y, 41.x, 38.y, 0.z,
                    '|'&c3, '-'&c3, '+'&c1,
                    styleStr("/ ", c3) ++ styleStr("Titled Panel", C_INDIAN_RED) ++ styleStr(" /", c3),
                    5.x, 26.y
                )

        // Create the scene (exit the game on 'q' press).
        val sc = new CPScene("scene", dim.?, bgPx,
            drawSpr,
            new CPOffScreenSprite(new CPFadeInShader(true, 1500, bgPx)), // Just shader for the entire screen.
            CPKeyboardSprite(KEY_LO_Q, _.exitGame()) // Exit the game on 'Q' press.
        )

        // Initialize the engine.
        CPEngine.init(
            CPGameInfo(name = "Canvas Example", initDim = dim.?),
            System.console() == null || args.contains("emuterm")
        )

        try
            // Start the game & wait for exit.
            CPEngine.startGame(
                new CPFadeShimmerLogoScene(
                    "logo",
                    dim.?,
                    bgPx,
                    Seq(C_STEEL_BLUE1, C_INDIAN_RED, C_LIGHT_STEEL_BLUE),
                    "scene"
                ),
                sc
            )
        finally CPEngine.dispose()

        sys.exit(0)


