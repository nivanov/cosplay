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
               ALl rights reserved.
*/

/**
 * https://www.asciiart.eu
 */
object CPAstronautImage extends CPArrayImage(
    // 47x27
    prepSeq(
        """
      |              _..._
      |            .'     '.      _
      |           /    .-""-\   _/ \
      |         .-|   /:.   |  |   |
      |         |  \  |:.   /.-'-./
      |         | .-'-;:__.'    =/
      |         .'=  *=|NASA _.='
      |        /   _.  |    ;
      |       ;-.-'|    \   |
      |      /   | \    _\  _\
      |      \__/'._;.  ==' ==\
      |               \    \   |
      |               /    /   /
      |               /-._/-._/
      |               \   `\  \
      |                `-._/._/
    """),
    (ch, _, _) => ch&C_WHITE
)

/**
 * Previews image using the built-in image viewer.
 */
@main def previewAstronautImage(): Unit =
    CPImage.previewImage(CPAstronautImage.trimBg())
    sys.exit(0)
