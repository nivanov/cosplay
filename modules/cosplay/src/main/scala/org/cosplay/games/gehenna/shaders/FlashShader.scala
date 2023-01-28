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

package org.cosplay.games.gehenna.shaders

import org.cosplay.*
import games.gehenna.*

import scala.util.*
import java.io.*
import org.apache.commons.io.*

import java.nio.charset.Charset
import scala.io.Source
import scala.jdk.CollectionConverters.*
import de.sciss.audiofile.*

import scala.collection.mutable.ArrayBuffer
import collection.immutable.{HashSet, StringOps}

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


class FlashShader() extends CPShader:
    var lastBright = 0D

    private var run = false

    private type Fun = Long => Float
    private var fun: Fun = null
    private var dur = 0L
    private var lastRenderMs = 0L

    def start(): Unit = run = true
    def stop(): Unit = run = false

    def setFun(fun: Fun): Unit =
        this.fun = fun
        dur =  0L
        lastRenderMs = System.currentTimeMillis()

    override def render(ctx: CPSceneObjectContext, objRect: CPRect, inCamera: Boolean): Unit =
        if run then
            var brightness = 0f
            if fun(dur) != 0 then
                brightness = fun(dur)
            else
                brightness = (lastBright - 0.1).toFloat
                if brightness <= 0 then brightness = 0

            lastBright = brightness

            val now = System.currentTimeMillis()
            dur += now - lastRenderMs
            lastRenderMs = now

            val canv = ctx.getCanvas
            objRect.loop((x,y) => {
                if canv.isValid(x, y) then
                    val zpx = canv.getZPixel(x, y)
                    val px = zpx.px
                    if px.char != ' ' then canv.drawPixel(px.withLighterFg(brightness), x, y, zpx.z)
            })
