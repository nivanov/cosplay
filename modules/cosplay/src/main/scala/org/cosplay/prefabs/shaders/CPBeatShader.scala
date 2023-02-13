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
  * Smoothing modes for the beat shader.
  */
enum CPBeatShaderSmoothing:
    /** Smoothes brightness both below and above beat threshold. */
    case SMOOTH_BOTH
    /** Binary (blinking) mode where brightness is either on or off depending on the threshold. */
    case SMOOTH_NONE
    /** Smoothes brightness only above beat threshold. */
    case SMOOTH_UP
    /** Smoothes brightness only below beat threshold. */
    case SMOOTH_DOWN

import CPBeatShaderSmoothing.*

/**
  * A shader that changes brightness of its object based on the perceived beat of the given sound. This shader uses
  * a simple beat-detection algorithm based on calculating root mean square (RMS) of amplitudes across all channels.
  * RMS is constantly recalculated over a sliding window of the past few seconds. If the current amplitude value is
  * above the RMS the beat is detected.
  *
  * It is important to note that this shader does not work by default for all types of sounds. For each particular
  * sound profile you may need to "play" with smoothing and threshold gain to achieve the best visual effect.
  *
  * Note that by default this shader is inactive and user must call [[start()]] method to start this shader.
  *
  * @param snd Sound asset to analyze for the beat detection. Note that the start of playing this sound should
  *     generally be synchronized in time with calling [[start()]] method on this shader. If not provided - ensure
  *     that method [[setSound()]] is called before this shader is started.
  * @param smoothing Smoothing mode. Default value is [[SMOOTH_UP]].
  * @param thresholdGain A constant that can be used to fine tune the calculated RMS-based threshold. Once threshold is
  *     calculated it will be multiplied by this constant. Default value is `1.0f`. Typical fine tuning values
  *     are between `0.5f` and `0.9f`.
  * @param thresholdTailMs Duration of the sliding window in milliseconds that is used for calculating RMS-based
  *     threshold.
  */
class CPBeatShader(
    snd: Option[CPSound] = None,
    smoothing: CPBeatShaderSmoothing = SMOOTH_UP,
    thresholdGain: Float = 1.0f,
    thresholdTailMs: Int = 2000
) extends CPShader:
    private var go = false
    private var dur = 0L
    private var lastRenderMs = 0L
    private type Fun = Long => Float
    private var fun: Option[Fun] = if snd.isDefined then buildFun(snd.get).? else None

    private def buildFun(snd: CPSound): Fun =
        def getUri(src: String): URI =
            if CPUtils.isResource(src) then getClass.getClassLoader.getResource(src).toURI else URI.create(src)

        val af = AudioFile.openRead(new BufferedInputStream(getUri(snd.getOrigin).toURL.openStream()))
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

        // Bringing amplitudes to [0,1] range.
        val maxAmp = ampSeq.max
        val minAmp = ampSeq.min
        val gain = maxAmp - minAmp
        val shift = minAmp / gain
        val normAmpSeq = ampSeq.map(_ / gain - shift)
        val sz = normAmpSeq.size

        // Bounded FIFO queue for threshold calculation.
        val queueCap = thresholdTailMs / winMs
        val queue = new mutable.Queue[Float](queueCap)
        // Manually maintain the sum of the elements in the queue for RMS calculation.
        var sum = 0f

        (ms: Long) => {
            // Calculate RMS of the elements in the queue..
            val threshold = (if queue.isEmpty then 0 else math.sqrt(sum / queue.size)) * thresholdGain
            val br = normAmpSeq(((ms % sndDur) / winMs).toInt.min(sz - 1))
            // Maintain constant max size of the FIFO queue.
            if queue.size == queueCap then
                val x = queue.dequeue()
                sum -= x * x
            queue.enqueue(br)
            sum += br * br
            smoothing match
                case SMOOTH_BOTH => br
                case SMOOTH_NONE => if br < threshold then 0 else 1
                case SMOOTH_UP => if br < threshold then 0 else br
                case SMOOTH_DOWN => if br < threshold then br else 1
        }

    /**
      * Starts the shader effect. Note that the start of playing the sound this shader is configured with must be
      * generally synchronized in time with calling this method.
      *
      * @see [[toggle()]]
      */
    def start(): Unit =
        !>(fun.nonEmpty, s"Sound asset must be set before shader can start.")
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

    /**
      * Sets given sound asset for this shader. Note that if this shader is currently active it will be stopped
      * by calling method [[stop()]] first. After the sound is set you need to call method [[start()]] to active
      * this shader. Note that the start of playing this sound should generally be synchronized in time with
      * calling [[start()]] method on this shader.
      *
      * @param snd
      */
    def setSound(snd: CPSound): Unit =
        if go then stop()
        fun = buildFun(snd).?

    /** @inheritdoc */
    override def render(ctx: CPSceneObjectContext, objRect: CPRect, inCamera: Boolean): Unit =
        if go then
            val now = ctx.getFrameMs
            dur += (now - lastRenderMs).max(0L)
            lastRenderMs = now
            val brightness = fun.get(dur)
            val canv = ctx.getCanvas
            objRect.loop((x, y) => {
                if canv.isValid(x, y) then
                    val zpx = canv.getZPixel(x, y)
                    val px = zpx.px
                    if px.char != ' ' then canv.drawPixel(px.withLighterFg(brightness), x, y, zpx.z)
            })



