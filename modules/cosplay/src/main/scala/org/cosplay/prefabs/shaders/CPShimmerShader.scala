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

          2D ASCII JVM GAME ENGINE FOR SCALA3
              (C) 2021 Rowan Games, Inc.
                ALl rights reserved.
*/

/**
  * Color shimmer shader.
  *
  * This shader provides color shimmer (or sparkling) effect for the entire
  * camera frame or the individual scene object it is attached to. If used for entire
  * camera frame effect it can be attached to an off-screen sprite.
  *
  * @param entireFrame Whether apply to the entire camera frame or just the object this
  *     shader is attached to.
  * @param colors Sequence of colors to use for shimmering effect. Colors will be selected randomly from this set.
  * @param keyFrame nth-frame to render the effect. For example, if key frame is `5` than the colors will
  *     change on each 5th frame and remain the same on all subsequent frames until next key frame is reached.
  * @param autoStart Whether to start shader right away. Default value is `false`.
  * @param skip Predicate allowing to skip certain pixel from the shader. Typically used to skip background
  *     or certain Z-index. Default predicate returns `false` for all pixels.
  * @param durMs Duration of the effect in milliseconds. By default, the effect will go forever.
  * @param onDuration Optional callback to call when this shader finishes by exceeding the duration
  *     specified by `durMs` parameter. Default is a no-op.
  *
  * @see [[CPFadeInShader]]
  * @see [[CPFadeOutShader]]
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
    private var go = autoStart
    private var lastImg: CPImage = _
    private var startMs: Long = 0

    if autoStart then start()

    /**
      * Starts the shader effect.
      */
    def start(): Unit =
        go = true
        lastImg = null
        startMs = System.currentTimeMillis()

    /**
      * Stops the shader effect without waiting for the duration. Note that `onDuration()` callback will not
      * be called in this case.
      */
    def stop(): Unit =
        go = false
        lastImg = null
        startMs = 0

    /**
      * Tests whether this shader is in progress or not.
      */
    def isFinished: Boolean = !go

    /** @inheritdoc */
    override def render(ctx: CPSceneObjectContext, objRect: CPRect, inCamera: Boolean): Unit =
        if go && System.currentTimeMillis() - startMs > durMs then
            stop()
            onDuration(ctx)
        if go then
            val canv = ctx.getCanvas
            if lastImg == null || ctx.getFrameCount % keyFrame == 0 then
                val rect = if entireFrame then ctx.getCameraFrame else objRect
                rect.loop((x, y) => {
                    val zpx = canv.getZPixel(x, y)
                    val px = zpx.px
                    if !skip(zpx, x, y) then
                        val rc = CPRand.rand(colors)
                        val newPx = if px.char == ' ' then px.withBg(Option(rc)) else px.withFg(rc)
                        canv.drawPixel(newPx, x, y, zpx.z)
                })
                lastImg = canv.capture(objRect)
            else
                canv.drawImage(lastImg, objRect.x, objRect.y, 0)



