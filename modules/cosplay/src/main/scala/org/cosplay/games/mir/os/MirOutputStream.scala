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

import org.cosplay.*
import java.io.*

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

/**
  *
  */
trait MirOutputStream extends Closeable, MirPrintable:
    /**
      *
      */
    @throws[IOException]
    def close(): Unit

    /**
      *
      * @param arr
      */
    @throws[IOException]
    def write(arr: Array[Byte]): Unit = write(arr, 0, arr.length)

    /**
      *
      * @param arr
      * @param off
      * @param len
      */
    @throws[IOException]
    def write(arr: Array[Byte], off: Int, len: Int): Unit =
        var i = 0
        while i < len do
            write(arr(off + i))
            i += 1

    /**
      *
      * @param b
      */
    @throws[IOException]
    def write(b: Int): Unit

/**
  *
  */
object MirOutputStream:
    /**
      *
      */
    def nullStream(): MirOutputStream =
        new MirOutputStream:
            private val impl = PrintStream(OutputStream.nullOutputStream())
            override def close(): Unit = impl.close()
            override def write(b: Int): Unit = impl.write(b)
            override def print(x: Any): Unit = impl.print(x.toString)

    /**
      *
      * @param con
      */
    def consoleStream(con: MirConsole): MirOutputStream =
        new MirOutputStream:
            private var closed = false

            def stutter(): Unit = Thread.sleep(CPRand.between(50L, 250L))

            override def close(): Unit = closed = true
            protected def ensureOpen(): Unit = if closed then throw new IOException("Stream closed.")
            override def write(b: Int): Unit =
                ensureOpen()
                con.print(s"${b.toChar}")

            override def println(x: Any): Unit =
                super.println(x)
                stutter() // Emulate "slow performance".
            override def print(x: Any): Unit =
                ensureOpen()
                con.print(x)

    /**
      *
      * @param impl
      */
    def nativeStream(impl: OutputStream): MirOutputStream =
        new MirOutputStream:
            private val ps = PrintStream(impl)
            override def close(): Unit = ps.close()
            override def write(b: Int): Unit = if !MirConsole.isControl(b.toChar) then ps.write(b)
            override def print(x: Any): Unit = ps.print(x.toString.filter(ch => !MirConsole.isControl(ch)))


