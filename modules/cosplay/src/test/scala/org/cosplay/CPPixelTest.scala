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

import CPColor.*
import CPPixel.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

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
object CPPixelTest:
    /**
      *
      */
    @Test
    def pixelTest(): Unit =
        val p1 = 'x'&C_BLACK
        val p4 = CPPixel('x', C_BLACK)
        val p5 = new CPPixel('x', C_BLACK, None, 0)

        import scala.language.implicitConversions

        val p2: CPPixel = 'x' -> C_BLACK
        val p3: CPPixel = ('x', C_BLACK)

        assertTrue(p1 == p2)
        assertTrue(p2 == p3)
        assertTrue(p3 == p4)
        assertTrue(p4 == p5)

