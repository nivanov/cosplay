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
import games.pong.*

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
  * Circular flashlight effect shader.
  *
  * This shader can be used to create a circular flashlight effect around the object. It renders its
  * effect only for a scene object this shader is attached to (i.e. it does not work for entire screen).
  *
  * @param radius Radius of circular flashlight effect.
  * @param autoStart Whether or not to toggle on the shader effect automatically. Default value is `false`.
  * @param skip Predicate allowing to skip certain pixel from the shader. Predicate takes a pixel (with its Z-order),
  *     and X and Y-coordinate of that pixel. Note that XY-coordinates are always in relation to the entire canvas.
  *     Typically used to skip background or certain Z-index. Default predicate returns `false` for all pixels.
  * @see [[CPFadeInShader]]
  * @see [[CPSlideInShader]]
  * @see [[CPSlideOutShader]]
  * @see [[CPShimmerShader]]
  * @see [[CPFadeOutShader]]
  * @see [[CPOffScreenSprite]]
  * @see [[CPStarStreakShader]]
  * @example See [[org.cosplay.examples.shader.CPShaderExample CPShaderExample]] class for the example of using shaders.
  */
class CPFlashlightShader(
    radius: Int,
    autoStart: Boolean = false,
    skip: (CPZPixel, Int, Int) => Boolean = (_, _, _) => false
) extends CPShader:
    private var go = autoStart

    /**
      * Toggles this shader effect on and off.
      *
      * @see [[start()]]
      * @see [[stop()]]
      */
    def toggle(): Unit = go = !go

    /**
      * Starts the shader effect.
      *
      * @see [[toggle()]]
      */
    def start(): Unit = go = true

    /**
      * Stops the shader effect.
      *
      * @see [[toggle()]]
      */
    def stop(): Unit = go = false

    /**
      * Returns `true` if this shader effect is on, `false` otherwise.
      */
    def isActive: Boolean = go

    /** @inheritdoc */
    override def render(ctx: CPSceneObjectContext, objRect: CPRect, inCamera: Boolean): Unit =
        if go && ctx.isVisible then
            val canv = ctx.getCanvas
            val cx = objRect.xCenter
            val cy = objRect.yCenter
            val effRect = CPRect(cx - radius * 2, cy - radius, radius * 4, radius * 2)
            effRect.loop((x, y) => {
                if canv.isValid(x, y) then
                    val zpx = canv.getZPixel(x, y)
                    if !skip(zpx, x, y) then
                        val px = zpx.px
                        // Account for character with/height ratio to make a proper circle...
                        // NOTE: we can't get the font metrics in the native ANSI terminal so
                        //       we use 1.85 as a general approximation.
                        val dx = (cx - x).abs.toFloat / 1.85
                        val dy = (cy - y).abs.toFloat
                        val r = Math.sqrt(dx * dx + dy * dy).toFloat
                        if r <= radius then // Flashlight is a circular effect.
                            if px.char == BG_PX.char then
                                val newFg = px.fg.lighter(0.2f * (1.0f - r / radius))
                                canv.drawPixel(px.withFg(newFg), x, y, zpx.z)
            })
