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

package org.cosplay.games.bird

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

import org.cosplay.*
import CPColor.*
import CPKeyboardKey.*
import CPPixel.*
import org.cosplay.CPArrayImage.*
import org.cosplay.CPFIGLetFont.*
import prefabs.images.ani.*
import prefabs.scenes.*
import prefabs.shaders.*

object CPBirdTitleScene extends CPScene("title", None, BG_PX):
    private val logoImg = FIG_BELL.render("Ascii Bird", C_WHITE).skin(
        (px, _, _) => px.char match
            //case '$' => px.withFg(C5)
            case _ => px.withFg(C_YELLOW)
    ).trimBg()
    private val helpImg = CPArrayImage(
        prepSeq(
            """
              |       GET AS FAR AS YOU CAN
              |      ~~~~~~~~~~~~~~~~~~~~~~~
              |
              |         [SPACE] - JUMP
              |
              |         [ENTER] - Play
              |         [Q]   -   Quit
              |
              |
              |
              |Copyright (C) 2022 Rowan Games, Inc
            """),
        (ch, _, y) =>
            if y >= 21 then ch&C_YELLOW
            else
                ch match
                    case _ => ch&C_GREEN
    ).trimBg()

    // Add scene objects...
    addObjects(
        CPImageSprite(xf = c => (c.w - logoImg.w) / 2, c => Math.max(0, c.h / 2 - logoImg.h - 1), 0, logoImg),
        CPImageSprite(xf = c => (c.w - helpImg.w) / 2, c => Math.max(0, c.h / 2 + 1), 0, helpImg),

        CPKeyboardSprite(KEY_LO_Q, _.exitGame()), // Exit on 'Q' press.
        CPKeyboardSprite(KEY_ENTER, _.switchScene("play"))// Transition to the next scene on 'Enter' press.
    )

    override def onActivate(): Unit =
        super.onActivate()

    override def onDeactivate(): Unit =
        super.onDeactivate()

