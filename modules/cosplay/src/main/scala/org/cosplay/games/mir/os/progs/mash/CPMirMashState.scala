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
    /**
      *
      */
    def getWorkingDirectory: CPMirDirectoryFile = ???

    /**
      *
      * @param dir
      */
    def setWorkingDirectory(dir: CPMirDirectoryFile): Unit = ???

    def getAllAliases: Seq[(String, String)] = ???

    def getAlias(alias: String): String = ???

    def addAlias(alias: String, value: String): Unit = ???

    def hasAlias(alias: String): Boolean = ???

    def removeAlias(alias: String): Boolean = ???

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

    def setVariable(name: String, value: String, scope: String): Unit = ???

    def getVariable(name: String, scope: String): String = ???

    def getAllVariables(scope: String): Seq[String] = ???

    def removeVariable(name: String, scope: String): Boolean = ???


