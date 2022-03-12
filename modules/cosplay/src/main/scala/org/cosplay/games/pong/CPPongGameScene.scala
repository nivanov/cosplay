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
import org.cosplay.games.pong.shaders.*
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
object CPPongGameScene extends CPScene("game", None, BG_PX):
    private var playerScore = 0
    private var enemyScore = 0
    private var playerPosY = 30f
    private var enemyPosY = 30f
    private var ballX = 25f
    private var ballY = 20f
    private val paddleSpeed = 1f
    private var ballAngle = 135
    private val ballSpeed = 1f
    private var startGame = false
    private var firstRound = true
    private var playerMovement = 0
    private var enemyMovement = 0

    private var lastBallY = 0f
    private var lastPlayerY = 0f
    private var lastEnemyY = 0f

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

    private final val bouncePaddleSnd = CPSound(s"sounds/games/pong/bounce1.wav", 0.2f)
    private final val bounceWallSnd = CPSound(s"sounds/games/pong/bounce2.wav", 0.6f)

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

    /**
      *
      * @param score
      * @return
      */
    private def mkScoreImage(score: Int): CPImage = FIG_BIG.render(score.toString, C_WHITE).trimBg()

    /**
      *
      * @param xf
      * @return
      */
    def mkScoreSprite(xf: (CPCanvas, CPImageSprite) ⇒ Int): CPImageSprite =
        new CPImageSprite(x = 0, y = 0, z = 0, mkScoreImage(0)):
            override def update(ctx: CPSceneObjectContext): Unit = setX(xf(ctx.getCanvas, this))

    private val playerScoreSpr = mkScoreSprite((canv, spr) ⇒ (canv.dim.w - spr.getImage.getWidth) / 4)
    private val enemyScoreSpr = mkScoreSprite((canv, spr) ⇒ (canv.dim.w - spr.getImage.getWidth) - ((canv.dim.w / 4) - 1))

    private val serveSpr = new CPImageSprite(x = 0, y = 0, z = 6, serveImg):
        override def update(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            setX((canv.dim.w - getImage.getWidth) / 2)
            setY((canv.dim.h - getImage.getHeight) / 2)

            if !startGame then
                setVisible(true)
                playerPosY = (canv.dim.h / 2) - 2f
                enemyPosY = (canv.dim.h / 2) - 2f

            ctx.getKbEvent match
                case Some(evt) =>
                    evt.key match
                        case KEY_SPACE =>
                            setVisible(false)
                            ballSpr.setVisible(true)
                            startGame = true
                        case _ => ()
                case None => ()

    private val ballSpr = new CPImageSprite("bs", 0, 0, 1, ballImg, false, Seq(CPPongBallShader)):
        override def update(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas

            lastBallY = ballY

            if firstRound then
                playerPosY = (canv.dim.h / 2) - 2f
                enemyPosY = (canv.dim.h / 2) - 2f

                ballX = canv.dim.wF - 8
                ballY = canv.dim.hF / 2

                if Random.between(0, 2).toInt == 1 then ballAngle = Random.between(135, 160)
                else ballAngle = Random.between(200, 225)

                firstRound = false

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

                if ballX.round <= 10 && vert then
                    if lastBallY > ballY && lastPlayerY > playerPosY then
                        ballAngle = 80
                    else if lastBallY < ballY && lastPlayerY < playerPosY then
                        ballAngle = 280

                if ballX.round >= canv.width - 10 && vert then
                    if lastBallY > ballY && lastEnemyY > enemyPosY then
                        ballAngle = 100
                    else if lastBallY < ballY && lastEnemyY < enemyPosY then
                        ballAngle = 300

                if vert then bouncePaddleSnd.playOnce() else bounceWallSnd.playOnce()

            def score(es: Int, ps: Int): Unit =
                if es < ps then
                    ballX = enemySpr.getX - 6f
                    ballY = canv.dim.hF / 2

                    if Random.between(0, 2) == 1 then ballAngle = Random.between(135, 160)
                    else ballAngle = Random.between(200, 225)
                else
                    ballX = playerSpr.getX + 2f
                    ballY = canv.dim.hF / 2

                    if Random.between(0, 2) == 1 then ballAngle = Random.between(-45, -20)
                    else ballAngle = Random.between(20, 45)

                enemyScore += es
                playerScore += ps
                enemyScoreSpr.setImage(mkScoreImage(enemyScore))
                playerScoreSpr.setImage(mkScoreImage(playerScore))
                startGame = false

            if ballX < canv.xMin - 4 then score(1, 0)
            else if ballY < canv.yMin then bounce(ballX, canv.yMin, false)
            else if ballX > ballMaxX + 2 then score(0, 1)
            else if ballY > ballMaxY then bounce(ballX, ballMaxY, false)
            else if ballY >= playerPosY.round - 2 && ballY <= (playerPosY + playerSpr.getHeight - 1).round && ballX.round <= 1 then
                bounce(1, ballY, true)
                playerShdr.start()
            else if ballY >= enemyPosY.round - 2 && ballY <= (enemyPosY + enemySpr.getHeight - 1).round && ballX.round >= canv.dim.w - enemySpr.getWidth - 5 then
                bounce(canv.dim.wF - enemySpr.getWidth - 5, ballY, true)
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

    private val playerSpr = new CPImageSprite(x = 1, y = 0, z = 0, mkPaddleImage(C_AQUA)):
        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            val canv = ctx.getCanvas

            lastPlayerY = playerPosY
            setY(playerPosY.toInt)

            def move(dy: Float): Unit =
                if dy > 0 && playerPosY < canv.height - 5 then playerPosY += dy.toInt
                else if dy < 0 && playerPosY > 0 then playerPosY += dy.toInt

            if startGame then
                ctx.getKbEvent match
                    case Some(evt) =>
                        evt.key match
                            case KEY_LO_W | KEY_UP => move(if evt.isRepeated then -paddleSpeed else -1.0f)
                            case KEY_LO_S | KEY_DOWN => move(if evt.isRepeated then paddleSpeed else 1.0f)
                            case _ => ()
                    case None => ()

    private val enemySpr = new CPImageSprite(x = 0, y = 0, z = 0, mkPaddleImage(C_RED)):
        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            val canv = ctx.getCanvas
            lastEnemyY = enemyPosY
            setX(canv.dim.w - 2)
            setY(enemyPosY.toInt)
            if startGame then
                if ballY >= enemyPosY + getHeight then enemyPosY += paddleSpeed
                else if ballY <= enemyPosY then enemyPosY -= paddleSpeed
                if enemyPosY <= 0 then enemyPosY = 0
                else if enemyPosY >= canv.h + getHeight + 1 then enemyPosY = canv.hF + getHeight + 1

    addObjects(CPKeyboardSprite(KEY_LO_Q, _.exitGame()),
        playerScoreSpr,
        enemyScoreSpr,
        netSpr,
        enemySpr,
        ballSpr,
        serveSpr,
        playerSpr,
        new CPOffScreenSprite(shaders = Seq(CPFadeInShader(true, 1000, BG_PX)))
    )
