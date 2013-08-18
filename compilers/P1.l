%{
#include <stdio.h>
#include "P1.tab.h"
%}

%%

"#define" {return HASH_DEFINE;}
"if" {return IF;}
"else" {return ELSE;}
"while" {return WHILE;}
"System.out.prinln" {return PRINT;}


%%

main(int argc, char **argv) {
		yylex();
}
