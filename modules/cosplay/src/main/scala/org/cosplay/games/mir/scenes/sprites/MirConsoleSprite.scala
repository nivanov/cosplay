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

package org.cosplay.games.mir.scenes.sprites

import org.cosplay.*
import CPPixel.*
import games.mir.*
import os.*
import CPKeyboardKey.*

import java.util.concurrent.CountDownLatch
import scala.util.*

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
  *
  */
class MirConsoleSprite extends CPCanvasSprite(id = "console") with MirConsole:
    import MirConsole.*

    private val keySnd = CPSound(s"$SND_HOME/keypress.wav", .05f)
    private val beepSnd = CPSound(s"$SND_HOME/beep.wav")
    private var beepCnt = 0
    private var isBeeping = false

    case class ZChar(ch: Char, fg: CPColor, bg: CPColor, z: Int):
        lazy val px: CPPixel = ch&&(fg, bg)

    private final val SPACE = ZChar(' ', FG, BG, Int.MinValue)
    private final val W = 300
    private final val H = 100
    private final val LAST_X = W - 1
    private final val LAST_Y = H - 1

    private final val CRS_BLINK_FRM_NUM = 13
    private final val TAB_SIZE = 8
    private final val mux = Object()

    private val buf = Array.ofDim[ZChar](H, W)
    private var bufX = 0 // X-coordinate within the buffer.
    private var bufY = 0 // Y-coordinate within the buffer.
    private var lastBufX = 0
    private var lastBufY = 0
    private var crsVis = true // Visible vs. non-visible cursor.
    private var crsSolid = true
    private var dim = CPDim(W, H)
    private var canvY = 0

    // Read line stuff.
    private var rlBuf: ReadLineBuffer = _
    private var rlMode = false
    private var rlLatch: CountDownLatch = _
    private var rlStartX = 0
    private var rlStartY = 0
    private var rlRepCh: Option[Char] = None

    /**
      *
      * @param maxLen
      * @param hist
      */
    class ReadLineBuffer(maxLen: Int, hist: Seq[String]):
        require(maxLen > 0)

        private var txt = ""
        private var pos = 0
        private var len = 0
        private val histLastIdx = hist.size - 1
        private var histIdx = histLastIdx
        private var savedBuf: Option[String] = None
        private var clear = false

        def moveLeft(): Unit = pos = 0.max(pos - 1)
        def moveRight(): Unit = pos = len.min(pos + 1)
        def needClear: Boolean = clear
        def cleared(): Unit = clear = false
        private def stripLen(): Int = txt.stripTrailing().length
        private def fromHistory(s: String): Unit =
            txt = s.stripTrailing()
            len = stripLen()
            clear = true
            moveEnd() // Move cursor to the last non-whitespace character.
        def historyUp(): Unit =
            if histIdx >= 0 then
                if histIdx == histLastIdx && savedBuf.isEmpty then savedBuf = txt.stripTrailing().?
                else if histIdx > 0 then histIdx -= 1
                fromHistory(hist(histIdx))
        def historyDown(): Unit =
            if histIdx == histLastIdx && savedBuf.isDefined then
                fromHistory(savedBuf.get)
                savedBuf = None
            else if histIdx < histLastIdx then
                histIdx += 1
                fromHistory(hist(histIdx))
        def moveHome(): Unit = pos = 0
        def moveEnd(): Unit = pos = len
        def getText: String = txt
        def getPos: Int = pos // Cursor position in the buffer.
        def insertChar(ch: Char): Unit =
            if len < maxLen then
                txt = s"${txt.substring(0, pos)}$ch${txt.substring(pos)}"
                pos += 1
                len = stripLen()
        private def delAtPos(): Unit = txt = s"${txt.substring(0, pos)}${txt.substring(pos + 1)} "
        def deleteChar(): Unit =
            if pos < len then
                delAtPos()
                len = stripLen()
        def backspace(): Unit =
            if pos > 0 then
                pos -= 1
                deleteChar()

    clear()

    // Is valid for buffer?
    inline private def isBufValid(x: Int, y: Int): Boolean = x >= 0 && x < W && y >= 0 && y < H

    override def clear(): Unit = for x <- 0 until W; y <- 0 until H do buf(y)(x) = SPACE
    override def clearLeft(): Unit = for x <- 0 until bufX do buf(bufY)(x) = SPACE
    override def clearRight(): Unit = for x <- bufX + 1 until W do buf(bufY)(x) = SPACE
    override def clearRow(): Unit = for x <- 0 until W do buf(x)(bufY) = SPACE
    override def clearColumn(): Unit = for y <- 0 until H do buf(y)(bufX) = SPACE
    override def clearAbove(): Unit = for y <- 0 until bufY do buf(y)(bufX) = SPACE
    override def clearBelow(): Unit = for y <- bufY + 1 until H do buf(y)(bufX) = SPACE
    override def isCursorVisible: Boolean = crsVis
    override def setCursorVisible(f: Boolean): Unit = crsVis = f

    // 'x' and 'y' are window coordinates.
    override def moveCursor(x: Int, y: Int): Unit =
        val y2 = y + canvY
        if isBufValid(x, y2) then mux.synchronized {
            bufX = x
            bufY = y2
        }
    override def getSize: CPDim = dim
    override def getCursorX: Int = bufX // Window X-coordinate.
    override def getCursorY: Int = bufY - canvY // Window Y-coordinate.
    // 'x' and 'y' are window coordinates.
    override def putChar(x: Int, y: Int, z: Int, ch: Char, fg: CPColor, bg: CPColor): Unit =
        ch match
            case CTRL_REV_COL => inverseColors()
            case CTRL_RST_COL => resetColors()
            case CTRL_BEEP => beepCnt += 1
            case _ =>
                val y2 = y + canvY
                if isBufValid(x, y2) then mux.synchronized {
                    val zch = buf(y2)(x)
                    if zch.z <= z then buf(y2)(x) = ZChar(ch, fg, bg, z)
                }

    override def readLine(repCh: Option[Char], maxLen: Int, hist: Seq[String]): String =
        require(!rlMode)
        rlLatch = CountDownLatch(1)
        rlStartX = bufX
        rlStartY = bufY
        rlRepCh = repCh
        rlBuf = new ReadLineBuffer(maxLen, hist)
        rlMode = true

        while rlLatch.getCount > 0 do
            try rlLatch.await()
            catch case _: InterruptedException => ()

        rlMode = false
        rlBuf.getText.strip()

    override def update(ctx: CPSceneObjectContext): Unit =
        super.update(ctx)
        if rlMode then
            require(rlBuf != null)
            mux.synchronized {
                ctx.getKbEvent match
                    case None => ()
                    case Some(evt) =>
                        if stateMgr.state.crtAudio then keySnd.play()
                        val key = evt.key
                        if key.isPrintable then rlBuf.insertChar(key.ch)
                        else if key == KEY_ENTER then rlLatch.countDown()
                        else if key == KEY_DEL then rlBuf.deleteChar()
                        else if key == KEY_HOME then rlBuf.moveHome()
                        else if key == KEY_END then rlBuf.moveEnd()
                        else if key == KEY_LEFT then rlBuf.moveLeft()
                        else if key == KEY_RIGHT then rlBuf.moveRight()
                        else if key == KEY_UP then rlBuf.historyUp()
                        else if key == KEY_DOWN then rlBuf.historyDown()
                        else if key == KEY_BACKSPACE then rlBuf.backspace()
            }

    override def render(ctx: CPSceneObjectContext): Unit =
        super.render(ctx)
        val canv = ctx.getCanvas

        // Handle beeps.
        if beepCnt > 0 && !isBeeping then
            isBeeping = true
            beepSnd.play(endFun = _ => {
                beepCnt -= 1
                isBeeping = false
            })

        mux.synchronized {
            dim = canv.dim

            if rlMode then
                if rlBuf.needClear then
                    // Clear everything below-and-after read-line start point.
                    for x <- 0 until W; y <- rlStartY until H do
                        if !(y == rlStartY && x < rlStartX) then buf(y)(x) = SPACE
                    rlBuf.cleared()

                bufX = rlStartX
                bufY = rlStartY

                var savedX = -1
                var savedY = -1
                var i = 0
                val rlPos = rlBuf.getPos
                for ch <- rlBuf.getText do
                    buf(bufY)(bufX) = if ch == ' ' then SPACE else ZChar(rlRepCh.getOrElse(ch), getFg, getBg, Int.MaxValue)
                    if i == rlPos then
                        savedX = bufX
                        savedY = bufY
                    i += 1
                    advanceCursor()

                if savedX != -1 && savedY != -1 then
                    bufX = savedX
                    bufY = savedY

            val w = dim.w.min(W)
            val h = dim.h.min(H)

            canvY = if bufY < h then 0 else bufY - h + 1

            var y = 0
            while y < h do
                var x = 0
                while x < w do
                    val zch = buf(y + canvY)(x)
                    canv.drawPixel(zch.px, x, y, zch.z)
                    x += 1
                y += 1

            if ctx.getFrameCount % CRS_BLINK_FRM_NUM == 0 then crsSolid = !crsSolid
            if lastBufY != bufY || lastBufX != bufX then crsSolid = true // Force solid state on cursor move.
            if crsSolid && crsVis then canv.inversePixel(bufX, bufY - canvY)

            lastBufX = bufX
            lastBufY = bufY
        }

    /**
      * Advances cursor respecting current window dimension.
      */
    private def advanceCursor(): Unit =
        require(Thread.holdsLock(mux))
        val w = dim.w.min(W)
        if bufX < w - 1 then bufX += 1
        else if bufY < LAST_Y then
            bufX = 0
            bufY += 1
        else
            // Scroll up the screen by 1 row.
            for y <- 1 until H do Array.copy(buf(y), 0, buf(y - 1), 0, W)
            // Clear the last row.
            for x <- 0 until W do buf(LAST_Y)(x) = SPACE
            bufX = 0
            rlStartY = 0.max(rlStartY - 1)

    override def print(x: Any): Unit =
        mux.synchronized {
            def put(ch: Char): Unit =
                putChar(getCursorX, getCursorY, ch)
                if !isControl(ch) then advanceCursor()
            x.toString.foreach(ch => ch match
                case '\r' => bufX = 0 // For Win-compatibility.
                case '\n' =>
                    bufX = LAST_X
                    advanceCursor()
                case '\t' => (0 until TAB_SIZE).foreach(_ => put(' '))
                case _ => put(ch)
            )
        }