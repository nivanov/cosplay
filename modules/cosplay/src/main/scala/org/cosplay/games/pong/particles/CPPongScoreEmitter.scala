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
  * Particle emitter for score effect.
  * 
  * @param xf X-coordinate producer for emission center.
  * @param yf Y-coordinate producer for emission center.
  */
class CPPongScoreEmitter(xf: () ⇒ Int, yf: () ⇒ Int) extends CPParticleEmitter():
    // Number of particles this emitter will emit on each update.
    private final val GEN_SIZE = 20
    private val MAX_AGE = 15
    private var age = 0

    override def reset(): Unit = age = 0
    override def emit(ctx: CPBaseContext): Iterable[CPParticle] =
        if !isPaused && age < MAX_AGE then
            age += 1
            // Emit particles in 360 degree semi-circle.
            for (_ <- 0 to GEN_SIZE) yield CPPongScoreParticle(xf(), yf(),
                (CPRand.randFloat() - 0.5f) * 3.5f,
                (CPRand.randFloat() - 0.5f) * 2f,
            )
        else
            Seq.empty
