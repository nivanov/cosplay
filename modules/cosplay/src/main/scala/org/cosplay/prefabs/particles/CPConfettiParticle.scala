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

package org.cosplay.prefabs.particles

import org.cosplay.{given, *}
import org.cosplay.CPPixel.*

/*
   _________            ______________
   __  ____/_______________  __ \__  /_____ _____  __
   _  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
   / /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
   \____/  \____//____/ /_/     /_/  \__,_/ _\__, /
                                            /____/

          2D ASCII JVM GAME ENGINE FOR SCALA3
              (C) 2021 Rowan Games, Inc.
                All rights reserved.
*/

/**
  * Particle for confetti effect.
  *
  * @param initX Initial x-coordinate.
  * @param initY Initial y-coordinate.
  * @param dx Per-frame x-speed.
  * @param dy Per-frame y-speed.
  * @param maxAge Maximum age of the particle, i.e. how many frames it will be visible.
  * @param colors Set of colors to randomly color the particle at each frame.
  * @param bgFg A color to fade in to when this particle dies.
  * @param chf Function that takes current age and return character to use for that frame.
  * @param z Z-index to use to draw this particle.
  */
class CPConfettiParticle(
    initX: Int,
    initY: Int,
    dx: Float,
    dy: Float,
    maxAge: Int,
    colors: Seq[CPColor],
    bgFg: CPColor,
    chf: Int => Char,
    z: Int) extends CPParticle:
    !>(colors.nonEmpty, "Colors cannot be empty.")

    // Defines the radius of explosion in terms of the particle age.
    private var x = initX.toFloat
    private var y = initY.toFloat
    // Linear color gradient, slowly dimming.
    private val cf = CPCurve.colorGradient(colors.rand, bgFg, maxAge)
    // X-curve for slowing down the speed of particle as it moves away from the center.
    private val dxf = CPCurve.lagrangePoly(Seq(
        x -> 1f,
        x + (dx * maxAge) / 4 -> 0.5f,
        x + dx * maxAge -> 0.3f
    ))
    // Y-curve for slowing down the speed of particle as it moves away from the center.
    private val dyf = CPCurve.lagrangePoly(Seq(
        y -> 1f,
        y + (dy * maxAge) / 4 -> 0.4f,
        y + dy * maxAge -> 0.2f
    ))
    private var age = 0

    override def update(ctx: CPSceneObjectContext): Unit =
        age += 1
        // Curve X and Y-coordinates changes.
        x += dx * dxf(x)
        y += dy * dyf(y)
    override def getX: Int = x.round
    override def getY: Int = y.round
    override val getZ: Int = z
    override def getPixel: CPPixel = chf(age)&cf()
    override def isAlive: Boolean = age < maxAge
