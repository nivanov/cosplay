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

package org.cosplay.games.snake

import org.cosplay.*
import games.*
import prefabs.shaders.*
import prefabs.sprites.*
import CPFIGLetFont.*
import CPArrayImage.*
import CPPixel.*
import CPColor.*
import CPKeyboardKey.*

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
  * @param dim Dimension for this scene.
  */
class CPSnakePlayScene(dim: CPDim) extends CPScene("play", Option(dim), BG_PX):
    private var score = 0
    private var go = true
    private var dead = false
    private val borderPx = ' '&&(C1, C1)
    private val scorePx = ' '&&(C2, C2)
    private val bodyPx = ' '&&(C3, C3)
    private val headPx = ' '&&(C4, C4)
    private val applePx = ' '&&(C5, C5)

    private val youLostImg = CPArrayImage(
        prepSeq(
            """
              |*****************************
              |**                         **
              |**    YOU LOST :-(         **
              |**    ~~~~~~~~~~~~         **
              |**                         **
              |**    [SPACE]   Continue   **
              |**    [Q]       Quit       **
              |**                         **
              |*****************************
            """),
        (ch, _, _) => ch match
            case '*' ⇒ ' '&&(C2, C2)
            case c if c.isLetter => c&&(C4, BG_PX.bg.get)
            case _ => ch&&(C3, BG_PX.bg.get)
    )

    private val youWonImg = CPArrayImage(
        prepSeq(
            """
              |*****************************
              |**                         **
              |**    YOU WON :-)          **
              |**    ~~~~~~~~~~~          **
              |**                         **
              |**    [SPACE]   Continue   **
              |**    [Q]       Quit       **
              |**                         **
              |*****************************
            """),
        (ch, _, _) => ch match
            case '*' ⇒ ' '&&(C2, C2)
            case c if c.isLetter => c&&(C4, BG_PX.bg.get)
            case _ => ch&&(C3, BG_PX.bg.get)
    )

    private val scoreSpr = new CPImageSprite(x = 0, y = 0, z = 1, img = mkScoreImage):
        override def update(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            setX((canv.w - getImage.w) / 2)
    private val scoreH = scoreSpr.getHeight
    private val borderSpr = new CPCanvasSprite:
        override def render(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            // Draw border.
            canv.drawRect(0, scoreH, CPDim(canv.w, canv.h - scoreH), 0, (_, _) ⇒ borderPx)
            canv.drawLine(1, scoreH + 1, 1, canv.h, 0, borderPx)
            canv.drawLine(canv.w - 2, scoreH + 1, canv.w - 2, canv.h, 0, borderPx)
            // Draw score rectangle fill.
            canv.fillRect(0, 0, canv.w, scoreH - 1, 0, (_, _) ⇒ scorePx)
    private val snakeSpr = new CPCanvasSprite:
        private final val INIT_SPEED = .5f
        private var snake: List[(Int, Int)] = Nil
        private var dx = 0f
        private var dy = 0f
        private var x = 0f
        private var y = 0f
        private var speed = INIT_SPEED

        override def onActivate(): Unit =
            super.onActivate()
            // Reset snake sprite on each scene activation.
            snake = Nil
            dx = 0f
            dy = 0f
            speed = INIT_SPEED

        /**
          * Turns snake.
          */
        private def turn(dx: Float, dy: Float): Unit =
            this.dx = dx
            this.dy = dy

        /**
          *
          * @param c
          * @return
          */
        private def isDead(c: CPCanvas): Boolean =
            val (hx, hy) = snake.head
            val rx = x * 2
            // Check for self-bite death.
            snake.tail.exists((a, b) ⇒ a == hx && b == hy) || y > c.yMax - 1 || y < scoreH || rx < 1 || rx > c.xMax - 2

        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)

            if go then
                val canv = ctx.getCanvas
                if snake.isEmpty then
                    // Initialize the snake.
                    val cx = canv.xCenter / 2
                    val cy = canv.yCenter
                    for (i ← 0 to 5) snake +:= cx + i -> cy
                    val (hx, hy) = snake.head
                    x = hx.toFloat
                    y = hy.toFloat
                    dx = speed
                    dy = 0
                // Check for snake death.
                if isDead(canv) then
                    go = false
                    dead = true
                    youLostSpr.setVisible(true)
                    youLostSnd.play(1000)
                    bgSnd.stop(1000)
                else
                    // Move snake.
                    x += dx
                    y += dy
                    val xInt = x.round
                    val yInt = y.round
                    val (hx, hy) = snake.head
                    if hx != xInt || hy != yInt then
                        snake = snake.dropRight(1)
                        snake +:= xInt -> yInt
                    ctx.getKbEvent match
                        case Some(evt) =>
                            // Turn snake.
                            evt.key match
                                case KEY_LO_W | KEY_UP => turn(0, -speed)
                                case KEY_LO_S | KEY_DOWN => turn(0, speed)
                                case KEY_LO_A | KEY_LEFT => turn(-speed, 0)
                                case KEY_LO_D | KEY_RIGHT => turn(speed, 0)
                                case _ => ()
                        case None => ()
            else
                if ctx.isKbKey(KEY_SPACE) then fadeOutShdr.start(_.switchScene("title", true))

        override def render(ctx: CPSceneObjectContext): Unit =
            require(snake.nonEmpty)
            val canv = ctx.getCanvas

            def draw(xy: (Int, Int), px: CPPixel): Unit =
                canv.drawPixel(px, xy._1 * 2, xy._2, 1)
                canv.drawPixel(px, xy._1 * 2 + 1, xy._2, 1)

            val hpx = if !go && dead then headPx.withChar('8').withFg(C_BLACK) else headPx
            val bpx = if !go && dead then bodyPx.withChar('X').withFg(C_BLACK) else bodyPx

            // Draw snake.
            draw(snake.head, hpx) // Head.
            snake.tail.foreach(draw(_, bpx)) // Rest of the body.

    // Announcements.
    private val youLostSpr = new CPCenteredImageSprite(img = youLostImg, 6)
    private val youWonSpr = new CPCenteredImageSprite(img = youWonImg, 6)

    private final val bgSnd = CPSound(s"sounds/games/snake/snake.wav", 0.7f)
    private final val yamSnd = CPSound(s"sounds/games/snake/yam.wav")
    private final val youLostSnd = CPSound(s"sounds/games/snake/you_lost.wav")
    private final val youWonSnd = CPSound(s"sounds/games/snake/you_won.wav")

    /** Creates score image. */
    private def mkScoreImage: CPImage = FIG_RECTANGLES.render(s"SCORE : $score", C_BLACK).trimBg()

    // Shaders.
    private val fadeInShdr = CPFadeInShader(true, 500, BG_PX)
    private val fadeOutShdr = CPFadeOutShader(true, 500, BG_PX)

    addObjects(
        new CPOffScreenSprite(Seq(fadeInShdr, fadeOutShdr)),
        // Scene-wide keyboard handlers.
        CPKeyboardSprite(KEY_LO_Q, _.exitGame()), // Handle 'Q' press globally for this scene.
        scoreSpr,
        borderSpr,
        snakeSpr,
        youWonSpr,
        youLostSpr
    )

    override def onDeactivate(): Unit =
        super.onDeactivate()
        bgSnd.stop()
        yamSnd.stop()
        youLostSnd.stop()
        youWonSnd.stop()

    override def onActivate(): Unit =
        super.onActivate()

        score = 0
        go = true
        dead = false
        bgSnd.loopAll(5000) // Start background audio.
        youWonSpr.setVisible(false)
        youLostSpr.setVisible(false)
