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

import org.cosplay.CPKeyboardKey.KEY_BACKSPACE

import scala.collection.mutable

private val ESC = '\u001b'
private val CSI = Seq(ESC, '[')
private val WCSI = Seq(ESC, 'O') // Windows application mode.

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
  * Companion object contains utility functions.
  *
  * @see [[CPKeyboardEvent]]
  */
object CPKeyboardKey:
    /**
      * Creates keyboard key with given ID.
      *
      * @param id ID of the keyboard key.
      */
    def ofId(id: String): CPKeyboardKey = CPKeyboardKey.values.find(_.id == id).get

/**
  * Enumeration of all supported keyboard keys.
  *
  * ### Remapped Keys
  * The following keystrokes are automatically re-mapped:
  *  - `CTRL+H` is mapped to [[CPKeyboardKey.KEY_BACKSPACE]].
  *  - `CTRL+I` is mapped to [[CPKeyboardKey.KEY_TAB]].
  *  - `CTRL+M` is mapped to [[CPKeyboardKey.KEY_ENTER]].
  *
  * NOTE: `CTRL+H`, `CTRL+I` and `CTRL+M` will not be detected as-is, and you should use their conversions
  * instead. Note that even-though this enumeration provides constants for `CTRL+H`, `CTRL+I` and `CTRL+M` they
  * will never be returned to the scene objects since they would always be automatically remapped.
  * This is the limitation of the ANSI terminals, i.e. `CTRL+M` generates the same ANSI code as `Enter`
  * key press.
  *
  * ### Reserved Keys
  * There are three reserved key strokes that are used by the game engine itself and therefore NOT available
  * to the game. These keystrokes are intercepted before frame update and not propagated to the scene object
  * context:
  *  - `CTRL+Q` - toggles in-game FPS overlay
  *  - `CTRL+L` - opens GUI-based loc viewer & debugger
  *  - `F12` - saves current frame screenshot as [[https://www.gridsagegames.com/rexpaint/ REXPaint]]
  *    *.xp image to the current working folder.
  *
  * @see [[CPKeyboardEvent.key]]
  */
enum CPKeyboardKey(val id: String, val isPrintable: Boolean, val ch: Char, val rawCodes: Seq[Char]*) extends mutable.HashMap[String, AnyRef]:
    case KEY_UNKNOWN extends CPKeyboardKey("Unknown", false, '\u0000', Seq.empty)

    case KEY_ESC extends CPKeyboardKey("Esc", false, '\u0000', Seq(ESC))
    case KEY_TAB extends CPKeyboardKey("Tab", false, '\u0000', Seq('\u0009'))
    case KEY_SPACE extends CPKeyboardKey("Space", true, ' ', Seq('\u0020'))
    case KEY_ENTER extends CPKeyboardKey("Enter", false, '\u0000', Seq('\u000d'))
    case KEY_BACKSPACE extends CPKeyboardKey("Backspace", false, '\u0000', Seq('\u0008'), Seq('\u007f'))

    case KEY_F1 extends CPKeyboardKey("F1", false, '\u0000', WCSI :+ 'P')
    case KEY_F2 extends CPKeyboardKey("F2", false, '\u0000', WCSI :+ 'Q')
    case KEY_F3 extends CPKeyboardKey("F3", false, '\u0000', WCSI :+ 'R')
    case KEY_F4 extends CPKeyboardKey("F4", false, '\u0000', WCSI :+ 'S')
    case KEY_F5 extends CPKeyboardKey("F5", false, '\u0000', CSI ++ Seq('1', '5', '~'))
    case KEY_F6 extends CPKeyboardKey("F6", false, '\u0000', CSI ++ Seq('1', '7', '~'))
    case KEY_F7 extends CPKeyboardKey("F7", false, '\u0000', CSI ++ Seq('1', '8', '~'))
    case KEY_F8 extends CPKeyboardKey("F8", false, '\u0000', CSI ++ Seq('1', '9', '~'))
    case KEY_F9 extends CPKeyboardKey("F9", false, '\u0000', CSI ++ Seq('2', '0', '~'))
    case KEY_F10 extends CPKeyboardKey("F10", false, '\u0000', CSI ++ Seq('2', '1', '~'))
    case KEY_F11 extends CPKeyboardKey("F11", false, '\u0000', CSI ++ Seq('2', '3', '~'))
    case KEY_F12 extends CPKeyboardKey("F12", false, '\u0000', CSI ++ Seq('2', '4', '~'))

    case KEY_INS extends CPKeyboardKey("Ins", false, '\u0000', CSI ++ Seq('2', '~'))
    case KEY_DEL extends CPKeyboardKey("Del", false, '\u0000', CSI ++ Seq('3', '~'))
    case KEY_PGUP extends CPKeyboardKey("PageUp", false, '\u0000', CSI ++ Seq('5', '~'))
    case KEY_PGDN extends CPKeyboardKey("PageDown", false, '\u0000', CSI ++ Seq('6', '~'))
    case KEY_HOME extends CPKeyboardKey("Home", false, '\u0000', CSI :+ 'H')
    case KEY_END extends CPKeyboardKey("End", false, '\u0000', CSI ++ Seq('F'), CSI ++ Seq('4', '~'))

    case KEY_UP extends CPKeyboardKey("Up", false, '\u0000', CSI :+ 'A', WCSI :+ 'A')
    case KEY_DOWN extends CPKeyboardKey("Down", false, '\u0000', CSI :+ 'B', WCSI :+ 'B')
    case KEY_LEFT extends CPKeyboardKey("Left", false, '\u0000', CSI :+ 'D', WCSI :+ 'D')
    case KEY_RIGHT extends CPKeyboardKey("Right", false, '\u0000', CSI :+ 'C', WCSI :+ 'C')

    case KEY_CTRL_UP extends CPKeyboardKey("Ctrl-Up", false, '\u0000', CSI ++ Seq('1', ';', '5', 'A'))
    case KEY_CTRL_DOWN extends CPKeyboardKey("Ctrl-Down", false, '\u0000', CSI ++ Seq('1', ';', '5', 'B'))
    case KEY_CTRL_LEFT extends CPKeyboardKey("Ctrl-Left", false, '\u0000', CSI ++ Seq('1', ';', '5', 'D'))
    case KEY_CTRL_RIGHT extends CPKeyboardKey("Ctrl-Right", false, '\u0000', CSI ++ Seq('1', ';', '5', 'C'))

    // Upper case letters.
    // zsh: for x in {a..z}; do; echo "case KEY_UP_${x:u} extends CPKeyboardKey(\"${x:u}\", Seq('${x:u}'))"; done
    case KEY_UP_A extends CPKeyboardKey("A", true, 'A', Seq('A'))
    case KEY_UP_B extends CPKeyboardKey("B", true, 'B', Seq('B'))
    case KEY_UP_C extends CPKeyboardKey("C", true, 'C', Seq('C'))
    case KEY_UP_D extends CPKeyboardKey("D", true, 'D', Seq('D'))
    case KEY_UP_E extends CPKeyboardKey("E", true, 'E', Seq('E'))
    case KEY_UP_F extends CPKeyboardKey("F", true, 'F', Seq('F'))
    case KEY_UP_G extends CPKeyboardKey("G", true, 'G', Seq('G'))
    case KEY_UP_H extends CPKeyboardKey("H", true, 'H', Seq('H'))
    case KEY_UP_I extends CPKeyboardKey("I", true, 'I', Seq('I'))
    case KEY_UP_J extends CPKeyboardKey("J", true, 'J', Seq('J'))
    case KEY_UP_K extends CPKeyboardKey("K", true, 'K', Seq('K'))
    case KEY_UP_L extends CPKeyboardKey("L", true, 'L', Seq('L'))
    case KEY_UP_M extends CPKeyboardKey("M", true, 'M', Seq('M'))
    case KEY_UP_N extends CPKeyboardKey("N", true, 'N', Seq('N'))
    case KEY_UP_O extends CPKeyboardKey("O", true, 'O', Seq('O'))
    case KEY_UP_P extends CPKeyboardKey("P", true, 'P', Seq('P'))
    case KEY_UP_Q extends CPKeyboardKey("Q", true, 'Q', Seq('Q'))
    case KEY_UP_R extends CPKeyboardKey("R", true, 'R', Seq('R'))
    case KEY_UP_S extends CPKeyboardKey("S", true, 'S', Seq('S'))
    case KEY_UP_T extends CPKeyboardKey("T", true, 'T', Seq('T'))
    case KEY_UP_U extends CPKeyboardKey("U", true, 'U', Seq('U'))
    case KEY_UP_V extends CPKeyboardKey("V", true, 'V', Seq('V'))
    case KEY_UP_W extends CPKeyboardKey("W", true, 'W', Seq('W'))
    case KEY_UP_X extends CPKeyboardKey("X", true, 'X', Seq('X'))
    case KEY_UP_Y extends CPKeyboardKey("Y", true, 'Y', Seq('Y'))
    case KEY_UP_Z extends CPKeyboardKey("Z", true, 'Z', Seq('Z'))

    case KEY_CTRL_A extends CPKeyboardKey("Ctrl-a", false, '\u0000', Seq('a' - 96))
    case KEY_CTRL_B extends CPKeyboardKey("Ctrl-b", false, '\u0000', Seq('b' - 96))
    case KEY_CTRL_C extends CPKeyboardKey("Ctrl-c", false, '\u0000', Seq('c' - 96))
    case KEY_CTRL_D extends CPKeyboardKey("Ctrl-d", false, '\u0000', Seq('d' - 96))
    case KEY_CTRL_E extends CPKeyboardKey("Ctrl-e", false, '\u0000', Seq('e' - 96))
    case KEY_CTRL_F extends CPKeyboardKey("Ctrl-f", false, '\u0000', Seq('f' - 96))
    case KEY_CTRL_G extends CPKeyboardKey("Ctrl-g", false, '\u0000', Seq('g' - 96))
    case KEY_CTRL_H extends CPKeyboardKey("Ctrl-h", false, '\u0000', Seq('h' - 96)) // Converted to 'KEY_BACKSPACE'.
    case KEY_CTRL_I extends CPKeyboardKey("Ctrl-i", false, '\u0000', Seq('i' - 96)) // Converted to 'KEY_TAB'.
    case KEY_CTRL_J extends CPKeyboardKey("Ctrl-j", false, '\u0000', Seq('j' - 96))
    case KEY_CTRL_K extends CPKeyboardKey("Ctrl-k", false, '\u0000', Seq('k' - 96))
    case KEY_CTRL_L extends CPKeyboardKey("Ctrl-l", false, '\u0000', Seq('l' - 96))
    case KEY_CTRL_M extends CPKeyboardKey("Ctrl-m", false, '\u0000', Seq('m' - 96)) // Converted to 'KEY_ENTER'.
    case KEY_CTRL_N extends CPKeyboardKey("Ctrl-n", false, '\u0000', Seq('n' - 96))
    case KEY_CTRL_O extends CPKeyboardKey("Ctrl-o", false, '\u0000', Seq('o' - 96))
    case KEY_CTRL_P extends CPKeyboardKey("Ctrl-p", false, '\u0000', Seq('p' - 96))
    case KEY_CTRL_Q extends CPKeyboardKey("Ctrl-q", false, '\u0000', Seq('q' - 96))
    case KEY_CTRL_R extends CPKeyboardKey("Ctrl-r", false, '\u0000', Seq('r' - 96))
    case KEY_CTRL_S extends CPKeyboardKey("Ctrl-s", false, '\u0000', Seq('s' - 96))
    case KEY_CTRL_T extends CPKeyboardKey("Ctrl-t", false, '\u0000', Seq('t' - 96))
    case KEY_CTRL_U extends CPKeyboardKey("Ctrl-u", false, '\u0000', Seq('u' - 96))
    case KEY_CTRL_V extends CPKeyboardKey("Ctrl-v", false, '\u0000', Seq('v' - 96))
    case KEY_CTRL_W extends CPKeyboardKey("Ctrl-w", false, '\u0000', Seq('w' - 96))
    case KEY_CTRL_X extends CPKeyboardKey("Ctrl-x", false, '\u0000', Seq('x' - 96))
    case KEY_CTRL_Y extends CPKeyboardKey("Ctrl-y", false, '\u0000', Seq('y' - 96))
    case KEY_CTRL_Z extends CPKeyboardKey("Ctrl-z", false, '\u0000', Seq('z' - 96))

    // Lower case letters.
    // zsh: for x in {a..z}; do; echo "case KEY_LO_${x:u} extends CPKeyboardKey(\"${x:l}\", Seq('${x:l}'))"; done
    case KEY_LO_A extends CPKeyboardKey("a", true, 'a', Seq('a'))
    case KEY_LO_B extends CPKeyboardKey("b", true, 'b', Seq('b'))
    case KEY_LO_C extends CPKeyboardKey("c", true, 'c', Seq('c'))
    case KEY_LO_D extends CPKeyboardKey("d", true, 'd', Seq('d'))
    case KEY_LO_E extends CPKeyboardKey("e", true, 'e', Seq('e'))
    case KEY_LO_F extends CPKeyboardKey("f", true, 'f', Seq('f'))
    case KEY_LO_G extends CPKeyboardKey("g", true, 'g', Seq('g'))
    case KEY_LO_H extends CPKeyboardKey("h", true, 'h', Seq('h'))
    case KEY_LO_I extends CPKeyboardKey("i", true, 'i', Seq('i'))
    case KEY_LO_J extends CPKeyboardKey("j", true, 'j', Seq('j'))
    case KEY_LO_K extends CPKeyboardKey("k", true, 'k', Seq('k'))
    case KEY_LO_L extends CPKeyboardKey("l", true, 'l', Seq('l'))
    case KEY_LO_M extends CPKeyboardKey("m", true, 'm', Seq('m'))
    case KEY_LO_N extends CPKeyboardKey("n", true, 'n', Seq('n'))
    case KEY_LO_O extends CPKeyboardKey("o", true, 'o', Seq('o'))
    case KEY_LO_P extends CPKeyboardKey("p", true, 'p', Seq('p'))
    case KEY_LO_Q extends CPKeyboardKey("q", true, 'q', Seq('q'))
    case KEY_LO_R extends CPKeyboardKey("r", true, 'r', Seq('r'))
    case KEY_LO_S extends CPKeyboardKey("s", true, 's', Seq('s'))
    case KEY_LO_T extends CPKeyboardKey("t", true, 't', Seq('t'))
    case KEY_LO_U extends CPKeyboardKey("u", true, 'u', Seq('u'))
    case KEY_LO_V extends CPKeyboardKey("v", true, 'v', Seq('v'))
    case KEY_LO_W extends CPKeyboardKey("w", true, 'w', Seq('w'))
    case KEY_LO_X extends CPKeyboardKey("x", true, 'x', Seq('x'))
    case KEY_LO_Y extends CPKeyboardKey("y", true, 'y', Seq('y'))
    case KEY_LO_Z extends CPKeyboardKey("z", true, 'z', Seq('z'))

    // Digits.
    // zsh: for x in {0..9}; do; echo "case KEY_${x} extends CPKeyboardKey(\"${x}\", Seq('${x}'))"; done
    case KEY_0 extends CPKeyboardKey("0", true, '0', Seq('0'))
    case KEY_1 extends CPKeyboardKey("1", true, '1', Seq('1'))
    case KEY_2 extends CPKeyboardKey("2", true, '2', Seq('2'))
    case KEY_3 extends CPKeyboardKey("3", true, '3', Seq('3'))
    case KEY_4 extends CPKeyboardKey("4", true, '4', Seq('4'))
    case KEY_5 extends CPKeyboardKey("5", true, '5', Seq('5'))
    case KEY_6 extends CPKeyboardKey("6", true, '6', Seq('6'))
    case KEY_7 extends CPKeyboardKey("7", true, '7', Seq('7'))
    case KEY_8 extends CPKeyboardKey("8", true, '8', Seq('8'))
    case KEY_9 extends CPKeyboardKey("9", true, '9', Seq('9'))

    // Non-alphanumerics.
    /** `'~'` key press. */
    case KEY_TILDE extends CPKeyboardKey("~", true, '~', Seq('~'))
    /** `'`' key press. */
    case KEY_BACK_QUOTE extends CPKeyboardKey("`", true, '`', Seq('`'))
    /** `'!'` key press. */
    case KEY_EXCL extends CPKeyboardKey("!", true, '!', Seq('!'))
    /** `'@'` key press. */
    case KEY_AT extends CPKeyboardKey("@", true, '@', Seq('@'))
    /** `'#'` key press. */
    case KEY_NUMBER_SIGN extends CPKeyboardKey("#", true, '#', Seq('#'))
    /** `'$'` key press. */
    case KEY_DOLLAR extends CPKeyboardKey("$", true, '$', Seq('$'))
    /** `'%'` key press. */
    case KEY_PERCENT extends CPKeyboardKey("%", true, '%', Seq('%'))
    /** `'^'` key press. */
    case KEY_CIRCUMFLEX extends CPKeyboardKey("^", true, '^', Seq('^'))
    /** `'&'` key press. */
    case KEY_AMPERSAND extends CPKeyboardKey("&", true, '&', Seq('&'))
    /** `'*'` key press. */
    case KEY_MULTIPLY extends CPKeyboardKey("*", true, '*', Seq('*'))
    /** `'('` key press. */
    case KEY_LPAR extends CPKeyboardKey("(", true, '(', Seq('('))
    /** `')'` key press. */
    case KEY_RPAR extends CPKeyboardKey(")", true, ')', Seq(')'))
    /** `'-'` key press. */
    case KEY_MINUS extends CPKeyboardKey("-", true, '-', Seq('-'))
    /** `'_'` key press. */
    case KEY_UNDERSCORE extends CPKeyboardKey("_", true, '_', Seq('_'))
    /** `'='` key press. */
    case KEY_EQUAL extends CPKeyboardKey("=", true, '=', Seq('='))
    /** `'+'` key press. */
    case KEY_PLUS extends CPKeyboardKey("+", true, '+', Seq('+'))
    /** `'['` key press. */
    case KEY_LBRKT extends CPKeyboardKey("[", true, '[', Seq('['))
    /** `']'` key press. */
    case KEY_RBRKT extends CPKeyboardKey("]", true, ']', Seq(']'))
    /** `'{'` key press. */
    case KEY_LCBRKT extends CPKeyboardKey("{", true, '{', Seq('{'))
    /** `'}'` key press. */
    case KEY_RCBRKT extends CPKeyboardKey("}", true, '}', Seq('}'))
    /** `';'` key press. */
    case KEY_SEMICOLON extends CPKeyboardKey(";", true, ';', Seq(';'))
    /** `':'` key press. */
    case KEY_COLON extends CPKeyboardKey(":", true, ':', Seq(':'))
    /** `''''` key press. */
    case KEY_SQUOTE extends CPKeyboardKey("'", true, ch = '\'', Seq('\''))
    /** `'"'` key press. */
    case KEY_DQUOTE extends CPKeyboardKey("\"", true, '"', Seq('"'))
    /** `'.'` key press. */
    case KEY_PERIOD extends CPKeyboardKey(".", true, '.', Seq('.'))
    /** `','` key press. */
    case KEY_COMMA extends CPKeyboardKey(",", true, ',', Seq(','))
    /** `'<'` key press. */
    case KEY_LT extends CPKeyboardKey("<", true, '<', Seq('<'))
    /** `'>'` key press. */
    case KEY_GT extends CPKeyboardKey(">", true, '>', Seq('>'))
    /** `'/'` key press. */
    case KEY_SLASH extends CPKeyboardKey("/", true, '/', Seq('/'))
    /** `'\\'` key press. */
    case KEY_BACK_SLASH extends CPKeyboardKey("\\", true, ch = '\\', Seq('\\'))
    /** `'|'` key press. */
    case KEY_VERT extends CPKeyboardKey("|", true, '|', Seq('|'))
    /** `'?'` key press. */
    case KEY_QUESTION extends CPKeyboardKey("?", true, '?', Seq('?'))

    override def hashCode(): Int = id.hashCode
    override def equals(obj: Any): Boolean = obj match
        case x: CPKeyboardKey => x.id == id
        case _ => false
    override def toString: String = s"'$id'"

