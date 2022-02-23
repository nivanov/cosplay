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

package org.cosplay.examples.video

import org.cosplay.*
import org.cosplay.CPColor.*
import org.cosplay.CPKeyboardKey.*
import org.cosplay.CPStyledString.styleStr
import org.cosplay.prefabs.shaders.*

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
  * Code example for video functionality.
  *
  * @see [[CPVideo]]
  * @see [[CPVideoSprite]]
  * @see [[CPVideoSpriteListener]]
  * @note Use [[https://www.ffmpeg.org/]] to convert video into separate JPEG images.
  * @note Use [[https://github.com/cslarsen/jp2a]] or similar to convert individual JPEG into ASCII.
  * @note See developer guide at [[https://cosplayengine.com]]
  */
object CPVideoExample:
    /**
      * Entry point for JVM runtime.
      *
      * @param args Ignored.
      */
    def main(args: Array[String]): Unit =
        val c1 = C_LIGHT_GREEN
        val c2 = C_ORANGE1
        val ctrlImg = new CPArrayImage(
            styleStr("[Space]", c1) ++ styleStr(" Play|Pause    ", c2) ++
            styleStr("[R]", c1) ++ styleStr(" Rewind    ", c2) ++
            styleStr("[Q]", c1) ++ styleStr(" Quit    ", c2) ++
            styleStr("[Ctrl-L]", c1) ++ styleStr(" Log    ", c2) ++
            styleStr("[Ctrl-Q]", c1) ++ styleStr(" FPS Overlay", c2)
        ).trimBg()
        val vidDim = CPVideoClip.getFrameDim
        val ctrlDim = ctrlImg.getDim
        val dim = CPDim((vidDim.w + 8).max(ctrlDim.w + 4), vidDim.h + 8)
        val vidSpr = new CPVideoSprite(vid = CPVideoClip, 4, 2, 0, 30, loop = true, collidable = false, autoPlay = true)
        val bgPx = CPPixel('.', C_GRAY2, C_GRAY1)
        // Create the scene.
        val sc = new CPScene("scene", Option(dim), bgPx,
            vidSpr,
            new CPKeyboardSprite((_, key) => key match // Separate keyboard control (as an example).
                case KEY_LO_R => vidSpr.rewind()
                case KEY_SPACE => vidSpr.toggle()
                case _ => ()
            ),
            new CPStaticImageSprite((dim.w - ctrlDim.w) / 2, dim.h - 4, 0, ctrlImg), // Help label.
            // Just for the initial scene fade-in effect.
            new CPOffScreenSprite(new CPFadeInShader(true, 1500, bgPx)),
            CPKeyboardSprite(KEY_LO_Q, _.exitGame()) // Exit the game on 'q' press.
        )

        // Initialize the engine.
        CPEngine.init(
            CPGameInfo(name = "Video Example", initDim = Option(dim)),
            System.console() == null || args.contains("emuterm")
        )

        // Start the game & wait for exit.
        try CPEngine.startGame(sc)
        finally CPEngine.dispose()

        sys.exit(0)


