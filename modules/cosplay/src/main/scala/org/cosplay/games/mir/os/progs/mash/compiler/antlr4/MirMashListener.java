// Generated from /Users/nivanov/cosplay/modules/cosplay/src/main/scala/org/cosplay/games/mir/os/progs/mash/compiler/antlr4/MirMash.g4 by ANTLR 4.10.1
package org.cosplay.games.mir.os.progs.mash.compiler.antlr4;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MirMashParser}.
 */
public interface MirMashListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MirMashParser#mash}.
	 * @param ctx the parse tree
	 */
	void enterMash(MirMashParser.MashContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#mash}.
	 * @param ctx the parse tree
	 */
	void exitMash(MirMashParser.MashContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#decls}.
	 * @param ctx the parse tree
	 */
	void enterDecls(MirMashParser.DeclsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#decls}.
	 * @param ctx the parse tree
	 */
	void exitDecls(MirMashParser.DeclsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#decl}.
	 * @param ctx the parse tree
	 */
	void enterDecl(MirMashParser.DeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#decl}.
	 * @param ctx the parse tree
	 */
	void exitDecl(MirMashParser.DeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#unsetDecl}.
	 * @param ctx the parse tree
	 */
	void enterUnsetDecl(MirMashParser.UnsetDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#unsetDecl}.
	 * @param ctx the parse tree
	 */
	void exitUnsetDecl(MirMashParser.UnsetDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#includeDecl}.
	 * @param ctx the parse tree
	 */
	void enterIncludeDecl(MirMashParser.IncludeDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#includeDecl}.
	 * @param ctx the parse tree
	 */
	void exitIncludeDecl(MirMashParser.IncludeDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#delimDecl}.
	 * @param ctx the parse tree
	 */
	void enterDelimDecl(MirMashParser.DelimDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#delimDecl}.
	 * @param ctx the parse tree
	 */
	void exitDelimDecl(MirMashParser.DelimDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#assignDecl}.
	 * @param ctx the parse tree
	 */
	void enterAssignDecl(MirMashParser.AssignDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#assignDecl}.
	 * @param ctx the parse tree
	 */
	void exitAssignDecl(MirMashParser.AssignDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#pipelineDecl}.
	 * @param ctx the parse tree
	 */
	void enterPipelineDecl(MirMashParser.PipelineDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#pipelineDecl}.
	 * @param ctx the parse tree
	 */
	void exitPipelineDecl(MirMashParser.PipelineDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#prgList}.
	 * @param ctx the parse tree
	 */
	void enterPrgList(MirMashParser.PrgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#prgList}.
	 * @param ctx the parse tree
	 */
	void exitPrgList(MirMashParser.PrgListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#prg}.
	 * @param ctx the parse tree
	 */
	void enterPrg(MirMashParser.PrgContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#prg}.
	 * @param ctx the parse tree
	 */
	void exitPrg(MirMashParser.PrgContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterArg(MirMashParser.ArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitArg(MirMashParser.ArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#argList}.
	 * @param ctx the parse tree
	 */
	void enterArgList(MirMashParser.ArgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#argList}.
	 * @param ctx the parse tree
	 */
	void exitArgList(MirMashParser.ArgListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#pipeOp}.
	 * @param ctx the parse tree
	 */
	void enterPipeOp(MirMashParser.PipeOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#pipeOp}.
	 * @param ctx the parse tree
	 */
	void exitPipeOp(MirMashParser.PipeOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#defCall}.
	 * @param ctx the parse tree
	 */
	void enterDefCall(MirMashParser.DefCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#defCall}.
	 * @param ctx the parse tree
	 */
	void exitDefCall(MirMashParser.DefCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#returnDecl}.
	 * @param ctx the parse tree
	 */
	void enterReturnDecl(MirMashParser.ReturnDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#returnDecl}.
	 * @param ctx the parse tree
	 */
	void exitReturnDecl(MirMashParser.ReturnDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#aliasDecl}.
	 * @param ctx the parse tree
	 */
	void enterAliasDecl(MirMashParser.AliasDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#aliasDecl}.
	 * @param ctx the parse tree
	 */
	void exitAliasDecl(MirMashParser.AliasDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#valDecl}.
	 * @param ctx the parse tree
	 */
	void enterValDecl(MirMashParser.ValDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#valDecl}.
	 * @param ctx the parse tree
	 */
	void exitValDecl(MirMashParser.ValDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void enterVarDecl(MirMashParser.VarDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void exitVarDecl(MirMashParser.VarDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#defDecl}.
	 * @param ctx the parse tree
	 */
	void enterDefDecl(MirMashParser.DefDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#defDecl}.
	 * @param ctx the parse tree
	 */
	void exitDefDecl(MirMashParser.DefDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#defNameDecl}.
	 * @param ctx the parse tree
	 */
	void enterDefNameDecl(MirMashParser.DefNameDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#defNameDecl}.
	 * @param ctx the parse tree
	 */
	void exitDefNameDecl(MirMashParser.DefNameDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#natDefDecl}.
	 * @param ctx the parse tree
	 */
	void enterNatDefDecl(MirMashParser.NatDefDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#natDefDecl}.
	 * @param ctx the parse tree
	 */
	void exitNatDefDecl(MirMashParser.NatDefDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#whileDecl}.
	 * @param ctx the parse tree
	 */
	void enterWhileDecl(MirMashParser.WhileDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#whileDecl}.
	 * @param ctx the parse tree
	 */
	void exitWhileDecl(MirMashParser.WhileDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#whileExprDecl}.
	 * @param ctx the parse tree
	 */
	void enterWhileExprDecl(MirMashParser.WhileExprDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#whileExprDecl}.
	 * @param ctx the parse tree
	 */
	void exitWhileExprDecl(MirMashParser.WhileExprDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#forDecl}.
	 * @param ctx the parse tree
	 */
	void enterForDecl(MirMashParser.ForDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#forDecl}.
	 * @param ctx the parse tree
	 */
	void exitForDecl(MirMashParser.ForDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#forExprDecl}.
	 * @param ctx the parse tree
	 */
	void enterForExprDecl(MirMashParser.ForExprDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#forExprDecl}.
	 * @param ctx the parse tree
	 */
	void exitForExprDecl(MirMashParser.ForExprDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#funParamList}.
	 * @param ctx the parse tree
	 */
	void enterFunParamList(MirMashParser.FunParamListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#funParamList}.
	 * @param ctx the parse tree
	 */
	void exitFunParamList(MirMashParser.FunParamListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#ifThen}.
	 * @param ctx the parse tree
	 */
	void enterIfThen(MirMashParser.IfThenContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#ifThen}.
	 * @param ctx the parse tree
	 */
	void exitIfThen(MirMashParser.IfThenContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#ifElse}.
	 * @param ctx the parse tree
	 */
	void enterIfElse(MirMashParser.IfElseContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#ifElse}.
	 * @param ctx the parse tree
	 */
	void exitIfElse(MirMashParser.IfElseContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#ifDecl}.
	 * @param ctx the parse tree
	 */
	void enterIfDecl(MirMashParser.IfDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#ifDecl}.
	 * @param ctx the parse tree
	 */
	void exitIfDecl(MirMashParser.IfDeclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParExpr(MirMashParser.ParExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParExpr(MirMashParser.ParExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpr(MirMashParser.UnaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpr(MirMashParser.UnaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code compExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCompExpr(MirMashParser.CompExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code compExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCompExpr(MirMashParser.CompExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code plusMinusExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPlusMinusExpr(MirMashParser.PlusMinusExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code plusMinusExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPlusMinusExpr(MirMashParser.PlusMinusExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pipelineExecExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPipelineExecExpr(MirMashParser.PipelineExecExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pipelineExecExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPipelineExecExpr(MirMashParser.PipelineExecExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code atomExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAtomExpr(MirMashParser.AtomExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code atomExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAtomExpr(MirMashParser.AtomExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code multDivModExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMultDivModExpr(MirMashParser.MultDivModExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code multDivModExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMultDivModExpr(MirMashParser.MultDivModExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code andOrExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAndOrExpr(MirMashParser.AndOrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code andOrExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAndOrExpr(MirMashParser.AndOrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCallExpr(MirMashParser.CallExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCallExpr(MirMashParser.CallExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code listExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterListExpr(MirMashParser.ListExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code listExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitListExpr(MirMashParser.ListExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code eqNeqExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterEqNeqExpr(MirMashParser.EqNeqExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code eqNeqExpr}
	 * labeled alternative in {@link MirMashParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitEqNeqExpr(MirMashParser.EqNeqExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#compoundExpr}.
	 * @param ctx the parse tree
	 */
	void enterCompoundExpr(MirMashParser.CompoundExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#compoundExpr}.
	 * @param ctx the parse tree
	 */
	void exitCompoundExpr(MirMashParser.CompoundExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#callParamList}.
	 * @param ctx the parse tree
	 */
	void enterCallParamList(MirMashParser.CallParamListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#callParamList}.
	 * @param ctx the parse tree
	 */
	void exitCallParamList(MirMashParser.CallParamListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#callParam}.
	 * @param ctx the parse tree
	 */
	void enterCallParam(MirMashParser.CallParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#callParam}.
	 * @param ctx the parse tree
	 */
	void exitCallParam(MirMashParser.CallParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(MirMashParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(MirMashParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#list}.
	 * @param ctx the parse tree
	 */
	void enterList(MirMashParser.ListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#list}.
	 * @param ctx the parse tree
	 */
	void exitList(MirMashParser.ListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#listElems}.
	 * @param ctx the parse tree
	 */
	void enterListElems(MirMashParser.ListElemsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#listElems}.
	 * @param ctx the parse tree
	 */
	void exitListElems(MirMashParser.ListElemsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#listElem}.
	 * @param ctx the parse tree
	 */
	void enterListElem(MirMashParser.ListElemContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#listElem}.
	 * @param ctx the parse tree
	 */
	void exitListElem(MirMashParser.ListElemContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirMashParser#qstring}.
	 * @param ctx the parse tree
	 */
	void enterQstring(MirMashParser.QstringContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirMashParser#qstring}.
	 * @param ctx the parse tree
	 */
	void exitQstring(MirMashParser.QstringContext ctx);
}