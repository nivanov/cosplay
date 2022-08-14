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
import org.cosplay.games.gehenna.shaders.TextDripShader
import org.cosplay.prefabs.shaders.*

import java.io.*
import org.apache.commons.io.*

import java.nio.charset.Charset
import scala.io.Source
import scala.jdk.CollectionConverters.*

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
    private val titleText = "Project Gehenna"
    private val startImg = CPImage.loadRexCsv("images/games/gehenna/StartBtn.csv").trimBg()
    private val settingsImg = CPImage.loadRexCsv("images/games/gehenna/SettingsBtn.csv").trimBg()
    private val helpImg = CPImage.loadRexCsv("images/games/gehenna/HelpBtn.csv").trimBg()

    private var menuSong = CPSound("sounds/games/gehenna/intro song.wav")

    private val fadeInShdr = CPSlideInShader.sigmoid(
        CPSlideDirection.CENTRIFUGAL,
        true,
        3000,
        GAME_BG_PX,
        onFinish = _ => TextDripShader.start()
    )

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

    private def songPlayingImg(): CPImage =
        new CPArrayImage(
            prepSeq(
                """
                  |
                  |Now Playing:
                  |
                  """
            ),
            (ch, _, _) => ch&NEON_BLUE
        )

    private val skullSpr = new CPImageSprite(x = 0, y = 0, z = 0, skullImg(), false, Seq(fadeInShdr)):
        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            val canv = ctx.getCanvas

            setY(((canv.w - getWidth) / 2) - 15)
            setX(((canv.h - getHeight) / 2) + 10)

    private def titleImage(): CPImage = FIG_OGRE.render(titleText, BLOOD_RED).trimBg()

    private val titleSpr = new CPImageSprite("title", 0, 0, 1, titleImage(), shaders = Seq(fadeInShdr, TextDripShader)):
        private var introSong = menuSong

        override def update(ctx: CPSceneObjectContext): Unit =
            setX((ctx.getCanvas.w - this.getWidth) / 2)

            if !introSong.isPlaying then
                introSong.loop(3000)

            if introSong != menuSong then
                introSong.stop(0)
                introSong = menuSong

    private val startSpr = new CPImageSprite("start", 0, 43, 1, startImg, shaders = Seq(fadeInShdr)):
        override def update(ctx: CPSceneObjectContext): Unit =
            setX((ctx.getCanvas.w - this.getWidth) / 2)

    private val helpSpr = new CPImageSprite("help", 0, 45, 1, helpImg, shaders = Seq(fadeInShdr)):
        override def update(ctx: CPSceneObjectContext): Unit =
            setX(((ctx.getCanvas.w - this.getWidth) / 2) - 30 - getWidth)

    private val settingsSpr = new CPImageSprite("settings", 0, 45, 1, settingsImg, shaders = Seq(fadeInShdr)):
        override def update(ctx: CPSceneObjectContext): Unit =
            setX(((ctx.getCanvas.w - this.getWidth) / 2) + 30)

    private val underLine: CPCanvasSprite = new CPCanvasSprite("underLine"):
        private val btns: List[CPImageSprite] = List(helpSpr, startSpr, settingsSpr)
        private var btnIndex = 1

        private val menuClick = CPSound("sounds/games/gehenna/UIClick.wav")

        override def render(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas

            val btn = btns(btnIndex)

            // Top line.
            canv.drawLine(btn.getX - 1, btn.getY - 1, btn.getX + btn.getWidth, btn.getY - 1, 1, '_'&NEON_BLUE)

            // Middle line.
            //canv.drawLine(btn.getX - 1, btn.getY, btn.getX + btn.getWidth, btn.getY, 1, '_'&NEON_BLUE)

            // Bottom line.
            canv.drawLine(btn.getX, btn.getY + 1, btn.getX + btn.getWidth - 1, btn.getY + 1, 1, '-'&NEON_BLUE)

            ctx.getKbEvent match
                case Some(evt) =>
                    evt.key match
                        case KEY_LO_D | KEY_RIGHT =>
                            btnIndex += 1
                            menuClick.stop(0)
                            menuClick.play(0)
                        case KEY_LO_A | KEY_LEFT =>
                            btnIndex -= 1
                            menuClick.stop(0)
                            menuClick.play(0)
                        case _ => ()
                case None => ()

            if btnIndex < 0 then
                btnIndex = btns.length - 1
            else if btnIndex > btns.length - 1 then
                btnIndex = 0

    private val nowPlaySpr = new CPImageSprite(x = 0, y = 10, z = 4, songPlayingImg(), false, Seq(fadeInShdr)):
        private final val normSpeed = -1f
        private final val focusSpeed = -0.7f
        private var speed = normSpeed
        private var currX = 0f

        private final val lvlDir ="gehenna/levels"
        private final val lvlDirFile = new File(lvlDir)

        private def readLines(res: String): Seq[String] =
            IOUtils.readLines(getClass.getClassLoader().getResourceAsStream(res), Charset.forName("UTF-8")).asScala.toSeq

        override def onStart(): Unit =
            super.onStart()
            val dirs = readLines(lvlDir)
            val rndDir = CPRand.rand(dirs.toSeq)
            val lvlTxt = readLines(s"$lvlDir/$rndDir/level.txt")
            menuSong = CPSound(s"$lvlDir/$rndDir/song.wav")
            lvlTxt.foreach(println)

            val songName = lvlTxt(1).replace(".LevelSongName:", "")
            println(songName)

        override def update(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas

            setY(canv.h - 7)

            currX += speed
            setX(currX.toInt)

            if (getX >= (canv.w / 2)) && ((getX - getWidth) <= (canv.w / 2)) then
                speed = focusSpeed
            else
                speed = normSpeed

            if currX <= -getWidth - 100 then
                currX = canv.w + 10
                speed = normSpeed

    addObjects(
        titleSpr,
        startSpr,
        helpSpr,
        settingsSpr,
        underLine,
        skullSpr,
        nowPlaySpr
    )
