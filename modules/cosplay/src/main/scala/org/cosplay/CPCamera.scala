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
               All rights reserved.
*/

/**
  * Camera panning descriptor.
  *
  * Camera tracking is the process by which the game engine automatically focuses on a particular scene object
  * and tracks its movement across screen keeping so that it stays in focus. Without camera tracking a scene object
  * could move outside the visible screen and become invisible.
  *
  * Camera descriptor defines a rectangular sub-region of the screen called *focus frame*. A tracking
  * object can move freely as long as it is fully contained in that focus frame. Once the tracking object
  * (at least partially) moves outside the focus frame, the focus frame and therefore the visible portion of
  * the current scene will shift to bring the tracking object back into the focus frame. The amount of shift as
  * well as its velocity is also controlled by this descriptor:
  * <pre>
  * +--------------------------------------------------+
  * |scene                                             |
  * |         +-------------------------------+        |
  * |         |screen/canvas                  |        |
  * |         |     +--------------------+    |        |
  * |         |     |focus frame         |    |        |
  * |         |     |                    |    |        |
  * |         |     |       o            |    |        |
  * |         |     |      /|\ tracking  |    |        |
  * |         |     |      / \ object    |    |        |
  * |         |     |                    |    |        |
  * |         |     +--------------------+    |        |
  * |         |                               |        |
  * |         +-------------------------------+        |
  * |                                                  |
  * +--------------------------------------------------+
  * </pre>
  *
  * Camera control is a property of scene and available via [[CPScene.getCamera]] method. The camera
  * descriptor is a mutable object. Once obtained via [[CPScene.getCamera]] method you can configure
  * it. Note that the default camera descriptor returned by the scene is not configured for tracking.
  *
  * @example See [[org.cosplay.examples.camera.CPCameraExample CPCameraExample]] class for the example of
  *     using camera.
  */
open class CPCamera:
    private var focusTrackId = none[String]
    private var focusFrameInset: CPInsets = CPInsets.ZERO
    private var panningStepX = 1.0f
    private var panningStepY = 1.0f
    private var minPanningX: Boolean = true
    private var minPanningY: Boolean = true

    /**
      * Creates camera descriptor.
      *
      * @param focusTrackId ID of the scene object to track the focus for by panning the camera
      *     to follow the focus subject.
      * @param focusFrameInsets Focus frame defined as insets from screen size.
      */
    def this(focusTrackId: String, focusFrameInsets: CPInsets) =
        this()
        setFocusTrackId(focusTrackId.?)
        setFocusFrameInsets(focusFrameInsets)

    /**
      * Gets panning step for X-axis defining how fast the camera focus will catch up with
      * tracking object that is outside the camera frame. Value of `1.0f` (default) usually
      * provides the smoothest panning animation.
      *
      * Note that if tracking object moves faster than the panning of the camera it can move
      * outside the visible screen.
      */
    def getPanningStepX: Float = panningStepX

    /**
      * Sets the panning step for X-axis.
      *
      * Panning step defines how fast the camera focus will catch up with
      * tracking object that is outside the camera frame. Value of `1.0f` (default) usually
      * provides the smoothest panning animation.
      *
      * Note that if tracking object moves faster than the panning of the camera it can move
      * outside the visible screen.
      *
      * @param panningStepX Panning step for X-axis.
      */
    def setPanningStepX(panningStepX: Float): Unit =
        !>(panningStepX > 0, s"Panning x-step must be > 0: $panningStepX")
        this.panningStepX = panningStepX

    /**
      * Sets the panning step for Y-axis.
      *
      * Panning step  defines how fast the camera focus will catch up with
      * tracking object that is outside the camera frame. Value of `1.0f` (default) usually
      * provides the smoothest panning animation.
      *
      * Note that if tracking object moves faster than the panning of the camera it can move
      * outside the visible screen.
      *
      * @param panningStepY Panning step for Y-axis.
      */
    def setPanningStepY(panningStepY: Float): Unit =
        !>(panningStepY > 0, s"Panning y-step must be > 0: $panningStepY")
        this.panningStepY = panningStepY

    /**
      * Gets panning step for Y-axis defining how fast the camera focus will catch up with
      * tracking object that is outside the camera frame. Value of `1.0f` (default) usually
      * provides the smoothest panning animation.
      *
      * Note that if tracking object moves faster than the panning of the camera it can move
      * outside the visible screen.
      */
    def getPanningStepY: Float = panningStepY

    /**
      * Sets whether camera panning will return tracking object into the center of the focus frame
      * along X-axis or just minimally inside. Default value is `true`.
      *
      * @param minPanningX Whether or not to return tracking object to the center of the focus frame
      *     along X-axis.
      */
    def setMinPanningX(minPanningX: Boolean): Unit = this.minPanningX = minPanningX

    /**
      * Sets whether camera panning will return tracking object into the center of the focus frame
      * along Y-axis or just minimally inside. Default value is `true`.
      *
      * @param minPanningY Whether or not to return tracking object to the center of the focus frame
      *     along Y-axis.
      */
    def setMinPanningY(minPanningY: Boolean): Unit = this.minPanningY = minPanningY

    /**
      * Tests whether camera panning will return tracking object into the center of the focus frame
      * along X-axis or just minimally inside. Default value is `true`.
      */
    def isMinPanningX: Boolean = minPanningX

    /**
      * Tests whether camera panning will return tracking object into the center of the focus frame
      * along Y-axis or just minimally inside. Default value is `true`.
      */
    def isMinPanningY: Boolean = minPanningY

    /**
      * Gets focus frame as a set of insets from the screen.
      */
    def getFocusFrameInsets: CPInsets = focusFrameInset

    /**
      * Sets focus frame as a set of insets for the screen.
      *
      * @param focusFrameInsets Insets for the screen to defined the focus frame.
      */
    def setFocusFrameInsets(focusFrameInsets: CPInsets): Unit =
        !>(focusFrameInset >= 0, "Focus frame inset must be >= 0: $focusFrameInset")
        this.focusFrameInset = focusFrameInsets

    /**
      * Gets ID of the scene object to be tracked. Returning `None` will turn off the focus tracking.
      */
    def getFocusTrackId: Option[String] = focusTrackId

    /**
      * Sets ID of the scene object to be tracked. Default value is `None` which turns the focus tracking off.
      *
      * @param focusTrackId ID of the scene object to track the focus for by panning the camera
      *     to follow the tracking subject or `None` to turn the focus tracking off.
      */
    def setFocusTrackId(focusTrackId: Option[String]): Unit = this.focusTrackId = focusTrackId
