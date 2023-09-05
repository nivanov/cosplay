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

package org.cosplay

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

private[cosplay] sealed case class CPLayoutSpec(
    padTop: Int,
    padLeft: Int,
    padBottom: Int,
    padRight: Int,
    xFloat: Int, // -1, 0, 1
    xFloatRel: Option[String],
    yFloat: Int, // -1, 0, 1
    yFloatRel: Option[String],
    pos: Int, // 1, 2, 3, 4
    posRel: Option[String]
)

/**
  *
  *
  * @param id Optional ID of this scene object. By default, the random 6-character ID will be used.
  * @param spec
  * @param shaders Optional sequence of shaders for this sprite.
  * @param tags Optional set of organizational or grouping tags. By default, the empty set is used.
  */
class CPLayoutSprite(
    id: String,
    spec: String,
    shaders: Seq[CPShader] = Seq.empty,
    tags: Set[String] = Set.empty
) extends CPOffScreenSprite(id, shaders, tags):
    override def monitor(ctx: CPSceneObjectContext): Unit = ()

    def updateSpec(spec: String): Unit = ???

