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

import java.io.*
import CPAsciiTable.*

import scala.annotation.targetName
import scala.collection.mutable
import scala.util.Using

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
  * ASCII-based table with minimal styling support. Can be used for structured logging and
  * provides output like this:
  * <pre>
  * +------------------------------------------------------+
  * |   Name        |               Value                  |
  * +===============+======================================+
  * | Game ID       | 52050358-d7a1-4def-b53f-db2bee8aea33 |
  * | Game name     | My Cool Game                         |
  * | Game URL      | 'null'                               |
  * | Description   | 'null'                               |
  * +------------------------------------------------------+
  * </pre>
  */
//noinspection ScalaWeakerAccess
class CPAsciiTable:
    /**
      * Cell style.
      */
    private sealed case class Style(
        var leftPad: Int = 1, // >= 0
        var rightPad: Int = 1, // >= 0
        var maxWidth: Int = Int.MaxValue, // > 0
        var align: String = "center" // center, left, right
    ):
        /** Gets overall padding (left + right). */
        def padding: Int = leftPad + rightPad

    /**
      * Cell style.
      */
    private object Style:
        /**
          *
          * @param sty Style text.
          */
        def apply(sty: String): Style =
            val cs = new Style

            if sty.nonEmpty then
                for e <- sty.split(',') do
                    val a = e.split(":")
                    require(a.length == 2, s"Invalid cell style: ${e.trim}")
                    val a0 = a(0).trim
                    val a1 = a(1).trim

                    a0 match
                        case "leftPad" => cs.leftPad = a1.toInt
                        case "rightPad" => cs.rightPad = a1.toInt
                        case "maxWidth" => cs.maxWidth = a1.toInt
                        case "align" => cs.align = a1.toLowerCase
                        case _ => assert(assertion = false, s"Invalid style: ${e.trim}")

            require(cs.leftPad >= 0, "Style 'leftPad' must >= 0.")
            require(cs.rightPad >= 0, "Style 'rightPad' must >= 0.")
            require(cs.maxWidth > 0, "Style 'maxWidth' must > 0.")
            require(cs.align == "center" || cs.align == "left" || cs.align == "right", "Style 'align' must be 'left', 'right' or 'center'.")

            cs

    /**
      * Cell holder.
      *
      * @param style
      * @param lines Lines that are already cut up per `style`, if required.
      */
    private sealed case class Cell(style: Style, lines: Seq[String]):
        // Cell's calculated width including padding.
        lazy val width: Int = style.padding + (if (height > 0) lines.map(_.length).max else 0)
        // Gets height of the cell.
        lazy val height: Int = lines.length

    /**
      * Margin holder.
      */
    private sealed case class Margin(
        top: Int = 0,
        right: Int = 0,
        bottom: Int = 0,
        left: Int = 0
    )

    // Table drawing symbols.
    private val HDR_HOR = "="
    private val HDR_VER = "|"
    private val HDR_CRS = "+"
    private val ROW_HOR = "-"
    private val ROW_VER = "|"
    private val ROW_CRS = "+"
    // Headers & rows.
    private var hdr = IndexedSeq.empty[Cell]
    private var rows = IndexedSeq.empty[IndexedSeq[Cell]]
    // Current row, if any.
    private var curRow: IndexedSeq[Cell] = _
    // Table's margin, if any.
    private var margin = Margin()

    /**
      * Global flag indicating whether or not to draw inside horizontal lines
      * between individual rows.
      */
    private val insideBorder = false
    /**
      * Global Flag indicating whether of not to automatically draw horizontal lines
      * for multiline rows.
      */
    private val multiLineAutoBorder = true
    /**
      * If lines exceeds the style's maximum width it will be broken up
      * either by nearest space (by whole words) or mid-word.
      */
    private val breakUpByWords = true

    private def dash(ch: String, len: Int): String = ch * len
    private def space(len: Int): String = " " * len

    /**
      * Sets table's margin.
      *
      * @param top Top margin.
      * @param right Right margin.
      * @param bottom Bottom margin.
      * @param left Left margin.
      */
    def margin(top: Int = 0, right: Int = 0, bottom: Int = 0, left: Int = 0): CPAsciiTable =
        margin = Margin(top, right, bottom, left)
        this

    /**
      * Starts data row.
      */
    def startRow(): Unit =
        curRow = IndexedSeq.empty[Cell]

    /**
      * Ends data row.
      */
    def endRow(): Unit =
        rows :+= curRow
        curRow = null

    /**
      * Adds row (one or more row cells).
      *
      * @param cells Row cells. For multi-line cells - use `Seq(...)`.
      */
    @targetName("plusEqualAny")
    def +=(cells: Any*): CPAsciiTable =
        startRow()
        cells foreach {
            case i: Iterable[_] => addRowCell(i.iterator.toSeq: _*)
            case a => addRowCell(a)
        }
        endRow()
        this

    /**
      * Adds row (one or more row cells).
      *
      * @param cells Row cells. For multi-line cells - use `Seq(...)`.
      */
    @targetName("plusEqualSeq")
    def +=(cells: mutable.Seq[Any]): CPAsciiTable =
        +=(cells.toSeq: _*)

    /**
      * Adds row (one or more row cells) with a given style.
      *
      * @param cells Row cells tuples (style, text). For multi-line cells - use `Seq(...)`.
      */
    @targetName("plusSlashAny")
    def +/(cells: (String, Any)*): CPAsciiTable =
        startRow()
        cells foreach {
            case i if i._2.isInstanceOf[Iterable[_]] => addStyledRowCell(i._1, i._2.asInstanceOf[Iterable[_]].iterator.toSeq: _*)
            case a => addStyledRowCell(a._1, a._2)
        }
        endRow()
        this

    /**
      * Adds row.
      *
      * @param cells Row cells.
      */
    def addRow(cells: List[Any]): CPAsciiTable =
        startRow()
        cells.foreach(p => addRowCell(p))
        endRow()
        this

    /**
      * Adds header (one or more header cells).
      *
      * @param cells Header cells. For multi-line cells - use `Seq(...)`.
      */
    @targetName("poundEqualAny")
    def #=(cells: Any*): CPAsciiTable =
        cells foreach {
            case i: Iterable[_] => addHeaderCell(i.iterator.toSeq: _*)
            case a => addHeaderCell(a)
        }
        this

    /**
      * Adds header (one or more header cells).
      *
      * @param cells Header cells. For multi-line cells - use `Seq(...)`.
      */
    @targetName("poundEqualSeq")
    def #=(cells: mutable.Seq[Any]): CPAsciiTable =
        #=(cells.toSeq: _*)

    /**
      * Adds styled header (one or more header cells).
      *
      * @param cells Header cells tuples (style, text). For multi-line cells - use `Seq(...)`.
      */
    @targetName("poundSlashAny")
    def #/(cells: (String, Any)*): CPAsciiTable =
        cells foreach {
            case i if i._2.isInstanceOf[Iterable[_]] => addStyledHeaderCell(i._1, i._2.asInstanceOf[Iterable[_]].iterator.toSeq: _*)
            case a => addStyledHeaderCell(a._1, a._2)
        }
        this

    /**
      * Adds headers.
      *
      * @param cells Header cells.
      */
    def addHeaders(cells: List[Any]): CPAsciiTable =
        cells.foreach(addHeaderCell(_))
        this

    /**
      * Adds headers with the given `style`.
      *
      * @param style Style top use.
      * @param cells Header cells.
      */
    def addStyledHeaders(style: String, cells: List[Any]): CPAsciiTable =
        cells.foreach(addHeaderCell(style, _))
        this

    // Handles the 'null' strings.
    private def x(s: Any): String = s match
        case null => "<null>"
        case _ => s.toString

    /**
      *
      * @param maxWidth
      * @param lines
      */
    private def breakUpByNearestSpace(maxWidth: Int, lines: Seq[String]): Seq[String] =
        lines.flatMap(line => {
            if line.isEmpty then
                mutable.Buffer("")
            else
                val leader = line.indexWhere(_ != ' ') // Number of leading spaces.

                val buf = mutable.Buffer.empty[String]

                var start = 0
                var lastSpace = -1
                var curr = 0
                val len = line.length

                def addLine(s: String): Unit = buf += (if (buf.isEmpty) s else s"${space(leader)}$s")

                while (curr < len)
                    if curr - start > maxWidth then
                        val end = if (lastSpace == -1) curr else lastSpace + 1 /* Keep space at the end of the line. */
                        addLine(line.substring(start, end))
                        start = end
                    if line.charAt(curr) == ' ' then
                        lastSpace = curr

                    curr += 1

                if start < len then
                    val lastLine = line.substring(start)
                    if lastLine.trim.nonEmpty then addLine(lastLine)

                buf
        })

    /**
      *
      * @param hdr
      * @param style
      * @param lines
      */
    private def mkStyledCell(hdr: Boolean, style: String, lines: Any*): Cell =
        val st = Style(style)
        val strLines = lines.map(x)

        Cell(
            st,
            if breakUpByWords then
                breakUpByNearestSpace(st.maxWidth, strLines)
            else
                (for str <- strLines yield str.grouped(st.maxWidth)).flatten
        )


    /**
      * Adds single header cell with the default style..
      *
      * @param lines One or more cell lines.
      */
    def addHeaderCell(lines: Any*): CPAsciiTable =
        hdr :+= mkStyledCell(
            true,
            DFLT_HEADER_STYLE,
            lines: _*
        )
        this

    /**
      * Adds single row cell with the default style.
      *
      * @param lines One or more row cells. Multiple lines will be printed on separate lines.
      */
    def addRowCell(lines: Any*): CPAsciiTable =
        curRow :+= mkStyledCell(
            false,
            DFLT_ROW_STYLE,
            lines: _*
        )
        this

    /**
      * Adds single header cell with the default style..
      *
      * @param style Style to use.
      * @param lines One or more cell lines.
      */
    def addStyledHeaderCell(style: String, lines: Any*): CPAsciiTable =
        hdr :+= mkStyledCell(
            hdr = true,
            if style.trim.isEmpty then DFLT_HEADER_STYLE else style,
            lines: _*
        )
        this

    /**
      * Adds single row cell with the default style.
      *
      * @param style Style to use.
      * @param lines One or more row cells. Multiple lines will be printed on separate lines.
      */
    def addStyledRowCell(style: String, lines: Any*): CPAsciiTable =
        curRow :+= mkStyledCell(
            false,
            if style.trim.isEmpty then DFLT_ROW_STYLE else style,
            lines: _*
        )
        this

    /**
      *
      * @param txt Text to align.
      * @param width Width already accounts for padding.
      * @param sty Style.
      */
    private def aligned(txt: String, width: Int, sty: Style): String =
        val d = width - txt.length
        sty.align match
            case "center" => s"${space(d / 2)}$txt${space(d / 2 + d % 2)}"
            case "left" => s"${space(sty.leftPad)}$txt${space(d - sty.leftPad)}"
            case "right" => s"${space(d - sty.rightPad)}$txt${space(sty.rightPad)}"
            case _ => throw new AssertionError(s"Invalid align option: $sty")

    override def toString: String = mkString()

    /**
      * Prepares output string.
      */
    private def mkString(): String =
        // Make sure table is not empty.
        if hdr.isEmpty && rows.isEmpty then return ""

        var colsNum = -1
        val isHdr = hdr.nonEmpty

        if isHdr then colsNum = hdr.size

        // Calc number of columns and make sure all rows are even.
        for r <- rows do
            if colsNum == -1 then colsNum = r.size
            else if colsNum != r.size then assert(assertion = false, "Table with uneven rows.")

        assert(colsNum > 0, "No columns found.")

        // At this point all rows in the table have the
        // the same number of columns.
        val colWidths = new Array[Int](colsNum) // Column widths.
        val rowHeights = new Array[Int](rows.length) // Row heights.
        // Header height.
        var hdrH = 0

        // Initialize column widths with header row (if any).
        for i <- hdr.indices do
            val c = hdr(i)
            colWidths(i) = c.width
            hdrH = math.max(hdrH, c.height)

        // Calculate row heights and column widths.
        for i <- rows.indices; j <- 0 until colsNum do
            val c = rows(i)(j)
            rowHeights(i) = math.max(rowHeights(i), c.height)
            colWidths(j) = math.max(colWidths(j), c.width)

        // Table width without the border.
        val tableW = colWidths.sum + colsNum - 1
        val tbl = new mutable.StringBuilder()
        // Top margin.
        for _ <- 0 until margin.top do tbl ++= " \n"

        /**
          *
          * @param crs
          * @param cor
              */
        def mkAsciiLine(crs: String, cor: String): String =
            s"${space(margin.left)}$crs${dash(cor, tableW)}$crs${space(margin.right)}\n"

        // Print header, if any.
        if isHdr then
            tbl ++= mkAsciiLine(HDR_CRS, HDR_HOR)
            for i <- 0 until hdrH do
                // Left margin and '|'.
                tbl ++= s"${space(margin.left)}$HDR_VER"

                for j <- hdr.indices do
                    val c = hdr(j)
                    if i >= 0 && i < c.height then
                        tbl ++= aligned(c.lines(i), colWidths(j), c.style)
                    else
                        tbl ++= space(colWidths(j))
                    tbl ++= s"$HDR_VER" // '|'

                // Right margin.
                tbl ++= s"${space(margin.right)}\n"

            tbl ++= mkAsciiLine(HDR_CRS, HDR_HOR)

        else
            tbl ++= mkAsciiLine(ROW_CRS, ROW_HOR)

        val tblWidthLine = s"${space(margin.left)}$ROW_CRS${dash(ROW_HOR, tableW)}$ROW_CRS${space(margin.right)}\n"

        // Print rows, if any.
        if rows.nonEmpty then
            val addHorLine = (i: Int) => {
                // Left margin and '+'
                tbl ++= s"${space(margin.left)}$ROW_CRS"
                for k <- rows(i).indices do tbl ++= s"${dash(ROW_HOR, colWidths(k))}$ROW_CRS"
                // Right margin.
                tbl ++= s"${space(margin.right)}\n"
            }

            for i <- rows.indices do
                val row = rows(i)
                val rowH = rowHeights(i)
                for j <- 0 until rowH do
                    // Left margin and '|'
                    tbl ++= s"${space(margin.left)}$ROW_VER"

                    for k <- row.indices do
                        val c = row(k)
                        val w = colWidths(k)
                        if j < c.height then
                            tbl ++= aligned(c.lines(j), w, c.style)
                        else
                            tbl ++= space(w)
                        tbl ++= s"$ROW_VER" // '|'


                    // Right margin.
                    tbl ++= s"${space(margin.right)}\n"

                if (i < rows.size - 1 && ((rowH > 1 && multiLineAutoBorder) || insideBorder))
                    addHorLine(i)

            tbl ++= tblWidthLine

        // Bottom margin.
        for _ <- 1 to margin.bottom do tbl ++= s" \n"
        val res = tbl.toString
        res.substring(0, res.length - 1)

    /**
      * Prepares table string representation for logger.
      * @param header Optional header.
      */
    private def mkLogString(header: Option[String] = None): String = s"${header.getOrElse("")}\n${mkString()}"

    /**
      * Renders this table to log as debug.
      *
      * @param log Logger.
      * @param header Optional header.
      */
    def debug(log: CPLog, header: Option[String] = None): Unit = log.debug(mkLogString(header))

    /**
      * Renders this table to log as info.
      *
      * @param log Logger.
      * @param header Optional header.
      */
    def info(log: CPLog, header: Option[String] = None): Unit = log.info(mkLogString(header))

    /**
      * Renders this table to log as warn.
      *
      * @param log Logger.
      * @param header Optional header.
      */
    def warn(log: CPLog, header: Option[String] = None): Unit = log.warn(mkLogString(header))

    /**
      * Renders this table to log as error.
      *
      * @param log Logger.
      * @param header Optional header.
      */
    def error(log: CPLog, header: Option[String] = None): Unit = log.error(mkLogString(header))

    /**
      * Renders this table to log as trace.
      *
      * @param log Logger.
      * @param header Optional header.
      */
    def trace(log: CPLog, header: Option[String] = None): Unit = log.trace(mkLogString(header))

    /**
      * Renders this table as an image.
      *
      * @param skin Skin function. The functions takes character and return a new pixel.
      */
    def toImage(skin: Char => CPPixel): CPImage =
        new CPArrayImage(CPArray2D(mkString().split("\n").toIndexedSeq.map(_.trim)).map(skin))

    /**
      * Renders this table to output stream.
      *
      * @param ps Output stream.
      */
    def render(ps: PrintStream = System.out): Unit = ps.println(mkString())

    /**
      * Renders this table to file.
      *
      * @param path File path.
      */
    def render(path: String): Unit = renderPrintStream(new PrintStream(path), path)

    /**
      * Renders this table to file.
      *
      * @param file File.
      */
    def render(file: java.io.File): Unit = renderPrintStream(new PrintStream(file), file.getAbsolutePath)

    private def renderPrintStream(f: => PrintStream, file: String): Unit =
        try Using.resource(f) { _.print(mkString()) }
        catch case e: IOException => E(s"Error outputting table into file: $file", e)

/**
  * Companion object contains utility functions.
  */
object CPAsciiTable:
    // Default styles.
    private val DFLT_ROW_STYLE = "align:left"
    private val DFLT_HEADER_STYLE = "align:center"

    /**
      * Creates new ASCII text table with all defaults.
      */
    def apply() = new CPAsciiTable

    /**
      * Creates new ASCII table with given header cells.
      *
      * @param hdrs Header.
      */
    def apply(hdrs: Any*): CPAsciiTable = (new CPAsciiTable).#=(hdrs: _*)

    /**
      * Creates new ASCII table with given header cells.
      *
      * @param hdrs Header.
      */
    def apply(hdrs: mutable.Seq[_]): CPAsciiTable = (new CPAsciiTable).#=(hdrs.toSeq: _*)

    /**
      * Creates new ASCII table with given headers and data.
      *
      * @param hdrs Headers.
      * @param data Table data (sequence of rows).
      */
    def of(hdrs: Seq[Any], data: Seq[Seq[Any]]): CPAsciiTable =
        val tbl = (new CPAsciiTable).#=(hdrs: _*)
        data.foreach(tbl.+=(_: _*))
        tbl