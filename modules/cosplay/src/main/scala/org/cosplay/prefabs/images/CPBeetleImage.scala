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

import org.cosplay.*
import org.cosplay.CPColor.*
import org.cosplay.CPArrayImage.*
import org.cosplay.CPPixel.*

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
 * https://www.asciiart.eu/
 */
object CPBeetleImage extends CPArrayImage(
    // 23x14
    prepSeq("""
      |    .--.       .--.
      |_  `    \     /    `  _
      | `\.===. \e^e/ .===./`
      |        \bf"fc/
      |     ,  asy2ksa  ,
      |    / `\a;hdh'a/` \
      |   /    a::cssa    \
      |.-' ,-'`a:::;sa`'-, '-.
      |    |   a::::ca   |
      |    |   a::::;a   |
      |    |   c::::bb   |
      |    |    fw:bbq   |
      |   .'             `.
      |_,'                 `,_
    """),
    (ch, _, _) => ch match
        case ' ' | 's' => XRAY
        case c @ (':' | ';' | 'y' | '2' | 'k' | '^' | '"') => c&C_DARK_ORANGE3
        case 'a' => '|'&C_DARK_ORANGE3
        case 'b' => '/'&C_DARK_ORANGE3
        case 'c' => '\\'&C_DARK_ORANGE3
        case 'd' => ','&C_DARK_ORANGE3
        case 'e' => 'e'&C_RED3
        case 'f' => '`'&C_DARK_ORANGE3
        case 'h' => '-'&C_DARK_ORANGE3
        case 'w' => '.'&C_DARK_ORANGE3
        case 'q' => '\''&C_DARK_ORANGE3
        case c => c&C_WHITE
)

/**
  * Previews image using the built-in image viewer.
  */
@main def previewBeetleImage(): Unit =
    CPImage.previewImage(CPBeetleImage)
    sys.exit(0)
