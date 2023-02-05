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

package org.cosplay.games.snake

/*
   _________            ______________
   __  ____/_______________  __ \__  /_____ _____  __
   _  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
   / /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
   \____/  \____//____/ /_/     /_/  \__,_/ _\__, /
                                            /____/

          2D ASCII JVM GAME ENGINE FOR SCALA3
              (C) 2021 Rowan Games, Inc.
                All rights reserved.
*/

import org.cosplay.games.*
import org.cosplay.{given, *}
import CPColor.*
import CPPixel.*
import CPKeyboardKey.*
import prefabs.scenes.CPFadeShimmerLogoScene

val BLUE_BLACK = CPColor("0x00000F")
val BG_PX = ' '&&(BLUE_BLACK, BLUE_BLACK) // Background pixel.
var audioOn = true // By default, the audio is ON.

/**
  * Classic snake game.
  *
  * ### Running Game
  * One-time Git clone & build:
  * {{{
  *     $ git clone https://github.com/nivanov/cosplay.git
  *     $ cd cosplay
  *     $ mvn package
  * }}}
  * to run the game:
  * {{{
  *     $ mvn -f modules/cosplay -P snake exec:java
  * }}}
  *
  * @see https://cosplayengine.com/devguide/snake_game.html
  */
object CPSnakeGame:
    /**
      * Entry point for JVM runtime.
      *
      * @param args Ignored.
      */
    def main(args: Array[String]): Unit =
        val gameInfo = CPGameInfo(name = "Ascii Snake")

        // Initialize the engine.
        CPEngine.init(gameInfo, System.console() == null || args.contains("emuterm"))

        // Start the game & wait for exit.
        try
            CPEngine.startGame(
                new CPFadeShimmerLogoScene("logo", None, BG_PX, CS, "title"),
                CPSnakeTitleScene
            )
        finally CPEngine.dispose()

        sys.exit(0)
