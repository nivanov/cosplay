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

package org.cosplay.games.mir.scenes

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
import CPKeyboardKey.*
import games.mir.*
import prefabs.shaders.*
import prefabs.sprites.*

/**
  *
  * @param id ID of the scene.
  * @param bgSndFile Background audio file name.
  */
abstract class CPMirStarStreakSceneBase(id: String, bgSndFile: String) extends CPMirCrtSceneBase(id, bgSndFile):
    private val colors = Seq(FG)
    private val clickSnd: CPSound = CPSound(s"$SND_HOME/click.wav")
    private val errSnd: CPSound = CPSound(s"$SND_HOME/error.wav")
    private val confirmSnd: CPSound = CPSound(s"$SND_HOME/confirm.wav", .5f)

    protected val starStreakShdr: CPStarStreakShader = CPStarStreakShader(
        true,
        BG,
        Seq(
            CPStarStreak('.', colors, 0.025, 30, (-.3f, 0f), 0),
            CPStarStreak('.', colors, 0.015, 25, (-.7f, 0f), 0),
            CPStarStreak('_', colors, 0.005, 50, (-1f, 0f), 0)
        ),
        skip = (zpx, _, _) ⇒ zpx.z >= 1
    )

    /**
      * Plays click sound then starts the fade out shader with given on-finish closure.
      *
      * @param f Context closure to call.
      */
    protected def clickThenFade(f: CPSceneObjectContext ⇒ Unit): Unit =
        clickSnd.play(0, _ ⇒ fadeOutShdr.start(f))

    /**
      * Plays click sound and (asynchronously) calls given closure.
      *
      * @param f Closure to call.
      */
    protected def click(f: () ⇒ Unit): Unit =
        clickSnd.play()
        f()

    /**
      *
      * @param errMsg Error message (including markup).
      * @param onAct Call on [[CPSceneObject.onActivate()]] callback.
      * @param onDeact Call on [[CPSceneObject.onDeactivate()]] callback.
      * @param title Optional dialog title.
      */
    protected def showError(errMsg: String, onAct: () ⇒ Unit, onDeact: () => Unit, title: String = "Error"): Unit =
        val dash = "-" * (2 + title.length)
        val errPxs = markup.process(
            s"""
               | <@ $title @>
               | $dash
               |
               | $errMsg
               | Please try again.
               |
               |
               |
               | <%[SPACE]%>  Continue
            """.stripMargin
        )
        errSnd.play()
        addObjects(new CPCenteredImageSprite(img = CPArrayImage(errPxs, BG_PX).trimBg(_ == BG_PX), z = 2):
            override def onActivate(): Unit = onAct()
            override def onDeactivate(): Unit = onDeact()
            override def update(ctx: CPSceneObjectContext): Unit =
                super.update(ctx)
                ctx.acquireMyFocus() // Ensure that only this dialog gets keyboard focus.
                ctx.getKbEvent match
                    case Some(evt) ⇒ evt.key match
                        case KEY_SPACE ⇒ click(() ⇒ ctx.deleteMyself())
                        case _ ⇒ ()
                    case None ⇒ ()
        )

    /**
      *
      * @param confirmMsg Confirmation message (including markup).
      * @param onAct Call on [[CPSceneObject.onActivate()]] callback.
      * @param onDeact Call on [[CPSceneObject.onDeactivate()]] callback.
      * @param title Optional dialog title.
      */
    protected def showConfirm(confirmMsg: String, onAct: () ⇒ Unit, onDeact: () => Unit, title: String = "Confirmation"): Unit =
        val dash = "-" * (2 + title.length)
        val errPxs = markup.process(
            s"""
               | <@ $title @>
               | $dash
               |
               | $confirmMsg
               |
               |
               |
               | <%[SPACE]%>  Continue
            """.stripMargin
        )
        confirmSnd.play()
        addObjects(new CPCenteredImageSprite(img = CPArrayImage(errPxs, BG_PX).trimBg(_ == BG_PX), z = 2):
            override def onActivate(): Unit = onAct()
            override def onDeactivate(): Unit = onDeact()
            override def update(ctx: CPSceneObjectContext): Unit =
                super.update(ctx)
                ctx.acquireMyFocus() // Ensure that only this dialog gets keyboard focus.
                ctx.getKbEvent match
                    case Some(evt) ⇒ evt.key match
                        case KEY_SPACE ⇒ click(() ⇒ ctx.deleteMyself())
                        case _ ⇒ ()
                    case None ⇒ ()
        )

    // Make sure to call 'super(...)'.
    override def onActivate(): Unit =
        super.onActivate()
        starStreakShdr.start()

    // Make sure to call 'super(...)'.
    override def onDeactivate(): Unit =
        super.onDeactivate()
        starStreakShdr.stop()


