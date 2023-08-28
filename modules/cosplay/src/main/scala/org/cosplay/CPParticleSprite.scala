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

import scala.collection.mutable

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
  * Scene object tailor-made for rendering particle effect.
  *
  * Particle sprite is part of the particle effect animation support. Particle effect support consists of three key components:
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
  * ### Sprites
  * CosPlay provides number of built-in sprites. A sprite is a scene objects, visible or off-screen,
  * that is custom designed for a particular use case. Built-in sprites provide concrete
  * implementations for the abstract methods in the base [[CPSceneObject]] class. Most non-trivial games
  * will use combination of the built-in sprites and their own ones. Here's the list of the built-in
  * sprites:
  *  - [[CPCanvasSprite]]
  *  - [[CPImageSprite]]
  *  - [[CPStaticImageSprite]]
  *  - [[CPLabelSprite]]
  *  - [[CPOffScreenSprite]]
  *  - [[CPKeyboardSprite]]
  *  - [[CPParticleSprite]]
  *  - [[CPVideoSprite]]
  *  - [[CPTextInputSprite]]
  *
  * @param id Optional ID of the sprite.
  * @param emitters Set of particle emitters this sprite will use.
  * @param collidable Whether or not this sprite provides collision shape. Default value is `false`.
  * @param shaders Optional sequence of shaders for this sprite. Default value is an empty sequence.
  * @param tags Optional set of organizational or grouping tags. By default, the empty set is used.
  * @example See [[org.cosplay.examples.particle.CPParticleExample CPParticleExample]] class for the example of
  *     using particle effect.
  * @see [[CPSceneObjectContext.getCanvas]] to get current canvas you can draw on.
  * @see [[CPCanvas]] various API to draw on the canvas.
  */
class CPParticleSprite(
    id: String = s"part-spr-${CPRand.guid6}",
    emitters: Seq[CPParticleEmitter],
    collidable: Boolean = false,
    shaders: Seq[CPShader] = Seq.empty,
    tags: Set[String] = Set.empty
) extends CPSceneObject(id):
    !>(emitters.nonEmpty, "Sequence of emitters cannot be empty.")

    private var x = 0
    private var y = 0
    private var z = 0
    private var dim = CPDim.ZERO
    private val liveParts = mutable.ArrayBuffer.empty[CPParticle]
    private var onStartFun: Option[CPSceneObjectContext => Unit] = None
    private var onEndFun: Option[CPSceneObjectContext => Unit] = None
    private var effStarted = false

    /** @inheritdoc */
    override def getDim: CPDim = dim
    /** @inheritdoc */
    override def getX: Int = x
    /** @inheritdoc */
    override def getY: Int = y
    /** @inheritdoc */
    override def getZ: Int = z

    /**
      * Sets or removes the callback for the start of the effect. Start of the effect is
      * defined as first game frame when the first particle was emitted.
      * Note that if the effect is cyclical the callback will be called everytime
      * the start of the effect is detected.
      *
      * @param fun Callback function to set or `None` to remove previously set callback.
      * @see [[setOnEnd()]]
      */
    def setOnStart(fun: Option[CPSceneObjectContext => Unit]): Unit =
        !>(fun != null, "Callback cannot be 'null'.")
        onStartFun = fun

    /**
      * Sets or removes the callback for the end of the effect. End of the effect is
      * defined as first game frame when there are no [[CPParticle.isAlive live]] particles remained.
      * Note that if the effect is cyclical the callback will be called everytime
      * the end of effect is detected.
      *
      * @param fun Callback function to set or `None` to remove any previously set callback.
      * @see [[setOnStart()]]
      */
    def setOnEnd(fun: Option[CPSceneObjectContext => Unit]): Unit =
        !>(fun != null, "Callback cannot be 'null'.")
        onEndFun = fun

    /**
      * Resets all emitters to their initial state by calling their [[CPParticleEmitter.reset()]] method.
      */
    def reset(): Unit = emitters.foreach(_.reset())

    /**
      * Pauses all emitters by calling their [[CPParticleEmitter.pause()]] method.
      */
    def pause(): Unit = emitters.foreach(_.pause())

    /**
      * Resumes all emitters by calling their [[CPParticleEmitter.resume()]] method.
      *
      * @param reset Whether or not to reset the emitter before resuming its emission.
      */
    def resume(reset: Boolean): Unit = emitters.foreach(_.resume(reset))

    /**
      * Toggles pause state of all emitters by calling their [[CPParticleEmitter.toggle()]] method.
      * Note that with this method you don't have the control of resetting the emitter before resuming
      * its emission.
      */
    def toggle(): Unit = emitters.foreach(_.toggle())

    /** Tests whether all emitters are paused or not. */
    def isPaused: Boolean = !emitters.exists(_.isPaused)

    /** Tests whether this sprite has any live particle remaining. */
    def hasLiveParticles: Boolean = liveParts.nonEmpty

    /** @inheritdoc */
    override def getRect: CPRect = new CPRect(getX, getY, dim)
    /** @inheritdoc */
    override def getCollisionRect: Option[CPRect] = Option.when(collidable)(getRect)
    /** @inheritdoc */
    override def getShaders: Seq[CPShader] = shaders
    /** @inheritdoc */
    override def getTags: Set[String] = tags
    /** @inheritdoc */
    override def update(ctx: CPSceneObjectContext): Unit =
        // Purge dead particles.
        liveParts.filterInPlace(_.isAlive)
        val wasEmpty = liveParts.isEmpty
        // Add newly emitted particles, if any.
        emitters.foreach(em => liveParts ++= em.emit(ctx))
        val effStart = wasEmpty && liveParts.nonEmpty
        if liveParts.nonEmpty then
            effStarted = true

            // Update each particle.
            liveParts.foreach(_.update(ctx))

            // Update sprite dimensions.
            x = liveParts.minBy(_.getX).getX
            y = liveParts.minBy(_.getY).getY
            z = liveParts.minBy(_.getZ).getZ
            val maxX = liveParts.maxBy(_.getX).getX
            val maxY = liveParts.maxBy(_.getY).getY
            dim = CPDim(maxX - x, maxY -y)

            if effStart then
                onStartFun match
                    case Some(fun) => fun(ctx)
                    case None => ()
        else
            x = 0
            y = 0
            z = 0
            dim = CPDim.ZERO
            if effStarted then
                onEndFun match
                    case Some(fun) => fun(ctx)
                    case None => ()
                effStarted = false

    /** @inheritdoc */ 
    override def render(ctx: CPSceneObjectContext): Unit =
        if liveParts.nonEmpty then
            // Render each particle.
            val canv = ctx.getCanvas
            liveParts.foreach(p => canv.drawPixel(p.getPixel, p.getX, p.getY, p.getZ))

