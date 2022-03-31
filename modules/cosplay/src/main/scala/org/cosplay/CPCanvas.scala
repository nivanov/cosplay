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

import CPCanvas.ArtLineStyle
import CPCanvas.ArtLineStyle.*
import impl.CPUtils
import CPPixel.*
import org.cosplay.CPCanvas.*

import scala.annotation.tailrec
import scala.collection.mutable

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
  * 2D rendering pane.
  *
  * For each game frame, the game engine creates a new empty canvas for all scene objects to draw on.
  * This canvas is available to scene objects via [[CPSceneObjectContext.getCanvas]] method. Game engine
  * then compares previous canvas and this one to determine which areas of the terminal need redrawing.
  * You can also create the canvas object outside game loop using companion object methods.
  *
  * Canvas `(0,0)` coordinate point is located in the top left corner. X-axis goes to the right and Y-axis
  * points down. Every drawn pixel on the canvas also has a Z-index or depth. Pixel with larger or equal
  * Z-index visually overrides the pixel with the smaller Z-index.
  *
  * ASCII-based graphics are vastly different from the traditional raster-based graphics.
  * Since ASCII-based drawing uses printable characters from ASCII character set that are displayed on basic text terminal
  * most differences are obvious but some are more subtle and unexpected. For example, while straight vertical and
  * horizontal lines are easy to implement many curved lines are much trickier to draw. Specifically, circles
  * and ellipses, for example, are hard to do properly in automated way. Moreover, many complex curves need to
  * be done manually, especially if they are dynamically redrawn on each frame update.
  *
  * This class provides many methods for basic line and rectangular drawing, circle and polylines, anti-aliasing,
  * "color" fill in, and much more. Most functions have multiple overridden variants with different parameters so that they
  * can be easily used in different contexts. Note also that this class deals primarily with line ASCII art and has
  * only few functions like [[antialias() antialiasing]] for the solid ASCII art.
  *
  * One of the useful techniques of obtaining a complex "drawn-on-canvas" images is to create
  * a new canvas object at the scene's start (see [[CPLifecycle.onStart()]] method), draw all necessary ASCII art on it and
  * then capture the [[CPImage image]] using one of the `capture()` methods in this class. Once [[CPImage image]] is
  * obtained you can use it with any image-based sprites to display it.
  *
  * There are many online tutorials for ASCII art that are highly recommended, including from:
  *  - [[https://www.ludd.ltu.se/~vk/pics/ascii/junkyard/techstuff/tutorials/Joan_Stark.html Joan Stark]]
  *  - [[https://www.ludd.ltu.se/~vk/pics/ascii/junkyard/techstuff/tutorials/Daniel_Au.html Daniel Au]]
  *  - [[https://www.ludd.ltu.se/~vk/pics/ascii/junkyard/techstuff/tutorials/Susie_Oviatt.html Susie Oviatt]]
  *  - [[https://www.ludd.ltu.se/~vk/pics/ascii/junkyard/techstuff/tutorials/Rowan_Crawford.html Rowan Crawford]]
  *  - [[https://www.ludd.ltu.se/~vk/pics/ascii/junkyard/techstuff/tutorials/Normand_Veilleux.html Normand Veilleux]]
  *  - [[https://www.ludd.ltu.se/~vk/pics/ascii/junkyard/techstuff/tutorials/Targon.html Targon]]
  *  - [[https://www.ludd.ltu.se/~vk/pics/ascii/junkyard/techstuff/tutorials/Hayley_Wakenshaw.html Hayley Wakenshaw]]
  *
  * See also the following resources for general ASCII art collections:
  *  - http://www.asciiworld.com/
  *  - https://www.asciiart.eu/
  *  - https://asciiart.website
  *  - https://www.incredibleart.org/links/ascii.html
  *  - http://www.afn.org/~afn39695/collect.html
  *  - http://blocktronics.org/
  *  - http://ansiart.com/
  *  - http://www.ascii-art.de/
  *  - https://llizardakaejm.wordpress.com/ascii-animations/
  *
  * @param pane Underlying z-pane.
  * @param clip Clipping region.
  * @see [[CPSceneObjectContext.getCanvas]]
  * @example See [[org.cosplay.examples.canvas.CPCanvasExample CPCanvasExample]] class for the example of
  *     using canvas.
  */
class CPCanvas(pane: CPZPixelPane, clip: CPRect):
    /**
      * Gets clipping region for this canvas. Note that it is available only for information
      * purpose since all functions in this class automatically handle the clipping.
      */
    def getClipRect: CPRect = clip

    /** Dimension of this canvas. */
    final val dim: CPDim = pane.getDim

    /** Width of this canvas. */
    final val width: Int = dim.w

    /** Width of this canvas (shortcut API). */
    final val w: Int = dim.w

    /** Width of this canvas as float (shortcut API). */
    final val wF: Float = dim.wF

    /** Height of this canvas. */
    final val height: Int = dim.h

    /** Height of this canvas (shortcut API). */
    final val h: Int = dim.h

    /** Height of this canvas as float (shortcut API). */
    final val hF: Float = dim.hF

    /** Maximum X-coordinate of this canvas. */
    final val xMax = width - 1

    /** Maximum X-coordinate of this canvas as a float. */
    final lazy val xMaxF = xMax.toFloat

    /** Maximum Y-coordinate of this canvas. */
    final val yMax = height - 1

    /** Maximum Y-coordinate of this canvas as a float. */
    final lazy val yMaxF = yMax.toFloat

    /** Minimum X-coordinate of this canvas (always zero). */
    final val xMin = 0

    /** Minimum X-coordinate of this canvas (always zero) as a float. */
    final lazy val xMinF = 0f

    /** Minimum Y-coordinate of this canvas (always zero). */
    final val yMin = 0

    /** Minimum Y-coordinate of this canvas (always zero) as a float. */
    final lazy val yMinF = 0f

    /** X-coordinate of the center point of this canvas. */
    final val xCenter = xMax / 2

    /** X-coordinate of the center point of this canvas as a float. */
    final lazy val xCenterF = xMaxF / 2

    /** Y-coordinate of the center point of this canvas. */
    final val yCenter = yMax / 2

    /** Y-coordinate of the center point of this canvas as a float. */
    final val yCenterF = yMaxF / 2

    /** Rectangle object (shape) for this canvas. */
    final val rect = new CPRect(0, 0, dim)

    /**
      * Tests whether or not given XY-coordinates are within this canvas.
      *
      * @param x X-coordinate to test.
      * @param y Y-coordinate to test.
      */
    def isValid(x: Int, y: Int): Boolean = x >= xMin && x <= xMax && y >= yMin && y <= yMax
    
    /**
      * Antialiases solid ascii-art canvas region. Works `only` for solid ascii-art.
      *
      * The antialiasing algorithm is based on implementation described at
      * https://codegolf.stackexchange.com/questions/5450/anti-aliasing-ascii-art
      *
      * @param x X-coordinate of the top level corner of the region.
      * @param y Y-coordinate of the top level corner of the region.
      * @param dim Dimension of the region.
      * @param isBlank Predicate defining whether a particular pixel should be considered as a blank.
      */
    def antialias(x: Int, y: Int, dim: CPDim, isBlank: CPPixel => Boolean): Unit =
        antialias(x, y, x + dim.w - 1, y + dim.h - 1, isBlank)

    /**
      * Antialiases solid ascii-art canvas region. Works `only` for solid ascii-art.
      *
      * The antialiasing algorithm is based on implementation described at
      * https://codegolf.stackexchange.com/questions/5450/anti-aliasing-ascii-art
      *
      * @param rect Region to antialias.
      * @param isBlank Predicate defining whether a particular pixel should be considered as a blank.
      */
    def antialias(rect: CPRect, isBlank: CPPixel => Boolean): Unit =
        rect.loop((x, y) => {
            val zpx = pane.getPixel(x, y)
            val px = zpx.px
            // Based on: https://codegolf.stackexchange.com/questions/5450/anti-aliasing-ascii-art
            if !isBlank(px) then
                val top = y > 0 && !isBlank(pane.getPixel(x, y - 1).px)
                val left = x > 0 && !isBlank(pane.getPixel(x - 1, y).px)
                val bottom = y < dim.h - 1 && !isBlank(pane.getPixel(x, y + 1).px)
                val right = x < dim.w - 1 && !isBlank(pane.getPixel(x + 1, y).px)

                pane.addPixel(px.withChar(CPUtils.aaChar(px.char, top, left, bottom, right)), x, y, zpx.z)
            else
                pane.addPixel(px, x, y, zpx.z)
        })

    /**
      * Antialiasing solid ascii-art canvas region. Works `only` for solid ascii-art.
      *
      * The antialiasing algorithm is based on implementation described at
      * https://codegolf.stackexchange.com/questions/5450/anti-aliasing-ascii-art
      *
      * @param x1 X-coordinate of the top left corner for the region.
      * @param y1 Y-coordinate of the top left corner for the region.
      * @param x2 X-coordinate of the bottom right corner for the region.
      * @param y2 Y-coordinate of the bottom right corner for the region.
      * @param isBlank Predicate defining whether a particular pixel should be considered as a blank.
      */
    def antialias(x1: Int, y1: Int, x2: Int, y2: Int, isBlank: CPPixel => Boolean): Unit =
        antialias(new CPRect(x1 -> y1, x2 -> y2), isBlank)

    /**
      * Draws a circle.
      *
      * @param x X-coordinate of the circle center.
      * @param y Y-coordinate of the circle center.
      * @param radius Radius of the circle in characters.
      * @param z Z-index of the drawing. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param xFactor Horizontal squishing factor. Values below 1.0 will squish the circle horizontally. Values
      *     above 1.0 will stretch the circle horizontally. X-factor of `2.0` and Y-factor of `1.0` typically produce
      *     correct circle using 1x2 monospace font.
      * @param yFactor Vertical squishing factor. Values below 1.0 will squish the circle vertically. Values
      *     above 1.0 will stretch the circle vertically. X-factor of `2.0` and Y-factor of `1.0` typically produce
      *     correct circle using 1x2 monospace font.
      * @param densFactor Density factor defines "granularity" of drawing. Values below `1.0` allow to compensate
      *     for discreetness of ASCII graphics. Typically, value of `0.5` is a good default.
      * @param fillGaps Whether or not to fill potential gaps in circle drawing. Filling the gaps
      *     slows down the drawing but produces a better looking circle.
      * @param pxs Sequence of pixels to use in drawing the circle.
      */
    def drawCircle(x: Int, y: Int, radius: Int, z: Int, xFactor: Float, yFactor: Float, densFactor: Float, fillGaps: Boolean, pxs: Seq[CPPixel]): CPRect =
        var i = 0
        val pxsNum = pxs.length
        drawCircle(x, y, radius, z, xFactor, yFactor, densFactor, fillGaps, (_, _) => {
            val px = pxs(i % pxsNum)
            i += 1
            px
        })

    /**
      * Draws a circle with a single pixel returning its final shape.
      *
      * @param x X-coordinate of the circle center.
      * @param y Y-coordinate of the circle center.
      * @param radius Radius of the circle in characters.
      * @param z Z-index of the drawing. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param xFactor Horizontal squishing factor. Values below 1.0 will squish the circle horizontally. Values
      *     above 1.0 will stretch the circle horizontally. X-factor of `2.0` and Y-factor of `1.0` typically produce
      *     correct circle using 1x2 monospace font.
      * @param yFactor Vertical squishing factor. Values below 1.0 will squish the circle vertically. Values
      *     above 1.0 will stretch the circle vertically. X-factor of `2.0` and Y-factor of `1.0` typically produce
      *     correct circle using 1x2 monospace font.
      * @param densFactor Density factor defines "granularity" of drawing. Values below `1.0` allow to compensate
      *     for discreetness of ASCII graphics. Typically, value of `0.5` is a good default.
      * @param fillGaps Whether or not to fill potential gaps in circle drawing. Filling the gaps
      *     slows down the drawing but produces a better looking circle.
      * @param px Pixel to draw a circle with.
      */
    def drawCircle(x: Int, y: Int, radius: Int, z: Int, xFactor: Float, yFactor: Float, densFactor: Float, fillGaps: Boolean, px: CPPixel): CPRect =
        drawCircle(x, y, radius, z, xFactor, yFactor, densFactor, fillGaps, (_, _) => px)

    /**
      * Draws a circle returning its final shape returning its final shape.
      *
      * @param x X-coordinate of the circle center.
      * @param y Y-coordinate of the circle center.
      * @param radius Radius of the circle in characters.
      * @param z Z-index of the drawing. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param xFactor Horizontal squishing factor. Values below 1.0 will squish the circle horizontally. Values
      *     above 1.0 will stretch the circle horizontally. X-factor of `2.0` and Y-factor of `1.0` typically produce
      *     correct circle using 1x2 monospace font.
      * @param yFactor Vertical squishing factor. Values below 1.0 will squish the circle vertically. Values
      *     above 1.0 will stretch the circle vertically. X-factor of `2.0` and Y-factor of `1.0` typically produce
      *     correct circle using 1x2 monospace font.
      * @param densFactor Density factor defines "granularity" of drawing. Values below `1.0` allow to compensate
      *     for discreetness of ASCII graphics. Typically, value of `0.5` is a good default.
      * @param fillGaps Whether or not to fill potential gaps in circle drawing. Filling the gaps
      *     slows down the drawing but produces a better looking circle.
      * @param pxf Pixel producer function.
      */
    def drawCircle(x: Int, y: Int, radius: Int, z: Int, xFactor: Float, yFactor: Float, densFactor: Float, fillGaps: Boolean, pxf: (Int, Int) => CPPixel): CPRect =
        val pxs = circlePixels(x, y, radius, xFactor, yFactor, densFactor, fillGaps, pxf)
        var x1 = Int.MaxValue
        var y1 = Int.MaxValue
        var x2 = -1
        var y2 = -1
        pxs.foreach(px => {
            x1 = x1.min(px.x)
            y1 = y1.min(px.y)
            x2 = x2.max(px.x)
            y2 = y2.max(px.y)
        })

        x1 = x1.max(0)
        y1 = y1.max(0)
        x2 = x2.min(xMax)
        y2 = y2.min(yMax)
        pxs.foreach(p => drawPixel(pxf(p.x, p.y), p.x, p.y, z))
        new CPRect(x1 -> y1, x2 -> y2)

    /**
      * Produces mutable array of pixels representing the circle.
      *
      * @param x X-coordinate of the circle center.
      * @param y Y-coordinate of the circle center.
      * @param radius Radius of the circle in characters.
      * @param z Z-index of the drawing. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param xFactor Horizontal squishing factor. Values below 1.0 will squish the circle horizontally. Values
      *     above 1.0 will stretch the circle horizontally. X-factor of `2.0` and Y-factor of `1.0` typically produce
      *     correct circle using 1x2 monospace font.
      * @param yFactor Vertical squishing factor. Values below 1.0 will squish the circle vertically. Values
      *     above 1.0 will stretch the circle vertically. X-factor of `2.0` and Y-factor of `1.0` typically produce
      *     correct circle using 1x2 monospace font.
      * @param densFactor Density factor defines "granularity" of drawing. Values below `1.0` allow to compensate
      *     for discreetness of ASCII graphics. Typically, value of `0.5` is a good default.
      * @param fillGaps Whether or not to fill potential gaps in circle drawing. Filling the gaps
      *     slows down the drawing but produces a better looking circle.
      * @param pxf Pixel producer function.
      */
    def circlePixels(x: Int, y: Int, radius: Int, xFactor: Float, yFactor: Float, densFactor: Float, fillGaps: Boolean, pxf: (Int, Int) => CPPixel): mutable.ArrayBuffer[CPPosPixel] =
        if radius > 0 then
            val q1 = mutable.ArrayBuffer.empty[CPInt2]
            val q2 = mutable.ArrayBuffer.empty[CPInt2]
            val q3 = mutable.ArrayBuffer.empty[CPInt2]
            val q4 = mutable.ArrayBuffer.empty[CPInt2]
            val r2 = radius * radius
            // Draw circle with potential gaps.
            var i = 0f
            while (i < radius.toFloat)
                val c = Math.sqrt(r2 - i * i).round.toInt
                val dx = (xFactor * i).round
                val dy = (yFactor * c).round
                val a1 = x + dx
                val a2 = x - dx
                val b1 = y - dy
                val b2 = y + dy
                q1 += CPInt2(a1, b1)
                q2 += CPInt2(a1, b2)
                q3 += CPInt2(a2, b2)
                q4 += CPInt2(a2, b1)
                i = i + densFactor
            var buf: mutable.ArrayBuffer[CPInt2] = null
            if fillGaps then
                buf = q1 ++ q2.reverse ++ q3 ++ q4.reverse
                // Fill in the gaps.
                val gaps = mutable.ArrayBuffer.empty[CPInt2]
                var last: CPInt2 = null
                for (p <- buf)
                    if last != null then
                        val dx = last.x - p.x
                        val dy = last.y - p.y
                        if dx.abs > 1 || dy.abs > 1 then
                            var x = last.x
                            var y = last.y
                            for (i <- 1 until dx.abs.max(dy.abs))
                                if x != p.x then x = last.x - i * dx.sign
                                if y != p.y then y = last.y - i * dy.sign
                                gaps += CPInt2(x, y)
                    last = p
                buf ++= gaps
            else
                buf = q1 ++ q2 ++ q3 ++ q4
            buf.map(p => CPPosPixel(pxf(p.x, p.y), p.x, p.y))
        else
            mutable.ArrayBuffer.empty

    /**
      * Draws a vector with a single pixel.
      *
      * @param x X-coordinate of the start point.
      * @param y Y-coordinate of the start point.
      * @param deg Angle of vector in degrees [0..360].
      * @param len Length of the vector.
      * @param z Z-index of the drawn vector. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param px Single pixel to draw vector with.
      */
    def drawVector(x: Int, y: Int, deg: Float, len: Int, z: Int, xFactor: Float, yFactor: Float, px: CPPixel): Unit =
        drawVector(x, y, deg, len, z, xFactor, yFactor, (_, _) => px)

    /**
      * Draws art-style vector.
      *
      * Art-style drawings use characters from the ``"`'.,/\|_-`` character set (unless modified by pixel
      * producing function whenever it is available as a parameter). Art-style drawing typically provide
      * smoother gradients for curved lines. [[ART_BLOCK]] style uses only `|\/_` characters while
      * [[ART_SMOOTH]] uses all of the ``"'.`,/\|_-`` characters. [[ART_SMOOTH]] style is less performant
      * then [[ART_BLOCK]].
      *
      * The following illustrates the difference between non-art and art-style drawings using a line vector
      * example:
      * {{{
      *     non-art         ART_BLOCK        ART_SMOOTH
      *  +------------+  +-------------+  +-------------+
      *  |      ***** |  |        ____ |  |        ____ |
      *  |    **      |  |    ___/     |  |    _,-'     |
      *  | ***        |  | __/         |  | ,-'         |
      *  +------------+  +-------------+  +-------------+
      * }}}
      *
      * @param x X-coordinate of the start point.
      * @param y Y-coordinate of the start point.
      * @param deg Angle of vector in degrees [0..360].
      * @param len Length of the vector.
      * @param z Z-index of the drawn vector. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param xFactor Horizontal squishing factor. Values below 1.0 will squish the vector horizontally. Values
      *     above 1.0 will stretch the vector horizontally.
      * @param yFactor Vertical squishing factor. Values below 1.0 will squish the vector vertically. Values
      *     above 1.0 will stretch the vector vertically.
      * @param pxf Pixel producer function.
      * @param style Art-style to draw the vector with. Default is [[ART_BLOCK]].
      */
    def drawArtVector(x: Int, y: Int, deg: Float, len: Int, z: Int, xFactor: Float, yFactor: Float, pxf: CPPosPixel => CPPixel, style: ArtLineStyle = ART_BLOCK): Unit =
        drawArtLine(
            x,
            y,
            x + (len * Math.cos(deg.toRadians) * xFactor).round.toInt,
            y + (len * Math.sin(deg.toRadians) * yFactor).round.toInt,
            z,
            pxf,
            style
        )

    /**
      * Draws a vector.
      *
      * @param x X-coordinate of the start point.
      * @param y Y-coordinate of the start point.
      * @param deg Angle of vector in degrees [0..360].
      * @param len Length of the vector.
      * @param z Z-index of the drawn vector. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param xFactor Horizontal squishing factor. Values below 1.0 will squish the vector horizontally. Values
      *     above 1.0 will stretch the vector horizontally.
      * @param yFactor Vertical squishing factor. Values below 1.0 will squish the vector vertically. Values
      *     above 1.0 will stretch the vector vertically.
      * @param pxs Sequence of pixels to use for drawing.
      */
    def drawVector(x: Int, y: Int, deg: Float, len: Int, z: Int, xFactor: Float, yFactor: Float, pxs: Seq[CPPixel]): Unit =
        var i = 0
        val pxsNum = pxs.length
        drawVector(x, y, deg, len, z, xFactor, yFactor, (_, _) => {
            val px = pxs(i % pxsNum)
            i += 1
            px
        })

    /**
      * Draws a vector.
      *
      * @param x X-coordinate of the start point.
      * @param y Y-coordinate of the start point.
      * @param deg Angle of vector in degrees [0..360].
      * @param len Length of the vector.
      * @param z Z-index of the drawn vector. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param xFactor Horizontal squishing factor. Values below 1.0 will squish the vector horizontally. Values
      *     above 1.0 will stretch the vector horizontally.
      * @param yFactor Vertical squishing factor. Values below 1.0 will squish the vector vertically. Values
      *     above 1.0 will stretch the vector vertically.
      * @param pxf Pixel producer function.
      */
    def drawVector(x: Int, y: Int, deg: Float, len: Int, z: Int, xFactor: Float, yFactor: Float, pxf: (Int, Int) => CPPixel): Unit =
        drawLine(
            x,
            y,
            x + (len * Math.cos(deg.toRadians) * xFactor).round.toInt,
            y + (len * Math.sin(deg.toRadians) * yFactor).round.toInt,
            z,
            pxf
        )

    /**
      * Produces mutable array of pixels representing the vector.
      *
      * @param x X-coordinate of the start point.
      * @param y Y-coordinate of the start point.
      * @param deg Angle of vector in degrees [0..360].
      * @param len Length of the vector.
      * @param z Z-index of the drawn vector. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param xFactor Horizontal squishing factor. Values below 1.0 will squish the vector horizontally. Values
      *     above 1.0 will stretch the vector horizontally.
      * @param yFactor Vertical squishing factor. Values below 1.0 will squish the vector vertically. Values
      *     above 1.0 will stretch the vector vertically.
      * @param pxf Pixel producer function.
      */
    def vectorPixels(x: Int, y: Int, deg: Float, len: Int, z: Int, xFactor: Float, yFactor: Float, pxf: (Int, Int) => CPPixel): mutable.ArrayBuffer[CPPosPixel] =
        linePixels(
            x,
            y,
            x + (len * Math.cos(deg.toRadians) * xFactor).round.toInt,
            y + (len * Math.sin(deg.toRadians) * yFactor).round.toInt,
            z,
            pxf
        )

    /**
      * Draws a line.
      * 
      * @param ax X-coordinate of the start point.
      * @param ay Y-coordinate of the start point.
      * @param bx X-coordinate of the end point.
      * @param by Y-coordinate of the end point.
      * @param z Z-index of the drawing. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param px Pixel to use to draw the line.
      */
    def drawLine(ax: Int, ay: Int, bx: Int, by: Int, z: Int, px: CPPixel): Unit =
        drawLine(ax, ay, bx, by, z, (_, _) => px)

    /**
      * Draws the line with custom endpoints.
      *
      * @param ax X-coordinate of the start point.
      * @param ay Y-coordinate of the start point.
      * @param bx X-coordinate of the end point.
      * @param by Y-coordinate of the end point.
      * @param z Z-index of the drawing. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param startPx Start pixel.
      * @param linePx Line pixel.
      * @param endPx End pixel.
      */
    def drawLine(ax: Int, ay: Int, bx: Int, by: Int, z: Int, startPx: CPPixel, linePx: CPPixel, endPx:CPPixel): Unit =
        drawLine(ax, ay, bx, by, z, (x, y) => {
            if x == ax && y == ay then startPx
            else if x == bx && y == by then endPx
            else linePx
        })

    /**
      * Draws a line.
      *
      * @param ax X-coordinate of the start point.
      * @param ay Y-coordinate of the start point.
      * @param bx X-coordinate of the end point.
      * @param by Y-coordinate of the end point.
      * @param z Z-index of the drawing. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxs Sequence of pixels to use to draw this line.
      */
    def drawLine(ax: Int, ay: Int, bx: Int, by: Int, z: Int, pxs: Seq[CPPixel]): Unit =
        var i = 0
        val pxsNum = pxs.length
        drawLine(ax, ay, bx, by, z, (_, _) => {
            val px = pxs(i % pxsNum)
            i += 1
            px
        })

    /**
      * Draws a line.
      *
      * @param ax X-coordinate of the start point.
      * @param ay Y-coordinate of the start point.
      * @param bx X-coordinate of the end point.
      * @param by Y-coordinate of the end point.
      * @param z Z-index of the drawing. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxf Pixel producing function.
      */
    def drawLine(ax: Int, ay: Int, bx: Int, by: Int, z: Int, pxf: (Int, Int) => CPPixel): Unit =
        lineAlgo(ax, ay, bx, by, z, (x, y) => drawPixel(pxf(x, y), x, y, z))

    /**
      * Gets a mutable array of pixels representing the art line.
      *
      * Art-style drawings use characters from the ``"`'.,/\|_-`` character set (unless modified by pixel
      * producing function whenever it is available as a parameter). Art-style drawing typically provide
      * smoother gradients for curved lines. [[ART_BLOCK]] style uses only `|\/_` characters while
      * [[ART_SMOOTH]] uses all of the ``"'.`,/\|_-`` characters. [[ART_SMOOTH]] style is less performant
      * then [[ART_BLOCK]].
      *
      * The following illustrates the difference between non-art and art-style drawings using a line vector
      * example:
      * {{{
      *     non-art         ART_BLOCK        ART_SMOOTH
      *  +------------+  +-------------+  +-------------+
      *  |      ***** |  |        ____ |  |        ____ |
      *  |    **      |  |    ___/     |  |    _,-'     |
      *  | ***        |  | __/         |  | ,-'         |
      *  +------------+  +-------------+  +-------------+
      * }}}
      *
      * @param ax X-coordinate of the start point.
      * @param ay Y-coordinate of the start point.
      * @param bx X-coordinate of the end point.
      * @param by Y-coordinate of the end point.
      * @param z Z-index of the drawing. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxf Pixel producing function.
      * @param style Art style. The default value is [[ART_BLOCK]].
      */
    def artLinePixels(ax: Int, ay: Int, bx: Int, by: Int, z: Int, pxf: CPPosPixel => CPPixel, style: ArtLineStyle = ART_BLOCK): mutable.ArrayBuffer[CPPosPixel] =
        var pxs = linePixels(ax, ay, bx, by, z, (x, y) => pxf(CPPosPixel(getZPixel(x, y).px, x, y)))
        if pxs.nonEmpty then
            def newPosPx(cur: CPPosPixel, next: CPPosPixel): CPPosPixel =
                val newCh = if cur.x == next.x then '|'
                    else if cur.y == next.y then '_'
                    else if cur.x > next.x then if cur.y < next.y then '/' else '\\'
                    else if cur.y < next.y then '\\' else '/'
                cur.withChar(newCh)

            val sz = pxs.size
            if sz == 1 then
                // Use '.' as a single character "line".
                pxs(0) = pxs(0).withChar('.')
            else if by <= ay then // Going up.
                val lastIdx = sz - 1
                for (i <- 0 until lastIdx) pxs(i) = newPosPx(pxs(i), pxs(i + 1))
                pxs(lastIdx) = pxs(lastIdx).withChar(pxs(lastIdx - 1).char) // Repeat the previous to last point.
            else // by > ay // Going down.
                for (i <- 1 until sz) pxs(i) = newPosPx(pxs(i), pxs(i - 1))
                pxs(0) = pxs(0).withChar(pxs(1).char) // Repeat the second point.

            if style == ART_SMOOTH && sz >= 3 then
                val sorted = if bx == ax then by < ay else bx < ax
                pxs = pxs.sorted

                // Smooth out:
                //   __/ -> ,-'
                //   \__ -> `-.
                for (i <- 0 until sz - 3)
                    val px1 = pxs(i)
                    val px2 = pxs(i + 1)
                    val px3 = pxs(i + 2)

                    def smooth3(i: Int, ch1: Char, ch2: Char, ch3: Char): Unit =
                        pxs(i) = px1.withChar(ch1)
                        pxs(i + 1) = px2.withChar(ch2)
                        pxs(i + 2) = px3.withChar(ch3)

                    if px1.char == '_' && px2.char == '_' && px3.char == '/' then smooth3(i, ',', '-', '\'')
                    else if px1.char == '\\' && px2.char == '_' && px3.char == '_' then smooth3(i, '`', '-', '.')

                if sorted then pxs = pxs.reverse
        pxs

    /**
      * Draws an art line.
      *
      * Art-style drawings use characters from the ``"`'.,/\|_-`` character set (unless modified by pixel
      * producing function whenever it is available as a parameter). Art-style drawing typically provide
      * smoother gradients for curved lines. [[ART_BLOCK]] style uses only `|\/_` characters while
      * [[ART_SMOOTH]] uses all of the ``"'.`,/\|_-`` characters. [[ART_SMOOTH]] style is less performant
      * then [[ART_BLOCK]].
      *
      * The following illustrates the difference between non-art and art-style drawings using a line vector
      * example:
      * {{{
      *     non-art         ART_BLOCK        ART_SMOOTH
      *  +------------+  +-------------+  +-------------+
      *  |      ***** |  |        ____ |  |        ____ |
      *  |    **      |  |    ___/     |  |    _,-'     |
      *  | ***        |  | __/         |  | ,-'         |
      *  +------------+  +-------------+  +-------------+
      * }}}
      *
      * @param ax X-coordinate of the start point.
      * @param ay Y-coordinate of the start point.
      * @param bx X-coordinate of the end point.
      * @param by Y-coordinate of the end point.
      * @param z Z-index of the drawing. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxf Pixel producting function.
      * @param style Art style to use. Default value is [[ART_BLOCK]].
      */
    def drawArtLine(ax: Int, ay: Int, bx: Int, by: Int, z: Int, pxf: CPPosPixel => CPPixel, style: ArtLineStyle = ART_BLOCK): Unit =
        var pxs = artLinePixels(ax, ay, bx, by, z, pxf, style)
        if pxs.nonEmpty then
            // Actually drawing.
            pxs.foreach(ppx => drawPixel(pxf(ppx), ppx.x, ppx.y, z))

    /**
      * Gets a mutable array of pixels representing a line.
      *
      * @param ax X-coordinate of the start point.
      * @param ay Y-coordinate of the start point.
      * @param bx X-coordinate of the end point.
      * @param by Y-coordinate of the end point.
      * @param z Z-index of the drawing. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxf Pixel producing function.
      */
    def linePixels(ax: Int, ay: Int, bx: Int, by: Int, z: Int, pxf: (Int, Int) => CPPixel): mutable.ArrayBuffer[CPPosPixel] =
        val buf = new mutable.ArrayBuffer[CPPosPixel]()
        lineAlgo(ax, ay, bx, by, z, (x, y) => buf += CPPosPixel(pxf(x, y), x, y))
        buf

    /**
      *
      * @param ax X-coordinate of the start point.
      * @param ay Y-coordinate of the start point.
      * @param bx X-coordinate of the end point.
      * @param by Y-coordinate of the end point.
      * @param z Z-index of the drawing. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxf
      */
    private def lineAlgo(ax: Int, ay: Int, bx: Int, by: Int, z: Int, pxf: (Int, Int) => Unit): Unit =
        val xD = bx - ax
        val yD = by - ay
        val maxD = xD.abs.max(yD.abs).toFloat
        val stepX = if xD == 0 then 0 else xD / maxD
        val stepY = if yD == 0 then 0 else yD / maxD

        var x = ax
        var y = ay
        var fx = ax.toFloat
        var fy = ay.toFloat

        while (x != bx || y != by)
            pxf(x, y)
            fx += stepX
            fy += stepY
            x = fx.round
            y = fy.round

        pxf(x, y)

    /**
      * Draws given image.
      *
      * @param img Image to draw.
      * @param x X-coordinate of the top left corner.
      * @param y Y-coordinate of the top left corner.
      * @param z Z-index to draw the given image at. Pixel with the larger or equal Z-index overrides the 
      *     pixel with the smaller one.
      */
    def drawImage(img: CPImage, x: Int, y: Int, z: Int): Unit =
        new CPRect(x, y, img.getDim).intersectWith(clip).loop((a, b) =>
            drawPixel(img.getPixel(a - x, b - y), a, b, z)
        )

    /**
      * Draws a single pixel.
      * 
      * @param px Pixel to draw.
      * @param x X-coordinate of the pixel.
      * @param y Y-coordinate of the pixel.
      * @param z Z-index for this pixel. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      */
    def drawPixel(px: CPPixel, x: Int, y: Int, z: Int): Unit =
        if !px.isXray && clip.contains(x, y) then
            val currZpx = pane.getPixel(x, y)
            if z >= currZpx.z then
                val normPx = if px.bg.isEmpty then px.withBg(currZpx.bg.orElse(pane.getBgPixel.bg)) else px
                pane.addPixel(normPx, x, y, z)

    /**
      * Draws a single pixel.
      *
      * @param px Pixel to draw.
      * @param xy Xy-coordinate tuple of the pixel.
      * @param z Z-index for this pixel. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      */
    def drawPixel(px: CPPixel, xy: (Int, Int), z: Int): Unit =
        drawPixel(px, xy._1, xy._2, z)

    /**
      * Draws a pixel.
      *
      * @param char Pixel character.
      * @param fg Pixel foreground color.
      * @param bg Pixel optional background color. Default value is `None`.
      * @param x X-coordinate of the pixel.
      * @param y Y-coordinate of the pixel.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one. 
      */
    def drawPixel(char: Char, fg: CPColor, bg: Option[CPColor] = None, x: Int, y: Int, z: Int): Unit =
        drawPixel(CPPixel(char, fg, bg), x, y, z)

    /**
      * Gets a pixel from given XY-coordinate.
      *
      * @param x X-coordinate.
      * @param y Y-coordinate.
      * @return Pixel at given coordinate.
      */
    def getZPixel(x: Int, y: Int): CPZPixel = pane.getPixel(x, y)

    /**
      * Draws a rectangle.
      * 
      * @param x1 X-coordinate of the left top corner.
      * @param y1 Y-coordinate of the left top corner.
      * @param x2 X-coordinate of the right bottom corner.
      * @param y2 Y-coordinate of the right bottom corner.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param px Pixel to use for drawing.
      */
    def drawRect(x1: Int, y1: Int, x2: Int, y2: Int, z: Int, px: CPPixel): Unit =
        drawRect(x1, y1, x2, y2, z, (_, _) => px)

    /**
      * Draws a rectangle.
      *
      * @param x1 X-coordinate of the left top corner.
      * @param y1 Y-coordinate of the left top corner.
      * @param dim Dimension of the rectangle.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param px Pixel to use for drawing.
      */
    def drawRect(x1: Int, y1: Int, dim: CPDim, z: Int, px: CPPixel): Unit =
        drawRect(x1, y1, x1 + dim.w - 1, y1 + dim.h - 1, z, (_, _) => px)

    /**
      * Draws a rectangle.
      *
      * @param rect Shape of rectangle to draw.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param px Pixel to draw the rectangle with.
      */
    def drawRect(rect: CPRect, z: Int, px: CPPixel): Unit =
        drawRect(rect.xMin, rect.yMin, rect.xMax, rect.yMax, z, (_, _) => px)

    /**
      * Draws a rectangle.
      *
      * @param x1 X-coordinate of the left top corner.
      * @param y1 Y-coordinate of the left top corner.
      * @param x2 X-coordinate of the right bottom corner.
      * @param y2 Y-coordinate of the right bottom corner.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxf Pixel producing function.
      */
    def drawRect(x1: Int, y1: Int, x2: Int, y2: Int, z: Int, pxf: (Int, Int) => CPPixel): Unit =
        drawPolyline(Seq(
            x1 -> y1,
            x2 -> y1,
            x2 -> y2,
            x1 -> y2,
            x1 -> y1
        ), z, pxf)

    /**
      * Draws a rectangle.
      *
      * @param x1 X-coordinate of the top left corner.
      * @param y1 Y-coordinate of the top left corner.
      * @param dim Dimension of the rectangle.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxf Pixel producing function.
      */
    def drawRect(x1: Int, y1: Int, dim: CPDim, z: Int, pxf: (Int, Int) => CPPixel): Unit =
        drawRect(x1, y1, x1 + dim.w - 1, y1 + dim.h - 1, z, pxf)

    /**
      * Draws rectangle.
      *
      * @param rect Rectangle shape to draw.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxf Pixel producing function.
      */
    def drawRect(rect: CPRect, z: Int, pxf: (Int, Int) => CPPixel): Unit =
        drawRect(rect.xMin, rect.yMin, rect.xMax, rect.yMax, z, pxf)

    /**
      * Draws rectangle with speific pixels for lines and corners.
      *
      * @param x1 X-coordinate of the left top corner.
      * @param y1 Y-coordinate of the left top corner.
      * @param x2 X-coordinate of the right bottom corner.
      * @param y2 Y-coordinate of the right bottom corner.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param leftTop Left top corner pixel.
      * @param top Top line pixel.
      * @param leftBottom Left bottom corner pixel.
      * @param left Left side pixel.
      * @param rightBottom Right bottom corner pixel.
      * @param bottom Bottom size pixel.
      * @param rightTop Right top corner pixel.
      * @param right Right side line pixel.
      */
    def drawRect(
        x1: Int,
        y1: Int,
        x2: Int,
        y2: Int,
        z: Int,
        leftTop: CPPixel,
        top: CPPixel,
        leftBottom: CPPixel,
        left: CPPixel,
        rightBottom: CPPixel,
        bottom: CPPixel,
        rightTop: CPPixel,
        right: CPPixel
    ): Unit =
        drawPolyline(Seq(
            x1 -> y1,
            x2 -> y1,
            x2 -> y2,
            x1 -> y2,
            x1 -> y1
        ),z, (a, b) =>
            if a == x1 && b == y1 then leftTop
            else if a == x1 && b == y2 then rightTop
            else if a == x2 && b == y1 then leftBottom
            else if a == x2 && b == y2 then rightBottom
            else if a == x1 then left
            else if a == x2 then right
            else if b == y1 then top
            else  bottom
        )

    /**
      * Draws rectangle with speific pixels for lines and corners.
      *
      * @param x1 X-coordinate of the top left corner.
      * @param y1 Y-coordinate of the top left corner.
      * @param dim Dimension of the rectangle.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param leftTop Left top corner pixel.
      * @param top Top line pixel.
      * @param leftBottom Left bottom corner pixel.
      * @param left Left side pixel.
      * @param rightBottom Right bottom corner pixel.
      * @param bottom Bottom size pixel.
      * @param rightTop Right top corner pixel.
      * @param right Right side line pixel.
      */
    def drawRect(
        x1: Int,
        y1: Int,
        dim: CPDim,
        z: Int,
        leftTop: CPPixel,
        top: CPPixel,
        leftBottom: CPPixel,
        left: CPPixel,
        rightBottom: CPPixel,
        bottom: CPPixel,
        rightTop: CPPixel,
        right: CPPixel
    ): Unit =
        drawRect(x1, y1, x1 + dim.w - 1, y1 + dim.h - 1, z, leftTop, top, leftBottom, left, rightBottom, bottom, rightTop, right)

    /**
      * Draws rectangle with speific pixels for lines and corners.
      *
      * @param rect Rectangle shape.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param leftTop Left top corner pixel.
      * @param top Top line pixel.
      * @param leftBottom Left bottom corner pixel.
      * @param left Left side pixel.
      * @param rightBottom Right bottom corner pixel.
      * @param bottom Bottom size pixel.
      * @param rightTop Right top corner pixel.
      * @param right Right side line pixel.
      */
    def drawRect(
        rect: CPRect,
        z: Int,
        leftTop: CPPixel,
        top: CPPixel,
        leftBottom: CPPixel,
        left: CPPixel,
        rightBottom: CPPixel,
        bottom: CPPixel,
        rightTop: CPPixel,
        right: CPPixel
    ): Unit =
        drawRect(rect.xMin, rect.yMin, rect.xMax, rect.yMax, z, leftTop, top, leftBottom, left, rightBottom, bottom, rightTop, right)

    /**
      * Draws a polyline.
      *
      * @param pts Points of polyline.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param px Pixel to use to draw a polyline with.
      */
    def drawPolyline(pts: Seq[(Int, Int)], z: Int, px: CPPixel): Unit =
        drawPolyline(pts, z, (_, _) => px)

    /**
      * Draws a polyline.
      *
      * @param pts Points of polyline.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxs Pixel producing function.
      */
    def drawPolyline(pts: Seq[(Int, Int)], z: Int, pxs: Seq[CPPixel]): Unit =
        val pxsNum = pxs.size
        var i = 0
        drawPolyline(pts, z, (_, _) => {
            val px = pxs(i % pxsNum)
            i += 1
            px
        })

    /**
      * Fills in bordered area with a pixel. This is also known as "bucket tool".
      *
      * @param x X-coordinate of the start point.
      * @param y Y-coordinate of the start point.
      * @param isBorder Border predicate.
      * @param pxf Fill in pixel producer.
      */
    def fill(x: Int, y: Int, isBorder: CPZPixel => Boolean, pxf: (Int, Int) => CPPixel): Unit =
        var seen: List[(Int, Int)] = Nil
        var stack = List(x -> y)

        while (stack.nonEmpty)
            val (a, b) = stack.head
            stack = stack.tail
            seen ::= a -> b
            val px = getZPixel(a, b)
            if !isBorder(px) then
                drawPixel(pxf(a, b), a, b, px.z)
                if a > 0 && !seen.contains((a - 1) -> b) then stack ::= (a - 1, b)
                if a < xMax && !seen.contains((a + 1) -> b) then stack ::= (a + 1, b)
                if b > 0 && !seen.contains(a -> (b - 1)) then stack ::= (a, b - 1)
                if b < yMax && !seen.contains(a -> (b + 1)) then stack ::= (a, b + 1)
    /**
      * Copies one rectangular areas in this canvas to another location in the same canvas.
      *
      * @param x1 X-coordinate of the top left corner of the area to copy.
      * @param y1 Y-coordinate of the top left corner of the area to copy.
      * @param x2 X-coordinate of the bottom right corner of the area to copy.
      * @param y2 Y-coordinate of the bottom right corner of the area to copy.
      * @param destX New X-coordinate of the top left corner.
      * @param destY New Y-coordinate of the top left corner.
      */
    def copyRect(x1: Int, y1: Int, x2: Int, y2: Int, destX: Int, destY: Int): Unit =
        val rect = new CPRect(x1 -> y1, x2 -> y2)
        val dx = rect.x - destX
        val dy = rect.y - destY
        rect.loop((a, b) => {
            val zpx = getZPixel(a, b)
            drawPixel(zpx.px, a - dx, b - dx, zpx.z)
        })

    /**
      * Copies one rectangular areas in this canvas to another location in the same canvas.
      *
      * @param x1 X-coordinate of the top left corner of the area to copy.
      * @param y1 Y-coordinate of the top left corner of the area to copy.
      * @param dim Dimension of the area to copy.
      * @param destX New X-coordinate of the top left corner.
      * @param destY New Y-coordinate of the top left corner.
      */
    def copyRect(x1: Int, y1: Int, dim: CPDim, destX: Int, destY: Int): Unit =
        copyRect(x1, y1, x1 + dim.w - 1, y1 + dim.h - 1, destX, destY)

    /**
      * Copies one rectangular areas in this canvas to another location in the same canvas.
      *
      * @param rect Rectangular shape to copy.
      * @param destX New X-coordinate of the top left corner.
      * @param destY New Y-coordinate of the top left corner.
      */
    def copyRect(rect: CPRect, destX: Int, destY: Int): Unit =
        copyRect(rect.xMin, rect.yMin, rect.xMax, rect.yMax, destX, destY)

    /**
      * Draws a polyline.
      *
      * @param pts Points of the polyline.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxf Pixel producing function.
      */
    def drawPolyline(pts: Seq[(Int, Int)], z: Int, pxf: (Int, Int) => CPPixel): Unit =
        require(pts.length >= 2, "Polyline must have at 2 or more points.")

        val max = pts.length - 1
        var i = 0
        while (i < max)
            val pt1 = pts(i)
            val pt2 = pts(i + 1)
            drawLine(pt1._1, pt1._2, pt2._1, pt2._2, z, pxf)
            i += 1

    /**
      * Gets a mutable array of pixels representing a polyline.
      *
      * @param pts Points of the polyline.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxf Pixel producing function.
      */
    def polylinePixels(pts: Seq[(Int, Int)], z: Int, pxf: (Int, Int) => CPPixel): mutable.ArrayBuffer[CPPosPixel] =
        require(pts.length >= 2, "Polyline must have at 2 or more points.")

        val buf = new mutable.ArrayBuffer[CPPosPixel]()
        val max = pts.length - 1
        var i = 0
        while (i < max)
            val pt1 = pts(i)
            val pt2 = pts(i + 1)
            buf ++= linePixels(pt1._1, pt1._2, pt2._1, pt2._2, z, pxf)
            i += 1
        buf

    /**
      * Draws an art-style polyline.
      *
      * Art-style drawings use characters from the ``"`'.,/\|_-`` character set (unless modified by pixel
      * producing function whenever it is available as a parameter). Art-style drawing typically provide
      * smoother gradients for curved lines. [[ART_BLOCK]] style uses only `|\/_` characters while
      * [[ART_SMOOTH]] uses all of the ``"'.`,/\|_-`` characters. [[ART_SMOOTH]] style is less performant
      * then [[ART_BLOCK]].
      *
      * The following illustrates the difference between non-art and art-style drawings using a line vector
      * example:
      * {{{
      *     non-art         ART_BLOCK        ART_SMOOTH
      *  +------------+  +-------------+  +-------------+
      *  |      ****  |  |        ___  |  |        ___  |
      *  |    **      |  |    ___/     |  |    _,-'     |
      *  | ***        |  | __/         |  | ,-'         |
      *  +------------+  +-------------+  +-------------+
      * }}}
      *
      * @param pts Points of the polyline.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxf Pixel producing function.
      * @param style Art style to use. Default value is [[ART_BLOCK]].
      */
    def drawArtPolyline(pts: Seq[(Int, Int)], z: Int, pxf: CPPosPixel => CPPixel, style: ArtLineStyle = ART_BLOCK): Unit =
        require(pts.length >= 2, "Polyline must have at 2 or more points.")

        for (i <- 0 until pts.length - 1)
            val pt1 = pts(i)
            val pt2 = pts(i + 1)
            drawArtLine(pt1._1, pt1._2, pt2._1, pt2._2, z, pxf, style)

    /**
      * Fills the rectangular shape with given pixel.
      *
      * @param x1 X-coordinate of the top left corner of the shape.
      * @param y1 Y-coordinate of the top left corner of the shape.
      * @param x2 X-coordinate of the bottom right corner of the shape.
      * @param y2 Y-coordinate of the bottom right corner of the shape.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param px Pixel to fill in wigth.
      */
    def fillRect(x1: Int, y1: Int, x2: Int, y2: Int, z: Int, px: CPPixel): Unit =
        fillRect(x1, y1, x2, y2, z, (_, _) => px)

    /**
      * Fills the rectangular shape with given pixel.
      *
      * @param x1 X-coordinate of the top left corner of the shape.
      * @param y1 Y-coordinate of the top left corner of the shape.
      * @param dim Dimension of the rectangle to fill in.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param px Pixel to fill in with.
      */
    def fillRect(x1: Int, y1: Int, dim: CPDim, z: Int, px: CPPixel): Unit =
        fillRect(x1, y1, x1 + dim.w - 1, y1 + dim.h - 1, z, (_, _) => px)

    /**
      * Fills the rectangular shape.
      *
      * @param x1 X-coordinate of the top left corner of the shape.
      * @param y1 Y-coordinate of the top left corner of the shape.
      * @param dim Dimension of the rectangle to fill in.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxf Fill in pixel producing function.
      */
    def fillRect(x1: Int, y1: Int, dim: CPDim, z: Int, pxf: (Int, Int) => CPPixel): Unit =
        fillRect(x1, y1, x1 + dim.w - 1, y1 + dim.h - 1, z, pxf)

    /**
      * Fills the rectangular shape.
      *
      * @param x1 X-coordinate of the top left corner of the shape.
      * @param y1 Y-coordinate of the top left corner of the shape.
      * @param dim Dimension of the rectangle to fill in.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxs Pixels to use for filling in.
      */
    def fillRect(x1: Int, y1: Int, dim: CPDim, z: Int, pxs: Seq[CPPixel]): Unit =
        fillRect(x1, y1, x1 + dim.w - 1, y1 + dim.h - 1, z, pxs)

    /**
      * Fills the rectangular shape.
      *
      * @param rect Rectangle shape to fill in.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param px Pixel to use for filling in.
      */
    def fillRect(rect: CPRect, z: Int, px: CPPixel): Unit =
        fillRect(rect, z, (_, _) => px)

    /**
      *
      * Fills the rectangular shape.
      *
      * @param x1 X-coordinate of the top left corner of the shape.
      * @param y1 Y-coordinate of the top left corner of the shape.
      * @param x2 X-coordinate of the bottom right corner of the shape.
      * @param y2 Y-coordinate of the bottom right corner of the shape.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxf Fill in pixel producing function.
      */
    def fillRect(x1: Int, y1: Int, x2: Int, y2: Int, z: Int, pxf: (Int, Int) => CPPixel): Unit =
        fillRect(new CPRect(x1 -> y1, x2 -> y2), z, pxf)

    /**
      * Fills the rectangular shape.
      *
      * @param rect Rectangular shape to fill in.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxf Fill in pixel producing function.
      */
    def fillRect(rect: CPRect, z: Int, pxf: (Int, Int) => CPPixel): Unit =
        rect.loop((x, y) => drawPixel(pxf(x, y), x, y, z))

    /**
      * Fills the rectangular shape.
      *
      * @param x1 X-coordinate of the top left corner of the shape.
      * @param y1 Y-coordinate of the top left corner of the shape.
      * @param x2 X-coordinate of the bottom right corner of the shape.
      * @param y2 Y-coordinate of the bottom right corner of the shape.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxs Pixels to use for filling in.
      */
    def fillRect(x1: Int, y1: Int, x2: Int, y2: Int, z: Int, pxs: Seq[CPPixel]): Unit =
        fillRect(new CPRect(x1 -> y1, x2 -> y2), z, pxs)

    /**
      * Fills the rectangular shape.
      * @param rect Rectangular shape to fill in.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxs Pixels to use for filling in.
      */
    def fillRect(rect: CPRect, z: Int, pxs: Seq[CPPixel]): Unit =
        var i = 0
        val pxsNum = pxs.length

        rect.loop((x, y) => {
            drawPixel(pxs(i % pxsNum), x, y, z)
            i += 1
        })

    /**
      * Captures given rectangular canvas region as an image. This method will capture only
      * "visible" image of the given region.
      *
      * @param x1 X-coordinate of the top left corner of the region.
      * @param y1 Y-coordinate of the top left corner of the region.
      * @param x2 X-coordinate of the bottom right corner of the region.
      * @param y2 Y-coordinate of the bottom right corner of the region.
      */
    def capture(x1: Int, y1: Int, x2: Int, y2: Int): CPImage =
        captureRect(x1, y1, x2, y2, _.px)

    /**
      * Captures given rectangular canvas region as an image. This method will capture only
      * "visible" image of the given region.
      *
      * @param x1 X-coordinate of the top left corner of the region.
      * @param y1 Y-coordinate of the top left corner of the region.
      */
    def capture(x1: Int, y1: Int, dim: CPDim): CPImage =
        captureRect(x1, y1, x1 + dim.w - 1, y1 + dim.h - 1, _.px)

    /**
      * Captures given rectangular canvas region as an image. This method will capture only
      * "visible" image of the given region.
      *
      * @param rect Rectangular region to capture.
      */
    def capture(rect: CPRect): CPImage =
        captureRect(rect.xMin, rect.yMin, rect.xMax, rect.yMax, _.px)

    /**
      * Captures entire canvas as an image. This method will capture only
      * "visible" image of the given region.
      */
    def capture(): CPImage = captureRect(xMin, yMin, xMax, yMax, _.px)

    /**
      * Captures given rectangular canvas region as an image. This method will capture only specified
      * Z-index layers replacing pixels on other layers with [[CPPixel.XRAY]] in the resulting image.
      *
      * @param x1 X-coordinate of the top left corner of the region.
      * @param y1 Y-coordinate of the top left corner of the region.
      * @param x2 X-coordinate of the bottom right corner of the region.
      * @param y2 Y-coordinate of the bottom right corner of the region.
      * @param zdx Z-indexes to capture.
      */
    def capture(x1: Int, y1: Int, x2: Int, y2: Int, zdx: Int*): CPImage =
        captureRect(x1, y1, x2, y2, zpx => if zdx.contains(zpx.z) then zpx.px else CPPixel.XRAY)

    /**
      * Captures given rectangular canvas region as an image. This method will capture only specified
      * z-index layers replacing pixels on other layers with [[CPPixel.XRAY]] in the resulting image.
      *
      * @param x1 X-coordinate of the top left corner of the region.
      * @param y1 Y-coordinate of the top left corner of the region.
      * @param dim Dimension of the region to capture.
      * @param zdx Z-indexes to capture.
      */
    def capture(x1: Int, y1: Int, dim: CPDim, zdx: Int*): CPImage =
        captureRect(x1, y1, x1 + dim.w - 1, y1 + dim.h - 1, zpx => if zdx.contains(zpx.z) then zpx.px else CPPixel.XRAY)

    /**
      * Captures given rectangular canvas region as an image. This method will capture only specified
      * z-index layers replacing pixels on other layers with [[CPPixel.XRAY]] in the resulting image.
      *
      * @param rect Rectangular region to capture.
      * @param zdx Z-indexes to capture.
      */
    def capture(rect: CPRect, zdx: Int*): CPImage =
        captureRect(rect.xMin, rect.yMin, rect.xMax, rect.yMax, zpx => if zdx.contains(zpx.z) then zpx.px else CPPixel.XRAY)

    /**
      * Captures given rectangular canvas region as an image.
      *
      * @param x1 X-coordinate of the top left corner of the region.
      * @param y1 Y-coordinate of the top left corner of the region.
      * @param x2 X-coordinate of the bottom right corner of the region.
      * @param y2 Y-coordinate of the bottom right corner of the region.
      * @param f Pixel transforming function.
      */
    private def captureRect(x1: Int, y1: Int, x2: Int, y2: Int, f: CPZPixel => CPPixel): CPImage =
        val rect = new CPRect(x1 -> y1, x2 -> y2)
        val arr = new CPArray2D[CPPixel](rect.dim)
        rect.loop((x, y) => arr.set(x - rect.x, y - rect.y, f(pane.getPixel(x, y))))
        CPArrayImage(arr, "code")

    /**
      * Draws given string using [[CPSystemFont system font]].
      *
      * @param x X-coordinate of the first character.
      * @param y Y-coordinate of the first character.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param str String to draw.
      * @param fg Foreground color.
      * @param bg Optional background color. Default value is `NOne`.
      */
    def drawString(x: Int, y: Int, z: Int, str: String, fg: CPColor, bg: Option[CPColor] = None): Unit =
        var i = 0
        val len = str.length
        while (i < len)
            drawPixel(CPPixel(str.charAt(i), fg, bg), x + i, y, z)
            i += 1

    /**
      * Draws given [[CPStyledString styled string]].
      *
      * @param x X-coordinate of the first character.
      * @param y Y-coordinate of the first character.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param ss Styled string to draw.
      */
    def drawStyledString(x: Int, y: Int, z: Int, ss: CPStyledString): Unit =
        drawPixels(x, y, z, ss.build())

    /**
      * Draws pixels.
      *
      * @param x X-coordinate of the start point.
      * @param y Y-coordinate of the start point.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param len Number of pixel to draw.
      * @param pxf Pixel producing function.
      */
    def drawPixels(x: Int, y: Int, z: Int, len: Int, pxf: (Int, Int) => CPPixel): Unit =
        var i = 0
        var a = x
        while (i < len)
            drawPixel(pxf(a, y), a, y, z)
            i += 1
            a += 1

    /**
      * Draws pixels.
      *
      * @param x X-coordinate of the start point.
      * @param y Y-coordinate of the start point.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param pxs Pixels to draw.
      */
    def drawPixels(x: Int, y: Int, z: Int, pxs: Seq[CPPixel]): Unit =
        drawPixels(x, y, z, pxs.size, (a, _) => pxs(a - x))

    /**
      * Draws a border.
      *
      * @param x1 X-coordinate of the top left corner.
      * @param y1 Y-coordinate of the top left corner.
      * @param x2 X-coordinate of the bottom right corner.
      * @param y2 Y-coordinate of the bottom right corner.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param leftTop Left top corner pixel.
      * @param top Top line pixel.
      * @param leftBottom Left bottom corner pixel.
      * @param left Left side pixel.
      * @param rightBottom Right bottom corner pixel.
      * @param bottom Bottom size pixel.
      * @param rightTop Right top corner pixel.
      * @param right Right side line pixel.
      * @param title Title of the border.
      * @param titleX X-coordinate of the title.
      * @param titleY Y-coordinate of the title.
      */
    def drawBorder(
        x1: Int,
        y1: Int,
        x2: Int,
        y2: Int,
        z: Int,
        top: CPPixel,
        leftTop: CPPixel,
        left: CPPixel,
        leftBottom: CPPixel,
        bottom: CPPixel,
        rightBottom: CPPixel,
        right: CPPixel,
        rightTop: CPPixel,
        title: Seq[CPPixel] = Seq.empty,
        titleX: Int = -1,
        titleY: Int = -1
    ): Unit =
        if title.nonEmpty && (titleX <= 0 || titleY <=0) then E("Title coordinate must be supplied and >= 0.")
        drawRect(x1, y1, x2, y2, z, leftTop, top, leftBottom, left, rightBottom, bottom, rightTop, right)
        if title.nonEmpty then drawPixels(titleX, titleY, z, title)

    /**
      * Draws a border.
      *
      * @param x1 X-coordinate of the top left corner.
      * @param y1 Y-coordinate of the top left corner.
      * @param x2 X-coordinate of the bottom right corner.
      * @param y2 Y-coordinate of the bottom right corner.
      * @param z Z-index. Pixel with the larger or equal Z-index overrides the pixel with the smaller one.
      * @param ver Pixel for vertical lines.
      * @param hor Pixel for horizontal lines.
      * @param corner Pixel for corners,
      * @param title Title of the border.
      * @param titleX X-coordinate of the title.
      * @param titleY Y-coordinate of the title.
      */
    def drawSimpleBorder(
        x1: Int,
        y1: Int,
        x2: Int,
        y2: Int,
        z: Int,
        ver: CPPixel,
        hor: CPPixel,
        corner: CPPixel,
        title: Seq[CPPixel] = Seq.empty,
        titleX: Int = -1,
        titleY: Int = -1
    ): Unit =
        drawBorder(x1, y1, x2, y2, z, hor, corner, ver, corner, hor, corner, ver, corner, title, titleX, titleY)

/**
  * Compantion object with utility functions.
  */
object CPCanvas:
    /**
      * Supported styles for the following methods:
      *  - [[CPCanvas.artLinePixels()]]
      *  - [[CPCanvas.drawArtLine()]]
      *  - [[CPCanvas.drawArtPolyline()]]
      *  - [[CPCanvas.drawArtVector()]]
      */
    enum ArtLineStyle:
        /**
          * Blocky, non-smoother style:
          * {{{
          *     ART_BLOCK
          *  +-------------+
          *  |        ____ |
          *  |    ___/     |
          *  | __/         |
          *  +-------------+
          * }}}
          */
        case ART_BLOCK

        /**
          * Antialiazed, smoothed out style:
          * {{{
          *     ART_SMOOTH
          *  +-------------+
          *  |        ____ |
          *  |    _,-'     |
          *  | ,-'         |
          *  +-------------+
          * }}}
          */
        case ART_SMOOTH

    /**
      * Creates new canvas with given dimensions and background pixel.
      *
      * @param w Width of the canvas.
      * @param h Height of the canvas.
      * @param bgPx Background pixel.
      */
    def apply(w: Int, h: Int, bgPx: CPPixel): CPCanvas = apply(CPDim(w, h), bgPx)

    /**
      * Creates new canvas with given dimension and background pixel.
      *
      * @param dim Dimension of the canvas.
      * @param bgPx Background pixel.
      */
    def apply(dim: CPDim, bgPx: CPPixel): CPCanvas =
        new CPCanvas(
            CPZPixelPane(
                new CPArray2D[CPZPixel](
                    dim,
                    CPZPixel(bgPx, Int.MinValue)
                ),
                bgPx
            ),
            new CPRect(0, 0, dim)
        )
