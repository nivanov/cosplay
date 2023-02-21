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

package org.cosplay.impl

import org.cosplay.*
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
               All rights reserved.
*/

/**
  * Map-based container for game objects.
  */
class CPContainer[T <: CPGameObject]:
    private val map = mutable.HashMap.empty[String /* GameObject ID */ , T]

    /**
      *
      */
    def clear(): Unit = map.clear()

    /**
      *
      */
    def values: Iterable[T] = map.values

    /**
      *
      * @param id
      */
    def get(id: String): Option[T] = map.get(id)

    /**
      *
      * @param id
      */
    def remove(id: String): Option[T] = map.remove(id)

    /**
      *
      * @param tags
      */
    def removeForTags(tags: String*): Unit = getForTags(tags).foreach(t => remove(t.getId))

    /**
      *
      * @param id
      */
    def contains(id: String): Boolean = map.contains(id)

    /**
      *
      * @param tags
      */
    def containsForTags(tags: Seq[String]): Boolean =
        map.values.exists(obj => tags.exists(tag => obj.getTags.contains(tag)))

    /**
      *
      */
    def isEmpty: Boolean = map.isEmpty

    /**
      *
      * @param f
      */
    def foreach(f: T => Unit): Unit = map.values.foreach(f)

    /**
      *
      * @param tags
      */
    def getForTags(tags: Seq[String]): Seq[T] =
        map.values.filter(obj => tags.exists(tag => obj.getTags.contains(tag))).toSeq

    /**
      *
      * @param tags
      */
    def countForTags(tags: Seq[String]): Int =
        map.values.count(obj => tags.exists(tag => obj.getTags.contains(tag)))

    /**
      *
      * @param id
      */
    def apply(id: String): T = map.getOrElse(id, raise(s"Unknown game object ID: $id"))

    /**
      *
      * @param t
      */
    def add(t: T): Unit = if map.contains(t.getId) then raise(s"Dup game object ID: ${t.getId}") else map.put(t.getId, t)

    /**
      *
      * @param ts
      */
    def add(ts: T*): Unit = ts.foreach(add)
