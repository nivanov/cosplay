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
import org.cosplay.games.mir.*
import org.cosplay.games.mir.os.*
import org.cosplay.games.mir.os.CPMirFileType.*
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

import org.cosplay.games.mir.os.CPMirFileSystem.*

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
) extends CPMirFile(FT_DIR, name, owner, parent) with Iterable[CPMirFile]:
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

    override def iterator: Iterator[CPMirFile] = children.iterator

    /**
      * Depth-first scan.
      *
      * @param f Callback for each scanned file that was accepted by the filter.
      * @param p Optional file filter.
      */
    def scan(f: CPMirFile => Unit, p: CPMirFile => Boolean = _ => true): Unit =
        for child <- children do
            if p(child) then f(child)
            child match
                case d: CPMirDirectoryFile => d.scan(f, p)
                case _ => ()

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
            case d: CPMirDirectoryFile => require(!d.isRoot)
            case _ => ()
        children += file

    /**
      *
      * @param file
      */
    def removeFile(file: CPMirFile): Boolean =
        children.indexOf(file) match
            case -1 => false
            case idx =>
                children.remove(idx)
                true

    /**
      *
      * @param name
      */
    def removeFile(name: String): Boolean =
        children.indexWhere(_.getName == name) match
            case -1 => false
            case idx =>
                children.remove(idx)
                true

    /**
      *
      * @param p File predicate.
      */
    def list(p: CPMirFile => Boolean): Seq[CPMirFile] = children.filter(p).toSeq

    /**
      *
      * @param path Relative or fully qualified path.
      */
    def file(path: String): Option[CPMirFile] =
        val p = path.strip()
        var f: CPMirFile = if p.startsWith(PATH_SEP) then root else this
        val parts = p.split(PATH_SEP).filter(_.nonEmpty)
        val n = parts.length
        var i = 0
        var failed = false
        while i < n && !failed do
            val part = parts(i)
            part match
                case "." => () // Ignore any '.'.
                case ".." => f match
                    // Ignore spurious '..'.
                    case d: CPMirDirectoryFile => if !d.isRoot then f = d.getParent.get
                    case _ => failed = true
                case name: String => f match
                    case d: CPMirDirectoryFile =>
                        d.children.find(_.getName == name) match
                            case Some(x) => f = x
                            case None => failed = true
                    // Non-directory can only be the last element.
                    case x => if i == n - 1 && x.getName == name then f = x else failed = true
            i += 1
        if failed then None else Some(f)

    /**
      *
      * @param path Relative or fully qualified path.
      */
    def dir(path: String): Option[CPMirDirectoryFile] = file(path) match
        case Some(f) => f match
            case d: CPMirDirectoryFile => Some(d)
            case _ => None
        case None => None

    /**
      *
      * @param path Relative or fully qualified path.
      */
    def exist(path: String): Boolean = file(path).isDefined

