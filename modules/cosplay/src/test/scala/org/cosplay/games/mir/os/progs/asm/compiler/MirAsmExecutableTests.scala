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

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

import scala.util.*

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
object MirAsmExecutableTests:
    /**
      *
      * @param code
      */
    def executeOk(code: String): Unit =
        Try((new MirAsmCompiler).compile(code, "test").execute(new MirAsmContext(null))).match
            case Success(_) => ()
            case Failure(e) => assertTrue(false, e.getMessage)

    /**
      *
      * @param code
      */
    def executeFail(code: String): Unit =
        Try((new MirAsmCompiler).compile(code, "test").execute(new MirAsmContext(null))).match
            case Success(_) => assertTrue(false)
            case Failure(e) =>
                println(s"<< Expected error below >>")
                e.printStackTrace()

    /**
      *
      */
    @Test
    def executeFailTests(): Unit =
        executeFail("push ; Missing parameter.")
        executeFail("push null, 1, 2, \"asdas\"; Too many parameter.")
        executeFail(
            """
              |let x, 10
              |let y, 2
              |let s, "cosplay"
              |push x
              |sub s
              |calln "_print"
              |""".stripMargin
        )

    /**
      *
      */
    @Test
    def breakOutTests(): Unit =
        executeOk(
            """
              |push "one "
              |push "two"
              |calln "concat"
              |push "one two"
              |eq
              |cbrk
              |""".stripMargin
        )

        executeOk(
            """
              |let x, 10
              |let y, 2
              |push x
              |push y
              |sub
              |push 8
              |eq
              |cbrk
              |""".stripMargin
        )
        executeOk(
            """
              |let x, 10
              |let y, 2
              |push x
              |push y
              |sub
              |push 7 ; Wrong result (should be 8).
              |neq
              |cbrk
              |""".stripMargin
        )
        executeOk(
            """
              |let x, 1
              |push x
              |push 2
              |add
              |push 3
              |eq
              |cbrk "Assertion"
              |""".stripMargin
        )

        executeFail("brk \"Assertion\"")
        executeFail(
            """
              |push 1
              |push 2
              |add
              |push 0
              |eq
              |cbrk "Expected break out"
              |""".stripMargin)

    /**
      *
      */
    @Test
    def jumpTests(): Unit =
        executeOk(
            """
              |let i, 0
              |loop:
              |     push i
              |     push 10
              |     eq
              |     cjmp end
              |     push "loop iteration"
              |     calln "_print"
              |     push i
              |     push 1
              |     add
              |     pop i
              |     jmp loop
              |end:
              |     push "Loop is done."
              |     calln "_print"
              |""".stripMargin
        )
        executeOk(
            """
              |push 1
              |jmp label
              |push 2
              |label: push 3
              |add
              |push 4
              |eq
              |cbrk "Assertion"
              |""".stripMargin
        )

        executeFail(
            """
              |push 1
              |jmp wrong_label ; Wrong label.
              |push 2
              |label: push 3
              |add
              |push 4
              |eq
              |cbrk "Assertion"
              |""".stripMargin
        )

/**
      *
      */
    @Test
    def executeOkTests(): Unit =
        executeOk("push 1")
        executeOk(
            """
              |push "----------"
              |calln "_print"
              |let x, 1
              |push x
              |push 2
              |add
              |calln "_print"
              |""".stripMargin
        )

        executeOk(
            """
              |push "**********"
              |calln "_print"
              |push "one "
              |push "two"
              |calln "concat"
              |calln "_print"
              |""".stripMargin
        )

        executeOk(
            """
              |push "############"
              |calln "_print"
              |let x, 10
              |let y, 2
              |push x
              |push y
              |sub
              |calln "_print"
              |""".stripMargin
        )
