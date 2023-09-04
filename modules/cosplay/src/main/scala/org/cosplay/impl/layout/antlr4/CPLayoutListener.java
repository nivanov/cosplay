// Generated from /Users/nivanov/cosplay/modules/cosplay/src/main/scala/org/cosplay/impl/layout/antlr4/CPLayout.g4 by ANTLR 4.12.0
package org.cosplay.impl.layout.antlr4;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CPLayoutParser}.
 */
public interface CPLayoutListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CPLayoutParser#layout}.
	 * @param ctx the parse tree
	 */
	void enterLayout(CPLayoutParser.LayoutContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPLayoutParser#layout}.
	 * @param ctx the parse tree
	 */
	void exitLayout(CPLayoutParser.LayoutContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPLayoutParser#decls}.
	 * @param ctx the parse tree
	 */
	void enterDecls(CPLayoutParser.DeclsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPLayoutParser#decls}.
	 * @param ctx the parse tree
	 */
	void exitDecls(CPLayoutParser.DeclsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPLayoutParser#decl}.
	 * @param ctx the parse tree
	 */
	void enterDecl(CPLayoutParser.DeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPLayoutParser#decl}.
	 * @param ctx the parse tree
	 */
	void exitDecl(CPLayoutParser.DeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPLayoutParser#specs}.
	 * @param ctx the parse tree
	 */
	void enterSpecs(CPLayoutParser.SpecsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPLayoutParser#specs}.
	 * @param ctx the parse tree
	 */
	void exitSpecs(CPLayoutParser.SpecsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPLayoutParser#spec}.
	 * @param ctx the parse tree
	 */
	void enterSpec(CPLayoutParser.SpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPLayoutParser#spec}.
	 * @param ctx the parse tree
	 */
	void exitSpec(CPLayoutParser.SpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPLayoutParser#padSpec}.
	 * @param ctx the parse tree
	 */
	void enterPadSpec(CPLayoutParser.PadSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPLayoutParser#padSpec}.
	 * @param ctx the parse tree
	 */
	void exitPadSpec(CPLayoutParser.PadSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPLayoutParser#posSpec}.
	 * @param ctx the parse tree
	 */
	void enterPosSpec(CPLayoutParser.PosSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPLayoutParser#posSpec}.
	 * @param ctx the parse tree
	 */
	void exitPosSpec(CPLayoutParser.PosSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPLayoutParser#floatSpec}.
	 * @param ctx the parse tree
	 */
	void enterFloatSpec(CPLayoutParser.FloatSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPLayoutParser#floatSpec}.
	 * @param ctx the parse tree
	 */
	void exitFloatSpec(CPLayoutParser.FloatSpecContext ctx);
}