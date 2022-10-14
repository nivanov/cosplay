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
import org.cosplay.games.mir.*

import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.lang3.StringUtils

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
            type StackList = mutable.ArrayBuffer[Any]
            type StackMap = mutable.HashMap[String, Any]

            val stack = mutable.Stack.empty[Any]
            val callStack = mutable.Stack.empty[Int]

            // Ensure instructions are sorted & indexed.
            val code = instrs.sortBy(_.line)
            // Create labels LUT.
            val lblLut: immutable.HashMap[String, Int] = immutable.HashMap.from(
                code.zipWithIndex.filter((instr, _) => instr.label.isDefined).map((instr, idx) => instr.label.get -> idx)
            )

            var idx = 0
            val len = code.length
            var exit = false
            while (idx < len && !exit)
                val instr = code(idx)
                val name = instr.name
                val params = instr.params
                val paramsCnt = params.length

                type MAE = MirAsmException

                def formatActual(act: Any): String = act match
                    case s: String => s"\"$s\""
                    case _: Any => act.toString

                def removeDot(s: String): String = if s.endsWith(".") then s.substring(0, s.length - 1) else s

                def error(errMsg: String): MAE = new MAE(errMsg, s"${removeDot(errMsg.trim)} - at line ${instr.line} in '${instr.getSourceCode(false)}'.", instr.dbg)
                def wrongStack(act: Any, exp: String): MAE = error(s"Unexpected asm stack value '${formatActual(act)}' - expecting $exp")
                def wrongParam(idx: Int, exp: String): MAE = error(s"Invalid asm ${nth(idx)} parameter - expecting $exp")
                def wrongVar(id: String, exp: String): MAE = error(s"Invalid asm variable '$id' type - expecting $exp")
                def wrongLabel(id: String): MAE = error(s"Undefined asm label in jump: $id")

                def checkParamCount(min: Int, max: Int): Unit =
                    if paramsCnt < min then throw error("Insufficient asm instruction parameters")
                    if paramsCnt > max then throw error("Too many asm instruction parameters")

                def getVar(id: String): Any = ctx.getVar(id) match
                    case Some(v) => v
                    case None => throw error(s"Undefined asm variable: $id")

                def pop(): Any =
                    try stack.pop()
                    catch case _: NoSuchElementException => throw error(s"Stack is empty (missing return value?)")

                def popStr(): String = pop() match
                    case s: String => s
                    case x => throw wrongStack(x, "string")

                def popList(): StackList = pop() match
                    case list: StackList => list
                    case x => throw wrongStack(x, "list")

                def popMap(): StackMap = pop() match
                    case map: StackMap => map
                    case x => throw wrongStack(x, "map")

                def popListOrMap(): StackMap | StackList = pop() match
                    case list: StackList => list
                    case map: StackMap => map
                    case x => throw wrongStack(x, "map or list")

                def popLong(): Long = pop() match
                    case d: Long => d
                    case x => throw wrongStack(x, "integer")

                def popInt(): Int = pop() match
                    case d: Long => d.toInt
                    case x => throw wrongStack(x, "integer")

                def popDouble(): Double = pop() match
                    case d: Double => d
                    case x => throw wrongStack(x, "double")

                def popBool(): Long =
                    val b = pop()
                    b match
                        case d: Long => if d  == 1L || d == 0L then d else throw wrongStack(b, "1(true) or 0(false)")
                        case _ => throw wrongStack(b, "1(true) or 0(false)")

                def popTrue(): Boolean = popBool() == 1L
                def popFalse(): Boolean = popBool() == 0L

                def varParam(idx: Int): String = params(idx) match
                    case IdParam(id) => id
                    case _ => throw wrongParam(idx, "variable")

                def labelParam(idx: Int): String = params(idx) match
                    case IdParam(id) => id
                    case _ => throw wrongParam(idx, "label")

                def longParam(idx: Int): Long = params(idx) match
                    case LongParam(d) => d
                    case _ => throw wrongParam(idx, "numeric")

                def intParam(idx: Int): Int  = params(idx) match
                    case LongParam(d) => d.toInt
                    case _ => throw wrongParam(idx, "numeric")

                def strOrVarParam(idx: Int): String =
                    params(idx) match
                        case StringParam(s) => s
                        case IdParam(id) => getVar(id) match
                            case s: String => s
                            case _ => throw wrongParam(idx, "string or string variable")
                        case _ => throw wrongParam(idx, "string or string variable")

                def anyParam(idx: Int): Any = // Any parameter including variable.
                    params(idx) match
                        case NullParam => null
                        case StringParam(s) => s
                        case LongParam(d) => d
                        case DoubleParam(d) => d
                        case IdParam(id) => getVar(id) // Returning variable's value (NOT its ID).

                //noinspection DuplicatedCode
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
                        // String concatenation.
                        case s: String => ctx.setVar(id, s"$s${v.toString}")
                        case _ => throw wrongVar(id, "numeric")

                //noinspection DuplicatedCode
                def multiplyVar(): Unit =
                    val id = varParam(0) // 1st parameter (always variable).
                    val v = anyParam(1) // 2d parameter (anything, including variable).
                    getVar(id) match
                        case d1: Long => v match
                            case d2: Long => ctx.setVar(id, d2 * d1)
                            case d2: Double => ctx.setVar(id, d2 * d1)
                            case _ => throw wrongParam(1, "numeric")
                        case d1: Double => v match
                            case d2: Long => ctx.setVar(id, d2 * d1)
                            case d2: Double => ctx.setVar(id, d2 * d1)
                            case _ => throw wrongParam(1, "numeric")
                        case _ => throw wrongVar(id, "numeric")

                //noinspection DuplicatedCode
                def divideVar(): Unit =
                    val id = varParam(0) // Dividend (always variable).
                    val v = anyParam(1) // Divisor (anything, including variable).
                    getVar(id) match
                        case d1: Long => v match
                            case d2: Long => ctx.setVar(id, d1 / d2)
                            case d2: Double => ctx.setVar(id, d1 / d2)
                            case _ => throw wrongParam(1, "numeric")
                        case d1: Double => v match
                            case d2: Long => ctx.setVar(id, d1 / d2)
                            case d2: Double => ctx.setVar(id, d1 / d2)
                            case _ => throw wrongParam(1, "numeric")
                        case _ => throw wrongVar(id, "numeric")

                def longVar(id: String): Long = getVar(id) match
                    case d: Long => d
                    case _ => throw wrongVar(id, "integer")

                def intVar(id: String): Int = getVar(id) match
                    case d: Long => d.toInt
                    case _ => throw wrongVar(id, "integer")

                def strVar(id: String): String = getVar(id) match
                    case s: String  => s
                    case _ => throw wrongVar(id, "string")

                def boolVar(id: String): Long = getVar(id) match
                    case d: Long => if d == 1 || d == 0 then d else throw wrongVar(id, "1(true) or 0(false)")
                    case _ => throw wrongVar(id, "1(true) or 0(false)")

                def incDecVar(v: Int): Unit =
                    require(v == 1 || v == -1)
                    val id = varParam(0) // 1st parameter (always variable).
                    ctx.setVar(id, longVar(id) + v)

                def incDec(v: Int): Unit =
                    require(v == 1 || v == -1)
                    pop() match
                        case d: Long => push(d + v)
                        case x => throw wrongStack(x, "integer")

                def pushBool(b: Boolean): Unit = push(if b then 1L else 0L)

                def push(v: Any): Unit =
                    v match
                        case d: Long => stack.push(d)
                        case d: Double => stack.push(d)
                        case s: String => stack.push(s)
                        case d: Int => stack.push(d.toLong)
                        case list: StackList => stack.push(list)
                        case map: StackMap => stack.push(map)
                        case _ => assert(false, s"Invalid asm stack value type: $v")

                def neg(): Unit = pop() match
                    case d: Long => push(-d)
                    case d: Double => push (-d)
                    case x => throw wrongStack(x, "numeric")

                def negv(): Unit =
                    val id = varParam(0)
                    getVar(id) match
                        case d: Long => ctx.setVar(id, -d)
                        case d: Double => ctx.setVar(id, -d)
                        case _ => throw wrongVar(id, "numeric")

                def notv(): Unit =
                    val id = varParam(0)
                    ctx.setVar(id, if boolVar(id) == 0L then 1L else 0L)

                def mod(): Unit =
                    val v1 = popLong()
                    val v2 = popLong()
                    push(v2 % v1)

                //noinspection DuplicatedCode
                def addSub(mul: Int): Unit =
                    require(mul == 1 || mul == -1)
                    val right = pop()
                    val left = pop()
                    left match
                        case d1: Long => right match
                            case d2: Long => push(d1 + d2 * mul)
                            case d2: Double => push(d1 + d2 * mul)
                            case _ => throw wrongStack(d1, "numeric")
                        case d1: Double => right match
                            case d2: Long => push(d1 + d2 * mul)
                            case d2: Double => push(d1 + d2 * mul)
                            case _ => throw wrongStack(d1, "numeric")
                        // String concatenation.
                        case s: String => push(s"$s${right.toString}")
                        case _ => throw wrongStack(left, "numeric or string")

                def and(): Unit = pushBool(popTrue() && popTrue())
                def or(): Unit = pushBool(popTrue() || popTrue())

                //noinspection DuplicatedCode
                def multiply(): Unit =
                    val v1 = pop()
                    val v2 = pop()
                    v1 match
                        case d1: Long => v2 match
                            case d2: Long => push(d2 * d1)
                            case d2: Double => push(d2 * d1)
                            case _ => throw wrongStack(d1, "numeric")
                        case d1: Double => v2 match
                            case d2: Long => push(d2 * d1)
                            case d2: Double => push(d2 * d1)
                            case _ => throw wrongStack(d1, "numeric")
                        case _ => throw wrongStack(v1, "numeric")

                //noinspection DuplicatedCode
                def divide(): Unit =
                    val v1 = pop() // Divisor.
                    val v2 = pop() // Dividend.
                    v1 match
                        case d1: Long => v2 match
                            case d2: Long => push(d2 / d1)
                            case d2: Double => push(d2 / d1)
                            case _ => throw wrongStack(d1, "numeric")
                        case d1: Double => v2 match
                            case d2: Long => push(d2 / d1)
                            case d2: Double => push(d2 / d1)
                            case _ => throw wrongStack(d1, "numeric")
                        case _ => throw wrongStack(v1, "numeric")

                def listMapOp[T](listOp: StackList => T, mapOp: StackMap => T): T =
                    val x = popListOrMap()
                    val res = x match
                        case list: StackList => listOp(list)
                        case map: StackMap => mapOp(map)
                    res

                object NativeFunctions:
                    def print(): Unit = ctx.getExecContext.out.print(pop().toString)
                    def println(): Unit = ctx.getExecContext.out.println(pop().toString)
                    def to_str(): Unit = push(pop().toString)
                    def length(): Unit = push(popStr().length.toLong)
                    def assert(): Unit =
                        // Note the reverse order of popping.
                        val msg = MirUtils.decapitalize(popStr())
                        val cond = popBool() == 0
                        if cond then throw error(s"Assertion - $msg")
                    def ensure(): Unit = if popBool() == 0 then throw error(s"Condition failed.")
                    def concat(): Unit =
                        val s1 = pop().toString
                        val s2 = pop().toString
                        push(s"$s2$s1") // Note reverse order...
                    def regex(): Unit = ()
                    def uppercase(): Unit = push(popStr().toUpperCase)
                    def lowercase(): Unit = push(popStr().toLowerCase)
                    def is_alpha(): Unit = pushBool(StringUtils.isAlpha(popStr()))
                    def is_num(): Unit = pushBool(StringUtils.isNumeric(popStr()))
                    def is_alphanum(): Unit = pushBool(StringUtils.isAlphanumeric(popStr()))
                    def is_whitespace(): Unit = pushBool(StringUtils.isWhitespace(popStr()))
                    def is_alphaspace(): Unit = pushBool(StringUtils.isAlphaSpace(popStr()))
                    def is_numspace(): Unit = pushBool(StringUtils.isNumericSpace(popStr()))
                    def is_alphanumspace(): Unit = pushBool(StringUtils.isAlphanumericSpace(popStr()))
                    def split(): Unit =
                        val sepRegex = popStr()
                        val str = popStr()
                        val list = mutable.ArrayBuffer.from(str.split(sepRegex))
                        push(list)
                    def split_trim(): Unit = ()
                    def trim(): Unit = push(popStr().trim())
                    def start_width(): Unit = ()
                    def end_width(): Unit = ()
                    def contains(): Unit = ()
                    def substr(): Unit = ()
                    def replace(): Unit = ()
                    def to_int(): Unit = ()
                    def to_double(): Unit = ()
                    def now(): Unit = ()
                    def year(): Unit = ()
                    def month(): Unit = ()
                    def day_of_month(): Unit = ()
                    def day_of_week(): Unit = ()
                    def day_of_year(): Unit = ()
                    def hour(): Unit = ()
                    def minute(): Unit = ()
                    def second(): Unit = ()
                    def week_of_month(): Unit = ()
                    def week_of_year(): Unit = ()
                    def quarter(): Unit = ()
                    def format_date(): Unit = ()
                    def format_time(): Unit = ()
                    def format_datetime(): Unit = ()
                    def new_list(): Unit = push(mutable.ArrayBuffer.empty[Any])
                    def new_map(): Unit = push(mutable.HashMap.empty[String, Any])
                    def clear(): Unit = listMapOp(_.clear(), _.clear())
                    def get(): Unit = ()
                    def copy(): Unit =()
                    def key_list(): Unit =()
                    def value_list(): Unit =()
                    def size(): Unit = push(listMapOp(_.size, _.size))
                    def drop(): Unit =()
                    def drop_right(): Unit =()
                    def contains_key(): Unit =()
                    def contains_value(): Unit =()
                    def put(): Unit = ()
                    def first(): Unit = push(popList().head)
                    def last(): Unit = push(popList().last)
                    def index_of(): Unit =()
                    def index_of_start(): Unit =()
                    def last_index_of(): Unit =()
                    def is_empty(): Unit = pushBool(listMapOp(_.isEmpty, _.isEmpty))
                    def non_empty(): Unit = pushBool(listMapOp(_.nonEmpty, _.nonEmpty))
                    def mk_string(): Unit =()
                    def distinct(): Unit =()
                    def add_list(): Unit =()
                    def has_all(): Unit =()
                    def has_any(): Unit =()
                    def add(): Unit =
                        val v = pop()
                        val list = popList()
                        list += v
                    def prepend(): Unit =
                        val v = pop()
                        val list = popList()
                        list.prepend(v)
                    def update(): Unit =()
                    def remove(): Unit =
                        val idx = popInt()
                        val list = popList()
                        list.remove(idx)
                    def take(): Unit =
                        val n = popInt()
                        val list = popList()
                        push(list.take(n))
                    def take_right(): Unit =
                        val n = popInt()
                        val list = popList()
                        push(list.takeRight(n))
                    def reverse(): Unit =()
                    def ceil(): Unit = push(math.ceil(popDouble()))
                    def floor(): Unit = push(math.floor(popDouble()))
                    def rint(): Unit = push(math.rint(popDouble()))
                    def round(): Unit = push(math.round(popDouble()))
                    def signum(): Unit = push(math.signum(popDouble()))
                    def sqrt(): Unit =()
                    def cbrt(): Unit =()
                    def acos(): Unit = push(math.acos(popDouble()))
                    def asin(): Unit = push(math.asin(popDouble()))
                    def atan(): Unit = push(math.atan(popDouble()))
                    def tan(): Unit = push(math.tan(popDouble()))
                    def sin(): Unit = push(math.sin(popDouble()))
                    def cos(): Unit = push(math.cos(popDouble()))
                    def tanh(): Unit = push(math.tanh(popDouble()))
                    def sinh(): Unit = push(math.sinh(popDouble()))
                    def cosh(): Unit = push(math.cosh(popDouble()))
                    def degrees(): Unit =()
                    def radians(): Unit =()
                    def exp(): Unit = push(math.exp(popDouble()))
                    def expm1(): Unit = push(math.expm1(popDouble()))
                    def log(): Unit = push(math.log(popDouble()))
                    def log10(): Unit = push(math.log10(popDouble()))
                    def square(): Unit =()
                    def log1p(): Unit = push(math.log1p(popDouble()))
                    def pi(): Unit =()
                    def euler(): Unit =()
                    def min(): Unit =()
                    def max(): Unit =()
                    def avg(): Unit =()
                    def stddev(): Unit =()
                    def pow(): Unit =()
                    def hypot(): Unit =()

                // Go to the "natural" next instruction... or jump.
                var nextInstr = true

                def jump(lbl: String): Unit =
                    lblLut.get(lbl) match
                        case Some(i) =>
                            idx = i
                            nextInstr = false
                        case None => throw wrongLabel(lbl)

                //noinspection DuplicatedCode
                def ltv(id: String, v: Any): Unit =
                    getVar(id) match
                        case d2: Long => v match
                            case d1: Long => pushBool(d2 < d1)
                            case d1: Double => pushBool(d2 < d1)
                            case _ => wrongParam(1, "numeric")
                        case d2: Double => v match
                            case d1: Long => pushBool(d2 < d1)
                            case d1: Double => pushBool(d2 < d1)
                            case _ => wrongParam(1, "numeric")
                        case _ => wrongVar(id, "numeric")

                //noinspection DuplicatedCode
                def ltev(id: String, v: Any): Unit =
                    getVar(id) match
                        case d2: Long => v match
                            case d1: Long => pushBool(d2 <= d1)
                            case d1: Double => pushBool(d2 <= d1)
                            case _ => wrongParam(1, "numeric")
                        case d2: Double => v match
                            case d1: Long => pushBool(d2 <= d1)
                            case d1: Double => pushBool(d2 <= d1)
                            case _ => wrongParam(1, "numeric")
                        case _ => wrongVar(id, "numeric")

                //noinspection DuplicatedCode
                def gtv(id: String, v: Any): Unit =
                    getVar(id) match
                        case d2: Long => v match
                            case d1: Long => pushBool(d2 > d1)
                            case d1: Double => pushBool(d2 > d1)
                            case _ => wrongParam(1, "numeric")
                        case d2: Double => v match
                            case d1: Long => pushBool(d2 > d1)
                            case d1: Double => pushBool(d2 > d1)
                            case _ => wrongParam(1, "numeric")
                        case _ => wrongVar(id, "numeric")

                //noinspection DuplicatedCode
                def gtev(id: String, v: Any): Unit =
                    getVar(id) match
                        case d2: Long => v match
                            case d1: Long => pushBool(d2 >= d1)
                            case d1: Double => pushBool(d2 >= d1)
                            case _ => wrongParam(1, "numeric")
                        case d2: Double => v match
                            case d1: Long => pushBool(d2 >= d1)
                            case d1: Double => pushBool(d2 >= d1)
                            case _ => wrongParam(1, "numeric")
                        case _ => wrongVar(id, "numeric")

                //noinspection DuplicatedCode
                def lt(v2: Any, v1: Any): Unit =
                    v2 match
                        case d2: Long => v1 match
                            case d1: Long => pushBool(d1 < d2)
                            case d1: Double => pushBool(d1 < d2)
                            case _ => wrongStack(v1, "numeric")
                        case d2: Double => v1 match
                            case d1: Long => pushBool(d1 < d2)
                            case d1: Double => pushBool(d1 < d2)
                            case _ => wrongStack(v1, "numeric")
                        case _ => wrongStack(v2, "numeric")

                //noinspection DuplicatedCode
                def gt(v2: Any, v1: Any): Unit =
                    v2 match
                        case d2: Long => v1 match
                            case d1: Long => pushBool(d1 > d2)
                            case d1: Double => pushBool(d1 > d2)
                            case _ => wrongStack(v1, "numeric")
                        case d2: Double => v1 match
                            case d1: Long => pushBool(d1 > d2)
                            case d1: Double => pushBool(d1 > d2)
                            case _ => wrongStack(v1, "numeric")
                        case _ => wrongStack(v2, "numeric")

                //noinspection DuplicatedCode
                def gte(v2: Any, v1: Any): Unit =
                    v2 match
                        case d2: Long => v1 match
                            case d1: Long => pushBool(d1 >= d2)
                            case d1: Double => pushBool(d1 >= d2)
                            case _ => wrongStack(v1, "numeric")
                        case d2: Double => v1 match
                            case d1: Long => pushBool(d1 >= d2)
                            case d1: Double => pushBool(d1 >= d2)
                            case _ => wrongStack(v1, "numeric")
                        case _ => wrongStack(v2, "numeric")

                //noinspection DuplicatedCode
                def lte(v2: Any, v1: Any): Unit =
                    v2 match
                        case d2: Long => v1 match
                            case d1: Long => pushBool(d1 <= d2)
                            case d1: Double => pushBool(d1 <= d2)
                            case _ => wrongStack(v1, "numeric")
                        case d2: Double => v1 match
                            case d1: Long => pushBool(d1 <= d2)
                            case d1: Double => pushBool(d1 <= d2)
                            case _ => wrongStack(v1, "numeric")
                        case _ => wrongStack(v2, "numeric")

                name match
                    case "cpop" =>
                        checkParamCount(0, 1)
                        if stack.nonEmpty then
                            if params.isEmpty then pop() else ctx.setVar(varParam(0), pop())
                    case "push" => checkParamCount(1, 1); push(anyParam(0))
                    case "pushn" => checkParamCount(1, Int.MaxValue); for (i <- 0 until paramsCnt) push(anyParam(i))
                    case "and" => checkParamCount(0, 0); and()
                    case "or" => checkParamCount(0, 0); or()
                    case "pop" => checkParamCount(0, 1); if params.isEmpty then pop() else ctx.setVar(varParam(0), pop())
                    case "add" => checkParamCount(0, 0); addSub(1)
                    case "mod" => checkParamCount(0, 0); mod()
                    case "inc" => checkParamCount(0, 0); incDec(1)
                    case "dec" => checkParamCount(0, 0); incDec(-1)
                    case "mul" => checkParamCount(0, 0); multiply()
                    case "div" => checkParamCount(0, 0); divide()
                    case "incv" => checkParamCount(1, 1); incDecVar(1)
                    case "decv" => checkParamCount(1, 1); incDecVar(-1)
                    case "sub" => checkParamCount(0, 0); addSub(-1)
                    case "addv" => checkParamCount(2, 2); addSubVar(1)
                    case "mulv" => checkParamCount(2, 2); multiplyVar()
                    case "divv" => checkParamCount(2, 2); divideVar()
                    case "subv" => checkParamCount(2, 2); addSubVar(-1)
                    case "neg" => checkParamCount(0, 0); neg()
                    case "negv" => checkParamCount(1, 1); negv()
                    case "not" => checkParamCount(0, 0); pushBool(!popTrue())
                    case "notv" => checkParamCount(1, 1); notv()
                    case "let" => checkParamCount(2, 2); ctx.setVar(varParam(0), anyParam(1))
                    case "dup" => checkParamCount(0, 0); if stack.nonEmpty then push(stack.head)
                    case "eq" => checkParamCount(0, 0); pushBool(pop() == pop())
                    case "neq" => checkParamCount(0, 0); pushBool(pop() != pop())
                    case "lt" => checkParamCount(0, 0); lt(pop(), pop())
                    case "lte" => checkParamCount(0, 0); lte(pop(), pop())
                    case "gt" => checkParamCount(0, 0); gt(pop(), pop())
                    case "gte" => checkParamCount(0, 0); gte(pop(), pop())
                    case "ltp" => checkParamCount(1, 1); lt(anyParam(0), pop())
                    case "ltep" => checkParamCount(1, 1); lte(anyParam(0), pop())
                    case "gtp" => checkParamCount(1, 1); gt(anyParam(0), pop())
                    case "gtep" => checkParamCount(1, 1); gte(anyParam(0), pop())
                    case "ltv" => checkParamCount(2, 2); ltv(varParam(0), anyParam(1))
                    case "ltev" => checkParamCount(2, 2); ltev(varParam(0), anyParam(1))
                    case "gtv" => checkParamCount(2, 2); gtv(varParam(0), anyParam(1))
                    case "gtev" => checkParamCount(2, 2); gtev(varParam(0), anyParam(1))
                    case "eqv" => checkParamCount(2, 2); pushBool(getVar(varParam(0)) == anyParam(1))
                    case "neqv" => checkParamCount(2, 2); pushBool(getVar(varParam(0)) != anyParam(1))
                    case "eqp" => checkParamCount(1, 1); pushBool(anyParam(0) == pop())
                    case "neqp" => checkParamCount(1, 1); pushBool(anyParam(0) != pop())
                    case "brk" => checkParamCount(0, 1); throw error(if paramsCnt == 1 then strOrVarParam(0) else "Aborted")
                    case "cbrk" => checkParamCount(0, 1); if popFalse() then throw error(if paramsCnt == 1 then strOrVarParam(0) else "Aborted")
                    case "cbrkv" => checkParamCount(1, 2); if getVar(varParam(0)) == 0L then throw error(if paramsCnt == 2 then strOrVarParam(1) else "Aborted")
                    case "cjmpv" => checkParamCount(2, 2); if getVar(varParam(0)) != 0L then jump(labelParam(1))
                    case "jmp" => checkParamCount(1, 1); jump(labelParam(0))
                    case "cjmp" => checkParamCount(1, 1); if popTrue() then jump(labelParam(0))
                    case "ifjmp" => checkParamCount(2, 2); if popTrue() then jump(labelParam(0)) else jump(labelParam(1))
                    case "ifjmpv" => checkParamCount(2, 2); if popTrue() then jump(strVar(varParam(0))) else jump(strVar(varParam(1)))
                    case "exit" => checkParamCount(0, 0); exit = true
                    case "nop" => checkParamCount(0, 0) // No-op instruction.
                    case "clr" => checkParamCount(0, 0); for _ <- 0 until popInt() do stack.pop()
                    case "clrp" => checkParamCount(1, 1); for _ <- 0 until intParam(0) do stack.pop()
                    case "clrv" => checkParamCount(1, 1); for _ <- 0 until intVar(varParam(0)) do stack.pop()
                    case "ssz" => checkParamCount(0, 0); push(stack.size)
                    case "calln" =>
                        checkParamCount(1, 1)
                        val fn = strOrVarParam(0)
                        fn match
                            case "print" => NativeFunctions.print()
                            case "println" => NativeFunctions.println()

                            // Date and time functions.
                            case "now" => NativeFunctions.now()
                            case "year" => NativeFunctions.year()
                            case "month" => NativeFunctions.month()
                            case "day_of_month" => NativeFunctions.day_of_month()
                            case "day_of_week" => NativeFunctions.day_of_week()
                            case "day_of_year" => NativeFunctions.day_of_year()
                            case "hour" => NativeFunctions.hour()
                            case "minute" => NativeFunctions.minute()
                            case "second" => NativeFunctions.second()
                            case "week_of_month" => NativeFunctions.week_of_month()
                            case "week_of_year" => NativeFunctions.week_of_year()
                            case "quarter" => NativeFunctions.quarter()
                            case "format_date" => NativeFunctions.format_date()
                            case "format_time" => NativeFunctions.format_time()
                            case "format_datetime" => NativeFunctions.format_datetime()

                            // Math functions.
                            case "ceil" => NativeFunctions.ceil()
                            case "floor" => NativeFunctions.floor()
                            case "rint" => NativeFunctions.rint()
                            case "round" => NativeFunctions.round()
                            case "signum" => NativeFunctions.signum()
                            case "sqrt" => NativeFunctions.sqrt()
                            case "cbrt" => NativeFunctions.cbrt()
                            case "acos" => NativeFunctions.acos()
                            case "asin" => NativeFunctions.asin()
                            case "atan" => NativeFunctions.atan()
                            case "tan" => NativeFunctions.tan()
                            case "sin" => NativeFunctions.sin()
                            case "cos" => NativeFunctions.cos()
                            case "tanh" => NativeFunctions.tanh()
                            case "sinh" => NativeFunctions.sinh()
                            case "cosh" => NativeFunctions.cosh()
                            case "degrees" => NativeFunctions.degrees()
                            case "radians" => NativeFunctions.radians()
                            case "exp" => NativeFunctions.exp()
                            case "expm1" => NativeFunctions.expm1()
                            case "log" => NativeFunctions.log()
                            case "log10" => NativeFunctions.log10()
                            case "square" => NativeFunctions.square()
                            case "log1p" => NativeFunctions.log1p()
                            case "pi" => NativeFunctions.pi()
                            case "euler" => NativeFunctions.euler()
                            case "min" => NativeFunctions.min()
                            case "max" => NativeFunctions.max()
                            case "avg" => NativeFunctions.avg()
                            case "stddev" => NativeFunctions.stddev()
                            case "pow" => NativeFunctions.pow()
                            case "hypot" => NativeFunctions.hypot()

                            // String functions.
                            case "concat" => NativeFunctions.concat()
                            case "to_str" => NativeFunctions.to_str()
                            case "length" => NativeFunctions.length()
                            case "regex" => NativeFunctions.regex()
                            case "uppercase" => NativeFunctions.uppercase()
                            case "lowercase" => NativeFunctions.lowercase()
                            case "is_alpha" => NativeFunctions.is_alpha()
                            case "is_num" => NativeFunctions.is_num()
                            case "is_alphanum" => NativeFunctions.is_alphanum()
                            case "is_whitespace" => NativeFunctions.is_whitespace()
                            case "is_alphaspace" => NativeFunctions.is_alphaspace()
                            case "is_numspace" => NativeFunctions.is_numspace()
                            case "is_alphanumspace" => NativeFunctions.is_alphanumspace()
                            case "split" => NativeFunctions.split()
                            case "trim" => NativeFunctions.trim()
                            case "split_trim" => NativeFunctions.split_trim()
                            case "start_width" => NativeFunctions.start_width()
                            case "end_width" => NativeFunctions.end_width()
                            case "contains" => NativeFunctions.contains()
                            case "substr" => NativeFunctions.substr()
                            case "replace" => NativeFunctions.replace()
                            case "to_int" => NativeFunctions.to_int()
                            case "to_double" => NativeFunctions.to_double()

                            case "assert" => NativeFunctions.assert()
                            case "ensure" => NativeFunctions.ensure()

                            // List/Map functions.
                            case "new_list" => NativeFunctions.new_list()
                            case "new_map" => NativeFunctions.new_map()
                            case "clear" => NativeFunctions.clear()
                            case "get" => NativeFunctions.get()
                            case "copy" => NativeFunctions.copy()
                            case "key_list" => NativeFunctions.key_list()
                            case "value_list" => NativeFunctions.value_list()
                            case "size" => NativeFunctions.size()
                            case "drop" => NativeFunctions.drop()
                            case "drop_right" => NativeFunctions.drop_right()
                            case "contains_key" => NativeFunctions.contains_key()
                            case "contains_value" => NativeFunctions.contains_value()
                            case "put" => NativeFunctions.put()
                            case "first" => NativeFunctions.first()
                            case "last" => NativeFunctions.last()
                            case "index_of" => NativeFunctions.index_of()
                            case "index_of_start" => NativeFunctions.index_of_start()
                            case "last_index_of" => NativeFunctions.last_index_of()
                            case "is_empty" => NativeFunctions.is_empty()
                            case "non_empty" => NativeFunctions.non_empty()
                            case "mk_string" => NativeFunctions.mk_string()
                            case "distinct" => NativeFunctions.distinct()
                            case "add_list" => NativeFunctions.add_list()
                            case "has_all" => NativeFunctions.has_all()
                            case "has_any" => NativeFunctions.has_any()
                            case "add" => NativeFunctions.add()
                            case "prepend" => NativeFunctions.prepend()
                            case "update" => NativeFunctions.update()
                            case "remove" => NativeFunctions.remove()
                            case "take" => NativeFunctions.take()
                            case "take_right" => NativeFunctions.take_right()
                            case "reverse" => NativeFunctions.reverse()

                            // TODO: debug only.
                            case "_println" => println(pop().toString)

                            case s => throw error(s"Unknown native function: $s")
                    case "call" =>
                        checkParamCount(1, 1)
                        callStack.push(idx + 1)
                        jump(labelParam(0))
                    case "ret" =>
                        checkParamCount(0, 0)
                        if callStack.isEmpty then throw error(s"'ret' outside of function body.")
                        idx = callStack.pop()
                        nextInstr = false
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
