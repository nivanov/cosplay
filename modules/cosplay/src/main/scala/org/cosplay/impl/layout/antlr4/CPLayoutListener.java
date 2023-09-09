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
	 * Enter a parse tree produced by {@link CPLayoutParser#items}.
	 * @param ctx the parse tree
	 */
	void enterItems(CPLayoutParser.ItemsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPLayoutParser#items}.
	 * @param ctx the parse tree
	 */
	void exitItems(CPLayoutParser.ItemsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPLayoutParser#item}.
	 * @param ctx the parse tree
	 */
	void enterItem(CPLayoutParser.ItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPLayoutParser#item}.
	 * @param ctx the parse tree
	 */
	void exitItem(CPLayoutParser.ItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPLayoutParser#padItem}.
	 * @param ctx the parse tree
	 */
	void enterPadItem(CPLayoutParser.PadItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPLayoutParser#padItem}.
	 * @param ctx the parse tree
	 */
	void exitPadItem(CPLayoutParser.PadItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPLayoutParser#posItem}.
	 * @param ctx the parse tree
	 */
	void enterPosItem(CPLayoutParser.PosItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPLayoutParser#posItem}.
	 * @param ctx the parse tree
	 */
	void exitPosItem(CPLayoutParser.PosItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPLayoutParser#floatItem}.
	 * @param ctx the parse tree
	 */
	void enterFloatItem(CPLayoutParser.FloatItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPLayoutParser#floatItem}.
	 * @param ctx the parse tree
	 */
	void exitFloatItem(CPLayoutParser.FloatItemContext ctx);
}