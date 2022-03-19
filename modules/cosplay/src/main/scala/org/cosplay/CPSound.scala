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

package org.cosplay

import javafx.animation.{KeyFrame, KeyValue, Timeline}
import javafx.scene.media.*
import javafx.scene.media.MediaPlayer.Status
import javafx.util.*
import CPSound.*
import impl.{CPContainer, CPUtils}

import java.io.File
import java.net.URL
import java.util.concurrent.CountDownLatch
import scala.collection.{immutable, mutable}

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
  * Sound clip container.
  *
  * Sounds object requires URI of the sound file in one of the supported formats: `AIFF`, `AU` or `WAV`.
  * Sound object is immutable but its playback can be controlled with changing volume, balance, rate, fade in
  * and out. Sounds are inherently asynchronous objects, you can have many sounds objects, all of them
  * are independent of each other, and you can play them in parallel.
  *
  * Sound is an asset. Just like other assets such as [[CPFont fonts]], [[CPImage images]], [[CPAnimation animations]] or
  * [[CPVideo videos]] they are not managed or governed by the CosPlay game engine unlike [[CPScene scenes]] and [[CPSceneObject scene objects]]
  * that are managed and governed by the game engine. Assets are typically created outside the game loop and
  * managed by the developer, they can be freely shared between scenes or scene objects as any other standard
  * Scala objects.
  *
  * **NOTE**: game engine must be [[CPEngine.init() initialized]] before sounds object can be created.
  *
  * @param src RFC-2396 URI as required by `java.net.URI` or 'resource' file. URI should point to a sound file in
  *     one of the supported format: `AIFF`, `AU` or `WAV`. Only HTTP, FILE, and JAR URIs are supported.
  * @param tags Optional set of organizational tags. Default is an empty set.
  * @note See https://freesound.org/ for the excellent source of royalty free sounds and audio-fx.
  * @example See [[org.cosplay.examples.sound.CPSoundExample CPSoundExample]] class for the example of
  *     using sounds.
  */
class CPSound(src: String, tags: Set[String] = Set.empty) extends CPGameObject(tags = tags) with CPAsset:
    private var timeline: Timeline = _
    private val player =
        if !CPEngine.isInit then
            E("CosPlay engine must be initialized (CPEngine.init(...) method) before sound can be created.")
        else
            val latch = new CountDownLatch(1)
            try
                val impl = new MediaPlayer(new Media(getUri))
                // Wait & load the sounds media.
                impl.setOnReady(new Runnable():
                    override def run(): Unit = latch.countDown()
                )
                latch.await()
                impl
            catch case e: Exception => E(s"Failed to load sound media: $src", e)
    private var vol = player.getVolume
    private val totalDur = player.getTotalDuration.toMillis.toLong

    /** @inheritdoc */
    override val getOrigin: String = src

    /**
      *
      */
    private def getUri: String =
        if CPUtils.isResource(src) then getClass.getClassLoader.getResource(src).toURI.toString else src

    /**
      *
      */
    private def init(): Unit = tracks.synchronized(tracks.add(this))

    /**
      * Gets current playback rate. Playback rate is clamped to the range [0.0, 8.0].
      */
    def getRate: Double = player.getRate

    /**
      * Sets current playback rate. Playback rate is clamped to the range [0.0, 8.0].
      *
      * @param rate Playback rate to set.
      */
    def setRate(rate: Double): Unit = player.setRate(rate)

    /**
      * Gets current audio balance. Audio balance is clamped to the range [-1.0, 1.0].
      */
    def getBalance: Double = player.getBalance

    /**
      * Sets the audio balance. Its effect will be clamped to the range [-1.0, 1.0].
      *
      * @param bal Audio balance to set.
      */
    def setBalance(bal: Double): Unit = player.setBalance(bal)

    /**
      * Starts the playback with specified fade in duration.
      * When playback reaches the end the player will rewind back again to the beginning and stop.
      *
      * @param fadeInMs Fade in duration in milliseconds. Default is zero.
      */
    def play(fadeInMs: Long = 0): Unit =
        player.setOnEndOfMedia(() => {
            seek(0) // Force rewind.
            player.stop()
        })
        if fadeInMs > 0 then fadeIn(fadeInMs) else player.play()

    /**
      * Pauses current playback.
       */
    def pause(): Unit = player.pause()

    /**
      * Rewinds the live media playback to the zero position. No effect
      * if the playback is not playing.
      */
    def rewind(): Unit = seek(0)

    /**
      * Sets start time of the playback to the given millisecond mark.
      *
      * @param ms Millisecond mark to set the start position to.
      */
    def seek(ms: Long): Unit =
        val dur = Duration.millis(ms.toDouble)
        player.seek(dur)

    /**
      * Gets current playback time in milliseconds.
      */
    def getCurrentTime: Long = player.getCurrentTime.toMillis.toLong

    /**
      * Gets media total duration in milliseconds.
      */
    def getTotalDuration: Long = totalDur

    /**
      * Starts the looping playback from the start of the media.
      *
      * @param fadeInMs Fade in duration in milliseconds.
      * @param endFun Optional callback to call when end of media is reached. Default is a no-op function.
      */
    def loopAll(fadeInMs: Long, endFun: CPSound => Unit = (_: CPSound) => ()): Unit =
        loop(fadeInMs, 0, totalDur, endFun)

    /**
      * Starts the looping playback.
      *
      * @param fadeInMs Fade in duration in milliseconds.
      * @param startMs Millisecond mark for the loop start.
      * @param endMs Millisecond mark for the loop end.
      * @param endFun Optional callback to call when end of media is reached. Default is a no-op function.
      */
    def loop(fadeInMs: Long, startMs: Long, endMs: Long, endFun: CPSound => Unit = (_: CPSound) => ()): Unit =
        player.setStartTime(Duration.millis(startMs.toDouble))
        player.setStopTime(Duration.millis(endMs.toDouble))
        player.setOnEndOfMedia(() => {
            endFun(this)
            seek(startMs)
            player.play()
        })
        if fadeInMs > 0 then fadeIn(fadeInMs) else player.play()

    /**
      *
      */
    private def stopTimeline(): Unit = if timeline != null then timeline.stop()

    /**
      * Fades in the playback at the current mark.
      *
      * @param fadeInMs Fade in duration in milliseconds.
      */
    def fadeIn(fadeInMs: Long): Unit =
        stopTimeline()
        val targetVol = player.getVolume
        timeline = new Timeline(
            new KeyFrame(
                Duration.millis(fadeInMs.toDouble),
                new KeyValue(player.volumeProperty(), targetVol)
            )
        )
        player.setVolume(0)
        player.play()
        timeline.play()

    /**
      * Gets current audio volume. Audio volume range is [0.0, 1.0].
      */
    def getVolume: Double = vol

    /**
      * Sets current audio volume. Audio volume range is [0.0, 1.0].
      *
      * @param vol Audio volume to set.
      */
    def setVolume(vol: Double): Unit =
        this.vol = vol
        player.setVolume(vol)

    /**
      * Adjusts the audio volume by given `delta`.
      *
      * @param delta Negative or positive delta to adjust the volume.
      */
    def adjustVolume(delta: Float): Unit = setVolume(vol + delta)

    /**
      * Fades in stopped or fades out the playing audio.
      *
      * @param fadeMs Fade in or fade out duration in milliseconds. Default is zero.
      */
    def toggle(fadeMs: Long = 0): Unit = if isPlaying then stop(fadeMs) else play(fadeMs)

    /**
      * Stops the playback.
      *
      * @param fadeOutMs Fade out duration in milliseconds. Default is zero.
      */
    def stop(fadeOutMs: Long = 0): Unit =
        stopTimeline()
        def end(): Unit =
            player.setStartTime(Duration.millis(0))
            player.setStopTime(Duration.millis(totalDur.toDouble))
            player.stop() // Also rewinds back.
            player.setOnEndOfMedia(null) // Stop looping, if any.
            player.setVolume(vol) // Restore the volume.

        if fadeOutMs <= 0 then end()
        else // Fade out.
            timeline = new Timeline(
                new KeyFrame(
                    Duration.millis(fadeOutMs.toDouble),
                    new KeyValue(player.volumeProperty(), 0)
                )
            )
            timeline.setOnFinished(_ => end())
            timeline.play()

    /**
      * Tests whether or not the audio playback is on.
      */
    def isPlaying: Boolean = player.getStatus == MediaPlayer.Status.PLAYING

    /**
      * Disposes this sound object. After this call this sound cannot be used.
      */
    def dispose(): Unit =
        stopTimeline()
        player.stop()
        player.dispose()
        tracks.synchronized(tracks.remove(src))

    // Constructor.
    init()
/**
  * Companion object with utility functionality.
  */
object CPSound:
    private val tracks = new CPContainer[CPSound] {}

    /**
      * Shortcut constructor for the sound with specific volume.
      *
      * @param src RFC-2396 URI as required by `java.net.URI` or 'resource' file. URI should point to a sound file in
      *     one of the supported format: `AIFF`, `AU` or `WAV`. Only HTTP, FILE, and JAR URIs are supported.
      * @param vol Audio volume to set. Audio volume range is [0.0, 1.0].
      * @param tags Optional set of organizational tags. Default is an empty set.
      */
    def apply(src: String, vol: Double, tags: Set[String] = Set.empty): CPSound =
        val snd = new CPSound(src, tags)
        snd.setVolume(vol)
        snd

    /**
      * Shortcut constructor for the sound.
      *
      * @param src RFC-2396 URI as required by `java.net.URI` or 'resource' file. URI should point to a sound file in
      *     one of the supported format: `AIFF`, `AU` or `WAV`. Only HTTP, FILE, and JAR URIs are supported.
      */
    def apply(src: String): CPSound = new CPSound(src)

    /**
      * Stops all sounds playback in the system.
      *
      * @param fadeOutMs Fade out duration in milliseconds.
      * @param tags Optional set of tags to filter the sounds to stop. If not provided, all sounds
      *     will be stopped.
      * @see [[CPSound.stop()]]
      */
    def stopAll(fadeOutMs: Long, tags: String*): Unit = tracks.synchronized {
        if tags.isEmpty then tracks.values.foreach(_.stop(fadeOutMs))
        else tracks.getForTags(tags: _*).foreach(_.stop(fadeOutMs))
    }

    /**
      * Loops over all sounds call given function.
      *
      * @param f Function to call on each sounds.
      * @param tags Optional set of tags to filter the sounds to loop over. If not provided, all sounds
      *     will be used.
      */
    def foreach(f: CPSound => Unit, tags: String*): Unit =
        if tags.isEmpty then
            tracks.values.foreach(f)
        else
            tracks.getForTags(tags: _*).foreach(f(_))

    /**
      * Disposes all sounds.
      *
      * @param tags Optional set of tags to filter the sounds to dispose. If not provided, all sounds
      *     will be disposed.
      * @see [[CPSound.dispose()]]
      */
    def disposeAll(tags: String*): Unit = tracks.synchronized {
        if tags.isEmpty then immutable.HashSet.from(tracks.values).foreach(_.dispose())
        else immutable.HashSet.from(tracks.getForTags(tags: _*)).foreach(_.dispose())
    }