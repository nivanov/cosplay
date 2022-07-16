/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the '
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an '
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

grammar CPMirMashCmd;

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

// Parser.
// =======

mashCmd: pipeline EOF; // Mash enty point.
pipeline
    : item
    | pipeline op item
    ;
item: prg AMP?;
prg: STR args;
args
    : STR
    | args STR
    ;
op: PIPE_IN | TO_FILE | APPEND;
// Lexer.
// ======

AMP: '&';
PIPE_IN: '||';
TO_FILE: '>';
APPEND: '>>';
STR: ~[\r\n\t]+;
WS: [ \r\t\u000C\n]+ -> skip;
ErrorChar: .;
