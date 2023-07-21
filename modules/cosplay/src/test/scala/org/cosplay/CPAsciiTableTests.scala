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
              (C) 2023 Rowan Games, Inc.
                ALl rights reserved.
*/

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
  *
  */
object CPAsciiTableTests:
    private final val c = "\u0002\u0007"

    @Test
    def visLengthTest(): Unit =
        val s = s"$c"
        assertTrue(s.visLength == 0)
        val s1 = s"ab$c${c}ef$c"
        assertTrue(s1.visLength == 4)
        assertTrue(s1.visOnly == "abef")
        val s2 = s"$c "
        assertTrue(s2.visLength == 1)

    @Test
    def equalityTest(): Unit =
        val tbl = CPAsciiTable("Header 1", s"Header $c$c${c}2", "Header 3")
        tbl += ("Cell 1.1", "Cell 1.2", s"Cell $c 1.3")
        tbl.addSeparator()
        tbl += (s"Cell $c$c$c$c$c$c${c}2.1", "Cell 2.2", "Cell 2.3")
        println(tbl.toString)
