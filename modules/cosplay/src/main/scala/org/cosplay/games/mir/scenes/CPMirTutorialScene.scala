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

import org.cosplay.*
import CPKeyboardKey.*
import games.mir.*
import scenes.sprites.*
import prefabs.sprites.*

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
  *
  */
object CPMirTutorialScene extends CPMirStarStreakSceneBase("tutorial", "bg1.wav"):
    private val txtPxs = markup.process(
        s"""
           |  <%Tutorial%>
           |  --------
           |
           |  1. <~Overview~>
           |  <%Escape From Mir%> is a real time strategy game based on in-depth space
           |  station Mir simulation that requires discovery, puzzle solving and knowledge
           |  of the basic physics principles, engineering and computer science. Yes... this
           |  is the game where you code programs, crawl the file system, hack the OS and
           |  even solve equations to find your way out of the calamity of the orbital crash.
           |
           |  2. <~Controls~>
           |  You interact with the Mir space station via MirX - a Unix-like operating system
           |  available through the terminal you spotted when you regained the consciousness.
           |  MirX has all the major attributes of the modern Unix OS architecture including
           |  Unix shell and many familiar commands.
           |
           |  Throughout the game you can also use the following keyboard shortcuts:
           |  <%[F10]%>    - Open Game Menu
           |  <%[F12]%>    - Take *.xp Screenshot
           |  <%[Ctrl-Q]%> - Toggle FPS Overlay
           |  <%[Ctrl-L]%> - Open Dev Mode
           |
           |  3. <~Strategy~>
           |  Use common sense... Solve life threatening problems first. Seek out your
           |  remaining crew. Establish communications with Mission Control Center and devise
           |  the rescue plan. Be prepared for the unexpected. If things feel repetitive or
           |  mundane - there is always a way to automate it. Snoop around - the clues can be
           |  in unusual places. Remember that when in game the time flows in real time.
           |
           |  Above all - you must escape from Mir to survive!
           |
           |
           |
           |
           |                            <%[Space]%>  Continue
           |
        """.stripMargin
    )
    private val img = CPArrayImage(txtPxs, BG_PX).trimBg()

    addObjects(
        // Sprite for ghost images.
        new CPMirGhostSprite(false),
        new CPCenteredImageSprite(img = img, z = 2),
        new CPKeyboardSprite((ctx, key) ⇒ key match
            case KEY_SPACE ⇒ next(ctx ⇒ ctx.switchScene(ctx.getGameCache("next_scene")))
            case _ ⇒ ()
        ),
        // Add full-screen shaders - order is important.
        new CPOffScreenSprite(shaders = Seq(starStreakShdr, crtShdr, fadeInShdr, fadeOutShdr))
    )


