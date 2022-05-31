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

package org.cosplay.games.mir.scenes

import org.cosplay.*
import games.mir.*
import prefabs.shaders.*
import CPSlideDirection.*
import scenes.shaders.*

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
  * @param id ID of the scene.
  * @param bgSndFile Background audio file name.
  */
abstract class CPMirCrtSceneBase(id: String, bgSndFile: String) extends CPScene(id, None, BG_PX):
    private val bgSnd = CPSound(s"$SND_HOME/$bgSndFile", 0.3f)
    private val turnOnSnd = CPSound(s"$SND_HOME/crt_turn_on.wav")
    private val tearSnd = CPSound(s"$SND_HOME/crt_tear.wav")
    private val knockSnd = CPSound(s"$SND_HOME/crt_knock.wav")
    private val noiseSnd = CPSound(s"$SND_HOME/crt_noise.wav")

    // Should be controlled by the subclass.
    protected val fadeInShdr: CPSlideInShader = CPSlideInShader.sigmoid(
        TOP_TO_BOTTOM,
        true,
        3500,
        bgPx = BG_PX,
        onFinish = _ ⇒ if stateMgr.state.crtVisual then crtShdr.start()
    )
    protected val fadeOutShdr: CPFadeOutShader = CPFadeOutShader(
        true,
        500,
        bgPx = BG_PX,
        onFinish = _ ⇒ if stateMgr.state.crtVisual then crtShdr.stop()
    )
    protected val crtShdr: CPOldCRTShader = new CPOldCRTShader(
        autoStart = false,
        overscanEffProb = stateMgr.state.crtOverscanProb,
        overscanFactor = stateMgr.state.crtOverscanFactor,
        tearEffProb = stateMgr.state.crtTearProb,
        tearSnd = if stateMgr.state.crtAudio then Option(tearSnd) else None
    )

    // Make sure to call 'super(...)'.
    override def onActivate(): Unit =
        // Start fade in.
        fadeInShdr.start()

        turnOnSnd.play()
        bgSnd.loop(2000)
        if stateMgr.state.crtAudio then
            crtShdr.setTearSound(Option(tearSnd))
            knockSnd.play()
            noiseSnd.loop(1000, _ ⇒ knockSnd.play())
        else
            crtShdr.setTearSound(None)

    // Make sure to call 'super(...)'.
    override def onDeactivate(): Unit =
        // Handles only audio.
        bgSnd.stop(500)
        noiseSnd.stop()


