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

package org.cosplay.games.pong

import org.cosplay.*
import games.*
import CPColor.*
import CPArrayImage.*
import CPPixel.*
import CPKeyboardKey.*
import prefabs.shaders.*
import prefabs.sprites.*
import games.pong.shaders.*

/*
   _________            ______________
   __  ____/_______________  __ \__  /_____ _____  __
   _  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
   / /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
   \____/  \____//____/ /_/     /_/  \__,_/ _\__, /
                                            /____/

          2D ASCII JVM GAME ENGINE FOR SCALA3
              (C) 2021 Rowan Games, Inc.
                All rights reserved.
*/

/**
  * Pong game title scene.
  */
object CPPongTitleScene extends CPScene("title", None, BG_PX):
    private val introSnd = CPSound(s"sounds/games/pong/intro.wav", 0.3f)
    private val logoImg = CPImage.loadRexXp("images/games/pong/pong_logo.xp").trimBg()
    private val sparkleShdr = CPSparkleShader(true, CS, autoStart = true, skip = (zpx, _, _) => zpx.px != BG_PX)
    private val fadeInShdr = CPSlideInShader(CPSlideDirection.CENTRIFUGAL, true, 3000, BG_PX)
    private val fadeOutShdr = CPSlideOutShader(CPSlideDirection.CENTRIPETAL, true, 500, BG_PX)

    // Add scene objects...
    addObjects(
        // Main logo.
        CPCenteredImageSprite(img = logoImg, 0),
        // Add all screen shaders.
        new CPOffScreenSprite(shaders = Seq(fadeInShdr, fadeOutShdr, sparkleShdr)),
        // Exit on 'Q' press.
        CPKeyboardSprite(KEY_LO_Q, _.exitGame()),
        // Toggle audio on 'CTRL+A' press.
        CPKeyboardSprite(KEY_CTRL_A, _ => toggleAudio()),
        // Transition to the next scene on 'Enter' press.
        CPKeyboardSprite(KEY_ENTER, _ => fadeOutShdr.start(_.switchScene("play")))
    )

    private def startBgAudio(): Unit = introSnd.loop(2000)
    private def stopBgAudio(): Unit = introSnd.stop(400)

    /**
      * Toggles audio on and off.
      */
    private def toggleAudio(): Unit =
        if audioOn then
            stopBgAudio()
            audioOn = false
        else
            startBgAudio()
            audioOn = true

    override def onActivate(): Unit =
        fadeInShdr.start() // Reset the shader.
        if audioOn then startBgAudio()

    override def onDeactivate(): Unit =
        stopBgAudio()
        sparkleShdr.stop()
