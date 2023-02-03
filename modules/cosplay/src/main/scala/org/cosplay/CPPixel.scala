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

import impl.CPAnsi.*
import CPColor.*
import CPPixel.*

import scala.annotation.targetName

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

/**
  * Single immutable character pixel.
  *
  * Character-pixel is a fundamental graphics unit in ASCII-based games. Just like the raster pixel on a graphics
  * screen it represents the smallest area that can be rendered on the screen.
  *
  * CosPlay pixel is immutable and consists of a character from the ASCII character set, a foreground color, an optional
  * background color and an optional tag. Note that pixel itself does not have a Z-index as it can be rendered
  * at any Z-index.
  *
  * Pixel creation and manipulation is used extensively throughout a CosPlay game. There are several ways you
  * can create a pixel:
  * {{{
  *     import org.cosplay.*
  *     import CPColor.*
  *     import CPPixel.*
  *
  *     // Canonical black 'x' on white background.
  *     new CPPixel('x', C_BLACK, Some(C_WHITE), 0)
  *
  *     // Few companion object shortcuts for often used cases...
  *     CPPixel('x', C_BLACK, C_WHITE) // Avoiding 'Some'.
  *     CPPixel('x', C_BLACK) // Skipping background all together.
  *     CPPixel('x', C_BLACK, 2) // Skipping background & setting a '2' ag.
  *     CPPixel('x', C_BLACK, C_WHITE, 2) // Setting up a '2' tag.
  * }}}
  * You can also use a convenient syntactic sugar `'&'` and `'&&'`based on given conversion and extension of `Char` type.
  * The following unit test demonstrates that usage. Note that usage of `'&'` extension operators is the
  * recommended way and that is what is used internally by CosPlay:
  * {{{
  *     import org.cosplay.*
  *     import CPColor.*
  *     import CPPixel.*
  *     // Must be enabled for conversions.
  *     import scala.language.implicitConversions
  *
  *     val p1 = 'x'&C_BLACK // Recommended way.
  *     val p2: CPPixel = 'x' -> C_BLACK // Requires explicit type declaration.
  *     val p3: CPPixel = ('x', C_BLACK) // Requires explicit type declaration.
  *     val p4 = CPPixel('x', C_BLACK)
  *     val p5 = new CPPixel('x', C_BLACK, None, 0)
  *
  *     assertTrue(p1 == p2)
  *     assertTrue(p2 == p3)
  *     assertTrue(p3 == p4)
  *     assertTrue(p4 == p5)
  *
  *     val p6 = 'x'&&(C_BLACK, C_WHITE) // Recommended way.
  *     val p7 = new CPPixel('x', C_BLACK, Option(C_WHITE), 0)
  *
  *     assertTrue(p6 == p7)
  * }}}
  *
  * @param char Pixel character.
  * @param fg Pixel foreground color.
  * @param bg Background color. If not provided, the background color of this pixel will not be set when rendering
  *     on the screen.
  * @param tag Pixel tag. Tag can be used to tag a pixel, for example, during a fill in algorithm to mark the
  *     pixel as touched.
  * @see [[CPPosPixel]]
  * @see [[CPZPixel]]
  */
final case class CPPixel(char: Char, fg: CPColor, bg: Option[CPColor] = None, tag: Int) extends Serializable:
    private var shadow: CPPixel = _

    override def toString: String = s"Pixel ['$char', fg=$fg, bg=$bg, tag=$tag]"

    /** ANSI sequence to render this pixel on ANSI terminal. */
    val ansi = s"$RESET_ALL${fg.fgAnsi}${if bg.isEmpty then "" else bg.get.bgAnsi}$char"

    /**
      * Gets a new pixel with the lower case character.
      */
    lazy val toLower: CPPixel = CPPixel(char.toLower, fg, bg, tag)

    /**
      * Gets a new pixel with the upper case character.
      */
    lazy val toUpper: CPPixel = CPPixel(char.toUpper, fg, bg, tag)

    /**
      * Gets a new pixel with inverse foreground and background color. If background color
      * is not set, returns this instance.
      */
    lazy val inverse: CPPixel = if bg.isEmpty then this else CPPixel(char, bg.get, fg.?, tag)

    /**
      * Gets a copy of this pixel with a new character.
      *
      * @param x New character.
      */
    inline def withChar(x: Char): CPPixel = if char != x then CPPixel(x, fg, bg, tag) else this

    /**
      * Gets a copy of this pixel with a new foreground color.
      *
      * @param x New foreground color.
      */
    inline def withFg(x: CPColor): CPPixel = if fg != x then CPPixel(char, x, bg, tag) else this

    /**
      * Gets a copy of this pixel with lighter foreground.
      *
      * @param factor Mixing factor in `[0.1]` range. `1.0` means pure white, `0.9` means 90% lighter,
      *     `0.1` means 10% lighter.
      * @see [[CPColor.lighter()]]
      */
    inline def withLighterFg(factor: Float): CPPixel = CPPixel(char, fg.lighter(factor), bg, tag)

    /**
      * Gets a copy of this pixel with darker foreground.
      *
      * @param factor Mixing factor in `[0,1]` range. `0.9` means 90% darker, `0.1` means 10% darker.
      * @see [[CPColor.darker()]]
      */
    inline def withDarkerFg(factor: Float): CPPixel = CPPixel(char, fg.darker(factor), bg, tag)

    /**
      * Gets a copy of this pixel with lighter background.
      *
      * @param factor Mixing factor in `[0.1]` range. `1.0` means pure white, `0.9` means 90% lighter,
      *     `0.1` means 10% lighter.
      * @see [[CPColor.lighter()]]
      */
    inline def withLighterBg(factor: Float): CPPixel = bg match
        case Some(c) => CPPixel(char, fg, c.lighter(factor).?, tag)
        case None => this

    /**
      * Gets a copy of this pixel with darker background.
      *
      * @param factor Mixing factor in `[0,1]` range. `0.9` means 90% darker, `0.1` means 10% darker.
      * @see [[CPColor.darker()]]
      */
    inline def withDarkerBg(factor: Float): CPPixel = bg match
        case Some(c) => CPPixel(char, fg, c.darker(factor).?, tag)
        case None => this

    /**
      * Gets a copy of this pixel with a new tag.
      *
      * @param t New tag.
      */
    inline def withTag(t: Int): CPPixel = if tag != t then CPPixel(char, fg, bg, t) else this

    /**
      * Gets a copy of this pixel with a new background color.
      *
      * @param x New background color.
      */
    inline def withBg(x: Option[CPColor]): CPPixel =
        if x != bg then
            if !(shadow != null && (shadow.bg == x || (shadow.bg.isDefined && x.isDefined && shadow.bg.get == x.get))) then
                shadow = CPPixel(char, fg, x, tag)

            require(shadow != null)
            shadow
        else
            this

    /**
      * Gets a copy of this pixel with a new foreground and background color.
      *
      * @param a New foreground color.
      * @param b New background color.
      */
    inline def withFgBg(a: CPColor, b: Option[CPColor]): CPPixel = if fg != a || bg != b then CPPixel(char, a, b, tag) else this

    /**
      * Tests whether this pixel is [[CPPixel.XRAY]].
      */
    lazy val isXray: Boolean = char == XRAY_CH

/**
  * Companion object with utility functions.
  */
object CPPixel:
    private val XRAY_CH = '\u0000'

    /**
      * Transparent non-rendering pixel. Background and foreground are unused. Note that this pixel
      * is never rendered on any terminal. It can only be used internally for
      * drawing on canvas, creating images, etc.
      */
    val XRAY: CPPixel = CPPixel(XRAY_CH, C_BLACK)

    /**
      * Creates new pixel without background and default zero tag.
      *
      * @param char Pixel character.
      * @param fg Pixel foreground.
      * @note Pixel tag will be set to zero.
      */
    def apply(char: Char, fg: CPColor): CPPixel = new CPPixel(char, fg, None, 0)

    /**
      * Creates new pixel without background.
      *
      * @param char Pixel character.
      * @param fg Pixel foreground.
      * @param tag Pixel tag.
      */
    def apply(char: Char, fg: CPColor, tag: Int): CPPixel = new CPPixel(char, fg, None, tag)

    /**
      * Creates new pixel.
      *
      * @param char Pixel character.
      * @param fg Pixel foreground.
      * @param bg Pixel background.
      * @note Pixel tag will be set to zero.
      */
    def apply(char: Char, fg: CPColor, bg: CPColor): CPPixel = new CPPixel(char, fg, bg.?, 0)

    /**
      * Creates new pixel.
      *
      * @param char Pixel character.
      * @param fg Pixel foreground.
      * @param bg Optional pixel background.
      * @note Pixel tag will be set to zero.
      */
    def apply(char: Char, fg: CPColor, bg: Option[CPColor]): CPPixel = new CPPixel(char, fg, bg, 0)

    /**
      * Creates new pixel.
      *
      * @param char Pixel character.
      * @param fg Pixel foreground.
      * @param bg Pixel background.
      * @param tag Pixel tag.
      */
    def apply(char: Char, fg: CPColor, bg: CPColor, tag: Int): CPPixel = new CPPixel(char, fg, bg.?, tag)

    /**
      * Creates new pixel.
      *
      * @param char Pixel character.
      * @param fg Pixel foreground.
      * @param bg Optional pixel background.
      * @param tag Pixel tag.
      */
    def apply(char: Char, fg: CPColor, bg: Option[CPColor], tag: Int): CPPixel = new CPPixel(char, fg, bg, tag)

    /**
      * Makes a sequence of pixel from the range of characters. Range must be sequential.
      *
      * @param first First character in the range.
      * @param last Last character in the range.
      * @param fg Foreground color.
      * @param bg Background color.
      * @note Pixel tag will be set to zero.
      */
    def seq(first: Char, last: Char, fg: CPColor, bg: Option[CPColor]): Seq[CPPixel] =
        if first > last then E(s"'first' char ('$first') must < 'last' char ('$last').")
        for ch <- first to last yield CPPixel(ch, fg, bg, 0)

    /**
      * Makes a sequence of pixel from the range of characters. Range must be sequential.
      *
      * @param first First character in the range.
      * @param last Last character in the range.
      * @param fgf Function for foreground color.
      * @param bgf Function for background color.
      * @note Pixel tag will be set to zero.
      */
    def seq(first: Char, last: Char, fgf: Char => CPColor, bgf: Char => Option[CPColor]): Seq[CPPixel] =
        if first > last then E(s"'first' char ('$first') must < 'last' char ('$last').")
        for ch <- first to last yield CPPixel(ch, fgf(ch), bgf(ch), 0)

    /**
      * Makes a sequence of pixel from given string.
      *
      * @param chars Sequence of characters.
      * @param fg Foreground color.
      * @param bg Background color.
      * @note Pixel tag will be set to zero.
      */
    def seq(chars: String, fg: CPColor, bg: Option[CPColor]): Seq[CPPixel] =
        for ch <- chars yield CPPixel(ch, fg, bg, 0)

    /**
      * Makes a sequence of pixel from given string.
      *
      * @param chars Sequence of characters.
      * @param fgf Function for foreground color.
      * @param bgf Function for background color.
      * @note Pixel tag will be set to zero.
      */
    def seq(chars: String, fgf: Char => CPColor, bgf: Char => Option[CPColor]): Seq[CPPixel] =
        for ch <- chars yield CPPixel(ch, fgf(ch), bgf(ch), 0)

    given Conversion[(Char, CPColor), CPPixel] = t => CPPixel(t._1, t._2)

    extension(ch: Char)
        /**
          * Adds `'&'` operator to `Char` type as a sugar to create pixel without background. For example:
          * {{{
          *     val x = 'x'&C_BLACK
          *     val ch = 'a'
          *     val a = ch&C_WHITE
          * }}}
          */
        @targetName("mkCharFgPixel")
        infix def &(c: CPColor): CPPixel = CPPixel(ch, c)

        /**
          * Adds `'&&'` operator to `Char` type as a sugar to create pixel with background. For example:
          * {{{
          *     val x = 'x'&&(C_BLACK, C_WHITE)
          *     val ch = 'a'
          *     val a = ch&&(C_WHITE, C_PINK)
          * }}}
          */
        @targetName("mkCharFgBgPixel")
        infix def &&(fg: CPColor, bg: CPColor): CPPixel = CPPixel(ch, fg, bg.?)

        /**
          * Adds `'&?'` operator to `Char` type as a sugar to create pixel with background. For example:
          * {{{
          *     val x = 'x'&?(C_BLACK, Option(C_WHITE))
          *     val ch = 'a'
          *     val a = ch&?(C_WHITE, Some(C_PINK))
          * }}}
          */
        @targetName("mkCharFgOptBgPixel")
        infix def &?(fg: CPColor, bg: Option[CPColor]): CPPixel = CPPixel(ch, fg, bg)