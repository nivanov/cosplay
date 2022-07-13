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
	 * Visit a parse tree produced by {@link CPMirMashParser#execDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecDecl(CPMirMashParser.ExecDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#unexportDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnexportDecl(CPMirMashParser.UnexportDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#defDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefDecl(CPMirMashParser.DefDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#nativeDefDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNativeDefDecl(CPMirMashParser.NativeDefDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#whileDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileDecl(CPMirMashParser.WhileDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#forDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForDecl(CPMirMashParser.ForDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#funParamList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunParamList(CPMirMashParser.FunParamListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code fpCallExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFpCallExpr(CPMirMashParser.FpCallExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code modExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModExpr(CPMirMashParser.ModExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code varAccessExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarAccessExpr(CPMirMashParser.VarAccessExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code plusMinusExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlusMinusExpr(CPMirMashParser.PlusMinusExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code atomExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomExpr(CPMirMashParser.AtomExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mapExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapExpr(CPMirMashParser.MapExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code anonDefExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnonDefExpr(CPMirMashParser.AnonDefExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParExpr(CPMirMashParser.ParExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpr(CPMirMashParser.UnaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code execExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecExpr(CPMirMashParser.ExecExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code forYieldExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForYieldExpr(CPMirMashParser.ForYieldExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code compExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompExpr(CPMirMashParser.CompExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfExpr(CPMirMashParser.IfExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code multDivModExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultDivModExpr(CPMirMashParser.MultDivModExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code andOrExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndOrExpr(CPMirMashParser.AndOrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallExpr(CPMirMashParser.CallExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code listExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListExpr(CPMirMashParser.ListExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code eqNeqExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqNeqExpr(CPMirMashParser.EqNeqExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#listItems}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListItems(CPMirMashParser.ListItemsContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#mapItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapItem(CPMirMashParser.MapItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#mapItems}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMapItems(CPMirMashParser.MapItemsContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#compoundExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompoundExpr(CPMirMashParser.CompoundExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#callParamList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallParamList(CPMirMashParser.CallParamListContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#varAccess}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarAccess(CPMirMashParser.VarAccessContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPMirMashParser#keyAccess}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyAccess(CPMirMashParser.KeyAccessContext ctx);
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