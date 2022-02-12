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
  * @param id ID of the sprite.
  * @param x Initial X-coordinate of the sprite.
  * @param y Initial Y-coordinate of the sprite.
  * @param z Z-index at which to render the image.
  * @param img The image to render. It can be changed later.
  * @param collidable Whether or not this sprite has a collision shape. Default is `false`.
  * @param shaders Optional set of shaders for this sprite. Default value is an empty sequence.
  * @see [[CPStaticImageSprite]]
  * @example See [[org.cosplay.examples.image.CPImageCarouselExample CPImageCarouselExample]] class for the example of
  *     using images.
  * @example See [[org.cosplay.examples.image.CPImageFormatsExample CPImageFormatsExample]] class for the example of
  *     using images.
  */
class CPImageSprite(
    id: String,
    x: Int,
    y: Int,
    z: Int,
    img: CPImage,
    collidable: Boolean = false,
    shaders: Seq[CPShader] = Seq.empty) extends CPSceneObject(id):
    private var myImg = img
    private var myDim = img.getDim
    private var myX = x
    private var myY = y
    private var myZ = z

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
      * Initial X-coordinate of the sprite.
      *
      * @see [[setX()]]
      */
    final val initX: Int = x

    /**
      * Initial Y-coordinate of the sprite.
      *
      * @see [[setY()]]
      */
    final val initY: Int = y

    /**
      * Initial Z-index of the sprite.
      *
      * @see [[setZ()]]
      */
    final val initZ: Int = z

    /**
      * Sets current X-coordinate. This coordinate will be returned from [[getX]] method.
      *
      * @param d X-coordinate to set.
      */
    def setX(d: Int): Unit = myX = d

    /**
      * Sets current Y-coordinate. This coordinate will be returned from [[getY]] method.
      *
      * @param d Y-coordinate to set.
      */
    def setY(d: Int): Unit = myY = d

    /**
      * Sets current Z-index. This index will be returned from [[getZ]] method.
      *
      * @param d Z-index to set.
      */
    def setZ(d: Int): Unit = myZ = d

    /** @inheritdoc */
    override def getShaders: Seq[CPShader] = shaders
    /** @inheritdoc */
    override def getX: Int = myX
    /** @inheritdoc */
    override def getY: Int = myY
    /** @inheritdoc */
    override def getZ: Int = myZ
    /** @inheritdoc */
    override def getDim: CPDim = myDim
    /** @inheritdoc */
    override def getRect: CPRect = new CPRect(getX, getY, myDim)
    /** @inheritdoc */
    override def getCollisionRect: Option[CPRect] = Option.when(collidable)(getRect)
    /** @inheritdoc */
    override def render(ctx: CPSceneObjectContext): Unit = ctx.getCanvas.drawImage(img, getX, getY, getZ)

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
      * @param shaders Optional set of shaders for this sprite. Default value is an empty sequence.
      * @see [[CPStaticImageSprite]]
      * @example See [[org.cosplay.examples.image.CPImageCarouselExample CPImageCarouselExample]] class for the example of
      *     using images.
      * @example See [[org.cosplay.examples.image.CPImageFormatsExample CPImageFormatsExample]] class for the example of
      *     using images.
      */
    def apply(
        id: String,
        xf: CPCanvas => Int,
        yf: CPCanvas => Int,
        z: Int,
        img: CPImage,
        collidable: Boolean = false,
        shaders: Seq[CPShader] = Seq.empty): CPImageSprite =
        new CPImageSprite(id, 0, 0, z, img, collidable, shaders):
            private var x = initX
            private var y = initY

            override def getX: Int = x
            override def getY: Int = y
            override def update(ctx: CPSceneObjectContext): Unit =
                val canv = ctx.getCanvas
                x = xf(canv)
                y = yf(canv)
