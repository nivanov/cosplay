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

package org.cosplay.examples.tilemapper

import org.cosplay.*
import CPColor.*
import CPKeyboardKey.*
import CPArrayImage.*
import CPPixel.*
import org.cosplay.prefabs.shaders.CPFadeInShader

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
  * Code example for tile mapping functionality.
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
  *     $ mvn -f modules/cosplay -P ex:tilemapper exec:java
  * }}}
  *
  * @see [[CPTileMapper]]
  * @note See developer guide at [[https://cosplayengine.com]]
  */
object CPTileMapperExample:
    /**
      * Entry point for JVM runtime.
      *
      * @param args Ignored.
      */
    def main(args: Array[String]): Unit =
        val door = CPArrayImage(
            prepSeq(
            """
                | ___EXIT__
                ||       |x|
                ||       |x|
                ||      "|x|
                ||       |x|
                ||     _Jxx|
                ||  _,Jxxxx|
                ||,Jxxxxxxx|
            """
            ),
            (ch, _, _) => ch match
                case 'x' => '/'&C_GRAY3
                case ch @ ('E' | 'X' | 'I' | 'T') => ch&C_MEDIUM_VIOLET_RED
                case _ => ch&C_SANDY_BROWN
        ).trimBg()

        val brick = CPArrayImage(
            prepSeq( // 8x3
            """
                |^^~^^~^^
                |___[___[
                |_[___{__
            """
            ),
            (ch, _, _) => ch match
                case '^' => '^'&&(C_DARK_OLIVE_GREEN3, C_GREEN_YELLOW)
                case '~' => '~'&&(C_DARK_OLIVE_GREEN1, C_GREEN_YELLOW)
                case '{' => '['&&(C_SANDY_BROWN, C_DARK_ORANGE3)
                case '-' => '_'&&(C_SANDY_BROWN, C_DARK_ORANGE3)
                case c => c&&(C_MAROON, C_DARK_ORANGE3)
        )

        val alien = CPArrayImage(
            prepSeq(
            """
                |                  .-.
                |    .-""`""-.    |(@ @)
                | _/`oOoOoOoOo`\_ \ \-/
                |'.-=-=-=-=-=-=-.' \/ \
                |  `-=.=-.-=.=-'    \ /\
                |     ^  ^  ^       _H_ \
                |
            """),
            (ch, _, _) => ch match
                case '@' => '@'&C_INDIAN_RED
                case c @ ('o' | 'O') => c&C_SKY_BLUE1
                case '^' => '^'&C_ORANGE1
                case '=' => '='&C_DARK_GREEN
                case c => c&C_LIME
        ).trimBg()

        val tileMap = CPArrayImage(
            // # - brick
            // X - alien
            // D - door
            prepSeq(
            """
                |#        #
                | #
                |  #
                |   #
                |      D
                |    ####
                |####
                |
                |      ##
                |    ##
                |  ##
                |##
                |       X
                |##########
            """),
            (ch, _, _) => ch&C_BLUE // Does not matter for map.
        )

        val brickDim = brick.getDim
        val mapDim = tileMap.getDim
        val alienDim = alien.getDim
        val dim = CPDim(mapDim.w * brickDim.w, mapDim.h * brickDim.h)
        val bgPx = '.'&&(C_GRAY2, C_GRAY1)
        var objs = List[CPSceneObject](
            // Just for the initial scene fade-in effect.
            new CPOffScreenSprite(new CPFadeInShader(true, 1500, bgPx)),
            // Exit the game on 'q' press.
            CPKeyboardSprite(KEY_LO_Q, _.exitGame()), // Exit the game on 'q' press.
        )

        // Layout tile sprites and add them to the scene.
        // Use brick's dimension as a tile dimension.
        CPTileMapper.layout(0, 0, tileMap, tileDim = brickDim, (ppx, x, y) => ppx.char match
            case '#' => Option(new CPStaticImageSprite(x, y, 0, brick))
            // Account for the door's different height relative to the brick.
            case 'D' => Option(new CPStaticImageSprite(x + 1, y - 5, 0, door))
            case 'X' => Option(new CPStaticImageSprite(
                x,
                // Account for the alien's different height relative to the brick.
                y - alienDim.height + brickDim.height + 1,
                0,
                alien))
            case _ => None
        ).foreach(objs ::= _)

        // Initialize the engine.
        CPEngine.init(
            CPGameInfo(name = "TileMapper Example", initDim = Option(dim)),
            System.console() == null || args.contains("emuterm")
        )

        // Start the game & wait for exit.
        try CPEngine.startGame(new CPScene("scene", Option(dim), bgPx, objs))
        finally CPEngine.dispose()

        sys.exit(0)
