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
  *
  */
class CPBorderShader(
    entireFrame: Boolean,
    width: Int,
    colorMixPerStep: Float,
    autoStart: Boolean = false,
    skip: (CPZPixel, Int, Int) => Boolean = (_, _, _) => false
) extends CPShader:
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
      * Toggles this shader effect on and off by calling either [[start()]] or [[stop()]] methods..
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
                def mixColor(c: CPColor): CPColor = if colorMix < 0 then c.darker(colorMix.abs) else c.lighter(colorMix)
                val zpx = canv.getZPixel(x, y)
                val px = zpx.px
                if px.bg.nonEmpty then
                    canv.drawPixel(px.withBg(Option(mixColor(px.bg.get))), x, y, zpx.z)

            for (d <- 0 until width)
                val mix = (width - d) * colorMixPerStep
                for (x <- (rect.xMin + d) to (rect.xMax - d))
                    updatePx(x, rect.yMin + d, mix)
                    updatePx(x, rect.yMax - d, mix)
                for (y <- (rect.yMin + d + 1) until (rect.yMax - d))
                    updatePx(rect.xMin + d, y, mix)
                    updatePx(rect.xMax - d, y, mix)