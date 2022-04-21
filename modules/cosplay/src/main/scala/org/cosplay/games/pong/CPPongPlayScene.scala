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

import org.cosplay.*
import games.*
import CPColor.*
import CPPixel.*
import CPArrayImage.*
import CPFIGLetFont.*
import CPKeyboardKey.*
import prefabs.shaders.*
import prefabs.sprites.*
import pong.shaders.*
import CPSlideDirection.*
import org.apache.commons.lang3.SystemUtils
import org.cosplay.prefabs.particles.confetti.CPConfettiEmitter

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
    private def randBallAngle(): Int = if CPRand.between(0, 2) == 1 then CPRand.between(135, 160) else CPRand.between(200, 225)

    private final val INIT_VAL = -1f
    private final val MAX_SCORE = 10
    private final val IS_WIN = SystemUtils.IS_OS_WINDOWS
    private final val INIT_BALL_SPEED = if IS_WIN then .8f else .9f
    private final val INIT_NPC_SPEED = if IS_WIN then .6f else .7f
    private final val BALL_SPEED_INCR = if IS_WIN then .045f else .05f
    private final val ENEMY_SPEED_INCR = if IS_WIN then .045f else .06f

    private var plyScore = 0
    private var npcScore = 0
    private final val plySpeed = 1.2f
    private var npcSpeed = INIT_NPC_SPEED
    private var ballSpeed = INIT_BALL_SPEED
    private var playing = false
    private var gameOver = false
    private var ballAngle = randBallAngle()

    private val ballImg = CPArrayImage(
        prepSeq(
            """
              | _
              |(_)
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

    private val plyImg = mkPaddleImage(C5)
    private val npcImg = mkPaddleImage(C3)
    require(plyImg.h == npcImg.h)
    require(plyImg.w == npcImg.w)
    private val paddleH = plyImg.h
    private val paddleW = plyImg.w

    private val plyShdr = CPPongPaddleShader()
    private val npcShdr = CPPongPaddleShader()

    private final val ballW = ballImg.getWidth
    private final val ballH = ballImg.getHeight

    // Audio assets.
    private final val paddleSnd = CPSound(s"sounds/games/pong/bounce1.wav", 0.2f)
    private final val wallSnd = CPSound(s"sounds/games/pong/bounce2.wav", 0.6f)
    private final val missSnd = CPSound(s"sounds/games/pong/miss.wav", 0.3f)
    private final val youLostSnd = CPSound(s"sounds/games/pong/you_lost.wav", 0.5f)
    private final val youWonSnd = CPSound(s"sounds/games/pong/you_won.wav", 0.5f)
    private final val bgSnd = CPSound(s"sounds/games/pong/bg.wav", 0.02f)
    private final val boostSnd = CPSound(s"sounds/games/pong/boost.wav", 0.2f)

    private val serveImg = CPArrayImage(
        prepSeq(
            """
              |+------------------------------+
              ||                              |
              ||    PLAYER 1 READY            |
              ||    ==============            |
              ||                              |
              ||    [SPACE]   Serve Ball      |
              ||    [CTRL&A]  Audio On/Off    |
              ||    [CTRL&L]  Log Console     |
              ||    [CTRL&Q]  FPS Overlay     |
              ||    [Q]       Quit            |
              ||                              |
              |+______________________________+
            """),
        (ch, _, _) => ch match
            case c if c.isLetter => c&C4
            case '+' => ch&C1
            case '&' ⇒ '+'&C4
            case _ => ch&C2
    ).trimBg()

    private val youLostImg = CPArrayImage(
        prepSeq(
            """
              |+------------------------------+
              ||                              |
              ||    YOU LOST :-(              |
              ||    ============              |
              ||                              |
              ||    [SPACE]   Continue        |
              ||    [CTRL&A]  Audio On/Off    |
              ||    [CTRL&L]  Log Console     |
              ||    [CTRL&Q]  FPS Overlay     |
              ||    [Q]       Quit            |
              ||                              |
              |+______________________________+
            """),
        (ch, _, _) => ch match
            case c if c.isLetter => c&C4
            case '&' ⇒ '+'&C4
            case '+' => ch&C1
            case _ => ch&C2
    ).trimBg()

    private val youWonImg = CPArrayImage(
        prepSeq(
            """
              |+------------------------------+
              ||                              |
              ||    YOU WON 8-)               |
              ||    ===========               |
              ||                              |
              ||    [SPACE]   Continue        |
              ||    [CTRL&A]  Audio On/Off    |
              ||    [CTRL&L]  Log Console     |
              ||    [CTRL&Q]  FPS Overlay     |
              ||    [Q]       Quit            |
              ||                              |
              |+______________________________+
            """),
        (ch, _, _) => ch match
            case c if c.isLetter => c&C4
            case '&' ⇒ '+'&C4
            case '+' => ch&C1
            case _ => ch&C2
    ).trimBg()

    /**
      * Creates score image.
      *
      * @param score Score value.
      */
    private def mkScoreImage(score: Int): CPImage = FIG_BIG.render(score.toString, C4).trimBg()

    /**
      * Create score sprite.
      *
      * @param xf X-coordinate producer.
      */
    private def mkScoreSprite(xf: (CPCanvas, CPImageSprite) ⇒ Int): CPImageSprite =
        new CPImageSprite(x = 0, y = 0, z = 0, mkScoreImage(0)): // Initial score is zero.
            override def update(ctx: CPSceneObjectContext): Unit = setX(xf(ctx.getCanvas, this))

    // Score sprites.
    private val plyScoreSpr = mkScoreSprite((canv, spr) ⇒ (canv.dim.w - spr.getImage.w) / 4)
    private val npcScoreSpr = mkScoreSprite((canv, spr) ⇒ (canv.dim.w - spr.getImage.h) - ((canv.dim.w / 4) - 1))
    private val plyScoreEmitter = new CPConfettiEmitter(
        () ⇒ plyScoreSpr.getRect.xCenter,
        () ⇒ plyScoreSpr.getRect.h / 2,
        15,
        15,
        CS,
        BG_PX.fg,
        _ ⇒ CPRand.rand("1234567890"),
        1
    )
    private val npcScoreEmitter = new CPConfettiEmitter(
        () ⇒ npcScoreSpr.getRect.xCenter,
        () ⇒ npcScoreSpr.getRect.h / 2,
        15,
        15,
        CS,
        BG_PX.fg,
        _ ⇒ CPRand.rand("1234567890"),
        1
    )
    private val plyScorePartSpr = CPParticleSprite(emitters = Seq(plyScoreEmitter))
    private val npcScorePartSpr = CPParticleSprite(emitters = Seq(npcScoreEmitter))

    // Boost announcement sprite.
    private val boostShdr = new CPShimmerShader(
        false,
        CS,
        2,
        true,
        skip = (zpx, _, _) ⇒ zpx.z != 10 || zpx.char == BG_PX.char || zpx.char == ' '
    )
    private val boostSpr = new CPImageSprite(x = 0, y = 0, z = 10, FIG_RECTANGLES.render("boost", C1).trimBg(), shaders = Seq(boostShdr)):
        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            val canv = ctx.getCanvas
            setX((canv.w - getWidth) / 2)
            setY(canv.h - getHeight - 2)

    // Net in the middle.
    private val netSpr = new CPCanvasSprite("net"):
        override def render(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            canv.drawLine(canv.dim.w / 2, 0, canv.dim.w / 2, canv.dim.h, 5, '|'&C2)

    // Player paddle.
    private val plySpr = new CPImageSprite(x = 0, y = 0, z = 0, plyImg, false, Seq(plyShdr)):
        private var y = INIT_VAL

        override def reset(): Unit =
            super.reset()
            y = INIT_VAL

        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            val canv = ctx.getCanvas
            if playing then
                if y == INIT_VAL then y = getY.toFloat

                def move(dy: Float): Unit =
                    y = clipPaddleY(canv, y, dy)
                    setY(y.round)

                ctx.getKbEvent match
                    case Some(evt) =>
                        evt.key match
                            case KEY_LO_W | KEY_UP => move(if evt.isRepeated then -plySpeed else -1.0f)
                            case KEY_LO_S | KEY_DOWN => move(if evt.isRepeated then plySpeed else 1.0f)
                            case _ => ()
                    case None => ()

    /**
      * Clips the position of the paddle on the screen.
      */
    private def clipPaddleY(canv: CPCanvas, currY: Float, dy: Float): Float =
        val maxY = canv.hF - paddleH
        if dy > 0 then Math.min(maxY, currY + dy)
        else if dy < 0 then Math.max(currY + dy, 0)
        else currY

    // NPC paddle sprite.
    private val npcSpr: CPImageSprite = new CPImageSprite(x = 1, y = 0, z = 0, npcImg, false, Seq(npcShdr)):
        private var y = INIT_VAL

        override def reset(): Unit =
            super.reset()
            y = INIT_VAL

        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            val canv = ctx.getCanvas
            setX(canv.w - paddleW)

            if playing then
                if y == INIT_VAL then y = getY.toFloat
                if CPRand.randFloat() < 0.95 then // Randomize enemy behavior.
                    val ballY = ballSpr.getY.toFloat
                    val dy = if y > ballY then -npcSpeed else if (y + paddleH / 2) < ballY then npcSpeed else 0f
                    y = clipPaddleY(canv, y, dy)
                    setY(y.round)

    // Ball sprite.
    private val ballSpr = new CPImageSprite("bs", 0, 0, 1, ballImg, false,
        Seq(CPPongBallBoostShader, new CPFlashlightShader(radius = 6))):
        private var x, y = INIT_VAL
        private var boosted = false

        override def reset(): Unit =
            super.reset()
            boosted = false
            x = INIT_VAL
            y = INIT_VAL

        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            val canv = ctx.getCanvas
            if playing then
                if x == INIT_VAL && y == INIT_VAL then
                    x = getX.toFloat
                    y = getY.toFloat

                // Adjust ball speed based on the canvas dimensions.
                var bs = canv.wF / canv.hF * 1.1f
                if boosted then bs *= 1.5f
                val rad = ballAngle * (Math.PI / 180)
                val xMax = canv.xMax - ballImg.w + 1f
                val yMax = canv.yMax - ballImg.h + 1f

                x += (bs * Math.cos(rad)).toFloat * ballSpeed
                y += (bs * 0.7 * -Math.sin(rad)).toFloat * ballSpeed

                def setBoost(on: Boolean): Unit =
                    boosted = on
                    boostSpr.setVisible(on)
                    if on then
                        CPPongBallBoostShader.start()
                        if audioOn then boostSnd.replay()
                    else
                        CPPongBallBoostShader.stop()
                        boostSnd.stop()

                def paddleReturn(isPly: Boolean): Unit =
                    setBoost(false)
                    val yr = y.round
                    val edge =
                        if isPly then
                            yr == plySpr.getY - ballH ||
                            yr == plySpr.getY - ballH + 1 ||
                            yr == plySpr.getY + plyImg.h ||
                            yr == plySpr.getY + plyImg.h - 1
                        else
                            yr == npcSpr.getY - ballH ||
                            yr == npcSpr.getY - ballH  + 1 ||
                            yr == npcSpr.getY + npcImg.h ||
                            yr == npcSpr.getY + npcImg.h - 1
                    if edge then setBoost(true)
                    x = if isPly then paddleW.toFloat else canv.wF - paddleW - ballW - 2
                    ballAngle = -ballAngle + 180 + CPRand.randInt(0, 10) - 5
                    if audioOn then paddleSnd.replay()
                    if isPly then plyShdr.start() else npcShdr.start()

                def score(plyScr: Int, npcScr: Int): Unit =
                    setBoost(false)

                    plyScore += plyScr
                    npcScore += npcScr

                    if plyScr > 0 then
                        ballSpeed += BALL_SPEED_INCR
                        npcSpeed += ENEMY_SPEED_INCR
                        plyScorePartSpr.resume(reset = true)
                    else
                        npcScorePartSpr.resume(reset = true)

                    if audioOn then missSnd.replay()
                    npcScoreSpr.setImage(mkScoreImage(npcScore))
                    plyScoreSpr.setImage(mkScoreImage(plyScore))

                    def finishGame(spr: CPImageSprite, shdr: CPSlideInShader, snd: CPSound): Unit =
                        playing = false
                        gameOver = true
                        bgSnd.stop(500) // Stop background audio.
                        shdr.start()
                        spr.show()
                        if audioOn then snd.replay(3000)

                    if plyScore == MAX_SCORE then finishGame(youWonSpr, youWonShdr, youWonSnd)
                    else if npcScore == MAX_SCORE then finishGame(youLostSpr, youLostShdr, youLostSnd)
                    else playing = false

                if x < 1 && y > plySpr.getY - ballH && y < plySpr.getY + paddleH then paddleReturn(true)
                else if x > xMax - 1 && y > npcSpr.getY - ballH && y < npcSpr.getY + paddleH then paddleReturn(false)
                else if y > yMax || y < 0 then
                    ballAngle = -ballAngle
                    if audioOn then wallSnd.replay()
                else if x < 0 then score(0, 1)
                else if x > canv.xMax then score(1, 0)

                setX(Math.min(Math.max(x, 0f), xMax).round)
                setY(Math.min(Math.max(y, 0f), yMax).round)

    // Announcements.
    private def mkShader = CPSlideInShader.sigmoid(CENTRIFUGAL, false, 500, BG_PX)
    private val serveShdr = mkShader
    private val youLostShdr = mkShader
    private val youWonShdr = mkShader
    private val serveSpr = new CPCenteredImageSprite(img = serveImg, 6, Seq(serveShdr))
    private val youLostSpr = new CPCenteredImageSprite(img = youLostImg, 6, Seq(youLostShdr))
    private val youWonSpr = new CPCenteredImageSprite(img = youWonImg, 6, Seq(youWonShdr))

    private val gameCtrlSpr = new CPOffScreenSprite():
        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            val canv = ctx.getCanvas
            if !playing then
                if gameOver then
                    if ctx.isKbKey(KEY_SPACE) then ctx.switchScene("title")
                    end if
                else // Not playing and not game over - first serve.
                    serveSpr.show()

                    // Reset positions.
                    plySpr.reset()
                    npcSpr.reset()
                    ballSpr.reset()
                    plySpr.setXY(0, canv.dim.h / 2 - paddleH / 2)
                    npcSpr.setXY(canv.w - paddleW, canv.dim.h / 2 - paddleH / 2)
                    ballSpr.setXY(canv.dim.w - paddleW - ballImg.w - 3, canv.dim.h / 2)
                    ballAngle = randBallAngle()

                    if ctx.isKbKey(KEY_SPACE) then
                        serveSpr.hide()
                        serveShdr.start()
                        playing = true
                        if audioOn then paddleSnd.replay()

    addObjects(
        // Handle 'Q' press globally for this scene.
        CPKeyboardSprite(KEY_LO_Q, _.exitGame()),
        // Toggle audio on 'Ctrl+A' press.
        CPKeyboardSprite(KEY_CTRL_A, _ => toggleAudio()),
        // Score sprites.
        plyScoreSpr,
        npcScoreSpr,
        plyScorePartSpr,
        npcScorePartSpr,
        // Game elements.
        netSpr,
        npcSpr,
        plySpr,
        ballSpr,
        boostSpr,
        // Game controller.
        gameCtrlSpr,
        // Announcements.
        serveSpr,
        youLostSpr,
        youWonSpr,
        // Scene-wide shader holder.
        new CPOffScreenSprite(shaders = Seq(CPFadeInShader(true, 1000, BG_PX)))
    )

    private def stopAudio(): Unit =
        youWonSnd.stop()
        youLostSnd.stop()
        wallSnd.stop()
        paddleSnd.stop()
        bgSnd.stop()

    override def onDeactivate(): Unit =
        super.onDeactivate()
        stopAudio()

    override def onActivate(): Unit =
        super.onActivate()

        // Start background audio.
        if audioOn then bgSnd.loop(2000)

        // All announcements are invisible initially.
        serveSpr.hide()
        youLostSpr.hide()
        youWonSpr.hide()
        boostSpr.hide()

        // State machine.
        playing = false
        gameOver = false
        plyScore = 0
        npcScore = 0
        npcSpeed = INIT_NPC_SPEED
        ballSpeed = INIT_BALL_SPEED
        ballAngle = randBallAngle()

        npcScoreSpr.setImage(mkScoreImage(npcScore))
        plyScoreSpr.setImage(mkScoreImage(plyScore))

    private def toggleAudio(): Unit =
        if audioOn then
            stopAudio() // Stop all sounds.
            audioOn = false
        else
            bgSnd.loop(2000)
            audioOn = true





