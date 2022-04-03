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
import org.cosplay.impl.CPUtils

import scala.collection.mutable

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
  * A scene objet tailor-made for showing animations based on [[CPAnimation]].
  *
  * This sprite allows to implement a visual scene object that has one or more animations associated with it. These
  * animations can be controlled either by the sprite itself or from the outside. This class provides necessary
  * implementation for [[update()]] and [[render()]] methods. Make sure to call `super` if overriding any of
  * these methods.
  *
  * There are additional methods that allow access to and the control of the animation in the sprite:
  *  - [[splice()]]
  *  - [[rewind()]]
  *  - [[change()]]
  *  - [[reset()]]
  *  - [[contains()]]
  *  - [[getAnimations]]
  *  - [[setOnKeyFrameChange()]]
  *
  * Typically, to create a moving sprite, one would override this class and implement movement logic
  * in overridden [[update()]] method (making sure to call `super`), as well as
  * overriding [[getX]], [[getY]] and [[getDim]] methods to return current coordinates and dimension.
  * Note that in most cases, one do need to override or change [[render()]] method.
  *
  * @param id Optional ID of the sprite.
  * @param anis Sequence of animation. Must have at least one animation in it.
  * @param x Initial X-coordinate.
  * @param y Initial Y-coordinate.
  * @param z Initial Z-index.
  * @param initAniId ID of the initial animation to play. This animation starts to play immediately.
  * @param collidable Whether or not this sprite is collidable.
  * @param shaders Optional sequence of shaders for this sprite. Default value is an empty list.
  * @see [[CPParticleSprite]]
  * @see [[CPShader]]
  * @see [[CPAnimation]]
  * @example See [[org.cosplay.examples.animation.CPAnimationExample CPAnimationExample]] source code for an
  *     example of animation functionality.
  */
class CPAnimationSprite(
    id: String = s"ani-spr-${CPRand.guid6}",
    anis: Seq[CPAnimation],
    x: Int,
    y: Int,
    z: Int,
    initAniId: String,
    collidable: Boolean = false,
    shaders: Seq[CPShader] = Seq.empty) extends CPSceneObject(id):
    require(anis.nonEmpty, "Sequence of animation cannot be empty.")

    private var keyFrameOpt: Option[CPAnimationKeyFrame] = None
    private var curAni: CPAnimation = getAni(initAniId)
    private var delayedAni: Option[CPAnimation] = None
    private var pausedAni: Option[CPAnimation] = None
    private val onKfChangeMap = mutable.HashMap.empty[String, (CPAnimation, CPSceneObjectContext) => Unit]

    /**
      *
      * @param id
      */
    private def getAni(id: String): CPAnimation = anis.find(_.getId == id) match
        case Some(ani) => ani
        case None => E(s"Unknown animation: $id")

    /**
      * Gets sequence of animation for this sprite.
      */
    def getAnimations: Seq[CPAnimation] = anis

    /**
      * Checks whether this sprite contains given animation.
      *
      * @param id ID of the animation to check.
      */
    def contains(id: String): Boolean = anis.exists(_.getId == id)

    /**
      * Gets currently running animation. Note that animation sprite always has a running
      * animation.
      */
    def getCurrentAnimation: CPAnimation = curAni

    /**
      * Splices in given animation. Current animation will be stopped, put on hold and the
      * spliced in animation will play. After it is finished the algorithm will revert back
      * to the stored animation.
      *
      * @param id ID of the animation to splice in. Note that this animation should have been
      *     passed into the constructor of this sprite.
      * @param reset Whether or not to [[reset()]] spliced in animation before it is played.
      *     Default value is `true`.
      * @param finish Whether or not to finish the current animation before the spliced in one
      *     starts. If `false`  current animation will stop immediately and the next frame will
      *     play the spliced in animation. The default value is `true`.
      */
    def splice(id: String, reset: Boolean = true, finish: Boolean = true): Unit =
        if curAni.getId != id then
            pausedAni = Option(curAni)
            switchAni(id, reset, finish)

    /**
      * Changes the currently playing animation.
      *
      * @param id ID of the new animation to change to. Note that this animation should have been
      *     passed into the constructor of this sprite.
      * @param reset Whether or not to [[reset()]] the animation before it is played.
      *     Default value is `true`.
      * @param finish Whether or not to finish the current animation before changing to the given
      *     one. If `false`  current animation will stop immediately and the next frame will
      *     play the new animation. The default value is `true`.
      */
    def change(id: String, reset: Boolean = true, finish: Boolean = true): Unit =
        if curAni.getId != id then
            pausedAni = None
            switchAni(id, reset, finish)

    /**
      *
      * @param id
      * @param reset
      * @param finish
      */
    private def switchAni(id: String, reset: Boolean, finish: Boolean): Unit =
        val ani = getAni(id)
        if finish then delayedAni = Option(ani) else curAni = ani
        if reset then ani.reset()

    /**
      * Resets current animation by calling its [[CPAnimation.reset()]] method.
      */
    def reset(): Unit = curAni.reset()

    /**
      * Sets or removes the callback on key frame change.
      *
      * @param id ID of animation.
      * @param f Key frame change callback function that takes [[CPAnimation]] and [[CPSceneObjectContext]]
      *     as its parameters. Use `None` to remove previously set callback.
      */
    def setOnKeyFrameChange(id: String, f: Option[(CPAnimation, CPSceneObjectContext) => Unit]): Unit = f match
        case Some(fun) => onKfChangeMap.put(id, fun)
        case None => onKfChangeMap.remove(id)

    /**
      * Rewinds this sprite by changing to initial animation.
      *
      * @param reset Whether or not to [[reset() reset]] the initial animation. Default value is `true`.
      * @param finish Whether or not to finish the current animation before changing to the initial
      *     one. If `false`  current animation will stop immediately and the next frame will
      *     play the initial animation. The default value is `true`.
      */
    def rewind(reset: Boolean = true, finish: Boolean = true): Unit =
        pausedAni = None
        change(initAniId)

    /** @inheritdoc */
    override def getShaders: Seq[CPShader] = shaders
    /** @inheritdoc */
    override def getX: Int = x
    /** @inheritdoc */
    override def getY: Int = y
    /** @inheritdoc */
    override def getZ: Int = z
    /** @inheritdoc */
    override def getCollisionRect: Option[CPRect] = Option.when(collidable)(getRect)
    /** @inheritdoc */
    override def getDim: CPDim = if keyFrameOpt.isDefined then keyFrameOpt.get.image.getDim else CPDim.ZERO
    /** @inheritdoc */
    override def update(ctx: CPSceneObjectContext): Unit =
        val kfOpt = curAni.keyFrame(new CPAnimationContext():
            private val myLog = ctx.getLog.getLog(id)

            override def getX: Int = CPAnimationSprite.this.getX
            override def getY: Int = CPAnimationSprite.this.getY
            override def getZ: Int = CPAnimationSprite.this.getZ
            override def getId: String = id
            override def getLog: CPLog = myLog
            override def getGameCache: CPCache = ctx.getGameCache
            override def getSceneCache: CPCache = ctx.getSceneCache
            override def getFrameCount: Long = ctx.getFrameCount
            override def getFrameMs: Long = ctx.getFrameMs
            override def getSceneFrameCount: Long = ctx.getSceneFrameCount
            override def getStartGameMs: Long = ctx.getStartGameMs
            override def getStartSceneMs: Long = ctx.getStartSceneMs
            override def sendMessage(id: String, msgs: AnyRef*): Unit = ctx.sendMessage(id, msgs)
            override def receiveMessage(): Seq[AnyRef] = ctx.receiveMessage()
        )

        kfOpt match
            case Some(kf) =>
                if keyFrameOpt.isEmpty || keyFrameOpt.get != kf then
                    onKfChangeMap.get(curAni.getId) match
                        case Some(fun) => fun(curAni, ctx)
                        case None => ()
                keyFrameOpt = kfOpt
            case None => ()

    /** @inheritdoc */
    override def render(ctx: CPSceneObjectContext): Unit =
        keyFrameOpt match
            case Some(keyFrame) =>
                ctx.getCanvas.drawImage(keyFrame.image, getX, getY, getZ)
                if delayedAni.isDefined && curAni.isLastFrame(keyFrame) then
                    curAni = delayedAni.get
                    delayedAni = None
            case None => pausedAni match
                case Some(ani) =>
                    curAni = ani
                    pausedAni = None
                case None => ()