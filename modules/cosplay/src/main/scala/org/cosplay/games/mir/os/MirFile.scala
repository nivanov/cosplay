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
import MirFileType.*
import MirFileSystem.*

import scala.collection.mutable
import games.mir.*

/**
  *
  * @param typ Type of the file.
  * @param name Name of file (not including its path).
  * @param owner User owner of this file.
  * @param dir Directory this file belongs to or `None` if this is a root directory file.
  * @param initMs Initial creation and update timestamp.
  * @param otherAccess Can others read or execute. Owner can do anything.
  * @param otherModify Can others change or delete. Owner can do anything.
  */
@SerialVersionUID(1_0_0L)
abstract class MirFile(
    typ: MirFileType,
    private var name: String,
    private var owner: MirUser,
    private var dir: Option[MirDirectoryFile],
    private var otherAccess: Boolean = false,
    private var otherModify: Boolean = false,
    private var initMs: Long
) extends Serializable:
    require((dir.isEmpty && typ == FT_DIR) || dir.nonEmpty)

    private var createTs = initMs
    private var updateTs = initMs
    private var size = 0L
    private var absPath = mkAbsolutePath()

    /** Unix 'inode' of the file. Only one storage device in MirX. */
    final val guid = CPRand.guid

    override def hashCode(): Int = guid.hashCode()
    override def equals(obj: Any): Boolean =
        obj match
            case f: MirFile => f.guid == guid
            case _ => false

    /**
      *
      */
    def getDir: Option[MirDirectoryFile] = dir

    /**
      *
      */
    def getAbsolutePath: String = absPath

    /**
      *
      */
    private def mkAbsolutePath(): String =
        val buf = mutable.ArrayBuffer.empty[String]
        var f: Option[MirFile] = Some(this)
        while f.isDefined do
            val v = f.get
            val p = v.getDir
            if p.isDefined then
                buf += v.getName
                buf += PATH_SEP
            f = p
        buf.toSeq.reverse.mkString

    /**
      *
      * @param dir
      */
    def setDir(dir: Option[MirDirectoryFile]): Unit =
        this.dir = dir
        absPath = mkAbsolutePath()

    /**
      *
      */
    def getName: String = name

    /**
      *
      * @param name
      */
    def setName(name: String): Unit =
        this.name = name
        absPath = mkAbsolutePath()

    /**
      *
      */
    def getCreateTimestamp: Long = createTs

    /**
      *
      */
    def getUpdateTimestamp: Long = updateTs

    /**
      *
      */
    def touch(): Unit = updateTs = MirClock.now()

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
      */
    def getOwner: MirUser = owner

    /**
      *
      * @param owner
      */
    def setOwner(owner: MirUser): Unit = this.owner = owner

    /**
      *
      */
    def getSize: Long = size

    /**
      *
      * @param size
      */
    def setSize(size: Long): Unit = this.size = size

    /**
      *
      */
    def canOtherAccess: Boolean = otherAccess

    /**
      *
      * @param otherAccess
      */
    def setOtherAccess(otherAccess: Boolean): Unit = this.otherAccess = otherAccess

    /**
      *
      * @param otherModify
      */
    def setOtherModify(otherModify: Boolean): Unit = this.otherModify = otherModify

    /**
      *
      */
    def canOtherModify: Boolean = otherModify

    /**
      *
      */
    def getType: MirFileType = typ

    override def toString: String = absPath

