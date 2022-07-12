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

import org.cosplay.*
import org.cosplay.games.mir.os.progs.*

import scala.concurrent.*
import scala.concurrent.ExecutionContext.Implicits.global

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
  * MirX.
  *
  * @param fs
  * @param users
  */
@SerialVersionUID(1_0_0L)
class CPMirOs(fsOpt: Option[CPMirFileSystem], users: Seq[CPMirUser]) extends Serializable:
    require(users.exists(_.isRoot))

    private val rootUsr = users.find(_.isRoot).get
    private var rt: CPMirRuntime = _
    private val fs = fsOpt.getOrElse(installFs())

    /**
      * Initializes brand new file system.
      */
    private def installFs(): CPMirFileSystem =
        val root = CPMirDirectoryFile.mkRoot(rootUsr)

        // Install directory structure.
        val bin = root.addDirFile("bin", rootUsr)
        val sbin = root.addDirFile("sbin", rootUsr)
        val dev = root.addDirFile("dev", rootUsr)
        val home = root.addDirFile("home", rootUsr)
        val tmp = root.addDirFile("tmp", rootUsr)
        val etc = root.addDirFile("etc", rootUsr)
        val lib = root.addDirFile("lib", rootUsr)
        val usr = root.addDirFile("usr", rootUsr)
        val usrBin = usr.addDirFile("bin", rootUsr)

        // Install files.
        sbin.addExecFile("boot", rootUsr, new CPMirBootProgram)
        sbin.addExecFile("ls", rootUsr, new CPMirLsProgram)
        sbin.addExecFile("login", rootUsr, new CPMirLoginProgram)
        sbin.addExecFile("mash", rootUsr, new CPMirMashProgram)

        new CPMirFileSystem(root)

    /**
      *
      */
    def getFs: CPMirFileSystem = fs

    /**
      *
      */
    def getRootUser: CPMirUser = rootUsr

    /**
      *
      */
    def getAllUsers: Seq[CPMirUser] = users

    /**
      *
      */
    def restart(): Unit = ???

    /**
      *
      */
    def boot(con: CPMirConsole): Unit =
        rt = CPMirRuntime(fs, con)

    /**
      *
      */
    def shutdown(): Unit = ???
