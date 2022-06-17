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

package org.cosplay.games.mir.scenes.sprites

import org.cosplay.*
import games.mir.*
import os.*

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
  *
  */
class CPMirConsoleSprite extends CPCanvasSprite(id = "console") with CPMirConsole:
    case class ZChar(ch: Char, z: Int)

    private final val W = 200
    private final val H = 100
    private val pane = Array.ofDim[ZChar](W, H)
    private var curX = 0
    private var curY = 0
    private var curVis = true
    private var dim = CPDim(W, H)
    private final val mux = Object()

    clear()

    override def clear(): Unit =
        var x = 0
        while x < W do
            var y = 0
            while y < H do
                pane(x)(y) = null
                y += 1
            x += 1

    override def clearLeft(): Unit = ???
    override def clearRight(): Unit = ???
    override def clearRow(): Unit = ???
    override def clearColumn(): Unit = ???
    inline override def isCursorVisible: Boolean = curVis
    inline override def setCursorVisible(f: Boolean): Unit = curVis = f
    override def moveCursor(x: Int, y: Int): Unit = ???
    override def getSize: CPDim = dim
    inline override def getCursorX: Int = curX
    inline override def getCursorY: Int = curY
    override def print(x: Any): Unit = ???
    override def putChar(x: Int, y: Int, z: Int, ch: Char, fg: org.cosplay.CPColor, bg: org.cosplay.CPColor): Unit = ???

    override def render(ctx: CPSceneObjectContext): Unit =
        val canv = ctx.getCanvas

        dim = canv.dim
