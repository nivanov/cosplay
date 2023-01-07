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

package org.cosplay.impl.guilog

import com.formdev.flatlaf.*
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDarkerIJTheme
import com.formdev.flatlaf.intellijthemes.*
import org.kordamp.ikonli.lineawesome.LineAwesomeSolid.*
import net.miginfocom.swing.MigLayout
import org.apache.commons.lang3.SystemUtils
import org.apache.commons.lang3.exception.ExceptionUtils

import org.cosplay.*
import org.cosplay.CPLogLevel.*
import org.cosplay.CPColor.*
import org.cosplay.impl.CPUtils
import org.cosplay.impl.guilog.CPGuiLog
import org.kordamp.ikonli.Ikon
import org.kordamp.ikonli.swing.FontIcon

import java.awt.*
import java.awt.datatransfer.StringSelection
import java.awt.event.*
import java.io.*
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.swing.*
import javax.swing.border.*
import javax.swing.event.*
import javax.swing.plaf.ColorUIResource
import javax.swing.text.*
import scala.collection.immutable.HashMap
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
  *
  */
class CPGuiLog(cat: String) extends CPLog:
    override def getLog(category: String): CPLog =
        require(category != null, "Log category cannot be 'null'.")
        val log = new CPGuiLog(s"$cat/$category")
        log.inheritFrom(this)
        log
    override def getCategory: String = cat
    override def log(nthFrame: Int, lvl: CPLogLevel, obj: Any, cat: String, ex: Throwable): Unit =
        if isEnabled(lvl) then
            CPGuiLog.addLog(nthFrame, lvl, cat, obj, ex)

/**
  *
  */
object CPGuiLog:
    CPUtils.initLaF()

    private val KB = 1024
    private val MB = KB * 1024
    private val GB = MB * 1024
    private val TB = GB * 1024

    private val MAX_DOC_SIZE = 50_000
    private val LOG_FONT_SIZE = 14
    private val MIN_LOG_SEARCH_TERM_LEN = 3
    private var frame: JFrame = _
    private var frameCnt = 1L
    private var logPaused = false
    private val flags = mutable.HashMap[CPLogLevel, Boolean](CPLogLevel.values.map(_ -> true).toSeq:_*)
    private val lvlColors = HashMap[CPLogLevel, (String, CPColor)](
        CPLogLevel.TRACE -> ("TRC", C_MISTY_ROSE1),
        CPLogLevel.DEBUG -> ("DBG", C_LIGHT_GREEN),
        CPLogLevel.INFO -> ("INF", C_SKY_BLUE1),
        CPLogLevel.WARN -> ("WRN", C_ORANGE1),
        CPLogLevel.ERROR -> ("ERR", C_ORANGE_RED1),
        CPLogLevel.FATAL -> ("FAT", C_HOT_PINK3),
    )
    private var bgPropName = "TextPane.inactiveBackground"

    private val logPanel = new JTextPane()
    private val logSearchOffs = mutable.ArrayBuffer.empty[Int]
    private var activeLogSearchOff = -1
    private val doc = logPanel.getStyledDocument
    private val numFmt = NumberFormat.getNumberInstance(Locale.US)
    numFmt.setGroupingUsed(false) // Remove comma from the formatting.
    private var font: Font = _

    private val fpsLbl = mkLabel("0", C_LIGHT_STEEL_BLUE)
    private val avgFpsLbl = mkLabel("0", C_LIGHT_STEEL_BLUE)
    private val low1PctFpsLbl = mkLabel("0", C_LIGHT_STEEL_BLUE)
    private val usrTimeLbl = mkLabel("0ms", C_WHITE)
    private val sysTimeLbl = mkLabel("0ms", C_WHITE)
    private val objCntLbl = mkLabel("0", C_WHITE)
    private val visCntLbl = mkLabel("0", C_WHITE)
    private val frameCntLbl = mkLabel("0", C_WHITE)

    private val gameStatusLbl = mkLabel("Running", C_WHITE)
    private val logStatusLbl = mkLabel("Running", C_WHITE)
    private val audioStatusLbl = mkLabel("On", C_WHITE)
    private val cpuUsageLbl = mkLabel("", C_WHITE)
    private val memUsageLbl = mkLabel("", C_WHITE)
    private val onHeapMemLbl = mkLabel("", C_WHITE)
    private val offHeapMemLbl = mkLabel("", C_WHITE)

    private val searchFld = new JTextField(50)
    private val searchCntLbl = mkLabel("0", C_WHITE)
    private var dbgKbCombo: JComboBox[String] = _
    private var dbgSimKbChkBox: JCheckBox = _
    private val clipboard = Toolkit.getDefaultToolkit.getSystemClipboard

    private var curTheme: Class[_] = classOf[FlatDarkPurpleIJTheme] // Default.

    private val copyLogAct: Action = mkAction(
        "Copy",
        Option(mkIcon(COPY)),
        "",
        enabled = false,
        None,
        _ =>
            try
                val txt = logPanel.getSelectedText
                if txt != null then
                    val sel = new StringSelection(txt)
                    clipboard.setContents(sel, sel)
            catch case _: Exception => ()
    )
    private val dbgSimKbAct: Action = mkAction(
        "Keyboard Sim",
        None,
        "Whether or not to simulate keyboard input.",
        enabled = true,
        None,
        _ => {
            dbgKbCombo.setEnabled(dbgSimKbChkBox.isSelected)
        })
    private val saveLogAct: Action = mkAction(
        "Save Log",
        Option(mkIcon(SAVE)),
        "<html>Save log to a file.<br/>Log must be paused.</html>",
        enabled = false,
        None,
        _ => {
            val fileChooser = new JFileChooser
            fileChooser.setDialogTitle("Specify a file to save:")
            val resp = fileChooser.showSaveDialog(frame)
            if resp == JFileChooser.APPROVE_OPTION then
                val file = fileChooser.getSelectedFile
                try
                    Using.resource(new PrintStream(file)) { ps =>
                        ps.println(doc.getText(0, doc.getLength))
                        file.setWritable(false, false)
                    }
                catch
                    case e: Exception => JOptionPane.showMessageDialog(
                        frame,
                        e.getLocalizedMessage,
                        "Error",
                        JOptionPane.ERROR_MESSAGE,
                        mkIcon(TIMES_CIRCLE, C_INDIAN_RED, 32)
                    )
        })
    private val searchNextAct: Action = mkAction(
        "Next",
        Option(mkIcon(STEP_FORWARD)),
        "<html>Next search result.<br/>Log must be paused.</html>",
        enabled = false,
        Option('N'),
        _ => {
            logSearchPrevNext(true)
        })
    private val searchPrevAct: Action = mkAction(
        "Prev",
        Option(mkIcon(STEP_BACKWARD)),
        "<html>Previous search result.<br/>Log must be paused.</html>",
        enabled = false,
        Option('P'),
        _ => {
            logSearchPrevNext(false)
        })
    private val searchClearAct: Action = mkAction(
        "",
        Option(mkIcon(TRASH_ALT)),
        "<html>Clear the search.<br/>Log must be paused.</html>",
        enabled = false,
        None,
        _ => {
            searchFld.setText("")
            resetLogSearch()
        })
    private val searchFirstAct: Action = mkAction(
        "",
        Option(mkIcon(FAST_BACKWARD)),
        "<html>Go to the first match.<br/>Log must be paused.</html>",
        enabled = false,
        None,
        _ => {
            if logSearchOffs.nonEmpty then
                activeLogSearchOff = logSearchOffs.head
                logPanel.setCaretPosition(activeLogSearchOff)
                searchLog()
        })
    private val searchLastAct: Action = mkAction(
        "",
        Option(mkIcon(FAST_FORWARD)),
        "<html>Go to the last match.<br/>Log must be paused.</html>",
        enabled = false,
        None,
        _ => {
            if logSearchOffs.nonEmpty then
                activeLogSearchOff = logSearchOffs.last
                logPanel.setCaretPosition(activeLogSearchOff)
                searchLog()
        })
    private val traceAct: Action = mkAction(
        "TRACE",
        None,
        "",
        enabled = true,
        None,
        _ => {
            flags.put(CPLogLevel.TRACE, !flags(CPLogLevel.TRACE))
            storeProps()
        })
    private val debugAct: Action = mkAction(
        "DEBUG",
        None,
        "",
        enabled = true,
        None,
        _ => {
            flags.put(CPLogLevel.DEBUG, !flags(CPLogLevel.DEBUG))
            storeProps()
        })
    private val infoAct: Action = mkAction(
        "INFO",
        None,
        "",
        enabled = true,
        None,
        _ => {
            flags.put(CPLogLevel.INFO, !flags(CPLogLevel.INFO))
            storeProps()
        })
    private val warnAct: Action = mkAction(
        "WARN",
        None,
        "",
        enabled = true,
        None,
        _ => {
            flags.put(CPLogLevel.WARN, !flags(CPLogLevel.WARN))
            storeProps()
        })
    private val errorAct: Action = mkAction(
        "ERROR",
        None,
        "",
        enabled = true,
        None,
        _ => {
            flags.put(CPLogLevel.ERROR, !flags(CPLogLevel.ERROR))
            storeProps()
        })
    private val fatalAct: Action = mkAction(
        "FATAL",
        None,
        "",
        enabled = true,
        None,
        _ => {
            flags.put(CPLogLevel.FATAL, !flags(CPLogLevel.FATAL))
            storeProps()
        })
    private val clearLogAct: Action = mkAction(
        "Clear",
        Option(mkIcon(TRASH_ALT)),
        "Clear entire log.",
        enabled = true,
        None,
        _ => {
            doc.remove(0, doc.getLength)
            resetLogSearch()
        })
    private val pauseLogAct: Action = mkAction(
        "Pause",
        Option(mkIcon(PAUSE)),
        "<html>Pause new log entries.<br/>Log must be paused for search.</html>",
        enabled = true,
        None,
        _ => {
            logPaused = true
            pauseLogAct.setEnabled(false)
            resumeLogAct.setEnabled(true)
            searchFld.setEnabled(true)
            searchFld.grabFocus()
            saveLogAct.setEnabled(true)
            logStatusLbl.setText("Paused")
            searchLog()
        })
    private val resumeLogAct: Action = mkAction(
        "Resume",
        Option(mkIcon(PLAY)),
        "Resume new log entries.",
        enabled = false,
        None,
        _ => {
            resetLogSearch()
            logPaused = false
            pauseLogAct.setEnabled(true)
            resumeLogAct.setEnabled(false)
            searchFld.setEnabled(false)
            saveLogAct.setEnabled(false)
            logStatusLbl.setText("Running")
        })
    private val closeLogAct: Action = mkAction(
        "Close",
        None,
        "Close log window.",
        enabled = true,
        None,
        _ => frame.setVisible(false)
        )
    private val pauseGameAct: Action = mkAction(
        "Pause",
        Option(mkIcon(PAUSE)),
        "Pause game play.",
        enabled = CPEngine.isGamePaused,
        None,
        _ => {
            CPEngine.pauseGame()
            pauseGameAct.setEnabled(false)
            resumeGameAct.setEnabled(true)
            dbgStepAct.setEnabled(true)
            dbgSimKbChkBox.setEnabled(true)
            dbgKbCombo.setEnabled(dbgSimKbChkBox.isSelected)
            gameStatusLbl.setText("Paused")
        })
    private val resumeGameAct: Action = mkAction(
        "Resume",
        Option(mkIcon(PLAY)),
        "Resume game play.",
        enabled = !CPEngine.isGamePaused,
        None,
        _ => {
            CPEngine.resumeGame()
            pauseGameAct.setEnabled(true)
            resumeGameAct.setEnabled(false)
            dbgStepAct.setEnabled(false)
            dbgSimKbChkBox.setEnabled(false)
            dbgKbCombo.setEnabled(false)
            gameStatusLbl.setText("Running")
        })
    private val audioOffAct: Action = mkAction(
        "Audio Off",
        Option(mkIcon(VOLUME_MUTE)),
        "Silence all audio.",
        enabled = true,
        None,
        _ => {
            audioStatusLbl.setText("Off")
            CPSound.foreach(_.setVolume(0f))
            audioOnAct.setEnabled(true)
            audioOffAct.setEnabled(false)
        })
    private val audioOnAct: Action = mkAction(
        "Audio On",
        Option(mkIcon(VOLUME_UP)),
        "Resume all audio.",
        enabled = false,
        None,
        _ => {
            audioStatusLbl.setText("On")
            CPSound.foreach(_.setVolume(1.0f))
            audioOnAct.setEnabled(false)
            audioOffAct.setEnabled(true)
        })
    private val dbgStepAct: Action = mkAction(
        "Step",
        Option(mkIcon(BUG)),
        "<html>Step one frame at a time.<br/>Game must be paused.</html>",
        enabled = CPEngine.isGamePaused,
        Option('S'.toInt),
        _ => {
            val kbKey = Option.when(dbgSimKbChkBox.isSelected)(CPKeyboardKey.ofId(dbgKbCombo.getSelectedItem.asInstanceOf[String]))
            if kbKey.isDefined then kbKey.get.clear() // Clear potential metadata from the key.
            CPEngine.debugStep(kbKey)
        })
    private val darkThemeAct: Action = mkAction(
        "Flat Dark",
        None,
        "Switch to 'Flat Dark' theme.",
        enabled = true,
        None,
        _ => {
            FlatDarkFlatIJTheme.setup()
            curTheme = classOf[FlatDarkFlatIJTheme]
            onThemeUpdate("TextPane.inactiveBackground")
            storeProps()
        })
    private val draculaThemeAct: Action = mkAction(
        "Flat Dracula",
        None,
        "Switch to 'Flat Dracula' theme.",
        enabled = true,
        None,
        _ => {
            FlatDraculaIJTheme.setup()
            curTheme = classOf[FlatDraculaIJTheme]
            onThemeUpdate("TextPane.inactiveBackground")
            storeProps()
        })
    private val oneDarkThemeAct: Action = mkAction(
        "One Dark",
        None,
        "Switch to 'One Dark' theme.",
        enabled = true,
        None,
        _ => {
            FlatOneDarkIJTheme.setup()
            curTheme = classOf[FlatOneDarkIJTheme]
            onThemeUpdate("TextPane.inactiveBackground")
            storeProps()
        })
    private val darkPurpleThemeAct: Action = mkAction(
        "Dark Purple",
        None,
        "Switch to 'Dark Purple' theme.",
        enabled = true,
        None,
        _ => {
            FlatDarkPurpleIJTheme.setup()
            curTheme = classOf[FlatDarkPurpleIJTheme]
            onThemeUpdate("TextPane.inactiveBackground")
            storeProps()
        })
    private val materialDarkerThemeAct: Action = mkAction(
        "Material Darker",
        None,
        "Switch to 'Material Darker' theme.",
        enabled = true,
        None,
        _ => {
            FlatMaterialDarkerIJTheme.setup()
            curTheme = classOf[FlatMaterialDarkerIJTheme]
            onThemeUpdate("TextPane.background")
            storeProps()
        })
    private val carbonThemeAct: Action = mkAction(
        "Carbon",
        None,
        "Switch to 'Carbon' theme.",
        enabled = true,
        None,
        _ => {
            FlatCarbonIJTheme.setup()
            curTheme = classOf[FlatCarbonIJTheme]
            onThemeUpdate("TextPane.background")
            storeProps()
        })
    private val stopGameAct: Action = mkAction(
        "Stop Game",
        Option(mkIcon(TIMES)),
        "Stop and exit game.",
        enabled = true,
        None,
        _ => {
            val resp = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to stop and exit the game?",
                "Exit Game",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                mkIcon(QUESTION, C_LIGHT_GOLDEN_ROD1, 32)
            )
            if resp == 0 then
                CPEngine.exitGame()
        })

    /**
      *
      */
    private def storeProps(): Unit =
        val props = new Properties()

        props.put("theme", curTheme.getName)
        flags.foreach((k, v) => props.put(k.toString, v.toString))

        val logDir = new File(SystemUtils.getUserHome, ".cosplay")
        if (logDir.exists() && logDir.isFile || !logDir.exists() && !logDir.mkdirs())
            addLog(1, CPLogLevel.ERROR, "log", "Failed to create: $logDir", null)

        val file = new File(logDir,"log.properties")

        try Using.resource(new FileOutputStream(file)) { out => props.store(out, null) }
        catch case _: Exception => ()

    /**
      *
      */
    private def loadProps(): Properties =
        val logFile = new File(SystemUtils.getUserHome, ".cosplay/log.properties")
        val props = new Properties()
        try Using.resource(new FileInputStream(logFile)) { in => props.load(in) }
        catch case _: Exception => ()
        props

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
      * @param next
      */
    private def logSearchPrevNext(next: Boolean): Unit =
        if logSearchOffs.nonEmpty then
            // Log search offsets are naturally sorted by the way they are collected.
            val curPos = if activeLogSearchOff != -1 then activeLogSearchOff else 0
            val posOpt = if next then
                logSearchOffs.find(_ > curPos)
            else
                val s = logSearchOffs.filter(_ < curPos)
                if s.isEmpty then None else Option(s.max)
            posOpt match
                case Some(pos) =>
                    activeLogSearchOff = pos
                    logPanel.setCaretPosition(pos)
                    searchLog()
                case None => ()

    /**
      *
      * @param name
      */
    private def onThemeUpdate(name: String): Unit =
        FlatLaf.updateUI()
        bgPropName = name
        updateDocBackground()
        if logPaused then searchLog()

    /**
      *
       */
    private def updateDocBackground(): Unit =
        val docLen = doc.getLength
        if docLen > 0 then
            val attrs = new SimpleAttributeSet()
            val dfltBg = UIManager.get(bgPropName).asInstanceOf[Color]
            StyleConstants.setBackground(attrs, dfltBg)
            doc.setCharacterAttributes(0, docLen, attrs, false)

    /**
      *
      */
    private def resetLogSearch(): Unit =
        if logSearchOffs.nonEmpty then
            logSearchOffs.clear()
            updateDocBackground()
            searchCntLbl.setText("0")
            searchNextAct.setEnabled(false)
            searchPrevAct.setEnabled(false)
            searchClearAct.setEnabled(false)
            searchFirstAct.setEnabled(false)
            searchLastAct.setEnabled(false)

    /**
      *
      */
    private def searchLog(): Unit =
        SwingUtilities.invokeLater(() => {
            resetLogSearch()

            val term = searchFld.getText.trim().toLowerCase
            val termLen = term.length
            if termLen > MIN_LOG_SEARCH_TERM_LEN then
                val docTxt = doc.getText(0, doc.getLength).toLowerCase
                var off = docTxt.indexOf(term)
                while (off != -1)
                    logSearchOffs += off
                    off = docTxt.indexOf(term, off + termLen)
                if logSearchOffs.nonEmpty then
                    val attrs = new SimpleAttributeSet()
                    for off <- logSearchOffs do
                        if off == activeLogSearchOff then
                            StyleConstants.setBackground(attrs, C_ORANGE_RED1.awt)
                        else
                            StyleConstants.setBackground(attrs, C_DODGER_BLUE1.awt)
                        doc.setCharacterAttributes(off, termLen, attrs, false)
                    searchCntLbl.setText(logSearchOffs.length.toString)
                    searchNextAct.setEnabled(true)
                    searchPrevAct.setEnabled(true)
                    searchClearAct.setEnabled(true)
                    searchFirstAct.setEnabled(true)
                    searchLastAct.setEnabled(true)
        })

    /**
      * @param isPaused
      */
    def onGamePauseResume(isPaused: Boolean): Unit =
        pauseGameAct.setEnabled(!isPaused)
        resumeGameAct.setEnabled(isPaused)
        dbgStepAct.setEnabled(isPaused)
        dbgSimKbChkBox.setEnabled(isPaused)
        dbgKbCombo.setEnabled(dbgSimKbChkBox.isSelected)
        gameStatusLbl.setText(if isPaused then "Paused" else "Running")

    /**
      *
      * @param stats
      */
    def updateStats(stats: CPRenderStats): Unit =
        frameCnt = stats.frameCount

        SwingUtilities.invokeLater(() => {
            fpsLbl.setText(numFmt.format(stats.fps))
            avgFpsLbl.setText(numFmt.format(stats.avgFps))
            usrTimeLbl.setText(s"${stats.userTimeNs / 1_000_000}ms")
            sysTimeLbl.setText(s"${stats.sysTimeNs / 1_000_000}ms")
            objCntLbl.setText(numFmt.format(stats.objCount))
            visCntLbl.setText(numFmt.format(stats.visObjCount))
            low1PctFpsLbl.setText(numFmt.format(stats.low1PctFps))
            frameCntLbl.setText(numFmt.format(stats.frameCount))
            val cpuUsage = CPUtils.cpuUsagePct
            val memUsage = CPUtils.memUsagePct
            cpuUsageLbl.setText(if cpuUsage < 0 then "n/a" else s"$cpuUsage%")
            memUsageLbl.setText(s"$memUsage%")
            onHeapMemLbl.setText(formatMem(CPUtils.onHeapMemUsage))
            offHeapMemLbl.setText(formatMem(CPUtils.offHeapMemUsage))
        })

    /**
      *
      * @param act
      * @param initState
      */
    private def mkCheckBox(act: Action, initState: Boolean): JCheckBox =
        val cb = new JCheckBox(act)
        cb.setFocusPainted(false)
        cb.setSelected(initState)
        cb

    /**
      *
      * @param act
      */
    private def mkCheckBoxMenuItem(act: Action): JCheckBoxMenuItem =
        val cb = new JCheckBoxMenuItem(act)
        cb.setFocusPainted(false)
        cb.setSelected(true)
        cb

    /**
      *
      */
    private def mkGameStatsPanel(): JPanel =
        val p =  mkPanel("wrap 3, insets 3 5 8 5, h 75!")
        val w = 120

        p.add(mkStatsPair("CUR FPS:", fpsLbl), s"gapleft 5, w $w!")
        p.add(mkStatsPair("OBJ CNT:", objCntLbl), s"w $w!")
        p.add(mkStatsPair("USR TIME:", usrTimeLbl), s"w $w!")

        p.add(mkStatsPair("AVG FPS:", avgFpsLbl), s"gapleft 5, w $w!")
        p.add(mkStatsPair("VIS CNT:", visCntLbl), s"w $w!")
        p.add(mkStatsPair("SYS TIME:", sysTimeLbl), s"w $w!")

        p.add(mkStatsPair("LOW FPS:", low1PctFpsLbl), s"gapleft 5, w $w!")
        p.add(mkStatsPair("FRM CNT:", frameCntLbl), s"w $w!")

        p.setBorder(BorderFactory.createTitledBorder("Game Stats"))
        p

    /**
      *
      * @param key
      * @param lbl
      */
    private def mkStatsPair(key: String, lbl: JLabel): JPanel =
        val p =  mkPanel("insets 0")
        p.add(new JLabel(key), "w 75!")
        p.add(lbl)
        p

    /**
      *
      * @param txt
      * @param fg
      */
    private def mkLabel(txt: String, fg: CPColor): JLabel =
        val lbl = new JLabel(txt)
        lbl.setForeground(fg.awt)
        lbl

    /**
      *
      * @param nthFrame
      * @param lvl
      * @param cat
      * @param obj
      * @param ex
      */
    private def addLog(nthFrame: Int, lvl: CPLogLevel, cat: String, obj: Any, ex: Throwable): Unit =
        if frameCnt % nthFrame == 0 then
            if frame == null then initGui()
            val objStr = if obj == null then "null" else obj.toString
            if objStr == CPUtils.PING_MSG then
                SwingUtilities.invokeLater(() => frame.setVisible(true))
            else if !logPaused && flags(lvl) then
                SwingUtilities.invokeLater(() => {
                    // Keep document to a fixed max size.
                    if doc.getLength > MAX_DOC_SIZE * 2 then
                        doc.remove(0, doc.getLength - MAX_DOC_SIZE)

                    val start = doc.getLength

                    def mkAttrs(fg: CPColor): SimpleAttributeSet =
                        val attrs = new SimpleAttributeSet()
                        StyleConstants.setFontSize(attrs, LOG_FONT_SIZE)
                        StyleConstants.setFontFamily(attrs, font.getFamily)
                        StyleConstants.setForeground(attrs, fg.awt)
                        attrs

                    // Add timestamp.
                    var attrs = mkAttrs(C_WHITE)
                    val tstamp = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now())
                    doc.insertString(doc.getLength, s"${tstamp.format("%1$s8")} ", attrs)

                    // Add level marker.
                    val (lvlStr, fg) = lvlColors(lvl)
                    attrs = mkAttrs(fg)
                    doc.insertString(doc.getLength, s" $lvlStr ", attrs)

                    // Add category.
                    attrs = mkAttrs(C_WHITE)
                    doc.insertString(doc.getLength, s" $cat", attrs)

                    // Add log message.
                    attrs = mkAttrs(C_WHITE)
                    doc.insertString(doc.getLength, s" $objStr\n", attrs)

                    if ex != null then
                        attrs = mkAttrs(C_PALE_TURQUOISE1)
                        doc.insertString(doc.getLength, ExceptionUtils.getStackTrace(ex), attrs)

                    StyleConstants.setLineSpacing(attrs, 0.2f)
                    doc.setParagraphAttributes(start, doc.getLength - start, attrs, false)

                    val pos = doc.getLength - 1
                    logPanel.setCaretPosition(pos)
                    activeLogSearchOff = -1
                })

    /**
      *
      */
    private def mkTopPanel(): JPanel =
        val p =  mkPanel("insets 5")
        p.add(mkLogCtrlPanel())
        p.add(mkLogLevelsPanel())
        p.add(mkGameStatsPanel())
        p.add(mkGameCtrlPanel(), "wrap")

        val p2 = mkPanel("insets 0")
        p2.add(mkLogSearchPanel(), "align left")
        p2.add(mkDebugPanel(), "align right, pushx")

        p.add(p2, "span 4, growx")
        p

    /**
      *
      * @param bytes
      */
    private def formatMem(bytes: Long): String =
        // For some (unknown) reason, at least on MacOS, the last
        // line can produce arithmetic exception with "/ by zero" message.
        try
            if bytes < KB then s"${numFmt.format(bytes)}B"
            else if bytes < MB then s"${numFmt.format(bytes / KB)}KB"
            else if bytes < GB then s"${numFmt.format(bytes / MB)}MB"
            else if bytes < TB then s"${numFmt.format(bytes / GB)}GB"
            else s"${numFmt.format(bytes / TB)}TB"
        catch
            case e: ArithmeticException => ""

    /**
      *
      */
    private def mkStatusPanel(): JPanel =
        val status =  mkPanel("insets 3 5")

        status.add(new JLabel("Log:"), "gapleft 3")
        status.add(logStatusLbl, "align left, w 60!")
        status.add(new JLabel("Game:"), "gapleft 10")
        status.add(gameStatusLbl, "align left, w 60!")
        status.add(new JLabel("Audio:"), "gapleft 10")
        status.add(audioStatusLbl, "align left, w 50!")
        status.add(new JLabel("CPU:"), "align right, pushx")
        status.add(cpuUsageLbl, "align left, w 25")
        status.add(new JLabel("Mem:"), "gapleft 10")
        status.add(memUsageLbl, "align left, w 25")
        status.add(new JLabel("On-Heap Mem:"), "gapleft 10")
        status.add(onHeapMemLbl, "align left, w 45")
        status.add(new JLabel("Off-Heap Mem:"), "gapleft 10")
        status.add(offHeapMemLbl, "align left, w 45, gapright 3")

        status.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(0, 7, 8, 7), new TitledBorder("")))
        status

    /**
      *
      * @param mig
      */
    private def mkPanel(mig: String): JPanel =
        new JPanel(new MigLayout(mig))

    /**
      *
      */
    private def mkLogCtrlPanel(): JPanel =
        val p = mkPanel("wrap 2, insets 10 10 14 10, h 75!")

        p.add(mkButton(pauseLogAct), "growx")
        p.add(mkButton(clearLogAct), "growx")
        p.add(mkButton(resumeLogAct), "growx")
        p.add(mkButton(closeLogAct), "growx")

        p.setBorder(BorderFactory.createTitledBorder("Log Ctrl"))

        p

    /**
      *
      */
    private def mkLogSearchPanel(): JPanel =
        val p = mkPanel("insets 0 5 0 0")

        p.add(new JLabel("Log Search:"), "wrap")
        p.add(searchFld, "w 200!")
        searchFld.setEnabled(false)
        searchFld.getDocument.addDocumentListener(new DocumentListener:
            private def update(): Unit =
                val txt = searchFld.getText.trim()
                if txt.length > MIN_LOG_SEARCH_TERM_LEN then
                    searchLog()
                else
                    resetLogSearch()

            override def insertUpdate(e: DocumentEvent): Unit = update()
            override def removeUpdate(e: DocumentEvent): Unit = update()
            override def changedUpdate(e: DocumentEvent): Unit = update()
        )
        p.add(mkButton(searchFirstAct))
        p.add(mkButton(searchNextAct))
        p.add(mkButton(searchPrevAct))
        p.add(mkButton(searchLastAct))
        p.add(mkButton(searchClearAct))
        p.add(new JLabel("Results:"), "gapleft 10")
        p.add(searchCntLbl, "wrap")
        p.add(new JLabel("<html><span style='font-size: 85%'><b>NOTE:</b> pause log to enable search</span></html>"))

        p

    /**
      *
      */
    private def mkDebugPanel(): JPanel =
        val p = mkPanel("insets 0 0 0 5")

        dbgSimKbChkBox = mkCheckBox(dbgSimKbAct, initState = false)
        dbgSimKbChkBox.setEnabled(false)

        p.add(new JLabel("Debug:"), "wrap")
        p.add(dbgSimKbChkBox)
        p.add(dbgKbCombo)
        p.add(mkButton(dbgStepAct), "wrap")
        p.add(new JLabel("<html><span style='font-size: 85%'><b>NOTE:</b> pause game to enable debug</span></html>"))

        p

    /**
      *
      * @param act
      * @param lvl
      */
    private def mkLogLevelPair(act: Action, lvl: CPLogLevel): JPanel =
        val p = mkPanel("insets 0")
        val cb = mkCheckBox(act, initState = true)
        cb.setText("")
        p.add(cb)
        val (txt, fg) = lvlColors(lvl)
        p.add(mkLabel(txt, fg))
        p

    /**
      *
      */
    private def mkLogLevelsPanel(): JPanel =
        val p = mkPanel("wrap 3, insets 15 10 15 10, h 75!")

        p.add(mkLogLevelPair(traceAct, CPLogLevel.TRACE))
        p.add(mkLogLevelPair(debugAct, CPLogLevel.DEBUG))
        p.add(mkLogLevelPair(infoAct, CPLogLevel.INFO))
        p.add(mkLogLevelPair(warnAct, CPLogLevel.WARN))
        p.add(mkLogLevelPair(errorAct, CPLogLevel.ERROR))
        p.add(mkLogLevelPair(fatalAct, CPLogLevel.FATAL))

        p.setBorder(BorderFactory.createTitledBorder("Log Levels"))

        p

    /**
      *
      */
    private def mkGameCtrlPanel(): JPanel =
        val p =  mkPanel("wrap 2, insets 10 10 14 10, h 75!")

        p.add(mkButton(pauseGameAct), "growx")
        p.add(mkButton(audioOffAct), "growx")
        p.add(mkButton(resumeGameAct), "growx")
        p.add(mkButton(audioOnAct), "growx")

        p.setBorder(BorderFactory.createTitledBorder("Game Ctrl"))

        p

    /**
      *
      * @param txt
      * @param icon
      * @param tooltip
      * @param enabled
      * @param mnemonic
      * @param f
      */
    private def mkAction(
        txt: String,
        icon: Option[Icon],
        tooltip: String,
        enabled: Boolean,
        mnemonic: Option[Int],
        f: ActionEvent => Unit): Action =
        val act = new AbstractAction(txt):
            override def actionPerformed(e: ActionEvent): Unit = f(e)
        act.setEnabled(enabled)
        act.putValue(Action.SELECTED_KEY, true)
        act.putValue(Action.SHORT_DESCRIPTION, tooltip)
        if mnemonic.isDefined then act.putValue(Action.MNEMONIC_KEY, mnemonic.get)
        if icon.isDefined then act.putValue(Action.SMALL_ICON, icon.get)
        act

    /**
      *
      * @param act
      */
    private def mkButton(act: Action): JButton =
        val b = new JButton(act)
        b.setFocusPainted(false)
        if act.getValue(Action.SMALL_ICON) != null then b.setHorizontalAlignment(SwingConstants.LEFT)
        b.setIconTextGap(10)
        b

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
      */
    private def mkLogPanel(): JScrollPane =
        val popup = new JPopupMenu()
        popup.add(mkMenuItem(copyLogAct))
        popup.addSeparator()
        popup.add(mkMenuItem(pauseLogAct))
        popup.add(mkMenuItem(resumeLogAct))
        popup.addSeparator()
        popup.add(mkMenuItem(saveLogAct))
        popup.addSeparator()
        popup.add(mkCheckBoxMenuItem(traceAct))
        popup.add(mkCheckBoxMenuItem(debugAct))
        popup.add(mkCheckBoxMenuItem(infoAct))
        popup.add(mkCheckBoxMenuItem(warnAct))
        popup.add(mkCheckBoxMenuItem(errorAct))
        popup.add(mkCheckBoxMenuItem(fatalAct))
        popup.addSeparator()
        popup.add(mkMenuItem(clearLogAct))
        popup.add(mkMenuItem(closeLogAct))
        popup.addPopupMenuListener(new PopupMenuListener:
            override def popupMenuWillBecomeVisible(e: PopupMenuEvent): Unit = copyLogAct.setEnabled(logPanel.getSelectedText != null)
            override def popupMenuWillBecomeInvisible(e: PopupMenuEvent): Unit = ()
            override def popupMenuCanceled(e: PopupMenuEvent): Unit = ()
        )
        logPanel.setComponentPopupMenu(popup)
        logPanel.setEditable(false)
        val sp = new JScrollPane(logPanel)
        sp.setPreferredSize(new Dimension(800, 600))
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED)
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS)
        sp.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5), new TitledBorder("Log")))
        sp.setInheritsPopupMenu(true)
        sp

    /**
      *
      */
    private def initFromProperties(): Unit =
        val props = loadProps()
        props.get("theme") match
            case "com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme" =>
                FlatDarkFlatIJTheme.setup()
                curTheme = classOf[FlatDarkFlatIJTheme]
                onThemeUpdate("TextPane.inactiveBackground")
            case "com.formdev.flatlaf.intellijthemes.FlatDraculaIJTheme" =>
                FlatDraculaIJTheme.setup()
                curTheme = classOf[FlatDraculaIJTheme]
                onThemeUpdate("TextPane.inactiveBackground")
            case "com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme" =>
                FlatOneDarkIJTheme.setup()
                curTheme = classOf[FlatOneDarkIJTheme]
                onThemeUpdate("TextPane.inactiveBackground")
            case "com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme" =>
                FlatDarkPurpleIJTheme.setup()
                curTheme = classOf[FlatDarkPurpleIJTheme]
                onThemeUpdate("TextPane.inactiveBackground")
            case "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDarkerIJTheme" =>
                FlatMaterialDarkerIJTheme.setup()
                curTheme = classOf[FlatMaterialDarkerIJTheme]
                onThemeUpdate("TextPane.background")
            case "com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme" =>
                FlatCarbonIJTheme.setup()
                curTheme = classOf[FlatDraculaIJTheme]
                onThemeUpdate("TextPane.background")
            case _ => ()

        CPLogLevel.values.foreach(lvl => {
            props.get(lvl.toString) match
                case fs: String =>
                    val f = java.lang.Boolean.parseBoolean(fs)
                    flags.put(lvl, f)
                    lvl match
                        case CPLogLevel.TRACE => traceAct.putValue(Action.SELECTED_KEY, f)
                        case CPLogLevel.DEBUG => debugAct.putValue(Action.SELECTED_KEY, f)
                        case CPLogLevel.INFO => infoAct.putValue(Action.SELECTED_KEY, f)
                        case CPLogLevel.WARN => warnAct.putValue(Action.SELECTED_KEY, f)
                        case CPLogLevel.ERROR => errorAct.putValue(Action.SELECTED_KEY, f)
                        case CPLogLevel.FATAL => fatalAct.putValue(Action.SELECTED_KEY, f)
                case _ => ()
        })

    /**
      *
      */
    //noinspection DuplicatedCode
    private def initGui(): Unit =
        font = Font
            .createFont(Font.TRUETYPE_FONT, CPUtils.getStream("fonts/SourceCodePro-Regular.ttf"))
            .deriveFont(12f)

        GraphicsEnvironment.getLocalGraphicsEnvironment.registerFont(font)

        dbgKbCombo = new JComboBox[String](CPKeyboardKey.values.map(_.id).filter(_ != "Unknown"))
        dbgKbCombo.setEnabled(false)

        frame = new JFrame("CosPlay Log")

        // Minimize (iconify) frame window on close.
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
        frame.addWindowListener(new WindowAdapter:
            override def windowClosing(e: WindowEvent): Unit = frame.setExtendedState(Frame.ICONIFIED)
        )

        val menuBar = new JMenuBar()
        val fileMenu = new JMenu("File")

        val logMenu = new JMenu("Log")
        logMenu.add(mkMenuItem(pauseLogAct))
        logMenu.add(mkMenuItem(resumeLogAct))
        logMenu.addSeparator()
        logMenu.add(mkMenuItem(saveLogAct))
        logMenu.addSeparator()
        logMenu.add(mkCheckBoxMenuItem(traceAct))
        logMenu.add(mkCheckBoxMenuItem(debugAct))
        logMenu.add(mkCheckBoxMenuItem(infoAct))
        logMenu.add(mkCheckBoxMenuItem(warnAct))
        logMenu.add(mkCheckBoxMenuItem(errorAct))
        logMenu.add(mkCheckBoxMenuItem(fatalAct))
        logMenu.addSeparator()
        logMenu.add(mkMenuItem(clearLogAct))

        val gameMenu = new JMenu("Game")
        gameMenu.add(mkMenuItem(pauseGameAct))
        gameMenu.add(mkMenuItem(resumeGameAct))
        gameMenu.addSeparator()
        gameMenu.add(mkMenuItem(audioOnAct))
        gameMenu.add(mkMenuItem(audioOffAct))
        gameMenu.addSeparator()
        gameMenu.add(mkMenuItem(stopGameAct))

        val themesMenu = new JMenu("Themes")
        themesMenu.add(mkMenuItem(darkThemeAct))
        themesMenu.add(mkMenuItem(draculaThemeAct))
        themesMenu.add(mkMenuItem(oneDarkThemeAct))
        themesMenu.add(mkMenuItem(darkPurpleThemeAct))
        themesMenu.add(mkMenuItem(materialDarkerThemeAct))
        themesMenu.add(mkMenuItem(carbonThemeAct))

        fileMenu.add(themesMenu)
        fileMenu.addSeparator()
        fileMenu.add(mkMenuItem(closeLogAct))

        menuBar.add(fileMenu)
        menuBar.add(logMenu)
        menuBar.add(gameMenu)
        frame.setJMenuBar(menuBar)

        val cp = frame.getContentPane
        cp.add(mkTopPanel(), BorderLayout.NORTH)
        cp.add(mkLogPanel(), BorderLayout.CENTER)
        cp.add(mkStatusPanel(), BorderLayout.SOUTH)

        try
            val img = new ImageIcon(getClass.getResource("/images/cosplay.png")).getImage
            try Taskbar.getTaskbar.setIconImage(img)
            catch case _: Exception => ()
            frame.setIconImage(img)
        catch case _: Exception => ()

        initFromProperties()

        // Init game pause state.
        onGamePauseResume(CPEngine.isGamePaused)

        frame.pack()
        frame.setLocationByPlatform(true)
        frame.setVisible(false) // Hide by default.


