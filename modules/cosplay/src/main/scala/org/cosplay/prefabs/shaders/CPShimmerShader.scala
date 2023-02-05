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

import org.cosplay.{given, *}

import scala.collection.mutable

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
  * Color shimmer shader.
  *
  * This shader provides color shimmer effect for the entire camera frame or the individual scene object
  * it is attached to. Shimmering consists of randomly changing a color of the pixel for a small period of time.
  * If used for entire camera frame effect it can be attached to an off-screen sprite. Note that unlike [[CPSparkleShader]]
  * that provides similar effect but with gradual dimming and brightening of color, the shimmer effect employs
  * random color selection from the given set of colors.
  *
  * @param entireFrame Whether apply to the entire camera frame or just the object this
  *     shader is attached to.
  * @param colors Sequence of colors to use for shimmering effect. Colors will be selected randomly from this set.
  * @param keyFrame nth-frame to render the effect. For example, if key frame is `5` than the colors will
  *     change on each 5th frame and remain the same on all subsequent frames until next key frame is reached.
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
  * @see [[CPFlashlightShader]]
  * @see [[CPSparkleShader]]
  * @see [[CPStarStreakShader]]
  * @example See [[org.cosplay.examples.shader.CPShaderExample CPShaderExample]] class for the example of using shaders.
  */
class CPShimmerShader(
    entireFrame: Boolean,
    colors: Seq[CPColor],
    keyFrame: Int,
    autoStart: Boolean = false,
    skip: (CPZPixel, Int, Int) => Boolean = (_, _, _) => false,
    durMs: Long = Long.MaxValue,
    onDuration: CPSceneObjectContext => Unit = _ => (),
) extends CPShader:
    require(durMs > CPEngine.frameMillis, s"Duration must be > ${CPEngine.frameMillis}ms.")

    case class PixelXYZ(px: CPPixel, x: Int, y: Int, z: Int)
    private var go = autoStart
    private val lastSet = mutable.ArrayBuffer.empty[PixelXYZ]
    private var startMs = 0L
    private var lastRect = CPRect.ZERO

    if autoStart then start()

    /**
      * Starts the shader effect.
      *
      * @see [[toggle()]]
      */
    def start(): Unit =
        go = true
        lastSet.clear()
        startMs = System.currentTimeMillis()

    /**
      * Stops the shader effect without waiting for the duration. Note that `onDuration()` callback will not
      * be called in this case.
      *
      * @see [[toggle()]]
      */
    def stop(): Unit =
        go = false
        lastSet.clear()
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
            val canv = ctx.getCanvas
            if lastSet.isEmpty || lastRect != canv.rect || ctx.getFrameCount % keyFrame == 0 then
                lastSet.clear()
                lastRect = canv.rect
                val rect = if entireFrame then ctx.getCameraFrame else objRect
                rect.loop((x, y) => {
                    if canv.isValid(x, y) then
                        val zpx = canv.getZPixel(x, y)
                        if !skip(zpx, x, y) then
                            val rc = colors.rand
                            val px = zpx.px
                            val newPx = if px.char == ' ' then px.withBg(rc.?) else px.withFg(rc)
                            canv.drawPixel(newPx, x, y, zpx.z)
                            lastSet += PixelXYZ(newPx, x, y, zpx.z)
                })
            else
                for px <- lastSet do canv.drawPixel(px.px, px.x, px.y, px.z)



