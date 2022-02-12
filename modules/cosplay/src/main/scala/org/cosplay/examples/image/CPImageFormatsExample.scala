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
  * Code example for image functionality.
  * 
  * @see [[CPImage]]
  * @see [[CPArrayImage]]
  * @note See developer guide at [[https://cosplayengine.com]]
  */
object CPImageFormatsExample:
    /**
      * Entry point for JVM runtime.
      *
      * @param args Ignored.
      */
    def main(args: Array[String]): Unit =
        // In-code image creation & "painting".
        val alienImg = CPArrayImage(
            prepSeq("""
                |.     .       .  .   . .   .   . .    +  .
                |  .     .  :     .    .. :. .___---------___.
                |       .  .   .    .  :.:. _".^ .^ ^.  '.. :"-_. .
                |    .  :       .  .  .:../:            . .^  :.:\.
                |        .   . :: +. :.:/: .   .    .        . . .:\
                | .  :    .     . _ :::/:               .  ^ .  . .:\
                |  .. . .   . - : :.:./.                        .  .:\
                |  .      .     . :..|:                    .  .  ^. .:|
                |    .       . : : ..||        .                . . !:|
                |  .     . . . ::. ::\(                           . :)/
                | .   .     : . : .:.|. ######              .#######::|
                |  :.. .  :-  : .:  ::|.#######           ..########:|
                | .  .  .  ..  .  .. :\ ########          :######## :/
                |  .        .+ :: : -.:\ ########       . ########.:/
                |    .  .+   . . . . :.:\. #######       #######..:/
                |      :: . . . . ::.:..:.\           .   .   ..:/
                |   .   .   .  .. :  -::::.\.       | |     . .:/
                |      .  :  .  .  .-:.+:.::.\             ..:/
                | .      -.   . . . .: .:::.:.\.           .:/
                |.   .   .  :      : ....::_:..:\   ___.  :/
                |   .   .  .   .:. .. .  .: :.:.:\       :/
                |     +   .   .   : . ::. :.:. .:.|\  .:/|
                |     .         +   .  .  ...:: ..| `--.:|
                |.      . . .   .  .  . ... :..:.."(  ..)"
                | .   .       .      :  .   .: ::/  .  .::\
                |
            """),
            (ch, _, _) => ch match
                // Static skinning.
                case '#' => ch&C_LIME.lighter(0.3)
                case ':' => ch&C_LIME.darker(0.5)
                case '+' => ch&C_LIME.darker(0.4)
                case '.' => ch&C_LIME.darker(0.6)
                case _ => ch&C_LIME
        ).trimBg().antialias(_.char != '#')

        // Load *.xp image created in REXPaint (https://www.gridsagegames.com/rexpaint/).
        val specImg = CPImage.load(
            "prefab/images/speck.xp",
            // Manually make background transparent as an example instead of calling 'trimBg()'.
            (px, _, _) => px.withBg(None)
        )

        // Load a simple *.txt image and "paint" it in the code..
        val gitarImg = CPImage.load(
            "prefab/images/gitar.txt",
            (px, _, _) => px.char match
                // Manually mark up the transparent background instead of calling 'trimBg()'.
                case 'x' => ' '&C_BLACK
                case ' ' => XRAY
                case 'o' => 'o'&C_GREY78
                case '=' => '='&C_GREY93
                case _ => px
        )

        val c1 = C_STEEL_BLUE1 // Color shortcut.
        val bgPx = CPPixel('.', C_GRAY2, C_GRAY1)
        val dim = CPDim(100, 32)

        val alienSpr = CPStaticImageSprite(2, 2, 0, alienImg)
        val alienLbl = CPLabelSprite(18, 28, 0, "In-code image", c1)

        val speckSpr = CPStaticImageSprite(70, 2, 0, specImg)
        val speckLbl = CPLabelSprite(67, 9, 0, "REXPaint *.xp image", c1)

        val gitarSpr = CPStaticImageSprite(50, 20, 0, gitarImg)
        val gitarLbl = CPLabelSprite(70, 29, 0, "*.txt image", c1)

        val sc = new CPScene("scene", Option(dim), bgPx,
            alienSpr, alienLbl,
            speckSpr, speckLbl,
            gitarSpr, gitarLbl,
            // Just for the initial scene fade-in effect.
            new CPOffScreenSprite(new CPFadeInShader(true, 1500, bgPx)),
            // Exit the game on 'q' press.
            CPKeyboardSprite(KEY_LO_Q, _.exitGame()) // Exit the game on 'q' press.
        )

        // Initialize the engine.
        CPEngine.init(
            CPGameInfo(name = "Image Formats Example", initDim = Option(dim)),
            System.console() == null || args.contains("emuterm")
        )

        // Start the game & wait for exit.
        try CPEngine.startGame(new CPLogoScene("logo", Option(dim), bgPx, List(C_LIME, C_PURPLE, C_GREY, C_STEEL_BLUE1), "scene"), sc)
        finally CPEngine.dispose()

        sys.exit(0)


