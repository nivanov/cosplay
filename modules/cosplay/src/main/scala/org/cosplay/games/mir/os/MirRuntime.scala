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

import java.util.concurrent.*
import scala.collection.mutable
import org.cosplay.games.mir.*

/**
  *
  */
object MirRuntime:
    final val THREAD_POOL_SIZE = 16

/**
  *
  * @param fs
  * @param con
  * @param host
  */
class MirRuntime(fs: MirFileSystem, con: MirConsole, host: String):
    import MirRuntime.*

    private val procs = mutable.HashMap.empty[Long, MirProcess]
    private val exec = Executors.newFixedThreadPool(THREAD_POOL_SIZE)
    private var pidGen = 1L

    /**
      *
      * @param parent Process that invokes this method (a parent process).
      * @param file
      * @param cmdArgs
      * @param workDir
      * @param usr
      * @param vars
      * @param aliases
      * @param lastExit
      * @param in
      * @param out
      * @param err
      */
    def exec(
        parent: Option[MirProcess],
        file: MirExecutableFile,
        cmdArgs: Seq[String],
        workDir: MirDirectoryFile,
        usr: MirUser,
        vars: mutable.HashMap[String, String],
        aliases: mutable.HashMap[String, String],
        lastExit: Int,
        in: MirInputStream = MirInputStream.nullStream(),
        out: MirOutputStream = MirOutputStream.consoleStream(con),
        err: MirOutputStream = MirOutputStream.consoleStream(con)): MirProcess =
        var queued = true
        var code: Option[Int] = None
        val submitTs = MirClock.now()
        var finishTs = -1L
        var startTs: Long = 0
        val pid = pidGen
        val ppid = parent.or(_.getPid, 0L)
        pidGen += 1

        val ctx = MirExecutableContext(
            pid,
            ppid,
            file,
            cmdArgs,
            con,
            this,
            fs,
            host,
            workDir,
            vars,
            aliases,
            lastExit,
            usr,
            in,
            out,
            err
        )

        val fut = exec.submit(new Callable[Int]() {
            override def call(): Int =
                queued = false
                startTs = MirClock.now()
                try
                    code = file.getExec.mainEntry(ctx).?
                catch
                    case _: InterruptedException => ()
                    case e: Exception => err.println(e.getLocalizedMessage)
                finishTs = MirClock.now()
                code.getOrElse(-1)
        })

        val proc = new MirProcess:
            override def getOwner: MirUser = usr
            override def getPid: Long = pid
            override def getParent: Option[MirProcess] = parent
            override def getProgramFile: MirExecutableFile = file
            override def getWorkingDirectory: MirDirectoryFile = workDir
            override def getCmdArguments: Seq[String] = cmdArgs
            override def getStartTime: Long = startTs
            override def getSubmitTime: Long = submitTs
            override def isQueued: Boolean = queued
            override def isDone: Boolean = fut.isDone
            override def kill(): Boolean = fut.cancel(true)
            override def isKilled: Boolean = fut.isCancelled
            override def exitCode(ms: Long = Long.MaxValue): Option[Int] = fut.get(ms, TimeUnit.MILLISECONDS).?
            override def getFinishTime: Long = finishTs

        procs.synchronized {
            procs.put(pid, proc)
        }

        proc

    /**
      *
      */
    def listAll: Seq[MirProcess] = procs.synchronized {
        procs.values.toSeq
    }

    /**
      *
      */
    def listRunning: Seq[MirProcess] = procs.synchronized {
        procs.values.filter(_.isRunning).toSeq
    }

    /**
      *
      * @param pid
      */
    def getProcess(pid: Long): Option[MirProcess] = procs.synchronized {
        procs.get(pid)
    }

    /**
      *
      */
    def kill(pid: Long): Boolean = procs.synchronized {
        procs.get(pid) match
            case Some(p) =>
                p.kill()
                procs.remove(pid)
                true
            case None => false
    }

