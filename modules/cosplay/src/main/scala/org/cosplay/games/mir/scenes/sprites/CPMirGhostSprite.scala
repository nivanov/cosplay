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

package org.cosplay.games.mir.scenes.sprites

import org.cosplay.*
import prefabs.sprites.*
import games.mir.*
import CPArrayImage.*
import CPPixel.*

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
  *
  */
private object Astronaut1Image extends CPArrayImage(
    prepSeq("""
        |        _..._
        |      .'     '.      _
        |     /    .-""-\   _/ \
        |   .-|   /:.   |  |   |
        |   |  \  |:.   /.-'-./
        |   | .-'-;:__.'    =/
        |   .'=  *=|MIR  _.='
        |  /   _.  |    ;
        | ;-.-'|    \   |
        |/   | \    _\  _\
        |\__/'._;.  ==' ==\
        |         \    \   |
        |         /    /   /
        |         /-._/-._/
        |         \   `\  \
        |          `-._/._/
        """),
    (ch, _, _) => ch&FG
)

/**
  *
  */
private object Astronaut2Image extends CPArrayImage(
    prepSeq("""
        |           _--,.--.
        |         /   ./  /
        |         \_.-\_.-\
        |         \    \   \
        |         /    /   |
        |.--. .-;.  ==, ==/
        |\   | /    -/  -/
        | ;-.-,|    /   |
        |  \   -.  |    ;
        |   .,=  *=|MIR  `.=,
        |   | .-,-;:--.,    =\
        |   |  /  |:.   \.-,-.\
        |   .-|   \:.   |  |   |
        |     \    `-,,-/  '-\_/
        |      .       .
        |       ` -...'
        """),
    (ch, _, _) => ch&FG
)

/**
  * @param avoidTopLeft Whether or not to avoid top-left corner.
  */
class CPMirGhostSprite(avoidTopLeft: Boolean) extends CPOffScreenSprite:
    private final val CYCLE_MS = CPEngine.fps * 10
    private val flyBySnd = CPSound(s"$SND_HOME/fly_by2.wav", .3f)
    private val sosSnd = CPSound(s"$SND_HOME/sos.wav", .2f)
    private val imgs = Seq(
        Astronaut1Image, Astronaut1Image.horFlip(),
        Astronaut2Image, Astronaut2Image.horFlip()
    ).map(_.trimBg())
    override def update(ctx: CPSceneObjectContext): Unit =
        super.update(ctx)
        val frmCnt = ctx.getSceneFrameCount
        val exists = ctx.getObject("ghost").nonEmpty
        if frmCnt > 0 && frmCnt % CYCLE_MS == 0 && !exists then
            val canv = ctx.getCanvas
            val img = CPRand.rand(imgs)
            val (dy, y) = if CPRand.coinFlip() then (-.3f, canv.yMax + 1) else (.3f, -img.h - 1)
            var (dx, x) = (.3f * (if CPRand.coinFlip() then -1 else 1), (canv.w - img.w) / 2)
            if avoidTopLeft && dx < 0 && dy > 0 then dx = dx.abs
            ctx.addObject(new CPBubbleSprite(
                "ghost",
                img = img,
                initX = x,
                initY = y,
                z = 1,
                dxf = _ ⇒ dx,
                dyf = _ ⇒ dy,
                bgPx = BG_PX,
                durMs = 3500,
                autoDelete = true
            ))
            flyBySnd.play(1000)
            sosSnd.play(1000)
