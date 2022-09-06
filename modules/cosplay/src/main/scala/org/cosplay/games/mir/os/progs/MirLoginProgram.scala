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

import org.cosplay.games.mir.*
import org.cosplay.games.mir.os.progs.MirLoginProgram.MIN_PWD_LEN
import os.*

/*
   _________            ______________
   __  ____/_______________  __ \__  /_____ _____  __
   _  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
   / /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
   \____/  \____//____/ /_/     /_/  \__,_/ _\__, /
                                            /____/

          2D ASCII JVM GAME ENGINE FOR SCALA3
              (C) 2021 Rowan Games, Inc.
                ALl rights reserved.
*/

/**
  *
  */
object MirLoginProgram:
    private final val MIN_PWD_LEN = 4
    private final val ERR_PREFIX = "  |-"

/**
  *
  */
class MirLoginProgram extends MirExecutable:
    import MirLoginProgram.*
    import MirConsole.*

    override def mainEntry(ctx: MirExecutableContext): Int =
        val out = ctx.out
        val con = ctx.con

        val ply = stateMgr.state.player
        val username = ply.getUsername
        val tty = "tty0"

        out.println()

        def err(s: String): Unit = con.println(s"$ERR_PREFIX $CTRL_BEEP${CTRL_REV_COL}error$CTRL_RST_COL $s")

        var done = false
        while !done do
            val login = con.promptReadLine("Login: ")
            con.println()
            if login != username then
                err(s"only ${ply.getCrewMember.get.nameCamelCase} ($username) ia authorized to login at this terminal ($tty).")
            else
                done = true

        con.println(s"Reset password for '$username' due to system fault restart.")
        con.println(s"Password must be at least $MIN_PWD_LEN characters.")

        done = false
        while !done do
            val passwd1 = con.promptReadLine("Password: ", Option('*'))
            val passwd2 = con.promptReadLine("\nConfirm password: ", Option('*'))
            con.println()
            if passwd1 != passwd2 then err(s"passwords do not match.")
            else if passwd1.length < MIN_PWD_LEN then err(s"password is too short (must be at least $MIN_PWD_LEN characters).")
            else
                done = true
                stateMgr.state.player.setPassword(passwd1)
                con.println(s"Password reset for '$username'.")

        val lastLoginTstamp = stateMgr.state.lastLoginTstamp

        stateMgr.state.lastLoginTstamp = MirClock.now()

        con.println(s"Last login ${MirClock.formatTimeDate(lastLoginTstamp)} on $tty.")
        con.println(s"Welcome back, ${ply.crew.camelFirstName}.")

        0

