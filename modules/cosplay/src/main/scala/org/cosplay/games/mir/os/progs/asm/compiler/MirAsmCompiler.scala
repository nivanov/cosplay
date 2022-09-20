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

import org.cosplay.impl.CPUtils
import org.cosplay.*
import games.mir.*
import os.progs.asm.compiler.antlr4.*
import os.progs.asm.compiler.MirAsmInstruction.*
import MirAsmExecutable.*
import org.antlr.v4.runtime.tree.*
import org.antlr.v4.runtime.*

import scala.collection.mutable
import scala.jdk.CollectionConverters.*

/**
  *
  */
class MirAsmCompiler:
    /**
      *
      * @param msg
      * @param line
      * @param charPos
      * @param code
      * @param origin
      */
    private def mkErrorMessage(
        msg: String,
        line: Int, // 1, 2, ...
        charPos: Int, // 0, 1, 2, ...
        code: String,
        origin: String
    ): String =
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
        var aMsg = CPUtils.decapitalize(msg) match
            case s: String if s.last == '.' => s
            case s: String => s"$s."

        // Cut the long syntax error.
        aMsg = aMsg.indexOf("expecting") match
            case idx if idx >= 0 => s"${aMsg.substring(0, idx).trim}."
            case _ => aMsg

        s"""#Syntax error in '$origin' at line $line - $aMsg
            #  |-- Line:  $origStr
            #  +-- Error: $ptrStr
            #""".stripMargin('#')
    /**
      *
      * @param code
      * @param origin
      */
    private class FiniteStateMachine(code: String, origin: String) extends MirAsmBaseListener:
        private val instrs = mutable.ArrayBuffer.empty[MirAsmInstruction]
        private val labels = mutable.HashSet.empty[String]
        private var params: mutable.ArrayBuffer[Param] = _
        private var dbg: MirAsmDebug = _

        override def enterInst(ctx: MirAsmParser.InstContext): Unit =
            params = mutable.ArrayBuffer.empty[Param]
            dbg = null

        override def exitDbg(using ctx: MirAsmParser.DbgContext): Unit =
            try
                val line = java.lang.Integer.parseInt(ctx.INT().get(0).getText)
                val pos = java.lang.Integer.parseInt(ctx.INT().get(1).getText)
                val origin = MirUtils.dequote(ctx.DQSTRING().getText)
                dbg = MirAsmDebug(line, pos, origin)
            catch case _: NumberFormatException => throw error(s"Invalid debug information format in: ${ctx.getText}")

        override def exitInst(using ctx: MirAsmParser.InstContext): Unit =
            val name = ctx.INSRT_NAME().getSymbol.getText
            val label =
                if ctx.label() == null
                then
                    None
                else
                    val txt = ctx.label().ID().getText
                    if labels.contains(txt) then
                        throw error(s"Duplicate label: $txt")
                    else
                        labels += txt
                        Option(txt)
            val line = ctx.start.getLine

            instrs += new MirAsmInstruction(label, line, name, params.toSeq, Option(dbg))

        override def exitParam(ctx: MirAsmParser.ParamContext): Unit =
            require(params != null)
            given ParserRuleContext = ctx
            val txt = ctx.getText
            if ctx.NULL() != null then params += NullParam
            else if ctx.DQSTRING() != null then params += StringParam(MirUtils.dequote(txt))
            else if ctx.ID() != null then params += IdParam(txt)
            else // Integer or real.
                val num = txt.replaceAll("_", "")

                try params += LongParam(java.lang.Long.parseLong(num)) // Try 'long'.
                catch case _: NumberFormatException =>
                    try params += DoubleParam(java.lang.Double.parseDouble(num)) // Try 'double'.
                    catch case _: NumberFormatException => throw error(s"Invalid numeric value: $txt")

        /**
          *
          */
        def getInstructions: Seq[MirAsmInstruction] = instrs.toSeq

        /**
          *
          * @param errMsg
          * @param ctx
          */
        private def error(errMsg: String)(using ctx: ParserRuleContext): MirAsmException =
            val tok = ctx.start
            new MirAsmException(mkErrorMessage(errMsg, tok.getLine, tok.getCharPositionInLine, code, origin), Option(dbg))

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
            throw new MirAsmException(mkErrorMessage(msg, line, charPos - 1, code, recog.getInputStream.getSourceName))

    /**
      *
      * @param code
      * @param origin
      */
    private def antlr4Setup(code: String, origin: String): (FiniteStateMachine, MirAsmParser) =
        val lexer = new MirAsmLexer(CharStreams.fromString(code, origin))
        val parser = new MirAsmParser(new CommonTokenStream(lexer))

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
    def compile(code: String, origin: String): MirAsmExecutable =
        val (fsm, parser) = antlr4Setup(code, origin)

        // Parse the input assembler and walk the built AST.
        (new ParseTreeWalker).walk(fsm, parser.asm())

        MirAsmExecutable(fsm.getInstructions)

