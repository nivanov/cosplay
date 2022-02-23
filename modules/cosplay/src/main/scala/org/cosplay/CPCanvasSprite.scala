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
  * Scene object tailor-made for canvas drawing.
  *
  * Canvas sprite takes dimension of the current [[CPSceneObjectContext.getCameraFrame camera frame]].
  * That allows its [[CPSceneObject.render()]] method, the only one method that needs to be implemented by
  * the sub-type, to draw on entire camera frame of the terminal.
  *
  * Note that by default this sprite sets its position and dimension equal to the entire camera frame. This may
  * affect shaders attached to this sprite if they are configured to work with object vs. entire frame since
  * this sprite will report its size as entire camera frame size. Shaders that work with this sprite should
  * account for it.
  *
  * ### Sprites
  * CosPlay provides number of built-in sprites. A sprite is a scene objects, visible or off-screen,
  * that is custom designed for a particular use case. Built-in sprites provide concrete
  * implementations for the abstract methods in the base [[CPSceneObject]] class. Most non-trivial games
  * will use combination of the built-in sprites and their own ones. Here's the list of the built-in
  * sprites:
  *  - [[CPCanvasSprite]]
  *  - [[CPImageSprite]]
  *  - [[CPStaticImageSprite]]
  *  - [[CPLabelSprite]]
  *  - [[CPVideoSprite]]
  *  - [[CPOffScreenSprite]]
  *  - [[CPKeyboardSprite]]
  *  - [[CPParticleSprite]]
  *  - [[CPTextInputSprite]]
  *
  * @param id Optional ID of this sprite.
  */
abstract class CPCanvasSprite(id: String = s"canv-spr-${CPUtils.guid6}") extends CPSceneObject(id):
    private var rect: CPRect = _

    /** @inheritdoc */
    override def update(ctx: CPSceneObjectContext): Unit = rect = ctx.getCameraFrame
    /** @inheritdoc */
    override def getX: Int = if rect == null then 0 else rect.x
    /** @inheritdoc */
    override def getY: Int = if rect == null then 0 else rect.y
    /** @inheritdoc */
    override def getZ: Int = 0
    /** @inheritdoc */
    override def getDim: CPDim = if rect == null then CPDim.ZERO else rect.dim
