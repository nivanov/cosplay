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
import games.mir.*
import os.progs.mash.*
import os.progs.asm.compiler.*
import os.progs.mash.compiler.antlr4.*
import org.antlr.v4.runtime.tree.*
import org.antlr.v4.runtime.*
import org.cosplay.games.mir.os.progs.asm.compiler.MirAsmException

import java.util.UUID
import java.util.regex.*
import scala.collection.mutable

import MirMashParser as MMP
import ParserRuleContext as PRC

/**
  * A container for translated assembler code and global declarations.
  *
  * @param asm Assembler instructions.
  * @param global Global scope.
  */
case class MirMashModule(asm: Seq[MirMashAsm], global: MirMashScope):
    require(global.isGlobal)

/**
  *
  */
enum MirMashDeclarationKind:
    case VAR, FUN, VAL, ALS, NAT

/**
  * A mash declaration of some kind.
  *
  * @param kind Type of the declaration.
  * @param scope Declaration scope.
  * @param name Declaration short name.
  */
class MirMashDecl(val kind: MirMashDeclarationKind, val scope: MirMashScope, val name: String):
    import MirMashDeclarationKind.*

    /** Fully qualified name. */
    val fqName: String = s"${name}_${scope.namespace}"
    val kindStr: String = kind match
        case VAR => "variable"
        case VAL => "value"
        case FUN => "function"
        case ALS => "alias"
        case NAT => "native function"

/**
  * Native or regular function declaration.
  *
  * @param paramCnt Number of function parameters.
  * @param kind Type of the declaration.
  * @param scope Declaration scope.
  * @param name Declaration name.
  */
class MirMashDefDecl(val paramCnt: Int, kind: MirMashDeclarationKind, scope: MirMashScope, name: String) extends MirMashDecl(kind, scope, name):
    require(kind == MirMashDeclarationKind.NAT || kind == MirMashDeclarationKind.FUN)

/**
  * Alias declaration.
  *
  * @param alias Alias value.
  * @param kind Type of the declaration.
  * @param scope Declaration scope.
  * @param name Declaration name.
  */
class MirMashAliasDecl(val alias: String, kind: MirMashDeclarationKind, scope: MirMashScope, name: String) extends MirMashDecl(kind, scope, name):
    require(kind == MirMashDeclarationKind.ALS)

/**
  *
  */
object MirMashScope:
    // Not very unique, of course - but good enough.
    private val base = UUID.randomUUID().hashCode().abs.toString
    private var cnt = 0L

    /**
      *
      */
    private def guid(): String =
        val id = s"S${base}_$cnt"
        cnt += 1
        id

/**
  * A namespace container.
  *
  * @param parent Optional parent scope (`None` for global scope).
  */
case class MirMashScope(parent: Option[MirMashScope] = None):
    import MirMashScope.*

    private val decls = mutable.HashMap.empty[String, MirMashDecl]
    private final val id = guid()

    val namespace: String =
        var x = parent
        var p = "$" + id
        while x.isDefined do
            val xx = x.get
            p += "$" + xx.id
            x = xx.parent
        p

    def isGlobal: Boolean = parent.isEmpty
    def hasDecl(name: String): Boolean = decls.contains(name)
    def addDecl(decl: MirMashDecl): Unit = decls += decl.name -> decl
    def removeDecl(decl: MirMashDecl): Unit = decls -= decl.name
    def getDecl(name: String): Option[MirMashDecl] = decls.get(name)
    def getDecls: Seq[MirMashDecl] = decls.values.toSeq
    def subScope: MirMashScope =
        val scope = MirMashScope(this.?)
        decls.values.foreach(scope.addDecl)
        scope

/**
  * Portable assembler block.
  *
  * @param origin Name of the original source (i.e. file name).
  * @param startLabel Start label of this portable block.
  * @param parent Optional parent of this compilation block.
  */
case class AsmBlock(origin: String, startLabel: String, parent: Option[AsmBlock] = None):
    private val asm = mutable.ArrayBuffer.empty[MirMashAsm]
    private val children = mutable.ArrayBuffer.empty[AsmBlock]

    // Add initial label marker to all nested blocks.
    if parent.isDefined then asm += MirMashAsm(startLabel.?, None, None, None)

    def linkAsm: Seq[MirMashAsm] =
        val code = mutable.ArrayBuffer.empty[MirMashAsm]
        code.addAll(asm) // Add itself.
        children.foreach(child => code.addAll(child.linkAsm)) // Add all of its children recursively.
        code.toSeq
    def subBlock(startLbl: String): AsmBlock =
        val block = AsmBlock(origin, startLbl, this.?)
        children += block
        block
    // NOTE: comment without leading ';'.
    def add(instr: String, lbl: String = null, cmt: String = null)(using ctx: PRC): Unit = asm.append(mkAsm(lbl.?, instr.?, cmt.?))
    def last(): Option[MirMashAsm] = asm.lastOption
    def removeLast(): Unit = if asm.nonEmpty then asm.remove(asm.length - 1)

    private def mkAsm(lbl: Option[String], instr: Option[String], cmt: Option[String])(using ctx: PRC): MirMashAsm =
        require(lbl.isDefined || instr.isDefined || cmt.isDefined)

        val tok = ctx.start
        val line = tok.getLine
        val pos = tok.getCharPositionInLine
        val dbg = MirMashAsmDebug(line, pos, origin)
        MirMashAsm(lbl, instr, cmt, dbg.?)

/**
  *
  * @param line Original source line name.
  * @param pos In-line character position.
  * @param origin Name of the original source (i.e. file name).
  */
case class MirMashAsmDebug(line: Int, pos: Int, origin: String)

/**
  *
  * @param label Optional asm instruction label.
  * @param instruction Optional instruction (if line contains only label and/or comment).
  * @param comment Optional comment.
  */
case class MirMashAsm(
    label: Option[String],
    instruction: Option[String],
    comment: Option[String], // Without leading ';'.
    dbg: Option[MirMashAsmDebug]
):
    require(label.isDefined || instruction.isDefined || comment.isDefined)

    def toAsmString(useDbg: Boolean): String =
        val lbl = label match
            case Some(s) => s"$s: "
            case None => ""
        val cmt = comment match
            case Some(s) => s"; $s"
            case None => ""
        if instruction.isEmpty then
            s"$lbl$cmt"
        else
            var ins = s"${instruction.getOrElse("")}${if useDbg && dbg.isDefined then s" @${dbg.get.line},${dbg.get.pos},\"${dbg.get.origin}\"" else ""}"
            if ins.nonEmpty then ins += " "
            f"$lbl%-5s$ins$cmt"

/**
  *
  */
object MirMashCompiler:
    final val VER = "1.0.1"
    final val VAR_REGEX = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$")
    final val STR_VAR_REGEX = Pattern.compile("[^\\\\]?\\$(\\$|#|!|@|\\*|[0-9]+|[a-zA-Z_][a-zA-Z0-9_]*)")

    /**
      *
      */
    enum StrKind:
        case NUM, UNDEF
        case VAR, VAL, ALS, FUN, NAT // Same as declaration type.

    /**
      *
      * @param kind
      * @param str
      * @param decl
      */
    case class StrEntity(kind: StrKind, str: String, decl: Option[MirMashDecl] = None)

/**
  *
  */
class MirMashCompiler:
    import MirMashDeclarationKind.*
    import MirMashCompiler.*

    // Not totally unique, of course, but good enough.
    private val rndBase = UUID.randomUUID().hashCode().abs.toString
    private var rndCnt = -1L

    private def genLabel(): String = { rndCnt += 1; s"L${rndBase}_$rndCnt" }
    private def genVar(): String = { rndCnt += 1; s"V${rndBase}_$rndCnt" }

    /**
      *
      */
    class DefDecl:
        private var params = mutable.ArrayBuffer.empty[String]
        private var name: Option[String] = None

        def addParam(param: String): Unit = params += param
        def containsParam(param: String): Boolean = params.contains(param)
        def setName(name: String): Unit = this.name = name.?
        def getFullName: String = s"${name.get}(${params.mkString(",")})"
        def getName: Option[String] = name
        def getParams: Seq[String] = params.toSeq
    case class IfDecl(var thenBlock: AsmBlock, var elseBlock: Option[AsmBlock] = None)
    case class ListDecl(listVar: String, tmpVar: String)

    /**
      *
      * @param code
      * @param origin
      */
    private class FiniteStateMachine(code: String, origin: String) extends MirMashBaseListener:
        private var block = AsmBlock(origin, genLabel())
        private var lastBlock: Option[AsmBlock] = None
        private var scope = MirMashScope() // Initially global scope.
        private val defLut = mutable.HashMap.empty[String/* Fully qualified name. */, String/* Asm label. */]

        // State stacks.
        private val defStack = mutable.Stack.empty[DefDecl]
        private val whileStack = mutable.Stack.empty[String]
        private val forStack = mutable.Stack.empty[String]
        private val ifStack = mutable.Stack.empty[IfDecl]
        private val paramStack = mutable.Stack.empty[Int]
        private val listStack = mutable.Stack.empty[ListDecl]

        /**
          *
          */
        def getCompileModule: MirMashModule = MirMashModule(block.linkAsm, scope)

        /**
          *
          * @param s
          * @param kind
          */
        private def checkIdRegex(s: String, kind: String)(using ctx: PRC): Unit =
            if !VAR_REGEX.matcher(s).find() then
                throw error(s"${MirUtils.capitalize(kind)} name '$s' does not match regex: ${VAR_REGEX.pattern()}")

        private def parseStr(s: String)(using ctx: PRC): StrEntity =
            scope.getDecl(s) match
                case Some(decl) => decl.kind match
                    case VAL => StrEntity(StrKind.VAL, s, decl.?)
                    case VAR => StrEntity(StrKind.VAR, s, decl.?)
                    case ALS => StrEntity(StrKind.ALS, s, decl.?)
                    case FUN => StrEntity(StrKind.FUN, s, decl.?)
                    case NAT => StrEntity(StrKind.NAT, s, decl.?)
                case None =>
                    val num = s.replaceAll("_", "")
                    try
                        java.lang.Long.parseLong(num) // Try 'long'.
                        StrEntity(StrKind.NUM, s)
                    catch case _: NumberFormatException =>
                        try
                            java.lang.Double.parseDouble(num) // Try 'double'.
                            StrEntity(StrKind.NUM, s)
                        catch case _: NumberFormatException =>
                            StrEntity(StrKind.UNDEF, s)

        override def exitIfThen(using ctx: MMP.IfThenContext): Unit = ifStack.push(IfDecl(lastBlock.get))
        override def exitIfElse(using ctx: MMP.IfElseContext): Unit = ifStack.head.elseBlock = lastBlock
        override def exitIfDecl(using ctx: MMP.IfDeclContext): Unit =
            val ifDecl = ifStack.pop()
            val lbl = genLabel()
            val thenBlk = ifDecl.thenBlock
            thenBlk.add(s"jmp $lbl", null, "End of the 'then' branch.")
            ifDecl.elseBlock match
                case Some(elseBlk) =>
                    elseBlk.add(s"jmp $lbl", null, "End of the 'else' branch.")
                    block.add(s"ifjmp ${thenBlk.startLabel}, ${elseBlk.startLabel}")
                case None =>
                    block.add(s"ifjmp ${thenBlk.startLabel}, $lbl")
            block.add(null, lbl, null)

        override def exitAssignDecl(using ctx: MMP.AssignDeclContext): Unit =
            val strEnt = parseStr(ctx.STR().getText)
            val str = strEnt.str
            strEnt.kind match
                case StrKind.VAR => block.add(s"pop ${strEnt.decl.get.fqName}")
                case StrKind.UNDEF => throw error(s"Undefined variable: $str")
                case StrKind.NUM => throw error(s"Invalid assigment to: $str")
                case StrKind.VAL => throw error(s"Can't reassign immutable variable: $str")
                case _ => throw error(s"Can't assign to ${strEnt.decl.get.kindStr}: $str")

        override def enterWhileExprDecl(using ctx: MMP.WhileExprDeclContext): Unit =
            val lbl = genLabel()
            block.add(null, lbl)
            whileStack.push(lbl)

        override def exitWhileDecl(using ctx: MMP.WhileDeclContext): Unit =
            require(lastBlock.isDefined)
            require(whileStack.nonEmpty)
            val body = lastBlock.get
            body.add(s"jmp ${whileStack.pop}", null, "End of the loop body.")
            block.add(s"cjmp ${body.startLabel}")

        override def exitForExprDecl(using ctx: MMP.ForExprDeclContext): Unit =
            // 'for' loop spawns its own scope since it introduces the variable.
            scope = scope.subScope
            forStack.push(addVar(VAR, ctx.STR().getText, false).fqName)

        override def exitForDecl(using ctx: MMP.ForDeclContext): Unit =
            val loopVar = forStack.pop()
            val body = lastBlock.get
            val idxVar = genVar()
            val listVar = genVar()
            val startLbl = genLabel()
            val iterLbl = genLabel()
            val endLbl = genLabel()

            block.add(s"pop $listVar")
            block.add(s"let $idxVar, 0")
            block.add(s"push $idxVar", startLbl, "'for' loop start.")
            block.add(s"push $listVar")
            block.add(s"calln \"size\"")
            block.add(s"eq")
            block.add(s"cjmp $endLbl")
            block.add(s"push $listVar")
            block.add(s"push $idxVar")
            block.add(s"calln \"get\"")
            block.add(s"pop $loopVar")
            block.add(s"jmp ${body.startLabel}")
            block.add(s"incv $idxVar", iterLbl, null)
            block.add(s"jmp $startLbl", null, "'for' loop end.")
            block.add(null, endLbl, null)

            body.add(s"jmp $iterLbl")
            toParentScope()

        override def exitDefDecl(using ctx: MMP.DefDeclContext): Unit =
            val funDeclHldr = funStackHead()
            require(funDeclHldr.getName.isDefined)
            val strEnt = parseStr(funDeclHldr.getName.get)
            val str = strEnt.str
            strEnt.kind match
                case StrKind.UNDEF =>
                    val funDeclHldr = funStackHead()
                    val decl = new MirMashDefDecl(funDeclHldr.getParams.length, FUN, scope, str)
                    defStack.pop()
                    scope.addDecl(decl)
                    require(lastBlock.isDefined)
                    val body = lastBlock.get
                    // Add the function to the function LUT.
                    defLut += decl.fqName -> body.startLabel
                    // Make sure the last instruction is 'ret'.
                    body.last() match
                        case Some(asm) if asm.instruction.isDefined && asm.instruction.get == "ret" => ()
                        case _ => body.add("ret")
                case StrKind.NUM => throw error(s"Numeric cannot be a function name: $str")
                case _ =>
                    require(strEnt.decl.isDefined)
                    val decl = strEnt.decl.get
                    throw error(s"Function '$str' conflicts with already declared ${decl.kindStr}.")

        override def enterDefCall(using ctx: MMP.DefCallContext): Unit = paramStack.push(0)
        override def exitCallParam(using ctx: MMP.CallParamContext): Unit = paramStack.push(paramStack.pop() + 1)

        override def enterListExpr(using ctx: MMP.ListExprContext): Unit =
            val listVar = genVar()
            listStack.push(ListDecl(listVar, genVar()))
            block.add(null, null, "List expression start.")
            block.add("calln \"new_list\"")
            block.add(s"pop $listVar", null, "Variable holding the list.")
        override def exitListElem(using ctx: MMP.ListElemContext): Unit =
            val decl = listStack.head
            block.add(s"pop ${decl.tmpVar}")
            block.add(s"push ${decl.listVar}")
            block.add(s"push ${decl.tmpVar}")
            block.add(s"calln \"add\"")
        override def exitListExpr(using ctx: MMP.ListExprContext): Unit =
            val decl = listStack.pop()
            block.add(s"push ${decl.listVar}")
            block.add(null, null, "List expression stop.")

        override def exitDefCall(using ctx: MMP.DefCallContext): Unit =
            val strEnt = parseStr(ctx.STR().getText)
            val str = strEnt.str
            val paramCnt = paramStack.pop()
            def checkParams(declCnt: Int): Unit =
                if paramCnt < declCnt then throw error(s"Insufficient parameters when calling '$str(...)' function.")
                else if paramCnt > declCnt then throw error(s"Too many parameters when calling '$str(...)' function.")
            strEnt.kind match
                case StrKind.FUN =>
                    val decl = strEnt.decl.get.asInstanceOf[MirMashDefDecl]
                    checkParams(decl.paramCnt)
                    defLut.get(decl.fqName) match
                        case Some(lbl) =>
                            block.add(s"call $lbl", null, s"Calling '$str(...)' function.")
                            block.add("cpop", null, "Ignore return value.")
                        case None => throw error(s"Calling undefined function: $str")
                case StrKind.NAT =>
                    val decl = strEnt.decl.get.asInstanceOf[MirMashDefDecl]
                    checkParams(decl.paramCnt)
                    block.add(s"calln \"$str\"")
                    block.add("cpop", null, "Ignore return value.")
                case _ => throw error(s"Unknown function: $str")

        override def exitCallExpr(using ctx: MMP.CallExprContext): Unit =
            require(block.last().get.instruction.get == "cpop")
            block.removeLast() // Remove last 'cpop'.

        override def exitDefNameDecl(using ctx: MMP.DefNameDeclContext): Unit =
            val s = ctx.STR().getText
            checkIdRegex(s, "function")
            funStackHead().setName(s)

        override def exitNatDefDecl(using ctx: MMP.NatDefDeclContext): Unit =
            val s = ctx.STR().getText
            checkIdRegex(s, "native function")
            val strEnt = parseStr(s)
            val str = strEnt.str
            strEnt.kind match
                case StrKind.UNDEF =>
                    val funDeclHldr = funStackHead()
                    scope.addDecl(new MirMashDefDecl(funDeclHldr.getParams.length, NAT, scope, str))
                    defStack.pop()
                case StrKind.NUM => throw error(s"Numeric cannot be a native function name: $str")
                case _ =>
                    val decl = strEnt.decl
                    require(decl.isDefined)
                    throw error(s"Native function '$str' conflicts with already declared ${decl.get.kindStr}.")

        override def enterNatDefDecl(ctx: MMP.NatDefDeclContext): Unit = defStack.push(new DefDecl)
        override def enterDefDecl(ctx: MMP.DefDeclContext): Unit = defStack.push(new DefDecl)

        private def funStackHead(): DefDecl =
            defStack.headOption match
                case Some(x) => x
                case None => assert(false, "Empty function decl stack.")

        override def exitAliasDecl(using ctx: MirMashParser.AliasDeclContext): Unit =
            val s = ctx.STR().getText
            checkIdRegex(s, "alias")
            val strEnt = parseStr(s)
            val str = strEnt.str
            strEnt.kind match
                case StrKind.ALS => throw error(s"Duplicate alias declaration: $str")
                case StrKind.NUM => throw error(s"Invalid alias declaration: $str")
                case _ => scope.addDecl(new MirMashAliasDecl(MirUtils.dequote(MirUtils.dequote(ctx.qstring().getText)), ALS, scope, str))

        override def exitFunParamList(using ctx: MMP.FunParamListContext): Unit =
            val s = ctx.STR().getText
            checkIdRegex(s, "function parameter")
            val strEnt = parseStr(s)
            val str = strEnt.str
            strEnt.kind match
                case StrKind.UNDEF =>
                    val funDeclHldr = funStackHead()
                    if funDeclHldr.containsParam(str) then throw error(s"Duplicate function parameter: $str")
                    else funDeclHldr.addParam(str)
                case StrKind.NUM => throw error(s"Numeric cannot be a function parameter: $str")
                case _ =>
                    require(strEnt.decl.isDefined)
                    val decl = strEnt.decl.get
                    throw error(s"Function parameter '$str' overrides existing ${decl.kindStr}.")

        override def enterCompoundExpr(using ctx: MMP.CompoundExprContext): Unit =
            scope = scope.subScope
            block = block.subBlock(genLabel())
            defStack.headOption match
                case Some(funDeclHldr) =>
                    block.add(null, null, s"Function '${funDeclHldr.getFullName}'.")
                    val decls = funDeclHldr.getParams.map(MirMashDecl(VAL, scope, _))
                    // Add parameters to the function scope.
                    decls.foreach(scope.addDecl)
                    // Pop local variables for each parameter (note reverse order).
                    decls.reverse.foreach(decl => block.add(s"pop ${decl.fqName}"))
                case None => () // No-op.

        private def toParentScope(): Unit = scope = scope.parent match
            case Some(s) => s
            case None => assert(false, "Attempt to exit global scope.")

        override def exitCompoundExpr(using ctx: MMP.CompoundExprContext): Unit =
            toParentScope()
            lastBlock = block.?
            block = block.parent match
                case Some(p) => p
                case None => assert(false, "Attempt to exit global block.")

        override def exitMash(using ctx: MMP.MashContext): Unit =
            require(defStack.isEmpty, "'def' stack is not empty.")
            require(whileStack.isEmpty, "'while' stack is not empty.")
            require(forStack.isEmpty, "'for' stack is not empty.")
            require(ifStack.isEmpty, "'if' stack is not empty.")
            require(paramStack.isEmpty, "'param' stack is not empty.")
            require(listStack.isEmpty, "'list' stack is not empty.")
            require(scope.isGlobal)
            block.add("exit")

        override def enterMash(using ctx: MMP.MashContext): Unit =
            val hdr = s"Generated by mash compiler ver. $VER on ${MirClock.formatNowTimeDate()}"
            block.add(null, null, hdr)
            block.add(null, null, "-" * hdr.length)

        override def exitReturnDecl(using ctx: MMP.ReturnDeclContext): Unit =
            if defStack.isEmpty then throw error(s"'return' can only be used inside of the function body.")
            block.add("ret")

        override def exitUnaryExpr(using ctx: MMP.UnaryExprContext): Unit =
            if ctx.MINUS() != null then block.add("neg")
            else if ctx.TILDA() != null then block.add("not")
            else  // 'EXCL'.
                val thenLbl = genLabel()
                val elseLbl = genLabel()
                val exitLbl = genLabel()
                block.add("push 0", null, "Start of '!' code.")
                block.add("eq")
                block.add(s"ifjmp $thenLbl, $elseLbl")
                block.add("push 1", thenLbl)
                block.add(s"jmp $exitLbl")
                block.add("push 0", elseLbl)
                block.add("nop", exitLbl)
        override def exitAndOrExpr(using ctx: MMP.AndOrExprContext): Unit =
            var v = genVar()
            block.add(s"pop $v")
            block.add("calln \"bool_sigmoid\"")
            block.add(s"push $v")
            block.add("calln \"bool_sigmoid\"")
            block.add(if ctx.AND() != null then "and" else "or")
        override def exitEqNeqExpr(using ctx: MMP.EqNeqExprContext): Unit = block.add(if ctx.EQ() != null then "eq" else "neq")
        override def exitPlusMinusExpr(using ctx: MMP.PlusMinusExprContext): Unit = block.add(if ctx.PLUS() != null then "add" else "sub")
        override def exitCompExpr(using ctx: MMP.CompExprContext): Unit =
            if ctx.LT() != null then block.add("lt")
            else if ctx.LTEQ() != null then block.add("lte")
            else if ctx.GT() != null then block.add("gt")
            else block.add("gte")
        override def exitMultDivModExpr(using ctx: MMP.MultDivModExprContext): Unit =
            if ctx.MOD() != null then block.add("mod")
            else if ctx.RIGHT_RIGHT() != null then block.add("sar")
            else if ctx.LEFT_LEFT() != null then block.add("sal")
            else if ctx.RIGHT_RIGHT_RIGHT() != null then block.add("shr")
            else if ctx.VERT() != null then block.add("or")
            else if ctx.AMP() != null then block.add("and")
            else if ctx.XOR() != null then block.add("xor")
            else if ctx.MULT() != null then block.add("mul")
            else block.add("div")

        /**
          * Gets dequoted string while processing variable expansion for double-quoted strings.
          *
          * @param qs Double or single quoted string.
          */
        private def dequote(qs: String)(using ctx: PRC): String =
            // TODO: handle variable expansion for double-quoted strings.
            if qs.startsWith("'") && qs.endsWith("'") then MirUtils.dequote(qs)
            else if qs.startsWith("\"") && qs.endsWith("\"") then
                val s = MirUtils.dequote(qs)
                val m = STR_VAR_REGEX.matcher(s)
                s
            else
                throw error(s"Uneven quotes in: $qs")

        override def exitAtom(using ctx: MMP.AtomContext): Unit =
            if ctx.NULL() != null then block.add("push null")
            else if ctx.BOOL() != null then block.add(s"push ${if ctx.getText == "true" then 1 else 0}")
            else if ctx.qstring() != null then block.add(s"push \"${dequote(ctx.qstring().getText)}\"")
            else
                require(ctx.STR() != null)
                val strEnt = parseStr(ctx.STR().getText)
                val str = strEnt.str
                strEnt.kind match
                    case StrKind.VAL | StrKind.VAR => block.add(s"push ${strEnt.decl.get.fqName}")
                    case StrKind.NUM => block.add(s"push $str")
                    case _ => throw error(s"Unknown identifier: $str")

        override def exitUnsetDecl(using ctx: MMP.UnsetDeclContext): Unit =
            val strEnt = parseStr(ctx.STR().getText)
            val name = strEnt.str
            strEnt.kind match
                case StrKind.VAR | StrKind.VAL | StrKind.ALS => scope.removeDecl(strEnt.decl.get)
                case _ => throw error(s"Only existing variables or aliases can be unset: $name")

        private def addVar(kind: MirMashDeclarationKind, s: String, pop: Boolean = true)(using ctx: PRC): MirMashDecl =
            checkIdRegex(s, "variable")
            val strEnt = parseStr(s)
            val name = strEnt.str
            strEnt.kind match
                case StrKind.UNDEF =>
                    val decl = MirMashDecl(kind, scope, name)
                    if pop then block.add(s"pop ${decl.fqName}")
                    scope.addDecl(decl)
                    decl
                case _ => throw error(s"Duplicate declaration: $name")

        override def exitValDecl(using ctx: MMP.ValDeclContext): Unit = addVar(VAL, ctx.STR().getText)
        override def exitVarDecl(using ctx: MMP.VarDeclContext): Unit = addVar(VAR, ctx.STR().getText)

        /**
          *
          * @param errMsg
          * @param ctx
          */
        private def error(errMsg: String)(using ctx: PRC): CPException =
            val tok = ctx.start
            new MirMashException(mkErrorMessage(errMsg, tok.getLine, tok.getCharPositionInLine, code, origin))

    /**
      *
      * @param msg
      * @param line
      * @param charPos
      * @param code
      * @param origin Code origin (file name, etc.).
      */
    //noinspection DuplicatedCode
    private def mkErrorMessage(
        msg: String,
        line: Int, // 1, 2, ...
        charPos: Int, // 0, 1, 2, ...
        code: String,
        origin: String): String =
        def errorPointer(in: String, charPos: Int): (String, String) =
            val in0 = in.strip()
            if in0.isEmpty || charPos < 0 then "<empty>" -> "<empty>"
            else
                var charPos0 = charPos - (in.length - in.stripLeading().length)
                if msg.contains("extraneous input") || msg.contains("mismatched input") then charPos0 += 1
                val len = in0.length
                val pos = Math.min(Math.max(0, charPos0), len)

                if pos == len then
                    s"${"-" * len}^" -> in0
                else
                    val dash = "-" * len
                    val ptrStr = s"${dash.substring(0, pos)}^${dash.substring(pos + 1)}"
                    ptrStr -> in0

        val codeLine = code.split("\n")(line - 1)
        val (ptrStr, origStr) = errorPointer(codeLine, charPos)
        var aMsg = MirUtils.decapitalize(msg) match
            case s: String if s.last == '.' => s
            case s: String => s"$s."

        // Cut the long syntax error.
        aMsg = aMsg.indexOf("expecting") match
            case idx if idx >= 0 => s"${aMsg.substring(0, idx).trim}."
            case _ => aMsg

        s"""# Mesh syntax error in '$origin' at line $line - $aMsg
            #   |-- Line:  $origStr
            #   +-- Error: $ptrStr
            #""".stripMargin('#')

    /**
      * Custom error handler.
      *
      * @param code
      */
    class CompilerErrorListener(code: String) extends BaseErrorListener:
        /**
          *
          * @param recog
          * @param badSymbol
          * @param line
          * @param charPos
          * @param msg
          * @param e
          */
        override def syntaxError(
            recog: Recognizer[_, _],
            badSymbol: scala.Any,
            line: Int, // 1, 2, ...
            charPos: Int, // 1, 2, ...
            msg: String,
            e: RecognitionException): Unit =
            throw new MirMashException(mkErrorMessage(msg, line, charPos - 1, code, recog.getInputStream.getSourceName))

    /**
      *
      * @param code
      * @param origin
      */
    //noinspection DuplicatedCode
    private def antlr4Setup(code: String, origin: String): (FiniteStateMachine, MirMashParser) =
        val lexer = new MirMashLexer(CharStreams.fromString(code, origin))
        val parser = new MirMashParser(new CommonTokenStream(lexer))

        // Set custom error handlers.
        lexer.removeErrorListeners()
        parser.removeErrorListeners()
        lexer.addErrorListener(new CompilerErrorListener(code))
        parser.addErrorListener(new CompilerErrorListener(code))

        // State automata + it's parser.
        new FiniteStateMachine(code, origin) -> parser

    /**
      *
      * @param code
      * @param origin
      * @throws MirMashException
      */
    def compileToAsm(code: String, origin: String): MirMashModule =
        val (fsm, parser) = antlr4Setup(code, origin)

        // Parse the input IDL and walk the built AST.
        (new ParseTreeWalker).walk(fsm, parser.mash())

        fsm.getCompileModule

    /**
      *
      * @param code Mash source code to compile.
      * @param origin Origin of the source code.
      * @throws MirMashException Thrown in case of syntax or runtime errors.
      */
    def compile(code: String, origin: String): MirMashExecutable =
        MirMashExecutable(compileToAsm(code, origin).asm.map(_.toAsmString(true)).mkString("\n"), origin)
