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

import org.cosplay.impl.CPUtils
import org.cosplay.*

import org.cosplay.games.mir.os.progs.mash.compiler.antlr4.*
import org.antlr.v4.runtime.tree.*
import org.antlr.v4.runtime.*

/**
  *
  */
class CPMirMashCompiler:
    /**
      *
      */
    private case class ErrorHolder(
        ptrStr: String,
        origStr: String
    )

    /**
      *
      * @param code
      * @param origin
      */
    private class FiniteStateMachine(code: String, origin: String) extends CPMirMashBaseListener

    /**
      *
      * @param in
      * @param charPos
      */
    private def mkErrorHolder(in: String, charPos: Int): ErrorHolder =
        val in0 = in.strip()
        if in0.isEmpty || charPos < 0 then ErrorHolder("<empty>", "<empty>")
        else
            val charPos0 = charPos - (in.length - in.stripLeading().length)
            val pos = Math.max(0, charPos0)
            val dash = "-" * in0.length
            val ptrStr = s"${dash.substring(0, pos)}^${dash.substring(pos + 1)}"
            val origStr = s"${in0.substring(0, pos)}${in0.charAt(pos)}${in0.substring(pos + 1)}"

            ErrorHolder(ptrStr, origStr)

    /**
      *
      * @param kind
      * @param msg
      * @param line
      * @param charPos
      * @param code
      * @param origin Code origin (file name, etc.).
      * @return
      */
    private def mkError(
        kind: String,
        msg: String,
        line: Int, // 1, 2, ...
        charPos: Int, // 0, 1, 2, ...
        code: String,
        origin: String): String =
        val codeLine = code.split("\n")(line - 1)
        val hold = mkErrorHolder(codeLine, charPos)
        var aMsg = CPUtils.decapitalize(msg) match
            case s: String if s.last == '.' => s
            case s: String => s"$s."

        // Cut the long syntax error.
        aMsg = aMsg.indexOf("expecting") match
            case idx if idx >= 0 => s"${aMsg.substring(0, idx).trim}."
            case _ => aMsg

        s"""# Mash $kind error in '$origin' at line $line - $aMsg
            #   |-- Line:  ${hold.origStr}
            #   +-- Error: ${hold.ptrStr}
            #""".stripMargin('#')

    /**
      *
      * @param msg
      * @param line
      * @param charPos
      * @param code
      * @param origin Code origin (file name, etc.).
      * @return
      */
    private def mkSyntaxError(
        msg: String,
        line: Int, // 1, 2, ...
        charPos: Int, // 0, 1, 2, ...
        code: String,
        origin: String): String = mkError("syntax", msg, line, charPos, code, origin)

    /**
      *
      * @param msg
      * @param line
      * @param charPos
      * @param code
      * @param origin Code origin (file name, etc.).
      * @return
      */
    private def mkRuntimeError(
        msg: String,
        line: Int, // 1, 2, ...
        charPos: Int, // 0, 1, 2, ...
        code: String,
        origin: String): String = mkError("runtime", msg, line, charPos, code, origin)

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
            throw new CPException(mkSyntaxError(msg, line, charPos - 1, code, recog.getInputStream.getSourceName))

    /**
      *
      * @param code
      * @param origin
      */
    private def antlr4Setup(code: String, origin: String): (FiniteStateMachine, CPMirMashParser) =
        val lexer = new CPMirMashLexer(CharStreams.fromString(code, origin))
        val parser = new CPMirMashParser(new CommonTokenStream(lexer))

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
    def compile(code: String, origin: String): CPMirMashCompilerResult =
        val (fsm, parser) = antlr4Setup(code, origin)

        // Parse the input IDL and walk the built AST.
        (new ParseTreeWalker).walk(fsm, parser.mash())

        null // TODO

