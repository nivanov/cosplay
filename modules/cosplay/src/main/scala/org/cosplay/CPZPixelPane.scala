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
  * `(0,0)`-based pane for [[CPZPixel]].
  *
  * It is used for internal purposes only.
  *
  * @see [[CPCanvas]]
  */
trait CPZPixelPane:
    /**
      * Gets dimension of this pane.
      */
    def getDim: CPDim

    /**
      * Gets background pixel of this pane.
      */
    def getBgPixel: CPPixel

    /**
      * Gets Z-pixel at given XY-coordinate.
      *
      * @param x X-coordinate of the pixel.
      * @param y Y-coordinate of the pixel.
      */
    def getPixel(x: Int, y: Int): CPZPixel

    /**
      * Gets optional Z-pixel at given XY-coordinate.
      *
      * @param x X-coordinate of the pixel.
      * @param y Y-coordinate of the pixel.
      * @return `Some` of pixel if given XY-coordinate is valid, `None` otherwise.
      */
    def optPixel(x: Int, y: Int): Option[CPZPixel]

    /**
      * Adds Z-pixel at given XY-coordinate.
      *
      * @param zpx Z-pixel to add.
      * @param x X-coordinate of the pixel.
      * @param y Y-coordinate of the pixel.
      */
    def addPixel(zpx: CPZPixel, x: Int, y: Int): Unit

    /**
      * Adds pixel at given XY-coordinate and Z-index.
      *
      * @param px Pixel to add.
      * @param x X-coordinate of the pixel.
      * @param y Y-coordinate of the pixel.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      */
    def addPixel(px: CPPixel, x: Int, y: Int, z: Int): Unit = addPixel(CPZPixel(px, z), x, y)

/**
  * Companion object with utility functions.
  */
object CPZPixelPane:
    /**
      * Creates new Z-pixel pane from given Z-pixel 2D array.
      *
      * @param arr Z-pixel 2D array.
      * @param bgPx Background pixel for new Z-pixel pane.
      */
    def apply(arr: CPArray2D[CPZPixel], bgPx: CPPixel): CPZPixelPane =
        if bgPx.isXray then E(s"Background pixel cannot be 'xray'.")

        new CPZPixelPane:
            override def getDim: CPDim = arr.dim
            override def getBgPixel: CPPixel = bgPx
            override def getPixel(x: Int, y: Int): CPZPixel = arr.get(x, y)
            override def optPixel(x: Int, y: Int): Option[CPZPixel] = Option.when(arr.rect.contains(x, y))(arr.get(x, y))
            override def addPixel(zpx: CPZPixel, x: Int, y: Int): Unit = arr.set(x, y, zpx)


