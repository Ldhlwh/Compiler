grammar Mx;

prog
	: progSec* EOF
	;
	
progSec
	: classDecl
	| funcDecl
	| variDecl
	;
	
classDecl
	: CLASS Identifier '{' memDecl* '}'     #classDeclaration
	;
	
memDecl
	: variDecl
	| funcDecl
	;
	
funcDecl
	: typeSpec Identifier '(' paramDeclList? ')' blockStmt  #functionDeclaration
	;
	
typeSpec
	: singleTypeSpec Brackets*
	;

singleTypeSpec
	: type = INT
	| type = CHAR
	| type = BOOL
	| type = STRING
	| type = VOID
	| type = Identifier
	;
	
paramDeclList
	: paramDecl (',' paramDecl)*
	;

paramList
    : expr (',' expr)*
    ;
	
paramDecl
	: typeSpec Identifier
	;
	
variDecl
	: typeSpec variInit (',' variInit)* ';'     #variableDeclaration
	;
	
variInit
	: Identifier (ASSIGN expr)?
	;
	
blockStmt
	: '{' blockCtnt* '}'
	;
	
blockCtnt
	: variDecl
	| stmt
	;
	
exprStmt
	: expr? ';'
	;



stmt
	: blockStmt #blockStatement
	| exprStmt  #expressionStatement
	| slctStmt  #selectionStatement
	| iterStmt  #iterationStatement
	| jumpStmt  #jumpStatement
	;
	
slctStmt
	: IF '(' expr ')' stmt (ELIF '(' expr ')' stmt)* (ELSE stmt)?
	;
	
iterStmt
	: FOR '(' declInit = variDecl  cond = expr? ';' step = expr? ')'	stmt 	# forInit
	| FOR '(' init = expr? ';' cond = expr? ';' step = expr? ')' stmt 			# for
	| WHILE '(' expr ')' stmt													# while	
	;
	
jumpStmt
	: BREAK ';'			# break
	| CONTINUE ';'		# continue
	| RETURN expr ';' 	# return
	;
	
expr
	: expr op = (INC | DEC)			# suffixIncrementDecrement
	| expr '(' paramList? ')'	    # functionCall
	| array = expr '[' index = expr ']'				# indexAccess
	| expr '.' Identifier			# memberAccess
	
	| <assoc = right> op = (INC | DEC) expr 			# prefixIncrementDecrement
	| <assoc = right> op = (ADD | SUB) expr 			# positiveNegative
	| <assoc = right> op = (LOGICNOT | BITNOT) expr 	# not
	| <assoc = right> op = NEW creator					# new

	| left = expr op = (MUL | DIV | MOD) right = expr		#binaryOperation
	| left = expr op = (ADD | SUB) right = expr				#binaryOperation
	| left = expr op = (LSHIFT | RSHIFT) right = expr		#binaryOperation
	| left = expr op = (LT | GT) right = expr				#binaryOperation
	| left = expr op = (LEQ | GEQ) right = expr				#binaryOperation
	| left = expr op = (EQ | NEQ) right = expr				#binaryOperation
	| left = expr op = BITAND right = expr				#binaryOperation
	| left = expr op = BITXOR right = expr				#binaryOperation
	| left = expr op = BITOR right = expr					#binaryOperation
	| left = expr op = LOGICAND right = expr				#binaryOperation
	| left = expr op = LOGICOR right = expr				#binaryOperation
	
	| <assoc = right> expr op = ASSIGN expr #assign
	
	| Identifier		# identifier
	| constant			# const
	| '(' expr ')'		# subExpression
	;
	
creator
	: singleTypeSpec ('[' expr ']')+ Brackets+ ('[' expr ']')+		#creatorError
	| singleTypeSpec ('[' expr ']')+ Brackets*						#creatorArray
	| singleTypeSpec												#creatorSingle
	;
	
constant
	: type = IntegerConst
	| type = CharConst
	| type = StringConst
	| type = BoolConst
	| type = NULL
	;

// Reserved Keys

BOOL : 'bool';
INT : 'int';
CHAR : 'char';
STRING : 'string';
NULL : 'null';
VOID : 'void';
fragment TRUE : 'true';
fragment FALSE : 'false';
IF : 'if';
ELIF : 'else if';
ELSE : 'else';
FOR : 'for';
WHILE : 'while';
BREAK : 'BREAK';
CONTINUE : 'continue';
RETURN : 'return';
NEW : 'new';
CLASS : 'class';
THIS : 'this';

// Symbols

ADD : '+';
SUB : '-';
MUL : '*';
DIV : '/';
MOD : '%';
INC : '++';
DEC : '--';

ASSIGN : '=';

LT : '<';
GT : '>';
EQ : '==';
NEQ : '!=';
LEQ : '<=';
GEQ : '>=';

LOGICAND : '&&';
LOGICOR : '||';
LOGICNOT : '!';

LSHIFT : '<<';
RSHIFT : '>>';	// Arithmetic Right Shift (Complement 1 on the left if negative)
BITNOT : '~';
BITOR : '|';
BITXOR : '^';
BITAND : '&';

// Fragments

fragment
Digit
	: [0-9]
	;

fragment
UpperLetter
	: [A-Z]
	;
	
fragment
LowerLetter
	: [a-z]
	;
	
fragment
Letter
	: UpperLetter | LowerLetter
	;
	
fragment
Underscore
	: '_'
	;
	
fragment
CChar
	: ~['\\\r\n]
	| EscapeSeq
	;

fragment
SChar
	: ~["\\\r\n]
	| EscapeSeq
	;

fragment
EscapeSeq
	: '\\' ['"?abfnrtv\\]
	;

Brackets
    : '[' ']'
    ;

// Constants

IntegerConst
	: [1-9][0-9]*
	| '0'
	;

CharConst
	: '\'' CChar '\''
	;

StringConst
	: '"' SChar* '"'
	;

BoolConst
	: TRUE
	| FALSE
	;
	
// Identifier
Identifier
	: Letter (Digit | Letter | Underscore)*
	;


// Whitespace and comments

WS  
	: [ \t\r\n]+ -> skip
    ;

COMMENT
    : '/*' .*? '*/' -> skip
    ;

LINE_COMMENT
    : '//' ~[\r\n]* -> skip
    ;
    
NEWLINE
	: '\r'? '\n' -> skip
	;
