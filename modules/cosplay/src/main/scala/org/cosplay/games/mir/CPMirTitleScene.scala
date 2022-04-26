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

package org.cosplay.games.mir

import org.cosplay.*
import CPArrayImage.*
import CPKeyboardKey.*
import CPPixel.*
import prefabs.shaders.*

/**
  *
  */
object CPMirTitleScene extends CPScene("title", None, BG_PX):
    private val logoImg = CPArrayImage(
        prepSeq(
            """
              | ______     ______     ______     ______     ______   ______
              |/\  ___\   /\  ___\   /\  ___\   /\  __ \   /\  == \ /\  ___\
              |\ \  __\   \ \___  \  \ \ \____  \ \  __ \  \ \  _-/ \ \  __\
              | \ \_____\  \/\_____\  \ \_____\  \ \_\ \_\  \ \_\    \ \_____\
              |  \/_____/   \/_____/   \/_____/   \/_/\/_/   \/_/     \/_____/
              |
              |                      ______   ______     ______     __    __
              |                     /\  ___\ /\  == \   /\  __ \   /\ "-./  \
              |                     \ \  __\ \ \  __<   \ \ \/\ \  \ \ \-./\ \
              |                      \ \_\    \ \_\ \_\  \ \_____\  \ \_\ \ \_\
              |         _____         \/_/     \/_/ /_/   \/_____/   \/_/  \/_/
              |     ,-:` \;',`'-,
              |   .'-;_,;  ':-;_,'.               __    __     __     ______
              |  /;   '/    ,  _`.-\             /\ "-./  \   /\ \   /\  == \
              | | '`. (`     /` ` \`|            \ \ \-./\ \  \ \ \  \ \  __<
              | |:.  `\`-.   \_   / |             \ \_\ \ \_\  \ \_\  \ \_\ \_\
              | |     (   `,  .`\ ;'|              \/_/  \/_/   \/_/   \/_/ /_/
              |  \     | .'     `-'/
              |   `.   ;/        .'
              |     `'-._____..'`           Copyright (C) 2022 Rowan Games, Inc
            """),
        (ch, _, _) => ch&FG
    ).trimBg()
    private val fadeInShdr = CPSlideInShader(CPSlideDirection.CENTRIFUGAL, true, 3000, BG_PX)
    private val fadeOutShdr = CPSlideOutShader(CPSlideDirection.CENTRIPETAL, true, 500, BG_PX)

    // Add scene objects...
    addObjects(
        // Main logo.
        CPCenteredImageSprite(img = logoImg, 0),
        // Add all screen shaders.
        new CPOffScreenSprite(shaders = Seq(fadeInShdr, fadeOutShdr)),
        // Exit on 'Q' press.
        CPKeyboardSprite(KEY_LO_Q, _.exitGame()),
        // Transition to the next scene on 'Enter' press.
        CPKeyboardSprite(KEY_ENTER, _ => fadeOutShdr.start(_.switchScene("play")))
    )

    override def onActivate(): Unit =
        super.onActivate()

    override def onDeactivate(): Unit =
        super.onDeactivate()
