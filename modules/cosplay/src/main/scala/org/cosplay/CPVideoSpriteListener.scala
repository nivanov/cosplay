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
  * Listener for [[CPVideoSprite]] playback.
  *
  * You can add and remove this listener to video sprite using [[CPVideoSprite.addListener()]] and
  * [[CPVideoSprite.removeListener()]] methods.
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
  * Here's some useful links for ASCII video in general:
  *  - Use [[https://www.ffmpeg.org/]] or similar to convert video into separate still images.
  *  - Use [[https://github.com/cslarsen/jp2a]] or similar to convert individual JPGs into ASCII.
  *  - [[https://john.dev/b?id=2019-02-23-ascii-face]] provides full example of ASCII vide.
  *
  * @example See [[org.cosplay.examples.video.CPVideoExample CPVideoExample]] class for the example of
  *     using video support.
  * @see [[CPVideoSprite.addListener()]]
  * @see [[CPVideoSprite.removeListener()]]
  */
trait CPVideoSpriteListener:
    /**
      * Called on each video frame.
      *
      * @param vid Video that video sprite is playing.
      * @param frameIdx Video frame index.
      * @param frame Video frame.
      * @param playing Whether or not playback is playing.
      */
    def onFrame(vid: CPVideo, frameIdx: Int, frame: CPImage, playing: Boolean): Unit
