[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://raw.githubusercontent.com/apache/opennlp/master/LICENSE)
[![build](https://github.com/nivanov/cosplay/actions/workflows/build.yml/badge.svg)](https://github.com/nivanov/cosplay/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/org.cosplayengine/cosplay.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.cosplayengine%22%20AND%20a:%22cosplay%22)

Check out [www.cosplayengine.com](https://cosplayengine.com) for the full documentation.

## Run
#### Git clone:
```shell
$ git clone https://github.com/nivanov/cosplay.git
$ cd cosplay
```
#### SBT:
```shell
$ sbt package
$ sbt "project cosplay; runMain org.cosplay.examples.tilemapper.CPTileMapperExample"
```
#### Maven:
```shell
$ mvn package
$ mvn -f modules/cosplay -P ex:tilemapper exec:java
```

## Copyright
Copyright (C) 2021 Rowan Games, Inc.

<img src="https://cosplayengine.com/images/cosplay-grey.gif" height="24px" alt="CosPlay Logo">


