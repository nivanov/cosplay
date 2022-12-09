package org.cosplay

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

import scala.collection.mutable

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
object CPDimTests:
    /**
      *
      */
    @Test
    def dimTest(): Unit =
        val dim1 = CPDim(5, 5)
        val dim2: CPDim = dim1 + 5
        assertTrue(dim2.w == 10 && dim2.h == 10)

        val dim3 = CPDim(5, 5)
        val dim4: CPDim = dim3 + dim1
        assertTrue(dim4.w == 10 && dim4.h == 10)
        assertTrue(dim4 > dim1 && dim4 > dim3)

        val dim5 = CPDim.ZERO
        val dim6 = CPDim(1, 1) * dim5
        assertTrue(dim6.isZero)

        assertTrue(dim6 == dim6.copy())

        assertTrue(CPDim(1, 2) == CPDim(1, 2))
        assertTrue(CPDim(1, 2) != CPDim(2, 1))

        assertTrue(CPDim(1, 2) <=@ CPDim(1, 3))
        assertTrue(CPDim(1, 2) <=@ CPDim(2, 2))

        assertTrue(CPDim(4, 2) >@ CPDim(1, 1))
        assertTrue(CPDim(5, 3) >@ CPDim(4, 2))

        println(s"dim3 $dim3")
