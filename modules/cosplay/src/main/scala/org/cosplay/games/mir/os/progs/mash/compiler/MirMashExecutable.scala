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

package org.cosplay.games.mir.os.progs.mash.compiler

import org.cosplay.games.mir.MirUtils
import org.cosplay.games.mir.os.MirExecutableContext
import org.cosplay.games.mir.os.progs.mash.*

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

import org.cosplay.games.mir.os.progs.asm.compiler.*

/**
  *
  */
object MirMashExecutable:
    /**
      *
      * @param asmCode Assembler code.
      * @param origin Origin of source code.
      */
    def apply(asmCode: String, origin: String): MirMashExecutable =
        (ctx: MirExecutableContext) =>
            try
                (new MirAsmCompiler)
                    .compile(asmCode, origin)
                    .execute(MirAsmContext(ctx))
            catch
                case e: MirAsmException =>
                    val synopsis = e.getSynopsis.trim
                    e.getDebug match
                        case Some(dbg) => throw new MirMashException(s"${MirUtils.removeDot(synopsis)} - at line ${dbg.line} in ${dbg.origin}.")
                        case None => throw new MirMashException(MirUtils.addDot(synopsis))

/**
  *
  */
trait MirMashExecutable:
    /**
      *
      * @param ctx
      */
    def execute(ctx: MirExecutableContext): Unit
