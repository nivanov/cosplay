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
  *
  * @param autoStart
  * @param lineEffectProb
  * @param tearEffectProb
  * @param tearSnd
  */
class CPOldCRTShader(
    autoStart: Boolean = false,
    lineEffectProb: Float,
    tearEffectProb: Float,
    tearSnd: Option[CPSound] = None
) extends CPShader:
    private val LINE_EFF_SIZE = 5
    private val LINE_EFF_FACTOR = .01f
    private val TEAR_LINE_NUM = 2

    private var go = autoStart
    private var lineEffOn = false
    private var tearEffOn = false
    private var forceLineEff = false
    private var forceTearEff = false
    private var lineIdx = 0
    private var snd: Option[CPSound] = tearSnd

    def getTearSound: Option[CPSound] = snd

    /**
      *
      * @param snd
      */
    def setTearSound(snd: Option[CPSound]): Unit = this.snd = snd

    /**
      *
      */
    def lineEffectNow(): Unit = forceLineEff = true

    /**
      *
      */
    def tearEffectNow(): Unit = forceTearEff = true

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
      * Toggles this shader effect on and off by calling either [[start()]] or [[stop()]] methods..
      *
      * @see [[start()]]
      * @see [[stop()]]
      */
    def toggle(): Unit = if go then stop() else start()

    /** @inheritdoc */
    override def render(ctx: CPSceneObjectContext, objRect: CPRect, inCamera: Boolean): Unit =
        if go then
            if !lineEffOn then lineEffOn = CPRand.randFloat() <= lineEffectProb
            if !tearEffOn then tearEffOn = CPRand.randFloat() <= tearEffectProb

            if forceLineEff then lineEffOn = true
            if forceTearEff then tearEffOn = true

            forceLineEff = false
            forceTearEff = false

            val canv = ctx.getCanvas

            if lineEffOn then
                if lineIdx > canv.yMax + LINE_EFF_SIZE then
                    lineEffOn = false
                    lineIdx = 0
                else
                    for (y <- lineIdx until (lineIdx - LINE_EFF_SIZE) by -1)
                        if canv.isValid(0, y) then
                            var x = 0
                            while (x <= canv.xMax)
                                val zpx = canv.getZPixel(x, y)
                                var px = zpx.px
                                px = px.withFg(px.fg.lighter(LINE_EFF_FACTOR * 2f))
                                if px.bg.isDefined then px = px.withBg(Option(px.bg.get.lighter(LINE_EFF_FACTOR)))
                                canv.drawPixel(px, x, y, zpx.z)
                                x += 1
                    lineIdx += 1

            if tearEffOn then
                val yIdx = CPRand.between(TEAR_LINE_NUM, canv.yMax)
                var d = 1
                for (y <- yIdx until (yIdx - TEAR_LINE_NUM) by -1)
                    var x = d
                    while (x <= canv.xMax)
                        if canv.isValid(x, y) && canv.isValid(x - d, y) then
                            val zpx = canv.getZPixel(x, y)
                            val px = zpx.px
                            canv.drawPixel(px, x - d, y, zpx.z)
                            canv.drawPixel(px.withChar(' '), x, y, zpx.z)
                        x += 1
                    d += 1
                if snd.isDefined then snd.get.play()
                tearEffOn = false



