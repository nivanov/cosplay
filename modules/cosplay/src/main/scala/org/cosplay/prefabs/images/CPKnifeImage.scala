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
object CPKnifeImage extends CPArrayImage(
    // 32x10
    prepSeq("""
      |                                                       ___
      |                                                      |_  |
      |                                                        | |
      |__                      ____                            | |
      |\ ````''''----....____.'\   ````''''--------------------| |--.               _____      .-.
      | :.                      `-._          c.o.s.p.l.a.y    | |   `''-----''''```     ``''|`: :|
      |  '::.                       `'--.._____________________| |                           | : :|
      |    '::..       ----....._______________________________| |                           | : :|
      |        `'-::...__________________________________________| |   .-''-..-'`-..-'`-..-''-.  :|
      |             ```'''---------------------------------------| |--'                        `'-'
      |                                                          | |
      |                                                         _| |
      |                                                        |___|
    """),
    (ch, _, _) => if ch.isLetter then ch&C_DARK_GOLDEN_ROD else ch&C_X11_ANTIQUE_WHITE
)

/**
 * Previews image using the built-in image viewer.
 */
@main def previewKnifeImage(): Unit =
    CPImage.previewImage(CPKnifeImage.trimBg())
    sys.exit(0)
