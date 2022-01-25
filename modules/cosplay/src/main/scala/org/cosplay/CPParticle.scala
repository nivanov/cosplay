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
  * Programmable particle.
  *
  * Particle is part of the particle effect animation support. Particle effect support consists of three key components:
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
  * @example See [[org.cosplay.examples.particle.CPParticleExample CPParticleExample]] class for the example of
  *     using particle effect.
  */
trait CPParticle:
    /**
      * This method is called on each frame update. Technically, a [[CPParticleSprite particle sprite]] call this method
      * when it itself receives [[CPSceneObject.update()]] callback passing its own scene context to this callback.
      *
      * @param ctx Scene object context of the particle sprite.
      */
    def update(ctx: CPSceneObjectContext): Unit

    /**
      * Gets current X-coordinate of this particle.
      */
    def getX: Int

    /**
      * Gets current Y-coordinate of this particle.
      */
    def getY: Int

    /**
      * Gets current Z-index of this particle.
      */
    def getZ: Int

    /**
      * Gets pixel for this particle.
      */
    def getPixel: CPPixel

    /**
      * Tests whether or not this particle is alive. Dead particles will be discarded by the
      * particle sprite and won't be rendered anymore. This method is used to check every particle
      * on each frame update.
      */
    def isAlive: Boolean
