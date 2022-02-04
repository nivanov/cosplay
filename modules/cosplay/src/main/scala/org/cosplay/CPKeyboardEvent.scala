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
  * Container for the keyboard input event.
  *
  * Your scene objects can access the current keyboard event using [[CPSceneObjectContext.getKbEvent]] method.
  *
  * @param key Keyboard key.
  * @param sameAsLast Whether or not last keyboard event had the same keyboard key.
  * @param eventFrame Frame number for this event.
  * @param eventNs Timestamp of the event in nanoseconds.
  * @param lastEventFrame Frame number of the last keyboard event.
  * @param lastEventNs Timestamp in nanoseconds of the last keyboard event.
  * @see [[CPSceneObjectContext.getKbEvent]]
  */
final case class CPKeyboardEvent(
    key: CPKeyboardKey,
    sameAsLast: Boolean,
    eventFrame: Long,
    eventNs: Long,
    lastEventFrame: Long,
    lastEventNs: Long
):
    /**
      * Whether or not this is a repeated press on keyboard of the same key.
      *
      * It is defined as:
      * {{{
      * sameAsLast && eventFrame - lastEventFrame == 1
      * }}}
      */
    def isRepeated: Boolean = sameAsLast && eventFrame - lastEventFrame == 1
