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
    lastName: String
):
    val dobDay: Int = CPRand.between(1, 28 + 1)
    val dobMonth: Int = CPRand.between(1, 12 + 1)
    val dobYear: Int = CPRand.between(1997 - 35, 1997 - 55 + 1)
    val nameCamelCase: String = s"${CaseUtils.toCamelCase(s"$firstName", true, ' ')} ${CaseUtils.toCamelCase(s"$lastName", true, ' ')}"
    val nameUpperCase: String = s"$firstName $lastName".toUpperCase
    val nameLowerCase: String = s"$firstName $lastName".toLowerCase
    val username: String = s"${firstName}_$lastName".toLowerCase
    val age: Int = 1997 - dobYear

    def debugString = s"$nameCamelCase, age $age, @$username, dob: $dobMonth/$dobDay/$dobYear"

/**
  *
  */
object CPMirPlayerData:
    /* https://en.wikipedia.org/wiki/List_of_astronauts_by_name */

    private val firstNames = Seq(
        "Liam",         "Noah",         "Oliver",       "James",        "Bill",         "Lucas",
        "Henry",        "Theo",         "Alan",         "Michel",       "Mark",         "Abe",
        "Jacob",        "Dan",          "Jayden",       "Mason",        "Matt",         "Ethan",
        "Andrew",       "Alex",         "Chris",        "Tyler",        "David",        "John",
        "Rob",          "Rick",         "Ivan",         "Leo",          "Valery",       "Viktor",
        "Vlad",         "Sergey",       "Lee",          "Boe",          "Gabriel",      "Samuel",
        "Elias",        "Matis",        "Max",          "Jonas",        "Noah",         "Felix",
        "Oskar",        "Emil",         "Henry",        "Denis",        "Kurt",         "Paul",
        "Otto",         "Frank",        "Sebastian",    "Hermann",      "Dieter",       "Mateo",
        "Santiago",     "Anders",       "Bjorn",        "Jens",         "Asger",        "Dustin",
        "Erik",         "Fiske",        "Garth",        "Gunnar",       "Ingvar",       "Ivar",
        "Sven",         "Torvald",      "Oliver"
    ).distinct
    private val lastNames = Seq(
        "Acaba",        "Acton",        "Adams",        "Adamson",      "Akers",        "Aldrin",
        "Allen",        "Altman",       "Anders",       "Anderson",     "Amstrong",     "Arnold",
        "Ashby",        "Baker",        "Banker",       "Barron",       "Barry",        "Bassett",
        "Bartoe",       "Bolden",       "Bowen",        "Bowmen",       "Brady",        "Brown",
        "Bresnik",      "Bridges",      "Burbank",      "Cabana",       "Cameron",      "Carey",
        "Carr",         "Casper",       "Cassidy",      "Cernan",       "Chelli",       "Chapman",
        "Clark",        "Collins",      "Coleman",      "Cooper",       "Conrad",       "Dana",
        "Davis",        "Duffy",        "Duke",         "Dunbar",       "Dutton",       "Dyson",
        "Edwards",      "Engle",        "Evans",        "Fabian",       "Faris",        "Favier",
        "Finley",       "Fisher",       "Freeman",      "Gardner",      "Gibson",       "Glenn",
        "Goodwin",      "Glover",       "Gordon",       "Hammond",      "Hart",         "Hennen",
        "Hoffman",      "Hurley",       "Hughes",       "Irwin",        "Johnson",      "Jones",
        "Kelly",        "Kopra",        "Kulin",        "Lawrence",     "Leslie",       "Lind",
        "Lindsey",      "Lockhart",     "Lu",           "Mackay",       "Magnus",       "Mann",
        "Meade",        "Melnik",       "Mitchell",     "Morin",        "Mullane",      "Musgrave",
        "Nelson",       "Newman",       "Nowak",        "Nyberg",       "Parker",       "Payton",
        "Peterson",     "Polansky",     "Remek",        "Robinson",     "Resnik",       "Rogers",
        "Runco",        "Satcher",      "Scott",        "Shane",        "Smith",        "Shriver",
        "Stewart",      "Swanson",      "Tanner",       "Thomas",       "Taylor",       "Thorton",
        "Voss",         "Walker",       "Weber",        "Wolf",         "Webster",      "Woodward",
        "Young",        "Zamka"
    ).distinct

    /**
      *
      * @return
      */
    def newPlayer: CPMirPlayerData = CPMirPlayerData(CPRand.rand(firstNames), CPRand.rand(lastNames))
