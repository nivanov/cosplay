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
  * Scene object tailor-made for rendering images.
  *
  * This sprite can be used to render static or moving image, the same image or changing images. For
  * static unchanging images see [[CPStaticImageSprite]] for even simpler API.
  *
  * ### Dynamic Image Position
  * If a game is intended to work with [[CPScene adaptive scene sizing]] than all of its scene objects
  * must adapt to changing size of the scene. Image sprite provides simple and convenient way to support
  * this logic. Here's an example of using image sprite to display an image at the center of the screen
  * dynamically adjusting its position on each frame to the actual canvas size:
  * {{{
  *     val img = ...
  *     val spr = new CPImageSprite("logo", 0, 0, 0, img):
  *         override def update(ctx: CPSceneObjectContext): Unit =
  *             val canv = ctx.getCanvas
  *             setX((canv.dim.width - getImage.getWidth) / 2)
  *             setY((canv.dim.height - getImage.getHeight) / 2)
  * }}}
  * Notes:
  *  - All we have to do is override [[CPSceneObject.update()]] method to update XY-coordinate on each frame.
  *  - Canvas instance will always have the current size of scene: whether it is defined explicitly or implicitly
  *    as the current size of the terminal.
  *  - Initial `(0,0)` position is not used as we override the actual position on each frame.
  *  - We use methods [[setX()]] and [[setY()]] to set current XY-coordinates.
  *  - Note that we don't need to override any other methods. Specifically, we don't need to override [[render()]]
  *    method since it relies on [[getX]] and [[getY]] method in its implementation.
  *
  * See also [[CPDynamicSprite]] and [[CPLayoutSprite]] for imperative scene object layout.
  *
  * ### Sprites
  * CosPlay provides number of built-in sprites. A sprite is a scene objects, fully or partially
  * visible including being entirely off-screen, that is custom designed for a particular use case.
  * Built-in sprites provide concrete implementations for the abstract methods in the base
  * [[CPSceneObject]] class. Most non-trivial games will use combination of the built-in sprites and
  * their own ones. Here's the list of some of the built-in sprites:
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
  * @param id Optional ID of the sprite. By default, a random ID will be used.
  * @param x Initial X-coordinate of the sprite. Default value is zero.
  * @param y Initial Y-coordinate of the sprite. Default value is zero.
  * @param z Initial z-index at which to render the image. Default value is zero.
  * @param img The image to render. It can be changed later.
  * @param collidable Whether or not this sprite has a collision shape. Default is `false`.
  * @param shaders Optional sequence of shaders for this sprite. Default value is an empty sequence.
  * @param tags Optional set of organizational or grouping tags. By default, the empty set is used.
  * @see [[CPStaticImageSprite]]
  * @example See [[org.cosplay.examples.image.CPImageCarouselExample CPImageCarouselExample]] class for the example of
  *     using images.
  * @example See [[org.cosplay.examples.image.CPImageFormatsExample CPImageFormatsExample]] class for the example of
  *     using images.
  * @see [[CPSceneObjectContext.getCanvas]] to get current canvas you can draw on.
  * @see [[CPCanvas]] various API to draw on the canvas.
  */
class CPImageSprite(
    id: String = s"img-spr-${CPRand.guid6}",
    x: Int = 0,
    y: Int = 0,
    z: Int = 0,
    img: CPImage,
    collidable: Boolean = false,
    shaders: Seq[CPShader] = Seq.empty,
    tags: Set[String] = Set.empty
) extends CPDynamicSprite(id, x, y, z, collidable, shaders, tags):
    private var myImg = img
    private var myDim = img.getDim

    /**
      * Changes the image this sprite is rendering.
      *
      * @param img New image to render.
      */
    def setImage(img: CPImage): Unit =
        myImg = img
        myDim = img.getDim

    /**
      * Gets current image to render.
      *
      * @return Current image to render.
      */
    def getImage: CPImage = myImg

    /**
      * Initial image of the sprite.
      *
      * @see [[setImage()]]
      */
    val initImg: CPImage = img

    /**
      * Resets this sprite to its initial XYZ-coordinates and the initial image.
      */
    def reset(): Unit =
        resetXYZ()
        setImage(initImg)

    /** @inheritdoc */
    override def getDim: CPDim = myDim
    /** @inheritdoc */
    override def getRect: CPRect = new CPRect(getX, getY, myDim)
    /** @inheritdoc */
    override def render(ctx: CPSceneObjectContext): Unit = ctx.getCanvas.drawImage(myImg, getX, getY, getZ)

/**
  * Companion object contains utility methods.
  */
object CPImageSprite:
    /**
      * A convenient shortcut constructor for the image sprite that works in adaptive scene.
      *
      * Instead of concrete XY-coordinates this method takes two functions that each takes an instance of
      * [[CPCanvas]] class and produce X or Y coordinate. This way this image sprite can update its position
      * on the canvas on each frame. See [[CPScene]] on details about adaptive scenes.
      *
      * @param id ID of the sprite.
      * @param xf X-coordinate producing function that takes [[CPCanvas]] parameter.
      * @param yf Y-coordinate producing function that takes [[CPCanvas]] parameter.
      * @param z Z-index at which to render the image.
      * @param img The image to render. It can be changed later.
      * @param collidable Whether or not this sprite has a collision shape. Default is `false`.
      * @param shaders Optional sequence of shaders for this sprite. Default value is an empty sequence.
      * @param tags Optional set of organizational or grouping tags. By default, the empty set is used.
      * @see [[CPStaticImageSprite]]
      * @example See [[org.cosplay.examples.image.CPImageCarouselExample CPImageCarouselExample]] class for the example of
      *     using images.
      * @example See [[org.cosplay.examples.image.CPImageFormatsExample CPImageFormatsExample]] class for the example of
      *     using images.
      */
    def apply(
        id: String = s"img-spr-${CPRand.guid6}",
        xf: CPCanvas => Int,
        yf: CPCanvas => Int,
        z: Int,
        img: CPImage,
        collidable: Boolean = false,
        shaders: Seq[CPShader] = Seq.empty,
        tags: Set[String] = Set.empty
    ): CPImageSprite =
        new CPImageSprite(id, z = z, img, collidable, shaders, tags):
            private var x = initX
            private var y = initY

            override def getX: Int = x
            override def getY: Int = y
            override def update(ctx: CPSceneObjectContext): Unit =
                val canv = ctx.getCanvas
                x = xf(canv)
                y = yf(canv)
