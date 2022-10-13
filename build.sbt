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

val cosPlayVer = "0.7.2"

val scalaMajVer = "3"
val scalaMinVer = "1.3"
val log4jVer = "2.18.0"
val scalaLoggingVer = "3.9.5"
val scalaParColVer = "1.0.4"
val commonsLang3Ver = "3.12.0"
val commonsMath3Ver = "3.6.1"
val commonsTextVer = "1.9"
val commonsCollectionsVer = "4.4"
val scalaReflectVer = "1.1.4"
val scalaTestVer = "3.2.13"
val junitVer = "5.9.0"
val openjfxVer = "17.0.2"
val ikonliVer = "12.3.1"
val flatlafVer = "2.4"
val miglayoutVer = "11.0"
val jlineVer = "3.21.0"
val jnaVer = "5.10.0"
val mixPanelVer = "1.5.1"
val antlr4Ver = "4.11.1"

ThisBuild / scalaVersion := s"$scalaMajVer.$scalaMinVer"
ThisBuild / version := cosPlayVer
ThisBuild / organization := "org.cosplay"
ThisBuild / organizationName := "Rowan Games, Inc."
ThisBuild / description := "2D ASCII Game Engine for Scala3."
ThisBuild / licenses := List("Apache-2.0" -> url("https://github.com/sbt/sbt/blob/develop/LICENSE"))
ThisBuild / homepage := Some(url("https://cosplayengine.com"))
ThisBuild / scmInfo := Some(ScmInfo(url("https://github.com/nivanov/cosplay"), "scm:git@github.com:nivanov/cosplay.git"))
ThisBuild / developers ++= List(
    "nivanov" -> "Nikita Ivanov",
    "vlad94568" -> "Vlad Ivanov",
    "leo94582" → "Leo Ivanov"
).map {
    case (username, fullName) => Developer(username, fullName, s"@$username", url(s"https://github.com/$username"))
}

lazy val cosplay = (project in file("modules/cosplay"))
    .settings(
        name := "CosPlay",
        version := cosPlayVer,

        // Scaladoc config.
        Compile / doc / scalacOptions ++= Seq(
            "-project-footer", "(C) 2022 Rowan Games, Inc.",
            "-project-version", cosPlayVer,
            "-siteroot", ".",
            "-skip-by-regex:org.cosplay.impl",
            "-skip-by-regex:org.cosplay.examples",
            "-skip-by-regex:org.cosplay.games",
            "-doc-root-content", "scaladoc/docroot.md",
            "-source-links:github://nivanov/cosplay/master",
            "-social-links:github::https://github.com/nivanov/cosplay"
        ),
        // JVM options for Java 17+.
        javaOptions += "--add-opens",
        javaOptions += "javafx.graphics/com.sun.javafx.application=ALL-UNNAMED",

        // Dependencies.
        libraryDependencies += "org.apache.commons" % "commons-math3" % commonsMath3Ver,
        libraryDependencies += "org.apache.commons" % "commons-lang3" % commonsLang3Ver,
        libraryDependencies += "org.apache.commons" % "commons-text" % commonsTextVer,
        libraryDependencies += "org.apache.commons" % "commons-collections4" % commonsCollectionsVer,
        libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % log4jVer,
        libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % log4jVer,
        libraryDependencies += "com.miglayout" % "miglayout-swing" % miglayoutVer,
        libraryDependencies += "org.kordamp.ikonli" % "ikonli-swing" % ikonliVer,
        libraryDependencies += "org.kordamp.ikonli" % "ikonli-lineawesome-pack" % ikonliVer,
        libraryDependencies += "com.formdev" % "flatlaf-intellij-themes" % flatlafVer,
        libraryDependencies += "com.formdev" % "flatlaf" % flatlafVer,
        libraryDependencies += "org.openjfx" % "javafx-media" % openjfxVer,
        libraryDependencies += "co.blocke" %% s"scala-reflection" % scalaReflectVer,
        libraryDependencies += "com.typesafe.scala-logging" % s"scala-logging_$scalaMajVer" % scalaLoggingVer,
        libraryDependencies += "org.jline" % "jline-terminal" % s"$jlineVer",
        libraryDependencies += "net.java.dev.jna" % "jna" % s"$jnaVer",
        libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % scalaParColVer,
        libraryDependencies += "com.mixpanel" % "mixpanel-java" % mixPanelVer,
        libraryDependencies += "org.antlr" % "antlr4-runtime" % antlr4Ver,

        // Test scope.
        libraryDependencies += "org.scalatest" %% s"scalatest" % s"$scalaTestVer" % Test,
        libraryDependencies += "org.junit.jupiter" % s"junit-jupiter-engine" % s"$junitVer" % Test
    )


