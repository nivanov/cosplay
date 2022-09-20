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
object MirAsmCompilerTests:
    MirClock.init(0)

    /**
      *
      * @param code Asm code to test.
      */
    private def compileOk(code: String): Unit =
        Try((new MirAsmCompiler).compile(code, "test")).match
            case Success(_) => ()
            case Failure(e) =>
                e.printStackTrace()
                assertTrue(false, e.getMessage)

    /**
      *
      * @param code Asm code to test.
      */
    private def compileFail(code: String): Unit =
        Try((new MirAsmCompiler).compile(code, "test")).match
            case Success(_) => assertTrue(false)
            case Failure(e) =>
                println(s"<< Expected error below >>")
                e.printStackTrace()

    /**
      * 
      */
    @Test
    def dupLabelTest(): Unit = compileFail(
        """
          |_label: ; Label.
          |     add s, 2
          |
          |_label: ; Should be an error...
          |
          |     push null
          |     pop
          |     push "qwerty"
          |
          |     mov "test", 1, null ; Inline comments.
          |""".stripMargin
    )

    /**
      *
      */
    @Test
    def syntaxTest(): Unit =
        compileFail("xyz s, 2 ; Unknown command.")
        compileFail("add s,, 2 ; Extra comma.")
        compileFail("add s, 2_00.12Ea34 ; Bad number.")
        compileFail("add 'bad string' ; Bad string.")
        compileFail(
            """
              |add @2, 300s, "asda"
              |""".stripMargin)
        compileFail(
            """
              |add @2, -300.23, "asda"
              |""".stripMargin)
        compileFail(
            """
              |add @2, 300, 'asda'
              |""".stripMargin)

    /**
      *
      */
    @Test
    def baseTest(): Unit = compileOk(
        """
          |; Testing assembler
          |; Comments
          |
          |;
          |; Start some code...
          |;
          |_label: ; Label.
          |     add s, 2_00.12E34 ; Comment.
          |     push null @1,1,"source"
          |     pop @2, 300, "asdadasa"
          |     push "qwerty", ""
          |""".stripMargin
    )
