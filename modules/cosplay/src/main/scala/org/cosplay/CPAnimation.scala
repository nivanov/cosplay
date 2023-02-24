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
import org.cosplay.CPPixel.*
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
               All rights reserved.
*/

/**
  * Definition of the animation.
  *
  * Animation is defined as a sequence of [[CPAnimationKeyFrame key frames]]. Each key frame
  * has an [[CPImage image]] and the index of its position in the sequence of key frames. Animation
  * produces a key frame given animation [[CPAnimationContext context]] via method [[keyFrame()]]. Note
  * that animation definition is abstracted out from the way it is rendered. The same animation can be
  * rendered differently. One such rendering is implemented by the built-in sprite [[CPAnimationSprite]] class.
  *
  * Animation is an asset. Just like other assets such as [[CPImage images]], [[CPSound sounds]], [[CPFont fonts]] or
  * [[CPVideo videos]] they are not managed or governed by the CosPlay game engine unlike [[CPScene scenes]] and [[CPSceneObject scene objects]]
  * that are managed and governed by the game engine. Assets are typically created outside the game loop and
  * managed by the developer, they can be freely shared between scenes or scene objects as any other standard
  * Scala objects.
  *
  * Class [[CPAnimationSprite]] provides convenient built-in support for animation-driven sprites. In most
  * cases you will to use or extend this sprite class to work with this animation.
  *
  * Note that companion object provides factory methods that produce often used types of animation:
  *  - [[CPAnimation.timeBased()]]
  *  - [[CPAnimation.filmStrip()]]
  *
  * ### Refresh Rate
  * Even though CosPlay's effective FPS is usually around 1,000 on modern computers, the typical ANSI
  * terminal struggles to render a full screen update even at partly 30 FPS. Large frame updates tend to
  * result in "rolling shutter" effect, which is pronounced more for certain shapes and certain movement
  * directions.
  *
  * It is important to note that this effect is more pronounced for horizontal movement of large
  * vertical shapes. Try to avoid this type of movement in your games.
  *
  * ### Discrete Animation
  * It should come as no surprise that animation in ANSI terminal can only happen in one
  * character units. You can't move by individual pixel - you only move by 10-20 pixel at a time depending
  * on the font size used by your terminal. Even the specific font size is not directly available to
  * the native ASCII game.
  *
  * Discrete animation is obviously more jerky and more stuttering comparing to the pixel-based animation of
  * the traditional graphics games. Vertical movement is more jerky than horizontal one since character height
  * is usually larger than the character width (and we can move only a one character at a time).
  *
  * There are two ways to mitigate this limitation:
  *  - Use smaller shapes for animation, prefer horizontal movement, and avoid prolong movements.
  *    "Rolling shutter" effect does not happen on each frame (more like every 20-40 frames) and so
  *    shorter animation sequences have a lesser chance of encountering this effect.
  *  - Use discrete animation as an artistic tool. When used properly and consistently it can result
  *    in a unique visual design of the game.
  *
  * ### Different Ways To Animate
  * In CosPlay there are different ways one could implement animated scene objects. In the end, all of these
  * approaches deliver similar results but each individual technique is tailor-made for a specific animation type:
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
  * ### Particle Effects
  * [[CPParticle Particle effect]] animation is based on the concept of a [[CPPixel pixel]]-based particle and particle
  * emitter. Particle emitter emits particles. Each particle and its emitter have a fully programmable behavior.
  * The key characteristic of particle effect animation is the randomness over the large number of individual elements
  * that you can easily model and implement using fully programmable particles and emitters.
  *
  * In our airplane example, lets consider how one could implement the explosion when the airplane is hit with
  * the missile. One could potentially implement such animated explosion as a long sequence of images but such
  * process would be very tidies and lack the desired randomness. Particle effect-based animation fits the bill
  * perfectly in such cases allowing to implement such random explosion animation in just a few lines of code.
  *
  * ### Canvas Drawing
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
    private val DFLT_BG = '.'&&(C_GRAY2, C_GRAY1)

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
        val dim = frameDim + 8
        CPEngine.init(
            CPGameInfo(
                name = s"Animation Preview $dim",
                initDim = dim.?,
                termBg = bg.bg.getOrElse(CPColor.C_DFLT_BG)
            ),
            emuTerm = emuTerm
        )
        val spr = CPAnimationSprite("spr", ani.seq, x = 4, y = 4, z = 0, ani.getId)
        CPEngine.rootLog().info(s"Animation ID: ${ani.getId}")
        try
            CPEngine.startGame(new CPScene(
                "scene",
                dim.?,
                bg,
                spr, // Animation we are previewing.
                CPKeyboardSprite(KEY_LO_Q, _.exitGame()), // Exit the game on 'Q' press.
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
        !>(frames.nonEmpty, s"Animation frames cannot be empty.")
        !>(frames.forall(_._2 > 0), s"Invalid animation frames duration (must be > 0).")
        !>(!(bounce && !loop), "'bounce' cannot be true when 'loop' is false.")

        new CPAnimation(id):
            private var lastFrameMs = 0L
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
                lastFrameMs = 0L
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
                        CPAnimationKeyFrame(
                            id,
                            frames(idx)._1,
                            idx
                        ).?
                    else
                        None
                else
                    None

    /**
      * Creates new filmstrip animation with given parameters.
      *
      * filmstrip animation is a variation of time-based animation where all key frames have the same
      * duration, like in a movie, hence the name.
      *
      * @param id Unique ID of the animation.
      * @param frameMs Individual frame duration in milliseconds (applied to all frames).
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
