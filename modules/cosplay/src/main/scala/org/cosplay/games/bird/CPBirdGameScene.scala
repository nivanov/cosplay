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

import scala.util.*
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
    private var speed = 1f
    private var vel = 0f
    private val jump = 7f
    private val gravity = 0.3f
    private var delta = 0.4f

    private var start = true
    private var dead = false

    private var score = 0

    private val darkerWhiteBg = C_WHITE.darker(0.1)
    private val roofPx = ' '&&(C_BLACK.lighter(0.1), darkerWhiteBg)
    private val wallPx = ' '&&(C_BLACK.lighter(0.1), darkerWhiteBg)
    private val winPx = '.'&&(C_BLACK, C_WHITE)

    private val minWidth = 3
    private val maxWidth = 10
    private val minHeight = 6
    private val maxHeight = 13
    private val minDepth = -5
    private val maxDepth = -1

    private val pipeGap = 15f
    private var pipeX = 0f
    private var curPipe = false
    private var pipeCut = 0f
    private var pipeWidth = 5f
    private var pipeCheck = false

    private val buildingGap = 2

    private val buildingSpeed = 7

//    private var bgW = 0
//    private var bgH = 0

    private val grassSpeed = 3



    //-----------------------------------------


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

                if (getY <= pipeCut.toInt - pipeGap.toInt || (getY + getHeight) >= pipeCut.toInt) &&
                    getX + getWidth >= pipeX.toInt &&
                    getX <= pipeX.toInt + pipeWidth - 1 then
                        dead = true
                        delta = 0.4
                        vel = 0
                        speed = 0f
                        setX(pipeX.toInt - (pipeWidth + 2).toInt)

            if dead && getY >= canv.height - 5 then
                start = false

            // Building spawner.
            val buildExpCnt = canv.width * 2 / buildingGap
            val buildActCnt = ctx.getObjectsForTags("building").length
            if buildActCnt < buildExpCnt then
                for x <- 0 to buildExpCnt do
                    ctx.addObject(newBuildingSpr(Random.between(minWidth, maxWidth), Random.between(minHeight, maxHeight), x * buildingGap, Random.between(minDepth, maxDepth)))

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
        private val px = '|'&&(C_GREEN,C_GREEN)
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
                    if pipeX <= -pipeWidth then curPipe = false else pipeX -= speed
                    if pipeX <= birdSpr.getX - pipeWidth  && !pipeCheck then
                        score += 1
                        scoreSpr.setImage(mkScoreImage(score))
                        pipeCheck = true

                val xi = pipeX.toInt
                val ci = pipeCut.toInt
                val gi = pipeGap.toInt

                canv.drawLine(xi, ci, xi, canv.dim.h, 0, px)
                canv.drawLine(xi, ci - gi, xi, 0, 0, px)
                for x <- 0 until pipeWidth.toInt - 1 do
                    canv.drawLine(xi + x, ci, xi + x, canv.dim.h, 0, px)
                    canv.drawLine(xi + x, ci - gi, xi + x, 0, 0, px)

    private def newBuildingSpr(width: Int, height: Int, posX: Int, depth: Int) : CPSceneObject =
        new CPCanvasSprite():
            private var x = posX
            private val tags = Set("building")

            override def getTags: Set[String] = tags
            override def update(ctx: CPSceneObjectContext): Unit =
                super.update(ctx)

                val canv = ctx.getCanvas
                val y = canv.height - height

                // Roof.
                canv.drawLine(x, y - 1, x + width, y - 1, -1, roofPx)

                // Walls.
                canv.drawLine(x, y, x, canv.height, -1, wallPx)
                canv.drawLine(x + width, y, x + width, canv.height, -1, wallPx)

                // Windows.
                for (index <- 0 to height) do
                    canv.drawLine(x + 1, y + index, x + width - 1, y + index, -1, winPx)

                if ctx.getFrameCount % buildingSpeed == 0 then
                    x -= 1
                    if x <= -width then
                        ctx.deleteMyself()

    /**
      *
      * @param posX
      * @param posY
      * @return
      */
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


