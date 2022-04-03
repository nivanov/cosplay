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
  * Scene object tailor-made for rendering text labels.
  *
  * This sprite accepts text and the font along with skinning function and renders this text
  * at the given XY-coordinates. Text of the label can change dynamically. Note that the dimension of the
  * sprite is changing along with the change of the label's text.
  *
  * ### Sprites
  * CosPlay provides number of built-in sprites. A sprite is a scene objects, visible or off-screen,
  * that is custom designed for a particular use case. Built-in sprites provide concrete
  * implementations for the abstract methods in the base [[CPSceneObject]] class. Most non-trivial games
  * will use combination of the built-in sprites and their own ones. Here's the list of the built-in
  * sprites:
  *  - [[CPCanvasSprite]]
  *  - [[CPImageSprite]]
  *  - [[CPLabelSprite]]
  *  - [[CPVideoSprite]]
  *  - [[CPStaticImageSprite]]
  *  - [[CPOffScreenSprite]]
  *  - [[CPKeyboardSprite]]
  *  - [[CPParticleSprite]]
  *  - [[CPTextInputSprite]]
  *
  * @param id Optional ID of this sprite.
  * @param x X-coordinate of the top-left corner of the label.
  * @param y Y-coordinate of the top-left corner of the label.
  * @param z Z-index at which to render the label.
  * @param font Font to use for label.
  * @param text Label text.
  * @param fg Foreground color.
  * @param bg Optional background color. Default value is `None`.
  * @param skin Skinning function. The function takes an existing pixel, its X and Y coordinate and
  *     return a new pixel. Default value is the function that returns the same pixel.
  * @param collidable Whether or not this sprite provides collision shape. Default value is `false`.
  * @param shaders Optional set of shaders for this sprite. Default value is an empty sequence.
  * @example See [[org.cosplay.examples.textinput.CPTextInputExample CPTextInputExample]] class for the example of
  *     using labels and text input.
  */
class CPLabelSprite(
    id: String = s"lbl-spr-${CPRand.guid6}",
    x: Int,
    y: Int,
    z: Int,
    font: CPFont = CPSystemFont,
    text: String,
    fg: CPColor,
    bg: Option[CPColor] = None,
    skin: (CPPixel, Int, Int) => CPPixel = (px, _, _) => px,
    collidable: Boolean = false,
    shaders: Seq[CPShader] = Seq.empty
) extends CPSceneObject(id):
    private var lblTxt = text
    private var img: CPImage = _
    private var dim: CPDim = _

    reset()

    /**
      * Creates label sprite with given parameters.
      *
      * @param x X-coordinate of the label.
      * @param y Y-coordinate of the label.
      * @param z Z-index to render label at.
      * @param text Label text.
      * @param fg Foreground color.
      */
    def this(x: Int, y: Int, z: Int, text: String, fg: CPColor) =
        this(CPRand.guid6, x, y, z, text = text, fg = fg)

    private def reset(): Unit =
        img = font.render(lblTxt, fg, bg).trimBg().skin(skin)
        dim = img.getDim

    /**
      * Gets label text.
      */
    def getText: String = lblTxt

    /**
      * Sets label text.
      *
      * @param s Label text to set.
      */
    def setText(s: String): Unit =
        lblTxt = s
        reset()

    /** @inheritdoc */
    def getDim: CPDim = dim
    /** @inheritdoc */
    override val getX: Int = x
    /** @inheritdoc */
    override val getY: Int = y
    /** @inheritdoc */
    override val getZ: Int = z
    /** @inheritdoc */
    override def getShaders: Seq[CPShader] = shaders
    /** @inheritdoc */
    override def getRect: CPRect = new CPRect(getX, getY, dim)
    /** @inheritdoc */
    override def getCollisionRect: Option[CPRect] = Option.when(collidable)(getRect)
    /** @inheritdoc */
    override def render(ctx: CPSceneObjectContext): Unit = ctx.getCanvas.drawImage(img, getX, getY, getZ)