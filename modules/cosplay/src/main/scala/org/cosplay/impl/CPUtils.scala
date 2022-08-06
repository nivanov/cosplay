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

package org.cosplay.impl

import org.cosplay.*
import CPKeyboardKey.*

import java.util.{Locale, Random, TimeZone, UUID}
import java.util.zip.*
import java.net.*
import java.io.*
import java.lang.management.*
import java.net.http.*
import scala.io.Source
import scala.sys.SystemProperties
import scala.util.Using
import java.nio.file.*
import scala.collection.mutable.ArrayBuffer
import com.mixpanel.mixpanelapi.*
import org.json.JSONObject
import org.apache.commons.lang3.*

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

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
  *
  */
object CPUtils:
    private val sysProps = new SystemProperties
    private val rt = Runtime.getRuntime
    private val sysMx = ManagementFactory.getOperatingSystemMXBean
    private val memMx = ManagementFactory.getMemoryMXBean
    // Open, unsecure storage.
    private val MIXPANEL_TOKEN = "b5149f93d5a7693c42d1fa558896b70f"
    private val DFLT_MIXPANEL_GUID = "314159265359"
    private val isTracking = !isSysEnvSet("COSPLAY_DISABLE_MIXPANEL")

    /** */
    final val PING_MSG = "8369926740-3247024617-2096692631-7483698541-4348351625-9412150510-5442257448-4805421296-5646586017-0232477804"
    
    /**
      * Safely splits given string into substring by '\n' character, ignoring Windows vs. Unix
      * differences in new line character.
      *
      * @param s String to split.
      * @return
      */
    def splitByNewLine(s: String): Seq[String] =
        s.replaceAll("\r", "").split("\n").toSeq

    /**
      *
      * @param ch
      * @param top
      * @param left
      * @param bottom
      * @param right
      * @see [[CPImage.antialias()]]
      * @see [[CPCanvas.antialias()]]
      */
    def aaChar(ch: Char, top: Boolean, left: Boolean, bottom: Boolean, right: Boolean): Char =
        // Based on: https://codegolf.stackexchange.com/questions/5450/anti-aliasing-ascii-art
        if (!top && !left && right && bottom) 'd'
        else if (!top && left && !right && bottom) 'b'
        else if (top && left && !right && !bottom) 'F'
        else if (top && !left && right && !bottom) 'Y'
        else if (!top && !left && !right && bottom) ';'
        else if (top && !left && !right && !bottom) 'V'
        else if (!top && left && !right && !bottom) '>'
        else if (!top && !left && right && !bottom) '<'
        else if (!top && !left && !right && !bottom) '@'
        else
            ch

    /**
      *
      */
    def onHeapMemUsage: Long = memMx.getHeapMemoryUsage.getUsed

    /**
      *
      */
    def offHeapMemUsage: Long = memMx.getNonHeapMemoryUsage.getUsed

    /**
      *
      */
    def cpuUsagePct: Int =
        sysMx.getSystemLoadAverage.toFloat.round

    /**
      *
      */
    def memUsagePct: Int =
        val free = rt.freeMemory()
        val total = rt.totalMemory()
        (free.toFloat / total * 100).round

    /**
      * Splits given collection by specified separator predicate.
      *
      * @param col Collection to split.
      * @param sep Separator predicate.
      */
    def splitBy[T](col: Seq[T], sep: T => Boolean): Seq[Seq[T]] =
        val bufs = new ArrayBuffer[ArrayBuffer[T]]()

        var idx = 0
        for t <- col do
            if sep(t) then
                bufs += ArrayBuffer.empty[T]
                idx += 1
            else
                if idx < bufs.size then bufs(idx) += t else bufs += ArrayBuffer(t)

        bufs.filter(_.nonEmpty).map(_.toSeq).toSeq

    /**
      * Trims leading and trailing elements satisfying given predicate.
      *
      * @param col Collection to trim.
      * @param trim Trimming predicate.
      */
    def trimBy[T](col: Seq[T], trim: T => Boolean): Seq[T] =
        col.indexWhere(!trim(_)) match
            case -1 => Seq.empty[T]
            case a => col.slice(a, col.lastIndexWhere(!trim(_)) + 1)

    /**
      * Gets system property, or environment variable (in that order), or `None` if none exists.
      *
      * @param s Name of the system property or environment variable.
      */
    def sysEnv(s: String): Option[String] = sysProps.get(s).orElse(sys.env.get(s))

    /**
      * Gets `true` if given property is set and its value is `true`, `false` in all
      * other cases.
      *
      * @param s Name of the system property or environment variable.
      */
    def sysEnvBool(s: String): Boolean = sysEnv(s) match
        case Some(ss) => java.lang.Boolean.valueOf(ss)
        case None => false

    /**
      * Tests whether given system property of environment variable is set or not.
      *
      * @param s @param s Name of the system property or environment variable.
      */
    def isSysEnvSet(s: String): Boolean = sysProps.get(s).nonEmpty || sys.env.contains(s)

    /**
      * Returns `true` if given system property, or environment variable is provided and has value
      * 'true'. In all other cases returns `false`.
      *
      * @param s Name of the system property or environment variable.
      */
    def isSysEnvTrue(s: String): Boolean = sysEnv(s) match
        case None => false
        case Some(v) => java.lang.Boolean.valueOf(v) == java.lang.Boolean.TRUE    

    /**
      *
      * @param url URL to read.
      */
    def readByteUrl(url: String): Array[Byte] =
        Using.resource(new URL(url).openStream()) { in => readByteStream(in, url) }

    /**
      *
      * @param bytes Array of bytes to unzip.
      */
    def unzipBytes(bytes: Array[Byte]): Array[Byte] =
        try
            val gis = new GZIPInputStream(new ByteArrayInputStream(bytes))
            try
                val out = new ByteArrayOutputStream(bytes.length)
                val buf = new Array[Byte](1024)
                while (gis.available() > 0)
                    val cnt = gis.read(buf, 0, 1024)
                    if cnt > 0 then out.write(buf, 0, cnt)
                close(out)
                out.toByteArray
            finally
                close(gis)
        catch
            case e: Exception => E(s"Failed to unzip byte array.", e)

    /**
      *
      * @param bytes Array of bytes to unzip.
      */
    def zipBytes(bytes: Array[Byte]): Array[Byte] =
        try
            val gis = new ByteArrayInputStream(bytes)
            try
                val bout = new ByteArrayOutputStream()
                val out = new GZIPOutputStream(bout)
                val buf = new Array[Byte](1024)
                while (gis.available() > 0)
                    val cnt = gis.read(buf, 0, 1024)
                    if cnt > 0 then out.write(buf, 0, cnt)
                close(out)
                bout.toByteArray
            finally
                close(gis)
        catch
            case e: Exception => E(s"Failed to zip byte array.", e)

    /**
      * Creates gzip file.
      *
      * @param f Original file to archive.
      * @param del Whether or not to remove the original file.
      */
    def gzipFile(f: File, del: Boolean): Unit =
        val gz = s"${f.getAbsolutePath}.gz"
        try
            // Do not user BOS here - it makes files corrupt.
            Using.resource(new GZIPOutputStream(new FileOutputStream(gz))) { stream =>
                stream.write(Files.readAllBytes(f.toPath))
                stream.flush()
            }
        catch
            case e: IOException => E(s"Error creating file: $gz", e)
        if del && !f.delete() then E(s"Error while deleting file: $f")

    /**
      * Gets resource stream from classpath.
      *
      * @param res Resource.
      */
    def getStream(res: String): InputStream =
        getClass.getClassLoader.getResourceAsStream(res) match
            case in if in != null => in
            case _ => E(s"Resource not found: $res")

    /**
      * Gets resource existing flag.
      *
      * @param res Resource.
      */
    def isResource(res: String): Boolean = getClass.getClassLoader.getResourceAsStream(res) != null

    /**
      *
      * @param src Local filesystem path, resources file or URL.
      */
    def readAllStrings(src: String, enc: String = "UTF-8"): List[String] =
        if isFile(src) then readFile(new File(src), enc)
        else if isResource(src) then readResource(src, enc)
        else if isUrl(src) then Using.resource(new URL(src).openStream()) { readStream(_, enc) }
        else E(s"Source not found or unsupported: $src")

    /**
      *
      * @param src Local filesystem path, resources file or URL.
      */
    def readAllBytes(src: String): Array[Byte] =
        if isFile(src) then CPUtils.readByteFile(new File(src))
        else if isResource(src) then readByteResource(src)
        else if isUrl(src) then readByteUrl(src)
        else E(s"Binary source not found or unsupported: $src")

    /**
      *
      * @param url URL to check.
      */
    def isUrl(url: String): Boolean =
        try
            new URL(url)
            true
        catch case _: MalformedURLException => false

    /**
      *
      * @param path Local file path to check.
      */
    def isFile(path: String): Boolean =
        val f = new File(path)
        f.exists() && f.isFile

    /**
      * Reads lines from given file.
      *
      * @param f File to read from.
      * @param enc Encoding.
      */
    def readFile(f: File, enc: String = "UTF-8"): List[String] =
        try Using.resource(Source.fromFile(f, enc)) { _.getLines().map(p => p).toList }
        catch case e: IOException => E(s"Failed to read file: ${f.getAbsolutePath}", e)

    /**
      * Reads all bytes from given file.
      *
      * @param f File to read from.
      */
    def readByteFile(f: File): Array[Byte] =
        try Files.readAllBytes(f.toPath)
        catch case e: IOException => E(s"Failed to read binary file: ${f.getAbsolutePath}", e)

    /**
      * Reads lines from given stream.
      *
      * @param in Stream to read from.
      * @param enc Encoding.
      */
    def readStream(in: InputStream, enc: String = "UTF-8"): List[String] =
        mapStream(in, enc, _.map(p => p).toList)

    /**
      * Checks if given collection has any dups.
      *
      * @param col Collection to check.
      */
    def hasDups[T](col: Seq[T]): Boolean = col.distinct.sizeCompare(col) != 0

    /**
      *
      * @param in Stream to read from.
      * @param name Name of the stream for error messages.
      */
    def readByteStream(in: InputStream, name: String): Array[Byte] =
        val buf = ArrayBuffer.empty[Byte]
        try
            var b = in.read()
            while (b != -1)
                buf += b.toByte
                b = in.read()
        catch case e: Exception => E(s"Failed to read binary stream: $name", e)
        finally close(in)
        buf.toArray

    /**
      * Closes closeable ignoring any exceptions.
      *
      * @param a Resource to close.
      */
    def close(a: Closeable): Unit =
        if a != null then
            try a.close()
            catch case _: IOException => ()

    /**
      * Maps lines from the given stream to an object.
      *
      * @param in Stream to read from.
      * @param enc Encoding.
      * @param mapper Function to read lines.
      */
    def mapStream[T](in: InputStream, enc: String, mapper: Iterator[String] => T): T =
        Using.resource(Source.fromInputStream(in, enc)) { src =>
            mapper(src.getLines())
        }

    /**
      * Reads lines from given resource.
      *
      * @param res Resource path to read from.
      * @param enc Encoding.
      */
    def readResource(res: String, enc: String = "UTF-8"): List[String] = readStream(getStream(res), enc)

    /**
      *
      * @param res Resource path to read from.
      */
    def readByteResource(res: String): Array[Byte] = readByteStream(getStream(res), res)

    /**
      * Maps lines from the given resource to an object.
      *
      * @param res Resource path to read from.
      * @param enc Encoding.
      * @param mapper Function to map lines.
      */
    def mapResource[T](res: String, enc: String = "UTF-8", mapper: Iterator[String] => T): T =
        mapStream(getStream(res), enc, mapper)

    /**
      *
      * @param evtName
      * @param props
      */
    private def sendMixpanelEvent(evtName: String, props: JSONObject): Unit =
        if isTracking then
            Future {
                try
                    // Anonymous data only.
                    val guid = NetworkInterface.getByInetAddress(InetAddress.getLocalHost) match
                        case null => DFLT_MIXPANEL_GUID
                        case nif =>
                            val addr = nif.getHardwareAddress
                            if addr == null then DFLT_MIXPANEL_GUID else addr.mkString(",").hashCode.toString
                    val msgBldr = new MessageBuilder(MIXPANEL_TOKEN)
                    val evt = msgBldr.event(guid, evtName, props)
                    val pckt = new ClientDelivery()
                    pckt.addMessage(evt)
                    new MixpanelAPI().deliver(pckt)
                catch
                    case _: Exception => () // Ignore.
            }

    /**
      * Records anonymous GA event. Ignores any errors.
      *
      * @param gi Game info.
      */
    def startPing(gi: CPGameInfo): Unit =
        if isTracking then
            val props = new JSONObject()
            props.put("game-name", gi.name)
            props.put("game-ver", gi.semVer)
            props.put("game-id", gi.id)
            props.put("cosplay-ver", CPVersion.latest.semver)
            props.put("cosplay-date", CPVersion.latest.date.toString)
            props.put("locale", Locale.getDefault.toString)
            props.put("os", SystemUtils.OS_NAME)
            props.put("java-ver", SystemUtils.JAVA_VERSION)
            props.put("java-vendor", SystemUtils.JAVA_VM_VENDOR)
            props.put("java-vm-info", SystemUtils.JAVA_VM_INFO)
            val tz = java.util.TimeZone.getDefault
            props.put("tz_zone_id", tz.toZoneId.toString)
            props.put("tz_name", tz.getDisplayName)
            sendMixpanelEvent("game-start", props)











