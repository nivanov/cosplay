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
               ALl rights reserved.
*/

/**
  *
  */
object CPRectTests:
    /**
      *
      */
    @Test
    def zeroRectTest(): Unit =
        val r = CPRect.ZERO

        assertTrue(r == r.copy())
        assertTrue(!r.contains(1, 1))
        assertTrue(!r.isAboveOf(CPRect(1, 1, 5, 5)))
        assertTrue(r.union(CPRect(1, 1, 5, 5)) == CPRect(1, 1, 5, 5))
        assertTrue(r.union(CPRect.ZERO) == r)
        assertTrue(CPRect(1, 1, 5, 5).intersectWith(r) == CPRect.ZERO)

    /**
      *
      */
    @Test
    def equalityTest(): Unit =
        assertTrue(CPRect(0, 0, 1, 1) == CPRect(0, 0, 1, 1))

    /**
      *
      */
    @Test
    def unionTest(): Unit =
        val rect1 = CPRect(0, 0, 1, 1)
        val rect2 = CPRect(3, 3, 2, 2)
        assertTrue(rect1.union(rect2) == CPRect(0, 0, 5, 5))

    /**
      *
      */
    @Test
    def intersectionTest(): Unit =
        val rect1 = CPRect(0, 0, 10, 10)
        val rect2 = CPRect(3, 3, 2, 2)
        assertTrue(rect1.intersectWith(rect2) == CPRect(3, 3, 2, 2))

        val rect3 = CPRect(0, 0, 10, 10)
        val rect4 = CPRect(5, 5, 7, 7)
        assertTrue(rect3.intersectWith(rect4) == CPRect(5, 5, 5, 5))

        // Not intersecting.
        assertTrue(CPRect(1, 1, 1, 1).intersectWith(CPRect(3, 3, 1, 1)) == CPRect.ZERO)

    /**
      *
      */
    @Test
    def growToTest(): Unit =
        val rect1 = CPRect(0, 0, 1, 1)
        val rect2 = rect1.growTo(2, 2)
        assertTrue(rect2.width == 3 && rect2.height == 3)

        val rect3 = CPRect(2, 2, 2, 2)
        val rect4 = rect3.growTo(0, 0)
        assertTrue(rect4.width == 4 && rect4.height == 4)

        println(s"rect4 $rect4")

    /**
      *
      */
    @Test
    def containsTest(): Unit =
        val rect = new CPRect(0, 0, (10, 10))

        assertTrue(rect.contains(1, 1))
        assertTrue(rect.contains(9, 9))
        assertTrue(rect.contains(0, 0))

        assertTrue(rect.contains(new CPRect(2, 2, 3, 3)))
        assertTrue(rect.contains(rect))

        assertFalse(rect.contains(-1, 1))
        assertFalse(rect.contains(10, 10))
        assertFalse(rect.contains(11, 11))
        assertFalse(rect.contains(11, 0))
        assertFalse(rect.contains(0, 11))

    /**
      * c
      */
    @Test
    def overlapTest(): Unit =
        val rect = new CPRect(0, 0, (10, 10))

        assertTrue(rect.overlaps(new CPRect(1, 1, 5, 5)))
        assertTrue(rect.overlaps(new CPRect(1, 1, 50, 5)))
        assertTrue(new CPRect(5, 5, 1, 1).overlaps(new CPRect(1, 1, 50, 50)))
        assertTrue(rect.overlaps(CPRect(-1, -2, 8, 15)))
        assertFalse(rect.overlaps(CPRect.ZERO))
