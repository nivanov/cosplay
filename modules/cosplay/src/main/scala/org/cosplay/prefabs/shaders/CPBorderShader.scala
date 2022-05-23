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

import org.cosplay.*
import CPColor.*
import CPZPixel.*

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
  * Border shade shader.
  *
  * This shader creates a gradually shaded border by dimming the background color of the pixels
  * closer to the border of the frame or object.
  *
  * @param entireFrame Whether apply to the entire camera frame or just the object this
  *     shader is attached to.
  * @param width How many lines (vertical and horizontal) the shaded border width will be.
  * @param compensateWidth Whether or not to compensate for non-square form of the character "pixel".
  *         If `true` then for each horizontal line there will be 2 vertical lines giving the visual
  *         effect of the both horizontal and vertical border lines having the same width.
  * @param colorMixPerStep Float value in `[0,1]` range indicating the color change per each step in the border.
  * @param autoStart Whether to start shader right away. Default value is `false`.
  * @see [[CPFadeOutShader]]
  * @see [[CPSlideInShader]]
  * @see [[CPSlideOutShader]]
  * @see [[CPShimmerShader]]
  * @see [[CPFlashlightShader]]
  * @see [[CPOffScreenSprite]]
  * @see [[CPStarStreakShader]]
  * @see [[CPOldCRTShader]]
  * @see [[CPFadeInShader]]
  * @example See [[org.cosplay.games.bird.CPBirdGame CPBirdGame]] class for the example of using this shader.
  * @example See [[org.cosplay.examples.shader.CPShaderExample CPShaderExample]] class for the example of using shaders.
  */
class CPBorderShader(
    entireFrame: Boolean,
    width: Int,
    compensateWidth: Boolean = true,
    colorMixPerStep: Float,
    autoStart: Boolean = false
) extends CPShader:
    require(colorMixPerStep >= 0f && colorMixPerStep <= 1f, "Color mix per step must be in [0,1] range.")
    private var go = autoStart

    /**
      * Starts the shader effect.
      *
      * @see [[toggle()]]
      */
    def start(): Unit = go = true

    /**
      * Stops the shader effect without waiting for the duration.
      *
      * @see [[toggle()]]
      */
    def stop(): Unit = go = false

    /**
      * Toggles this shader effect on and off by calling either [[start()]] or [[stop()]] methods.
      *
      * @see [[start()]]
      * @see [[stop()]]
      */
    def toggle(): Unit = if go then stop() else start()

    /**
      * Tests whether or not shader is currently active.
      */
    def isActive: Boolean = go

    /** @inheritdoc */
    override def render(ctx: CPSceneObjectContext, objRect: CPRect, inCamera: Boolean): Unit =
        if go && (entireFrame || (ctx.isVisible && inCamera)) then
            val rect = if entireFrame then ctx.getCameraFrame else objRect
            val canv = ctx.getCanvas

            def updatePx(x: Int, y: Int, colorMix: Float): Unit =
                val mix = if colorMix < -1f then -1f else if colorMix > 1f then 1f else colorMix
                def mixColor(c: CPColor): CPColor = if mix < 0 then c.darker(mix.abs) else c.lighter(mix)
                val zpx = canv.getZPixel(x, y)
                val px = zpx.px
                if px.bg.nonEmpty then
                    canv.drawPixel(px.withBg(Option(mixColor(px.bg.get))), x, y, zpx.z)

            if !compensateWidth then
                for (d <- 0 until width)
                    val mix = (width - d) * colorMixPerStep
                    for (x <- (rect.xMin + d) to (rect.xMax - d))
                        updatePx(x, rect.yMin + d, mix)
                        updatePx(x, rect.yMax - d, mix)
                    for (y <- (rect.yMin + d + 1) until (rect.yMax - d))
                        updatePx(rect.xMin + d, y, mix)
                        updatePx(rect.xMax - d, y, mix)
            else
                for (d <- 0 until width)
                    val mix = (width - d) * colorMixPerStep
                    for (x <- (rect.xMin + d * 2) to (rect.xMax - d * 2))
                        updatePx(x, rect.yMin + d, mix)
                        updatePx(x, rect.yMax - d, mix)
                var d = 0
                for (k <- 0 until width)
                    val mix = (width - k) * colorMixPerStep
                    for (y <- (rect.yMin + k + 1) until (rect.yMax - k))
                        updatePx(rect.xMin + d, y, mix)
                        updatePx(rect.xMin + d + 1, y, mix)
                        updatePx(rect.xMax - d - 1, y, mix)
                        updatePx(rect.xMax - d, y, mix)
                    d += 2
