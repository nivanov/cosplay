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

package org.cosplay.prefabs.scenes

import org.cosplay.*
import CPArrayImage.*
import CPPixel.*
import CPColor.*
import org.cosplay.prefabs.shaders.*

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
  * A scene that displays CosPlay logo with shimmering colors for a few seconds.
  *
  * @param id ID of the scene.
  * @param dim Optional dimension of the scene. Note that if dimension is `None` then scene will adapt to the
  *     terminal dimension on each frame. That means that the scene's canvas on which all scene objects are
  *     rendered can change its size from frame to frame. In such case, make sure that all scene objects take this into
  *     account in their rendering routines.
  * @param bgPx Background pixel of the scene. Background pixel is shown when none of the scene objects
  *     has drawn a pixel at that particular coordinate.
  * @param colors Logo will shimmer with these colors. Typically, these should be the game's primary colors.
  * @param nextSc ID of the next scene to switch to once this scene has finished its shimmering logo effect.
  */
class CPLogoScene(id: String, dim: Option[CPDim], bgPx: CPPixel, colors: Seq[CPColor], nextSc: String) extends CPScene(id, dim, bgPx):
    require(colors.nonEmpty, "Color sequence cannot be empty.")

    private val initFg = bgPx.bg.getOrElse(bgPx.fg)
    private val logoImg = CPArrayImage(
        prepSeq(
            """
              |POWERED BY
              |
              |_________            ______________
              |__  ____/_______________  __ \__  /_____ _____  __
              |_  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
              |/ /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
              |\____/  \____//____/ /_/     /_/  \__,_/ _\__, /
              |                                         /____/
            """),
        (ch, _, _) => ch&initFg
    ).replaceBg(bgPx)

    // Skip background & space pixels from shaders' effect.
    val skipFn: (CPZPixel, Int, Int) => Boolean = (zpx: CPZPixel, _, _) => {
        val px = zpx.px
        px.char == ' ' || px == bgPx
    }

    // Shaders to use.
    private val shimmerShdr = new CPShimmerShader(false, colors, 2, true, skipFn)
    private val foShdr = new CPFadeOutShader(false, 750, bgPx, _.switchScene(nextSc), false, skipFn)
    private val fiShdr = new CPFadeInShader(false, 1500, bgPx, _ => foShdr.start(), true, skipFn)

    // Main logo sprite with 3 shaders.
    private val logoSpr = new CPImageSprite("logo", 0, 0, 0, logoImg, false, Seq(shimmerShdr, fiShdr, foShdr)):
        override def update(ctx: CPSceneObjectContext): Unit =
            // Center the logo on each frame (ensuring the support for adaptive scenes).
            val canv = ctx.getCanvas
            setX((canv.dim.w - getImage.w) / 2)
            setY((canv.dim.h - getImage.h) / 2)

    // Add sprite to this scene.
    addObjects(logoSpr)