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

          2D ASCII JVM GAME ENGINE FOR SCALA3
              (C) 2021 Rowan Games, Inc.
                All rights reserved.
*/

/**
  * Output terminal interface.
  *
  * This is game engine view on the underlying terminal. CosPlay comes built-in with two
  * implementations for this interface:
  *  - [[org.cosplay.impl.jlineterm.CPJLineTerminal]] native JLine-based implementation that works with
  *    any OS command line terminal application supporting ANSI escape sequences are a standard
  *    for in-band signaling to control cursor location, color, font styling, and other options. For running
  *    games in xterm, iTerm, any VTE-based terminal, Windows Terminal, etc. this is the implementation
  *    to use.
  *  - [[org.cosplay.impl.emuterm.CPEmuTerminal]] GUI-based terminal emulator. It behaves in exactly the same
  *    manner as native ANSI-based terminal. It takes no advantage of raster graphics in drawing or color
  *    rendering. It also adheres to the FPS capping by the game engine. Terminal emulator is an ideal tool during
  *    the game development as you can quickly iterate, start and stop games right from IDEs without any need
  *    for the external builds.
  *
  * When initializing CosPlay game engine using one of the [[CPEngine.init()]] methods you are
  * passing a boolean parameter which indicates whether to use native or a GUI-based terminal
  * emulator. This way the game engine automatically knows which of the two built-in implementations
  * to use and you don't need to specify these classes anywhere else. For the most use cases this is
  * all that is needed.
  *
  * Note that there's an easy way to check whether or not your game is running in the native terminal.
  * Execute the following code somewhere in your game engine initialization routine:
  * {{{
  *     val isInNativeTerm = System.console() != null
  * }}}
  * and pass the appropriate flag into one of the [[CPEngine.init()]] methods. This way your game will automatically
  * choose the appropriate terminal implementation based on what environment it is running on.
  *
  * ### Custom Terminal Implementation
  * You may decide to provide your own terminal implementation, for example, targeting iOS, Android or WebGL output.
  * Here are the steps to do it:
  *  - Create a class that implements this trait.
  *  - This class should have at least one public constructor that takes one parameter of type [[CPGameInfo]].
  *  - This class should be instantiable through standard reflection from Scala code.
  *  - Set system property `COSPLAY_TERM_CLASSNAME=x.y.z.MyTerm`, where `x.y.z.MyTerm` is a fully qualified
  *    name of the class you developed. Make sure to set this system property before calling one of
  *    the [[CPEngine.init()]] methods.
  *  - Initialize the game engine as usual. Setting `COSPLAY_TERM_CLASSNAME` system property will override
  *    the default terminal selection.
  *
  * @see [[org.cosplay.impl.jlineterm.CPJLineTerminal]]
  * @see [[org.cosplay.impl.emuterm.CPEmuTerminal]]
  * @see [[CPGameInfo]]
  * @see [[CPEngine.init()]]
  */
trait CPTerminal:
    /**
      * Renders given screen. Note that both screen and camera frame can be smaller, equal or bigger
      * than available terminal dimension.
      *
      * @param scr Entire scene screen.
      * @param camRect Sub-region of screen to draw, i.e. camera frame. Camera frame
      *     is always smaller or equal to the screen dimension.
      * @param forceRedraw Whether or not to force a full redraw.
      */
    def render(scr: CPScreen, camRect: CPRect, forceRedraw: Boolean): Unit

    /**
      * Tests whether or not this implementation deals with a native ANSI-based terminal. GUI-based terminal emulator
      * implementation returns `false`, while JLine-based built-in implementation returns `true`.
      */
    def isNative: Boolean

    /**
      * Disposes this terminal. Terminal cannot be accessed after this call.
      */
    def dispose(): Unit

    /**
      * Gets current terminal dimension.
      */
    def getDim: CPDim

    /**
      * Sets the title of the terminal. Implementation can ignore this call if this is not supported by the
      * underlying terminal application.
      *
      * @param s Terminal title to set.
      */
    def setTitle(s: String): Unit

    /**
      * Gets a root logger for this terminal.
      */
    def getRootLog: CPLog

    /**
      * This is called if [[isNative]] method returns `false`. This call should return a pressed keyboard key.
      *
      * @see [[nativeKbRead()]]
      */
    def kbRead(): Option[CPKeyboardKey]

    /**
      * This is called if [[isNative]] method returns `true`. This call should return a pressed ASCII keyboard key.
      * Method should return `-1` if given timeout elapsed and no terminal key press detected.
      *
      * @param timeoutMs Timeout in milliseconds waiting for the terminal key press.
      */
    def nativeKbRead(timeoutMs: Long): Int
