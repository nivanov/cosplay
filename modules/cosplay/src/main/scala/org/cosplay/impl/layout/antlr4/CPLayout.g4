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

grammar CPLayout;

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

// Parser.
// =======
layout: decls* EOF;
decls
    : decl
    | decls decl
    ;
decl: ID EQ items SCOLON;
items
    : item
    | items COMMA item
    ;
item
    : marginItem
    | xItem
    | yItem
    ;
marginItem: 'margin' COLON LBRK NUM COMMA NUM COMMA NUM COMMA NUM RBRK;
xItem: 'x' COLON ('before' | 'left' | 'center' | 'right' | 'after') LPAR ID? RPAR;
yItem: 'y' COLON ('above' | 'top' | 'center' | 'bottom' | 'below') LPAR ID? RPAR;

// Lexer.
// ======
EQ: '=';
SCOLON: ';';
COLON: ':';
COMMA: ',';
LPAR: '(';
RPAR: ')';
LBRK: '[';
RBRK: ']';
NUM: '-'? [0-9] [0-9]*;
ID: [a-zA-Z0-9-_$]+;
COMMENT : ('//' ~[\r\n]* '\r'? ('\n'| EOF) | '/*' .*? '*/' ) -> skip;
WS: [ \r\t\n\u000C]+ -> skip;
ErrorChar: .;
