grammar Drainer;

predicate: expression EOF;
expression: expression AND expression # AndExpression
   | expression OR expression # OrExpression
   | NOT expression # NotExpression
   | LPAREN expression RPAREN # ParenExpression
   | variable # VariableExpression;
variable: VARIABLE;

fragment VALID_ID_START: ('a'..'z') | ('A'..'Z') | '_' ;
fragment VALID_ID_CHAR: VALID_ID_START | ('0'..'9');
VARIABLE: VALID_ID_START VALID_ID_CHAR*;
LPAREN: '(';
RPAREN: ')';
AND: '&';
NOT: '!';
OR: '|';
WS: [ \r\n\t] + -> skip;
