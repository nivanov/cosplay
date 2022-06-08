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
import games.*
import CPPixel.*
import CPArrayImage.*
import CPFIGLetFont.*
import CPKeyboardKey.*
import prefabs.shaders.*
import CPColor.*

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
    private val youLostSnd = CPSound("sounds/games/bird/you_lost.wav")

    // Bird metrics.
    private var speed = 1f
    private var vel = 0f
    private val jump = 7f
    private val gravity = 0.3f
    private var delta = 0.4f

    private var start = true
    private var dead = false

    private var score = 0

    private final val BUILD_COLORS = Seq(C_GRAY3, C_GRAY4, C_GRAY5, C_GRAY6, C_GRAY7)
    private final val BUILD_WALL_PX = ' '&&(C_BLACK, C_GRAY6)
    private final val BUILD_WIN_PX = '.'&&(C_WHITE, C_GRAY6)
    private final val BUILD_MIN_W = 4
    private final val BUILD_MAX_W = 10
    private final val BUILD_MIN_H = 6
    private final val BUILD_MAX_H = 13
    private final val PIPE_GAP = 15f
    private final val PIPE_WIDTH = 5f
    private final val BUILD_GAP_MAX = 2

    private var pipeX = 0f
    private var curPipe = false
    private var pipeCut = 0f
    private var pipeCheck = false

    private val buildSpeed = 7
    private val grassSpeed = 3

    private def mkScoreImage(score: Int): CPImage = FIG_BIG.render(score.toString, C4).trimBg()

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
            case '^' | '-' => ch&C_WHITE
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

    private val birdAnis = Seq(CPAnimation.filmStrip("ani", 250, imgs = birdImgs))
    private val birdSpr = new CPAnimationSprite("bird", anis = birdAnis, 15, 5, 0, "ani", false):
        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            val canv = ctx.getCanvas

            ctx.getKbEvent match
                case Some(evt) =>
                    evt.key match
                        case KEY_LO_W | KEY_UP | KEY_SPACE => if !dead then
                            vel = 0
                            vel -= jump
                            delta = 0.6f
                            if !start then start = true
                        case _ => ()
                case None => ()

            if start then
                vel += delta

                if vel <= 0 then
                    setY(getY - 1)
                    vel += 1
                else if vel > 0 then
                    setY(getY + (gravity * vel).toInt)
                    delta += 0.001f

                if (getY <= pipeCut.toInt - PIPE_GAP.toInt || (getY + getHeight) >= pipeCut.toInt) &&
                    getX + getWidth >= pipeX.toInt &&
                    getX <= pipeX.toInt + PIPE_WIDTH - 1 then
                        dead = true
                        if audioOn then hitSnd.play(0, _ ⇒ fallSnd.play())
                        delta = 0.4
                        vel = 0
                        speed = 0f
                        setX(pipeX.toInt - (PIPE_WIDTH + 2).toInt)

            if dead && getY >= canv.height - 5 then
                start = false

            // Building spawner.
            val buildExpCnt = canv.width * 2 / BUILD_GAP_MAX
            val buildActCnt = ctx.getObjectsForTags("building").length
            if buildActCnt < buildExpCnt then
                for x <- buildActCnt to buildExpCnt do
                    ctx.addObject(newBuildingSprite(
                        CPRand.between(BUILD_MIN_W, BUILD_MAX_W),
                        CPRand.between(BUILD_MIN_H, BUILD_MAX_H),
                        x * BUILD_MAX_W + CPRand.between(0, BUILD_GAP_MAX))
                    )

            // Grass spawner.
            val grassExpCnt = canv.width / brickImg.w + brickImg.w // Number of bricks to fill at least entire screen.
            val grassActCnt = ctx.getObjectsForTags("grass").length
            if grassActCnt < grassExpCnt then
                for x <- grassActCnt to grassExpCnt do
                    ctx.addObject(newGrassSprite(x * brickImg.w, canv.height - brickImg.getHeight))

    private val scoreSpr = new CPImageSprite("score", 0, 0, 1, mkScoreImage(score)):
        override def update(ctx: CPSceneObjectContext): Unit =
            setX((ctx.getCanvas.width / 2) - 3)

    private val pipeSpr = new CPCanvasSprite("pipe"):
        private val FG = CPColor("0xE617BC")
        private val BG = CPColor("0x8E0CF2")
        private val px = 'X'&&(FG, BG)
        override def render(ctx: CPSceneObjectContext): Unit =
            super.render(ctx)
            val canv = ctx.getCanvas
            if start then
                if !curPipe then
                    curPipe = true
                    pipeCheck = false
                    pipeX = canv.xMax.toFloat
                    pipeCut = CPRand.between(12f, canv.yMax - 2f)
                else
                    if pipeX <= -PIPE_WIDTH then curPipe = false else pipeX -= speed
                    if pipeX <= birdSpr.getX - PIPE_WIDTH && !pipeCheck then
                        score += 1
                        scoreSpr.setImage(mkScoreImage(score))
                        pipeCheck = true
                        if audioOn then passSnd.play()

                val x = pipeX.toInt
                val c = pipeCut.toInt
                val g = PIPE_GAP.toInt

                for a <- 0 until PIPE_WIDTH.toInt - 1 do
                    val px2 = px.withBg(Option(px.bg.get.lighter(a.toFloat / 10f)))
                    canv.drawLine(x + a, c, x + a, canv.dim.h, 0, px2)
                    canv.drawLine(x + a, c - g, x + a, 0, 0, px2)

    private def newBuildingSprite(width: Int, height: Int, posX: Int) : CPSceneObject =
        new CPCanvasSprite():
            private var x = posX
            private val tags = Set("building")
            private val col = CPRand.rand(BUILD_COLORS)
            private val wallPx = BUILD_WALL_PX.withBg(Option(col))
            private val winPx = BUILD_WIN_PX.withBg(Option(col))

            override def getTags: Set[String] = tags
            override def update(ctx: CPSceneObjectContext): Unit =
                super.update(ctx)

                val canv = ctx.getCanvas
                val y = canv.height - height

                // Walls.
                canv.drawLine(x, y, x, canv.height, -1, wallPx)
                canv.drawLine(x + width, y, x + width, canv.height, -1, wallPx)

                // Windows.
                for (index <- 0 to height) do
                    canv.drawLine(x + 1, y + index, x + width - 1, y + index, -1, winPx)

                if ctx.getFrameCount % buildSpeed == 0 then
                    x -= 1
                    if x <= -width then
                        ctx.deleteMyself()

    private def newGrassSprite(posX: Int, posY: Int) : CPSceneObject =
        new CPImageSprite(x = posX, y = posY, z = -1, img = brickImg):
            override def getTags: Set[String] = Set("grass")
            override def update(ctx: CPSceneObjectContext): Unit =
                super.update(ctx)
                if ctx.getFrameCount % grassSpeed == 0 then
                    setX(getX - 1)
                    if getX <= -brickImg.w then
                        ctx.deleteMyself()
                setY(ctx.getCanvas.height - brickImg.h)

    addObjects(
        // Handle 'Q' press globally for this scene.
        CPKeyboardSprite(KEY_LO_Q, _.exitGame()),
        birdSpr,
        pipeSpr,
        scoreSpr
    )

    private def startBgAudio(): Unit = bgSnd.loop(2000)
    private def stopBgAudio(): Unit = bgSnd.stop(400)

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

    override def onDeactivate(): Unit = stopBgAudio()



