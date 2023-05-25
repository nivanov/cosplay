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

import org.cosplay.CPArrayImage.*
import org.cosplay.impl.CPUtils
import scala.collection.mutable.ArrayBuffer

/**
  * An image based on two-dimensional pixel array.
  *
  * This is the primary tool for creating in-code images.
  * This class has number of constructors and companion utility methods to aid in creating
  * in-code images in variety of ways. Here's a typical example of using this class to create
  * an in-code image. Note the use of companion object function [[prepSeq()]]:
  * {{{
  * object CPAlienImage extends CPArrayImage(
  *     prepSeq("""
  *         |    .  .
  *         |     \/
  *         |    (@@)
  *         | g/\_)(_/\e
  *         |g/\(=--=)/\e
  *         |    //\\
  *         |   _|  |_
  *       """),
  *     (ch, _, _) => ch&C_LIME
  * )
  * }}}
  *
  * @param data Image data. Note that given data array should not have `null` values.
  * @param origin The origin of the image like file path or URL.
  */
class CPArrayImage(data: CPArray2D[CPPixel], origin: String = "code") extends CPImage(origin):
    !>(!data.contains(_ == null), "Image data cannot have 'null' values.")

    /**
      * Creates an image from 2D array of characters.
      *
      * @param data Image data.
      * @param skin Skinning function. The function takes a character, its X and Y coordinate and returns
      *     a new pixel.
      */
    def this(data: CPArray2D[Char], skin: (Char, Int, Int) => CPPixel) = this(data.map((ch, x, y) => skin(ch, x, y)))

    /**
      * Creates an image from the sequence of strings.
      *
      * @param data Image data.
      * @param skin Skinning function. The function takes a character, its X and Y coordinate and returns
      *     a new pixel.
      */
    def this(data: Seq[String], skin: (Char, Int, Int) => CPPixel) = this(CPArray2D(data), skin)

    /**
      * Creates an image from a single string.
      *
      * @param data Single string image data.
      * @param skin Skinning function. The function takes a character, its X and Y coordinate and returns
      *     a new pixel.
      */
    def this(data: String, skin: (Char, Int, Int) => CPPixel) = this(Seq(data), skin)

    /**
      * Creates a single pixel image.
      *
      * @param px Image pixel.
      */
    def this(px: CPPixel) = this(new CPArray2D[CPPixel](px))

    /**
      * Creates an image from give 2D array of characters.
      *
      * @param chArr Image data.
      * @param fg Foreground color of image pixels.
      * @param bg Optional background color for image pixels.
      */
    def this(chArr: CPArray2D[Char], fg: CPColor, bg: Option[CPColor]) = this(chArr.map(ch => CPPixel(ch, fg, bg, 0)))

    /**
      * Creates an image from give 2D array of characters without background color.
      *
      * @param chArr Image data.
      * @param fg Foreground color of image pixels.
      */
    def this(chArr: CPArray2D[Char], fg: CPColor) = this(chArr.map(ch => CPPixel(ch, fg, None, 0)))

    /**
      * Creates an image from give string.
      *
      * @param data Single string image data.
      * @param fg Foreground color of image pixels.
      * @param bg Optional background color for image pixels.
      */
    def this(data: String, fg: CPColor, bg: Option[CPColor]) = this(CPArray2D(data), fg, bg)

    /**
      * Creates an image from give string without background color.
      *
      * @param data Single string image data.
      * @param fg Foreground color of image pixels.
      */
    def this(data: String, fg: CPColor) = this(CPArray2D(data), fg, None)

    /**
      * Creates an image from given sequence of pixels.
      *
      * @param pxs Sequence of pixel to create a new image from.
      */
    def this(pxs: Seq[CPPixel]) = this(CPArray2D(pxs, pxs.size))

    /** @inheritdoc */ 
    override def getDim: CPDim = data.dim
    /** @inheritdoc */ 
    override def getPixel(x: Int, y: Int): CPPixel = data.get(x, y)

/**
  * Companion object contains utility functions.
  */
object CPArrayImage:
    /**
      * This is a special constructor that expects given sequence of pixels to represent one or
      * multiple lines of text. This method takes given pixels, splits them into individual lines by the
      * system-specific new line separator, and then aligns those lines based on the [[align]] parameter. In the
      * end, it creates a new image from those split and optionally aligned lines of text.
      *
      * This constructor conveniently supports [[CPMarkup]] functionality. Here's an example of defining
      * [[CPMarkup]] and using this constructor to create an image:
      * {{{
      *     val markup = CPMarkup(
      *         C_GREEN,
      *         Option(C_BLACK),
      *         Seq(
      *             CPMarkupElement("<$", "$>", _&&(C_RED, C_WHITE)),
      *             CPMarkupElement("{#", "#}", _&&(C_BLUE, C_YELLOW)),
      *             CPMarkupElement("(?", "?)", _&&(C_BLACK, C_WHITE))
      *         )
      *     )
      *     val pxs = markup.process("text <$ red on white (? black on white ?)$> {# blue on yellow #}")
      *     val img = CPArrayImage(pxs, bgPx, align = -1) // Left-aligned image.
      * }}}
      *
      * @param pxs List of pixels to split and align.
      * @param spacePx A pixel to use to pad for leading and/or trailing spaces.
      * @param align Alignment of text. The only allowed values are (with `2` being the default value):
      *  - `-1` - left justified alignment, i.e. add necessary spaces to the end of the line..
      *  - `0` - centered alignment, i.e. add necessary spaces to both end and the beginning of the line.
      *  - `1` - right justified alignment, i.e. add necessary spaces at the beginning of the line.
      *  - `2` - no alignment, the text is taken as is. Necessary spaces added to the end of the each line.
      */
    def apply(pxs: Seq[CPPixel], spacePx: CPPixel, align: Int = 2): CPImage =
        !>(align == -1 || align == 0 || align == 1 || align == 2, "'align' parameter can only be one of '-1', '0', '1', or '2'.")

        val lines = CPUtils
            .splitBy(pxs.filter(_.char != '\r'), _.char == '\n')
            .map(seq => if align == 2 then seq else CPUtils.trimBy(seq, px => px.char == spacePx.char))
            .map(seq => ArrayBuffer.from(seq))
        val maxSz = lines.maxBy(_.size).size
        for line <- lines if line.size < maxSz do
            val d = maxSz - line.length
            if align == -1 || align == 2 then // Left align.
                (0 until d).foreach(_ => line += spacePx)
            else if align == 1 then // Right align.
                (0 until d).foreach(_ => line.prepend(spacePx))
            else // Center align.
                val left = d / 2
                val right = d - left
                (0 until left).foreach(_ => line.prepend(spacePx))
                (0 until right).foreach(_ => line += spacePx)

        new CPArrayImage(CPArray2D(lines.flatMap(_.toSeq), maxSz))

    /**
      * Converts margin-based Scala string into sequence of strings.
      *
      * @param marginCh Margin character.
      * @param s Margin-based Scala string to convert.
      * @param trim Whether or not to trim leading and trailing empty string.
      */
    def prepSeq(marginCh: Char, s: String, trim: Boolean): Seq[String] =
        if s.isEmpty then Seq.empty
        else
            var arr = CPUtils.splitByNewLine(s.stripMargin(marginCh))

            if arr.nonEmpty then
                if trim then
                    if arr.head.trim.isEmpty then arr = arr.tail // Trim leading empty string.
                    if arr.last.trim.isEmpty then arr = arr.dropRight(1) // Trim trailing empty string.
                arr
            else
                Seq.empty

    /**
      * Converts margin-based Scala string into sequence of strings. Equivalent to:
      * {{{
      *     prepSeq('|', s, trim)
      * }}}
      *
      * @param s Margin-based Scala string to convert. `'|'` character will be used for margin.
      * @param trim Whether or not to trim leading and trailing empty string. The default value is `true`.
      */
    def prepSeq(s: String, trim: Boolean = true): Seq[String] = prepSeq('|', s, trim)

    /**
      * Converts margin-based Scala string into sequence of strings.
      *
      * @param marginCh Margin character.
      * @param len Length to pad to each string.
      * @param s Margin-based Scala string to convert.
      * @param trim Whether or not to trim leading and trailing empty string.
      */
    def prepPadSeq(marginCh: Char, len: Int, s: String, trim: Boolean): Seq[String] =
        prepSeq(marginCh, s, trim).map(_.padTo(len, ' '))

    /**
      * Converts margin-based Scala string into sequence of strings.
      *
      * @param len Length to pad to each string.
      * @param s Margin-based Scala string to convert. `'|'` character will be used for margin.
      * @param trim Whether or not to trim leading and trailing empty string. The default value is `true`.
      */
    def prepPadSeq(len: Int, s: String, trim: Boolean = true): Seq[String] =
        prepSeq(s, trim).map(_.padTo(len, ' '))





