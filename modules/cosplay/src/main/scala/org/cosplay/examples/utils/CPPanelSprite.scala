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

package org.cosplay.examples.utils

import org.cosplay.*
import org.cosplay.CPColor.*
import org.cosplay.CPStyledString.*

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

import org.cosplay.CPPixel.*

/**
  * Reusable titled panel for the built-in examples. It creates the following bordered panel:
  * <pre>
  *  +----< Title >-----+
  *  |                  |
  *  |                  |
  *  |                  |
  *  +------------------+
  * </pre>
  *
  * @param x1 Left-top X-coordinate.
  * @param y1 Left-top Y-coordinate.
  * @param x2 Right-bottom X-coordinate.
  * @param y2 Right-bottom Y-coordinate.
  * @param z Z-index of the panel.
  * @param title Title of the panel.
  * @note See developer guide at [[https://cosplayengine.com]]
  */
class CPPanelSprite(x1: Int, y1: Int, x2: Int, y2: Int, z: Int, title: String) extends CPSceneObject:
    !>(x2 > x1, "'x2' must be > 'x1'.")
    !>(y2 > y1, "'y2' must be > 'y1'.")

    private val dim = CPDim(x2 - x1, y2 - y1)
    private val c1 = C_GREEN_YELLOW
    private val c2 = C_LIGHT_STEEL_BLUE
    private val c3 = C_DARK_ORANGE3

    override def getX: Int = x1
    override def getY: Int = y1
    override def getZ: Int = z
    override def getDim: CPDim = dim

    override def render(ctx: CPSceneObjectContext): Unit =
        val canv = ctx.getCanvas
        canv.fillRect(x1, y1, x2, y2, z, ' '&C_BLACK)
        canv.drawRectBorder(
            getRect,
            z,
            "-.|'-'|.",
            c1,
            styleStr("< ", c1) ++ styleStr(title, c3) ++ styleStr(" >", c1),
            x1 + (x2 - x1 - title.length - 4) / 2,
            y1,
            (_, _, px) => px.withDarkerFg(.5f)
        )

