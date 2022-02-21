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
import scala.util.*


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
    private val paddleSpeed = 0.7f
    private var ballAngle = Random.between(91, 179)
    private val ballSpeed = 1.5f

    private val ballImg = CPArrayImage(
        prepSeq(
            """
              |  _
              | (_)
            """
        ),
        (ch, _, _) => ch&C_DARK_GOLDEN_ROD
    ).trimBg()

    def mkPaddleImage(c: CPColor): CPImage =
        CPArrayImage(
            prepSeq(
                """
                  |X
                  |X
                  |X
                  |X
                  |X
                """
            ),
            (ch, _, _) => ' '&&(C_BLACK, c)
        )

    private val playerImg = mkPaddleImage(C_AQUA)
    private val enemyImg = mkPaddleImage(C_GREEN_YELLOW)

    private final val ballW = ballImg.getWidth
    private final val ballH = ballImg.getHeight

    private def mkScoreImage(score: Int): CPImage = FIG_BIG.render(score.toString, C_WHITE).trimBg()

    private val playerScoreSpr = new CPImageSprite("playerScoreSpr", 0, 0, 0, mkScoreImage(0)):
        override def update(ctx: CPSceneObjectContext): Unit =
            setX((ctx.getCanvas.dim.w - getImage.getWidth) / 4)

    private val enemyScoreSpr = new CPImageSprite("enemyScoreSpr", 0, 0, 0, mkScoreImage(0)):
        override def update(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            setX((canv.dim.w - getImage.getWidth) - ((canv.dim.w / 4) - 1))

    private val ballSpr = new CPImageSprite("ballSpr", 0, 0, 0, ballImg, false, Seq(CPPongBallShader)):
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

                if vert then ballAngle = -ballAngle + 180
                else ballAngle = -ballAngle

            if ballX < canv.xMin then
                ballX = canv.xMax.toFloat / 3
                ballY = canv.yMax.toFloat / 2

                ballAngle = Random.between(1, 89)

                enemyScore += 1
                enemyScoreSpr.setImage(mkScoreImage(enemyScore))
            else if ballY < canv.yMin then
                bounce(ballX, canv.yMin, false)
            else if ballX > ballMaxX then
                ballX = canv.xMax.toFloat - canv.xMax.toFloat / 3
                ballY = canv.yMax.toFloat / 2

                playerScore += 1
                playerScoreSpr.setImage(mkScoreImage(playerScore))

                ballAngle = Random.between(91, 179)
            else if ballY > ballMaxY then
                bounce(ballX, ballMaxY, false)
            else if ballY <= playerPosY.round && ballY >= (playerPosY - 6).round && ballX.round <= 1 then
                bounce(4, ballY, true)
                playerShdr.start()
            else if ballY <= enemyPosY.round && ballY >= (enemyPosY - 6).round && ballX.round >= canv.dim.w - 4 then
                bounce(canv.xMax.toFloat - 4, ballY, true)
                enemyShdr.start()

            setX(ballX.round)
            setY(ballY.round)

    private val netSpr = new CPCanvasSprite("net"):
        override def render(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            canv.drawLine(canv.dim.w / 2, 0, canv.dim.w / 2, canv.dim.h, 100, '|'&C_AQUA)

    private val playerPx = ' '&&(C_BLACK, C_AQUA)
    private val enemyPx = ' '&&(C_BLACK, C_GREEN_YELLOW)
    private val playerColors = Seq(CPColor("#F57064"), CPColor("#4CF5F5"), CPColor("#F5AF33"), CPColor("#40F58E"))
    private val enemyColors = Seq(CPColor("#F57064"), CPColor("#4CF5F5"), CPColor("#F5AF33"), CPColor("#40F58E"))
    private val playerShdr = new CPShimmerShader(false, playerColors, 2, durMs = 500)
    private val enemyShdr = new CPShimmerShader(false, playerColors, 2, durMs = 500)

    private val playerSpr = new CPCanvasSprite("player", Seq(playerShdr)):
        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            setRect(new CPRect(1, (playerPosY - 5).round, 1, 5))
            val canv = ctx.getCanvas
            def move(dy: Float): Unit =
                if dy > 0 && playerPosY < canv.height - 1 then playerPosY += dy
                else if dy < 0 && playerPosY > 5 then playerPosY += dy
            ctx.getKbEvent match
                case Some(evt) =>
                    evt.key match
                        case KEY_LO_W | KEY_UP => move(if evt.isRepeated then -paddleSpeed else -1.0f)
                        case KEY_LO_S | KEY_DOWN => move(if evt.isRepeated then paddleSpeed else 1.0f)
                        case _ => ()
                case None => ()
        override def render(ctx: CPSceneObjectContext): Unit =
            ctx.getCanvas.drawLine(1, playerPosY.round, 1, (playerPosY - 5).round, 100, playerPx)

    private val enemySpr = new CPCanvasSprite("enemy", Seq(enemyShdr)):
        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            val canv = ctx.getCanvas
            setRect(new CPRect(canv.dim.w - 2, (enemyPosY - 5).round, 1, 5))
            if ballY > (enemyPosY - 2.5).round then enemyPosY += paddleSpeed
            else if ballY < (enemyPosY - 2.5).round then enemyPosY -= paddleSpeed
        override def render(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            canv.drawLine(canv.dim.w - 2, enemyPosY.round, canv.dim.w - 2, (enemyPosY - 5).round, 100, enemyPx)

    addObjects(CPKeyboardSprite(KEY_LO_Q, _.exitGame()),
        playerScoreSpr,
        enemyScoreSpr,
        netSpr,
        playerSpr,
        enemySpr,
        ballSpr
    )
