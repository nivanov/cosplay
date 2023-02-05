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

package org.cosplay.impl

import org.cosplay.{given, *}
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
                All rights reserved.
*/

/**
  *
  */
object CPUtilsTests:
    /**
      *
      */
    @Test
    def zipUnzipTest(): Unit =
        val arr = Array[Byte](1, 2, 3, 4, 5, 6)
        val zipped = CPUtils.zipBytes(arr)
        val unzipped = CPUtils.unzipBytes(zipped)
        assertFalse(arr sameElements zipped)
        assertFalse(unzipped sameElements zipped)
        assertTrue(arr sameElements unzipped)

    @Test
    def trimByTest(): Unit =
        assertTrue(CPUtils.trimBy("  a b c     ", _ == ' ').mkString == "a b c")
        assertTrue(CPUtils.trimBy("     ", _ == ' ').mkString == "")
        assertTrue(CPUtils.trimBy("a b c", _ == ' ').mkString == "a b c")
        assertTrue(CPUtils.trimBy("a b c  ", _ == ' ').mkString == "a b c")
        assertTrue(CPUtils.trimBy("  a b c", _ == ' ').mkString == "a b c")
        assertTrue(CPUtils.trimBy("x  a b c     x", ch => ch == ' ' || ch == 'x').mkString == "a b c")

    @Test
    def splitByTest(): Unit =
        var s = "123"
        var ss = CPUtils.splitBy(s, ch => ch == ' ' || ch == 'x')
        assertTrue(ss.length == 1)
        assertTrue(ss.head.mkString == "123")

        s = "  123x456    789    a b xx c"
        ss = CPUtils.splitBy(s, ch => ch == ' ' || ch == 'x').filter(_.nonEmpty)
        assertTrue(ss.length == 6)
        assertTrue(ss.head.mkString == "123")
        assertTrue(ss(1).mkString == "456")
        assertTrue(ss(2).mkString == "789")
        assertTrue(ss(3).mkString == "a")
        assertTrue(ss(4).mkString == "b")
        assertTrue(ss(5).mkString == "c")

        val s2 = "ab\n\nc\nd"
        val s2s = CPUtils.splitBy(s2, _ == '\n')
        assertEquals(4, s2s.length)

