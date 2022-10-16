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

package org.cosplay.games.mir.os.progs.mash.compiler

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

trait MirMashNatives:
    final val NATIVE_DECLS =
        """
          | native def new_list()
          | native def add(list, v)
          | native def size(list)
          | native def length(s)
          | native def uppercase(s)
          | native def lowercase(s)
          | native def trim(s)
          | native def to_str(s)
          | native def is_alpha(s)
          | native def is_num(s)
          | native def ensure(cond)
          | native def split(str, regex)
          | native def remove(list, idx)
          | native def take(list, n)
          | native def take_right(list, n)
          | native def get(listOrMap, idxOrKey)
          | native def _println(s)
          | native def now()
          | native def year()
          | native def month()
          | native def day_of_month()
          | native def day_of_week()
          | native def day_of_year()
          | native def hour()
          | native def minute()
          | native def second()
          | native def week_of_month()
          | native def week_of_year()
          | native def quarter()
          | native def format_date(ms)
          | native def format_time(ms)
          | native def format_datetime(ms)
          | native def guid()
          | native def guid6()
          | native def rand()
          | native def rand_letter()
          | native def rand_low_letter()
          | native def rand_up_letter()
          | native def rand_digit()
          | native def rand_symbol()
          | native def coin_flip()
          | native def rand_int(from, to)
          | native def ceil(d)
          | native def floor(d)
          | native def rint(d)
          | native def round(d)
          | native def signum(d)
          | native def sqrt(d)
          | native def cbrt(d)
          | native def acos(d)
          | native def asin(d)
          | native def atan(d)
          | native def tan(d)
          | native def sin(d)
          | native def cos(d)
          | native def tanh(d)
          | native def sinh(d)
          | native def cosh(d)
          | native def degrees(d)
          | native def radians(d)
          | native def exp(d)
          | native def expm1(d)
          | native def log(d)
          | native def log10(d)
          | native def square(d)
          | native def log1p(d)
          | native def pi(d)
          | native def euler(d)
          | native def min(d)
          | native def max(d)
          | native def abs(d)
          | native def stddev(d)
          | native def pow(d, i)
          | native def hypot(d)
          |""".stripMargin



