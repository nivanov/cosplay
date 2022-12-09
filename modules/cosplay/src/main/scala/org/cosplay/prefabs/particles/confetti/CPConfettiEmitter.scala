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

package org.cosplay.prefabs.particles.confetti

import org.cosplay.*
import CPColor.*
import games.snake.*

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
  * Particle emitter for confetti effect.
  *
  * @param xf X-coordinate producer for emission center.
  * @param yf Y-coordinate producer for emission center.
  * @param genSize Number of particles this emitter will emit on each frame update.
  * @param maxAge Maximum age of the particle, i.e. how many frames it will be visible.
  * @param colors Set of colors to randomly color the particles.
  * @param bgFg A color to fade in to when a particle dies.
  * @param chf Function that takes current age and return character to use for that frame.
  * @param z Z-index to use to draw particles from this emitter.
  */
class CPConfettiEmitter(
    xf: () => Int,
    yf: () => Int,
    genSize: Int,
    maxAge: Int,
    colors: Seq[CPColor],
    bgFg: CPColor,
    chf: Int => Char,
    z: Int) extends CPParticleEmitter():
    require(colors.nonEmpty, "Colors cannot be empty.")

    // Number of particles this emitter will emit on each update.
    private final val GEN_SIZE = 10
    private val MAX_AGE = 15
    private var age = 0

    override def reset(): Unit = age = 0
    override def emit(ctx: CPBaseContext): Iterable[CPParticle] =
        if !isPaused && age < MAX_AGE then
            age += 1
            // Emit particles in 360 degree semi-circle.
            for _ <- 0 to GEN_SIZE yield
                CPConfettiParticle(
                    xf(),
                    yf(),
                    (CPRand.randFloat() - 0.5f) * 3.5f,
                    (CPRand.randFloat() - 0.5f) * 2f,
                    maxAge,
                    colors,
                    bgFg,
                    chf,
                    z
                )
        else
            Seq.empty
