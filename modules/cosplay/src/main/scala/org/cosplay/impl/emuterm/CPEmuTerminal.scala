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

package org.cosplay.impl.emuterm

import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme
import org.kordamp.ikonli.lineawesome.LineAwesomeSolid.*
import com.formdev.flatlaf.*
import org.apache.commons.lang3.SystemUtils
import org.cosplay.*
import org.cosplay.CPKeyboardKey.*
import org.cosplay.CPColor.*
import org.cosplay.impl.*
import org.cosplay.impl.guilog.CPGuiLog
import org.kordamp.ikonli.Ikon
import org.kordamp.ikonli.swing.FontIcon

import java.awt.*
import java.awt.Frame.*
import java.awt.RenderingHints.*
import java.awt.event.*
import java.awt.event.KeyEvent.*
import java.awt.image.BufferedImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import javax.swing.*
import javax.swing.JFrame.*
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
               All rights reserved.
*/

/**
  * Terminal emulation properties:
  * - COSPLAY_EMUTERM_FONT_NAME
  * - COSPLAY_EMUTERM_FONT_SIZE
  * - COSPLAY_EMUTERM_CH_WIDTH_OFFSET
  * - COSPLAY_EMUTERM_CH_HEIGHT_OFFSET
  * - COSPLAY_EMUTERM_ANTIALIAS
  */
class CPEmuTerminal(gameInfo: CPGameInfo) extends CPTerminal:
    private val isKbLog = CPUtils.sysEnvBool("COSPLAY_KB_LOG")
    private val INIT_PXS_SIZE = 100 * 100
    private val INIT_GLYPHS_SIZE = 1000
    private val GLYPHS_LOAD_FACTOR = 0.2
    private val DFLT_DIM = CPDim(100, 50)
    private var frame: JFrame = _
    private var panel: JPanel = _
    private val fontName = CPUtils.sysEnv("COSPLAY_EMUTERM_FONT_NAME").getOrElse("Monospaced")
    private val fontSize = Integer.decode(CPUtils.sysEnv("COSPLAY_EMUTERM_FONT_SIZE").getOrElse("14"))
    private var chWOff = Integer.decode(CPUtils.sysEnv("COSPLAY_EMUTERM_CH_WIDTH_OFFSET").getOrElse("0"))
    private val chHOff = Integer.decode(CPUtils.sysEnv("COSPLAY_EMUTERM_CH_HEIGHT_OFFSET").getOrElse("0"))
    private val bg = gameInfo.termBg.awt
    private var termFont: Font = _
    private var curDim: CPDim = gameInfo.initDim.getOrElse(DFLT_DIM)
    private var chW = -1
    private var chH = -1
    private var descent = 0
    private val pxs = new mutable.ArrayBuffer[CPPosPixel](INIT_PXS_SIZE)
    private var bufImg: BufferedImage = _
    private val glyphCache = new mutable.HashMap[CPPixel, BufferedImage](INIT_GLYPHS_SIZE, GLYPHS_LOAD_FACTOR)
    private var kbKey = none[CPKeyboardKey]
    private val renderMux = new Object
    private val kbMux = new Object
    private val isAA = CPUtils.isSysEnvSet("COSPLAY_EMUTERM_ANTIALIAS")
    private val root = new CPGuiLog("")
    private val pauseGameAct: Action = mkAction(
        "Pause",
        Some(mkIcon(PAUSE)),
        "Pause game play.",
        enabled = true,
        _ => {
            CPEngine.pauseGame()
            pauseGameAct.setEnabled(false)
            resumeGameAct.setEnabled(true)
        })
    private val resumeGameAct: Action = mkAction(
        "Resume",
        Some(mkIcon(PLAY)),
        "Resume game play.",
        enabled = false,
        _ => {
            CPEngine.resumeGame()
            pauseGameAct.setEnabled(true)
            resumeGameAct.setEnabled(false)
        })
    private val openLogAct: Action = mkAction(
        "Open Log",
        None,
        "Open log window.",
        enabled = true,
        _ => {
            CPEngine.rootLog().info(CPUtils.PING_MSG)
        })
    private val stopGameAct: Action = mkAction(
        "Stop Game",
        Some(mkIcon(TIMES)),
        "Stop and exit game.",
        enabled = true,
        _ => {
            if JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to stop and exit the game?",
                "Exit Game",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                mkIcon(QUESTION, C_LIGHT_GOLDEN_ROD1, 32)
            ) == 0 then
                CPEngine.exitGame()
        })

    /**
      *
      */
    private def initFontMetrics(): Unit =
        val fonts = GraphicsEnvironment.getLocalGraphicsEnvironment.getAllFonts.toList
        val fontNames = (fonts.map(_.getFontName) ++ fonts.map(_.getName) ++ fonts.map(_.getFamily)).distinct
        if !fontNames.contains(fontName) then raise(s"Unknown emulator font name: $fontName")
        termFont = new Font(fontName, Font.PLAIN, fontSize)
        val g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).createGraphics()
        configFont(g)
        val fm = g.getFontMetrics(termFont)
        descent = fm.getMaxDescent
        val maxW = fm.getWidths.max
        val maxH = fm.getHeight

        // Fix sometime incorrect font width (on MacOS or Linux).
        if !CPUtils.isSysEnvSet("COSPLAY_EMUTERM_CH_WIDTH_OFFSET") && maxW > maxH / 2 then
            chWOff = -(maxW - maxH / 2)

        chW = maxW + chWOff
        chH = maxH - fm.getLeading + chHOff
        g.dispose()

    /**
     *
     * @param ikon
     * @param fg
     * @param size
     */
    private def mkIcon(ikon: Ikon, fg: CPColor, size: Int): Icon =
        FontIcon.of(
            ikon,
            size,
            fg.awt
        )

    /**
     *
     * @param ikon
     */
    private def mkIcon(ikon: Ikon): Icon =
        mkIcon(
            ikon,
            C_WHITE,
            UIManager.get("Button.font").asInstanceOf[Font].getSize
        )

    /**
      *
      * @param g
      * @param c
      * @param x X-coordinate
      * @param y Y-coordinate
      * @param w
      * @param h
      */
    private def fillRect(g: Graphics2D, c: Color, x: Int, y: Int, w: Int, h: Int): Unit =
        g.setColor(c)
        g.fillRect(x, y, w, h)

    /**
      *
      * @param g
      */
    private def configFont(g: Graphics2D): Unit =
        g.setFont(termFont)
        if isAA || SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC then
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)

    /**
      *
      * @param txt
      * @param icon
      * @param tooltip
      * @param enabled
      * @param f
      */
    private def mkAction(
        txt: String,
        icon: Option[Icon],
        tooltip: String,
        enabled: Boolean,
        f: ActionEvent => Unit): Action =
        val act = new AbstractAction(txt):
            override def actionPerformed(e: ActionEvent): Unit = f(e)
        act.setEnabled(enabled)
        act.putValue(Action.SHORT_DESCRIPTION, tooltip)
        if icon.isDefined then act.putValue(Action.SMALL_ICON, icon.get)
        act

    /**
      *
      * @param act
      */
    private def mkMenuItem(act: Action): JMenuItem =
        val mi = new JMenuItem(act)
        mi.setFocusPainted(false)
        mi

    /**
      *
      * @param w
      * @param h
      */
    private def safeDim(w: Int, h: Int): Dimension =
        val scrSz = Toolkit.getDefaultToolkit.getScreenSize
        val safeW = if w >= scrSz.width then (scrSz.width * 0.8).round.toInt else w
        val safeH = if h >= scrSz.height then (scrSz.height * 0.8).round.toInt else h
        new Dimension(safeW, safeH)

    /**
      *
      */
    private def init(): Unit =
        CPUtils.initLaF()
        initFontMetrics()

        // Setup frame and panel inside it.
        frame = new JFrame
        panel = new JPanel:
            override def paintComponent(g: Graphics): Unit =
                super.paintComponent(g)
                val sz = panel.getSize()
                val w = sz.width
                val h = sz.height
                if bufImg != null && (w != bufImg.getWidth() || h != bufImg.getHeight()) then bufImg = null
                if bufImg == null then bufImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)
                val g2 = bufImg.createGraphics()
                fillRect(g2, bg, 0, 0, w, h)
                renderMux.synchronized:
                    if pxs.nonEmpty then
                        for px <- pxs do
                            val glyph = glyphCache.get(px.px) match
                                case Some(img) => img
                                case None =>
                                    val img = new BufferedImage(chW, chH, BufferedImage.TYPE_INT_RGB)
                                    val g = img.createGraphics()
                                    configFont(g)
                                    if px.bg.isDefined then fillRect(g, px.bg.get.awt, 0, 0, chW, chH)
                                    g.setColor(px.fg.awt)
                                    g.drawString(s"${px.char}", 0, chH - descent)
                                    g.dispose()
                                    glyphCache.put(px.px, img)
                                    img
                            g2.drawImage(glyph, px.x, px.y, chW, chH, null)
                g2.dispose()
                if bufImg != null then g.asInstanceOf[Graphics2D].drawImage(bufImg, 0, 0, w, h, null)

        // Clear up focus traverse key bindings.
        val empty = new java.util.HashSet()
        frame.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, empty)
        frame.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, empty)
        frame.setFocusTraversalKeys(KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS, empty)
        frame.setFocusTraversalKeys(KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS, empty)

        frame.addKeyListener(new KeyAdapter:
            private val tk = Toolkit.getDefaultToolkit
            override def keyPressed(e: KeyEvent): Unit =
                def shift(shiftKey: CPKeyboardKey, normKey: CPKeyboardKey): CPKeyboardKey =
                    val capslock = tk.getLockingKeyState(KeyEvent.VK_CAPS_LOCK)
                    if e.isShiftDown || capslock then shiftKey else normKey
                def ctrl(ctrlKey: CPKeyboardKey, normKey: CPKeyboardKey): CPKeyboardKey = if e.isControlDown then ctrlKey else normKey

                if isKbLog then println(e)
                kbKey = kbMux.synchronized:
                    Option(e.getKeyCode match
                        case VK_ENTER => KEY_ENTER
                        case VK_BACK_SPACE => KEY_BACKSPACE
                        case VK_TAB => KEY_TAB
                        case VK_ESCAPE => KEY_ESC
                        case VK_SPACE => KEY_SPACE

                        case VK_F1 => KEY_F1
                        case VK_F2 => KEY_F2
                        case VK_F3 => KEY_F3
                        case VK_F4 => KEY_F4
                        case VK_F5 => KEY_F5
                        case VK_F6 => KEY_F6
                        case VK_F7 => KEY_F7
                        case VK_F8 => KEY_F8
                        case VK_F9 => KEY_F9
                        case VK_F10 => KEY_F10
                        case VK_F11 => KEY_F11
                        case VK_F12 => KEY_F12

                        case VK_1 => shift(KEY_EXCL, KEY_1)
                        case VK_2 => shift(KEY_AT, KEY_2)
                        case VK_3 => shift(KEY_NUMBER_SIGN, KEY_3)
                        case VK_4 => shift(KEY_DOLLAR, KEY_4)
                        case VK_5 => shift(KEY_PERCENT, KEY_5)
                        case VK_6 => shift(KEY_CIRCUMFLEX, KEY_6)
                        case VK_7 => shift(KEY_AMPERSAND, KEY_7)
                        case VK_8 => shift(KEY_MULTIPLY, KEY_8)
                        case VK_9 => shift(KEY_LPAR, KEY_9)
                        case VK_0 => shift(KEY_RPAR, KEY_0)

                        case VK_INSERT => KEY_INS
                        case VK_DELETE => KEY_DEL
                        case VK_PAGE_UP => KEY_PGUP
                        case VK_PAGE_DOWN => KEY_PGDN
                        case VK_END => KEY_END
                        case VK_HOME => KEY_HOME

                        case VK_LEFT => ctrl(KEY_CTRL_LEFT, KEY_LEFT)
                        case VK_UP => ctrl(KEY_CTRL_UP, KEY_UP)
                        case VK_RIGHT => ctrl(KEY_CTRL_RIGHT, KEY_RIGHT)
                        case VK_DOWN => ctrl(KEY_CTRL_DOWN, KEY_DOWN)

                        case VK_OPEN_BRACKET => shift(KEY_LCBRKT, KEY_LBRKT)
                        case VK_CLOSE_BRACKET => shift(KEY_RCBRKT, KEY_RBRKT)
                        case VK_COMMA => shift(KEY_LT, KEY_COMMA)
                        case VK_PERIOD => shift(KEY_GT, KEY_PERIOD)
                        case VK_SLASH => shift(KEY_QUESTION, KEY_SLASH)
                        case VK_BACK_SLASH => shift(KEY_VERT, KEY_BACK_SLASH)
                        case VK_QUOTE => shift(KEY_DQUOTE, KEY_SQUOTE)
                        case VK_BACK_QUOTE => shift(KEY_TILDE, KEY_BACK_QUOTE)
                        case VK_MINUS => shift(KEY_UNDERSCORE, KEY_MINUS)
                        case VK_EQUALS => shift(KEY_PLUS, KEY_EQUAL)
                        case VK_SEMICOLON => shift(KEY_COLON, KEY_SEMICOLON)

                        case VK_A => ctrl(KEY_CTRL_A, shift(KEY_UP_A, KEY_LO_A))
                        case VK_B => ctrl(KEY_CTRL_B, shift(KEY_UP_B, KEY_LO_B))
                        case VK_C => ctrl(KEY_CTRL_C, shift(KEY_UP_C, KEY_LO_C))
                        case VK_D => ctrl(KEY_CTRL_D, shift(KEY_UP_D, KEY_LO_D))
                        case VK_E => ctrl(KEY_CTRL_E, shift(KEY_UP_E, KEY_LO_E))
                        case VK_F => ctrl(KEY_CTRL_F, shift(KEY_UP_F, KEY_LO_F))
                        case VK_G => ctrl(KEY_CTRL_G, shift(KEY_UP_G, KEY_LO_G))
                        case VK_H => ctrl(KEY_CTRL_H, shift(KEY_UP_H, KEY_LO_H))
                        case VK_I => ctrl(KEY_CTRL_I, shift(KEY_UP_I, KEY_LO_I))
                        case VK_J => ctrl(KEY_CTRL_J, shift(KEY_UP_J, KEY_LO_J))
                        case VK_K => ctrl(KEY_CTRL_K, shift(KEY_UP_K, KEY_LO_K))
                        case VK_L => ctrl(KEY_CTRL_L, shift(KEY_UP_L, KEY_LO_L))
                        case VK_M => ctrl(KEY_CTRL_M, shift(KEY_UP_M, KEY_LO_M))
                        case VK_N => ctrl(KEY_CTRL_N, shift(KEY_UP_N, KEY_LO_N))
                        case VK_O => ctrl(KEY_CTRL_O, shift(KEY_UP_O, KEY_LO_O))
                        case VK_P => ctrl(KEY_CTRL_P, shift(KEY_UP_P, KEY_LO_P))
                        case VK_Q => ctrl(KEY_CTRL_Q, shift(KEY_UP_Q, KEY_LO_Q))
                        case VK_R => ctrl(KEY_CTRL_R, shift(KEY_UP_R, KEY_LO_R))
                        case VK_S => ctrl(KEY_CTRL_S, shift(KEY_UP_S, KEY_LO_S))
                        case VK_T => ctrl(KEY_CTRL_T, shift(KEY_UP_T, KEY_LO_T))
                        case VK_U => ctrl(KEY_CTRL_U, shift(KEY_UP_U, KEY_LO_U))
                        case VK_V => ctrl(KEY_CTRL_V, shift(KEY_UP_V, KEY_LO_V))
                        case VK_W => ctrl(KEY_CTRL_W, shift(KEY_UP_W, KEY_LO_W))
                        case VK_X => ctrl(KEY_CTRL_X, shift(KEY_UP_X, KEY_LO_X))
                        case VK_Y => ctrl(KEY_CTRL_Y, shift(KEY_UP_Y, KEY_LO_Y))
                        case VK_Z => ctrl(KEY_CTRL_Z, shift(KEY_UP_Z, KEY_LO_Z))

                        case _ => null
                    )
        )
        panel.addComponentListener:
            new ComponentAdapter:
                override def componentResized(e: ComponentEvent): Unit =
                    val sz = panel.getSize()
                    val newDim = CPDim(sz.width / chW, sz.height / chH)
                    if newDim != curDim then
                        curDim = CPDim(sz.width / chW, sz.height / chH)
                        bufImg = null
        panel.setBackground(bg)
        panel.setPreferredSize(safeDim(curDim.w * chW, curDim.h * chH))
        panel.setMinimumSize(new Dimension(10 * chW, 10 * chH)) // 10x10 char is minimum dimension.
        frame.setBackground(bg)
        frame.getContentPane.setBackground(bg)
        frame.getContentPane.add(panel, BorderLayout.CENTER)
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

        val popup = new JPopupMenu()
        popup.add(mkMenuItem(pauseGameAct))
        popup.add(mkMenuItem(resumeGameAct))
        popup.addSeparator()
        popup.add(mkMenuItem(stopGameAct))
        popup.addSeparator()
        popup.add(mkMenuItem(openLogAct))

        panel.setComponentPopupMenu(popup)

        try
            val img = new ImageIcon(getClass.getResource("/images/cosplay.png")).getImage
            try Taskbar.getTaskbar.setIconImage(img)
            catch case _: Exception => ()
            frame.setIconImage(img)
        catch case _: Exception => ()

        frame.pack()
        frame.setMinimumSize(new Dimension(80 * chW, 20 * chH))
        frame.setLocationRelativeTo(null) // Center on the screen.
        frame.setVisible(true) // Show.

    override def render(scr: CPScreen, camRect: CPRect, forceRedraw: Boolean): Unit =
        !>(scr.getRect.contains(camRect), s"Screen: ${scr.getRect} does not contain camera: $camRect")

        val tw = curDim.w
        val th = curDim.h
        val cw = camRect.w
        val ch = camRect.h
        val effW = tw.min(cw)
        val effH = th.min(ch)
        val effRect = CPRect(camRect.x, camRect.y, effW, effH)
        // Centering camera frame in terminal window.
        val offX = if tw > cw then (tw - cw) / 2 * chW else 0
        val offY = if th > ch then (th - ch) / 2 * chH else 0

        renderMux.synchronized:
            pxs.clear()
            effRect.loop((x, y) =>
                val px = scr.getPixel(x, y).px
                if !px.isXray then
                    pxs += CPPosPixel(
                        px,
                        offX + chW * (x - effRect.x), // Screen pixel-level coordinate.
                        offY + chH * (y - effRect.y) // Screen pixel-level coordinate.
                    )
            )

        panel.repaint()

    override val isNative: Boolean = false
    override def getRootLog: CPLog = root
    override def dispose(): Unit = frame.dispose()
    override def getDim: CPDim = curDim
    override def setTitle(s: String): Unit = frame.setTitle(s)
    override def nativeKbRead(timeoutMs: Long): Int = assert(assertion = false, "Unsupported.")
    override def kbRead(): Option[CPKeyboardKey] =
        kbMux.synchronized:
            kbKey.flatMap(key =>
                kbKey = None
                key.clear()
                key.?
            )

    init()


