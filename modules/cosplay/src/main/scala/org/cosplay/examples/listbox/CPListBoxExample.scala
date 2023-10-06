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
import org.cosplay.CPStyledString.*
import org.cosplay.prefabs.scenes.*
import org.cosplay.prefabs.shaders.*
import scala.collection.mutable
import java.io.*

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
  * Code example for built-in listbox functionality.
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
    private val termDim = CPDim(100, 40)
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
      * Entry point for JVM runtime.
      *
      * @param args Ignored.
      */
    def main(args: Array[String]): Unit =
        val bgPx = ' '&&(C_GRAY2, C_BLACK)

        class FsModelElement(file: File, line: Seq[CPPixel]) extends CPListBoxElement[File]:
            override def getLine: Seq[CPPixel] = line
            override def getValue: File = file
        class FsModel(file: File) extends CPListBoxModel:
            private var selIdx = -1
            private var buf = mutable.ArrayBuffer.empty[FsModelElement]
            private var sz = 0

            rescan(file)

            override def getSelectionIndex: Int = selIdx
            override def getElement[File](i: Int): Option[CPListBoxElement[File]] =
                if i >= 0 && i < sz then buf(i).asInstanceOf[CPListBoxElement[File]].? else None
            override def getSize: Int = sz

            private def mkLine(s: String): Seq[CPPixel] = CPSystemFont.renderAsSeq(s, fg, bg.?)
            def rescan(file: File): Unit =
                buf.clear()
                val parent = file.getParentFile
                for f <- file.listFiles() do
                    if !f.getName.startsWith(".") then // Skip hidden files.
                        var line = mkLine(f.getName)
                        if f.isDirectory then line = line.map(_.withDarkerFg(.5f))
                        buf += FsModelElement(f, line)
                buf = buf.sortBy(_.getValue.getName)
                buf.prepend(FsModelElement(if parent != null then parent else file, mkLine("..")))
                buf.prepend(FsModelElement(file, mkLine(".")))
                sz = buf.size
                selIdx = 0
            def moveUp(): Unit = if selIdx > 0 then selIdx -= 1
            def moveDown(): Unit = if selIdx >= 0 && selIdx < sz - 1 then selIdx += 1

        val W = 30
        val H = 10
        // Get current working directory.
        val model: FsModel = new FsModel(File(File(".").getAbsolutePath).getParentFile)
        val sc = new CPScene("scene", termDim.?, bgPx,
            // Just for the initial scene fade-in effect.
            new CPOffScreenSprite(new CPFadeInShader(true, 1500, bgPx)),
            CPKeyboardSprite(_.exitGame(), KEY_LO_Q, KEY_UP_Q),  // Exit on 'Q' press.
            new CPListBoxSprite("listbox", 0, 0, 1,
                model = model,
                width = W, height = H,
                onKey = (ctx, _, key) => key match
                    case KEY_UP => model.moveUp()
                    case KEY_DOWN => model.moveDown()
                    case KEY_SPACE | KEY_ENTER => model.getSelectedValue[File] match
                        case Some(file) if file.isDirectory => model.rescan(file)
                        case _ => ()
                    case KEY_LO_Q | KEY_UP_Q => ctx.exitGame()
                    case _ => ()
                ,
                selSkin = (_, px) => px.withFg(bg).withBg(fg.?)
            ),
            // For the initial focus acquisition.
            CPSingletonSprite(fun = _.acquireFocus("listbox"), scope = CPSingletonScope.SCENE),
            // Bordered panel underneath the listbox.
            CPTitlePanelSprite(
                "border",
                0, 0, W + 2, H + 2, // "+2" accounts for border width (i.e. 2 pixels horizontally and vertically).
                z = -1, // Put border on the background.
                bg,
                "-.|'-'|.",
                fg,
                bg.?,
                styleStr("< ", fg) ++ styleStr("File Selector", C_DARK_ORANGE3) ++ styleStr(" >", fg),
                // Border darkening gradient.
                borderSkin = (_, y, px) => px.withDarkerFg(.8f - y.min(6) / 20.0f)
            ),
            // Buttons markup at the bottom.
            new CPImageSprite(
                "btns",
                new CPArrayImage(markup.process("<%[Up]%>/<%[Down]%> Move Up/Down    <%[Space]%>/<%[Enter]%> Select    <%[Q]%> Quit"))
            ),
            // "Path:" label.
            new CPLabelSprite("label", "Path:", fg.lighter(.5f), bg.?),
            // Sprite that renders currently selected path.
            new CPDynamicSprite("path"):
                private var w = 0
                private var pxs = Seq.empty[CPPixel]

                override def getDim: CPDim = CPDim(w, 1) // Dynamically update sprite's dimensions.
                override def update(ctx: CPSceneObjectContext): Unit =
                    pxs = model.getSelectedValue[File] match
                        case Some(f) => CPSystemFont.renderAsSeq(f.getAbsolutePath, fg, bg.?)
                        case None => Seq.empty
                    w = pxs.size // Make sure to update sprite's dimensions.
                override def render(ctx: CPSceneObjectContext): Unit = ctx.getCanvas.drawPixels(getX, getY, getZ, pxs)
            ,
            // Layout controller.
            CPLayoutSprite("layout",
                """
                  | border = x: center(), y: center(); // Center layout.
                  | listbox = x: center(border), y: center(border); // Centered within 'panel'.
                  | label = x: same(border), y: below(border), off: [1, 1];
                  | path = x: after(label), y: same(label), off: [1, 0];
                  | btns = x: center(), y: below(path), off: [0, 3];
                  |""".stripMargin
            )
        )

        // Initialize the engine.
        CPEngine.init(
            CPGameInfo(name = "ListBox Example - File Chooser", initDim = termDim.?),
            System.console() == null || args.contains("emuterm")
        )

        // Start the example & wait for exit.
        try CPEngine.startGame(
            new CPFadeShimmerLogoScene("logo", termDim.?, bgPx, List(C_STEEL_BLUE1, C_LIME, C_ORANGE1), "scene"),
            sc
        )
        finally CPEngine.dispose()

        sys.exit(0)



