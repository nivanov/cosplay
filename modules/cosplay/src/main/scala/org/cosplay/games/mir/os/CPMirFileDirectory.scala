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
trait CPMirFileDirectory:
    /**
      *
      * @param path Relative to root or fully qualified path.
      */
    def file[T <: CPMirFile](path: String): Option[T]

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
    private def mandatoryFile[T <: CPMirFile](path: String): T = file[T](path) match
        case None => throw E(s"File is missing: $path")
        case Some(f) => f

    /**
      *
      * @param path Relative to root or fully qualified path.
      */
    def regFile(path: String): CPMirRegularFile = mandatoryFile[CPMirRegularFile](path)

    /**
      *
      * @param path Relative to root or fully qualified path.
      */
    def execFile(path: String): CPMirExecutableFile = mandatoryFile[CPMirExecutableFile](path)

    /**
      *
      * @param path Relative to root or fully qualified path.
      */
    def devFile(path: String): CPMirDeviceFile = mandatoryFile[CPMirDeviceFile](path)

    /**
      *
      * @param path Relative to root or fully qualified path.
      */
    def dirFile(path: String): CPMirDirectoryFile = mandatoryFile[CPMirDirectoryFile](path)

    /**
      *
      * @param path Relative to root or fully qualified path.
      */
    def regFileOpt(path: String): Option[CPMirRegularFile] = file[CPMirRegularFile](path)

    /**
      *
      * @param path Relative to root or fully qualified path.
      */
    def execFileOpt(path: String): Option[CPMirExecutableFile] = file[CPMirExecutableFile](path)

    /**
      *
      * @param path Relative to root or fully qualified path.
      */
    def devFileOpt(path: String): Option[CPMirDeviceFile] = file[CPMirDeviceFile](path)

    /**
      *
      * @param path Relative to root or fully qualified path.
      */
    def dirFileOpt(path: String): Option[CPMirDirectoryFile] = file[CPMirDirectoryFile](path)


