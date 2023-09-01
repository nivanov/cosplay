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
               All rights reserved.
*/

/**
  * Scene object context during frame update.
  *
  * This type is the main access point to the most of CosPlay functionality for the scene objects comprising
  * the gameplay. On each frame update all scene objects from the current scene receive [[CPSceneObject.update()]] call
  * and optional [[CPSceneObject.render()]] call both of which receive the instance of this type. Also, the
  * [[CPShader shaders]] attached to that scene objects, if any, receive the callback with the instance of this type.
  *
  * Scene object context functionality can be grouped into:
  *  - Keyboard input focus [[CPSceneObjectContext.getFocusOwner management]]
  *  - Accessing, adding, replacing and removing scene objects
  *  - Access [[CPSceneObjectContext.getGameCache game]] and [[CPSceneObjectContext.getSceneCache scene]] user-data storage
  *  - Access [[CPSceneObjectContext.getLog game log]]
  *  - [[CPSceneObjectContext.addScene() Adding]], [[CPSceneObjectContext.deleteScene() removing]], and [[CPSceneObjectContext.switchScene() switching]] scenes
  *  - Checking [[CPSceneObjectContext.collisions() collisions]]
  *  - [[CPSceneObjectContext.sendMessage() Sending]] and [[CPSceneObjectContext.receiveMessage() receiving]] messages between scene objects
  *  - Accessing game & [[CPSceneObjectContext.getRenderStats rendering]] statistics
  *  - Getting scene [[CPSceneObjectContext.getCanvas canvas]] to draw on
  *  - Accessing scene [[CPSceneObjectContext.getCamera camera descriptor]]
  *  - [[CPSceneObjectContext.exitGame() Exiting]] game
  *
  *
  * @see [[CPSceneObject.update()]]
  * @see [[CPSceneObject.render()]]
  * @see [[CPSceneObject.monitor()]]
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
      * Checks whether the object this context is attached to is visible.
      */
    def isVisible: Boolean

    /**
      * Gets current camera frame. Camera frame is always a sub-region of the scene. In most cases, it is
      * a smaller value between scene dimension and terminal dimension.
      */
    def getCameraFrame: CPRect

    /**
      * Exits the [[CPEngine.startGame()]] method.
      */
    def exitGame(): Unit

    /**
      * Schedules given function to run at least `delayMs` milliseconds later or upon scene switch. Given
      * function will only run if its timer elapses during the current scene or scene changes. Note that
      * given function will run at the minimum the next frame and never in the current frame
      * (even if `delayMs` is set to 1ms, for example).
      *
      * @param delayMs Minimum number of milliseconds before given function will run in the current scene.
      *         Note that the actual delay can be bigger but never smaller than this parameter unless
      *         scene changes.
      * @param f A function to run later in the current scene.
      */
    def runLater(delayMs: Long, f: CPSceneObjectContext => Unit): Unit

    /**
      * Schedules given function to run at the next frame update. More specifically,
      * this function will run before any of the scene objects updates on the next frame. Given function
      * will only run if the next frame belongs to the same scene. In other words, at scene switch
      * all currently pending functions will be discarded.
      *
      * @param f A function to run in the next frame update of the current scene.
      */
    def runNextFrame(f: CPSceneObjectContext => Unit): Unit

    /**
      * Deletes given scene object after this update cycle. Change will be visible only
      * on the next frame update. Note that focus owner will be released if held by the deleted
      * object.
      *
      * @param id ID of the scene object to delete after this update cycle.
      */
    def deleteObject(id: String): Unit

    /**
      * Deletes current scene object after this update cycle. Change will be visible only
      * on the next frame update. Note that focus owner will be released if held by the this
      * object.
      */
    def deleteMyself(): Unit = deleteObject(getId)

    /**
      * Adds given scene object to the scene after this update cycle. Change will be visible only
      * on the next frame update.
      *
      * @param obj Scene object to add.
      * @param replace If the scene object with the same ID as given object exists, this flag allows
      *     to remove the existing scene object first thus effectively replace it with the new scene object
      *     with the same ID. The default value is `false`.
      */
    def addObject(obj: CPSceneObject, replace: Boolean = false): Unit

    /**
      * Gets scene object with given ID or `None` if such object cannot be found.
      *
      * @param id ID of the scene object to get.
      */
    def getObject(id: String): Option[CPSceneObject]

    /**
      * Gets all scene objects in the current scene.
      */
    def getObjects: Iterable[CPSceneObject]

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
    def getObjectsForTags(tags: Set[String]): Seq[CPSceneObject]

    /**
      * Deletes scene objects with given tags. All tags must be present in the scene object
      * to be deleted. Change will be visible only on the next frame update. Note that focus owner
      * will be released if held by the deleted object.
      *
      * @param tags One or more tags to filter by.
      */
    def deleteObjectsForTags(tags: String*): Unit = getObjectsForTags(tags.toSet).foreach(obj => deleteObject(obj.getId))

    /**
      * Gets number of scene objects with given tags. All tags must be present in the scene object
      * to be returned as part of the result sequence.
      *
      * @param tags One or more tags to filter by.
      */
    def countObjectsForTags(tags: String*): Int

    /**
      * Adds new or replaces the existing scene. Change will be visible only on the next frame update.
      *
      * @param newSc A scene to add.
      * @param switchTo Whether or not to immediately switch to this scene right after this frame update cycle.
      * @param delCur If immediately switching to the new scene, whether or not to remove the current scene.
      * @param replace If the scene with the same ID as a new one already exists, this flag allows to delete
      *     the existing scene first thus effectively replacing the existing scene with the new one with the same ID.
      *     This is convenient shortcut for removing the scene manually before re-adding it again, if required. The
      *     default value is `false`. Note that you cannot replace the current scene as you cannot remove the
      *     current scene.
      */
    def addScene(newSc: CPScene, switchTo: Boolean = false, delCur: Boolean = false, replace: Boolean = false): Unit

    /**
      * Switches to the given scene. Note that switch will happen only after the current frame update cycle
      * completes.
      *
      * @param id ID of the scene to switch to.
      * @param delCur Whether or not to remove the current scene.
      */
    def switchScene(id: String, delCur: Boolean = false): Unit

    /**
      * Switches to the given scene as well as settings game cache parameters before the switch. Note that
      * switch will happen only after the current frame update cycle completes.
      *
      * @param id ID of the scene to switch to.
      * @param delCur Whether or not to remove the current scene.
      * @param cacheProps Game cache parameters to set before switching the scene.
      */
    def switchScene(id: String, delCur: Boolean, cacheProps: (String, AnyRef)*): Unit =
        val cache = getGameCache
        cacheProps.foreach(t => cache.put(t._1, t._2))
        switchScene(id, delCur)

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
      * Checks if the current frame's keyboard event, if any, contains given keyboard key.
      *
      * @param key Keyboard key to check.
      * @see [[getKbEvent]]
      * @see [[CPKeyboardEvent.key]]
      */
    def isKbKey(key: CPKeyboardKey): Boolean =
        getKbEvent match
            case Some(k) => k.key == key
            case None => false

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
    def acquireMyFocus(): Unit = acquireFocus(getId)

    /**
      * Gets the scene object this context is currently associated with.
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
