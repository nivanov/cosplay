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
    case CENTER extends CPLayoutDirection("center")
    override def toString: String = s

private[cosplay] sealed class CPLayoutRelation(
    dir: CPLayoutDirection,
    rel: Option[String]
):
    override def toString: String = s"$dir(${rel.getOrElse("")})"
import CPLayoutDirection.*
private[cosplay] sealed case class CPLayoutSpec(
    var id: String,
    var padding: CPInsets = CPInsets.ZERO,
    var xFloat: CPLayoutRelation = CPLayoutRelation(LEFT, None),
    var yFloat: CPLayoutRelation = CPLayoutRelation(TOP, None),
    var pos: CPLayoutRelation = CPLayoutRelation(BOTTOM, None)
):
    override def toString: String =
        s"$id = " +
        s"top: ${padding.top}, " +
        s"left: ${padding.left}, " +
        s"bottom: ${padding.bottom}, " +
        s"right: ${padding.right}, " +
        s"xfloat: $xFloat, " +
        s"yfloat: $yFloat, " +
        s"pos: $pos" +
        ";"

/**
  *
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
    private var specs = Seq.empty[CPLayoutSpec]

    private def warn(msg: String): Unit = ctx.getLog.warnx(CPEngine.fps * 60, msg) // Throttle for a minute.
    override def monitor(ctx: CPSceneObjectContext): Unit =
        val laidOut = mutable.ArrayBuffer.empty[String]
        val seen = mutable.ArrayBuffer.empty[String]

        def layout(spr: CPDynamicSprite): Unit = ???

        for spec <- specs do
            ctx.getObject(spec.id) match
                case Some(o) =>
                    if o.isVisible then // Ignore invisible scene objects.
                        o match
                            case spr: CPDynamicSprite => layout(spr)
                            case _ => throw CPException(s"Only scene objects extending 'CPDynamicSprite' can be used in layout: ${spec.id}")
                case None =>
                    warn(s"Attempt to layout scene object with unknown ID (ignoring): ${spec.id}")
                    () // Ignore unknown object.

    def updateSpec(code: String): Unit = specs = CPLayoutCompiler.compile(code).getOrRethrow()


