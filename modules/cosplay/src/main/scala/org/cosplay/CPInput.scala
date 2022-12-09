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
  * Interface for the external input devices such as joysticks and game pads.
  *
  * You can add support for such devices by calling [[CPEngine.addInput()]] and [[CPEngine.removeInput()]] methods
  * on the game engine. Note that keyboard support is built into CosPlay and you don't need to do anything additional
  * to access it. This interface is designed to add additional external input devices that are not automatically
  * supported by CosPlay.
  *
  * @see [[CPEngine.addInput()]]
  * @see [[CPEngine.removeInput()]]
  */
trait CPInput:
    /**
      * Implementation should poll the external input devices and return a keyboard event representing
      * the the original input event, if any. Note that if the input is registered with
      * the engine its events (from this method) will override the built-in keyboard events happened
      * in the same frame.
      *
      * @param ctx Current input context.
      * @return Keyboard event that represents the event from the external input device.
      */
    def poll(ctx: CPInputContext): Option[CPKeyboardKey]
