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
               All rights reserved.
*/

/**
  * 2D dimension immutable container.
  *
  * @param width Width in characters.
  * @param height Height in characters.
  */
final case class CPDim(width: Int, height: Int) extends CPIntTuple[CPDim](width, height)
    with Ordered[CPDim]
    with Serializable:
    override protected def ctor(ints: Seq[Int]): CPDim =
        assert(ints.sizeIs == arity)
        CPDim(ints.head, ints(1))

    override def compare(that: CPDim): Int = area.compareTo(that.area)

    /**
      * Width in characters (shortcut API).
      */
    val w: Int = width

    /**
      * Height in characters (shortcut API).
      */
    val h: Int = height

    /**
      * Width in characters as float (shortcut API).
      */
    val wF: Float = width.toFloat

    /**
      * Height in characters as float (shortcut API).
      */
    val hF: Float = height.toFloat

    /**
      * Creates `d`x`d` square dimension.
      *
      * @param d Square dimension.
      */
    def this(d: Int) = this(d, d)

    /**
      * Tests whether this dimension is less than the given one.
      * Note that this operation will check that both width and height are less than.
      *
      * @param that Other dimension to check.
      */
    @targetName("lessThan")
    infix def <@(that: CPDim): Boolean = width < that.width && height < that.height

    /**
      * Tests whether this dimension is less than or equal to the given one.
      * Note that this operation will check that both width and height are less than or equal.
      *
      * @param that Other dimension to check.
      */
    @targetName("lessThanOrEqual")
    infix def <=@(that: CPDim): Boolean = width <= that.width && height <= that.height

    /**
      * Tests whether this dimension is greater than the given one.
      * Note that this operation will check that both width and height are greater than.
      *
      * @param that Other dimension to check.
      */
    @targetName("greaterThan")
    infix def >@(that: CPDim): Boolean = width > that.width && height > that.height

    /**
      * Tests whether this dimension is less than or greater to the given one.
      * Note that this operation will check that both width and height are greater than or equal.
      *
      * @param that Other dimension to check.
      */
    @targetName("greaterThanOrEqual")
    infix def >=@(that: CPDim): Boolean = width >= that.width && height >= that.height

    /**
      * Area in characters for this dimension.
      */
    final val area: Int = width * height

    /**
      * Whether width or height equals to zero.
      */
    final val isEmpty: Boolean = area == 0

    /**
      * Whether width and height are greater than zero.
      */
    final val nonEmpty: Boolean = area > 0

    override def toString: String = s"[${width}x$height]"

/**
  * Companion object with static utility functions.
  */
object CPDim:
    /**
      * Zero dimension.
      */
    final val ZERO = new CPDim(0)
