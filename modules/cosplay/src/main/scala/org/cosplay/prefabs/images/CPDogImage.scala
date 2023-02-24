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
object CPDogImage extends CPArrayImage(
    // 60x36
    prepSeq(
    """
      |           __.
      |        .-".'                      .--.            _..._
      |      .' .'                     .'    \       .-""  __ ""-.
      |     /  /                     .'       : --..:__.-""  ""-. \
      |    :  :                     /         ;.d$$    sbp_.-""-:_:
      |    ;  :                    : ._       :P .-.   ,"TP
      |    :   \                    \  T--...-; : d$b  :d$b
      |     \   `.                   \  `..'    ; $ $  ;$ $
      |      `.   "-.                 ).        : T$P  :T$P
      |        \..---^..             /           `-'    `._`._
      |       .'        "-.       .-"                     T$$$b
      |      /             "-._.-"               ._        '^' ;
      |     :                                    \.`.         /
      |     ;                                -.   \`."-._.-'-'
      |    :                                 .'\   \ \ \ \
      |    ;  ;                             /:  \   \ \ . ;
      |   :   :                            ,  ;  `.  `.;  :
      |   ;    \        ;                     ;    "-._:  ;
      |  :      `.      :                     :         \/
      |  ;       /"-.    ;                    :
      | :       /    "-. :                  : ;
      | :     .'        T-;                 ; ;
      | ;    :          ; ;                /  :
      | ;    ;          : :              .'    ;
      |:    :            ;:         _..-"\     :
      |:     \           : ;       /      \     ;
      |;    . '.         '-;      /        ;    :
      |;  \  ; :           :     :         :    '-.
      |'.._L.:-'           :     ;          ;    . `.
      |                     ;    :          :  \  ; :
      |                     :    '-..       '.._L.:-'
      |                      ;     , `.
      |                      :   \  ; :
      |                      '..__L.:-'
    """),
    (ch, _, _) => ch&C_SANDY_BROWN
)

/**
  * Previews image using the built-in image viewer.
  */
@main def previewDogImage(): Unit =
    CPImage.previewImage(CPDogImage.trimBg())
    sys.exit(0)
