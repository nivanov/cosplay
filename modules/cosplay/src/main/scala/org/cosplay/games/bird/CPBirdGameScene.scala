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

import org.cosplay.*
import games.*
import CPColor.*
import CPPixel.*
import CPArrayImage.*
import CPFIGLetFont.*
import CPKeyboardKey.*
import prefabs.shaders.*
import prefabs.sprites.*

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


object CPBirdGameScene extends CPScene("play", None, BG_PX):
    private val speed = 5
    private var vel = 0

    private val birdImg = CPArrayImage(
        prepSeq(
            """
              | \\
              |( ^)>
              | //
            """
        ),
        (ch, _, _) => ch&C_YELLOW
    ).trimBg()

    private val birdSpr = new CPImageSprite("bird", 15, 15, 0, birdImg):
        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            val canv = ctx.getCanvas
            println("Bird")

            ctx.getKbEvent match
                case Some(evt) =>
                    evt.key match
                        case KEY_LO_W | KEY_UP | KEY_SPACE => vel += 5
                        case _ => ()
                case None => ()


