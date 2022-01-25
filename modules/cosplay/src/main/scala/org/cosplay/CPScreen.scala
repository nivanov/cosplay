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

import org.cosplay.*
import org.cosplay.impl.CPUtils

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
  * Game engine view on terminal screen.
  *
  * @param dim Screen dimension.
  * @param bgPixel Background pixel for the screen.
  */
final class CPScreen(dim: CPDim, bgPixel: CPPixel) extends CPZPixelPane:
    private val buf = new CPArray2D[CPZPixel](dim, CPZPixel(bgPixel, Int.MinValue))
    private val rect: CPRect = buf.rect

    override def getDim: CPDim = dim
    override def getBgPixel: CPPixel = bgPixel

    /**
      * Gets rectangular shape of this screen.
      */
    def getRect: CPRect = rect

    /**
      * Copies region of this screen to another screen.
      *
      * @param other Other screen to copy to.
      */
    def copyTo(other: CPScreen, target: CPRect): Unit = buf.copyTo(other.buf, target)

    /**
      * Creates a new screen that is a copy of this screen.
      */
    def copy(): CPScreen =
        val newScr = CPScreen(dim, bgPixel)
        copyTo(newScr, rect)
        newScr

    override def addPixel(zpx: CPZPixel, x: Int, y: Int): Unit = if rect.contains(x, y) then buf.set(x, y, zpx)
    override def getPixel(x: Int, y: Int): CPZPixel = buf.get(x, y)
    override def optPixel(x: Int, y: Int): Option[CPZPixel] = Option.when(rect.contains(x, y))(buf.get(x, y))

    /**
      * Gets the pixel at given XY-coordinate.
      *
      * @param x X-coordinate of the pixel to get.
      * @param y Y-coordinate of the pixel to get.
      */
    def apply(x: Int, y: Int): Option[CPZPixel] = optPixel(x, y)

    /**
      * Clears this screen.
      */
    def clear(): Unit = buf.clear()

    /**
      * Creates new canvas that can be used to draw on this screen.
      */
    def canvas(): CPCanvas = new CPCanvas(this, rect)

    /**
      * Creates new canvas that can be used to draw on this screen with given
      * clipping region.
      *
      * @param clip Clipping region.
      */
    def canvas(clip: CPRect): CPCanvas = new CPCanvas(this, clip)

    override def equals(obj: Any): Boolean = obj match
        case other: CPScreen => buf == other.buf
        case _ => false
