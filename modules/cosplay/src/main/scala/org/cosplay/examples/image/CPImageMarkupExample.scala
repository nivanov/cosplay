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

package org.cosplay.examples.image

import org.cosplay.*
import CPPixel.*
import CPArrayImage.*
import CPColor.*
import CPKeyboardKey.*
import prefabs.scenes.*
import prefabs.shaders.*

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
  * Code example for markup-based image functionality.
  *
  * ### Running Example
  * One-time Git clone & build using Maven or SBT:
  * {{{
  *     $ git clone https://github.com/nivanov/cosplay.git
  *     $ cd cosplay
  *
  *     $ mvn package
  *     $ sbt package
  * }}}
  * to run example using Maven or SBT:
  * {{{
  *     $ mvn -f modules/cosplay -P ex:image_markup exec:java
  *     $ sbt "project cosplay; runMain org.cosplay.examples.image.CPImageMarkupExample"
  * }}}
  *
  * @see [[CPImage]]
  * @see [[CPArrayImage]]
  * @note See developer guide at [[https://cosplayengine.com]]
  */
object CPImageMarkupExample:
    /**
      * Entry point for JVM runtime.
      *
      * @param args Ignored.
      */
    def main(args: Array[String]): Unit =
        val bgPx = ' '&&(C_GRAY2, C_GRAY1)

        val markup = new CPImageMarkup(
            C_GREEN,
            Option(C_GRAY1),
            List(
                ("<%", "%>", (ch: Char) => CPPixel(ch, C_WHITE, Option(C_RED))),
                (" a", "%>>", (ch: Char) => CPPixel(ch, C_WHITE, Option(C_RED)))
            )
        )

        val markupImg = CPImage.markupImage(
            prepSeq(
                """
                  |Default text <% WHITE ON RED %> another default text.
                  |"""),
            markup
        )

        val sc = new CPScene("scene", None, bgPx,
            new CPCenteredImageSprite(img = markupImg, z = 0),
            // Just for the initial scene fade-in effect.
            new CPOffScreenSprite(new CPFadeInShader(true, 1500, bgPx)),
            // Exit the game on 'Q' press.
            CPKeyboardSprite(KEY_LO_Q, _.exitGame())
        )

        // Initialize the engine.
        CPEngine.init(
            CPGameInfo(name = "Image Formats Example"),
            System.console() == null || args.contains("emuterm")
        )

        // Start the game & wait for exit.
        try CPEngine.startGame(new CPFadeShimmerLogoScene("logo", None, bgPx, List(C_LIME, C_PURPLE, C_GREY, C_STEEL_BLUE1), "scene"), sc)
        finally CPEngine.dispose()

        sys.exit(0)


