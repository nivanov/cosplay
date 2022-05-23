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

          2D ASCII GAME ENGINE FOR SCALA3
            (C) 2021 Rowan Games, Inc.
               ALl rights reserved.
*/

import org.cosplay.*
import CPPixel.*

import scala.collection.mutable

/**
  * An individual streak descriptor used by [[CPStarStreakShader]].
  *
  * @param ch Character to use to draw stars in this streak.
  * @param colors Set of initial color for fading from. Colors will be randomly chosen for each star.
  * @param ratio Percentage of stars visible at the same time. For example, the value of `0.04` means  4%, and
  *         if the camera frame size is 100x50 characters then 4% ratio will result in 200 stars showing at
  *         any given time.
  * @param steps Number of frames that it takes for entire star lifecycle in this streak.
  * @param z Z-index at which to draw the stars in this streak.
  * @param speed Tuple defining the X and Y-axis increment on each frame. This tuples defines both the speed
  *        and the direction stars in this streak will move with.
  * @example See [[org.cosplay.examples.shader.CPShaderExample CPShaderExample]] class for the example of using shaders.
  * @example See [[org.cosplay.games.snake.CPSnakeTitleScene]] game scene for example of using this shader.
  * @see [[CPStarStreakShader]]
  * @see [[CPFadeOutShader]]
  * @see [[CPSlideInShader]]
  * @see [[CPSlideOutShader]]
  * @see [[CPShimmerShader]]
  * @see [[CPSparkleShader]]
  * @see [[CPFlashlightShader]]
  * @see [[CPOffScreenSprite]]
  */
case class CPStarStreak(
    ch: Char,
    colors: Seq[CPColor],
    ratio: Float,
    steps: Int,
    speed: (Float, Float),
    z: Int
):
    require(ratio >= 0f && ratio <= 1f, "Ratio must be in [0,1] range.")
    require(colors.nonEmpty, "Colors cannot be empty.")

/**
  * Star streak shader.
  *
  * This shader produces a star streak effect with one or more streaks. Each streak defined by [[CPStarStreak]] class
  * provides unique configuration for the set of stars: its character to render with, number of stars relative to the size
  * of the current canvas, colors to use for fading, its lifetime, speed and direction to move and z-index. Each such
  * set of stars can "streak" differently, e.g. providing parallax effect.
  *
  * For example, here's an example of using this shader in the built-in [[org.cosplay.games.snake.CPSnakeGame Snake]] game
  * for parallax starry skies effect on its title scene:
  * {{{
  *  private val starStreakShdr = CPStarStreakShader(
  *     true,
  *     BG_PX.bg.get,
  *      Seq(
  *          CPStarStreak('.', CS, 0.025, 30, (-.5f, 0f), 0),
  *          CPStarStreak('.', CS, 0.015, 25, (-1.5f, 0f), 0),
  *          CPStarStreak('_', CS, 0.005, 20, (-2.0f, 0f), 0)
  *      ),
  *      skip = (zpx, _, _) ⇒ zpx.z == 1
  *  )
  * }}}
  *
  * @param entireFrame Whether apply to the entire camera frame or just the object this shader is attached to.
  * @param bg Background color to fade into.
  * @param autoStart Whether to start shader right away. Default value is `false`.
  * @param skip Predicate allowing to skip certain pixel from the shader. Predicate takes a pixel (with its Z-order),
  *     and X and Y-coordinate of that pixel. Note that XY-coordinates are always in relation to the entire canvas.
  *     Typically used to skip background or certain Z-index. Default predicate returns `false` for all pixels.
  * @param durMs Duration of the effect in milliseconds. By default, the effect will go forever.
  * @param onDuration Optional callback to call when this shader finishes by exceeding the duration
  *     specified by `durMs` parameter. Default is a no-op.
  * @example See [[org.cosplay.examples.shader.CPShaderExample CPShaderExample]] class for the example of using shaders.
  * @example See [[org.cosplay.games.snake.CPSnakeTitleScene]] game scene for example of using this shader.
  * @see [[CPStarStreak]]
  * @see [[CPFadeOutShader]]
  * @see [[CPSlideInShader]]
  * @see [[CPSlideOutShader]]
  * @see [[CPShimmerShader]]
  * @see [[CPSparkleShader]]
  * @see [[CPFlashlightShader]]
  * @see [[CPOffScreenSprite]]
  */
class CPStarStreakShader(
    entireFrame: Boolean,
    bg: CPColor,
    streaks: Seq[CPStarStreak],
    autoStart: Boolean = false,
    skip: (CPZPixel, Int, Int) => Boolean = (_, _, _) => false,
    durMs: Long = Long.MaxValue,
    onDuration: CPSceneObjectContext => Unit = _ => ()
) extends CPShader:
    require(streaks.nonEmpty, "Streaks cannot be empty.")

    case class Star(streak: CPStarStreak, initX: Int, initY: Int):
        private var x = initX.toFloat
        private var y = initY.toFloat
        private val initCol = CPRand.rand(streak.colors)
        private val grad = CPColor.gradientSeq(initCol, bg, streak.steps)
        private val gradSz = grad.size
        private var gradIdx = CPRand.between(0, gradSz)

        def isAlive: Boolean = gradIdx < gradSz
        def draw(canv: CPCanvas): Unit =
            canv.drawPixel(streak.ch&grad(gradIdx % gradSz), x.round, y.round, streak.z)
            x += streak.speed._1
            y += streak.speed._2
            gradIdx += 1

    private val stars = mutable.ArrayBuffer.empty[Star]
    private var go = autoStart
    private var startMs = 0L

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
        stars.clear()
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
            stars.filterInPlace(_.isAlive)
            val canv = ctx.getCanvas
            val rect = if entireFrame then ctx.getCameraFrame else objRect
            // Replenish with new sparkles until full.
            for (streak ← streaks)
                val num = (canv.w * canv.h * streak.ratio).round
                var cnt = stars.count(_.streak == streak)
                while cnt < num do
                    var found = false
                    while !found do
                        val x = rect.randX()
                        val y = rect.randY()
                        found = !skip(canv.getZPixel(x, y), x, y)
                        if found then
                            stars += Star(streak, x, y)
                            cnt += 1

            // Draw sparkles.
            stars.foreach(_.draw(canv))

