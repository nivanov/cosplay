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
  * Scene object that has no rendering content of its own.
  *
  * This type of sprites is typically used for shaders-only rendering, e.g. fade in of the entire screen, etc.
  * Off-screen sprite has XY-coordinate set to `(0,0)` and its Z-index set to zero. Its dimension
  * set to [[CPDim.ZERO]] and it shape set to [[CPRect.ZERO]] as well.
  *
  * Since this is an off-screen sprite the method [[CPSceneObject.render()]] will never
  * be called. Use [[CPSceneObject.update()]] callback, if necessary, instead and make sure to
  * call `super.update(...)`.
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
  * @param id Optional ID of this scene object. By default, the random 6-character ID will be used.
  * @param shaders Optional set of shaders for this sprite.
  */
class CPOffScreenSprite(id: String = s"off-scr-spr-${CPUtils.guid6}", shaders: Seq[CPShader] = Seq.empty) extends CPSceneObject(id):
    setVisible(false)

    /**
      * Creates off-screen sprite with default ID and given set of shaders.
      *
      * @param shaders Set of shaders for this sprite.
      */
    def this(shaders: Seq[CPShader]) = this(CPUtils.guid6, shaders)

    /**
      * Creates off-screen sprite with default ID and given shaders.
      *
      * @param shader Shader for this sprite.
      */
    def this(shader: CPShader) = this(CPUtils.guid6, Seq(shader))

    /** @inheritdoc */
    override def getX: Int = 0
    /** @inheritdoc */
    override def getY: Int = 0
    /** @inheritdoc */
    override def getZ: Int = 0
    /** @inheritdoc */
    override def getDim: CPDim = CPDim.ZERO
    /** @inheritdoc */
    override def getRect: CPRect = CPRect.ZERO
    /** @inheritdoc */
    override def getShaders: Seq[CPShader] = shaders