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
  *
  */
class MirMashProgram extends MirExecutable:
    private val sz = CPRand.between(3000.kb, 10000.kb)

    override def getSizeOnDisk: Long = sz
    override def mainEntry(ctx: MirExecutableContext): Int =
        initState(ctx)

        val con = ctx.con
        var exit = false
        var exitCode = 0
        val hist = mutable.ArrayBuffer.empty[String]
        val prompt = "$"

        while !exit do
            con.print(s"$prompt ")
            val line = con.readLine(maxLen = 512, hist = hist.toSeq)
            con.println(s"\nexecuting '$line'.")
            hist += line

        exitCode

    /**
      *
      * @param ctx
      */
    private def initState(ctx: MirExecutableContext): Unit =
        // Default & well-known Unix environment variables.
        // TODO: move some of it to '/etc/include.mash'.
        ctx.vars.put("PS", """\\u@\\h:\\w\\$""")
        ctx.vars.put("HOST", ctx.host)
        ctx.vars.put("PWD", ctx.workDir.getAbsolutePath)
        ctx.vars.put("HOME", ctx.usr.getHomeDirectory)
        ctx.vars.put("UID", ctx.usr.getUid.toString)
        ctx.vars.put("USER", ctx.usr.getUsername)
        ctx.vars.put("LANG", "en_US")
        ctx.vars.put("SHELL", ctx.file.getAbsolutePath)
        ctx.vars.put("MAIL", s"${ctx.usr.getHomeDirectory}/inbox")
