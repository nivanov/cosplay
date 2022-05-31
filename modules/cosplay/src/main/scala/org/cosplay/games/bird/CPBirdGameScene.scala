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

    private val pipeGap = 15f
    private var pipeX = 0f
    private var curPipe = false
    private var pipeCut = 0f
    private var pipeWidth = 5f
    private var pipeCheck = false

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

    private var buildingAmount = 0
    private var buildingTotal = 0
    private val buildingGap = 5

    private val buildingSpeed = 0.6f

    private var buildingMove = false
    private var buildingOffset = 0f

    private var bgW = 0
    private var bgH = 0

    private var grassAmount = 0
    private var grassTotal = 0
    private val grassSpeed = 0.35f // Working value: 0.2f

    private var grassMove = false
    private var grassOffset = 0f

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
            bgW = canv.width
            bgH = canv.height

            ctx.getKbEvent match
                case Some(evt) =>
                    evt.key match
                        case KEY_LO_W | KEY_UP | KEY_SPACE =>
                            if !dead then
                                vel = 0
                                vel -= jump
                                delta = 0.6f

                                if !start then start = true

                        case _ => ()
                case None => ()

            if start then
                vel += delta

            if start then
                if vel <= 0 then
                    setY(getY - 1)
                    vel += 1
                else if vel > 0 then
                    setY(getY + (gravity * vel).toInt)
                    delta += 0.001f

            if (getY <= pipeCut.toInt - pipeGap.toInt) || (getY + getHeight) >= pipeCut.toInt && start then
                if ((getX + getWidth) >= pipeX.toInt) && getX <= pipeX.toInt + (pipeWidth - 1) then
                    dead = true
                    delta = 0.4
                    vel = 0
                    speed = 0f
                    setX(pipeX.toInt - (pipeWidth + 2).toInt)

            if dead && getY >= canv.height then
                start = false

            // Building spawner.
            if ctx.getObjectsForTags("building").length < (canv.width / buildingGap).toInt then
                for x <- 0 to (canv.width / buildingGap).toInt do
                    ctx.addObject(newBuildingSpr(Random.between(minWidth, maxWidth).toInt, Random.between(minHeight, maxHeight).toInt, buildingAmount * buildingGap, Random.between(minDepth, maxDepth).toInt))
                    buildingAmount += 1
                    buildingTotal += 1

            buildingMove = false
            buildingOffset += buildingSpeed
            if buildingOffset >= 1 then
                buildingOffset -= 1
                buildingMove = true

            // Grass spawner.
            if grassAmount < (canv.width / brickImg.getWidth - 1).toInt + 5 then
                for x <- 0 to (canv.width / brickImg.getWidth - 1).toInt + 5 do
                    ctx.addObject(newGrassSpr(grassAmount * brickImg.getWidth - 1, canv.height - brickImg.getHeight))
                    grassAmount += 1
                    grassTotal += 1

            grassMove = false
            grassOffset += grassSpeed
            if grassOffset >= 1 then
                grassOffset -= 1
                grassMove = true

    private val scoreSpr = new CPImageSprite("score", 10, 0, 1, mkScoreImage(score)):
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
                    if pipeX <= -pipeWidth then
                        curPipe = false
                    else
                        pipeX -= speed

                    if pipeX <= birdSpr.getX - pipeWidth  && !pipeCheck then
                        score += 1
                        scoreSpr.setImage(mkScoreImage(score))
                        pipeCheck = true

                canv.drawLine(pipeX.toInt, pipeCut.toInt, pipeX.toInt, canv.dim.h, 0, px)
                canv.drawLine(pipeX.toInt, pipeCut.toInt - pipeGap.toInt, pipeX.toInt, 0, 0, px)

                for x <- 0 until pipeWidth.toInt - 1 do
                    canv.drawLine(pipeX.toInt + x, pipeCut.toInt, pipeX.toInt + x, canv.dim.h, 0, px)
                    canv.drawLine(pipeX.toInt + x, pipeCut.toInt - pipeGap.toInt, pipeX.toInt + x, 0, 0, px)

    /**
     * Creates a new building sprite.
     *
     * @param width
     * @param height
     * @param posX
     * @param startPosY
     * @param depth
     * @return
     */
    private def newBuildingSpr(width:Int, height:Int, posX:Int, depth:Int) : CPSceneObject =
        new CPCanvasSprite("building" + buildingTotal):
            private var newPosX = posX.toFloat
            private val tags = Set("building")
            private var startPosY = 0
            println(depth)

            override def getTags: Set[String] = tags
            override def update(ctx: CPSceneObjectContext): Unit =
                super.update(ctx)
                if buildingMove then newPosX -= 1

                val canv = ctx.getCanvas
                // Roof.
                canv.drawLine(newPosX.toInt, startPosY - height - 1, newPosX.toInt + width, startPosY - height - 1, depth, roofPx)
                // Walls.
                canv.drawLine(newPosX.toInt, startPosY - height, newPosX.toInt, startPosY, depth, wallPx)
                canv.drawLine(newPosX.toInt + width, startPosY - height, newPosX.toInt + width, startPosY, depth, wallPx)
                // Windows.
                for (index <- 0 to height) do
                    canv.drawLine(newPosX.toInt + 1, startPosY + index - height, newPosX.toInt + width - 1, startPosY + index - height, depth, winPx)
                if newPosX  <= 0 - width - 2 then
                    buildingAmount -= 1
                    ctx.deleteObject(getId)

                startPosY = canv.height

    private def newGrassSpr(posX:Int, posY:Int) : CPSceneObject =
        new CPImageSprite("grass" + grassTotal, posX, posY, -1, brickImg):
            private var newPosX = posX.toFloat
            private val tag = Set("grass")

            override def getTags: Set[String] = tag
            override def update(ctx: CPSceneObjectContext): Unit =
                super.update(ctx)

                if grassMove then newPosX -= 1

                val canv = ctx.getCanvas

                setX(newPosX.toInt)

                if getX <= -brickImg.getWidth then
                    ctx.deleteObject(getId)
                    grassAmount -= 1

                setY(canv.height - brickImg.getHeight)

    addObjects(
        // Handle 'Q' press globally for this scene.
        CPKeyboardSprite(KEY_LO_Q, _.exitGame()),
        birdSpr,
        pipeSpr,
        scoreSpr
    )


