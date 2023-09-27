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

import org.cosplay.*
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
  * Orientation of centering for [[CPCenteredImageSprite]] instance.
  */
enum CPCenteredImageSpriteOrientation:
    /** Only horizontal centering. */
    case HOR
    /** Only vertical centering. */
    case VERT
    /** Both horizontal and vertical centering. */
    case BOTH

import org.cosplay.CPCenteredImageSpriteOrientation.*

/**
  * Image sprite that centers its image on the canvas on each frame update.
  *
  * @param id Optional ID of the sprite.
  * @param img The image to render. It can be [[CPImageSprite.setImage() changed later]].
  * @param z Z-index at which to render the image.
  * @param collidable Whether or not this sprite has a collision shape. Default is `false`.
  * @param shaders Optional sequence of shaders for this sprite. Default value is an empty sequence.
  * @param orient Centering orientation. Default value is [[CPCenteredImageSpriteOrientation.BOTH]]. Note that you need
  *     set requires X or Y coordinates manually if not using [[CPCenteredImageSpriteOrientation.BOTH]] orientation.
  * @param tags Optional set of organizational or grouping tags. By default, the empty set is used.
  * @see [[CPImageSprite.setY()]]
  * @see [[CPImageSprite.setX()]]
  */
class CPCenteredImageSprite(
    id: String = s"center-img-spr-${CPRand.guid6}",
    img: CPImage,
    z: Int,
    collidable: Boolean = false,
    shaders: Seq[CPShader] = Seq.empty,
    orient: CPCenteredImageSpriteOrientation = BOTH,
    tags: Set[String] = Set.empty
) extends CPImageSprite(id, 0, 0, z, img, collidable, shaders, tags):
    override def update(ctx: CPSceneObjectContext): Unit =
        super.update(ctx)
        val canv = ctx.getCanvas
        // Center itself.
        if orient == HOR || orient == BOTH then setX((canv.dim.w - getImage.getWidth) / 2)
        if orient == VERT || orient == BOTH then setY((canv.dim.h - getImage.getHeight) / 2)
