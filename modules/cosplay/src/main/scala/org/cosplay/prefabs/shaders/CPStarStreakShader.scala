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
  *
  * @param ch
  * @param colors
  * @param ratio
  * @param steps
  * @param z
  * @param speed
  */
case class CPStarStreak(
    ch: Char,
    colors: Seq[CPColor],
    ratio: Float,
    steps: Int,
    speed: (Float, Float),
    z: Int
)

/**
  *
  * @param bg
  * @param streaks
  * @param autoStart
  * @param skip
  * @param durMs
  * @param onDuration
  */
class CPStarStreakShader(
    bg: CPColor,
    streaks: Seq[CPStarStreak],
    autoStart: Boolean = false,
    skip: (CPZPixel, Int, Int) => Boolean = (_, _, _) => false,
    durMs: Long = Long.MaxValue,
    onDuration: CPSceneObjectContext => Unit = _ => ()
) extends CPShader:
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
        if go && System.currentTimeMillis() - startMs > durMs then
            stop()
            onDuration(ctx)

        if go then
            // Remove stale sparkles, if any.
            stars.filterInPlace(_.isAlive)
            val canv = ctx.getCanvas
            // Replenish with new sparkles until full.
            for (streak ← streaks)
                val num = (canv.w * canv.h * streak.ratio).round
                val rect = canv.rect
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

