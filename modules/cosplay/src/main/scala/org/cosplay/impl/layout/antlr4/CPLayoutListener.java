// Generated from /Users/nivanov/cosplay/modules/cosplay/src/main/scala/org/cosplay/impl/layout/antlr4/CPLayout.g4 by ANTLR 4.13.1
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
	 * Enter a parse tree produced by {@link CPLayoutParser#marginItem}.
	 * @param ctx the parse tree
	 */
	void enterMarginItem(CPLayoutParser.MarginItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPLayoutParser#marginItem}.
	 * @param ctx the parse tree
	 */
	void exitMarginItem(CPLayoutParser.MarginItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPLayoutParser#xItem}.
	 * @param ctx the parse tree
	 */
	void enterXItem(CPLayoutParser.XItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPLayoutParser#xItem}.
	 * @param ctx the parse tree
	 */
	void exitXItem(CPLayoutParser.XItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPLayoutParser#yItem}.
	 * @param ctx the parse tree
	 */
	void enterYItem(CPLayoutParser.YItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPLayoutParser#yItem}.
	 * @param ctx the parse tree
	 */
	void exitYItem(CPLayoutParser.YItemContext ctx);
}