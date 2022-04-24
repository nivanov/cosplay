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

package org.cosplay.examples.camera

import org.cosplay.CPArrayImage.prepSeq
import org.cosplay.CPCanvas.ArtLineStyle.ART_SMOOTH
import org.cosplay.CPColor.*
import org.cosplay.CPKeyboardKey.*
import org.cosplay.CPPixel.*
import org.cosplay.prefabs.scenes.CPFadeShimmerLogoScene
import org.cosplay.prefabs.shaders.*
import org.cosplay.*

import scala.collection.mutable

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
  * Code example for camera management.
  *
  * ### Running Example
  * One-time Git clone & build:
  * {{{
  *     $ git clone https://github.com/nivanov/cosplay.git
  *     $ cd cosplay
  *     $ mvn package
  * }}}
  * to run example:
  * {{{
  *     $ mvn -f modules/cosplay -P ex:camera exec:java
  * }}}
  * 
  * @note See developer guide at [[https://cosplayengine.com]] 
  */
object CPCameraExample:
    /**
      * Entry point for JVM runtime.
      *
      * @param args Ignored.
      */
    def main(args: Array[String]): Unit =
        val bgPx = ' '&&(C_BLACK, C_GRAY1)
        val bgW = 200
        val bgH = 40
        val dim = CPDim(bgW, bgH)
        val bgCanv = CPCanvas(CPDim(bgW, bgH), bgPx)

        // +===================>--START--<======================+
        // | Procedural generation of the terrain & background. |
        // +====================================================+

        // Paint the starry sky.
        val starClrs = CS_X11_CYANS ++ CS_X11_ORANGES ++ CS_X11_REDS ++ CS_X11_WHITES
        bgCanv.fillRect(bgCanv.rect, -1, (x, y) => {
            if y < 7 && CPRand.randFloat() < 0.02 then
                val ch = if CPRand.randFloat() < 0.5 then '+' else '*'
                ch&CPRand.rand(starClrs)
            else
                XRAY
        })

        // Mountains polyline.
        val mntPts = mutable.ArrayBuffer.empty[(Int, Int)]
        mntPts += 0 -> 39 // Initial point.
        var x = 0
        var i = 0
        val lastX = bgW - 20
        while x < lastX do
            x += CPRand.randInt(10, 20)
            val y = if i % 2 == 0 then CPRand.randInt(5, 20) else CPRand.randInt(25, 38)
            mntPts += x -> y
            i += 1
        mntPts += bgW - 1 -> 39
        mntPts += 0 -> 39 // Close in the polyline.

        // Paint the mountains with white peaks.
        bgCanv.drawArtPolyline(
            mntPts.toSeq,
            0,
            // Use 'tag=1' to mark mountains outline for filling later.
            ppx => ppx.px.withFg(C_GRAY1.lighter(1 - ppx.y.toFloat / 40)).withTag(1),
            ART_SMOOTH
        )
        val blank = ' '&C_BLACK
        // NOTE: we are using 'tag=1' to detect the outline of the mountains.
        bgCanv.fill(11, 37, {
            _.px.tag == 1
        }, (x, y) =>
            if y < 5 then 'X'&C_WHITE
            else if y < 20 then 'X'&C_GRAY1.lighter(1 - (y - 5).toFloat / 15)
            else blank
        )

        val fiShdr = new CPFadeInShader(true, 500, bgPx)
        val foShdr = new CPFadeOutShader(true, 300, bgPx, _.exitGame())

        val bgSpr = new CPImageSprite("bg", 0, 0, 0, bgCanv.capture(), false, Seq(fiShdr, foShdr)):
            private var lastCamFrame: CPRect = _
            private var xf = initX.toFloat

            override def getX: Int = xf.round
            override def render(ctx: CPSceneObjectContext): Unit =
                val camFrame = ctx.getCameraFrame
                // Produce parallax effect for background sprite.
                if lastCamFrame != null && lastCamFrame.w == camFrame.w then
                    xf -= (lastCamFrame.x - camFrame.x).sign * 0.7f
                lastCamFrame = ctx.getCameraFrame
                super.render(ctx)

        // Paint the brick-like ground.
        val brickImg = CPArrayImage(
            // 5x3
            prepSeq(
                """
                  |^^"^^
                  |___[_
                  |_[___
                """
            ),
            (ch, _, _) => ch match
                case '^' => '^'&&(C_DARK_GREEN, C_GREEN_YELLOW)
                case '"' => '@'&&(C_GRAY3, C_GREEN_YELLOW)
                case '{' => '['&&(C_SANDY_BROWN, C_DARK_ORANGE3)
                case '-' => '_'&&(C_DARK_ORANGE3, C_DARK_ORANGE3)
                case c => c&&(C_MAROON, C_DARK_ORANGE3)
        )
        
        val brickCanv = CPCanvas(CPDim(bgW, 3), bgPx)
        for (i <- 0 until bgW / brickImg.getWidth) brickCanv.drawImage(brickImg, i * 5, 0, 2)
        val brickY = bgH - brickImg.getHeight
        val brickSpr = new CPStaticImageSprite("bricks", 0, brickY, 2, brickCanv.capture())

        // Paint palm trees.
        val palmImg = CPArrayImage(
            prepSeq(
                """
                  |    __ _.--..--._ _
                  | .-' _/   _/\_   \_'-.
                  ||__ /   _/x@@z\_   \__|
                  |   |___/\_x@@z  \___|
                  |          x@@z
                  |          x@@z
                  |           x@@z
                  |            x@@z
                  |             x@@z
                """),
            (ch, _, _) => ch match
                case 'x' => '\\'&C_ORANGE1
                case 'z' => '/'&C_ORANGE1
                case '@' => '_'&C_ORANGE1
                case c => c&C_GREEN1
        ).trimBg()
        val palmY = bgH - brickImg.getHeight - palmImg.getHeight
        val palmSeq = for (i <- 0 until 6) yield
            new CPStaticImageSprite(s"palm$i", CPRand.randInt(10, bgW - 10), palmY, 3, palmImg)

        // +===================>--END--<========================+
        // | Procedural generation of the terrain & background. |
        // +====================================================+

        val ufoImg = CPArrayImage(
            prepSeq(
                """
                  |    .-""`""-.
                  | _/`oOoOoOoOo`\_
                  |'.-=-=-=-=-=-=-.'
                  |  `-=.=-.-=.=-'
                  |     ^  ^  ^
                """
            ),
            (ch, _, _) => ch match
                case c@('o' | 'O') => c&C_SKY_BLUE1
                case '^' => '^'&C_ORANGE1
                case '=' => '='&C_INDIAN_RED
                case c => c&C_CYAN1
        ).trimBg()

        val ufoSpr = new CPImageSprite("ufo", 0, bgH - brickImg.getHeight - ufoImg.getHeight, 4, ufoImg):
            private var x = initX.toFloat

            override def getX: Int = x.round
            override def update(ctx: CPSceneObjectContext): Unit =
                // NOTE: we don't need to override 'render(...)' method - it just stays default.
                super.update(ctx)
                ctx.getKbEvent match
                    case Some(evt) => evt.key match
                        // NOTE: if keyboard event is repeated (same key pressed in consecutive frames) -
                        // we use smaller movement amount to smooth out the movement. If key press
                        // is "new" we move the entire character position to avoid an initial "dead keystroke" feel.
                        case KEY_LO_A | KEY_LEFT => x -= (if evt.isRepeated then 0.8f else 1.0f)
                        case KEY_LO_D | KEY_RIGHT => x += (if evt.isRepeated then 0.8f else 1.0f)
                        case _ => ()
                    case None => ()

        val initDim = CPDim(bgW / 2, bgH)

        var objs = List(
            bgSpr,
            brickSpr,
            ufoSpr,
            // On 'Ctrl-q' kick in fade out shader that will exit the game once it is finished.
            CPKeyboardSprite(KEY_LO_Q, _ => foShdr.start()),
        )
        objs ++= palmSeq

        // Create the scene.
        val sc = new CPScene("scene", Option(dim), bgPx, objs)
        val cam = sc.getCamera

        cam.setFocusTrackId(Option("ufo"))
        cam.setFocusFrameInsets(new CPInsets(10, 0))

        // Initialize the engine.
        CPEngine.init(
            CPGameInfo(name = "Camera Example", initDim = Option(initDim)),
            System.console() == null || args.contains("emuterm")
        )

        try
            // Start the game & wait for exit.
            CPEngine.startGame(
                new CPFadeShimmerLogoScene(
                    id = "logo",
                    Option(initDim),
                    bgPx,
                    Seq(C_ORANGE1, C_SKY_BLUE1, C_CYAN1, C_DARK_ORANGE3, C_GREEN_YELLOW),
                    nextSc = "scene"
                ),
                sc
            )
        finally CPEngine.dispose()

        sys.exit(0)
