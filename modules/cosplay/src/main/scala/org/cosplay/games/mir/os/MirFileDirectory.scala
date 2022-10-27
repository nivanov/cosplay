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

/**
  * Directory read-only access API.
  */
trait MirFileDirectory:
    /**
      *
      * @param path Relative to root or fully qualified path.
      */
    def file[T <: MirFile](path: String): Option[T]

    /**
      *
      * @param path Relative to root or fully qualified path.
      */
    def exist(path: String): Boolean = file(path).isDefined

    /**
      *
      * @param path
      * @tparam T
      */
    private def mandatoryFile[T <: MirFile](path: String): T = file[T](path).getOrThrow(E(s"File is missing: $path"))

    /**
      *
      * @param path Relative to root or fully qualified path.
      */
    def regFile(path: String): MirRegularFile = mandatoryFile[MirRegularFile](path)

    /**
      *
      * @param path Relative to root or fully qualified path.
      */
    def execFile(path: String): MirExecutableFile = mandatoryFile[MirExecutableFile](path)

    /**
      *
      * @param path Relative to root or fully qualified path.
      */
    def devFile(path: String): MirDeviceFile = mandatoryFile[MirDeviceFile](path)

    /**
      *
      * @param path Relative to root or fully qualified path.
      */
    def dirFile(path: String): MirDirectoryFile = mandatoryFile[MirDirectoryFile](path)

    /**
      *
      * @param path Relative to root or fully qualified path.
      */
    def regFileOpt(path: String): Option[MirRegularFile] = file[MirRegularFile](path)

    /**
      *
      * @param path Relative to root or fully qualified path.
      */
    def execFileOpt(path: String): Option[MirExecutableFile] = file[MirExecutableFile](path)

    /**
      *
      * @param path Relative to root or fully qualified path.
      */
    def devFileOpt(path: String): Option[MirDeviceFile] = file[MirDeviceFile](path)

    /**
      *
      * @param path Relative to root or fully qualified path.
      */
    def dirFileOpt(path: String): Option[MirDirectoryFile] = file[MirDirectoryFile](path)


