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
import games.mir.*
import org.cosplay.games.mir.station.CPMirCrewMember

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
object CPMirUser:
    private val ROOT_UID = 0 // Same as in Unix, i.e. root UID is zero.
    private var usrIdGen = 1

    /**
      *
      */
    private def genUserId: Int =
        val id = usrIdGen
        usrIdGen += 1
        id

    /**
      *
      * @param username
      * @param password
      * @param player
      */
    def apply(username: String, password: String, player: Option[CPMirCrewMember]): CPMirUser =
        require(username != null && username.nonEmpty)
        require(password != null && password.nonEmpty)

        new CPMirUser :
            private val id = genUserId
            private var pwd = password

            override def getCrewMember: Option[CPMirCrewMember] = player
            override def getUid: Int = id
            override def getPassword: String = pwd
            override def setPassword(pwd: String): Unit =
                require(pwd != null && pwd.nonEmpty)
                this.pwd = pwd
            override def getUsername: String = username
            override def isRoot: Boolean = false

    /**
      *
      */
    def mkRoot(): CPMirUser =
        new CPMirUser:
            private var pwd = CPRand.guid6

            override def getCrewMember: Option[CPMirCrewMember] = None
            override def getUid: Int = ROOT_UID
            override def getPassword: String = pwd
            override def getUsername: String = "root"
            override def setPassword(pwd: String): Unit =
                require(pwd != null && pwd.nonEmpty)
                this.pwd = pwd
            override def isRoot: Boolean = true

/**
  *
  */
trait CPMirUser extends Serializable:
    /**
      *
      */
    def getCrewMember: Option[CPMirCrewMember]

    /**
      *
      */
    def crew: CPMirCrewMember =
        require(!isRoot)
        getCrewMember.get

    /**
      *
      */
    def isRoot: Boolean

    /**
      *
      */
    def getUsername: String

    /**
      *
      */
    def getPassword: String

    /**
      *
      */
    def getHomeDirectory: String = s"/home/$getUsername"

    /**
      *
      * @param pwd
      */
    def setPassword(pwd: String): Unit

    /**
      *
      */
    def getUid: Int
