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
import scala.collection.*

object CPLayoutCompiler:
    private class FiniteStateMachine extends CPLayoutBaseListener:
        private val specs = mutable.ArrayBuffer.empty[CPLayoutSpec]
        private var spec = CPLayoutSpec(id = null)

        override def enterDecl(ctx: CPLayoutParser.DeclContext): Unit = spec = CPLayoutSpec(id = ctx.ID().getText)
        override def exitDecl(ctx: CPLayoutParser.DeclContext): Unit = specs += spec
        override def exitFloatItem(ctx: CPLayoutParser.FloatItemContext): Unit =
            val rel = if ctx.ID() == null then None else ctx.ID().getText.?
            val isX = ctx.getChild(0).getText == "xfloat"
            val dir = ctx.getChild(2).getText match
                case "top" => CPLayoutDirection.TOP
                case "left" => CPLayoutDirection.LEFT
                case "bottom" => CPLayoutDirection.BOTTOM
                case "right" => CPLayoutDirection.RIGHT
                case "center" => CPLayoutDirection.CENTER
                case _ => assert(false)
            if isX then spec.xFloat = CPLayoutRelation(dir, rel) else spec.yFloat = CPLayoutRelation(dir, rel)

        override def exitPadItem(ctx: CPLayoutParser.PadItemContext): Unit =
            val num = ctx.NUM().getText.toInt
            ctx.getChild(0).getText match
                case "top" => spec.padding.withTop(num)
                case "left" => spec.padding.withLeft(num)
                case "bottom" => spec.padding.withBottom(num)
                case "right" => spec.padding.withRight(num)
                case "vert" => spec.padding.withVert(num)
                case "hor" => spec.padding.withHor(num)
                case _ => assert(false)

        override def exitPosItem(ctx: CPLayoutParser.PosItemContext): Unit =
            val rel = if ctx.ID() == null then None else ctx.ID().getText.?
            val dir = ctx.getChild(2).getText match
                case "above" => CPLayoutDirection.TOP
                case "left" => CPLayoutDirection.LEFT
                case "below" => CPLayoutDirection.BOTTOM
                case "right" => CPLayoutDirection.RIGHT
                case _ => assert(false)
            spec.pos = CPLayoutRelation(dir, rel)

        def getSpecs: Seq[CPLayoutSpec] = specs

    private def antlr4Setup(src: String, origin: String): (FiniteStateMachine, CPLayoutParser) =
        val lexer = new CPLayoutLexer(CharStreams.fromString(src, origin))
        val parser = new CPLayoutParser(new CommonTokenStream(lexer))

        // State automata + it's parser.
        new FiniteStateMachine -> parser

    def compile(src: String, origin: String): Try[Seq[CPLayoutSpec]] = Try:
        val (fsm, parser) = antlr4Setup(src, origin)
        ParseTreeWalker().walk(fsm, parser.layout())
        fsm.getSpecs


