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

package org.cosplay.games.mir.os.fs

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
import games.mir.*
import os.fs.*
import os.*
import CPMirFileType.*
import scala.collection.mutable

/**
  *
  */
object CPMirDirectoryFile:
    /**
      * Creates file system root directory.
      *
      * @param owner Root user.
      */
    def mkRoot(owner: CPMirUser): CPMirDirectoryFile =
        require(owner.isRoot)
        CPMirDirectoryFile("", owner, None)

import CPMirFileSystem.*

/**
  *
  * @param name
  * @param owner
  * @param parent
  */
class CPMirDirectoryFile(
    name: String,
    owner: CPMirUser,
    parent: Option[CPMirDirectoryFile]
) extends CPMirFile(FT_DIR, name, owner, parent):
    private val children = mutable.ArrayBuffer.empty[CPMirFile]

    /**
      *
      * @param name
      * @param owner
      * @param parent
      */
    def this(name: String, owner: CPMirUser, parent: CPMirDirectoryFile) = this(name, owner, Some(parent))

    /** */
    val isRoot: Boolean = parent.isEmpty

    /** */
    val root: CPMirDirectoryFile =
        if isRoot then this
        else
            var p = this
            while p.getParent.isDefined do p = p.getParent.get
            p

    /**
      *
      * @param file
      */
    @throws[CPException]
    def addFile(file: CPMirFile): Unit =
        require(isRoot || file.getParent.get == this)
        if file.getName == "." then throw E(s"File name '.' is reserved.")
        if file.getName == ".." then throw E(s"File name '..' is reserved.")
        if children.exists(_.getName == file.getName) then throw E(s"This directory already has a file with name: ${file.getName}")
        file match
            case d: CPMirDirectoryFile ⇒ require(!d.isRoot)
            case _ ⇒ ()
        children += file

    /**
      *
      * @param file
      */
    def removeFile(file: CPMirFile): Boolean =
        children.indexOf(file) match
            case -1 ⇒ false
            case idx ⇒
                children.remove(idx)
                true

    /**
      *
      * @param name
      * @return
      */
    def removeFile(name: String): Boolean =
        children.indexWhere(_.getName == name) match
            case -1 ⇒ false
            case idx ⇒
                children.remove(idx)
                true

    /**
      *
      * @param p File predicate.
      */
    def list(p: CPMirFile ⇒ Boolean): Seq[CPMirFile] = children.filter(p).toSeq

    /**
      *
      * @param path Relative or fully qualified path.
      * @return
      */
    def file(path: String): Option[CPMirFile] =
        val p = path.strip()
        var f: CPMirDirectoryFile = if p.startsWith(PATH_SEP) then root else this
        val parts = p.split(PATH_SEP)
        val n = parts.length
        var i = 0
        var failed = false
        while i < n && !failed do
            val part = parts(i)
            part match
                case "." ⇒ ()
                case ".." ⇒ if !f.isRoot then f = f.getParent.get
                case name: String ⇒
                    children.find(_.getName == name) match
                        case Some(x) ⇒ x match
                            case d: CPMirDirectoryFile ⇒ f = d
                            case _ ⇒ failed = true
                        case None ⇒ failed = true
            i += 1
        if failed then None else Some(f)



    /**
      *
      * @param path Relative or fully qualified path.
      * @return
      */
    def exist(path: String): Boolean = file(path).isDefined

