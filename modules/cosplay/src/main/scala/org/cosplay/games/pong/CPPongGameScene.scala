package org.cosplay.games.pong

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
    private var ballAngle = Random.between(30, 60)
    private val ballSpeed = 1.5f
    private var startGame = false

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

    private val serveImg = CPArrayImage(
        prepSeq(
            """
              |+----------------+
              ||                |
              ||   Serve Ball   |
              ||                |
              ||    [SPACE]     |
              ||                |
              |+________________+
            """),
        (ch, _, _) => ch match
            case c if c.isLetter => c&C_STEEL_BLUE1
            case '+' => ch&C_STEEL_BLUE1
            case '[' | ']' => ch.toUpper&C_DARK_ORANGE
            case _ => ch&C_LIME
    ).trimBg()

    private val playerScoreSpr = new CPImageSprite(x = 0, y = 0, z = 0, mkScoreImage(0)):
        override def update(ctx: CPSceneObjectContext): Unit =
            setX((ctx.getCanvas.dim.w - getImage.getWidth) / 4)

    private val enemyScoreSpr = new CPImageSprite(x = 0, y = 0, z = 0, mkScoreImage(0)):
        override def update(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            setX((canv.dim.w - getImage.getWidth) - ((canv.dim.w / 4) - 1))

    private val serveSpr = new CPImageSprite(x = 0, y = 0, z = 6, serveImg):
        override def update(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            setX((canv.dim.w - getImage.getWidth) / 2)
            setY((canv.dim.h - getImage.getHeight) / 2)

            if !startGame then
                setVisible(true)
                ballSpr.setVisible(false)
                playerPosY = (canv.dim.h / 2) + 2f
                enemyPosY = (canv.dim.h / 2) + 2f

            ctx.getKbEvent match
                case Some(evt) =>
                    evt.key match
                        case KEY_SPACE =>
                            setVisible(false)
                            ballSpr.setVisible(true)
                            startGame = true
                        case _ => ()
                case None => ()

    private val ballSpr = new CPImageSprite("bs", 0, 0, 0, ballImg, false, Seq(CPPongBallShader)):
        override def update(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            val rad = ballAngle * (Math.PI / 180)
            val ballMaxX = (canv.xMax - ballImg.w + 1).toFloat
            val ballMaxY = (canv.yMax - ballImg.h + 1).toFloat

            if startGame then
                ballX = ballX + ballSpeed * Math.cos(rad).toFloat
                ballY = ballY + (ballSpeed * 0.7 * -Math.sin(rad)).toFloat

            def bounce(x: Float, y: Float, vert: Boolean): Unit =
                ballX = x
                ballY = y
                if vert then ballAngle = -ballAngle + 180
                else ballAngle = -ballAngle

            def score(es: Int, ps: Int): Unit =
                ballX = canv.xMaxF / 2
                ballY = canv.yMaxF / 2
                ballAngle = Random.between(30, 60)
                enemyScore += es
                playerScore += ps
                enemyScoreSpr.setImage(mkScoreImage(enemyScore))
                playerScoreSpr.setImage(mkScoreImage(playerScore))
                startGame = false

            if ballX < canv.xMin then score(1, 0)
            else if ballY < canv.yMin then bounce(ballX, canv.yMin, false)
            else if ballX > ballMaxX then score(0, 1)
            else if ballY > ballMaxY then bounce(ballX, ballMaxY, false)
            else if ballY <= playerPosY.round && ballY >= (playerPosY - playerImg.getHeight).round && ballX.round <= 1 then
                bounce(4, ballY, true)
                playerShdr.start()
            else if ballY <= enemyPosY.round && ballY >= (enemyPosY - enemyImg.getHeight).round && ballX.round >= canv.dim.w - 4 then
                bounce(canv.xMaxF - 4, ballY, true)
                enemyShdr.start()

            setX(ballX.round)
            setY(ballY.round)

    private val netSpr = new CPCanvasSprite("net"):
        override def render(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            canv.drawLine(canv.dim.w / 2, 0, canv.dim.w / 2, canv.dim.h, 5, '|'&C_AQUA)

    private val playerPx = ' '&&(C_BLACK, C_AQUA)
    private val enemyPx = ' '&&(C_BLACK, C_GREEN_YELLOW)
    private val playerShdr = CPPongPaddleShader('>')
    private val enemyShdr = CPPongPaddleShader('<')

    private val playerSpr = new CPCanvasSprite():
        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            val canv = ctx.getCanvas
            def move(dy: Float): Unit =
                if dy > 0 && playerPosY < canv.height - 1 then playerPosY += dy
                else if dy < 0 && playerPosY > 5 then playerPosY += dy

            if startGame then
                ctx.getKbEvent match
                    case Some(evt) =>
                        evt.key match
                            case KEY_LO_W | KEY_UP => move(if evt.isRepeated then -paddleSpeed else -1.0f)
                            case KEY_LO_S | KEY_DOWN => move(if evt.isRepeated then paddleSpeed else 1.0f)
                            case _ => ()
                    case None => ()
        override def render(ctx: CPSceneObjectContext): Unit =
            ctx.getCanvas.drawLine(1, playerPosY.round, 1, (playerPosY - 5).round, 100, playerPx)

    private val enemySpr = new CPCanvasSprite():
        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            val canv = ctx.getCanvas
            if ballY > enemyPosY - 2.5 then enemyPosY += paddleSpeed
            else if ballY < enemyPosY - 2.5 then enemyPosY -= paddleSpeed

            if enemyPosY < canv.height - 1 then enemyPosY += paddleSpeed
            else if enemyPosY > 5 then enemyPosY -= paddleSpeed

            if startGame then
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
        ballSpr,
        serveSpr,
        CPOffScreenSprite(shaders = Seq(CPFadeInShader(true, 1000, bgPx)))
    )
