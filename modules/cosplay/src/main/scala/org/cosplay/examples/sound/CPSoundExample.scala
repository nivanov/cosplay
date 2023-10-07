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
import org.cosplay.CPPixel.*
import CPStyledString.*
import org.cosplay.CPKeyboardKey.*
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
               All rights reserved.
*/

/**
  * Code example for audio functionality.
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
  *     $ mvn -f modules/cosplay -P ex:sound exec:java
  * }}}
  * 
  * @see [[CPSound]]
  * @note See developer guide at [[https://cosplayengine.com]]
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
            CPGameInfo(name = "Sound Example", initDim = dim.?),
            System.console() == null || args.contains("emuterm")
        )

        val snd = CPSound(s"sounds/examples/${Seq("bg1", "bg2", "bg3").rand}.wav")
        val panel = CPTitlePanelSprite(
            "panel",
            4, 2, 0, 77, 7,
            C_BLACK,
            "-.|'-'|.",
            C_GREEN_YELLOW,
            C_BLACK.?,
            styleStr("< ", C_GREEN_YELLOW) ++ styleStr("Audio Player", C_DARK_ORANGE3) ++ styleStr(" >", C_GREEN_YELLOW)
        )

        val c1 = C_SKY_BLUE1
        val c2 = C_LIGHT_CYAN1
        val lbl = CPStaticImageSprite(12, 5, 1, new CPArrayImage(
            styleStr("[SPACE]", c1) ++ styleStr(" Play/Pause", c2) ++
            styleStr("    [UP]/[DOWN] ", c1) ++ styleStr("Volume Up/Down", c2) ++
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

        val bgPx = '.'&&(C_GRAY2, C_GRAY1)
        val sc = new CPScene("scene", dim.?, bgPx,
            panel,
            lbl,
            ctrl,
            // Just for the initial scene fade-in effect.
            new CPOffScreenSprite(new CPFadeInShader(true, 1500, bgPx)),
            // Exit the example on 'Q' press.
            CPKeyboardSprite(_.exitGame(), KEY_LO_Q, KEY_UP_Q)
        )

        // Start the example & wait for exit.
        try CPEngine.startGame(sc)
        finally CPEngine.dispose()

        sys.exit(0)


