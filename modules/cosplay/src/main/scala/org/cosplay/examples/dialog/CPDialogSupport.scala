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
import org.cosplay.CPKeyboardKey.*
import org.cosplay.CPPixel.*

/**
  * Simple "framework" for basic modal dialog functionality.
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
      * Makes dynamic image sprite with given image.
      *
      * @param img Image to make the dynamic image sprite with.
      */
    private def mkImageSpr(img: CPImage): CPImageSprite = new CPImageSprite(
        // Coordinates don't matter as it will be laid out.
        x = 0, y = 0, z = 0,
        img = img
    )

    /**
      * Makes "Rectangle" font label sprite for given title with gradual vertical darkening.
      *
      * @param text Title string.
      */
    private def mkTitleSpr(text: String): CPLabelSprite = new CPLabelSprite(
        x = 0, y = 0, z = 0,
        font = CPFIGLetFont.FIG_RECTANGLES,
        fg = fg,
        text = text,
        skin = (px, _, y) =>
            if y == 0 then px.withDarkerFg(.5f)
            else if y == 1 then px.withDarkerFg(.3f)
            else if y == 2 then px.withDarkerFg(.2f)
            else px
    )

    /**
      * Creates panel sprite with given width and height.
      *
      * @param w Panel width.
      * @param h Panel height.
      */
    private def mkPanelSpr(w: Int, h: Int): CPTitlePanelSprite =
        CPTitlePanelSprite(
            CPRand.guid6,
            0, 0, w, h, -1,
            C_BLACK,
            "-.|'-'|.",
            C_GREEN_YELLOW,
            C_BLACK.?,
            Seq.empty,
            borderSkin = (_, _, px) => px.withDarkerFg(0.5f),
        )

    /**
      * Shows dialog with given title, message and `[Enter] Continue` button. `ESC` key also closes
      * the dialog.
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
        onStart: (CPSceneObjectContext) => Unit = _ => (),
        onEnd: (CPSceneObjectContext) => Unit = _ => ()
    ): Unit =
        val titleSpr = mkTitleSpr(title)
        val dashSpr = mkImageSpr(CPSystemFont.render("-" * titleSpr.getWidth, fg.darker(.5f)))
        val msgSpr = mkImageSpr(CPSystemFont.render(msg, fg))
        val btnSpr = mkImageSpr(new CPArrayImage(markup.process("<%[Enter]%> Continue")))
        val panelW = Seq(titleSpr.getWidth, msgSpr.getImage.w, btnSpr.getImage.w).max
            + 4 // Left padding.
            + 4 // Right padding.
            + 2 // Account for left and right 1-pixel borders.
        val panelH =  titleSpr.getHeight
            + 1 // Dash.
            + 1 // Empty line between the dash and the message.
            + 1 // Message.
            + 3 // 3 empty lines between message and the button.
            + 1 // Button line.
            + 3 // Top & bottom padding.
            + 2 // Account for top and bottom 1-pixel borders.
        val panelSpr = mkPanelSpr(panelW, panelH)
        val layoutSpr = CPLayoutSprite(CPRand.guid6,
            s"""
              | // Centered dialog panel.
              | ${panelSpr.getId} = x: center(), y: center();
              | // Laying out constituent sprites.
              | ${titleSpr.getId} = x: left(${panelSpr.getId}), y: top(${panelSpr.getId}), off: [4, 1];
              | ${dashSpr.getId} = x: same(${titleSpr.getId}), y: below(${titleSpr.getId});
              | ${msgSpr.getId} = x: same(${titleSpr.getId}), y: below(${dashSpr.getId}), off: [0, 1];
              | ${btnSpr.getId} = x: same(${titleSpr.getId}), y: below(${msgSpr.getId}), off: [0, 3];
              |""".stripMargin
        )
        val objs = Seq(panelSpr, titleSpr, dashSpr, msgSpr, btnSpr, layoutSpr)
        showDialog(
            ctx,
            objs,
            onStart,
            x => {
                objs.foreach(o => x.deleteObject(o.getId))
                onEnd(x)
            },
            (x, key) => key match
                case KEY_ESC | KEY_ENTER => true
                case _ => false
        )

    /**
      *
      * @param ctx The context of the caller.
      * @param objs Dynamic sprite comprising the UI of the dialog.
      * @param onStart Function to call when dialog's constituent sprites are added to the scene but not rendered yet.
      * @param onEnd Function to call after dialog's constituent sprites are removed from the scene.
      * @param onKey
      */
    def showDialog(
        ctx: CPSceneObjectContext,
        objs: Seq[CPSceneObject],
        onStart: (CPSceneObjectContext) => Unit,
        onEnd: (CPSceneObjectContext) => Unit,
        onKey: (CPSceneObjectContext, CPKeyboardKey) => Boolean // Return 'true' to exit the dialog.
    ): Unit =
        objs.foreach(ctx.addObject(_))
        // Add the controller sprite.
        ctx.addObject(CPOffScreenSprite(x => { // Called on each update() callback.
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
                    if onKey(x, key) then
                        objs.foreach(o => x.deleteObject(o.getId))
                        x.deleteMyself()
                        onEnd(x)
                case None => ()
        }))
        onStart(ctx)
