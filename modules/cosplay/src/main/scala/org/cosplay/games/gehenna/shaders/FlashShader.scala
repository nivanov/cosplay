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

import scala.collection.mutable._
import scala.collection.mutable.ArrayBuffer
import collection.immutable.HashSet

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


class FlashShader extends CPShader:
    private var go = false
    private var wasGo = false
    private val radius = 10
    private var bgPx = GAME_BG_PX

    private var brightness = 0f
    private val brightChng = 0.05f

    private var mag:Seq[Float] = Seq(0f)
    private var msTest = ProjectGehennaTitle.magTestLength
    private var timeWhenStart = 0f
    private var goMs = 0f

    //var ctx = //TODO

    /**
     * Toggles this shader effect on and off.
     *
     * @see [[go()]]
     * @see [[stop()]]
     */
    def toggle(): Unit = go = !go

    /**
     * Starts the shader effect.
     *
     * @see [[toggle()]]
     */
    def start(ctx: CPSceneObjectContext): Unit =
        go = true
        timeWhenStart = ctx.getFrameMs

    /**
     * Stops the shader effect.
     *
     * @see [[toggle()]]
     */
    def stop(): Unit = go = false

    /**
     * Returns `true` if this shader effect is on, `false` otherwise.
     */
    def isActive: Boolean = go

    /** @inheritdoc */

//    def changeBPM(newBPM: Int): Unit =
//        bpm = newBPM
//        rate = ((60/bpm.toFloat) * 1000).round
//        //println(s"Rate is : $rate")

    def changeMag(newMag: Seq[Float]): Unit =
        mag = newMag

    override def render(ctx: CPSceneObjectContext, objRect: CPRect, inCamera: Boolean): Unit =
        goMs = ctx.getFrameMs
        if go && ctx.isVisible && goMs >= timeWhenStart + msTest then
            brightness = 1f

        if brightness != 0 && brightness > brightChng then
            brightness -= brightChng

        val canv = ctx.getCanvas
        objRect.loop((x,y) => {
            if canv.isValid(x, y) then
                val zpx = canv.getZPixel(x, y)
                val px = zpx.px
                if px.char != ' ' then canv.drawPixel(px.withLighterFg(brightness), x, y, zpx.z)
        })








//        if go && ctx.isVisible && (ctx.getFrameMs - rate) >= lastMs then
//            lastMs = ctx.getFrameMs
//            currFade = CPRand.between(0.5f, 1f)
//
//        if currFade != 0 && currFade > fadeChange then
//            currFade -= fadeChange
//
//        val canv = ctx.getCanvas
//        objRect.loop((x, y) => {
//            if canv.isValid(x, y) then
//                val zpx = canv.getZPixel(x, y)
//                val px = zpx.px
//                if px.char != ' ' then canv.drawPixel(px.withLighterFg(currFade), x, y, zpx.z)
//        })

