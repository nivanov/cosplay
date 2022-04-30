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
  * Font descriptor.
  *
  * Font is an asset. Just like other assets such as [[CPImage images]], [[CPSound sounds]], [[CPAnimation animations]] or
  * [[CPVideo videos]] they are not managed or governed by the CosPlay game engine unlike [[CPScene scenes]] and [[CPSceneObject scene objects]]
  * that are managed and governed by the game engine. Assets are typically created outside the game loop and
  * managed by the developer, they can be freely shared between scenes or scene objects as any other standard
  * Scala objects.
  *
  * CosPlay comes with a full support for [[CPFIGLetFont FIGLet fonts]] as well as standard "system" font , all
  * of which implement this interface. In other words, there's no difference whether you are
  * drawing with a system (1-character height) font or a FIGLet font.
  *
  * @see [[CPFIGLetFont]] FIGLet fonts.
  * @see [[CPSystemFont]] System font.
  * @see [[CPStyledString]] Styled string using system font.
  * @see [[CPCanvas.drawStyledString()]] Canvas drawing using system font or styled strings.
  * @example See [[org.cosplay.examples.fonts.CPFontsExample CPFontsExample]] source code for an
  *     example of font functionality.
  */
abstract class CPFont(origin: String) extends CPGameObject with CPAsset:
    /** @inheritdoc */
    override val getOrigin: String = origin

    /**
      * Whether or not this is a "standard" 1-character height system font.
      */
    def isSystem: Boolean

    /**
      * Gets encoding used by this font. Note that for the system font it returns `"UTF-8"`.
      */
    def getEncoding: String

    /**
      * Gets height of this font in standard characters.
      */
    def getHeight: Int

    /**
      * Gets width of this font in standard characters.
      */
    def getWidth: Int

    /**
      * Gets baseline of this font in standard character.
      */
    def getBaseline: Int

    /**
      * Renders given multiline text as an image. Supplied string will be split by system-specific
      * "new line' character sequence ('\n' or '\n\r'). This call is equivalent to:
      * {{{
      * renderSeq(s.split(CPUtils.NL).filter(!_.isBlank).toSeq, fg, bg, align)
      * }}}
      *
      * @param s Multiline text to render as an image. Must be non-empty.
      * @param fg Foreground color to use.
      * @param bg Optional background color to use. Default value is `None`.
      * @param align Alignment of text. The only allowed values:
      *  - `-1` - right justified alignment.
      *  - `0` - centered alignment.
      *  - `1` - right justified alignment.
      *
      *  Default value is `0`.
      *
      * @return Image as a rendering of the given string with this font.
      */
    def renderMulti(s: String, fg: CPColor, bg: Option[CPColor] = None, align: Int = 0): CPImage =
        renderSeq(s.split(CPUtils.NL).filter(!_.isBlank).toSeq, fg, bg, align)

    /**
      * Renders single line text as an image with this font.
      *
      * @param s Text to render. Must be non-empty.
      * @param fg Foreground color to use.
      * @param bg Optional background color to use. Default value is `None`.
      * @return Image as a rendering of the given single line text with this font.
      */
    def render(s: String, fg: CPColor, bg: Option[CPColor] = None): CPImage

    /**
      * Renders given multiline text as an image.
      *
      * @param ss Sequence of text lines to render as an image. Must be non-empty.
      * @param fg Foreground color to use.
      * @param bg Optional background color to use. Default value is `None`.
      * @param align Alignment of text. The only allowed values:
      *  - `-1` - right justified alignment.
      *  - `0` - centered alignment.
      *  - `1` - right justified alignment.
      *
      *  Default value is `0`.
      *
      * @return Image as a rendering of the given string with this font.
      */
    def renderSeq(ss: Seq[String], fg: CPColor, bg: Option[CPColor] = None, align: Int = 0): CPImage =
        require(ss.nonEmpty, "Sequence of text lines cannot be empty.")
        require(align == -1 || align == 0 || align == 1, "Align value must be -1, 1 or 1.")

        if ss.sizeIs == 1 then render(ss.head, fg, bg)
        else
            val imgs: Seq[CPImage] = ss.map(render(_, fg, bg))
            val maxW = imgs.maxBy(_.w).w
            val bgPx = CPPixel(' ', fg, bg)

            def doAlign(img: CPImage): CPImage =
                val w = img.w
                val d = maxW - w
                if d == 0 then img
                    else if align == -1 then img.cropByInsets(CPInsets(0, 0, 0, d), bgPx)
                    else if align == 1 then img.cropByInsets(CPInsets(0, d, 0, 0), bgPx)
                    else
                        val half1 = d / 2
                        val half2 = d - half1
                        img.cropByInsets(CPInsets(0, half1, 0, half2), bgPx) // align == 0

            imgs.tail.foldLeft(doAlign(imgs.head))((a, b) => a.stitchBelow(doAlign(b), bgPx))
