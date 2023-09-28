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

package org.cosplay.examples.textinput

import org.cosplay.*
import org.cosplay.CPColor.*
import org.cosplay.CPPixel.*
import org.cosplay.CPKeyboardKey.*
import org.cosplay.CPStyledString.*
import org.cosplay.prefabs.shaders.*

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
  * Code example for built-in text input functionality.
  *
  * ### Running Example
  * One-time Git clone & build:
  * {{{
  *     $ git clone https://github.com/nivanov/cosplay.git
  *     $ cd cosplay
  *     $ mvn package
  * }}}
  * to run example:
  * {{{
  *     $ mvn -f modules/cosplay -P ex:textinput exec:java
  * }}}
  *
  * @see [[CPLabelSprite]]
  * @see [[CPTextInputSprite]]
  * @see [[CPKeyboardKey]]
  * @see [[CPKeyboardEvent]]
  * @see [[CPKeyboardSprite]]
  * @note See developer guide at [[https://cosplayengine.com]]
  */
object CPTextInputExample:
    /**
      * Entry point for JVM runtime.
      *
      * @param args Ignored.
      */
    def main(args: Array[String]): Unit =
        val termDim = CPDim(100, 40)

        def mkSkin(active: Boolean, passwd: Boolean): (Char, Int, Boolean) => CPPixel =
            (ch: Char, pos: Int, isCur: Boolean) =>
                val ch2 = if passwd && !ch.isWhitespace then '*' else ch
                if active then
                    if isCur then ch2&&(C_WHITE, C_SLATE_BLUE3)
                    else ch2&&(C_BLACK, C_WHITE)
                else ch2&&(C_BLACK, C_WHITE.darker(0.3f))

        val userTin = CPTextInputSprite("usrTin", 0, 0, 1,
            15, 20,
            "",
            mkSkin(true, false),
            mkSkin(false, false),
            submitKeys = Seq(KEY_ENTER, KEY_TAB),
            next = "pwdTin".?
        )
        val pwdTin = CPTextInputSprite("pwdTin", 0, 0, 1,
            15, 20,
            "",
            mkSkin(true, true),
            mkSkin(false, true),
            submitKeys = Seq(KEY_ENTER, KEY_TAB),
            next = "usrTin".?
        )
        val panel = CPTitlePanelSprite(
            "panel",
            0, 0, 23, 9, 0,
            C_BLACK,
            "-.|'-'|.",
            C_GREEN_YELLOW,
            C_BLACK.?,
            styleStr("< ", C_GREEN_YELLOW) ++ styleStr("Login", C_DARK_ORANGE3) ++ styleStr(" >", C_GREEN_YELLOW),
            borderSkin = (_, _, px) => px.withDarkerFg(0.5f),
        )
        val bgPx = '.'&&(C_GRAY2, C_GRAY1)
        val sc = new CPScene("scene", termDim.?, bgPx,
            // Just for the initial scene fade-in effect.
            new CPOffScreenSprite(new CPFadeInShader(true, 1500, bgPx)),
            new CPLabelSprite("usrLbl", 0, 0, 1, text = "Username:", C_LIGHT_STEEL_BLUE),
            new CPLabelSprite("pwdLbl", 0, 0, 1, text = "Password:", C_LIGHT_STEEL_BLUE),
            userTin,
            pwdTin,
            panel,
            // Acquire the focus at the beginning by username text input.
            CPOffScreenSprite(ctx => if ctx.getSceneFrameCount == 0 then ctx.acquireFocus("usrTin")),
            CPLayoutSprite("layout",
                """
                  | // Centered dialog panel.
                  | panel = x: center(), y: center();
                  |
                  | // Username.
                  | usrLbl = off: [3, 1], x: left(panel), y: top(panel);
                  | usrTin = x: same(usrLbl), y: below(usrLbl);
                  |
                  | // Password.
                  | pwdLbl = off: [3, 1], x: left(panel), y: below(usrTin);
                  | pwdTin = x: same(pwdLbl), y: below(pwdLbl);
                  |""".stripMargin
            )
        )

        // Initialize the engine.
        CPEngine.init(
            CPGameInfo(name = "Text Input Example", initDim = termDim.?),
            System.console() == null || args.contains("emuterm")
        )

        // Start the game & wait for exit.
        try CPEngine.startGame(sc)
        finally CPEngine.dispose()

        sys.exit(0)


