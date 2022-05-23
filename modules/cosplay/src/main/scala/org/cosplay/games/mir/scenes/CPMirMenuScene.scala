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

package org.cosplay.games.mir.scenes

import org.cosplay.*
import CPPixel.*
import CPColor.*
import CPArrayImage.*
import CPKeyboardKey.*
import games.mir.*
import sprites.*
import prefabs.sprites.*

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
  * Main menu scene.
  */
object CPMirMenuScene extends CPMirStarStreakSceneBase("menu", "bg1.wav"):
    private val snd = CPSound(s"$SND_HOME/crt_turn_on.wav")
    private val img = new CPArrayImage(
        prepSeq(
            """
              |   ____________________
              |,-'                    `-.
              ||    G a m e  M e n u    |.
              ||________________________|.
              ||                        |.
              ||                        |.
              ||     [C] - Continue     |.
              ||                        |.
              ||     [N] - New Game     |.
              ||     [S] - Save Game    |.
              ||     [L] - Load Game    |.
              ||                        |.
              ||     [O] - Options      |.
              ||     [T] - Tutorial     |.
              ||     [Q] - Quit         |.
              ||                        |.
              |'-.____________________.-'
              """
        ),
        (ch, _, _) => ch&&(FG, BG)
    ).trimBg()

    /**
      *
      * @param scId
      */
    private def nextScene(scId: String): Unit = snd.play(0, _ ⇒ fadeOutShdr.start(_.switchScene(scId)))

    addObjects(
        // Sprite for ghost images.
        new CPMirGhostSprite(false),
        new CPCenteredImageSprite(img = img, z = 2),
        new CPKeyboardSprite((ctx, key) ⇒ key match
            case KEY_LO_Q ⇒ snd.play(0, _ ⇒ fadeOutShdr.start(_.exitGame()))
            case KEY_LO_T ⇒ nextScene("tutorial")
            case KEY_LO_O ⇒ nextScene("options")
            case KEY_LO_N ⇒ nextScene("new_game")
            case _ ⇒ ()
        ),
        // Add full-screen shaders - order is important.
        new CPOffScreenSprite(shaders = Seq(starStreakShdr, crtShdr, fadeInShdr, fadeOutShdr))
    )
