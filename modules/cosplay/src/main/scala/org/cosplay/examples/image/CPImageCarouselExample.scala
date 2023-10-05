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

package org.cosplay.examples.image

import org.cosplay.*
import CPArrayImage.prepSeq
import org.cosplay.CPColor.*
import org.cosplay.CPPixel.*
import org.cosplay.CPKeyboardKey.*
import org.cosplay.prefabs.images.*
import org.cosplay.prefabs.scenes.*
import org.cosplay.prefabs.shaders.*

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
  * Code example for image functionality.
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
  *     $ mvn -f modules/cosplay -P ex:image-carousel exec:java
  * }}}
  *
  * @see [[CPImage]]
  * @see [[CPArrayImage]]
  * @note See developer guide at [[https://cosplayengine.com]]
  */
object CPImageCarouselExample:
    // Images for the carousel.
    private val imgs = Seq(
        CPAardvarkImage.trimBg(),
        CPCubesImage.trimBg(),
        CPAlienImage.trimBg(),
        CPAmigaImage,
        CPAtari2080STImage,
        CPBearImage.trimBg(),
        CPBeetleImage,
        CPCastleImage.trimBg(),
        CPGlobeImage,
        CPHelicopterImage.trimBg(),
        CPAstronaut1Image.trimBg(),
        CPOceanLinerImage,
        CPPlaneImage.trimBg(),
        CPSaturnImage.trimBg(),
        CPSkullImage.trimBg(),
        CPTruckImage.trimBg(),
        CPBananaImage.trimBg(),
        CPBatImage.trimBg(),
        CPCrownImage.trimBg(),
        CPDolphinImage.trimBg(),
        CPGameBoyImage.trimBg(),
        CPGrimReaperImage.trimBg(),
        CPIceCreamImage.trimBg(),
        CPSunGlassesImage.trimBg(),
        CPSwordImage.trimBg(),
        CPFlamingoImage.trimBg(),
        CPCalculatorImage.trimBg(),
        CPCarImage.trimBg(),
        CPUmbrellaImage.trimBg(),
        CPSpaceShipImage.trimBg(),
        CPBedImage.trimBg(),
        CPMoon1Image.trimBg(),
        CPKnifeImage.trimBg(),
        CPTntImage.trimBg(),
        CPCactusImage.trimBg(),
        CPShieldImage.trimBg(),
        CPCloudImage,
        CPTornadoImage.trimBg(),
        CPGuitarImage,
        CPSpeckImage
    )
    private val BLUE_BLACK = CPColor("0x00000F")
    private val bgPx = ' '&&(BLUE_BLACK, BLUE_BLACK)

    class CarouselSprite(img: CPImage, viewDim: CPDim) extends CPSceneObject:
        private val centerY = (viewDim.h - img.h) / 2
        private val centerX = (viewDim.w - img.w) / 2
        private val leftOffScrX = -(img.w + 1)
        private val rightOffScrX = viewDim.w
        private val stepX = 1.0f
        private val fadeInShdr = new CPFadeInShader(false, 2000.ms, bgPx, autoStart = false)
        private val fadeOutShdr = new CPFadeOutShader(false, 1000.ms, bgPx, onFinish = _ => setVisible(false))
        private val shdrs = Seq(fadeInShdr, fadeOutShdr)

        private var x = leftOffScrX.toFloat
        private var step = 0f
        private var targetX = 0

        def isMoving: Boolean = step != 0
        private def move(newX: Int, newStep: Float, newTargetX: Int): Unit =
            x = newX.toFloat
            step = newStep
            targetX = newTargetX
        def placeInCenter(): Unit =
            move(centerX, 0f, centerX)
            setVisible(true)
            fadeInShdr.start()
        def fadeInFromLeft(): Unit =
            move(leftOffScrX, stepX, centerX)
            setVisible(true)
            fadeInShdr.start()
        def fadeInFromRight(): Unit =
            move(rightOffScrX, -stepX, centerX)
            setVisible(true)
            fadeInShdr.start()
        def fadeOutToLeft(): Unit =
            move(centerX, -stepX, leftOffScrX)
            fadeOutShdr.start()
        def fadeOutToRight(): Unit =
            move(centerX, stepX, rightOffScrX)
            fadeOutShdr.start()

        override def getShaders: Seq[CPShader] = shdrs
        override def getX: Int = x.round
        override val getY: Int = centerY
        override val getZ: Int = 0
        override def getDim: CPDim = img.getDim
        override def update(ctx: CPSceneObjectContext): Unit = if getX != targetX then x += step else step = 0
        override def render(ctx: CPSceneObjectContext): Unit = ctx.getCanvas.drawImage(img, getX, getY, getZ)

    /**
      * Entry point for JVM runtime.
      *
      * @param args Ignored.
      */
    def main(args: Array[String]): Unit =
        val maxImgW = imgs.maxBy(_.getDim.w).getWidth
        val maxImgH = imgs.maxBy(_.getDim.h).getHeight

        val ctrlImg = new CPArrayImage(
            prepSeq(
                """
                  |              LEFT            RIGHT
                  |          .----..----.    .----..----.
                  |          | <= || A  |    | D  || => |
                  |          `----'`----'    `----'`----'
                  |
                  |[Q] Quit   [CTRL+Q] FPS Overlay   [CTRL+L] Open Log
                """),
            (ch, _, _) => ch match
                case c if c.isLetter => c&C_STEEL_BLUE1
                case '|' | '.' | '`' | '-' | '\'' => ch&C_LIME
                case _ => ch.toUpper&C_DARK_ORANGE
        ).trimBg()

        val dim = CPDim(maxImgW + 8 * 2, maxImgH + ctrlImg.h + 4)
        val viewDim = CPDim(dim.w, maxImgH)
        var sprIdx = 0
        val sprs = imgs.map(img => new CarouselSprite(img, viewDim)).toIndexedSeq

        sprs.head.placeInCenter()

        // Off-screen sprite for keyboard control.
        val kbCtrl = new CPKeyboardSprite((ctx, key) => key match
            case KEY_LEFT | KEY_LO_A => // Scroll carousel left.
                val curSpr = sprs(sprIdx)
                if !curSpr.isMoving then
                    curSpr.fadeOutToLeft()
                    sprIdx = if sprIdx == 0 then sprs.size - 1 else sprIdx - 1
                    sprs(sprIdx).fadeInFromRight()
            case KEY_RIGHT | KEY_LO_D => // Scroll carousel right.
                val curSpr = sprs(sprIdx)
                if !curSpr.isMoving then
                    curSpr.fadeOutToRight()
                    sprIdx = if sprIdx == sprs.size - 1 then 0 else sprIdx + 1
                    sprs(sprIdx).fadeInFromLeft()
            case KEY_LO_Q => ctx.exitGame() // Exit the game on 'Q' press.
            case _ => ()
        )

        val sc = new CPScene("scene", dim.?, bgPx,
            (
                // Control sprites.
                Seq(
                    // Control image/label.
                    CPStaticImageSprite((dim.w - ctrlImg.w) / 2, dim.h - ctrlImg.h - 2, 0, ctrlImg),
                    kbCtrl,
                    // Just for the initial scene fade-in effect.
                    new CPOffScreenSprite(new CPFadeInShader(true, 1500.ms, bgPx)),
                )
                ++
                // Add carousel images sprites.
                sprs
            ):_*
        )

        // Initialize the engine.
        CPEngine.init(
            CPGameInfo(name = "Image Carousel Example", initDim = dim.?),
            System.console() == null || args.contains("emuterm")
        )

        // Start the example & wait for exit.
        try CPEngine.startGame(
            new CPFadeShimmerLogoScene(
                id = "logo",
                dim.?,
                bgPx,
                C_STEEL_BLUE1 :: C_LIME :: C_ORANGE1 :: Nil,
                nextSc = "scene"
            ),
            sc
        )
        finally CPEngine.dispose()

        sys.exit(0)

