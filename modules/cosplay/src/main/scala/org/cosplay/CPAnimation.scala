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

import org.cosplay.*
import org.cosplay.CPColor.*
import org.cosplay.CPKeyboardKey.*
import org.cosplay.impl.CPUtils

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
  * Definition of the animation.
  *
  * Animation is defined as a sequence of [[CPAnimationKeyFrame key frames]]. Each key frame
  * has an [[CPImage image]] and the index of its position in the sequence of key frames. Animation
  * produces a key frame given animation [[CPAnimationContext context]] via method [[keyFrame()]]. Note
  * that animation definition is abstracted out from the way it is rendered. The same animation can be
  * rendered differently. Once such rendering is implemented by the built-in sprite [[CPAnimationSprite]] class.
  *
  * Animation is an asset. Just like other assets such as [[CPImage images]], [[CPSound sounds]], [[CPFont fonts]] or
  * [[CPVideo videos]] they are not managed or governed by the CosPlay game engine unlike [[CPSceneObject scene objects]]
  * that are managed and governed by the game engine. Assets are typically created outside of the game engine and
  * managed by the developer, they can be freely shared between scenes or scene objects as any other standard
  * Scala objects.
  *
  * Class [[CPAnimationSprite]] provides convenient built-in support for animation-driven sprites. In most
  * cases you will to use or extend this sprite class to work with this animation.
  *
  * Companion object also provides factory methods that produce often used types of animation:
  *  - [[CPAnimation.timeBased()]]
  *  - [[CPAnimation.filmStrip()]]
  *
  * ### Different ways to animate
  * In CosPlay there are different ways one could implement animated scene objects. In the end, all of these
  * approaches deliver the same result but each individual technique is tailor-made for a specific animation type:
  *  - **Animated Sprites**
  *  - [[CPParticle Particle Effects]]
  *  - [[CPCanvas Canvas Drawing]]
  *  - [[CPVideo Video Sprites]]
  *  - [[CPShader Shaders]]
  *
  * ### **Animated Sprites**
  * This is a classic sprite animation technique ideally suited for animations that can be defined as a
  * sequence of individual similarly or identically sized images. When played, each individual images is
  * shown for a short period of time one after another delivering animation effect.
  *
  * Lets consider an animation of the airplane in a top-down view game play. The basic animations for the airplane
  * banking left or right, taking off or landing are ideally suited for sprite-based animation as they can
  * easily be defined as a short sequence of individual images.
  *
  * ### Particle effects
  * [[CPParticle Particle effect]] animation is based on the concept of a [[CPPixel pixel]]-based particle and particle
  * emitter. Particle emitter emits particles. Each particle and its emitter have a fully programmable behavior.
  * The key characteristic of particle effect animation is the randomness over the large number of individual elements
  * that you can easily implement using fully programmable particles and emitters.
  *
  * In our airplane example, lets consider how one could implement the explosion when the airplane is hit with
  * the missile. One could potentially implement such animated explosion as a long sequence of images but such
  * process would be very tidies and lack the desired randomness. Particle effect-based animation fits the bill
  * perfectly in such cases allowing to implement such random explosion animation in just a few lines of code.
  *
  * ### Canvas drawing
  * Sometime, a simple drawing on the [[CPCanvas canvas]] is all that's needed for a desired animation. Consider
  * how one could implement a laser strike in our airplane example. A laser strike can be defined as a variable
  * length line of pixel shown for a split second. The best way to implement it is with one line of code using
  * many of the drawing functions in [[CPCanvas]] class and putting this logic into [[CPSceneObject.render()]]
  * method.
  *
  * ### Video Sprites
  * [[CPVideoSprite Video sprite]] is a variation of sprite-based animation. In case of video, there are typically a lot more frames
  * (often 1000s of frames) and all these frames have the same dimension. [[CPVideo]] and [[CPVideoSprite]] provide
  * easy-to-use mechanism to implement it. Back to our airplane example, the video-based animation would be ideal
  * choice for the cutscenes, entry video, etc.
  *
  * ### Shaders
  * [[CPShader Shader]] is a piece of user-defined code that is executed on each frame for each scene object that has one or
  * more shaders attached to it. There are types of animations that simply don't fit any previous type. The typical example
  * of shader-based animation is the various lighting effect: flash-lite, sun shadows, transitions,
  * highlighting, etc. These types of animation simply cannot be reasonably implemented using sprites, or particles,
  * or canvas drawing. In such cases, shaders provide simple and effective contract to implement this behavior. Yet
  * another unique characteristic of shaders is their application reusability. In fact, the same shader can be added
  * to multiple scene objects to provide its effect.
  *
  * In our airplane example, shaders can be used for shadow effect or "flashing" the airplane when it is
  * hit by the enemy fire.
  *
  * @param id Unique ID of the animation.
  * @see [[CPAnimationContext]]
  * @see [[CPAnimationKeyFrame]]
  * @see [[CPAnimationSprite]] for the built-in sprite that works with this animation.
  * @example See [[org.cosplay.examples.animation.CPAnimationExample CPAnimationExample]] source code for an
  *     example of animation functionality.
  */
abstract class CPAnimation(id: String) extends CPGameObject(id) with CPAsset:
    /** @inheritdoc */
    override val getOrigin: String = getClass.getName

    /**
      * Gets a key frame for a given animation context.
      *
      * This is the "brains" of the animation. Given the animation context, that provides access to
      * the state of the game as well as the information about sprite that is rendering this animation,
      * this method should return an appropriate key frame.
      *
      * Returning `None` will result in not showing any image in the current game frame, effectively
      * hiding the sprite that is rendering this animation (this is the behaviour of [[CPAnimationSprite]]).
      * Returning `Some` will result in rendering key frame's image by the sprite. Note that this method
      * may return the same key frame throughout multiple calls indicating that the same image
      * should be rendered over consecutive game frames.
      *
      * See factory methods in the companion object [[CPAnimation.timeBased()]] and [[CPAnimation.filmStrip()]]
      * that create animations with frequently used strategies.
      *
      * @param ctx Animation context.
      * @return Key frame for given context or `None` if one isn't available. `None` should
      *     typically be returned when you want to stop and hide the animation. Note that this method
      *     may return the same key frame throughout multiple calls indicating that the same image
      *     should be rendered over consecutive game frames.
      * @see [[CPAnimation.timeBased()]]
      * @see [[CPAnimation.filmStrip()]]
      */
    def keyFrame(ctx: CPAnimationContext): Option[CPAnimationKeyFrame]

    /**
      * Gets number of key frames in this animation.
      */
    def getKeyFrameCount: Int

    /**
      * Resets this animation to its initial state.
      */
    def reset(): Unit

    /**
      * Shortcut method that checks if given key frame is the last one in the sequence.
      *
      * @param kf Key frame to check. Note that this key frame must belong to this animation.
      * @return `true` if it is indeed the last key frame, `false` otherwise.
      */
    def isLastFrame(kf: CPAnimationKeyFrame): Boolean =
        kf.animationId == id && kf.index == getKeyFrameCount - 1

    /**
      * Shortcut method that checks if given key frame is the first one in the sequence.
      *
      * @param kf Key frame to check. Note that this key frame must belong to this animation.
      */
    def isFirstFrame(kf: CPAnimationKeyFrame): Boolean =
        kf.animationId == id && kf.index == 0

/**
  * Companion object containing factory methods.
  */
object CPAnimation:
    private final val DFLT_BG = CPPixel('.', C_GRAY2, C_GRAY1)

    /**
      * Previews given animation.
      *
      * Typically, you would use this method together with image definition. Place this call into `@main`
      * function to quickly preview the animation by running this function:
      * {{{
      * object CPCubeAniImage extends CPArrayImage(
      *     prepSeq(13 * 8,
      *         """
      *           |+------+      +------+      +------+      +------+      +------+   +-------+    +------+   +------+
      *           ||`.    |`.    |\     |\     |      |     /|     /|    .'|    .'|  / |     /|    |      |   |\     |\
      *           ||  `+--+---+  | +----+-+    +------+    +-+----+ |  +---+--+'  |  +-+----+ |    +------+   | +----+-+
      *           ||   |  |   |  | |    | |    |      |    | |    | |  |   |  |   |  | |    | |    |      |   | |    | |
      *           |+---+--+   |  +-+----+ |    +------+    | +----+-+  |   +--+---+  | +----+-+    +------+   +-+----+ |
      *           | `. |   `. |   \|     \|    |      |    |/     |/   | .'   | .'   |/     |/     |      |    \|     \|
      *           |   `+------+    +------+    +------+    +------+    +------+'     +------+      +------+     +------+
      *     """),
      *     (ch, _, _) => ch&C_WHITE
      * )
      *
      * @main def preview(): Unit =
      *     val ani = CPAnimation.filmStrip(
      *         "ani",
      *         200,
      *         true,
      *         false,
      *         CPCubeAniImage.trimBg().split(13, 7)
      *     )
      *     CPAnimation.previewAnimation(ani, CPDim(13, 7))
      *     sys.exit(0)
      * }}}
      *
      * @param ani Animation to preview.
      * @param frameDim Dimension of the keyframe.
      * @param emuTerm Whether to use a terminal emulation. Default value is `true`.
      * @param bg Background pixel. Default value is `CPPixel('.', C_GRAY2, C_GRAY1)`.
      * @see [[CPImage.previewAnimation()]]
      * @see [[CPImage.previewImage()]]
      */
    def previewAnimation(ani: CPAnimation, frameDim: CPDim, emuTerm: Boolean = true, bg: CPPixel = DFLT_BG): Unit =
        if bg.bg.isDefined then System.setProperty("COSPLAY_TERM_BG_RGB", bg.bg.get.rgb.toString)
        val dim = frameDim + 8
        CPEngine.init(
            CPGameInfo(
                name = s"Animation Preview $dim",
                devName = "(C) 2022 Rowan Games, Inc.",
                initDim = Option(dim),
                termBg = bg.bg.getOrElse(CPColor.C_DFLT_BG)
            ),
            emuTerm = emuTerm
        )
        val spr = CPAnimationSprite("spr", Seq(ani), 4, 4, 0, ani.getId)
        CPEngine.rootLog().info(s"Animation ID: ${ani.getId}")
        try
            CPEngine.startGame(new CPScene(
                "scene",
                Option(dim),
                bg,
                spr, // Animation we are previewing.
                CPUtils.makeExitGameOnLoQ()
            ))
        finally
            CPEngine.dispose()

    /**
      * Creates new time-based animation with given parameters.
      *
      * Time-based animation is based on idea that animation is a sequence of image and duration pairs.
      * Playing such animation simply means sequentially playing and image for its specified duration and
      * then switching to the next one and repeating the same process.
      *
      * Note that unlike discreet graphics based animation the timing tuning of the terminal ASCII-based
      * animation can be tricky. It is often counterintuitive that faster refresh rate produces blurrier
      * animation and the slower timing would sometime yield better result. There is also a significant difference
      * on whether the sprite is moving vertically or horizontally on the terminal screen as well as how much
      * it moves in relation to animation timing. There's also a perceptible difference in how sprite animation
      * is viewed on the built-in terminal emulator and the actual native terminal such as iTerm2 or xterm. Make sure
      * to experiment with timing of your animations!
      *
      * @param id Unique ID of the animation.
      * @param loop Whether or not to loop the animation. If `false` animation will stop after the last key frame is
      *     played out. Default value is `true`.
      * @param bounce If `loop` is `true` this defines how animation will loop once it reaches the last key
      *     frame: from the beginning or bounce and going backward towards the beginning. Default value is `false` which
      *     will force looping animation to start from the beginning.
      * @param frames Animation images with their corresponding durations in milliseconds.
      * @return Newly created animation.
      * @see [[filmStrip()]]
      */
    def timeBased(id: String, loop: Boolean = true, bounce: Boolean = false, frames: Seq[(CPImage, Long)]): CPAnimation =
        if frames.isEmpty then E(s"Animation frames cannot be empty.")
        if frames.exists(_._2 <= 0) then E(s"Invalid animation frames duration (must be > 0).")
        if bounce && !loop then E("'bounce' cannot be true when 'loop' is false.")

        new CPAnimation(id):
            private var lastFrameMs = 0l
            private val imgsCnt = frames.length
            private val lastIdx = imgsCnt - 1
            private var idx = 0
            private var idxIncr = 1
            private var playing = true

            override def getId: String = id
            override def getKeyFrameCount: Int = imgsCnt
            override def reset(): Unit =
                idx = 0
                idxIncr = 1
                playing = true
                lastFrameMs = 0l
            override def keyFrame(ctx: CPAnimationContext): Option[CPAnimationKeyFrame] =
                if playing then
                    if lastFrameMs == 0 then lastFrameMs = ctx.getFrameMs
                    if ctx.getFrameMs - lastFrameMs > frames(idx)._2 then // Time to switch to the next key frame...
                        lastFrameMs = ctx.getFrameMs
                        if idx == lastIdx then
                            if loop then
                                if bounce then
                                    idxIncr = -1
                                    idx += idxIncr
                                else
                                    idxIncr = 1
                                    idx = 0
                            else
                                playing = false
                        else if idx == 0 then
                            idxIncr = 1
                            idx += idxIncr
                        else
                            idx += idxIncr
                    if playing then
                        Option(CPAnimationKeyFrame(
                            id,
                            frames(idx)._1,
                            idx
                        ))
                    else
                        None
                else
                    None

    /**
      * Creates new film-strip animation with given parameters.
      *
      * Film-strip animation is a variation of time-based animation where all key frames have the same
      * duration, like in a movie, hence the name.
      *
      * @param id Unique ID of the animation.
      * @param frameMs Duration in milliseconds for all frames.
      * @param loop Whether or not to loop the animation. If `false` animation will stop after the last key frame is
      *     played out. Default value is `true`.
      * @param bounce If `loop` is `true` this defines how animation will loop once it reaches the last key
      *     frame: from the beginning or bounce and going backward towards the beginning. Default value is `false` which
      *     will force looping animation to start from the beginning.
      * @param imgs Sequence of animation images.
      * @see [[timeBased()]]
      */
    def filmStrip(id: String, frameMs: Long, loop: Boolean = true, bounce: Boolean = false, imgs: Seq[CPImage]): CPAnimation =
        timeBased(id, loop, bounce, imgs.map((_, frameMs)))
