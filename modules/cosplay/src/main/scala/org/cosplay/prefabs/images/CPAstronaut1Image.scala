package org.cosplay.prefabs.images

import org.cosplay.*
import CPColor.*
import CPArrayImage.*
import CPPixel.*

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
 * https://www.asciiart.eu
 */
object CPAstronaut1Image extends CPArrayImage(
    prepSeq(
        """
          |        _..._
          |      .'     '.      _
          |     /    .-""-\   _/ \
          |   .-|   /:.   |  |   |
          |   |  \  |:.   /.-'-./
          |   | .-'-;:__.'    =/
          |   .'=  *=|NASA _.='
          |  /   _.  |    ;
          | ;-.-'|    \   |
          |/   | \    _\  _\
          |\__/'._;.  ==' ==\
          |         \    \   |
          |         /    /   /
          |         /-._/-._/
          |         \   `\  \
          |          `-._/._/
        """),
    (ch, _, _) => if ch.isLetter then CPPixel(ch, C_RED, Option(C_WHITE)) else ch&C_WHITE
)

/**
 * Previews image using the built-in image viewer.
 */
@main def previewAstronaut1Image(): Unit =
    CPImage.previewImage(CPAstronaut1Image.trimBg())
    sys.exit(0)
