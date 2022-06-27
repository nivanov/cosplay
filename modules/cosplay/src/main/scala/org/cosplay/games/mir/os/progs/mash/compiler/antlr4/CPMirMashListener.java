// Generated from /Users/nivanov/cosplay/modules/cosplay/src/main/scala/org/cosplay/games/mir/os/progs/mash/compiler/antlr4/CPMirMash.g4 by ANTLR 4.10.1
package org.cosplay.games.mir.os.progs.mash.compiler.antlr4;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CPMirMashParser}.
 */
public interface CPMirMashListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#mash}.
	 * @param ctx the parse tree
	 */
	void enterMash(CPMirMashParser.MashContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#mash}.
	 * @param ctx the parse tree
	 */
	void exitMash(CPMirMashParser.MashContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#decls}.
	 * @param ctx the parse tree
	 */
	void enterDecls(CPMirMashParser.DeclsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#decls}.
	 * @param ctx the parse tree
	 */
	void exitDecls(CPMirMashParser.DeclsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#decl}.
	 * @param ctx the parse tree
	 */
	void enterDecl(CPMirMashParser.DeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#decl}.
	 * @param ctx the parse tree
	 */
	void exitDecl(CPMirMashParser.DeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#valDecl}.
	 * @param ctx the parse tree
	 */
	void enterValDecl(CPMirMashParser.ValDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#valDecl}.
	 * @param ctx the parse tree
	 */
	void exitValDecl(CPMirMashParser.ValDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void enterVarDecl(CPMirMashParser.VarDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void exitVarDecl(CPMirMashParser.VarDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#assignDecl}.
	 * @param ctx the parse tree
	 */
	void enterAssignDecl(CPMirMashParser.AssignDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#assignDecl}.
	 * @param ctx the parse tree
	 */
	void exitAssignDecl(CPMirMashParser.AssignDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#exportDecl}.
	 * @param ctx the parse tree
	 */
	void enterExportDecl(CPMirMashParser.ExportDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#exportDecl}.
	 * @param ctx the parse tree
	 */
	void exitExportDecl(CPMirMashParser.ExportDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#unexportDecl}.
	 * @param ctx the parse tree
	 */
	void enterUnexportDecl(CPMirMashParser.UnexportDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#unexportDecl}.
	 * @param ctx the parse tree
	 */
	void exitUnexportDecl(CPMirMashParser.UnexportDeclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code atomExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAtomExpr(CPMirMashParser.AtomExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code atomExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAtomExpr(CPMirMashParser.AtomExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(CPMirMashParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(CPMirMashParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#qstring}.
	 * @param ctx the parse tree
	 */
	void enterQstring(CPMirMashParser.QstringContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#qstring}.
	 * @param ctx the parse tree
	 */
	void exitQstring(CPMirMashParser.QstringContext ctx);
}