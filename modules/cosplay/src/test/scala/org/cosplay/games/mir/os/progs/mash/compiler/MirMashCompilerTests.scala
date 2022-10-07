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
                mod.asm.foreach(loc => println(loc.toAsmString(false)))
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

    @Test
    def baseTest(): Unit =
        compileOk(
            """
              |native def println(s)
              |def fun(a, b, c) = {
              |     def another_fun(x, y) = return x + y
              |
              |     val x = b + another_fun(c, 1)
              |
              |     println(a)
              |     println(x)
              |}
              |fun(1, 2, 3)
              |""".stripMargin)
//        compileOk(
//            """
//              |var x = 2
//              |val w = 20
//              |var z = x - (w * 10)
//              |var q = (x + 5) * (x + 3)
//              |""".stripMargin)
//        compileOk("var x = 5 + 5; var z = (x + 5) * (x + 3)")
//        compileOk("var x = 5 + 2")
//        compileOk("var x = 10_000")
//        compileOk("var x = true; var b = false; var c = null")
//        compileOk("var x = 10; var _long_variable = x")
//        compileOk("val x = 10; val y = 'abc'")
//
//        compileFail("var x = d")
//        compileFail("var x = 1as1212")

