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
  * A scene monitor is any scene object that wants to be notified on each frame after all scene objects
  * have been updated but before any of them were rendered. It allows, for example, to rearrange UI sprites
  * on the screen after all of them had a chance to update their inner state but before they are actually
  * rendered on the screen. Essentially, it provides for "post-update, pre-render" notification.
  *
  * Note that this trait should only be implemented by [[CPSceneObject]] subclasses.
  *
  * @tparam T Any scene object.
  */
trait CPSceneMonitor[T <: CPSceneObject]:
    /**
      * Called on each frame after all scene objects have been updated but before any of them were rendered.
      *
      * @param ctx Frame context. This context provides bulk of functionality that a scene object
      *     can do in a game, e.g. interact with other scene objects, check collisions, read input
      *     events and manage input focus, add or remove scene objects, add new and switch between scenes, etc.
      */
    def monitor(ctx: CPSceneObjectContext): Unit
