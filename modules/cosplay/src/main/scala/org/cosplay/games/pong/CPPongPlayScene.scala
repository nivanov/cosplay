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
import org.cosplay.games.*
import org.cosplay.CPColor.*
import org.cosplay.CPPixel.*
import org.cosplay.CPArrayImage.*
import org.cosplay.CPFIGLetFont.*
import org.cosplay.CPKeyboardKey.*
import org.cosplay.prefabs.shaders.*
import org.cosplay.games.pong.shaders.*
import org.cosplay.prefabs.shaders.CPSlideDirection.*
import org.apache.commons.lang3.SystemUtils
import org.cosplay.prefabs.particles.CPConfettiEmitter
import org.cosplay.prefabs.sprites.CPCenteredImageSprite

/*
   _________            ______________
   __  ____/_______________  __ \__  /_____ _____  __
   _  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
   / /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
   \____/  \____//____/ /_/     /_/  \__,_/ _\__, /
                                            /____/

          2D ASCII JVM GAME ENGINE FOR SCALA3
              (C) 2021 Rowan Games, Inc.
                All rights reserved.
*/

/**
  * Pong main gameplay scene.
  */
object CPPongPlayScene extends CPScene("play", None, BG_PX):
    private def randBallAngle(): Int = if CPRand.between(0, 2) == 1 then CPRand.between(135, 160) else CPRand.between(200, 225)

    private val INIT_VAL = -1f
    private val MAX_SCORE = 10
    private val IS_WIN = SystemUtils.IS_OS_WINDOWS
    private val INIT_BALL_SPEED = if IS_WIN then .8f else .9f
    private val INIT_NPC_SPEED = if IS_WIN then .6f else .7f
    private val BALL_SPEED_INCR = if IS_WIN then .045f else .05f
    private val ENEMY_SPEED_INCR = if IS_WIN then .045f else .06f

    private var plyScore = 0
    private var npcScore = 0
    private val plySpeed = 1.2f
    private var npcSpeed = INIT_NPC_SPEED
    private var ballSpeed = INIT_BALL_SPEED
    private var playing = false
    private var gameOver = false
    private var ballAngle = randBallAngle()

    private val ballImg = new CPArrayImage(
        prepSeq(
            """
              | _
              |(_)
            """
        ),
        (ch, _, _) => ch&C1
    ).trimBg()

    private def mkPaddleImage(c: CPColor): CPImage =
        new CPArrayImage(
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
    !>(plyImg.h == npcImg.h)
    !>(plyImg.w == npcImg.w)
    private val paddleH = plyImg.h
    private val paddleW = plyImg.w

    private val plyShdr = CPPongPaddleShader()
    private val npcShdr = CPPongPaddleShader()

    private val ballW = ballImg.getWidth
    private val ballH = ballImg.getHeight

    // Audio assets.
    private val paddleSnd = CPSound(s"sounds/games/pong/bounce1.wav", 0.2f)
    private val wallSnd = CPSound(s"sounds/games/pong/bounce2.wav", 0.6f)
    private val missSnd = CPSound(s"sounds/games/pong/miss.wav", 0.3f)
    private val youLostSnd = CPSound(s"sounds/games/pong/you_lost.wav", 0.5f)
    private val youWonSnd = CPSound(s"sounds/games/pong/you_won.wav", 0.5f)
    private val bgSnd = CPSound(s"sounds/games/pong/bg.wav", 0.02f)
    private val boostSnd = CPSound(s"sounds/games/pong/boost.wav", 0.2f)

    private val menuSkin = (ch: Char, _: Int, _: Int) => ch match
        case c if c.isLetter => c & C4
        case '+' => ch & C1
        case '&' => '+' & C4
        case _ => ch & C2
    private val serveImg = new CPArrayImage(
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
        menuSkin
    ).trimBg()

    private val youLostImg = new CPArrayImage(
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
        menuSkin
    ).trimBg()

    private val youWonImg = new CPArrayImage(
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
        menuSkin
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
    private def mkScoreSprite(xf: (CPCanvas, CPImageSprite) => Int): CPImageSprite =
        new CPImageSprite(x = 0, y = 0, z = 0, mkScoreImage(0)): // Initial score is zero.
            override def update(ctx: CPSceneObjectContext): Unit = setX(xf(ctx.getCanvas, this))

    // Score sprites.
    private val plyScoreSpr = mkScoreSprite((canv, spr) => (canv.dim.w - spr.getImage.w) / 4)
    private val npcScoreSpr = mkScoreSprite((canv, spr) => (canv.dim.w - spr.getImage.h) - ((canv.dim.w / 4) - 1))
    private def mkEmitter(spr: CPImageSprite): CPConfettiEmitter =
        new CPConfettiEmitter(
            () => spr.getRect.centerX,
            () => spr.getRect.h / 2,
            genSize = 15,
            maxAge = 15,
            CS,
            BG_PX.fg,
            _ => "1234567890".rand,
            z = 1
        )
    private val plyScoreEmitter = mkEmitter(plyScoreSpr)
    private val npcScoreEmitter = mkEmitter(npcScoreSpr)
    private val plyScorePartSpr = CPParticleSprite(emitters = plyScoreEmitter.seq)
    private val npcScorePartSpr = CPParticleSprite(emitters = npcScoreEmitter.seq)

    // Boost announcement sprite.
    private val boostShdr = new CPShimmerShader(
        false,
        CS,
        keyFrame = 2,
        true,
        skip = (zpx, _, _) => zpx.z != 10 || zpx.char == BG_PX.char || zpx.char == ' '
    )
    private val boostSpr = new CPImageSprite(x = 0, y = 0, z = 10, FIG_RECTANGLES.render("boost", C1).trimBg(), shaders = boostShdr.seq):
        override def update(ctx: CPSceneObjectContext): Unit =
            super.update(ctx)
            val canv = ctx.getCanvas
            setX((canv.w - getWidth) / 2)
            setY(canv.h - getHeight - 2)

    // Net in the middle.
    private val netSpr = new CPCanvasSprite("net"):
        override def render(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            canv.drawLine(canv.dim.w / 2, 0, canv.dim.w / 2, canv.dim.h, z = 5, '|'&C2)

    // Player paddle.
    private val plySpr = new CPImageSprite(x = 0, y = 0, z = 0, plyImg, false, plyShdr.seq):
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
    private val npcSpr: CPImageSprite = new CPImageSprite(x = 1, y = 0, z = 0, npcImg, false, npcShdr.seq):
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
    private val ballSpr = new CPImageSprite("bs", x = 0, y = 0, z = 1, ballImg, false,
        Seq(CPPongBallBoostShader, new CPFlashlightShader(radius = 6, autoStart = true))):
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
                    def isEdge(spr: CPImageSprite): Boolean =
                        val y = spr.getY
                        val y2 = y + spr.getHeight
                        yr == y - ballH || yr == y - ballH + 1 || yr == y2 || yr == y2 - 1
                    val edge = if isPly then isEdge(plySpr) else isEdge(npcSpr)
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
                        bgSnd.stop(500.ms) // Stop background audio.
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
    private def mkShader = CPSlideInShader.sigmoid(CENTRIFUGAL, false, 500.ms, BG_PX)
    private val serveShdr = mkShader
    private val youLostShdr = mkShader
    private val youWonShdr = mkShader
    private val serveSpr = new CPCenteredImageSprite(img = serveImg, 6, serveShdr.seq)
    private val youLostSpr = new CPCenteredImageSprite(img = youLostImg, 6, youLostShdr.seq)
    private val youWonSpr = new CPCenteredImageSprite(img = youWonImg, 6, youWonShdr.seq)

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
        // Toggle audio on 'CTRL+A' press.
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
        new CPOffScreenSprite(shaders = CPFadeInShader(true, 1000.ms, BG_PX).seq)
    )

    private def stopAudio(): Unit =
        youWonSnd.stop()
        youLostSnd.stop()
        wallSnd.stop()
        paddleSnd.stop()
        bgSnd.stop()

    override def onDeactivate(): Unit = stopAudio()
    override def onActivate(): Unit =
        // Start background audio.
        if audioOn then bgSnd.loop(2000.ms)

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
            bgSnd.loop(2000.ms)
            audioOn = true





