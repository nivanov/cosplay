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
    private var ballX = 25f
    private var ballY = 20f
    private val paddleSpeed = 0.4f
    private var ballAngle = 180
    private val ballSpeed = 1.5f

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
            val ballMaxX = (canv.xMax - ballImg.w + 1).toFloat
            val ballMaxY = (canv.yMax - ballImg.h + 1).toFloat

            ballX = ballX + ballSpeed * Math.cos(rad).toFloat
            ballY = ballY + (ballSpeed * 0.7 * -Math.sin(rad)).toFloat

            def bounce(x: Float, y: Float, vert: Boolean): Unit =
                ballX = x
                ballY = y
                if (vert && ballAngle >= 180 && ballAngle <= 270) ||
                    (vert && ballAngle >= 0 && ballAngle <= 90) ||
                    (!vert && ballAngle >= 90 && ballAngle <= 180) ||
                    (!vert && ballAngle >= 270 && ballAngle <= 360) then
                    ballAngle += 450
                else
                    ballAngle += 270
                if !vert then ballAngle += CPRand.randInt(1, 5)
                ballAngle = ballAngle % 360

                if ballAngle >= 0 && ballAngle <= 10 then ballAngle = 11
                else if ballAngle >= 80 && ballAngle <= 90 then ballAngle = 79
                else if ballAngle >= 90 && ballAngle <= 100 then ballAngle = 101
                else if ballAngle >= 170 && ballAngle <= 180 then ballAngle = 169
                else if ballAngle >= 180 && ballAngle <= 190 then ballAngle = 191
                else if ballAngle >= 260 && ballAngle <= 270 then ballAngle = 259
                else if ballAngle >= 270 && ballAngle <= 280 then ballAngle = 281
                else if ballAngle >= 350 && ballAngle <= 360 then ballAngle = 349

            //if ballX < canv.xMin then bounce(canv.xMin, ballY, true)
            if ballX < canv.xMin then
                ballX = canv.xMax/2
                ballY = canv.yMax/2
                ballAngle = -ballAngle

                playerScore += 1
            else if ballY < canv.yMin then bounce(ballX, canv.yMin, false)
            //else if ballX > ballMaxX then bounce(ballMaxX, ballY, true)
            else if ballX > ballMaxX then
                ballX = canv.xMax/2
                ballY = canv.yMax/2
                ballAngle = -ballAngle

                enemyScore += 1
            else if ballY > ballMaxY then bounce(ballX, ballMaxY, false)
            else if ballY <= (playerPosY).round && ballY >= (playerPosY - 6).round && ballX.round <= 1 then
                bounce(3, ballY, true)
                println("Hit player paddle")
            else if ballY <= (enemyPosY).round && ballY >= (enemyPosY - 6).round && ballX.round >= canv.xMax - 3 then
                bounce(canv.xMax - 6, ballY, true)
                println("Hit enemy paddle")

            setX(ballX.round.toInt)
            setY(ballY.round.toInt)

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

            if ballY > (enemyPosY - 2.5).round then enemyPosY += paddleSpeed
            else if ballY < (enemyPosY - 2.5).round then enemyPosY -= paddleSpeed

    addObjects(CPKeyboardSprite(KEY_LO_Q, _.exitGame()),
        playerScoreSpr,
        enemyScoreSpr,
        borderSpr,
        playerSpr,
        enemySpr,
        ballSpr
    )
