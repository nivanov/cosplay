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

grammar MirMash;

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

mash: decls* EOF; // Mash entry point.
decls
    : decl
    | decls decl
    ;
decl
    : varDecl
    | valDecl
    | delimDecl
    | defDecl
    | natDefDecl
    | whileDecl
    | forDecl
    | aliasDecl
    | assignDecl
    | includeDecl
    | ifDecl
    | returnDecl
    | pipelineDecl
    | defCall
    ;
includeDecl: INCLUDE qstring;
delimDecl: SCOL;
assignDecl: STR ASSIGN expr;
pipelineDecl: prgList AMP?;
prgList
    : prg
    | prgList pipeOp prg
    ;
prg: (STR | qstring) argList?;
arg: STR | qstring;
argList
    : arg
    | argList arg
    ;
pipeOp: VERT | GT | APPEND_FILE;
defCall: STR LPAR callParamList? RPAR;
returnDecl: RETURN expr?;
aliasDecl: ALIAS STR ASSIGN qstring;
valDecl: VAL STR ASSIGN expr;
varDecl: VAR STR ASSIGN expr;
defDecl: defNameDecl LPAR funParamList? RPAR ASSIGN compoundExpr;
defNameDecl: DEF STR;
natDefDecl: NATIVE DEF STR LPAR funParamList? RPAR;
whileDecl: whileExprDecl DO compoundExpr;
whileExprDecl: WHILE expr;
forDecl: forExprDecl DO compoundExpr;
forExprDecl: FOR STR IN expr;
funParamList
    : STR
    | funParamList COMMA STR
    ;
ifThen: THEN compoundExpr;
ifElse: ELSE compoundExpr;
ifDecl: IF expr ifThen ifElse?;
expr
    // NOTE: order of productions defines precedence.
    : op=(MINUS | NOT) expr # unaryExpr
    | LPAR expr RPAR # parExpr
    | expr op=(MULT | DIV | MOD) expr # multDivModExpr
    | expr op=(PLUS | MINUS) expr # plusMinusExpr
    | expr op=(LTEQ | GTEQ | LT | GT) expr # compExpr
    | expr op=(EQ | NEQ) expr # eqNeqExpr
    | expr op=(AND | OR) expr # andOrExpr
    | defCall # callExpr
    | BQUOTE pipelineDecl BQUOTE # pipelineExecExpr
    | atom # atomExpr
    ;
compoundExpr
    : decl
    | LBRACE decl* RBRACE
    ;
callParamList
    : expr
    | callParamList COMMA expr
    ;
atom
    : NULL
    | BOOL
    | STR // Number or variable access (var or val).
    | qstring
    ;
qstring
    : SQSTRING
    | DQSTRING
    ;

// Lexer.
// ======

ALIAS: 'alias';
INCLUDE: 'include';
VAL: 'val';
VAR: 'var';
DEF: 'def';
ASSOC: '->';
NATIVE: 'native';
RETURN: 'return';
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
COMMA: ',';
MINUS: '-';
DOT: '.';
ASSIGN: '=';
PLUS: '+';
MULT: '*';
SCOL: ';';
DIV: '/';
MOD: '%';
STR: [0-9a-zA-Z_./-]+;
COMMENT : ('//' ~[\r\n]* '\r'? ('\n'| EOF) | '/*' .*? '*/' ) -> skip;
WS: [ \r\t\n\u000C]+ -> skip;
ErrorChar: .;
