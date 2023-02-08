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

package org.cosplay

/*
   _________            ______________
   __  ____/_______________  __ \__  /_____ _____  __
   _  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
   / /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
   \____/  \____//____/ /_/     /_/  \__,_/ _\__, /
                                            /____/

          2D ASCII JVM GAME ENGINE FOR SCALA3
              (C) 2021 Rowan Games, Inc.
                All rights reserved.
*/

import org.cosplay.{given, *}
import CPColor.*
import CPPixel.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
  *
  */
object CPMarkupTests:
    /**
      *
      * @param elms
      */
    private def mkMarkup(elms: List[(String, String)]): CPMarkup =
        new CPMarkup(
            C_GREEN,
            C_GRAY1.?,
            elms.map(elm => (elm._1, elm._2, (ch: Char) => CPPixel(ch, C_WHITE, C_RED.?)))
        )

    @Test
    def checkMarkupProcess(): Unit =
        val markup = CPMarkup(
            C_GREEN,
            C_BLACK.?,
            Seq(
                CPMarkupElement("<$", "$>", _&&(C_RED, C_WHITE)),
                CPMarkupElement("{#", "#}", _&&(C_BLUE, C_YELLOW)),
                CPMarkupElement("(?", "?)", _&&(C_BLACK, C_WHITE))
            )
        )

        def check(in: String, res: Seq[CPPixel]): Unit =
            val pxs = markup.process(in)
            if pxs != res then
                println(s"Invalid for: $in")
                pxs.foreach(println)
                assertTrue(false)
            else
                assertTrue(true)

        check("a<$b$>", Seq(
            'a'&&(C_GREEN, C_BLACK),
            'b'&&(C_RED, C_WHITE)
        ))
        check("a<$b{#c#}$>{#d#}", Seq(
            'a'&&(C_GREEN, C_BLACK),
            'b'&&(C_RED, C_WHITE),
            'c'&&(C_BLUE, C_YELLOW),
            'd'&&(C_BLUE, C_YELLOW)
        ))
        check("a<$b{#c#}$>{#d#}e", Seq(
            'a'&&(C_GREEN, C_BLACK),
            'b'&&(C_RED, C_WHITE),
            'c'&&(C_BLUE, C_YELLOW),
            'd'&&(C_BLUE, C_YELLOW),
            'e'&&(C_GREEN, C_BLACK),
        ))
        check("a<$b{#c(?x?)#}$>{#d#}e", Seq(
            'a'&&(C_GREEN, C_BLACK),
            'b'&&(C_RED, C_WHITE),
            'c'&&(C_BLUE, C_YELLOW),
            'x'&&(C_BLACK, C_WHITE),
            'd'&&(C_BLUE, C_YELLOW),
            'e'&&(C_GREEN, C_BLACK),
        ))

    @Test
    def checksTest(): Unit =
        assertDoesNotThrow(() => mkMarkup(
            List(("<%", "%>"))
        ))
        assertThrows(classOf[CPException], () => mkMarkup(
            List((" a", "%>>"))
        ))
        assertThrows(classOf[CPException], () => mkMarkup(
            List(("a", ""))
        ))
        assertThrows(classOf[CPException], () => mkMarkup(
            List(
                ("<%", "%>"),
                ("<$", "$>"),
                ("<~", "%>")
            )
        ))
        assertThrows(classOf[CPException], () => mkMarkup(
            List(
                ("<%", "%>"),
                ("<%", "%>>")
            )
        ))
