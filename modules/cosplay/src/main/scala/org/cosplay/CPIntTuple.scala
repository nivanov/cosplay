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

import scala.annotation.targetName

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
  * Mixin trait for supporting multi-int tuples.
  */
protected trait CPIntTuple[T](val ints: Int*):
    /**
      * Arity of this tuple.
      */
    val arity: Int = ints.size

    /**
      *
      */
    protected def ctor(ints: Seq[Int]): T

    /**
      * Maps this tuple using given function.
      *
      * @param f Mapping function.
      */
    inline def mapInt(f: Int => Int): T = ctor(ints.map(f))

    /**
      * Creates a copy of this tuple.
      */
    inline def copy: T = ctor(ints)

    /**
      * Tests whether all values in this tuple are zero.
      */
    inline def isZero: Boolean = ints.forall(_ == 0)

    /**
      * Tests whether all values in this tuple are one.
      */
    inline def isOne: Boolean = ints.forall(_ == 1)

    /**
      * Checks if this tuple contains only whole (>= 0) numbers.
      */
    inline def isWhole: Boolean = ints.forall(_ >= 0)

    /**
      * Checks if this tuple contains only positive (> 0) numbers.
      */
    inline def isPositive: Boolean = ints.forall(_ > 0)

    /**
      * Less-then '<' operator for tuples.
      *
      * @param x Other tuple to compare.
      * @note Tuples must have the same arity.
      */
    @targetName("lessTuple")
    inline def <(x: CPIntTuple[_]): Boolean =
        assert(x.arity == arity)
        ints.zip(x.ints).forall(p => p._1 < p._2)

    /**
      * Less-then '<' operator for `int`.
      *
      * @param x Other tuple to compare.
      */
    @targetName("lessInt")
    inline def <(x: Int): Boolean = ints.forall(_ < x)

    /**
      * Less-then-or-equal '<=' operator for `int`.
      *
      * @param x Other tuple to compare.
      */
    @targetName("lessThenOrEqualInt")
    inline def <=(x: Int): Boolean = ints.forall(_ <= x)

    /**
      * Greater-then '>' operator for `int`.
      *
      * @param x Other tuple to compare.
      * @note Tuples must have the same arity.
      */
    @targetName("greaterInt")
    inline def >(x: Int): Boolean = ints.forall(_ > x)

    /**
      * Greater-then-or-equal '>=' operator for `int`.
      *
      * @param x Other tuple to compare.
      * @note Tuples must have the same arity.
      */
    @targetName("greaterThenOrEqualInt")
    inline def >=(x: Int): Boolean = ints.forall(_ >= x)

    /**
      * Greater-then '>' operator for tuples.
      *
      * @param x Other tuple to compare.
      * @note Tuples must have the same arity.
      */
    @targetName("greaterTuple")
    inline def >(x: CPIntTuple[_]): Boolean =
        assert(x.arity == arity)
        ints.zip(x.ints).forall(p => p._1 > p._2)

    /**
      * Less-then-or-equal '<=' operator for tuples.
      *
      * @param x Other tuple to compare.
      * @note Tuples must have the same arity.
      */
    @targetName("lessThenOrEqualTuple")
    inline def <=(x: CPIntTuple[_]): Boolean =
        assert(x.arity == arity)
        ints.zip(x.ints).forall(p => p._1 <= p._2)

    /**
      * Greater-then-or-equal '>=' operator for tuples.
      *
      * @param x Other tuple to compare.
      * @note Tuples must have the same arity.
      */
    @targetName("greaterThenOrEqualTuple")
    inline def >=(x: CPIntTuple[_]): Boolean =
        assert(x.arity == arity)
        ints.zip(x.ints).forall(p => p._1 >= p._2)

    /**
      * Multiple two tuples by multiplying their corresponding values.
      *
      * @param x Other tuple to multiply with.
      * @note Tuples must have the same arity.
      */
    @targetName("multiTuple")
    inline def *(x: CPIntTuple[T]): T =
        assert(x.arity == arity)
        ctor(ints.zip(x.ints).map(p => p._1 * p._2))

    /**
      * Device this tuple by the given tuple by dividing their corresponding values.
      *
      * @param x Other tuple to divide by.
      * @note Tuples must have the same arity.
      */
    @targetName("divideTuple")
    inline def /(x: CPIntTuple[T]): T =
        assert(x.arity == arity)
        ctor(ints.zip(x.ints).map(p => p._1 / p._2))

    /**
      * Subtracts given tuple from this one by subtracting their corresponding values.
      *
      * @param x Tuple to subtract.
      * @note Tuples must have the same arity.
      */
    @targetName("minusTuple")
    inline def -(x: CPIntTuple[T]): T =
        assert(x.arity == arity)
        ctor(ints.zip(x.ints).map(p => p._1 - p._2))

    /**
      * Adds two tuples by adding their corresponding values.
      *
      * @param x Tuple to add.
      * @note Tuples must have the same arity.
      */
    @targetName("plusTuple")
    inline def +(x: CPIntTuple[_]): T =
        assert(x.arity == arity)
        ctor(ints.zip(x.ints).map(p => p._1 + p._2))

    /**
      * Subtracts given value from each member of this tuple.
      *
      * @param x Value to subtract.
      * @return New tuple as a result of subtraction.
      */
    @targetName("minusInt")
    inline def -(x: Int): T = mapInt(_ - x)

    /**
      * Adds given value to each member of this tuple.
      *
      * @param x Value to add.
      * @return New tuple as a result of addition.
      */
    @targetName("plusInt")
    inline def +(x: Int): T = mapInt(_ + x)

    /**
      * Multiplies given value with each member of this tuple.
      *
      * @param x Value to multiple by.
      * @return New tuple as a result of multiplication.
      */
    @targetName("multiInt")
    inline def *(x: Int): T = mapInt(_ * x)

    /**
      * Divides each member of this tuple by the given value.
      *
      * @param x Value to divide by.
      * @return New tuple as a result of division.
      */
    @targetName("divideInt")
    inline def /(x: Int): T = mapInt(_ / x)

    override def equals(obj: Any): Boolean = obj match
        case t: CPIntTuple[_] => t.ints == ints
        case _ => false

    override def toString: String = ints.mkString("[", ",", "]")
