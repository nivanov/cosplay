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

package org.cosplay.games.pong.shaders

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
import org.cosplay.CPColor.*
import org.cosplay.games.pong.*
import org.cosplay.impl.CPUtils

import scala.collection.mutable
import scala.util.Random

/**
  * Title screen "sparkle" shader.
  */
class CPPongTitleSparkleShader extends CPShader:
    case class Sparkle(zpx: CPZPixel, x: Int, y: Int):
        private val initCol = CPRand.rand(CS)
        private val grad = CPColor.gradientSeq(zpx.px.fg, initCol, 20) ++ CPColor.gradientSeq(initCol, zpx.px.fg, 20)
        private val gradSz = grad.size
        private var gradIdx = Random.between(0, gradSz)

        def isAlive: Boolean = gradIdx < gradSz
        def draw(canv: CPCanvas): Unit =
            canv.drawPixel(zpx.px.withFg(grad(gradIdx % gradSz)), x, y, zpx.z)
            gradIdx += 1

    private final val NUM = 200
    private val sparkles = mutable.ArrayBuffer.empty[Sparkle]
    private var go = false

    def start(): Unit = go = true

    /** @inheritdoc */
    override def render(ctx: CPSceneObjectContext, objRect: CPRect, inCamera: Boolean): Unit =
        val canv = ctx.getCanvas
        if go && canv.w >= 40 & canv.h >= 20 then
            // Remove stale sparkles, if any.
            sparkles.filterInPlace(_.isAlive)

            // Replenish with new sparkles until full (NUM).
            while sparkles.size < NUM do
                var found = false
                while !found do
                    val x = canv.rect.randX()
                    val y = canv.rect.randY()
                    val zpx = canv.getZPixel(x, y)
                    found = zpx.px == BG_PX && !sparkles.exists(s ⇒ s.x == x && s.y == y)
                    if found then sparkles += Sparkle(zpx, x, y)

            // Draw sparkles.
            sparkles.foreach(_.draw(canv))