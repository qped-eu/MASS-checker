/**
 * Define a grammar called Hello
 */
grammar DrRacket;

@header
{
	import static org.apache.commons.lang3.StringEscapeUtils.escapeXml;
  	import java.util.List;
}

@parser::members
{
	
  public boolean hasCommentsBefore(Token token) {
  	List<Token> hiddenTokens = ((CommonTokenStream) getTokenStream()).
			getHiddenTokensToLeft(token.getTokenIndex(),Token.HIDDEN_CHANNEL);
	if (hiddenTokens == null)
		return false;
	else
		return !hiddenTokens.isEmpty();
  }

  public String commentsBefore(Token token) {
  	StringBuilder result = new StringBuilder();
	for (Token commentToken : ((CommonTokenStream) getTokenStream()).
								getHiddenTokensToLeft(token.getTokenIndex(), Token.HIDDEN_CHANNEL)) {
		result.append(commentToken.getText()).append("\n");
	}
	return result.toString(); 	
  }
  
  public void comments(Token token) {
  	if (hasCommentsBefore(token))
		xml.append("<comment>" + escapeXml(commentsBefore(token)) + "</comment>");
  }
  
  public final StringBuilder xml = new StringBuilder();
}


start : 
{
	xml.append("<?xml version='1.0' encoding='UTF-8' standalone='yes'?>");
	xml.append("<drracket>");
}
expr* 
{
	xml.append("</drracket>");
};

expr : terminal | round_paren | square_paren | quote | quasiquote | unquote | vector;

terminal : 
string_terminal | hash_terminal | true_terminal | false_terminal | symbol_terminal | name_terminal | number_terminal | character_terminal | lambda_terminal;

string_terminal : STRING
{
	comments($STRING);
	xml.append("<terminal value='" + escapeXml($STRING.text) + "' line='" + $STRING.line + "' type='String'/>");
} ;

hash_terminal : HASH_NAME
{
	comments($HASH_NAME);
	xml.append("<terminal value='" + escapeXml($HASH_NAME.text) + "' line='" + $HASH_NAME.line + "' type='HashName'/>");
} ;

true_terminal : TRUE
{
    comments($TRUE);
    xml.append("<terminal value='true' line='" + $TRUE.line + "' type='Boolean'/>");
} ;

false_terminal : FALSE
{
    comments($FALSE);
    xml.append("<terminal value='false' line='" + $FALSE.line + "' type='Boolean'/>");
} ;

symbol_terminal : SYMBOL
{
    comments($SYMBOL);
    xml.append("<terminal value='" + escapeXml($SYMBOL.text) + "' line='" + $SYMBOL.line + "' type='Symbol'/>");
} ;

lambda_terminal : LAMBDA
{
    comments($LAMBDA);
    xml.append("<terminal value='lambda' line='" + $LAMBDA.line + "' type='Name'/>");
} ;

name_terminal : NAME
{
    comments($NAME);
    xml.append("<terminal value='" + escapeXml($NAME.text) + "' line='" + $NAME.line + "' type='Name'/>");
} ;

number_terminal : NUMBER
{
    comments($NUMBER);
    xml.append("<terminal value='" + escapeXml($NUMBER.text) + "' line='" + $NUMBER.line + "' type='Number'/>");
} ;

character_terminal : CHARACTER
{
    comments($CHARACTER);
    xml.append("<terminal value='" + escapeXml($CHARACTER.text) + "' line='" + $CHARACTER.line + "' type='Character' />");
} ;

round_paren : t='(' 
{
	comments($t);
	xml.append("<paren type='round' line='" + $t.line+ "'>");
}
expr*

')'
{
	xml.append("</paren>");
} ;

square_paren : t='[' 
{
	comments($t);
	xml.append("<paren type='square' line='" + $t.line+ "'>");
}
expr*

']'
{
	xml.append("</paren>");
} ;

quote : t=QUOTE 
{
	comments($t);
	xml.append("<quote line='" + $t.line+ "'>");
}
expr
{
	xml.append("</quote>");
} ;

quasiquote : t='`' 
{
	comments($t);
	xml.append("<quasiquote line='" + $t.line+ "'>");
}
expr
{
	xml.append("</quasiquote>");
} ;

unquote : t=',' 
{
	comments($t);
	xml.append("<unquote line='" + $t.line+ "'>");
}
expr
{
	xml.append("</unquote>");
} ;

vector : t='#' 
{
	comments($t);
	xml.append("<vector line='" + $t.line+ "'>");
}
expr
{
	xml.append("</vector>");
} ;

TRUE : '#true'| '#t' | '#T' | 'true';

FALSE : '#false'| '#f' | '#F' | 'false';

// A symbol is a quote character followed by a name. A symbol is a value, just like 42, '(), or #false.
SYMBOL
    : '\'' NAME
    ;
    
QUOTE
    : '\''
    ; 

// A number is a number such as 123, 3/2, or 5.5.
NUMBER
    : '-'? 
    ( INT
    | INT '.' [0-9]* [1-9]
    | INT '/' INT)
    ;

INT: [1-9] [0-9]*
   | '0'
   ;

// A string is a sequence of characters enclosed by a pair of ".
// Unlike symbols, strings may be split into characters and manipulated by a variety of functions.
// For example, "abcdef", "This is a string", and "This is a string with \" inside" are all strings.
STRING:
    '"' ( '""' | '\\"' | ~["] )* '"'
    ;
    

// A character begins with #\ and has the name of the character.
// For example, #\a, #\b, and #\space are characters.
CHARACTER
    : '#' '\u005C' [A-Za-z0-9]
    | '#' '\u005C' 'space'
    ;

LAMBDA 
    : 'λ'
    ;

NAME :
    ~[",'`()[\]{}|;#\p{White_Space}]+
    ;

HASH_NAME : '#' NAME;

WS : [\p{White_Space}]+ -> skip;

COMMENT
  :  ';' ~[\r\n]* -> channel(HIDDEN);
  