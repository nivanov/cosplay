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

package org.cosplay.games.mir.os

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

import java.io.*
import org.cosplay.*
import games.mir.*
import games.mir.os.*
import MirFileType.*
import org.cosplay.impl.CPUtils

import java.io.FileInputStream

/**
  *
  * @param name Name of file (not including its path).
  * @param owner User owner of this file.
  * @param dir Parent directory of this file, i.e. a directory this file belongs to.
  * @param otherAcs Can others read or execute. Owner can do anything.
  * @param otherMod Can others change or delete. Owner can do anything.
  * @param initMs Initial creation and update timestamp. Defaults to the current time.
  */
class MirRegularFile(
    name: String,
    owner: MirUser,
    dir: MirDirectoryFile,
    otherAcs: Boolean,
    otherMod: Boolean,
    lines: Seq[String] = Seq.empty,
    initMs: Long = MirClock.now()
) extends MirFile(FT_REG, name, owner, dir.?, otherAcs, otherMod, initMs):
    private val file = CPEngine.homeFile(CPRand.guid)

    /**
      *
      * @param name
      * @param owner
      * @param parent
      * @param otherAcs
      * @param otherMod
      * @param path
      */
    def this(
        name: String,
        owner: MirUser,
        parent: MirDirectoryFile,
        otherAcs: Boolean,
        otherMod: Boolean,
        path: String
    ) = this(name, owner, parent, otherAcs, otherMod, CPUtils.readAllStrings(path))

    /**
      *
      */
    @throws[IOException]
    def readLines: Seq[String] =
        val in = getInput
        try in.readLines()
        finally in.close()

    /**
      *
      * @param lines
      * @param append
      */
    @throws[IOException]
    def writeLines(lines: Seq[String], append: Boolean = false): Unit =
        val out = getOutput(append)
        try lines.foreach(out.println)
        finally out.close()

    /**
      *
      */
    @throws[IOException]
    def getInput: MirInputStream =
        MirInputStream.nativeStream(new FileInputStream(file))

    /**
      *
      * @param append
      */
    @throws[IOException]
    def getOutput(append: Boolean = false): MirOutputStream =
        MirOutputStream.nativeStream(new FileOutputStream(file, append))