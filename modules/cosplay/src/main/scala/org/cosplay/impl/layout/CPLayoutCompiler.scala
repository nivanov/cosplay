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

package org.cosplay.impl.layout

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
import org.antlr.v4.runtime.tree.*
import org.antlr.v4.runtime.*
import org.cosplay.impl.layout.antlr4.*
import scala.util.*
import scala.collection.mutable

object CPLayoutCompiler:
    private class FiniteStateMachine extends CPLayoutBaseListener:
        private val specs = mutable.ArrayBuffer.empty[CPLayoutSpec]
        private var spec = CPLayoutSpec(id = null)

        override def enterDecl(ctx: CPLayoutParser.DeclContext): Unit =
            val id = ctx.ID().getText
            if specs.exists(_.id == id) then throw CPException(s"Duplicate layout sprite ID: $id")
            spec = CPLayoutSpec(id)
        override def exitDecl(ctx: CPLayoutParser.DeclContext): Unit = specs += spec
        override def exitOffItem(ctx: CPLayoutParser.OffItemContext): Unit =
            spec.off = CPInt2(
                ctx.getChild(3).getText.toInt,
                ctx.getChild(5).getText.toInt
            )
        override def exitYItem(ctx: CPLayoutParser.YItemContext): Unit =
            val rel = if ctx.ID() == null then None else ctx.ID().getText.?
            val dir = ctx.getChild(2).getText match
                case "above" => CPLayoutDirection.ABOVE
                case "same" => CPLayoutDirection.SAME
                case "top" => CPLayoutDirection.TOP
                case "bottom" => CPLayoutDirection.BOTTOM
                case "below" => CPLayoutDirection.BELOW
                case "center" => CPLayoutDirection.CENTER
                case _ => assert(false)
            spec.y = CPLayoutRelation(dir, rel)
        override def exitXItem(ctx: CPLayoutParser.XItemContext): Unit =
            val rel = if ctx.ID() == null then None else ctx.ID().getText.?
            val dir = ctx.getChild(2).getText match
                case "same" => CPLayoutDirection.SAME
                case "before" => CPLayoutDirection.BEFORE
                case "left" => CPLayoutDirection.LEFT
                case "right" => CPLayoutDirection.RIGHT
                case "after" => CPLayoutDirection.AFTER
                case "center" => CPLayoutDirection.CENTER
                case _ => assert(false)
            spec.x = CPLayoutRelation(dir, rel)
        def getSpecs: Seq[CPLayoutSpec] = specs.toSeq

    private def antlr4Setup(src: String): (FiniteStateMachine, CPLayoutParser) =
        val lexer = new CPLayoutLexer(CharStreams.fromString(src, "code"))
        val parser = new CPLayoutParser(new CommonTokenStream(lexer))

        // Set custom error handlers.
        lexer.removeErrorListeners()
        parser.removeErrorListeners()
        lexer.addErrorListener(new CompilerErrorListener)
        parser.addErrorListener(new CompilerErrorListener)

        // State automata + it's parser.
        new FiniteStateMachine -> parser

    private class CompilerErrorListener extends BaseErrorListener:
        override def syntaxError(
            recog: Recognizer[_, _],
            badSymbol: scala.Any,
            line: Int,
            charPos: Int,
            msg: String,
            e: RecognitionException): Unit = throw CPException(msg, e.?)

    def compile(src: String): Try[Seq[CPLayoutSpec]] = Try:
        val (fsm, parser) = antlr4Setup(src)
        ParseTreeWalker().walk(fsm, parser.layout())
        fsm.getSpecs


