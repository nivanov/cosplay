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

import impl.CPUtils

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
  * Root type for CosPlay classes.
  *
  * Game object has ID and organizational tags that can be used to organize
  * game objects. See [[CPSceneObjectContext.getObjectsForTags()]] method for getting scene objects based on
  * their tags.
  *
  * @param id Unique ID of this game object. By default, a random 6-character ID will be used.
  * @param tags Optional set of organizational tags. These tags are here only for the game
  *     developer benefits as they are not used by the game engine itself. By default, the
  *     empty set is used.
  * @see [[CPSceneObjectContext.getObjectsForTags()]]
  */
abstract class CPGameObject(id: String = CPRand.guid6, tags: Set[String] = Set.empty):
    /**
      * Gets unique ID of this game object.
      */
    def getId: String = id

    /**
      * Gets optional set of organizational tags. Note that by default the set of tags is empty.
      *
      * @see [[CPSceneObjectContext.getObjectsForTags()]]
      */
    def getTags: Set[String] = tags

    override def equals(obj: Any): Boolean = obj match
        case o: CPGameObject => o.getId == getId
        case _ => false