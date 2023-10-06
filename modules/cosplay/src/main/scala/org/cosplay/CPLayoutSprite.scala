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
import org.cosplay.examples.layout.*

private[cosplay] enum CPLayoutDirection(private val s: String):
    case LEFT extends CPLayoutDirection("left")
    case RIGHT extends CPLayoutDirection("right")
    case TOP extends CPLayoutDirection("top")
    case BOTTOM extends CPLayoutDirection("bottom")
    case BEFORE extends CPLayoutDirection("before")
    case AFTER extends CPLayoutDirection("after")
    case BELOW extends CPLayoutDirection("below")
    case ABOVE extends CPLayoutDirection("above")
    case SAME extends CPLayoutDirection("same")
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
    var off: CPInt2 = CPInt2.ZERO,
    var x: CPLayoutRelation = CPLayoutRelation(LEFT, None),
    var y: CPLayoutRelation = CPLayoutRelation(TOP, None)
):
    override def toString: String =
        s"$id = " +
        s"off: $off, " +
        s"x: $x, " +
        s"y: $y" +
        ";"

/**
  * This is an off-screen sprite that provides layout capabilities for other dynamic sprites. This sprite
  * relies on provided layout specification that dictates how every sprite should be positioned in relation
  * to the entire screen or other sprite.
  *
  * Here are several important notes about how this sprite works:
  *  - This sprite only deals with the XY-position of sprites and does NOT deal with Z-index or the
  *    size of the sprite. In other words, it never resizes the sprite it is laying out and expects
  *    that all constituent sprites have their size correctly set in [[CPSceneObject.update]] method.
  *  - Only sprites that extend [[CPDynamicSprite]] can be using with this layout sprite.
  *  - This sprite perform the layout on each frame.
  *  - Laid out sprites can overlap.
  *
  * ### Layout Specification
  * Layout specification is a string that consists of semi-colon separated commands. Each command starts
  * with the sprite ID, followed by '=' character and a set of instructions for this sprite position.
  *
  * Here's basic BNF form for the layout specification:
  * {{{
  *    layout: decls* EOF;
  *    decls
  *        : decl
  *        | decls decl
  *        ;
  *    decl: ID '='' items ';';
  *    items
  *        : item
  *        | items ','' item
  *        ;
  *    item
  *        : offItem
  *        | xItem
  *        | yItem
  *        ;
  *    offItem: 'off' ':'' '[' NUM '.' NUM ']';
  *    xItem: 'x' ':' ('same' | 'before' | 'left' | 'center' | 'right' | 'after') '(' ID? ')'';
  *    yItem: 'y' ':' ('same' | 'above' | 'top' | 'center' | 'bottom' | 'below') '(' ID? ')';
  *
  *    NUM: '-'? [0-9] [0-9]*;
  *    ID: [a-zA-Z0-9-_$]+;
  * }}}
  *
  * Here's an example of the layout specification (from [[CPLayoutExample]]):
  * {{{
  * // This is the main panel centered on the screen.
  * Panel-1 = x: center(), y: center();
  *
  * // These panels are placed in the 4 corners of the panel 1.
  * Panel-2 = off: [1, 0], x: left(Panel-1), y: top(Panel-1);
  * Panel-3 = off: [-1, 0], x: right(Panel-1), y: top(Panel-1);
  * Panel-4 = off: [1, 0], x: left(Panel-1), y: bottom(Panel-1);
  * Panel-5 = off: [-1, 0], x: right(Panel-1), y: bottom(Panel-1);
  *
  * // Panels 6 and 7 go after each other.
  * Panel-6 = x: after(Panel-2), y: below(Panel-2);
  * Panel-7 = x: after(Panel-6), y: same(Panel-6);
  *
  * // Centered image with 2-row offset.
  * img = off: [0, 2], x: center(Panel-1), y: center(Panel-1);
  * }}}
  *
  * Notes:
  *  - Each sprite is positioned by its top-left XY-coordinates.
  *  - Offsets can be negative and are applied to the top-left XY-coordinates of the sprite.
  *  - Default specification is 'off: [0,0], x: left(), y: top();'
  *  - C-style comments are allowed in any place in the specification.
  *  - 'left', 'right', 'top' and 'bottom' are referring to a relative position inside of the referral.
  *  - 'before', 'after', 'above', and 'below' are referring to a relative position outside of the referral.
  *  - '()' (empty brackets) refer to a entire scene screen as a referral "sprite".
  *  - Only sprites extending [[CPDynamicSprite]] can be used in layout specification. However, any
  *    scene object can act as a referral.
  *  - 'center' provides centering for either X or Y-coordinates.
  *  - 'same' allows to use the same X or Y-coordinates as a referral.
  *  - Use [[CPSpacerSprite]] to help fill up space in your layout.
  *
  * @param id ID of this scene object.
  * @param spec Layout specification as described above. Note that specification can be updated
  *             later using [[CPLayoutSprite.updateSpec]] method.
  * @param shaders Optional sequence of shaders for this sprite. Default value is an empty sequence.
  * @param tags Optional set of organizational or grouping tags. By default, the empty set is used.
  * @see [[CPDynamicSprite]]
  * @see [[CPSpacerSprite]]
  * @see [[CPLayoutExample]]
  * @example See [[org.cosplay.examples.layout.CPLayoutExample CPLayoutExample]] class for the example of
  *     using this sprite.
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
                                        case SAME => spr.setX(xRelRect.x)
                                        case _ => throw CPException(s"Invalid X-coordinate direction: ${spec.x.dir}")
                                    spec.y.dir match
                                        case TOP => spr.setY(yRelRect.y + 1)
                                        case ABOVE => spr.setY(yRelRect.y - dim.h)
                                        case CENTER => spr.setY(yRelRect.y + (yRelRect.h - dim.h) / 2)
                                        case BOTTOM => spr.setY(yRelRect.yMax - dim.h)
                                        case BELOW => spr.setY(yRelRect.yMax + 1)
                                        case SAME => spr.setY(xRelRect.y)
                                        case _ => throw CPException(s"Invalid Y-coordinate direction: ${spec.y.dir}")
                                    spr.incrX(spec.off.x)
                                    spr.incrY(spec.off.y)
                                case None => ()
                        case _ => throw CPException(s"Only scene objects extending 'CPDynamicSprite' can be used in layout: $id")
                    laidOut += id
            obj

        for spec <- specs do layout(spec.id)

    /**
      * Updates the layout spec to given value. It will take an effect on the next frame.
      *
      * @param spec Layout specification.
      */
    def updateSpec(spec: String): Unit = specs = CPLayoutCompiler.compile(spec).getOrRethrow()


