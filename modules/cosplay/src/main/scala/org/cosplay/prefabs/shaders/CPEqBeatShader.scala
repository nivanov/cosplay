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

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, Queue}

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
    private var go = false
    private var dur = 0L
    private var lastRenderMs = 0L
    private type Fun = Long => Float
    private val fun = buildFun()

    private def buildFun(): Fun =
        def getUri(src: String): URI =
            if CPUtils.isResource(src) then getClass.getClassLoader.getResource(src).toURI else URI.create(src)

        val af = AudioFile.openRead(getUri(snd.getOrigin).toURL.openStream())
        val numChs = af.numChannels
        val sndDur = snd.getTotalDuration
        val winMs = 10 // 10ms worth of samples, ~3 data points per game frame.
        val bufSz = (af.sampleRate.round / 1_000 * winMs).toInt
        val ampSeq = ArrayBuffer.empty[Float]
        val buf = af.buffer(bufSz)

        var frames = af.numFrames.toInt
        while frames > 0 do
            val sz = math.min(bufSz, frames)
            af.read(buf, 0, sz)
            ampSeq += (buf.map(_.sum / sz).sum / numChs).toFloat
            frames -= sz

        val maxAmp = ampSeq.max
        val minAmp = ampSeq.min
        val gain = maxAmp - minAmp
        val shift = minAmp / gain
        val normAmpSeq = ampSeq.map(_ / gain - shift)
        val sz = normAmpSeq.size

        val ringSz = 100 // Look back 100 data points (~1s).
        val ring = new mutable.Queue[Float](ringSz)
        var sum = 0f

        (ms: Long) => {
            val threshold = if ring.isEmpty then 0 else sum / ring.size
            val br = normAmpSeq(((ms % sndDur) / winMs).toInt.min(sz - 1))
            if ring.size == ringSz then sum -= ring.dequeue()
            ring.enqueue(br)
            sum += br
            if br < threshold then br else 1
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
            val now = ctx.getFrameMs
            dur += (now - lastRenderMs).max(0L)
            lastRenderMs = now
            val brightness = fun(dur)
            val canv = ctx.getCanvas
            objRect.loop((x, y) => {
                if canv.isValid(x, y) then
                    val zpx = canv.getZPixel(x, y)
                    val px = zpx.px
                    if px.char != ' ' then canv.drawPixel(px.withLighterFg(brightness), x, y, zpx.z)
            })



