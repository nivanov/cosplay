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

import impl.CPAnsi.*
import impl.*

import java.awt.Color

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
  * An RGB/HSB color.
  *
  * A color is an immutable container for 24-bit RGB value.
  *
  * ### Color Creation
  * Color companion object provides 100s of the pre-built color constants for all xterm and X11 standard colors.
  * New color creation is an expensive process. It is highly recommended using one of the many built-in color
  * constants or pre-create the colors you need upfront. As a rule of thumb - avoid new color creation on each frame
  * update.
  *
  * ### 8-Bit & 24-Bit Color Depth
  * Most modern ANSI terminals support "True Color" [[https://en.wikipedia.org/wiki/ANSI_escape_code#24-bit 24-bit colors]],
  * the unlimited combination of Red, Green, and Blue values in RGB. Some older terminals, however, only
  * provide support for [[https://en.wikipedia.org/wiki/ANSI_escape_code#8-bit 8-bit colors]] where each color is a
  * lookup number into 256-color lookup table (LUT). These are also often called xterm colors despite the fact
  * that xterm terminal application does support the 24-bit colors. Note that there's only an approximate translation
  * from a true 24-bit color to 8-bit color, i.e. the closest 8-bit color will be used when translating from
  * 24-bit color to 8-bit color. More specifically, many darker 24-bit colors will convert as 8-bit dark grey.
  *
  * By default, CosPlay stores all colors in 24-bit format internally and renders all colors as 24-bit colors in both native
  * terminal and the built-in terminal emulator. If, however, you want to automatically convert 24-bit color to the nearest
  * 8-bit color during rendering you need to set system property `COSPLAY_FORCE_8BIT_COLOR=true`. Note that this will force
  * both native terminal and built-in terminal emulator to use 8-bit color conversion even though technically the
  * built-in terminal emulator can always display true 24-bit color. This is only necessary if running the game in the
  * native terminal that does not support 24-bit colors.
  *
  * Note also that since 8-bit color space is much smaller many color effects like fade in and fade out, color gradients,
  * etc. will look less smooth when using 8-bit colors.
  *
  * ### XTerm vs X11 Color Names
  * Companion object provides constants for pre-built colors:
  *  - Names starting with `C_` represent colors in [[https://www.ditig.com/256-colors-cheat-sheet xterm naming convention]].
  *  - Names starting with `C_X11_` represent colors in [[https://en.wikipedia.org/wiki/X11_color_names X11 color naming convention]].
  *  - Names starting with `CS_X11_` represent colors grouped by the primary color in [[https://en.wikipedia.org/wiki/X11_color_names X11 color naming convention]].
  *
  * You are free to use any constants with any naming convention, as well as mix and match - they are all just colors. Note
  * that although there are many similar and identical colors across two naming groups, they do differ in some as well as
  * offer some unique colors. CosPlay provides these two sets of constants for convenience. Please, refer to the
  * following links for specific color references:
  *  - [[https://www.ditig.com/256-colors-cheat-sheet xterm color LUT]]
  *  - [[https://en.wikipedia.org/wiki/X11_color_names X11 color names]]
  *  - [[https://en.wikipedia.org/wiki/ANSI_escape_code#Colors 8-bit and 24-bit colors]]
  *
  * @param red Red RGB component.
  * @param green Green RGB component.
  * @param blue Blue RGB component.
  */
final case class CPColor(red: Int, green: Int, blue: Int) extends CPIntTuple[CPColor](red, green, blue) with Ordered[CPColor]:
    import CPColor.*

    assert(red >= 0 && red <= 0xFF && green >= 0 && green <= 0xFF && blue >= 0 && blue <= 0xFF, s"Invalid RGB values [$red,$green,$blue].")

    private val strClr = s"[r=$red,g=$green,b=$blue]"

    override def compare(that: CPColor): Int = rgb.compareTo(that.rgb)
    override protected def ctor(rgb: Seq[Int]): CPColor =
        assert(rgb.sizeIs == 3)
        CPColor(rgb.head, rgb(1), rgb(2))

    /** RGB value of this color. */
    final val rgb: Int = ((red & 0x0ff) << 16) | ((green & 0x0ff) << 8) | (blue & 0x0ff)

    /**
      * HSB (Hue, Saturation, Brightness) values as 3-element array for this color.
      *
      * @see [[hue]]
      * @see [[saturation]]
      * @see [[brightness]]
      */
    final val hsb: Array[Float] = Color.RGBtoHSB(red, green, blue, null)

    /**
      * 8-bit xterm lookup number for this color.
      *
      * Note that if converted from 24-bit color this is only an approximation unless there's a direct match
      * between 24-bit color space and 8-bit color space.
      */
    final val xterm: Int = toXterm(red, green, blue)

    /**
     * If 8-bit color space is used.
     *
     * By default, CosPlay stores all colors in 24-bit format internally and renders all colors as 24-bit colors in both native
     * terminal and the built-in terminal emulator. If, however, you want to automatically convert 24-bit color to the nearest
     * 8-bit color during rendering you need to set system property `COSPLAY_FORCE_8BIT_COLOR=true`. Note that this will force
     * both native terminal and built-in terminal emulator to use 8-bit color conversion even though technically the
     * built-in terminal emulator can always display true 24-bit color. This is only necessary if running the game in the
     * native terminal that does not support 24-bit colors.
     */
    final val color8Bit: Boolean = force8Bit

    /**
     * If 24-bit color space is used (default behavior).
     *
     * By default, CosPlay stores all colors in 24-bit format internally and renders all colors as 24-bit colors in both native
     * terminal and the built-in terminal emulator. If, however, you want to automatically convert 24-bit color to the nearest
     * 8-bit color during rendering you need to set system property `COSPLAY_FORCE_8BIT_COLOR=true`. Note that this will force
     * both native terminal and built-in terminal emulator to use 8-bit color conversion even though technically the
     * built-in terminal emulator can always display true 24-bit color. This is only necessary if running the game in the
     * native terminal that does not support 24-bit colors.
     */
    final val color24Bit: Boolean = !force8Bit

    /** Color's hue. */
    final val hue: Float = hsb(0)

    /** Color's saturation. */
    final val saturation: Float = hsb(1)

    /** Color's brightness. */
    final val brightness: Float = hsb(2)

    /** Hexadecimal string representation of this color's RGB value in `0x000000` upper case format */
    final val hex: String = s"0x${rgb.toHexString.toUpperCase}"

    /**
     * ANSI foreground color sequence for this color. It automatically accounts for 8-bit or 24-bit color rendering.
     */
    final val fgAnsi = if force8Bit then fg8Bit(xterm) else fg24Bit(red, green, blue)

    /**
     * ANSI background color sequence for this color. It automatically accounts for 8-bit or 24-bit color rendering.
     */
    final val bgAnsi = if force8Bit then bg8Bit(xterm) else bg24Bit(red, green, blue)

    /**
     * Standard Java AWT color. It automatically accounts for 8-bit or 24-bit color rendering.
     */
    final val awt = if force8Bit then new Color(XTERM_COLORS(xterm)) else new Color(red, green, blue)

    /**
      * Gets a new color by multiplying RGB values by given `factor`.
      *
      * @param factor Factor to multiply each RGB value by.
      */
    def transform(factor: Float): CPColor =
        assert(factor >= 0 && factor <= 1, "Factor must be >= 0 && <= 1")
        mapInt(d => (d * factor).round)

    /**
      * Gets a new color by multiplying RGB values by given channel-specific `factor`s.
      *
      * @param factorR Factor to multiply each red-channel value by.
      * @param factorG Factor to multiply each green-channel value by.
      * @param factorB Factor to multiply each blue-channel value by.
      */
    def transform(factorR: Float, factorG: Float, factorB: Float): CPColor =
        assert(factorR >= 0 && factorR <= 1, "Red channel factor must be >= 0 && <= 1")
        assert(factorG >= 0 && factorG <= 1, "Green channel factor must be >= 0 && <= 1")
        assert(factorB >= 0 && factorB <= 1, "Blue channel factor must be >= 0 && <= 1")
        CPColor((red * factorR).round, (green * factorG).round, (blue * factorB).round)

    /**
      * Gets a new darker color.
      *
      * @param factor Mixing factor in `[0,1]` range. 0.9` means 90% darker, 0.1` means 10% darker.
      */
    inline def darker(factor: Float): CPColor =
        assert(factor >= 0 && factor <= 1, "Factor must be >= 0 && <= 1")
        val inv: Float = 1.0f - factor
        mapInt(d => (d * inv).round)

    /**
      * Gets a new black-and-white version of this color. It is using basic
      * averaging method.
      */
    inline def bw(): CPColor =
        val avg = (red + green + blue) / 3
        CPColor(avg, avg, avg)

    /**
      * Gets a new black-and-white version of this color. It is method that is weighted for lighter
      * greens and darker blues.
      */
    inline def bw2(): CPColor =
        val avg = (red * 0.299 + green * 0.587 + blue * 0.114).toInt
        CPColor(avg, avg, avg)

    /**
      * Get a new lighter color.
      *
      * @param factor Mixing factor in `[0.1]` range. '1.0' means pure white, '0.9' means 90% lighter,
      *     '0.1' means 10% lighter.
      */
    inline def lighter(factor: Float): CPColor =
        assert(factor >= 0 && factor <= 1, "Factor must be >= 0 && <= 1")
        mapInt(d => Math.round(d + (255 - d) * factor))

    override def toString: String = strClr

/**
  * Companion object contains utility functions and color constants.
  */
object CPColor:
    // https://stackoverflow.com/questions/11765623/convert-hex-to-closest-x11-color-number
    private final val Q2C = Seq(0x0, 0x5f, 0x87, 0xaf, 0xd7, 0xff).toIndexedSeq

    // xterm LUT.
    private final val XTERM_COLORS = Seq(
        0x000000,
        0x800000,
        0x008000,
        0x808000,
        0x000080,
        0x800080,
        0x008080,
        0xc0c0c0,
        0x808080,
        0xff0000,
        0x00ff00,
        0xffff00,
        0x0000ff,
        0xff00ff,
        0x00ffff,
        0xffffff,
        0x000000,
        0x00005f,
        0x000087,
        0x0000af,
        0x0000d7,
        0x0000ff,
        0x005f00,
        0x005f5f,
        0x005f87,
        0x005faf,
        0x005fd7,
        0x005fff,
        0x008700,
        0x00875f,
        0x008787,
        0x0087af,
        0x0087d7,
        0x0087ff,
        0x00af00,
        0x00af5f,
        0x00af87,
        0x00afaf,
        0x00afd7,
        0x00afff,
        0x00d700,
        0x00d75f,
        0x00d787,
        0x00d7af,
        0x00d7d7,
        0x00d7ff,
        0x00ff00,
        0x00ff5f,
        0x00ff87,
        0x00ffaf,
        0x00ffd7,
        0x00ffff,
        0x5f0000,
        0x5f005f,
        0x5f0087,
        0x5f00af,
        0x5f00d7,
        0x5f00ff,
        0x5f5f00,
        0x5f5f5f,
        0x5f5f87,
        0x5f5faf,
        0x5f5fd7,
        0x5f5fff,
        0x5f8700,
        0x5f875f,
        0x5f8787,
        0x5f87af,
        0x5f87d7,
        0x5f87ff,
        0x5faf00,
        0x5faf5f,
        0x5faf87,
        0x5fafaf,
        0x5fafd7,
        0x5fafff,
        0x5fd700,
        0x5fd75f,
        0x5fd787,
        0x5fd7af,
        0x5fd7d7,
        0x5fd7ff,
        0x5fff00,
        0x5fff5f,
        0x5fff87,
        0x5fffaf,
        0x5fffd7,
        0x5fffff,
        0x870000,
        0x87005f,
        0x870087,
        0x8700af,
        0x8700d7,
        0x8700ff,
        0x875f00,
        0x875f5f,
        0x875f87,
        0x875faf,
        0x875fd7,
        0x875fff,
        0x878700,
        0x87875f,
        0x878787,
        0x8787af,
        0x8787d7,
        0x8787ff,
        0x87af00,
        0x87af5f,
        0x87af87,
        0x87afaf,
        0x87afd7,
        0x87afff,
        0x87d700,
        0x87d75f,
        0x87d787,
        0x87d7af,
        0x87d7d7,
        0x87d7ff,
        0x87ff00,
        0x87ff5f,
        0x87ff87,
        0x87ffaf,
        0x87ffd7,
        0x87ffff,
        0xaf0000,
        0xaf005f,
        0xaf0087,
        0xaf00af,
        0xaf00d7,
        0xaf00ff,
        0xaf5f00,
        0xaf5f5f,
        0xaf5f87,
        0xaf5faf,
        0xaf5fd7,
        0xaf5fff,
        0xaf8700,
        0xaf875f,
        0xaf8787,
        0xaf87af,
        0xaf87d7,
        0xaf87ff,
        0xafaf00,
        0xafaf5f,
        0xafaf87,
        0xafafaf,
        0xafafd7,
        0xafafff,
        0xafd700,
        0xafd75f,
        0xafd787,
        0xafd7af,
        0xafd7d7,
        0xafd7ff,
        0xafff00,
        0xafff5f,
        0xafff87,
        0xafffaf,
        0xafffd7,
        0xafffff,
        0xd70000,
        0xd7005f,
        0xd70087,
        0xd700af,
        0xd700d7,
        0xd700ff,
        0xd75f00,
        0xd75f5f,
        0xd75f87,
        0xd75faf,
        0xd75fd7,
        0xd75fff,
        0xd78700,
        0xd7875f,
        0xd78787,
        0xd787af,
        0xd787d7,
        0xd787ff,
        0xd7af00,
        0xd7af5f,
        0xd7af87,
        0xd7afaf,
        0xd7afd7,
        0xd7afff,
        0xd7d700,
        0xd7d75f,
        0xd7d787,
        0xd7d7af,
        0xd7d7d7,
        0xd7d7ff,
        0xd7ff00,
        0xd7ff5f,
        0xd7ff87,
        0xd7ffaf,
        0xd7ffd7,
        0xd7ffff,
        0xff0000,
        0xff005f,
        0xff0087,
        0xff00af,
        0xff00d7,
        0xff00ff,
        0xff5f00,
        0xff5f5f,
        0xff5f87,
        0xff5faf,
        0xff5fd7,
        0xff5fff,
        0xff8700,
        0xff875f,
        0xff8787,
        0xff87af,
        0xff87d7,
        0xff87ff,
        0xffaf00,
        0xffaf5f,
        0xffaf87,
        0xffafaf,
        0xffafd7,
        0xffafff,
        0xffd700,
        0xffd75f,
        0xffd787,
        0xffd7af,
        0xffd7d7,
        0xffd7ff,
        0xffff00,
        0xffff5f,
        0xffff87,
        0xffffaf,
        0xffffd7,
        0xffffff,
        0x080808,
        0x121212,
        0x1c1c1c,
        0x262626,
        0x303030,
        0x3a3a3a,
        0x444444,
        0x4e4e4e,
        0x585858,
        0x606060,
        0x666666,
        0x767676,
        0x808080,
        0x8a8a8a,
        0x949494,
        0x9e9e9e,
        0xa8a8a8,
        0xb2b2b2,
        0xbcbcbc,
        0xc6c6c6,
        0xd0d0d0,
        0xdadada,
        0xe4e4e4,
        0xeeeeee
    ).toIndexedSeq

    /** */
    private final val force8Bit = CPUtils.sysEnvBool("COSPLAY_FORCE_8BIT_COLOR")

    /**
      * Creates new color with this RGB string value.
      *
      * @param rgb RGB value.
      * @example {{{CPColor("0x1212ef")}}}
      * @example {{{CPColor("123123")}}}
      */
    def apply(rgb: String): CPColor = apply(Integer.decode(rgb))

    /**
      * Creates new color with this RGB value.
      *
      * @param rgb RGB value.
      */
    def apply(rgb: Int): CPColor =
        val r = (rgb >> 16) & 0x0ff
        val g = (rgb >> 8) & 0x0ff
        val b = rgb & 0x0ff

        CPColor(r,g, b)

    /**
      *
      * @param r Red.
      * @param g Green
      * @param b Blue.
      */
    private def toXterm(r: Int, g: Int, b: Int): Int =
        // https://stackoverflow.com/questions/11765623/convert-hex-to-closest-x11-color-number
        def colorDist(R: Int, G: Int, B: Int, r: Int, g: Int, b: Int): Int = (R - r) * (R - r) + (G - g) * (G - g) + (B - b) * (B - b)
        def color6Cube(v: Int): Int = if v < 48 then 0 else if v < 115 then 1 else (v - 35) / 40

        val ir = color6Cube(r)
        val ig = color6Cube(g)
        val ib = color6Cube(b)
        val clrIdx = 36 * ir + 6 * ig + ib
        val avg = (r + g + b) / 3
        val grayIdx = if avg > 238 then 23 else (avg - 3) / 10
        val cr = Q2C(ir)
        val cg = Q2C(ig)
        val cb = Q2C(ib)
        val gv = 8 + 10 * grayIdx
        val clrErr = colorDist(cr, cg, cb, r, g, b)
        val grayErr = colorDist(gv, gv, gv, r, g, b)
        if clrErr <= grayErr then 16 + clrIdx else 232 + grayIdx

    /**
      * Creates function for gradient color between two given colors.
      *
      * @param c1 Color 1.
      * @param c2 Color 2.
      * @param steps How many step of gradients between color 1 and color 2 to generate.
      */
    def gradientFun(c1: CPColor, c2: CPColor, steps: Int): () => CPColor =
        val grad = gradientSeq(c1, c2, steps)
        var i = 0
        () => {
            val c = grad(i % steps)
            i += 1
            c
        }

    /**
      * Gets a new color that is a mixture between given two colors with specified balance.
      *
      * @param c1 First color to mix.
      * @param c2 Second color to mix.
      * @param balance Mixture balance. Value must be in [0,1] range. Value `0` means that
      *     color `c1` will be 100% of the mixture, value `1` means that color `c2` will
      *     be 100% of the mixture.
      */
    def mixture(c1: CPColor, c2: CPColor, balance: Float): CPColor =
        val dr = (c2.red - c1.red) * balance
        val dg = (c2.green - c1.green) * balance
        val db = (c2.blue - c1.blue) * balance

        CPColor(
            (c1.red + dr).round,
            (c1.green + dg).round,
            (c1.blue + db).round
        )

    /**
      * Creates sequence of gradient colors between two given colors.
      *
      * @param c1 Color 1.
      * @param c2 Color 2.
      * @param steps How many step of gradients between color 1 and color 2 to generate.
      */
    def gradientSeq(c1: CPColor, c2: CPColor, steps: Int): Seq[CPColor] =
        val stepsF = steps.toFloat
        val dr: Float = (c2.red - c1.red) / stepsF
        val dg: Float = (c2.green - c1.green) / stepsF
        val db: Float = (c2.blue - c1.blue) / stepsF

        for  (i <- 1 to steps) yield CPColor(
            (c1.red + i * dr).round,
            (c1.green + i * dg).round,
            (c1.blue + i * db).round
        )

    final val C_X11_LIGHT_SALMON = CPColor(255, 160, 122)
    final val C_X11_SALMON = CPColor(250, 128, 114)
    final val C_X11_DARK_SALMON = CPColor(233, 150, 122)
    final val C_X11_LIGHT_CORAL = CPColor(240, 128, 128)
    final val C_X11_INDIAN_RED = CPColor(205, 92, 92)
    final val C_X11_CRIMSON = CPColor(220, 20, 60)
    final val C_X11_FIRE_BRICK = CPColor(178, 34, 34)
    final val C_X11_RED = CPColor(255, 0, 0)
    final val C_X11_DARK_RED = CPColor(139, 0, 0)

    /**
      *
      */
    final val CS_X11_REDS = Seq(
        C_X11_LIGHT_SALMON,
        C_X11_SALMON,
        C_X11_DARK_SALMON,
        C_X11_LIGHT_CORAL,
        C_X11_INDIAN_RED,
        C_X11_CRIMSON,
        C_X11_FIRE_BRICK,
        C_X11_RED,
        C_X11_DARK_RED
    ).sorted

    final val C_X11_CORAL = CPColor(255, 127, 80)
    final val C_X11_TOMATO = CPColor(255, 99, 71)
    final val C_X11_ORANGE_RED = CPColor(255, 69, 0)
    final val C_X11_GOLD = CPColor(255, 215, 0)
    final val C_X11_ORANGE = CPColor(255, 165, 0)
    final val C_X11_DARK_ORANGE = CPColor(255, 140, 0)

    /**
      *
      */
    final val CS_X11_ORANGES = Seq(
        C_X11_CORAL,
        C_X11_TOMATO,
        C_X11_ORANGE_RED,
        C_X11_GOLD,
        C_X11_ORANGE,
        C_X11_DARK_ORANGE
    ).sorted

    /*
     * System colors.
     */
    final val C_BLACK = CPColor(0,  0,  0)
    final val C_MAROON  = CPColor(128, 0, 0)
    final val C_GREEN  = CPColor(0, 128, 0)
    final val C_OLIVE  = CPColor(128, 128, 0)
    final val C_NAVY  = CPColor(0, 0, 128)
    final val C_PURPLE  = CPColor(128, 0, 128)
    final val C_TEAL  = CPColor(0, 128, 128)
    final val C_SILVER  = CPColor(192, 192, 192)
    final val C_GREY  = CPColor(128, 128, 128)
    final val C_RED  = CPColor(255, 0, 0)
    final val C_LIME  = CPColor(0, 255, 0)
    final val C_YELLOW  = CPColor(255, 255, 0)
    final val C_BLUE  = CPColor(0, 0, 255)
    final val C_FUCHSIA  = CPColor(255, 0, 255)
    final val C_AQUA  = CPColor(0, 255, 255)
    final val C_WHITE  = CPColor(255, 255, 255)

    /*
     * xterm colors.
     * https://www.ditig.com/256-colors-cheat-sheet
     */
    final val C_GREY0 = CPColor(0, 0, 0)
    final val C_NAVY_BLUE = CPColor(0, 0, 95)
    final val C_DARK_BLUE = CPColor(0, 0, 135)
    final val C_BLUE3 = CPColor(0, 0, 175)
    final val C_BLUE31 = CPColor(0, 0, 215)
    final val C_BLUE1 = CPColor(0, 0, 255)
    final val C_DARK_GREEN = CPColor(0, 95, 0)
    final val C_STEEL_BLUE14 = CPColor(0, 95, 95)
    final val C_STEEL_BLUE14A = CPColor(0, 95, 135)
    final val C_STEEL_BLUE14B = CPColor(0, 95, 175)
    final val C_DODGER_BLUE3 = CPColor(0, 95, 215)
    final val C_DODGER_BLUE2 = CPColor(0, 95, 255)
    final val C_GREEN4 = CPColor(0, 135, 0)
    final val C_SPRING_GREEN4 = CPColor(0, 135, 95)
    final val C_TURQUOISE4 = CPColor(0, 135, 135)
    final val C_STEEL_BLUE13 = CPColor(0, 135, 175)
    final val C_STEEL_BLUE13A = CPColor(0, 135, 215)
    final val C_DODGER_BLUE1 = CPColor(0, 135, 255)
    final val C_GREEN3 = CPColor(0, 175, 0)
    final val C_SPRING_GREEN3 = CPColor(0, 175, 95)
    final val C_DARK_CYAN = CPColor(0, 175, 135)
    final val C_LIGHT_SEA_GREEN = CPColor(0, 175, 175)
    final val C_STEEL_BLUE12 = CPColor(0, 175, 215)
    final val C_STEEL_BLUE11 = CPColor(0, 175, 255)
    final val C_GREEN3A = CPColor(0, 215, 0)
    final val C_SPRING_GREEN3A = CPColor(0, 215, 95)
    final val C_SPRING_GREEN2 = CPColor(0, 215, 135)
    final val C_CYAN3 = CPColor(0, 215, 175)
    final val C_DARK_TURQUOISE = CPColor(0, 215, 215)
    final val C_TURQUOISE2 = CPColor(0, 215, 255)
    final val C_GREEN1 = CPColor(0, 255, 0)
    final val C_SPRING_GREEN2A = CPColor(0, 255, 95)
    final val C_SPRING_GREEN1 = CPColor(0, 255, 135)
    final val C_MEDIUM_SPRING_GREEN = CPColor(0, 255, 175)
    final val C_CYAN2 = CPColor(0, 255, 215)
    final val C_CYAN1 = CPColor(0, 255, 255)
    final val C_DARK_RED = CPColor(95, 0, 0)
    final val C_DEEP_PINK4 = CPColor(95, 0, 95)
    final val C_PURPLE4 = CPColor(95, 0, 135)
    final val C_PURPLE4A = CPColor(95, 0, 175)
    final val C_PURPLE3 = CPColor(95, 0, 215)
    final val C_BLUE_VIOLET = CPColor(95, 0, 255)
    final val C_ORANGE4 = CPColor(95, 95, 0)
    final val C_GREY37 = CPColor(95, 95, 95)
    final val C_MEDIUM_PURPLE34 = CPColor(95, 95, 135)
    final val C_SLATE_BLUE3 = CPColor(95, 95, 175)
    final val C_SLATE_BLUE3A = CPColor(95, 95, 215)
    final val C_ROYAL_BLUE1 = CPColor(95, 95, 255)
    final val C_CHARTREUSE4 = CPColor(95, 135, 0)
    final val C_DARK_SEA_GREEN4 = CPColor(95, 135, 95)
    final val C_PALE_TURQUOISE4 = CPColor(95, 135, 135)
    final val C_STEEL_BLUE = CPColor(95, 135, 175)
    final val C_STEEL_BLUE3 = CPColor(95, 135, 215)
    final val C_CORNFLOWER_BLUE = CPColor(95, 135, 255)
    final val C_CHARTREUSE3 = CPColor(95, 175, 0)
    final val C_DARK_SEA_GREEN4A = CPColor(95, 175, 95)
    final val C_CADET_BLUE = CPColor(95, 175, 135)
    final val C_CADET_BLUE1 = CPColor(95, 175, 175)
    final val C_SKY_BLUE1 = CPColor(95, 175, 215)
    final val C_STEEL_BLUE1 = CPColor(95, 175, 255)
    final val C_CHARTREUSE3A = CPColor(95, 215, 0)
    final val C_PALE_GREEN3 = CPColor(95, 215, 95)
    final val C_SEA_GREEN3 = CPColor(95, 215, 135)
    final val C_AQUAMARINE3 = CPColor(95, 215, 175)
    final val C_MEDIUM_TURQUOISE = CPColor(95, 215, 215)
    final val C_STEEL_BLUE1A = CPColor(95, 215, 255)
    final val C_CHARTREUSE2 = CPColor(95, 255, 0)
    final val C_SEA_GREEN2 = CPColor(95, 255, 95)
    final val C_SEA_GREEN1 = CPColor(95, 255, 135)
    final val C_SEA_GREEN1A = CPColor(95, 255, 175)
    final val C_AQUAMARINE1 = CPColor(95, 255, 215)
    final val C_DARK_SLATE_GRAY2 = CPColor(95, 255, 255)
    final val C_DARK_RED1 = CPColor(135, 0, 0)
    final val C_DEEP_PINK4A = CPColor(135, 0, 95)
    final val C_DARK_MAGENTA = CPColor(135, 0, 135)
    final val C_DARK_MAGENTA1 = CPColor(135, 0, 175)
    final val C_DARK_VIOLET = CPColor(135, 0, 215)
    final val C_PURPLE1 = CPColor(135, 0, 255)
    final val C_ORANGE4A = CPColor(135, 95, 0)
    final val C_LIGHT_PINK4 = CPColor(135, 95, 95)
    final val C_PLUM4 = CPColor(135, 95, 135)
    final val C_MEDIUM_PURPLE33 = CPColor(135, 95, 175)
    final val C_MEDIUM_PURPLE33A = CPColor(135, 95, 215)
    final val C_SLATE_BLUE1 = CPColor(135, 95, 255)
    final val C_YELLOW4 = CPColor(135, 135, 0)
    final val C_WHEAT4 = CPColor(135, 135, 95)
    final val C_GREY53 = CPColor(135, 135, 135)
    final val C_LIGHT_SLATE_GREY = CPColor(135, 135, 175)
    final val C_MEDIUM_PURPLE3 = CPColor(135, 135, 215)
    final val C_LIGHT_SLATE_BLUE = CPColor(135, 135, 255)
    final val C_YELLOW4A = CPColor(135, 175, 0)
    final val C_DARK_OLIVE_GREEN3 = CPColor(135, 175, 95)
    final val C_DARK_SEA_GREEN = CPColor(135, 175, 135)
    final val C_LIGHT_SKY_BLUE3 = CPColor(135, 175, 175)
    final val C_LIGHT_SKY_BLUE3A = CPColor(135, 175, 215)
    final val C_SKY_BLUE12 = CPColor(135, 175, 255)
    final val C_CHARTREUSE2A = CPColor(135, 215, 0)
    final val C_DARK_OLIVE_GREEN3A = CPColor(135, 215, 95)
    final val C_PALE_GREEN3A = CPColor(135, 215, 135)
    final val C_DARK_SEA_GREEN3 = CPColor(135, 215, 175)
    final val C_DARK_SLATE_GRAY3 = CPColor(135, 215, 215)
    final val C_SKY_BLUE11 = CPColor(135, 215, 255)
    final val C_CHARTREUSE1 = CPColor(135, 255, 0)
    final val C_LIGHT_GREEN = CPColor(135, 255, 95)
    final val C_LIGHT_GREEN1 = CPColor(135, 255, 135)
    final val C_PALE_GREEN1 = CPColor(135, 255, 175)
    final val C_AQUAMARINE1A = CPColor(135, 255, 215)
    final val C_DARK_SLATE_GRAY1 = CPColor(135, 255, 255)
    final val C_RED3 = CPColor(175, 0, 0)
    final val C_DEEP_PINK4B = CPColor(175, 0, 95)
    final val C_MEDIUM_VIOLET_RED = CPColor(175, 0, 135)
    final val C_MAGENTA3 = CPColor(175, 0, 175)
    final val C_DARK_VIOLET1 = CPColor(175, 0, 215)
    final val C_PURPLE2 = CPColor(175, 0, 255)
    final val C_DARK_ORANGE3 = CPColor(175, 95, 0)
    final val C_INDIAN_RED = CPColor(175, 95, 95)
    final val C_HOT_PINK3 = CPColor(175, 95, 135)
    final val C_MEDIUM_ORCHID3 = CPColor(175, 95, 175)
    final val C_MEDIUM_ORCHID = CPColor(175, 95, 215)
    final val C_MEDIUM_PURPLE32 = CPColor(175, 95, 255)
    final val C_DARK_GOLDEN_ROD = CPColor(175, 135, 0)
    final val C_LIGHT_SALMON3 = CPColor(175, 135, 95)
    final val C_ROSY_BROWN = CPColor(175, 135, 135)
    final val C_GREY63 = CPColor(175, 135, 175)
    final val C_MEDIUM_PURPLE32A = CPColor(175, 135, 215)
    final val C_MEDIUM_PURPLE31 = CPColor(175, 135, 255)
    final val C_GOLD3 = CPColor(175, 175, 0)
    final val C_DARK_KHAKI = CPColor(175, 175, 95)
    final val C_NAVAJO_WHITE3 = CPColor(175, 175, 135)
    final val C_GREY69 = CPColor(175, 175, 175)
    final val C_LIGHT_STEEL_BLUE3 = CPColor(175, 175, 215)
    final val C_LIGHT_STEEL_BLUE = CPColor(175, 175, 255)
    final val C_YELLOW3 = CPColor(175, 215, 0)
    final val C_DARK_OLIVE_GREEN3B = CPColor(175, 215, 95)
    final val C_DARK_SEA_GREEN3A = CPColor(175, 215, 135)
    final val C_DARK_SEA_GREEN2 = CPColor(175, 215, 175)
    final val C_LIGHT_CYAN3 = CPColor(175, 215, 215)
    final val C_LIGHT_SKYBLUE1 = CPColor(175, 215, 255)
    final val C_GREEN_YELLOW = CPColor(175, 255, 0)
    final val C_DARK_OLIVE_GREEN2 = CPColor(175, 255, 95)
    final val C_PALE_GREEN1A = CPColor(175, 255, 135)
    final val C_DARK_SEA_GREEN2A = CPColor(175, 255, 175)
    final val C_DARK_SEA_GREEN1 = CPColor(175, 255, 215)
    final val C_PALE_TURQUOISE1 = CPColor(175, 255, 255)
    final val C_RED3A = CPColor(215, 0, 0)
    final val C_DEEP_PINK3 = CPColor(215, 0, 95)
    final val C_DEEP_PINK3A = CPColor(215, 0, 135)
    final val C_MAGENTA3A = CPColor(215, 0, 175)
    final val C_MAGENTA3B = CPColor(215, 0, 215)
    final val C_MAGENTA2 = CPColor(215, 0, 255)
    final val C_DARK_ORANGE3A = CPColor(215, 95, 0)
    final val C_INDIAN_RED1 = CPColor(215, 95, 95)
    final val C_HOT_PINK3A = CPColor(215, 95, 135)
    final val C_HOT_PINK2 = CPColor(215, 95, 175)
    final val C_ORCHID = CPColor(215, 95, 215)
    final val C_MEDIUM_ORCHID1 = CPColor(215, 95, 255)
    final val C_ORANGE3 = CPColor(215, 135, 0)
    final val C_LIGHT_SALMON3A = CPColor(215, 135, 95)
    final val C_LIGHT_PINK3 = CPColor(215, 135, 135)
    final val C_PINK3 = CPColor(215, 135, 175)
    final val C_PLUM3 = CPColor(215, 135, 215)
    final val C_VIOLET = CPColor(215, 135, 255)
    final val C_GOLD3A = CPColor(215, 175, 0)
    final val C_LIGHT_GOLDEN_ROD3 = CPColor(215, 175, 95)
    final val C_TAN = CPColor(215, 175, 135)
    final val C_MISTY_ROSE3 = CPColor(215, 175, 175)
    final val C_THISTLE3 = CPColor(215, 175, 215)
    final val C_PLUM2 = CPColor(215, 175, 255)
    final val C_YELLOW3A = CPColor(215, 215, 0)
    final val C_KHAKI3 = CPColor(215, 215, 95)
    final val C_LIGHT_GOLDEN_ROD2 = CPColor(215, 215, 135)
    final val C_LIGHT_YELLOW3 = CPColor(215, 215, 175)
    final val C_GREY84 = CPColor(215, 215, 215)
    final val C_LIGHT_STEEL_BLUE1 = CPColor(215, 215, 255)
    final val C_YELLOW2 = CPColor(215, 255, 0)
    final val C_DARK_OLIVE_GREEN1 = CPColor(215, 255, 95)
    final val C_DARK_OLIVE_GREEN1A = CPColor(215, 255, 135)
    final val C_DARK_SEA_GREEN1A = CPColor(215, 255, 175)
    final val C_HONEYDEW2 = CPColor(215, 255, 215)
    final val C_LIGHT_CYAN1 = CPColor(215, 255, 255)
    final val C_RED1 = CPColor(255, 0, 0)
    final val C_DEEP_PINK2 = CPColor(255, 0, 95)
    final val C_DEEP_PINK1 = CPColor(255, 0, 135)
    final val C_DEEP_PINK1A = CPColor(255, 0, 175)
    final val C_MAGENTA2A = CPColor(255, 0, 215)
    final val C_MAGENTA1 = CPColor(255, 0, 255)
    final val C_ORANGE_RED1 = CPColor(255, 95, 0)
    final val C_INDIAN_RED1A = CPColor(255, 95, 95)
    final val C_INDIAN_RED1B = CPColor(255, 95, 135)
    final val C_HOT_PINK = CPColor(255, 95, 175)
    final val C_HOT_PINK1 = CPColor(255, 95, 215)
    final val C_MEDIUM_ORCHID1A = CPColor(255, 95, 255)
    final val C_DARK_ORANGE = CPColor(255, 135, 0)
    final val C_SALMON1 = CPColor(255, 135, 95)
    final val C_LIGHT_CORAL = CPColor(255, 135, 135)
    final val C_HOT_PINK31 = CPColor(255, 135, 175)
    final val C_ORCHID2 = CPColor(255, 135, 215)
    final val C_ORCHID1 = CPColor(255, 135, 255)
    final val C_ORANGE1 = CPColor(255, 175, 0)
    final val C_SANDY_BROWN = CPColor(255, 175, 95)
    final val C_LIGHT_SALMON1 = CPColor(255, 175, 135)
    final val C_LIGHT_PINK1 = CPColor(255, 175, 175)
    final val C_PINK1 = CPColor(255, 175, 215)
    final val C_PLUM1 = CPColor(255, 175, 255)
    final val C_GOLD1 = CPColor(255, 215, 0)
    final val C_LIGHT_GOLDEN_ROD2B = CPColor(255, 215, 95)
    final val C_LIGHT_GOLDEN_ROD2A = CPColor(255, 215, 135)
    final val C_NAVAJO_WHITE1 = CPColor(255, 215, 175)
    final val C_MISTY_ROSE1 = CPColor(255, 215, 215)
    final val C_THISTLE1 = CPColor(255, 215, 255)
    final val C_YELLOW1 = CPColor(255, 255, 0)
    final val C_LIGHT_GOLDEN_ROD1 = CPColor(255, 255, 95)
    final val C_KHAKI1 = CPColor(255, 255, 135)
    final val C_WHEAT1 = CPColor(255, 255, 175)
    final val C_CORN_SILK1 = CPColor(255, 255, 215)
    final val C_GREY100 = CPColor(255, 255, 255)
    final val C_GREY3 = CPColor(8, 8, 8)
    final val C_GREY7 = CPColor(18, 18, 18)
    final val C_GREY11 = CPColor(28, 28, 28)
    final val C_GREY15 = CPColor(38, 38, 38)
    final val C_GREY19 = CPColor(48, 48, 48)
    final val C_GREY23 = CPColor(58, 58, 58)
    final val C_GREY27 = CPColor(68, 68, 68)
    final val C_GREY30 = CPColor(78, 78, 78)
    final val C_GREY35 = CPColor(88, 88, 88)
    final val C_GREY39 = CPColor(98, 98, 98)
    final val C_GREY42 = CPColor(108, 108, 108)
    final val C_GREY46 = CPColor(118, 118, 118)
    final val C_GREY50 = CPColor(128, 128, 128)
    final val C_GREY54 = CPColor(138, 138, 138)
    final val C_GREY58 = CPColor(148, 148, 148)
    final val C_GREY62 = CPColor(158, 158, 158)
    final val C_GREY66 = CPColor(168, 168, 168)
    final val C_GREY70 = CPColor(178, 178, 178)
    final val C_GREY74 = CPColor(188, 188, 188)
    final val C_GREY78 = CPColor(198, 198, 198)
    final val C_GREY82 = CPColor(208, 208, 208)
    final val C_GREY85 = CPColor(218, 218, 218)
    final val C_GREY89 = CPColor(228, 228, 228)
    final val C_GREY93 = CPColor(238, 238, 238)

    final val C_DFLT_BG = CPColor(17, 17, 17)

    final val C_XTERM_ALL = Seq(
        C_GREY0,
        C_NAVY_BLUE,
        C_DARK_BLUE,
        C_BLUE3,
        C_BLUE31,
        C_BLUE1,
        C_DARK_GREEN,
        C_STEEL_BLUE14,
        C_STEEL_BLUE14A,
        C_STEEL_BLUE14B,
        C_DODGER_BLUE3,
        C_DODGER_BLUE2,
        C_GREEN4,
        C_SPRING_GREEN4,
        C_TURQUOISE4,
        C_STEEL_BLUE13,
        C_STEEL_BLUE13A,
        C_DODGER_BLUE1,
        C_GREEN3,
        C_SPRING_GREEN3,
        C_DARK_CYAN,
        C_LIGHT_SEA_GREEN,
        C_STEEL_BLUE12,
        C_STEEL_BLUE11,
        C_GREEN3A,
        C_SPRING_GREEN3A,
        C_SPRING_GREEN2,
        C_CYAN3,
        C_DARK_TURQUOISE,
        C_TURQUOISE2,
        C_GREEN1,
        C_SPRING_GREEN2A,
        C_SPRING_GREEN1,
        C_MEDIUM_SPRING_GREEN,
        C_CYAN2,
        C_CYAN1,
        C_DARK_RED,
        C_DEEP_PINK4,
        C_PURPLE4,
        C_PURPLE4A,
        C_PURPLE3,
        C_BLUE_VIOLET,
        C_ORANGE4,
        C_GREY37,
        C_MEDIUM_PURPLE34,
        C_SLATE_BLUE3,
        C_SLATE_BLUE3A,
        C_ROYAL_BLUE1,
        C_CHARTREUSE4,
        C_DARK_SEA_GREEN4,
        C_PALE_TURQUOISE4,
        C_STEEL_BLUE,
        C_STEEL_BLUE3,
        C_CORNFLOWER_BLUE,
        C_CHARTREUSE3,
        C_DARK_SEA_GREEN4A,
        C_CADET_BLUE,
        C_CADET_BLUE1,
        C_SKY_BLUE1,
        C_STEEL_BLUE1,
        C_CHARTREUSE3A,
        C_PALE_GREEN3,
        C_SEA_GREEN3,
        C_AQUAMARINE3,
        C_MEDIUM_TURQUOISE,
        C_STEEL_BLUE1A,
        C_CHARTREUSE2,
        C_SEA_GREEN2,
        C_SEA_GREEN1,
        C_SEA_GREEN1A,
        C_AQUAMARINE1,
        C_DARK_SLATE_GRAY2,
        C_DARK_RED1,
        C_DEEP_PINK4A,
        C_DARK_MAGENTA,
        C_DARK_MAGENTA1,
        C_DARK_VIOLET,
        C_PURPLE1,
        C_ORANGE4A,
        C_LIGHT_PINK4,
        C_PLUM4,
        C_MEDIUM_PURPLE33,
        C_MEDIUM_PURPLE33A,
        C_SLATE_BLUE1,
        C_YELLOW4,
        C_WHEAT4,
        C_GREY53,
        C_LIGHT_SLATE_GREY,
        C_MEDIUM_PURPLE3,
        C_LIGHT_SLATE_BLUE,
        C_YELLOW4A,
        C_DARK_OLIVE_GREEN3,
        C_DARK_SEA_GREEN,
        C_LIGHT_SKY_BLUE3,
        C_LIGHT_SKY_BLUE3A,
        C_SKY_BLUE12,
        C_CHARTREUSE2A,
        C_DARK_OLIVE_GREEN3A,
        C_PALE_GREEN3A,
        C_DARK_SEA_GREEN3,
        C_DARK_SLATE_GRAY3,
        C_SKY_BLUE11,
        C_CHARTREUSE1,
        C_LIGHT_GREEN,
        C_LIGHT_GREEN1,
        C_PALE_GREEN1,
        C_AQUAMARINE1A,
        C_DARK_SLATE_GRAY1,
        C_RED3,
        C_DEEP_PINK4B,
        C_MEDIUM_VIOLET_RED,
        C_MAGENTA3,
        C_DARK_VIOLET1,
        C_PURPLE2,
        C_DARK_ORANGE3,
        C_INDIAN_RED,
        C_HOT_PINK3,
        C_MEDIUM_ORCHID3,
        C_MEDIUM_ORCHID,
        C_MEDIUM_PURPLE32,
        C_DARK_GOLDEN_ROD,
        C_LIGHT_SALMON3,
        C_ROSY_BROWN,
        C_GREY63,
        C_MEDIUM_PURPLE32A,
        C_MEDIUM_PURPLE31,
        C_GOLD3,
        C_DARK_KHAKI,
        C_NAVAJO_WHITE3,
        C_GREY69,
        C_LIGHT_STEEL_BLUE3,
        C_LIGHT_STEEL_BLUE,
        C_YELLOW3,
        C_DARK_OLIVE_GREEN3B,
        C_DARK_SEA_GREEN3A,
        C_DARK_SEA_GREEN2,
        C_LIGHT_CYAN3,
        C_LIGHT_SKYBLUE1,
        C_GREEN_YELLOW,
        C_DARK_OLIVE_GREEN2,
        C_PALE_GREEN1A,
        C_DARK_SEA_GREEN2A,
        C_DARK_SEA_GREEN1,
        C_PALE_TURQUOISE1,
        C_RED3A,
        C_DEEP_PINK3,
        C_DEEP_PINK3A,
        C_MAGENTA3A,
        C_MAGENTA3B,
        C_MAGENTA2,
        C_DARK_ORANGE3A,
        C_INDIAN_RED1,
        C_HOT_PINK3A,
        C_HOT_PINK2,
        C_ORCHID,
        C_MEDIUM_ORCHID1,
        C_ORANGE3,
        C_LIGHT_SALMON3A,
        C_LIGHT_PINK3,
        C_PINK3,
        C_PLUM3,
        C_VIOLET,
        C_GOLD3A,
        C_LIGHT_GOLDEN_ROD3,
        C_TAN,
        C_MISTY_ROSE3,
        C_THISTLE3,
        C_PLUM2,
        C_YELLOW3A,
        C_KHAKI3,
        C_LIGHT_GOLDEN_ROD2,
        C_LIGHT_YELLOW3,
        C_GREY84,
        C_LIGHT_STEEL_BLUE1,
        C_YELLOW2,
        C_DARK_OLIVE_GREEN1,
        C_DARK_OLIVE_GREEN1A,
        C_DARK_SEA_GREEN1A,
        C_HONEYDEW2,
        C_LIGHT_CYAN1,
        C_RED1,
        C_DEEP_PINK2,
        C_DEEP_PINK1,
        C_DEEP_PINK1A,
        C_MAGENTA2A,
        C_MAGENTA1,
        C_ORANGE_RED1,
        C_INDIAN_RED1A,
        C_INDIAN_RED1B,
        C_HOT_PINK,
        C_HOT_PINK1,
        C_MEDIUM_ORCHID1A,
        C_DARK_ORANGE,
        C_SALMON1,
        C_LIGHT_CORAL,
        C_HOT_PINK31,
        C_ORCHID2,
        C_ORCHID1,
        C_ORANGE1,
        C_SANDY_BROWN,
        C_LIGHT_SALMON1,
        C_LIGHT_PINK1,
        C_PINK1,
        C_PLUM1,
        C_GOLD1,
        C_LIGHT_GOLDEN_ROD2B,
        C_LIGHT_GOLDEN_ROD2A,
        C_NAVAJO_WHITE1,
        C_MISTY_ROSE1,
        C_THISTLE1,
        C_YELLOW1,
        C_LIGHT_GOLDEN_ROD1,
        C_KHAKI1,
        C_WHEAT1,
        C_CORN_SILK1,
        C_GREY100,
        C_GREY3,
        C_GREY7,
        C_GREY11,
        C_GREY15,
        C_GREY19,
        C_GREY23,
        C_GREY27,
        C_GREY30,
        C_GREY35,
        C_GREY39,
        C_GREY42,
        C_GREY46,
        C_GREY50,
        C_GREY54,
        C_GREY58,
        C_GREY62,
        C_GREY66,
        C_GREY70,
        C_GREY74,
        C_GREY78,
        C_GREY82,
        C_GREY85,
        C_GREY89,
        C_GREY93
    ).sorted

    /*
     * Basic grays.
     */
    final val C_GRAY0 = CPColor("0x000000")
    final val C_GRAY1 = CPColor("0x111111")
    final val C_GRAY2 = CPColor("0x222222")
    final val C_GRAY3 = CPColor("0x333333")
    final val C_GRAY4 = CPColor("0x444444")
    final val C_GRAY5 = CPColor("0x555555")
    final val C_GRAY6 = CPColor("0x666666")
    final val C_GRAY7 = CPColor("0x777777")
    final val C_GRAY8 = CPColor("0x888888")
    final val C_GRAY9 = CPColor("0x999999")
    final val C_GRAY10 = CPColor("0xaaaaaa")
    final val C_GRAY11 = CPColor("0xbbbbbb")
    final val C_GRAY12 = CPColor("0xcccccc")
    final val C_GRAY13 = CPColor("0xdddddd")
    final val C_GRAY14 = CPColor("0xeeeeee")
    final val C_GRAY15 = CPColor("0xffffff")

    /*
     * X11 colors.
     */
    final val C_X11_GAINSBORO = CPColor(220, 220, 220)
    final val C_X11_LIGHT_GRAY = CPColor(211, 211, 211)
    final val C_X11_SILVER = CPColor(192, 192, 192)
    final val C_X11_DARK_GRAY = CPColor(169, 169, 169)
    final val C_X11_GRAY = CPColor(128, 128, 128)
    final val C_X11_DIM_GRAY = CPColor(105, 105, 105)
    final val C_X11_LIGHT_SLATE_GRAY = CPColor(119, 136, 153)
    final val C_X11_SLATE_GRAY = CPColor(112, 128, 144)
    final val C_X11_DARK_SLATE_GRAY = CPColor(47, 79, 79)
    final val C_X11_BLACK = CPColor(0, 0, 0)

    /**
      *
      */
    final val CS_X11_GRAYS = Seq(
        C_X11_GAINSBORO,
        C_X11_LIGHT_GRAY,
        C_X11_SILVER,
        C_X11_DARK_GRAY,
        C_X11_GRAY,
        C_X11_DIM_GRAY,
        C_X11_LIGHT_SLATE_GRAY,
        C_X11_SLATE_GRAY,
        C_X11_DARK_SLATE_GRAY,
        C_X11_BLACK
    ).sorted

    final val C_X11_CORN_SILK = CPColor(255, 248, 220)
    final val C_X11_BLANCHED_ALMOND = CPColor(255, 235, 205)
    final val C_X11_BISQUE = CPColor(255, 228, 196)
    final val C_X11_NAVAJO_WHITE = CPColor(255, 222, 173)
    final val C_X11_WHEAT = CPColor(245, 222, 179)
    final val C_X11_BURLY_WOOD = CPColor(222, 184, 135)
    final val C_X11_TAN = CPColor(210, 180, 140)
    final val C_X11_ROSY_BROWN = CPColor(188, 143, 143)
    final val C_X11_SANDY_BROWN = CPColor(244, 164, 96)
    final val C_X11_GOLDEN_ROD = CPColor(218, 165, 32)
    final val C_X11_PERU = CPColor(205, 133, 63)
    final val C_X11_CHOCOLATE = CPColor(210, 105, 30)
    final val C_X11_SADDLE_BROWN = CPColor(139, 69, 19)
    final val C_X11_SIENNA = CPColor(160, 82, 45)
    final val C_X11_BROWN = CPColor(165, 42, 42)
    final val C_X11_MAROON = CPColor(128, 0, 0)

    /**
      *
      */
    final val CS_X11_BROWNS = Seq(
        C_X11_CORN_SILK,
        C_X11_BLANCHED_ALMOND,
        C_X11_BISQUE,
        C_X11_NAVAJO_WHITE,
        C_X11_WHEAT,
        C_X11_BURLY_WOOD,
        C_X11_TAN,
        C_X11_ROSY_BROWN,
        C_X11_SANDY_BROWN,
        C_X11_GOLDEN_ROD,
        C_X11_PERU,
        C_X11_CHOCOLATE,
        C_X11_SADDLE_BROWN,
        C_X11_SIENNA,
        C_X11_BROWN,
        C_X11_MAROON
    ).sorted

    final val C_X11_WHITE = CPColor(255, 255, 255)
    final val C_X11_SNOW = CPColor(255, 250, 250)
    final val C_X11_HONEY_DEW = CPColor(240, 255, 240)
    final val C_X11_MINT_CREAM = CPColor(245, 255, 250)
    final val C_X11_AZURE = CPColor(240, 255, 255)
    final val C_X11_ALICE_BLUE = CPColor(240, 248, 255)
    final val C_X11_GHOST_WHITE = CPColor(248, 248, 255)
    final val C_X11_WHITE_SMOKE = CPColor(245, 245, 245)
    final val C_X11_SEA_SHELL = CPColor(255, 245, 238)
    final val C_X11_BEIGE = CPColor(245, 245, 220)
    final val C_X11_OLD_LACE = CPColor(253, 245, 230)
    final val C_X11_FLORAL_WHITE = CPColor(255, 250, 240)
    final val C_X11_IVORY = CPColor(255, 255, 240)
    final val C_X11_ANTIQUE_WHITE = CPColor(250, 235, 215)
    final val C_X11_LINEN = CPColor(250, 240, 230)
    final val C_X11_LAVENDER_BLUSH = CPColor(255, 240, 245)
    final val C_X11_MISTY_ROSE = CPColor(255, 228, 225)

    /**
      *
      */
    final val CS_X11_WHITES = Seq(
        C_X11_WHITE,
        C_X11_SNOW,
        C_X11_HONEY_DEW,
        C_X11_MINT_CREAM,
        C_X11_AZURE,
        C_X11_ALICE_BLUE,
        C_X11_GHOST_WHITE,
        C_X11_WHITE_SMOKE,
        C_X11_SEA_SHELL,
        C_X11_BEIGE,
        C_X11_OLD_LACE,
        C_X11_FLORAL_WHITE,
        C_X11_IVORY,
        C_X11_ANTIQUE_WHITE,
        C_X11_LINEN,
        C_X11_LAVENDER_BLUSH,
        C_X11_MISTY_ROSE
    ).sorted

    final val C_X11_PINK = CPColor(255, 192, 203)
    final val C_X11_LIGHT_PINK = CPColor(255, 182, 193)
    final val C_X11_HOT_PINK = CPColor(255, 105, 180)
    final val C_X11_DEEP_PINK = CPColor(255, 20, 147)
    final val C_X11_PALE_VIOLET_RED = CPColor(219, 112, 147)
    final val C_X11_MEDIUM_VIOLET_RED = CPColor(199, 21, 133)

    /**
      *
      */
    final val CS_X11_PINKS = Seq(
         C_X11_PINK,
         C_X11_LIGHT_PINK,
         C_X11_HOT_PINK,
         C_X11_DEEP_PINK,
         C_X11_PALE_VIOLET_RED,
         C_X11_MEDIUM_VIOLET_RED
    ).sorted

    final val C_X11_LAVENDER = CPColor(230, 230, 250)
    final val C_X11_THISTLE = CPColor(216, 191, 216)
    final val C_X11_PLUM = CPColor(221, 160, 221)
    final val C_X11_VIOLET = CPColor(238, 130, 238)
    final val C_X11_ORCHID = CPColor(218, 112, 214)
    final val C_X11_FUCHSIA = CPColor(255, 0, 255)
    final val C_X11_MAGENTA = CPColor(255, 0, 255)
    final val C_X11_MEDIUM_ORCHID = CPColor(186, 85, 211)
    final val C_X11_MEDIUM_PURPLE = CPColor(147, 112, 219)
    final val C_X11_BLUE_VIOLET = CPColor(138, 43, 226)
    final val C_X11_DARK_VIOLET = CPColor(148, 0, 211)
    final val C_X11_DARK_ORCHID = CPColor(153, 50, 204)
    final val C_X11_DARK_MAGENTA = CPColor(139, 0, 139)
    final val C_X11_PURPLE = CPColor(128, 0, 128)
    final val C_X11_INDIGO = CPColor(75, 0, 130)

    /**
      *
      */
    final val CS_X11_PURPLES = Seq(
         C_X11_LAVENDER,
         C_X11_THISTLE,
         C_X11_PLUM,
         C_X11_VIOLET,
         C_X11_ORCHID,
         C_X11_FUCHSIA,
         C_X11_MAGENTA,
         C_X11_MEDIUM_ORCHID,
         C_X11_MEDIUM_PURPLE,
         C_X11_BLUE_VIOLET,
         C_X11_DARK_VIOLET,
         C_X11_DARK_ORCHID,
         C_X11_DARK_MAGENTA,
         C_X11_PURPLE,
         C_X11_INDIGO
    ).sorted

    final val C_X11_POWDER_BLUE = CPColor(176, 224, 230)
    final val C_X11_LIGHT_BLUE = CPColor(173, 216, 230)
    final val C_X11_LIGHT_SKY_BLUE = CPColor(135, 206, 250)
    final val C_X11_SKY_BLUE = CPColor(135, 206, 235)
    final val C_X11_DEEP_SKY_BLUE = CPColor(0, 191, 255)
    final val C_X11_LIGHT_STEEL_BLUE = CPColor(176, 196, 222)
    final val C_X11_DODGER_BLUE = CPColor(30, 144, 255)
    final val C_X11_CORN_FLOWER_BLUE = CPColor(100, 149, 237)
    final val C_X11_STEEL_BLUE = CPColor(70, 130, 180)
    final val C_X11_ROYAL_BLUE = CPColor(65, 105, 225)
    final val C_X11_BLUE = CPColor(0, 0, 255)
    final val C_X11_MEDIUM_BLUE = CPColor(0, 0, 205)
    final val C_X11_DARK_BLUE = CPColor(0, 0, 139)
    final val C_X11_NAVY = CPColor(0, 0, 128)
    final val C_X11_MIDNIGHT_BLUE = CPColor(25, 25, 112)
    final val C_X11_MEDIUM_SLATE_BLUE = CPColor(123, 104, 238)
    final val C_X11_SLATE_BLUE = CPColor(106, 90, 205)
    final val C_X11_DARK_SLATE_BLUE = CPColor(72, 61, 139)

    /**
      *
      */
    final val CS_X11_BLUES = Seq(
        C_X11_POWDER_BLUE,
        C_X11_LIGHT_BLUE,
        C_X11_LIGHT_SKY_BLUE,
        C_X11_SKY_BLUE,
        C_X11_DEEP_SKY_BLUE,
        C_X11_LIGHT_STEEL_BLUE,
        C_X11_DODGER_BLUE,
        C_X11_CORN_FLOWER_BLUE,
        C_X11_STEEL_BLUE,
        C_X11_ROYAL_BLUE,
        C_X11_BLUE,
        C_X11_MEDIUM_BLUE,
        C_X11_DARK_BLUE,
        C_X11_NAVY,
        C_X11_MIDNIGHT_BLUE,
        C_X11_MEDIUM_SLATE_BLUE,
        C_X11_SLATE_BLUE,
        C_X11_DARK_SLATE_BLUE
    ).sorted

    final val C_X11_LIGHT_CYAN = CPColor(224, 255, 255)
    final val C_X11_CYAN = CPColor(0, 255, 255)
    final val C_X11_AQUA = CPColor(0, 255, 255)
    final val C_X11_AQUAMARINE = CPColor(127, 255, 212)
    final val C_X11_MEDIUM_AQUAMARINE = CPColor(102, 205, 170)
    final val C_X11_PALE_TURQUOISE = CPColor(175, 238, 238)
    final val C_X11_TURQUOISE = CPColor(64, 224, 208)
    final val C_X11_MEDIUM_TURQUOISE = CPColor(72, 209, 204)
    final val C_X11_DARK_TURQUOISE = CPColor(0, 206, 209)
    final val C_X11_LIGHT_SEA_GREEN = CPColor(32, 178, 170)
    final val C_X11_CADET_BLUE = CPColor(95, 158, 160)
    final val C_X11_DARK_CYAN = CPColor(0, 139, 139)
    final val C_X11_TEAL = CPColor(0, 128, 128)

    /**
      *
      */
    final val CS_X11_CYANS = Seq(
        C_X11_LIGHT_CYAN,
        C_X11_CYAN,
        C_X11_AQUA,
        C_X11_AQUAMARINE,
        C_X11_MEDIUM_AQUAMARINE,
        C_X11_PALE_TURQUOISE,
        C_X11_TURQUOISE,
        C_X11_MEDIUM_TURQUOISE,
        C_X11_DARK_TURQUOISE,
        C_X11_LIGHT_SEA_GREEN,
        C_X11_CADET_BLUE,
        C_X11_DARK_CYAN,
        C_X11_TEAL
    ).sorted

    final val C_X11_LAWN_GREEN = CPColor(124, 252, 0)
    final val C_X11_CHARTREUSE = CPColor(127, 255, 0)
    final val C_X11_LIME_GREEN = CPColor(50, 205, 50)
    final val C_X11_LIME = CPColor(0, 255, 0)
    final val C_X11_FOREST_GREEN = CPColor(34, 139, 34)
    final val C_X11_GREEN = CPColor(0, 128, 0)
    final val C_X11_DARK_GREEN = CPColor(0, 100, 0)
    final val C_X11_GREEN_YELLOW = CPColor(173, 255, 47)
    final val C_X11_YELLOW_GREEN = CPColor(154, 205, 50)
    final val C_X11_SPRING_GREEN = CPColor(0, 255, 127)
    final val C_X11_MEDIUM_SPRING_GREEN = CPColor(0, 250, 154)
    final val C_X11_LIGHT_GREEN = CPColor(144, 238, 144)
    final val C_X11_PALE_GREEN = CPColor(152, 251, 152)
    final val C_X11_DARK_SEA_GREEN = CPColor(143, 188, 143)
    final val C_X11_MEDIUM_SEA_GREEN = CPColor(60, 179, 113)
    final val C_X11_SEA_GREEN = CPColor(46, 139, 87)
    final val C_X11_OLIVE = CPColor(128, 128, 0)
    final val C_X11_DARK_OLIVE_GREEN = CPColor(85, 107, 47)
    final val C_X11_OLIVE_DRAB = CPColor(107, 142, 35)

    /**
      *
      */
    final val CS_X11_GREENS = Seq(
        C_X11_LAWN_GREEN,
        C_X11_CHARTREUSE,
        C_X11_LIME_GREEN,
        C_X11_LIME,
        C_X11_FOREST_GREEN,
        C_X11_GREEN,
        C_X11_DARK_GREEN,
        C_X11_GREEN_YELLOW,
        C_X11_YELLOW_GREEN,
        C_X11_SPRING_GREEN,
        C_X11_MEDIUM_SPRING_GREEN,
        C_X11_LIGHT_GREEN,
        C_X11_PALE_GREEN,
        C_X11_DARK_SEA_GREEN,
        C_X11_MEDIUM_SEA_GREEN,
        C_X11_SEA_GREEN,
        C_X11_OLIVE,
        C_X11_DARK_OLIVE_GREEN,
        C_X11_OLIVE_DRAB
    ).sorted

    final val C_X11_LIGHT_YELLOW = CPColor(255, 255, 224)
    final val C_X11_LEMON_CHIFFON = CPColor(255, 250, 205)
    final val C_X11_LIGHT_GOLDEN_ROD_YELLOW = CPColor(250, 250, 210)
    final val C_X11_PAPAYA_WHIP = CPColor(255, 239, 213)
    final val C_X11_MOCCASIN = CPColor(255, 228, 181)
    final val C_X11_PEACH_PUFF = CPColor(255, 218, 185)
    final val C_X11_PALE_GOLDEN_ROD = CPColor(238, 232, 170)
    final val C_X11_KHAKI = CPColor(240, 230, 140)
    final val C_X11_DARK_KHAKI = CPColor(189, 183, 107)
    final val C_X11_YELLOW = CPColor(255, 255, 0)

    /**
      *
      */
    final val CS_X11_YELLOWS = Seq(
        C_X11_LIGHT_YELLOW,
        C_X11_LEMON_CHIFFON,
        C_X11_LIGHT_GOLDEN_ROD_YELLOW,
        C_X11_PAPAYA_WHIP,
        C_X11_MOCCASIN,
        C_X11_PEACH_PUFF,
        C_X11_PALE_GOLDEN_ROD,
        C_X11_KHAKI,
        C_X11_DARK_KHAKI,
        C_X11_YELLOW
    ).sorted

    /**
      *
      */
    final val CS_X11_ALL =
        CS_X11_WHITES ++
        CS_X11_CYANS ++
        CS_X11_ORANGES ++
        CS_X11_YELLOWS ++
        CS_X11_REDS ++
        CS_X11_PURPLES ++
        CS_X11_GREENS ++
        CS_X11_BROWNS ++
        CS_X11_BLUES ++
        CS_X11_PINKS ++
        CS_X11_GRAYS

    /**
      *
      */
    final val C_X11_GROUPS = Seq(
        CS_X11_REDS,
        CS_X11_GRAYS,
        CS_X11_PINKS,
        CS_X11_CYANS,
        CS_X11_BLUES,
        CS_X11_BROWNS,
        CS_X11_GREENS,
        CS_X11_ORANGES,
        CS_X11_PURPLES,
        CS_X11_WHITES,
        CS_X11_YELLOWS
    )

    /**
      *
      */
    final val C_SYS_GROUP = Seq(
        C_BLACK,
        C_MAROON,
        C_GREEN,
        C_OLIVE,
        C_NAVY,
        C_PURPLE,
        C_TEAL,
        C_SILVER,
        C_GREY,
        C_RED,
        C_LIME,
        C_YELLOW,
        C_BLUE,
        C_FUCHSIA,
        C_AQUA,
        C_WHITE
    )



