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
  * A sprite that provide classic list box selector. It supports single selection over a list of values.
  * This sprite uses an instance of [[CPListBoxModel]] to govern its operation. One needs to provide the model
  * instance and `onKey` function to update the model based on the keyboard events.
  *
  * Note that this sprite will automatically process  [[KEY_LEFT]] and [[KEY_RIGHT]] keys to adjust the viewport
  * of the listbox when the content of the model does not fit the dimensions of the sprite. These keys will still be
  * passed to `onKey()` function call.
  *
  * ### UI Framework
  * Although CosPlay does not define an opinionated UI framework several sprites and supporting classes are often
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
  * @param model List box model to use.
  * @param width Width of the sprite.
  * @param height Height of the sprite.
  * @param onKey A function that is called on each frame if a keyboard key was pressed. It takes
  *              scene object context, model and the pressed keyboard key. This function should
  *              manipulate the `model` instance passed into this constructor to update model state
  *              for given keyboard input. These model changes will then be rendered by this sprite.
  * @param selSkin Skinning function for the currently selected row. It takes relative X-coordinate
  *                and current pixel return a new skinned pixel for that location.
  * @param collidable Whether or not this sprite provides collision shape.
  * @param shaders Optional sequence of shaders for this sprite.
  * @param tags Optional set of organizational or grouping tags.
  * @see [[CPListBoxModel]]
  * @example See [[org.cosplay.examples.listbox.CPListBoxExample CPListBoxExample]] class for the example of
  *     using this sprite.
  */
class CPListBoxSprite(
    id: String = s"listbox-spr-${CPRand.guid6}",
    x: Int = 0,
    y: Int = 0,
    z: Int = 0,
    model: CPListBoxModel,
    width: Int,
    height: Int,
    onKey: (CPSceneObjectContext, CPListBoxModel, CPKeyboardKey) => Unit,
    selSkin: (Int, CPPixel) => CPPixel,
    collidable: Boolean = false,
    shaders: Seq[CPShader] = Seq.empty,
    tags: Set[String] = Set.empty
) extends CPDynamicSprite(id, x, y, z, collidable, shaders, tags):
    !>(width > 0, s"Width must be > 0: $width")
    !>(height > 0, s"Height must be > 0: $height")
    private val dim = CPDim(width, height)
    private var xOff = 0
    private var lastKey = none[CPKeyboardKey]
    private var viewStartIdx = 0

    override def update(ctx: CPSceneObjectContext): Unit =
        lastKey = None
        if ctx.isFocusOwner then
            ctx.getKbEvent match
                case Some(evt) =>
                    lastKey = evt.key.?
                    onKey(ctx, model, evt.key)
                case None => ()
    override def render(ctx: CPSceneObjectContext): Unit =
        val selIdx = model.getSelectionIndex
        val sz = model.getSize
        val canv = ctx.getCanvas
        if viewStartIdx >= sz then viewStartIdx = (sz - height).max(0)
        if selIdx < viewStartIdx then viewStartIdx = selIdx
        else if selIdx >= viewStartIdx + height then viewStartIdx = selIdx - height + 1
        var i = viewStartIdx
        val lastIdx = (i + height - 1).min(i + sz - viewStartIdx - 1)
        lastKey match
            case Some(key) if key == KEY_LEFT => if xOff > 0 then xOff -= 1
            case Some(key) if key == KEY_RIGHT =>
                var k = i
                var offNeeded = false
                while k <= lastIdx && !offNeeded do
                    model.getElement(k) match
                        case Some(elm) => if elm.getLine.length - xOff > width then offNeeded = true
                        case _ => ()
                    k += 1
                if offNeeded then xOff += 1
            case _ => ()
        val myX = getX
        var myY = getY
        val myZ = getZ
        while i <= lastIdx do
            model.getElement(i) match
                case Some(elm) =>
                    val pxs = if i == selIdx then elm.getLine.zipWithIndex.map(t => selSkin(t._2, t._1)) else elm.getLine
                    canv.drawPixels(myX, myY, myZ, pxs.slice(xOff, (xOff + width).min(pxs.length)))
                case None => ()
            i += 1
            myY += 1
    override def getDim: CPDim = dim
