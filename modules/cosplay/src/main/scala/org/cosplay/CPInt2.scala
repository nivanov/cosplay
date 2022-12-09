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
  * General 2-int tuple.
  *
  * @param i1 1st integer.
  * @param i2 2nd integer.
  */
final class CPInt2(val i1: Int, val i2: Int) extends CPIntTuple[CPInt2](i1, i2) with Ordered[CPInt2]:
    override inline def compare(that: CPInt2): Int =
        if i1 < that.i1 then -1
        else if i1 > that.i1 then 1
        else if i2 < that.i2 then -1
        else if i2 > that.i2 then 1
        else 0

    override protected def ctor(ints: Seq[Int]): CPInt2 =
        assert(ints.sizeIs == arity)
        CPInt2(ints.head, ints(1))

    /**
      * Creates tuple with two `i` elements.
      *
      * @param i Tuple value.
      */
    def this(i: Int) = this(i, i)

    /**
      * X-coordinate accessor (returns `i1` value). Allows to treat this type as XY-coordinate holder.
      */
    val x: Int = i1

    /**
      * Y-coordinate accessor (returns `i2` value). Allows to treat this type as XY-coordinate holder.
      */
    val y: Int = i2

/**
  * Companion object with utility and factory methods.
  */
object CPInt2:
    /**
      * Equivalent of `CPInt2(1)`.
      */
    final val ONE = CPInt2(1)

    /**
      * Equivalent of `CPInt2(-1)`.
      */
    final val NEG_ONE = CPInt2(-1)

    /**
      * Equivalent of `CPInt2(0)`.
      */
    final val ZERO = CPInt2(0)

    given Conversion[CPInt2, (Int, Int)] = t => t.i1 -> t.i2
    given Conversion[(Int, Int), CPInt2] = t => CPInt2(t._1, t._2)
