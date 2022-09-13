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

import org.cosplay.*
import scala.collection.*
import scala.collection.immutable
import scala.collection.mutable

import MirAsmInstruction.*

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
object MirAsmExecutable:
    /**
      *
      * @param errMsg
      */
    class AsmRuntimeException(errMsg: String) extends CPException(errMsg)

    /**
      *
      * @param errMsg
      * @param instr
      */
    private def error(errMsg: String)(using instr: MirAsmInstruction): AsmRuntimeException =
        new AsmRuntimeException(s"$errMsg - at line ${instr.line} in '${instr.getSourceCode}'.")

    /**
      *
      * @param instrs
      */
    def apply(instrs: Seq[MirAsmInstruction]): MirAsmExecutable =
        (state: MirAsmState) =>
            val stack = new MirAsmStack

            // Ensure instructions are sorted & indexed.
            val code = instrs.sortBy(_.line)
            // Create labels LUT.
            val lblLut = immutable.HashMap.from(
                code.zipWithIndex.filter((instr, _) => instr.label.isDefined).map((instr, idx) => instr.label.get -> idx)
            )

            for (instr <- instrs)
                given MirAsmInstruction = instr
                val name = instr.name
                val params = instr.params
                val paramsCnt = params.length

                def ensureParams(min: Int, max: Int): Unit =
                    if paramsCnt < min then throw error("Insufficient instruction parameters")
                    if paramsCnt > max then throw error("Too many instruction parameters")

                def getVar(id: String): Any =
                    state.getVar(id) match
                        case Some(v) => v
                        case None => throw error(s"Trying to access undefined variable: $id")

                name match
                    case "push" =>
                        ensureParams(1, 1)
                        params.head match
                            case NullParam => stack.push(null)
                            case StringParam(s) => stack.push(s)
                            case LongParam(d) => stack.push(d)
                            case DoubleParam(d) => stack.push(d)
                            case VarParam(id) => stack.push(getVar(id))
                    case _ => throw error(s"Unknown assembler instruction: ${instr.name}")

/**
  *
  */
trait MirAsmExecutable:
    /**
      *
      * @param state
      */
    def execute(state: MirAsmState): Unit
