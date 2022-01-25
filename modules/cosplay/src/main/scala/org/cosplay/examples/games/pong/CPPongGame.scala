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

package org.cosplay.examples.games.pong

import org.cosplay.CPColor.*
import org.cosplay.*
import org.cosplay.prefabs.scenes.CPLogoScene

/*
   _________            ______________
   __  ____/_______________  __ \__  /_____ _____  __
   _  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
   / /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
   \____/  \____//____/ /_/     /_/  \__,_/ _\__, /
                                            /____/

          2D ASCII JVM GAME ENGINE FOR SCALA3
              (C) 2021 Rowan Games, Inc.
                ALl rights reserved.
*/

val bgPx = CPPixel('.', C_GRAY2, C_GRAY1)

/**
 *
 */
object CPPongGame:
    /**
     * Entry point for JVM runtime.
     *
     * @param args Ignored.
     */
    def main(args: Array[String]): Unit =
        val gameInfo = CPGameInfo(
            name = "Classic Pong",
            devName = "(C) 2021 Rowan Games, Inc."
        )

        // Initialize the engine.
        CPEngine.init(gameInfo, System.console() == null || args.contains("emuterm"))

        // Start the game & wait for exit.
        try CPEngine.startGame(CPPongTitleScene, CPPongGameScene)
        finally CPEngine.dispose()

        sys.exit(0)


