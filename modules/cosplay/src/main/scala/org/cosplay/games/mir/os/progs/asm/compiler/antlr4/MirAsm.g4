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

grammar MirAsm;

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

asm: code EOF; // Assembler entry point.
code
    : inst
    | code inst
    ;
inst: label? name=NAME plist;
label: ID COLON;
plist
    : param
    | plist param
    ;
param
    : qstring
    | BOOL
    | NULL
    | ID
    | INT REAL? EXP?
    ;
qstring
    : SQSTRING
    | DQSTRING
    ;

// Lexer.
// ======
NAME
    : 'mov'
    | 'add'
    ;
SQSTRING: SQUOTE (~'\'')* SQUOTE;
DQSTRING: DQUOTE ((~'"') | ('\\''"'))* DQUOTE; // Allow for \" (escape double quote) in the string.
BOOL: 'true' | 'false';
NULL: 'null';
SQUOTE: '\'';
DQUOTE: '"';
COLON: ':';
DOT: '.';
INT: '0' | [1-9] [_0-9]*;
REAL: DOT [0-9]+;
EXP: [Ee] [+\-]? INT;
ID: [0-9a-zA-Z_]+;
COMMENT: ';' ~[\r\n]* '\r'? ('\n'| EOF) -> skip;
WS: [ \r\t\u000C\n]+ -> skip;
ErrorChar: .;