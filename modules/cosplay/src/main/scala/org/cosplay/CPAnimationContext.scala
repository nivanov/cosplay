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
  * Animation context that is passed into [[CPAnimation.keyFrame()]] method.
  *
  * @see [[CPAnimation.keyFrame()]]
  * @example See [[org.cosplay.examples.animation.CPAnimationExample CPAnimationExample]] source code for an
  *     example of animation functionality.
  */
trait CPAnimationContext extends CPBaseContext:
    /**
      * X-coordinate of the scene object rendering the animation.
      *
      * @see [[CPSceneObject.getY]]
      */
    def getX: Int

    /**
      * Y-coordinate of the scene object rendering the animation.
      *
      * @see [[CPSceneObject.getY]]
      */
    def getY: Int

    /**
      * Z-index of the scene object rendering the animation.
      *
      * @see [[CPSceneObject.getZ]]
      */
    def getZ: Int

    /**
      * ID of the scene object the rendering is used by.
      *
      * @see [[CPGameObject.getId]]
      */
    def getId: String
    
    /**
      * Sends direct message(s) to the scene object with given ID. Scene object must
      * belong to the current scene, i.e. one cannot send a message to the scene objects
      * from another scene. To exchange data between scenes you should use
      * [[CPSceneObjectContext.getGameCache game cache]]. Note that messages will be available to
      * recipient scene objects starting with the next frame. Messages will be stored until they are
      * retrieved or the scene is changed.
      *
      * @param id ID of the game object to send messages to.
      * @param msgs Sequence of zero or messages (objects) to send.
      */
    def sendMessage(id: String, msgs: AnyRef*): Unit

    /**
      * Gets direct messages send to this scene object, if any. Returns an empty sequence if no messages
      * pending delivery. Note that sent messages are stored until they are retrieved or the scene is changed.
      */
    def receiveMessage(): Seq[AnyRef]
