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

/**
  * The scope for the [[CPSingletonSprite]].
  */
enum CPSingletonScope:
    /**  Scope of the entire game. One event at the beginning of the game loop. */
    case GAME
    /**  Scope of the scene. One event at the beginning of the scene life cycle. */
    case SCENE
    /**  Scope of the individual singleton object. One event at the beginning of the singleton life cycle. */
    case OBJECT

import CPSingletonScope.*

/**
  * Special type of off screen sprite that performs given function only once the first time
  * this sprite is processed by either the current scene or globally. Note that if the sprite is never added to the
  * scene or never processed as part of the game loop, its function will never be called. Depending on the `isMon`
  * parameter the function will be called in either [[CPSceneObject.update()]] or [[CPSceneObject.monitor()]] callbacks.
  *
  * @param id Optional ID of this scene object. By default, the random 6-character ID will be used.
  * @param fun Function to call.
  * @param isMon If `true` then given function will be called from [[CPSceneObject.monitor()]] callback, otherwise
  *              it will be called from [[CPSceneObject.update()]] method. Default value is `false`.
  * @param scope The scope of this singleton. Default value is [[CPSingletonScope.OBJECT]].
  */
class CPSingletonSprite(
    id: String = s"singleton-${CPRand.guid6}",
    fun: CPSceneObjectContext => Unit,
    isMon: Boolean = false,
    scope: CPSingletonScope = OBJECT
) extends CPOffScreenSprite(id):
    private var touched = false
    private def check(using ctx: CPSceneObjectContext): Unit =
        scope match
            case GAME => if ctx.getFrameCount == 0 then fun(ctx)
            case SCENE => if ctx.getSceneFrameCount == 0 then fun(ctx)
            case OBJECT => if !touched then fun(ctx)
    override def update(using ctx: CPSceneObjectContext): Unit =
        if !isMon then check
        touched = true
    override def monitor(using ctx: CPSceneObjectContext): Unit =
        if isMon then check

