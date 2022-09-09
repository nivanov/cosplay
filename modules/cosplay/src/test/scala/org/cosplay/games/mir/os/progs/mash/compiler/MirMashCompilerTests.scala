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
    @Test
    def baseTest(): Unit =
        val comp = new MirMashCompiler

        def compile(code: String): Unit =
            Try(comp.compile(code, "test")). match
                case Success(_) => ()
                case Failure(e) => assertTrue(false, e.getMessage)

        compile("var x = 10")
        compile("val x = 10; val y = 'abc'")
        compile(
            """
              |native def listOf(fromIncl, toExcl)
              |
              |val x = null
              |var x = true
              |
              |val WIDTH = 100
              |val HEIGHT = {
              |    val a = 100
              |
              |    (a + 100) * 80
              |}
              |
              |val list0 = (1, 2, 3, true, null)
              |var list1 = () // Empty list.
              |
              |list1 = {
              |    var a = 1; var b = 2;
              |    (a, b)
              |}
              |
              |val map = ~(HEIGHT -> 2 * 0.4, "name"->"cosplay" + ' ' + "engine")
              |
              |println(map["name"]["key"])
              |
              |val s = `cat /home/file.txt | wc | take 1`
              |
              |echo "Some text echoing" | wc -l
              |echo "Some staff" > new_file.txt
              |
              |println("Number of command line arguments is $#")
              |println("Last exit status is $?")
              |println("Current PID $$")
              |println("Last background PID $!")
              |
              |someapp();;;
              |
              |alias ll = "ls -la -F"
              |
              |/*
              | * Test function.
              | *
              | * @param a 1st parameter.
              | */
              |def fun(a, b, c) = {
              |    def z() = println()
              |    val q = (1.054 + 2_00_00) * $4 + valName[2]
              |    while $q <= work() do println("something")
              |    val newList = for i <- list() yield i + "str"
              |
              |    newList = (1, 2, 3)
              |
              |    val anotherList = (1, "true", true, null, ("sub", "list"), newList[1])
              |    println("nikita") // Direct function call.
              |    newList[q]("some", 1, "arg") // FP-style function call.
              |    if boolFunc() && $w then doSome()
              |    if boolVal() then {
              |        def z() = println()
              |        println(z())
              |    }
              |    else
              |        println("Else")
              |    if !boolVal() then null else null
              |
              |    fun1((p, b) => println(p))
              |}
              |
              |def fun1(f, param) = {
              |    // Comment.
              |    f($param)
              |}
              |""".stripMargin)

