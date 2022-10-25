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
          | // Console functions.
          | native def print(s)
          | native def println(s)
          |
          | // Date and time functions.
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
          | native def format_date(d)
          | native def format_time(d)
          | native def format_datetime(d)
          |
          | // Misc. functions.
          | native def rand()
          | native def rand_int(from, to)
          | native def guid()
          | native def guid6()
          | native def rand_letter()
          | native def rand_low_letter()
          | native def rand_up_letter()
          | native def rand_digit()
          | native def rand_symbol()
          | native def coin_flip()
          |
          | // Math functions.
          | native def bool_sigmoid(d)
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
          | native def pi()
          | native def euler()
          | native def min(list)
          | native def max(list)
          | native def abs(d)
          | native def stddev(list)
          | native def pow(d, v)
          | native def hypot(d, v)
          |
          | // String functions.
          | native def concat(s1, s2)
          | native def to_str(d)
          | native def length(s)
          | native def regex(s1, s2)
          | native def uppercase(s)
          | native def lowercase(s)
          | native def is_alpha(s)
          | native def is_num(s)
          | native def is_alphanum(s)
          | native def is_whitespace(s)
          | native def is_alphaspace(s)
          | native def is_numspace(s)
          | native def is_alphanumspace(s)
          | native def split(s1, s2)
          | native def trim(s)
          | native def split_trim(s1, s2)
          | native def start_width(s1, s2)
          | native def end_width(s1, s2)
          | native def contains(s1, s2)
          | native def substr(s, x, y)
          | native def replace(s1, s2, s3)
          | native def to_long(s)
          | native def to_double(s)
          |
          | // Debug functions.
          | native def assert(cond, s)
          | native def ensure(cond)
          |
          | // List/Map functions.
          | native def new_list()
          | native def new_map()
          | native def map_from(list)
          | native def clear(list)
          | native def get(list, idxOrKey)
          | native def copy(list)
          | native def key_list(map)
          | native def value_list(map)
          | native def size(listOrMap)
          | native def drop(list, n)
          | native def drop_right(list, n)
          | native def contains_key(map, key)
          | native def contains_value(map, value)
          | native def put(map, key, value)
          | native def first(list)
          | native def last(list)
          | native def index_of(list, v)
          | native def index_of_start(list, v)
          | native def last_index_of(list, v)
          | native def is_empty(listOrMap)
          | native def non_empty(listOrMap)
          | native def mk_string(list, sep)
          | native def distinct(list)
          | native def add_list(list1, list2)
          | native def has_all(list1, list2)
          | native def has_any(list1, list2)
          | native def add(list, elm)
          | native def prepend(list, elm)
          | native def update(list, idx, elm)
          | native def remove(list, idx)
          | native def take(list, n)
          | native def take_right(list, n)
          | native def reverse(list)
          |
          | // Debug only.
          | native def _println(s)
          |
          |""".stripMargin



