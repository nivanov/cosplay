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
import org.apache.commons.math3.analysis.polynomials.*

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
  * Set of utilities for interpolation functions.
  */
object CPCurve:
    /**
      * Creates a function that produces a color that is a gradient from the given parameters.
      * Each call to the returned function will produce a color that is an incremental shift
      * from color `c1` to color `c2`.
      *
      * @param c1 Start of the gradient color.
      * @param c2 End of the gradient color.
      * @param steps Number of steps of the gradient. Returned function will cycle back and will start from
      *     the beginning of the gradient if it is called more than `steps` times.
      * @return Gradient producing function.
      * @see [[CPColor.gradientFun()]]
      */
    def colorGradient(c1: CPColor, c2: CPColor, steps: Int): () => CPColor =
        CPColor.gradientFun(c1, c2, steps)

    /**
      * Gets a function that is a polynomial [[https://mathworld.wolfram.com/LagrangeInterpolatingPolynomial.html Lagrange interpolation]]
      * for the given set of interpolating points.
      *
      * @param points Lagrange interpolation points (abscissas and function values).
      * @return Polynomial Lagrange interpolation for the given set of interpolating
      *     points.
      * @see https://mathworld.wolfram.com/LagrangeInterpolatingPolynomial.html
      * @see https://en.wikipedia.org/wiki/Lagrange_polynomial
      */
    def lagrangePoly(points: Seq[(Float, Float)]): Float => Float =
        if points.sizeIs < 2 then E(s"Need two or more interpolating points.")
        try
            val poly = PolynomialFunctionLagrangeForm(
                points.map(_._1.toDouble).toArray,
                points.map(_._2.toDouble).toArray
            )
            (z: Float) => poly.value(z).toFloat
        catch
            case e: Exception => E(s"Failed to interpolate Lagrange polynomial.", e)

    /**
      * Gets a linear gradient function for given two values.
      *
      * @param f1 Inclusive start of the gradient.
      * @param f2 Inclusive end of the gradient.
      * @param steps Number of steps in gradient interpolation.
      * @return Linear gradient function for given two values.
      */
    def linearGradient(f1: Float, f2: Float, steps: Int): () => Float =
        val df = (f2 - f1) / steps
        var i = 0
        () => {
            val f = f1 + (i % steps) * df
            i += 1
            f
        }




