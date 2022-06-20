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
class CPMirOs(fs: CPMirFileSystem, users: Seq[CPMirUser]) extends Serializable:
    require(users.exists(_.isRoot))

    private val rootUsr = users.find(_.isRoot).get

    /**
      *
      */
    inline def getFs: CPMirFileSystem = fs

    /**
      *
      */
    inline def getRootUser: CPMirUser = rootUsr

    /**
      *
      */
    inline def getAllUsers: Seq[CPMirUser] = users

    /**
      *
      */
    def boot(con: CPMirConsole): Unit =
        // TODO: test only.
        val dummy =
            """
              |/*
              | * Licensed to the Apache Software Foundation (ASF) under one or more
              | * contributor license agreements.  See the NOTICE file distributed with
              | * this work for additional information regarding copyright ownership.
              | * The ASF licenses this file to You under the Apache License, Version 2.0
              | * (the "License"); you may not use this file except in compliance with
              | * the License.  You may obtain a copy of the License at
              | *
              | *      https://www.apache.org/licenses/LICENSE-2.0
              | *
              | * Unless required by applicable law or agreed to in writing, software
              | * distributed under the License is distributed on an "AS IS" BASIS,
              | * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
              | * See the License for the specific language governing permissions and
              | * limitations under the License.
              | */
              |
              |package org.cosplay.games.mir.scenes
              |
              |import org.cosplay.*
              |import games.mir.*
              |import sprites.*
              |
              |/*
              |   _________            ______________
              |   __  ____/_______________  __ \__  /_____ _____  __
              |   _  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
              |   / /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
              |   \____/  \____//____/ /_/     /_/  \__,_/ _\__, /
              |                                            /____/
              |
              |          2D ASCII GAME ENGINE FOR SCALA3
              |            (C) 2021 Rowan Games, Inc.
              |               ALl rights reserved.
              |*/
              |
              |object CPMirMainScene extends CPMirCrtSceneBase("main", "bg5.wav"):
              |    private val conSpr = CPMirConsoleSprite()
              |
              |    addObjects(
              |        conSpr,
              |        // Add full-screen shaders - order is important.
              |        new CPOffScreenSprite(shaders = Seq(crtShdr, fadeInShdr, fadeOutShdr))
              |    )
              |""".stripMargin.split("\n")

        Future {
            while true do
                for s ← dummy do
                    con.println(s)
                    Thread.sleep(CPRand.between(500, 1000))
        }

    /**
      *
      */
    def shutdown(): Unit = ???
