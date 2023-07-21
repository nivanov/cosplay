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

import org.junit.jupiter.api.Test

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
  *
  */
//noinspection ScalaUnusedSymbol
object CPFIGLetFontTests:
    @Test
    def fontTestProperties(): Unit =
        CPFIGLetFont.ALL_FIG_FONTS
            .groupBy(_.getHeight)
            .map((height: Int, seq: Seq[CPFIGLetFont]) => (height, seq))
            .toSeq
            .sortBy(_._1)
            .foreach((h, seq) => println(s"$h -> ${seq.map(_.getOrigin).mkString(",")}"))

    @Test
    def fontTests(): Unit =
        /*
            #!/bin/bash
            i=0
            while read p;
            do
                up=`echo "${p%.*}" | tr '[a-z \-]' '[A-Z__]'`
                echo "val font$i = CPFIGLetFont.FIG_$up"
                i=`expr $i + 1`
            done < "figlets.txt"
        */
        val font0 = CPFIGLetFont.FIG_3D_DIAGONAL
        val font1 = CPFIGLetFont.FIG_3D_ASCII
        val font2 = CPFIGLetFont.FIG_3X5
        val font3 = CPFIGLetFont.FIG_4MAX
        val font4 = CPFIGLetFont.FIG_5_LINE_OBLIQUE
        val font5 = CPFIGLetFont.FIG_ACROBATIC
        val font6 = CPFIGLetFont.FIG_ALLIGATOR
        val font7 = CPFIGLetFont.FIG_ALLIGATOR2
        val font8 = CPFIGLetFont.FIG_ALPHA
        val font9 = CPFIGLetFont.FIG_ALPHABET
        val font10 = CPFIGLetFont.FIG_AMC_AAA01
        val font11 = CPFIGLetFont.FIG_AMC_NEKO
        val font12 = CPFIGLetFont.FIG_AMC_RAZOR
        val font13 = CPFIGLetFont.FIG_AMC_RAZOR2
        val font14 = CPFIGLetFont.FIG_AMC_SLASH
        val font15 = CPFIGLetFont.FIG_AMC_SLIDER
        val font16 = CPFIGLetFont.FIG_AMC_THIN
        val font17 = CPFIGLetFont.FIG_AMC_TUBES
        val font18 = CPFIGLetFont.FIG_AMC_UNTITLED
        val font19 = CPFIGLetFont.FIG_AMC_3_LINE
        val font20 = CPFIGLetFont.FIG_AMC_3_LIV1
        val font21 = CPFIGLetFont.FIG_ANSI_REGULAR
        val font22 = CPFIGLetFont.FIG_ANSI_SHADOW
        val font23 = CPFIGLetFont.FIG_ARROWS
        val font24 = CPFIGLetFont.FIG_ASCII_NEW_ROMAN
        val font25 = CPFIGLetFont.FIG_AVATAR
        val font27 = CPFIGLetFont.FIG_BANNER
        val font28 = CPFIGLetFont.FIG_BANNER3
        val font29 = CPFIGLetFont.FIG_BANNER3_D
        val font30 = CPFIGLetFont.FIG_BANNER4
        val font31 = CPFIGLetFont.FIG_BARBWIRE
        val font32 = CPFIGLetFont.FIG_BASIC
        val font33 = CPFIGLetFont.FIG_BEAR
        val font34 = CPFIGLetFont.FIG_BELL
        val font35 = CPFIGLetFont.FIG_BENJAMIN
        val font36 = CPFIGLetFont.FIG_BIG
        val font37 = CPFIGLetFont.FIG_BIG_CHIEF
        val font38 = CPFIGLetFont.FIG_BIG_MONEY_NE
        val font39 = CPFIGLetFont.FIG_BIG_MONEY_NW
        val font40 = CPFIGLetFont.FIG_BIG_MONEY_SE
        val font41 = CPFIGLetFont.FIG_BIG_MONEY_SW
        val font42 = CPFIGLetFont.FIG_BIGFIG
        val font44 = CPFIGLetFont.FIG_BLOCK
        val font45 = CPFIGLetFont.FIG_BLOCKS
        val font46 = CPFIGLetFont.FIG_BLOODY
        val font47 = CPFIGLetFont.FIG_BOLGER
        val font48 = CPFIGLetFont.FIG_BRACED
        val font49 = CPFIGLetFont.FIG_BRIGHT
        val font50 = CPFIGLetFont.FIG_BROADWAY
        val font51 = CPFIGLetFont.FIG_BROADWAY_KB
        val font53 = CPFIGLetFont.FIG_BULBHEAD
        val font54 = CPFIGLetFont.FIG_CALIGRAPHY
        val font55 = CPFIGLetFont.FIG_CALIGRAPHY2
        val font56 = CPFIGLetFont.FIG_CALVIN_S
        val font57 = CPFIGLetFont.FIG_CARDS
        val font58 = CPFIGLetFont.FIG_CATWALK
        val font59 = CPFIGLetFont.FIG_CHISELED
        val font60 = CPFIGLetFont.FIG_CHUNKY
        val font61 = CPFIGLetFont.FIG_COINSTAK
        val font62 = CPFIGLetFont.FIG_COLA
        val font63 = CPFIGLetFont.FIG_COLOSSAL
        val font64 = CPFIGLetFont.FIG_COMPUTER
        val font65 = CPFIGLetFont.FIG_CONTESSA
        val font66 = CPFIGLetFont.FIG_CONTRAST
        val font67 = CPFIGLetFont.FIG_COSMIKE
        val font68 = CPFIGLetFont.FIG_CRAWFORD
        val font69 = CPFIGLetFont.FIG_CRAWFORD2
        val font70 = CPFIGLetFont.FIG_CRAZY
        val font71 = CPFIGLetFont.FIG_CRICKET
        val font72 = CPFIGLetFont.FIG_CURSIVE
        val font73 = CPFIGLetFont.FIG_CYBERLARGE
        val font74 = CPFIGLetFont.FIG_CYBERMEDIUM
        val font75 = CPFIGLetFont.FIG_CYBERSMALL
        val font76 = CPFIGLetFont.FIG_CYGNET
        val font77 = CPFIGLetFont.FIG_DANC4
        val font78 = CPFIGLetFont.FIG_DANCING_FONT
        val font80 = CPFIGLetFont.FIG_DEF_LEPPARD
        val font81 = CPFIGLetFont.FIG_DELTA_CORPS_PRIEST_1
        val font82 = CPFIGLetFont.FIG_DIAMOND
        val font83 = CPFIGLetFont.FIG_DIET_COLA
        val font85 = CPFIGLetFont.FIG_DOH
        val font86 = CPFIGLetFont.FIG_DOOM
        val font87 = CPFIGLetFont.FIG_DOS_REBEL
        val font88 = CPFIGLetFont.FIG_DOT_MATRIX
        val font89 = CPFIGLetFont.FIG_DOUBLE
        val font90 = CPFIGLetFont.FIG_DOUBLE_SHORTS
        val font91 = CPFIGLetFont.FIG_DR_PEPPER
        val font92 = CPFIGLetFont.FIG_DWHISTLED
        val font93 = CPFIGLetFont.FIG_EFTI_CHESS
        val font94 = CPFIGLetFont.FIG_EFTI_FONT
        val font95 = CPFIGLetFont.FIG_EFTI_ITALIC
        val font96 = CPFIGLetFont.FIG_EFTI_PITI
        val font97 = CPFIGLetFont.FIG_EFTI_ROBOT
        val font98 = CPFIGLetFont.FIG_EFTI_WALL
        val font99 = CPFIGLetFont.FIG_EFTI_WATER
        val font100 = CPFIGLetFont.FIG_ELECTRONIC
        val font101 = CPFIGLetFont.FIG_ELITE
        val font102 = CPFIGLetFont.FIG_EPIC
        val font103 = CPFIGLetFont.FIG_FENDER
        val font104 = CPFIGLetFont.FIG_FILTER
        val font105 = CPFIGLetFont.FIG_FIRE_FONT_K
        val font106 = CPFIGLetFont.FIG_FIRE_FONT_S
        val font107 = CPFIGLetFont.FIG_FLIPPED
        val font108 = CPFIGLetFont.FIG_FLOWER_POWER
        val font109 = CPFIGLetFont.FIG_FOUR_TOPS
        val font110 = CPFIGLetFont.FIG_FRAKTUR
        val font111 = CPFIGLetFont.FIG_FUN_FACE
        val font112 = CPFIGLetFont.FIG_FUN_FACES
        val font113 = CPFIGLetFont.FIG_FUZZY
        val font114 = CPFIGLetFont.FIG_GEORGI16
        val font115 = CPFIGLetFont.FIG_GEORGIA11
        val font116 = CPFIGLetFont.FIG_GHOST
        val font117 = CPFIGLetFont.FIG_GHOULISH
        val font118 = CPFIGLetFont.FIG_GLENYN
        val font119 = CPFIGLetFont.FIG_GOOFY
        val font120 = CPFIGLetFont.FIG_GOTHIC
        val font121 = CPFIGLetFont.FIG_GRACEFUL
        val font122 = CPFIGLetFont.FIG_GRADIENT
        val font123 = CPFIGLetFont.FIG_GRAFFITI
        val font124 = CPFIGLetFont.FIG_GREEK
        val font125 = CPFIGLetFont.FIG_HEART_LEFT
        val font126 = CPFIGLetFont.FIG_HEART_RIGHT
        val font127 = CPFIGLetFont.FIG_HENRY_3D
        val font128 = CPFIGLetFont.FIG_HEX
        val font129 = CPFIGLetFont.FIG_HIEROGLYPHS
        val font130 = CPFIGLetFont.FIG_HOLLYWOOD
        val font131 = CPFIGLetFont.FIG_HORIZONTAL_LEFT
        val font132 = CPFIGLetFont.FIG_HORIZONTAL_RIGHT
        val font133 = CPFIGLetFont.FIG_ICL_1900
        val font134 = CPFIGLetFont.FIG_IMPOSSIBLE
        val font135 = CPFIGLetFont.FIG_INVITA
        val font136 = CPFIGLetFont.FIG_ISOMETRIC1
        val font137 = CPFIGLetFont.FIG_ISOMETRIC2
        val font138 = CPFIGLetFont.FIG_ISOMETRIC3
        val font139 = CPFIGLetFont.FIG_ISOMETRIC4
        val font140 = CPFIGLetFont.FIG_ITALIC
        val font142 = CPFIGLetFont.FIG_JACKY
        val font143 = CPFIGLetFont.FIG_JAZMINE
        val font145 = CPFIGLetFont.FIG_JS_BLOCK_LETTERS
        val font146 = CPFIGLetFont.FIG_JS_BRACKET_LETTERS
        val font147 = CPFIGLetFont.FIG_JS_CAPITAL_CURVES
        val font148 = CPFIGLetFont.FIG_JS_CURSIVE
        val font149 = CPFIGLetFont.FIG_JS_STICK_LETTERS
        val font150 = CPFIGLetFont.FIG_KATAKANA
        val font151 = CPFIGLetFont.FIG_KBAN
        val font152 = CPFIGLetFont.FIG_KEYBOARD
        val font153 = CPFIGLetFont.FIG_KNOB
        val font154 = CPFIGLetFont.FIG_KONTO
        val font155 = CPFIGLetFont.FIG_KONTO_SLANT
        val font156 = CPFIGLetFont.FIG_LARRY_3D
        val font157 = CPFIGLetFont.FIG_LARRY_3D_2
        val font158 = CPFIGLetFont.FIG_LCD
        val font159 = CPFIGLetFont.FIG_LEAN
        val font160 = CPFIGLetFont.FIG_LETTERS
        val font161 = CPFIGLetFont.FIG_LIL_DEVIL
        val font162 = CPFIGLetFont.FIG_LINE_BLOCKS
        val font163 = CPFIGLetFont.FIG_LINUX
        val font164 = CPFIGLetFont.FIG_LOCKERGNOME
        val font165 = CPFIGLetFont.FIG_MADRID
        val font166 = CPFIGLetFont.FIG_MARQUEE
        val font167 = CPFIGLetFont.FIG_MAXFOUR
        val font168 = CPFIGLetFont.FIG_MERLIN1
        val font169 = CPFIGLetFont.FIG_MERLIN2
        val font170 = CPFIGLetFont.FIG_MIKE
        val font171 = CPFIGLetFont.FIG_MINI
        val font173 = CPFIGLetFont.FIG_MNEMONIC
        val font174 = CPFIGLetFont.FIG_MODULAR
        val font175 = CPFIGLetFont.FIG_MORSE
        val font176 = CPFIGLetFont.FIG_MORSE2
        val font177 = CPFIGLetFont.FIG_MOSCOW
        val font179 = CPFIGLetFont.FIG_MUZZLE
        val font180 = CPFIGLetFont.FIG_NANCYJ
        val font181 = CPFIGLetFont.FIG_NANCYJ_FANCY
        val font182 = CPFIGLetFont.FIG_NANCYJ_IMPROVED
        val font183 = CPFIGLetFont.FIG_NANCYJ_UNDERLINED
        val font184 = CPFIGLetFont.FIG_NIPPLES
        val font185 = CPFIGLetFont.FIG_NSCRIPT
        val font186 = CPFIGLetFont.FIG_NT_GREEK
        val font187 = CPFIGLetFont.FIG_NV_SCRIPT
        val font188 = CPFIGLetFont.FIG_O8
        val font189 = CPFIGLetFont.FIG_OCTAL
        val font190 = CPFIGLetFont.FIG_OGRE
        val font191 = CPFIGLetFont.FIG_OLD_BANNER
        val font192 = CPFIGLetFont.FIG_OS2
        val font193 = CPFIGLetFont.FIG_PAGGA
        val font194 = CPFIGLetFont.FIG_PATORJK_CHEESE
        val font195 = CPFIGLetFont.FIG_PATORJK_HEX
        val font196 = CPFIGLetFont.FIG_PAWP
        val font197 = CPFIGLetFont.FIG_PEAKS
        val font198 = CPFIGLetFont.FIG_PEAKS_SLANT
        val font199 = CPFIGLetFont.FIG_PEBBLES
        val font200 = CPFIGLetFont.FIG_PEPPER
        val font201 = CPFIGLetFont.FIG_POISON
        val font202 = CPFIGLetFont.FIG_PUFFY
        val font203 = CPFIGLetFont.FIG_PUZZLE
        val font205 = CPFIGLetFont.FIG_RAMMSTEIN
        val font206 = CPFIGLetFont.FIG_RECTANGLES
        val font207 = CPFIGLetFont.FIG_RED_PHOENIX
        val font208 = CPFIGLetFont.FIG_RELIEF
        val font209 = CPFIGLetFont.FIG_RELIEF2
        val font210 = CPFIGLetFont.FIG_REVERSE
        val font211 = CPFIGLetFont.FIG_ROMAN
        val font213 = CPFIGLetFont.FIG_ROTATED
        val font214 = CPFIGLetFont.FIG_ROUNDED
        val font215 = CPFIGLetFont.FIG_ROWAN_CAP
        val font216 = CPFIGLetFont.FIG_ROZZO
        val font217 = CPFIGLetFont.FIG_RUNIC
        val font218 = CPFIGLetFont.FIG_RUNYC
        val font219 = CPFIGLetFont.FIG_S_BLOOD
        val font220 = CPFIGLetFont.FIG_SANTA_CLARA
        val font221 = CPFIGLetFont.FIG_SCRIPT
        val font222 = CPFIGLetFont.FIG_SERIFCAP
        val font223 = CPFIGLetFont.FIG_SHADOW
        val font224 = CPFIGLetFont.FIG_SHIMROD
        val font225 = CPFIGLetFont.FIG_SHORT
        val font226 = CPFIGLetFont.FIG_SL_SCRIPT
        val font227 = CPFIGLetFont.FIG_SLANT
        val font228 = CPFIGLetFont.FIG_SLANT_RELIEF
        val font229 = CPFIGLetFont.FIG_SLIDE
        val font230 = CPFIGLetFont.FIG_SMALL
        val font231 = CPFIGLetFont.FIG_SMALL_CAPS
        val font232 = CPFIGLetFont.FIG_SMALL_ISOMETRIC1
        val font233 = CPFIGLetFont.FIG_SMALL_KEYBOARD
        val font234 = CPFIGLetFont.FIG_SMALL_POISON
        val font235 = CPFIGLetFont.FIG_SMALL_SCRIPT
        val font236 = CPFIGLetFont.FIG_SMALL_SHADOW
        val font237 = CPFIGLetFont.FIG_SMALL_SLANT
        val font238 = CPFIGLetFont.FIG_SMALL_TENGWAR
        val font239 = CPFIGLetFont.FIG_SOFT
        val font240 = CPFIGLetFont.FIG_SPEED
        val font241 = CPFIGLetFont.FIG_SPLIFF
        val font242 = CPFIGLetFont.FIG_STACEY
        val font243 = CPFIGLetFont.FIG_STAMPATE
        val font244 = CPFIGLetFont.FIG_STAMPATELLO
        val font245 = CPFIGLetFont.FIG_STANDARD
        val font246 = CPFIGLetFont.FIG_STAR_STRIPS
        val font247 = CPFIGLetFont.FIG_STAR_WARS
        val font248 = CPFIGLetFont.FIG_STELLAR
        val font249 = CPFIGLetFont.FIG_STFOREK
        val font250 = CPFIGLetFont.FIG_STICK_LETTERS
        val font251 = CPFIGLetFont.FIG_STOP
        val font252 = CPFIGLetFont.FIG_STRAIGHT
        val font253 = CPFIGLetFont.FIG_STRONGER_THAN_ALL
        val font254 = CPFIGLetFont.FIG_SUB_ZERO
        val font255 = CPFIGLetFont.FIG_SWAMP_LAND
        val font256 = CPFIGLetFont.FIG_SWAN
        val font257 = CPFIGLetFont.FIG_SWEET
        val font258 = CPFIGLetFont.FIG_TANJA
        val font259 = CPFIGLetFont.FIG_TENGWAR
        val font261 = CPFIGLetFont.FIG_TEST1
        val font262 = CPFIGLetFont.FIG_THE_EDGE
        val font263 = CPFIGLetFont.FIG_THICK
        val font264 = CPFIGLetFont.FIG_THIN
        val font265 = CPFIGLetFont.FIG_THIS
        val font266 = CPFIGLetFont.FIG_THORNED
        val font267 = CPFIGLetFont.FIG_THREE_POINT
        val font268 = CPFIGLetFont.FIG_TICKS
        val font269 = CPFIGLetFont.FIG_TICKS_SLANT
        val font270 = CPFIGLetFont.FIG_TILES
        val font271 = CPFIGLetFont.FIG_TINKER_TOY
        val font272 = CPFIGLetFont.FIG_TOMBSTONE
        val font273 = CPFIGLetFont.FIG_TRAIN
        val font274 = CPFIGLetFont.FIG_TREK
        val font275 = CPFIGLetFont.FIG_TSALAGI
        val font276 = CPFIGLetFont.FIG_TUBULAR
        val font277 = CPFIGLetFont.FIG_TWISTED
        val font278 = CPFIGLetFont.FIG_TWO_POINT
        val font279 = CPFIGLetFont.FIG_UNIVERS
        val font280 = CPFIGLetFont.FIG_USA_FLAG
        val font281 = CPFIGLetFont.FIG_VARSITY
        val font282 = CPFIGLetFont.FIG_WAVY
        val font283 = CPFIGLetFont.FIG_WEIRD
        val font284 = CPFIGLetFont.FIG_WET_LETTER
        val font285 = CPFIGLetFont.FIG_WHIMSY
        val font286 = CPFIGLetFont.FIG_WOW
