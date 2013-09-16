%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>  	
#include "P1.tab.h"
%}

%%

"#define" {return HASH_DEFINE;}
"if" {return IF;}
"else" {return ELSE;}
"while" {return WHILE;}
"System.out.println" {return PRINT;}

"(" {return OPAREN;}
")" {return CPAREN;}
"[" {return OSPAREN;}
"]" {return CSPAREN;}
"{" {return OFPAREN;}
"}" {return CFPAREN;}
     
"=" {return EQUALTO;}
"!" {return NOT;}

"+" {yylval.str = "+\0"; return BOP;}
"&" {yylval.str = "&\0"; return BOP;}
"<" {yylval.str = "<\0"; return BOP;}
"-" {yylval.str = "-\0"; return BOP;}
"*" {yylval.str = "*\0"; return BOP;}
"/" {yylval.str = "/\0"; return BOP;}

"length" {return LEN;}
"true" {yylval.str = yytext;
		return BOOLVAL;}
"false" {yylval.str = yytext;
		 return BOOLVAL;}

"String" {return STRING;}
"class" {return CLASS;}
"public" {return PUBLIC;}
"static" {return STATIC;}
"void" {return VOID;}
"main" {return MAIN;}
"extends" {return EXTENDS;}
"return" {return RETURN;}
"this" {//printf("this\n");
		return THIS;}
"new" {return NEW;}
"int" {return INT;}
"boolean" {return BOOL;}

";" {return COLON;}
"," {return COMMA;}
"." {return DOT;}

("//".*\n) {/*Another type of comment*/}
\/\*([^*]|[\r\n]|(\*+([^*\/]|[\r\n])))*\*+\/ {/*Multi-line comments, ignore*/}
\/\/([^\n\r])*[\n\r] {/*Single line comment, ignore*/ }

[ \n\t] {}

[a-zA-Z_][a-zA-Z_0-9]* {//printf("%s\n",yytext);
						yylval.str = yytext;
						printf("%s\n",yytext);
						return ID;
						}
[0-9]+ {yylval.str = yytext;
		// printf("%s\n",yytext);
		// yylval.num = atoi(yytext);
		return NUM;}

<<EOF>> {return 0;}
%%
