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

package org.cosplay.games.mir.os.progs.mash

import org.cosplay.*
import games.mir.*
import games.mir.os.*

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
  *
  */
class MirMashProgram extends MirExecutable:
    private val sz = CPRand.between(3000.kb, 10000.kb)

    override def getSizeOnDisk: Long = sz
    override def mainEntry(ctx: MirExecutableContext): Int =
        val state = initState(ctx)

        0

    /**
      *
      * @param ctx
      */
    private def initState(ctx: MirExecutableContext): MirMashState =
        val state = new MirMashState()

        // Default & well-known Unix environment variables.
        state.setVariable("PS", """\\u@\\h:\\w\\$""", false)
        state.setVariable("HOST", ctx.host, false)
        state.setVariable("PWD", ctx.workDir.getAbsolutePath, false)
        state.setVariable("HOME", ctx.usr.getHomeDirectory, false)
        state.setVariable("UID", ctx.usr.getUid.toString, false)
        state.setVariable("USER", ctx.usr.getUsername, false)
        state.setVariable("LANG", "en_US", false)
        state.setVariable("SHELL", ctx.file.getAbsolutePath, false)
        state.setVariable("MAIL", s"${ctx.usr.getHomeDirectory}/inbox", false)

        state
