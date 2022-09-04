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
    | delDecl
    | defDecl
    | nativeDefDecl
    | whileDecl
    | forDecl
    | aliasDecl
    | expr
    | pipelineDecl
    ;
delDecl: SCOL;
pipelineDecl: prgList AMP?;
prgList
    : prg
    | prgList pipeOp prg
    ;
prg: STR argList?;
arg: (STR | qstring);
argList
    : arg
    | argList arg
    ;
pipeOp: VERT | GT | APPEND_FILE;
aliasDecl: ALIAS STR ASSIGN pipelineDecl;
letDecl: LET STR ASSIGN expr;
defDecl: DEF STR LPAR funParamList? RPAR ASSIGN compoundExpr;
nativeDefDecl: NATIVE DEF STR LPAR funParamList? RPAR;
whileDecl: WHILE expr DO compoundExpr;
forDecl: FOR STR IN expr DO compoundExpr;
funParamList
    : STR
    | funParamList COMMA STR
    ;
expr
    // NOTE: order of productions defines precedence.
    : op=(MINUS | NOT) expr # unaryExpr
    | expr MOD expr # modExpr
    | LPAR expr RPAR # parExpr
    | IF expr THEN compoundExpr (ELSE compoundExpr)? # ifExpr
    | FOR STR IN expr YIELD compoundExpr # forYieldExpr
    | LPAR funParamList? RPAR ANON_DEF compoundExpr # anonDefExpr
    | expr op=(MULT | DIV | MOD) expr # multDivModExpr
    | expr op=(PLUS | MINUS) expr # plusMinusExpr
    | expr op=(LTEQ | GTEQ | LT | GT) expr # compExpr
    | expr op=(EQ | NEQ) expr # eqNeqExpr
    | expr op=(AND | OR) expr # andOrExpr
    | atom # atomExpr
    | LPAR listItems? RPAR # listExpr
    | TILDA LPAR mapItems? RPAR # mapExpr
    | STR LPAR callParamList? RPAR # callExpr
    | varAccess LPAR callParamList? RPAR # fpCallExpr
    | varAccess # varAccessExpr
    | BQUOTE pipelineDecl BQUOTE # pipelineExecExpr
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
    | LBRACE (decl|expr)* RBRACE
    ;
callParamList
    : compoundExpr
    | callParamList COMMA compoundExpr
    ;
varAccess
    : DOLLAR STR keyAccess*
    | DOLLAR LPAR STR RPAR keyAccess*
    | CMD_ARGS_NUM
    | LAST_EXIT_STATUS
    | LAST_PID
    | LAST_BG_PID
    | CMD_ARGS_LIST
    ;
keyAccess: LBR expr RBR;
atom
    : NULL
    | STR
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
ASSIGN: '=';
PLUS: '+';
QUESTION: '?';
MULT: '*';
SCOL: ';';
DIV: '/';
MOD: '%';
DOLLAR: '$';
//INT: '0' | [1-9] [_0-9]*;
//REAL: DOT [0-9]+;
//EXP: [Ee] [+\-]? INT;
//UNDERSCORE: '_';
//fragment LETTER: [a-zA-Z];
//IDENT: (UNDERSCORE|LETTER)+(UNDERSCORE|LETTER|[0-9])*;
//PATH_STR: [0-9a-zA-Z${}/._-]+;
STR: [0-9a-zA-Z${}/._-]+;
COMMENT : ('//' ~[\r\n]* '\r'? ('\n'| EOF) | '/*' .*? '*/' ) -> skip;
WS: [ \r\t\u000C\n]+ -> skip;
ErrorChar: .;
