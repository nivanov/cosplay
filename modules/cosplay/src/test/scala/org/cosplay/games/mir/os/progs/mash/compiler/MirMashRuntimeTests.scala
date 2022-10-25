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
object MirMashRuntimeTests extends MirMashNatives:
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
        executeOk(
            s"""
              |$NATIVE_DECLS
              |
              |set list = new_list()
              |add(list, 1)
              |add(list, 2)
              |ensure(size(list) == 2)
              |""".stripMargin)
        executeOk(
            s"""
               |$NATIVE_DECLS
               |
               |val list = [4, 6, 20, 45, 2, 3, 4, 93, 12]
               |_println("STDDEV is: " + stddev(list))
               |""".stripMargin)
        executeOk(
            s"""
               |$NATIVE_DECLS
               |
               |set list = [1, 2]
               |ensure(size(list) == 2)
               |ensure(size([1, 2, 3, true, false]) == 5)
               |ensure(size([1, 2, 3, [true, false]]) == 4)
               |ensure(year() == 1997)
               |ensure(to_str(1) == "1")
               |ensure(to_long("123") == 123)
               |ensure(to_double("1.2") == 1.2)
               |hour()
               |minute()
               |second()
               |week_of_month()
               |week_of_year()
               |quarter()
               |cos(0.5)
               |sin(0.5)
               |val x = rand()
               |val x1 = rand_int(10, 20)
               |_println("GUID: " + guid())
               |
               |val map = map_from([
               |    [1, "1"],
               |    [2, "2"],
               |    ["str-3", 3]
               |])
               |_println("Key(0)=" + get(key_list(map), 0))
               |_println("Key(2)=" + get(key_list(map), 2))
               |ensure(get(key_list(map), 0) == "1")
               |ensure(get(key_list(map), 2) == "str-3")
               |ensure(get(map, "1") == "1")
               |ensure(get(map, "2") == "2")
               |ensure(get(map, "str-3") == 3)
               |""".stripMargin)
        executeOk(
            s"""
               |$NATIVE_DECLS
               |
               |val list = [1, 2]
               |ensure(get(list, 0) == 1)
               |ensure(get(list, 1) == 2)
               |""".stripMargin)
        executeOk(
            s"""
              |$NATIVE_DECLS
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
            s"""
              |$NATIVE_DECLS
              |
              |val x = (!true == false) || (2 != 3)
              |_println("x=" + x)
              |ensure(x == 1)
              |""".stripMargin)
        executeOk(
            s"""
               |$NATIVE_DECLS
               |ensure(60 & 13 == 12)
               |ensure(60 | 13 == 61)
               |ensure(60 ^ 13 == 49)
               |ensure(~60 == -61)
               |ensure(60 << 2 == 240)
               |ensure(60 >> 2 == 15)
               |ensure(60 >>> 2 == 15)
               |
               |""".stripMargin)
        executeOk(
            s"""
              |$NATIVE_DECLS
              |
              |for a <- [1, 2, 3, 4] do _println("List element: " + a)
              |val list = ["a", "b", "c"]
              |_println("-----")
              |for a <- list do _println("List element: " + a)
              |for a <- [] do ensure(false)
              |""".stripMargin)
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
              |set x = 10
              |unset x
              |_println(x) // Should fail as variable is unset.
              |""".stripMargin)
        executeFail(
            s"""
               |$NATIVE_DECLS
               |for a <- null do _println("List element: " + a)
               |""".stripMargin)
        executeFail(
            s"""
               |$NATIVE_DECLS
               |for a <- true do _println("List element: " + a)
               |""".stripMargin)
        executeFail(
            s"""
               |$NATIVE_DECLS
               |for a <- "123" do _println("List element: " + a)
               |""".stripMargin)
        executeFail(
            s"""
               |$NATIVE_DECLS
               |for a <- [] do {
               |    val a = 0
               |    _println("List element: " + a)
               |}
               |""".stripMargin)
        executeFail(
            s"""
               |$NATIVE_DECLS
               |
               |val list = null
               |get(list, 3)
               |""".stripMargin)
        executeFail(
            s"""
               |$NATIVE_DECLS
               |
               |val list = [1, 2]
               |get(list, 3)
               |""".stripMargin)
        executeFail(
            s"""
               |$NATIVE_DECLS
               |
               |val list = [1, 2]
               |get(list, "key")
               |""".stripMargin)
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
        executeFail("var x = (true && false) || 'string'")
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
