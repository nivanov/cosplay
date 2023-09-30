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

class CPSpacerSprite(
    id: String = s"spacer-spr-${CPRand.guid6}",
    x: Int,
    y: Int,
    z: Int,
    private var width: Int,
    private var height: Int,
    collidable: Boolean = false,
    shaders: Seq[CPShader] = Seq.empty,
    tags: Set[String] = Set.empty
) extends CPDynamicSprite(id, x, y, z, collidable, shaders = shaders, tags = tags):
    private var dim = CPDim(width, height)

    /**  Sets new width. */
    def setWidth(w: Int): Unit =
        !>(w >= 0, "Width must be >= 0.")
        dim = CPDim(w, dim.height)
    /** Sets new height.  */
    def setHeight(h: Int): Unit =
        !>(h >= 0, "Height must be >= 0.")
        dim = CPDim(dim.width, h)
    /** Sets new dimension. */
    def setDim(dim: CPDim): Unit =
        !>(dim != null, "Dimension cannot be null.")
        this.dim = dim
    /** Sets new width and height. */
    def setSize(w: Int, h: Int): Unit =
        !>(w >= 0, "Width must be >= 0.")
        !>(h >= 0, "Height must be >= 0.")
        dim = CPDim(w, h)

    override def getDim: CPDim = dim

/**
  *
  */
object CPSpacerSprite:
    /**
      * Creates new spacer sprite.
      *
      * @param width Width of the spacer.
      * @param height Height of the spacer.
      */
    def apply(width: Int, height: Int): CPSpacerSprite =
        new CPSpacerSprite(x = 0, 0, 0, width, height)

    /**
      * Creates new spacer sprite.
      *
      * @param dim Dimension of the spacer.
      */
    def apply(dim: CPDim): CPSpacerSprite =
        new CPSpacerSprite(x = 0, 0, 0, dim.width, dim.height)
