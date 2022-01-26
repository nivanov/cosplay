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

import scala.collection.mutable

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
  * Scene object context during frame update.
  *
  * This type is main access point to the most of CosPlay functionality for the scene objects comprising
  * the gameplay. On each game frame update all scene objects from the current scene receive [[CPSceneObject.update()]] call
  * and optional [[CPSceneObject.render()]] call both of which receive the instance of this type. Also, the
  * [[CPShader shaders]] attached to that scene objects, if any, receive the callback with the instance of this type.
  *
  * Scene object context functionality can be grouped into:
  *  - Keyboard input focus [[CPSceneObjectContext.getFocusOwner management]]
  *  - Accessing, adding, and removing scene objects
  *  - Access [[CPSceneObjectContext.getGameCache game]] and [[CPSceneObjectContext.getSceneCache scene]] user-data storage
  *  - Access [[CPSceneObjectContext.getLog game log]]
  *  - [[CPSceneObjectContext.addScene() Adding]], [[CPSceneObjectContext.deleteScene() removing]], and [[CPSceneObjectContext.switchScene() switching]] scenes
  *  - Check [[CPSceneObjectContext.collisions() collisions]]
  *  - [[CPSceneObjectContext.sendMessage() Send]] and [[CPSceneObjectContext.receiveMessage() receive]] messages between scene objects
  *  - Accessing game & [[CPSceneObjectContext.getRenderStats rendering]] statistics
  *  - Getting scene [[CPSceneObjectContext.getCanvas canvas]] to draw on
  *  - Accessing scene [[CPSceneObjectContext.getCamera camera descriptor]]
  *  - [[CPSceneObjectContext.exitGame() Exiting]] game
  *
  *
  * @see [[CPSceneObject.update()]]
  * @see [[CPSceneObject.render()]]
  * @see [[CPShader.render()]]
  */
trait CPSceneObjectContext extends CPBaseContext:
    /**
      * ID of the scene object that is being processed.
      */
    def getId: String

    /**
      * The canvas instance on which this scene object renders its content. In most cases, you should only
      * render in [[CPSceneObject.render()]] call.
      */
    def getCanvas: CPCanvas

    /**
      * Gets rendering statistics, if available.
      *
      * @see [[getFrameCount]]
      * @see [[getFrameMs]]
      * @see [[getStartGameMs]]
      * @see [[getSceneFrameCount]]
      */
    def getRenderStats: Option[CPRenderStats]

    /**
      * Gets camera control descriptor for the current scene.
      */
    def getCamera: CPCamera

    /**
      * Gets current camera frame. Camera frame is always a sub-region of the scene. In most cases, it is
      * smaller of scene dimension and terminal dimension.
      */
    def getCameraFrame: CPRect

    /**
      * Exits the [[CPEngine.startGame()]] method.
      */
    def exitGame(): Unit

    /**
      * Deletes given scene object after this update cycle. Change will be visible only
      * on the next frame update.
      *
      * @param id ID of the scene object to delete after this update cycle.
      */
    def deleteObject(id: String): Unit

    /**
      * Adds given scene object to the scene after this update cycle. Change will be visible only
      * on the next frame update.
      *
      * @param obj Scene object to add.
      */
    def addObject(obj: CPSceneObject): Unit

    /**
      * Gets scene object with given ID.
      *
      * @param id ID of the scene object to get.
      */
    def getObject(id: String): Option[CPSceneObject]

    /**
      * Gets scene object with given ID. Throws [[CPException]] exception if object with given
      * ID not found.
      *
      * @param id ID of the scene object to get.
      * @throws CPException Thrown if object with given ID not found.
      */
    def grabObject(id: String): CPSceneObject

    /**
      * Gets sequence of scene objects with given tags. All tags must be present in the scene object
      * to be returned as part of the result sequence.
      *
      * @param tags One or more tags to filter by.
      */
    def getObjectsForTags(tags: String*): Seq[CPSceneObject]

    /**
      * Adds new scene. Change will be visible only on the next frame update.
      *
      * @param newSc A scene to add.
      * @param switchTo Whether or not to immediately switch to this scene right after this frame update cycle.
      * @param delCur If immediately switching to the new scene, whether or not to remove the current scene.
      */
    def addScene(newSc: CPScene, switchTo: Boolean = false, delCur: Boolean = false): Unit

    /**
      * Switches to the given scene. Note that switch will happen only after the current frame update cycle
      * completes.
      *
      * @param id ID of the scene to switch to.
      * @param delCur Whether or not to remove the current scene.
      */
    def switchScene(id: String, delCur: Boolean = false): Unit

    /**
      * Deletes given scene. Note that you can't delete current scene.
      *
      * @param id ID of the scene to delete. Cannot be the current scene.
      */
    def deleteScene(id: String): Unit

    /**
      * Gets current frame's keyboard event.
      */
    def getKbEvent: Option[CPKeyboardEvent]

    /**
      * Tests whether or not current object is a input keyboard focus owner.
      *
      * @see [[acquireFocus()]]
      * @see [[getFocusOwner]]
      * @see [[acquireMyFocus()]]
      * @see [[releaseFocus()]]
      * @see [[releaseMyFocus()]]
      */
    def isFocusOwner: Boolean = getFocusOwner match
        case Some(id) => getId == id
        case None => false

    /**
      * Makes the current scene object the owner of the input keyboard focus.
      *
      * @see [[acquireFocus()]]
      * @see [[getFocusOwner]]
      * @see [[acquireMyFocus()]]
      * @see [[releaseFocus()]]
      * @see [[releaseMyFocus()]]
      */
    def acquireMyFocus(): Unit

    /**
      * Gets the current owner of input keyboard focus.
      */
    def getOwner: CPSceneObject

    /**
      * Makes the scene object with given ID the owner of the input keyboard focus.
      *
      * @param id ID of the scene object.
      * @see [[acquireFocus()]]
      * @see [[getFocusOwner]]
      * @see [[acquireMyFocus()]]
      * @see [[releaseFocus()]]
      * @see [[releaseMyFocus()]]
      */
    def acquireFocus(id: String): Unit

    /**
      * Gets ID of the scene object owning the input focus, if any. If `None` - no object is
      * owning the input focus and all objects will get input events.
      *
      * @see [[acquireFocus()]]
      * @see [[getFocusOwner]]
      * @see [[acquireMyFocus()]]
      * @see [[releaseFocus()]]
      * @see [[releaseMyFocus()]]
      */
    def getFocusOwner: Option[String]

    /**
      * Releases the input keyboard focus if it is held by the current scene object. No-op in all other
      * cases.
      *
      * @see [[acquireFocus()]]
      * @see [[getFocusOwner]]
      * @see [[acquireMyFocus()]]
      * @see [[releaseFocus()]]
      * @see [[releaseMyFocus()]]
      */
    def releaseMyFocus(): Unit

    /**
      * Releases the input focus if it is help by the object with given ID. No-op in all
      * other cases.
      *
      * @param id ID of the scene object.
      * @see [[acquireFocus()]]
      * @see [[getFocusOwner]]
      * @see [[acquireMyFocus()]]
      * @see [[releaseFocus()]]
      * @see [[releaseMyFocus()]]
      */
    def releaseFocus(id: String): Unit

    /**
      * Sends direct message(s) to the scene object with given ID. Scene object must
      * belong to the current scene, i.e. one cannot send a message to the scene objects
      * from another scene. To exchange data between scenes you should use
      * [[CPSceneObjectContext.getGameCache game cache]]. Note that messages will be available to
      * recipient scene objects starting with the next frame. Messages will be stored until they are
      * retrieved or the scene is changed.
      *
      * @param id Scene object ID from the current scene.
      * @param msgs Messages to send.
      * @see [[receiveMessage()]]
      */
    def sendMessage(id: String, msgs: AnyRef*): Unit

    /**
      * Gets direct messages send to this scene object, if any. Returns an empty sequence if no messages
      * pending delivery. Note that sent messages are stored until they are retrieved or the scene is changed.
      *
      * @see [[sendMessage()]]
      */
    def receiveMessage(): Seq[AnyRef]

    /**
      * Gets the sequence of scene objects this object is colliding with. If no collisions are detected - this method
      * returns an empty list.
      *
      * **NOTE**: for a scene object to participate in the collision it must provide its
      * [[CPSceneObject.getCollisionRect collision shape]]. If scene object does not provide the
      * [[CPSceneObject.getCollisionRect collision shape]] (the default behavior) it will be ignored
      * when checking for collision by other scene objects, and an exception will be thrown if such
      * object initiates the check for the collision with itself.
      *
      * @param zs Zero or more Z-indexes to check collision at. If none provided, scene objects at all
      *     Z-indexes with be checked.
      * @see [[CPSceneObject.getCollisionRect]]
      */
    def collisions(zs: Int*): Seq[CPSceneObject]

    /**
      * Gets the sequence of scene objects colliding with given rectangular. If no collisions are
      * detected - this method returns an empty list.
      *
      * **NOTE**: for a scene object to participate in the collision it must provide its
      * [[CPSceneObject.getCollisionRect collision shape]]. If scene object does not provide the
      * [[CPSceneObject.getCollisionRect collision shape]] (the default behavior) it will be ignored
      * when checking for collision by other scene objects, and an exception will be thrown if such
      * object initiates the check for the collision with itself.
      *
      * @param zs Zero or more Z-indexes to check collision at. If none provided, scene objects at all
      *     Z-indexes with be checked.
      * @see [[CPSceneObject.getCollisionRect]]
      */
    def collisions(rect: CPRect, zs: Int*): Seq[CPSceneObject]

    /**
      * Gets the sequence of scene objects colliding with given coordinate. If no collisions are
      * detected - this method returns an empty list.
      *
      * **NOTE**: for a scene object to participate in the collision it must provide its
      * [[CPSceneObject.getCollisionRect collision shape]]. If scene object does not provide the
      * [[CPSceneObject.getCollisionRect collision shape]] (the default behavior) it will be ignored
      * when checking for collision by other scene objects, and an exception will be thrown if such
      * object initiates the check for the collision with itself.
      *
      * @param zs Zero or more Z-indexes to check collision at. If none provided, scene objects at all
      *     Z-indexes with be checked.
      * @see [[CPSceneObject.getCollisionRect]]
      */
    def collisions(x: Int, y: Int, zs: Int*): Seq[CPSceneObject]
