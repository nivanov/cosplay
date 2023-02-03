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

import CPKeyboardKey.*
import CPColor.*
import CPStyledString.*
import impl.CPUtils

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
  * Definition of the video.
  *
  * Video rendered in ASCII character is rather an acquired taste... However, when used carefully and tastefully
  * it can provide striking visuals for ASCII-based game. There are [[https://www.google.com/search?q=ascii+video plenty of tooling]]
  * available that can help you to convert a standard video like MP4 into a set of JPEG images and then convert these images into
  * ASCII art images. Once you have ASCII art images you can use CosPlay video support to playback that video.
  *
  * Video support consists of three key components:
  *  - [[CPVideo]]
  *  - [[CPVideoSprite]]
  *  - [[CPVideoSpriteListener]]
  *
  * Video is defined as a sequence of same-sized frames where each frame is an image. [[CPVideoSprite]] provides
  * rendering of that video while [[CPVideoSpriteListener]] allows the video playback to synchronize with
  * other action in the game like sound or animation.
  *
  * Video is an asset. Just like other assets such as [[CPFont fonts]], [[CPSound sounds]], [[CPAnimation animations]] or
  * [[CPVideo videos]] they are not managed or governed by the CosPlay game engine unlike [[CPScene scenes]] and [[CPSceneObject scene objects]]
  * that are managed and governed by the game engine. Assets are typically created outside the game loop and
  * managed by the developer, they can be freely shared between scenes or scene objects as any other standard
  * Scala objects.
  *
  * Here's some useful links for ASCII videos:
  *  - Use [[https://www.ffmpeg.org/]] or similar to convert video into separate still images.
  *  - Use [[https://github.com/cslarsen/jp2a]] or similar to convert individual JPGs into ASCII.
  *  - [[https://john.dev/b?id=2019-02-23-ascii-face]] provides full example of ASCII video.
  *
  * @param id ID for this video.
  * @param origin The origin of this video: file path or URL.
  * @param tags  Optional set of organizational tags. Default is an empty set.
  * @example See [[org.cosplay.examples.video.CPVideoExample CPVideoExample]] class for the example of
  *     using video support.
  */
abstract class CPVideo(id: String, origin: String, tags: Set[String] = Set.empty) extends CPGameObject(id, tags) with CPAsset:
    /** @inheritdoc */
    override val getOrigin: String = origin

    /**
      * Gets number of video frames in this video.
      */
    def getFrameCount: Int

    /**
      * Gets video frame dimension.
      */
    def getFrameDim: CPDim

    /**
      * Gets frame for given index.
      *
      * @param idx Video frame index to get.
      */
    def getFrame(idx: Int): CPImage

/**
  * Companion object provides utility functions.
  */
object CPVideo:
    private val DFLT_BG = CPPixel('.', C_GRAY2, C_GRAY1)

    /**
      * Previews given video using built-in video player.
      *
      * @param vid Video to preview.
      * @param bg Optional background pixel to use. Default value is {{{CPPixel('.', C_GRAY2, C_GRAY1)}}}.
      * @param emuTerm Whether or not to use terminal emulation. Default value is `true`.
      */
    def previewVideo(vid: CPVideo, bg: CPPixel = DFLT_BG, emuTerm: Boolean = true): Unit =
        var spr: CPVideoSprite = null

        /**
          *
          */
        def makeKbCtrl(): CPSceneObject = new CPOffScreenSprite:
            override def update(ctx: CPSceneObjectContext): Unit =
                require(spr != null)

                if ctx.getKbEvent.isDefined then
                    ctx.getKbEvent.get.key match
                        case KEY_LO_Q => ctx.exitGame() // Exit preview on 'Q' press.
                        case KEY_LO_R => spr.rewind()
                        case KEY_SPACE => spr.toggle()
                        case _ => ()

        val lblImg = new CPArrayImage(
            styleStr("[SPACE]", C_MEDIUM_PURPLE3) ++ styleStr("Play|Pause    ", C_GREEN1) ++
            styleStr("[R]", C_MEDIUM_PURPLE3) ++ styleStr("Rewind    ", C_GREEN1) ++
            styleStr("[Q]", C_MEDIUM_PURPLE3) ++ styleStr("Quit    ", C_GREEN1) ++
            styleStr("[CTRL-L]", C_MEDIUM_PURPLE3) ++ styleStr("Log    ", C_GREEN1) ++
            styleStr("[CTRL-Q]", C_MEDIUM_PURPLE3) ++ styleStr("FPS Overlay", C_GREEN1)
        ).trimBg()
        val vidDim = vid.getFrameDim
        val lblDim = lblImg.getDim
        val scDim = CPDim((vidDim.w + 8).max(lblDim.w + 4), vidDim.h + 8)

        CPEngine.init(
            CPGameInfo(
                name = s"Video Preview (${vid.getFrameCount} ${vidDim.w}x${vidDim.h} frames)",
                initDim = scDim.?
            ),
            emuTerm = emuTerm
        )
        spr = new CPVideoSprite("spr", vid, 4, 2, 0, 30, loop = true, collidable = false, autoPlay = true)
        try
            CPEngine.rootLog().info(s"Video preview [origin=${vid.getOrigin}, frameCount=${vid.getFrameCount}, frameDim=${vid.getFrameDim}, class=${vid.getClass.getName}]")
            CPEngine.startGame(new CPScene(
                "scene",
                scDim.?,
                bg,
                spr, // Video we are previewing.
                makeKbCtrl(),
                new CPStaticImageSprite((scDim.w - lblDim.w) / 2, scDim.h - 4, 0, lblImg)
            ))
        finally
            CPEngine.dispose()
