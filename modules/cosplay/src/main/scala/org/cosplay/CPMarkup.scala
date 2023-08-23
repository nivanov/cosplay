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

import org.cosplay
import org.cosplay.*
import org.cosplay.CPPixel.*
import org.cosplay.impl.*
import scala.collection.mutable.ArrayBuffer

/*
   _________            ______________
   __  ____/_______________  __ \__  /_____ _____  __
   _  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
   / /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
   \____/  \____//____/ /_/     /_/  \__,_/ _\__, /
                                            /____/

          2D ASCII JVM GAME ENGINE FOR SCALA3
              (C) 2021 Rowan Games, Inc.
                All rights reserved.
*/

/**
  * Markup element.
  *
  * @param openTag Open tag.
  * @param closeTag Closing tag.
  * @param skin Character to pixel converter.
  * @see [[CPMarkup]]
  */
case class CPMarkupElement(openTag: String, closeTag: String, skin: Char => CPPixel) extends Serializable

/**
  * Markup specification that can be used to convert a sequence of characters to the sequence of [[CPPixel pixels]]
  * based on this specification.
  *
  * Each specification has mandatory default foreground and optional background colors. It also has a list
  * of markup elements (potentially empty) where each element has opening and closing tag and a function
  * that takes a character and returns [[CPPixel pixel]].
  *
  * Here's an example of defining the markup:
  * {{{
  *     val markup = CPMarkup(
  *         C_GREEN,
  *         C_BLACK.?,
  *         Seq(
  *             CPMarkupElement("<$", "$>", _&&(C_RED, C_WHITE)),
  *             CPMarkupElement("{#", "#}", _&&(C_BLUE, C_YELLOW)),
  *             CPMarkupElement("(?", "?)", _&&(C_BLACK, C_WHITE))
  *         )
  *     )
  * }}}
  * Once defined this markup can be used to convert a string ito sequence of pixels and then create an
  * image:
  * {{{
  *     val pxs = markup.process("text <$ red on white (? black on white ?)$> {# blue on yellow #}")
  *     val img = CPArrayImage(pxs, bgPx, align = -1) // Left-aligned image.
  * }}}
  *
  * @param fg Default foreground color.
  * @param bg Default optional background color.
  * @param elements Markup elements. Can be empty.
  * @see [[CPMarkupElement]]
  * @see [[CPArrayImage.apply()]] method for creating an image from the list of pixel representing text.
  */
case class CPMarkup(fg: CPColor, bg: Option[CPColor], elements: Seq[CPMarkupElement]) extends Serializable:
    for elm <- elements do
        !>(
            elm.openTag.nonEmpty && elm.closeTag.nonEmpty,
            s"Markup cannot have empty opening or closing tags: '${elm.openTag}' '${elm.closeTag}'"
        )
        !>(!elm.openTag.contains(' '), s"Markup opening tag cannot have space: '${elm.openTag}'")
        !>(!elm.closeTag.contains(' '), s"Markup closing tag cannot have space: '${elm.closeTag}'")
    !>(!CPUtils.hasDups(elements.flatMap(e => Seq(e.openTag, e.closeTag))), s"Markup opening and closing tags cannot have duplicates.")
    !>(!elements.exists(elm => elements.exists(x =>
        x != elm &&
        (
            x.openTag.contains(elm.openTag) ||
            x.openTag.contains(elm.closeTag) ||
            x.closeTag.contains(elm.openTag) ||
            x.closeTag.contains(elm.closeTag) ||
            elm.openTag.contains(x.closeTag) ||
            elm.closeTag.contains(x.closeTag) ||
            elm.openTag.contains(x.openTag) ||
            elm.closeTag.contains(x.openTag)
        )
    )), "Markup elements cannot have intersecting opening or closing tags.")

    /**
      * Creates new markup spec with given parameters.
      *
      * @param fg Default foreground color.
      * @param bg Default optional background color.
      * @param elms Markup elements as list of tuples. Can be empty.
      */
    def this(fg: CPColor, bg: Option[CPColor], elms: List[(String, String, Char => CPPixel)]) =
        this(fg, bg, elms.map(elm => CPMarkupElement(elm._1, elm._2, elm._3)))

    /**
      * Creates new markup spec with a single markup element.
      *
      * @param fg Default foreground color.
      * @param bg Default optional background color.
      * @param openTag Open tag.
      * @param closeTag Closing tag.
      * @param skin Character to pixel converter.
      */
    def this(fg: CPColor, bg: Option[CPColor], openTag: String, closeTag: String, skin: Char => CPPixel) =
        this(fg, bg, Seq(CPMarkupElement(openTag, closeTag, skin)))

    /**
      * Converts sequence of characters into list of [[CPPixel pixels]] based on this markup.
      * See [[CPArrayImage.apply()]] method for a convenient way of creating an image from the list of pixel
      * representing text.
      *
      * @param in Input sequence of characters.
      * @see [[CPArrayImage.apply()]] method for creating an image from the list of pixel representing text.
      */
    def process(in: String): List[CPPixel] =
        var skin = (ch: Char) => ch&&(fg, bg)
        var skinStack = List(skin)
        val buf = ArrayBuffer.empty[CPPixel]
        val len = in.length
        var idx = 0
        var start = 0
        while idx < len do
            val s = in.substring(start, idx + 1)
            elements.find(elm => s.endsWith(elm.openTag)) match
                case Some(elm) =>
                    for ch <- s.substring(0, s.length - elm.openTag.length) do buf += skin(ch)
                    start += s.length
                    // Stack push.
                    skin = elm.skin
                    skinStack ::= skin
                case None =>
                    elements.find(elm => s.endsWith(elm.closeTag)) match
                        case Some(elm) =>
                            for ch <- s.substring(0, s.length - elm.closeTag.length) do buf += skin(ch)
                            start += s.length
                            // Stack pop.
                            skinStack = skinStack.tail
                            skin = skinStack.head
                        case None => ()
            idx += 1
        for i <- start until len do buf += skin(in.charAt(i))
        buf.toList


