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

import org.cosplay.impl.CPContainer

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
  * A scene is a container for [[CPSceneObject scene objects]].
  *
  * Scene has an ID, optional dimension and one or more scene objects. Note that scene must always have at
  * least one scene object. In CosPlay there is no hierarchy of scene objects in the scene - all scene objects form
  * a simple flat structure inside of the scene. Just like with any other [[CPGameObject game objects]], you can use
  * [[CPGameObject.getTags tags]] to organize scenes and scene objects.
  *
  * ### Creating a scene
  * Scene has a strong encapsulation contract in relation to its scene objects: you can only add initial scene objects to
  * the scene at the time of scene creation. Once scene is created, its constituent scene objects can only be
  * manipulated via methods in [[CPSceneObjectContext]] class passed to scene objects on each frame update.
  *
  * In CosPlay you create a new scene in one of two ways:
  *  - Instantiate [[CPScene]] class.<br/>
  *    When creating an instance of [[CPScene]] class you'll be using one of the constructors provided by [[CPScene]]
  *    class. Every constructor requires a set of initial scene objects to be supplied.
  *    {{{
  *        val myScene = new CPScene("id", None, bgPx, spr1, spr2)
  *    }}}
  *  - Extend [[CPScene]] class.<br/>
  *    When subclassing [[CPScene]] you need to use [[addObjects()]] method available to the sub-classes to add
  *    initial scene objects.
  *    {{{
  *         object MyScene extends CPScene("id", None, bgPx):
  *             addObjects(spr1, spr2)
  *    }}}
  *
  * Note that you can dynamically add and remove scene as well as scene objects via [[CPSceneObjectContext]] instance.
  * See [[CPSceneObjectContext]] API for more details.
  *
  * ### Scene dimension
  * CosPlay scene can be *adaptive* or *static* in terms of its dimensions. When scene dimension
  * is set it becomes unchangeable (static) for the lifecycle of the scene and its scene objects can rely on this fact. However,
  * if scene dimension is set as `None`, it will adapt to the terminal dimension on each frame meaning that the scene's
  * dimension and therefore its canvas on which all scene objects are rendered can change its size from frame to
  * frame. Make sure that all scene objects in the adaptive scene take this into account in their rendering routines.
  *
  * @param id ID of the scene.
  * @param dim Optional dimension of the scene. Note that if dimension is `None` then scene will adapt to the
  *     terminal dimension on each frame. That means that the scene's canvas on which all scene objects are
  *     rendered can change its size from frame to frame. In such case, make sure that all scene objects take this into
  *     account in their rendering routines.
  * @param bgPx Background pixel of the scene. Background pixel is shown when none of the scene objects
  *     has drawn a pixel at that particular coordinate.
  * @see [[CPEngine.startGame()]]
  * @see [[org.cosplay.prefabs.scenes.CPLogoScene]]
  */
open class CPScene(id: String, dim: Option[CPDim], bgPx: CPPixel) extends CPGameObject(id) with CPLifecycle:
    private val cam = CPCamera()
    private[cosplay] val objects = CPContainer[CPSceneObject]()

    /**
      * Adds scene object(s) to this scene.
      *
      * @param objs Scene objects to add.
      */
    protected def addObjects(objs: CPSceneObject*): Unit = objs.foreach(objects.add)

    /**
      * Gets camera panning descriptor associated with this scene. By default, the camera panning
      * is not attached to any scene object. You need to configure the returning camera descriptor
      * if you need camera tracking.
      */
    def getCamera: CPCamera = cam

    /**
      * Gets this scene dimension.
      */
    def getDim: Option[CPDim] = dim

    /**
      * Gets background pixel of this scene.
      */
    def getBgPixel: CPPixel = bgPx

    /**
      * Creates new scene with given parameters.
      *
      * @param id ID of the scene.
      * @param dim Optional dimension of the scene. Note that if dimension is `None` then scene will adapt to the
      *     terminal dimension on each frame. That means that the scene's canvas on which all scene objects are
      *     rendered can change its size from frame to frame. Make sure that all scene objects take this into
      *     account in their rendering routines.
      * @param objs Initial scene objects to add. Note that after the scene is created you can dynamically add and
      *     remove scene objects via [[CPSceneObjectContext]] instance. See [[CPSceneObjectContext]] API for
      *     more details.
      */
    def this(id: String, dim: Option[CPDim], bgPixel: CPPixel, objs: List[CPSceneObject]) =
        this(id, dim, bgPixel)
        objs.foreach(objects.add)

    /**
      * Creates new scene with given parameters.
      *
      * @param id ID of the scene.
      * @param dim Optional dimension of the scene. Note that if dimension is `None` then scene will adapt to the
      *     terminal dimension on each frame. That means that the scene's canvas on which all scene objects are
      *     rendered can change its size from frame to frame. Make sure that all scene objects take this into
      *     account in their rendering routines.
      * @param objs Initial scene objects to add. Note that after the scene is created you can dynamically add and
      *     remove scene objects via [[CPSceneObjectContext]] instance. See [[CPSceneObjectContext]] API for
      *     more details.
     */
    def this(id: String, dim: Option[CPDim], bgPixel: CPPixel, objs: CPSceneObject*) =
        this(id, dim, bgPixel)
        objs.foreach(objects.add)
