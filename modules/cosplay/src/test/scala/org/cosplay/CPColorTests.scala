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

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.cosplay.CPColor.*

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
object CPColorTests:
    /**
      *
      */
    @Test
    def darkerTest(): Unit =
        val c = C_LIME
        val c2 = c.darker(0.5f)
        val c3 = CPColor(0, (255 * 0.5f).round, 0)
        assertTrue(c2 == c3)

    @Test
    def HSBandRGBTest(): Unit =
        val c = CPColor(10, 20, 30)
        assertEquals(c, CPColor.fromHSB(c.hue, c.saturation, c.brightness))

    /**
     *
     */
    @Test
    def modificationsTest(): Unit =
        val c = CPColor(32, 64, 128)

        val c1 = c.bw()
        val c2 = c.bw2()
        val c3 = c.awt
        val c4 = c.bgAnsi
        val c5 = c.fgAnsi
        val c6 = c.darker(0.5)
        val c7 = c.lighter(0.5)
        val c8 = c.transformRGB(0.2, 0.8, 0.5)
        val c9 = c.color8Bit
        val f1 = CPColor.gradientFun(C_WHITE, C_BLACK, 20)
        val s1 = CPColor.gradientSeq(C_WHITE, C_BLACK, 20)

    /**
      *
      */
    //@Test
    def genX11ColorSwatches(): Unit = genColorSwatches(CPColor.CS_X11_ALL)

    /**
      *
      */
    //@Test
    def genXTermColorSwatches(): Unit = genColorSwatches(CPColor.CS_XTERM_ALL)

    /**
      *
      */
    private def genColorSwatches(cs: Seq[CPColor]): Unit =
        // ^(\s+)val ([\d\w_]+) = CPColor\((\d{1,3}, \d{1,3}, \d{1,3})\)$
        // $1val $2 = new CPColor($3, "$2")
        /*
            <tr>
                <td>
                    <div class="color-swatch" style="background: rgb(230, 230, 250)"></div>
                </td>
                <td>
                    <code>C_X11_LAVENDER</code>
                </td>
                <td>
                    <b>r</b>:&nbsp;230&nbsp;&nbsp;<b>g</b>:&nbsp;230&nbsp;&nbsp;<b>b</b>:&nbsp;250&nbsp;&nbsp;<code>#11f48e</code>
                </td>
            </tr>
        */
        for c <- cs do
            println(
                s"""
                  |<tr>
                  |    <td>
                  |        <div class="color-swatch" style="background: rgb(${c.red}, ${c.green}, ${c.blue})"></div>
                  |    </td>
                  |    <td>
                  |        <code>${c.getName}</code>
                  |    </td>
                  |    <td>
                  |        <b>r</b>:&nbsp;${c.red}&nbsp;&nbsp;<b>g</b>:&nbsp;${c.green}&nbsp;&nbsp;<b>b</b>:&nbsp;${c.blue}&nbsp;&nbsp;<code>${c.cssHex}</code>
                  |    </td>
                  |</tr>""".stripMargin)

    /**
      *
      */
    @Test
    def xterm256Test(): Unit =
        assertTrue(16 == CPColor(0,0,0).xterm)
        assertTrue(17 == CPColor(0,0,95).xterm)
        assertTrue(18 == CPColor(0,0,135).xterm)
        assertTrue(19 == CPColor(0,0,175).xterm)
        assertTrue(20 == CPColor(0,0,215).xterm)
        assertTrue(21 == CPColor(0,0,255).xterm)
        assertTrue(22 == CPColor(0,95,0).xterm)
        assertTrue(23 == CPColor(0,95,95).xterm)
        assertTrue(24 == CPColor(0,95,135).xterm)
        assertTrue(25 == CPColor(0,95,175).xterm)
        assertTrue(26 == CPColor(0,95,215).xterm)
        assertTrue(27 == CPColor(0,95,255).xterm)
        assertTrue(28 == CPColor(0,135,0).xterm)
        assertTrue(29 == CPColor(0,135,95).xterm)
        assertTrue(30 == CPColor(0,135,135).xterm)
        assertTrue(31 == CPColor(0,135,175).xterm)
        assertTrue(32 == CPColor(0,135,215).xterm)
        assertTrue(33 == CPColor(0,135,255).xterm)
        assertTrue(34 == CPColor(0,175,0).xterm)
        assertTrue(35 == CPColor(0,175,95).xterm)
        assertTrue(36 == CPColor(0,175,135).xterm)
        assertTrue(37 == CPColor(0,175,175).xterm)
        assertTrue(38 == CPColor(0,175,215).xterm)
        assertTrue(39 == CPColor(0,175,255).xterm)
        assertTrue(40 == CPColor(0,215,0).xterm)
        assertTrue(41 == CPColor(0,215,95).xterm)
        assertTrue(42 == CPColor(0,215,135).xterm)
        assertTrue(43 == CPColor(0,215,175).xterm)
        assertTrue(44 == CPColor(0,215,215).xterm)
        assertTrue(45 == CPColor(0,215,255).xterm)
        assertTrue(46 == CPColor(0,255,0).xterm)
        assertTrue(47 == CPColor(0,255,95).xterm)
        assertTrue(48 == CPColor(0,255,135).xterm)
        assertTrue(49 == CPColor(0,255,175).xterm)
        assertTrue(50 == CPColor(0,255,215).xterm)
        assertTrue(51 == CPColor(0,255,255).xterm)
        assertTrue(52 == CPColor(95,0,0).xterm)
        assertTrue(53 == CPColor(95,0,95).xterm)
        assertTrue(54 == CPColor(95,0,135).xterm)
        assertTrue(55 == CPColor(95,0,175).xterm)
        assertTrue(56 == CPColor(95,0,215).xterm)

    /**
      *
      */
    @Test
    def equalTest(): Unit =
        val c1 = CPColor(0, 128, 0)
        val c2 = CPColor(128, 128, 0)
        val c3 = CPColor(0, 128, 0)
        assertTrue(c1 != c2)
        assertTrue(c1 == c3)

        val c1a = CPColor("0x008000")
        val c2a = CPColor("#808000")
        val c1b = CPColor("#008000")

        assert(c1 == c1a)
        assert(c1 == c1b)
        assert(c2 == c2a)
        assert(c1b == c1a)