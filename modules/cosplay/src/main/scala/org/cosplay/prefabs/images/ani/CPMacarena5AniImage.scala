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

package org.cosplay.prefabs.images.ani

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
  * https://www.hellers.ws/images/geek/MrAsciiDoesTheMacarena.txt
  */
object CPMacarena5AniImage extends CPArrayImage(
    // 3x3
    prepSeq(
        """
          | o> o  o  o  o  o  o  o
          | \  x </ <|></><\><)> |\
          |/<  >\/<  >\/<  >\ >> L
        """
    ),
    (ch, _, y) => ch match
        case ' ' => XRAY
        case _ if y != 2 => ch&C_WHITE
        case _ if y == 2 => ch&C_DARK_ORANGE3
)

/**
  * Previews image using the built-in image viewer.
  */
@main def previewMacarena5AniImage(): Unit =
    CPImage.previewAnimation(CPMacarena5AniImage.trimBg().split(3, 3))
    sys.exit(0)