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

package org.cosplay.examples.video

import org.cosplay.*
import org.cosplay.CPPixel.*
import org.cosplay.CPColor.*

import scala.io.Source
import scala.util.Using

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
  * 40 frames from [[https://ascii.co.uk/animated-art/3d-tunnel-animated-ascii-art.html]].
  *
  * @see [[CPVideo]]
  * @see [[CPVideoSprite]]
  * @see [[CPVideoSpriteListener]]
  * @note Use [[https://www.ffmpeg.org/]] to convert video into separate JPEG images.
  * @note Use [[https://github.com/cslarsen/jp2a]] or similar to convert individual JPEG into ASCII.
  * @note See developer guide at [[https://cosplayengine.com]]
  */
object CPVideoClip extends CPVideo("vid", "https://ascii.co.uk/animated-art/3d-tunnel-animated-ascii-art.html"):
    private val RAW_FOOTAGE = "prefab/video/tunnel.txt" // Under 'resources' folder...
    private val FRAME_CNT = 40 // We know upfront that there are 40 frames there.
    private val frames: Seq[CPImage] = {
        val in = getClass.getClassLoader.getResourceAsStream(RAW_FOOTAGE)
        if in != null then
            Using.resource(Source.fromInputStream(in, "UTF-8")) { rs =>
                val lines = rs.getLines().toSeq.filter(_.trim.nonEmpty) // Load all lines and skip empty ones.
                lines.grouped(lines.size / FRAME_CNT).toSeq.map { frameLines =>
                    // Psychedelic mode :-)
                    val c = CS_X11_ALL.rand
                    new CPArrayImage(frameLines, (ch, _, _) => {
                        ch match
                            // Color it for more contrast.
                            case '.' => ch&c.darker(0.4)
                            case ',' => ch&c.darker(0.3)
                            case ':' => ch&c.darker(0.25)
                            case ';' => ch&c.darker(0.2)
                            case 'i' => ch&c.darker(0.15)
                            case '1' => ch&c.darker(0.05)
                            case ' ' => XRAY
                            case _ => ch&c
                    })
                }
            }
        else
            throw Exception(s"Unable to find or load: $RAW_FOOTAGE")
    }

    override val getFrameCount: Int = frames.size
    override val getFrameDim: CPDim = frames.head.getDim
    override def getFrame(idx: Int): CPImage = frames(idx)
