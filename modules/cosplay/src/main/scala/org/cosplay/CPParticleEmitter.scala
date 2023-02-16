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

package org.cosplay

import org.cosplay.impl.CPUtils

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
  * Particle emitter.
  *
  * Particle emitter is part of the particle effect animation support. Particle effect support consists of three key components:
  *  - [[CPParticle Particle]]
  *  - [[CPParticleEmitter Particle emitter]]
  *  - [[CPParticleSprite Particle sprite]]
  *
  * Particle defines a single-pixel element (a-la mini-sprite) that has its own lifecycle and gets updated on each
  * frame update. Particle emitter is a components that is responsible for creating particles. And particle
  * sprite ties all that together into single renderable component. Particle sprite can have one or more particle
  * emitters and each emitter can create multiple particles. One of the key features of particle effect support is its
  * modularity: particle implementation can be reused by multiple emitters, and an emitter implementation can be reused by
  * multiple independent particle sprites.
  *
  * Particle emitter is an asset. Just like other assets such as [[CPFont fonts]], [[CPImage images]], [[CPAnimation animations]] or
  * [[CPVideo videos]] they are not managed or governed by the CosPlay game engine unlike [[CPScene scenes]] and [[CPSceneObject scene objects]]
  * that are managed and governed by the game engine. Assets are typically created outside the game loop and
  * managed by the developer, they can be freely shared between scenes or scene objects as any other standard
  * Scala objects.
  *
  * @param id Unique ID of this emitter. By default, a random 6-character ID will be used.
  * @param tags Optional set of organizational tags. These tags are here only for the game
  *     developer benefits as they are not used by the game engine itself. By default, the
  *     empty set is used.
  * @example See [[org.cosplay.examples.particle.CPParticleExample CPParticleExample]] class for the example of
  *     using particle effect. 
  */
abstract class CPParticleEmitter(id: String = s"part-emitter-${CPRand.guid6}", tags: Set[String] = Set.empty) extends CPGameObject(id, tags) with CPAsset:
    private var paused = true

    /** @inheritdoc */
    override val getOrigin: String = getClass.getName

    /**
      * Resets this emitter to its initial state.
      */
    def reset(): Unit = ()

    /**
      * Pause emission of the particles.
      */
    def pause(): Unit = paused = true

    /**
      * Resumes emission of the particles.
      *
      * @param reset Whether or not to reset this emitter before resuming emission.
      */
    def resume(reset: Boolean): Unit =
        if reset then this.reset()
        paused = false

    /**
      * Tests whether or not this emitter is paused.
      */
    def isPaused: Boolean = paused

    /**
      * Toggles pause status of this emitter. Note that with this method you don't have the control of
      * resetting the emitter before resuming its emission.
      */
    def toggle(): Unit = paused = !paused

    /**
      * Gets collections of particles that constitutes a particle emission for this frame update. Note
      * that this method is called on each frame update by the [[CPParticleSprite particle sprite]].
      *
      * @param ctx Emission context.
      */
    def emit(ctx: CPBaseContext): Iterable[CPParticle]

