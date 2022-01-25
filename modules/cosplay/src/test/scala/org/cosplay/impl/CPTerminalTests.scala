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

import org.cosplay.*
import org.cosplay.impl.jlineterm.CPJLineTerminal
import org.junit.jupiter.api.Test

/**
  *
  */
object CPTerminalTests:
    /**
      *
      */
    @Test
    def termTests(): Unit =
        val gameInfo = CPGameInfo(
            name = "Name",
            devName = "Developer",
            initDim = Some(CPDim(10, 10)),
            termBg = CPColor.C_DFLT_BG,
            minDim = Some(CPDim(10, 10))
        )

        val term = new CPJLineTerminal(gameInfo)
        term.dispose()
