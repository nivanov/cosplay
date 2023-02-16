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
               All rights reserved.
*/

/**
  * Contains lifecycle state enumeration.
  */
object CPLifecycle:
    /**
      * Lifecycle states.
      */
    enum State:
        /**
          * Initial state of un-started object.
          */
        case LF_INIT

        /**
          * Object has been started. Called at most once.
          * Objects are started only once when they are first encountered by the game engine.
          */
        case LF_STARTED

        /**
          * Object has been activated. Called zero or more times.
          * Scene and scene objects activated when the scene becomes an active (playing) scene.
          */
        case LF_ACTIVE

        /**
          * Object has been deactivated. Called zero or more times.
          * Scene and scene objects deactivated when the game engine is switched to another scene.
          */
        case LF_INACTIVE

        /**
          * Object has been stopped. Called at most once.
          * Scenes and scene objects are stopped when either game is stopped or an object is removed.
          */
        case LF_STOPPED

/**
  * Lifecycle type for [[CPScene scenes]] and [[CPSceneObject scene objects]]. 
  * 
  * Objects supporting this lifecycle transition their states as follows:
  * <pre>
  *   +---------+   +------------+   +-----------+   +-------------+   +------------+
  *   | LF_INIT |-->| LF_STARTED |-->| LF_ACTIVE |-->| LF_INACTIVE |-->| LF_STOPPED |
  *   +---------+   +------------+   +-----------+   +-------------+   +------------+
  *                                        |                   |
  *                                        +---<-----<----<----+
  * </pre>
  *
  * Specifically, lifecycle objects are started and stopped only once but they can
  * transition from active to inactive state more than once. When a scene starts it and its scene
  * objects will be started, if they haven't been started before, and activated. When switching to another
  * scene, the current scene and its scene objects will be deactivated. Scene and scene objects get stopped
  * when either the entire game [[CPSceneObjectContext.exitGame() exits]] or an object gets removed.
  *
  * The typical use case for the lifecycle events is resource initialization and disposal, logging,
  * stats collections, etc. It is especially important in online games or the games that pull or
  * stream online assets.
  *
  * Note that when overriding the following callback methods make sure to call `super`:
  *  - [[onActivate()]]
  *  - [[onDeactivate()]]
  *  - [[onStart()]]
  *  - [[onStop()]]
  */
trait CPLifecycle:
    import org.cosplay.CPLifecycle.State.*

    /**
      *
      */
    private var state: CPLifecycle.State = LF_INIT

    /** */
    final private[cosplay] def onActivateX(): Unit =
        state = LF_ACTIVE
        onActivate()

    /** */
    final private[cosplay] def onDeactivateX(): Unit =
        state = LF_INACTIVE
        onDeactivate()

    /** */
    final private[cosplay] def onStartX(): Unit =
        state = LF_STARTED
        onStart()

    /** */
    final private[cosplay] def onStopX(): Unit =
        state = LF_STOPPED
        onStop()

    /**
      * Gets current lifecycle state.
      */
    final def getState: CPLifecycle.State = state

    /**
      * Callback on lifecycle object activation. Default implementation is no-op.
      *
      * @see [[CPLifecycle.State.LF_ACTIVE]]
      */
    def onActivate(): Unit = ()

    /**
      * Callback on lifecycle object deactivation. Default implementation is no-op.
      *
      * @see [[CPLifecycle.State.LF_INACTIVE]]
      */
    def onDeactivate(): Unit = ()

    /**
      * Callback on lifecycle object start. Default implementation is no-op.
      *
      * @see [[CPLifecycle.State.LF_STARTED]]
      */
    def onStart(): Unit = ()

    /**
      * Callback on lifecycle object stop. Default implementation is no-op.
      *
      * @see [[CPLifecycle.State.LF_STOPPED]]
      */
    def onStop(): Unit = ()
