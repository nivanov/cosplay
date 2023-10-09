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
  * Dynamic sprites can change their screen position. It provides an implementation for managing
  * sprite's X, Y, and Z coordinates as well as storing the sprite's initial X, Y, and Z coordinates.
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
  * @param collidable Whether or not this sprite provides collision shape. Default value is `false`.
  * @param shaders Optional sequence of shaders for this sprite. Default value is an empty sequence.
  * @param tags Optional set of organizational or grouping tags. By default, the empty set is used.
  * @see [[CPLayoutSprite]]
  */
abstract class CPDynamicSprite(
    id: String,
    x: Int = 0,
    y: Int = 0,
    z: Int = 0,
    collidable: Boolean = false,
    shaders: Seq[CPShader] = Seq.empty,
    tags: Set[String] = Set.empty
) extends CPSceneObject(id, tags):
    private var myX = x
    private var myY = y
    private var myZ = z

    /**
      * Initial X-coordinate of the sprite.
      *
      * @see [[setX()]]
      */
    val initX: Int = x

    /**
      * Initial Y-coordinate of the sprite.
      *
      * @see [[setY()]]
      */
    val initY: Int = y

    /**
      * Initial Z-index of the sprite.
      *
      * @see [[setZ()]]
      */
    val initZ: Int = z

    /**
      * Sets current X-coordinate. This coordinate will be returned from [[getX]] method.
      *
      * @param d X-coordinate to set.
      */
    def setX(d: Int): Unit = myX = d

    /**
      * Increments current X-coordinate by given value.
      *
      * @param d A value (negative, zero or positive) to increment X-coordinate by.
      */
    def incrX(d: Int): Unit = myX += d

    /**
      * Increments current Y-coordinate by given value.
      *
      * @param d A value (negative, zero or positive) to increment Y-coordinate by.
      */
    def incrY(d: Int): Unit = myY += d

    /**
      * Sets current Y-coordinate. This coordinate will be returned from [[getY]] method.
      *
      * @param d Y-coordinate to set.
      */
    def setY(d: Int): Unit = myY = d

    /**
      * Resets this sprite to its initial XY-coordinates.
      */
    def resetXYZ(): Unit =
        resetXY()
        setZ(initZ)

    /**
      * Resets this sprite to its initial XYZ-coordinates.
      */
    def resetXY(): Unit =
        setXY(initX, initY)

    /**
      * Sets both current XY-coordinates.
      *
      * @param a X-coordinate to set.
      * @param b Y-coordinate to set.
      */
    def setXY(a: Int, b: Int): Unit =
        setX(a)
        setY(b)

    /**
      * Sets both current XY-coordinates as wel as z-index.
      *
      * @param a X-coordinate to set.
      * @param b Y-coordinate to set.
      * @param z Z-index to set.
      */
    def setXYZ(a: Int, b: Int, z: Int): Unit =
        setX(a)
        setY(b)
        setZ(z)

    /**
      * Sets current z-index. This index will be returned from [[getZ]] method.
      *
      * @param z Z-index to set.
      */
    def setZ(z: Int): Unit = myZ = z

    /** @inheritdoc */
    override def getShaders: Seq[CPShader] = shaders
    /** @inheritdoc */
    override def getX: Int = myX
    /** @inheritdoc */
    override def getY: Int = myY
    /** @inheritdoc */
    override def getZ: Int = myZ
    /** @inheritdoc */
    override def getCollisionRect: Option[CPRect] = Option.when(collidable)(getRect)


