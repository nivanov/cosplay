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
import os.progs.mash.compiler.antlr4.*

import org.antlr.v4.runtime.tree.*
import org.antlr.v4.runtime.*

import java.util.UUID
import scala.collection.mutable

/**
  *
  */
class MirMashCompiler:
    /**
      *
      */
    enum DeclKind:
        case VAR, DEF, VAL, ALS

    /**
      * A declaration of some kind.
      *
      * @param kind
      * @param name
      */
    case class Decl(kind: DeclKind, name: String)

    /**
      *
      */
    enum StrKind:
        case NUM, VAR, VAL, ALS, DEF, ANY

    /**
      *
      * @param kind
      * @param str
      */
    case class StrEntity(kind: StrKind, str: String)

    /**
      * A container for assembler code and global declarations.
      */
    case class CompiledModule(asmCode: IndexedSeq[AsmCode], globals: Scope):
        require(globals.isGlobal)

    /**
      * A namespace.
      *
      * @param parent
      */
    case class Scope(parent: Option[Scope] = None):
        private val decls = mutable.HashMap.empty[String, Decl]

        def isGlobal: Boolean = parent.isEmpty
        def hasDecl(name: String): Boolean = decls.contains(name)
        def addDecl(decl: Decl): Unit =
            require(!decls.contains(decl.name))
            decls += decl.name -> decl
        def getDecl(name: String): Option[Decl] = decls.get(name)
        def getDecls: Seq[Decl] = decls.values.toSeq

    /**
      *
      * @param label
      * @param instr
      * @param comment
      */
    case class AsmCode(label: Option[String], instr: Option[String], comment: Option[String]):
        /**
          *
          */
        def toAsmString: String =
            val lbl = label match
                case Some(s) => s"$s:"
                case None => ""
            val ins = instr.getOrElse("")
            val cmt = comment match
                case Some(s) => s"; $s"
                case None => ""
            String.format("%1$-10s %2$-15s %3$s", lbl, ins, cmt)

    // Not very unique, of course - but good enough.
    private val lblBase = UUID.randomUUID().hashCode().toString
    private var lblCnt = 0L

    /**
      *
      */
    private def genLabel(): String =
        val lbl = s"L$lblBase-$lblCnt"
        lblCnt += 1
        lbl

    /**
      *
      * @param code
      * @param origin
      */
    private class FiniteStateMachine(code: String, origin: String) extends MirMashBaseListener:
        private val asm = mutable.ArrayBuffer.empty[AsmCode]
        private val scope = Scope() // Initially global scope.

        /**
          *
          */
        def getCompileModule: CompiledModule =
            require(scope.isGlobal)
            CompiledModule(asm.toIndexedSeq, scope)

        private def addInstr(instr: String): Unit = asm += AsmCode(None, Option(instr), None)
        private def addLabeledInstr(label: String, instr: String): Unit = asm += AsmCode(Option(label), Option(instr), None)
        private def parseStr(s: String): StrEntity =
            scope.getDecl(s) match
                case Some(decl) => decl.kind match
                    case DeclKind.VAL => StrEntity(StrKind.VAL, s)
                    case DeclKind.VAR => StrEntity(StrKind.VAR, s)
                    case DeclKind.ALS => StrEntity(StrKind.ALS, s)
                    case DeclKind.DEF => StrEntity(StrKind.DEF, s)
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
                            StrEntity(StrKind.ANY, s)

        override def exitAtom(ctx: MirMashParser.AtomContext): Unit =
            given ParserRuleContext = ctx
            if ctx.NULL() != null then addInstr("push null")
            else if ctx.BOOL() != null then addInstr(s"push ${if ctx.getText == "true" then 1 else 0}")
            else if ctx.qstring() != null then
                val qStr = ctx.qstring()
                if qStr.SQSTRING() != null then
                    val s = MirUtils.dequote(qStr.SQSTRING().getText)
                    addInstr(s"push \"$s\"")
                else if qStr.DQSTRING() != null then
                    // TODO: handle variable expansion for double-quoted strings.
                    val s = MirUtils.dequote(qStr.DQSTRING().getText)
                    addInstr(s"push \"$s\"")
            else
                require(ctx.STR() != null)
                val strEnt = parseStr(ctx.STR().getText)
                strEnt.kind match
                    case StrKind.VAL | StrKind.VAR | StrKind.NUM => addInstr(s"push ${strEnt.str}")
                    case _ => throw error("Unexpected expression")

        private def addVar(kind: DeclKind, s: String)(using ctx: ParserRuleContext): Unit =
            val strEnt = parseStr(s)
            val name = strEnt.str
            strEnt.kind match
                case StrKind.ANY =>
                    addInstr(s"pop $name")
                    scope.addDecl(Decl(kind, name))
                case StrKind.ALS | StrKind.DEF | StrKind.VAL | StrKind.VAR => throw error(s"Duplicate variable declaration: $name")
                case StrKind.NUM => throw error("Unexpected expression")

        override def exitValDecl(ctx: MirMashParser.ValDeclContext): Unit =
            given ParserRuleContext = ctx
            addVar(DeclKind.VAL, ctx.STR().getText)
        override def exitVarDecl(ctx: MirMashParser.VarDeclContext): Unit =
            given ParserRuleContext = ctx
            addVar(DeclKind.VAR, ctx.STR().getText)

        /**
          *
          * @param errMsg
          * @param ctx
          */
        private def error(errMsg: String)(using ctx: ParserRuleContext): CPException =
            val tok = ctx.start
            new CPException(mkErrorMessage(errMsg, tok.getLine, tok.getCharPositionInLine, code, origin))

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
            throw new CPException(mkErrorMessage(msg, line, charPos - 1, code, recog.getInputStream.getSourceName))

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
      */
    def compile(code: String, origin: String): CompiledModule =
        val (fsm, parser) = antlr4Setup(code, origin)

        // Parse the input IDL and walk the built AST.
        (new ParseTreeWalker).walk(fsm, parser.mash())

        fsm.getCompileModule

