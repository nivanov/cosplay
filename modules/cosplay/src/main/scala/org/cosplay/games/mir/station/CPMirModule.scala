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

package org.cosplay.games.mir.station

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
               ALl rights reserved.
*/

/**
  *
  */
object CPMirModule:
    /**
      *
      * @param name
      * @param abbr
      * @param launchDate
      * @param pwrDev
      * @param oxyDev
      * @param fdrDev
      * @param fspDev
      * @param apsDev
      */
    def apply(
        name: String,
        abbr: String,
        launchDate: Date,
        pwrDev: CPMirModuleDevice,
        oxyDev: CPMirModuleDevice,
        fdrDev: CPMirModuleDevice,
        fspDev: CPMirModuleDevice,
        apsDev: CPMirModuleDevice
        ): CPMirModule =
        new CPMirModule:
            override def getName: String = name
            override def getAbbreviation: String = abbr
            override def getLaunchDate: Date = launchDate
            override def getAirPressureDevice: CPMirModuleDevice = apsDev
            override def getFireDetectorDevice: CPMirModuleDevice = fdrDev
            override def getFireSuppressionDevice: CPMirModuleDevice = fspDev
            override def getOxygenDetectorDevice: CPMirModuleDevice = oxyDev
            override def getPowerSupplyDevice: CPMirModuleDevice = pwrDev
/**
  *
  */
trait CPMirModule:
    /**
      *
      */
    def getName: String

    /**
      *
      */
    def getLaunchDate: Date

    /**
      *
      */
    def getAbbreviation: String

    /**
      *
      */
    def getPowerSupplyDevice: CPMirModuleDevice

    /**
      *
      */
    def getOxygenDetectorDevice: CPMirModuleDevice

    /**
      *
      */
    def getFireDetectorDevice: CPMirModuleDevice

    /**
      *
      */
    def getFireSuppressionDevice: CPMirModuleDevice

    /**
      *
      */
    def getAirPressureDevice: CPMirModuleDevice

