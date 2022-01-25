package org.cosplay

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import CPColor.*
import CPRand.*
import impl.*

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
object CPImageTests:
    /**
      *
      */
    @Test
    def saveLoadTest(): Unit =
        val dim = CPDim(50, 50)
        val canv = CPCanvas(dim, CPPixel('.', C_GRAY3, C_GRAY1))
        canv.fillRect(1, 1, 49, 49, 0, (x, y) => CPPixel(randSymbol(), randXtermColor(), randXtermColor()))
        val img1 = canv.capture(0, 0, dim)
        val file = CPUtils.homePath("test_img.csv")
        img1.save(file, C_BLACK)
        val img2 = CPImage.load(file)
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
        assertTrue(szDim.width == img.getDim.width + 10)
        assertTrue(szDim.height == img.getDim.height)

        val szImg2 = img.resizeByInsets(CPInsets(1, 2, 1, 5))
        val szDim2 = szImg2.getDim
        assertTrue(szDim2.width == img.getDim.width + 2 + 5)
        assertTrue(szDim2.height == img.getDim.height + 1 + 1)

        val szImg3 = img.resizeByInsets(CPInsets(-1, 2, 1, -5))
        val szDim3 = szImg3.getDim
        assertTrue(szDim3.width == img.getDim.width + 2 - 5)
        assertTrue(szDim3.height == img.getDim.height - 1 + 1)
