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

package org.cosplay.examples.sound

import org.cosplay.*
import org.cosplay.CPColor.*
import org.cosplay.CPStyledString.*
import org.cosplay.CPKeyboardKey.*
import org.cosplay.examples.utils.*
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
  * Code example for audio functionality.
  * 
  * @see [[CPSound]]
  */
object CPSoundExample:
    /**
      * Entry point for JVM runtime.
      *
      * @param args Ignored.
      */
    def main(args: Array[String]): Unit =
        val dim = CPDim(85, 11)

        // Initialize the engine.
        CPEngine.init(
            CPGameInfo(
                name = "Sound Example",
                devName = "(C) 2021 Rowan Games, Inc.",
                initDim = Option(dim)
            ),
            System.console() == null || args.contains("emuterm")
        )

        try
            val snd = CPSound(src = s"sounds/examples/${CPRand.rand(Seq("bg1", "bg2", "bg3"))}.wav")

            val panel = CPPanelSprite(4, 2, 80, 8, 0, "Audio Player")

            val c1 = C_SKY_BLUE1
            val c2 = C_LIGHT_CYAN1
            val lbl = CPStaticImageSprite(12, 5, 1, new CPArrayImage(
                styleStr("[Space]", c1) ++ styleStr(" Play/Pause", c2) ++
                styleStr("    [Up]/[Down] ", c1) ++ styleStr("Volume Up/Down", c2) ++
                styleStr("    [R] ", c1) ++ styleStr("Rewind", c2)
            ).trimBg())

            val ctrl = new CPOffScreenSprite:
                private val FADE_MS = 500L

                override def update(ctx: CPSceneObjectContext): Unit =
                    if ctx.getSceneFrameCount == 0 then snd.play(FADE_MS) // Auto-play on start.
                    ctx.getKbEvent match
                        case Some(evt) => evt.key match
                            case KEY_SPACE => snd.toggle(FADE_MS)
                            case KEY_UP => snd.adjustVolume(.1f)
                            case KEY_DOWN => snd.adjustVolume(-.1f)
                            case KEY_LO_R => snd.rewind()
                            case _ => ()
                        case None => ()

            val bgPx = CPPixel('.', C_GRAY2, C_GRAY1)
            val sc = new CPScene("scene", Some(dim), bgPx,
                panel,
                lbl,
                ctrl,
                // Just for the initial scene fade-in effect.
                new CPOffScreenSprite(new CPFadeInShader(true, 1500, bgPx)),
                // Exit the game on 'q' press.
                CPKeyboardSprite(KEY_LO_Q, _.exitGame()) // ¯\_(ツ)_/¯
            )

            // Start the game & wait for exit.
            CPEngine.startGame(sc)
        finally
            CPEngine.dispose()

        sys.exit(0)


