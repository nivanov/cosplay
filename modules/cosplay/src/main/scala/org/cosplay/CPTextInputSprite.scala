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
               All rights reserved.
*/

import org.cosplay.*
import org.cosplay.CPKeyboardKey.*
import org.cosplay.impl.CPUtils

import scala.collection.mutable

/**
  * Scene object tailor-made for accepting text input.
  *
  * This sprite acts as a classic input field: user can input text using keyboard and cursor keys.
  * It supports different field and buffer lengths, skinning input (i.e. password field) as well as user-defined
  * set of keys for submission and cancellation. You can also chain multiple text inputs together to create an
  * auto-navigable forms. **NOTE**: this sprite works only with system font - you can't use FIGLeft fonts with this sprite.
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
  * @param id Optional ID of this sprite.
  * @param x X-coordinate of the top-left corner.
  * @param y Y-coordinate of the top-left corner.
  * @param z Z-index at which to render this sprite.
  * @param visLen Visible length of this field.
  * @param maxBuf Overall buffer length of this field. It should always be greater then or equal to visible length.
  * @param initTxt Optional initial text to show at the initial state. Default value is an empty string.
  * @param onSkin The skinning function for the active state (when sprite has keyboard focus).
  *     Skin function takes a character, its position in the input string and whether or not it is at the
  *     current cursor position (i.e. cursor is over the character). The function must return [[CPPixel]] instance.
  * @param offSkin The skinning function for the inactive state (when sprite does not have keyboard focus).
  *     Skin function takes a character, its position in the input string and whether or not it is at the
  *     current cursor position (i.e. cursor is over the character). The function must return [[CPPixel]] instance.
  * @param next Optional scene object ID to switch keyboard focus to after the user pressed one of the
  *     submit keys. Default value is `None`.
  * @param cancelKeys Optional set of keyboard keys to accept for cancellation action. When one of these keys
  *     is pressed the sprite will reset to its initial state, marked as ready and its result set to `None`.
  *     Default value is [[CPKeyboardKey.KEY_ESC]].
  * @param submitKeys Optional set of keyboard keys to accept for submission action. When one of these keys
  *     is pressed the sprite will make result available via [[isReady]] method and will optionally switch
  *     keyboard focus for the `next` scene object, if any. Default value is [[CPKeyboardKey.KEY_ENTER]].
  * @param tags Optional set of organizational or grouping tags. By default, the empty set is used.
  * @param keyFilter Optional filter on keyboard events. Only if this filter returns `true` for a given
  *     keyboard event its key will be used as an input. Note that this filter is not applied to built-in
  *     keyboard keys such as cursor movements, escape, backspace, as well as cancel and submit keys. This
  *     filter can be used, for example, to ensure that only digits can be entered.
  * @param collidable Whether or not this sprite has a collision shape. Default is `false`.
  * @param shaders Optional sequence of shaders for this sprite. Default value is an empty sequence.
  * @example See [[org.cosplay.examples.textinput.CPTextInputExample CPTextInputExample]] class for the example of
  *     using labels and text input.
  * @see [[CPSceneObjectContext.getCanvas]] to get current canvas you can draw on.
  * @see [[CPCanvas]] various API to draw on the canvas.
  */
class CPTextInputSprite(
    id: String = s"input-spr-${CPRand.guid6}",
    x: Int,
    y: Int,
    z: Int,
    visLen: Int,
    maxBuf: Int,
    private var initTxt: String = "",
    onSkin: (Char, Int, Boolean) => CPPixel,
    offSkin: (Char, Int, Boolean) => CPPixel,
    private var next: Option[String] = None,
    cancelKeys: Seq[CPKeyboardKey] = Seq(KEY_ESC),
    submitKeys: Seq[CPKeyboardKey] = Seq(KEY_ENTER),
    tags: Set[String] = Set.empty,
    keyFilter: CPKeyboardEvent => Boolean = _ => true,
    collidable: Boolean = false,
    shaders: Seq[CPShader] = Seq.empty
) extends CPDynamicSprite(id, x, y, z, collidable, shaders, tags):
    !>(maxBuf >= visLen, "'maxBuf' must be >= 'visLen'.")
    !>(initTxt != null, "Initial text cannot be 'null'.")

    private val dim = CPDim(visLen, 1)
    private val buf = mutable.ArrayBuffer.empty[Char]
    private var curPos = -1
    private var lastStart = 0
    private var ready = false
    private val pxs = mutable.ArrayBuffer.empty[CPPixel]
    private var res = (none[CPKeyboardKey], none[String])
    private var lastKey = none[CPKeyboardKey]

    reset()

    private def reset(): Unit =
        buf.clear()
        buf.addAll(initTxt.substring(0, maxBuf.min(initTxt.length)))
        val len = buf.length
        lastStart = (len - visLen).max(0)
        curPos = if len < maxBuf then len else len - 1

    /** @inheritdoc */ 
    override def render(ctx: CPSceneObjectContext): Unit =
        val skin = if ctx.isFocusOwner then onSkin else offSkin
        pxs.clear()
        val delta = curPos - lastStart
        val start = if delta >= 0 && delta < visLen then lastStart else lastStart + delta.sign
        lastStart = start
        val end = start + visLen
        val len = buf.length
        var i = start
        while i < end do
            val ch = if i < len then buf(i) else ' '
            pxs += skin(ch, i, i == curPos)
            i += 1
        ctx.getCanvas.drawPixels(getX, getY, getZ, pxs.toSeq)

    /** @inheritdoc */
    override def update(ctx: CPSceneObjectContext): Unit =
        ctx.getKbEvent match
            case Some(evt) =>
                lastKey = evt.key.?
                evt.key match
                    case KEY_LEFT => if curPos > 0 then curPos -= 1
                    case KEY_RIGHT => if curPos < buf.length then curPos += 1
                    case KEY_BACKSPACE => if curPos > 0 then
                        curPos -= 1
                        buf.remove(curPos)
                    case KEY_DEL => if buf.nonEmpty && curPos < buf.length then buf.remove(curPos)
                    case key if cancelKeys.contains(key) => done(None)
                    case key if submitKeys.contains(key) =>
                        done(buf.toString().?)
                        if next.isDefined then ctx.acquireFocus(next.get)
                    case key if keyFilter(evt) && key.isPrintable =>
                        if curPos < maxBuf then
                            buf.insert(curPos, key.ch)
                            curPos += 1
                    case _ => ()
            case None => ()

    private def done(optRes: Option[String]): Unit =
        ready = true
        res = (lastKey, optRes)
        if optRes.isEmpty then
            reset()

    /**
      * Clears this sprite resetting it to its initial state.
      *
      * @param initTxt Optional new initial text to use. Default value is `null` which will retain the previous
      *     initial text.
      */
    def clear(initTxt: String = null): Unit =
        if initTxt != null then this.initTxt = initTxt
        ready = false
        res = (None, None)
        reset()

    /** Gets current initial text for this sprite. */
    def getInitText: String = initTxt

    /**
      * Gets optional ID of the 'next' scene object which will receive the keyboard focus after this
      * sprite result is ready.
      */
    def getNext: Option[String] = next

    /**
      * Sets optional ID of the 'next' scene object which will receive the keyboard focus after this
      * sprite result is ready.
      *
      * @param next ID of the 'next' scene object or `None` to remove it.
      */
    def setNext(next: Option[String]): Unit = this.next = next

    /**
      * Whether or not the result is either submitted or cancelled, i.e. ready.
      *
      * @see [[getResult()]]
      */
    def isReady: Boolean = ready

    /**
      * Gets the tuple of the last key pressed and input result. Input result will be `None` if result is not
      * yet ready or got cancelled. Last key pressed is `None` when no keys were pressed yet on this sprite.
      * Call method [[isReady()]] on each frame to check whether the input result is actually ready.
      *
      * Note that if result was ready the internal ready flag will be reset to `false` so that the next
      * call to [[isReady()]] will return `false`.
      *
      * @see [[isReady()]]
      */
    def getResult: (Option[CPKeyboardKey], Option[String]) =
        ready = false
        res

    /** @inheritdoc */
    def getDim: CPDim = dim
