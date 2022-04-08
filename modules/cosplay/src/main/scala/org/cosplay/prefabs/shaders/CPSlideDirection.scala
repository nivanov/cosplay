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

package org.cosplay.prefabs.shaders

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
  * Direction of the slide effect produced by [[CPSlideInShader]] and [[CPSlideOutShader]] shaders.
  *
  * @see [[CPSlideInShader]]
  * @see [[CPSlideOutShader]]
  */
enum CPSlideDirection:
    /**  Smooth slide effect from left to right. */
    case LEFT_TO_RIGHT

    /**  Smooth slide effect from right to left. */
    case RIGHT_TO_LEFT

    /**  Smooth slide effect top to down. */
    case TOP_TO_BOTTOM

    /**  Smooth slide effect down up. */
    case BOTTOM_TO_TOP

    /**  Smooth slide effect from center outward. */
    case CENTRIFUGAL

    /**  Smooth slide effect from outward to center. */
    case CENTRIPETAL

    /**  Slide using random-pixel effect. */
    case RANDOM

    /**  Slide using random vertical line effect. */
    case RANDOM_VERT_LINE

    /**  Slide using random horizontal line effect. */
    case RANDOM_HOR_LINE
