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

package org.cosplay.prefabs.sprites

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
               ALl rights reserved.
*/

/**
  *
  * @param id Optional ID of the sprite.
  * @param img The image to render. It can be changed later.
  * @param z Z-index at which to render the image.
  * @param shaders Optional set of shaders for this sprite. Default value is an empty sequence.
  */
class CPCenteredImageSprite(
    id: String = s"center-img-spr-${CPUtils.guid6}",
    img: CPImage,
    z: Int,
    shaders: Seq[CPShader] = Seq.empty) extends CPImageSprite(id, 0, 0, z, img, shaders = shaders):
    override def update(ctx: CPSceneObjectContext): Unit =
        super.update(ctx)
        val canv = ctx.getCanvas
        // Center itself.
        setX((canv.dim.w - getImage.getWidth) / 2)
        setY((canv.dim.h - getImage.getHeight) / 2)
