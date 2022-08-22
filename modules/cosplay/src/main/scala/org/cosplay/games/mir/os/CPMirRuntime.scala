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
object CPMirRuntime:
    final val THREAD_POOL_SIZE = 16

/**
  *
  * @param fs
  * @param con
  * @param clock
  */
class CPMirRuntime(fs: CPMirFileSystem, con: CPMirConsole, clock: CPMirClock):
    import CPMirRuntime.*

    private val procs = mutable.HashMap.empty[Long, CPMirProcess]
    private val exec = Executors.newFixedThreadPool(THREAD_POOL_SIZE)
    private var pidGen = 1L

    /**
      *
      * @param parent
      * @param file
      * @param args
      * @param workDir
      * @param usr
      * @param env
      * @param in
      * @param out
      * @param err
      * @return
      */
    def exec(
        parent: Option[CPMirProcess],
        file: CPMirExecutableFile,
        args: Seq[String],
        workDir: CPMirDirectoryFile,
        usr: CPMirUser,
        env: Map[String, String],
        in: CPMirInputStream = CPMirInputStream.nullStream(),
        out: CPMirOutputStream = CPMirOutputStream.consoleStream(con),
        err: CPMirOutputStream = CPMirOutputStream.consoleStream(con)): CPMirProcess =
        var queued = true
        var code: Option[Int] = None
        val submitTs = clock.now()
        var finishTs = -1L
        var startTs: Long = 0
        val pid = pidGen
        pidGen += 1

        val ctx = CPMirExecutableContext(
            pid,
            args,
            con,
            this,
            clock,
            fs,
            workDir,
            env,
            usr,
            in,
            out,
            err
        )

        val fut = exec.submit(new Callable[Int]() {
            override def call(): Int =
                queued = false
                startTs = clock.now()
                try
                    code = Option(file.getExec.mainEntry(ctx))
                catch
                    case _: InterruptedException => ()
                    case e: Exception => err.println(e.getLocalizedMessage)
                finishTs = clock.now()
                code.getOrElse(-1)
        })

        val proc = new CPMirProcess:
            override def getOwner: CPMirUser = usr
            override def getPid: Long = pid
            override def getParent: Option[CPMirProcess] = parent
            override def getProgramFile: CPMirExecutableFile = file
            override def getWorkingDirectory: CPMirDirectoryFile = workDir
            override def getArguments: Seq[String] = args
            override def getStartTime: Long = startTs
            override def getSubmitTime: Long = submitTs
            override def isQueued: Boolean = queued
            override def isDone: Boolean = fut.isDone
            override def kill(): Boolean = fut.cancel(true)
            override def isKilled: Boolean = fut.isCancelled
            override def exitCode(ms: Long = Long.MaxValue): Option[Int] = Option(fut.get(ms, TimeUnit.MILLISECONDS))
            override def getFinishTime: Long = finishTs

        procs.synchronized {
            procs.put(pid, proc)
        }

        proc

    /**
      *
      */
    def listAll: Seq[CPMirProcess] = procs.synchronized {
        procs.values.toSeq
    }

    /**
      *
      */
    def listRunning: Seq[CPMirProcess] = procs.synchronized {
        procs.values.filter(_.isRunning).toSeq
    }

    /**
      *
      * @param pid
      */
    def get(pid: Long): Option[CPMirProcess] = procs.synchronized {
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

