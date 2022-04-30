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
  * Root type for all CosPlay scene objects.
  *
  * Scene objects can be visible or invisible, have rectangular shape,
  * have XY-coordinates of its left-top corner and Z-index. On each game frame and for each scene object
  * the engine calls [[update()]] method and, if object is visible and in frame, calls
  * [[render()]] method followed by calling all of its [[getShaders shaders]].
  *
  * [[CPScene Scene]] is a container for for scene objects. Scene objects can be added or removed from
  * the scene dynamically via [[CPSceneObjectContext]] instance passed to [[update()]] and [[render()]] methods.
  * Note that all scenes should have at least one scene object. In CosPlay there is no hierarchy of scene objects
  * in the scene - all scene objects form a simple flat structure inside of the scene. Just like with any
  * other [[CPGameObject game objects]], you can use [[CPGameObject.getTags tags]] to organize scenes and scene objects.
  *
  * Generally, you would use one of many existing scene object subclasses like the various sprites provided out-of-the-box.
  * You can, however, just as easily subclass this class directly and provide the necessary implementation. At the
  * minimum you need to implement all abstract methods and at least one of either
  * [[render()]] or [[update()]] methods.
  *
  * Here's a basic minimal scene object implementation that prints "Hello CosPlay!" text at <code>(0,0)</code>
  * coordinate in black color:
  * {{{
  * object HelloSprite extends CPSceneObject("id"):
  *     override def getX: Int = 0
  *     override def getY: Int = 0
  *     override def getZ: Int = 0
  *     override def getDim: CPDim = CPDim(15, 1)
  *     override def render(ctx: CPSceneObjectContext): Unit =
  *         ctx.getCanvas.drawString(0, 0, 0, "Hello CosPlay!", CPColor.C_BLACK)
  * }}}
  *
  * If scene object is [[isVisible invisible]] than only [[update()]] method will be called
  * on each frame. If object is visible and in camera frame - method [[render()]] will be called as well to
  * render itself. Note that shaders are called always, regardless of whether the object visible, in camera
  * frame or invisible.
  *
  * @param id Optional ID of this scene object. By default, the random 6-character ID will be used.
  */
abstract class CPSceneObject(id: String = CPRand.guid6) extends CPGameObject(id) with CPLifecycle:
    private var visible = true

    private[cosplay] def toExtStr: String =
        val xy = s"($getX,$getY)"
        val tags = s"(${getTags.mkString(",")})"
        val collide = getCollisionRect match
            case Some(rect) => rect.toString
            case None => "none"
        s"[id=$id, tags=$tags, vis=$isVisible, xy=$xy, z=$getZ, dim=$getDim, col=$collide]"

    /**
      * Checks the visibility flag.
      *
      * If object is invisible than only [[update()]] method will be called
      * on each frame. If object is visible and in camera frame - method [[render()]] will be called
      * as well to render itself. Note that shaders are called regardless of whether the object visible, in camera
      * frame or invisible.
      *
      * @see [[show()]]
      * @see [[hide()]]
      * @see [[setVisible()]]
      */
    def isVisible: Boolean = visible

    /**
      * Sets visibility flag. Note that by default all scene objects are visible.
      *
      * @param vis `true` to make this object visible, `false` otherwise.
      * @see [[show()]]
      * @see [[hide()]]
      */
    def setVisible(vis: Boolean): Unit = visible = vis

    /**
      * Shortcut method for showing this object.
      * Note that by default all scene objects are visible.
      *
      * @see [[setVisible()]]
      */
    def show(): Unit = visible = true

    /**
      * Shortcut method for hiding this object.
      * Note that by default all scene objects are visible.
      *
      * @see [[setVisible()]]
      */
    def hide(): Unit = visible = false

    /**
      * Gets the optional list of [[CPShader shaders]] attached to this scene object. By default, returns
      * an empty list. Note that shaders are called regardless of whether the object visible, in camera
      * frame or invisible.
      */
    def getShaders: Seq[CPShader] = Seq.empty

    /**
      * Gets current X-coordinate of this object within dimensions of its scene. Note that returned value
      * is allowed to be outside scene's dimension (e.g. negative value). In such cases, the clipping of the
      * scene rendering will result in showing only portion or none of the object.
      */
    def getX: Int

    /**
      * Gets current Y-coordinate of this object within dimensions of its scene. Note that returned value
      * is allowed to be outside scene's dimension (e.g. negative value). In such cases, the clipping of the
      * scene rendering will result in showing only portion or none of the object.
      */
    def getY: Int

    /**
      * Gets Z-index or order to use in rendering of this object. A pixel with higher Z-index visually
      * overrides the overlapping pixel in the same XY-coordinate with equal or smaller Z-index.
      */
    def getZ: Int

    /**
      * Gets current dimension (width and height) of this object.
      *
      * @see [[getWidth]]
      * @see [[getHeight]]
      * @see [[getRect]]
      */
    def getDim: CPDim

    /**
      * Gets current width of this object.
      *
      * @see [[getHeight]]
      * @see [[getDim]]
      */
    inline def getWidth: Int = getDim.w

    /**
      * Gets current height of this object.
      *
      * @see [[getWidth]]
      * @see [[getDim]]
      */
    inline def getHeight: Int = getDim.h

    /**
      * Gets rectangular shape of this sprite. It is basically a combination of its top-left
      * corner XY-coordinate and sprite's dimension.
      *
      * @see [[getX]]
      * @see [[getY]]
      * @see [[getDim]]
      */
    def getRect: CPRect = new CPRect(getX, getY, getDim)

    /**
      * Gets optional collision shape or hit box for this sprite.
      *
      * @return Optional collision shape, `None` if this sprite does not support collisions.
      * @see [[CPSceneObjectContext.collisions()]]
      */
    def getCollisionRect: Option[CPRect] = None

    /**
      * Called to update the internal state of this scene object. This callback is called each frame on every
      * object in the scene and it is called before any [[render()]] callback. Note that all scene object will
      * receive this callback before first [[render()]] callback.
      *
      * @param ctx Frame context. This context provides bulk of functionality that a scene object
      *     can do in a game, e.g. interact with other scene objects, check collisions, read input
      *     events and manage input focus, add or remove scene objects, add new and switch between scenes, etc.
      * @see [[render()]]
      * @see [[isVisible]]
      */
    def update(ctx: CPSceneObjectContext): Unit = {}

    /**
      * Called to render this scene object. Only visible and in camera frame objects will receive this
      * callback. This callback is called on scene object
      * after all scene objects received [[update()]] callback. Note that unlike [[update()]] callbacks
      * and shaders that are called for all scene objects on each frame, this callback is only called for scene
      * objects that are visible and, at least partially, in camera frame.
      *
      * @param ctx Frame context. This context provides bulk of functionality that a scene object
      *     can do in a game, e.g. interact with other scene objects, check collisions, read input
      *     events and manage input focus, add or remove scene objects, add new and switch between scenes, etc.
      * @see [[update()]]
      * @see [[isVisible]]
      */
    def render(ctx: CPSceneObjectContext): Unit = {}
