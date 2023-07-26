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

package org.cosplay.impl.jlineterm

import org.apache.commons.lang3.SystemUtils
import org.cosplay.*
import org.cosplay.CPColor.*
import org.cosplay.CPPixel.*
import org.cosplay.impl.CPAnsi.*
import org.cosplay.impl.guilog.CPGuiLog
import org.cosplay.impl.*
import org.cosplay.impl.jlineterm.CPJLineTerminal.restore
import org.jline.terminal.*
import org.jline.utils.NonBlockingReader

import java.io.*
import java.util.logging.LogManager
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

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

object CPJLineTerminal:
    def restore(w: PrintWriter): Unit =
        w.write(WIN_TITLE_REST)
        w.write(USE_PRI_SCR_BUF)
        w.write(RESET_ALL)
        w.write(CUR_REST)
        w.write(CUR_SHOW)
        w.flush()

/**
  * 
  */
class CPJLineTerminal(gameInfo: CPGameInfo) extends CPTerminal:
    private var term: Terminal = _
    private var writer: PrintWriter = _
    private var reader: NonBlockingReader = _
    private val buf = new mutable.StringBuilder(10000)
    private val bg = gameInfo.termBg
    private var last: CPArray2D[CPPixel] = _ // Copy of the last drawn camera frame.
    private val root = new CPGuiLog("")
    // Background pixel in case terminal window is bigger than camera frame.
    private val bgPx = ' '&&(CPColor.C_BLACK, bg)
    @volatile private var curDim: CPDim = CPDim.ZERO
    private var termDimReader: TermDimensionReader = _

    /**
      *
      * @param termDim
      * @param scr
      * @param camRect
      */
    private class TermScreen(termDim: CPDim, scr: CPScreen, camRect: CPRect):
        !>(scr.getRect.contains(camRect))

        private val termRect = new CPRect(0, 0, termDim) // Exact terminal window.
        private val termCamRect = centerCamRect() // Camera rect positioned (centered) within terminal window.
        private val xOff = camRect.x - termCamRect.x
        private val yOff = camRect.y - termCamRect.y

        /**
          *
          */
        private def centerCamRect(): CPRect =
            val camDim = camRect.dim
            var x, y = 0
            var w = camDim.w
            var h = camDim.h
            if termDim.w > camDim.w then
                x = (termDim.w - camDim.w) / 2
            else if termDim.w < camDim.w then
                w = termDim.w
            if termDim.h > camDim.h then
                y = (termDim.h - camDim.h) / 2
            else if termDim.h < camDim.h then
                h = termDim.h
            CPRect(x, y, w, h)

        def getPx(x: Int, y: Int): CPPixel = if termCamRect.contains(x, y) then scr.getPixel(x + xOff, y + yOff).px else bgPx
        def loop(f: (Int, Int) => Unit): Unit = termRect.loop(f)

    private class TermDimensionReader extends Thread:
        @volatile var st0p = false

        obtainDim()

        private def obtainDim(): Unit =
            assert(term != null)
            val termSz = term.getSize
            curDim =
                if termSz.getColumns == 0 || termSz.getRows == 0 then
                    raise(s"Failed to get '${term.getName}/${term.getType}' terminal window size.")
                else
                    CPDim(termSz.getColumns, termSz.getRows)

        override def run(): Unit =
            while !st0p do
                obtainDim()
                try Thread.sleep(250.ms)
                catch case _: InterruptedException => ()

    override def render(scr: CPScreen, camRect: CPRect, forceRedraw: Boolean): Unit =
        !>(scr.getRect.contains(camRect))

        val termDim = getDim
        val termScr = new TermScreen(termDim, scr, camRect)

        if forceRedraw || last == null || last.dim != termDim then
            last = new CPArray2D[CPPixel](termDim)
            redrawToAnsi(termScr)
        else
            diffToAnsi(termScr)

    override val isNative: Boolean = true
    override def getDim: CPDim = curDim
    override def nativeKbRead(timeoutMs: Long): Int = reader.read(timeoutMs)
    override def kbRead(): Option[CPKeyboardKey] = assert(assertion = false, "Unsupported.")

    private def write(s: String): Unit =
        writer.print(s)
        term.flush()
    private def addPx(x: Int, y: Int, px: CPPixel): Unit =
        if !px.isXray then
            buf ++= curPos(x, y)
            buf ++= px.ansi
            last.set(x, y, px)

    /**
      * Fully redraws screen with given camera frame.
      *
      * @param scr Term screen.
      */
    private def redrawToAnsi(scr: TermScreen): Unit =
        draw(scr, (x, y) => addPx(x, y, scr.getPx(x, y)))

    /**
      * Redraws only the difference between last camera frame and the given one.
      *
      * @param scr Current entire screen.
      */
    private def diffToAnsi(scr: TermScreen): Unit =
        draw(scr, (x, y) => {
            val px = scr.getPx(x, y)
            val lastPx = last.get(x, y)
            if px != lastPx then
                addPx(x, y, px)
        })

    private def draw(scr: TermScreen, f: (Int, Int) => Unit): Unit =
        buf.clear()
        buf ++= CUR_HIDE
        scr.loop((x, y) => f(x, y))
        write(buf.toString)
    private def init(): Unit =
        term = TerminalBuilder.builder()
            .name("cosplay")
            .system(true)
            .nativeSignals(true)
            .signalHandler(
                new Terminal.SignalHandler:
                    def handle(sig: Terminal.Signal): Unit = ()
            )
            .dumb(false)
            .`type`("xterm-256color")
            .build()

        // Set raw mode.
        term.enterRawMode()

        writer = term.writer()
        reader = term.reader()

        termDimReader = TermDimensionReader()
        termDimReader.start()

        write(WIN_TITLE_SAVE)
        write(CUR_SAVE)
        write(USE_ALT_SCR_BUF)
        write(CUR_HIDE)
        write(CLR_SCR)

        // Make sure to restore terminal after non-standard exit.
        sys.addShutdownHook(restore(term.writer()))

    override def getRootLog: CPLog = root
    override def setTitle(title: String): Unit = write(winTitle(title))
    override def dispose(): Unit =
        assert(term != null)

        if termDimReader != null then
            termDimReader.st0p = true
            termDimReader.interrupt()
            termDimReader.join()
        try reader.close()
        catch case _: IOException => ()
        reader.shutdown()
        restore(writer)
        if SystemUtils.IS_OS_WINDOWS then write(WIN_TERM_RESET) // Windows-specific terminal reset.
        term.close() // Close terminal to reset it to original state.

    // Init the terminal.
    init()
