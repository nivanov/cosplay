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
               ALl rights reserved.
*/

/**
  *
  */
object CPMirFileSystemTests:
    private def initFs(): CPMirFileSystem =
        val rootUsr = CPMirUser.mkRoot()
        val rootDir = CPMirDirectoryFile.mkRoot(rootUsr)

        def addRegFile(name: String, dir: CPMirDirectoryFile): Unit = dir.addFile(new CPMirRegularFile(name, rootUsr, dir))

        val fs = new CPMirFileSystem(rootDir)

        addRegFile("info.txt", rootDir)
        addRegFile("image.png", rootDir)
        addRegFile("script.mash", rootDir)

        val binDir = new CPMirDirectoryFile("bin", rootUsr, rootDir)

        rootDir.addFile(binDir)

        addRegFile("bin_info.txt", binDir)
        addRegFile("bin_image.png", binDir)
        addRegFile("bin_script.mash", binDir)

        fs

    @Test
    def pathTest(): Unit =
        val fs = initFs()
        val root = fs.root
        val bin = root.dir("/bin").get

        def assertRootPath(p1: String, p2: String): Unit = assertPath(root, p1, p2)
        def assertPath(cp: CPMirDirectoryFile, p1: String, p2: String): Unit = assert(cp.file(p1).get.getAbsolutePath == p2)

        root.scan(f ⇒ println(f.getAbsolutePath))
        assertRootPath("/info.txt", "/info.txt")
        assertRootPath("info.txt", "/info.txt")
        assertRootPath("./info.txt", "/info.txt")
        assertRootPath("./.././info.txt", "/info.txt")
        assertRootPath("../info.txt", "/info.txt")
        assertRootPath("../../info.txt", "/info.txt")
        assertRootPath("/bin/bin_info.txt", "/bin/bin_info.txt")
        assertRootPath("/bin/./bin_info.txt", "/bin/bin_info.txt")
        assertRootPath("/bin/././bin_info.txt", "/bin/bin_info.txt")
        assertRootPath("/bin/../bin/bin_info.txt", "/bin/bin_info.txt")
        assertRootPath("../bin/../bin/bin_info.txt", "/bin/bin_info.txt")

        assertPath(bin, "bin_info.txt", "/bin/bin_info.txt")
        assertPath(bin, "./../bin/bin_info.txt", "/bin/bin_info.txt")
        assertPath(bin, "./bin_info.txt", "/bin/bin_info.txt")




