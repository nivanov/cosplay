    
    
    _________            ______________               
    __  ____/_______________  __ \__  /_____ _____  __
    _  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
    / /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
    \____/  \____//____/ /_/     /_/  \__,_/ _\__, /  
                                             /____/
           ASCII Game Engine for Scala3
    

[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://raw.githubusercontent.com/apache/opennlp/master/LICENSE)
[![build](https://github.com/nivanov/cosplay/actions/workflows/build.yml/badge.svg)](https://github.com/nivanov/cosplay/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/org.cosplayengine/cosplay.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.cosplayengine%22%20AND%20a:%22cosplay%22)

## "Escape From Mir" Game
"Escape From Mir" is a full game about an astronaut stranded on the damaged space station "Mir". It is a game that will 
challenge you survival, detective, orbital physics, puzzle solving and resource management skills and is targeted towards 
engineers and programmers. It is largely based on real events. Check out [www.cosplayengine.com](https://cosplayengine.com/devguide/mir_game.html) for the full 
documentation.

## Introduction
The year is 1997. It’s been 11 years since the launch of the Mir space station - the world’s first permanent human 
habitat in orbit above the planet Earth. Over the decade the Mir station has been assembled from 7 separate modules 
and is now manned by an international crew of 5 astronauts.

On the morning of June 24 the unmanned resupply vessel Progress-34 launched just a few months earlier from Baikonur, 
Russia crashed into Mir space station during the routine re-docking procedure. Station is badly damaged. Status of the 
crew is unknown, air is leaking, structural and orbit control damage alarm is on, the power supply subsystem 
is offline. You regain consciousness in the airlock “Core Module” of the station. Through the zero-gravity mayhem of 
the crash you see a working computer terminal that’s... rebooting.

You must escape from Mir to survive.

## Run
#### Git clone:
```shell
$ git clone https://github.com/nivanov/cosplay.git
$ cd cosplay
```
#### SBT:
```shell
$ sbt package
$ sbt "package cosplay; runMain org.cosplay.games.mir.CPMirGame"
```
#### Maven:
```shell
$ mvn package
$ mvn -f modules/cosplay -P mir exec:java
```

## Questions?
* Join [discord](https://discord.gg/gDQuYJDM)
* [Documentation](https://cosplayengine.com), [examples](https://github.com/nivanov/cosplay/tree/master/modules/cosplay/src/main/scala/org/cosplay/examples) and built-in [games](https://github.com/nivanov/cosplay/tree/master/modules/cosplay/src/main/scala/org/cosplay/games)
* Post a question at [Stack Overflow](https://stackoverflow.com/questions/ask) using <code>cosplay</code> tag
* File a bug or improvement in [GitHub Issues](https://github.com/nivanov/cosplay/issues)
* Join project on [GitHub](https://github.com/nivanov/cosplay/issues)

## Copyright
Copyright (C) 2021 Rowan Games, Inc.

<img src="https://cosplayengine.com/images/cosplay-grey.gif" height="24px" alt="CosPlay Logo">


