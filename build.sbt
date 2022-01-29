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

val cosPlayVer = "0.1.1"

val scalaMajVer = "3"
val scalaMinVer = "1.0"
val log4jVer = "2.17.1"
val scalaLoggingVer = "3.9.4"
val scalaParColVer = "1.0.4"
val jlineVer = "3.20.0"
val commonsLang3Ver = "3.12.0"
val commonsMath3Ver = "3.6.1"
val scalaReflectVer = "1.1.4"
val junitVer = "5.8.2"
val scalaTestVer = "3.2.11"
val openjfxVer = "15.0.1"
val flatlafVer = "1.6.1"
val ikonliVer = "12.3.0"
val miglayoutVer = "11.0"

ThisBuild / scalaVersion := s"$scalaMajVer.$scalaMinVer"
ThisBuild / organization := "org.cosplay"
ThisBuild / organizationName := "Rowan Games, Inc."
ThisBuild / description := "2D ASCII Game Engine for Scala3."
ThisBuild / licenses := List("Apache-2.0" -> url("https://github.com/sbt/sbt/blob/develop/LICENSE"))
ThisBuild / homepage := Some(url("https://github.com/nivanov/cosplay"))
ThisBuild / scmInfo := Some(ScmInfo(url("https://github.com/nivanov/cosplay"), "scm:git@github.com:nivanov/cosplay.git"))
ThisBuild / developers ++= List(
    "nivanov" -> "Nikita Ivanov",
    "vlad94568" -> "Vlad Ivanov"
).map {
    case (username, fullName) => Developer(username, fullName, s"@$username", url(s"https://github.com/$username"))
}

lazy val cosplay = (project in file("modules/cosplay"))
    .settings(
        name := "CosPlay",
        Compile / doc / scalacOptions ++= Seq(
            "-project-footer", "(C) 2021 Rowan Games, Inc.",
            "-project-version", cosPlayVer,
            "-siteroot", ".",
            "-skip-by-id:org.cosplay.impl",
            "-skip-by-id:org.cosplay.impl.emuterm",
            "-skip-by-id:org.cosplay.impl.jlineterm",
            "-skip-by-id:org.cosplay.impl.guilog",
            "-doc-root-content", "scaladoc/docroot.md",
            "-source-links:github://nivanov/cosplay/master",
            "-social-links:github::https://github.com/nivanov/cosplay"
        ),
        libraryDependencies += "org.apache.commons" % "commons-math3" % s"$commonsMath3Ver",
        libraryDependencies += "org.apache.commons" % "commons-lang3" % s"$commonsLang3Ver",
        libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % s"$log4jVer",
        libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % s"$log4jVer",
        libraryDependencies += "com.miglayout" % "miglayout-swing" % s"$miglayoutVer",
        libraryDependencies += "org.kordamp.ikonli" % "ikonli-swing" % s"$ikonliVer",
        libraryDependencies += "org.kordamp.ikonli" % "ikonli-lineawesome-pack" % s"$ikonliVer",
        libraryDependencies += "com.formdev" % "flatlaf-intellij-themes" % s"$flatlafVer",
        libraryDependencies += "com.formdev" % "flatlaf" % s"$flatlafVer",
        libraryDependencies += "org.openjfx" % "javafx-media" % s"$openjfxVer",
        libraryDependencies += "co.blocke" %% s"scala-reflection" % s"$scalaReflectVer",
        libraryDependencies += "com.typesafe.scala-logging" % s"scala-logging_$scalaMajVer" % s"$scalaLoggingVer",
        libraryDependencies += "org.jline" % s"jline-terminal" % s"$jlineVer",
        libraryDependencies += "org.scala-lang.modules" %% s"scala-parallel-collections" % s"$scalaParColVer",

        // Test scope.
        libraryDependencies += "org.scalatest" %% s"scalatest" % s"$scalaTestVer" % Test,
        libraryDependencies += "org.junit.jupiter" % s"junit-jupiter-engine" % s"$junitVer" % Test
    )


