<pre>
   _________            ______________
   __  ____/_______________  __ \__  /_____ _____  __
   _  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
   / /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
   \____/  \____//____/ /_/     /_/  \__,_/ _\__, /
                                            /____/
   🕹 2D ASCII JVM GAME ENGINE FOR SCALA3

</pre>
- Full documentation at [[https://cosplayengine.com]]
- Examples [[https://cosplayengine.com/devguide/examples.html]]
- <b>Main API</b> is located in [[file:org/cosplay.html org.cosplay]] package.
- GitHub project [[https://github.com/nivanov/cosplay]]

[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://raw.githubusercontent.com/apache/opennlp/master/LICENSE)
[![build](https://github.com/nivanov/cosplay/actions/workflows/build.yml/badge.svg)](https://github.com/nivanov/cosplay/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/org.cosplayengine/cosplay.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.cosplayengine%22%20AND%20a:%22cosplay%22)

[[https://cosplayengine.com/devguide/examples.html Examples]] located under `org.cosplay.examples` package:
   - [[https://github.com/nivanov/cosplay/tree/master/modules/cosplay/src/main/scala/org/cosplay/examples/animation Animation Examples]] 
   - [[https://github.com/nivanov/cosplay/tree/master/modules/cosplay/src/main/scala/org/cosplay/examples/camera Camera Examples]]       
   - [[https://github.com/nivanov/cosplay/tree/master/modules/cosplay/src/main/scala/org/cosplay/examples/video Video Examples]]
   - [[https://github.com/nivanov/cosplay/tree/master/modules/cosplay/src/main/scala/org/cosplay/examples/image Image Examples]]
   - [[https://github.com/nivanov/cosplay/tree/master/modules/cosplay/src/main/scala/org/cosplay/examples/canvas Canvas Examples]]
   - [[https://github.com/nivanov/cosplay/tree/master/modules/cosplay/src/main/scala/org/cosplay/examples/shader Shaders Examples]]
   - [[https://github.com/nivanov/cosplay/tree/master/modules/cosplay/src/main/scala/org/cosplay/examples/sound Sound Examples]]
   - [[https://github.com/nivanov/cosplay/tree/master/modules/cosplay/src/main/scala/org/cosplay/examples/textinput Text Input Examples]]
   - [[https://github.com/nivanov/cosplay/tree/master/modules/cosplay/src/main/scala/org/cosplay/examples/particle Particle Effect Examples]]
   - [[https://github.com/nivanov/cosplay/tree/master/modules/cosplay/src/main/scala/org/cosplay/examples/fonts Font Examples]]
   - [[https://github.com/nivanov/cosplay/tree/master/modules/cosplay/src/main/scala/org/cosplay/examples/tilemapper Tile Mapping Examples]]

Built-in [[https://cosplayengine.com/devguide/examples.html games]] located under `org.cosplay.games` package:
   - [[https://github.com/nivanov/cosplay/tree/master/modules/cosplay/src/main/scala/org/cosplay/games/pong Classic Pong Game]]
   - [[https://github.com/nivanov/cosplay/tree/master/modules/cosplay/src/main/scala/org/cosplay/games/macarena Macarena Game]]

Maven dependency:
<pre>
&lt;dependency&gt;
  &lt;groupId&gt;org.cosplayengine&lt;/groupId&gt;
  &lt;artifactId&gt;cosplay&lt;/artifactId&gt;
  &lt;version&gt;0.5.1&lt;/version&gt;
&lt;/dependency&gt;
</pre>

SBT dependency:
<pre>
libraryDependencies += "org.cosplayengine" % "cosplay" % "0.5.1"
</pre>
