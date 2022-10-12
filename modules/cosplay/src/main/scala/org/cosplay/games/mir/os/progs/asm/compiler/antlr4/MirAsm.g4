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
    : NL* inst (NL*|EOF)
    | code inst (NL*|EOF)
    ;
inst: label? INSRT_NAME plist? dbg?;
dbg: AT INT COMMA INT COMMA DQSTRING; // Source file information for mash.
label: ID COLON NL*;
plist
    : param
    | plist COMMA param
    ;
param
    : DQSTRING
    | NULL
    | ID
    | INT REAL? EXP?
    ;

// Lexer.
// ======
INSRT_NAME
    : 'mulv'
    | 'ssz'
    | 'cpop'
    | 'clr'
    | 'clrv'
    | 'clrp'
    | 'ifjmp'
    | 'ifjmpv'
    | 'nop'
    | 'eqp'
    | 'neqp'
    | 'and'
    | 'or'
    | 'mod'
    | 'divv'
    | 'mul'
    | 'div'
    | 'eq'
    | 'neq'
    | 'eqv'
    | 'neqv'
    | 'ltp'
    | 'ltep'
    | 'gtp'
    | 'gtep'
    | 'lt'
    | 'lte'
    | 'gt'
    | 'gte'
    | 'ltv'
    | 'ltev'
    | 'gtv'
    | 'gtev'
    | 'brk'
    | 'cbrk'
    | 'cbrkv'
    | 'push'
    | 'pushn'
    | 'pop'
    | 'add'
    | 'sub'
    | 'addv'
    | 'subv'
    | 'calln'
    | 'call'
    | 'callx'
    | 'jmp'
    | 'cjmp' // Conditional jump over stack value.
    | 'cjmpv' // Conditional jump over variable value.
    | 'inc'
    | 'incv'
    | 'dec'
    | 'decv'
    | 'ret'
    | 'let'
    | 'dup'
    | 'not'
    | 'notv'
    | 'neg'
    | 'negv'
    | 'exit'
    ;
DQSTRING: DQUOTE ((~'"') | ('\\''"'))* DQUOTE; // Allow for \" (escape double quote) in the string.
NULL: 'null';
DQUOTE: '"';
SCOLOR: ';';
COMMA: ',';
NL: '\n';
COLON: ':';
DOT: '.';
AT: '@';
INT: '0' | '-'? [1-9] [_0-9]*;
REAL: DOT [0-9]+;
EXP: [Ee] [+\-]? INT;
ID: [0-9a-zA-Z_$]+;
COMMENT: SCOLOR ~[\r\n]* '\r'? (NL| EOF) -> skip;
WS: [ \r\t\u000C]+ -> skip;
ErrorChar: .;
