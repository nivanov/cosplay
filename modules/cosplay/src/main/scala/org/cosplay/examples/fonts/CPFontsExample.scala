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

package org.cosplay.examples.fonts

import org.cosplay.*
import CPColor.*
import CPFIGLetFont.*
import CPKeyboardKey.KEY_LO_Q
import org.cosplay.prefabs.scenes.CPLogoScene
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
               ALl rights reserved.
*/

/**
  * Code example for the built-in FIGLet font functionality.
  *
  * @see [[CPFont]]
  * @see [[CPSystemFont]]
  * @see [[CPFIGLetFont]]
  */
object CPFontsExample:
    /**
      * Entry point for JVM runtime.
      *
      * @param args Ignored.
      */
    def main(args: Array[String]): Unit =
        val dim = CPDim(100, 36)

        // Initialize the engine.
        CPEngine.init(
            CPGameInfo(name = "Fonts Example", initDim = Some(dim)),
            System.console() == null || args.contains("emuterm")
        )

        try
            val bgPx = CPPixel('.', C_GRAY2, C_GRAY1)

            val sysFontImg1 = CPSystemFont.renderMulti(
                """
                |Rendering multi-line text
                |using system font with CENTER (0) alignment (default).
                |""".stripMargin, C_WHITE).trimBg()
            val sysFontImg2 = CPSystemFont.renderMulti(
                """
                |Rendering multi-line text
                |using system font with LEFT (-1) alignment.
                |""".stripMargin, C_WHITE, align = -1).trimBg()
            val sysFontImg3 = CPSystemFont.renderMulti(
                """
                |Rendering multi-line text
                |using system font with RIGHT (1) alignment.
                |""".stripMargin, C_WHITE, align = 1).trimBg()

            val sc = new CPScene("scene", Some(dim), bgPx,
                // Same text with different alignment.
                CPStaticImageSprite((dim.width - sysFontImg1.getWidth) / 2, 1, 4, sysFontImg1),
                CPStaticImageSprite((dim.width - sysFontImg2.getWidth) / 2, 5, 4, sysFontImg2),
                CPStaticImageSprite((dim.width - sysFontImg3.getWidth) / 2, 9, 4, sysFontImg3),

                // Same font with different spacing.
                CPStaticImageSprite(30, 14, 4, FIG_OGRE.render("CosPlay", C_WHITE).trimBg()),
                CPStaticImageSprite(30, 20, 4, FIG_OGRE.withKerning().render("CosPlay", C_LIGHT_STEEL_BLUE).trimBg()),
                CPStaticImageSprite(30, 27, 4, FIG_OGRE.withFullWidth().render("CosPlay", C_LIGHT_CORAL).trimBg()),

                // Just for the initial scene fade-in effect.
                new CPOffScreenSprite(Seq(new CPFadeInShader(true, 500, bgPx))),
                // Exit the game on 'q' press.
                CPKeyboardSprite(KEY_LO_Q, _.exitGame()) // ¯\_(ツ)_/¯
            )

            // Start the game & wait for exit.
            CPEngine.startGame(
                new CPLogoScene(
                    "logo",
                    Some(dim),
                    bgPx,
                    Seq(C_WHITE, C_LIGHT_STEEL_BLUE, C_DARK_ORANGE3),
                    "scene"
                ),
                sc
            )
        finally
            CPEngine.dispose()

        sys.exit(0)


