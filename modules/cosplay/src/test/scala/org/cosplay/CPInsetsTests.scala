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

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/*
   _________            ______________
   __  ____/_______________  __ \__  /_____ _____  __
   _  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
   / /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
   \____/  \____//____/ /_/     /_/  \__,_/ _\__, /
                                            /____/

          2D ASCII GAME ENGINE FOR SCALA3
            (C) 2021 Rowan Games, Inc.
               All rights reserved.
*/

/**
  *
  */
object CPInsetsTests:
    @Test
    def insetTest(): Unit =
        val ins = CPInsets(1, 1, 1, 1)
        val ins1 = ins + 2
        assertTrue(ins1.top == 3 && ins1.left == 3 && ins1.right == 3 && ins1.bottom == 3)

        val ins3 = CPInsets(1, 1, 1, 1)
        assertTrue(ins3 == ins)

        val ins4 = ins1 + ins3
        assertTrue(ins4.top == 4 && ins4.right == 4)

        assertTrue(ins4.isWhole)
        assertTrue(ins4.isPositive)
        assertFalse(new CPInsets(-1).isPositive)

        assertTrue(ins4 == ins4.copy())

        assertTrue(new CPInsets(2, 4) == CPInsets(4, 2, 4, 2))
        assertTrue(new CPInsets(1) == new CPInsets(1, 1))
        assertTrue(new CPInsets(2, 4) != CPInsets(2, 2, 2, 4))

        println(s"ins4 $ins4")
