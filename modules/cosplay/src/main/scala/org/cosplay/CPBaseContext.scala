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

import scala.collection.mutable

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
  * Base type for various scene-related context classes.
  *
  * @see [[CPSceneObjectContext]]
  * @see [[CPAnimationContext]]
  * @see [[CPInputContext]]
  */
trait CPBaseContext:
    /**
      * Gets log instance.
      */
    def getLog: CPLog

    /**
      * Gets game-wide cache. Game-wide cache can be used to share data across scenes.
      *
      * A cache is a map-like type that is used for game-wide and scene-wide user-data containers. The instances of this
      * class are created and managed by the game engine. Scene and game caches can be used to exchange and store
      * user-defined data between frames of the same scene or between scenes of the game. Note that by default these caches
      * are in-memory only and not persistent between game executions. One could, however, add persistence using one of
      * the lifecycle methods available through [[CPLifecycle]] type that is extended by both [[CPSceneObject]]
      * and [[CPScene]] types.
      *
      * @see [[getSceneCache]]
      */
    def getGameCache: CPCache

    /**
      * Gets scene-wide cache. Scene-wide cache gets cleared when scene changes.
      *
      * A cache is a map-like type that is used for game-wide and scene-wide user-data containers. The instances of this
      * class are created and managed by the game engine. Scene and game caches can be used to exchange and store
      * user-defined data between frames of the same scene or between scenes of the game. Note that by default these caches
      * are in-memory only and not persistent between game executions. One could, however, add persistence using one of
      * the lifecycle methods available through [[CPLifecycle]] type that is extended by both [[CPSceneObject]]
      * and [[CPScene]] types.
      *
      * @see [[getGameCache]]
      */
    def getSceneCache: CPCache

    /**
      * Gets global frame count for the game.
      */
    def getFrameCount: Long

    /**
      * Gets frame count since the beginning of the current scene.
      */
    def getSceneFrameCount: Long

    /**
      * Gets the timestamp in milliseconds of the game start.
      */
    def getStartGameMs: Long

    /**
      * Gets the timestamp in milliseconds of the current scene start.
      */
    def getStartSceneMs: Long

    /**
      * Gets the timestamp in milliseconds for the current frame.
      *
      * This value will be the same for all objects across all callbacks for the entirety of the
      * current frame processing. It is used to provide the time ```simultaneity``` across all scene
      * objects at a given frame.
      */
    def getFrameMs: Long
