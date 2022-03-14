package org.cosplay.games.pong

import org.cosplay.CPFIGLetFont.FIG_BIG
import org.cosplay.CPScene

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

import org.cosplay.*
import CPColor.*
import CPPixel.*
import CPArrayImage.*
import CPFIGLetFont.*
import prefabs.shaders.*
import CPKeyboardKey.*
import org.cosplay.games.pong.shaders.*

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
  * Pong main gameplay scene.
  */
object CPPongPlayScene extends CPScene("play", None, BG_PX):
    private var playerScore = 0
    private var enemyScore = 0
    private final val paddleSpeed = 1.2f
    private final val ballSpeed = 1f
    private var playing = false
    private var ballAngle = if CPRand.between(0, 2) == 1 then CPRand.between(135, 160) else CPRand.between(200, 225)

    private val ballImg = CPArrayImage(
        prepSeq(
            """
              |  _
              | (_)
            """
        ),
        (ch, _, _) => ch&C1
    ).trimBg()

    private def mkPaddleImage(c: CPColor): CPImage =
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

    private val playerImg = mkPaddleImage(C5)
    private val enemyImg = mkPaddleImage(C3)

    private final val ballW = ballImg.getWidth
    private final val ballH = ballImg.getHeight

    private final val bouncePaddleSnd = CPSound(s"sounds/games/pong/bounce1.wav", 0.2f)
    private final val bounceWallSnd = CPSound(s"sounds/games/pong/bounce2.wav", 0.6f)

    private val serveImg = CPArrayImage(
        prepSeq(
            """
              |+----------------------------+
              ||                            |
              ||  Player 1 Ready            |
              ||  ==============            |
              ||                            |
              ||  [SPACE]   Serve Ball      |
              ||  [ESC]     Pause/Resume    |
              ||  [Q]       Quit Any Time   |
              ||                            |
              |+____________________________+
            """),
        (ch, _, _) => ch match
            case c if c.isLetter => c&C4
            case '+' => ch&C1
            case _ => ch&C2
    ).trimBg()

    private val pausedImg = CPArrayImage(
        prepSeq(
            """
              |+-------------------------+
              ||                         |
              ||  Game Paused            |
              ||  ===========            |
              ||                         |
              ||  [ESC]  Resume          |
              ||  [Q]    Quit Any Time   |
              ||                         |
              |+_________________________+
            """),
        (ch, _, _) => ch match
            case c if c.isLetter => c&C4
            case '+' => ch&C1
            case _ => ch&C2
    ).trimBg()

    /**
      *
      * @param score Score value.
      * @return
      */
    private def mkScoreImage(score: Int): CPImage = FIG_BIG.render(score.toString, C4).trimBg()

    /**
      *
      * @param xf X-coordinate producer.
      * @return
      */
    private def mkScoreSprite(xf: (CPCanvas, CPImageSprite) ⇒ Int): CPImageSprite =
        new CPImageSprite(x = 0, y = 0, z = 0, mkScoreImage(0)):
            override def update(ctx: CPSceneObjectContext): Unit = setX(xf(ctx.getCanvas, this))

    // Score sprites.
    private val playerScoreSpr = mkScoreSprite((canv, spr) ⇒ (canv.dim.w - spr.getImage.w) / 4)
    private val enemyScoreSpr = mkScoreSprite((canv, spr) ⇒ (canv.dim.w - spr.getImage.h) - ((canv.dim.w / 4) - 1))

    // Net in the middle.
    private val netSpr = new CPCanvasSprite("net"):
        override def render(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            canv.drawLine(canv.dim.w / 2, 0, canv.dim.w / 2, canv.dim.h, 5, '|'&C2)

    // Player paddle.
    private val playerSpr = new CPImageSprite(x = 0, y = 0, z = 0, playerImg):
        private var y = -1f

        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            val canv = ctx.getCanvas

            if playing then

                if y == -1f then y = getY.toFloat

                def move(dy: Float): Unit =
                    val maxY = canv.height - playerImg.h - 1f
                    if dy > 0 && y < maxY then y = Math.min(maxY, y + dy)
                    else if dy < 0 && y > 0 then y = Math.max(y + dy, 0)
                    setY(y.round)

                ctx.getKbEvent match
                    case Some(evt) =>
                        evt.key match
                            case KEY_LO_W | KEY_UP => move(if evt.isRepeated then -paddleSpeed else -1.0f)
                            case KEY_LO_S | KEY_DOWN => move(if evt.isRepeated then paddleSpeed else 1.0f)
                            case _ => ()
                    case None => ()

    // Computer paddle.
    private val enemySpr = new CPImageSprite(x = 1, y = 0, z = 0, enemyImg):
        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            val canv = ctx.getCanvas
            setX(canv.dim.w - enemyImg.w)

    // Ball sprite.
    private val ballSpr = new CPImageSprite("bs", 0, 0, 1, ballImg, false, Seq(CPPongBallShader)):
        private var x, y = -1f

        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            val canv = ctx.getCanvas
            if playing then
                if x == -1f && y == -1f then
                    x = getX.toFloat
                    y = getY.toFloat

                val rad = ballAngle * (Math.PI / 180)
                val xMax = canv.xMax - ballImg.w + 1f
                val yMax = canv.yMax - ballImg.h + 1f

                x += (ballSpeed * Math.cos(rad)).toFloat
                y += (ballSpeed * 0.7 * -Math.sin(rad)).toFloat

                setX(Math.min(Math.max(x, 0f), xMax).round)
                setY(Math.min(Math.max(y, 0f), yMax).round)

    // Serve announcement.
    private val serveSpr = new CPImageSprite(x = 0, y = 0, z = 6, serveImg):
        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            val canv = ctx.getCanvas
            // Center itself.
            setX((canv.dim.w - getImage.getWidth) / 2)
            setY((canv.dim.h - getImage.getHeight) / 2)

            // Center ball, player and enemy sprites.
            if !playing then
                setVisible(true)
                playerSpr.setY(canv.dim.h / 2 - playerImg.h / 2)
                enemySpr.setY(canv.dim.h / 2 - enemyImg.h / 2)
                ballSpr.setX(canv.dim.w - enemyImg.w - ballImg.w - 3)
                ballSpr.setY(canv.dim.h / 2)

            ctx.getKbEvent match
                case Some(evt) =>
                    evt.key match
                        case KEY_SPACE =>
                            setVisible(false)
                            playing = true
                        case _ => ()
                case None => ()

    addObjects(
        // Scene-wide Keyboard handlers.
        CPKeyboardSprite(KEY_LO_Q, _.exitGame()), // Handle 'Q' press globally for this scene.
        CPKeyboardSprite(KEY_ESC, _ ⇒ playing = !playing), // Handle 'ESC' press globally for this scene.
        // Main game elements.
        playerScoreSpr,
        enemyScoreSpr,
        netSpr,
        enemySpr,
        playerSpr,
        ballSpr,
        serveSpr,
        // Scene-wide shader holder.
        new CPOffScreenSprite(shaders = Seq(CPFadeInShader(true, 1000, BG_PX)))
    )



