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
  * A programmable shader for scene object. 
  * 
  * Shader is a piece of user-defined code that is executed on each frame pass for each scene object that has one or 
  * more shaders attached to it.
  *
  * ### Different Ways To Animate
  * In CosPlay there are different ways one could implement animated scene objects. In the end, all of these
  * approaches deliver similar results but each individual technique is tailor-made for a specific animation type:
  *  - [[CPAnimation Animated Sprites]]
  *  - [[CPParticle Particle Effects]]
  *  - [[CPCanvas Canvas Drawing]]
  *  - [[CPVideo Video Sprites]]
  *  - **Shaders**
  *
  * ### Animated Sprites
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
  * that you can easily implement using fully programmable particles and emitters.
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
  * ### **Shaders**
  * CPShader Shader is a piece of user-defined code that is executed on each frame for each scene object that has one or
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
  * Shader is an asset. Just like other assets such as [[CPFont fonts]], [[CPImage images]], [[CPAnimation animations]] or
  * [[CPVideo videos]] they are not managed or governed by the CosPlay game engine unlike [[CPScene scenes]] and [[CPSceneObject scene objects]]
  * that are managed and governed by the game engine. Assets are typically created outside the game loop and
  * managed by the developer, they can be freely shared between scenes or scene objects as any other standard
  * Scala objects.
  *
  * ### Full-Screen Shaders
  * Shader can work with either a scene object it is attached to or with full screen. Typically, a full
  * screen shader will be attached to an off-screen sprite (since the particular scene object such shader
  * is attached to is irrelevant). In more elaborate games, there could be multiple off-screen sprites with
  * multiple full-screen shaders - where all these shaders work with the same screen real estate.  In such
  * cases it can be non-trivial to control the order in which shaders are executed, if required. The recommended
  * technique for such cases is to have only one off-screen sprite that attaches all full-screen shaders so that
  * their order can be easily defined and controlled.
  *
  * ### Order Of Processing
  * Note that shader pass consists of two phases:
  * - On the 1st phase shaders for all visible scene objects are processed
  * - On the 2nd phase shaders for all invisible scene objects are processed.
  * This allows to minimize the interference between object shaders and full-screen shaders that are typically
  * attached to the off-screen sprite that is invisible. In other words, full-screen shaders will be execute 
  * after all object shaders in a given scene.
  *
  * @see [[org.cosplay.prefabs.shaders.CPFadeInShader]]
  * @see [[org.cosplay.prefabs.shaders.CPFadeOutShader]]
  * @example See [[org.cosplay.examples.shader.CPShaderExample CPShaderExample]] class for the example of using shaders.
  */
trait CPShader extends CPAsset:
    /** @inheritdoc */
    override val getOrigin: String = getClass.getName

    /**
      * Called on each frame pass on scene object that has this shaders attached to it. This callback is called
      * regardless of whether or not the scene object is visible or in camera frame.
      *
      * @param ctx Scene object context for this shader pass. Note that scene object context is associated with the
      *     scene object this shader is attached to.
      * @param objRect Current rectangular shape of the scene object this shader is attached to in the canvas
      *     coordinate system.
      * @param inCamera Whether or not the scene object is in camera frame. Note that this callback is called
      *     regardless of whether or not the scene object is visible or in camera frame.
      */
    def render(ctx: CPSceneObjectContext, objRect: CPRect, inCamera: Boolean): Unit
