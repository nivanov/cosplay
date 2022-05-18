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
import org.cosplay.games.mir.CPMirPlayer.*

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
  * @param character
  * @param game
  */
case class CPMirFavGame(character: String, game: String) extends Serializable:
    def debugString: String = s"$character from $game"

/**
  *
  * @param title
  * @param author
  */
case class CPMirFavBook(title: String, author: String) extends Serializable:
    def debugString: String = s"$title by $author"

/**
  *
  * @param name
  * @param teams
  */
case class CPMirTeamSport(name: String, teams: Seq[String]) extends Serializable

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
    private val sport = CPRand.rand(sports)
    val dobDay: Int = CPRand.between(1, 28 + 1)
    val dobMonth: Int = CPRand.between(1, 12 + 1)
    val dobYear: Int = CPRand.between(EVENT_YEAR - 35, EVENT_YEAR - 55 + 1)
    val nameCamelCase: String = s"${CaseUtils.toCamelCase(s"$firstName", true, ' ')} ${CaseUtils.toCamelCase(s"$lastName", true, ' ')}"
    val nameUpperCase: String = s"$firstName $lastName".toUpperCase
    val nameLowerCase: String = s"$firstName $lastName".toLowerCase
    val wifeCamelCase: String = s"${CaseUtils.toCamelCase(s"$wifeFirstName", true, ' ')} ${CaseUtils.toCamelCase(s"$wifeLastName", true, ' ')}"
    val wifeUpperCase: String = s"$wifeFirstName $wifeLastName".toUpperCase
    val wifeLowerCase: String = s"$wifeFirstName $wifeLastName".toLowerCase
    val username: String = s"${firstName.head}$lastName".toLowerCase
    val favBands: Seq[String] = for (i <- 0 to CPRand.randInt(1, 3)) yield CPRand.rand(rockBands)
    val favGames: Seq[CPMirFavGame] = for (i <- 0 to CPRand.randInt(1, 3)) yield CPRand.rand(nesGames)
    val favColor: String = CPRand.rand(colors)
    val favSport: String = sport.name
    val favSportTeam: String = CPRand.rand(sport.teams)
    val age: Int = EVENT_YEAR - dobYear
    val passwords: Seq[String] = {
        val nums = List(EVENT_YEAR, dobYear)
            ++ children.map(_.age)
            ++ children.map(_.dobYear)
        val words = List(favColor, favSport, favSportTeam, firstName, wifeFirstName, homeTown, birthTown, username)
            ++ favBands
            ++ favGames.map(_.character)
            ++ children.map(_.name)
        for (w ← words.distinct; n ← nums.distinct) yield s"${w.toLowerCase}$n"
    }

    def debugString: String =
        s"""
           |$nameCamelCase, @$username, $role, living in $homeTown, $homeCountry
           |age $age, dob: $dobMonth/$dobDay/$dobYear in $birthTown, $birthCountry
           |married to $wifeCamelCase
           |${children.size} children: ${children.map(_.debugString).mkString(", ")}
           |Fav bands: ${favBands.mkString(", ")}
           |Fav game character: ${favGames.map(_.debugString).mkString(", ")}
           |Fav color: $favColor
           |Fav sport: $favSport
           |Fav sport team: $favSportTeam
           |Passwords: ${passwords.mkString(", ")}
           |""".stripMargin

/**
  *
  */
object CPMirPlayer:
    private val sports: Seq[CPMirTeamSport] = Seq(
        CPMirTeamSport("Basketball", Seq(
            "76ers",        "Bucks",        "Bulls",        "Cavaliers",        "Celtics",
            "Clippers",     "Grizzlies",    "Hawks",        "Heat",             "Hornets",
            "Jazz",         "Kings",        "Knicks",       "Lakers",           "Magic",
            "Mavericks",    "Nets",         "Nuggets",      "Pacers",           "Pelicans",
            "Pistons",      "Raptors",      "Rockets",      "Spurs",            "Suns",
            "Thunder",      "Timberwolves", "Blazers",      "Warriors",         "Wizards"
        ).distinct),
        CPMirTeamSport("Football", Seq(
            "49ers",        "Bears",        "Bengals",      "Bills",            "Broncos",
            "Browns",       "Buccaneers",   "Cardinals",    "Chargers",         "Chiefs",
            "Colts",        "Commanders",   "Cowboys",      "Dolphins",         "Eagles",
            "Falcons",      "Giants",       "Jaguars",      "Jets",             "Lions",
            "Packers",      "Panthers",     "Patriots",     "Raiders",          "Rams",
            "Ravens",       "Saints",       "Seahawks",     "Steelers",         "Texans",
            "Titans",       "Vikings"
        ).distinct),
        CPMirTeamSport("Baseball", Seq(
            "Braves",       "Marlins",      "Mets",         "Phillies",         "Nationals",
            "Cubs",         "Reds",         "Brewers",      "Pirates",          "Cardinals",
            "Diamondbacks", "Rockies",      "Dodgers",      "Padres",           "Giants",
            "Orioles",      "RedSox",       "Yankees",      "Rays",             "Jays ",
            "WhiteSox",     "Indians",      "Tigers",       "Royals",           "Twins",
            "Astros",       "Angels",       "Athletics",    "Mariners",         "Rangers"
        ).distinct),
        CPMirTeamSport("Hockey", Seq(
            "Ducks",        "Coyotes",      "Bruins",       "Sabres",           "Flames",
            "Hurricanes",   "Blackhawks",   "Avalanche",    "BlueJackets",      "Stars",
            "RedWings ",    "Oilers",       "Panthers",     "Kings ",           "Wild",
            "Canadiens",    "Predators",    "Devils",       "Islanders",        "Rangers",
            "Senators",     "Flyers",       "Penguins",     "Sharks",           "Kraken",
            "Blues ",       "Lightning",    "MapleLeafs",   "Canucks",          "GoldenKnights",
            "Capitals",     "Jets"
        ).distinct),
        CPMirTeamSport("Soccer", Seq(
            "Liverpool",    "Manchester",   "RealMadrid",   "Bayern",           "Inter",
            "Ajax",         "Chelsea",      "Barcelona",    "Milan",            "PSG",
            "Napoli",       "Porto",        "Atlético",     "Tottenham",        "Arsenal",
            "Juventus",     "Benfica",      "Bayer",        "Sporting",         "Leipzig",
            "Villarreal",   "Borussia",     "Sevilla",      "Rangers",          "Lazio",
            "Eindhoven",    "Monaco",       "Manchester",   "Roma",
            "Atalanta",     "Slavia",       "Flora",        "Shakhtar",         "Feyenoord",
            "WestHam",      "Athletic",     "Olympiakos",   "Celtic",           "Lyon",
            "Rennes",       "Eintracht",    "Freiburg",     "CrystalPalace",    "Zenit",
            "Sheriff"
        ).distinct)
    )
    private val colors = Seq(
        "yellow", "amber", "orange", "vermilion", "red", "magenta", "purple", "violet", "blue", "teal", "green", "chartreuse"
    )
    private val nesGames: Seq[CPMirFavGame] = Seq[(String, String)](
        ("Mario", "Super Mario"),
        ("Mario", "Super Mario 3"),
        ("Luigi", "Super Mario 3"),
        ("Yoshi", "Super Mario 3"),
        ("Pitch", "Super Mario 3"),
        ("Daisy", "Super Mario 3"),
        ("Bowser", "Super Mario 3"),
        ("Koopa", "Super Mario 3"),
        ("Kirby", "Kirby"),
        ("Bandicoot", "Crash Bandicoot"),
        ("Scorpion", "Mortal Kombat"),
        ("Lara", "Tomb Raider"),
        ("Pikachu", "Pokemon"),
        ("Ash", "Pokemon"),
        ("Jessy", "Pokemon"),
        ("James", "Pokemon"),
        ("Bulbasaur", "Pokemon"),
        ("Charmander", "Pokemon"),
        ("Squirtle", "Pokemon"),
        ("Caterpie", "Pokemon"),
        ("Pidgey", "Pokemon"),
        ("Rattata", "Pokemon"),
        ("Spearow", "Pokemon"),
        ("Nidorina", "Pokemon"),
        ("Jigglypuff", "Pokemon"),
        ("Zubat", "Pokemon"),
        ("Sonic", "Sonic the Hedgehog"),
        ("Spyro", "Spyro The Dragon"),
        ("Link", "The Legend of Zelda"),
        ("Zelda", "The Legend of Zelda"),
        ("Ganon", "The Legend of Zelda"),
        ("Majora", "The Legend of Zelda"),
        ("Deku", "The Legend of Zelda")
    ).map(t ⇒ CPMirFavGame(t._1, t._2))

    private val rockBands = Seq(
        "Nirvana",      "Soundgarden",      "Radiohead",        "Pantera ",         "Metallica",
        "Oasis",        "U2",               "Queen",            "Weezer",           "Slayer",
        "Korn",         "Pavement",         "Anthrax",          "Blur",             "Pixies",
        "Bush",         "Garbage",          "Megadeth",         "Pulp",             "Aerosmith",
        "Live",         "Tool",             "Silverchair",      "Emperor",          "Creed",
        "Incubus",      "Testament",        "Helmet",           "Sublime",          "Primus",
        "Extreme",      "Scorpions",        "Sepultura",        "Candlebox",        "Meshuggah",
        "Mudhoney",     "Death",            "Firehouse",        "Ministry",         "Semisonic",
        "Motorhead",    "Opeth",            "Warrant",          "Everclear",        "Portishead",
        "Deftones",     "Cake",             "Filter",           "KMFDM",            "Tonic",
        "Hole",         "Carcass",          "Godsmack",         "Queensryche",      "Dokken",
        "Unleashed",    "Sebadoh",          "Fastball",         "Poison",           "Superchunk",
        "Dismember",    "Ammonia",          "Steelheart",       "Danzig",           "Rush",
        "Slaughter",    "Rancid",           "Suffocation",      "BoDeans",          "Wilco",
        "Failure",      "Thunder",          "Obituary",         "Belly",            "Winger",
        "Amorphis",     "Overkill",         "Dio",              "Cinderella",       "Entombed",
        "Kiss",         "Whitesnake",       "Exodus",           "Manowar",          "Cracker",
        "Len",          "Stryper",          "Grave",            "Kix",              "Pariah",
        "Kreator",      "Destruction",      "Heloween",         "Sponge",           "Eels",
        "Superdrag",    "Therion",          "Behemoth",         "L7",               "Incantation",
        "Gun",          "Trixter",          "Ratt",             "Rammstein",        "BulletBoys",
        "Divinyls",     "Suede",            "Voivod",           "Heart",            "Vixen",
        "Vader",        "Forbidden",        "Lit",              "Tankard",          "Muse",
        "Deicide",      "Annihilator",      "Travis",
    ).distinct
    
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
