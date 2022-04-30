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

import org.cosplay.CPColor.*
import org.cosplay.CPPixel.*
import org.cosplay.impl.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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
object CPScreenTests:
    private val rndChars = "1234567890~!@#$%^&*()-="

    /**
      *
      */
    @Test
    def screenCopy(): Unit =
        val bg = CPPixel('.', C_PINK1, C_LIGHT_PINK1)
        val scr1 = CPScreen(CPDim(500, 100), bg)
        val scr2 = CPScreen(CPDim(500, 100), bg)

        scr1.canvas().fillRect(scr1.getRect, -1, (_, _) => CPRand.rand(rndChars)&C_BLACK)
        scr1.copyTo(scr2, scr1.getRect)

        assertEquals(scr1, scr2)

    /**
      *
      */
    @Test
    def screenCopyPerformance(): Unit =
        val dim = CPDim(1000, 1000)
        val bgPx = CPPixel('.', C_PINK1, C_LIGHT_PINK1)
        val scr1 = CPScreen(dim, bgPx)
        val scr2 = CPScreen(dim, bgPx)

        scr1.canvas().fillRect(scr1.getRect, -1, (_, _) => CPRand.rand(rndChars)&C_BLACK)

        val num = 10
        val start = System.currentTimeMillis()
        for (_ <- 0 until num)
            scr1.copyTo(scr2, scr1.getRect)

        val dur = System.currentTimeMillis() - start

        println(s"Performance of one $dim screen copy is  ${dur / num} ms.")

    /**
      *
      */
    @Test
    def screenClear(): Unit =
        val dim = CPDim(100, 100)
        val bgPx = CPPixel('.', C_PINK1, C_LIGHT_PINK1)
        val scr = CPScreen(dim, bgPx)

        for (x <- 0 until scr.getDim.w; y <- 0 until scr.getDim.h)
            assert(scr.getPixel(x, y).px == bgPx)

    /**
      *
      */
    @Test
    def screenClearPerformance(): Unit =
        val dim = CPDim(1000, 1000)
        val bg = CPPixel('.', C_PINK1, C_LIGHT_PINK1)
        val scr = CPScreen(dim, bg)

        val num = 10
        val start = System.currentTimeMillis()
        for (_ <- 0 until num)
            scr.clear()

        val dur = System.currentTimeMillis() - start

        println(s"Performance of one $dim screen clears is ${dur / num}ms.")
