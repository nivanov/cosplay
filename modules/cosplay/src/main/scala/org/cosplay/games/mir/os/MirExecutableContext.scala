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

import org.cosplay.games.mir.*
import scala.collection.mutable

/**
  *
  * @param pid
  * @param parent
  * @param file
  * @param cmdArgs
  * @param con
  * @param rt
  * @param fs
  * @param host
  * @param workDir
  * @param vars
  * @param aliases
  * @param lastExit
  * @param usr
  * @param in
  * @param out
  * @param err
  */
case class MirExecutableContext(
    pid: Long,
    parent: Option[MirProcess],
    file: MirExecutableFile,
    cmdArgs: Seq[String],
    con: MirConsole,
    rt: MirRuntime,
    fs: MirFileSystem,
    host: String,
    var workDir: MirDirectoryFile,
    vars: mutable.HashMap[String, Any],
    aliases: mutable.HashMap[String, String],
    var lastExit: Int,
    usr: MirUser,
    in: MirInputStream,
    out: MirOutputStream,
    err: MirOutputStream
):
    /**
      * Forks child process with given parameters.
      *
      * @param file Executable file to fork with.
      * @param cmdArgs Command line arguments.
      * @param in
      * @param out
      * @param err
      */
    def fork(
        file: MirExecutableFile,
        cmdArgs: Seq[String],
        in: MirInputStream = MirInputStream.nullStream(),
        out: MirOutputStream = MirOutputStream.consoleStream(con),
        err: MirOutputStream = MirOutputStream.consoleStream(con)): MirProcess =
        rt.exec(
            rt.getProcess(pid),
            file,
            cmdArgs,
            workDir,
            usr,
            vars,
            aliases,
            lastExit,
            in,
            out,
            err
        )

