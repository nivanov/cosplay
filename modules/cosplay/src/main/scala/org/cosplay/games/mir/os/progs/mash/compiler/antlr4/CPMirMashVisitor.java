// Generated from /Users/nivanov/cosplay/modules/cosplay/src/main/scala/org/cosplay/games/mir/os/progs/mash/compiler/antlr4/CPMirMash.g4 by ANTLR 4.10.1
package org.cosplay.games.mir.os.progs.mash.compiler.antlr4;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CPMirMashParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CPMirMashVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#mash}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMash(CPMirMashParser.MashContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#decls}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecls(CPMirMashParser.DeclsContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl(CPMirMashParser.DeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#valDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValDecl(CPMirMashParser.ValDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#varDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDecl(CPMirMashParser.VarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#assignDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignDecl(CPMirMashParser.AssignDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#exportDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExportDecl(CPMirMashParser.ExportDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#unexportDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnexportDecl(CPMirMashParser.UnexportDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code atomExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomExpr(CPMirMashParser.AtomExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(CPMirMashParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#qstring}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQstring(CPMirMashParser.QstringContext ctx);
}