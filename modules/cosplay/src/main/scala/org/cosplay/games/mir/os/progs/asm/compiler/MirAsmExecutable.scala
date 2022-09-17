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
            val code = instrs.sortBy(_.line)
            // Create labels LUT.
            val lblLut: immutable.HashMap[String, Int] = immutable.HashMap.from(
                code.zipWithIndex.filter((instr, _) => instr.label.isDefined).map((instr, idx) => instr.label.get -> idx)
            )

            var idx = 0
            val len = code.length
            while (idx < len)
                val instr = code(idx)
                val name = instr.name
                val params = instr.params
                val paramsCnt = params.length

                type ARE = AsmRuntimeException

                def error(errMsg: String): ARE = new ARE(s"$errMsg - at line ${instr.line} in '${instr.getSourceCode}'.")
                def wrongStack(act: Any, exp: String): ARE = error(s"Unexpected stack value ($act) - expecting $exp")
                def wrongParam(idx: Int, exp: String): ARE = error(s"Invalid ${nth(idx)} parameter - expecting $exp")
                def wrongVar(id: String, exp: String): ARE = error(s"Invalid variable '$id' type - expecting $exp")
                def wrongLabel(id: String): ARE = error(s"Undefined label in jump: $id")

                def checkParamCount(min: Int, max: Int): Unit =
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
                        case IdParam(id) => id
                        case _ => throw wrongParam(idx, "variable")

                def labelParam(idx: Int): String =
                    params(idx) match
                        case IdParam(id) => id
                        case _ => throw wrongParam(idx, "label")

                def strParam(idx: Int): String =
                    params(idx) match
                        case StringParam(s) => s
                        case _ => throw wrongParam(idx, "string")

                def longParam(idx: Int): Long =
                    params(idx) match
                        case LongParam(d) => d
                        case _ => throw wrongParam(idx, "integer")

                def anyParam(idx: Int): Any =
                    params(idx) match
                        case NullParam => null
                        case StringParam(s) => s
                        case LongParam(d) => d
                        case DoubleParam(d) => d
                        case IdParam(id) => getVar(id) // Returning variable's value (NOT its ID).

                def addSubVar(mul: Int): Unit =
                    require(mul == 1 || mul == -1)
                    val id = varParam(0) // 1st parameter (always variable).
                    val v = anyParam(1) // 2d parameter (anything, including variable).
                    getVar(id) match
                        case d1: Long => v match
                            case d2: Long => ctx.setVar(id, d2 + d1 * mul)
                            case d2: Double => ctx.setVar(id, d2 + d1 * mul)
                            case _ => throw wrongParam(1, "numeric")
                        case d1: Double => v match
                            case d2: Long => ctx.setVar(id, d2 + d1 * mul)
                            case d2: Double => ctx.setVar(id, d2 + d1 * mul)
                            case _ => throw wrongParam(1, "numeric")
                        case _ => throw wrongVar(id, "numeric")

                def incDecVar(v: Int): Unit =
                    require(v == 1 || v == -1)
                    val id = varParam(0) // 1st parameter (always variable).
                    getVar(id) match
                        case d: Long => ctx.setVar(id, d + v)
                        case _ => throw wrongVar(id, "integer")

                def incDec(v: Int): Unit =
                    require(v == 1 || v == -1)
                    pop() match
                        case d: Long => stack.push(d + v)
                        case x => throw wrongStack(x, "integer")

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
                        val s1 = pop().toString
                        val s2 = pop().toString
                        stack.push(s"$s2$s1") // Note reverse order...

                // Go to the "natural" next instruction... or jump.
                var nextInstr = true

                def jump(lbl: String): Unit =
                    lblLut.get(lbl) match
                        case Some(i) =>
                            idx = i
                            nextInstr = false
                        case None => throw wrongLabel(lbl)

                name match
                    case "push" => checkParamCount(1, 1); stack.push(anyParam(0))
                    case "pop" => checkParamCount(0, 1); if params.isEmpty then pop() else ctx.setVar(varParam(0), pop())
                    case "add" => checkParamCount(0, 0); addSub(1)
                    case "inc" => checkParamCount(0, 0); incDec(1)
                    case "dec" => checkParamCount(0, 0); incDec(-1)
                    case "incv" => checkParamCount(1, 1); incDecVar(1)
                    case "decv" => checkParamCount(1, 1); incDecVar(-1)
                    case "sub" => checkParamCount(0, 0); addSub(-1)
                    case "addv" => checkParamCount(2, 2); addSubVar(1)
                    case "subv" => checkParamCount(2, 2); addSubVar(-1)
                    case "calln" =>
                        checkParamCount(1, 1)
                        val fn = strParam(0)
                        fn match
                            case "print" => NativeFunctions.print()
                            case "concat" => NativeFunctions.concat()
                            case "_print" => println(pop().toString) // Debug only.
                            case s => throw error(s"Unknown native function: $s")
                    case "let" => checkParamCount(2, 2); ctx.setVar(varParam(0), anyParam(1))
                    case "dup" => checkParamCount(0, 0); if stack.nonEmpty then stack.push(stack.head)
                    case "eq" => checkParamCount(0, 0); stack.push(if pop() == pop() then 1L else 0L)
                    case "neq" => checkParamCount(0, 0); stack.push(if pop() == pop() then 0L else 1L)
                    case "eqv" => checkParamCount(2, 2); stack.push(if getVar(varParam(0)) == anyParam(1) then 1L else 0L)
                    case "neqv" => checkParamCount(2, 2); stack.push(if getVar(varParam(0)) == anyParam(1) then 0L else 1L)
                    case "brk" => checkParamCount(0, 1); throw error(if paramsCnt == 1 then strParam(0) else "Aborted")
                    case "cbrk" => checkParamCount(0, 1); if popLong() == 0L then throw error(if paramsCnt == 1 then strParam(0) else "Aborted")
                    case "cbrkv" => checkParamCount(1, 2); if getVar(varParam(0)) == 0L then throw error(if paramsCnt == 2 then strParam(1) else "Aborted")
                    case "cjmpv" => checkParamCount(2, 2); if getVar(varParam(0)) != 0L then jump(labelParam(1))
                    case "jmp" => checkParamCount(1, 1); jump(labelParam(0))
                    case "cjmp" => checkParamCount(1, 1); if popLong() != 0L then jump(labelParam(0))
                    case "call" => ()
                    case "ret" => ()
                    case "exit" => ()
                    case _ => throw error(s"Unknown assembler instruction: ${instr.name}")

                if nextInstr then idx += 1

/**
  *
  */
trait MirAsmExecutable:
    /**
      *
      * @param ctx
      */
    def execute(ctx: MirAsmContext): Unit
