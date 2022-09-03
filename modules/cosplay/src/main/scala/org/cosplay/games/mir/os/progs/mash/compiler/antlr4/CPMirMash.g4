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

grammar CPMirMash;

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

mash: decls EOF; // Mash enty point.
decls
    : decl
    | decls decl
    ;
decl
    : letDecl
    | defDecl
    | nativeDefDecl
    | whileDecl
    | forDecl
    | compoundExpr
    | aliasDecl
    | pipelineDecl
    ;
pipelineDecl: prgList AMP?;
prgList
    : prg
    | prgList pipeOp prg
    ;
prg: path argList?;
path: PATH_STR;
arg: (PATH_STR | qstring);
argList
    : arg
    | argList arg
    ;
pipeOp: VERT | GT | APPEND_FILE;
aliasDecl: ALIAS IDENT ASSIGN pipelineDecl;
letDecl: LET IDENT ASSIGN expr;
defDecl: DEF IDENT LPAR funParamList? RPAR ASSIGN compoundExpr;
nativeDefDecl: NATIVE DEF IDENT LPAR funParamList? RPAR;
whileDecl: WHILE expr DO compoundExpr;
forDecl: FOR IDENT IN expr DO compoundExpr;
funParamList
    : IDENT
    | funParamList COMMA IDENT
    ;
expr
    // NOTE: order of productions defines precedence.
    : op=(MINUS | NOT) expr # unaryExpr
    | expr MOD expr # modExpr
    | LPAR expr RPAR # parExpr
    | IF expr THEN compoundExpr (ELSE compoundExpr)? # ifExpr
    | FOR IDENT IN expr YIELD compoundExpr # forYieldExpr
    | LPAR funParamList? RPAR ANON_DEF compoundExpr # anonDefExpr
    | expr op=(MULT | DIV | MOD) expr # multDivModExpr
    | expr op=(PLUS | MINUS) expr # plusMinusExpr
    | expr op=(LTEQ | GTEQ | LT | GT) expr # compExpr
    | expr op=(EQ | NEQ) expr # eqNeqExpr
    | expr op=(AND | OR) expr # andOrExpr
    | atom # atomExpr
    | LPAR listItems? RPAR # listExpr
    | TILDA LPAR mapItems? RPAR # mapExpr
    | IDENT LPAR callParamList? RPAR # callExpr
    | varAccess LPAR callParamList? RPAR # fpCallExpr
    | varAccess # varAccessExpr
    ;
listItems
    : expr
    | listItems COMMA expr
    ;
mapItem: expr ASSOC expr;
mapItems
    : mapItem
    | mapItems COMMA mapItem
    ;
compoundExpr
    : expr
    | LBRACE (decl|expr)+ RBRACE
    ;
callParamList
    : expr
    | callParamList COMMA expr
    ;
varAccess
    : DOLLAR INT // '$1' command line parameter access. '$0' is entire command line as a string.
    | DOLLAR LPAR INT RPAR
    | DOLLAR IDENT keyAccess*
    | DOLLAR LPAR IDENT RPAR keyAccess*
    | CMD_ARGS_NUM
    | LAST_EXIT_STATUS
    | LAST_PID
    | LAST_BG_PID
    | CMD_ARGS_LIST
    ;
keyAccess: LBR expr RBR;
atom
    : NULL
    | INT REAL? EXP?
    | BOOL
    | qstring
    ;
qstring
    : SQSTRING
    | DQSTRING
    ;

// Lexer.
// ======

ALIAS: 'alias';
CMD_ARGS_NUM: '$#';
LAST_EXIT_STATUS: '$?';
LAST_PID: '$$';
LAST_BG_PID: '$!';
CMD_ARGS_LIST: '$@';
LET: 'let';
DEF: 'def';
ANON_DEF: '=>';
ASSOC: '->';
NATIVE: 'native';
IF: 'if';
THEN: 'then';
ELSE: 'else';
WHILE: 'while';
DO: 'do';
YIELD: 'yield';
FOR: 'for';
IN: '<-';
PATH_STR: [0-9a-zA-Z${}/._-]+ {getText().equals("bin/sh")}?;
SQSTRING: SQUOTE (~'\'')* SQUOTE;
DQSTRING: DQUOTE ((~'"') | ('\\''"'))* DQUOTE; // Allow for \" (escape double quote) in the string.
BOOL: 'true' | 'false';
NULL: 'null';
EQ: '==';
NEQ: '!=';
GTEQ: '>=';
LTEQ: '<=';
GT: '>';
LT: '<';
AND: '&&';
AMP: '&';
APPEND_FILE: '>>';
OR: '||';
VERT: '|';
NOT: '!';
LPAR: '(';
RPAR: ')';
LBRACE: '{';
RBRACE: '}';
SQUOTE: '\'';
DQUOTE: '"';
BQUOTE: '`';
TILDA: '~';
LBR: '[';
RBR: ']';
POUND: '#';
COMMA: ',';
MINUS: '-';
DOT: '.';
UNDERSCORE: '_';
ASSIGN: '=';
PLUS: '+';
QUESTION: '?';
MULT: '*';
DIV: '/';
MOD: '%';
DOLLAR: '$';
INT: '0' | [1-9] [_0-9]*;
REAL: DOT [0-9]+;
EXP: [Ee] [+\-]? INT;
fragment LETTER: [a-zA-Z];
IDENT: (UNDERSCORE|LETTER)+(UNDERSCORE|LETTER|[0-9])*;
COMMENT : ('//' ~[\r\n]* '\r'? ('\n'| EOF) | '/*' .*? '*/' ) -> skip;
WS: [ \r\t\u000C\n]+ -> skip;
ErrorChar: .;
