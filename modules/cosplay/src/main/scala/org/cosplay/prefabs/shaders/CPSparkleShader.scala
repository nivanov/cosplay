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

package org.cosplay.prefabs.shaders

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

import org.cosplay.*
import games.*
import CPColor.*

import scala.collection.mutable

/**
  * Color sparkle shader.
  *
  * This shader provides color sparkle effect for the entire camera frame. Sparkling consists of picking number
  * of random pixels on the frame and gradually dimming and brightening their colors creating a sparkling effect.
  * Not that unlike [[CPShimmerShader]] that produces a random color changes, this shader relies on smooth gradual
  * dimming and brightening. Both effects are visually similar but work differently.
  *
  * @param entireFrame Whether apply to the entire camera frame or just the object this shader is attached to.
  * @param colors Set of color to use for sparking effect. Colors will be randomly chosen for each pixel.
  * @param ratio Percentage of pixels to sparkle at the same time. Default value is `0.04`, i.e. 4%. For example,
  *         if the camera frame size is 100x50 characters then the default 4% ratio will result in 200 pixels
  *         sparkling at any given time.
  * @param steps Number of frames that it takes for entire sparkle cycle from brightening to dimming back.
  * @param autoStart Whether to start shader right away. Default value is `false`.
  * @param skip Predicate allowing to skip certain pixel from the shader. Predicate takes a pixel (with its Z-order),
  *     and X and Y-coordinate of that pixel. Note that XY-coordinates are always in relation to the entire canvas.
  *     Typically used to skip background or certain Z-index. Default predicate returns `false` for all pixels.
  * @param durMs Duration of the effect in milliseconds. By default, the effect will go forever.
  * @param onDuration Optional callback to call when this shader finishes by exceeding the duration
  *     specified by `durMs` parameter. Default is a no-op.
  *
  * @see [[CPFadeInShader]]
  * @see [[CPFadeOutShader]]
  * @see [[CPSlideInShader]]
  * @see [[CPSlideOutShader]]
  * @see [[CPFlashlightShader]]
  * @see [[CPShimmerShader]]
  * @see [[CPStarStreakShader]]
  * @example See [[org.cosplay.examples.shader.CPShaderExample CPShaderExample]] class for the example of using shaders.
  * @example See [[org.cosplay.games.pong.CPPongTitleScene]] game scene for example of using this shader.
  */
class CPSparkleShader(
    entireFrame: Boolean,
    colors: Seq[CPColor],
    ratio: Float = 0.04f,
    steps: Int = 40,
    autoStart: Boolean = false,
    skip: (CPZPixel, Int, Int) => Boolean = (_, _, _) => false,
    durMs: Long = Long.MaxValue,
    onDuration: CPSceneObjectContext => Unit = _ => (),
) extends CPShader:
    require(durMs > CPEngine.frameMillis, s"Duration must be > ${CPEngine.frameMillis}ms.")
    require(ratio >= 0f && ratio <= 1f, "Ratio must be in [0,1] range.")
    require(colors.nonEmpty, "Colors cannot be empty.")

    case class Sparkle(zpx: CPZPixel, x: Int, y: Int):
        private val initCol = CPRand.rand(colors)
        private val grad = CPColor.gradientSeq(zpx.px.fg, initCol, steps / 2) ++ CPColor.gradientSeq(initCol, zpx.px.fg, steps / 2)
        private val gradSz = grad.size
        private var gradIdx = CPRand.between(0, gradSz)

        def isAlive: Boolean = gradIdx < gradSz
        def draw(canv: CPCanvas): Unit =
            canv.drawPixel(zpx.px.withFg(grad(gradIdx % gradSz)), x, y, zpx.z)
            gradIdx += 1

    private val sparkles = mutable.ArrayBuffer.empty[Sparkle]
    private var go = autoStart
    private var startMs = 0L

    if autoStart then start()

    /**
      * Starts the shader effect.
      *
      * @see [[toggle()]]
      */
    def start(): Unit =
        go = true
        startMs = System.currentTimeMillis()

    /**
      * Stops the shader effect without waiting for the duration. Note that `onDuration()` callback will not
      * be called in this case.
      *
      * @see [[toggle()]]
      */
    def stop(): Unit =
        go = false
        sparkles.clear()
        startMs = 0

    /**
      * Toggles this shader effect on and off by calling either [[start()]] or [[stop()]] methods..
      *
      * @see [[start()]]
      * @see [[stop()]]
      */
    def toggle(): Unit = if go then stop() else start()

    /**
      * Tests whether this shader is in progress or not.
      */
    def isOn: Boolean = go

    /** @inheritdoc */
    override def render(ctx: CPSceneObjectContext, objRect: CPRect, inCamera: Boolean): Unit =
        val flag = go && (entireFrame || (ctx.isVisible && inCamera)) 
        
        if flag && System.currentTimeMillis() - startMs > durMs then
            stop()
            onDuration(ctx)
        else if flag then
            // Remove stale sparkles, if any.
            sparkles.filterInPlace(_.isAlive)
            val canv = ctx.getCanvas
            // Replenish with new sparkles until full.
            val num = (canv.w * canv.h * ratio).round
            val rect = if entireFrame then ctx.getCameraFrame else objRect
            while sparkles.size < num do
                var found = false
                while !found do
                    val x = rect.randX()
                    val y = rect.randY()
                    val zpx = canv.getZPixel(x, y)
                    found = !skip(zpx, x, y) && !sparkles.exists(s => s.x == x && s.y == y)
                    if found then sparkles += Sparkle(zpx, x, y)

            // Draw sparkles.
            sparkles.foreach(_.draw(canv))