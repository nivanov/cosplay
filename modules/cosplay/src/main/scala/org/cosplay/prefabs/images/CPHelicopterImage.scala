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
object CPHelicopterImage extends CPArrayImage(
    // 36x8
    prepSeq("""
      |---------------+---------------
      |          ____/^^[___              _
      |        _/|^+----+   |#___________//
      |       ( -+ |____|    _____,.----+/
      |        `=_________--'            \
      |          \_|___|__
    """),
    (ch, _, _) => ch&C_WHITE
)

/**
  * Previews image using the built-in image viewer.
  */
@main def previewHelicopterImage(): Unit =
    CPImage.previewImage(CPHelicopterImage.trimBg())
    sys.exit(0)
