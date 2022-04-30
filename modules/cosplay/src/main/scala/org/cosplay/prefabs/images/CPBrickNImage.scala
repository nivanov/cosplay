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
import CPColor.*
import CPPixel.*
import CPArrayImage.*

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
  * https://www.asciiart.eu/
  */
object CPBrickNImage extends CPArrayImage(
    // 8x3
    prepSeq("""
      |^^"^^"^^
      |___[___[
      |_[___{__
    """),
    (ch, _, _) => ch match
        case '^' => '^'&&(C_DARK_OLIVE_GREEN1, C_GREEN_YELLOW)
        case '"' => '"'&&(C_GREEN1, C_GREEN_YELLOW)
        case '{' => '['&&(C_SANDY_BROWN, C_DARK_ORANGE3)
        case '-' => '_'&&(C_SANDY_BROWN, C_DARK_ORANGE3)
        case c ⇒ c&&(C_MAROON, C_DARK_ORANGE3)
)

/**
  * Previews image using the built-in image viewer.
  *
  * @param args Ignored.
  */
@main def main(args: String*): Unit =
    CPImage.previewImage(CPBrickNImage, emuTerm = !args.contains("systerm"))
    sys.exit(0)