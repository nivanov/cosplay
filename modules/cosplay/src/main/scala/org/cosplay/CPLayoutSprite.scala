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

private sealed case class LayoutSpec(
    padTop: Int,
    padLeft: Int,
    padBottom: Int,
    padRight: Int
)

/**
  * This sprite is part of UI toolkit at CosPlay.
  *
  * @param id Optional ID of this scene object. By default, the random 6-character ID will be used.
  * @param shaders Optional sequence of shaders for this sprite.
  * @param tags Optional set of organizational or grouping tags. By default, the empty set is used.
  */
class CPLayoutSprite(
    id: String,
    shaders: Seq[CPShader] = Seq.empty,
    tags: Set[String] = Set.empty
) extends CPOffScreenSprite(id, shaders, tags) with CPSceneMonitor[_]:
    override def monitor(ctx: CPSceneObjectContext): Unit = ()

    /** Unlinks all previously linked constituent sprites from this layout sprite. */
    def clear(): Unit

    /**
      *
      * @param id ID of the dynamic sprite to link. If the sprite with given ID is already linked - this operation
      *     is ignored.
      * @param anchor Optional anchor scene object ID relative to which to layout the given dynamic sprite.
      *     If `None`, the entire scene canvas will be used instead.
      * @param spec Specific layout instructions to use.
      */
    def link(id: String, anchor: Option[String], spec: String): Unit

    /**
      *
      * @param id
      * @return
      */
    def isLinked(id: String): Boolean

    /**
      * Unlinks dynamic sprite with given `id` from this layout sprite.
      *
      * @param id ID of the previously linked dynamic sprite.
      * @return `True` if dynamic sprite with given `id` was successfully unlinked, `false` otherwise.
      */
    def unlink(id: String): Boolean

