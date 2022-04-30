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

import impl.CPUtils

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
  * System 1-character high font.
  *
  * @see [[CPFIGLetFont]]
  * @see [[CPStyledString]]
  * @example See [[org.cosplay.examples.fonts.CPFontsExample CPFontsExample]] source code for an
  *     example of font functionality.
  */
object CPSystemFont extends CPFont(getClass.getName):
    /** @inheritdoc */ 
    override def isSystem: Boolean = true
    /** @inheritdoc */ 
    override def getHeight: Int = 1
    /** @inheritdoc */ 
    override def getWidth: Int = 1
    /** @inheritdoc */ 
    override def getEncoding: String = "UTF-8"
    /** @inheritdoc */ 
    override def getBaseline: Int = 1
    /** @inheritdoc */ 
    override def render(s: String, fg: CPColor, bg: Option[CPColor]): CPImage = new CPArrayImage(s, fg, bg)



