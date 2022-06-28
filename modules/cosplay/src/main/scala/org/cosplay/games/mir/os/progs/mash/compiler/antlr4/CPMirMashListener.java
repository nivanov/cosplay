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
	 * Enter a parse tree produced by {@link CPMirMashParser#defDecl}.
	 * @param ctx the parse tree
	 */
	void enterDefDecl(CPMirMashParser.DefDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#defDecl}.
	 * @param ctx the parse tree
	 */
	void exitDefDecl(CPMirMashParser.DefDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#nativeDefDecl}.
	 * @param ctx the parse tree
	 */
	void enterNativeDefDecl(CPMirMashParser.NativeDefDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#nativeDefDecl}.
	 * @param ctx the parse tree
	 */
	void exitNativeDefDecl(CPMirMashParser.NativeDefDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#whileDecl}.
	 * @param ctx the parse tree
	 */
	void enterWhileDecl(CPMirMashParser.WhileDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#whileDecl}.
	 * @param ctx the parse tree
	 */
	void exitWhileDecl(CPMirMashParser.WhileDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#forDecl}.
	 * @param ctx the parse tree
	 */
	void enterForDecl(CPMirMashParser.ForDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#forDecl}.
	 * @param ctx the parse tree
	 */
	void exitForDecl(CPMirMashParser.ForDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#funParamList}.
	 * @param ctx the parse tree
	 */
	void enterFunParamList(CPMirMashParser.FunParamListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#funParamList}.
	 * @param ctx the parse tree
	 */
	void exitFunParamList(CPMirMashParser.FunParamListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fpCallExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFpCallExpr(CPMirMashParser.FpCallExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fpCallExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFpCallExpr(CPMirMashParser.FpCallExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code modExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterModExpr(CPMirMashParser.ModExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code modExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitModExpr(CPMirMashParser.ModExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code varAccessExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterVarAccessExpr(CPMirMashParser.VarAccessExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code varAccessExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitVarAccessExpr(CPMirMashParser.VarAccessExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code plusMinusExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPlusMinusExpr(CPMirMashParser.PlusMinusExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code plusMinusExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPlusMinusExpr(CPMirMashParser.PlusMinusExprContext ctx);
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
	 * Enter a parse tree produced by the {@code mapExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMapExpr(CPMirMashParser.MapExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mapExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMapExpr(CPMirMashParser.MapExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code anonDefExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAnonDefExpr(CPMirMashParser.AnonDefExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code anonDefExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAnonDefExpr(CPMirMashParser.AnonDefExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParExpr(CPMirMashParser.ParExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParExpr(CPMirMashParser.ParExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpr(CPMirMashParser.UnaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpr(CPMirMashParser.UnaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forYieldExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterForYieldExpr(CPMirMashParser.ForYieldExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forYieldExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitForYieldExpr(CPMirMashParser.ForYieldExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code compExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCompExpr(CPMirMashParser.CompExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code compExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCompExpr(CPMirMashParser.CompExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIfExpr(CPMirMashParser.IfExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIfExpr(CPMirMashParser.IfExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code multDivModExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMultDivModExpr(CPMirMashParser.MultDivModExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code multDivModExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMultDivModExpr(CPMirMashParser.MultDivModExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code andOrExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAndOrExpr(CPMirMashParser.AndOrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code andOrExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAndOrExpr(CPMirMashParser.AndOrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCallExpr(CPMirMashParser.CallExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCallExpr(CPMirMashParser.CallExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code listExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterListExpr(CPMirMashParser.ListExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code listExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitListExpr(CPMirMashParser.ListExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code eqNeqExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterEqNeqExpr(CPMirMashParser.EqNeqExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code eqNeqExpr}
	 * labeled alternative in {@link CPMirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitEqNeqExpr(CPMirMashParser.EqNeqExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#listItems}.
	 * @param ctx the parse tree
	 */
	void enterListItems(CPMirMashParser.ListItemsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#listItems}.
	 * @param ctx the parse tree
	 */
	void exitListItems(CPMirMashParser.ListItemsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#mapItem}.
	 * @param ctx the parse tree
	 */
	void enterMapItem(CPMirMashParser.MapItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#mapItem}.
	 * @param ctx the parse tree
	 */
	void exitMapItem(CPMirMashParser.MapItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#mapItems}.
	 * @param ctx the parse tree
	 */
	void enterMapItems(CPMirMashParser.MapItemsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#mapItems}.
	 * @param ctx the parse tree
	 */
	void exitMapItems(CPMirMashParser.MapItemsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#compoundExpr}.
	 * @param ctx the parse tree
	 */
	void enterCompoundExpr(CPMirMashParser.CompoundExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#compoundExpr}.
	 * @param ctx the parse tree
	 */
	void exitCompoundExpr(CPMirMashParser.CompoundExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#callParamList}.
	 * @param ctx the parse tree
	 */
	void enterCallParamList(CPMirMashParser.CallParamListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#callParamList}.
	 * @param ctx the parse tree
	 */
	void exitCallParamList(CPMirMashParser.CallParamListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#varAccess}.
	 * @param ctx the parse tree
	 */
	void enterVarAccess(CPMirMashParser.VarAccessContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#varAccess}.
	 * @param ctx the parse tree
	 */
	void exitVarAccess(CPMirMashParser.VarAccessContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashParser#keyAccess}.
	 * @param ctx the parse tree
	 */
	void enterKeyAccess(CPMirMashParser.KeyAccessContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashParser#keyAccess}.
	 * @param ctx the parse tree
	 */
	void exitKeyAccess(CPMirMashParser.KeyAccessContext ctx);
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