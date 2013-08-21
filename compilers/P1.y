%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
extern FILE* yyin;

struct ArgTemp {
	char* Name;
	struct ArgTemp* Next;	
};

typedef struct ArgTemp Arg;

struct MacroNodeTemp {
	char* Macro;
	char* Expansion;
	Arg* args_sentinel;
	Arg* args_current;
	int num_args;
	struct MacroNodeTemp* Next;
};

typedef struct MacroNodeTemp MacroNode;

MacroNode* sentinel = NULL;
MacroNode* current = NULL;

void MakeMacroNode (char* macro, char* expansion) {
	MacroNode* node;
	node = (MacroNode*) malloc(sizeof(MacroNode));
	node->Macro = (char*) malloc(strlen(macro));	
	strcpy(node->Macro, macro);
	node->Expansion = (char*) malloc(strlen(expansion));	
	strcpy(node->Expansion, expansion);
	if (sentinel == NULL) {
		sentinel = current = node;
	} else {
		current->Next = node;
		current = current->Next;
	}
	node->Next = NULL;
	node->num_args = 0;
	node->args_sentinel = NULL;
	node->args_current = NULL;
}

void AddArgs (char* arg) {
	Arg* node;
	node = (Arg*) malloc(sizeof(Arg));
	node->Name = malloc(strlen(arg));
	strcpy(node->Name, arg);
	node->Next = NULL;
	current->num_args++;
	if (current->args_sentinel == NULL) {
		current->args_sentinel = node;
		current->args_current = node;
	} else {
		current->args_current->Next = node;
		current->args_current = current->args_current->Next;
	}
}


%}

%union {
	int num;
	char* str;	
}

/* Token declarations */
%token EQUALTO
%token HASH_DEFINE
%token IF
%token ELSE
%token WHILE
%token PRINT
%token OPAREN
%token CPAREN
%token OSPAREN
%token CSPAREN
%token OFPAREN
%token CFPAREN
%token NOT
%token <str> BOP
%token LEN
%token <str> BOOLVAL
%token STRING
%token CLASS
%token PUBLIC
%token STATIC
%token VOID
%token MAIN
%token EXTENDS
%token RETURN
%token THIS
%token NEW
%token INT
%token BOOL
%token COLON
%token COMMA
%token DOT
%token <str> ID
%token <str> NUM

%type <str>  Identifier GOAL MainClass TypeDecStar TypeDec MethodDecStar MethodDec QTICTI TICTI CTypeIDStar TypeIDColonStar Type StatementStar Statement Exp QExpCExp ExpCExp CExpStar PExp MacroDefStar MacroDef MacDefStatement MacDefExp QIDCID IDCID CIDStar 

%%
GOAL: MacroDefStar MainClass TypeDecStar
;

MainClass: CLASS Identifier OFPAREN PUBLIC STATIC VOID MAIN OPAREN STRING OSPAREN CSPAREN Identifier CPAREN OFPAREN PRINT OPAREN Exp CPAREN COLON CFPAREN CFPAREN
;

TypeDecStar: 
| TypeDecStar TypeDec
;

TypeDec: CLASS Identifier OFPAREN TypeIDColonStar MethodDecStar CFPAREN
| CLASS Identifier EXTENDS Identifier OFPAREN TypeIDColonStar MethodDecStar CFPAREN
;

MethodDecStar:
| MethodDecStar MethodDec
;

MethodDec: PUBLIC Type Identifier OPAREN QTICTI CPAREN OFPAREN TypeIDColonStar StatementStar RETURN Exp COLON CFPAREN
;

QTICTI:
| TICTI
;

TICTI: Type Identifier CTypeIDStar
;

CTypeIDStar:
| CTypeIDStar COMMA Type ID
;

TypeIDColonStar:
| TypeIDColonStar Type Identifier COLON
;

Type: INT OSPAREN CSPAREN
| BOOL
| INT
;

StatementStar:
| StatementStar Statement
;

Statement: OFPAREN StatementStar CFPAREN
| PRINT OPAREN Exp CPAREN COLON
| Identifier EQUALTO Exp COLON 
| Identifier OSPAREN Exp CSPAREN EQUALTO Exp COLON
| IF OPAREN Exp CPAREN Statement
| IF OPAREN Exp CPAREN Statement ELSE Statement
| WHILE OPAREN Exp CPAREN Statement
| Identifier OPAREN QExpCExp CPAREN COLON
;

Exp: PExp BOP PExp 
| PExp OSPAREN PExp CSPAREN 
| PExp DOT LEN
| PExp
| PExp DOT Identifier OPAREN QExpCExp CPAREN
| Identifier OPAREN QExpCExp CPAREN
;

QExpCExp:
| ExpCExp
;

ExpCExp: Exp CExpStar
;

CExpStar:
| CExpStar COMMA Exp
;

PExp: NUM
| BOOLVAL 
| Identifier {/*printf("!%s\n",$1);*/} 
| THIS
| NEW INT OSPAREN Exp CSPAREN
| NEW Identifier OPAREN CPAREN
| NOT Exp
| OPAREN Exp CPAREN
;

MacroDefStar:
| MacroDefStar MacroDef
;

MacroDef: MacDefExp
| MacDefStatement
;

MacDefStatement: HASH_DEFINE Identifier OPAREN QIDCID CPAREN OFPAREN StatementStar CFPAREN {}
;

MacDefExp: HASH_DEFINE Identifier OPAREN QIDCID CPAREN OPAREN Exp CPAREN {//printf("%s\n", $2);}
}
;

QIDCID: 
| IDCID
;

IDCID: Identifier CIDStar
;

CIDStar:
| CIDStar COMMA ID
;

Identifier: ID {}
;
%%

main (int argc, char** argv) {
	do {
		yyparse();
	} while(!feof(yyin));
}

yyerror (char* s) {
	printf("Error\n");
}
