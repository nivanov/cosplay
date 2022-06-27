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
    : valDecl
    | varDecl
    | assignDecl
    | exportDecl
    | unexportDecl
    | defDecl
    | nativeDefDecl
    | whileDecl
    | forDecl
    ;
valDecl: VAL_KW IDENT ASSIGN expr;
varDecl: VAR_KW IDENT (ASSIGN expr)?;
assignDecl: IDENT ASSIGN expr;
exportDecl: EXPORT_KW IDENT;
unexportDecl: UNEXPORT_KW IDENT;
defDecl: DEF_KW IDENT LPAR funParamList? RPAR ASSIGN compoundExpr;
nativeDefDecl: NATIVE_KW DEF_KW IDENT LPAR funParamList? RPAR;
whileDecl: WHILE_KW expr DO_KW compoundExpr;
forDecl: FOR_KW IDENT IN_KW expr DO_KW compoundExpr;
funParamList
    : IDENT
    | funParamList COMMA IDENT
    ;
expr
    // NOTE: order of productions defines precedence.
    : IF_KW expr THEN_KW compoundExpr ELSE_KW compoundExpr # ifExpr
    | FOR_KW IDENT IN_KW expr YIELD_KW compoundExpr # forYieldExpr
    | LPAR funParamList? RPAR ANON_DEF_KW compoundExpr # anonDefExpr
    | op=(MINUS | NOT) expr # unaryExpr
    | LPAR expr RPAR # parExpr
    | expr op=(MULT | DIV | MOD) expr # multDivModExpr
    | expr op=(PLUS | MINUS) expr # plusMinusExpr
    | expr op=(LTEQ | GTEQ | LT | GT) expr # compExpr
    | expr op=(EQ | NEQ) expr # eqNeqExpr
    | expr op=(AND | OR) expr # andOrExpr
    | atom # atomExpr
    | LPAR exprList RPAR # listExpr
    | IDENT LPAR callParamList? RPAR # callExpr
    | varAccess LPAR callParamList? RPAR # fpCallExpr
    | varAccess # varAccessExpr
    ;
exprList
    : expr
    | exprList COMMA expr
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
    | DOLLAR IDENT listAccess?
    | DOLLAR LPAR IDENT RPAR listAccess?
    | DOLLAR POUND // '$#' number of command line paramters.
    | DOLLAR QUESTION // '$?' exit code of the last command.
    ;
listAccess: LBR INT RBR;
atom
    : NULL
    | INT REAL? EXP?
    | BOOL
    | qstring
    ;
qstring
    : SQSTRING
    | DQSTRING
    | BQSTRING
    ;

// Lexer.
// ======

// Keywords.
VAR_KW: 'var';
VAL_KW: 'val';
DEF_KW: 'def';
ANON_DEF_KW: '=>';
EXPORT_KW: 'export';
UNEXPORT_KW: 'unexport';
NATIVE_KW: 'native';
IF_KW: 'if';
THEN_KW: 'then';
ELSE_KW: 'else';
WHILE_KW: 'while';
DO_KW: 'do';
YIELD_KW: 'yield';
FOR_KW: 'for';
IN_KW: '<-';

// Tokens.
SQSTRING: SQUOTE ((~'\'') | ('\\''\''))* SQUOTE; // Allow for \' (escaped single quote) in the string.
DQSTRING: DQUOTE ((~'"') | ('\\''"'))* DQUOTE; // Allow for \" (escape double quote) in the string.
BQSTRING: BQUOTE ((~'`') | ('\\''`'))* BQUOTE; // Allow for \` (escape double quote) in the string.
BOOL: 'true' | 'false';
NULL: 'null';
EQ: '==';
NEQ: '!=';
GTEQ: '>=';
LTEQ: '<=';
GT: '>';
LT: '<';
AND: '&&';
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
COLON: ':';
MINUS: '-';
DOT: '.';
UNDERSCORE: '_';
ASSIGN: '=';
PLUS: '+';
QUESTION: '?';
MULT: '*';
DIV: '/';
MOD: '%';
AT: '@';
DOLLAR: '$';
INT: '0' | [1-9] [_0-9]*;
REAL: DOT [0-9]+;
EXP: [Ee] [+\-]? INT;
fragment LETTER: [a-zA-Z];
//ID: (UNDERSCORE|LETTER|MINUS|DOT|[0-9])+;
IDENT: (UNDERSCORE|LETTER)+(UNDERSCORE|LETTER|[0-9])*;
COMMENT : ('//' ~[\r\n]* '\r'? ('\n'| EOF) | '/*' .*? '*/' ) -> skip;
WS: [ \r\t\u000C\n]+ -> skip;
ErrorChar: .;
