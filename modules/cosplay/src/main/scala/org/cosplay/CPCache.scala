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
  * A map-like type that is used by [[CPSceneObjectContext]] for game-wide and scene-wide
  * user-data containers. 
  * 
  * The instances of this class are created and managed by the game engine
  * and available via [[CPSceneObjectContext.getSceneCache]] and [[CPSceneObjectContext.getGameCache]]
  * methods. Scene and game caches can be used to exchange and store user-defined data between frames of the
  * same scene or between scenes of the game. Note that by default these caches are in-memory only and not persistent
  * between game executions. One coule, however, add persistence using one of the lifecycle methods available
  * through [[CPLifecycle]] type that is extended by both [[CPSceneObject]] and [[CPScene]] types.
  *
  * Note that all mutating operations will be queued up and executed at the end of the current
  * frame processing. Specifically, any changes will be "visible" to other scene objects only
  * on the next frame.
  *
  * @param delayedQ Queue for delayed operations. Used internally by the game engine.
  * @see [[CPSceneObjectContext.getSceneCache]]
  * @see [[CPSceneObjectContext.getGameCache]]
  */
final class CPCache(delayedQ: mutable.ArrayBuffer[() => Unit]):
    private val map = mutable.HashMap.empty[String, AnyRef]

    /**
      * Optionally returns the value associated with a key.
      *
      * @param key Key value.
      * @return An option value containing the value associated with `key` in this cache,
      *     or `None` if none exists.
      */
    def get[T](key: String): Option[T] = map.get(key).asInstanceOf[Option[T]]

    /**
      * Adds a new key/value pair to this cache.
      * If the map already contains a mapping for the key, it will be overridden by the new value.
      *
      * Note that all mutating operations will be queued up and executed at the end of the current
      * frame processing. Specifically, any changes will be "visible" to other scene objects only
      * on the next frame.
      *
      * @param key The key to update.
      * @param value  The new value.
      */
    def put(key: String, value: AnyRef): Unit =
        val myKey = key
        val myValue = value
        delayedQ += (() => map.put(myKey, myValue))

    /**
      * Removes a key from this cache.
      *
      * Note that all mutating operations will be queued up and executed at the end of the current
      * frame processing. Specifically, any changes will be "visible" to other scene objects only
      * on the next frame.
      *
      * @param key The key to be removed.
      */
    def remove(key: String): Unit =
        val myKey = key
        delayedQ += (() => map.remove(myKey))

    /**
      * Tests whether this cache contains a binding for a key.
      *
      * @param key The key.
      */
    def contains(key: String): Boolean = map.contains(key)

    /**
      * Retrieves the value which is associated with the given key.
      *
      * @param key The key.
      * @throws NoSuchElementException Thrown if there is no mapping for given `key`.
      */
    def apply[T](key: String): T = map(key).asInstanceOf[T]

    /**
      * Removes all mappings from this cache.
      *
      * Note that all mutating operations will be queued up and executed at the end of the current
      * frame processing. Specifically, any changes will be "visible" to other scene objects only
      * on the next frame.
      */
    def clear(): Unit = delayedQ += (() => map.clear())

    /**
      *
      */
    private[cosplay] def reset(): Unit = map.clear()

    /**
      * Retains only those mappings for which the predicate `f` returns `true`.
      *
      * Note that all mutating operations will be queued up and executed at the end of the current
      * frame processing. Specifically, any changes will be "visible" to other scene objects only
      * on the next frame.
      *
      * @param f The test predicate.
      */
    def filterInPlace(f: (String, AnyRef) => Boolean): Unit =
        val myF = f
        delayedQ += (() => map.filterInPlace(myF))

    /**
      * Applies a transformation function to all values contained in this cache.
      * The transformation function produces new values from existing keys
      * associated values.
      *
      * Note that all mutating operations will be queued up and executed at the end of the current
      * frame processing. Specifically, any changes will be "visible" to other scene objects only
      * on the next frame.
      *
      * @param f The transformation to apply
      */
    def mapValuesInPlace(f: (String, AnyRef) => AnyRef): Unit =
        val myF = f
        delayedQ += (() => map.mapValuesInPlace(myF))

    /**
      * Tests whether the cache is empty.
      *
      * @return `true` if the cache contains no elements, `false` otherwise.
      */
    def isEmpty: Boolean = map.isEmpty

    /**
      * Gets iterator can be used only once.
      */
    def iterator: Iterator[(String, AnyRef)] = map.iterator
