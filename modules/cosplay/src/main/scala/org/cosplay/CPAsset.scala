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

          2D ASCII JVM GAME ENGINE FOR SCALA3
              (C) 2021 Rowan Games, Inc.
                ALl rights reserved.
*/

/**
  * Mark up type indicating that the object is an asset.
  *
  * Assets like [[CPImage images]], [[CPSound sounds]], [[CPFont fonts]], [[CPAnimation animations]] or [[CPVideo videos]]
  * are not managed or governed by the CosPlay game engine unlike [[CPScene scenes]] and [[CPSceneObject scene objects]] that are managed and governed
  * by the game engine. Assets are typically created outside the game loop and managed by the developer, they
  * can be freely shared between scenes or scene objects as any other standard Scala objects.
  *
  * Here's current list of assets:
  *  - [[CPImage]]
  *  - [[CPAnimation]]
  *  - [[CPSound]]
  *  - [[CPVideo]]
  *  - [[CPFont]]
  *  - [[CPParticleEmitter]]
  */
trait CPAsset:
    /**
      * Gets the origin of this asset. Typically, this should be a URL, file name or class name for in-code assets
      * like array images, animations or system font.
      */
    def getOrigin: String
