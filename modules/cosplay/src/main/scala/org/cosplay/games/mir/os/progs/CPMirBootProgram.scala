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

package org.cosplay.games.mir.os.progs

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
import os.*
import CPMirConsole.*

/**
  *
  */
class CPMirBootProgram extends CPMirExecutable:
    override def mainEntry(ctx: CPMirExecutableContext): Int =
        val boot = s"""
            |Award Modular BIOS v4.50G, An Energy Star Ally
            |Copyright (C) 1984-92, Award Software, Inc.
            |
            |80486DX2 CPU at 66MHz
            |64MB memory
            |
            |Starting MirX...
            |
            | /88      /88 /88          /88   /88
            || 888    /888|__/         | 88  / 88
            || 8888  /8888 /88  /888888|  88/ 88/
            || 88 88/88 88| 88 /88__  88\\  8888/
            || 88  888| 88| 88| 88  \\__/ >88  88
            || 88\\  8 | 88| 88| 88      /88/\\  88
            || 88 \\/  | 88| 88| 88     | 88  \\ 88
            ||__/     |__/|__/|__/     |__/  |__/
            |
            |ver 1.12.04, Dec 12, 1993
            |Copyright (C) 1991-94, RCS "Energia", Russia
            |
            |Runtime system initialized: ${CPMirRuntime.THREAD_POOL_SIZE} threads
            |System clock synchronized: ${CPMirClock.formatTimeDate()}
            |
            |""".stripMargin

        val fs = ctx.fs
        val out = ctx.out

        boot.split("\n").foreach(out.println)

        out.println()
        out.println("Pressurized modules:")
        for mod <- stateMgr.state.station.allModules do
            out.println(s"  |- ${mod.getName} ('${mod.getAbbreviation}') - OK.")

        out.println()
        out.println("Device map:")
        val devDir = fs.dirFile("/dev")
        devDir.list().foreach(f => out.println(s"  |- '${f.getAbsolutePath}' initialized."))

        // Only show the 1st time.
        if stateMgr.state.osRebootCnt == 1 then
            out.println()
            out.println("MirX rebooted due to:")
            out.println("  |- Fault 0x11F0 (power supply interruption)")
            out.println("  |- Fault 0x10B7 (docking module 'dck' - structural integrity sensors)")
            out.println("  |- Fault 0x217A (docking module 'dck' - oxygen sensor readout)")

        out.println()
        out.println("Users verified:")
        val passwd = fs.regFile("/etc/passwd").readLines
        for line <- passwd do
            val parts = line.split(":")
            out.println(s"  |- ${parts.head}, ${parts(2)} -> ${parts(3)}")

        ctx.exec(fs.file("/sbin/login").get, Seq.empty)

        // Return code.
        0
