// Generated from io\bitbucket\plt\autotutor\DrRacket.g4 by ANTLR 4.10.1
package eu.qped.Temp;

	import static org.apache.commons.lang3.StringEscapeUtils.escapeXml;
  	import java.util.List;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DrRacketParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, TRUE=8, FALSE=9, 
		SYMBOL=10, QUOTE=11, NUMBER=12, INT=13, STRING=14, CHARACTER=15, LAMBDA=16, 
		NAME=17, HASH_NAME=18, WS=19, COMMENT=20;
	public static final int
		RULE_start = 0, RULE_expr = 1, RULE_terminal = 2, RULE_string_terminal = 3, 
		RULE_hash_terminal = 4, RULE_true_terminal = 5, RULE_false_terminal = 6, 
		RULE_symbol_terminal = 7, RULE_lambda_terminal = 8, RULE_name_terminal = 9, 
		RULE_number_terminal = 10, RULE_character_terminal = 11, RULE_round_paren = 12, 
		RULE_square_paren = 13, RULE_quote = 14, RULE_quasiquote = 15, RULE_unquote = 16, 
		RULE_vector = 17;
	private static String[] makeRuleNames() {
		return new String[] {
			"start", "expr", "terminal", "string_terminal", "hash_terminal", "true_terminal", 
			"false_terminal", "symbol_terminal", "lambda_terminal", "name_terminal", 
			"number_terminal", "character_terminal", "round_paren", "square_paren", 
			"quote", "quasiquote", "unquote", "vector"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'['", "']'", "'`'", "','", "'#'", null, null, null, 
			"'''", null, null, null, null, "'\\u03BB'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, "TRUE", "FALSE", "SYMBOL", 
			"QUOTE", "NUMBER", "INT", "STRING", "CHARACTER", "LAMBDA", "NAME", "HASH_NAME", 
			"WS", "COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "DrRacket.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


		
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

	public DrRacketParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class StartContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public StartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).enterStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).exitStart(this);
		}
	}

	public final StartContext start() throws RecognitionException {
		StartContext _localctx = new StartContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_start);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{

				xml.append("<?xml version='1.0' encoding='UTF-8' standalone='yes'?>");
				xml.append("<drracket>");

			setState(40);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__2) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << TRUE) | (1L << FALSE) | (1L << SYMBOL) | (1L << QUOTE) | (1L << NUMBER) | (1L << STRING) | (1L << CHARACTER) | (1L << LAMBDA) | (1L << NAME) | (1L << HASH_NAME))) != 0)) {
				{
				{
				setState(37);
				expr();
				}
				}
				setState(42);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}

				xml.append("</drracket>");

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public TerminalContext terminal() {
			return getRuleContext(TerminalContext.class,0);
		}
		public Round_parenContext round_paren() {
			return getRuleContext(Round_parenContext.class,0);
		}
		public Square_parenContext square_paren() {
			return getRuleContext(Square_parenContext.class,0);
		}
		public QuoteContext quote() {
			return getRuleContext(QuoteContext.class,0);
		}
		public QuasiquoteContext quasiquote() {
			return getRuleContext(QuasiquoteContext.class,0);
		}
		public UnquoteContext unquote() {
			return getRuleContext(UnquoteContext.class,0);
		}
		public VectorContext vector() {
			return getRuleContext(VectorContext.class,0);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).exitExpr(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_expr);
		try {
			setState(52);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TRUE:
			case FALSE:
			case SYMBOL:
			case NUMBER:
			case STRING:
			case CHARACTER:
			case LAMBDA:
			case NAME:
			case HASH_NAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(45);
				terminal();
				}
				break;
			case T__0:
				enterOuterAlt(_localctx, 2);
				{
				setState(46);
				round_paren();
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 3);
				{
				setState(47);
				square_paren();
				}
				break;
			case QUOTE:
				enterOuterAlt(_localctx, 4);
				{
				setState(48);
				quote();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 5);
				{
				setState(49);
				quasiquote();
				}
				break;
			case T__5:
				enterOuterAlt(_localctx, 6);
				{
				setState(50);
				unquote();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 7);
				{
				setState(51);
				vector();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TerminalContext extends ParserRuleContext {
		public String_terminalContext string_terminal() {
			return getRuleContext(String_terminalContext.class,0);
		}
		public Hash_terminalContext hash_terminal() {
			return getRuleContext(Hash_terminalContext.class,0);
		}
		public True_terminalContext true_terminal() {
			return getRuleContext(True_terminalContext.class,0);
		}
		public False_terminalContext false_terminal() {
			return getRuleContext(False_terminalContext.class,0);
		}
		public Symbol_terminalContext symbol_terminal() {
			return getRuleContext(Symbol_terminalContext.class,0);
		}
		public Name_terminalContext name_terminal() {
			return getRuleContext(Name_terminalContext.class,0);
		}
		public Number_terminalContext number_terminal() {
			return getRuleContext(Number_terminalContext.class,0);
		}
		public Character_terminalContext character_terminal() {
			return getRuleContext(Character_terminalContext.class,0);
		}
		public Lambda_terminalContext lambda_terminal() {
			return getRuleContext(Lambda_terminalContext.class,0);
		}
		public TerminalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_terminal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).enterTerminal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).exitTerminal(this);
		}
	}

	public final TerminalContext terminal() throws RecognitionException {
		TerminalContext _localctx = new TerminalContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_terminal);
		try {
			setState(63);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(54);
				string_terminal();
				}
				break;
			case HASH_NAME:
				enterOuterAlt(_localctx, 2);
				{
				setState(55);
				hash_terminal();
				}
				break;
			case TRUE:
				enterOuterAlt(_localctx, 3);
				{
				setState(56);
				true_terminal();
				}
				break;
			case FALSE:
				enterOuterAlt(_localctx, 4);
				{
				setState(57);
				false_terminal();
				}
				break;
			case SYMBOL:
				enterOuterAlt(_localctx, 5);
				{
				setState(58);
				symbol_terminal();
				}
				break;
			case NAME:
				enterOuterAlt(_localctx, 6);
				{
				setState(59);
				name_terminal();
				}
				break;
			case NUMBER:
				enterOuterAlt(_localctx, 7);
				{
				setState(60);
				number_terminal();
				}
				break;
			case CHARACTER:
				enterOuterAlt(_localctx, 8);
				{
				setState(61);
				character_terminal();
				}
				break;
			case LAMBDA:
				enterOuterAlt(_localctx, 9);
				{
				setState(62);
				lambda_terminal();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class String_terminalContext extends ParserRuleContext {
		public Token STRING;
		public TerminalNode STRING() { return getToken(DrRacketParser.STRING, 0); }
		public String_terminalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string_terminal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).enterString_terminal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).exitString_terminal(this);
		}
	}

	public final String_terminalContext string_terminal() throws RecognitionException {
		String_terminalContext _localctx = new String_terminalContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_string_terminal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			((String_terminalContext)_localctx).STRING = match(STRING);

				comments(((String_terminalContext)_localctx).STRING);
				xml.append("<terminal value='" + escapeXml((((String_terminalContext)_localctx).STRING!=null?((String_terminalContext)_localctx).STRING.getText():null)) + "' line='" + (((String_terminalContext)_localctx).STRING!=null?((String_terminalContext)_localctx).STRING.getLine():0) + "' type='String'/>");

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Hash_terminalContext extends ParserRuleContext {
		public Token HASH_NAME;
		public TerminalNode HASH_NAME() { return getToken(DrRacketParser.HASH_NAME, 0); }
		public Hash_terminalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hash_terminal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).enterHash_terminal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).exitHash_terminal(this);
		}
	}

	public final Hash_terminalContext hash_terminal() throws RecognitionException {
		Hash_terminalContext _localctx = new Hash_terminalContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_hash_terminal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			((Hash_terminalContext)_localctx).HASH_NAME = match(HASH_NAME);

				comments(((Hash_terminalContext)_localctx).HASH_NAME);
				xml.append("<terminal value='" + escapeXml((((Hash_terminalContext)_localctx).HASH_NAME!=null?((Hash_terminalContext)_localctx).HASH_NAME.getText():null)) + "' line='" + (((Hash_terminalContext)_localctx).HASH_NAME!=null?((Hash_terminalContext)_localctx).HASH_NAME.getLine():0) + "' type='HashName'/>");

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class True_terminalContext extends ParserRuleContext {
		public Token TRUE;
		public TerminalNode TRUE() { return getToken(DrRacketParser.TRUE, 0); }
		public True_terminalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_true_terminal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).enterTrue_terminal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).exitTrue_terminal(this);
		}
	}

	public final True_terminalContext true_terminal() throws RecognitionException {
		True_terminalContext _localctx = new True_terminalContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_true_terminal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			((True_terminalContext)_localctx).TRUE = match(TRUE);

			    comments(((True_terminalContext)_localctx).TRUE);
			    xml.append("<terminal value='true' line='" + (((True_terminalContext)_localctx).TRUE!=null?((True_terminalContext)_localctx).TRUE.getLine():0) + "' type='Boolean'/>");

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class False_terminalContext extends ParserRuleContext {
		public Token FALSE;
		public TerminalNode FALSE() { return getToken(DrRacketParser.FALSE, 0); }
		public False_terminalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_false_terminal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).enterFalse_terminal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).exitFalse_terminal(this);
		}
	}

	public final False_terminalContext false_terminal() throws RecognitionException {
		False_terminalContext _localctx = new False_terminalContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_false_terminal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			((False_terminalContext)_localctx).FALSE = match(FALSE);

			    comments(((False_terminalContext)_localctx).FALSE);
			    xml.append("<terminal value='false' line='" + (((False_terminalContext)_localctx).FALSE!=null?((False_terminalContext)_localctx).FALSE.getLine():0) + "' type='Boolean'/>");

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Symbol_terminalContext extends ParserRuleContext {
		public Token SYMBOL;
		public TerminalNode SYMBOL() { return getToken(DrRacketParser.SYMBOL, 0); }
		public Symbol_terminalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_symbol_terminal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).enterSymbol_terminal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).exitSymbol_terminal(this);
		}
	}

	public final Symbol_terminalContext symbol_terminal() throws RecognitionException {
		Symbol_terminalContext _localctx = new Symbol_terminalContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_symbol_terminal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77);
			((Symbol_terminalContext)_localctx).SYMBOL = match(SYMBOL);

			    comments(((Symbol_terminalContext)_localctx).SYMBOL);
			    xml.append("<terminal value='" + escapeXml((((Symbol_terminalContext)_localctx).SYMBOL!=null?((Symbol_terminalContext)_localctx).SYMBOL.getText():null)) + "' line='" + (((Symbol_terminalContext)_localctx).SYMBOL!=null?((Symbol_terminalContext)_localctx).SYMBOL.getLine():0) + "' type='Symbol'/>");

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Lambda_terminalContext extends ParserRuleContext {
		public Token LAMBDA;
		public TerminalNode LAMBDA() { return getToken(DrRacketParser.LAMBDA, 0); }
		public Lambda_terminalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambda_terminal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).enterLambda_terminal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).exitLambda_terminal(this);
		}
	}

	public final Lambda_terminalContext lambda_terminal() throws RecognitionException {
		Lambda_terminalContext _localctx = new Lambda_terminalContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_lambda_terminal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			((Lambda_terminalContext)_localctx).LAMBDA = match(LAMBDA);

			    comments(((Lambda_terminalContext)_localctx).LAMBDA);
			    xml.append("<terminal value='lambda' line='" + (((Lambda_terminalContext)_localctx).LAMBDA!=null?((Lambda_terminalContext)_localctx).LAMBDA.getLine():0) + "' type='Name'/>");

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Name_terminalContext extends ParserRuleContext {
		public Token NAME;
		public TerminalNode NAME() { return getToken(DrRacketParser.NAME, 0); }
		public Name_terminalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name_terminal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).enterName_terminal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).exitName_terminal(this);
		}
	}

	public final Name_terminalContext name_terminal() throws RecognitionException {
		Name_terminalContext _localctx = new Name_terminalContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_name_terminal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(83);
			((Name_terminalContext)_localctx).NAME = match(NAME);

			    comments(((Name_terminalContext)_localctx).NAME);
			    xml.append("<terminal value='" + escapeXml((((Name_terminalContext)_localctx).NAME!=null?((Name_terminalContext)_localctx).NAME.getText():null)) + "' line='" + (((Name_terminalContext)_localctx).NAME!=null?((Name_terminalContext)_localctx).NAME.getLine():0) + "' type='Name'/>");

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Number_terminalContext extends ParserRuleContext {
		public Token NUMBER;
		public TerminalNode NUMBER() { return getToken(DrRacketParser.NUMBER, 0); }
		public Number_terminalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number_terminal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).enterNumber_terminal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).exitNumber_terminal(this);
		}
	}

	public final Number_terminalContext number_terminal() throws RecognitionException {
		Number_terminalContext _localctx = new Number_terminalContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_number_terminal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86);
			((Number_terminalContext)_localctx).NUMBER = match(NUMBER);

			    comments(((Number_terminalContext)_localctx).NUMBER);
			    xml.append("<terminal value='" + escapeXml((((Number_terminalContext)_localctx).NUMBER!=null?((Number_terminalContext)_localctx).NUMBER.getText():null)) + "' line='" + (((Number_terminalContext)_localctx).NUMBER!=null?((Number_terminalContext)_localctx).NUMBER.getLine():0) + "' type='Number'/>");

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Character_terminalContext extends ParserRuleContext {
		public Token CHARACTER;
		public TerminalNode CHARACTER() { return getToken(DrRacketParser.CHARACTER, 0); }
		public Character_terminalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_character_terminal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).enterCharacter_terminal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).exitCharacter_terminal(this);
		}
	}

	public final Character_terminalContext character_terminal() throws RecognitionException {
		Character_terminalContext _localctx = new Character_terminalContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_character_terminal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			((Character_terminalContext)_localctx).CHARACTER = match(CHARACTER);

			    comments(((Character_terminalContext)_localctx).CHARACTER);
			    xml.append("<terminal value='" + escapeXml((((Character_terminalContext)_localctx).CHARACTER!=null?((Character_terminalContext)_localctx).CHARACTER.getText():null)) + "' line='" + (((Character_terminalContext)_localctx).CHARACTER!=null?((Character_terminalContext)_localctx).CHARACTER.getLine():0) + "' type='Character' />");

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Round_parenContext extends ParserRuleContext {
		public Token t;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public Round_parenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_round_paren; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).enterRound_paren(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).exitRound_paren(this);
		}
	}

	public final Round_parenContext round_paren() throws RecognitionException {
		Round_parenContext _localctx = new Round_parenContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_round_paren);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
			((Round_parenContext)_localctx).t = match(T__0);

				comments(((Round_parenContext)_localctx).t);
				xml.append("<paren type='round' line='" + (((Round_parenContext)_localctx).t!=null?((Round_parenContext)_localctx).t.getLine():0)+ "'>");

			setState(97);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__2) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << TRUE) | (1L << FALSE) | (1L << SYMBOL) | (1L << QUOTE) | (1L << NUMBER) | (1L << STRING) | (1L << CHARACTER) | (1L << LAMBDA) | (1L << NAME) | (1L << HASH_NAME))) != 0)) {
				{
				{
				setState(94);
				expr();
				}
				}
				setState(99);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(100);
			match(T__1);

				xml.append("</paren>");

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Square_parenContext extends ParserRuleContext {
		public Token t;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public Square_parenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_square_paren; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).enterSquare_paren(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).exitSquare_paren(this);
		}
	}

	public final Square_parenContext square_paren() throws RecognitionException {
		Square_parenContext _localctx = new Square_parenContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_square_paren);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			((Square_parenContext)_localctx).t = match(T__2);

				comments(((Square_parenContext)_localctx).t);
				xml.append("<paren type='square' line='" + (((Square_parenContext)_localctx).t!=null?((Square_parenContext)_localctx).t.getLine():0)+ "'>");

			setState(108);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__2) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << TRUE) | (1L << FALSE) | (1L << SYMBOL) | (1L << QUOTE) | (1L << NUMBER) | (1L << STRING) | (1L << CHARACTER) | (1L << LAMBDA) | (1L << NAME) | (1L << HASH_NAME))) != 0)) {
				{
				{
				setState(105);
				expr();
				}
				}
				setState(110);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(111);
			match(T__3);

				xml.append("</paren>");

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QuoteContext extends ParserRuleContext {
		public Token t;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode QUOTE() { return getToken(DrRacketParser.QUOTE, 0); }
		public QuoteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quote; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).enterQuote(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).exitQuote(this);
		}
	}

	public final QuoteContext quote() throws RecognitionException {
		QuoteContext _localctx = new QuoteContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_quote);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
			((QuoteContext)_localctx).t = match(QUOTE);

				comments(((QuoteContext)_localctx).t);
				xml.append("<quote line='" + (((QuoteContext)_localctx).t!=null?((QuoteContext)_localctx).t.getLine():0)+ "'>");

			setState(116);
			expr();

				xml.append("</quote>");

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QuasiquoteContext extends ParserRuleContext {
		public Token t;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public QuasiquoteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quasiquote; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).enterQuasiquote(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).exitQuasiquote(this);
		}
	}

	public final QuasiquoteContext quasiquote() throws RecognitionException {
		QuasiquoteContext _localctx = new QuasiquoteContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_quasiquote);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(119);
			((QuasiquoteContext)_localctx).t = match(T__4);

				comments(((QuasiquoteContext)_localctx).t);
				xml.append("<quasiquote line='" + (((QuasiquoteContext)_localctx).t!=null?((QuasiquoteContext)_localctx).t.getLine():0)+ "'>");

			setState(121);
			expr();

				xml.append("</quasiquote>");

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnquoteContext extends ParserRuleContext {
		public Token t;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public UnquoteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unquote; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).enterUnquote(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).exitUnquote(this);
		}
	}

	public final UnquoteContext unquote() throws RecognitionException {
		UnquoteContext _localctx = new UnquoteContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_unquote);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
			((UnquoteContext)_localctx).t = match(T__5);

				comments(((UnquoteContext)_localctx).t);
				xml.append("<unquote line='" + (((UnquoteContext)_localctx).t!=null?((UnquoteContext)_localctx).t.getLine():0)+ "'>");

			setState(126);
			expr();

				xml.append("</unquote>");

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VectorContext extends ParserRuleContext {
		public Token t;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public VectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_vector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).enterVector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DrRacketListener ) ((DrRacketListener)listener).exitVector(this);
		}
	}

	public final VectorContext vector() throws RecognitionException {
		VectorContext _localctx = new VectorContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_vector);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			((VectorContext)_localctx).t = match(T__6);

				comments(((VectorContext)_localctx).t);
				xml.append("<vector line='" + (((VectorContext)_localctx).t!=null?((VectorContext)_localctx).t.getLine():0)+ "'>");

			setState(131);
			expr();

				xml.append("</vector>");

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0014\u0087\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"+
		"\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0001\u0000\u0001"+
		"\u0000\u0005\u0000\'\b\u0000\n\u0000\f\u0000*\t\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0003\u00015\b\u0001\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0003\u0002@\b\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0005\f`\b\f\n"+
		"\f\f\fc\t\f\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0005\rk\b"+
		"\r\n\r\f\rn\t\r\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0000\u0000\u0012\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012"+
		"\u0014\u0016\u0018\u001a\u001c\u001e \"\u0000\u0000\u0085\u0000$\u0001"+
		"\u0000\u0000\u0000\u00024\u0001\u0000\u0000\u0000\u0004?\u0001\u0000\u0000"+
		"\u0000\u0006A\u0001\u0000\u0000\u0000\bD\u0001\u0000\u0000\u0000\nG\u0001"+
		"\u0000\u0000\u0000\fJ\u0001\u0000\u0000\u0000\u000eM\u0001\u0000\u0000"+
		"\u0000\u0010P\u0001\u0000\u0000\u0000\u0012S\u0001\u0000\u0000\u0000\u0014"+
		"V\u0001\u0000\u0000\u0000\u0016Y\u0001\u0000\u0000\u0000\u0018\\\u0001"+
		"\u0000\u0000\u0000\u001ag\u0001\u0000\u0000\u0000\u001cr\u0001\u0000\u0000"+
		"\u0000\u001ew\u0001\u0000\u0000\u0000 |\u0001\u0000\u0000\u0000\"\u0081"+
		"\u0001\u0000\u0000\u0000$(\u0006\u0000\uffff\uffff\u0000%\'\u0003\u0002"+
		"\u0001\u0000&%\u0001\u0000\u0000\u0000\'*\u0001\u0000\u0000\u0000(&\u0001"+
		"\u0000\u0000\u0000()\u0001\u0000\u0000\u0000)+\u0001\u0000\u0000\u0000"+
		"*(\u0001\u0000\u0000\u0000+,\u0006\u0000\uffff\uffff\u0000,\u0001\u0001"+
		"\u0000\u0000\u0000-5\u0003\u0004\u0002\u0000.5\u0003\u0018\f\u0000/5\u0003"+
		"\u001a\r\u000005\u0003\u001c\u000e\u000015\u0003\u001e\u000f\u000025\u0003"+
		" \u0010\u000035\u0003\"\u0011\u00004-\u0001\u0000\u0000\u00004.\u0001"+
		"\u0000\u0000\u00004/\u0001\u0000\u0000\u000040\u0001\u0000\u0000\u0000"+
		"41\u0001\u0000\u0000\u000042\u0001\u0000\u0000\u000043\u0001\u0000\u0000"+
		"\u00005\u0003\u0001\u0000\u0000\u00006@\u0003\u0006\u0003\u00007@\u0003"+
		"\b\u0004\u00008@\u0003\n\u0005\u00009@\u0003\f\u0006\u0000:@\u0003\u000e"+
		"\u0007\u0000;@\u0003\u0012\t\u0000<@\u0003\u0014\n\u0000=@\u0003\u0016"+
		"\u000b\u0000>@\u0003\u0010\b\u0000?6\u0001\u0000\u0000\u0000?7\u0001\u0000"+
		"\u0000\u0000?8\u0001\u0000\u0000\u0000?9\u0001\u0000\u0000\u0000?:\u0001"+
		"\u0000\u0000\u0000?;\u0001\u0000\u0000\u0000?<\u0001\u0000\u0000\u0000"+
		"?=\u0001\u0000\u0000\u0000?>\u0001\u0000\u0000\u0000@\u0005\u0001\u0000"+
		"\u0000\u0000AB\u0005\u000e\u0000\u0000BC\u0006\u0003\uffff\uffff\u0000"+
		"C\u0007\u0001\u0000\u0000\u0000DE\u0005\u0012\u0000\u0000EF\u0006\u0004"+
		"\uffff\uffff\u0000F\t\u0001\u0000\u0000\u0000GH\u0005\b\u0000\u0000HI"+
		"\u0006\u0005\uffff\uffff\u0000I\u000b\u0001\u0000\u0000\u0000JK\u0005"+
		"\t\u0000\u0000KL\u0006\u0006\uffff\uffff\u0000L\r\u0001\u0000\u0000\u0000"+
		"MN\u0005\n\u0000\u0000NO\u0006\u0007\uffff\uffff\u0000O\u000f\u0001\u0000"+
		"\u0000\u0000PQ\u0005\u0010\u0000\u0000QR\u0006\b\uffff\uffff\u0000R\u0011"+
		"\u0001\u0000\u0000\u0000ST\u0005\u0011\u0000\u0000TU\u0006\t\uffff\uffff"+
		"\u0000U\u0013\u0001\u0000\u0000\u0000VW\u0005\f\u0000\u0000WX\u0006\n"+
		"\uffff\uffff\u0000X\u0015\u0001\u0000\u0000\u0000YZ\u0005\u000f\u0000"+
		"\u0000Z[\u0006\u000b\uffff\uffff\u0000[\u0017\u0001\u0000\u0000\u0000"+
		"\\]\u0005\u0001\u0000\u0000]a\u0006\f\uffff\uffff\u0000^`\u0003\u0002"+
		"\u0001\u0000_^\u0001\u0000\u0000\u0000`c\u0001\u0000\u0000\u0000a_\u0001"+
		"\u0000\u0000\u0000ab\u0001\u0000\u0000\u0000bd\u0001\u0000\u0000\u0000"+
		"ca\u0001\u0000\u0000\u0000de\u0005\u0002\u0000\u0000ef\u0006\f\uffff\uffff"+
		"\u0000f\u0019\u0001\u0000\u0000\u0000gh\u0005\u0003\u0000\u0000hl\u0006"+
		"\r\uffff\uffff\u0000ik\u0003\u0002\u0001\u0000ji\u0001\u0000\u0000\u0000"+
		"kn\u0001\u0000\u0000\u0000lj\u0001\u0000\u0000\u0000lm\u0001\u0000\u0000"+
		"\u0000mo\u0001\u0000\u0000\u0000nl\u0001\u0000\u0000\u0000op\u0005\u0004"+
		"\u0000\u0000pq\u0006\r\uffff\uffff\u0000q\u001b\u0001\u0000\u0000\u0000"+
		"rs\u0005\u000b\u0000\u0000st\u0006\u000e\uffff\uffff\u0000tu\u0003\u0002"+
		"\u0001\u0000uv\u0006\u000e\uffff\uffff\u0000v\u001d\u0001\u0000\u0000"+
		"\u0000wx\u0005\u0005\u0000\u0000xy\u0006\u000f\uffff\uffff\u0000yz\u0003"+
		"\u0002\u0001\u0000z{\u0006\u000f\uffff\uffff\u0000{\u001f\u0001\u0000"+
		"\u0000\u0000|}\u0005\u0006\u0000\u0000}~\u0006\u0010\uffff\uffff\u0000"+
		"~\u007f\u0003\u0002\u0001\u0000\u007f\u0080\u0006\u0010\uffff\uffff\u0000"+
		"\u0080!\u0001\u0000\u0000\u0000\u0081\u0082\u0005\u0007\u0000\u0000\u0082"+
		"\u0083\u0006\u0011\uffff\uffff\u0000\u0083\u0084\u0003\u0002\u0001\u0000"+
		"\u0084\u0085\u0006\u0011\uffff\uffff\u0000\u0085#\u0001\u0000\u0000\u0000"+
		"\u0005(4?al";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}