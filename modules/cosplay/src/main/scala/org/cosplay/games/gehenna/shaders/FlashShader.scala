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

package org.cosplay.games.gehenna.shaders

import org.cosplay.*
import games.gehenna.*
import scala.util.*

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


class FlashShader extends CPShader:
//    (radius: Int, bpm: Int, bgPx: CPPixel, skip: (CPZPixel, Int, Int) => Boolean = (_, _, _) => false)
    private var go = true
    private var radius = 10
    private var bpm = 50
    private var bgPx = GAME_BG_PX

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

    def changeBPM(newBPM: Int): Unit =
        bpm = newBPM
        rate = ((60/bpm.toFloat) * 1000).round
        //println(s"Rate is : $rate")

    def changeRadius: Int = radius

    private var rate = (60/bpm) * 1000
    private var lastMs = 0L

    private var currFade = 0f
    private val fadeChange = 0.05f

    override def render(ctx: CPSceneObjectContext, objRect: CPRect, inCamera: Boolean): Unit =
        if go && ctx.isVisible && (ctx.getFrameMs - rate) >= lastMs then
            lastMs = ctx.getFrameMs
            currFade = CPRand.between(0.5f, 1f)

        if currFade != 0 && currFade > fadeChange then
            currFade -= fadeChange

        val canv = ctx.getCanvas
        objRect.loop((x, y) => {
            if canv.isValid(x, y) then
                val zpx = canv.getZPixel(x, y)
                val px = zpx.px
                if px.char != ' ' then canv.drawPixel(px.withLighterFg(currFade), x, y, zpx.z)
        })

