// Generated from /Users/nivanov/cosplay/modules/cosplay/src/main/scala/org/cosplay/games/mir/os/progs/asm/compiler/antlr4/MirAsm.g4 by ANTLR 4.10.1
package org.cosplay.games.mir.os.progs.asm.compiler.antlr4;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MirAsmParser}.
 */
public interface MirAsmListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MirAsmParser#asm}.
	 * @param ctx the parse tree
	 */
	void enterAsm(MirAsmParser.AsmContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirAsmParser#asm}.
	 * @param ctx the parse tree
	 */
	void exitAsm(MirAsmParser.AsmContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirAsmParser#code}.
	 * @param ctx the parse tree
	 */
	void enterCode(MirAsmParser.CodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirAsmParser#code}.
	 * @param ctx the parse tree
	 */
	void exitCode(MirAsmParser.CodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirAsmParser#inst}.
	 * @param ctx the parse tree
	 */
	void enterInst(MirAsmParser.InstContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirAsmParser#inst}.
	 * @param ctx the parse tree
	 */
	void exitInst(MirAsmParser.InstContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirAsmParser#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(MirAsmParser.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirAsmParser#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(MirAsmParser.LabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirAsmParser#plist}.
	 * @param ctx the parse tree
	 */
	void enterPlist(MirAsmParser.PlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirAsmParser#plist}.
	 * @param ctx the parse tree
	 */
	void exitPlist(MirAsmParser.PlistContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirAsmParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(MirAsmParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirAsmParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(MirAsmParser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link MirAsmParser#qstring}.
	 * @param ctx the parse tree
	 */
	void enterQstring(MirAsmParser.QstringContext ctx);
	/**
	 * Exit a parse tree produced by {@link MirAsmParser#qstring}.
	 * @param ctx the parse tree
	 */
	void exitQstring(MirAsmParser.QstringContext ctx);
}