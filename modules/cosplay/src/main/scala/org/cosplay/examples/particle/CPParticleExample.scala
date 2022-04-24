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

package org.cosplay.examples.particle

import org.cosplay.*
import CPArrayImage.*
import CPColor.*
import CPKeyboardKey.*
import CPStyledString.styleStr
import CPPixel.*
import org.cosplay.prefabs.scenes.CPFadeShimmerLogoScene
import org.cosplay.prefabs.shaders.CPFadeInShader

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
  * Code example for particle effect functionality.
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
  *     $ mvn -f modules/cosplay -P ex:particle exec:java
  * }}}
  * 
  * @see [[CPParticle]]
  * @see [[CPParticleEmitter]]
  * @see [[CPParticleSprite]]
  * @note See developer guide at [[https://cosplayengine.com]]
  */
object CPParticleExample:
    /**
      * Entry point for JVM runtime.
      *
      * @param args Ignored.
      */
    def main(args: Array[String]): Unit =
        val bgPx = '.'&&(C_GRAY2, C_GRAY1)
        val w = 100
        val h = 40
        val dim = CPDim(w, h)

        val bomb = CPArrayImage(
            prepSeq("""
              | )
              | (
              |.-`-.
              |:   :
              |:TNT:
              |:___:
            """),
            (ch, _, _) => ch&C_GREEN_YELLOW
        ).trimBg()

        val c1 = C_LIGHT_GREEN
        val c2 = C_ORANGE1

        val ctrlImg = new CPArrayImage(
            styleStr("[Space]", c1) ++ styleStr(" Kaboom!    ", c2) ++
            styleStr("[Q]", c1) ++ styleStr(" Quit    ", c2) ++
            styleStr("[Ctrl-L]", c1) ++ styleStr(" Log    ", c2) ++
            styleStr("[Ctrl-Q]", c1) ++ styleStr(" FPS Overlay", c2)
        ).trimBg()

        // Initialize the engine.
        CPEngine.init(
            CPGameInfo(name = "Particle Example", initDim = Option(dim)),
            System.console() == null || args.contains("emuterm")
        )

        val boomSnd = CPSound(src = "sounds/examples/boom.wav")
        val COLORS = CS_X11_REDS ++ CS_X11_ORANGES ++ CS_X11_CYANS
        val MAX_AGE = 15

        /**
          *
          * @param initX Initial X-coordinate.
          * @param initY Initial Y-coordinate.
          * @param dx Delta for X-axis.
          * @param dy Delta for Y-axis.
          */
        class KaboomParticle(initX: Int, initY: Int, dx: Float, dy: Float) extends CPParticle:
            // Defines the radius of explosion in terms of the particle age.
            private var x = initX.toFloat
            private var y = initY.toFloat
            // Linear color gradient, slowly dimming.
            private val cf = CPCurve.colorGradient(CPRand.rand(COLORS), C_GRAY1, MAX_AGE)
            // X-curve for slowing down the speed of particle as it moves away from the center.
            private val dxf = CPCurve.lagrangePoly(Seq(
                x -> 1f,
                x + (dx * MAX_AGE) / 4 -> 0.5f,
                x + dx * MAX_AGE -> 0.3f
            ))
            // Y-curve for slowing down the speed of particle as it moves away from the center.
            private val dyf = CPCurve.lagrangePoly(Seq(
                y -> 1f,
                y + (dy * MAX_AGE) / 4 -> 0.4f,
                y + dy * MAX_AGE -> 0.2f
            ))
            private var age = 0

            override def update(ctx: CPSceneObjectContext): Unit =
                age += 1
                // Curve X and Y-coordinates changes.
                x += dx * dxf(x)
                y += dy * dyf(y)
            override def getX: Int = x.round
            override def getY: Int = y.round
            override val getZ: Int = 1
            override def getPixel: CPPixel = CPRand.randSymbol()&cf()
            override def isAlive: Boolean = age < MAX_AGE

        val bw = bomb.getDim.w
        val bh = bomb.getDim.h

        val emitter = new CPParticleEmitter():
            // Number of particles this emitter will emit on each update.
            private final val GEN_SIZE = 20
            // Emit from the center of the 'bomb' sprite.
            private final val x = (w - bw) / 2 + bw / 2
            private final val y = (h - bh) / 2 + bh / 2
            private var age = 0

            override def reset(): Unit = age = 0
            override def emit(ctx: CPBaseContext): Iterable[CPParticle] =
                if !isPaused && age < MAX_AGE then
                    age += 1
                    // Emit particles in 360 degree semi-circle.
                    for (_ <- 0 to GEN_SIZE) yield KaboomParticle(x, y,
                        (CPRand.randFloat() - 0.5f) * 3.5f,
                        (CPRand.randFloat() - 0.5f) * 2f,
                    )
                else
                    Seq.empty

        val kaboomSpr = CPParticleSprite("kaboom", Seq(emitter))

        kaboomSpr.setOnStart(Option(_ => boomSnd.play()))
        kaboomSpr.setOnEnd(Option(_ => boomSnd.stop(1000)))

        val ctrlSpr = new CPOffScreenSprite():
            override def update(ctx: CPSceneObjectContext): Unit =
                if ctx.getKbEvent.isDefined && ctx.getKbEvent.get.key == KEY_SPACE then
                    kaboomSpr.resume(reset = true)

        // Pause the effect sprite initially.
        kaboomSpr.pause()

        val ctrlDim = ctrlImg.getDim
        val bombX = (w - bw) / 2
        val bombY = (h - bh) / 2

        val sc = new CPScene("scene", Option(dim), bgPx,
            kaboomSpr,
            ctrlSpr,
            new CPStaticImageSprite("bomb", bombX, bombY, 0, bomb),
            new CPStaticImageSprite((w - ctrlDim.w) / 2, h - 4, 0, ctrlImg), // Help label.
            // Just for the initial scene fade-in effect.
            new CPOffScreenSprite(new CPFadeInShader(true, 1500, bgPx)),
            // Exit the game on 'Q' press.
            CPKeyboardSprite(KEY_LO_Q, _.exitGame())
        )

        // Start the game & wait for exit.
        try CPEngine.startGame(new CPFadeShimmerLogoScene("logo", Option(dim), bgPx, COLORS, "scene"), sc)
        finally CPEngine.dispose()

        sys.exit(0)


