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
  * will use combination of the built-in sprites and their own ones. Here's the list of some of the built-in
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
  * ### UI Framework
  * Although CosPlay does not define an opinionated UI framework, several sprites and supporting classes are often
  * used for constructing UI element on the screen. These include:
  *  - [[CPLayoutSprite]]
  *  - [[CPDynamicSprite]]
  *  - [[CPLabelSprite]]
  *  - [[CPSpacerSprite]]
  *  - [[CPTitlePanelSprite]]
  *  - [[CPListBoxSprite]]
  *  - [[CPTextInputSprite]]
  *  - [[CPSystemFont]]
  *
  *  You can can also look at the following UI-related examples:
  *   - [[org.cosplay.examples.listbox.CPListBoxExample]]
  *   - [[org.cosplay.examples.dialog.CPDialogExample]]
  *   - [[org.cosplay.examples.layout.CPLayoutExample]]
  *   - [[org.cosplay.examples.textinput.CPTextInputExample]]
  *
  * @param id Optional ID of this sprite.
  * @param x Initial X-coordinate of the top-left corner of the label. Default value is zero.
  * @param y Initial Y-coordinate of the top-left corner of the label. Default value is zero.
  * @param z Initial Z-index at which to render the label. Default value is zero.
  * @param font Font to use for label.
  * @param text Label text.
  * @param fg Foreground color.
  * @param bg Optional background color. Default value is `None`.
  * @param skin Skinning function. The function takes an existing pixel, its X and Y coordinate and
  *     return a new pixel. Default value is the function that returns the same pixel.
  * @param collidable Whether or not this sprite provides collision shape. Default value is `false`.
  * @param shaders Optional sequence of shaders for this sprite. Default value is an empty sequence.
  * @param tags Optional set of organizational or grouping tags. By default, the empty set is used.
  * @example See [[org.cosplay.examples.textinput.CPTextInputExample CPTextInputExample]] class for the example of
  *     using labels and text input.
  */
class CPLabelSprite(
    id: String = s"lbl-spr-${CPRand.guid6}",
    x: Int = 0,
    y: Int = 0,
    z: Int = 0,
    font: CPFont = CPSystemFont,
    text: String,
    fg: CPColor,
    bg: Option[CPColor] = None,
    skin: (CPPixel, Int, Int) => CPPixel = (px, _, _) => px,
    collidable: Boolean = false,
    shaders: Seq[CPShader] = Seq.empty,
    tags: Set[String] = Set.empty
) extends CPDynamicSprite(id, x, y, z, collidable, shaders, tags):
    private var lblTxt = text
    private var img: CPImage = _
    private var dim: CPDim = _

    reset()

    /**
      * Creates label sprite with given parameters using system font.
      *
      * @param x X-coordinate of the label.
      * @param y Y-coordinate of the label.
      * @param z Z-index to render label at.
      * @param text Label text.
      * @param fg Foreground color.
      * @param bg Background color.
      */
    def this(x: Int, y: Int, z: Int, text: String, fg: CPColor, bg: Option[CPColor]) =
        this(id = CPRand.guid6, x = x, y = y, z = z, text = text, fg = fg, bg = bg)

    /**
      * Creates label sprite with given parameters using system font.
      *
      * @param x X-coordinate of the label.
      * @param y Y-coordinate of the label.
      * @param z Z-index to render label at.
      * @param text Label text.
      * @param fg Foreground color.
      */
    def this(x: Int, y: Int, z: Int, text: String, fg: CPColor) =
        this(id = CPRand.guid6, x = x, y = y, z = z, text = text, fg = fg)

    /**
      * Creates label sprite with given parameters using system font.
      * Initial coordinates will be (0, 0, 0).
      *
      * @param id Label ID.
      * @param text Label text.
      * @param fg Foreground color.
      * @param bg Background color.
      */
    def this(id: String, text: String, fg: CPColor, bg: Option[CPColor]) =
        this(id = id, x = 0, y = 0, z = 0, text = text, fg = fg)

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
    override def getRect: CPRect = new CPRect(getX, getY, dim)
    /** @inheritdoc */
    override def render(ctx: CPSceneObjectContext): Unit = ctx.getCanvas.drawImage(img, getX, getY, getZ)