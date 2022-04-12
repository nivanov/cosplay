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

package org.cosplay.impl

import java.time.{LocalDate, Year}

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
  * Release version holder. Note that this is manually changing property. For every official
  * release the new version will be added to this object manually.
  */
object CPVersion:
    final val year = Year.now().toString
    final val tagline = "2D ASCII Game Engine for Scala3"
    final val copyright = s"(C) $year Rowan Games, Inc."

    /**
      *
      * @param semver Semver-based release version of the CosPlay.
      * @param date Date of this release.
      */
    case class Version(semver: String,  date: LocalDate):
        override def toString = s"Version [version=$semver, date=$date]"

    // +=================================================+
    // | UPDATE THIS SEQUENCE FOR EACH RELEASE MANUALLY. |
    // +=================================================+
    private final val VERSIONS = Seq(
        Version("0.1.0", LocalDate.of(2022, 1, 25)),
        Version("0.1.1", LocalDate.of(2022, 2, 25)),
        Version("0.2.0", LocalDate.of(2022, 3, 18)),
        Version("0.5.0", LocalDate.of(2022, 3, 20)),
        Version("0.5.1", LocalDate.of(2022, 3, 21)),
        Version("0.6.0", LocalDate.of(2022, 4, 3)),
    ).sortBy(_.semver)
    // +=================================================+
    // | UPDATE THIS SEQUENCE FOR EACH RELEASE MANUALLY. |
    // +=================================================+

    /**
      * Gets current version.
      */
    lazy val latest: Version = VERSIONS.last

