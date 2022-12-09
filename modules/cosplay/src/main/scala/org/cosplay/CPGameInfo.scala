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

import java.text.SimpleDateFormat
import java.util.Date

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
  * Descriptor of the game.
  *
  * @param id Unique ID of the game. If not provided, the default value will be a randomly generated globally unique ID.
  * @param name Public, display name of the game. This parameter is required and does not have default value.
  * @param semVer Semantic version of the game. See https://semver.org/ for details. Default value is `1.0.0`
  * @param initDim Optional initial game dimension. It is used only by `emuterm` built-in terminal emulator to
  *     set the initial terminal emulator dimension. It is ignored by the native terminal. If not provided,
  *     terminal emulator will use its default dimension.
  * @param termBg Optional background color for the terminal. The default value is `0x111111` RGB color.
  * @param minDim Optional minimal native terminal window size. This is only checked for the native terminal.
  *     If the native terminal size is smaller - an exception is thrown before game is started.
  *     For terminal emulator it is ignored.
  */
final case class CPGameInfo(
    id: String = CPRand.guid,
    name: String,
    semVer: String = "1.0.0",
    initDim: Option[CPDim] = None,
    termBg: CPColor = CPColor("0x111111"),
    minDim: Option[CPDim] = None
)
