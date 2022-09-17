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
object MirAsmInstruction:
    trait Param
    object NullParam extends Param:
        override def toString: String = "null"
    case class IdParam(id: String) extends Param:
        override def toString: String = id
    case class StringParam(s: String) extends Param:
        override def toString: String = s"\"$s\""
    case class LongParam(d: Long) extends Param:
        override def toString: String = d.toString
    case class DoubleParam(d: Double) extends Param:
        override def toString: String = d.toString

import MirAsmInstruction.*

/**
  *
  * @param label Optional label for this instruction.
  * @param line Line number in the original source code.
  * @param name Instruction name.
  * @param params Instruction parameter list in the same order they appear in the source code.
  */
case class MirAsmInstruction(
    label: Option[String],
    line: Int,
    name: String,
    params: Seq[Param]
):
    /**
      *
      */
    def getSourceCode: String =
        val lbl = label match
            case Some(s) => s"$s: "
            case None => ""
        s"$lbl$name ${params.mkString(", ")}"

