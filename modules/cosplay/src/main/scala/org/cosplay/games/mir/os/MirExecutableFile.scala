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
import org.cosplay.games.mir.os.*
import org.cosplay.games.mir.os.MirFileType.*

/**
  *
  * @param name Name of file (not including its path).
  * @param owner User owner of this file.
  * @param exe Executable program.
  * @param otherAcs Can others read or execute. Owner can do anything.
  * @param otherMod Can others change or delete. Owner can do anything.
  * @param initMs Initial creation and update timestamp. Defaults to the current time.
  */
class MirExecutableFile(
    name: String,
    owner: MirUser,
    parent: MirDirectoryFile,
    exe: MirExecutable,
    otherAcs: Boolean,
    otherMod: Boolean,
    initMs: Long = MirClock.now()
) extends MirFile(FT_EXE, name, owner, parent.?, otherAcs, otherMod, initMs):
    setSize(exe.getSizeOnDisk)

    /**
      *
      */
    def getExec: MirExecutable = exe

