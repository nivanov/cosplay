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
    def getLine: Seq[CPPixel]
    def getValue: T

/**
  * A model for listbox sprite.
  */
trait CPListBoxModel:
    def getSize: Int
    def getElement[T](i: Int): CPListBoxElement[T]

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
  * @param initSelIdx
  * @param next
  * @param cancelKeys
  * @param submitKeys
  * @param collidable Whether or not this sprite provides collision shape.
  * @param shaders Optional sequence of shaders for this sprite.
  * @param tags Optional set of organizational or grouping tags.
  *  @see [[CPListBoxModel]]
  */
class CPListBoxSprite(
    id: String = s"listbox-spr-${CPRand.guid6}",
    x: Int,
    y: Int,
    z: Int,
    model: CPListBoxModel,
    width: Int,
    height: Int,
    selSkin: (Int, CPPixel) => CPPixel,
    initSelIdx: Int = 0,
    private var next: Option[String] = None,
    cancelKeys: Seq[CPKeyboardKey] = Seq(KEY_ESC),
    submitKeys: Seq[CPKeyboardKey] = Seq(KEY_ENTER),
    collidable: Boolean = false,
    shaders: Seq[CPShader] = Seq.empty,
    tags: Set[String] = Set.empty
) extends CPDynamicSprite(id, x, y, z, collidable, shaders, tags):
    private val dim = CPDim(width, height)
    private var curIdx = cap(initSelIdx)

    reset()

    override def update(ctx: CPSceneObjectContext): Unit = super.update(ctx)
    override def render(ctx: CPSceneObjectContext): Unit = super.render(ctx)
    override def getDim: CPDim = dim

    private def cap(idx: Int): Int =
        if idx < 0 then 0
        else if idx >= model.getSize then model.getSize - 1
        else idx

    /**
      * Resets listbox to its initial state.
      */
    def reset(): Unit =
        curIdx = cap(initSelIdx)

    /**
      * Gets value of the currently selected element in the listbox. Note that listbox always
      * has one element selected.
      */
    def getSelection[T](): T = model.getElement(curIdx).getValue

    /**
      * Gets optional ID of the next scene object which will receive the keyboard focus after this sprite.
      */
    def getNext: Option[String] = next

    /**
      * Sets optional ID of the next scene object which will receive the keyboard focus after this sprite.
      *
      * @param next ID of the 'next' scene object or `None` to remove it.
      */
    def setNext(next: Option[String]): Unit = this.next = next
