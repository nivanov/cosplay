package org.cosplay

import org.cosplay.prefabs.images.ani.CPSpinningGlobeAniImage
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
object CPBackgroundFillTest:
    /**
      *
      */
    @Test
    def backgroundTest(): Unit =
        val img = CPSpinningGlobeAniImage
        println(img.getDim)
