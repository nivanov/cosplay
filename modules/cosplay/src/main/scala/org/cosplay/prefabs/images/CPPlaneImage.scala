package org.cosplay.prefabs.images

import org.cosplay.*
import org.cosplay.CPColor.*
import org.cosplay.CPArrayImage.*
import org.cosplay.CPPixel.*


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
object CPPlaneImage extends CPArrayImage(
    // 32x10
    prepSeq("""
      |            ____            ___
      |  |        | ___\          /   |
      | _:_______|/'(..)`\_______/  | |
      |<_|``````  \__~~__/       ___|_|
      |  :\_____(=========,   ,--\__|_/
      |  |       \       /---'
      |           |     /
      |           |____/
    """),
    (ch, _, _) => ch&C_WHITE
)

/**
  * Previews image using the built-in image viewer.
  */
@main def previewPlaneImage(): Unit =
    CPImage.previewImage(CPPlaneImage.trimBg())
    sys.exit(0)
