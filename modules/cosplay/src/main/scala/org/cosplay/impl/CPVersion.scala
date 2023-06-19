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

import org.cosplay.*
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
               All rights reserved.
*/

/**
  * Release version holder. Note that this is manually changing property. For every official
  * release the new version will be added to this object manually.
  */
//noinspection ScalaWeakerAccess
object CPVersion:
    val year: String = Year.now().toString
    val tagline = "2D ASCII Game Engine for Scala3"
    val copyright = s"(C) $year Rowan Games, Inc."

    /**
      *
      * @param semver Semver-based release version of the CosPlay.
      * @param date Date of this release.
      */
    case class Version(semver: String,  date: LocalDate):
        override def toString = s"Version [version=$semver, date=$date]"

    // +==================================================+
    // | UPDATE BELOW SEQUENCE FOR EACH RELEASE MANUALLY. |
    // +==================================================+
    private val VERSIONS = Seq(
        Version("0.1.0", LocalDate.of(2022, 1, 25)),
        Version("0.1.1", LocalDate.of(2022, 2, 25)),
        Version("0.2.0", LocalDate.of(2022, 3, 18)),
        Version("0.5.0", LocalDate.of(2022, 3, 20)),
        Version("0.5.1", LocalDate.of(2022, 3, 21)),
        Version("0.6.0", LocalDate.of(2022, 4, 3)),
        Version("0.6.5", LocalDate.of(2022, 4, 14)),
        Version("0.6.6", LocalDate.of(2022, 5, 10)),
        Version("0.7.0", LocalDate.of(2022, 6, 14)),
        Version("0.7.1", LocalDate.of(2022, 6, 29)),
        Version("0.7.2", LocalDate.of(2022, 6, 30)),
        Version("0.7.3", LocalDate.of(2022, 11, 22)),
        Version("0.7.4", LocalDate.of(2022, 12, 8)),
        Version("0.7.5", LocalDate.of(2022, 12, 9)),
        Version("0.8.0", LocalDate.of(2023, 2, 4)),
        Version("0.8.1", LocalDate.of(2023, 2, 6)),
        Version("0.8.2", LocalDate.of(2023, 2, 7)),
        Version("0.8.3", LocalDate.of(2023, 2, 8)),
        Version("0.8.4", LocalDate.of(2023, 2, 9)),
        Version("0.8.5", LocalDate.of(2023, 2, 13)),
        Version("0.8.6", LocalDate.of(2023, 2, 14)),
        Version("0.8.7", LocalDate.of(2023, 2, 20)),
        Version("0.8.8", LocalDate.of(2023, 2, 21)),
        Version("0.8.9", LocalDate.of(2023, 3, 22)),
        Version("0.8.10", LocalDate.of(2023, 4, 30)),
        Version("0.9.0", LocalDate.of(2023, 6, 18)),
        Version("0.9.1", LocalDate.of(2023, 6, 19)),
    ).sortBy(_.semver)
    // +==================================================+
    // | UPDATE ABOVE SEQUENCE FOR EACH RELEASE MANUALLY. |
    // +==================================================+

    !>(VERSIONS.map(_.semver).distinct.sizeIs == VERSIONS.size, "Semver not unique.")
    !>(VERSIONS.map(_.date).distinct.sizeIs == VERSIONS.size, "Release date not unique.")

    /**
      * Gets current version.
      */
    lazy val latest: Version = VERSIONS.last

