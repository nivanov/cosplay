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

package org.cosplay.examples.listbox

import org.cosplay.*
import org.cosplay.CPColor.*
import org.cosplay.CPPixel.*
import org.cosplay.CPKeyboardKey.*
import org.cosplay.prefabs.scenes.*
import org.cosplay.prefabs.shaders.*
import scala.collection.*

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
  *     $ mvn -f modules/cosplay -P ex:listbox exec:java
  * }}}
  *
  * @see [[CPListBoxSprite]]
  * @see [[CPDynamicSprite]]
  * @note See developer guide at [[https://cosplayengine.com]]
  */
object CPListBoxExample:
    /**
      * Entry point for JVM runtime.
      *
      * @param args Ignored.
      */
    def main(args: Array[String]): Unit =
        val termDim = CPDim(100, 40)
        val bgPx = ' '&&(C_GRAY2, C_BLACK)
        class FsModel(path: String) extends CPListBoxModel:
            private var idx = -1
            private val buf = mutable.ArrayBuffer.empty[CPListBoxElement[_]]
            private var sz = 0

            rescan(path)

            override def getSelectionIndex: Int = idx
            override def getElement[T](i: Int): Option[CPListBoxElement[T]] = if i >= 0 && i < sz then buf(i).asInstanceOf[CPListBoxElement[T]].? else None
            override def getSize: Int = sz

            def rescan(path: String): Unit =
                // TODO
                buf.clear()
                sz = 0
            def moveUp(): Unit = if idx > 0 then idx -= 1
            def moveDown(): Unit = if idx >= 0 && idx < sz - 1 then idx += 1

        val model = new FsModel("") // TODO
        val sc = new CPScene("scene", termDim.?, bgPx,
            // Just for the initial scene fade-in effect.
            new CPOffScreenSprite(new CPFadeInShader(true, 1500, bgPx)),
            CPKeyboardSprite(_.exitGame(), KEY_LO_Q, KEY_UP_Q),  // Exit on 'Q' press.
            new CPListBoxSprite("listbox", 0, 0, 1,
                model = model,
                width = 30, height = 10,
                onKey = (m, key) =>
                    ???,
                selSkin = (x, px) =>
                    ???
            ),
            CPLayoutSprite("layout", "listbox = x: center(), y: center();") // Center layout.
        )

        // Initialize the engine.
        CPEngine.init(
            CPGameInfo(name = "ListBox Example - [Q] To Exit", initDim = termDim.?),
            System.console() == null || args.contains("emuterm")
        )

        // Start the example & wait for exit.
        try CPEngine.startGame(
            new CPFadeShimmerLogoScene("logo", termDim.?, bgPx, List(C_STEEL_BLUE1, C_LIME, C_ORANGE1), "scene"),
            sc
        )
        finally CPEngine.dispose()

        sys.exit(0)



