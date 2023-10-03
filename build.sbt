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
               All rights reserved.
*/

val cosPlayVer = "0.9.4"

val scalaMajVer = "3"
val scalaMinVer = "3.0"
val log4jVer = "2.20.0"
val scalaLoggingVer = "3.9.5"
val scalaParColVer = "1.0.4"
val commonsLang3Ver = "3.13.0"
val commonsMath3Ver = "3.6.1"
val commonsTextVer = "1.10.0"
val commonsIoVer = "2.13.0"
val commonsCollectionsVer = "4.4"
val scalaReflectVer = "1.2.2"
val scalaTestVer = "3.2.16"
val junitVer = "5.10.0"
val openjfxVer = "18"
val ikonliVer = "12.3.1"
val flatlafVer = "3.2"
val miglayoutVer = "11.1"
val jlineVer = "3.23.0"
val jnaVer = "5.13.0"
val mixPanelVer = "1.5.2"
val audioFileVer = "2.4.2"
val jansiVer = "2.4.0"
val antlrVer = "4.13.1"

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
    "leo94582" -> "Leo Ivanov"
).map {
    case (username, fullName) => Developer(username, fullName, s"@$username", url(s"https://github.com/$username"))
}

lazy val cosplay = (project in file("modules/cosplay"))
    .settings(
        name := "CosPlay",
        // NOTE:
        // -----
        // This (enabling forking) will force CosPlay to use terminal emulator and will not work in the same
        // terminal console as SBT itself. If forking is disabled, then CosPlay will try to run in the same
        // console as SBT and will fail due to JLine native libraries loading semantics (SBT also uses JLine).
        //
        // If you want to run a built-in game or example in the terminal console use supplied maven build
        // 'modules/cosplay/pom.xml'. For example, to run built-in 'Pong' game (maven profile 'pong') in the
        // terimanl console run the following command from the project root directory:
        //
        // $ mvn clean package
        // $ mvn -f modules/cosplay -P pong exec:java
        //
        // See 'modules/cosplay/pom.xml' for all available profiles to run.
        fork := true,
        // Scaladoc config.
        Compile / doc / scalacOptions ++= Seq(
            "-project-footer", "(C) 2023 Rowan Games, Inc.",
            "-project-version", cosPlayVer,
            "-siteroot", ".",
            "-skip-by-regex:org.cosplay.impl",
            "-skip-by-regex:org.cosplay.examples",
            "-skip-by-regex:org.cosplay.games",
            "-doc-root-content", "scaladoc/docroot.md",
            "-source-links:github://nivanov/cosplay/master",
            "-social-links:github::https://github.com/nivanov/cosplay"
        ),
        // Dependencies.
        libraryDependencies += "org.apache.commons"         % "commons-math3"                   % commonsMath3Ver,
        libraryDependencies += "org.apache.commons"         % "commons-lang3"                   % commonsLang3Ver,
        libraryDependencies += "commons-io"                 % "commons-io"                      % commonsIoVer,
        libraryDependencies += "org.apache.commons"         % "commons-text"                    % commonsTextVer,
        libraryDependencies += "org.apache.commons"         % "commons-collections4"            % commonsCollectionsVer,
        libraryDependencies += "org.apache.logging.log4j"   % "log4j-core"                      % log4jVer,
        libraryDependencies += "org.apache.logging.log4j"   % "log4j-api"                       % log4jVer,
        libraryDependencies += "com.miglayout"              % "miglayout-swing"                 % miglayoutVer,
        libraryDependencies += "org.kordamp.ikonli"         % "ikonli-swing"                    % ikonliVer,
        libraryDependencies += "org.kordamp.ikonli"         % "ikonli-lineawesome-pack"         % ikonliVer,
        libraryDependencies += "com.formdev"                % "flatlaf-intellij-themes"          % flatlafVer,
        libraryDependencies += "com.formdev"                % "flatlaf"                          % flatlafVer,
        libraryDependencies += "org.openjfx"                % "javafx-media"                    % openjfxVer,
        libraryDependencies += "com.typesafe.scala-logging" % s"scala-logging_$scalaMajVer"     % scalaLoggingVer,
        libraryDependencies += "org.jline"                  % "jline-terminal"                  % jlineVer,
        libraryDependencies += "org.fusesource.jansi"       % "jansi"                           % jansiVer,
        libraryDependencies += "net.java.dev.jna"           % "jna"                             % jnaVer,
        libraryDependencies += "com.mixpanel"               % "mixpanel-java"                   % mixPanelVer,
        libraryDependencies += "de.sciss"                   % "audiofile_3"                      % audioFileVer,
        libraryDependencies += "co.blocke"                  %% "scala-reflection"                % scalaReflectVer,
        libraryDependencies += "org.scala-lang.modules"     %% "scala-parallel-collections"     % scalaParColVer,
        libraryDependencies += "org.antlr"                  % "antlr4"                          % antlrVer,

        // Test scope.
        libraryDependencies += "org.scalatest"              %% s"scalatest"                     % scalaTestVer % Test,
        libraryDependencies += "org.junit.jupiter"          % s"junit-jupiter-engine"           % junitVer % Test
    )
