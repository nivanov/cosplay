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

package org.cosplay

import scala.collection.mutable

/**
  * Utility that provides tile mapping functionality.
  *
  * @example See [[org.cosplay.examples.tilemapper.CPTileMapperExample CPTileMapperExample]] class for the example of
  *     using tile mapper.
  */
object CPTileMapper:
    /**
      * Given tile map and individual tile dimension this method lays out the tiles and returns the list
      * of scene objects representing the tile map. A tile `map` is an image where each pixel can be mapped
      * into a scene object using `mapping` function.
      *
      * @param x X-coordinate of the initial tile.
      * @param y Y-coordinate of the initial tile.
      * @param map An image representing the tile map.
      * @param tileDim The dimension of the individual tile. Note that all tiles will have to be of the same dimension.
      * @param mapping Mapping function that takes a pixel and its coordinates from the tile map and
      *     returns a scene object that should represent that tile.
      */
    def layout(
        x: Int,
        y: Int,
        map: CPImage,
        tileDim: CPDim,
        mapping: (CPPosPixel, Int, Int) => Option[CPSceneObject]
    ): List[CPSceneObject] =
        var a = x
        var b = y
        val buf = mutable.ArrayBuffer.empty[CPSceneObject]
        map.loop((px, mapX, mapY) =>
            a = x + mapX * tileDim.w
            b = y + mapY * tileDim.h
            mapping(CPPosPixel(px, mapX, mapY), a, b) match
                case Some(obj) => buf += obj
                case None => ()
        )
        buf.toList

