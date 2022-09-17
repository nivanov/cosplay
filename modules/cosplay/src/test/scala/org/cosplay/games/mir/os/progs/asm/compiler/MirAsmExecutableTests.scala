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
              |calln "_println"
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
        executeOk(
            """
              |push 4
              |push 2
              |div
              |dup
              |calln "_println"
              |pop v
              |eqv v, 2
              |cbrk
              |""".stripMargin
        )
        executeOk(
            """
              | let a, 8
              | let b, 4
              | mulv a, b
              | eqv a, 32
              | cbrk
              |""".stripMargin
        )
        executeOk(
            """
              |push 1
              |neg
              |push -1
              |eq
              |cbrk
              |""".stripMargin
        )
        executeOk(
            """
              |push 0
              |not
              |push 1
              |eq
              |cbrk
              |""".stripMargin
        )
        executeOk(
            """
              |let x, 0
              |notv x
              |eqv x, 1
              |cbrk
              |""".stripMargin
        )
        executeOk(
            """
              | let a, 8
              | let b, 4
              | divv a, b
              | eqv a, 2
              | cbrk
              |""".stripMargin
        )
        executeOk(
            """
              |let x, 1
              |addv x, 2
              |eqv x, 3
              |cbrk "Assertion"
              |""".stripMargin
        )
        executeOk(
            """
              |let x, 1
              |addv x, 2
              |eqv x, 3
              |pop r
              |cbrkv r, "Assertion"
              |""".stripMargin
        )
        executeOk(
            """
              |push 1
              |dup
              |eq
              |cbrk "Assertion"
              |""".stripMargin
        )
        executeOk(
            """
              |push "1"
              |push 1
              |calln "tostr"
              |eq
              |cbrk
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
              |     calln "_println"
              |     push i
              |     push 1
              |     add
              |     pop i
              |     jmp loop
              |end:
              |     push "Loop is done."
              |     calln "_println"
              |""".stripMargin
        )
        executeOk(
            """
              |let i, 0
              |loop:
              |     eqv i, 10
              |     pop c
              |     cjmpv c, end
              |     push "loop iteration"
              |     calln "_println"
              |     addv i, 1
              |     jmp loop
              |end:
              |     push "Loop is done."
              |     calln "_println"
              |""".stripMargin
        )
        executeOk(
            """
              |let i, 0
              |let sz, 10
              |loop:
              |     eqv i, sz
              |     cjmp end
              |     push "loop iteration #"
              |     push i
              |     calln "concat"
              |     calln "_println"
              |     incv i
              |     jmp loop
              |end:
              |     push "Loop is done."
              |     calln "_println"
              |""".stripMargin
        )
        executeOk(
            """
              |push 1
              |jmp label
              |push 2
              |label:
              |     push 3
              |     add
              |     push 4
              |     eq
              |     cbrk "Assertion"
              |""".stripMargin
        )

        executeFail(
            """
              |push 1
              |jmp wrong_label ; Wrong label.
              |push 2
              |label:
              |     push 3
              |     add
              |     push 4
              |     eq
              |     cbrk "Assertion"
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
              |calln "_println"
              |let x, 1
              |push x
              |push 2
              |add
              |calln "_println"
              |""".stripMargin
        )

        executeOk(
            """
              |push "**********"
              |calln "_println"
              |push "one "
              |push "two"
              |calln "concat"
              |calln "_println"
              |""".stripMargin
        )

        executeOk(
            """
              |push "############"
              |calln "_println"
              |let x, 10
              |let y, 2
              |push x
              |push y
              |sub
              |calln "_println"
              |""".stripMargin
        )
