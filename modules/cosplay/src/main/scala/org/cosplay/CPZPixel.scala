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
  * Immutable container that combines [[CPPixel pixel]] and its Z-index.
  *
  * @param px Pixel.
  * @param z Pixel's Z-index.
  * @see [[CPPosPixel]]
  * @see [[CPZPixelPane]]
  */
final case class CPZPixel(px: CPPixel, z: Int):
    /** Shortcut for pixel's [[CPPixel.char character]]. */
    val char: Char = px.char

    /** Shortcut for pixel's [[CPPixel.fg foreground]]. */
    val fg: CPColor = px.fg

    /** Shortcut for pixel's [[CPPixel.bg background]]. */
    val bg: Option[CPColor] = px.bg

    /** Shortcut for pixel's [[CPPixel.tag tag]]. */
    val tag: Int = px.tag

    /**
      * Gets a copy of this Z-pixel with a new character.
      *
      * @param ch New character.
      */
    inline def withChar(ch: Char): CPZPixel = if px.char != ch then CPZPixel(px.withChar(ch), z) else this

    /**
      * Gets a copy of this Z-pixel with a new foreground.
      *
      * @param c New foreground.
      */
    inline def withFg(c: CPColor): CPZPixel = if px.fg != c then CPZPixel(px.withFg(c), z) else this

    /**
      * Gets a copy of this Z-pixel with a new tag.
      *
      * @param t New tag.
      */
    inline def withTag(t: Int): CPZPixel = if px.tag != t then CPZPixel(px.withTag(t), z) else this

    /**
      * Gets a copy of this Z-pixel with a new background.
      *
      * @param c New background.
      */
    inline def withBg(c: Option[CPColor]): CPZPixel = if px.bg != c then CPZPixel(px.withBg(c), z) else this

    /**
      * Gets a copy of this Z-pixel with a new foreground and background.
      *
      * @param fg New foreground.
      * @param bg New background.
      */
    inline def withFgBg(fg: CPColor, bg: Option[CPColor]): CPZPixel =
        if px.bg != bg || px.fg != fg then CPZPixel(px.withFgBg(fg, bg), z) else this