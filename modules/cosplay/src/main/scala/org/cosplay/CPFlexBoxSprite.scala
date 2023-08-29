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
  *
  */
object CPFlexBoxSprite:
    /**
      *
      * @param id
      * @param z
      * @param bgPx
      * @param padding
      * @param ids
      * @return
      */
    def forIds(
        id: String,
        z: Int,
        bgPx: CPPixel,
        padding: CPInsets,
        ids: String*
    ): CPFlexBoxSprite = CPFlexBoxSprite(id, obj => ids.contains(obj.getId), z, bgPx, padding)

/**
  * This sprite is part of UI toolkit at CosPlay.
  *
  * @param id Optional ID of this scene object. By default, the random 6-character ID will be used.
  * @param compFilter
  * @param z
  * @param bgPx
  * @param padding
  * @param collidable
  * @param shaders
  * @param tags Optional set of organizational or grouping tags. By default, the empty set is used.
  */
class CPFlexBoxSprite(
    id: String,
    compFilter: CPSceneObject => Boolean,
    z: Int,
    bgPx: CPPixel,
    padding: CPInsets,
    collidable: Boolean = false,
    shaders: Seq[CPShader] = Seq.empty,
    tags: Set[String] = Set.empty
) extends CPSceneObject(id, tags):
    private var x = Int.MaxValue // Init value.
    private var y = Int.MaxValue // Init value.
    private var dim = CPDim.ZERO

    /** @inheritdoc */
    override def getDim: CPDim = dim
    /** @inheritdoc */
    override def getX: Int = x
    /** @inheritdoc */
    override def getY: Int = y
    /** @inheritdoc */
    override def getZ: Int = z
    /** @inheritdoc */
    override def getShaders: Seq[CPShader] = shaders
    /** @inheritdoc */
    override def getCollisionRect: Option[CPRect] = Option.when(collidable)(getRect)
    /** @inheritdoc */
    override def update(ctx: CPSceneObjectContext): Unit =
        for obj <- ctx.getObjects.filter(compFilter) do
            x = x.min(obj.getX)
            y = y.min(obj.getY)
            dim = CPDim(obj.getX2 - x + 1, obj.getY2 - y + 1)
    /** @inheritdoc */
    override def render(ctx: CPSceneObjectContext): Unit =
        if dim.w > 0 && dim.h > 0 then ctx.getCanvas.fillRect(getRect, z, bgPx)

