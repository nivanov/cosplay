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

/**
  * Direction of the slide effect produced by [[CPSlideInShader]] and [[CPSlideOutShader]] shaders.
  *
  * @see [[CPSlideInShader]]
  * @see [[CPSlideOutShader]]
  */
enum CPSlideDirection:
    /**  Slide effect of collapsing from top and bottom. */
    case HOR_COLLAPSE

    /**  Slide effect of expanding from center towards top and bottom. */
    case HOR_EXPAND

    /**  Slide effect of collapsing from left and right. */
    case VER_COLLAPSE

    /**  Slide effect of expanding from center towards left and right. */
    case VER_EXPAND

    /**  Slide effect from left to right. */
    case LEFT_TO_RIGHT

    /**  Slide effect from right to left. */
    case RIGHT_TO_LEFT

    /**  Slide effect top to down. */
    case TOP_TO_BOTTOM

    /**  Slide effect down up. */
    case BOTTOM_TO_TOP

    /**  Slide effect from center outward. */
    case CENTRIFUGAL

    /**  Slide effect from outward to center. */
    case CENTRIPETAL

    /**  Slide using random-pixel effect. */
    case RANDOM

    /**  Slide using random vertical line effect. */
    case RANDOM_VERT_LINE

    /**  Slide using random horizontal line effect. */
    case RANDOM_HOR_LINE

object CPSlideDirection:
    /**
      *
      * @param dir
      * @param dim
      * @param maxFrmCnt
      */
    def mkMatrix(dir: CPSlideDirection, dim: CPDim, maxFrmCnt: Int): Array[Array[Int]] =
        val w = dim.w
        val h = dim.h
        val matrix = Array.ofDim[Int](w, h)
        dir match
            case HOR_COLLAPSE ⇒
                var d = 0f
                val dx = maxFrmCnt.toFloat / h * 2
                var x = 0
                var y = 0
                val n = h / 2
                while (y < n)
                    x = 0
                    while (x < w)
                        val dr = d.round
                        matrix(x)(y) = dr
                        matrix(x)(h - 1 - y) = dr
                        x += 1
                    y += 1
                    d += dx
            case HOR_EXPAND ⇒
                var d = maxFrmCnt.toFloat
                val dx = d / h * 2
                var x = 0
                var y = 0
                val n = h / 2
                while (y < n)
                    x = 0
                    while (x < w)
                        val dr = d.round
                        matrix(x)(y) = dr
                        matrix(x)(h - 1 - y) = dr
                        x += 1
                    y += 1
                    d -= dx
            case VER_COLLAPSE ⇒
                var d = 0f
                val dx = maxFrmCnt.toFloat / w * 2
                var x = 0
                var y = 0
                val n = w / 2
                while (x < n)
                    y = 0
                    while (y < h)
                        val dr = d.round
                        matrix(x)(y) = dr
                        matrix(w - 1 - x)(y) = dr
                        y += 1
                    x += 1
                    d += dx
            case VER_EXPAND ⇒
                var d = maxFrmCnt.toFloat
                val dx = d / w * 2
                var x = 0
                var y = 0
                val n = w / 2
                while (x < n)
                    y = 0
                    while (y < h)
                        val dr = d.round
                        matrix(x)(y) = dr
                        matrix(w - 1 - x)(y) = dr
                        y += 1
                    x += 1
                    d -= dx
            case LEFT_TO_RIGHT ⇒
                var d = maxFrmCnt.toFloat
                val dx = d / w
                var x = w - 1
                var y = 0
                while (x >= 0)
                    y = 0
                    while (y < h)
                        matrix(x)(y) = d.round
                        y += 1
                    x -= 1
                    d -= dx
            case RIGHT_TO_LEFT ⇒
                var d = maxFrmCnt.toFloat
                val dx = d / w
                var x = 0
                var y = 0
                while (x < w)
                    y = 0
                    while (y < h)
                        matrix(x)(y) = d.round
                        y += 1
                    x += 1
                    d -= dx
            case TOP_TO_BOTTOM ⇒
                var d = maxFrmCnt.toFloat
                val dx = d / w
                var x = 0
                var y = h - 1
                while (y >= 0)
                    x = 0
                    while (x < w)
                        matrix(x)(y) = d.round
                        x += 1
                    d -= dx
                    y -= 1
            case BOTTOM_TO_TOP ⇒
                var d = maxFrmCnt.toFloat
                val dx = d / w
                var x = 0
                var y = 0
                while (y < h)
                    x = 0
                    while (x < w)
                        matrix(x)(y) = d.round
                        x += 1
                    d -= dx
                    y += 1
            case RANDOM ⇒
                var x = 0
                var y = 0
                while (x < w)
                    y = 0
                    while (y < h)
                        matrix(x)(y) = CPRand.randInt(0, maxFrmCnt)
                        y += 1
                    x += 1
            case RANDOM_VERT_LINE ⇒
                var x = 0
                var y = 0
                while (x < w)
                    y = 0
                    val d = CPRand.randInt(0, maxFrmCnt)
                    while (y < h)
                        matrix(x)(y) = d
                        y += 1
                    x += 1
            case RANDOM_HOR_LINE ⇒
                var x = 0
                var y = 0
                while (y < h)
                    x = 0
                    val d = CPRand.randInt(0, maxFrmCnt)
                    while (x < w)
                        matrix(x)(y) = d
                        x += 1
                    y += 1
            case CENTRIFUGAL =>
                val hFactor = 2f
                val cx = w / 2
                val cy = h / 2
                val r = Math.min(w.toFloat, h * hFactor) / 2
                var x = 0
                var y = 0
                while (y < h)
                    x = 0
                    while (x < w)
                        val k = Math.sqrt(Math.pow(cx - x, 2) + Math.pow((cy - y) * hFactor, 2)).toFloat
                        val d = (maxFrmCnt * k.min(r) / r).round
                        matrix(x)(y) = d
                        x += 1
                    y += 1
            case CENTRIPETAL =>
                val hFactor = 2f
                val cx = w / 2
                val cy = h / 2
                val r = Math.min(w.toFloat, h * hFactor) / 2
                var x = 0
                var y = 0
                while (y < h)
                    x = 0
                    while (x < w)
                        val k = Math.sqrt(Math.pow(cx - x, 2) + Math.pow((cy - y) * hFactor, 2)).toFloat
                        val d = (maxFrmCnt * (1 - k.min(r) / r)).round
                        matrix(x)(y) = d
                        x += 1
                    y += 1

        matrix
