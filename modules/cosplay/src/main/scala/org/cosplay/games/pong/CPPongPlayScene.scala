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
    private val ballImg = CPArrayImage(
        prepSeq(
            """
              |  _
              | (_)
            """
        ),
        (ch, _, _) => ch&C1
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

    private val playerImg = mkPaddleImage(C2)
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
              ||  [SPACE]   Serve Ball      |
              ||  [Q]       Quit Any Time   |
              ||                            |
              |+____________________________+
            """),
        (ch, _, _) => ch match
            case c if c.isLetter => c&C4
            case '+' => ch&C1
            case _ => ch&C2
    ).trimBg()

    /**
      *
      * @param score
      * @return
      */
    private def mkScoreImage(score: Int): CPImage = FIG_BIG.render(score.toString, C4).trimBg()

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

    private val netSpr = new CPCanvasSprite("net"):
        override def render(ctx: CPSceneObjectContext): Unit =
            val canv = ctx.getCanvas
            canv.drawLine(canv.dim.w / 2, 0, canv.dim.w / 2, canv.dim.h, 5, '|'&C2)

    addObjects(
        CPKeyboardSprite(KEY_LO_Q, _.exitGame()),
        playerScoreSpr,
        enemyScoreSpr,
        netSpr,
//        enemySpr,
//        ballSpr,
//        serveSpr,
//        playerSpr,
        new CPOffScreenSprite(shaders = Seq(CPFadeInShader(true, 1000, BG_PX)))
    )



