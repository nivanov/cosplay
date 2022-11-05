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
    private final val CUR_PX = ' '&&(FG, FG)
    private final val CUR_BLINK_FRM_NUM = 13
    private final val TAB_SIZE = 8
    private final val mux = Object()
    private val pane = Array.ofDim[ZChar](H, W)
    private var curX = 0
    private var curY = 0
    private var lastCurX = 0
    private var lastCurY = 0
    private var curVis = true // Visible vs. non-visible.
    private var curSolid = true
    private var dim = CPDim(W, H)
    private var canvY = 0

    // Read line staff.
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

        private var buf = ""
        private var pos = 0
        private var len = 0
        private val histLastIdx = hist.size - 1
        private var histIdx = histLastIdx
        private var savedBuf = ""

        def moveLeft(): Unit = pos = 0.max(pos - 1)
        def moveRight(): Unit = pos = len.min(pos + 1)
        private def fromHistory(s: String): Unit =
            val curLen = buf.length
            buf = s.padTo(curLen, ' ')
            len = buf.stripTrailing().length
            pos = len

        def historyUp(): Unit =
            if histIdx > 0 then
                if histIdx == histLastIdx then savedBuf = buf.stripTrailing()
                fromHistory(hist(histIdx))
                histIdx -= 1
        def historyDown(): Unit =
            if histIdx < histLastIdx then ()
            else if histIdx == histLastIdx then
                histIdx -= 1
                fromHistory(if histIdx == 0 then savedBuf else hist(histIdx))
        def moveHome(): Unit = pos = 0
        def moveEnd(): Unit = pos = len
        def getText: String = buf
        def getPos: Int = pos
        def insertChar(ch: Char): Unit =
            if len < maxLen then
                buf = s"${buf.substring(0, pos)}$ch${buf.substring(pos)}"
                pos += 1
                len = buf.stripTrailing().length
        def deleteChar(): Unit =
            if pos < len then
                buf = s"${buf.substring(0, pos)}${buf.substring(pos + 1)} "
                len = buf.stripTrailing().length
        def backspace(): Unit =
            if pos > 0 then
                pos -= 1
                buf = s"${buf.substring(0, pos)}${buf.substring(pos + 1)} "
                len = buf.stripTrailing().length

    clear()

    inline private def isPositionValid(x: Int, y: Int): Boolean = x >= 0 && x < W && y >= 0 && y < H

    override def clear(): Unit = for x <- 0 until W; y <- 0 until H do pane(y)(x) = SPACE
    override def clearLeft(): Unit = for x <- 0 until curX do pane(curY)(x) = SPACE
    override def clearRight(): Unit = for x <- curX + 1 until W do pane(curY)(x) = SPACE
    override def clearRow(): Unit = for x <- 0 until W do pane(x)(curY) = SPACE
    override def clearColumn(): Unit = for y <- 0 until H do pane(y)(curX) = SPACE
    override def clearAbove(): Unit = for y <- 0 until curY do pane(y)(curX) = SPACE
    override def clearBelow(): Unit = for y <- curY + 1 until H do pane(y)(curX) = SPACE
    override def isCursorVisible: Boolean = curVis
    override def setCursorVisible(f: Boolean): Unit = curVis = f
    override def moveCursor(x: Int, y: Int): Unit =
        val y2 = y + canvY
        if isPositionValid(x, y2) then mux.synchronized {
            curX = x
            curY = y2
        }
    override def getSize: CPDim = dim
    override def getCursorX: Int = curX
    override def getCursorY: Int = curY - canvY
    override def putChar(x: Int, y: Int, z: Int, ch: Char, fg: CPColor, bg: CPColor): Unit =
        ch match
            case CTRL_REV_COL => inverseColors()
            case CTRL_RST_COL => resetColors()
            case CTRL_BEEP => beepCnt += 1
            case _ =>
                val y2 = y + canvY
                if isPositionValid(x, y2) then mux.synchronized {
                    val zch = pane(y2)(x)
                    if zch.z <= z then pane(y2)(x) = ZChar(ch, fg, bg, z)
                }

    override def readLine(repCh: Option[Char], maxLen: Int, hist: Seq[String]): String =
        require(!rlMode)
        rlLatch = CountDownLatch(1)
        rlStartX = curX
        rlStartY = curY
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
                curX = rlStartX
                curY = rlStartY
                var saveCurX = curX
                var saveCurY = curY
                var i = 0
                val bufPos = rlBuf.getPos
                for ch <- rlBuf.getText do
                    pane(curY)(curX) = ZChar(if ch == ' ' then ch else rlRepCh.getOrElse(ch), getFg, getBg, Int.MaxValue)
                    if i == bufPos then
                        saveCurX = curX
                        saveCurY = curY
                    i += 1
                    advanceCursor(dim.w)

                if i != bufPos then
                    curX = saveCurX
                    curY = saveCurY

            canvY = if curY < canv.h then 0 else curY - canv.h + 1
            val w = canv.w.min(W)
            val h = canv.h

            var y = 0
            while y < h do
                var x = 0
                while x < w do
                    val zch = pane(y + canvY)(x)
                    canv.drawPixel(zch.px, x, y, zch.z)
                    x += 1
                y += 1

            if ctx.getFrameCount % CUR_BLINK_FRM_NUM == 0 then curSolid = !curSolid
            if lastCurY != curY || lastCurX != curX then curSolid = true // Force solid state on move.
            if curSolid && curVis then canv.drawPixel(CUR_PX, curX, curY - canvY, Int.MaxValue)

            lastCurX = curX
            lastCurY = curY
        }

    /**
      *
      * @param w Actual width to use.
      */
    private def advanceCursor(w: Int): Unit =
        require(Thread.holdsLock(mux))

        if curX < w - 1 then curX += 1
        else if curY < LAST_Y then
            curX = 0
            curY += 1
        else
            for y <- 1 until H do Array.copy(pane(y), 0, pane(y - 1), 0, W)
            for x <- 0 until W do pane(LAST_Y)(x) = SPACE
            curX = 0

    override def print(x: Any): Unit =
        mux.synchronized {
            def put(ch: Char): Unit =
                putChar(getCursorX, getCursorY, ch)
                if !isControl(ch) then advanceCursor(W)
            x.toString.foreach(ch => ch match
                case '\r' => curX = 0 // For Win-compatibility just in case.
                case '\n' =>
                    curX = LAST_X
                    advanceCursor(W)
                case '\t' => (0 until TAB_SIZE).foreach(_ => put(' '))
                case _ => put(ch)
            )
        }