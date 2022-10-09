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

import java.io.*

/**
  * Wrapper for input stream.
  */
class MirInputStream(impl: InputStream) extends Closeable:
    /**
      *
      */
    @throws[IOException]
    def available(): Int = impl.available()

    /**
      *
      */
    @throws[IOException]
    def close(): Unit = impl.close()

    /**
      *
      */
    @throws[IOException]
    def read(): Int = impl.read()

    /**
      *
      */
    @throws[IOException]
    def readLines(): Seq[String] = readAllBytes().map(_.toChar).mkString("", "", "").split("[\r\n]").toSeq

    /**
      *
      * @param arr
      */
    @throws[IOException]
    def read(arr: Array[Byte]): Int = impl.read(arr)

    /**
      *
      * @param arr
      * @param off
      * @param len
      */
    @throws[IOException]
    def read(arr: Array[Byte], off: Int, len: Int): Int = impl.read(arr, off, len)

    /**
      *
      */
    @throws[IOException]
    def readAllBytes(): Array[Byte] = impl.readAllBytes()

    /**
      *
      * @param arr
      * @param off
      * @param len
      */
    @throws[IOException]
    def readNBytes(arr: Array[Byte], off: Int, len: Int): Int = impl.readNBytes(arr, off, len)

    /**
      *
      * @param len
      */
    @throws[IOException]
    def readNBytes(len: Int): Array[Byte] = impl.readNBytes(len)

    /**
      *
      * @param n
      */
    @throws[IOException]
    def skip(n: Long): Long = impl.skip(n)

/**
  *
  */
object MirInputStream:
    /**
      *
      */
    def nullStream(): MirInputStream = MirInputStream(InputStream.nullInputStream())

    /**
      *
      * @param impl
      */
    def nativeStream(impl: InputStream): MirInputStream = MirInputStream(impl)


