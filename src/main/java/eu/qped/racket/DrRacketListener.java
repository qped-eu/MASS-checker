// Generated from io\bitbucket\plt\autotutor\DrRacket.g4 by ANTLR 4.10.1
package eu.qped.racket;

	import static org.apache.commons.lang3.StringEscapeUtils.escapeXml;
  	import java.util.List;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DrRacketParser}.
 */
public interface DrRacketListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DrRacketParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(DrRacketParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link DrRacketParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(DrRacketParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link DrRacketParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(DrRacketParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link DrRacketParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(DrRacketParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link DrRacketParser#terminal}.
	 * @param ctx the parse tree
	 */
	void enterTerminal(DrRacketParser.TerminalContext ctx);
	/**
	 * Exit a parse tree produced by {@link DrRacketParser#terminal}.
	 * @param ctx the parse tree
	 */
	void exitTerminal(DrRacketParser.TerminalContext ctx);
	/**
	 * Enter a parse tree produced by {@link DrRacketParser#string_terminal}.
	 * @param ctx the parse tree
	 */
	void enterString_terminal(DrRacketParser.String_terminalContext ctx);
	/**
	 * Exit a parse tree produced by {@link DrRacketParser#string_terminal}.
	 * @param ctx the parse tree
	 */
	void exitString_terminal(DrRacketParser.String_terminalContext ctx);
	/**
	 * Enter a parse tree produced by {@link DrRacketParser#hash_terminal}.
	 * @param ctx the parse tree
	 */
	void enterHash_terminal(DrRacketParser.Hash_terminalContext ctx);
	/**
	 * Exit a parse tree produced by {@link DrRacketParser#hash_terminal}.
	 * @param ctx the parse tree
	 */
	void exitHash_terminal(DrRacketParser.Hash_terminalContext ctx);
	/**
	 * Enter a parse tree produced by {@link DrRacketParser#true_terminal}.
	 * @param ctx the parse tree
	 */
	void enterTrue_terminal(DrRacketParser.True_terminalContext ctx);
	/**
	 * Exit a parse tree produced by {@link DrRacketParser#true_terminal}.
	 * @param ctx the parse tree
	 */
	void exitTrue_terminal(DrRacketParser.True_terminalContext ctx);
	/**
	 * Enter a parse tree produced by {@link DrRacketParser#false_terminal}.
	 * @param ctx the parse tree
	 */
	void enterFalse_terminal(DrRacketParser.False_terminalContext ctx);
	/**
	 * Exit a parse tree produced by {@link DrRacketParser#false_terminal}.
	 * @param ctx the parse tree
	 */
	void exitFalse_terminal(DrRacketParser.False_terminalContext ctx);
	/**
	 * Enter a parse tree produced by {@link DrRacketParser#symbol_terminal}.
	 * @param ctx the parse tree
	 */
	void enterSymbol_terminal(DrRacketParser.Symbol_terminalContext ctx);
	/**
	 * Exit a parse tree produced by {@link DrRacketParser#symbol_terminal}.
	 * @param ctx the parse tree
	 */
	void exitSymbol_terminal(DrRacketParser.Symbol_terminalContext ctx);
	/**
	 * Enter a parse tree produced by {@link DrRacketParser#lambda_terminal}.
	 * @param ctx the parse tree
	 */
	void enterLambda_terminal(DrRacketParser.Lambda_terminalContext ctx);
	/**
	 * Exit a parse tree produced by {@link DrRacketParser#lambda_terminal}.
	 * @param ctx the parse tree
	 */
	void exitLambda_terminal(DrRacketParser.Lambda_terminalContext ctx);
	/**
	 * Enter a parse tree produced by {@link DrRacketParser#name_terminal}.
	 * @param ctx the parse tree
	 */
	void enterName_terminal(DrRacketParser.Name_terminalContext ctx);
	/**
	 * Exit a parse tree produced by {@link DrRacketParser#name_terminal}.
	 * @param ctx the parse tree
	 */
	void exitName_terminal(DrRacketParser.Name_terminalContext ctx);
	/**
	 * Enter a parse tree produced by {@link DrRacketParser#number_terminal}.
	 * @param ctx the parse tree
	 */
	void enterNumber_terminal(DrRacketParser.Number_terminalContext ctx);
	/**
	 * Exit a parse tree produced by {@link DrRacketParser#number_terminal}.
	 * @param ctx the parse tree
	 */
	void exitNumber_terminal(DrRacketParser.Number_terminalContext ctx);
	/**
	 * Enter a parse tree produced by {@link DrRacketParser#character_terminal}.
	 * @param ctx the parse tree
	 */
	void enterCharacter_terminal(DrRacketParser.Character_terminalContext ctx);
	/**
	 * Exit a parse tree produced by {@link DrRacketParser#character_terminal}.
	 * @param ctx the parse tree
	 */
	void exitCharacter_terminal(DrRacketParser.Character_terminalContext ctx);
	/**
	 * Enter a parse tree produced by {@link DrRacketParser#round_paren}.
	 * @param ctx the parse tree
	 */
	void enterRound_paren(DrRacketParser.Round_parenContext ctx);
	/**
	 * Exit a parse tree produced by {@link DrRacketParser#round_paren}.
	 * @param ctx the parse tree
	 */
	void exitRound_paren(DrRacketParser.Round_parenContext ctx);
	/**
	 * Enter a parse tree produced by {@link DrRacketParser#square_paren}.
	 * @param ctx the parse tree
	 */
	void enterSquare_paren(DrRacketParser.Square_parenContext ctx);
	/**
	 * Exit a parse tree produced by {@link DrRacketParser#square_paren}.
	 * @param ctx the parse tree
	 */
	void exitSquare_paren(DrRacketParser.Square_parenContext ctx);
	/**
	 * Enter a parse tree produced by {@link DrRacketParser#quote}.
	 * @param ctx the parse tree
	 */
	void enterQuote(DrRacketParser.QuoteContext ctx);
	/**
	 * Exit a parse tree produced by {@link DrRacketParser#quote}.
	 * @param ctx the parse tree
	 */
	void exitQuote(DrRacketParser.QuoteContext ctx);
	/**
	 * Enter a parse tree produced by {@link DrRacketParser#quasiquote}.
	 * @param ctx the parse tree
	 */
	void enterQuasiquote(DrRacketParser.QuasiquoteContext ctx);
	/**
	 * Exit a parse tree produced by {@link DrRacketParser#quasiquote}.
	 * @param ctx the parse tree
	 */
	void exitQuasiquote(DrRacketParser.QuasiquoteContext ctx);
	/**
	 * Enter a parse tree produced by {@link DrRacketParser#unquote}.
	 * @param ctx the parse tree
	 */
	void enterUnquote(DrRacketParser.UnquoteContext ctx);
	/**
	 * Exit a parse tree produced by {@link DrRacketParser#unquote}.
	 * @param ctx the parse tree
	 */
	void exitUnquote(DrRacketParser.UnquoteContext ctx);
	/**
	 * Enter a parse tree produced by {@link DrRacketParser#vector}.
	 * @param ctx the parse tree
	 */
	void enterVector(DrRacketParser.VectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link DrRacketParser#vector}.
	 * @param ctx the parse tree
	 */
	void exitVector(DrRacketParser.VectorContext ctx);
}