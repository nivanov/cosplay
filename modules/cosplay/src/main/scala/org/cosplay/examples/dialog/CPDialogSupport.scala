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
    private val liteBg = fg.darker(.6f)
    private val darkBg = fg.darker(.8f)
    private val markup = CPMarkup(
        fg,
        bg.?,
        Seq(
            CPMarkupElement("<%", "%>", _ && (liteFg, bg)), // Light.
            CPMarkupElement("<@", "@>", _ && (bg, liteFg)) // Dark.
        )
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
            z = -1, w, h,
            C_BLACK,
            "-.|'-'|.",
            C_GREEN_YELLOW,
            C_BLACK.?,
            Seq.empty,
           // Border darkening gradient.
           borderSkin = (_, y, px) => px.withDarkerFg(.8f - y.min(6) / 20.0f)
        )

    /**
      * Creates and returns list of dialog constituents including a layout controller.
      *
      * @param title Dialog title to be rendered using "Rectangles" ASCII font.
      * @param msgObjs Scene objects representing the message. They will be laid out vertically
      *                one below another. At least one object is required.
      * @param btnsMd Buttons line markup.
      */
    private def mkBasicDialogParts(
        title: String,
        msgObjs: Seq[CPSceneObject],
        btnsMd: String
    ): Seq[CPSceneObject] =
        !>(msgObjs.nonEmpty, "At least one message object is required.")
        val titleSpr = mkTitleSpr(title)
        val dashSpr = mkImageSpr(CPSystemFont.render("-" * titleSpr.getWidth, fg.darker(.5f)))
        val btnSpr = mkImageSpr(new CPArrayImage(markup.process(btnsMd)))
        val panelW = Seq(titleSpr.getWidth, msgObjs.map(_.getWidth).max, btnSpr.getImage.w).max
            + 4 // Left padding.
            + 4 // Right padding.
            + 2 // Account for left and right 1-pixel borders.
        val panelH = titleSpr.getHeight
            + 1 // Dash.
            + 1 // Empty line between the dash and the message.
            + msgObjs.size // Message.
            + 3 // 3 empty lines between message and the button.
            + 1 // Button line.
            + 3 // Top & bottom padding.
            + 2 // Account for top and bottom 1-pixel borders.
        val panelSpr = mkPanelSpr(panelW, panelH)
        val spacerSpr = CPSpacerSprite(1, 1)
        var lastId = spacerSpr.getId
        val msgLayoutSpec = for obj <- msgObjs yield
            val spec = s"${obj.getId} = x: same(${titleSpr.getId}), y: below($lastId);"
            lastId = obj.getId
            spec
        val layoutSpr = CPLayoutSprite(CPRand.guid6,
            s"""
               | // Centered dialog panel.
               | ${panelSpr.getId} = x: center(), y: center();
               | // Laying out constituent sprites.
               | ${titleSpr.getId} = x: left(${panelSpr.getId}), y: top(${panelSpr.getId}), off: [4, 1];
               | ${dashSpr.getId} = x: same(${titleSpr.getId}), y: below(${titleSpr.getId});
               | ${spacerSpr.getId} = x: same(${titleSpr.getId}), y: below(${dashSpr.getId});
               | ${msgLayoutSpec.mkString(" ")}
               | ${btnSpr.getId} = x: same(${titleSpr.getId}), y: below(${msgObjs.last.getId}), off: [0, 3];
               |""".stripMargin
        )
        Seq(panelSpr, titleSpr, dashSpr, btnSpr, spacerSpr, layoutSpr) ++ msgObjs

    /**
      * Example login dialog with username and password text fields.
      *
      * @param ctx The context of the caller.
      * @param onStart Function to call when dialog's constituent sprites are added to the scene but not rendered yet.
      * @param onOk Callback if 'ok' is chosen. Takes scene object context, entered username and password
      *             (both of which can be empty).
      * @param onCancel Callback if 'cancel' is chose.
      */
    def showLogin(
        ctx: CPSceneObjectContext,
        onStart: CPSceneObjectContext => Unit = _ => (),
        onOk: (CPSceneObjectContext, String, String) => Unit,
        onCancel: CPSceneObjectContext => Unit
    ): Unit =
        // Makes the skins for active/inactive text fields.
        def mkSkin(active: Boolean, passwd: Boolean): (Char, Int, Boolean) => CPPixel =
            (ch: Char, pos: Int, isCur: Boolean) =>
                val ch2 = if passwd && !ch.isWhitespace then '*' else ch
                if active then
                    if isCur then ch2 && (bg, fg)
                    else ch2 && (liteFg, liteBg)
                else
                    ch2 && (liteFg, darkBg)

        val titleSpr = mkTitleSpr("Login")
        val dashSpr = mkImageSpr(CPSystemFont.render("-" * titleSpr.getWidth, fg.darker(.5f)))
        val usrSpr = CPTextInputSprite(x = 0, 0, 1,
            40, 40,
            "",
            mkSkin(true, false),
            mkSkin(false, false),
            submitKeys = Seq(KEY_ENTER, KEY_TAB, KEY_CTRL_A)
        )
        val pwdSpr = CPTextInputSprite(x = 0, 0, 1,
            40, 40,
            "",
            mkSkin(true, true),
            mkSkin(false, true),
            submitKeys = Seq(KEY_ENTER, KEY_TAB, KEY_CTRL_A)
        )
        usrSpr.setNext(pwdSpr.getId.?)
        pwdSpr.setNext(usrSpr.getId.?)
        val usrLblSpr = new CPLabelSprite(x = 0, 0, 1, text = "Username:", fg)
        val pwdLblSpr = new CPLabelSprite(x = 0, 0, 1, text = "Password:", fg)
        val btnSpr = mkImageSpr(new CPArrayImage(markup.process("<%[ESC]%> Cancel    <%[Ctrl-A]%> Submit")))
        val panelW = usrSpr.getWidth // Text field.
            + 4 // Left padding.
            + 4 // Right padding.
            + 2 // Account for left and right 1-pixel borders.
        val panelH = titleSpr.getHeight
            + 1 // Dash.
            + 1 // Empty line between the dash and the message.
            + 5 // Two text fields with labels.
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
               | ${usrLblSpr.getId} = x: same(${titleSpr.getId}), y: below(${dashSpr.getId}), off: [0, 1];
               | ${usrSpr.getId} = x: same(${titleSpr.getId}), y: below(${usrLblSpr.getId});
               | ${pwdLblSpr.getId} = x: same(${titleSpr.getId}), y: below(${usrSpr.getId}), off: [0, 1];
               | ${pwdSpr.getId} = x: same(${titleSpr.getId}), y: below(${pwdLblSpr.getId});
               | ${btnSpr.getId} = x: same(${titleSpr.getId}), y: below(${pwdSpr.getId}), off: [0, 3];
               |""".stripMargin
        )
        val ctrlSpr = CPSingletonSprite(fun = _.acquireFocus(usrSpr.getId))
        val objs = Seq(panelSpr, titleSpr, dashSpr, usrLblSpr, usrSpr, pwdLblSpr, pwdSpr, btnSpr, layoutSpr, ctrlSpr)
        var isOk = false
        showDialog(
            ctx,
            objs,
            onStart,
            onEnd = x =>
                if isOk then
                    val username = usrSpr.getResult._2.getOrElse("")
                    val pwd = pwdSpr.getResult._2.getOrElse("")
                    onOk(x, username, pwd)
                else
                    onCancel(x),
            onKey = (x, key) => key match
                case KEY_ESC | KEY_CTRL_A =>
                    isOk = key == KEY_CTRL_A
                    true
                case _ => false
        )

    /**
      *
      * @param ctx The context of the caller.
      * @param title Dialog title to be rendered using "Rectangles" ASCII font.
      * @param msgs One or more message lines.
      * @param onStart Function to call when dialog's constituent sprites are added to the scene but not rendered yet.
      * @param onYes Callback when 'yes' is chosen.
      * @param onNo Callback when 'no' is chosen.
      */
    def showYesNo(
        ctx: CPSceneObjectContext,
        title: String,
        msgs: Seq[String],
        onStart: CPSceneObjectContext => Unit = _ => (),
        onYes: CPSceneObjectContext => Unit,
        onNo: CPSceneObjectContext => Unit
    ): Unit =
        require(msgs.nonEmpty, "At least one message is required.")
        val objs = mkBasicDialogParts(
            title,
            msgs.map(msg => mkImageSpr(CPSystemFont.render(msg, fg, bg.?))),
            "<%[N]%> No    <%[Y]%> Yes"
        )
        var isYes = false
        showDialog(
            ctx,
            objs,
            onStart,
            x => if isYes then onYes(x) else onNo(x),
            (x, key) => key match
                case KEY_ESC | KEY_UP_N | KEY_LO_Y | KEY_UP_Y | KEY_LO_N =>
                    isYes = key == KEY_UP_Y || key == KEY_LO_Y
                    true
                case _ => false
        )

    /**
      * Shows dialog with given title, message and `[Enter] Continue` button. `ESC` key also closes
      * the dialog.
      *
      * @param ctx The context of the caller.
      * @param title Dialog title to be rendered using "Rectangles" ASCII font.
      * @param msgs One or more message lines.
      * @param onStart Function to call when dialog's constituent sprites are added to the scene but not rendered yet.
      * @param onEnd Function to call after dialog's constituent sprites are removed from the scene.
      */
    def showConfirm(
        ctx: CPSceneObjectContext,
        title: String,
        msgs: Seq[String],
        onStart: CPSceneObjectContext => Unit = _ => (),
        onEnd: CPSceneObjectContext => Unit = _ => ()
    ): Unit =
        require(msgs.nonEmpty, "At least one message is required.")
        val objs = mkBasicDialogParts(
            title,
            msgs.map(msg => mkImageSpr(new CPArrayImage(markup.process(msg)))),
            "<%[Enter]%> Continue"
        )
        showDialog(
            ctx,
            objs,
            onStart,
            onEnd(_),
            (x, key) => key match
                case KEY_ESC | KEY_ENTER => true
                case _ => false
        )

    /**
      * Internal implementation for dialog logic.
      *
      * @param ctx The context of the caller.
      * @param objs Scene objects comprising the UI of the dialog. They will be automatically
      *             added to the current scene and automatically removed once the dialog is closed.
      * @param onStart Function to call when dialog's constituent sprites are added to the scene but not rendered yet.
      * @param onEnd Function to call after dialog's constituent sprites are removed from the scene.
      * @param onKey Called on each key press within the dialog. Should return `true` if dialog
      *              should be closed in response to this key press, `false` otherwise to keep
      *              dialog open.
      */
    private def showDialog(
        ctx: CPSceneObjectContext,
        objs: Seq[CPSceneObject],
        onStart: CPSceneObjectContext => Unit,
        onEnd: CPSceneObjectContext => Unit,
        onKey: (CPSceneObjectContext, CPKeyboardKey) => Boolean // Return 'true' to exit the dialog.
    ): Unit =
        // Add dialog components to the current scene.
        objs.foreach(ctx.addObject(_))
        // Add the controller sprite.
        ctx.addObject(CPOffScreenSprite(x =>  // Called on each update() callback.
            var keyOpt = none[CPKeyboardKey]
            // Scan text input sprites first since their hold keyboard focus.
            for obj <- objs if keyOpt.isEmpty do obj match
                case ti: CPTextInputSprite if ti.isReady => keyOpt = ti.getResult._1
                case _ => ()
            // If no key press found in text inputs, check regular keyboard event on each frame.
            if keyOpt.isEmpty then keyOpt = ctx.getKbEvent.flatMap(_.key.?)

            keyOpt match
                case Some(key) =>
                    if onKey(x, key) then
                        // Remove dialog components from the current scene.
                        objs.foreach(o => x.deleteObject(o.getId))
                        // Remove this controller scene object itself from the scene.
                        x.deleteMyself()
                        onEnd(x)
                case None => ()
        ))
        onStart(ctx)
