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

package org.cosplay.prefabs.sprites

import org.cosplay.*
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
               All rights reserved.
*/

/**
  * Image sprite that provides moving and fading out image effect.
  * This typically can be used for word or speech bubbles in the game and such.
  *
  * @param id Optional ID of the sprite.
  * @param img The image to render. It can be changed later.
  * @param initX Initial X-coordinate of the sprite.
  * @param initY Initial Y-coordinate of the sprite.
  * @param z Z-index at which to render the image.
  * @param dxf Function providing per-frame X-coordinate delta. Defines speed of movement on X-axis.
  * @param dyf Function providing per-frame Y-coordinate delta. Defines speed of movement on Y-axis.
  * @param bgPx Background pixel to fade out to.
  * @param durMs Duration of the shader effect in milliseconds.
  * @param onFinish Optional callback to call when the effect is finished. Default is a np-op.
  * @param autoDelete Optional flag on whether or not to auto-delete the sprite from its scene
  *     when the effect is finished. Default value is `true`.
  * @param tags Optional set of organizational or grouping tags. By default, the empty set is used.
  */
class CPBubbleSprite(
    id: String = s"bubble-img-spr-${CPRand.guid6}",
    img: CPImage,
    initX: Int,
    initY: Int,
    z: Int,
    dxf: CPSceneObjectContext => Float,
    dyf: CPSceneObjectContext => Float,
    bgPx: CPPixel,
    durMs: Long,
    onFinish: CPSceneObjectContext => Unit = _ => (),
    autoDelete: Boolean = true,
    tags: String*
) extends CPImageSprite(id, initX, initY, z, img, tags = tags: _*):
    private val shdrs = Seq(
        CPFadeOutShader(
            false,
            durMs,
            bgPx,
            autoStart = true,
            onFinish = ctx => {
                // Delete the sprite when shader is finished, if required.
                if autoDelete then ctx.deleteObject(id)
                onFinish(ctx)
            },
            skip = (zpx, _, _) => zpx.z > z // Don't modify above Z-layers.
        )
    )
    private var x: Float = initX.toFloat
    private var y: Float = initY.toFloat

    override def getShaders: Seq[CPShader] = shdrs
    override def update(ctx: CPSceneObjectContext): Unit =
        super.update(ctx)
        x += dxf(ctx)
        y += dyf(ctx)
        setX(x.round)
        setY(y.round)