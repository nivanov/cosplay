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

import org.cosplay.*
import games.mir.os.*
import games.mir.os.fs.*

/**
  *
  * @param os
  * @param player
  * @param crew
  * @param props
  * @param bf
  * @param fg
  * @param crtEffect
  * @param elapsedSec
  */
@SerialVersionUID(1_0_0L)
case class CPMirState(
    os: CPMirOs,
    player: CPMirPlayer,
    crew: List[CPMirPlayer],
    props: Map[String, AnyRef],
    bf: CPColor,
    fg: CPColor,
    crtEffect: Boolean,
    elapsedSec: Long
) extends Serializable

/**
  *
  */
class CPMirGameManager:
    private var loaded = false
    private var loadFailed = false
    private val state =
        try
            loadLatest() match
                case Some(v) ⇒
                    loaded = true
                    v
                case None ⇒
                    loaded = false
                    init()
        catch
            case e: Exception ⇒
                loadFailed = true
                loaded = false
                init()

    /**
      *
      * @return
      */
    private def init(): CPMirState = ???

    /**
      *
      * @return
      */
    private def loadLatest(): Option[CPMirState] = ???

    /**
      *
      * @return
      */
    def isLatestLoaded: Boolean = loaded

    /**
      *
      * @return
      */
    def isLatestLoadFailed: Boolean = loadFailed

    /**
      *
      */
    def snapshot(): Unit = ???


