// Generated from /Users/nivanov/cosplay/modules/cosplay/src/main/scala/org/cosplay/games/mir/os/progs/mash/compiler/antlr4/CPMirMashCmd.g4 by ANTLR 4.10.1
package org.cosplay.games.mir.os.progs.mash.compiler.antlr4;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CPMirMashCmdParser}.
 */
public interface CPMirMashCmdListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CPMirMashCmdParser#mashCmd}.
	 * @param ctx the parse tree
	 */
	void enterMashCmd(CPMirMashCmdParser.MashCmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashCmdParser#mashCmd}.
	 * @param ctx the parse tree
	 */
	void exitMashCmd(CPMirMashCmdParser.MashCmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashCmdParser#pipeline}.
	 * @param ctx the parse tree
	 */
	void enterPipeline(CPMirMashCmdParser.PipelineContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashCmdParser#pipeline}.
	 * @param ctx the parse tree
	 */
	void exitPipeline(CPMirMashCmdParser.PipelineContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashCmdParser#prg}.
	 * @param ctx the parse tree
	 */
	void enterPrg(CPMirMashCmdParser.PrgContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashCmdParser#prg}.
	 * @param ctx the parse tree
	 */
	void exitPrg(CPMirMashCmdParser.PrgContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashCmdParser#argList}.
	 * @param ctx the parse tree
	 */
	void enterArgList(CPMirMashCmdParser.ArgListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashCmdParser#argList}.
	 * @param ctx the parse tree
	 */
	void exitArgList(CPMirMashCmdParser.ArgListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashCmdParser#str}.
	 * @param ctx the parse tree
	 */
	void enterStr(CPMirMashCmdParser.StrContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashCmdParser#str}.
	 * @param ctx the parse tree
	 */
	void exitStr(CPMirMashCmdParser.StrContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPMirMashCmdParser#op}.
	 * @param ctx the parse tree
	 */
	void enterOp(CPMirMashCmdParser.OpContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPMirMashCmdParser#op}.
	 * @param ctx the parse tree
	 */
	void exitOp(CPMirMashCmdParser.OpContext ctx);
}