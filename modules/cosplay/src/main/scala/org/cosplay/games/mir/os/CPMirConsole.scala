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

package org.cosplay.games.mir.os

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

import org.cosplay.*
import games.mir.*

/**
  *
  */
trait CPMirConsole:
    private var fg = FG
    private var bg = BG
    private final val DFLT_Z = 0

    /**
      *
      */
    def getSize: CPDim

    /**
      *
      */
    def println(): Unit = print("\n")

    /**
      *
      * @param x
      */
    def println(x: Any): Unit = print(s"$x\n")

    /**
      *
      * @param x
      */
    def print(x: Any): Unit

    /**
      *
      * @param text
      * @param args
      */
    def printf(text: String, args: Any*): Unit = print(text.format(args))

    /**
      *
      */
    def clearLeft(): Unit

    /**
      *
      */
    def clearRight(): Unit

    /**
      *
      */
    def clearRow(): Unit

    /**
      *
      */
    def clearColumn(): Unit

    /**
      *
      */
    def clear(): Unit

    /**
      *
      * @param f
      */
    def setCursorVisible(f: Boolean): Unit

    /**
      *
      */
    def isCursorVisible: Boolean

    /**
      *
      * @param x
      * @param y
      */
    def moveCursor(x: Int, y: Int): Unit

    /**
      *
      */
    def getCursorX: Int

    /**
      * 
      */
    def getCursorY: Int

    /**
      *
      * @param x
      * @param y
      * @param ch
      */
    def putChar(x: Int, y: Int, ch: Char): Unit = putChar(x, y, DFLT_Z, ch, getFg, getBg)

    /**
      *
      * @param x
      * @param y
      * @param ch
      * @param fg
      * @param bg
      */
    def putChar(x: Int, y: Int, z: Int, ch: Char, fg: CPColor, bg: CPColor): Unit

    /**
      *
      * @param c
      */
    inline def setBg(c: CPColor): Unit = bg = c

    /**
      *
      */
    inline def getBg: CPColor = bg

    /**
      *
      * @param c
      */
    inline def setFg(c: CPColor): Unit = fg = c

    /**
      *
      */
    inline def getFg: CPColor = fg
