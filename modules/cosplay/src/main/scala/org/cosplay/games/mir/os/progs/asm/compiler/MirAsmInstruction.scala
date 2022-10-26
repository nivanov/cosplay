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

package org.cosplay.games.mir.os.progs.asm.compiler

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
object MirAsmInstruction:
    trait Param
    object NullParam extends Param { override def toString: String = "null" }
    case class IdParam(id: String) extends Param { override def toString: String = id }
    case class StringParam(s: String) extends Param { override def toString: String = s"\"$s\"" }
    case class LongParam(d: Long) extends Param { override def toString: String = d.toString }
    case class DoubleParam(d: Double) extends Param { override def toString: String = d.toString }

/*
Less boilerplate syntax...
==========================

object MirAsmInstruction:
    trait Param
    object NullParam -> Param { ^def toString: String = "null" }
    #class IdParam(id: String) -> Param { ^def toString: String = id }
    #class StringParam(s: String) -> Param { ^def toString: String = s"\"$s\"" }
    #class LongParam(d: Long) -> Param { ^def toString: String = d.toString }
    #class DoubleParam(d: Double) -> Param { ^def toString: String = d.toString }

*/

import MirAsmInstruction.*

/**
  *
  * @param line Original source line name.
  * @param pos In-line character position.
  * @param origin Name of the original source (i.e. file name).
  */
case class MirAsmDebug(line: Int, pos: Int, origin: String)

/**
  *
  * @param label Optional label for this instruction.
  * @param line Line number in the original source code.
  * @param name Instruction name.
  * @param params Instruction parameter list in the same order they appear in the source code.
  * @param dbg Optional debug information for this instruction.
  */
case class MirAsmInstruction(
    label: Option[String],
    line: Int,
    name: String,
    params: Seq[Param],
    dbg: Option[MirAsmDebug]
):
    /**
      * @param useDbg Whether or not to include debug information.
      */
    def getSourceCode(useDbg: Boolean): String =
        val lbl = label match
            case Some(s) => s"$s: "
            case None => ""
        val dbgStr =
            if useDbg && dbg.isDefined then
                val d = dbg.get
                s" @${d.line},${d.pos},\"${d.origin}\""
            else
                ""
        s"$lbl$name ${params.mkString(", ")}$dbgStr".trim

