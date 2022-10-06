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

import MirMashParser as MMP
import ParserRuleContext as PRC

import java.util.UUID
import scala.collection.mutable

/**
  * A container for translated assembler code and global declarations.
  *
  * @param asm Assembler instructions.
  * @param global Global scope.
  */
case class MirMashModule(asm: IndexedSeq[MirMashAsm], global: MirMashScope):
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
    val fqName: String = s"${scope.namespace}_$name"
    val kindStr: String = kind match
        case VAR => "variable"
        case VAL => "value"
        case FUN => "function"
        case ALS => "alias"
        case NAT => "native function"

/**
  *
  * @param params Function declaration site parameters.
  * @param kind Type of the declaration.
  * @param scope Declaration scope.
  * @param name Declaration name.
  */
class MirMashDefDecl(val params: Seq[String], kind: MirMashDeclarationKind, scope: MirMashScope, name: String) extends MirMashDecl(kind, scope, name)

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

    val namespace: String = {
        def z(s: String): String = "$" + s + "_"
        var x = parent
        var p = z(id)
        while (x.isDefined)
            val xx = x.get
            p += z(xx.id)
            x = xx.parent
        p
    }

    def isGlobal: Boolean = parent.isEmpty
    def hasDecl(name: String): Boolean = decls.contains(name)
    def addDecl(decl: MirMashDecl): Unit = decls += decl.name -> decl
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

    def getAsm: Seq[MirMashAsm] =
        val code = mutable.ArrayBuffer.empty[MirMashAsm]
        code.addAll(asm) // Add itself.
        children.foreach(child => code.addAll(child.getAsm)) // Add all of its children recursively.
        code.toSeq
    def subBlock(startLbl: String): AsmBlock =
        val block = AsmBlock(origin, startLbl, this.?)
        children += block
        block
    // NOTE: comment without leading ';'.
    def add(instr: String, lbl: String = null, cmt: String = null)(using ctx: PRC): Unit = asm.append(mkAsm(lbl.?, instr.?, cmt.?))

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

    // Not very unique, of course - but good enough.
    private val lblBase = UUID.randomUUID().hashCode().abs.toString
    private var lblCnt = 0L

    /**
      *
      */
    private def genLabel(): String =
        val lbl = s"L${lblBase}_$lblCnt"
        lblCnt += 1
        lbl

    /**
      *
      * @param code
      * @param origin
      */
    private class FiniteStateMachine(code: String, origin: String) extends MirMashBaseListener:
        private var block = AsmBlock(origin, genLabel())
        private var lastBlock: Option[AsmBlock] = None
        private var scope = MirMashScope() // Initially global scope.
        private var funParams: mutable.ArrayBuffer[String] = _
        private val funLut = mutable.HashMap.empty[String/* Fully qualified name. */, String/* Asm label. */]

        def clearFunParams(): Unit = funParams = mutable.ArrayBuffer.empty[String]

        /**
          *
          */
        def getCompileModule: MirMashModule =
            require(scope.isGlobal)
            MirMashModule(block.getAsm.toIndexedSeq, scope)

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

        override def exitDefDecl(using ctx: MirMashParser.DefDeclContext): Unit =
            val strEnt = parseStr(ctx.STR().getText)
            val str = strEnt.str
            strEnt.kind match
                case StrKind.UNDEF =>
                    val decl = new MirMashDefDecl(funParams.toSeq, FUN, scope, str)
                    scope.addDecl(decl)
                    require(lastBlock.isDefined)
                    val body = lastBlock.get
                    // Add the function to the function LUT.
                    funLut += decl.fqName -> body.startLabel
                    // Make sure the last instruction is 'ret'.
                    val lastInstr = body.getAsm.last.instruction
                    if lastInstr.isEmpty || lastInstr.get != "ret" then body.add("ret")
                case StrKind.NUM => throw error(s"Numeric cannot be a function name: $str")
                case _ =>
                    require(strEnt.decl.isDefined)
                    val decl = strEnt.decl.get
                    throw error(s"Function name ($str) conflicts with existing ${decl.kindStr}.")

        private def call(name: String, isExpr: Boolean)(using ctx: PRC): Unit =
            val strEnt = parseStr(name)
            val str = strEnt.str
            strEnt.kind match
                case StrKind.FUN =>
                    val decl = strEnt.decl.get
                    funLut.get(decl.fqName) match
                        case Some(lbl) =>
                            block.add(s"call $lbl", null, s"Calling '$str' function.")
                            if !isExpr then block.add("cpop")
                        case None => throw error(s"Calling undefined function: $str")
                case StrKind.NAT =>
                    block.add(s"calln \"$str\"")
                    if !isExpr then block.add("cpop")
                case _ => throw error(s"Calling unknown function: $str")
        override def exitDefCall(using ctx: MMP.DefCallContext): Unit = call(ctx.STR().getText, false)
        override def exitCallExpr(using ctx: MMP.CallExprContext): Unit = call(ctx.defCall().STR().getText, true)

        override def exitNatDefDecl(using ctx: MMP.NatDefDeclContext): Unit =
            val strEnt = parseStr(ctx.STR().getText)
            val str = strEnt.str
            strEnt.kind match
                case StrKind.UNDEF => scope.addDecl(new MirMashDefDecl(funParams.toSeq, NAT, scope, str))
                case StrKind.NUM => throw error(s"Numeric cannot be a native function name: $str")
                case _ =>
                    require(strEnt.decl.isDefined)
                    val decl = strEnt.decl.get
                    throw error(s"Native function name ($str) conflicts with existing ${decl.kindStr}.")
        override def enterNatDefDecl(ctx: MMP.NatDefDeclContext): Unit = clearFunParams()
        override def enterDefDecl(ctx: MMP.DefDeclContext): Unit = clearFunParams()
        override def exitFunParamList(using ctx: MMP.FunParamListContext): Unit =
            val strEnt = parseStr(ctx.STR().getText)
            val str = strEnt.str
            strEnt.kind match
                case StrKind.UNDEF =>
                    if funParams.contains(str) then throw error(s"Duplicate function parameter: $str")
                    else funParams += str
                case StrKind.NUM => throw error(s"Numeric cannot be a function parameter: $str")
                case _ =>
                    require(strEnt.decl.isDefined)
                    val decl = strEnt.decl.get
                    throw error(s"Function parameter ($str) overrides existing ${decl.kindStr}.")

        override def enterCompoundExpr(using ctx: MMP.CompoundExprContext): Unit =
            scope = scope.subScope
            block = block.subBlock(genLabel())
            val decls = funParams.map(MirMashDecl(VAL, scope, _))
            // Add parameters to the function scope.
            decls.foreach(scope.addDecl)
            // Pop local variables for each parameter.
            decls.reverse.foreach(decl => block.add(s"pop ${decl.fqName}"))
        override def exitCompoundExpr(using ctx: MMP.CompoundExprContext): Unit =
            scope = scope.parent match
                case Some(s) => s
                case None => assert(false, "Exit global scope.")
            lastBlock = block.?
            block = block.parent match
                case Some(p) => p
                case None => assert(false, "Exit global block.")
            clearFunParams()

        override def exitMash(using ctx: MMP.MashContext): Unit = block.add("exit")
        override def enterMash(using ctx: MMP.MashContext): Unit =
            val hdr = s"Generated by mash compiler ver. $VER on ${MirClock.formatNowTimeDate()}"
            block.add(null, null, hdr)
            block.add(null, null, "-" * hdr.length)

        override def exitReturnDecl(using ctx: MMP.ReturnDeclContext): Unit = block.add("ret")
        override def exitUnaryExpr(using ctx: MMP.UnaryExprContext): Unit = block.add(if ctx.MINUS() != null then "neg" else "not")
        override def exitAndOrExpr(using ctx: MMP.AndOrExprContext): Unit = block.add(if ctx.AND() != null then "and" else "or")
        override def exitEqNeqExpr(using ctx: MMP.EqNeqExprContext): Unit = block.add(if ctx.EQ() != null then "eq" else "neq")
        override def exitPlusMinusExpr(using ctx: MMP.PlusMinusExprContext): Unit = block.add(if ctx.PLUS() != null then "add" else "sub")
        override def exitCompExpr(using ctx: MMP.CompExprContext): Unit =
            if ctx.LT() != null then block.add("lt")
            else if ctx.LTEQ() != null then block.add("lte")
            else if ctx.GT() != null then block.add("gt")
            else block.add("gte")
        override def exitMultDivModExpr(using ctx: MMP.MultDivModExprContext): Unit =
            if ctx.MOD() != null then block.add("mod")
            else if ctx.MULT() != null then block.add("mul")
            else block.add("div")
        override def exitAtom(using ctx: MMP.AtomContext): Unit =
            if ctx.NULL() != null then block.add("push null")
            else if ctx.BOOL() != null then block.add(s"push ${if ctx.getText == "true" then 1 else 0}")
            else if ctx.qstring() != null then
                val qStr = ctx.qstring()
                if qStr.SQSTRING() != null then
                    val s = MirUtils.dequote(qStr.SQSTRING().getText)
                    block.add(s"push \"$s\"")
                else if qStr.DQSTRING() != null then
                    // TODO: handle variable expansion for double-quoted strings.
                    val s = MirUtils.dequote(qStr.DQSTRING().getText)
                    block.add(s"push \"$s\"")
            else
                require(ctx.STR() != null)
                val strEnt = parseStr(ctx.STR().getText)
                val str = strEnt.str
                strEnt.kind match
                    case StrKind.VAL | StrKind.VAR => block.add(s"push ${strEnt.decl.get.fqName}")
                    case StrKind.NUM => block.add(s"push $str")
                    case _ => throw error(s"Unknown identifier: $str")

        private def addVar(kind: MirMashDeclarationKind, s: String)(using ctx: PRC): Unit =
            val strEnt = parseStr(s)
            val name = strEnt.str
            strEnt.kind match
                case StrKind.UNDEF =>
                    val decl = MirMashDecl(kind, scope, name)
                    block.add(s"pop ${decl.fqName}")
                    scope.addDecl(decl)
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
