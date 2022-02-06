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

package org.cosplay.games.pong

import org.cosplay.CPColor.*
import org.cosplay.*
import org.cosplay.CPArrayImage.*
import prefabs.shaders.*
import org.cosplay.CPFIGLetFont.*
import org.cosplay.CPCanvas.*
import org.cosplay.CPDim.*
import CPPixel.*
import CPKeyboardKey.*
import prefabs.images.*
import prefabs.scenes.*


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
  * Pong main scene.
  */
object CPPongGameScene extends CPScene("game", None, bgPx):
    private var playerScore = 0
    private var enemyScore = 0
    private var playerPosY = 30f
    private var enemyPosY = 30f
    private var ballX = 25
    private var ballY = 20
    private val paddleSpeed = 0.4f
    private var ballAngle = 35
    private val ballSpeed = 1f

    private var playerScoreImg = FIG_BIG.render(playerScore.toString, C_WHITE).trimBg()
    private val enemyScoreImg = FIG_BIG.render(enemyScore.toString, C_WHITE).trimBg()
    private val ballImg = CPArrayImage(
        prepSeq(
            """
              | _
              |(_)
            """
        ),
        (ch, _, _) => ch&C_DARK_GOLDEN_ROD
    ).trimBg()

    private final val ballW = ballImg.getWidth
    private final val ballH = ballImg.getHeight

    private val playerScoreSpr = new CPImageSprite("playerScoreSpr", 0, 0, 0, playerScoreImg, shaders = Seq(CPFadeInShader(true, 1500, bgPx))):
        override def update(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            setX((canv.dim.w - playerScoreImg.getWidth) / 4)

    private val enemyScoreSpr = new CPImageSprite("enemyScoreSpr", 0, 0, 0, enemyScoreImg):
        override def update(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            setX((canv.dim.w - enemyScoreImg.getWidth) - ((canv.dim.w / 4) - 1))

    private val ballSpr = new CPImageSprite("ballSpr", 0, 0, 0, ballImg):
        override def update(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas

            val rad = ballAngle * (Math.PI / 180)
            ballX = (ballX + ballSpeed * Math.cos(rad)).round.toInt
            ballY = (ballY + ballSpeed * -Math.sin(rad)).round.toInt

            var change = false

            def xy(x: Int, y: Int): Unit =
                ballX = x
                ballY = y
                change = true

            val canvMaxX = canv.xMax - ballImg.w + 1
            val canvMaxY = canv.yMax - ballImg.h + 1

            ctx.getLog.info(s"pre ball [x=$ballX, y=$ballY], canv=[xMin=${canv.xMin}, yMin=${canv.yMin}, xMax=$canvMaxX, yMax=$canvMaxY]")

            if ballX < canv.xMin then xy(canv.xMin, ballY)
            if ballY < canv.yMin then xy(ballX, canv.yMin)
            if ballX > canv.xMax - ballImg.w + 1 then xy(canvMaxX, ballY)
            if ballY > canv.yMax - ballImg.h + 1 then xy(ballX, canvMaxY)

            ctx.getLog.info(s"post ball [x=$ballX, y=$ballY], canv=[xMin=${canv.xMin}, yMin=${canv.yMin}, xMax=$canvMaxX, yMax=$canvMaxY]")

            if change then
                ballAngle += 270
                ctx.getLog.info(s"Change angle [angle=$ballAngle]")

            setX(ballX)
            setY(ballY)

    private val borderSpr = new CPCanvasSprite("border"):
        override def render(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas

            canv.drawPolyline(Seq(
                canv.dim.w / 2 -> 0,
                canv.dim.w / 2 -> canv.dim.h
            ), 100, '|'&C_AQUA)

    private val playerPx = CPPixel(' ', C_BLACK, Option(C_AQUA))
    private val enemyPx = CPPixel(' ', C_BLACK, Option(C_GREEN_YELLOW))

    private val playerSpr = new CPCanvasSprite("player"):
        override def render(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas

            def move(dy: Float): Unit =
                if dy > 0 && playerPosY < canv.height - 1 then playerPosY += dy
                else if dy < 0 && playerPosY > 5 then playerPosY += dy

            canv.drawLine(1, playerPosY.round, 1, (playerPosY - 5).round, 100, playerPx)
            ctx.getKbEvent match
                case Some(evt) =>
                    evt.key match
                        case KEY_LO_W | KEY_UP => move(if evt.isRepeated then -paddleSpeed else -1.0f)
                        case KEY_LO_S | KEY_DOWN => move(if evt.isRepeated then paddleSpeed else 1.0f)
                        case _ => ()
                case None => ()

    private val enemySpr = new CPCanvasSprite("enemy"):
        override def render(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            canv.drawLine(canv.dim.w - 2, enemyPosY.round, canv.dim.w - 2, (enemyPosY - 5).round, 100, enemyPx)

            if ballY > enemyPosY then enemyPosY += paddleSpeed
            else if ballY < enemyPosY then enemyPosY -= paddleSpeed

    addObjects(CPKeyboardSprite(KEY_LO_Q, _.exitGame()),
        playerScoreSpr,
        enemyScoreSpr,
        borderSpr,
        playerSpr,
        enemySpr,
        ballSpr
    )
