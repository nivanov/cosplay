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
import org.cosplay.CPSound.*
import org.cosplay.impl.*
import javafx.event.*

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
               All rights reserved.
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
        !>(CPEngine.isInit, "CosPlay engine must be initialized (CPEngine.init(...) method) before sound can be created.")

        val latch = new CountDownLatch(1)
        try
            val impl = new MediaPlayer(new Media(getUri))
            // Wait & load the sounds media.
            impl.setOnReady(
                new Runnable():
                    override def run(): Unit = latch.countDown()
            )
            latch.await()
            impl
        catch case e: Exception => raise(s"Failed to load sound media: $src", e.?)
    private var vol = player.getVolume
    private val totalDur = player.getTotalDuration.toMillis.toLong

    /** @inheritdoc */
    override val getOrigin: String = src

    private def getUri: String = if CPUtils.isResource(src) then getClass.getClassLoader.getResource(src).toURI.toString else src
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
      * Starts the playback with specified fade in duration. If previously paused, then playback
      * resumes where it was paused. If playback was stopped, playback starts from the start.
      * When playback reaches the end the player will rewind back to the beginning and stops.
      *
      * @param fadeInMs Fade in duration in milliseconds. Default is zero.
      * @param endFun Optional callback to call when end of media is reached. Default is a no-op function.
      */
    def play(fadeInMs: Long = 0, endFun: CPSound => Unit = (_: CPSound) => ()): Unit =
        val it = this
        player.setOnEndOfMedia(
            new Runnable():
                override def run(): Unit =
                    seek(0) // Force rewind.
                    player.stop()
                    CPUtils.par(() => endFun(it))
        )
        if fadeInMs > 0 then fadeIn(fadeInMs) else player.play()

    /**
      * Stops the playback first, rewinds and then starts the playback with specified fade in duration.
      * If previously paused or stopped, then playback resumes from the start.
      * When playback reaches the end the player will rewind back to the beginning and stops.
      *
      * @param fadeInMs Fade in duration in milliseconds. Default is zero.
      */
    def replay(fadeInMs: Long = 0): Unit =
        stop()
        play(fadeInMs)

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
    def loop(fadeInMs: Long, endFun: CPSound => Unit = (_: CPSound) => ()): Unit =
        val it = this
        player.setOnEndOfMedia(
            new Runnable():
                override def run(): Unit =
                    endFun(it)
                    seek(0)
                    player.play()
        )
        if fadeInMs > 0 then fadeIn(fadeInMs) else player.play()

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
      * Fades in stopped or pauses the playing audio.
      *
      * @param fadeInMs Fade in duration in milliseconds. Default is zero.
      */
    def toggle(fadeInMs: Long = 0): Unit = if isPlaying then pause() else play(fadeInMs)

    /**
      * Stops the playback.
      *
      * @param fadeOutMs Fade out duration in milliseconds. Default is zero.
      */
    def stop(fadeOutMs: Long = 0): Unit =
        stopTimeline()
        def end(): Unit =
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
            timeline.setOnFinished(new EventHandler[ActionEvent] {
                override def handle(t: ActionEvent): Unit = end()
            })
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
    def stopAll(fadeOutMs: Long, tags: Seq[String]): Unit = tracks.synchronized {
        if tags.isEmpty then tracks.values.foreach(_.stop(fadeOutMs))
        else tracks.getForTags(tags).foreach(_.stop(fadeOutMs))
    }

    /**
      * Loops over all sounds in the system calling given function.
      *
      * @param f Function to call on each sounds.
      * @param tags Optional set of tags to filter the sounds to loop over. If not provided, all sounds
      *     will be used.
      */
    def foreach(f: CPSound => Unit, tags: String*): Unit =
        if tags.isEmpty then
            tracks.values.foreach(f)
        else
            tracks.getForTags(tags).foreach(f(_))

    /**
      * Disposes all sounds.
      *
      * @param tags Optional set of tags to filter the sounds to dispose. If not provided, all sounds
      *     will be disposed.
      * @see [[CPSound.dispose()]]
      */
    def disposeAll(tags: Seq[String]): Unit = tracks.synchronized {
        if tags.isEmpty then immutable.HashSet.from(tracks.values).foreach(_.dispose())
        else immutable.HashSet.from(tracks.getForTags(tags)).foreach(_.dispose())
    }

