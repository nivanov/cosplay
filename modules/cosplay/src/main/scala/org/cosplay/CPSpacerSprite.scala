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
  * Simple sprite that only provides its dimension for layout purposes and has no rendering content.
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
  * @param id ID of this scene object.
  * @param x Initial X-coordinate of the top-left corner of the sprite. Default value is zero.
  * @param y Initial Y-coordinate of the top-left corner of the sprite. Default value is zero.
  * @param z Initial Z-index at which to render the sprite. Default value is zero.
  * @param width Spacer width.
  * @param height Spacer height.
  * @param collidable Whether or not this sprite provides collision shape.
  * @param shaders Optional sequence of shaders for this sprite.
  * @param tags Optional set of organizational or grouping tags.
  * @see [[CPLayoutSprite]]
  */
class CPSpacerSprite(
    id: String = s"spacer-spr-${CPRand.guid6}",
    x: Int = 0,
    y: Int = 0,
    z: Int = 0,
    private var width: Int,
    private var height: Int,
    collidable: Boolean = false,
    shaders: Seq[CPShader] = Seq.empty,
    tags: Set[String] = Set.empty
) extends CPDynamicSprite(id, x, y, z, collidable, shaders = shaders, tags = tags):
    private var dim = CPDim(width, height)

    /**  Sets new width. */
    def setWidth(w: Int): Unit =
        !>(w >= 0, "Width must be >= 0.")
        dim = CPDim(w, dim.height)
    /** Sets new height.  */
    def setHeight(h: Int): Unit =
        !>(h >= 0, "Height must be >= 0.")
        dim = CPDim(dim.width, h)
    /** Sets new dimension. */
    def setDim(dim: CPDim): Unit =
        !>(dim != null, "Dimension cannot be null.")
        this.dim = dim
    /** Sets new width and height. */
    def setSize(w: Int, h: Int): Unit =
        !>(w >= 0, "Width must be >= 0.")
        !>(h >= 0, "Height must be >= 0.")
        dim = CPDim(w, h)

    override def getDim: CPDim = dim

/**
  * Companion object.
  */
object CPSpacerSprite:
    /**
      * Creates new spacer sprite.
      *
      * @param width Width of the spacer.
      * @param height Height of the spacer.
      */
    def apply(width: Int, height: Int): CPSpacerSprite =
        new CPSpacerSprite(width = width, height)

    /**
      * Creates new spacer sprite.
      *
      * @param dim Dimension of the spacer.
      */
    def apply(dim: CPDim): CPSpacerSprite =
        new CPSpacerSprite(width = dim.width, dim.height)
