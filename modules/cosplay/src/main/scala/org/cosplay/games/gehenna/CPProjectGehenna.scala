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

package org.cosplay.games.gehenna

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

import org.cosplay.*
import org.cosplay.CPColor.*
import org.cosplay.CPFIGLetFont.*
import org.cosplay.CPPixel.*
import org.cosplay.prefabs.scenes.*

val BLUE_BLACK = CPColor("0x00000F")
val ORIG_BLUE = CPColor("0x003040")
val BLOOD_RED = CPColor("#eb1b0c")
val ORANGE_ORANGE = CPColor("#EB8B0C")
val NEON_BLUE = CPColor("#0CDCEB")
val C1 = CPColor("0xFF8000") // Orange.
val C2 = CPColor("0x33FF33") // Green (bright).
val C3 = CPColor("0x008C46") // Green (dark).
val C4 = CPColor("0xFFFFFF") // White.
val C5 = CPColor("0x66B2FF") // Light blue.
val C6 = CPColor("0x66FFB2") // Light olive.
val CS = Seq(C1, C2, C3, C4, C5, C6)

val LOGO_BG_PX = ' '&&(BLUE_BLACK, BLUE_BLACK)
val GAME_BG_PX = ' '&&(BLUE_BLACK, BLUE_BLACK) // Background pixel.

/**
  * Work in progress on demo game.
  */
object CPProjectGehenna:
    def main(args: Array[String]): Unit =
        val dim = CPDim(100, 100) // Dimension for the scenes.

        // Initialize the engine.
        CPEngine.init(CPGameInfo(
            name = "Project Gehenna",
            initDim = dim.?
        ))

        try
            CPEngine.startGame(
                new CPSlideShimmerLogoScene("logo", None, LOGO_BG_PX, CS, "title"),
                CPProjectGehennaTitle,
            )
        finally CPEngine.dispose()

        sys.exit(0)

