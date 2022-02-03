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

package org.cosplay.examples.shader

import org.cosplay.*
import CPPixel.*
import CPArrayImage.prepSeq
import CPColor.*
import CPKeyboardKey.*
import CPStyledString.styleStr
import org.cosplay.prefabs.scenes.*
import org.cosplay.prefabs.shaders.*

/*
   _________            ______________
   __  ____/_______________  __ \__  /_____ _____  __
   _  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
   / /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
   \____/  \____//____/ /_/     /_/  \__,_/ _\__, /
                                            /____/

          2D ASCII GAME ENGINE FOR SCALA3
            (C) 2021 Rowan Games, Inc.
               ALl rights reserved.
*/

/**
  * Code example for shaders functionality.
  *
  * @see [[CPShader]]
  * @see [[CPFadeInShader]]
  * @see [[CPFadeOutShader]]
  */
object CPShaderExample:
    /**
      * Entry point for JVM runtime.
      *
      * @param args Ignored.
      */
    def main(args: Array[String]): Unit =
        val bgPx = CPPixel('.', C_GRAY2, C_GRAY1)
        val dim = CPDim(80, 40)

        // In-code image creation & "painting".
        val bulbImg = CPArrayImage(
            prepSeq("""
              |  ___
              | /   \
              |{ -+~ }
              | \   /
              |  ]#[
              |  ]#[
            """),
            (ch, _, _) => ch match
                // Static skinning.
                case '-' | '~' | '+' => ch&C_ORANGE1
                case '#' | '[' | ']' => ch&C_GRAY6
                case '_' => ch&C_SKY_BLUE1
                case _ => ch&C_WHITE
        ).trimBg()

        val c1 = C_LIGHT_GREEN
        val c2 = C_ORANGE1

        val ctrlImg = CPArrayImage(
            prepSeq(
                """
                  |                    UP
                  |               .----..----.
                  |               | /\ || W  |
                  |    LEFT       `----'`----'         RIGHT
                  |.----..----.                    .----..----.
                  || <= || A  |                    | D  || => |
                  |`----'`----'       DOWN         `----'`----'
                  |               .----..----.
                  |               | \/ || S  |
                  |               `----'`----'
                  |
                  |            [Q]      Quit
                  |            [Ctrl+Q] FPS Overlay
                  |            [Ctrl+L] Open Log
                  |            [Space]  Toggle Light
                """),
            (ch, _, _) => ch match
                case c if c.isLetter => c&C_STEEL_BLUE1
                case '|' | '.' | '`' | '-' | '\'' => ch&C_LIME
                case _ => ch.toUpper&C_DARK_ORANGE
        ).trimBg()

        object FlashLightShader extends CPShader:
            private final val RADIUS = 8
            private var on = false

            def toggle(): Unit = on = !on
            override def render(ctx: CPSceneObjectContext, objRect: CPRect, inCamera: Boolean): Unit =
                if on then
                    val canv = ctx.getCanvas
                    val cx = objRect.centerX
                    val cy = objRect.centerY
                    val effRect = CPRect(cx - RADIUS * 2, cy - RADIUS, RADIUS * 4, RADIUS * 2)
                    effRect.loop((x, y) => {
                        if canv.isValid(x, y) then
                            // Account for character with/height ratio to make a proper circle...
                            val dx = (cx - x).abs.toFloat / 1.85
                            val dy = (cy - y).abs.toFloat
                            val r = Math.sqrt(dx * dx + dy * dy).toFloat
                            if r <= RADIUS then // Flashlight is a circular effect.
                                val zpx = canv.getZPixel(x, y)
                                val px = zpx.px
                                val newFg = px.fg.lighter(0.8f * (1.0f - r / RADIUS))
                                canv.drawPixel(px.withFg(newFg), x, y, zpx.z)
                    })

        val bulbSpr = new CPImageSprite("bulb",
            // Center it on the screen.
            (dim.width - bulbImg.getWidth) / 2,
            (dim.height - bulbImg.getHeight) / 2 - 5, 1, bulbImg, false, Seq(FlashLightShader)):
            private var x = initX.toFloat
            private var y = initY.toFloat

            override def getX: Int = x.round
            override def getY: Int = y.round
            // NOTE: we don't need to override 'render(...)' method - it stays default.
            override def update(ctx: CPSceneObjectContext): Unit =
                super.update(ctx)
                ctx.getKbEvent match
                    case Some(evt) => evt.key match
                        // NOTE: if keyboard event is repeated (same key pressed in consecutive frames) -
                        // we use smaller movement amount to smooth out the movement. If key press
                        // is "new" we move the entire character position to avoid an initial "dead keystroke" feel.
                        case KEY_LO_A | KEY_LEFT => x -= (if evt.isRepeated then 0.6f else 1.0f)
                        case KEY_LO_D | KEY_RIGHT => x += (if evt.isRepeated then 0.6f else 1.0f)
                        case KEY_LO_W | KEY_UP => y -= (if evt.isRepeated then 0.4f else 1.0f)
                        case KEY_LO_S | KEY_DOWN => y += (if evt.isRepeated then 0.4f else 1.0f)
                        case KEY_SPACE => FlashLightShader.toggle()
                        case _ => ()
                    case None => ()

        val sc = new CPScene("scene", Option(dim), bgPx,
            bulbSpr,
            CPStaticImageSprite((dim.width - ctrlImg.getWidth) / 2, 24, 0, ctrlImg),
            // Just for the initial scene fade-in effect.
            new CPOffScreenSprite(new CPFadeInShader(true, 1500, bgPx)),
            // Exit the game on 'q' press.
            CPKeyboardSprite(KEY_LO_Q, _.exitGame()) // ¯\_(ツ)_/¯
        )

        // Initialize the engine.
        CPEngine.init(
            CPGameInfo(name = "Shaders Example", initDim = Option(dim)),
            System.console() == null || args.contains("emuterm")
        )

        // Start the game & wait for exit.
        try CPEngine.startGame(new CPLogoScene("logo", Option(dim), bgPx, List(C_STEEL_BLUE1, C_LIME, C_ORANGE1), "scene"), sc)
        finally CPEngine.dispose()

        sys.exit(0)


