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

package org.cosplay.games.mir.os.progs.mash.compiler

import org.cosplay.games.mir.*

import java.util.regex.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*

import scala.collection.mutable


/*
   _________            ______________
   __  ____/_______________  __ \__  /_____ _____  __
   _  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
   / /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
   \____/  \____//____/ /_/     /_/  \__,_/ _\__, /
                                            /____/

          2D ASCII GAME ENGINE FOR SCALA3
            (C) 2021 Rowan Games, Inc.
               ALl rights reserved.
*/

/**
  * 
  */
object MirMashVarExpansionTests:
    private final val STR_VAR_REGEX = Pattern.compile("\\$\\{?(\\$|#|!|@|\\*|[0-9]+|[a-zA-Z_][a-zA-Z0-9_]*)}?")
    private val vars = mutable.HashMap(
        "var1" -> "'var1'",
        "var2" -> "'var2'",
        "var3" -> "'var3'",
        "a" -> "'a'",
        "b" -> "'b'",
        "$" -> "##",
        "1" -> "'1'"
    )

    private def dequote(qs: String): String =
        if qs.startsWith("'") && qs.endsWith("'") then MirUtils.dequote(qs)
        else if qs.startsWith("\"") && qs.endsWith("\"") then
            val s = MirUtils.dequote(qs)
            val m = STR_VAR_REGEX.matcher(s)
            val buf = new StringBuffer()
            while m.find() do m.appendReplacement(buf, vars(m.group(1)))
            m.appendTail(buf)
            buf.toString
        else
            throw Exception(s"Uneven quotes in: $qs")

    @Test
    def testStringExpansion(): Unit =
        val s = "\"Text$var1 $$var2 another param=$1 text${var3}text$a${b}\""
        println(s)
        println(dequote(s))



