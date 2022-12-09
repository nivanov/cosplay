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

package org.cosplay.impl

import org.cosplay.CPColor

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
  *
  */
object CPAnsi:
    final val ESC = "\u001b"
    final val BELL = "\u0007"
    final val CSI = s"$ESC["
    final val OSC = s"$ESC]"

    final val BOLD = s"${CSI}1m"
    final val REVERSED = s"${CSI}7m"
    final val INVISIBLE = s"${CSI}8m"
    final val UNDERLINE_ON = s"${CSI}4m"
    final val UNDERLINE_OFF = s"${CSI}24m"

    final val RESET_ALL = s"${CSI}0m"
    final val RESET_BG = s"${CSI}49m"

    final val CLR_SCR = s"${CSI}2J"
    final val CLR_SCREEN_AFTER = s"${CSI}0J"
    final val CLR_SCREEN_BEFORE = s"${CSI}1J"
    final val CLR_LINE = s"${CSI}K"
    final val CLR_LINE_AFTER = s"${CSI}0K"
    final val CLR_LINE_BEFORE = s"${CSI}1K"

    final val CUR_HIDE = s"$CSI?25l"
    final val CUR_SHOW = s"$CSI?25h"
    final val CUR_UP = s"${CSI}1A"
    final val CUR_DOWN = s"${CSI}1B"
    final val CUR_LEFT = s"${CSI}1D"
    final val CUR_RIGHT = s"${CSI}1C"
    final val CUR_LINE_HOME = s"${CSI}0G"
    final val CUR_SCREEN_HOME = s"${CSI}H"
    final val CUR_SAVE = s"${CSI}s"
    final val CUR_REST = s"${CSI}u"

    final val USE_ALT_SCR_BUF = s"$CSI?1049h"
    final val USE_PRI_SCR_BUF = s"$CSI?1049l"

    final val WIN_TITLE_SAVE = s"${CSI}22t"
    final val WIN_TITLE_REST = s"${CSI}23t"

    // Window-only specifics.
    final val WIN_TERM_RESET = s"$ESC!p"

    /**
      *
      * @param s
      * @param fg
      */
    inline def str(s: String, fg: CPColor): String = s"${fg.fgAnsi}$s$RESET_ALL"

    /**
      *
      * @param s
      * @param fg
      * @param bg
      */
    inline def str(s: String, fg: CPColor, bg: CPColor): String = s"${fg.fgAnsi}${bg.bgAnsi}$s$RESET_ALL"

    /** ANSI sequence for 24-bit foreground color. */
    inline def fg24Bit(r: Int, g: Int, b: Int): String = s"${CSI}38;2;$r;$g;${b}m"

    /** ANSI sequence for 24-bit background color. */
    inline def bg24Bit(r: Int, g: Int, b: Int): String = s"${CSI}48;2;$r;$g;${b}m"

    /** ANSI sequence for 8-bit xterm lookup foreground color. */
    inline def fg8Bit(n: Int): String = s"${CSI}38;5;${n}m"

    /** ANSI sequence for 8-bit xterm lookup background color. */
    inline def bg8Bit(n: Int): String = s"${CSI}48;5;${n}m"

    /**
      *
      * @param title
      */
    inline def winTitle(title: String): String = s"${OSC}0;$title$BELL"

    /**
      * Cursor ANSI position is 1-based.
      *
      * @param x 0-based x coordinate.
      * @param y 0-based y coordinate.
      */
    inline def curPos(x: Int, y: Int): String =
        val y1 = y + 1
        val x1 = x + 1
        s"$CSI$y1;${x1}H"
