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

package org.cosplay.games.mir

import org.apache.commons.text.*
import org.cosplay.*

import java.util.Locale

/*
   _________            ______________
   __  ____/_______________  __ \__  /_____ _____  __
   _  /    _  __ \_  ___/_  /_/ /_  /_  __ `/_  / / /
   / /___  / /_/ /(__  )_  ____/_  / / /_/ /_  /_/ /
   \____/  \____//____/ /_/     /_/  \__,_/ _\__, /
                                            /____/

          2D ASCII JVM GAME ENGINE FOR SCALA3
              (C) 2021 Rowan Games, Inc.
                ALl rights reserved.
*/

/**
  *
  */
case class CPMirPlayerData(
    firstName: String,
    lastName: String,
    locale: Locale
):
    val dobDay: Int = CPRand.between(1, 28 + 1)
    val dobMonth: Int = CPRand.between(1, 12 + 1)
    val dobYear: Int = CPRand.between(1997 - 35, 1997 - 55 + 1)
    val voiceSubFolder: String = "us"
    val nameCamelCase: String = s"${CaseUtils.toCamelCase(s"$firstName", true, ' ')} ${CaseUtils.toCamelCase(s"$lastName", true, ' ')}"
    val nameUpperCase: String = s"$firstName $lastName".toUpperCase
    val nameLowerCase: String = s"$firstName $lastName".toLowerCase
    val username: String = s"${firstName}_$lastName".toLowerCase
    val age: Int = 1997 - dobYear

    def debugString = s"$nameCamelCase, age $age, @$username, dob: $dobMonth/$dobDay/$dobYear, locale: $locale"

/**
  *
  */
object CPMirPlayerData:
    /* https://en.wikipedia.org/wiki/List_of_astronauts_by_name*/

    private val usFN = Seq(
        "Liam", "Noah", "Oliver", "Elijah", "James", "Bill", "Benjamin", "Lucas", "Henry", "Theo", "Alan",
        "Michel", "Mark", "Abe", "Jacob", "Dan", "Jayden", "Mason", "Matt", "Ethan", "Andrew", "Alex",
        "Chris", "Tyler", "David", "John", "Rob", "Rick"
    ).distinct
    private val ruFN = Seq(
        "Sergey", "Vladimir", "Anton", "Alex", "Aleksey", "Yuri", "Stanislav", "Oleg", "Ivan", "Anatoli",
        "Geogri", "Valentin", "Valery", "Konstantin", "Viktor", "Evgeniy", "Petr"
    ).distinct
    private val usLN = Seq(
        "Acaba", "Acton", "Adams", "Adamson", "Akers", "Aldrin", "Allen", "Alsbury", "Altman", "Anders",
        "Anderson", "Antonelli", "Amstrong", "Arnold", "Ashby"
    ).distinct
    private val ruLN = Seq(
        "Afanasyev", "Aimbetov", "Aksyonov", "Aleksandrov", "Artemyev", "Artsebarsky", "Artyukhin",
        "Atkov", "Aubakirov", "Avdeyev"
    ).distinct

    private val names = Map[Locale, (Seq[String], Seq[String])](
        Locale.US -> (usFN, usLN),
        new Locale("ru", "RU") -> (ruFN, ruLN)
    )

    /**
      *
      * @return
      */
    def genNewPlayer: CPMirPlayerData =
        val loc = Locale.getDefault
        names.get(loc) match
            case Some((fn, ln)) => CPMirPlayerData(CPRand.rand(fn), CPRand.rand(ln), loc)
            case None =>
                // Default to 
                val (fn, ln) = names(Locale.US)
                CPMirPlayerData(CPRand.rand(fn), CPRand.rand(ln), Locale.US)
