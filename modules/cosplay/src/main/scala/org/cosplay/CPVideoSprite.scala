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

import org.cosplay.impl.CPUtils

import scala.collection.mutable

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
  * Scene object tailor-made for rendering [[CPVideo videos]].
  *
  * Video support consists of three key components:
  *  - [[CPVideo]]
  *  - [[CPVideoSprite]]
  *  - [[CPVideoSpriteListener]]
  *
  * Video is defined as a sequence of same-sized frames where each frame is an image. [[CPVideoSprite]] provides
  * rendering of that video while [[CPVideoSpriteListener]] allows the video playback to synchronize with
  * other action in the game like sound or animation. Note that video sprite does not provide any playback
  * controls out of the box.
  *
  * ### Sprites
  * CosPlay provides number of built-in sprites. A sprite is a scene objects, visible or off-screen,
  * that is custom designed for a particular use case. Built-in sprites provide concrete
  * implementations for the abstract methods in the base [[CPSceneObject]] class. Most non-trivial games
  * will use combination of the built-in sprites and their own ones. Here's the list of the built-in
  * sprites:
  *  - [[CPCanvasSprite]]
  *  - [[CPImageSprite]]
  *  - [[CPStaticImageSprite]]
  *  - [[CPLabelSprite]]
  *  - [[CPOffScreenSprite]]
  *  - [[CPKeyboardSprite]]
  *  - [[CPParticleSprite]]
  *  - [[CPVideoSprite]]
  *  - [[CPTextInputSprite]]
  *
  * Here's some useful links for ASCII video in general:
  *  - Use [[https://www.ffmpeg.org/]] or similar to convert video into separate still images.
  *  - Use [[https://github.com/cslarsen/jp2a]] or similar to convert individual JPGs into ASCII.
  *  - [[https://john.dev/b?id=2019-02-23-ascii-face]] provides full example of ASCII vide.
  *
  * @param id Optional ID of the sprite.
  * @param vid Video to render.
  * @param x Initial X-coordinate of the sprite.
  * @param y Initial Y-coordinate of the sprite.
  * @param z Z-index at which to render the image.
  * @param fps Frame-per-second to use in rendering the video.
  * @param autoPlay Whether to autoplay the video.
  * @param loop Whether or not to loop the playback.
  * @param collidable Whether or not this sprite has a collision shape. Default is `false`.
  * @param shaders Optional sequence of shaders for this sprite. Default value is an empty sequence.
  * @example See [[org.cosplay.examples.video.CPVideoExample CPVideoExample]] class for the example of
  *     using video support.
  * @see [[CPSceneObjectContext.getCanvas]] to get current canvas you can draw on.
  * @see [[CPCanvas]] various API to draw on the canvas.
  */
//noinspection ScalaWeakerAccess
class CPVideoSprite(
    id: String = s"video-spr-${CPRand.guid6}",
    vid: CPVideo,
    x: Int,
    y: Int,
    z: Int,
    fps: Int,
    autoPlay: Boolean,
    loop: Boolean,
    collidable: Boolean = false,
    shaders: Seq[CPShader] = Seq.empty) extends CPSceneObject(id):
    private val reg = mutable.HashSet.empty[CPVideoSpriteListener]
    private var playing = autoPlay
    private var frameIdx = 0
    private val frameDim = vid.getFrame(0).getDim
    private val frameMs = 1_000 / fps
    private var lastFrameMs = 0L
    private val sprRect = new CPRect(x, y, frameDim)
    private val clsRect = Option.when(collidable)(getRect)

    /** @inheritdoc */
    override def getX: Int = x
    /** @inheritdoc */
    override def getY: Int = y
    /** @inheritdoc */
    override def getZ: Int = z
    /** @inheritdoc */
    override def getDim: CPDim = frameDim
    /** @inheritdoc */
    override def getRect: CPRect = sprRect
    /** @inheritdoc */
    override def getCollisionRect: Option[CPRect] = clsRect
    /** @inheritdoc */
    override def getShaders: Seq[CPShader] = shaders
    /** @inheritdoc */
    override def render(ctx: CPSceneObjectContext): Unit =
        val frame = vid.getFrame(frameIdx)
        ctx.getCanvas.drawImage(frame, getX, getY, getZ)
        reg.foreach(_.onFrame(vid, frameIdx, frame, playing))
        if playing then
            val now = ctx.getFrameMs
            if now - lastFrameMs > frameMs then
                lastFrameMs = now
                frameIdx =
                    if frameIdx == vid.getFrameCount - 1 then
                        if loop then 0 else frameIdx
                    else
                        frameIdx + 1

    /**
      * Adds video playback listener.
      *
      * @param lst Listener to add.
      */
    def addListener(lst: CPVideoSpriteListener): Unit = reg += lst

    /**
      * Removes video playback listener.
      * @param lst Listener to remove.
      */
    def removeListener(lst: CPVideoSpriteListener): Unit = reg -= lst

    /**
      * Starts playback.
      */
    def startPlayback(): Unit = playing = true

    /**
      * Stops playback.
      */
    def stopPlayback(): Unit = playing = false

    /**
      * Tests whether or not playback is playing.
      */
    def isPlaying: Boolean = playing

    /**
      * Toggles playback.
      */
    def toggle(): Unit = if playing then stopPlayback() else startPlayback()

    /**
      * Gets current video frame index.
      */
    def getFrameIndex: Long = frameIdx

    /**
      * Rewinds the playback back to the beginning.
      */
    def rewind(): Unit = seek(0)

    /**
      * Seeks playback to given vide frame index.
      *
      * @param idx Video frame index to seek.
      */
    def seek(idx: Int): Unit = frameIdx = idx