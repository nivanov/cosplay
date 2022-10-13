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

import org.cosplay.games.mir.MirClock
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
object MirAsmRuntimeTests:
    MirClock.init(0)
    
    /**
      *
      * @param code Asm code to test.
      */
    private def executeOk(code: String): Unit =
        Try((new MirAsmCompiler).compile(code, "test").execute(new MirAsmContext(null))).match
            case Success(_) => ()
            case Failure(e) => assertTrue(false, e.getMessage)

    /**
      *
      * @param code Asm code to test.
      */
    private def executeFail(code: String): Unit =
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
              |let c, 5
              |push 1
              |pop b
              |pop c ; Stack is empty!
              |""".stripMargin)

        executeFail(
            """
              |let x, 10
              |let y, 2
              |let s, "cosplay"
              |push x
              |sub s ; Wrong operand type.
              |calln "_println"
              |""".stripMargin
        )

    /**
      *
      */
    @Test
    def breakTests(): Unit =
        executeOk(
            """
              |start: nop ; Just a start marker.
              |pushn 2, 3
              |eq
              |ifjmp true_label, false_label
              |true_label:
              |     brk
              |false_label:
              |     push "ok"
              |     calln "_println"
              |""".stripMargin
        )
        executeOk(
            """
              |start: nop ; Just a start marker.
              |let lbl1, "true_label"
              |let lbl2, "false_label"
              |pushn 2, 3
              |neq
              |ifjmpv lbl2,                    lbl1
              |true_label:
              |     brk
              |
              |     false_label:
              |                  push "ok"
              |                  calln "_println"
              |""".stripMargin
        )
        executeOk(
            """
              |start: nop ; Just a start marker.
              |push 2
              |ltp 5
              |cbrk
              |push 2
              |
              |         let y, 5
              |    ltp      y
              |""".stripMargin)
        executeOk(
            """
              |let x, 2
              |ltv x, 5
              |cbrk
              |let x, 5
              |let y, 10
              |ltv x, y
              |cbrk
              |""".stripMargin)
        executeOk(
            """
              |pushn 2, 5
              |lt
              |push 5
              |dup
              |lte
              |cbrk
              |""".stripMargin)
        executeOk(
            """
              |pushn 5, 2
              |gt
              |cbrk
              |let x, 5
              |gtv x, 2
              |cbrk
              |gtev x, x
              |cbrk
              |pushn 5, 5
              |gte
              |cbrk
              |""".stripMargin)
        executeOk(
            """
              |pushn 1, 0
              |and
              |push 0
              |eq
              |cbrk
              |""".stripMargin
        )
        executeOk(
            """
              |pushn 1, 0
              |or
              |push 1
              |eq
              |cbrk
              |""".stripMargin
        )
        executeOk(
            """
              |pushn 10, 3
              |mod
              |push 1
              |eq
              |cbrk
              |""".stripMargin
        )
        executeOk(
            """
              |pushn "one ", "two"
              |calln "concat"
              |eqp "one two"
              |cbrk
              |let correctRes, "one two"
              |pushn "one ", "two"
              |calln "concat"
              |eqp correctRes
              |cbrk
              |""".stripMargin
        )

        executeOk(
            """
              |let x, 10
              |let y, 2
              |pushn x, y
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
              |pushn x, y
              |sub
              |neqp 7 ; Wrong result (should be 8).
              |cbrk
              |""".stripMargin
        )
        executeOk(
            """
              |let x, 1
              |pushn x, 2
              |add
              |eqp 3
              |cbrk "Assertion"
              |""".stripMargin
        )
        executeOk(
            """
              |pushn 4, 2
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
              |pushn "1", 1
              |calln "to_str"
              |eq
              |cbrk
              |""".stripMargin
        )

        executeFail(
            """
              |push 0
              |push "Test assertion"
              |calln "assert"
              |""".stripMargin)
        executeFail(
            """
              |pushn 1, "asaa"
              |add
              |""".stripMargin)
        executeFail("brk \"Assertion\"")
        executeFail(
            """
              |pushn 5, 2
              |lt
              |cbrk
              |""".stripMargin)
        executeFail(
            """
              |let errMsg "Expected break out"
              |pushn 1, 2
              |add
              |push 0
              |eq
              |cbrk errMsg
              |""".stripMargin
        )

    /**
      *
      */
    @Test
    def jumpTests(): Unit =
        executeOk(
            """
              |call fun
              |ssz
              |eqp 3
              |cbrk "Should be zero here."
              |exit
              |fun:
              |     ssz
              |     eqp 0
              |     cbrk "Should be zero here."
              |     pushn 1, 2, 3
              |     ret
              |""".stripMargin)
        executeOk(
            """
              |let i, 0
              |let doneStr, "Loop is done!"
              |let pri, "_println"
              |loop:
              |     ; Start of the loop...
              |     pushn i, 10
              |     eq
              |     cjmp end
              |     pushn "loop iteration #", i
              |     calln "concat"
              |     calln pri
              |     pushn i, 1
              |     add
              |     pop i
              |     jmp loop ; Some comments;
              |     ; End of the loop...
              |end:
              |     push doneStr
              |     calln pri
              |""".stripMargin
        )
        executeOk(
            """
              |let i, 0
              |loop:
              |     eqv i, 10
              |     pop c
              |     cjmpv c, end
              |     call fun
              |     addv i, 1
              |     jmp loop
              |fun:
              |     push "function body..."
              |     calln "_println"
              |     ret
              |end:
              |     push "Loop is done."
              |     calln "_println"
              |     exit
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
              |     pushn "loop iteration #", i
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
        executeOk(
            """
              |let c, 5
              |push 1
              |cpop b
              |cpop c ; Stack is empty - no change.
              |eqv b, 1
              |cbrk
              |neqv c, 1
              |cbrk
              |""".stripMargin)
        executeOk("push 1")
        executeOk(
            """
              |push "----------"
              |calln "_println"
              |let x, 1
              |pushn x, 2
              |add
              |calln "_println"
              |""".stripMargin
        )

        executeOk(
            """
              |push "**********"
              |calln "_println"
              |pushn "one ", "two"
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
