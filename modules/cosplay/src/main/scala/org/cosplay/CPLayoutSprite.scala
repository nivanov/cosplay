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

import org.cosplay.*
import org.cosplay.impl.layout.*
import scala.collection.mutable

private[cosplay] enum CPLayoutDirection(private val s: String):
    case LEFT extends CPLayoutDirection("left")
    case RIGHT extends CPLayoutDirection("right")
    case TOP extends CPLayoutDirection("top")
    case BOTTOM extends CPLayoutDirection("bottom")
    case BEFORE extends CPLayoutDirection("before")
    case AFTER extends CPLayoutDirection("after")
    case BELOW extends CPLayoutDirection("below")
    case ABOVE extends CPLayoutDirection("above")
    case CENTER extends CPLayoutDirection("center")
    override def toString: String = s

private[cosplay] sealed case class CPLayoutRelation(
    dir: CPLayoutDirection,
    rel: Option[String]
):
    override def toString: String = s"$dir(${rel.getOrElse("")})"
import CPLayoutDirection.*
private[cosplay] sealed case class CPLayoutSpec(
    var id: String,
    var offset: CPInt2 = CPInt2.ZERO,
    var x: CPLayoutRelation = CPLayoutRelation(LEFT, None),
    var y: CPLayoutRelation = CPLayoutRelation(TOP, None)
):
    override def toString: String =
        s"$id = " +
        s"margin: $offset, " +
        s"x: $x, " +
        s"y: $y" +
        ";"

/**
  * TODO
  *
  * @param id Optional ID of this scene object. By default, the random 6-character ID will be used.
  * @param spec
  * @param shaders Optional sequence of shaders for this sprite.
  * @param tags Optional set of organizational or grouping tags. By default, the empty set is used.
  * @see [[CPDynamicSprite]]
  */
class CPLayoutSprite(
    id: String,
    spec: String,
    shaders: Seq[CPShader] = Seq.empty,
    tags: Set[String] = Set.empty
) extends CPOffScreenSprite(id, shaders, tags):
    private var specs = CPLayoutCompiler.compile(spec).getOrRethrow()

    override def monitor(using ctx: CPSceneObjectContext): Unit =
        val laidOut = mutable.ArrayBuffer.empty[String]
        val seen = mutable.ArrayBuffer.empty[String]

        def layout(id: String): CPSceneObject =
            val obj = ctx.getObject(id) match
                case Some(o) => o
                case None => throw CPException(s"Attempt to layout scene object with unknown ID: $id")
            if specs.exists(_.id == id) then // Skip the layout for IDs not in the spec list.
                if !laidOut.contains(id) then
                    if seen.contains(id) then throw CPException(s"Cyclical layout dependency on ID: $id")
                    seen += id
                    obj match
                        case spr: CPDynamicSprite =>
                            specs.find(_.id == id) match
                                case Some(spec) =>
                                    def getRect(idOpt: Option[String]): CPRect =
                                        idOpt match
                                            case Some(id) => layout(id).getRect
                                            case None => ctx.getCanvas.rect
                                    val xRelRect = getRect(spec.x.rel)
                                    val yRelRect = getRect(spec.y.rel)
                                    val dim = spr.getDim
                                    spec.x.dir match
                                        case LEFT => spr.setX(xRelRect.x + 1)
                                        case BEFORE => spr.setX(xRelRect.x - dim.w)
                                        case CENTER => spr.setX(xRelRect.x + (xRelRect.w - dim.w) / 2)
                                        case RIGHT => spr.setX(xRelRect.xMax - dim.w)
                                        case AFTER => spr.setX(xRelRect.xMax + 1)
                                        case _ => throw CPException(s"Invalid X-coordinate direction: ${spec.x.dir}")
                                    spec.y.dir match
                                        case TOP => spr.setY(yRelRect.y + 1)
                                        case ABOVE => spr.setY(yRelRect.y - dim.h)
                                        case CENTER => spr.setY(yRelRect.y + (yRelRect.h - dim.h) / 2)
                                        case BOTTOM => spr.setY(yRelRect.yMax - dim.h)
                                        case BELOW => spr.setY(yRelRect.yMax + 1)
                                        case _ => throw CPException(s"Invalid Y-coordinate direction: ${spec.x.dir}")
                                    spr.incrX(spec.offset.x)
                                    spr.incrY(spec.offset.y)
                                case None => ()
                        case _ => throw CPException(s"Only scene objects extending 'CPDynamicSprite' can be used in layout: $id")
                    laidOut += id
            obj

        for spec <- specs do layout(spec.id)

    def updateSpec(spec: String): Unit = specs = CPLayoutCompiler.compile(spec).getOrRethrow()


