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

import org.cosplay.games.mir.*
import scala.util.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*

/**
  *
  */
object MirMashRuntimeTests:
    MirClock.init(0)

    /**
      *
      * @param code Mash code to test.
      */
    private def executeOk(code: String): Unit =
        Try((new MirMashCompiler).compile(code, "test").execute(null)) match
            case Success(_) => ()
            case Failure(e) =>
                e.printStackTrace()
                assertTrue(false, e.getMessage)

    /**
      *
      * @param code Mash code to test.
      */
    private def executeFail(code: String): Unit =
        Try((new MirMashCompiler).compile(code, "test").execute(null)).match
            case Success(_) => assertTrue(false)
            case Failure(e) =>
                println(s"<< Expected error below >>")
                e.printStackTrace()

    @Test
    def nativeFunctionsTest(): Unit =
        val natives =
            """
              | native def new_list()
              | native def add(list, v)
              | native def size(list)
              | native def length(s)
              | native def uppercase(s)
              | native def lowercase(s)
              | native def trim(s)
              | native def to_str(s)
              | native def is_alpha(s)
              | native def is_num(s)
              | native def ensure(cond)
              | native def split(str, regex)
              | native def remove(list, idx)
              | native def take(list, n)
              | native def take_right(list, n)
              |""".stripMargin

        executeOk(
            s"""
              |$natives
              |
              |val list = new_list()
              |add(list, 1)
              |add(list, 2)
              |ensure(size(list) == 2)
              |""".stripMargin)
        executeOk(
            s"""
               |$natives
               |
               |val list = [1, 2]
               |ensure(size(list) == 2)
               |ensure(size([1, 2, 3, true, false]) == 5)
               |ensure(size([1, 2, 3, [true, false]]) == 4)
               |""".stripMargin)
        executeOk(
            s"""
              |$natives
              |
              |val s = "cosplay"
              |ensure([1, 2, 3] == [1, 2, 3])
              |ensure(to_str(123) == "123")
              |ensure(length(s) == 7)
              |ensure(uppercase("cosplay") == "COSPLAY")
              |ensure(lowercase("cOsPlAy") == "cosplay")
              |ensure(trim("    trim   ") == "trim")
              |ensure(is_alpha("cosplay") == true)
              |ensure(is_alpha("cosplay12") == false)
              |ensure(is_num("12") == true)
              |ensure(is_num("122323cosplay") == false)
              |ensure(size(split("one two three", " ")) == 3)
              |ensure(split("one two three", " ") == ["one", "two", "three"])
              |ensure(size(take([1, 2, 3, 4], 2)) == 2)
              |ensure(take([1, 2, 3, 4], 2) == [1, 2])
              |ensure(size(take_right([1, 2, 3, 4], 1)) == 1)
              |ensure(take_right([1, 2, 3, 4], 1) == [4])
              |""".stripMargin)

    @Test
    def okTest(): Unit =
        executeOk(
            """
              |native def ensure(cond) // Check passing order of parameters.
              |def f(a, b, c) = {
              |     ensure(a == 1)
              |     ensure(b == 2)
              |     ensure(c == 3)
              |}
              |f(1, 2, 3)
              |""".stripMargin)
        executeOk(
            """
              |native def assert(cond, msg)
              |def fun(x) = return x + 1
              |assert(fun(2) == 3, "Something is wrong")
              |assert(fun(4) == 5, "Something is wrong")
              |assert(fun("text") == "text1", "Something is wrong")
              |""".stripMargin)
        executeOk(
            """
              |def fun(x) = return x
              |val x = fun(1)
              |""".stripMargin)
        executeOk(
            """
              |native def _println(s)
              |var x  = 0
              |var odd = "odd"
              |var even = "even"
              |while (x < 10) do {
              |     x = x + 1
              |     if x % 2 == 0 then _println(even)
              |     else if x == 3 then { _println(odd); _println("It is: " + x) }
              |     else {
              |         _println(odd)
              |         if x == 7 then _println("It is: " + x)
              |     }
              |}
              |""".stripMargin)
        executeOk("var x = 5 + 5")
        executeOk("var x = 5 + 5; var z = (x + 5) * (x + 3)")
        executeOk("var x = (true && false) || true")
        executeOk(
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
        executeOk(
            """
              |native def assert(cond, msg)
              |var x = 0
              |while x < 10 do x = x + 1
              |assert(x == 10, "Invalid loop implementation.")
              |""".stripMargin)
        executeOk("")
        executeOk(
            """
              |
              |
              |
              |
              |""".stripMargin)

    @Test
    def failTest(): Unit =
        executeFail(
            """
              |def fun() = val x = 0
              |val z = 1 + fun() // No return value in expression.
              |""".stripMargin)
        executeFail(
            """
              |native def assert(cond, msg)
              |var x = 0
              |while x < 10 do x = x + 1
              |assert(x < 10, "Invalid assertion.")
              |""".stripMargin)
        executeFail("var x = 5 + 'fail'")
        executeFail("var x = (true && false) || 5")
        executeFail(
            """
              |native def foo(a, b, c)
              |native def foo(a, b, c)
              |""".stripMargin)
        executeFail(
            """
              |var a = 7 * 7
              |native def foo(a, b, c)
              |""".stripMargin)
        executeFail(
            """
              |var list = ""
              |native def f(x)
              |for a <- list do {
              |    var z = 3
              |    var y = (z + 5) * (z + 3)
              |    f(y * 8)
              |}
              |var w = z * 8
              |""".stripMargin)
