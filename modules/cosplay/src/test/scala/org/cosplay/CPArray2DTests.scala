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

import org.junit.jupiter.api.Assertions.*
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
               All rights reserved.
*/

/**
  *
  */
object CPArray2DTests:
    /**
      *
      */
    @Test
    def equalityTest(): Unit =
        assertTrue(new CPArray2D[Char](2, 2, 'a') == CPArray2D(Seq("aa", "aa")))
        assertTrue(new CPArray2D[Char](2, 2, 'a') != CPArray2D(Seq("aa", "1aa")))

    /**
      *
      */
    @Test
    def givensTest(): Unit =
        new CPArray2D[Char](100, 100, 'a')
        new CPArray2D[CPPixel](100, 100, CPPixel.XRAY)
        new CPArray2D[CPZPixel](100, 100, CPZPixel(CPPixel.XRAY, Int.MinValue))

    /**
      *
      */
    @Test
    def copyTest(): Unit =
        val arr1 = new CPArray2D[Char](100, 100, 'a')
        val arr2 = arr1.copy()
        assertTrue(arr1 == arr2)
        assertTrue(arr2.get(x = 0, y = 0) == 'a')

    /**
      *
      */
    @Test
    def applyTest(): Unit =
        val seq = Seq("cosplay", "game", "engine")
        val arr = CPArray2D(seq)

        /*
        |cosplay
        |game
        |engine
        */

        assertTrue(arr.width == 7)
        assertTrue(arr.height == 3)
        assertTrue(arr.get(0, 0) == 'c')
        assertTrue(arr.get(0, 1) == 'g')
        assertTrue(arr.get(0, 2) == 'e')

    /**
      *
      */
    @Test
    def trimTest(): Unit =
        val arr1 = new CPArray2D[Int](10, 10, 0)

        arr1.set(5, 5, 1)
        arr1.set(5, 6, 1)
        arr1.set(6, 5, 1)
        arr1.set(6, 6, 1)

        val arr2 = arr1.trim(0)

        assertTrue(arr2.dim == CPDim(2, 2))
