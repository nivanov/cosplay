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

package org.cosplay.games.mir.scenes

import org.cosplay.*
import CPPixel.*
import CPColor.*
import CPArrayImage.*
import CPKeyboardKey.*
import games.mir.*
import sprites.*
import prefabs.sprites.*
import scala.util.*

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
  * Load game scene.
  */
object MirLoadScene extends MirStarStreakSceneBase("load", "bg1.wav"):
    private val menuPxs = markup.process(
        s"""
           | <@ Load Game @>
           | -----------
           |
           | <%[C]%> - Load Latest (Continue)
           |
           | <%[1]%> - Load 01m Ago
           | <%[2]%> - Load 05m Ago
           | <%[3]%> - Load 15m Ago
           | <%[4]%> - Load 30m Ago
           |
           | (you can repeatedly load previous state to go beyond 30 minutes)
           |
           |
           | <%[SPACE]%>  Back To Menu
        """.stripMargin
    )
    private val menuSpr = new CPCenteredImageSprite(img = CPArrayImage(menuPxs, BG_PX).trimBg(_ == BG_PX), z = 2)

    addObjects(
        // Sprite for ghost images.
        new MirGhostSprite(false),
        menuSpr,
        new CPKeyboardSprite((_, key) => key match
            case KEY_SPACE => clickThenFade(_.switchScene("menu"))
            case _ => ()
        ),
        // Add full-screen shaders - order is important.
        new CPOffScreenSprite(shaders = Seq(starStreakShdr, crtShdr, fadeInShdr, fadeOutShdr))
    )
