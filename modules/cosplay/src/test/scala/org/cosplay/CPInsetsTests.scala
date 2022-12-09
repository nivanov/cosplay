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
object CPInsetsTests:
    /**
      *
      */
    @Test
    def insetTest(): Unit =
        val ins = CPInsets(1, 1, 1, 1)
        val ins1 = ins + 2
        assertTrue(ins1.i1 == 3 && ins1.i2 == 3 && ins1.i3 == 3 && ins1.i4 == 3)

        val ins3 = CPInsets(1, 1, 1, 1)
        assertTrue(ins3 == ins)

        val ins4 = ins1 + ins3
        assertTrue(ins4.i1 == 4 && ins4.i2 == 4)

        assertTrue(ins4.isWhole)
        assertTrue(ins4.isPositive)
        assertFalse(new CPInsets(-1).isPositive)

        assertTrue(ins4 == ins4.copy())

        assertTrue(new CPInsets(2, 4) == CPInsets(4, 2, 4, 2))
        assertTrue(new CPInsets(1) == new CPInsets(1, 1))
        assertTrue(new CPInsets(2, 4) != CPInsets(2, 2, 2, 4))

        println(s"ins4 $ins4")
