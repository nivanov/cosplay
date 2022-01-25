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
               ALl rights reserved.
*/

/**
  *
  */
object CPInt4Tests:
    /**
      *
      */
    @Test
    def int4Test(): Unit =
        val d = CPInt4(1, 1, 1, 1)
        val d1 = d + 2
        assertTrue(d1.i1 == 3 && d1.i2 == 3 && d1.i3 == 3 && d1.i4 == 3)

        val d3: CPInt4 = CPInt4(1, 1, 1, 1)
        assertTrue(d3 == d)

        val d4 = d1 + d3
        assertTrue(d4.i1 == 4 && d4.i2 == 4)

        assertTrue(d4.isWhole)
        assertTrue(d4.isPositive)
        assertFalse(CPInt4.NEG_ONE.isPositive)

        assertTrue(d4 == d4.copy)
