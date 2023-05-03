grammar Drainer;

predicate: expression EOF;
expression: variable # VariableExpression
  | wildVariable # WildVariableExpression
  | NOT expression # NotExpression
  | LPAREN expression RPAREN # ParenExpression
  | expression AND expression # AndExpression
  | expression OR expression # OrExpression;
variable: VARIABLE;
wildVariable: WILD_VARIABLE;

fragment VALID_VARIABLE_CHAR: ('0'..'9') | ('a'..'z') | ('A'..'Z') | UNDERSCORE | DOT;
fragment VALID_WILD_VARIABLE_CHAR: VALID_VARIABLE_CHAR | ASTERISK | QUESTION_MARK;

VARIABLE: VALID_VARIABLE_CHAR VALID_VARIABLE_CHAR*;
WILD_VARIABLE: VALID_WILD_VARIABLE_CHAR VALID_WILD_VARIABLE_CHAR*;
LPAREN: '(';
RPAREN: ')';
AND: '&';
NOT: '!';
OR: '|';
UNDERSCORE: '_';
DOT: '.';
QUESTION_MARK: '?';
ASTERISK: '*';
WS: [ \r\n\t] + -> skip;
