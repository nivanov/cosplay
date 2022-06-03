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

import org.cosplay.*
import CPMirFileType.*

/**
  *
  * @param typ
  * @param name
  * @param owner
  * @param parent
  * @param otherRead
  * @param otherWrite
  */
@SerialVersionUID(1_0_0L)
abstract class CPMirFile(
    typ: CPMirFileType,
    private var name: String,
    private var owner: CPMirUser,
    private var parent: Option[CPMirFile],
    private var otherRead: Boolean = false,
    private var otherWrite: Boolean = false
) extends Serializable:
    require(parent.isEmpty || parent.get.getType == FT_DIR)

    private var createTs = CPMirClock.now()
    private var updateTs = CPMirClock.now()
    private var size = 0L

    final val guid = CPRand.guid

    override def hashCode(): Int = guid.hashCode()
    override def equals(obj: Any): Boolean =
        obj match
            case f: CPMirFile ⇒ f.guid == guid
            case _ ⇒ false

    /**
      *
      * @return
      */
    def getParent: Option[CPMirFile] = parent

    /**
      *
      * @param parent
      */
    def setParent(parent: Option[CPMirFile]): Unit = this.parent = parent

    /**
      *
      * @return
      */
    def getName: String = name

    /**
      *
      * @param name
      */
    def setName(name: String): Unit = this.name = name

    /**
      *
      * @return
      */
    def getCreateTimestamp: Long = createTs

    /**
      *
      * @return
      */
    def getUpdateTimestamp: Long = updateTs

    /**
      *
      */
    def touch(): Unit = updateTs = CPMirClock.now()

    /**
      *
      * @param createTs
      */
    def setCreateTimestamp(createTs: Long): Unit = this.createTs = createTs

    /**
      *
      * @param updateTs
      */
    def setUpdateTimestamp(updateTs: Long): Unit = this.updateTs = updateTs

    /**
      *
      * @return
      */
    def getOwner: CPMirUser = owner

    /**
      *
      * @param owner
      */
    def setOwner(owner: CPMirUser): Unit = this.owner = owner

    /**
      *
      * @return
      */
    def getSize: Long = size

    /**
      *
      * @param size
      */
    def setSize(size: Long): Unit = this.size = size

    /**
      *
      * @return
      */
    def canOtherRead: Boolean = otherRead

    /**
      *
      * @param otherRead
      */
    def setOtherRead(otherRead: Boolean): Unit = this.otherRead = otherRead

    /**
      *
      * @param otherWrite
      */
    def setOtherWrite(otherWrite: Boolean): Unit = this.otherWrite = otherWrite

    /**
      *
      * @return
      */
    def canOtherWrite: Boolean = otherWrite

    /**
      *
      * @return
      */
    def getType: CPMirFileType = typ

