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

package org.cosplay

import scala.language.implicitConversions
import org.cosplay.CPColor.*
import org.cosplay.CPKeyboardKey.*

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
  *
  */
object CPArtPolylineApp:
    /**
      *
      */
    def main(args: Array[String]): Unit =
        val dim = (100, 40)
        val bgPx = CPPixel(' ', C_GRAY2, C_GRAY1)

        CPEngine.init(
            CPGameInfo(name = s"Art Polyline Test", initDim = dim.?),
            emuTerm = System.console() == null || args.contains("emuterm")
        )

        try
            val spr = new CPCanvasSprite("spr"):
                override def render(ctx: CPSceneObjectContext): Unit =
                    super.render(ctx)

                    val pts = Seq(
                        1 -> 2,
                        6 -> 10,
                        21 -> 15,
                        36 -> 20,
                        21 -> 25,
                        36 -> 10,
                        31 -> 35,
                        4 -> 31,
                        1 -> 2
                    )
                    ctx.getCanvas.drawArtPolyline(pts, 0, _.px.withFg(C_WHITE), CPCanvas.ArtLineStyle.ART_SMOOTH)

            CPEngine.startGame(new CPScene("scene", dim.?, bgPx,
                spr,
                CPKeyboardSprite(KEY_LO_Q, _.exitGame()) // Exit the game on 'Q' press.
            ))
        finally
            CPEngine.dispose()


