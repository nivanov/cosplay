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

package org.cosplay.examples.dialog

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
import org.cosplay.CPColor.*
import org.cosplay.CPPixel.*

/**
  * Mixin trait that provide basic dialog support.
  */
object CPDialogSupport:
    private val bg = C_BLACK
    private val fg = C_GREEN
    private val liteFg = fg.lighter(.5f)
    private val markup = CPMarkup(
        fg,
        bg.?,
        CPMarkupElement("<%", "%>", _ && (liteFg, bg)).seq, // Light.
    )

    /**
      * Shows dialog with given title, message and '[Enter] Continue' button.
      *
      * @param ctx The context of the caller.
      * @param title Dialog title to be rendered using "Rectangles" ASCII font.
      * @param msg One line message.
      * @param onStart Function to call when dialog's constituent sprites are added to the scene but not rendered yet.
      * @param onEnd Function to call after dialog's constituent sprites are removed from the scene.
      */
    def showConfirm(
        ctx: CPSceneObjectContext,
        title: String,
        msg: String,
        onStart: () => Unit,
        onEnd: () => Unit,
        fg: CPColor,
    ): Unit =
        val titleImg = CPFIGLetFont.FIG_RECTANGLES.render(title, fg)
        val dashImg = CPSystemFont.render("-" * titleImg.w, fg)
        val msgImg = CPSystemFont.render(msg, fg)
        val btnImg = new CPArrayImage(markup.process("<%[Enter]%> Continue"))

    /**
      *
      * @param ctx The context of the caller.
      * @param objs
      * @param onStart Function to call when dialog's constituent sprites are added to the scene but not rendered yet.
      * @param onEnd Function to call after dialog's constituent sprites are removed from the scene.
      * @param onKey
      */
    def showDialog(
        ctx: CPSceneObjectContext,
        objs: Seq[CPSceneObject],
        onStart: () => Unit,
        onEnd: () => Unit,
        onKey: (CPSceneObjectContext, CPKeyboardKey) => Boolean // Return 'true' to exit the dialog.
    ): Unit =
        ctx.addObject(CPOffScreenSprite(c => { // Called on each update() callback.
            // Obtain the pressed key, if any.
            var keyOpt = none[CPKeyboardKey]

            // Test text input sprites first since their hold keyboard focus.
            for obj <- objs if keyOpt.isEmpty do obj match
                case ti: CPTextInputSprite => if ti.isReady then keyOpt = ti.getResult._1
                case _ => ()
            // If no text input, check regular keyboard event on each frame.
            if keyOpt.isEmpty then keyOpt = ctx.getKbEvent.flatMap(_.key.?)

            keyOpt match
                case Some(key) =>
                    if onKey(c, key) then
                        objs.foreach(o => c.deleteObject(o.getId))
                        c.deleteMyself()
                        onEnd()
                case None => ()
        }))
        objs.foreach(ctx.addObject(_))
        onStart()
