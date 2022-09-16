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
      * Special type for assembler runtime exceptions.
      *
      * @param errMsg Error message.
      */
    class AsmRuntimeException(errMsg: String) extends CPException(errMsg)

    /**
      *
      * @param idx
      */
    def nth(idx: Int): String =
        require(idx >= 0)
        idx match
            case 0 => "1st"
            case 1 => "2nd"
            case 2 => "3rd"
            case i => s"${i + 1}th"

    /**
      *
      * @param instrs Sequence of assembler instructions to build an executable from.
      */
    def apply(instrs: Seq[MirAsmInstruction]): MirAsmExecutable =
        (ctx: MirAsmContext) =>
            val stack = new MirAsmStack

            // Ensure instructions are sorted & indexed.
            val code = instrs.sortBy(_.line).zipWithIndex
            // Create labels LUT.
            val lblLut = immutable.HashMap.from(
                code.filter((instr, _) => instr.label.isDefined).map((instr, idx) => instr.label.get -> idx)
            )

            for ((instr, idx) <- code)
                val name = instr.name
                val params = instr.params
                val paramsCnt = params.length

                type ARE = AsmRuntimeException

                def error(errMsg: String): ARE = new ARE(s"$errMsg - at line ${instr.line} in '${instr.getSourceCode}'.")
                def wrongStack(act: Any, exp: String): ARE = error(s"Unexpected stack value ($act) - expecting $exp")
                def wrongParam(idx: Int, exp: String): ARE = error(s"Invalid ${nth(idx)} parameter - expecting $exp")
                def wrongVar(id: String, exp: String): ARE = error(s"Invalid variable '$id' type - expecting $exp")

                def ensure(min: Int, max: Int): Unit =
                    if paramsCnt < min then throw error("Insufficient instruction parameters")
                    if paramsCnt > max then throw error("Too many instruction parameters")

                def getVar(id: String): Any =
                    ctx.getVar(id) match
                        case Some(v) => v
                        case None => throw error(s"Undefined variable: $id")

                def pop(): Any =
                    try stack.pop()
                    catch case _: NoSuchElementException => throw error(s"Stack is empty")

                def popStr(): String =
                    pop() match
                        case s: String => s
                        case x => throw wrongStack(x, "string")

                def popLong(): Long =
                    pop() match
                        case d: Long => d
                        case x => throw wrongStack(x, "integer")

                def varParam(idx: Int): String =
                    params(idx) match
                        case VarParam(id) => id
                        case _ => throw wrongParam(idx, "variable")

                def strParam(idx: Int): String =
                    params(idx) match
                        case StringParam(s) => s
                        case _ => throw wrongParam(idx, "string")

                def anyParam(idx: Int): Any =
                    params(idx) match
                        case NullParam => null
                        case StringParam(s) => s
                        case LongParam(d) => d
                        case DoubleParam(d) => d
                        case VarParam(id) => getVar(id)

                def addSub(mul: Int): Unit =
                    require(mul == 1 || mul == -1)
                    val v1 = pop()
                    val v2 = pop()
                    v1 match
                        case d1: Long => v2 match
                            case d2: Long => stack.push(d2 + d1 * mul)
                            case d2: Double => stack.push(d2 + d1 * mul)
                            case _ => throw wrongStack(d1, "numeric")
                        case d1: Double => v2 match
                            case d2: Long => stack.push(d2 + d1 * mul)
                            case d2: Double => stack.push(d2 + d1 * mul)
                            case _ => throw wrongStack(d1, "numeric")
                        case _ => throw wrongStack(v1, "numeric")

                object NativeFunctions:
                    def print(): Unit = ctx.getExecContext.out.print(pop().toString)
                    def concat(): Unit =
                        val s1 = popStr()
                        val s2 = popStr()
                        stack.push(s"$s2$s1") // Note reverse order...

                name match
                    case "push" => ensure(1, 1); stack.push(anyParam(0))
                    case "pop" => ensure(0, 1); if params.isEmpty then pop() else ctx.setVar(varParam(0), pop())
                    case "add" => ensure(0, 0); addSub(1)
                    case "sub" => ensure(0, 0); addSub(-1)
                    case "calln" =>
                        ensure(1, 1)
                        val fn = strParam(0)
                        fn match
                            case "print" => NativeFunctions.print()
                            case "concat" => NativeFunctions.concat()
                            case "_print" => println(pop().toString) // Debug only.
                            case s => throw error(s"Unknown native function: $s")
                    case "let" => ensure(2, 2); ctx.setVar(varParam(0), anyParam(1))
                    case "dup" => ensure(0, 0); if stack.nonEmpty then stack.push(stack.head)
                    case "jmp" => ()
                    case "eq" => ensure(0, 0); stack.push(if pop() == pop() then 1L else 0L)
                    case "neq" => ensure(0, 0); stack.push(if pop() == pop() then 0L else 1L)
                    case "brk" => ensure(0, 1); throw error(if paramsCnt == 1 then strParam(0) else "Aborted")
                    case "cbrk" => ensure(0, 1); if popLong() == 0 then throw error(if paramsCnt == 1 then strParam(0) else "Aborted")
                    case "cjmp" => ()
                    case "call" => ()
                    case "ret" => ()
                    case "exit" => ()
                    case _ => throw error(s"Unknown assembler instruction: ${instr.name}")

/**
  *
  */
trait MirAsmExecutable:
    /**
      *
      * @param ctx
      */
    def execute(ctx: MirAsmContext): Unit
