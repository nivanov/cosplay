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
  * General 4-int tuple.
  *
  * @param i1 1st value.
  * @param i2 2nd value.
  * @param i3 3rd value.
  * @param i4 4th value.
  */
class CPInt4(val i1: Int, val i2: Int, val i3: Int, val i4: Int) extends CPIntTuple[CPInt4](i1, i2, i3, i4) with Ordered[CPInt4]:
    override inline def compare(that: CPInt4): Int =
        if i1 < that.i1 then -1
        else if i1 > that.i1 then 1
        else if i2 < that.i2 then -1
        else if i2 > that.i2 then 1
        else if i3 < that.i3 then -1
        else if i3 > that.i3 then 1
        else if i4 < that.i4 then -1
        else if i4 > that.i4 then 1
        else 0
    override protected def ctor(ints: Seq[Int]): CPInt4 =
        assert(ints.sizeIs == arity)
        CPInt4(ints.head, ints(1), ints(2), ints(3))

    /**
      * Creates 4-int tuple with the same value.
      *
      * @param i Single value.
      */
    def this(i: Int) = this(i, i, i, i)

    /**
      * Creates 4-int tuple from two 2-int tuples.
      *
      * @param t1 Tuple for 1st and 2nd value.
      * @param t2 Tuple for 3rd and 4th value.
      */
    def this(t1: CPInt2, t2: CPInt2) = this(t1.i1, t1.i2, t2.i1, t2.i2)

    /**
      * Creates 4-int tuple from given parameters.
      *
      * @param t1 Tuple for 1st and 2nd values.
      * @param i3 3rd value.
      * @param i4 4th value.
      */
    def this(t1: CPInt2, i3: Int, i4: Int) = this(t1.i1, t1.i2, i3, i4)

    /**
      * Creates 4-int tuple from given parameters.
      *
      * @param i1 1st value.
      * @param i2 2rd value.
      * @param t2 3rd and 4th values.
      */
    def this(i1: Int, i2: Int, t2: CPInt2) = this(i1, i2, t2.i1, t2.i2)

/**
  * Companion object with utility functions.
  */
object CPInt4:
    /**
      * Equivalent of `CPInt4(1)`.
      */
    val ONE = CPInt4(1)

    /**
      * Equivalent of `CPInt4(-1)`.
      */
    val NEG_ONE = CPInt4(-1)

    /**
      * Equivalent of `CPInt4(0)`.
      */
    val ZERO = CPInt4(0)

    given Conversion[CPInt4, (Int, Int, Int, Int)] = t => (t.i1, t.i2, t.i3, t.i4)
    given Conversion[(Int, Int, Int, Int), CPInt4] = t => CPInt4(t._1, t._2, t._3, t._4)
