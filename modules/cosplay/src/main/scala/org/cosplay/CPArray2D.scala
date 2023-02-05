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

import org.cosplay.{given, *}
import scala.collection.mutable
import scala.reflect.ClassTag

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
  * Immutable 2D-array. Optionally, has a clear value that is used to [[clear()]] out array.
  *
  * @param width Width of the array. Must be >= 0.
  * @param height Height of the array. Must be >= 0.
  * @tparam T Type of the array element.
  * @note If clear value is not set, the default clear value is `null`.
  */
//noinspection ScalaWeakerAccess
class CPArray2D[T](val width: Int, val height: Int)(using c: ClassTag[T]):
    if width < 0 || height < 0 then E(s"2D array dimension must be >= 0: [$width, $height]")

    private val data = Array.ofDim[T](width, height)
    private var clearVal: T = _
    private var isClearBufInit = false
    private lazy val clearBuf: Array[Array[T]] = Array.ofDim[T](width, height)

    /** Shape of this array as a rectangle.*/
    val rect: CPRect = CPRect(x = 0, y = 0, width, height)

    /** Maximum X-coordinate. If width is zero this will equal to `-1`. */
    val xMax: Int = width - 1

    /** Maximum Y-coordinate. If height is zero this will equal to `-1`. */
    val yMax: Int = height - 1

    /** Dimension of this array. */
    val dim: CPDim = CPDim(width, height)

    /** Number of cells in this 2D array. */
    val size: Int = width * height

    /** Checks whether or not this array is empty, i.e. it's [[size]] == 0. */
    val isEmpty: Boolean = size == 0

    /** Checks whether or not this array is not empty, i.e. it's [[size]] != 0. */
    val nonEmpty: Boolean = size > 0

    /**
      * Creates 1x1 array with a given single value.
      *
      * @param t Value to set at [0,0] coordinate.
      */
    def this(t: T)(using ClassTag[T]) =
        this(1, 1)
        set(0, 0, t)

    /**
      * Creates `w`x`h` array with specified clear value.
      *
      * @param w Array width.
      * @param h Array height.
      * @param clearVal Clear value.
      */
    def this(w: Int, h: Int, clearVal: T)(using ClassTag[T]) =
        this(w, h)
        this.clearVal = clearVal
        clear()

    /**
      * Creates array with specified dimension and clear value.
      *
      * @param dim Array dimension.
      * @param clearVal Clear value.
      */
    def this(dim: CPDim, clearVal: T)(using ClassTag[T]) =
        this(dim.w, dim.h)
        this.clearVal = clearVal
        clear()

    /**
      * Creates array with specified dimension.
      *
      * @param dim Array dimension.
      */
    def this(dim: CPDim)(using ClassTag[T]) =
        this(dim.w, dim.h)

    /**
      * Checks whether given XY-coordinate is valid for this array.
      *
      * @param x X-coordinate to check.
      * @param y Y-coordinate to check.
      */
    def isValid(x: Int, y: Int): Boolean = nonEmpty && x >= 0 && x <= xMax && y >= 0 && y <= yMax

    /**
      * Splits this array into `num` evenly split sub-arrays.
      *
      * @param num Number of sub-arrays to split into.
      * @return Sequence of sub-arrays.
      * @note `num` must be > 0. If this array is empty, an empty
      *     sequence will be returned.
      */
    def split(num: Int)(using c: ClassTag[T]): Seq[CPArray2D[T]] =
        require(num > 0, "Number of splits must be > 0.")
        if isEmpty then Seq.empty
        else
            if width > height && width % num == 0 then split(width / num, height)
            else if width < height && height % num == 0 then split(width, height / num)
            else if width % num == 0 then split(width / num, height / num)
            else E(s"Unable to determine split dimensions for $num sub-parts.")

    /**
      * Splits this array into a sequence of `w`x`h` arrays.
      *
      * @param w Width of the split.
      * @param h Height of the split.
      * @return Sequence of `w`x`h` arrays.
      * @note `w` and `h` must be > 0.
      * @note Given `w` and `h` must produce an even split. If this array is empty, an empty
      *     sequence will be returned.
      */
    def split(w: Int, h: Int)(using c: ClassTag[T]): Seq[CPArray2D[T]] =
        require(w > 0 && h > 0, "Width and height must be > 0.")
        if isEmpty then Seq.empty
        else
            if w > width then E(s"Invalid split width (too big): $w")
            if h > height then E(s"Invalid split height (too big): $h")
            if width % w != 0 then E(s"Uneven split width: $w ($width % $w != 0)")
            if height % h != 0 then E(s"Uneven split height: $h ($height % $h != 0)")

            val buf = mutable.Buffer.empty[CPArray2D[T]]

            var x, y = 0
            val xMax = width - 1
            val yMax = height - 1
            while (x <= xMax)
                y = 0
                while (y <= yMax)
                    buf.append(extract(CPRect(x, y, w, h)))
                    y += h
                x += w

            buf.toSeq

    /**
      * Maps this array to an array of different type.
      *
      * @param f Mapping function.
      * @tparam B Type of the new array.
      */
    def map[B](f: T => B)(using c: ClassTag[B]): CPArray2D[B] =
        val arr = new CPArray2D[B](width, height)
        rect.loop((x, y) => arr.set(x, y, f(data(x)(y))))
        arr

    /**
      * Map this array using extended mapping function.
      *
      * @param f Mapping function that takes value and its XY-coordinate in the array.
      * @tparam B Type of the new array.
      */
    def map[B](f: (T, Int, Int) => B)(using c: ClassTag[B]): CPArray2D[B] =
        val arr = new CPArray2D[B](width, height)
        rect.loop((x, y) => arr.set(x, y, f(data(x)(y), x, y)))
        arr

    /**
      * Calls given function for each array element.
      *
      * @param f Function to call for each element.
      */
    def foreach(f: T => Unit): Unit = rect.loop((x, y) => f(data(x)(y)))

    /**
      * Checks whether this array contains at least one element satisfying given predicate.
      *
      * @param p Predicate to test.
      */
    def contains(p: T => Boolean): Boolean =
        var x = 0
        var y = 0
        var found = false
        while (x <= xMax && !found)
            y = 0
            while (y <= yMax && !found)
                if p(data(x)(y)) then found = true
                y += 1
            x += 1
        found

    /**
      * Collapses given array into a single value given the initial value and associative binary operation
      * acting as an accumulator. Folding over the elements in this 2D array will be horizontal first. In other words,
      * given the 2D array with the following coordinates:
      * {{{
      *     +-----------------+
      *     |(0,0) (1,0) (2,0)|
      *     |(0,1) (1,1) (2,1)|
      *     |(0,2) (1,2) (2,2)|
      *     +-----------------+
      * }}}
      * this method will iterate in the following order:
      * {{{
      *     (0,0) (1,0) (2,0) (0,1) (1,1) (2,1) (0,2) (1,2) (2,2)
      * }}}
      *
      * @param z Initial value.
      * @param op Accumulating binary operation.
      */
    def foldHor[Z](z: Z)(op: (Z, T) => Z): Z =
        var zz = z
        loopHor((t, _, _) => zz = op(zz, t))
        zz

    /**
      * Collapses given array into a single value given the initial value and associative binary operation
      * acting as an accumulator. Folding over the elements in this 2D array will be vertical first. In other words,
      * given the 2D array with the following coordinates:
      * {{{
      *     +-----------------+
      *     |(0,0) (1,0) (2,0)|
      *     |(0,1) (1,1) (2,1)|
      *     |(0,2) (1,2) (2,2)|
      *     +-----------------+
      * }}}
      * this method will iterate in the following order:
      * {{{
      *     (0,0) (0,1) (0,2) (1,0) (1,1) (1,2) (2,0) (2,1) (2,2)
      * }}}
      *
      * @param z Initial value.
      * @param op Accumulating binary operation.
      */
    def foldVert[Z](z: Z)(op: (Z, T) => Z): Z =
        var zz = z
        loopVert((t, _, _) => zz = op(zz, t))
        zz

    /**
      * Counts how many elements in this array satisfying given predicate.
      *
      * @param p Predicate to test.
      */
    def count(p: T => Boolean): Int = foldHor(0)((n, t) => if p(t) then n + 1 else n)

    /**
      * Calls given function for each array element.
      * Iteration over the elements in this 2D array will be horizontal first. In other words,
      * given the 2D array with the following coordinates:
      * {{{
      *     +-----------------+
      *     |(0,0) (1,0) (2,0)|
      *     |(0,1) (1,1) (2,1)|
      *     |(0,2) (1,2) (2,2)|
      *     +-----------------+
      * }}}
      * this method will iterate in the following order:
      * {{{
      *     (0,0) (1,0) (2,0) (0,1) (1,1) (2,1) (0,2) (1,2) (2,2)
      * }}}
      *
      * @param f Function to call for each element. The function takes value and its XY-coordinate
      *     in the array.
      * @see [[loopVert()]]
      * @see [[loopHor()]]
      */
    def loop(f: (T, Int, Int) => Unit): Unit = loopHor(f)

    /**
      * Calls given function for each array element.
      * Iteration over the elements in this 2D array will be horizontal first. In other words,
      * given the 2D array with the following coordinates:
      * {{{
      *     +-----------------+
      *     |(0,0) (1,0) (2,0)|
      *     |(0,1) (1,1) (2,1)|
      *     |(0,2) (1,2) (2,2)|
      *     +-----------------+
      * }}}
      * this method will iterate in the following order:
      * {{{
      *     (0,0) (1,0) (2,0) (0,1) (1,1) (2,1) (0,2) (1,2) (2,2)
      * }}}
      *
      * @param f Function to call for each element. The function takes value and its XY-coordinate
      *     in the array.
      * @see [[loopVert()]]
      * @see [[loop()]]
      */
    def loopHor(f: (T, Int, Int) => Unit): Unit = rect.loopHor((x, y) => f(get(x, y), x, y))

    /**
      * Calls given function for each array element.
      * Iteration over the elements in this 2D array will be vertical first. In other words,
      * given the 2D array with the following coordinates:
      * {{{
      *     +-----------------+
      *     |(0,0) (1,0) (2,0)|
      *     |(0,1) (1,1) (2,1)|
      *     |(0,2) (1,2) (2,2)|
      *     +-----------------+
      * }}}
      * this method will iterate in the following order:
      * {{{
      *     (0,0) (0,1) (0,2) (1,0) (1,1) (1,2) (2,0) (2,1) (2,2)
      * }}}
      *
      * @param f Function to call for each element. The function takes value and its XY-coordinate
      *     in the array.
      * @see [[loop()]]
      * @see [[loopHor()]]
      */
    def loopVert(f: (T, Int, Int) => Unit): Unit = rect.loopVert((x, y) => f(get(x, y), x, y))

   /**
      *
      */
    private def initClearBuf(): Unit =
        rect.loop((x, y) => clearBuf(x)(y) = clearVal)
        isClearBufInit = true

    /**
      * Clears this array with the clear value.
      *
      * @note If the clear value is not set, the default value is `null`.
      */
    def clear(): Unit =
        if nonEmpty then
            if !isClearBufInit then initClearBuf()
            var x = 0
            while (x < width)
                Array.copy(clearBuf(x), 0, data(x), 0, height)
                x += 1

    /**
      * Clears this array with given clear value.
      */
    def clear(clearVal: T): Unit =
        this.clearVal = clearVal
        initClearBuf()
        clear()

    /**
      * Gets array value for given XY-coordinate.
      *
      * @param x X-coordinate.
      * @param y Y-coordinate.
      */
    def get(x: Int, y: Int): T = data(x)(y)

    /**
      * Sets array value at a given XY-coordinate.
      *
      * @param x X-coordinate.
      * @param y Y-coordinate.
      * @param t Value to set.
      */
    def set(x: Int, y: Int, t: T): Unit = data(x)(y) = t

    /**
      *
      * @param x X-coordinate
      * @param f Blank predicate.
      */
    private def isColumnBlank(x: Int, f: T => Boolean): Boolean = isBlank(x, f, false)

    /**
      *
      * @param y Y-coordinate
      * @param f Blank predicate.
      */
    private def isRowBlank(y: Int, f: T => Boolean): Boolean = isBlank(y, f, true)

    private def isBlank(d: Int, f: T => Boolean, isRow: Boolean): Boolean =
        val max = if isRow then xMax else yMax
        if nonEmpty then
            var a = 0
            var isBlank = true
            while (a <= max && isBlank)
                isBlank = f(if isRow then data(a)(d) else data(d)(a))
                a += 1
            isBlank
        else
            false

    /**
      * Creates new trimmed array with a given blank value.
      *
      * @param blank Blank value to use for trimming.
      * @note If trimming is not possible, returns this array.
      */
    def trim(blank: T)(using c: ClassTag[T]): CPArray2D[T] = trim(_ == blank)

    /**
      * Creates new trimmed array. Rows and columns are trimmed if all their values
      * satisfy given predicate.
      *
      * @param f Trimming predicate. If `true` the value will be considered blank.
      * @return Trimmed out (smaller) copy of this array.
      * @note If trimming is not possible, returns this array.
      */
    def trim(f: T => Boolean)(using c: ClassTag[T]): CPArray2D[T] =
        if nonEmpty then
            var x2, y2 = 0
            while (x2 <= xMax && isColumnBlank(x2, f)) x2 += 1
            while (y2 <= yMax && isRowBlank(y2, f)) y2 += 1

            var xMax2 = xMax
            var yMax2 = yMax
            while (xMax2 >= 0 && isColumnBlank(xMax2, f)) xMax2 -= 1
            while (yMax2 >= 0 && isRowBlank(yMax2, f)) yMax2 -= 1

            if x2 > 0 || xMax2 < xMax || y2 > 0 || y2 < yMax2 then
                extract(CPRect(x2, y2, xMax2 - x2 + 1, yMax2 - y2 + 1))
            else
                this
        else
            this

    /**
      * Gets a deep copy of this array.
      */
    def copy(): CPArray2D[T] =
        val cp = new CPArray2D(width, height, clearVal)(using c: ClassTag[T])
        copyTo(cp)
        cp

    /**
      * Extracts deep copy sub-array with given shape.
      *
      * @param rect A shape to extract as a deep copy.
      * @return Deep copy sub-array with given shape.
      */
    def extract(rect: CPRect)(using c: ClassTag[T]): CPArray2D[T] =
        if nonEmpty && this.rect.contains(rect) then
            val arr = new CPArray2D[T](rect.dim)
            var arrX = 0
            for x <- rect.xMin to rect.xMax do
                Array.copy(data(x), rect.yMin, arr.data(arrX), 0, rect.height)
                arrX += 1
            arr
        else
            this

    /**
      * Copies a given frame into another 2D array.
      *
      * @param other Another array to copy to.
      * @param frame A frame to copy.
      */
    def copyTo(other: CPArray2D[T], frame: CPRect = rect): Unit =
        if nonEmpty && other.nonEmpty && rect.contains(frame) then
            for x <- frame.xMin to frame.xMax do
                Array.copy(data(x), frame.yMin, other.data(x), frame.yMin, frame.height)

    /**
      * Creates copy of this array with flipped X and Y coordinates.
      */
    def flip(using c: ClassTag[T]): CPArray2D[T] =
        if nonEmpty then
            val arr = new CPArray2D[T](height, width)
            rect.loop((x, y) => arr.set(y, x, data(x)(y)))
            arr
        else
            this

    override def equals(obj: Any): Boolean = obj match
        case other: CPArray2D[_] => dim == other.dim && !rect.exists((x, y) => data(x)(y) != other.data(x)(y))
        case _ => false

/**
  * Contains factory methods for 2D arrays.
  */
object CPArray2D:
    /**
      * Creates new 2D array from given list of [[CPPosPixel]] instances.
      * Note that clear value is not set.
      *
      * @param pps List of [[CPPosPixel]] instances to create a new array from.
      */
    def apply(pps: List[CPPosPixel]): CPArray2D[CPPixel] =
        val w = pps.maxBy(_.x).x + 1
        val h = pps.maxBy(_.y).y + 1
        val arr = new CPArray2D[CPPixel](w, h)
        pps.foreach(pp => arr.set(pp.x, pp.y, pp.px))
        arr
        
    /**
      * Creates new 2D array from given sequence of [[CPPixel pixels]] and width.
      * Note that clear value is not set.
      *
      * @param pxs Sequence of [[CPPixel pixels]].
      * @param width Required width. Height is calculated automatically.
      */
    def apply(pxs: Seq[CPPixel], width: Int): CPArray2D[CPPixel] =
        val sz = pxs.size
        if width < 0 || width > sz || sz % width != 0 then
            E(s"Invalid width: $width")
        val height = sz / width
        val arr = new CPArray2D[CPPixel](width, height)
        var i = 0
        while i < sz do
            arr.set(i % width, i / width, pxs(i))
            i += 1
        arr

    /**
      * Creates new 2D array of [[Char]] from given string. Height of the array will be 1.
      * Note that clear value will be set to ' ' (space).
      *
      * @param str String to creates new 2D array from.
      */
    def apply(str: String): CPArray2D[Char] = CPArray2D(Seq(str))

    /**
      * Creates new 2D array of [[Char]] from given sequence of strings.
      * Note that clear value will be set to ' ' (space). All strings will be padded
      * to the maximum value with clear value ' ' (space) too.
      *
      * @param data Sequence of strings to create new 2D array from.
      */
    def apply(data: Seq[String]): CPArray2D[Char] =
        val w = data.maxBy(_.length).length
        val h = data.length
        val arr = new CPArray2D[Char](h, w, ' ') // Note reverse width and height.
        var y = 0
        while (y < h)
            // Normalize (pad).
            val s = data(y).padTo(w, arr.clearVal)
            val a1 = s.toCharArray
            val a2 = arr.data(y)
            Array.copy(a1, 0, a2, 0, w)
            y += 1
        arr.flip
