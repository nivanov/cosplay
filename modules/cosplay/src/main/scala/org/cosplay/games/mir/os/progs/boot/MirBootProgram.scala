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

package org.cosplay.games.mir.os.progs.boot

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
import org.cosplay.games.mir.os.MirConsole.*

/**
  *
  */
class MirBootProgram extends MirExecutable:
    override def mainEntry(ctx: MirExecutableContext): Int =
        val boot =
            s"""Award Modular BIOS v4.50G, An Energy Star Ally
            |Copyright (C) 1984-92, Award Software, Inc.
            |
            |80486DX2 CPU at 66MHz
            |64MB memory
            |
            |Starting MirX...
            |Runtime system initialized: ${MirRuntime.THREAD_POOL_SIZE} threads
            |System clock synchronized: ${MirClock.formatNowTimeDate()}
            |""".stripMargin

        val logo = s"""
            | /88      /88 /88          /88   /88
            || 888    /888|__/         | 88  / 88
            || 8888  /8888 /88  /888888|  88/ 88/
            || 88 88/88 88| 88 /88__  88\\  8888/
            || 88  888| 88| 88| 88  \\__/ >88  88
            || 88\\  8 | 88| 88| 88      /88/\\  88
            || 88 \\/  | 88| 88| 88     | 88  \\ 88
            ||__/     |__/|__/|__/     |__/  |__/
            |
            |ver ${MirOs.VERSION}, Dec 12, 1993
            |Copyright (C) 1991-94, RCS "Energia", Russia
            |""".
        stripMargin

        val fs = ctx.fs
        val out = ctx.out

        boot.split("\n").foreach(out.println)

        out.println()
        out.println("Pressurized modules:")
        val mods = stateMgr.state.station.allModules
        for (mod, idx) <- mods.zipWithIndex do
            val sep = if idx == mods.length - 1 then "+-" else "|-"
            out.println(s"  $sep ${mod.name} ('${mod.abbreviation}') - OK.")

        out.println()
        out.println("Device map:")
        val devDir = fs.dirFile("/dev")
        val devs = devDir.list()
        for (file, idx) <- devs.zipWithIndex do
            val sep = if idx == devs.length - 1 then "+-" else "|-"
            out.println(s"  $sep '${file.getAbsolutePath}' initialized.")

        // Only show the 1st time.
        if stateMgr.state.osRebootCnt == 1 then
            out.println()
            out.println("MirX rebooted due to:")
            out.println("  |- Fault 0x11F0 (power supply interruption)")
            out.println("  |- Fault 0x10B7 (docking module 'dck' - structural integrity sensors)")
            out.println("  +- Fault 0x217A (docking module 'dck' - oxygen sensor readout)")

        out.println()
        out.println("Users verified:")
        val pwds = fs.regFile("/etc/passwd").readLines
        for (line, idx) <- pwds.zipWithIndex do
            val parts = line.split(":")
            val username = parts.head
            if username != "root" then
                val sep = if idx == pwds.length - 1 then "+-" else "|-"
                val base = s"  $sep $username, ${parts(2)} -> ${parts(3)}"
                if username == stateMgr.state.player.getUsername then out.println(s"$base (*)") else out.println(base)

        logo.split("\n").foreach(out.println)

        // Start 'sbin/login' and wait indefinitely.
        ctx.fork(fs.file("/sbin/login").get, Seq.empty).exitCode().getOrElse(-1)
