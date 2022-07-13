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

package org.cosplay.games.bird

import org.cosplay.*
import CPPixel.*
import CPArrayImage.*
import CPFIGLetFont.*
import CPKeyboardKey.*
import CPColor.*
import prefabs.shaders.*
import prefabs.particles.confetti.*
import prefabs.sprites.*
import CPSlideDirection.*

import scala.collection.mutable

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

/**
 *
 */
object CPBirdGameScene extends CPScene("play", None, GAME_BG_PX):
    private val bgSnd = CPSound("sounds/games/bird/bg.wav")
    private val passSnd = CPSound("sounds/games/bird/pass_pipe.wav")
    private val hitSnd = CPSound("sounds/games/bird/hit_pipe.wav", .3f)
    private val fallSnd = CPSound("sounds/games/bird/fall.wav", .7f)
    private val youLostSnd = CPSound("sounds/games/bird/you_lost.wav", .5f)

    // Bird metrics.
    private var speed = 1f
    private var vel = 0f
    private val jump = 5f
    private val gravity = 0.2f
    private var delta = 0.4f
    private var dead = false

    private var score = 0

    private final val BUILD_COLORS = Seq(C_GRAY3, C_GRAY4, C_GRAY5, C_GRAY6, C_GRAY7).map(c => c.setBlue(c.blue + 35))
    private final val PIPE_BG = CPColor("0x4C338F")
    private final val PIPE_PX = '|'&&(PIPE_BG, PIPE_BG)
    private final val BUILD_WALL_PX = ' '&&(C_BLACK, C_GRAY6)
    private final val BUILD_WIN_PX = CPPixel('.', C_X11_ANTIQUE_WHITE, C_GRAY6, tag = 1) // Use tag '1' in shader.
    private final val BUILD_MIN_W = 4
    private final val BUILD_MAX_W = 10
    private final val BUILD_MIN_H = 6
    private final val BUILD_MAX_H = 13
    private final var PIPE_WIDTH = 5
    private final var PIPE_GAP_HEIGHT = 15
    private final val BUILD_GAP_MAX = 4
    private final val BUILD_GAP_MIN = -4

    private var closestPipeX = 60
    private var closestPipeCut = 0

    private val buildSpeed = 7
    private val grassSpeed = 3

    private val fadeInShdr = CPSlideInShader.sigmoid(
        CPSlideDirection.CENTRIFUGAL,
        true,
        1000,
        GAME_BG_PX
    )
    private val CS = Seq(C1, C2, C3, C4, C5)
    private val starStreakShdr = CPStarStreakShader(
        true,
        GAME_BG_PX.bg.get,
        Seq(
            CPStarStreak('.', CS, 0.025, 30, (-.3f, .2f), 0),
            CPStarStreak(':', CS, 0.015, 25, (-.3f, .4f), 0),
            CPStarStreak('|', CS, 0.005, 50, (-.3f, .8f), 0)
        ),
        autoStart = true,
        skip = (zpx, _, _) => zpx.z == 1
    )
    private val borderShdr = CPBorderShader(true, 5, true, -.03f, true)

    private def mkScoreImage(): CPImage = FIG_BIG.render(score.toString, C1).trimBg()

    private val birdImgs = new CPArrayImage(
        prepSeq(
            """
              | \\
              |( ^)>
              | //
              |------
              | //
              |( -)>
              | \\
              |------
            """
        ).filter(!_.endsWith("------")),
        (ch, _, _) => ch match
            case '^' | '-' => ch&C1
            case _ => ch&C4
    ).split(5, 3)

    private val brickImg = new CPArrayImage(
        // 5x3
        prepSeq(
            """
              |^^"^^
              |___[_
              |_[___
            """
        ),
        (ch, _, _) => ch match
            case '^' => '^'&&(C_DARK_GREEN, C_GREEN_YELLOW)
            case '"' => '@'&&(C_GRAY3, C_GREEN_YELLOW)
            case '{' => '['&&(C_SANDY_BROWN, C_DARK_ORANGE3)
            case '-' => '_'&&(C_DARK_ORANGE3, C_DARK_ORANGE3)
            case c => c&&(C_MAROON, C_DARK_ORANGE3)
    )

    private val C = CPColor("0xF360F6")
    private val youLostImg = new CPArrayImage(
        prepSeq(
            """
              |**********************************
              |**                              **
              |**    TRY AGAIN :-)             **
              |**    -------------             **
              |**                              **
              |**    [ENTER]   Restart         **
              |**    [Q]       Quit            **
              |**                              **
              |**    [CTRL+A]  Audio On/OFF    **
              |**    [CTRL+Q]  FPD Overlay     **
              |**    [CTRL+L]  Log Console     **
              |**                              **
              |**********************************
            """),
        (ch, _, _) => ch match
            case '*' => ' '&&(C, C)
            case c if c.isLetter || c == '/' => c&&(C4, BLUE_BLACK)
            case _ => ch&&(C3, BLUE_BLACK)
    )

    private val scoreEmitter = new CPConfettiEmitter(
        () => scoreSpr.getRect.centerX,
        () => scoreSpr.getRect.centerY,
        10,
        15,
        CS.map(c => c.transformHSB(1f, 1.2f, 1f)),
        GAME_BG_PX.fg,
        _ => CPRand.rand("oO0Xx"),
        0
    )
    private val scorePartSpr = CPParticleSprite(emitters = Seq(scoreEmitter))

    private val birdAnis = Seq(CPAnimation.filmStrip("ani", 100, imgs = birdImgs))
    private val birdSpr = new CPAnimationSprite("bird", anis = birdAnis, 15, 5, 10, "ani", false):
        private var cnt = 0L
        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)

            cnt += 1

            if !dead then
                val canv = ctx.getCanvas

                if cnt > CPEngine.fps then // Give it a second...
                    ctx.getKbEvent match
                        case Some(evt) =>
                            evt.key match
                                case KEY_LO_W | KEY_UP | KEY_SPACE =>
                                    vel = 0
                                    vel -= jump
                                    delta = 0.6f
                                case _ => ()
                        case None => ()

                    vel += delta

                    if vel <= 0 then
                        setY(getY - 1)
                        vel += 1
                    else if vel > 0 then
                        setY(getY + (gravity * vel).toInt)
                        delta += 0.001f

                // Bottom pipe.
                if ((getY + getHeight) >= closestPipeCut) &&
                    getX + getWidth >= closestPipeX && // Touching pipe.
                    getX <= closestPipeX + PIPE_WIDTH - 1 then
                    kill(0, true)
                // Top pipe.
                else if (getY <= closestPipeCut - PIPE_GAP_HEIGHT) &&
                    getX + getWidth >= closestPipeX && // Touching pipe.
                    getX <= closestPipeX + PIPE_WIDTH - 1 then
                    kill(0, true)
                else if getY <= 0 then kill(5, false)
                else if getY >= canv.height then kill(0, false)

                // Building spawner.
                val buildExpCnt = (canv.width / BUILD_MAX_W) + BUILD_MAX_W // Number of buildings to fill at least entire screen.
                val buildActCnt = ctx.countObjectsForTags("building")
                if buildActCnt < buildExpCnt then
                    for x <- buildActCnt to buildExpCnt do
                        ctx.addObject(newBuildingSprite(
                            CPRand.between(BUILD_MIN_W, BUILD_MAX_W),
                            CPRand.between(BUILD_MIN_H, BUILD_MAX_H),
                            x * BUILD_MAX_W + CPRand.between(BUILD_GAP_MIN, BUILD_GAP_MAX))
                        )

                // Grass spawner.
                val grassExpCnt = canv.width / brickImg.w + 1 // Number of bricks to fill at least entire screen.
                val grassActCnt = ctx.countObjectsForTags("grass")
                if grassActCnt < grassExpCnt then
                    for x <- grassActCnt to grassExpCnt do
                        ctx.addObject(newGrassSprite(x * brickImg.w, canv.height - brickImg.getHeight))

                // Pipe spawner.
                val pipeSpacing = canv.w / 2
                val pipeExpCnt = (canv.width / pipeSpacing) * 2 // Number of pipes to fill at least entire screen.
                val pipeActCnt = ctx.countObjectsForTags("pipe")
                if pipeActCnt < pipeExpCnt then
                    for x <- pipeActCnt to pipeExpCnt do
                        ctx.addObject(newPipeSprite(5, canv.w + pipeSpacing * x))

                // Game difficulty.
                if score == 0 then
                    PIPE_WIDTH = 5
                    PIPE_GAP_HEIGHT = 15
                else if score == 5 then
                    PIPE_WIDTH = 6
                    PIPE_GAP_HEIGHT = 13
                else if score == 10 then
                    PIPE_WIDTH = 10
                    PIPE_GAP_HEIGHT = 10
                else if score == 15 then
                    PIPE_WIDTH = 13
                    PIPE_GAP_HEIGHT = 7

    private val scoreSpr = new CPImageSprite("score", 0, 0, 1, mkScoreImage()):
        override def update(ctx: CPSceneObjectContext): Unit =
            setX((ctx.getCanvas.width / 2) - 3)

    private val lostShdr = CPSlideInShader.sigmoid(LEFT_TO_RIGHT, false, 1000, GAME_BG_PX)
    private val lostBorderShdr = CPBorderShader(false, 3, true, -.03f, true)
    private val loseSpr = new CPCenteredImageSprite(img = youLostImg, z = 3, Seq(lostShdr, lostBorderShdr)):
        override def update(ctx: CPSceneObjectContext): Unit =
            if dead && isVisible then
                val canv = ctx.getCanvas

                setX((canv.w / 2) - getWidth / 2)
                setY((canv.h / 2) - getHeight / 2)

                ctx.getKbEvent match
                    case Some(evt) =>
                        evt.key match
                            case KEY_ENTER => restart(ctx)
                            case _ => ()
                    case None => ()

    private case class Sparkle(x: Int, y: Int, fg: CPColor, bg: CPColor)
    private val winSparkleColors = Seq(C_DARK_GREEN, C_DARK_ORANGE, C_DARK_GOLDEN_ROD, C_BLUE, C_RED)
    private val winSparkleShdr = new CPShader:
        private val map = mutable.HashMap.empty[String, mutable.HashSet[Sparkle]]

        override def render(ctx: CPSceneObjectContext, objRect: CPRect, inCamera: Boolean): Unit =
            if ctx.isVisible && inCamera then
                val canv = ctx.getCanvas
                val objId = ctx.getId

                if ctx.getFrameCount % buildSpeed == 0 then map.clear()

                val set = map.get(objId) match
                    case Some(s) => s
                    case None => mutable.HashSet.empty

                if set.isEmpty then
                    objRect.loop((x, y) => {
                        if canv.isValid(x, y) then
                            val zpx = canv.getZPixel(x, y)
                            val px = zpx.px
                            if px.tag == 1 && CPRand.randFloat() < .02f then
                                set += Sparkle(x, y, CPRand.rand(winSparkleColors), px.bg.get)
                    })
                    map += objId -> set

                for s <- set do canv.drawPixel('.'&&(s.fg, s.bg), s.x, s.y, 1)

    private def newBuildingSprite(width: Int, height: Int, posX: Int) : CPSceneObject =
        new CPCanvasSprite(shaders = Seq(winSparkleShdr), tags = "building"):
            private var x = posX
            private val col = CPRand.rand(BUILD_COLORS)
            private val wallPx = BUILD_WALL_PX.withBg(Option(col))
            private val winPx = BUILD_WIN_PX.withBg(Option(col))

            override def update(ctx: CPSceneObjectContext): Unit =
                super.update(ctx)

                val canv = ctx.getCanvas
                val y = canv.height - height

                // Walls.
                canv.drawLine(x, y, x, canv.height, 1, wallPx)
                canv.drawLine(x + width, y, x + width, canv.height, 1, wallPx)

                // Windows.
                for index <- 0 to height do canv.drawLine(x + 1, y + index, x + width - 1, y + index, 1, winPx)

                if ctx.getFrameCount % buildSpeed == 0 && !dead then
                    x -= 1
                    if x <= -width then ctx.deleteMyself()

    private def newPipeSprite(width: Int, posX: Int) : CPSceneObject =
        new CPCanvasSprite(tags = "pipe"):
            private var pipeX = posX
            private var finished = false
            private var gapStartY = -1
            private val col = CPRand.randX11Color().darker(0.5f)

            override def render(ctx: CPSceneObjectContext): Unit =
                super.render(ctx)
                val canv = ctx.getCanvas

                if gapStartY == -1 then gapStartY = CPRand.between(12, canv.yMax - 2)
                if !dead then pipeX -= 1
                if pipeX + width <= 0 then ctx.deleteMyself()

                if !finished && pipeX <= birdSpr.getX then
                    finished = true
                    closestPipeX = 60
                    closestPipeCut = 0
                    score += 1
                    scoreSpr.setImage(mkScoreImage())
                    scorePartSpr.resume(true)
                    if audioOn then passSnd.play()

                if !finished && pipeX <= closestPipeX then
                    closestPipeX = pipeX
                    closestPipeCut = gapStartY

                for a <- 0 until width - 1 do
                    val bg = col.lighter(a.toFloat / 20f)
                    val fg = bg.darker(.3f)
                    val px2 = PIPE_PX.withFg(fg).withBg(Option(bg))
                    val x = pipeX + a
                    canv.drawLine(x, gapStartY, x, canv.dim.h, 2, px2)
                    canv.drawLine(x, gapStartY - PIPE_GAP_HEIGHT, x, 0, 2, px2)

    private def newGrassSprite(posX: Int, posY: Int) : CPSceneObject =
        new CPImageSprite(x = posX, y = posY, z = 1, img = brickImg, tags = "grass"):
            override def update(ctx: CPSceneObjectContext): Unit =
                super.update(ctx)
                if ctx.getFrameCount % grassSpeed == 0 && !dead then
                    setX(getX - 1)
                    if getX <= -brickImg.w then ctx.deleteMyself()
                setY(ctx.getCanvas.height - brickImg.h)

    private def kill(velChange: Float, pipe: Boolean) : Unit =
        dead = true
        if audioOn then
            stopBgAudio()
            hitSnd.play(200, _ => fallSnd.play(0, _ => {
                loseSpr.show()
                lostShdr.start()
                birdSpr.hide()
                youLostSnd.play()
            }))
        delta = 0.4
        vel = velChange
        speed = 0f
        if pipe then birdSpr.setX(closestPipeX - PIPE_WIDTH + 2)

    private def restart(ctx : CPSceneObjectContext) : Unit =
        if audioOn then youLostSnd.stop(250)

        // Delete all buildings, grass, and pipes.
        ctx.deleteObjectsForTags("building")
        ctx.deleteObjectsForTags("grass")
        ctx.deleteObjectsForTags("pipe")

        // Reset variables.
        ctx.runNextFrame(_ => closestPipeX = 60)
        ctx.runNextFrame(_ => closestPipeCut = 0)
        speed = 1f
        vel = 0f
        delta = 0.4f
        score = 0
        scoreSpr.setImage(mkScoreImage())

        loseSpr.hide()
        fadeInShdr.start()

        // Reset bird.
        birdSpr.setXY(15, 10)
        birdSpr.show()

        if audioOn then startBgAudio()

        // Make sure NOT to change dead/live state in the middle of frame update.
        ctx.runNextFrame(_ => dead = false)

    loseSpr.hide() // Hide initially.

    addObjects(
        // Exit on 'Q' press.
        CPKeyboardSprite(KEY_LO_Q, _.exitGame()),
        // Toggle audio on 'CTRL+A' press.
        CPKeyboardSprite(KEY_CTRL_A, _ => toggleAudio()),
        // Off screen sprite since shaders are applied to entire screen.
        new CPOffScreenSprite(shaders = Seq(fadeInShdr, borderShdr, starStreakShdr)),
        birdSpr,
        scoreSpr,
        loseSpr,
        scorePartSpr
    )

    private def startBgAudio(): Unit = bgSnd.loop(2000)
    private def stopBgAudio(): Unit = bgSnd.stop(500)

    /**
      * Toggles audio on and off.
      */
    private def toggleAudio(): Unit =
        if audioOn then
            stopBgAudio()
            audioOn = false
        else
            startBgAudio()
            audioOn = true

    override def onDeactivate(): Unit = if audioOn then stopBgAudio()
    override def onActivate(): Unit = if audioOn then startBgAudio()



