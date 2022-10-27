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
import org.cosplay.games.mir.os.MirFileType.*

import java.io.*
import scala.collection.mutable

/**
  *
  */
object MirDirectoryFile:
    /**
      * Creates file system root directory.
      *
      * @param owner Root user.
      * @param initMs Initial creation and update timestamp. Defaults to the current time.
      */
    def mkRoot(owner: MirUser, initMs: Long = MirClock.now()): MirDirectoryFile =
        require(owner.isRoot)
        MirDirectoryFile("", owner, None, true, false, initMs)

import org.cosplay.games.mir.os.MirFileSystem.*

/**
  *
  * @param name Name of file (not including its path).
  * @param owner User owner of this file.
  * @param dir Parent directory of this directory or `None` if this is a root directory.
  * @param otherAcs Can others read or execute. Owner can do anything.
  * @param otherMod Can others change or delete. Owner can do anything.
  * @param initMs Initial creation and update timestamp. Defaults to the current time.
  */
class MirDirectoryFile(
    name: String,
    owner: MirUser,
    dir: Option[MirDirectoryFile],
    otherAcs: Boolean = true,
    otherMod: Boolean = false,
    initMs: Long = MirClock.now()
) extends MirFile(FT_DIR, name, owner, dir, otherAcs, otherMod, initMs) with MirFileDirectory with Iterable[MirFile]:
    private val children = mutable.ArrayBuffer.empty[MirFile]

    /** */
    val isRoot: Boolean = dir.isEmpty

    /** */
    val root: MirDirectoryFile =
        if isRoot then this
        else
            var p = this
            while p.getDir.isDefined do p = p.getDir.get
            p

    override def iterator: Iterator[MirFile] = children.iterator

    /**
      * Depth-first scan.
      *
      * @param f Callback for each scanned file that was accepted by the filter.
      * @param p Optional file filter.
      */
    def scan(f: MirFile => Unit, p: MirFile => Boolean = _ => true): Unit =
        for child <- children do
            if p(child) then f(child)
            child match
                case d: MirDirectoryFile => d.scan(f, p)
                case _ => ()

    /**
      *
      * @param file
      */
    @throws[CPException]
    def addFile(file: MirFile): Unit =
        require(isRoot || file.getDir.get == this)
        if file.getName == "." then throw E(s"File name '.' is reserved.")
        if file.getName == ".." then throw E(s"File name '..' is reserved.")
        if children.exists(_.getName == file.getName) then throw E(s"This directory already has a file with name: ${file.getName}")
        file match
            case d: MirDirectoryFile => require(!d.isRoot)
            case _ => ()
        children += file

    /**
      *
      * @param name Name of file (not including its path).
      * @param owner User owner of this file.
      * @param otherAcs Can others read or execute. Owner can do anything.
      * @param otherMod Can others change or delete. Owner can do anything.
      * @param lines Optional file content as sequence of individual lines.
      */
    @throws[CPException]
    @throws[IOException]
    def addRegFile(name: String, owner: MirUser, otherAcs: Boolean = false, otherMod: Boolean = false, lines: Seq[String] =  Seq.empty): MirRegularFile =
        val f = new MirRegularFile(name, owner, this, otherAcs, otherMod, lines)
        addFile(f)
        if lines.nonEmpty then
            val out = f.getOutput(false)
            try lines.foreach(out.println)
            finally out.close()
        f

    /**
      *
      * @param name Name of file (not including its path).
      * @param owner User owner of this file.
      * @param exe Executable program.
      * @param otherAcs Can others read or execute. Owner can do anything.
      * @param otherMod Can others change or delete. Owner can do anything.
      */
    @throws[CPException]
    def addExecFile(name: String, owner: MirUser, exe: MirExecutable, otherAcs: Boolean = false, otherMod: Boolean = false): MirExecutableFile =
        val f = new MirExecutableFile(name, owner, this, exe, otherAcs, otherMod)
        addFile(f)
        f

    /**
      *
      * @param name Name of file (not including its path).
      * @param owner User owner of this file.
      * @param drv Device driver.
      */
    @throws[CPException]
    def addDeviceFile(name: String, owner: MirUser, drv: MirDeviceDriver): MirDeviceFile =
        val f = new MirDeviceFile(name, owner, this, drv)
        addFile(f)
        f

    /**
      *
      * @param name Name of file (not including its path).
      * @param owner User owner of this file.
      * @param otherAcs Can others read or execute. Owner can do anything.
      * @param otherMod Can others change or delete. Owner can do anything.
      * @param initMs Initial creation and update timestamp. Defaults to the current time.
      */
    @throws[CPException]
    def addDirFile(
        name: String,
        owner: MirUser,
        otherAcs: Boolean = true,
        otherMod: Boolean = false,
        initMs: Long = MirClock.now()): MirDirectoryFile =
        val d = new MirDirectoryFile(name, owner, this.?, otherAcs, otherMod, initMs)
        addFile(d)
        d

    /**
      *
      * @param file
      */
    def removeFile(file: MirFile): Boolean =
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
      * @param p Optional file predicate.
      */
    def list(p: MirFile => Boolean = _ => true): Seq[MirFile] = children.filter(p).toSeq

    /**
      *
      * @param path Relative or fully qualified path.
      */
    def resolve(path: String): Option[MirFile] =
        val p = path.strip()
        var f: MirFile = if p.startsWith(PATH_SEP) then root else this
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
                    case d: MirDirectoryFile => if !d.isRoot then f = d.getDir.get
                    case _ => failed = true
                case name: String => f match
                    case d: MirDirectoryFile =>
                        d.children.find(_.getName == name) match
                            case Some(x) => f = x
                            case None => failed = true
                    // Non-directory can only be the last element.
                    case x => if i == n - 1 && x.getName == name then f = x else failed = true
            i += 1
        if failed then None else Some(f)

    /**
      *
      * @param path Relative to this directory or fully qualified path.
      */
    def file[T <: MirFile](path: String): Option[T] = resolve(path) match
        case Some(f) => f match
            case x: T => Some(x)
            case _ => None
        case None => None
