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

import java.net.*
import java.io.*
import de.sciss.audiofile.*
import org.cosplay.impl.CPUtils

import scala.collection.mutable.ArrayBuffer

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
  *
  * @param snd
  */
class CPEqBeatShader(snd: CPSound) extends CPShader:
    private final val BUF_SZ = 4096

    private var go = false
    private var lastBrightness = 0F
    private var dur = 0L
    private var lastRenderMs = 0L
    private type Fun = Long => Float
    private val fun = buildFun()

    private def getUri(src: String): URI =
        if CPUtils.isResource(src) then getClass.getClassLoader.getResource(src).toURI else URI.create(src)
    private def buildFun(): Fun =
        val af = AudioFile.openRead(getUri(snd.getOrigin).toURL.openStream())
        val magSeq = ArrayBuffer[Float]()
        val buf = af.buffer(BUF_SZ)
        var remainFrames = af.numFrames.toInt

        while remainFrames > 0 do
            val chunkSz = math.min(BUF_SZ, remainFrames)
            af.read(buf, 0, chunkSz) // Read channels.
            buf.foreach(chan => magSeq += chan.map(math.abs).max.toFloat)
            remainFrames -= chunkSz

        val maxMag = magSeq.max.max(0.0001f)
        val normMagSeq = magSeq.map(_ / maxMag)
        val magDurMs = snd.getTotalDuration / magSeq.size
        val sz = normMagSeq.size

        (ms: Long) => {
            val normMs = ms % snd.getTotalDuration
            var idx1 = (normMs / magDurMs).toInt
            if idx1 >= sz - 1 then idx1 = 1
            val idx2 = idx1 + 1
            val y1 = normMagSeq(idx1)
            val y2 = normMagSeq(idx2)
            val t1 = idx1 * magDurMs
            val t2 = idx2 * magDurMs
            val a = (y2 - y1) / (t2 - t1).toFloat
            val b = y1 - a * t1

            // Return value.
            normMs * a + b
        }

    /**
      * Starts the shader effect.
      *
      * @see [[toggle()]]
      */
    def start(): Unit =
        go = true
        dur = 0L
        lastRenderMs = System.currentTimeMillis()

    /**
      * Stops the shader effect.
      *
      * @see [[toggle()]]
      */
    def stop(): Unit =
        go = false

    /**
      * Toggles this shader effect on and off by calling either [[start()]] or [[stop()]] methods.
      *
      * @see [[start()]]
      * @see [[stop()]]
      */
    def toggle(): Unit = if go then stop() else start()

    /**
      * Tests whether this shader is in progress or not.
      */
    def isActive: Boolean = go

    override def render(ctx: CPSceneObjectContext, objRect: CPRect, inCamera: Boolean): Unit =
        if go then
            var brightness = fun(dur)
            // For zero brightness we gradually reduce it to get dimming effect.
            if brightness == 0F then brightness = (lastBrightness - 0.2).max(0).toFloat
            lastBrightness = brightness
            dur += ctx.getFrameMs - lastRenderMs
            lastRenderMs = ctx.getFrameMs
            val canv = ctx.getCanvas
            objRect.loop((x, y) => {
                if canv.isValid(x, y) then
                    val zpx = canv.getZPixel(x, y)
                    val px = zpx.px
                    if px.char != ' ' then canv.drawPixel(px.withLighterFg(brightness), x, y, zpx.z)
            })



