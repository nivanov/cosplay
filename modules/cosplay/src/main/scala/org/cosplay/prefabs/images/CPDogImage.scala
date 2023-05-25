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
  * https://www.asciiart.eu
  */
object CPDogImage extends CPArrayImage(
    // 60x36
    prepSeq(
    """
      |           __.
      |        .-".'                      .--.            _..._
      |      .' .'                     .'    \       .-""  __ ""-.
      |     /  /                     .'       : --..:__.-""  ""-. \
      |    :  :                     /         ;.d$$    sbp_.-""-:_:
      |    ;  :                    : ._       :P .-.   ,"TP
      |    :   \                    \  T--...-; : d$b  :d$b
      |     \   `.                   \  `..'    ; $ $  ;$ $
      |      `.   "-.                 ).        : T$P  :T$P
      |        \..---^..             /           `-'    `._`._
      |       .'        "-.       .-"                     T$$$b
      |      /             "-._.-"               ._        '^' ;
      |     :                                    \.`.         /
      |     ;                                -.   \`."-._.-'-'
      |    :                                 .'\   \ \ \ \
      |    ;  ;                             /:  \   \ \ . ;
      |   :   :                            ,  ;  `.  `.;  :
      |   ;    \        ;                     ;    "-._:  ;
      |  :      `.      :                     :         \/
      |  ;       /"-.    ;                    :
      | :       /    "-. :                  : ;
      | :     .'        T-;                 ; ;
      | ;    :          ; ;                /  :
      | ;    ;          : :              .'    ;
      |:    :            ;:         _..-"\     :
      |:     \           : ;       /      \     ;
      |;    . '.         '-;      /        ;    :
      |;  \  ; :           :     :         :    '-.
      |'.._L.:-'           :     ;          ;    . `.
      |                     ;    :          :  \  ; :
      |                     :    '-..       '.._L.:-'
      |                      ;     , `.
      |                      :   \  ; :
      |                      '..__L.:-'
    """),
    (ch, _, _) => ch&C_SANDY_BROWN
)

/**
  * Previews image using the built-in image viewer.
  */
@main def previewDogImage(): Unit =
    CPImage.previewImage(CPDogImage.trimBg())
    sys.exit(0)
