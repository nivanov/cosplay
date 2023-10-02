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

import org.cosplay.*
import org.cosplay.CPColor.*
import org.cosplay.CPPixel.*
import org.cosplay.CPFIGLetFont.*
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
  *     $ mvn -f modules/cosplay -P ex:dialog exec:java
  * }}}
  *
  * @see [[CPLayoutSprite]]
  * @see [[CPDynamicSprite]]
  * @note See developer guide at [[https://cosplayengine.com]]
  */
object CPDialogExample:
    /**
      * Entry point for JVM runtime.
      *
      * @param args Ignored.
      */
    def main(args: Array[String]): Unit =
        val termDim = CPDim(100, 40)
        val bgPx = ' '&&(C_GRAY2, C_BLACK)
        val sc = new CPScene("scene", termDim.?, bgPx,
            // Just for the initial scene fade-in effect.
            new CPOffScreenSprite(new CPFadeInShader(true, 1500, bgPx)),
            // Spin up dialog workflow at the start.
            new CPSingletonSprite(fun = ctx =>
                CPDialogSupport.showYesNo(
                    ctx = ctx,
                    title = "Login",
                    msgs = "Do you want to start login demo?".seq,
                    onYes = x => CPDialogSupport.showLogin(
                        x,
                        onOk =
                            (x2, username, pwd) => CPDialogSupport.showConfirm(
                                x2,
                                title = "Login",
                                msgs = Seq(
                                    s"User entered <@$username@> username and <@$pwd@> password. ",
                                    "You can click <%ESC%> or <%[Enter]%> to exit this example."
                                ),
                                onEnd = _.exitGame()
                            ),
                        onCancel =
                            x2 => CPDialogSupport.showConfirm(
                                x2,
                                title = "Login",
                                msgs = Seq(
                                    "Login dialog was cancelled by the user.",
                                    "You can click <%ESC%> or <%[Enter]%> to exit this example."
                                ),
                                onEnd = _.exitGame()
                            ),
                    ),
                    onNo = _.exitGame()
                )
            )
        )

        // Initialize the engine.
        CPEngine.init(
            CPGameInfo(name = "Dialog Example", initDim = termDim.?),
            System.console() == null || args.contains("emuterm")
        )

        // Start the game & wait for exit.
        try CPEngine.startGame(sc)
        finally CPEngine.dispose()

        sys.exit(0)



