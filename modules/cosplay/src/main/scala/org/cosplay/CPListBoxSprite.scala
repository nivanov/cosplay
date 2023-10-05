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

import org.cosplay.CPKeyboardKey.*
import org.cosplay.impl.CPUtils
import scala.collection.mutable

/**
  * An element of the listbox model.
  *
  * @see [[CPListBoxModel]]
  */
trait CPListBoxElement[T]:
    /** Gets the sequence of pixel visually representing this listbox value. */
    def getLine: Seq[CPPixel]
    /** Gets the value of the this listbox element. */
    def getValue: T

/**
  * A model for listbox sprite.
  */
trait CPListBoxModel:
    /** Gets the total number of elements in the model. */
    def getSize: Int
    /** Gets the model element with given index or `None` if index is invalid or model is empty.. */
    def getElement[T](i: Int): Option[CPListBoxElement[T]]
    /**
      * Gets the index of the currently selected item.
      * Model must always have an element selected unless it is empty.
      */
    def getSelectionIndex: Int
    /** A shortcut getter for the value of the selected element or `None` if the model is empty.*/
    def getSelectedValue[T]: Option[T] = getElement[T](getSelectionIndex).flatMap(_.getValue.?)

/**
  *
  * @param id ID of this scene object.
  * @param x Initial X-coordinate of the top-left corner of the sprite.
  * @param y Initial Y-coordinate of the top-left corner of the sprite.
  * @param z Initial Z-index at which to render the sprite.
  * @param model
  * @param width
  * @param height
  * @param selSkin
  * @param collidable Whether or not this sprite provides collision shape.
  * @param shaders Optional sequence of shaders for this sprite.
  * @param tags Optional set of organizational or grouping tags.
  * @see [[CPListBoxModel]]
  */
class CPListBoxSprite(
    id: String = s"listbox-spr-${CPRand.guid6}",
    x: Int,
    y: Int,
    z: Int,
    model: CPListBoxModel,
    width: Int,
    height: Int,
    onKey: (CPListBoxModel, CPKeyboardKey) => Unit,
    selSkin: (Int, CPPixel) => CPPixel,
    collidable: Boolean = false,
    shaders: Seq[CPShader] = Seq.empty,
    tags: Set[String] = Set.empty
) extends CPDynamicSprite(id, x, y, z, collidable, shaders, tags):
    !>(width > 0, s"Width must be > 0: $width")
    !>(height > 0, s"Height must be > 0: $height")
    private val dim = CPDim(width, height)
    private var xOff = 0
    private var viewStartIdx = 0

    override def update(ctx: CPSceneObjectContext): Unit =
        if ctx.isFocusOwner then
            ctx.getKbEvent match
                case Some(evt) => onKey(model, evt.key)
                case None => ()
    override def render(ctx: CPSceneObjectContext): Unit =
        val selIdx = model.getSelectionIndex
        if selIdx < viewStartIdx then viewStartIdx = selIdx
        else if selIdx >= viewStartIdx + width then viewStartIdx = selIdx - width + 1
        var i = viewStartIdx
        val sz = model.getSize
        val lastIdx = (i + width - 1).min(i + sz - viewStartIdx - 1)
        val canv = ctx.getCanvas
        while i <= lastIdx do
            model.getElement(i) match
                case Some(elm) =>
                    canv.drawPixels(
                        x + xOff,
                        y,
                        z,
                        if i == selIdx then elm.getLine.zipWithIndex.map(t => selSkin(t._2, t._1)) else elm.getLine
                    )
                case None => ()
            i += 1
    override def getDim: CPDim = dim
