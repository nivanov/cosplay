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

package org.cosplay.examples.animation

import org.cosplay.CPArrayImage.prepSeq
import org.cosplay.CPColor.*
import org.cosplay.CPPixel.*
import org.cosplay.CPKeyboardKey.*
import org.cosplay.CPPixel.XRAY
import org.cosplay.prefabs.scenes.CPLogoScene
import org.cosplay.prefabs.shaders.*
import org.cosplay.*

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
  * Code example for animation functionality.
  *
  * @see [[CPAnimation]]
  * @see [[CPAnimationContext]]
  * @see [[CPAnimationSprite]]
  * @see [[CPAnimationKeyFrame]]
  * @note See developer guide at [[https://cosplayengine.com]]     
  */
object CPAnimationExample:
    /**
      * Entry point for JVM runtime.
      *
      * @param args Ignored.
      */
    def main(args: Array[String]): Unit =
        val skin = (ch: Char, _: Int, _: Int) => ch match
            case ' ' => XRAY
            case 'o' => ch&C_LIGHT_CORAL
            case _ => ch&C_WHITE

        val imgsRight = CPArrayImage(
            // 8 frames @ 6x3
            prepSeq(
                """
                  |  _ o
                  |   /\
                  |  | \
                  |------
                  |
                  |  ,\o
                  | /)|
                  |------
                  | __|
                  |   \o
                  |   ( \
                  |------
                  |  \ /
                  |   |
                  |  /o\
                  |------
                  |   |__
                  | o/
                  |/ )
                  |------
                  |
                  | o/_
                  | | (\
                  |------
                  | o _
                  | /\
                  | / |
                  |------
                """).filter(!_.endsWith("------")
            ),
            skin).split(6, 3)
        val imgsLeft = imgsRight.map(_.horFlip())
        val imgsIdle = CPArrayImage(
            prepSeq(
                """
                  |   o
                  |  /|\
                  |  / \
                  |------
                  |   o
                  |  /|'
                  |  / \
                  |------
                  |   o-
                  |  /|
                  |  / \
                  |------
                  |  -o
                  |   |\
                  |  / \
                  |------
                  |   o
                  |  '|\
                  |  / \
                  |------
                """).filter(!_.endsWith("------")
            ),
            skin).split(5, 3)
        val imgVert = CPArrayImage(
            prepSeq(
                """
                  |   o/
                  |  /|
                  |  / \
                  |------
                  |   o)
                  |  /|
                  |  ( \
                  |------
                  |  (o
                  |   |\
                  |  / )
                  |------
                  |  \o
                  |   |\
                  |  / \
                  |------
                """).filter(!_.endsWith("------")
            ),
            skin).split(5, 3)
        val imgHelp = CPArrayImage(
            prepSeq(
                """
                  |                    UP
                  |               .----..----.
                  |               | /\ || w  |
                  |    LEFT       `----'`----'       RIGHT
                  |.----..----.                  .----..----.
                  || <- || a  |                  | -> || d  |
                  |`----'`----'                  `----'`----'
                  |                   DOWN
                  |               .----..----.
                  |               | \/ || s  |
                  |               `----'`----'
                """),
            (ch, _, _) => ch match
                case c if c.isLetter && c.isLower => c.toUpper&C_STEEL_BLUE1
                case c if c.isLetter && c.isUpper => c&C_DARK_ORANGE
                case _ => ch.toUpper&C_STEEL_BLUE1
        ).trimBg()

        val bgPx = CPPixel('.', C_GRAY2, C_GRAY1)
        val dim = CPDim(100, 40)

        // Initialize the engine.
        CPEngine.init(
            CPGameInfo(name = "Animation Example", initDim = Option(dim)),
            System.console() == null || args.contains("emuterm")
        )

        val bgSnd = CPSound(src = "sounds/examples/bg0.wav")
        val stepSnd = CPSound(src = "sounds/examples/step.wav")
        val hopSnd = CPSound(src = "sounds/examples/hop.wav")

        val aniSeq = Seq(
            // In film strip animation each frame is shown for the same amount of time.
            CPAnimation.filmStrip("right", 150, imgs = imgsRight),
            CPAnimation.filmStrip("left", 150, imgs = imgsLeft),
            // In time-based animation each frame has its own duration.
            CPAnimation.timeBased("idle", frames = Seq(
                imgsIdle.head -> 1000,
                imgsIdle(1) -> 100,
                imgsIdle(2) -> 1000,
                imgsIdle(3) -> 1000,
                imgsIdle(4) -> 100
            )),
            CPAnimation.filmStrip("vert", 150, imgs = imgVert)
        )

        val fiShdr = new CPFadeInShader(true, 500, bgPx)
        val foShdr = new CPFadeOutShader(true, 300, bgPx, _.exitGame())

        val player = new CPAnimationSprite("player", aniSeq, 45, 19, 0, "idle", false, Seq(fiShdr, foShdr)):
            // Use 'float' type for coordinates to smooth out the movement.
            private var x = super.getX.toFloat
            private var y = super.getY.toFloat

            private def move(ctx: CPSceneObjectContext, dx: Float, dy: Float): Unit =
                if dx != 0f then
                    // Change animation without waiting for the current one to complete.
                    change(if dx < 0 then "left" else "right", finish = false)
                    x += dx
                    if dx.abs.round == 1.0f || ctx.getFrameCount % 6 == 0 then hopSnd.play()
                else if dy != 0f then
                    // Change animation without waiting for the current one to complete.
                    change("vert", finish = false)
                    y += dy

            override def onStart(): Unit =
                super.onStart()
                bgSnd.setVolume(0.2f) // Make background 20% volume.
                bgSnd.loopAll(1500) // Auto-play with fade-in.
                // Example of the per-frame sound synchronization.
                setOnKeyFrameChange("vert", Option((_, _) => stepSnd.play()))
            override def getX: Int = x.round
            override def getY: Int = y.round
            override def update(ctx: CPSceneObjectContext): Unit =
                super.update(ctx)
                // Demo the log snapshoting (rendering stats get logged in every 2 seconds).
                if ctx.getFrameCount % 60 == 0 then ctx.getLog.snapshot()
                ctx.getKbEvent match
                    case Some(evt) =>
                        evt.key match
                            // NOTE: if keyboard event is repeated (same key pressed in consecutive frames) -
                            // we use smaller movement amount to smooth out the movement. If key press
                            // is "new" we move the entire character position to avoid an initial "dead keystroke" feel.
                            case KEY_LO_A | KEY_LEFT => move(ctx, if evt.isRepeated then -0.35f else -1.0f, 0)
                            case KEY_LO_D | KEY_RIGHT => move(ctx, if evt.isRepeated then 0.35f else 1.0f, 0)
                            case KEY_LO_W | KEY_UP => move(ctx, 0, if evt.isRepeated then -0.3f else -1.0f)
                            case KEY_LO_S | KEY_DOWN => move(ctx, 0, if evt.isRepeated then 0.3f else 1.0f)
                            case _ => ()
                    // Switch to 'idle' waiting for the current animation to complete (default).
                    case None => change("idle")

        val sc = new CPScene("scene", Option(dim), bgPx,
            player,
            CPStaticImageSprite(28, 28, 0, imgHelp),
            // On 'Ctrl-q' kick in fade out shader that will exit the game once it is finished.
            CPKeyboardSprite(KEY_LO_Q, _ => foShdr.start()) // Exit the game on 'q' press.
        )

        try
            // Start the game & wait for exit.
            CPEngine.startGame(
                new CPLogoScene(
                    "logo",
                    Option(dim),
                    bgPx,
                    Seq(C_ORANGE1, C_STEEL_BLUE1, C_DARK_ORANGE, C_WHITE, C_LIGHT_CORAL),
                    "scene"
                ),
                sc
            )
        finally
            CPEngine.dispose()

        sys.exit(0)
