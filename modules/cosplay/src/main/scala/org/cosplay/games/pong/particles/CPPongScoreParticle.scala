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

package org.cosplay.games.pong.particles

import org.cosplay.games.pong.*
import org.cosplay.*
import CPColor.*

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
  *
  * @param initX Initial x-coordinate.
  * @param initY Initial y-coordinate.
  * @param dx X-speed.
  * @param dy Y-speed.
  */
class CPPongScoreParticle(initX: Int, initY: Int, dx: Float, dy: Float) extends CPParticle:
    private val COLORS = CS_X11_REDS ++ CS_X11_ORANGES ++ CS_X11_CYANS
    private val MAX_AGE = 15
    // Defines the radius of explosion in terms of the particle age.
    private var x = initX.toFloat
    private var y = initY.toFloat
    // Linear color gradient, slowly dimming.
    private val cf = CPCurve.colorGradient(CPRand.rand(COLORS), BG_PX.fg, MAX_AGE)
    // X-curve for slowing down the speed of particle as it moves away from the center.
    private val dxf = CPCurve.lagrangePoly(Seq(
        x -> 1f,
        x + (dx * MAX_AGE) / 4 -> 0.5f,
        x + dx * MAX_AGE -> 0.3f
    ))
    // Y-curve for slowing down the speed of particle as it moves away from the center.
    private val dyf = CPCurve.lagrangePoly(Seq(
        y -> 1f,
        y + (dy * MAX_AGE) / 4 -> 0.4f,
        y + dy * MAX_AGE -> 0.2f
    ))
    private var age = 0

    override def update(ctx: CPSceneObjectContext): Unit =
        age += 1
        // Curve X and Y-coordinates changes.
        x += dx * dxf(x)
        y += dy * dyf(y)
    override def getX: Int = x.round
    override def getY: Int = y.round
    override val getZ: Int = 1
    override def getPixel: CPPixel = BG_PX.withFg(cf())
    override def isAlive: Boolean = age < MAX_AGE
