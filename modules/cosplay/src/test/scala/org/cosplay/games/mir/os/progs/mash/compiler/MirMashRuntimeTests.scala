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
            case Success(mod) => ()
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
    def okTest(): Unit =
        executeOk("var x = 5 + 5")
        executeOk("var x = 5 + 5; var z = (x + 5) * (x + 3)")
        executeOk("var x = (true && false) || true")
        executeOk(
            """
              |native def empty()
              |native def foo(a, b, c)
              |native def bar(a, b, c)
              |""".stripMargin)
        executeOk(
            """
              |val a = {
              |    native def x()
              |    3
              |}
              |val b = {
              |    native def x() // Not a dup since it is from the different scope.
              |    3
              |}
              |""".stripMargin)
        executeOk(
            """
              |var x = {
              |    var z = 3
              |    var y = (z + 5) * (z + 3)
              |    y * 8
              |}
              |""".stripMargin)

    @Test
    def failTest(): Unit =
        executeFail("var x = 5 + 'fail'")
        executeFail("var x = (true && false) || 5")
        executeFail(
            """
              |native def foo(a, b, c)
              |native def foo(a, b, c)
              |""".stripMargin)
        executeFail(
            """
              |var a = { 7 * 7 }
              |native def foo(a, b, c)
              |""".stripMargin)
        executeFail(
            """
              |var x = {
              |    var z = 3
              |    var y = (z + 5) * (z + 3)
              |    y * 8
              |}
              |var w = z * 8
              |""".stripMargin)
