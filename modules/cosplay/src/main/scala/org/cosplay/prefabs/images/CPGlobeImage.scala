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
object CPGlobeImage extends CPArrayImage(
    // 21x10
    prepSeq("""
      |       ______
      |    ,-:::b;de`:-,
      |  .'h;ne;::dkd;;e'.
      | /;;;;da::::e::nf;h\
      ||:df;;(f:::::a;;;;bf|
      ||kg::fb;;g:::b;;;;a:|
      ||:::::(;;;;e::.fb;;d|
      | \:::::c;;d:::::fhd/
      |  `::::;a:::::::::'
      |    `'-.:::::.-'`
  """),
    (ch, _, _) => ch match
        case ' ' => XRAY
        case c @ (';' | '(') => c&C_LIGHT_GOLDEN_ROD1
        case _ @ 'a' => '/'&C_LIGHT_GOLDEN_ROD1
        case _ @ 'b' => '\\'&C_LIGHT_GOLDEN_ROD1
        case _ @ 'c' => '|'&C_LIGHT_GOLDEN_ROD1
        case _ @ 'd' => '\''&C_LIGHT_GOLDEN_ROD1
        case _ @ 'e' => ','&C_LIGHT_GOLDEN_ROD1
        case _ @ 'f' => '`'&C_LIGHT_GOLDEN_ROD1
        case _ @ 'g' => '.'&C_LIGHT_GOLDEN_ROD1
        case _ @ 'h' => '-'&C_LIGHT_GOLDEN_ROD1
        case _ @ 'k' => ':'&C_LIGHT_GOLDEN_ROD1
        case _ @ 'n' => '_'&C_LIGHT_GOLDEN_ROD1
        case c => c&C_DARK_BLUE
)

/**
  * Previews image using the built-in image viewer.
  */
@main def previewGlobeImage(): Unit =
    CPImage.previewImage(CPGlobeImage)
    sys.exit(0)