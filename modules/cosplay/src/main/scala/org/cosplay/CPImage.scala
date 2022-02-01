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

import CPKeyboardKey.*
import CPImage.*
import CPColor.*
import impl.CPUtils
import CPPixel.*
import org.cosplay.prefabs.images.*

import java.io.{File, PrintStream}
import java.nio.{ByteBuffer, ByteOrder}
import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer
import scala.util.Using

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
  * Defines rectangular shape and its content.
  *
  * Image is one of the key types in CosPlay. Almost everything that is drawn on the screen is represented
  * by an image: FIGLet font string, sprites, video and animation frames, etc. Image itself is just a
  * container for [[CPPixel pixels]]. It can be created in-code or loaded and saved from and to the external file
  * or resource. To render the image on the screen you can use [[CPCanvas.drawImage()]] method.
  *
  * Image is an asset. Just like other assets such as [[CPFont fonts]], [[CPSound sounds]], [[CPAnimation animations]] or
  * [[CPVideo videos]] they are not managed or governed by the CosPlay game engine unlike [[CPSceneObject scene objects]]
  * that are managed and governed by the game engine. Assets are typically created outside of the game engine and
  * managed by the developer, they can be freely shared between scenes or scene objects as any other standard
  * Scala objects.
  *
  * ### In-Code images
  * One of the convenient features of CosPlay is that images can be created & painted (a.k.a. skinned) right
  * in code with very minimal gestalt. The easiest way is to use [[CPArrayImage]] class that provides utility
  * methods to convert a margin-based Scala string into an image. For [[CPAlienImage example]]:
  * {{{
  * import org.cosplay.*
  * import CPColor.*
  * import CPArrayImage.*
  * import CPPixel.*
  *
  * object CPAlienImage extends CPArrayImage(
  *     prepSeq("""
  *         |    .  .
  *         |     \/
  *         |    (@@)
  *         | g/\_)(_/\e
  *         |g/\(=--=)/\e
  *         |    //\\
  *         |   _|  |_
  *       """),
  *     (ch, _, _) => ch&C_LIME
  * )
  * }}}
  * Another [[CPAmigaImage example]] with more sophisticated skinning:
  * {{{
  * import org.cosplay.*
  * import CPColor.*
  * import CPArrayImage.*
  * import CPPixel.*
  *
  * object CPAmigaImage extends CPArrayImage(
  *     prepSeq("""
  *       |  .---------.
  *       |  |.-------.|
  *       |  ||>run#xx||
  *       |  ||xxxxxxx||
  *       |  |"-------'|
  *       |.-^---------^-.
  *       ||x---~xxxAMiGA|
  *       |"-------------'
  *     """),
  *     (ch, _, _) => ch match
  *         case ' ' => XRAY
  *         case c @ ('A' | 'M' | 'i' | 'G') => CPPixel(c, C_NAVY, C_WHITE)
  *         case c @ ('r' | 'u' | 'n' | '#') => c&C_GREEN_YELLOW
  *         case 'x' => CPPixel(' ', C_BLACK)
  *         case '>' => '>'&C_GREEN_YELLOW
  *         case '~' => '~'&C_ORANGE_RED
  *         case c => c&C_WHITE
  * )
  * }}}
  *
  * ### Loading images
  * You can load images from the external source like URL or file path in one of the following formats:
  *  - `*.xp` [[https://www.gridsagegames.com/rexpaint/ REXPaint XP]] format. This is a native binary format supported by
  *    [[https://www.gridsagegames.com/rexpaint/ REXPaint]] ASCII editor. This format supports full color information.
  *  - `*.csv` [[https://www.gridsagegames.com/rexpaint/ REXPaint CSV]] format. This is the interchangeable format
  *    that is natively supported by [[https://www.gridsagegames.com/rexpaint/ REXPaint]] ASCII editor and also
  *    supported by CosPlay to save an image with. This format supports full color information.
  *  - `*.txt` format. Image in this format is a simple *.txt file and it does not provide or store any color
  *     information.
  *
  *  Use one of the following methods from the companion object to load the image from file path, resource folder
  *  or URL:
  *   - [[load()]]
  *   - [[loadRexCsv()]]
  *   - [[loadRexXp()]]
  *   - [[loadTxt()]]
  *
  * For example:
  * {{{
  * val speckImg = CPImage.load(
  *     "prefab/images/speck.xp",
  *     (px, _, _) => px.withBg(None) // Make background transparent.
  * )
  * }}}
  *
  * ### Saving images
  * You can save image to the local file path in the following format:
  *  - `*.csv` [[https://www.gridsagegames.com/rexpaint/ REXPaint CSV]] format. This is the interchangeable format
  *    that is natively supported by [[https://www.gridsagegames.com/rexpaint/ REXPaint]] ASCII editor and also
  *    supported by CosPlay to save an image with. This format supports full color information.
  *
  *  Use one of the following methods from the companion object to save the image to the file path:
  *   - [[save() save(...)]]
  *
  * ### ASCII art online
  * There's a vast collection of existing ASCII art imagery online. Here's some of the main resources where
  * you can find it:
  *  - http://www.asciiworld.com/
  *  - https://www.asciiart.eu/
  *  - http://www.ascii-art.de/
  *  - https://asciiart.website
  *  - http://www.afn.org/~afn39695/collect.htm
  *  - https://www.incredibleart.org/links/ascii.html
  *  - http://blocktronics.org/
  *  - http://ansiart.com/
  *  - https://llizardakaejm.wordpress.com/ascii-animations/
  *
  * ### Prefabs
  * CosPlay comes with a list of prefab image, animations and video clips. All of them are shipped with CosPlay
  * and can be found in `org.cosplay.prefabs` package an its sub-packages.
  *
  * ### ASCII art editors
  * [[https://www.gridsagegames.com/rexpaint REXPaint]] ASCII editor is an excellent free editor for ASCII art
  * from the creator of the [[https://www.gridsagegames.com/cogmind/ Cogmind]] game.
  * [[https://www.gridsagegames.com/rexpaint REXPaint]] editor is highly recommended for images with non-trivial
  * coloring.
  *
  * @param origin The origin of the image like file path or URL.
  * @example See [[org.cosplay.examples.image.CPImageCarouselExample CPImageCarouselExample]] class for the example of
  *     using images.
  * @example See [[org.cosplay.examples.image.CPImageFormatsExample CPImageFormatsExample]] class for the example of
  *     using images.
  */
abstract class CPImage(origin: String) extends CPGameObject with CPAsset:
    override val toString: String = s"Image [dim=$getDim, origin=$origin]"
    
    /** @inheritdoc */
    override val getOrigin: String = origin

    /**
      * Saves this image in [[https://www.gridsagegames.com/rexpaint/ REXPaint CSV]] format.
      *
      * @param path Local file path.
      * @param bg Background pixel to replace in pixels with no background.
      * @see https://www.gridsagegames.com/rexpaint
      */
    def save(path: String, bg: CPColor): Unit =
        save(new File(path), bg)

    /**
      * Saves this image in [[https://www.gridsagegames.com/rexpaint/ REXPaint CSV]] format.
      *
      * @param file File instance.
      * @param bg Background pixel to replace in pixels with no background.
      * @see https://www.gridsagegames.com/rexpaint
      */
    def save(file: File, bg: CPColor): Unit =
        Using.resource(new PrintStream(file)) { ps =>
            val dim = getDim
            ps.println(s"CosPlay image [${dim.width}x${dim.height}, origin=$origin, bg=${bg.hex}]")
            loop((px, x, y) => ps.println(s"$x,$y,${px.char.toInt},${px.fg.hex},${px.bg.getOrElse(bg).hex}"))
        }

    /**
      * Splits this image into sequence of `[w,h]` images.
      *
      * @param w Split width.
      * @param h Split height.
      */
    def split(w: Int, h: Int): Seq[CPImage] =
        toArray2D.split(w, h).zipWithIndex.map((rect, _) => new CPArrayImage(rect))

    /**
      * Creates shallow copy of this image with given skin function. Note that this method
      * will wrap this image and delegate to it instead of creating a full copy of the image.
      * To make a deep copy use [[copy()]] method.
      *
      * @param f Skinning function. The function takes an existing pixel, its X and Y coordinate and
      *     return a new pixel.
      * @see [[copy()]]
      */
    def skin(f: (CPPixel, Int, Int) => CPPixel): CPImage =
        val it = this

        new CPImage(it.getOrigin):
            def getDim: CPDim = it.getDim
            def getPixel(x: Int, y: Int): CPPixel = f(it.getPixel(x, y), x, y)

    /**
      * Resizes and centers the image returning a new image instance. Given dimension can be bigger
      * or smaller.
      *
      * @param newDim Dimension ot reize by.
      * @param bgPx Backgound pixel in case of bigger dimension. Default value is [[CPPixel.XRAY]].
      * @see [[resizeByInsets()]]
      */
    def resizeByDim(newDim: CPDim, bgPx: CPPixel = CPPixel.XRAY): CPImage =
        val dim = getDim
        resizeByInsets(new CPInsets((newDim.width - dim.width) / 2, (newDim.height - dim.height) / 2))

    /**
      * Resizes this image using given insets. Insets can be positive or negative.
      *
      * @param insets Insets to resize by.
      * @param bgPx Backgound pixel in case of bigger dimension. Default value is [[CPPixel.XRAY]].
      * @see [[resizeByDim()]]
      */
    def resizeByInsets(insets: CPInsets, bgPx: CPPixel = CPPixel.XRAY): CPImage =
        val dim = getDim
        val w = dim.width + insets.horOffset
        val h = dim.height + insets.verOffset
        val data = new CPArray2D[CPPixel](w, h, bgPx)

        loop((px, x, y) => {
            val x2 = x + insets.left
            val y2 = y + insets.top

            if data.isValid(x2, y2) then data.set(x2, y2, px)
        })

        CPArrayImage(data, origin)

    /**
      * Converts this image into 2D array of pixels.
      */
    def toArray2D: CPArray2D[CPPixel] =
        val data = new CPArray2D[CPPixel](getDim)
        data.rect.loop((x, y) => data.set(x, y, getPixel(x, y)))
        data

    /**
      * Tests whether this and given images have the same dimensions and the same pixels in corresponding
      * locations.
      *
      * @param img Image to test.
      */
    def isSameAs(img: CPImage): Boolean =
        getDim == img.getDim && exists((px, x, y) => px == img.getPixel(x, y))

    /**
      * Antialiases solid ASCII art image returning new image. Works `only` for solid ASCII art.
      * Algorithm is loosely based on https://codegolf.stackexchange.com/questions/5450/anti-aliasing-ascii-art.
      *
      * @param isBlank Function to determine if given pixel is blank for the purpose of antialiasing.
      * @see https://codegolf.stackexchange.com/questions/5450/anti-aliasing-ascii-art
      */
    def antialias(isBlank: CPPixel => Boolean): CPImage =
        val data = new CPArray2D[CPPixel](getDim)
        val dim = getDim
        data.rect.loop((x, y) => {
            val px = getPixel(x, y)
            // Based on: https://codegolf.stackexchange.com/questions/5450/anti-aliasing-ascii-art
            if !isBlank(px) then
                val top = y > 0 && !isBlank(getPixel(x, y - 1))
                val left = x > 0 && !isBlank(getPixel(x - 1, y))
                val bottom = y < dim.height - 1 && !isBlank(getPixel(x, y + 1))
                val right = x < dim.width - 1 && !isBlank(getPixel(x + 1, y))

                data.set(x, y, px.withChar(CPUtils.aaChar(px.char, top, left, bottom, right)))
            else
                data.set(x, y, px)
        })
        new CPArrayImage(data, origin)

    /**
      * Trims blank pixels from this image returning a new, trimmed image.
      *
      * @param isBlank Function to determine is pixel is blank.
      */
    def trim(isBlank: CPPixel => Boolean): CPImage = new CPArrayImage(toArray2D.trim(isBlank), origin)

    /**
      * Loops over the pixels in image.
      *
      * @param f Function to call on each pixel. Note that unlike standard [[foreach()]] funciton, this
      *     function also takes pixel's XY-coordinate.
      */
    def loop(f: (CPPixel, Int, Int) => Unit): Unit = getRect.loop((x, y) => f(getPixel(x, y), x, y))

    /**
      * Tests if there is a pixel in this image for which given predicate would return `true`.
      *
      * @param f Predicate to test.
      */
    def exists(f: (CPPixel, Int, Int) => Boolean): Boolean = getRect.exists((x, y) => f(getPixel(x, y), x, y))

    /**
      * Loops over the pixels in image.
      *
      * @param f Function to call on each pixel.
      */
    def foreach(f: CPPixel => Unit): Unit = loop((px, _, _) => f(px))

    /**
      * Detects all background pixels and replaces them with a given pixel returning new image.
      *
      * A pixel is considered to be a background pixel when:
      *  - It's [[CPPixel.char characater]] is space (' ').
      *  - It's not equal to given `bgPx`.
      *  - It's on the edge of the image or there's a path from it to the edge of the image through background
      *    pixels only.
      *
      *  For example, a fully enclosed area of the image that contains spaces will no be considered a background
      *  as there's no path to the edge of the image without crossing a non-background image.
      *
      * @param bgPx Pixel to replace the detected background pixels.
      */
    def replaceBg(bgPx: CPPixel): CPImage =
        import CPPixel.*

        val rect = getRect
        val xMax = rect.xMax
        val yMax = rect.yMax
        val arr = toArray2D
        var ok = true

        // NOTE: implementation can be improved performance wise.
        while ok do
            ok = false
            arr.rect.loop((x, y) =>
                val px = arr.get(x, y)
                // Space at the edge of the image is always considered background.
                if px != bgPx && px.char == ' ' &&
                    (
                        x == 0 ||
                        y == 0 ||
                        x == xMax ||
                        y == yMax ||
                        arr.get(x + 1, y) == bgPx ||
                        arr.get(x - 1, y) == bgPx ||
                        arr.get(x, y + 1) == bgPx ||
                        arr.get(x, y - 1) == bgPx
                    ) then
                        arr.set(x, y, bgPx)
                        ok = true
            )

        new CPArrayImage(arr, origin)

    /**
      * Detects all background pixels and replaces them with [[CPPixel.XRAY]] returning a new image.
      * This effectively makes the background transparent.
      */
    def trimBg(): CPImage = replaceBg(CPPixel.XRAY)

    /**
      * Makes a deep snapshot copy of the current image.
      */
    def copy(): CPImage = new CPArrayImage(toArray2D, origin)

    /**
      * Makes a deep snapshot copy of the subregion of the current image.
      *
      * @param rect Subregion to copy.
      */
    def copy(rect: CPRect): CPImage = new CPArrayImage(toArray2D.extract(rect), origin)

    /**
      * Attaches given image underneath this image returning a new combined image.
      *
      * @param img Image to attached.
      * @param bgPx Background pixel to use when combines image has larged width.
      */
    def stitchBelow(img: CPImage, bgPx: CPPixel = CPPixel.XRAY): CPImage =
        val w = getWidth.max(img.getWidth)
        val h = getHeight + img.getHeight
        val canv = CPCanvas(w, h, bgPx)
        canv.drawImage(this, 0, 0, 0)
        canv.drawImage(img, 0, getHeight, 0)
        canv.capture()

    /**
      * Attaches given image to the right of this image returning a new combined image.
      *
      * @param img Image to attached.
      * @param bgPx Background pixel to use when combines image has larged height.
      */
    def stitchRight(img: CPImage, bgPx: CPPixel = CPPixel.XRAY): CPImage =
        val w = getWidth + img.getWidth
        val h = getHeight.max(img.getHeight)
        val canv = CPCanvas(w, h, bgPx)
        canv.drawImage(this, 0, 0, 0)
        canv.drawImage(img, getWidth, 0, 0)
        canv.capture()

    /**
      * Flips this image horizontally returning a new image. Note that this method does only
      * shallow copy and returned image will delegate to this image. Use [[copy()]] method to
      * get a full deep copy before calling this method to get a full new image.
      *
      * @see [[copy()]]
      */
    def horFlip(): CPImage =
        val it = this

        if getDim.isOne then
            this
        else
            val xMax = getRect.xMax
            new CPImage(it.getOrigin):
                override def getDim: CPDim = it.getDim
                override def getPixel(x: Int, y: Int): CPPixel = horPixelFlip(it.getPixel(xMax - x, y))

    /**
      * Flips this image vertically returning a new image. Note that this method does only
      * shallow copy and returned image will delegate to this image. Use [[copy()]] method to
      * get a full deep copy before calling this method to get a full new image.
      *
      * @see [[copy()]]
      */
    def verFlip(): CPImage =
        val it = this

        if getDim.isOne then
            this
        else
            val yMax = getRect.yMax
            new CPImage(it.getOrigin):
                override def getDim: CPDim = it.getDim
                override def getPixel(x: Int, y: Int): CPPixel = verPixelFlip(it.getPixel(x, yMax - y))

    /**
      *
      * @param px
      * @param map
      */
    private def flipPixel(px: CPPixel, map: Seq[(Char, Char)]): CPPixel =
        map.find(px.char == _._1) match
            case Some(t) => px.withChar(t._2)
            case None => map.find(px.char == _._2) match
                case Some(t) => px.withChar(t._1)
                case None => px

    /**
      *
      * @param px
      */
    private def horPixelFlip(px: CPPixel): CPPixel = flipPixel(px, HOR_FLIP_MAP)

    /**
      *
      * @param px
      */
    private def verPixelFlip(px: CPPixel): CPPixel = flipPixel(px, VER_FLIP_MAP)

    /**
      * Gets rectangle shape of this image.
      */
    def getRect: CPRect = new CPRect(getDim)

    /**
      * Gets pixel at the given XY-coordinate.
      *
      * @param x X-coordinate of the pixel.
      * @param y Y-coordinate of the pixel.
      */
    def getPixel(x: Int, y: Int): CPPixel

    /**
      * Gets image dimension.
      */
    def getDim: CPDim

    /**
      * Shortcut to get image width.
      */
    def getWidth: Int = getDim.width

    /**
      * Shortcut to get image height.
      */
    def getHeight: Int = getDim.height

/**
  * Companion object with utility functions.
  */
object CPImage:
    /** */
    val NL: String = System.getProperty("line.separator")

    // First search for '_1' then, if not found, search for '_2'.
    private final val HOR_FLIP_MAP = Seq(
        '{' -> '}',
        '/' -> '\\',
        '<' -> '>',
        '(' -> ')',
        '[' -> ']',
        '`' -> '\'',
        '\'' -> '\'', // Don't replace.
        '{' -> '}',
        'e' -> 'g',
        'J' -> 'L',
        'q' -> 'p',
        'd' -> 'b'
    )

    // First search for '_1' then, if not found, search for '_2'.
    private final val VER_FLIP_MAP = Seq(
        '-' -> '-', // Don't replace.
        '_' -> '-',
        '`' -> '.',
        '"' -> ',',
        ',' -> '\'',
        '\'' -> ',',
        '/' -> '\\',
        'a' -> 'e',
        'b' -> 'p',
        'V' -> '^',
        'M' -> 'W',
        '9' -> '6',
        'P' -> 'd',
        'z' -> 's'
    )

    private final val DFLT_BG = CPPixel('.', C_GRAY2, C_GRAY1)

    /**
      * Loads image using [[https://www.gridsagegames.com/rexpaint/ REXPaint CSV]] format.
      *
      * @param src Local filesystem path, resources file or URL.
      * @param skin Skinning function. The function takes an existing pixel, its X and Y coordinate and
      *     return a new pixel. Default value is a function that return the same pixel.
      */
    def loadRexCsv(src: String, skin: (CPPixel, Int, Int) => CPPixel = (px, _, _) => px): CPImage =
        val arr = CPArray2D(
            CPUtils.readAllStrings(src).tail.zipWithIndex.map((line, i) => {
                val idx = i + 1
                val parts = line.split(",").map(_.strip)
                if parts.length != 5 then E(s"Invalid CSV file format at line $idx: $src")
                try
                    val x = Integer.decode(parts(0))
                    val y = Integer.decode(parts(1))
                    val ch = Integer.decode(parts(2)).toChar
                    val fg = CPColor(Integer.decode(parts(3)))
                    val bg = CPColor(Integer.decode(parts(4)))

                    CPPosPixel(CPPixel(ch, fg, Some(bg)), x, y)
                catch
                    case e: Exception => E(s"Invalid CSV file format at line $idx: $src", e)
            })
        )
        new CPArrayImage(arr.map(skin))

    /**
      * Loads image using [[https://www.gridsagegames.com/rexpaint/ REXPaint XP]] format.
      *
      * @param src Local filesystem path, resources file or URL.
      * @param skin Skinning function. The function takes an existing pixel, its X and Y coordinate and
      *     return a new pixel. Default value is the function that returns the same pixel.
      * @note Implementation is based on https://github.com/biscon/xpreader/blob/master/src/REXReader.java
      */
    def loadRexXp(src: String, skin: (CPPixel, Int, Int) => CPPixel = (px, _, _) => px): CPImage =
        val bb = ByteBuffer.wrap(CPUtils.unzipBytes(CPUtils.readAllBytes(src)))
        bb.order(ByteOrder.LITTLE_ENDIAN)
        bb.getInt // Version (skip).
        val layerCnt = bb.getInt
        if layerCnt <= 0 then E(s"Image file is empty: $src")
        val layers = ArrayBuffer.empty[CPArray2D[CPPixel]]
        for (_ <- 0 until layerCnt)
            val w = bb.getInt
            val h = bb.getInt
            val layer = new CPArray2D[CPPixel](w, h, CPPixel.XRAY)
            var idx = 0
            val len = w * h
            while (idx < len)
                val x = idx / h
                val y = idx % h
                val ch = bb.getInt.toChar
                val fgR = unsigned(bb.get)
                val fgG = unsigned(bb.get)
                val fgB = unsigned(bb.get)
                val bgR = unsigned(bb.get)
                val bgG = unsigned(bb.get)
                val bgB= unsigned(bb.get)
                val fg = CPColor(fgR, fgG, fgB)
                val bg = if bgR == 255 && bgG == 0 && bgB == 255 then None else Some(CPColor(bgR, bgG, bgB)) // Transparency background.
                val px = if ch == ' ' then CPPixel.XRAY else CPPixel(ch, fg, bg)
                layer.set(x, y, px)
                idx += 1
            layers += layer
        val w = layers.head.width
        val h = layers.head.height
        val data = new CPArray2D[CPPixel](w, h, CPPixel.XRAY)
        for (layer <- layers)
            layer.map((px, x, y) => if !px.isXray then data.set(x, y, px))
        new CPArrayImage(data.map(skin))

    /**
      *
      * @param b
      */
    private def unsigned(b: Byte): Int = b.toInt & 0xFF

    /**
      * Loads image using `*.txt` format.
      *
      * @param src Local filesystem path, resources file or URL.
      * @param skin Skinning function. The function takes an existing pixel, its X and Y coordinate and
      *     return a new pixel. Default value is the function that returns the same pixel.
      */
    def loadTxt(src: String, skin: (CPPixel, Int, Int) => CPPixel = (px, _, _) => px): CPImage =
        new CPArrayImage(CPArray2D(CPUtils.readAllStrings(src)).map(_&C_WHITE).map(skin))

    /**
      * Loads image auto-detecting its format based on the file path extension. The following extensions are
      * recognized:
      *  - `*.csv` will use [[https://www.gridsagegames.com/rexpaint/ REXPaint CSV]] format.
      *  - `*.xp` will use [[https://www.gridsagegames.com/rexpaint/ REXPaint XP]] format.
      *  - `*.csv` will use text format.
      *
      *
      * @param path Local filesystem file path.
      * @param skin Skinning function. The function takes an existing pixel, its X and Y coordinate and
      *     return a new pixel. Default value is the function that returns the same pixel.
      */
    def load(path: String, skin: (CPPixel, Int, Int) => CPPixel = (px, _, _) => px): CPImage =
        if path.endsWith(".csv") then loadRexCsv(path, skin)
        else if path.endsWith(".xp") then loadRexXp(path, skin)
        else if path.endsWith(".txt") then loadTxt(path, skin)
        else E(s"Unsupported file image format (must be .csv, .txt or .xp): $path")

    /**
      * Previews given sequence of image as animation.
      *
      * @param imgs Sequence of images to be used as key frames for the animation.
      * @param fps How many frame per second to show.
      * @param emuTerm Whether or not to use terminal emulation. Default value is `true`.
      * @param bg Optional background pixel for the terminal. Default value is {{{CPPixel('.', C_GRAY2, C_GRAY1)}}}.
      */
    def previewAnimation(imgs: Seq[CPImage], fps: Int = 5, emuTerm: Boolean = true, bg: CPPixel = DFLT_BG): Unit =
        require(fps < 1_000)
        val frameDim = imgs.head.getDim
        require(imgs.forall(_.getDim == frameDim), "All images must be of the same dimension.")
        val w = frameDim.width
        val h = frameDim.height
        val dim = CPDim(w + 8, h + 8)
        CPEngine.init(
            CPGameInfo(
                name = s"Animation Preview (${w}x$h)",
                devName = "(C) 2021 Rowan Games, Inc.",
                initDim = Some(dim),
                termBg = bg.bg.getOrElse(CPColor.C_DFLT_BG)
            ),
            emuTerm = emuTerm
        )
        try
            val ani = CPAnimation.filmStrip("ani", 1_000 / fps, true, false, imgs)
            val spr = CPAnimationSprite("spr", Seq(ani), 4, 4, 0, "ani")
            CPEngine.rootLog().info(s"Animation preview [" +
                s"frames=${imgs.size}, " +
                s"frameDim=$frameDim, " +
                s"]")
            CPEngine.startGame(new CPScene(
                "scene",
                Some(dim),
                bg,
                spr, // Animation we are previewing.
                CPUtils.makeExitGameOnLoQ()
            ))
        finally
            CPEngine.dispose()

    /**
      * Previews given image.
      *
      * @param img Image to preview.
      * @param bg Optional background pixel for the terminal. Default value is {{{CPPixel('.', C_GRAY2, C_GRAY1)}}}.
      * @param emuTerm Whether or not to use terminal emulation. Default value is `true`.
      */
    def previewImage(img: CPImage, bg: CPPixel = DFLT_BG, emuTerm: Boolean = true): Unit =
        val imgDim = img.getDim
        val dim = imgDim + 8
        CPEngine.init(
            CPGameInfo(
                name = s"Image Preview (${img.getClass.getSimpleName}, ${imgDim.width}x${imgDim.height})",
                devName = "(C) 2021 Rowan Games, Inc.",
                initDim = Some(dim),
                termBg = bg.bg.getOrElse(CPColor.C_DFLT_BG)
            ),
            emuTerm = emuTerm
        )
        try
            CPEngine.rootLog().info(s"Image preview [" +
                s"origin=${img.getOrigin}, " +
                s"dim=${img.getDim}, " +
                s"class=${img.getClass.getName}" +
            s"]")
            CPEngine.startGame(new CPScene(
                "scene",
                Some(dim),
                bg,
                CPImageSprite("spr", 4, 4, 0, img, false), // Image we are previewing.
                CPUtils.makeExitGameOnLoQ()
            ))
        finally
            CPEngine.dispose()
