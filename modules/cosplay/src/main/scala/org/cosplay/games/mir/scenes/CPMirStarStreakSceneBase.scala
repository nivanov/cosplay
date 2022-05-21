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

import org.cosplay.*
import games.mir.*
import prefabs.shaders.*

/**
  *
  * @param id ID of the scene.
  * @param bgSndFile Background audio file name.
  */
abstract class CPMirStarStreakSceneBase(id: String, bgSndFile: String) extends CPMirCrtSceneBase(id, bgSndFile):
    private val colors = Seq(FG)

    protected val starStreakShdr: CPStarStreakShader = CPStarStreakShader(
        true,
        BG,
        Seq(
            CPStarStreak('.', colors, 0.025, 30, (-.3f, 0f), 0),
            CPStarStreak('.', colors, 0.015, 25, (-.7f, 0f), 0),
            CPStarStreak('_', colors, 0.005, 50, (-1f, 0f), 0)
        ),
        skip = (zpx, _, _) ⇒ zpx.z >= 1
    )

    // Make sure to call 'super(...)'.
    override def onActivate(): Unit =
        super.onActivate()
        starStreakShdr.start()

    // Make sure to call 'super(...)'.
    override def onDeactivate(): Unit =
        super.onDeactivate()
        starStreakShdr.stop()


