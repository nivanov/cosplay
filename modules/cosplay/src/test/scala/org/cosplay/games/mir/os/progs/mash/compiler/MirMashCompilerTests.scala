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

import org.cosplay.games.mir.*

import scala.util.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*

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
object MirMashCompilerTests:
    MirClock.init(0)

    /**
      *
      * @param code Mash code to test.
      */
    private def compileOk(code: String): Unit =
        Try((new MirMashCompiler).compileToAsm(code, "test")) match
            case Success(mod) =>
                println("---------------------")
                mod.asm.foreach(loc => println(loc.toAsmString(true)))
            case Failure(e) =>
                e.printStackTrace()
                assertTrue(false, e.getMessage)

    /**
      *
      * @param code Mash code to test.
      */
    private def compileFail(code: String): Unit =
        Try((new MirMashCompiler).compileToAsm(code, "test")).match
            case Success(_) => assertTrue(false)
            case Failure(e) =>
                println(s"<< Expected error below >>")
                e.printStackTrace()

    /**
      *
      */
    @Test
    def baseOkTest(): Unit =
        compileOk(
            """
              |native def _println(s)
              |var x  = 0
              |while x < 10 do {
              |     x = x + 1
              |     if x % 2 == 0 then _println("Even") else _println("Odd")
              |}
              |""".stripMargin)
        compileOk("")
        compileOk(
            """
              |var x  = 0
              |while x < 10 do {
              |     x = x + 1
              |}
              |""".stripMargin)
        compileOk("var x = 5 + 5")
        compileOk("var x = 5 + 5; var z = (x + 5) * (x + 3)")
        compileOk("var x = (true && false) || true")
        compileOk(
            """
              |alias x = "ls -la"
              |alias y = 'pwd -c'
              |""".stripMargin)
        compileOk(
            """
              |native def empty()
              |native def foo(
              |     a,
              |     b,
              |     c
              |)
              |/*
              | * Function bar.
              | */
              |native def bar(
              |     a,
              |     /* Parameter b. */
              |     b,
              |     // Parameter c.
              |     c
              |)
              |""".stripMargin)
        compileOk(
            """
              |var list = ""
              |for a <- list do {
              |    native def x()
              |    3
              |}
              |for a <- list do {
              |    native def x() // Not a dup since it is from the different scope.
              |    3
              |}
              |""".stripMargin)
        compileOk(
            """
              |def z() = {
              |     def q() = {}
              |     def q1() = {}
              |     def q2() = {}
              |}
              |def fun(a, b, c) = {
              |     def another_fun(x, y) = return x + y
              |     return another_fun(1, "cosplay")
              |}
              |fun(1, 2, 3)
              |""".stripMargin)
        compileOk(
            """
              |var x = 2
              |val w = 20
              |var z = x - (w * 10)
              |var q = (x + 5) * (x + 3)
              |""".stripMargin)
        compileOk("var x = 5 + 5; var z = (x + 5) * (x + 3)")
        compileOk("var x = 5 + 2")
        compileOk("var x = 10_000")
        compileOk("var x = true; var b = false; var c = null")
        compileOk("var x = 10; var _long_variable = x")
        compileOk("val x = 10; val y = 'abc'")

    /**
      *
      */
    @Test
    def baseFailTest(): Unit =
        compileFail("val 0x = 1 // Invalid variable name.")
        compileFail("val .x = 1 // Invalid variable name.")
        compileFail(
            """
              |def f(1x) = return true // Invalid parameter name.
              |""".stripMargin)
        compileFail(
            """
              |val x = 0
              |x = x + 1 // Can't modify immutable variable.
              |""".stripMargin)
        compileFail(
            """
              |val list = "" // Just for compilation.
              |for a <- list do {
              |     return 2; // Can't use return here.
              |}
              |""".stripMargin)
        compileFail(
            """
              |alias x = "ls -l"
              |alias x = 'pwd'
              |""".stripMargin)
        compileFail("var x = d")
        compileFail("var x = 'wrong quote\"")
        compileFail("var x = \"\"wrong quote\"")
        compileFail("var x = \"wrong quote'")
        compileFail("var x = 1as1212")
        compileFail(
            """
              |def z() = {
              |     def q() = {}
              |     def q1() = {}
              |     def q1() = {}
              |}
              |""".stripMargin)
        compileFail(
            """
              |def fun(a, b, c) = {
              |     def another_fun(x, y) = return x + y
              |     return another_fun_(1, "cosplay")
              |}
              |fun(1, 2, 3)
              |""".stripMargin)



