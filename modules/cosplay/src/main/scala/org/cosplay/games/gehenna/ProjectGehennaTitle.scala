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

package org.cosplay.games.gehenna

import org.cosplay.*
import CPColor.*
import CPPixel.*
import CPKeyboardKey.*
import org.cosplay.CPArrayImage.*
import org.cosplay.CPFIGLetFont.*
import org.cosplay.games.gehenna.shaders.{FlashShader, TextDripShader}
import org.cosplay.prefabs.shaders.*

import java.io.*
import org.apache.commons.io.*

import java.nio.charset.Charset
import scala.io.Source
import scala.jdk.CollectionConverters.*
import de.sciss.audiofile.*

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import collection.immutable.HashSet

/*
   _________            ______________
   __  ____/_______________  __ \__  /_____ _____  __
   _  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
   / /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
   \____/  \____//____/ /_/     /_/  \__,_/ _\__, /
                                            /____/

          2D ASCII JVM GAME ENGINE FOR SCALA3
              (C) 2021 Rowan Games, Inc.
                ALl rights reserved.
*/


object ProjectGehennaTitle extends CPScene("title", None, GAME_BG_PX):
    private final val TITLE = "Project Gehenna"
    private final val START_IMG = CPImage.loadRexCsv("images/games/gehenna/StartBtn.csv").trimBg()
    private final val SETTINGS_IMG = CPImage.loadRexCsv("images/games/gehenna/SettingsBtn.csv").trimBg()
    private final val HELP_IMG = CPImage.loadRexCsv("images/games/gehenna/HelpBtn.csv").trimBg()
    private final val TITLE_IMG = FIG_OGRE.render(TITLE, BLOOD_RED).trimBg()
    private final val NOW_PLAYING_MS = 5000L
    private final var DFLT_SONG = CPSound("sounds/games/gehenna/introsong.wav")

    private val fadeInShdr = CPSlideInShader.sigmoid(
        CPSlideDirection.CENTRIFUGAL,
        true,
        3000,
        GAME_BG_PX,
        onFinish = _ => TextDripShader.start()
    )
    // Variables for audio file plugin for operations in blocks of this size.
    final val BUF_SZ = 8192 //8192

    private def skullImg(): CPImage =
        new CPArrayImage(
            prepSeq(
                """
                |                      :::!~!!!!!:.
                |                  .xUHWH!! !!?M88WHX:.
                |                .X*#M@$!!  !X!M$$$$$$WWx:.
                |               :!!!!!!?H! :!$!$$$$$$$$$$8X:
                |              !!~  ~:~!! :~!$!#$$$$$$$$$$8X:
                |             :!~::!H!<   ~.U$X!?R$$$$$$$$MM!
                |             ~!~!!!!~~ .:XW$$$U!!?$$$$$$RMM!
                |               !:~~~ .:!M"T#$$$$WX??#MRRMMM!
                |               ~?WuxiW*`   `"#$$$$8!!!!??!!!
                |             :X- M$$$$       `"T#$T~!8$WUXU~
                |            :%`  ~#$$$m:        ~!~ ?$$$$$$
                |          :!`.-   ~T$$$$8xx.  .xWW- ~""##*"
                |.....   -~~:<` !    ~?T#$$@@W@*?$$      /`
                |W$@@M!!! .!~~ !!     .:XUW$W!~ `"~:    :
                |#"~~`.:x%`!!  !H:   !WM$$$$Ti.: .!WUn+!`
                |:::~:!!`:X~ .: ?H.!u "$$$B$$$!W:U!T$$M~
                |.~~   :X@!.-~   ?@WTWo("*$$$W$TH$! `
                |Wi.~!X$?!-~    : ?$$$B$Wu("**$RM!
                |$R@i.~~ !     :   ~$$$$$B$$en:``
                |?MXT@Wx.~    :     ~"##*$$$$M~
                """
            ),
            (ch, _, _) => ch&BLOOD_RED.darker(0.55)
        )

    private def songPlayingImg(darkness: Float): CPImage =
        new CPArrayImage(
            prepSeq(
                """
                  |
                  |Now Playing:
                  |
                  """
            ),
            (ch, _, _) => ch&NEON_BLUE.darker(darkness)
        )

    private val flashShdr = new FlashShader()
    private val skullFlashShdr = new FlashShader()
    private val skullSpr = new CPImageSprite(x = 0, y = 0, z = 0, skullImg(), false, Seq(fadeInShdr, skullFlashShdr)):
        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            val canv = ctx.getCanvas
            skullFlashShdr.changeBPM(0) // TODO: change the shader logic.
            setY(((canv.w - getWidth) / 2) - 15)
            setX(((canv.h - getHeight) / 2) + 10)

    private val titleSpr = new CPImageSprite("title", 0, 0, 1, TITLE_IMG, shaders = Seq(fadeInShdr, TextDripShader, flashShdr)):
        override def update(ctx: CPSceneObjectContext): Unit =
            flashShdr.changeBPM(0)
            setX((ctx.getCanvas.w - this.getWidth) / 2)

    private val startSpr = new CPImageSprite("start", 0, 43, 1, START_IMG, shaders = Seq(fadeInShdr)):
        override def update(ctx: CPSceneObjectContext): Unit =
            setX((ctx.getCanvas.w - this.getWidth) / 2)

    private val helpSpr = new CPImageSprite("help", 0, 45, 1, HELP_IMG, shaders = Seq(fadeInShdr)):
        override def update(ctx: CPSceneObjectContext): Unit =
            setX(((ctx.getCanvas.w - this.getWidth) / 2) - 30 - getWidth)

    private val settingsSpr = new CPImageSprite("settings", 0, 45, 1, SETTINGS_IMG, shaders = Seq(fadeInShdr)):
        override def update(ctx: CPSceneObjectContext): Unit =
            setX(((ctx.getCanvas.w - this.getWidth) / 2) + 30)

    private val underLineSpr = new CPCanvasSprite("underLine"):
        private val btns = List(helpSpr, startSpr, settingsSpr)
        private var btnIndex = 1
        private var darkness = 1f
        private var otherDarkness = 3f
        private var lastBtn = btns(btnIndex)

        private def switchBtn(change: Int): Unit =
            lastBtn = btns(btnIndex)
            btnIndex += change
            otherDarkness = darkness
            darkness = 1

        override def render(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas

            val btn = btns(btnIndex)

            // Bottom line.
            canv.drawLine(btn.getX, btn.getY + 1, btn.getX + btn.getWidth - 1, btn.getY + 1, 1, '-'&NEON_BLUE.darker(darkness))

            // Other line.
            if otherDarkness != 3 then
                canv.drawLine(lastBtn.getX, lastBtn.getY + 1, lastBtn.getX + lastBtn.getWidth - 1, lastBtn.getY + 1, 1, '-'&NEON_BLUE.darker(otherDarkness))
                if otherDarkness < 1f then otherDarkness += 0.1f

            if darkness > 0.1f then darkness -= 0.1f

            ctx.getKbEvent match
                case Some(evt) =>
                    evt.key match
                        case KEY_LO_D | KEY_RIGHT => switchBtn(1)
                        case KEY_LO_A | KEY_LEFT => switchBtn(-1)
                        case _ => ()
                case None => ()

            if btnIndex < 0 then btnIndex = btns.length - 1
            else if btnIndex > btns.length - 1 then btnIndex = 0

    private val nowPlaySpr = new CPImageSprite(x = 0, y = 10, z = 4, songPlayingImg(1), false, Seq(fadeInShdr)):
        private var visible = false
        private var lastMs = 0L
        private var darkness = 1f
        private val fadeSpeed = 0.03f

        override def onStart(): Unit =
            super.onStart()
            menuSongChange()

        override def update(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas

            setY(canv.h - 7)
            setX((canv.w / 2) - getWidth / 2)

            // Appear.
            if visible && ctx.getFrameMs - lastMs >= NOW_PLAYING_MS then
                visible = false
                lastMs = ctx.getFrameMs

            if visible && darkness > 0.1 then
                darkness -= fadeSpeed
                setImage(songPlayingImg(darkness))

            // Disappear.
            if !visible && ctx.getFrameMs - lastMs >= NOW_PLAYING_MS then
                visible = true
                lastMs = ctx.getFrameMs

            if !visible && darkness < 1 then
                darkness += fadeSpeed
                setImage(songPlayingImg(darkness))

    private def jukebox(): Unit = ???

    /**
      *
      * @param af
      * @param snd
      * @param measureMs
      */
    private def sequence(af: AudioFile, snd: CPSound, measureMs: Long): Seq[Float] =
        val seq = ArrayBuffer[Float]()
        val buf = af.buffer(BUF_SZ)
        var remain = af.numFrames

        while remain > 0 do
            val chunk = math.min(BUF_SZ, remain).toInt
            af.read(buf, 0, chunk)
            buf.foreach(chan => seq += math.abs(chan.maxBy(math.abs)).toFloat)
            remain -= chunk

        val chanLenMs = snd.getTotalDuration / seq.size
        val splitSz = (measureMs / chanLenMs).toInt

        seq.sliding(splitSz, splitSz).map(_.max).toSeq







    
    private def menuSongChange(): Unit =
        val lvlDir ="gehenna/levels"
        val lvlDirFile = new File(lvlDir)

        def mkFile(res: String): File =
            new File(getClass.getClassLoader.getResource(res).toURI)
        def readLines(res: String): Seq[String] =
            IOUtils.readLines(getClass.getClassLoader.getResourceAsStream(res), Charset.forName("UTF-8")).asScala.toSeq

        val dirs = readLines(lvlDir)
        val rndDir = CPRand.rand(dirs)
        val lvlTxt = readLines(s"$lvlDir/$rndDir/level.txt")
        //introSong.stop()
        DFLT_SONG = CPSound(s"$lvlDir/$rndDir/song.wav")

        val songName = lvlTxt(1).replace(".LevelSongName:", "")
//        println(songName)
//        curBpm = (lvlTxt(2).replace(".LevelBPM:", "")).toInt

        val af = AudioFile.openRead(mkFile(s"$lvlDir/$rndDir/song.wav"))
        songPlay(af)
        af.close()

    private def songPlay(in: AudioFile): Unit =
        DFLT_SONG.play(30, CPSound => menuSongChange())

        sequence(in, DFLT_SONG, 500)

//        val buf = in.buffer(bufSz)
//        println(bufSz)
//
//        var mag = 0.0
//        var remain = in.numFrames
//        while (remain > 0) {
//            val chunk = math.min(bufSz, remain).toInt
//            in.read(buf, 0, chunk)
//            buf.foreach { chan =>
//                mag = math.max(mag, math.abs(chan.maxBy(math.abs)))
//            }
//            remain -= chunk
//        }
//        println(f"Maximum magnitude detected: $mag%1.3f")
//        in.close()

//Exact chunk is 8192
//Exact buffer is [[D@4f3e7344
//Exact buffer size is 8192
















    addObjects(
        titleSpr,
        startSpr,
        helpSpr,
        settingsSpr,
        underLineSpr,
        skullSpr,
        nowPlaySpr
    )
