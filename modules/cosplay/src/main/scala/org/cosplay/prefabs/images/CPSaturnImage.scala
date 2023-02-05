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

package org.cosplay.prefabs.images

import org.cosplay.{given, *}
import CPColor.*
import CPArrayImage.*
import CPPixel.*

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
  * Art by Bob Allison
  * https://www.incredibleart.org/links/ascii/bob_allison_ascii.html
  */
object CPSaturnImage extends CPArrayImage(
    // 49x22
    prepSeq("""
      |                                             ___
      |                                          ,o88888
      |                                       ,o8888888'
      |                 ,:o:o:oooo.        ,8O88Pd8888"
      |             ,.::.::o:ooooOoOoO. ,oO8O8Pd888'"
      |           ,.:.::o:ooOoOoOO8O8OOo.8OOPd8O8O"
      |          , ..:.::o:ooOoOOOO8OOOOo.FdO8O8"
      |         , ..:.::o:ooOoOO8O888O8O,COCOO"
      |        , . ..:.::o:ooOoOOOO8OOOOCOCO"
      |         . ..:.::o:ooOoOoOO8O8OCCCC"o
      |            . ..:.::o:ooooOoCoCCC"o:o
      |            . ..:.::o:o:,cooooCo"oo:o:
      |         `   . . ..:.:cocoooo"'o:o:::'
      |         .`   . ..::ccccoc"'o:o:o:::'
      |        :.:.    ,c:cccc"':.:.:.:.:.'
      |      ..:.:"'`::::c:"'..:.:.:.:.:.'
      |    ...:.'.:.::::"'    . . . . .'
      |   .. . ....:."' `   .  . . ''
      | . . . ...."'
      | .. . ."'
      |.
    """),
    (ch, _, _) => ch&C_WHITE
)

/**
  * Previews image using the built-in image viewer.
  */
@main def previewSaturnImage(): Unit =
    CPImage.previewImage(CPSaturnImage.trimBg())
    sys.exit(0)

