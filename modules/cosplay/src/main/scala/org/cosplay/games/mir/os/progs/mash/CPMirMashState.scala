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

package org.cosplay.games.mir.os.progs.mash

import org.cosplay.games.mir.os.*
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
  * Global state of the mash interpreter instance.
  */
class CPMirMashState:
    private val aliases = mutable.HashMap.empty[String, String]
    private var workDir: CPMirDirectoryFile = _

    /**
      *
      */
    def getWorkingDirectory: CPMirDirectoryFile = workDir

    /**
      *
      * @param workDir
      */
    def setWorkingDirectory(workDir: CPMirDirectoryFile): Unit = this.workDir = workDir

    /**
      *
      */
    def getAllAliases: Seq[(String, String)] = aliases.toSeq

    /**
      *
      * @param name
      */
    def getAlias(name: String): Option[String] = aliases.get(name)

    /**
      *
      * @param name
      * @param value
      */
    def addAlias(name: String, value: String): Option[String] = aliases.put(name, value)

    /**
      *
      * @param name
      * @return
      */
    def hasAlias(name: String): Boolean = aliases.contains(name)

    /**
      *
      * @param name
      * @return
      */
    def removeAlias(name: String): Option[String] = aliases.remove(name)

    def getCurrentFilename: String = ???

    def setCurrentFilename (filename: String): Unit = ???

    def getCmdArgument(n: Int): String = ???

    def getAllCmdArguments: Seq[String] = ???

    def setCmdArguments(args: Seq[String]): Unit = ???

    def getLastExitStatus: Int = ???

    def setLastExitStatus(d: Int): Unit = ???

    def getPid: Long = ???

    def setPid(pid: Long): Unit = ???

    def getLastBackgroundPid: Long = ???

    def setLastBackgroundPid(pid: Long): Unit = ???

    def setVariable(name: String, value: String, scope: Option[String] = None): Unit = ???

    def getVariable(name: String, scope: Option[String] = None): String = ???

    def getAllVariables(scope: String): Seq[String] = ???

    def removeVariable(name: String, scope: Option[String] = None): Boolean = ???


