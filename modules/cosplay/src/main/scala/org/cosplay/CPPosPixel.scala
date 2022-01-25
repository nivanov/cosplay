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

/**
  * Immutable container for [[CPPixel pixel]] and its XY-coordinate.
  *
  * @param px Pixel.
  * @param x Pixel X-coordinate.
  * @param y Pixel Y-coordinate.
  * @see [[CPZPixel]]
  */
final case class CPPosPixel(px: CPPixel, x: Int, y: Int) extends Ordered[CPPosPixel] :
    override def compare(that: CPPosPixel): Int =
        if x < that.x then -1
        else if x > that.x then 1
        else if y < that.y then -1
        else if y > that.y then 1
        else 0

    /**
      *
      */
    val char: Char = px.char

    /**
      *
      */
    val fg: CPColor = px.fg

    /**
      *
      */
    val bg: Option[CPColor] = px.bg

    /**
      *
      */
    val tag: Int = px.tag

    /**
      *
      */
    lazy val pos: (Int, Int) = x -> y

    /**
      *
      */
    lazy val toInt2: CPInt2 = CPInt2(x, y)

    /**
      *
      * @param ch
      */
    inline def withChar(ch: Char): CPPosPixel = if px.char != ch then CPPosPixel(CPPixel(ch, px.fg, px.bg, px.tag), x, y) else this

    /**
      *
      * @param c
      */
    inline def withFg(c: CPColor): CPPosPixel = if px.fg != c then CPPosPixel(CPPixel(px.char, c, px.bg, px.tag), x, y) else this

    /**
      *
      * @param t
      */
    inline def withTag(t: Int): CPPosPixel = if px.tag != t then CPPosPixel(CPPixel(px.char, px.fg, px.bg, t), x, y) else this

    /**
      *
      * @param c
      */
    inline def withBg(c: Option[CPColor]): CPPosPixel = if px.bg != c then CPPosPixel(CPPixel(px.char, px.fg, c, px.tag), x, y) else this

    /**
      *
      * @param other
      */
    inline def isTopOf(other: CPPosPixel): Boolean = y < other.y

    /**
      *
      * @param other
      */
    inline def isBelowOf(other: CPPosPixel): Boolean = y > other.y

    /**
      *
      * @param other
      */
    inline def isRightOf(other: CPPosPixel): Boolean = x > other.x

    /**
      *
      * @param other
      */
    inline def isLeftOf(other: CPPosPixel): Boolean = x < other.x

    /**
      *
      * @param other
      */
    inline def isSamePos(other: CPPosPixel): Boolean = x == other.x && y == other.y

    /**
      *
      * @param other
      */
    inline def isSameRow(other: CPPosPixel): Boolean = y == other.y

    /**
      *
      * @param other
      */
    inline def isSameColumn(other: CPPosPixel): Boolean = x == other.x

    /**
      *
      * @param other
      */
    inline def isTopRightOf(other: CPPosPixel): Boolean = x > other.x && y < other.y

    /**
      *
      * @param other
      */
    inline def isTopLeftOf(other: CPPosPixel): Boolean = x < other.x && y < other.y

    /**
      *
      * @param other
      */
    inline def isBellowLeftOf(other: CPPosPixel): Boolean = x < other.x && y > other.y

    /**
      *
      * @param other
      */
    inline def isBellowRightOf(other: CPPosPixel): Boolean = x > other.x && y > other.y
