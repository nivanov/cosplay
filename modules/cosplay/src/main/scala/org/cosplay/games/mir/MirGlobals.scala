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

package org.cosplay.games.mir

import org.cosplay.*
import CPColor.*
import CPPixel.*

import scala.annotation.targetName


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

val GAME_NAME = "Escape From Mir"
val GAME_VER = "0.0.1"

val EVENT_YEAR = 1997
val NPC_CNT = 2
val stateMgr = MirStateManager()
val BG = stateMgr.state.bg
val FG = stateMgr.state.fg
val FG_LITE = FG.lighter(.4f)
val FG_DARK = FG.darker(.4f)
val BG_PX = ' '&&(BG, BG)
val SND_HOME = "mir/sounds"
val IMG_HOME = "mir/images"
val markup = CPMarkup(
    FG,
    BG.?,
    Seq(
        CPMarkupElement("<%", "%>", _&&(FG_LITE, BG)), // Light.
        CPMarkupElement("<~", "~>", _&&(FG_DARK, BG)), // Dark.
        CPMarkupElement("<@", "@>", _&&(BG, FG)) // Reverse.
    )
)

extension[T](ref: T)
    @targetName("asAnOption")
    def `?`: Option[T] = Option(ref)

extension[R, T](opt: Option[T])
    def or(f: T => R, dflt: => R): R = opt match
        case Some(t) => f(t)
        case None => dflt

extension(d: Int)
    // To bytes...
    def kb: Long = d * 1024
    def mb: Long = d * 1024 * 1024
    def gb: Long = d * 1024 * 1024 * 1024

    // To milliseconds...
    def secs: Long = d * 1000
    def mins: Long = d * 1000 * 60
    def hours: Long = d * 1000 * 60 * 60
    def days: Long = d * 1000 * 60 * 60 * 24
    def weeks: Long = d * 1000 * 60 * 60 * 24 * 7



