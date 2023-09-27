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
  * Special type of off screen sprite that performs given function only once the first time
  * this sprite is processed by either the current scene or globally. Note that if the sprite is never added to the
  * scene or never processed as part of the game loop, its function will never be called. Depending on the `isMon`
  * parameter the function will be called in either [[CPSceneObject.update()]] or [[CPSceneObject.monitor()]] callbacks.
  *
  * @param id Optional ID of this scene object. By default, the random 6-character ID will be used.
  * @param fun Function to call.
  * @param isMon If `true` then given function will be called from [[CPSceneObject.monitor()]] callback, otherwise
  *              it will be called from [[CPSceneObject.update()]] method. Default value is `false`.
  * @param isGlobal If `true` than 1st global frame count will trigger the function call, otherwise
  *                 the local scene zero frame count will be the trigger. Note that if this parameter is `false`,
  *                 than the function can be called multiple times (as many times as given scene gets activated).
  *                 Default value is `true`.
  */
class CPSingletonSprite(
    id: String = s"singleton-${CPRand.guid6}",
    fun: CPSceneObjectContext => Unit,
    isMon: Boolean = false,
    isGlobal: Boolean = true
) extends CPOffScreenSprite(id):
    private def check(using ctx: CPSceneObjectContext): Unit =
        if (isGlobal && ctx.getFrameCount == 0) || (!isGlobal && ctx.getSceneFrameCount == 0) then fun(ctx)
    override def update(using ctx: CPSceneObjectContext): Unit = if !isMon then check
    override def monitor(using ctx: CPSceneObjectContext): Unit = if isMon then check

