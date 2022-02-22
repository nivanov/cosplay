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

import CPKeyboardKey.*
import org.cosplay.impl.CPUtils

import scala.collection.mutable

/**
  * Scene object tailor-made for accepting text input.
  *
  * This sprites acts as a classic input field: user can input text using keyboard and cursor keys.
  * It supports skinning input (i.e. password field) as well as user-defined set of keys for submission and
  * cancellation. You can also chain multiple text inputs together to create a auto-navigable forms.
  * **NOTE**: this sprite works only with system font - you can't use FIGLeft fonts with this sprite.
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
  * @param onSkin The skinning function to the active state (when sprite has keyboard focus).
  * @param offSkin The skinning function to the inactive state (when sprite does not have keyboard focus).
  * @param next Optional scene object ID to switch keyboard focus to after the user pressed one of the
  *     submit keys. Default value is `None`.
  * @param cancelKeys Optional set of keyboard keys to accept for cancellation action. When one of these keys
  *     is pressed the sprite will reset to its initial state. Default value is [[CPKeyboardKey.KEY_ESC]].
  * @param submitKeys Optional set of keyboard keys to accept for submission action. When one of these keys
  *     is pressed the sprite will make result available via [[isReady]] method and will optionally switch
  *     keyboard focus for the `next` scene object, if any. Default value is [[CPKeyboardKey.KEY_ENTER]].
  * @example See [[org.cosplay.examples.textinput.CPTextInputExample CPTextInputExample]] class for the example of
  *     using labels and text input.
  */
class CPTextInputSprite(
    id: String = s"input-spr-${CPUtils.guid6}",
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
    submitKeys: Seq[CPKeyboardKey] = Seq(KEY_ENTER)
) extends CPSceneObject(id):
    require(maxBuf >= visLen)
    require(initTxt != null)

    private val dim = CPDim(visLen, 1)
    private val buf = mutable.ArrayBuffer.empty[Char]
    private var curPos = -1
    private var lastStart = 0
    private var ready = false
    private val pxs = mutable.ArrayBuffer.empty[CPPixel]
    private var res: Option[String] = None

    reset()

    /**
      *
      */
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
        ctx.getCanvas.drawPixels(x, y, z, pxs.toSeq)

    /** @inheritdoc */
    override def update(ctx: CPSceneObjectContext): Unit =
        ctx.getKbEvent match
            case Some(evt) =>
                evt.key match
                    case KEY_LEFT => if curPos > 0 then curPos -= 1
                    case KEY_RIGHT => if curPos < buf.length then curPos += 1
                    case KEY_BACKSPACE => if curPos > 0 then
                        curPos -= 1
                        buf.remove(curPos)
                    case KEY_DEL => if buf.nonEmpty && curPos < buf.length then buf.remove(curPos)
                    case key if cancelKeys.contains(key) => done(None)
                    case key if submitKeys.contains(key) =>
                        done(Option(buf.toString()))
                        if next.isDefined then ctx.acquireFocus(next.get)
                    case key if key.isPrintable =>
                        if curPos < maxBuf then
                            buf.insert(curPos, key.ch)
                            curPos += 1
                    case _ => ()
            case None => ()

    /**
      *
      * @param optRes
      */
    private def done(optRes: Option[String]): Unit =
        ready = true
        res = optRes
        if res.isEmpty then
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
        res = None
        reset()

    /**
      * Gets current initial text for this sprite.
      */
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
      * Whether or not the result is ready.
      */
    def isReady: Boolean = ready

    /**
      * Gets input result, `None` if result is not yet ready.
      */
    def getResult: Option[String] = res

    /** @inheritdoc */
    def getDim: CPDim = dim
    /** @inheritdoc */
    def getX: Int = x
    /** @inheritdoc */
    def getY: Int = y
    /** @inheritdoc */
    def getZ: Int = z
