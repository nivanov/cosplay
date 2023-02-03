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

import CPFIGLetFont.FIG_HDR_MARKER
import impl.CPUtils
import org.cosplay.CPPixel.*

import java.nio.charset.MalformedInputException
import scala.collection.mutable
import scala.util.Try

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

/*
       _____  ___  ____   __                _
       |  ___||_ _|/ ___| / _|  ___   _ __  | |_  ___  _
       | |_    | || |  _ | |_  / _ \ | '_ \ | __|/ __|(_)
       |  _|   | || |_| ||  _|| (_) || | | || |_ \__ \ _
       |_|    |___|\____||_|   \___/ |_| |_| \__||___/(_)

       The FIGfont Version 2 FIGfont and FIGdriver Standard
       === ======= ======= = ======= === ========= ========
              Draft 2.0 Copyright 1996, 1997
                  by John Cowan and Paul Burton
              Portions Copyright 1991, 1993, 1994
                  by Glenn Chappell and Ian Chai
              May be freely copied and distributed.
*/

/**
  * [[http://www.figlet.org/ FIGLet]] font.
  *
  * FIGLet fonts allow creating art text out of ordinary letters, for example:
  * <pre>
  *
  *   /$$$$$$                      /$$$$$$$  /$$
  *  /$$__  $$                    | $$__  $$| $$
  * | $$  \__/  /$$$$$$   /$$$$$$$| $$  \ $$| $$  /$$$$$$  /$$   /$$
  * | $$       /$$__  $$ /$$_____/| $$$$$$$/| $$ |____  $$| $$  | $$
  * | $$      | $$  \ $$|  $$$$$$ | $$____/ | $$  /$$$$$$$| $$  | $$
  * | $$    $$| $$  | $$ \____  $$| $$      | $$ /$$__  $$| $$  | $$
  * |  $$$$$$/|  $$$$$$/ /$$$$$$$/| $$      | $$|  $$$$$$$|  $$$$$$$
  *  \______/  \______/ |_______/ |__/      |__/ \_______/ \____  $$
  *    ___  _____  ___  ____  __      __   _  _            /$$  | $$
  *   / __)(  _  )/ __)(  _ \(  )    /__\ ( \/ )          |  $$$$$$/
  *  ( (__  )(_)( \__ \ )___/ )(__  /(__)\ \  /            \______/
  *   \___)(_____)(___/(__)  (____)(__)(__)(__)
  *                                         ,,
  *   .g8"""bgd                 `7MM"""Mq.`7MM
  * .dP'     `M                   MM   `MM. MM
  * dM'       ` ,pW"Wq.  ,pP"Ybd  MM   ,M9  MM   ,6"Yb.`7M'   `MF'
  * MM         6W'   `Wb 8I   `"  MMmmdM9   MM  8)   MM  VA   ,V
  * MM.        8M     M8 `YMMMa.  MM        MM   ,pm9MM   VA ,V
  * `Mb.     ,'YA.   ,A9 L.   I8  MM        MM  8M   MM    VVV
  *   `"bmmmd'  `Ybmd9'  M9mmmP'.JMML.    .JMML.`Moo9^Yo.  ,V
  *                                                       ,V
  *                                                    OOb"
  * </pre>
  *
  * You can read the full FIGLet specification [[http://www.jave.de/figlet/figfont.html here]]. This class
  * provides relatively full implementation for FIGLet fonts with the following limitations:
  *  - code tags are NOT supported
  *  - only UTF-8 and windows-1252 charsets are supported
  *  - only left-to-right font direction is supported
  *  - vertical smushing iS NOT supported (ignored)
  *
  * CosPlay comes with **277 built-in pre-packaged FIGLet fonts**. Companion object for this class contains
  * constants for all these fonts.
  *
  * Here's an example of rendering FIGLet fonts using static image sprites. Note that when invoking [[render()]]
  * it returns an image that can be rendered or used just like any other image in CosPlay:
  * {{{
  * val spr1 = CPStaticImageSprite(30, 14, 4, FIG_OGRE.render("CosPlay", C_WHITE).trimBg())
  * val spr1 = CPStaticImageSprite(30, 20, 4, FIG_OGRE.withKerning().render("CosPlay", C_LIGHT_STEEL_BLUE).trimBg())
  * val spr1 = CPStaticImageSprite(30, 27, 4, FIG_OGRE.withFullWidth().render("CosPlay", C_LIGHT_CORAL).trimBg())
  * }}}
  *
  * Here's the list of helpful links when working with FIGLet fonts:
  *  - [[http://www.figlet.org]] - Linux/Unix FIGLet utility.
  *  - [[http://www.jave.de/figlet/figfont.html]] - full specification for FIGLet font.
  *  - [[https://patorjk.com/software/taag]] - convenient online tester for various FIGLet fonts.
  *
  * @example See [[org.cosplay.examples.fonts.CPFontsExample CPFontsExample]] source code for an
  *     example of FIGLet font functionality.
  */
class CPFIGLetFont(flfPath: String) extends CPFont(flfPath):
    case class FIGLet(private val data: CPArray2D[Char], width: Int, height: Int):
        def raw(x: Int, y: Int): Char = data.get(x, y)
        def loop(f: (Int, Int) => Unit): Unit = data.rect.loop(f)
        def get(x: Int, y: Int): Char =
            val ch = raw(x, y)
            if ch == figHardblank then ' ' else ch

    private var figHeight: Int = -1
    private var figBaseline: Int = -1
    private var figHardblank: Char = _
    private var figMaxLength: Int = -1
    private var figComLines: Int = -1
    private var figOldLayout: Int = 0
    private var figFullLayout: Int = 0
    private var figPrintDirection: Int = -1
    private var figCodeTagCount: Int = -1

    private var enc: String = _
    private val chars = Array.ofDim[FIGLet](256)

    // Horizontal kerning, smushing and full width.
    private var figSmushRule1 = false
    private var figSmushRule2 = false
    private var figSmushRule3 = false
    private var figSmushRule4 = false
    private var figSmushRule5= false
    private var figSmushRule6 = false
    private var figFullWidth = false
    private var figKerning = false
    private var figUniSmush = false
    private var figCtrlSmush = false

    // Initialize.
    loadAndInit()

    /**
      *
      * @param kerning
      * @param uniSmush
      * @param ctrlSmush
      * @param fullWidth
      */
    private def withFitting(kerning: Boolean, uniSmush: Boolean, ctrlSmush: Boolean, fullWidth: Boolean): CPFIGLetFont =
        figKerning = kerning
        figFullWidth = fullWidth
        figUniSmush = uniSmush
        figCtrlSmush = ctrlSmush
        this

    /**
      * Clones this font with kerning fitting.
      *
      * @see [[http://www.jave.de/figlet/figfont.html]] for FIGLet specification for details.
      * @see [[https://patorjk.com/software/taag]] for convenient online tester for various FIGLet fonts and their properties.
      */
    def withKerning(): CPFIGLetFont =
        CPFIGLetFont(flfPath).withFitting(kerning = true, uniSmush = false, ctrlSmush = false, fullWidth = false)

    /**
      * Clones this font with controlled smushing.
      *
      * @see [[http://www.jave.de/figlet/figfont.html]] for FIGLet specification for details.
      * @see [[https://patorjk.com/software/taag]] for convenient online tester for various FIGLet fonts and their properties.
      */
    def withControlSmushing(): CPFIGLetFont =
        CPFIGLetFont(flfPath).withFitting(kerning = false, uniSmush = false, ctrlSmush = true, fullWidth = false)

    /**
      * Clones this font with universal smushing.
      *
      * @see [[http://www.jave.de/figlet/figfont.html]] for FIGLet specification for details.
      * @see [[https://patorjk.com/software/taag]] for convenient online tester for various FIGLet fonts and their properties.
      */
    def withUniversalSmushing(): CPFIGLetFont =
        CPFIGLetFont(flfPath).withFitting(kerning = false, uniSmush = true, ctrlSmush = false, fullWidth = false)

    /**
      * Clones this font with full width
      *
      * @see [[http://www.jave.de/figlet/figfont.html]] for FIGLet specification for details.
      * @see [[https://patorjk.com/software/taag]] for convenient online tester for various FIGLet fonts and their properties.
      */
    def withFullWidth(): CPFIGLetFont =
        CPFIGLetFont(flfPath).withFitting(kerning = false, uniSmush = true, ctrlSmush = false, fullWidth = true)

    /** @inheritdoc */
    override def render(s: String, fg: CPColor, bg: Option[CPColor] = None): CPImage =
        def getFIGChar(code: Int): FIGLet =
            if code < 0 || code >= chars.length then E(s"Invalid FIGLet font char code: $code")
            val figlet = chars(code)
            if figlet == null then E(s"Unsupported char code: $code")
            figlet

        val data = new CPArray2D[CPPixel](s.foldLeft(0)((sum, ch) => sum + getFIGChar(ch).width), figHeight)

        def kerningPos(startX: Int, figCh: FIGLet): Int =
            var kernX = startX
            var x, y = 0
            var overlapping = false

            while (kernX > 0 && !overlapping)
                x = 0
                while (x < figCh.width && !overlapping)
                    y = 0
                    while (y < figHeight && !overlapping)
                        val px1 = data.get(kernX + x, y)
                        if px1 != null then
                            val ch1 =  px1.char
                            val ch2 = figCh.raw(x, y)
                            overlapping = ch1 != ' ' && ch2 != ' '
                        y += 1
                    x += 1
                if !overlapping then kernX -= 1

            if overlapping then kernX += 1

            kernX

        def smushingPos(startX: Int, figCh: FIGLet): Int =
            var smushX = startX
            var x, y = 0
            var ch1: Char = 0
            var ch2: Char = 0
            var overlapping = false

            while (smushX > 0 && !overlapping)
                x = 0
                while (x < figCh.width && !overlapping)
                    y = 0
                    while (y < figHeight && !overlapping)
                        val px1 = data.get(smushX + x, y)
                        if px1 != null then
                            ch1 =  px1.char
                            ch2 = figCh.raw(x, y)
                            overlapping = ch1 != ' ' && ch2 != ' '
                        y += 1
                    x += 1
                if !overlapping then smushX -= 1

            if overlapping then
                if figSmushRule6 then
                    if ch1 == figHardblank && ch2 == figHardblank then smushX
                    else smushX + 1
                else
                    if ch1 == figHardblank || ch2 == figHardblank then smushX + 1
                    else smushX
            else
                smushX

        if figFullWidth then
            var chX = 0
            for ch <- s do
                val figCh = getFIGChar(ch)
                figCh.loop((x, y) => data.set(chX + x, y, CPPixel(figCh.get(x, y), fg, bg)))
                chX += figCh.width
        else if figKerning || figUniSmush then
            var chX = 0
            for ch <- s do
                val figCh = getFIGChar(ch)
                chX = if chX == 0 then 0 else if figKerning then kerningPos(chX, figCh) else smushingPos(chX, figCh)
                figCh.loop((x, y) => {
                    val x2 = chX + x
                    val px1 = data.get(x2, y)
                    if px1 == null || px1.char == ' ' then data.set(x2, y, CPPixel(figCh.raw(x, y), fg, bg))
                })
                chX += figCh.width
        else if figCtrlSmush then
            var chX = 0
            for ch <- s do
                val figCh = getFIGChar(ch)
                // Controlled smushing.
                chX = if chX == 0 then 0 else smushingPos(chX, figCh)
                figCh.loop((x, y) => {
                    val x2 = chX + x
                    val px1 = data.get(x2, y)
                    val ch2 = figCh.raw(x, y)
                    if px1 == null || px1.char == ' ' then data.set(x2, y, CPPixel(ch2, fg, bg))
                    else
                        val ch1 = px1.char

                        def isBorderChar(ch: Char): Boolean =
                            ch == '|' || ch == '/' || ch == '\\' || ch == '[' || ch == ']' ||
                                ch == '(' || ch == ')' || ch == '{' || ch == '}' || ch == '<' || ch == '>'

                        if figSmushRule2 then
                            if ch1 == '_' && isBorderChar(ch2) then
                                data.set(x2, y, CPPixel(ch2, fg, bg))
                        if figSmushRule3 then
                            val rule3 = "|/\\[]{}()<>"
                            if ch1 != ch2 && isBorderChar(ch1) && isBorderChar(ch2) then
                                // Note entirely up to spec...
                                val ch = if rule3.indexOf(ch1) < rule3.indexOf(ch2) then ch2 else ch1
                                data.set(x2, y, CPPixel(ch, fg, bg))
                        if figSmushRule4 then
                            if (ch1 == '[' && ch2 == ']') ||
                                (ch1 == ']' && ch2 == '[') ||
                                (ch1 == '{' && ch2 == '}') ||
                                (ch1 == '}' && ch2 == '{') ||
                                (ch1 == '(' && ch2 == ')') ||
                                (ch1 == ')' && ch2 == '(') then
                                data.set(x2, y, '|'&?(fg, bg))
                        if figSmushRule5 then
                            if ch1 == '/' && ch2 == '\\' then data.set(x2, y, '|'&?(fg, bg))
                            else if ch1 == '\\' && ch2 == '/' then data.set(x2, y, 'Y'&?(fg, bg))
                            else if ch1 == '>' && ch2 == '<' then data.set(x2, y, 'X'&?(fg, bg))
                })
                chX += figCh.width
        else
            assert(false)

        // Switch hardblanks to spaces.
        data.rect.loop((x, y) => {
            val px = data.get(x, y)
            if px != null && px.char == figHardblank then data.set(x, y, px.withChar(' '))
        })

        new CPArrayImage(data.trim(px => px == null || px.char == ' '))

    /** @inheritdoc */
    override def isSystem: Boolean = false
    /** @inheritdoc */
    override def getHeight: Int = figHeight
    /** @inheritdoc */
    override def getWidth: Int = figMaxLength
    /** @inheritdoc */
    override def getEncoding: String = enc
    /** @inheritdoc */
    override def getBaseline: Int = figBaseline

    /*

          flf2a$ 6 5 20 15 3 0 143 229    NOTE: The first five characters in
            |  | | | |  |  | |  |   |     the entire file must be "flf2a".
           /  /  | | |  |  | |  |   \
  Signature  /  /  | |  |  | |   \   Codetag_Count
    Hardblank  /  /  |  |  |  \   Full_Layout*
         Height  /   |  |   \  Print_Direction
         Baseline   /    \   Comment_Lines
          Max_Length      Old_Layout*
    */
    /**
      *
      * @param hdr
      */
    private def parseFIGHeader(hdr: String): Unit =
        def wrongHeader(err: String): CPException = new CPException(s"Invalid FLF file header ($err): $flfPath")

        val parts = hdr.split(" ").filter(_.nonEmpty).map(_.trim)

        if parts.length < 7 then wrongHeader("less than 7 mandatory fields")

        // Mandatory fields.
        val p0 = parts(0) // Header.
        val p1 = parts(1) // Height.
        val p2 = parts(2) // Baseline.
        val p3 = parts(3) // Max length.
        val p4 = parts(4) // Old layout.
        val p5 = parts(5) // Comments lines.

        if !p0.startsWith(FIG_HDR_MARKER) then throw wrongHeader(s"missing '$FIG_HDR_MARKER' marker")
        if p0.length != FIG_HDR_MARKER.length + 1 then throw wrongHeader("invalid marker or missing hardblank")

        figHardblank = p0.charAt(FIG_HDR_MARKER.length)
        figHeight = try p1.toInt catch case _: NumberFormatException => throw wrongHeader("invalid height")
        figBaseline = try p2.toInt catch case _: NumberFormatException => throw wrongHeader("invalid baseline")
        figMaxLength = try p3.toInt catch case _: NumberFormatException => throw wrongHeader("invalid max length")
        figOldLayout = try p4.toInt catch case _: NumberFormatException => throw wrongHeader("invalid old layout")
        figComLines = try p5.toInt catch case _: NumberFormatException => throw wrongHeader("invalid comments lines")

        if figComLines < 0 then throw wrongHeader("negative comments line")
        if figHeight <= 0 then throw wrongHeader("height must > 0")
        if figBaseline > figHeight then throw wrongHeader("baseline > height")
        if figMaxLength < 2 then throw wrongHeader("max length is too small")

        if parts.length >= 7 then // There are fonts that violating min 7 fields rule - so we parse them defensively...
            figPrintDirection = try parts(6).toInt catch case _: NumberFormatException => throw wrongHeader("invalid print direction")
        if parts.length >= 8 then
            figFullLayout = try parts(7).toInt catch case _: NumberFormatException => throw wrongHeader("invalid full layout")
        if parts.length >= 9 then
            figCodeTagCount = try parts(8).toInt catch case _: NumberFormatException => throw wrongHeader("invalid code tag count")

        if figPrintDirection == 1 then E(s"Right-to-left FIGLet fonts are not supported: $flfPath")

        if figFullLayout > 0 then
            if figOldLayout == -1 &&
                (figFullLayout & 64) == 0 &&
                (figFullLayout & 128) == 0 then figFullWidth = true
            else if figOldLayout == 0 &&
                (figFullLayout & 64) == 64 &&
                (figFullLayout & 128) == 0 then figKerning = true
            else if figOldLayout == 0 &&
                (figFullLayout & 1) == 0 &&
                (figFullLayout & 2) == 0 &&
                (figFullLayout & 4) == 0 &&
                (figFullLayout & 8) == 0 &&
                (figFullLayout & 16) == 0 &&
                (figFullLayout & 32) == 0 &&
                (figFullLayout & 128) == 128 then figUniSmush = true
            else if figOldLayout > 0 &&
                (figFullLayout & 128) == 128 then figCtrlSmush = true
            else
                E(s"Cannot determine font layout: $flfPath")
        else
            if figOldLayout == -1 then figFullWidth = true
            else if figOldLayout == 0 then figKerning = true
            else if figOldLayout > 0 && figOldLayout <= 63 then figCtrlSmush = true
            else throw wrongHeader(s"invalid 'Old_Layout' field: $figOldLayout")

        if figCtrlSmush || figUniSmush then
            figSmushRule1 = (figOldLayout & 1) == 1 || (figFullLayout & 1) == 1
            figSmushRule2 = (figOldLayout & 2) == 2 || (figFullLayout & 2) == 2
            figSmushRule3 = (figOldLayout & 4) == 4 || (figFullLayout & 4) == 4
            figSmushRule4 = (figOldLayout & 8) == 8 || (figFullLayout & 8) == 8
            figSmushRule5 = (figOldLayout & 16) == 16 || (figFullLayout & 16) == 16
            figSmushRule6 = (figOldLayout & 32) == 32 || (figFullLayout & 32) == 32

        if !figKerning && !figUniSmush && !figCtrlSmush && !figFullWidth then
            E(s"undetermined layout")

    /**
      *
      * @param lines
      * @param startIdx
      */
    private def parseFIGLet(lines: IndexedSeq[String], startIdx: Int): FIGLet =
        val eol = lines(startIdx).last
        val chLines = mutable.Buffer.empty[String]
        val endIdx = startIdx + figHeight - 1

        for idx <- startIdx to endIdx do
            val chLine = lines(idx)
            if chLine.endsWith(s"$eol$eol") then chLines += chLine.dropRight(2)
            else if chLine.endsWith(s"$eol") then chLines += chLine.dropRight(1)
            else E(s"FLF character line $idx '$chLine' does not properly terminate in $flfPath'")

        val width = chLines.maxBy(_.length).length

        // Defensively pad to max width to combat many fonts that are not 100% following FIGLet spec.
        chLines.foreach(line => line.padTo(width, ' '))

        val x = CPArray2D(chLines.toSeq)

        FIGLet(
            x,
            width,
            figHeight
        )

    /**
      *
      */
    private def loadAndInit(): Unit =
        var lines = try
            enc = "UTF-8"
            CPUtils.readAllStrings(flfPath, enc).toIndexedSeq
        catch
            case _: MalformedInputException =>
                try
                    enc = "windows-1252"
                    CPUtils.readAllStrings(flfPath, enc).toIndexedSeq
                catch
                    case e: Exception => E(s"Failed to read (unsupported encoding?): $flfPath", e)

        // Some fonts are not following FIGLet spec with extra spaces after EOL...
        lines = lines.map(_.stripTrailing())

        if lines.isEmpty then E(s"FLF file is empty: $flfPath")

        parseFIGHeader(lines.head)

        var lineIdx = figComLines + 1
        // Scan required printable ASCII character set (32 -> 126).
        for asciiCode <- 32 to 126 do
            chars(asciiCode) = parseFIGLet(lines, lineIdx)
            lineIdx += figHeight
        // Scan additional required Deutsch FIG characters.
        for asciiCode <- Seq(196, 214, 220, 228, 246, 252, 223) do
            chars(asciiCode) = parseFIGLet(lines, lineIdx)
            lineIdx += figHeight
        // NOTE: code tagged FIGCharacters are ignored.

/**
  * Companion object contains constants for all built-in FIGLet fonts.
  *
  * Useful links:
  *  - [[http://www.jave.de/figlet/figfont.html]] for FIGLet specification for details.
  *  - [[https://patorjk.com/software/taag]] for convenient online tester for various FIGLet fonts and their properties.
  */
object CPFIGLetFont:
    private val FIG_HDR_MARKER = "flf2a"

    /*
        #!/bin/bash
        while read p;
        do
            up=`echo "${p%.*}" | tr '[-a-z ]' '[_A-Z_]'`
            lo=`echo "${p%.*}" | tr ' ' '-'`
            echo "lazy val FIG_$up = CPFIGLetFont(\"$lo-figlet-font\", \"figlets/$p\")"
        done < "figlets.txt"
    */

    lazy val FIG_SLANT: CPFIGLetFont = CPFIGLetFont("figlets/Slant.flf")
    lazy val FIG_MODULAR: CPFIGLetFont = CPFIGLetFont("figlets/Modular.flf")
    lazy val FIG_1ROW: CPFIGLetFont = CPFIGLetFont("figlets/1Row.flf")
    lazy val FIG_3D: CPFIGLetFont = CPFIGLetFont("figlets/3-D.flf")
    lazy val FIG_3D_DIAGONAL: CPFIGLetFont = CPFIGLetFont("figlets/3D Diagonal.flf")
    lazy val FIG_3D_ASCII: CPFIGLetFont = CPFIGLetFont("figlets/3D-ASCII.flf")
    lazy val FIG_3X5: CPFIGLetFont = CPFIGLetFont("figlets/3x5.flf")
    lazy val FIG_4MAX: CPFIGLetFont = CPFIGLetFont("figlets/4Max.flf")
    lazy val FIG_5_LINE_OBLIQUE: CPFIGLetFont = CPFIGLetFont("figlets/5 Line Oblique.flf")
    lazy val FIG_ACROBATIC: CPFIGLetFont = CPFIGLetFont("figlets/Acrobatic.flf")
    lazy val FIG_ALLIGATOR: CPFIGLetFont = CPFIGLetFont("figlets/Alligator.flf")
    lazy val FIG_ALLIGATOR2: CPFIGLetFont = CPFIGLetFont("figlets/Alligator2.flf")
    lazy val FIG_ALPHA: CPFIGLetFont = CPFIGLetFont("figlets/Alpha.flf")
    lazy val FIG_ALPHABET: CPFIGLetFont = CPFIGLetFont("figlets/Alphabet.flf")
    lazy val FIG_AMC_AAA01: CPFIGLetFont = CPFIGLetFont("figlets/AMC AAA01.flf")
    lazy val FIG_AMC_NEKO: CPFIGLetFont = CPFIGLetFont("figlets/AMC Neko.flf")
    lazy val FIG_AMC_RAZOR: CPFIGLetFont = CPFIGLetFont("figlets/AMC Razor.flf")
    lazy val FIG_AMC_RAZOR2: CPFIGLetFont = CPFIGLetFont("figlets/AMC Razor2.flf")
    lazy val FIG_AMC_SLASH: CPFIGLetFont = CPFIGLetFont("figlets/AMC Slash.flf")
    lazy val FIG_AMC_SLIDER: CPFIGLetFont = CPFIGLetFont("figlets/AMC Slider.flf")
    lazy val FIG_AMC_THIN: CPFIGLetFont = CPFIGLetFont("figlets/AMC Thin.flf")
    lazy val FIG_AMC_TUBES: CPFIGLetFont = CPFIGLetFont("figlets/AMC Tubes.flf")
    lazy val FIG_AMC_UNTITLED: CPFIGLetFont = CPFIGLetFont("figlets/AMC Untitled.flf")
    lazy val FIG_AMC_3_LINE: CPFIGLetFont = CPFIGLetFont("figlets/AMC 3 Line.flf")
    lazy val FIG_AMC_3_LIV1: CPFIGLetFont = CPFIGLetFont("figlets/AMC 3 Liv1.flf")
    lazy val FIG_ANSI_REGULAR: CPFIGLetFont = CPFIGLetFont("figlets/ANSI Regular.flf")
    lazy val FIG_ANSI_SHADOW: CPFIGLetFont = CPFIGLetFont("figlets/ANSI Shadow.flf")
    lazy val FIG_ARROWS: CPFIGLetFont = CPFIGLetFont("figlets/Arrows.flf")
    lazy val FIG_ASCII_NEW_ROMAN: CPFIGLetFont = CPFIGLetFont("figlets/ASCII New Roman.flf")
    lazy val FIG_AVATAR: CPFIGLetFont = CPFIGLetFont("figlets/Avatar.flf")
    lazy val FIG_BANNER: CPFIGLetFont = CPFIGLetFont("figlets/Banner.flf")
    lazy val FIG_BANNER3: CPFIGLetFont = CPFIGLetFont("figlets/Banner3.flf")
    lazy val FIG_BANNER3_D: CPFIGLetFont = CPFIGLetFont("figlets/Banner3-D.flf")
    lazy val FIG_BANNER4: CPFIGLetFont = CPFIGLetFont("figlets/Banner4.flf")
    lazy val FIG_BARBWIRE: CPFIGLetFont = CPFIGLetFont("figlets/Barbwire.flf")
    lazy val FIG_BASIC: CPFIGLetFont = CPFIGLetFont("figlets/Basic.flf")
    lazy val FIG_BEAR: CPFIGLetFont = CPFIGLetFont("figlets/Bear.flf")
    lazy val FIG_BELL: CPFIGLetFont = CPFIGLetFont("figlets/Bell.flf")
    lazy val FIG_BENJAMIN: CPFIGLetFont = CPFIGLetFont("figlets/Benjamin.flf")
    lazy val FIG_BIG: CPFIGLetFont = CPFIGLetFont("figlets/Big.flf")
    lazy val FIG_BIG_CHIEF: CPFIGLetFont = CPFIGLetFont("figlets/Big Chief.flf")
    lazy val FIG_BIG_MONEY_NE: CPFIGLetFont = CPFIGLetFont("figlets/Big Money-ne.flf")
    lazy val FIG_BIG_MONEY_NW: CPFIGLetFont = CPFIGLetFont("figlets/Big Money-nw.flf")
    lazy val FIG_BIG_MONEY_SE: CPFIGLetFont = CPFIGLetFont("figlets/Big Money-se.flf")
    lazy val FIG_BIG_MONEY_SW: CPFIGLetFont = CPFIGLetFont("figlets/Big Money-sw.flf")
    lazy val FIG_BIGFIG: CPFIGLetFont = CPFIGLetFont("figlets/Bigfig.flf")
    lazy val FIG_BLOCK: CPFIGLetFont = CPFIGLetFont("figlets/Block.flf")
    lazy val FIG_BLOCKS: CPFIGLetFont = CPFIGLetFont("figlets/Blocks.flf")
    lazy val FIG_BLOODY: CPFIGLetFont = CPFIGLetFont("figlets/Bloody.flf")
    lazy val FIG_BOLGER: CPFIGLetFont = CPFIGLetFont("figlets/Bolger.flf")
    lazy val FIG_BRACED: CPFIGLetFont = CPFIGLetFont("figlets/Braced.flf")
    lazy val FIG_BRIGHT: CPFIGLetFont = CPFIGLetFont("figlets/Bright.flf")
    lazy val FIG_BROADWAY: CPFIGLetFont = CPFIGLetFont("figlets/Broadway.flf")
    lazy val FIG_BROADWAY_KB: CPFIGLetFont = CPFIGLetFont("figlets/Broadway KB.flf")
    lazy val FIG_BULBHEAD: CPFIGLetFont = CPFIGLetFont("figlets/Bulbhead.flf")
    lazy val FIG_CALIGRAPHY: CPFIGLetFont = CPFIGLetFont("figlets/Caligraphy.flf")
    lazy val FIG_CALIGRAPHY2: CPFIGLetFont = CPFIGLetFont("figlets/Caligraphy2.flf")
    lazy val FIG_CALVIN_S: CPFIGLetFont = CPFIGLetFont("figlets/Calvin S.flf")
    lazy val FIG_CARDS: CPFIGLetFont = CPFIGLetFont("figlets/Cards.flf")
    lazy val FIG_CATWALK: CPFIGLetFont = CPFIGLetFont("figlets/Catwalk.flf")
    lazy val FIG_CHISELED: CPFIGLetFont = CPFIGLetFont("figlets/Chiseled.flf")
    lazy val FIG_CHUNKY: CPFIGLetFont = CPFIGLetFont("figlets/Chunky.flf")
    lazy val FIG_COINSTAK: CPFIGLetFont = CPFIGLetFont("figlets/Coinstak.flf")
    lazy val FIG_COLA: CPFIGLetFont = CPFIGLetFont("figlets/Cola.flf")
    lazy val FIG_COLOSSAL: CPFIGLetFont = CPFIGLetFont("figlets/Colossal.flf")
    lazy val FIG_COMPUTER: CPFIGLetFont = CPFIGLetFont("figlets/Computer.flf")
    lazy val FIG_CONTESSA: CPFIGLetFont = CPFIGLetFont("figlets/Contessa.flf")
    lazy val FIG_CONTRAST: CPFIGLetFont = CPFIGLetFont("figlets/Contrast.flf")
    lazy val FIG_COSMIKE: CPFIGLetFont = CPFIGLetFont("figlets/Cosmike.flf")
    lazy val FIG_CRAWFORD: CPFIGLetFont = CPFIGLetFont("figlets/Crawford.flf")
    lazy val FIG_CRAWFORD2: CPFIGLetFont = CPFIGLetFont("figlets/Crawford2.flf")
    lazy val FIG_CRAZY: CPFIGLetFont = CPFIGLetFont("figlets/Crazy.flf")
    lazy val FIG_CRICKET: CPFIGLetFont = CPFIGLetFont("figlets/Cricket.flf")
    lazy val FIG_CURSIVE: CPFIGLetFont = CPFIGLetFont("figlets/Cursive.flf")
    lazy val FIG_CYBERLARGE: CPFIGLetFont = CPFIGLetFont("figlets/Cyberlarge.flf")
    lazy val FIG_CYBERMEDIUM: CPFIGLetFont = CPFIGLetFont("figlets/Cybermedium.flf")
    lazy val FIG_CYBERSMALL: CPFIGLetFont = CPFIGLetFont("figlets/Cybersmall.flf")
    lazy val FIG_CYGNET: CPFIGLetFont = CPFIGLetFont("figlets/Cygnet.flf")
    lazy val FIG_DANC4: CPFIGLetFont = CPFIGLetFont("figlets/DANC4.flf")
    lazy val FIG_DANCING_FONT: CPFIGLetFont = CPFIGLetFont("figlets/Dancing Font.flf")
    lazy val FIG_DEF_LEPPARD: CPFIGLetFont = CPFIGLetFont("figlets/Def Leppard.flf")
    lazy val FIG_DELTA_CORPS_PRIEST_1: CPFIGLetFont = CPFIGLetFont("figlets/Delta Corps Priest 1.flf")
    lazy val FIG_DIAMOND: CPFIGLetFont = CPFIGLetFont("figlets/Diamond.flf")
    lazy val FIG_DIET_COLA: CPFIGLetFont = CPFIGLetFont("figlets/Diet Cola.flf")
    lazy val FIG_DOH: CPFIGLetFont = CPFIGLetFont("figlets/Doh.flf")
    lazy val FIG_DOOM: CPFIGLetFont = CPFIGLetFont("figlets/Doom.flf")
    lazy val FIG_DOS_REBEL: CPFIGLetFont = CPFIGLetFont("figlets/DOS Rebel.flf")
    lazy val FIG_DOT_MATRIX: CPFIGLetFont = CPFIGLetFont("figlets/Dot Matrix.flf")
    lazy val FIG_DOUBLE: CPFIGLetFont = CPFIGLetFont("figlets/Double.flf")
    lazy val FIG_DOUBLE_SHORTS: CPFIGLetFont = CPFIGLetFont("figlets/Double Shorts.flf")
    lazy val FIG_DR_PEPPER: CPFIGLetFont = CPFIGLetFont("figlets/Dr Pepper.flf")
    lazy val FIG_DWHISTLED: CPFIGLetFont = CPFIGLetFont("figlets/DWhistled.flf")
    lazy val FIG_EFTI_CHESS: CPFIGLetFont = CPFIGLetFont("figlets/Efti Chess.flf")
    lazy val FIG_EFTI_FONT: CPFIGLetFont = CPFIGLetFont("figlets/Efti Font.flf")
    lazy val FIG_EFTI_ITALIC: CPFIGLetFont = CPFIGLetFont("figlets/Efti Italic.flf")
    lazy val FIG_EFTI_PITI: CPFIGLetFont = CPFIGLetFont("figlets/Efti Piti.flf")
    lazy val FIG_EFTI_ROBOT: CPFIGLetFont = CPFIGLetFont("figlets/Efti Robot.flf")
    lazy val FIG_EFTI_WALL: CPFIGLetFont = CPFIGLetFont("figlets/Efti Wall.flf")
    lazy val FIG_EFTI_WATER: CPFIGLetFont = CPFIGLetFont("figlets/Efti Water.flf")
    lazy val FIG_ELECTRONIC: CPFIGLetFont = CPFIGLetFont("figlets/Electronic.flf")
    lazy val FIG_ELITE: CPFIGLetFont = CPFIGLetFont("figlets/Elite.flf")
    lazy val FIG_EPIC: CPFIGLetFont = CPFIGLetFont("figlets/Epic.flf")
    lazy val FIG_FENDER: CPFIGLetFont = CPFIGLetFont("figlets/Fender.flf")
    lazy val FIG_FILTER: CPFIGLetFont = CPFIGLetFont("figlets/Filter.flf")
    lazy val FIG_FIRE_FONT_K: CPFIGLetFont = CPFIGLetFont("figlets/Fire Font-k.flf")
    lazy val FIG_FIRE_FONT_S: CPFIGLetFont = CPFIGLetFont("figlets/Fire Font-s.flf")
    lazy val FIG_FLIPPED: CPFIGLetFont = CPFIGLetFont("figlets/Flipped.flf")
    lazy val FIG_FLOWER_POWER: CPFIGLetFont = CPFIGLetFont("figlets/Flower Power.flf")
    lazy val FIG_FOUR_TOPS: CPFIGLetFont = CPFIGLetFont("figlets/Four Tops.flf")
    lazy val FIG_FRAKTUR: CPFIGLetFont = CPFIGLetFont("figlets/Fraktur.flf")
    lazy val FIG_FUN_FACE: CPFIGLetFont = CPFIGLetFont("figlets/Fun Face.flf")
    lazy val FIG_FUN_FACES: CPFIGLetFont = CPFIGLetFont("figlets/Fun Faces.flf")
    lazy val FIG_FUZZY: CPFIGLetFont = CPFIGLetFont("figlets/Fuzzy.flf")
    lazy val FIG_GEORGI16: CPFIGLetFont = CPFIGLetFont("figlets/Georgi16.flf")
    lazy val FIG_GEORGIA11: CPFIGLetFont = CPFIGLetFont("figlets/Georgia11.flf")
    lazy val FIG_GHOST: CPFIGLetFont = CPFIGLetFont("figlets/Ghost.flf")
    lazy val FIG_GHOULISH: CPFIGLetFont = CPFIGLetFont("figlets/Ghoulish.flf")
    lazy val FIG_GLENYN: CPFIGLetFont = CPFIGLetFont("figlets/Glenyn.flf")
    lazy val FIG_GOOFY: CPFIGLetFont = CPFIGLetFont("figlets/Goofy.flf")
    lazy val FIG_GOTHIC: CPFIGLetFont = CPFIGLetFont("figlets/Gothic.flf")
    lazy val FIG_GRACEFUL: CPFIGLetFont = CPFIGLetFont("figlets/Graceful.flf")
    lazy val FIG_GRADIENT: CPFIGLetFont = CPFIGLetFont("figlets/Gradient.flf")
    lazy val FIG_GRAFFITI: CPFIGLetFont = CPFIGLetFont("figlets/Graffiti.flf")
    lazy val FIG_GREEK: CPFIGLetFont = CPFIGLetFont("figlets/Greek.flf")
    lazy val FIG_HEART_LEFT: CPFIGLetFont = CPFIGLetFont("figlets/Heart Left.flf")
    lazy val FIG_HEART_RIGHT: CPFIGLetFont = CPFIGLetFont("figlets/Heart Right.flf")
    lazy val FIG_HENRY_3D: CPFIGLetFont = CPFIGLetFont("figlets/Henry 3D.flf")
    lazy val FIG_HEX: CPFIGLetFont = CPFIGLetFont("figlets/Hex.flf")
    lazy val FIG_HIEROGLYPHS: CPFIGLetFont = CPFIGLetFont("figlets/Hieroglyphs.flf")
    lazy val FIG_HOLLYWOOD: CPFIGLetFont = CPFIGLetFont("figlets/Hollywood.flf")
    lazy val FIG_HORIZONTAL_LEFT: CPFIGLetFont = CPFIGLetFont("figlets/Horizontal Left.flf")
    lazy val FIG_HORIZONTAL_RIGHT: CPFIGLetFont = CPFIGLetFont("figlets/Horizontal Right.flf")
    lazy val FIG_ICL_1900: CPFIGLetFont = CPFIGLetFont("figlets/ICL-1900.flf")
    lazy val FIG_IMPOSSIBLE: CPFIGLetFont = CPFIGLetFont("figlets/Impossible.flf")
    lazy val FIG_INVITA: CPFIGLetFont = CPFIGLetFont("figlets/Invita.flf")
    lazy val FIG_ISOMETRIC1: CPFIGLetFont = CPFIGLetFont("figlets/Isometric1.flf")
    lazy val FIG_ISOMETRIC2: CPFIGLetFont = CPFIGLetFont("figlets/Isometric2.flf")
    lazy val FIG_ISOMETRIC3: CPFIGLetFont = CPFIGLetFont("figlets/Isometric3.flf")
    lazy val FIG_ISOMETRIC4: CPFIGLetFont = CPFIGLetFont("figlets/Isometric4.flf")
    lazy val FIG_ITALIC: CPFIGLetFont = CPFIGLetFont("figlets/Italic.flf")
    lazy val FIG_JACKY: CPFIGLetFont = CPFIGLetFont("figlets/Jacky.flf")
    lazy val FIG_JAZMINE: CPFIGLetFont = CPFIGLetFont("figlets/Jazmine.flf")
    lazy val FIG_JS_BLOCK_LETTERS: CPFIGLetFont = CPFIGLetFont("figlets/JS Block Letters.flf")
    lazy val FIG_JS_BRACKET_LETTERS: CPFIGLetFont = CPFIGLetFont("figlets/JS Bracket Letters.flf")
    lazy val FIG_JS_CAPITAL_CURVES: CPFIGLetFont = CPFIGLetFont("figlets/JS Capital Curves.flf")
    lazy val FIG_JS_CURSIVE: CPFIGLetFont = CPFIGLetFont("figlets/JS Cursive.flf")
    lazy val FIG_JS_STICK_LETTERS: CPFIGLetFont = CPFIGLetFont("figlets/JS Stick Letters.flf")
    lazy val FIG_KATAKANA: CPFIGLetFont = CPFIGLetFont("figlets/Katakana.flf")
    lazy val FIG_KBAN: CPFIGLetFont = CPFIGLetFont("figlets/Kban.flf")
    lazy val FIG_KEYBOARD: CPFIGLetFont = CPFIGLetFont("figlets/Keyboard.flf")
    lazy val FIG_KNOB: CPFIGLetFont = CPFIGLetFont("figlets/Knob.flf")
    lazy val FIG_KONTO: CPFIGLetFont = CPFIGLetFont("figlets/Konto.flf")
    lazy val FIG_KONTO_SLANT: CPFIGLetFont = CPFIGLetFont("figlets/Konto Slant.flf")
    lazy val FIG_LARRY_3D: CPFIGLetFont = CPFIGLetFont("figlets/Larry 3D.flf")
    lazy val FIG_LARRY_3D_2: CPFIGLetFont = CPFIGLetFont("figlets/Larry 3D 2.flf")
    lazy val FIG_LCD: CPFIGLetFont = CPFIGLetFont("figlets/LCD.flf")
    lazy val FIG_LEAN: CPFIGLetFont = CPFIGLetFont("figlets/Lean.flf")
    lazy val FIG_LETTERS: CPFIGLetFont = CPFIGLetFont("figlets/Letters.flf")
    lazy val FIG_LIL_DEVIL: CPFIGLetFont = CPFIGLetFont("figlets/Lil Devil.flf")
    lazy val FIG_LINE_BLOCKS: CPFIGLetFont = CPFIGLetFont("figlets/Line Blocks.flf")
    lazy val FIG_LINUX: CPFIGLetFont = CPFIGLetFont("figlets/Linux.flf")
    lazy val FIG_LOCKERGNOME: CPFIGLetFont = CPFIGLetFont("figlets/Lockergnome.flf")
    lazy val FIG_MADRID: CPFIGLetFont = CPFIGLetFont("figlets/Madrid.flf")
    lazy val FIG_MARQUEE: CPFIGLetFont = CPFIGLetFont("figlets/Marquee.flf")
    lazy val FIG_MAXFOUR: CPFIGLetFont = CPFIGLetFont("figlets/Maxfour.flf")
    lazy val FIG_MERLIN1: CPFIGLetFont = CPFIGLetFont("figlets/Merlin1.flf")
    lazy val FIG_MERLIN2: CPFIGLetFont = CPFIGLetFont("figlets/Merlin2.flf")
    lazy val FIG_MIKE: CPFIGLetFont = CPFIGLetFont("figlets/Mike.flf")
    lazy val FIG_MINI: CPFIGLetFont = CPFIGLetFont("figlets/Mini.flf")
    lazy val FIG_MNEMONIC: CPFIGLetFont = CPFIGLetFont("figlets/Mnemonic.flf")
    lazy val FIG_MORSE: CPFIGLetFont = CPFIGLetFont("figlets/Morse.flf")
    lazy val FIG_MORSE2: CPFIGLetFont = CPFIGLetFont("figlets/Morse2.flf")
    lazy val FIG_MOSCOW: CPFIGLetFont = CPFIGLetFont("figlets/Moscow.flf")
    lazy val FIG_MUZZLE: CPFIGLetFont = CPFIGLetFont("figlets/Muzzle.flf")
    lazy val FIG_NANCYJ: CPFIGLetFont = CPFIGLetFont("figlets/Nancyj.flf")
    lazy val FIG_NANCYJ_FANCY: CPFIGLetFont = CPFIGLetFont("figlets/Nancyj-Fancy.flf")
    lazy val FIG_NANCYJ_IMPROVED: CPFIGLetFont = CPFIGLetFont("figlets/Nancyj-Improved.flf")
    lazy val FIG_NANCYJ_UNDERLINED: CPFIGLetFont = CPFIGLetFont("figlets/Nancyj-Underlined.flf")
    lazy val FIG_NIPPLES: CPFIGLetFont = CPFIGLetFont("figlets/Nipples.flf")
    lazy val FIG_NSCRIPT: CPFIGLetFont = CPFIGLetFont("figlets/NScript.flf")
    lazy val FIG_NT_GREEK: CPFIGLetFont = CPFIGLetFont("figlets/NT Greek.flf")
    lazy val FIG_NV_SCRIPT: CPFIGLetFont = CPFIGLetFont("figlets/NV Script.flf")
    lazy val FIG_O8: CPFIGLetFont = CPFIGLetFont("figlets/O8.flf")
    lazy val FIG_OCTAL: CPFIGLetFont = CPFIGLetFont("figlets/Octal.flf")
    lazy val FIG_OGRE: CPFIGLetFont = CPFIGLetFont("figlets/Ogre.flf")
    lazy val FIG_OLD_BANNER: CPFIGLetFont = CPFIGLetFont("figlets/Old Banner.flf")
    lazy val FIG_OS2: CPFIGLetFont = CPFIGLetFont("figlets/OS2.flf")
    lazy val FIG_PAGGA: CPFIGLetFont = CPFIGLetFont("figlets/Pagga.flf")
    lazy val FIG_PATORJK_CHEESE: CPFIGLetFont = CPFIGLetFont("figlets/Patorjk's Cheese.flf")
    lazy val FIG_PATORJK_HEX: CPFIGLetFont = CPFIGLetFont("figlets/Patorjk-HeX.flf")
    lazy val FIG_PAWP: CPFIGLetFont = CPFIGLetFont("figlets/Pawp.flf")
    lazy val FIG_PEAKS: CPFIGLetFont = CPFIGLetFont("figlets/Peaks.flf")
    lazy val FIG_PEAKS_SLANT: CPFIGLetFont = CPFIGLetFont("figlets/Peaks Slant.flf")
    lazy val FIG_PEBBLES: CPFIGLetFont = CPFIGLetFont("figlets/Pebbles.flf")
    lazy val FIG_PEPPER: CPFIGLetFont = CPFIGLetFont("figlets/Pepper.flf")
    lazy val FIG_POISON: CPFIGLetFont = CPFIGLetFont("figlets/Poison.flf")
    lazy val FIG_PUFFY: CPFIGLetFont = CPFIGLetFont("figlets/Puffy.flf")
    lazy val FIG_PUZZLE: CPFIGLetFont = CPFIGLetFont("figlets/Puzzle.flf")
    lazy val FIG_RAMMSTEIN: CPFIGLetFont = CPFIGLetFont("figlets/Rammstein.flf")
    lazy val FIG_RECTANGLES: CPFIGLetFont = CPFIGLetFont("figlets/Rectangles.flf")
    lazy val FIG_RED_PHOENIX: CPFIGLetFont = CPFIGLetFont("figlets/Red Phoenix.flf")
    lazy val FIG_RELIEF: CPFIGLetFont = CPFIGLetFont("figlets/Relief.flf")
    lazy val FIG_RELIEF2: CPFIGLetFont = CPFIGLetFont("figlets/Relief2.flf")
    lazy val FIG_REVERSE: CPFIGLetFont = CPFIGLetFont("figlets/Reverse.flf")
    lazy val FIG_ROMAN: CPFIGLetFont = CPFIGLetFont("figlets/Roman.flf")
    lazy val FIG_ROTATED: CPFIGLetFont = CPFIGLetFont("figlets/Rotated.flf")
    lazy val FIG_ROUNDED: CPFIGLetFont = CPFIGLetFont("figlets/Rounded.flf")
    lazy val FIG_ROWAN_CAP: CPFIGLetFont = CPFIGLetFont("figlets/Rowan Cap.flf")
    lazy val FIG_ROZZO: CPFIGLetFont = CPFIGLetFont("figlets/Rozzo.flf")
    lazy val FIG_RUNIC: CPFIGLetFont = CPFIGLetFont("figlets/Runic.flf")
    lazy val FIG_RUNYC: CPFIGLetFont = CPFIGLetFont("figlets/Runyc.flf")
    lazy val FIG_S_BLOOD: CPFIGLetFont = CPFIGLetFont("figlets/S Blood.flf")
    lazy val FIG_SANTA_CLARA: CPFIGLetFont = CPFIGLetFont("figlets/Santa Clara.flf")
    lazy val FIG_SCRIPT: CPFIGLetFont = CPFIGLetFont("figlets/Script.flf")
    lazy val FIG_SERIFCAP: CPFIGLetFont = CPFIGLetFont("figlets/Serifcap.flf")
    lazy val FIG_SHADOW: CPFIGLetFont = CPFIGLetFont("figlets/Shadow.flf")
    lazy val FIG_SHIMROD: CPFIGLetFont = CPFIGLetFont("figlets/Shimrod.flf")
    lazy val FIG_SHORT: CPFIGLetFont = CPFIGLetFont("figlets/Short.flf")
    lazy val FIG_SL_SCRIPT: CPFIGLetFont = CPFIGLetFont("figlets/SL Script.flf")
    lazy val FIG_SLANT_RELIEF: CPFIGLetFont = CPFIGLetFont("figlets/Slant Relief.flf")
    lazy val FIG_SLIDE: CPFIGLetFont = CPFIGLetFont("figlets/Slide.flf")
    lazy val FIG_SMALL: CPFIGLetFont = CPFIGLetFont("figlets/Small.flf")
    lazy val FIG_SMALL_CAPS: CPFIGLetFont = CPFIGLetFont("figlets/Small Caps.flf")
    lazy val FIG_SMALL_ISOMETRIC1: CPFIGLetFont = CPFIGLetFont("figlets/Small Isometric1.flf")
    lazy val FIG_SMALL_KEYBOARD: CPFIGLetFont = CPFIGLetFont("figlets/Small Keyboard.flf")
    lazy val FIG_SMALL_POISON: CPFIGLetFont = CPFIGLetFont("figlets/Small Poison.flf")
    lazy val FIG_SMALL_SCRIPT: CPFIGLetFont = CPFIGLetFont("figlets/Small Script.flf")
    lazy val FIG_SMALL_SHADOW: CPFIGLetFont = CPFIGLetFont("figlets/Small Shadow.flf")
    lazy val FIG_SMALL_SLANT: CPFIGLetFont = CPFIGLetFont("figlets/Small Slant.flf")
    lazy val FIG_SMALL_TENGWAR: CPFIGLetFont = CPFIGLetFont("figlets/Small Tengwar.flf")
    lazy val FIG_SOFT: CPFIGLetFont = CPFIGLetFont("figlets/Soft.flf")
    lazy val FIG_SPEED: CPFIGLetFont = CPFIGLetFont("figlets/Speed.flf")
    lazy val FIG_SPLIFF: CPFIGLetFont = CPFIGLetFont("figlets/Spliff.flf")
    lazy val FIG_STACEY: CPFIGLetFont = CPFIGLetFont("figlets/Stacey.flf")
    lazy val FIG_STAMPATE: CPFIGLetFont = CPFIGLetFont("figlets/Stampate.flf")
    lazy val FIG_STAMPATELLO: CPFIGLetFont = CPFIGLetFont("figlets/Stampatello.flf")
    lazy val FIG_STANDARD: CPFIGLetFont = CPFIGLetFont("figlets/Standard.flf")
    lazy val FIG_STAR_STRIPS: CPFIGLetFont = CPFIGLetFont("figlets/Star Strips.flf")
    lazy val FIG_STAR_WARS: CPFIGLetFont = CPFIGLetFont("figlets/Star Wars.flf")
    lazy val FIG_STELLAR: CPFIGLetFont = CPFIGLetFont("figlets/Stellar.flf")
    lazy val FIG_STFOREK: CPFIGLetFont = CPFIGLetFont("figlets/Stforek.flf")
    lazy val FIG_STICK_LETTERS: CPFIGLetFont = CPFIGLetFont("figlets/Stick Letters.flf")
    lazy val FIG_STOP: CPFIGLetFont = CPFIGLetFont("figlets/Stop.flf")
    lazy val FIG_STRAIGHT: CPFIGLetFont = CPFIGLetFont("figlets/Straight.flf")
    lazy val FIG_STRONGER_THAN_ALL: CPFIGLetFont = CPFIGLetFont("figlets/Stronger Than All.flf")
    lazy val FIG_SUB_ZERO: CPFIGLetFont = CPFIGLetFont("figlets/Sub-Zero.flf")
    lazy val FIG_SWAMP_LAND: CPFIGLetFont = CPFIGLetFont("figlets/Swamp Land.flf")
    lazy val FIG_SWAN: CPFIGLetFont = CPFIGLetFont("figlets/Swan.flf")
    lazy val FIG_SWEET: CPFIGLetFont = CPFIGLetFont("figlets/Sweet.flf")
    lazy val FIG_TANJA: CPFIGLetFont = CPFIGLetFont("figlets/Tanja.flf")
    lazy val FIG_TENGWAR: CPFIGLetFont = CPFIGLetFont("figlets/Tengwar.flf")
    lazy val FIG_TEST1: CPFIGLetFont = CPFIGLetFont("figlets/Test1.flf")
    lazy val FIG_THE_EDGE: CPFIGLetFont = CPFIGLetFont("figlets/The Edge.flf")
    lazy val FIG_THICK: CPFIGLetFont = CPFIGLetFont("figlets/Thick.flf")
    lazy val FIG_THIN: CPFIGLetFont = CPFIGLetFont("figlets/Thin.flf")
    lazy val FIG_THIS: CPFIGLetFont = CPFIGLetFont("figlets/THIS.flf")
    lazy val FIG_THORNED: CPFIGLetFont = CPFIGLetFont("figlets/Thorned.flf")
    lazy val FIG_THREE_POINT: CPFIGLetFont = CPFIGLetFont("figlets/Three Point.flf")
    lazy val FIG_TICKS: CPFIGLetFont = CPFIGLetFont("figlets/Ticks.flf")
    lazy val FIG_TICKS_SLANT: CPFIGLetFont = CPFIGLetFont("figlets/Ticks Slant.flf")
    lazy val FIG_TILES: CPFIGLetFont = CPFIGLetFont("figlets/Tiles.flf")
    lazy val FIG_TINKER_TOY: CPFIGLetFont = CPFIGLetFont("figlets/Tinker-Toy.flf")
    lazy val FIG_TOMBSTONE: CPFIGLetFont = CPFIGLetFont("figlets/Tombstone.flf")
    lazy val FIG_TRAIN: CPFIGLetFont = CPFIGLetFont("figlets/Train.flf")
    lazy val FIG_TREK: CPFIGLetFont = CPFIGLetFont("figlets/Trek.flf")
    lazy val FIG_TSALAGI: CPFIGLetFont = CPFIGLetFont("figlets/Tsalagi.flf")
    lazy val FIG_TUBULAR: CPFIGLetFont = CPFIGLetFont("figlets/Tubular.flf")
    lazy val FIG_TWISTED: CPFIGLetFont = CPFIGLetFont("figlets/Twisted.flf")
    lazy val FIG_TWO_POINT: CPFIGLetFont = CPFIGLetFont("figlets/Two Point.flf")
    lazy val FIG_UNIVERS: CPFIGLetFont = CPFIGLetFont("figlets/Univers.flf")
    lazy val FIG_USA_FLAG: CPFIGLetFont = CPFIGLetFont("figlets/USA Flag.flf")
    lazy val FIG_VARSITY: CPFIGLetFont = CPFIGLetFont("figlets/Varsity.flf")
    lazy val FIG_WAVY: CPFIGLetFont = CPFIGLetFont("figlets/Wavy.flf")
    lazy val FIG_WEIRD: CPFIGLetFont = CPFIGLetFont("figlets/Weird.flf")
    lazy val FIG_WET_LETTER: CPFIGLetFont = CPFIGLetFont("figlets/Wet Letter.flf")
    lazy val FIG_WHIMSY: CPFIGLetFont = CPFIGLetFont("figlets/Whimsy.flf")
    lazy val FIG_WOW: CPFIGLetFont = CPFIGLetFont("figlets/Wow.flf")

    /**
      * Sequence of all FIGLet fonts.
      */
    lazy val ALL_FIG_FONTS = Seq(
        FIG_SLANT,
        FIG_MODULAR,
        FIG_1ROW,
        FIG_3D,
        FIG_3D_DIAGONAL,
        FIG_3D_ASCII,
        FIG_3X5,
        FIG_4MAX,
        FIG_5_LINE_OBLIQUE,
        FIG_ACROBATIC,
        FIG_ALLIGATOR,
        FIG_ALLIGATOR2,
        FIG_ALPHA,
        FIG_ALPHABET,
        FIG_AMC_AAA01,
        FIG_AMC_NEKO,
        FIG_AMC_RAZOR,
        FIG_AMC_RAZOR2,
        FIG_AMC_SLASH,
        FIG_AMC_SLIDER,
        FIG_AMC_THIN,
        FIG_AMC_TUBES,
        FIG_AMC_UNTITLED,
        FIG_AMC_3_LINE,
        FIG_AMC_3_LIV1,
        FIG_ANSI_REGULAR,
        FIG_ANSI_SHADOW,
        FIG_ARROWS,
        FIG_ASCII_NEW_ROMAN,
        FIG_AVATAR,
        FIG_BANNER,
        FIG_BANNER3,
        FIG_BANNER3_D,
        FIG_BANNER4,
        FIG_BARBWIRE,
        FIG_BASIC,
        FIG_BEAR,
        FIG_BELL,
        FIG_BENJAMIN,
        FIG_BIG,
        FIG_BIG_CHIEF,
        FIG_BIG_MONEY_NE,
        FIG_BIG_MONEY_NW,
        FIG_BIG_MONEY_SE,
        FIG_BIG_MONEY_SW,
        FIG_BIGFIG,
        FIG_BLOCK,
        FIG_BLOCKS,
        FIG_BLOODY,
        FIG_BOLGER,
        FIG_BRACED,
        FIG_BRIGHT,
        FIG_BROADWAY,
        FIG_BROADWAY_KB,
        FIG_BULBHEAD,
        FIG_CALIGRAPHY,
        FIG_CALIGRAPHY2,
        FIG_CALVIN_S,
        FIG_CARDS,
        FIG_CATWALK,
        FIG_CHISELED,
        FIG_CHUNKY,
        FIG_COINSTAK,
        FIG_COLA,
        FIG_COLOSSAL,
        FIG_COMPUTER,
        FIG_CONTESSA,
        FIG_CONTRAST,
        FIG_COSMIKE,
        FIG_CRAWFORD,
        FIG_CRAWFORD2,
        FIG_CRAZY,
        FIG_CRICKET,
        FIG_CURSIVE,
        FIG_CYBERLARGE,
        FIG_CYBERMEDIUM,
        FIG_CYBERSMALL,
        FIG_CYGNET,
        FIG_DANC4,
        FIG_DANCING_FONT,
        FIG_DEF_LEPPARD,
        FIG_DELTA_CORPS_PRIEST_1,
        FIG_DIAMOND,
        FIG_DIET_COLA,
        FIG_DOH,
        FIG_DOOM,
        FIG_DOS_REBEL,
        FIG_DOT_MATRIX,
        FIG_DOUBLE,
        FIG_DOUBLE_SHORTS,
        FIG_DR_PEPPER,
        FIG_DWHISTLED,
        FIG_EFTI_CHESS,
        FIG_EFTI_FONT,
        FIG_EFTI_ITALIC,
        FIG_EFTI_PITI,
        FIG_EFTI_ROBOT,
        FIG_EFTI_WALL,
        FIG_EFTI_WATER,
        FIG_ELECTRONIC,
        FIG_ELITE,
        FIG_EPIC,
        FIG_FENDER,
        FIG_FILTER,
        FIG_FIRE_FONT_K,
        FIG_FIRE_FONT_S,
        FIG_FLIPPED,
        FIG_FLOWER_POWER,
        FIG_FOUR_TOPS,
        FIG_FRAKTUR,
        FIG_FUN_FACE,
        FIG_FUN_FACES,
        FIG_FUZZY,
        FIG_GEORGI16,
        FIG_GEORGIA11,
        FIG_GHOST,
        FIG_GHOULISH,
        FIG_GLENYN,
        FIG_GOOFY,
        FIG_GOTHIC,
        FIG_GRACEFUL,
        FIG_GRADIENT,
        FIG_GRAFFITI,
        FIG_GREEK,
        FIG_HEART_LEFT,
        FIG_HEART_RIGHT,
        FIG_HENRY_3D,
        FIG_HEX,
        FIG_HIEROGLYPHS,
        FIG_HOLLYWOOD,
        FIG_HORIZONTAL_LEFT,
        FIG_HORIZONTAL_RIGHT,
        FIG_ICL_1900,
        FIG_IMPOSSIBLE,
        FIG_INVITA,
        FIG_ISOMETRIC1,
        FIG_ISOMETRIC2,
        FIG_ISOMETRIC3,
        FIG_ISOMETRIC4,
        FIG_ITALIC,
        FIG_JACKY,
        FIG_JAZMINE,
        FIG_JS_BLOCK_LETTERS,
        FIG_JS_BRACKET_LETTERS,
        FIG_JS_CAPITAL_CURVES,
        FIG_JS_CURSIVE,
        FIG_JS_STICK_LETTERS,
        FIG_KATAKANA,
        FIG_KBAN,
        FIG_KEYBOARD,
        FIG_KNOB,
        FIG_KONTO,
        FIG_KONTO_SLANT,
        FIG_LARRY_3D,
        FIG_LARRY_3D_2,
        FIG_LCD,
        FIG_LEAN,
        FIG_LETTERS,
        FIG_LIL_DEVIL,
        FIG_LINE_BLOCKS,
        FIG_LINUX,
        FIG_LOCKERGNOME,
        FIG_MADRID,
        FIG_MARQUEE,
        FIG_MAXFOUR,
        FIG_MERLIN1,
        FIG_MERLIN2,
        FIG_MIKE,
        FIG_MINI,
        FIG_MNEMONIC,
        FIG_MORSE,
        FIG_MORSE2,
        FIG_MOSCOW,
        FIG_MUZZLE,
        FIG_NANCYJ,
        FIG_NANCYJ_FANCY,
        FIG_NANCYJ_IMPROVED,
        FIG_NANCYJ_UNDERLINED,
        FIG_NIPPLES,
        FIG_NSCRIPT,
        FIG_NT_GREEK,
        FIG_NV_SCRIPT,
        FIG_O8,
        FIG_OCTAL,
        FIG_OGRE,
        FIG_OLD_BANNER,
        FIG_OS2,
        FIG_PAGGA,
        FIG_PATORJK_CHEESE,
        FIG_PATORJK_HEX,
        FIG_PAWP,
        FIG_PEAKS,
        FIG_PEAKS_SLANT,
        FIG_PEBBLES,
        FIG_PEPPER,
        FIG_POISON,
        FIG_PUFFY,
        FIG_PUZZLE,
        FIG_RAMMSTEIN,
        FIG_RECTANGLES,
        FIG_RED_PHOENIX,
        FIG_RELIEF,
        FIG_RELIEF2,
        FIG_REVERSE,
        FIG_ROMAN,
        FIG_ROTATED,
        FIG_ROUNDED,
        FIG_ROWAN_CAP,
        FIG_ROZZO,
        FIG_RUNIC,
        FIG_RUNYC,
        FIG_S_BLOOD,
        FIG_SANTA_CLARA,
        FIG_SCRIPT,
        FIG_SERIFCAP,
        FIG_SHADOW,
        FIG_SHIMROD,
        FIG_SHORT,
        FIG_SL_SCRIPT,
        FIG_SLANT_RELIEF,
        FIG_SLIDE,
        FIG_SMALL,
        FIG_SMALL_CAPS,
        FIG_SMALL_ISOMETRIC1,
        FIG_SMALL_KEYBOARD,
        FIG_SMALL_POISON,
        FIG_SMALL_SCRIPT,
        FIG_SMALL_SHADOW,
        FIG_SMALL_SLANT,
        FIG_SMALL_TENGWAR,
        FIG_SOFT,
        FIG_SPEED,
        FIG_SPLIFF,
        FIG_STACEY,
        FIG_STAMPATE,
        FIG_STAMPATELLO,
        FIG_STANDARD,
        FIG_STAR_STRIPS,
        FIG_STAR_WARS,
        FIG_STELLAR,
        FIG_STFOREK,
        FIG_STICK_LETTERS,
        FIG_STOP,
        FIG_STRAIGHT,
        FIG_STRONGER_THAN_ALL,
        FIG_SUB_ZERO,
        FIG_SWAMP_LAND,
        FIG_SWAN,
        FIG_SWEET,
        FIG_TANJA,
        FIG_TENGWAR,
        FIG_TEST1,
        FIG_THE_EDGE,
        FIG_THICK,
        FIG_THIN,
        FIG_THIS,
        FIG_THORNED,
        FIG_THREE_POINT,
        FIG_TICKS,
        FIG_TICKS_SLANT,
        FIG_TILES,
        FIG_TINKER_TOY,
        FIG_TOMBSTONE,
        FIG_TRAIN,
        FIG_TREK,
        FIG_TSALAGI,
        FIG_TUBULAR,
        FIG_TWISTED,
        FIG_TWO_POINT,
        FIG_UNIVERS,
        FIG_USA_FLAG,
        FIG_VARSITY,
        FIG_WAVY,
        FIG_WEIRD,
        FIG_WET_LETTER,
        FIG_WHIMSY,
        FIG_WOW
    )

    /**
      * FIGLet fonts with the 1 row height.
      */
    lazy val HEIGHT_1_FIGLET_FONTS = Seq(
        FIG_BENJAMIN,
        FIG_HEX,
        FIG_MNEMONIC, 
        FIG_MORSE,
        FIG_MORSE2,
        FIG_OCTAL,
        FIG_WOW
    )

    /**
      * FIGLet fonts with the 2 rows height.
      */
    lazy val HEIGHT_2_FIGLET_FONTS = Seq(
        FIG_1ROW, 
        FIG_CYBERSMALL, 
        FIG_KONTO,
        FIG_KONTO_SLANT,
        FIG_TWO_POINT
    )

    /**
      * FIGLet fonts with the 3 rows height.
      */
    lazy val HEIGHT_3_FIGLET_FONTS = Seq(
        FIG_BIGFIG, 
        FIG_BROADWAY_KB, 
        FIG_CALVIN_S, 
        FIG_DOUBLE_SHORTS, 
        FIG_EFTI_PITI,
        FIG_JS_BLOCK_LETTERS,
        FIG_MIKE,
        FIG_PAGGA,
        FIG_ROTATED,
        FIG_SHORT,
        FIG_SMALL_TENGWAR,
        FIG_THREE_POINT
    )

    /**
      * FIGLet fonts with the 4 rows height.
      */
    lazy val HEIGHT_4_FIGLET_FONTS = Seq(
        FIG_4MAX,
        FIG_AMC_3_LINE,
        FIG_AMC_3_LIV1,
        FIG_ASCII_NEW_ROMAN,
        FIG_BULBHEAD,
        FIG_CONTESSA,
        FIG_CYBERLARGE,
        FIG_CYBERMEDIUM,
        FIG_DANC4,
        FIG_EFTI_WALL,
        FIG_EFTI_WATER,
        FIG_FLIPPED,
        FIG_FOUR_TOPS,
        FIG_GLENYN,
        FIG_GRACEFUL,
        FIG_HEART_LEFT,
        FIG_HEART_RIGHT,
        FIG_HIEROGLYPHS,
        FIG_ITALIC,
        FIG_JS_BRACKET_LETTERS,
        FIG_JS_CAPITAL_CURVES,
        FIG_JS_STICK_LETTERS,
        FIG_KNOB,
        FIG_LINUX,
        FIG_LOCKERGNOME,
        FIG_MADRID,
        FIG_MAXFOUR,
        FIG_MINI,
        FIG_MUZZLE,
        FIG_PEPPER,
        FIG_SERIFCAP,
        FIG_SMALL_KEYBOARD,
        FIG_SMALL_SHADOW,
        FIG_STFOREK,
        FIG_STICK_LETTERS,
        FIG_STRAIGHT,
        FIG_TEST1,
        FIG_WAVY
    )

    /**
      * FIGLet fonts with the 5 rows height.
      */
    lazy val HEIGHT_5_FIGLET_FONTS = Seq(
        FIG_BRACED,
        FIG_CHUNKY,
        FIG_CYGNET,
        FIG_DOUBLE,
        FIG_DR_PEPPER,
        FIG_EFTI_CHESS,
        FIG_EFTI_FONT,
        FIG_EFTI_ITALIC,
        FIG_ELITE,
        FIG_FILTER,
        FIG_LINE_BLOCKS,
        FIG_PUZZLE,
        FIG_SHADOW,
        FIG_SMALL,
        FIG_SMALL_CAPS,
        FIG_SMALL_SCRIPT,
        FIG_SMALL_SLANT,
        FIG_SPLIFF,
        FIG_THICK,
        FIG_THORNED,
        FIG_TOMBSTONE,
        FIG_TSALAGI
    )

    /**
      * FIGLet fonts with the 6 rows height.
      */
    lazy val HEIGHT_6_FIGLET_FONTS = Seq(
        FIG_SLANT,
        FIG_3X5,
        FIG_5_LINE_OBLIQUE,
        FIG_AMC_SLIDER,
        FIG_AVATAR,
        FIG_BELL,
        FIG_BRIGHT,
        FIG_CARDS,
        FIG_COLA,
        FIG_CONTRAST,
        FIG_COSMIKE,
        FIG_CURSIVE,
        FIG_DIET_COLA,
        FIG_EFTI_ROBOT,
        FIG_GOOFY,
        FIG_GRAFFITI,
        FIG_HORIZONTAL_LEFT,
        FIG_HORIZONTAL_RIGHT,
        FIG_INVITA,
        FIG_JS_CURSIVE,
        FIG_LCD,
        FIG_LETTERS,
        FIG_MOSCOW,
        FIG_O8,
        FIG_OGRE,
        FIG_PEAKS_SLANT,
        FIG_RECTANGLES,
        FIG_ROWAN_CAP,
        FIG_RUNIC,
        FIG_RUNYC,
        FIG_S_BLOOD,
        FIG_SANTA_CLARA,
        FIG_SHIMROD,
        FIG_SL_SCRIPT,
        FIG_SLIDE,
        FIG_SPEED,
        FIG_STAMPATE,
        FIG_STAMPATELLO,
        FIG_STANDARD,
        FIG_SUB_ZERO,
        FIG_THIN,
        FIG_TICKS,
        FIG_TICKS_SLANT,
        FIG_TRAIN,
        FIG_TREK,
        FIG_USA_FLAG,
        FIG_WEIRD
    )

    /**
      * FIGLet fonts with the 7 rows height.
      */
    lazy val HEIGHT_7_FIGLET_FONTS = Seq(
        FIG_MODULAR,
        FIG_ALLIGATOR,
        FIG_ALLIGATOR2,
        FIG_ALPHABET,
        FIG_AMC_RAZOR,
        FIG_AMC_THIN,
        FIG_ANSI_REGULAR,
        FIG_ANSI_SHADOW,
        FIG_BANNER3,
        FIG_BANNER4,
        FIG_BOLGER,
        FIG_COMPUTER,
        FIG_DANCING_FONT,
        FIG_FENDER,
        FIG_FUN_FACE,
        FIG_FUN_FACES,
        FIG_FUZZY,
        FIG_GHOULISH,
        FIG_KBAN,
        FIG_OLD_BANNER,
        FIG_OS2,
        FIG_RAMMSTEIN,
        FIG_RED_PHOENIX,
        FIG_RELIEF,
        FIG_RELIEF2,
        FIG_ROUNDED,
        FIG_ROZZO,
        FIG_SCRIPT,
        FIG_SMALL_ISOMETRIC1,
        FIG_SMALL_POISON,
        FIG_SOFT,
        FIG_STACEY,
        FIG_STAR_WARS,
        FIG_STOP,
        FIG_THE_EDGE,
        FIG_THIS,
        FIG_TINKER_TOY,
        FIG_VARSITY,
        FIG_WET_LETTER
    )

    /**
      * FIGLet fonts with the 8 rows height.
      */
    lazy val HEIGHT_8_FIGLET_FONTS = Seq(
        FIG_3D,
        FIG_AMC_TUBES,
        FIG_AMC_UNTITLED,
        FIG_ARROWS,
        FIG_BANNER,
        FIG_BANNER3_D,
        FIG_BARBWIRE,
        FIG_BASIC,
        FIG_BIG,
        FIG_BIG_CHIEF,
        FIG_BLOCK,
        FIG_CATWALK,
        FIG_COINSTAK,
        FIG_CRAWFORD,
        FIG_CRAWFORD2,
        FIG_CRICKET,
        FIG_DIAMOND,
        FIG_DOOM,
        FIG_HENRY_3D,
        FIG_JACKY,
        FIG_KATAKANA,
        FIG_LEAN,
        FIG_LIL_DEVIL,
        FIG_MARQUEE,
        FIG_MERLIN1,
        FIG_NANCYJ,
        FIG_NANCYJ_FANCY,
        FIG_NANCYJ_IMPROVED,
        FIG_NANCYJ_UNDERLINED,
        FIG_NIPPLES,
        FIG_PEAKS,
        FIG_PUFFY,
        FIG_STELLAR,
        FIG_SWAMP_LAND,
        FIG_TANJA,
        FIG_TILES,
        FIG_TUBULAR,
        FIG_TWISTED
    )

    /**
      * FIGLet fonts with the 9 rows height.
      */
    lazy val HEIGHT_9_FIGLET_FONTS = Seq(
        FIG_AMC_RAZOR2,
        FIG_BEAR,
        FIG_CHISELED,
        FIG_DELTA_CORPS_PRIEST_1,
        FIG_EPIC,
        FIG_FIRE_FONT_K,
        FIG_FIRE_FONT_S,
        FIG_GHOST,
        FIG_GOTHIC,
        FIG_GRADIENT,
        FIG_GREEK,
        FIG_KEYBOARD,
        FIG_LARRY_3D,
        FIG_LARRY_3D_2,
        FIG_MERLIN2,
        FIG_NT_GREEK,
        FIG_PAWP,
        FIG_SLANT_RELIEF,
        FIG_STAR_STRIPS,
        FIG_STRONGER_THAN_ALL,
        FIG_SWAN
    )

    /**
      * FIGLet fonts with the 10 rows height.
      */
    lazy val HEIGHT_10_FIGLET_FONTS = Seq(
        FIG_3D_ASCII,
        FIG_AMC_NEKO,
        FIG_AMC_SLASH,
        FIG_BLOODY,
        FIG_DOT_MATRIX,
        FIG_DWHISTLED,
        FIG_FLOWER_POWER,
        FIG_HOLLYWOOD,
        FIG_JAZMINE,
        FIG_PEBBLES,
        FIG_ROMAN,
        FIG_TENGWAR,
        FIG_WHIMSY
    )

    /**
      * FIGLet fonts with the 11 rows height.
      */
    lazy val HEIGHT_11_FIGLET_FONTS = Seq(
        FIG_BIG_MONEY_NE,
        FIG_BIG_MONEY_NW,
        FIG_BLOCKS,
        FIG_BROADWAY,
        FIG_COLOSSAL,
        FIG_DOS_REBEL,
        FIG_GEORGIA11,
        FIG_ISOMETRIC1,
        FIG_ISOMETRIC2,
        FIG_ISOMETRIC3,
        FIG_ISOMETRIC4,
        FIG_REVERSE,
        FIG_UNIVERS
    )

    /**
      * FIGLet fonts with the 12 rows height.
      */
    lazy val HEIGHT_12_FIGLET_FONTS = Seq(
        FIG_ACROBATIC,
        FIG_BIG_MONEY_SE,
        FIG_BIG_MONEY_SW,
        FIG_ELECTRONIC,
        FIG_IMPOSSIBLE,
        FIG_PATORJK_HEX,
        FIG_POISON
    )

    /**
      * FIGLet fonts with the 13 rows height.
      */
    lazy val HEIGHT_13_FIGLET_FONTS = Seq( FIG_CRAZY,
        FIG_ICL_1900,
        FIG_SWEET)

    /**
      * FIGLet fonts with the 14 rows height.
      */
    lazy val HEIGHT_14_FIGLET_FONTS = Seq(
        FIG_PATORJK_CHEESE
    )

    /**
      * FIGLet fonts with the 15 rows height.
      */
    lazy val HEIGHT_15_FIGLET_FONTS = Seq(
        FIG_AMC_AAA01,
        FIG_FRAKTUR
    )

    /**
      * FIGLet fonts with the 16 rows height.
      */
    lazy val HEIGHT_16_FIGLET_FONTS = Seq(
        FIG_3D_DIAGONAL,
        FIG_DEF_LEPPARD,
        FIG_GEORGI16,
        FIG_NSCRIPT,
        FIG_NV_SCRIPT
    )

    /**
      * FIGLet fonts with the 20 rows height.
      */
    lazy val HEIGHT_20_FIGLET_FONTS = Seq(
        FIG_CALIGRAPHY2
    )

    /**
      * FIGLet fonts with the 21 rows height.
      */
    lazy val HEIGHT_21_FIGLET_FONTS = Seq(
        FIG_CALIGRAPHY
    )

    /**
      * FIGLet fonts with the 22 rows height.
      */
    lazy val HEIGHT_22_FIGLET_FONTS = Seq(
        FIG_ALPHA
    )

    /**
      * FIGLet fonts with the 25 rows height.
      */
    lazy val HEIGHT_25_FIGLET_FONTS = Seq(
        FIG_DOH
    )
