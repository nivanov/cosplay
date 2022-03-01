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

package org.cosplay.games.pong.shaders

import org.cosplay.*

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

/**
  * Creates slight flashlight effect around the ball.
  */
object CPPongBallShader extends CPShader:
    private final val RADIUS = 6

    /** @inheritdoc */
    override def render(ctx: CPSceneObjectContext, objRect: CPRect, inCamera: Boolean): Unit =
        if ctx.isVisible then
            val canv = ctx.getCanvas
            val cx = objRect.xCenter
            val cy = objRect.yCenter
            val effRect = CPRect(cx - RADIUS * 2, cy - RADIUS, RADIUS * 4, RADIUS * 2)
            effRect.loop((x, y) => {
                if canv.isValid(x, y) then
                    // Account for character with/height ratio to make a proper circle...
                    // NOTE: we can't get the font metrics in the native ANSI terminal so
                    //       we use 1.85 as a general approximation.
                    val dx = (cx - x).abs.toFloat / 1.85
                    val dy = (cy - y).abs.toFloat
                    val r = Math.sqrt(dx * dx + dy * dy).toFloat
                    if r <= RADIUS then // Flashlight is a circular effect.
                        val zpx = canv.getZPixel(x, y)
                        val px = zpx.px
                        val newFg = px.fg.lighter(0.2f * (1.0f - r / RADIUS))
                        canv.drawPixel(px.withFg(newFg), x, y, zpx.z)
            })
