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

import org.cosplay.impl.CPUtils

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
  * Scene object tailor-made for handling keyboard events.
  *
  * This is an off-screen sprite that has only one purpose of handling keyboard events.
  * Since this is an off-screen sprite the method [[CPSceneObject.render()]] will never
  * be called. Use [[CPSceneObject.update()]] callback, if necessary, instead and make sure to
  * call `super.update(...)`.
  *
  * This is an example of the often used idiom by built-in examples to handle 'Ctrl-q' key press to exit the game:
  * {{{
  *     CPKeyboardSprite(KEY_LO_Q, _.exitGame())
  * }}}
  *
  * ### Sprites
  * CosPlay provides number of built-in sprites. A sprite is a scene objects, visible or off-screen,
  * that is custom designed for a particular use case. Built-in sprites provide concrete
  * implementations for the abstract methods in the base [[CPSceneObject]] class. Most non-trivial games
  * will use combination of the built-in sprites and their own ones. Here's the list of the built-in
  * sprites:
  *  - [[CPCanvasSprite]]
  *  - [[CPImageSprite]]
  *  - [[CPLabelSprite]]
  *  - [[CPVideoSprite]]
  *  - [[CPStaticImageSprite]]
  *  - [[CPOffScreenSprite]]
  *  - [[CPKeyboardSprite]]
  *  - [[CPParticleSprite]]
  *  - [[CPTextInputSprite]]
  *
  * @param f Keyboard key handler.
  * @param tags Optional set of organizational or grouping tags. By default, the empty set is used.
  */
class CPKeyboardSprite(
    f: (CPSceneObjectContext, CPKeyboardKey) => Unit,
    tags: Seq[String] = Seq.empty
) extends CPOffScreenSprite(s"kbd-spr-${CPRand.guid6}", tags = tags):
    /** @inheritdoc */ 
    override def update(ctx: CPSceneObjectContext): Unit = ctx.getKbEvent match
        case Some(evt) => f(ctx, evt.key)
        case None => ()

    /**
      * Creates keyboard sprite handling a single keyboard key. This is an example of the often used idiom by
      * built-in examples to handle 'Ctrl-q' key press to exit the game:
      * {{{
      *     CPKeyboardSprite(KEY_LO_Q, _.exitGame())
      * }}}
      *
      * @param key Keyboard key to handle.
      * @param act A function to call when given `key` is pressed.
      */
    def this(key: CPKeyboardKey, act: CPSceneObjectContext => Unit) =
        this((ctx, k) => if k == key then act(ctx))

