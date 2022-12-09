package org.cosplay

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

import scala.language.implicitConversions

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
object CPInt2Tests:
    /**
      *
      */
    @Test
    def int2Test(): Unit =
        val d = CPInt2(1, 1)
        val d1 = d + 2
        assertTrue(d1.i1 == 3 && d1.i2 == 3)

        val d11 = d * 2
        assertTrue(d11.i1 == 2 && d11.i2 == 2)

        val d2: CPInt2 = 1 -> 1
        val d3: CPInt2 = (1, 1)
        assertTrue(d2 == d3)

        val d4 = d2 + d3
        assertTrue(d4.i1 == 2 && d4.i2 == 2)

        assertTrue(d4.isWhole)
        assertTrue(d4.isPositive)
        assertFalse(CPInt2.NEG_ONE.isPositive)

        assertTrue(d4 == d4.copy)

        def i1 = CPInt2(1, 2)
        def i2 = CPInt2(2, 1)
        def i3 = CPInt2(1, 2)
        assertTrue(i1 != i2)
        assertTrue(i1 == i3)
