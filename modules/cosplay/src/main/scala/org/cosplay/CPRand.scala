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

import scala.util.Random
import CPColor.*
import org.apache.commons.lang3.RandomStringUtils
import java.util.UUID

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
  * Provides convenient functions for random number generation and usage.
  *
  * @see [[Random]]
  */
object CPRand:
    private final val RND = new Random()
    private final val LO_LETTERS = "qwertyuiopasdfghjklzxcvbnm"
    private final val UP_LETTERS = "QWERTYUIOPASDFGHJKLZXCVBNM"
    private final val LETTERS = s"$LO_LETTERS$UP_LETTERS"
    private final val DIGITS = "1234567890"
    private final val SYMBOLS = "~!@#$%^&*()_+-=[]{}';:\",.<>/?"

    /**
      * Creates new globally unique 16-bytes type 4 UUID.
      */
    def guid: String = UUID.randomUUID().toString.toLowerCase

    /**
      * Creates new 6-bytes UUID. This UUID is NOT globally unique.
      */
    def guid6: String = RandomStringUtils.random(6, true, true).toLowerCase

    /**
      * Gets random value from the given sequence. It's equivalent to:
      * {{{
      *     seq(RND.nextInt(seq.size))
      * }}}
      *
      * @param seq Sequence to get the random value from.
      */
    def rand[T](seq: Seq[T]): T = seq(RND.nextInt(seq.size))

    /**
      * Gets random integer in the given range.
      *
      * @param from Inclusive range from value.
      * @param to Inclusive range to value.
      */
    def randInt(from: Int, to: Int): Int =
        assert(from < to)
        RND.nextInt(to - from) + from

    /**
      * Gets random integer in the given range.
      *
      * @param from Inclusive range from value.
      * @param to Inclusive range to value.
      */
    def randIntExcl(from: Int, to: Int): Int =
        assert(from < to)
        RND.nextInt(to - from) + from

    /**
      * Gets random long in the given range.
      *
      * @param from Inclusive range from value.
      * @param to Inclusive range to value.
      */
    def randLong(from: Long, to: Long): Long =
        assert(from < to)
        RND.nextLong(to - from) + from

    /**
      * Gets random long in the given range.
      *
      * @param from Inclusive range from value.
      * @param to Inclusive range to value.
      */
    def randLongExcl(from: Long, to: Long): Long =
        assert(from < to)
        RND.nextLong(to - from) + from

    /**
      * Gets random integer in the given range.
      *
      * @param from Inclusive range from value.
      * @param to Inclusive range to value.
      */
    def randIntIncl(from: Int, to: Int): Int =
        assert(from < to)
        RND.nextInt(to - from + 1) + from

    /**
      * Gets random xterm color from the list of [[CPColor.CS_XTERM_ALL]].
      */
    def randXtermColor(): CPColor = rand(CPColor.CS_XTERM_ALL)

    /**
      * Gets random xterm color from the list of [[CPColor.CS_X11_ALL]].
      */
    def randX11Color(): CPColor = rand(CPColor.CS_X11_ALL)

    /**
      * Gets a random system color from the list of [[CPColor.C_SYS_GROUP]].
      */
    def randSysColor(): CPColor = rand(CPColor.C_SYS_GROUP)

    /**
      * Gets random integer.
      */
    def randInt(): Int = RND.nextInt()

    /**
      * Gets the next pseudorandom, uniformly distributed `float` value between `0.0` and `1.0`.
      */
    def randFloat(): Float = RND.nextFloat()

    /**
      * Gets the next pseudorandom, uniformly distributed `double` value between `0.0` and `1.0`.
      */
    def randDouble(): Double = RND.nextDouble()

    /**
      * Random boolean value.
      */
    def coinFlip(): Boolean = RND.nextFloat() < .5f

    /**
      * Gets a random uppercase or lowercase letter.
      */
    def randLetter(): Char = rand(LETTERS)

    /**
      * Gets a random lowercase letter.
      */
    def randLoLetter(): Char = rand(LO_LETTERS)

    /**
      * Gets a random uppercase letter.
      */
    def randUpLetter(): Char = rand(UP_LETTERS)

    /**
      * Gets a random symbol from the list of `~!@#$%^&*()_+-=[]{}';:\",.<>/?`.
      */
    def randSymbol(): Char = rand(SYMBOLS)

    /**
      * Geta a random digit.
      */
    def randDigit(): Char = rand(DIGITS)

    /**
      * Gets random number in given range.
      *
      * @param a Inclusive start of the range.
      * @param b Exclusive end of the range.
      */
    def between(a: Int, b: Int): Int = if a < b then RND.between(a, b) else RND.between(b, a)

    /**
      * Gets random number in given range.
      *
      * @param a Inclusive start of the range.
      * @param b Exclusive end of the range.
      */
    def between(a: Double, b: Double): Double = if a < b then RND.between(a, b) else RND.between(b, a)

    /**
      * Gets random number in given range.
      *
      * @param a Inclusive start of the range.
      * @param b Exclusive end of the range.
      */
    def between(a: Long, b: Long): Long = if a < b then RND.between(a, b) else RND.between(b, a)

    /**
      * Gets random number in given range.
      *
      * @param a Inclusive start of the range.
      * @param b Exclusive end of the range.
      */
    def between(a: Float, b: Float): Float = if a < b then RND.between(a, b) else RND.between(b, a)

    /**
      * Gets a random red color from the list of [[CPColor.CS_X11_REDS]].
      */
    def randX11Red(): CPColor = rand(CS_X11_REDS)

    /**
      * Gets a random gray color from the list of [[CPColor.CS_X11_GRAYS]].
      */
    def randX11Gray(): CPColor = rand(CS_X11_GRAYS)

    /**
      * Gets a random pink color from the list of [[CPColor.CS_X11_PINKS]].
      */
    def randX11Pink(): CPColor = rand(CS_X11_PINKS)

    /**
      * Gets a random cyan color from the list of [[CPColor.CS_X11_CYANS]].
      */
    def randX11Cyan(): CPColor = rand(CS_X11_CYANS)

    /**
      * Gets a random blue color from the list of [[CPColor.CS_X11_BLUES]].
      */
    def randX11Blue(): CPColor = rand(CS_X11_BLUES)

    /**
      * Gets a random brown color from the list of [[CPColor.CS_X11_BROWNS]].
      */
    def randX11Brown(): CPColor = rand(CS_X11_BROWNS)

    /**
      * Gets a random green color from the list of [[CPColor.CS_X11_GREENS]].
      */
    def ranX11Green(): CPColor = rand(CS_X11_GREENS)

    /**
      * Gets a random orange color from the list of [[CPColor.CS_X11_ORANGES]].
      */
    def randX11Orange(): CPColor = rand(CS_X11_ORANGES)

    /**
      * Gets a random purple color from the list of [[CPColor.CS_X11_PURPLES]].
      */
    def randX11Purple(): CPColor = rand(CS_X11_PURPLES)

    /**
      * Gets a random white color from the list of [[CPColor.CS_X11_WHITES]].
      */
    def randX11White(): CPColor = rand(CS_X11_WHITES)

    /**
      * Gets a random yellow color from the list of [[CPColor.CS_X11_YELLOWS]].
      */
    def randX11Yellow(): CPColor = rand(CS_X11_YELLOWS)

