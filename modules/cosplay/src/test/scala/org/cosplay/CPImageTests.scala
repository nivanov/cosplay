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
import org.cosplay.CPColor.*
import org.cosplay.CPRand.*
import org.cosplay.impl.*

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
object CPImageTests:
    /**
      *
      */
    @Test
    def saveLoadTest(): Unit =
        CPTestEngine.ensureStarted()

        val dim = CPDim(50, 50)
        val canv = CPCanvas(dim, CPPixel('.', C_GRAY3, C_GRAY1))
        canv.fillRect(1, 1, 49, 49, 0, (_, _) => CPPixel(randSymbol(), randXtermColor(), randXtermColor()))
        val img1 = canv.capture(0, 0, dim)
        val file = CPEngine.homeFile("test_img.csv")
        assertTrue(file.isSuccess)
        img1.saveRexCsv(file.get, C_BLACK)
        val img2 = CPImage.load(file.get.getAbsolutePath)
        assertTrue(img1 != img2)
        assertTrue(img1.isSameAs(img2))
        val arr1 = img1.toArray2D
        val arr2 = img2.toArray2D
        assertTrue(arr1 == arr2)

    @Test
    def sameAsTest(): Unit =
        val img1 = CPSystemFont.render("test image", C_WHITE)
        val img2 = CPSystemFont.render("test image", C_WHITE)
        assertTrue(img1 != img2) // Different IDs...
        assertTrue(img1.isSameAs(img2))

    @Test
    def resizeTest(): Unit =
        val img = CPSystemFont.render("test image", C_WHITE)
        val szImg = img.resizeByDim(CPDim(img.getWidth + 10, img.getHeight))
        val szDim = szImg.getDim
        assertTrue(szDim.w == img.getDim.w + 10)
        assertTrue(szDim.h == img.getDim.h)

        val szImg2 = img.resizeByInsets(CPInsets(1, 2, 1, 5))
        val szDim2 = szImg2.getDim
        assertTrue(szDim2.w == img.getDim.w + 2 + 5)
        assertTrue(szDim2.h == img.getDim.h + 1 + 1)

        val szImg3 = img.resizeByInsets(CPInsets(-1, 2, 1, -5))
        val szDim3 = szImg3.getDim
        assertTrue(szDim3.w == img.getDim.w + 2 - 5)
        assertTrue(szDim3.h == img.getDim.h - 1 + 1)
