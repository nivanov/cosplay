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

package org.cosplay.examples.layout

import org.cosplay.*
import org.cosplay.CPColor.*
import org.cosplay.CPPixel.*
import org.cosplay.CPFIGLetFont.*
import org.cosplay.CPKeyboardKey.*
import org.cosplay.CPStyledString.*
import org.cosplay.prefabs.shaders.*
import org.cosplay.prefabs.sprites.*

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
  * Code example for built-in text input functionality.
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
  *     $ mvn -f modules/cosplay -P ex:layout exec:java
  * }}}
  *
  * @see [[CPLayoutSprite]]
  * @see [[CPDynamicSprite]]
  * @note See developer guide at [[https://cosplayengine.com]]
  */
object CPLayoutExample:
    /**
      * Entry point for JVM runtime.
      *
      * @param args Ignored.
      */
    def main(args: Array[String]): Unit =
        val termDim = CPDim(100, 40)

        def mkPanel(name: String, w: Int, h: Int, z: Int): CPDynamicSprite =
            CPTitlePanelSprite(
                name,
                0, 0, w, h, z,
                C_BLACK,
                "-.|'-'|.",
                C_GREEN_YELLOW,
                styleStr(name, C_DARK_ORANGE3)
            )

        val bgPx = ' '&&(C_GRAY2, C_BLACK)
        val sc = new CPScene("scene", termDim.?, bgPx,
            // Just for the initial scene fade-in effect.
            new CPOffScreenSprite(new CPFadeInShader(true, 1500, bgPx)),
            CPKeyboardSprite(KEY_LO_Q, _.exitGame()),
            mkPanel("Panel-1", 60, 30, 0),
            mkPanel("Panel-2", 15, 5, 1),
            mkPanel("Panel-3", 15, 5, 1),
            mkPanel("Panel-4", 15, 5, 1),
            mkPanel("Panel-5", 15, 5, 1),
            mkPanel("Panel-6", 15, 5, 1),
            mkPanel("Panel-7", 10, 5, 1),
            new CPImageSprite("img", 0, 0, 1, FIG_OGRE.render("CosPlay", C_WHITE).trimBg()),
            // Dynamic layout specification.
            CPLayoutSprite("layout",
                """
                  | // This is the main panel centered on the screen.
                  | Panel-1 = x: center(), y: center();
                  |
                  | // These panels are placed in the 4 corners of the panel 1.
                  | Panel-2 = off: [1, 0], x: left(Panel-1), y: top(Panel-1);
                  | Panel-3 = off: [-1, 0], x: right(Panel-1), y: top(Panel-1);
                  | Panel-4 = off: [1, 0], x: left(Panel-1), y: bottom(Panel-1);
                  | Panel-5 = off: [-1, 0], x: right(Panel-1), y: bottom(Panel-1);
                  |
                  | // Panels 6 and 7 go after each other.
                  | Panel-6 = x: after(Panel-2), y: below(Panel-2);
                  | Panel-7 = x: after(Panel-6), y: same(Panel-6);
                  |
                  | // Centered image with 2-row offset.
                  | img = off: [0, 2], x: center(Panel-1), y: center(Panel-1);
                  |""".stripMargin
            )
        )

        // Initialize the engine.
        CPEngine.init(
            CPGameInfo(name = "Layout Example - [Q] to Exit", initDim = termDim.?),
            System.console() == null || args.contains("emuterm")
        )

        // Start the game & wait for exit.
        try CPEngine.startGame(sc)
        finally CPEngine.dispose()

        sys.exit(0)



