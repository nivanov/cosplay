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
import scala.collection.mutable.ArrayBuffer

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
enum CPMirPLayerRole:
    /** Chief flight engineer. */
    case ROLE_ENGINEER

    /** Chief mission specialist. */
    case ROLE_SPECIALIST

    /** Chief mission pilot. */
    case ROLE_PILOT

/**
  *
  * @param name
  * @param isMale
  */
case class CPMirPlayerChild(
    name: String,
    isMale: Boolean
) extends Serializable:
    val dobDay: Int = CPRand.between(1, 28 + 1)
    val dobMonth: Int = CPRand.between(1, 12 + 1)
    val dobYear: Int = CPRand.between(EVENT_YEAR - 1, EVENT_YEAR - 20 + 1)
    val age: Int = EVENT_YEAR - dobYear

    def debugString: String = s"$name, ${if isMale then "boy" else "girl"}, age $age, dob: $dobMonth/$dobDay/$dobYear"

/**
  *
  * @param firstName
  * @param lastName
  * @param birthCountry
  * @param birthTown
  * @param homeCountry
  * @param homeTown
  * @param role
  * @param wifeFirstName
  * @param wifeLastName
  * @param children
  */
case class CPMirPlayer(
    firstName: String,
    lastName: String,
    birthCountry: String,
    birthTown: String,
    homeCountry: String,
    homeTown: String,
    role: CPMirPLayerRole,
    wifeFirstName: String,
    wifeLastName: String,
    children: Seq[CPMirPlayerChild]
) extends Serializable:
    val dobDay: Int = CPRand.between(1, 28 + 1)
    val dobMonth: Int = CPRand.between(1, 12 + 1)
    val dobYear: Int = CPRand.between(EVENT_YEAR - 35, EVENT_YEAR - 55 + 1)
    val nameCamelCase: String = s"${CaseUtils.toCamelCase(s"$firstName", true, ' ')} ${CaseUtils.toCamelCase(s"$lastName", true, ' ')}"
    val nameUpperCase: String = s"$firstName $lastName".toUpperCase
    val nameLowerCase: String = s"$firstName $lastName".toLowerCase
    val wifeCamelCase: String = s"${CaseUtils.toCamelCase(s"$wifeFirstName", true, ' ')} ${CaseUtils.toCamelCase(s"$wifeLastName", true, ' ')}"
    val wifeUpperCase: String = s"$wifeFirstName $wifeLastName".toUpperCase
    val wifeLowerCase: String = s"$wifeFirstName $wifeLastName".toLowerCase
    val username: String = s"${firstName}_$lastName".toLowerCase
    val age: Int = EVENT_YEAR - dobYear

    def debugString: String =
        s"""
           |$nameCamelCase, @$username, $role, living in $homeTown, $homeCountry
           |age $age, dob: $dobMonth/$dobDay/$dobYear in $birthTown, $birthCountry
           |married to $wifeCamelCase
           |${children.size} children: ${children.map(_.debugString).mkString(", ")}
           |""".stripMargin

/**
  *
  */
object CPMirPlayer:
    private val towns = Seq(
        ("USA", "Baltimore"),
        ("USA", "Boston"),
        ("USA", "Chicago"),
        ("Spain", "Madrid"),
        ("Belgium", "Brussels"),
        ("Bosnia", "Sarajevo"),
        ("Canada", "Ottawa"),
        ("Canada", "Toronto"),
        ("Canada", "Montreal"),
        ("Canada", "Vancouver"),
        ("Chile", "Santiago"),
        ("Colombia", "Bogota"),
        ("Cyprus", "Nicosia"),
        ("Finland", "Helsinki"),
        ("France", "Paris"),
        ("Norway", "Oslo"),
        ("Germany", "Berlin"),
        ("Greece", "Athens"),
        ("Hungary", "Budapest"),
        ("Iceland", "Reykjavik"),
        ("Ireland", "Dublin"),
        ("Italy", "Rome"),
        ("Poland", "Warsaw"),
        ("Portugal", "Lisbon"),
        ("Serbia", "Belgrade"),
        ("USA", "Miami"),
        ("USA", "Atlanta"),
        ("Sweden", "Stockholm"),
        ("Switzerland", "Bern"),
        ("England", "London"),
        ("Sweden", "Stockholm"),
        ("USA", "Austin"),
        ("USA", "Seattle"),
        ("USA", "Nashville"),
        ("USA", "Charleston"),
        ("USA", "Portland"),
        ("USA", "Denver"),
        ("USA", "Houston"),
        ("USA", "Phoenix"),
        ("USA", "Charlotte"),
        ("USA", "Dallas")
    )

    private val girlNames = Seq(
        "Olivia",       "Emma",         "Charlotte",    "Amelia",       "Ava",          "Sophia",
        "Isabella",     "Mia",          "Evelyn",       "Harper",       "Angelique",    "Vida",
        "Emberlynn",    "Flora",        "Murphy",       "Arleth",       "Ocean",        "Oakleigh",
        "Freyja",       "Mylah",        "Taytum",       "Elia",         "Jaylani",      "Zayla",
        "Navy",         "Della",        "Clover",       "Ruby",         "Brooklyn",     "Alice",
        "Aubrey",       "Autumn",       "Leilani",      "Savannah",     "Valentina",    "Kennedy",
        "Madelyn",      "Josephine",    "Bella",        "Skylar",       "Genesis",      "Sophie",
        "Hailey",       "Sadie",        "Natalia",      "Quinn",        "Caroline",     "Allison",
        "Gabriella",    "Anna",         "Serenity",     "Nevaeh",       "Cora",         "Ariana",
        "Emery",        "Lydia",        "Jade",         "Sarah",        "Eva",          "Adeline",
        "Madeline",     "Piper",        "Rylee",        "Athena",       "Peyton",       "Everleigh",
        "Vivian",       "Clara",        "Raelynn",      "Liliana",      "Samantha",     "Maria",
        "Iris",         "Ayla",         "Eloise",       "Lyla",         "Eliza",        "Hadley",
        "Melody",       "Julia",        "Parker",       "Rose",         "Isabelle",     "Brielle",
        "Adalynn",      "Arya"
    ).distinct

    /* https://en.wikipedia.org/wiki/List_of_astronauts_by_name */

    private val boyNames = Seq(
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
        "Sven",         "Torvald",      "Oliver",       "Liam",         "Noah",         "Oliver",
        "Elijah",       "William",      "James",        "Benjamin",     "Lucas",        "Henry",
        "Alex",         "Mason",        "Michael",      "Ethan",        "Daniel",       "Jacob",
        "Logan",        "Jackson",      "Levi",         "Sebastian",    "Mateo",        "Jack",
        "Owen",         "Theo",         "Aiden",        "Samuel",       "Joseph",       "John",
        "David",        "Wyatt",        "Luke",         "Asher",        "Carter",       "Julian",
        "Grayson",      "Jayden",       "Gabriel",      "Isaac",        "Lincoln",      "Anthony",
        "Hudson",       "Dylan",        "Ezra",         "Thomas",       "Charles",      "Jaxon",
        "Josiah",       "Isaiah",       "Andrew",       "Elias",        "Joshua",       "Nathan",
        "Caleb",        "Ryan",         "Adrian",       "Miles",        "Eli",          "Nolan",
        "Aaron",        "Cameron",      "Ezekiel",      "Colton",       "Luca",         "Landon",
        "Hunter",       "Jonathan",     "Axel",         "Easton",       "Cooper",       "Jeremiah",
        "Angel",        "Roman",        "Connor",       "Jameson",      "Robert",       "Greyson",
        "Jordan",       "Ian",          "Carson",       "Jaxson",       "Leonardo",     "Nicholas",
        "Dominic",      "Austin",       "Everett",      "Brooks",       "Xavier",       "Kai",
        "Jose",         "Parker",       "Adam",         "Jace",         "Wesley",       "Kayden"
    ).distinct
    private val familyNames = Seq(
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
    private def newChild(): CPMirPlayerChild =
        val isBoy = CPRand.randFloat() > 0.5f
        CPMirPlayerChild(
            if isBoy then CPRand.rand(boyNames) else CPRand.rand(girlNames),
            isBoy
        )

    /**
      *
      * @return
      */
    def newPlayer: CPMirPlayer =
        val birth = CPRand.rand(towns)
        val home = CPRand.rand(towns)
        val kids = ArrayBuffer.empty[CPMirPlayerChild]
        for (i ← 0 until CPRand.randInt(1, 4)) kids += newChild()
        CPMirPlayer(
            CPRand.rand(boyNames),
            CPRand.rand(familyNames),
            birth._1,
            birth._2,
            home._1,
            home._2,
            CPRand.rand(CPMirPLayerRole.values.toSeq),
            CPRand.rand(girlNames),
            CPRand.rand(familyNames),
            kids.toSeq
        )
