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
import CPSlideDirection.*
import org.apache.commons.math3.analysis.function.*

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
  * Slide out shader.
  *
  * This shader can be used for 'slide out' or the directional gradual hide effect for the entire
  * camera frame or the individual scene object it is attached to. If used for entire
  * camera frame effect it can be attached to an off-screen sprite.
  *
  * @param dir Slide direction as defined by [[CPSlideDirection]].
  * @param entireFrame Whether apply to the entire camera frame or just the object this
  *     shader is attached to.
  * @param durMs Duration of the effect in milliseconds.
  * @param bgPx Background pixel to fade out to.
  * @param onFinish Optional callback to call when this shader finishes. Default is a no-op.
  * @param autoStart Whether to start shader right away. Default value is `true`.
  * @param skip Predicate allowing to skip certain pixel from the shader. Predicate takes a pixel (with its Z-order),
  *     and X and Y-coordinate of that pixel. Note that XY-coordinates are always in relation to the entire canvas.
  *     Typically used to skip background or certain Z-index. Default predicate returns `false` for all pixels.
  * @param balance A function that produces value in [0, 1] range that is used in color mixture.
  *     Value `0` means that the color will be 100% background, value `1` means that the color will be
  *     100% the actual pixel color, value `0.5` means that the color will be a 50% mix between the background
  *     and the actual pixel color. The function takes two parameters: first is a current frame number since the start
  *     of the effect, and the second parameter is the last frame number of the effect. First parameter is always less
  *     then the second one. By default, the the `(a, b) => a.toFloat / b` function is used that gives gradual color
  *     transition through the frames range. Another popular function to use here is a sigmoid
  *     function: `(a, b) => sigmoid.value(a - b / 2).toFloat` that gives a different visual effect.
  * @see [[CPFadeInShader]]
  * @see [[CPFadeOutShader]]
  * @see [[CPSlideInShader]]
  * @see [[CPShimmerShader]]
  * @see [[CPSparkleShader]]
  * @see [[CPStarStreakShader]]
  * @see [[CPFlashlightShader]]
  * @see [[CPOffScreenSprite]]
  * @example See [[org.cosplay.examples.shader.CPShaderExample CPShaderExample]] class for the example of using shaders.
  */
class CPSlideOutShader(
    dir: CPSlideDirection,
    entireFrame: Boolean,
    durMs: Long,
    bgPx: CPPixel,
    onFinish: CPSceneObjectContext => Unit = _ => (),
    autoStart: Boolean = false,
    skip: (CPZPixel, Int, Int) => Boolean = (_, _, _) => false,
    balance: (Int, Int) => Float = (a, b) => a.toFloat / b
) extends CPShader:
    !>(durMs > CPEngine.frameMillis, s"Duration must be > ${CPEngine.frameMillis}ms.")
    !>(bgPx.bg.nonEmpty, s"Background pixel must have background color defined: $bgPx")

    private var frmCnt = 0
    private val maxFrmCnt = (durMs / CPEngine.frameMillis).toInt
    private val bgBg = bgPx.bg.get
    private val bgFg = bgPx.fg
    private var go = autoStart
    private var cb: CPSceneObjectContext => Unit = onFinish
    private var matrix: Array[Array[Int]] = _

    if autoStart then start()

    /**
      * Resets this shaders to its initial state starting its effect on the next frame.
      *
      * @param onFinish Optional override for the callback to call when shader effect is finished.
      *         If not provided, the default value is the callback supplied at the creation of this shader.
      */
    def start(onFinish: CPSceneObjectContext => Unit = cb): Unit =
        cb = onFinish
        frmCnt = 0
        go = true

    /**
      * Set the callback to call when shader effect is finished.
      *
      * @param onFinish Override for the callback to call when shader effect is finished.
      */
    def setOnFinish(onFinish: CPSceneObjectContext => Unit): Unit = cb = onFinish

    /**
      * Tests whether this shader is in progress.
      */
    def isActive: Boolean = go

    /** @inheritdoc */
    override def render(ctx: CPSceneObjectContext, objRect: CPRect, inCamera: Boolean): Unit =
        if go && (entireFrame || (ctx.isVisible && inCamera)) then
            val rect = if entireFrame then ctx.getCameraFrame else objRect
            if matrix == null then matrix = CPSlideDirection.mkMatrix(dir, rect.dim, maxFrmCnt)
            val canv = ctx.getCanvas
            rect.loop((x, y) => {
                if canv.isValid(x, y) then
                    val zpx = canv.getZPixel(x, y)
                    val px = zpx.px
                    if px != bgPx && !skip(zpx, x, y) then
                        val px = zpx.px
                        val maxFrame = matrix(x - rect.x)(y - rect.y)
                        val bal = if frmCnt >= maxFrame then 1f else balance(frmCnt, maxFrame)
                        !>(bal >= 0f && bal <= 1f, s"Invalid balance value: $bal (must be in [0,1] range).")
                        val newFg = CPColor.mixture(px.fg, bgFg, bal)
                        val newBg = px.bg match
                            case Some(c) => CPColor.mixture(c, bgBg, bal).?
                            case None => None
                        canv.drawPixel(px.withFg(newFg).withBg(newBg), x, y, zpx.z)
            })
            frmCnt += 1
            if frmCnt == maxFrmCnt then
                go = false
                cb(ctx)

/**
  * Companion object with utility methods.
  */
object CPSlideOutShader:
    /**
      * Creates new slide out shader with sigmoid-based color balance function.
      *
      * @param dir Slide direction as defined by [[CPSlideDirection]].
      * @param entireFrame Whether apply to the entire camera frame or just the object this shader is attached to.
      * @param durMs Duration of the effect in milliseconds.
      * @param bgPx Background pixel to fade in from.
      * @param onFinish Optional callback to call when this shader finishes. Default is a no-op.
      * @param autoStart Whether to start shader right away. Default value is `true`.
      * @param skip Predicate allowing to skip certain pixel from the shader. Typically used to skip background
      *     or certain Z-index. Default predicate returns `false` for all pixels.
      */
    def sigmoid(
        dir: CPSlideDirection,
        entireFrame: Boolean,
        durMs: Long,
        bgPx: CPPixel,
        onFinish: CPSceneObjectContext => Unit = _ => (),
        autoStart: Boolean = true,
        skip: (CPZPixel, Int, Int) => Boolean = (_, _, _) => false
    ): CPSlideOutShader =
        val sigmoid = new Sigmoid()
        new CPSlideOutShader(dir, entireFrame, durMs, bgPx, onFinish, autoStart, skip, (a, b) => sigmoid.value(a - b / 2).toFloat)

