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

/**
  * Fade in shader. 
  * 
  * This shader can be used for 'fade in' effect for the entire
  * camera frame or the individual scene object it is attached to. If used for entire
  * camera frame effect it can be attached to an off-screen sprite.
  *
  * @param entireFrame Whether apply to the entire camera frame or just the object this
  *     shader is attached to.
  * @param durMs Duration of the fade in effect in millis.
  * @param bgPx Background pixel to fade in from.
  * @param onFinish Optional callback to call when this shader finishes. Default is a no-op.
  * @param autoStart Whether to start shader right away. Default value is `true`.
  * @param skip Predicate allowing to skip certain pixel from the shader. Typically used to skip background
  *     or certain Z-index. Default predicate returns `false` for all pixels.
  * @see [[CPOffScreenSprite]]
  * @see [[CPFadeOutShader]]
  * @example See [[org.cosplay.examples.shader.CPShaderExample CPShaderExample]] class for the example of using shaders.      
  */
class CPFadeInShader(
    entireFrame: Boolean,
    durMs: Long,
    bgPx: CPPixel,
    onFinish: CPSceneObjectContext => Unit = _ => (),
    autoStart: Boolean = true,
    skip: (CPZPixel, Int, Int) => Boolean = (_, _, _) => false
) extends CPShader:
    private var frmCnt = 0
    private val maxFrmCnt = durMs / CPEngine.frameMillis
    private val bgBg = bgPx.bg.get
    private val bgFg = bgPx.fg
    private val crossOverBrightness = bgFg.brightness
    private var go = autoStart

    /**
      * Resets this shaders to its initial state starting its effect on the next frame.
      */
    def start(): Unit =
        frmCnt = 0
        go = true

    /**
      * Tests whether this shader is in progress or not.
      */
    def isFinished: Boolean = !go

    /** @inheritdoc */
    override def render(ctx: CPSceneObjectContext, objRect: CPRect, inCamera: Boolean): Unit =
        if go then
            if frmCnt < maxFrmCnt then
                if entireFrame || inCamera then
                    val rect = if entireFrame then ctx.getCameraFrame else objRect
                    val canv = ctx.getCanvas
                    rect.loop((x, y) => {
                        if canv.isValid(x, y) then
                            val zpx = canv.getZPixel(x, y)
                            val px = zpx.px
                            if px != bgPx && !skip(zpx, x, y) then
                                val px = zpx.px
                                val balance = frmCnt.toFloat / maxFrmCnt
                                val newFg = CPColor.mixture(bgFg, px.fg, frmCnt.toFloat / maxFrmCnt)
                                val newBg = px.bg match
                                    case Some(c) => Option(CPColor.mixture(bgBg, c, frmCnt.toFloat / maxFrmCnt))
                                    case None => None
                                var newPx = px.withFg(newFg).withBg(newBg)
                                val xc = if px.char == ' ' then newBg.getOrElse(newFg) else newFg
                                if xc.brightness <= crossOverBrightness then newPx = newPx.withChar(bgPx.char)
                                canv.drawPixel(newPx, x, y, zpx.z)
                    })
                    frmCnt += 1
                    if frmCnt == maxFrmCnt then
                        go = false
                        onFinish(ctx)
