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
import org.cosplay.CPPixel.*

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
  * A sprite representing a dynamic panel with titled border. It creates the bordered panel similar to this:
  * <pre>
  *  .------Title-------.
  *  |                  |
  *  |                  |
  *  |                  |
  *  '------------------'
  * </pre>
  *
  * @param id ID of this scene object.
  * @param x Initial X-coordinate of the top-left corner of the sprite.
  * @param y Initial Y-coordinate of the top-left corner of the sprite.
  * @param width Immutable panel width.
  * @param height Immutable panel height.
  * @param z Initial Z-index at which to render the sprite.
  * @param bg Panel background color.
  * @param borderChars Border chars. See [[CPCanvas.drawRectBorder()]] method for details.
  * @param borderFg Border color.
  * @param title Optional title for the border as sequence of pixels. Title is always centered. If
  *              sequence is empty, no title will be drawn.
  * @param borderSkin Skin function for the border that takes x and y coordinates as well as default pixel
  *                   at that location and returns the skinned pixel. Default value is no-op function.
  * @param collidable Whether or not this sprite provides collision shape. Default value is `false`.
  * @param shaders Optional sequence of shaders for this sprite. Default value is an empty sequence.
  * @param tags Optional set of organizational or grouping tags. Default value is an empty set.
  *
  * @see [[org.cosplay.examples.dialog.CPDialogExample]]
  * @see [[org.cosplay.examples.layout.CPLayoutExample]]
  * @see [[org.cosplay.examples.textinput.CPTextInputExample]]
  * @see [[org.cosplay.examples.sound.CPSoundExample]]
  */
class CPTitlePanelSprite(
    id: String,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    z: Int,
    bg: CPColor,
    borderChars: String,
    borderFg: CPColor,
    title: Seq[CPPixel],
    borderSkin: (Int, Int, CPPixel) => CPPixel = (_, _, px) => px,
    collidable: Boolean = false,
    shaders: Seq[CPShader] = Seq.empty,
    tags: Set[String] = Set.empty
) extends CPDynamicSprite(id, x, y, z, collidable, shaders, tags):
    private val dim = CPDim(width, height)

    /** @inheritdoc */
    override def getDim: CPDim = dim
    /** @inheritdoc */
    override def getRect: CPRect = new CPRect(getX, getY, dim)
    /** @inheritdoc */
    override def render(ctx: CPSceneObjectContext): Unit =
        val canv = ctx.getCanvas
        val x1 = getX
        val y1 = getY
        val x2 = x1 + width - 1
        val y2 = y1 + height - 1
        val z1 = getZ
        canv.fillRect(x1, y1, x2, y2, z1, ' '&bg)
        canv.drawRectBorder(
            getRect,
            z1,
            borderChars,
            borderFg,
            title,
            x1 + (width - title.length) / 2,
            y1,
            borderSkin
        )

