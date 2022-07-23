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

object CPMirConsole:
    /**  */
    val CTRL_REV_COL = '\u0001'
    /**  */
    val CTRL_RST_COL = '\u0002'
    /**  */
    val CTRL_BEEP = '\u0007'

    /**
      *
      * @param ch
      */
    def isControl(ch: Char): Boolean = ch match
        case CTRL_REV_COL | CTRL_RST_COL | CTRL_BEEP => true
        case _ => false

/**
  *
  */
trait CPMirConsole extends CPMirPrintable:
    private var fg = FG
    private var bg = BG

    protected val initFg: CPColor = FG
    protected val initBg: CPColor = BG

    /**
      * Reads the line form the console at the current cursor position. This call will be blocked
      * until 'Enter' key is pressed.
      *
      * @param repCh Optional character to replace entered characters (e.g. when entering password).
      * @param maxLen Optional max length of the input.
      * @param hist Optional history to use for history scrolling and auto-completion.
      */
    def readLine(repCh: Option[Char] = None, maxLen: Int = Int.MaxValue, hist: Seq[String] = Seq.empty): String

    /**
      * Gets the current size of the console window in characters.
      */
    def getSize: CPDim
    
    /**
      *
      */
    def clearBelow(): Unit

    /**
      *
      */
    def clearAbove(): Unit

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
      * Puts char at given coordinate without moving the cursor. Uses default Z-index and
      * currently set foreground and background colors.
      *
      * @param x
      * @param y
      * @param ch
      */
    def putChar(x: Int, y: Int, ch: Char): Unit = putChar(x, y, 0, ch, getFg, getBg)

    /**
      * Puts char at given coordinate without moving the cursor.
      *
      * @param x
      * @param y
      * @param z
      * @param ch
      * @param fg
      * @param bg
      */
    def putChar(x: Int, y: Int, z: Int, ch: Char, fg: CPColor, bg: CPColor): Unit

    /**
      *
      */
    def inverseColors(): Unit =
        setFg(initBg)
        setBg(initFg)

    /**
      *
      */
    def resetColors(): Unit =
        setFg(initFg)
        setBg(initBg)

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
