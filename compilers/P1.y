%{
#define SIZE 10000
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

void AddArgs (MacroNode* node, char* arg);
char* Replace(char* id, char* val, char* string);
char* MakeExpansion(MacroNode* node, char* args);
void MakeMacroNode (char* macro, char* arguments, char* expansion);
MacroNode* Search(char* id);

MacroNode* sentinel = NULL;
MacroNode* current = NULL;

void AddArgs (MacroNode* mnode, char* arg) {
	Arg* node;
	node = (Arg*) malloc(sizeof(Arg));
	node->Name = malloc(SIZE);
	sprintf(node->Name, "%s", arg);
	node->Next = NULL;
	mnode->num_args++;
	if (mnode->args_sentinel == NULL) {
		mnode->args_sentinel = node;
		mnode->args_current = node;
	} else {
		mnode->args_current->Next = node;
		mnode->args_current = mnode->args_current->Next;
	}
}

void MakeMacroNode (char* macro, char* arguments, char* expansion) {
	MacroNode* node;
	node = (MacroNode*) malloc(sizeof(MacroNode));
	node->Next = NULL;
	node->num_args = 0;
	node->args_sentinel = NULL;
	node->args_current = NULL;

	// !!!!!!!!!!!!! use something else instead of size.
	node->Macro = (char*) malloc(SIZE);
	strcpy(node->Macro, macro);
	node->Expansion = (char*) malloc(SIZE);	
	strcpy(node->Expansion, expansion);
		
	if (sentinel == NULL) {
		sentinel = current = node;
	} else {
		current->Next = node;
		current = current->Next;
	}

	// Adding arguments.
	char* arg;
	// printf("!!!!!!!!!!!%s %s", node->Macro, node->Expansion);
	arg = strtok (arguments,",");
	while (arg != NULL) {
		// printf("A-%s", arg);
		AddArgs(node, arg);
		arg = strtok (NULL, ",");
	}
	// printf("\n");
}

MacroNode* Search(char* id) {
	MacroNode* macro = sentinel;
	while (macro != NULL) {
		if (!strcmp(macro->Macro, id)) {
			return macro;
		}
		macro = macro->Next;
	}
	return NULL;
}

char* Replace(char* id, char* val, char* string) {
	// printf("FNREP : %s %s %s \n", id, val, string);
	char* output = (char*) malloc(SIZE);
	char* input = (char*) malloc(SIZE);
	sprintf(input, "%s", string);
	char* remaining = (char*) malloc(SIZE);
	sprintf(output, "");
	int span = 0;
	span = strlen(id);
	char* temp = (char*) malloc(SIZE);
	char* modifiedval = (char*) malloc(SIZE);

	MacroNode* nested = Search(val);
	if (nested == NULL) {
		sprintf(modifiedval, "%s", val);
	} else {
		sprintf(modifiedval, "%s", MakeExpansion(nested, ""));
	}
	
	char* mark;
	mark = strstr(input, id);
	while (mark != NULL && strcmp(input, "\0")) {
		sprintf(remaining, "%s", mark + span);
		strncpy(mark, "\0", 1);
		sprintf(temp, "%s", input);
		strcat(output, temp);
		strcat(output, modifiedval);
		sprintf(input, "%s", remaining);
		mark = strstr(input, id);
	}
	strcat(output, input);
	// printf("%s \n", output);
	
	return output;
}

char* MakeExpansion(MacroNode* node, char* args) {
	Arg* curr = node->args_sentinel;
	char* val;
	char* temp = (char*) malloc(SIZE);
	sprintf(temp, "%s", node->Expansion);
	val = strtok (args,",");
	while (curr != NULL && val != NULL) {
		// printf("REP : %s %s\n", curr->Name, val);
		sprintf(temp, "%s", Replace(curr->Name, val, temp));
		// printf("HEY %s\n", temp);
		val = strtok (NULL, ",");
		curr = curr->Next; 
	}
	return temp;
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
GOAL: MacroDefStar MainClass TypeDecStar {printf("%s %s %s", $1, $2, $3); free($1); free($2); free($3);}
;

MainClass: CLASS Identifier OFPAREN PUBLIC STATIC VOID MAIN OPAREN STRING OSPAREN CSPAREN Identifier CPAREN OFPAREN PRINT OPAREN Exp CPAREN COLON CFPAREN CFPAREN {$$ = (char*) malloc(SIZE); sprintf($$, "class %s {public static void main ( String[] %s ) {System.out.println(%s);}}", $2, $12,$17); free($12); free($17);}
;

TypeDecStar: {$$ = (char*) malloc(SIZE); sprintf($$, "");}
| TypeDecStar TypeDec {$$ = (char*) malloc(SIZE); sprintf($$, "%s %s", $1, $2); free($1); free($2);}
;

TypeDec: CLASS Identifier OFPAREN TypeIDColonStar MethodDecStar CFPAREN {$$ = (char*) malloc(SIZE); sprintf($$, "class %s {%s %s}\n", $2, $4, $5); free($4); free($5);}
| CLASS Identifier EXTENDS Identifier OFPAREN TypeIDColonStar MethodDecStar CFPAREN {$$ = (char*) malloc(SIZE); sprintf($$, "class %s extends %s {%s %s}\n", $2, $4, $6, $7); free($6); free($7);}
;

MethodDecStar: {$$ = (char*) malloc(SIZE); sprintf($$, "");}
| MethodDecStar MethodDec {$$ = (char*) malloc(SIZE); sprintf($$, "%s %s", $1, $2); free($1); free($2);}
;

MethodDec: PUBLIC Type Identifier OPAREN QTICTI CPAREN OFPAREN TypeIDColonStar StatementStar RETURN Exp COLON CFPAREN {$$ = (char*) malloc(SIZE); sprintf($$, "public %s %s (%s) {%s %s return %s;}\n", $2, $3, $5, $8, $9, $11); free($2); free($3); free($5); free($8); free($9); free($11);}
;

QTICTI: {$$ = (char*) malloc(SIZE); sprintf($$, "");}
| TICTI {$$ = (char*) malloc(SIZE); sprintf($$, "%s", $1); free($1);}
;

TICTI: Type Identifier CTypeIDStar {$$ = (char*) malloc(SIZE); sprintf($$, "%s %s %s", $1, $2, $3); free($1); free($2); free($3);}
;

CTypeIDStar: {$$ = (char*) malloc(SIZE); sprintf($$, "");}
| CTypeIDStar COMMA Type Identifier {$$ = (char*) malloc(SIZE); sprintf($$, "%s, %s %s", $1, $3, $4); free($1); free($3); free($4);}
;

TypeIDColonStar: {$$ = (char*) malloc(SIZE); sprintf($$, "");}
| TypeIDColonStar Type Identifier COLON {$$ = (char*) malloc(SIZE); sprintf($$, "%s %s %s;\n", $1, $2, $3); free($1); free($2); free($3);}
;

Type:  INT OSPAREN CSPAREN {$$ = (char*) malloc(SIZE); sprintf($$, "int[]");}
| BOOL {$$ = (char*) malloc(SIZE); sprintf($$, "boolean");}
| INT {$$ = (char*) malloc(SIZE); sprintf($$, "int");}
| Identifier {$$ = (char*) malloc(SIZE); sprintf($$, "%s", $1);}
;

StatementStar: {$$ = (char*) malloc(SIZE); sprintf($$, "");}
| Statement StatementStar {$$ = (char*) malloc(SIZE); sprintf($$, "%s %s", $1, $2); free($1); free($2);}
;

Statement: OFPAREN StatementStar CFPAREN {$$ = (char*) malloc(SIZE); sprintf($$, "{%s}\n", $2); free($2);}
| PRINT OPAREN Exp CPAREN COLON {$$ = (char*) malloc(SIZE); sprintf($$, "System.out.println(%s);\n", $3); free($3);}
| Identifier EQUALTO Exp COLON {$$ = (char*) malloc(SIZE); sprintf($$, "%s = %s;\n", $1, $3); free($1); free($3);}
| Identifier OSPAREN Exp CSPAREN EQUALTO Exp COLON {$$ = (char*) malloc(SIZE); sprintf($$, "%s[%s] = %s;\n", $1, $3, $6); free($1);
													free($3); free($6);}
| IF OPAREN Exp CPAREN Statement {$$ = (char*) malloc(SIZE); sprintf($$, "if (%s) %s", $3, $5); free($3); free($5);}
| IF OPAREN Exp CPAREN Statement ELSE Statement {$$ = (char*) malloc(SIZE); sprintf($$, "if (%s) %s else %s", $3, $5, $7 ); free($3);
												 free($5); free($7);}
| WHILE OPAREN Exp CPAREN Statement {$$ = (char*) malloc(SIZE); sprintf($$, "while (%s) %s", $3, $5); free($3); free($5);}
| Identifier OPAREN QExpCExp CPAREN COLON {$$ = (char*) malloc(SIZE); // sprintf($$, "%s(%s);\n", $1, $3); 
	MacroNode* macst = Search($1);
	// printf("$$ %s %s A-%s\n", macst->Macro, macst->Expansion, macst->args_sentinel->Name);
	if (macst != NULL) {
		sprintf($$, "%s;", MakeExpansion(macst, $3));
		//sprintf($$, "%s;",  $3);
	} else {
		sprintf($$, "%s(%s)", $1, $3);
	}
	free($1); free($3);}
;

Exp: PExp BOP PExp {$$ = (char*) malloc(SIZE); sprintf($$, "%s %s %s", $1, $2, $3); 
		 			// printf("# %s # %s # %s\n", $1, $2, $3);	
		 			free($1); free($3);
	 				}
| PExp OSPAREN PExp CSPAREN {$$ = (char*) malloc(SIZE); sprintf($$, "%s [%s]", $1, $3); free($1); free($3);}
| PExp DOT LEN {$$ = (char*) malloc(SIZE); sprintf($$, "%s.length", $1); free($1);}
| PExp {$$ = (char*) malloc(SIZE); sprintf($$, "%s", $1); free($1);}
| PExp DOT Identifier OPAREN QExpCExp CPAREN {$$ = (char*) malloc(SIZE); sprintf($$, "%s.%s(%s)", $1, $3, $5); free($1); free($3); free($5);}
| Identifier OPAREN QExpCExp CPAREN {$$ = (char*) malloc(SIZE); //sprintf($$, "%s(%s)", $1, $3); free($1); free($3);}
	MacroNode* macst = Search($1);
	if (macst != NULL) {
		sprintf($$, "%s", MakeExpansion(macst, $3));
	} else {
		sprintf($$, "%s(%s)", $1, $3);
	}
	free($1); free($3);}	
;

QExpCExp: {$$ = (char*) malloc(SIZE); sprintf($$, "");}
| ExpCExp {$$ = (char*) malloc(SIZE); sprintf($$, "%s", $1); free($1);}
;

ExpCExp: Exp CExpStar {$$ = (char*) malloc(SIZE); sprintf($$, "%s %s", $1, $2); free($1); free($2);}
;

CExpStar: {$$ = (char*) malloc(SIZE); sprintf($$, "");}
| CExpStar COMMA Exp {$$ = (char*) malloc(SIZE); sprintf($$, "%s,%s", $1, $3); free($1); free($3);}
;

PExp: NUM {$$ = (char*) malloc(SIZE); sprintf($$, "%s", $1);}
| BOOLVAL {$$ = (char*) malloc(SIZE); sprintf($$, "%s", $1);}
| Identifier {$$ = (char*) malloc(SIZE); sprintf($$, "%s", $1); free($1);}
| THIS {$$ = (char*) malloc(SIZE); sprintf($$, "this");}
| NEW INT OSPAREN Exp CSPAREN {$$ = (char*) malloc(SIZE); sprintf($$, "new int[%s]", $4); free($4);}
| NEW Identifier OPAREN CPAREN {$$ = (char*) malloc(SIZE); sprintf($$, "new %s()", $2); free($2);}
| NOT Exp {$$ = (char*) malloc(SIZE); sprintf($$, "!%s", $2); free($2);}
| OPAREN Exp CPAREN {$$ = (char*) malloc(SIZE); sprintf($$, "(%s)", $2); free($2);}
;

MacroDefStar: {$$ = (char*) malloc(SIZE); sprintf($$, "");}
| MacroDefStar MacroDef {$$ = (char*) malloc(SIZE); sprintf($$, "%s %s", $1, $2); free($1); free($2);}
;

MacroDef: MacDefExp {$$ = (char*) malloc(SIZE); sprintf($$, "%s", $1); free($1);}
| MacDefStatement {$$ = (char*) malloc(SIZE); sprintf($$, "%s", $1); free($1);}
;

MacDefStatement: HASH_DEFINE Identifier OPAREN QIDCID CPAREN OFPAREN StatementStar CFPAREN {$$ = (char*) malloc(SIZE); sprintf($$, "");
	char* macstate = (char*) malloc(SIZE);
	sprintf(macstate,"{%s}",$7);
	MakeMacroNode($2, $4, macstate);	
	free($2); free($4); free($7); free(macstate);
	}
;

MacDefExp: HASH_DEFINE Identifier OPAREN QIDCID CPAREN OPAREN Exp CPAREN {$$ = (char*) malloc(SIZE); sprintf($$, "");
	char* macexp = (char*) malloc(SIZE);
	sprintf(macexp,"(%s)", $7);
	MakeMacroNode($2, $4, macexp);
	free($2); free($4); free($7); free(macexp);
	}
;

QIDCID: {$$ = (char*) malloc(SIZE); sprintf($$, "");}
| IDCID {$$ = (char*) malloc(SIZE); sprintf($$, "%s", $1); free($1);}
;

IDCID: Identifier CIDStar {$$ = (char*) malloc(SIZE); sprintf($$, "%s%s", $1, $2); free($1); free($2);}
;

CIDStar: {$$ = (char*) malloc(SIZE); sprintf($$, "");}
| CIDStar COMMA Identifier {$$ = (char*) malloc(SIZE); sprintf($$, "%s,%s", $1, $3); free($1); free($3);}
;

Identifier: ID {$$ = (char*) malloc(SIZE); sprintf($$, "%s", $1);}
;
%%

main (int argc, char** argv) {
	do {
		yyparse();
	} while(!feof(yyin));
/*Type:  INT OSPAREN CSPAREN {$$ = (char*) malloc(SIZE); sprintf($$, "int[]");}
| BOOL {$$ = (char*) malloc(SIZE); sprintf($$, "boolean");}
| INT {$$ = (char*) malloc(SIZE); sprintf($$, "int");}
Type: BOOL {$$ = (char*) malloc(SIZE); sprintf($$, "boolean");}
| INT {$$ = (char*) malloc(SIZE); sprintf($$, "int");}
| Identifier {$$ = (char*) malloc(SIZE); sprintf($$, "%s", $1); free($1);}
| Type OSPAREN CSPAREN {$$ = (char*) malloc(SIZE); sprintf($$, "%s[]", $1); free($1);}
;
;*/
}

yyerror (char* s) {
	printf("// Failed to parse macrojava code.\n");
}
