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

import scala.collection.mutable

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
  * Builder for a styled string rendered using system font.
  *
  * This utility class provides a builder pattern to built styled (colored) strings that
  * are rendered using a [[CPSystemFont system font]]. Once styled string is built it can be
  * rendered via [[CPCanvas.drawStyledString()]] method.
  *
  * Here's an example of using styled string together with [[CPArrayImage array image]] to quickly
  * create an image with a styled textual content:
  * {{{
  * import CPStyledString.styleStr
  * 
  * val lblImg = new CPArrayImage(
  *     styleStr("[Space]", C_WHITE) ++ styleStr("Play|Pause ", C_GREEN) ++
  *     styleStr("[R]", C_WHITE) ++ styleStr("Rewind ", C_GREEN) ++
  *     styleStr("[Q]", C_WHITE) ++ styleStr("Quit ", C_GREEN) ++
  *     styleStr("[Ctrl-L]", C_WHITE) ++ styleStr("Log ", C_GREEN) ++
  *     styleStr("[Ctrl-Q]", C_WHITE) ++ styleStr("FPS Overlay", C_GREEN)
  * ).trimBg()
  * }}}
  *
  * @see [[CPCanvas.drawStyledString()]]
  * @see [[CPSystemFont]]
  * @example See [[org.cosplay.examples.fonts.CPFontsExample CPFontsExample]] source code for an
  *     example of font functionality.
  */
class CPStyledString:
    private var fg: CPColor = _
    private var bg: Option[CPColor] = None
    private val buf = mutable.Buffer.empty[CPPixel]

    /**
      * Creates styled string.
      *
      * @param s String.
      * @param fgColor Foreground color.
      * @param bgColor Background color.
      */
    def this(s: String, fgColor: CPColor, bgColor: CPColor) =
        this()
        fg(fgColor)
        bg(bgColor)
        str(s)

    /**
      * Creates styled string with no background.
      *
      * @param s String.
      * @param fgColor Foreground color.
      */
    def this(s: String, fgColor: CPColor) =
        this()
        fg(fgColor)
        str(s)

    /**
      * Clears this styled string.
      */
    def clear(): CPStyledString =
        buf.clear()
        this

    /**
      * Adds given object string representation to this style string using current foreground and
      * background colors.
      *
      * @param obj Object to add.
      */
    def str(obj: Any): CPStyledString =
        if fg == null then E(s"Foreground color must be specified first.")
        for (ch <- obj.toString) buf += CPPixel(ch, fg, bg)
        this

    /**
      * Sets current foreground color.
      *
      * @param fg Foreground color to set.
      */
    def fg(fg: CPColor): CPStyledString =
        this.fg = fg
        this

    /**
      * Sets current foreground and background colors.
      *
      * @param fg Foreground color to set.
      * @param bg Background color to set. Use `None` to remove background color.
      */
    def fgbg(fg: CPColor, bg: Option[CPColor]): CPStyledString =
        this.fg = fg
        this.bg = bg
        this

    /**
      * Sets current foreground and background colors.
      *
      * @param fg Foreground color to set.
      * @param bg Background color to set.
      */
    def fgbg(fg: CPColor, bg: CPColor): CPStyledString =
        this.fg = fg
        this.bg = Option(bg)
        this

    /**
      * Sets current background color.
      *
      * @param bg Background color to set. Use `None` to remove background color.
      */
    def bg(bg: Option[CPColor]): CPStyledString =
        this.bg = bg
        this

    /**
      * Sets current background color.
      *
      * @param bg Background color to set.
      */
    def bg(bg: CPColor): CPStyledString =
        this.bg = Option(bg)
        this

    /**
      * Finalizes this styled string and renders it into sequence of [[CPPixel pixels]].
      */
    def build(): Seq[CPPixel] = buf.toSeq

/**
  * Companion object contains factory functions.
  */
object CPStyledString:
    /**
      * Creates and builds style string with no background.
      *
      * @param s String.
      * @param fg Foreground color to use.
      */
    def styleStr(s: String, fg: CPColor): Seq[CPPixel] = CPStyledString(s, fg).build()

    /**
      * Creates and builds style string.
      *
      * @param s String.
      * @param fg Foreground color to use.
      * @param bg Background color to use.
      */
    def styleStr(s: String, fg: CPColor, bg: CPColor): Seq[CPPixel] = CPStyledString(s, fg, bg).build()
