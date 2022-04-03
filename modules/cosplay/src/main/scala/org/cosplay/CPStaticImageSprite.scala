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
               ALl rights reserved.
*/

/**
  * Scene object tailor-made for rendering static images.
  *
  * This sprite is very similar to [[CPImageSprite]] sprite and they both provide the same functionality.
  * However, for non-moving non-changing images this sprite provides shorter and simple API and better
  * performance.
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
  * @param id Optional ID of the sprite.
  * @param x X-coordinate of the sprite.
  * @param y Y-coordinate of the sprite.
  * @param z Z-index at which to render the image.
  * @param img The image to render.
  * @param collidable Whether or not this sprite has a collision shape. Default is `false`.
  * @param shaders Optional set of shaders for this sprite. Default value is an empty sequence.
  * @see [[CPImageSprite]]
  * @example See [[org.cosplay.examples.image.CPImageCarouselExample CPImageCarouselExample]] class for the example of
  *     using images.
  * @example See [[org.cosplay.examples.image.CPImageFormatsExample CPImageFormatsExample]] class for the example of
  *     using images.
  */
class CPStaticImageSprite(
    id: String = s"static-img-spr-${CPRand.guid6}",
    x: Int,
    y: Int,
    z: Int,
    img: CPImage,
    collidable: Boolean = false,
    shaders: Seq[CPShader] = Seq.empty) extends CPSceneObject(id):
    private val imgDim = img.getDim
    private val imgRect = new CPRect(x, y, imgDim)

    /**
      * Creates new static image sprite.
      *
      * @param x X-coordinate of the sprite.
      * @param y Y-coordinate of the sprite.
      * @param z Z-index at which to render the image.
      * @param img The image to render.
      */
    def this(x: Int, y: Int, z: Int, img: CPImage) =
        this(CPRand.guid6, x, y, z, img)

    /** @inheritdoc */
    override def getShaders: Seq[CPShader] = shaders
    /** @inheritdoc */
    override val getX: Int = x
    /** @inheritdoc */
    override val getY: Int = y
    /** @inheritdoc */
    override val getZ: Int = z
    /** @inheritdoc */
    override val getDim: CPDim = imgDim
    /** @inheritdoc */
    override val getRect: CPRect = imgRect
    /** @inheritdoc */
    override def getCollisionRect: Option[CPRect] = Option.when(collidable)(getRect)
    /** @inheritdoc */
    override def render(ctx: CPSceneObjectContext): Unit = ctx.getCanvas.drawImage(img, x, y, z)
