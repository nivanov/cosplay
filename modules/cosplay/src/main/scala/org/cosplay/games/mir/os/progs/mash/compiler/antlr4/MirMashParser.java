// Generated from C:/Users/Nikita Ivanov/Documents/GitHub/cosplay/modules/cosplay/src/main/scala/org/cosplay/games/mir/os/progs/mash/compiler/antlr4\MirMash.g4 by ANTLR 4.10.1
package org.cosplay.games.mir.os.progs.mash.compiler.antlr4;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MirMashParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ALIAS=1, INCLUDE=2, VAL=3, VAR=4, SET=5, UNSET=6, DEF=7, NATIVE=8, RETURN=9, 
		IF=10, THEN=11, ELSE=12, WHILE=13, DO=14, FOR=15, IN=16, LIST_START=17, 
		LIST_END=18, SQSTRING=19, DQSTRING=20, BOOL=21, NULL=22, EQ=23, NEQ=24, 
		GTEQ=25, LTEQ=26, GT=27, LT=28, AND=29, AMP=30, APPEND_FILE=31, OR=32, 
		VERT=33, NOT=34, LPAR=35, RPAR=36, LBRACE=37, RBRACE=38, SQUOTE=39, DQUOTE=40, 
		BQUOTE=41, COMMA=42, MINUS=43, DOT=44, ASSIGN=45, PLUS=46, MULT=47, SCOL=48, 
		DIV=49, MOD=50, STR=51, COMMENT=52, WS=53, ErrorChar=54;
	public static final int
		RULE_mash = 0, RULE_decls = 1, RULE_decl = 2, RULE_unsetDecl = 3, RULE_includeDecl = 4, 
		RULE_delimDecl = 5, RULE_assignDecl = 6, RULE_pipelineDecl = 7, RULE_prgList = 8, 
		RULE_prg = 9, RULE_arg = 10, RULE_argList = 11, RULE_pipeOp = 12, RULE_defCall = 13, 
		RULE_returnDecl = 14, RULE_aliasDecl = 15, RULE_valDecl = 16, RULE_varDecl = 17, 
		RULE_defDecl = 18, RULE_defNameDecl = 19, RULE_natDefDecl = 20, RULE_whileDecl = 21, 
		RULE_whileExprDecl = 22, RULE_forDecl = 23, RULE_forExprDecl = 24, RULE_funParamList = 25, 
		RULE_ifThen = 26, RULE_ifElse = 27, RULE_ifDecl = 28, RULE_expr = 29, 
		RULE_compoundExpr = 30, RULE_callParamList = 31, RULE_callParam = 32, 
		RULE_atom = 33, RULE_list = 34, RULE_listElems = 35, RULE_listElem = 36, 
		RULE_qstring = 37;
	private static String[] makeRuleNames() {
		return new String[] {
			"mash", "decls", "decl", "unsetDecl", "includeDecl", "delimDecl", "assignDecl", 
			"pipelineDecl", "prgList", "prg", "arg", "argList", "pipeOp", "defCall", 
			"returnDecl", "aliasDecl", "valDecl", "varDecl", "defDecl", "defNameDecl", 
			"natDefDecl", "whileDecl", "whileExprDecl", "forDecl", "forExprDecl", 
			"funParamList", "ifThen", "ifElse", "ifDecl", "expr", "compoundExpr", 
			"callParamList", "callParam", "atom", "list", "listElems", "listElem", 
			"qstring"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'alias'", "'include'", "'val'", "'var'", "'set'", "'unset'", "'def'", 
			"'native'", "'return'", "'if'", "'then'", "'else'", "'while'", "'do'", 
			"'for'", "'<-'", "'['", "']'", null, null, null, "'null'", "'=='", "'!='", 
			"'>='", "'<='", "'>'", "'<'", "'&&'", "'&'", "'>>'", "'||'", "'|'", "'!'", 
			"'('", "')'", "'{'", "'}'", "'''", "'\"'", "'`'", "','", "'-'", "'.'", 
			"'='", "'+'", "'*'", "';'", "'/'", "'%'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ALIAS", "INCLUDE", "VAL", "VAR", "SET", "UNSET", "DEF", "NATIVE", 
			"RETURN", "IF", "THEN", "ELSE", "WHILE", "DO", "FOR", "IN", "LIST_START", 
			"LIST_END", "SQSTRING", "DQSTRING", "BOOL", "NULL", "EQ", "NEQ", "GTEQ", 
			"LTEQ", "GT", "LT", "AND", "AMP", "APPEND_FILE", "OR", "VERT", "NOT", 
			"LPAR", "RPAR", "LBRACE", "RBRACE", "SQUOTE", "DQUOTE", "BQUOTE", "COMMA", 
			"MINUS", "DOT", "ASSIGN", "PLUS", "MULT", "SCOL", "DIV", "MOD", "STR", 
			"COMMENT", "WS", "ErrorChar"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "MirMash.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MirMashParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class MashContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(MirMashParser.EOF, 0); }
		public List<DeclsContext> decls() {
			return getRuleContexts(DeclsContext.class);
		}
		public DeclsContext decls(int i) {
			return getRuleContext(DeclsContext.class,i);
		}
		public MashContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mash; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterMash(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitMash(this);
		}
	}

	public final MashContext mash() throws RecognitionException {
		MashContext _localctx = new MashContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_mash);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALIAS) | (1L << INCLUDE) | (1L << VAL) | (1L << VAR) | (1L << SET) | (1L << UNSET) | (1L << DEF) | (1L << NATIVE) | (1L << RETURN) | (1L << IF) | (1L << WHILE) | (1L << FOR) | (1L << SQSTRING) | (1L << DQSTRING) | (1L << SCOL) | (1L << STR))) != 0)) {
				{
				{
				setState(76);
				decls(0);
				}
				}
				setState(81);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(82);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclsContext extends ParserRuleContext {
		public DeclContext decl() {
			return getRuleContext(DeclContext.class,0);
		}
		public DeclsContext decls() {
			return getRuleContext(DeclsContext.class,0);
		}
		public DeclsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decls; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterDecls(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitDecls(this);
		}
	}

	public final DeclsContext decls() throws RecognitionException {
		return decls(0);
	}

	private DeclsContext decls(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		DeclsContext _localctx = new DeclsContext(_ctx, _parentState);
		DeclsContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_decls, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(85);
			decl();
			}
			_ctx.stop = _input.LT(-1);
			setState(91);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new DeclsContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_decls);
					setState(87);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(88);
					decl();
					}
					} 
				}
				setState(93);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class DeclContext extends ParserRuleContext {
		public VarDeclContext varDecl() {
			return getRuleContext(VarDeclContext.class,0);
		}
		public ValDeclContext valDecl() {
			return getRuleContext(ValDeclContext.class,0);
		}
		public DelimDeclContext delimDecl() {
			return getRuleContext(DelimDeclContext.class,0);
		}
		public UnsetDeclContext unsetDecl() {
			return getRuleContext(UnsetDeclContext.class,0);
		}
		public DefDeclContext defDecl() {
			return getRuleContext(DefDeclContext.class,0);
		}
		public NatDefDeclContext natDefDecl() {
			return getRuleContext(NatDefDeclContext.class,0);
		}
		public WhileDeclContext whileDecl() {
			return getRuleContext(WhileDeclContext.class,0);
		}
		public ForDeclContext forDecl() {
			return getRuleContext(ForDeclContext.class,0);
		}
		public AliasDeclContext aliasDecl() {
			return getRuleContext(AliasDeclContext.class,0);
		}
		public AssignDeclContext assignDecl() {
			return getRuleContext(AssignDeclContext.class,0);
		}
		public IncludeDeclContext includeDecl() {
			return getRuleContext(IncludeDeclContext.class,0);
		}
		public IfDeclContext ifDecl() {
			return getRuleContext(IfDeclContext.class,0);
		}
		public ReturnDeclContext returnDecl() {
			return getRuleContext(ReturnDeclContext.class,0);
		}
		public PipelineDeclContext pipelineDecl() {
			return getRuleContext(PipelineDeclContext.class,0);
		}
		public DefCallContext defCall() {
			return getRuleContext(DefCallContext.class,0);
		}
		public DeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitDecl(this);
		}
	}

	public final DeclContext decl() throws RecognitionException {
		DeclContext _localctx = new DeclContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_decl);
		try {
			setState(109);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(94);
				varDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(95);
				valDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(96);
				delimDecl();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(97);
				unsetDecl();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(98);
				defDecl();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(99);
				natDefDecl();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(100);
				whileDecl();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(101);
				forDecl();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(102);
				aliasDecl();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(103);
				assignDecl();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(104);
				includeDecl();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(105);
				ifDecl();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(106);
				returnDecl();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(107);
				pipelineDecl();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(108);
				defCall();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnsetDeclContext extends ParserRuleContext {
		public TerminalNode UNSET() { return getToken(MirMashParser.UNSET, 0); }
		public TerminalNode STR() { return getToken(MirMashParser.STR, 0); }
		public UnsetDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unsetDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterUnsetDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitUnsetDecl(this);
		}
	}

	public final UnsetDeclContext unsetDecl() throws RecognitionException {
		UnsetDeclContext _localctx = new UnsetDeclContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_unsetDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(111);
			match(UNSET);
			setState(112);
			match(STR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IncludeDeclContext extends ParserRuleContext {
		public TerminalNode INCLUDE() { return getToken(MirMashParser.INCLUDE, 0); }
		public QstringContext qstring() {
			return getRuleContext(QstringContext.class,0);
		}
		public IncludeDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_includeDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterIncludeDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitIncludeDecl(this);
		}
	}

	public final IncludeDeclContext includeDecl() throws RecognitionException {
		IncludeDeclContext _localctx = new IncludeDeclContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_includeDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
			match(INCLUDE);
			setState(115);
			qstring();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DelimDeclContext extends ParserRuleContext {
		public TerminalNode SCOL() { return getToken(MirMashParser.SCOL, 0); }
		public DelimDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_delimDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterDelimDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitDelimDecl(this);
		}
	}

	public final DelimDeclContext delimDecl() throws RecognitionException {
		DelimDeclContext _localctx = new DelimDeclContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_delimDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			match(SCOL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssignDeclContext extends ParserRuleContext {
		public TerminalNode STR() { return getToken(MirMashParser.STR, 0); }
		public TerminalNode ASSIGN() { return getToken(MirMashParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public AssignDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterAssignDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitAssignDecl(this);
		}
	}

	public final AssignDeclContext assignDecl() throws RecognitionException {
		AssignDeclContext _localctx = new AssignDeclContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_assignDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(119);
			match(STR);
			setState(120);
			match(ASSIGN);
			setState(121);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PipelineDeclContext extends ParserRuleContext {
		public PrgListContext prgList() {
			return getRuleContext(PrgListContext.class,0);
		}
		public TerminalNode AMP() { return getToken(MirMashParser.AMP, 0); }
		public PipelineDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pipelineDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterPipelineDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitPipelineDecl(this);
		}
	}

	public final PipelineDeclContext pipelineDecl() throws RecognitionException {
		PipelineDeclContext _localctx = new PipelineDeclContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_pipelineDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(123);
			prgList(0);
			setState(125);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				{
				setState(124);
				match(AMP);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrgListContext extends ParserRuleContext {
		public PrgContext prg() {
			return getRuleContext(PrgContext.class,0);
		}
		public PrgListContext prgList() {
			return getRuleContext(PrgListContext.class,0);
		}
		public PipeOpContext pipeOp() {
			return getRuleContext(PipeOpContext.class,0);
		}
		public PrgListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prgList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterPrgList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitPrgList(this);
		}
	}

	public final PrgListContext prgList() throws RecognitionException {
		return prgList(0);
	}

	private PrgListContext prgList(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		PrgListContext _localctx = new PrgListContext(_ctx, _parentState);
		PrgListContext _prevctx = _localctx;
		int _startState = 16;
		enterRecursionRule(_localctx, 16, RULE_prgList, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(128);
			prg();
			}
			_ctx.stop = _input.LT(-1);
			setState(136);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new PrgListContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_prgList);
					setState(130);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(131);
					pipeOp();
					setState(132);
					prg();
					}
					} 
				}
				setState(138);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class PrgContext extends ParserRuleContext {
		public TerminalNode STR() { return getToken(MirMashParser.STR, 0); }
		public QstringContext qstring() {
			return getRuleContext(QstringContext.class,0);
		}
		public ArgListContext argList() {
			return getRuleContext(ArgListContext.class,0);
		}
		public PrgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prg; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterPrg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitPrg(this);
		}
	}

	public final PrgContext prg() throws RecognitionException {
		PrgContext _localctx = new PrgContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_prg);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STR:
				{
				setState(139);
				match(STR);
				}
				break;
			case SQSTRING:
			case DQSTRING:
				{
				setState(140);
				qstring();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(144);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				setState(143);
				argList(0);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgContext extends ParserRuleContext {
		public TerminalNode STR() { return getToken(MirMashParser.STR, 0); }
		public QstringContext qstring() {
			return getRuleContext(QstringContext.class,0);
		}
		public ArgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arg; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterArg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitArg(this);
		}
	}

	public final ArgContext arg() throws RecognitionException {
		ArgContext _localctx = new ArgContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_arg);
		try {
			setState(148);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STR:
				enterOuterAlt(_localctx, 1);
				{
				setState(146);
				match(STR);
				}
				break;
			case SQSTRING:
			case DQSTRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(147);
				qstring();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgListContext extends ParserRuleContext {
		public ArgContext arg() {
			return getRuleContext(ArgContext.class,0);
		}
		public ArgListContext argList() {
			return getRuleContext(ArgListContext.class,0);
		}
		public ArgListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterArgList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitArgList(this);
		}
	}

	public final ArgListContext argList() throws RecognitionException {
		return argList(0);
	}

	private ArgListContext argList(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ArgListContext _localctx = new ArgListContext(_ctx, _parentState);
		ArgListContext _prevctx = _localctx;
		int _startState = 22;
		enterRecursionRule(_localctx, 22, RULE_argList, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(151);
			arg();
			}
			_ctx.stop = _input.LT(-1);
			setState(157);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ArgListContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_argList);
					setState(153);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(154);
					arg();
					}
					} 
				}
				setState(159);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class PipeOpContext extends ParserRuleContext {
		public TerminalNode VERT() { return getToken(MirMashParser.VERT, 0); }
		public TerminalNode GT() { return getToken(MirMashParser.GT, 0); }
		public TerminalNode APPEND_FILE() { return getToken(MirMashParser.APPEND_FILE, 0); }
		public PipeOpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pipeOp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterPipeOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitPipeOp(this);
		}
	}

	public final PipeOpContext pipeOp() throws RecognitionException {
		PipeOpContext _localctx = new PipeOpContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_pipeOp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GT) | (1L << APPEND_FILE) | (1L << VERT))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DefCallContext extends ParserRuleContext {
		public TerminalNode STR() { return getToken(MirMashParser.STR, 0); }
		public TerminalNode LPAR() { return getToken(MirMashParser.LPAR, 0); }
		public TerminalNode RPAR() { return getToken(MirMashParser.RPAR, 0); }
		public CallParamListContext callParamList() {
			return getRuleContext(CallParamListContext.class,0);
		}
		public DefCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterDefCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitDefCall(this);
		}
	}

	public final DefCallContext defCall() throws RecognitionException {
		DefCallContext _localctx = new DefCallContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_defCall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			match(STR);
			setState(163);
			match(LPAR);
			setState(165);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LIST_START) | (1L << SQSTRING) | (1L << DQSTRING) | (1L << BOOL) | (1L << NULL) | (1L << NOT) | (1L << LPAR) | (1L << BQUOTE) | (1L << MINUS) | (1L << STR))) != 0)) {
				{
				setState(164);
				callParamList(0);
				}
			}

			setState(167);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReturnDeclContext extends ParserRuleContext {
		public TerminalNode RETURN() { return getToken(MirMashParser.RETURN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ReturnDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterReturnDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitReturnDecl(this);
		}
	}

	public final ReturnDeclContext returnDecl() throws RecognitionException {
		ReturnDeclContext _localctx = new ReturnDeclContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_returnDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			match(RETURN);
			setState(171);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(170);
				expr(0);
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AliasDeclContext extends ParserRuleContext {
		public TerminalNode ALIAS() { return getToken(MirMashParser.ALIAS, 0); }
		public TerminalNode STR() { return getToken(MirMashParser.STR, 0); }
		public TerminalNode ASSIGN() { return getToken(MirMashParser.ASSIGN, 0); }
		public QstringContext qstring() {
			return getRuleContext(QstringContext.class,0);
		}
		public AliasDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aliasDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterAliasDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitAliasDecl(this);
		}
	}

	public final AliasDeclContext aliasDecl() throws RecognitionException {
		AliasDeclContext _localctx = new AliasDeclContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_aliasDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(173);
			match(ALIAS);
			setState(174);
			match(STR);
			setState(175);
			match(ASSIGN);
			setState(176);
			qstring();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValDeclContext extends ParserRuleContext {
		public TerminalNode VAL() { return getToken(MirMashParser.VAL, 0); }
		public TerminalNode STR() { return getToken(MirMashParser.STR, 0); }
		public TerminalNode ASSIGN() { return getToken(MirMashParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ValDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterValDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitValDecl(this);
		}
	}

	public final ValDeclContext valDecl() throws RecognitionException {
		ValDeclContext _localctx = new ValDeclContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_valDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			match(VAL);
			setState(179);
			match(STR);
			setState(180);
			match(ASSIGN);
			setState(181);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarDeclContext extends ParserRuleContext {
		public TerminalNode STR() { return getToken(MirMashParser.STR, 0); }
		public TerminalNode ASSIGN() { return getToken(MirMashParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode VAR() { return getToken(MirMashParser.VAR, 0); }
		public TerminalNode SET() { return getToken(MirMashParser.SET, 0); }
		public VarDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterVarDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitVarDecl(this);
		}
	}

	public final VarDeclContext varDecl() throws RecognitionException {
		VarDeclContext _localctx = new VarDeclContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_varDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			_la = _input.LA(1);
			if ( !(_la==VAR || _la==SET) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(184);
			match(STR);
			setState(185);
			match(ASSIGN);
			setState(186);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DefDeclContext extends ParserRuleContext {
		public DefNameDeclContext defNameDecl() {
			return getRuleContext(DefNameDeclContext.class,0);
		}
		public TerminalNode LPAR() { return getToken(MirMashParser.LPAR, 0); }
		public TerminalNode RPAR() { return getToken(MirMashParser.RPAR, 0); }
		public TerminalNode ASSIGN() { return getToken(MirMashParser.ASSIGN, 0); }
		public CompoundExprContext compoundExpr() {
			return getRuleContext(CompoundExprContext.class,0);
		}
		public FunParamListContext funParamList() {
			return getRuleContext(FunParamListContext.class,0);
		}
		public DefDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterDefDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitDefDecl(this);
		}
	}

	public final DefDeclContext defDecl() throws RecognitionException {
		DefDeclContext _localctx = new DefDeclContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_defDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(188);
			defNameDecl();
			setState(189);
			match(LPAR);
			setState(191);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STR) {
				{
				setState(190);
				funParamList(0);
				}
			}

			setState(193);
			match(RPAR);
			setState(194);
			match(ASSIGN);
			setState(195);
			compoundExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DefNameDeclContext extends ParserRuleContext {
		public TerminalNode DEF() { return getToken(MirMashParser.DEF, 0); }
		public TerminalNode STR() { return getToken(MirMashParser.STR, 0); }
		public DefNameDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defNameDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterDefNameDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitDefNameDecl(this);
		}
	}

	public final DefNameDeclContext defNameDecl() throws RecognitionException {
		DefNameDeclContext _localctx = new DefNameDeclContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_defNameDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(197);
			match(DEF);
			setState(198);
			match(STR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NatDefDeclContext extends ParserRuleContext {
		public TerminalNode NATIVE() { return getToken(MirMashParser.NATIVE, 0); }
		public TerminalNode DEF() { return getToken(MirMashParser.DEF, 0); }
		public TerminalNode STR() { return getToken(MirMashParser.STR, 0); }
		public TerminalNode LPAR() { return getToken(MirMashParser.LPAR, 0); }
		public TerminalNode RPAR() { return getToken(MirMashParser.RPAR, 0); }
		public FunParamListContext funParamList() {
			return getRuleContext(FunParamListContext.class,0);
		}
		public NatDefDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_natDefDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterNatDefDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitNatDefDecl(this);
		}
	}

	public final NatDefDeclContext natDefDecl() throws RecognitionException {
		NatDefDeclContext _localctx = new NatDefDeclContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_natDefDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(200);
			match(NATIVE);
			setState(201);
			match(DEF);
			setState(202);
			match(STR);
			setState(203);
			match(LPAR);
			setState(205);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STR) {
				{
				setState(204);
				funParamList(0);
				}
			}

			setState(207);
			match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WhileDeclContext extends ParserRuleContext {
		public WhileExprDeclContext whileExprDecl() {
			return getRuleContext(WhileExprDeclContext.class,0);
		}
		public TerminalNode DO() { return getToken(MirMashParser.DO, 0); }
		public CompoundExprContext compoundExpr() {
			return getRuleContext(CompoundExprContext.class,0);
		}
		public WhileDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterWhileDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitWhileDecl(this);
		}
	}

	public final WhileDeclContext whileDecl() throws RecognitionException {
		WhileDeclContext _localctx = new WhileDeclContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_whileDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(209);
			whileExprDecl();
			setState(210);
			match(DO);
			setState(211);
			compoundExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WhileExprDeclContext extends ParserRuleContext {
		public TerminalNode WHILE() { return getToken(MirMashParser.WHILE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public WhileExprDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileExprDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterWhileExprDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitWhileExprDecl(this);
		}
	}

	public final WhileExprDeclContext whileExprDecl() throws RecognitionException {
		WhileExprDeclContext _localctx = new WhileExprDeclContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_whileExprDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(213);
			match(WHILE);
			setState(214);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForDeclContext extends ParserRuleContext {
		public ForExprDeclContext forExprDecl() {
			return getRuleContext(ForExprDeclContext.class,0);
		}
		public TerminalNode DO() { return getToken(MirMashParser.DO, 0); }
		public CompoundExprContext compoundExpr() {
			return getRuleContext(CompoundExprContext.class,0);
		}
		public ForDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterForDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitForDecl(this);
		}
	}

	public final ForDeclContext forDecl() throws RecognitionException {
		ForDeclContext _localctx = new ForDeclContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_forDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(216);
			forExprDecl();
			setState(217);
			match(DO);
			setState(218);
			compoundExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForExprDeclContext extends ParserRuleContext {
		public TerminalNode FOR() { return getToken(MirMashParser.FOR, 0); }
		public TerminalNode STR() { return getToken(MirMashParser.STR, 0); }
		public TerminalNode IN() { return getToken(MirMashParser.IN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ForExprDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forExprDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterForExprDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitForExprDecl(this);
		}
	}

	public final ForExprDeclContext forExprDecl() throws RecognitionException {
		ForExprDeclContext _localctx = new ForExprDeclContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_forExprDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(220);
			match(FOR);
			setState(221);
			match(STR);
			setState(222);
			match(IN);
			setState(223);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunParamListContext extends ParserRuleContext {
		public TerminalNode STR() { return getToken(MirMashParser.STR, 0); }
		public FunParamListContext funParamList() {
			return getRuleContext(FunParamListContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(MirMashParser.COMMA, 0); }
		public FunParamListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funParamList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterFunParamList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitFunParamList(this);
		}
	}

	public final FunParamListContext funParamList() throws RecognitionException {
		return funParamList(0);
	}

	private FunParamListContext funParamList(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		FunParamListContext _localctx = new FunParamListContext(_ctx, _parentState);
		FunParamListContext _prevctx = _localctx;
		int _startState = 50;
		enterRecursionRule(_localctx, 50, RULE_funParamList, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(226);
			match(STR);
			}
			_ctx.stop = _input.LT(-1);
			setState(233);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new FunParamListContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_funParamList);
					setState(228);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(229);
					match(COMMA);
					setState(230);
					match(STR);
					}
					} 
				}
				setState(235);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class IfThenContext extends ParserRuleContext {
		public TerminalNode THEN() { return getToken(MirMashParser.THEN, 0); }
		public CompoundExprContext compoundExpr() {
			return getRuleContext(CompoundExprContext.class,0);
		}
		public IfThenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifThen; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterIfThen(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitIfThen(this);
		}
	}

	public final IfThenContext ifThen() throws RecognitionException {
		IfThenContext _localctx = new IfThenContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_ifThen);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(236);
			match(THEN);
			setState(237);
			compoundExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IfElseContext extends ParserRuleContext {
		public TerminalNode ELSE() { return getToken(MirMashParser.ELSE, 0); }
		public CompoundExprContext compoundExpr() {
			return getRuleContext(CompoundExprContext.class,0);
		}
		public IfElseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifElse; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterIfElse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitIfElse(this);
		}
	}

	public final IfElseContext ifElse() throws RecognitionException {
		IfElseContext _localctx = new IfElseContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_ifElse);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(239);
			match(ELSE);
			setState(240);
			compoundExpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IfDeclContext extends ParserRuleContext {
		public TerminalNode IF() { return getToken(MirMashParser.IF, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public IfThenContext ifThen() {
			return getRuleContext(IfThenContext.class,0);
		}
		public IfElseContext ifElse() {
			return getRuleContext(IfElseContext.class,0);
		}
		public IfDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterIfDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitIfDecl(this);
		}
	}

	public final IfDeclContext ifDecl() throws RecognitionException {
		IfDeclContext _localctx = new IfDeclContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_ifDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(242);
			match(IF);
			setState(243);
			expr(0);
			setState(244);
			ifThen();
			setState(246);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				setState(245);
				ifElse();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ParExprContext extends ExprContext {
		public TerminalNode LPAR() { return getToken(MirMashParser.LPAR, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(MirMashParser.RPAR, 0); }
		public ParExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterParExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitParExpr(this);
		}
	}
	public static class UnaryExprContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode MINUS() { return getToken(MirMashParser.MINUS, 0); }
		public TerminalNode NOT() { return getToken(MirMashParser.NOT, 0); }
		public UnaryExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterUnaryExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitUnaryExpr(this);
		}
	}
	public static class CompExprContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode LTEQ() { return getToken(MirMashParser.LTEQ, 0); }
		public TerminalNode GTEQ() { return getToken(MirMashParser.GTEQ, 0); }
		public TerminalNode LT() { return getToken(MirMashParser.LT, 0); }
		public TerminalNode GT() { return getToken(MirMashParser.GT, 0); }
		public CompExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterCompExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitCompExpr(this);
		}
	}
	public static class PlusMinusExprContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(MirMashParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(MirMashParser.MINUS, 0); }
		public PlusMinusExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterPlusMinusExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitPlusMinusExpr(this);
		}
	}
	public static class PipelineExecExprContext extends ExprContext {
		public List<TerminalNode> BQUOTE() { return getTokens(MirMashParser.BQUOTE); }
		public TerminalNode BQUOTE(int i) {
			return getToken(MirMashParser.BQUOTE, i);
		}
		public PipelineDeclContext pipelineDecl() {
			return getRuleContext(PipelineDeclContext.class,0);
		}
		public PipelineExecExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterPipelineExecExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitPipelineExecExpr(this);
		}
	}
	public static class AtomExprContext extends ExprContext {
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public AtomExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterAtomExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitAtomExpr(this);
		}
	}
	public static class MultDivModExprContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode MULT() { return getToken(MirMashParser.MULT, 0); }
		public TerminalNode DIV() { return getToken(MirMashParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(MirMashParser.MOD, 0); }
		public MultDivModExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterMultDivModExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitMultDivModExpr(this);
		}
	}
	public static class AndOrExprContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode AND() { return getToken(MirMashParser.AND, 0); }
		public TerminalNode OR() { return getToken(MirMashParser.OR, 0); }
		public AndOrExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterAndOrExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitAndOrExpr(this);
		}
	}
	public static class CallExprContext extends ExprContext {
		public DefCallContext defCall() {
			return getRuleContext(DefCallContext.class,0);
		}
		public CallExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterCallExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitCallExpr(this);
		}
	}
	public static class ListExprContext extends ExprContext {
		public ListContext list() {
			return getRuleContext(ListContext.class,0);
		}
		public ListExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterListExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitListExpr(this);
		}
	}
	public static class EqNeqExprContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode EQ() { return getToken(MirMashParser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(MirMashParser.NEQ, 0); }
		public EqNeqExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterEqNeqExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitEqNeqExpr(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 58;
		enterRecursionRule(_localctx, 58, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				_localctx = new UnaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(249);
				((UnaryExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==NOT || _la==MINUS) ) {
					((UnaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(250);
				expr(11);
				}
				break;
			case 2:
				{
				_localctx = new ParExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(251);
				match(LPAR);
				setState(252);
				expr(0);
				setState(253);
				match(RPAR);
				}
				break;
			case 3:
				{
				_localctx = new CallExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(255);
				defCall();
				}
				break;
			case 4:
				{
				_localctx = new PipelineExecExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(256);
				match(BQUOTE);
				setState(257);
				pipelineDecl();
				setState(258);
				match(BQUOTE);
				}
				break;
			case 5:
				{
				_localctx = new ListExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(260);
				list();
				}
				break;
			case 6:
				{
				_localctx = new AtomExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(261);
				atom();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(281);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(279);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
					case 1:
						{
						_localctx = new MultDivModExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(264);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(265);
						((MultDivModExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MULT) | (1L << DIV) | (1L << MOD))) != 0)) ) {
							((MultDivModExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(266);
						expr(10);
						}
						break;
					case 2:
						{
						_localctx = new PlusMinusExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(267);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(268);
						((PlusMinusExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==MINUS || _la==PLUS) ) {
							((PlusMinusExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(269);
						expr(9);
						}
						break;
					case 3:
						{
						_localctx = new CompExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(270);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(271);
						((CompExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GTEQ) | (1L << LTEQ) | (1L << GT) | (1L << LT))) != 0)) ) {
							((CompExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(272);
						expr(8);
						}
						break;
					case 4:
						{
						_localctx = new EqNeqExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(273);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(274);
						((EqNeqExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==EQ || _la==NEQ) ) {
							((EqNeqExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(275);
						expr(7);
						}
						break;
					case 5:
						{
						_localctx = new AndOrExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(276);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(277);
						((AndOrExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==AND || _la==OR) ) {
							((AndOrExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(278);
						expr(6);
						}
						break;
					}
					} 
				}
				setState(283);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class CompoundExprContext extends ParserRuleContext {
		public List<DeclContext> decl() {
			return getRuleContexts(DeclContext.class);
		}
		public DeclContext decl(int i) {
			return getRuleContext(DeclContext.class,i);
		}
		public TerminalNode LBRACE() { return getToken(MirMashParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(MirMashParser.RBRACE, 0); }
		public CompoundExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compoundExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterCompoundExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitCompoundExpr(this);
		}
	}

	public final CompoundExprContext compoundExpr() throws RecognitionException {
		CompoundExprContext _localctx = new CompoundExprContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_compoundExpr);
		int _la;
		try {
			setState(293);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ALIAS:
			case INCLUDE:
			case VAL:
			case VAR:
			case SET:
			case UNSET:
			case DEF:
			case NATIVE:
			case RETURN:
			case IF:
			case WHILE:
			case FOR:
			case SQSTRING:
			case DQSTRING:
			case SCOL:
			case STR:
				enterOuterAlt(_localctx, 1);
				{
				setState(284);
				decl();
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 2);
				{
				setState(285);
				match(LBRACE);
				setState(289);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ALIAS) | (1L << INCLUDE) | (1L << VAL) | (1L << VAR) | (1L << SET) | (1L << UNSET) | (1L << DEF) | (1L << NATIVE) | (1L << RETURN) | (1L << IF) | (1L << WHILE) | (1L << FOR) | (1L << SQSTRING) | (1L << DQSTRING) | (1L << SCOL) | (1L << STR))) != 0)) {
					{
					{
					setState(286);
					decl();
					}
					}
					setState(291);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(292);
				match(RBRACE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CallParamListContext extends ParserRuleContext {
		public CallParamContext callParam() {
			return getRuleContext(CallParamContext.class,0);
		}
		public CallParamListContext callParamList() {
			return getRuleContext(CallParamListContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(MirMashParser.COMMA, 0); }
		public CallParamListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_callParamList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterCallParamList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitCallParamList(this);
		}
	}

	public final CallParamListContext callParamList() throws RecognitionException {
		return callParamList(0);
	}

	private CallParamListContext callParamList(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		CallParamListContext _localctx = new CallParamListContext(_ctx, _parentState);
		CallParamListContext _prevctx = _localctx;
		int _startState = 62;
		enterRecursionRule(_localctx, 62, RULE_callParamList, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(296);
			callParam();
			}
			_ctx.stop = _input.LT(-1);
			setState(303);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new CallParamListContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_callParamList);
					setState(298);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(299);
					match(COMMA);
					setState(300);
					callParam();
					}
					} 
				}
				setState(305);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class CallParamContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public CallParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_callParam; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterCallParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitCallParam(this);
		}
	}

	public final CallParamContext callParam() throws RecognitionException {
		CallParamContext _localctx = new CallParamContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_callParam);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(306);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtomContext extends ParserRuleContext {
		public TerminalNode NULL() { return getToken(MirMashParser.NULL, 0); }
		public TerminalNode BOOL() { return getToken(MirMashParser.BOOL, 0); }
		public TerminalNode STR() { return getToken(MirMashParser.STR, 0); }
		public QstringContext qstring() {
			return getRuleContext(QstringContext.class,0);
		}
		public AtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitAtom(this);
		}
	}

	public final AtomContext atom() throws RecognitionException {
		AtomContext _localctx = new AtomContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_atom);
		try {
			setState(312);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NULL:
				enterOuterAlt(_localctx, 1);
				{
				setState(308);
				match(NULL);
				}
				break;
			case BOOL:
				enterOuterAlt(_localctx, 2);
				{
				setState(309);
				match(BOOL);
				}
				break;
			case STR:
				enterOuterAlt(_localctx, 3);
				{
				setState(310);
				match(STR);
				}
				break;
			case SQSTRING:
			case DQSTRING:
				enterOuterAlt(_localctx, 4);
				{
				setState(311);
				qstring();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ListContext extends ParserRuleContext {
		public TerminalNode LIST_START() { return getToken(MirMashParser.LIST_START, 0); }
		public TerminalNode LIST_END() { return getToken(MirMashParser.LIST_END, 0); }
		public ListElemsContext listElems() {
			return getRuleContext(ListElemsContext.class,0);
		}
		public ListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitList(this);
		}
	}

	public final ListContext list() throws RecognitionException {
		ListContext _localctx = new ListContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_list);
		try {
			setState(320);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(314);
				match(LIST_START);
				setState(315);
				match(LIST_END);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(316);
				match(LIST_START);
				setState(317);
				listElems(0);
				setState(318);
				match(LIST_END);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ListElemsContext extends ParserRuleContext {
		public ListElemContext listElem() {
			return getRuleContext(ListElemContext.class,0);
		}
		public ListElemsContext listElems() {
			return getRuleContext(ListElemsContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(MirMashParser.COMMA, 0); }
		public ListElemsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listElems; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterListElems(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitListElems(this);
		}
	}

	public final ListElemsContext listElems() throws RecognitionException {
		return listElems(0);
	}

	private ListElemsContext listElems(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ListElemsContext _localctx = new ListElemsContext(_ctx, _parentState);
		ListElemsContext _prevctx = _localctx;
		int _startState = 70;
		enterRecursionRule(_localctx, 70, RULE_listElems, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(324);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				{
				}
				break;
			case 2:
				{
				setState(323);
				listElem();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(331);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ListElemsContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_listElems);
					setState(326);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(327);
					match(COMMA);
					setState(328);
					listElem();
					}
					} 
				}
				setState(333);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ListElemContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ListElemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listElem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterListElem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitListElem(this);
		}
	}

	public final ListElemContext listElem() throws RecognitionException {
		ListElemContext _localctx = new ListElemContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_listElem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(334);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QstringContext extends ParserRuleContext {
		public TerminalNode SQSTRING() { return getToken(MirMashParser.SQSTRING, 0); }
		public TerminalNode DQSTRING() { return getToken(MirMashParser.DQSTRING, 0); }
		public QstringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qstring; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).enterQstring(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirMashListener ) ((MirMashListener)listener).exitQstring(this);
		}
	}

	public final QstringContext qstring() throws RecognitionException {
		QstringContext _localctx = new QstringContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_qstring);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(336);
			_la = _input.LA(1);
			if ( !(_la==SQSTRING || _la==DQSTRING) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1:
			return decls_sempred((DeclsContext)_localctx, predIndex);
		case 8:
			return prgList_sempred((PrgListContext)_localctx, predIndex);
		case 11:
			return argList_sempred((ArgListContext)_localctx, predIndex);
		case 25:
			return funParamList_sempred((FunParamListContext)_localctx, predIndex);
		case 29:
			return expr_sempred((ExprContext)_localctx, predIndex);
		case 31:
			return callParamList_sempred((CallParamListContext)_localctx, predIndex);
		case 35:
			return listElems_sempred((ListElemsContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean decls_sempred(DeclsContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean prgList_sempred(PrgListContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean argList_sempred(ArgListContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean funParamList_sempred(FunParamListContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 4:
			return precpred(_ctx, 9);
		case 5:
			return precpred(_ctx, 8);
		case 6:
			return precpred(_ctx, 7);
		case 7:
			return precpred(_ctx, 6);
		case 8:
			return precpred(_ctx, 5);
		}
		return true;
	}
	private boolean callParamList_sempred(CallParamListContext _localctx, int predIndex) {
		switch (predIndex) {
		case 9:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean listElems_sempred(ListElemsContext _localctx, int predIndex) {
		switch (predIndex) {
		case 10:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u00016\u0153\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002"+
		"#\u0007#\u0002$\u0007$\u0002%\u0007%\u0001\u0000\u0005\u0000N\b\u0000"+
		"\n\u0000\f\u0000Q\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0005\u0001Z\b\u0001\n\u0001\f\u0001"+
		"]\t\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002n\b\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0003\u0007~\b\u0007\u0001\b\u0001\b\u0001\b"+
		"\u0001\b\u0001\b\u0001\b\u0001\b\u0005\b\u0087\b\b\n\b\f\b\u008a\t\b\u0001"+
		"\t\u0001\t\u0003\t\u008e\b\t\u0001\t\u0003\t\u0091\b\t\u0001\n\u0001\n"+
		"\u0003\n\u0095\b\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0005\u000b\u009c\b\u000b\n\u000b\f\u000b\u009f\t\u000b\u0001\f"+
		"\u0001\f\u0001\r\u0001\r\u0001\r\u0003\r\u00a6\b\r\u0001\r\u0001\r\u0001"+
		"\u000e\u0001\u000e\u0003\u000e\u00ac\b\u000e\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u00c0\b\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0003"+
		"\u0014\u00ce\b\u0014\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0005\u0019\u00e8\b\u0019\n\u0019\f\u0019\u00eb\t\u0019"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0003\u001c\u00f7\b\u001c"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0003\u001d\u0107\b\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0005\u001d\u0118\b\u001d\n\u001d\f\u001d\u011b\t\u001d\u0001"+
		"\u001e\u0001\u001e\u0001\u001e\u0005\u001e\u0120\b\u001e\n\u001e\f\u001e"+
		"\u0123\t\u001e\u0001\u001e\u0003\u001e\u0126\b\u001e\u0001\u001f\u0001"+
		"\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0005\u001f\u012e"+
		"\b\u001f\n\u001f\f\u001f\u0131\t\u001f\u0001 \u0001 \u0001!\u0001!\u0001"+
		"!\u0001!\u0003!\u0139\b!\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001"+
		"\"\u0003\"\u0141\b\"\u0001#\u0001#\u0003#\u0145\b#\u0001#\u0001#\u0001"+
		"#\u0005#\u014a\b#\n#\f#\u014d\t#\u0001$\u0001$\u0001%\u0001%\u0001%\u0000"+
		"\u0007\u0002\u0010\u00162:>F&\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010"+
		"\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJ\u0000"+
		"\t\u0003\u0000\u001b\u001b\u001f\u001f!!\u0001\u0000\u0004\u0005\u0002"+
		"\u0000\"\"++\u0002\u0000//12\u0002\u0000++..\u0001\u0000\u0019\u001c\u0001"+
		"\u0000\u0017\u0018\u0002\u0000\u001d\u001d  \u0001\u0000\u0013\u0014\u015b"+
		"\u0000O\u0001\u0000\u0000\u0000\u0002T\u0001\u0000\u0000\u0000\u0004m"+
		"\u0001\u0000\u0000\u0000\u0006o\u0001\u0000\u0000\u0000\br\u0001\u0000"+
		"\u0000\u0000\nu\u0001\u0000\u0000\u0000\fw\u0001\u0000\u0000\u0000\u000e"+
		"{\u0001\u0000\u0000\u0000\u0010\u007f\u0001\u0000\u0000\u0000\u0012\u008d"+
		"\u0001\u0000\u0000\u0000\u0014\u0094\u0001\u0000\u0000\u0000\u0016\u0096"+
		"\u0001\u0000\u0000\u0000\u0018\u00a0\u0001\u0000\u0000\u0000\u001a\u00a2"+
		"\u0001\u0000\u0000\u0000\u001c\u00a9\u0001\u0000\u0000\u0000\u001e\u00ad"+
		"\u0001\u0000\u0000\u0000 \u00b2\u0001\u0000\u0000\u0000\"\u00b7\u0001"+
		"\u0000\u0000\u0000$\u00bc\u0001\u0000\u0000\u0000&\u00c5\u0001\u0000\u0000"+
		"\u0000(\u00c8\u0001\u0000\u0000\u0000*\u00d1\u0001\u0000\u0000\u0000,"+
		"\u00d5\u0001\u0000\u0000\u0000.\u00d8\u0001\u0000\u0000\u00000\u00dc\u0001"+
		"\u0000\u0000\u00002\u00e1\u0001\u0000\u0000\u00004\u00ec\u0001\u0000\u0000"+
		"\u00006\u00ef\u0001\u0000\u0000\u00008\u00f2\u0001\u0000\u0000\u0000:"+
		"\u0106\u0001\u0000\u0000\u0000<\u0125\u0001\u0000\u0000\u0000>\u0127\u0001"+
		"\u0000\u0000\u0000@\u0132\u0001\u0000\u0000\u0000B\u0138\u0001\u0000\u0000"+
		"\u0000D\u0140\u0001\u0000\u0000\u0000F\u0144\u0001\u0000\u0000\u0000H"+
		"\u014e\u0001\u0000\u0000\u0000J\u0150\u0001\u0000\u0000\u0000LN\u0003"+
		"\u0002\u0001\u0000ML\u0001\u0000\u0000\u0000NQ\u0001\u0000\u0000\u0000"+
		"OM\u0001\u0000\u0000\u0000OP\u0001\u0000\u0000\u0000PR\u0001\u0000\u0000"+
		"\u0000QO\u0001\u0000\u0000\u0000RS\u0005\u0000\u0000\u0001S\u0001\u0001"+
		"\u0000\u0000\u0000TU\u0006\u0001\uffff\uffff\u0000UV\u0003\u0004\u0002"+
		"\u0000V[\u0001\u0000\u0000\u0000WX\n\u0001\u0000\u0000XZ\u0003\u0004\u0002"+
		"\u0000YW\u0001\u0000\u0000\u0000Z]\u0001\u0000\u0000\u0000[Y\u0001\u0000"+
		"\u0000\u0000[\\\u0001\u0000\u0000\u0000\\\u0003\u0001\u0000\u0000\u0000"+
		"][\u0001\u0000\u0000\u0000^n\u0003\"\u0011\u0000_n\u0003 \u0010\u0000"+
		"`n\u0003\n\u0005\u0000an\u0003\u0006\u0003\u0000bn\u0003$\u0012\u0000"+
		"cn\u0003(\u0014\u0000dn\u0003*\u0015\u0000en\u0003.\u0017\u0000fn\u0003"+
		"\u001e\u000f\u0000gn\u0003\f\u0006\u0000hn\u0003\b\u0004\u0000in\u0003"+
		"8\u001c\u0000jn\u0003\u001c\u000e\u0000kn\u0003\u000e\u0007\u0000ln\u0003"+
		"\u001a\r\u0000m^\u0001\u0000\u0000\u0000m_\u0001\u0000\u0000\u0000m`\u0001"+
		"\u0000\u0000\u0000ma\u0001\u0000\u0000\u0000mb\u0001\u0000\u0000\u0000"+
		"mc\u0001\u0000\u0000\u0000md\u0001\u0000\u0000\u0000me\u0001\u0000\u0000"+
		"\u0000mf\u0001\u0000\u0000\u0000mg\u0001\u0000\u0000\u0000mh\u0001\u0000"+
		"\u0000\u0000mi\u0001\u0000\u0000\u0000mj\u0001\u0000\u0000\u0000mk\u0001"+
		"\u0000\u0000\u0000ml\u0001\u0000\u0000\u0000n\u0005\u0001\u0000\u0000"+
		"\u0000op\u0005\u0006\u0000\u0000pq\u00053\u0000\u0000q\u0007\u0001\u0000"+
		"\u0000\u0000rs\u0005\u0002\u0000\u0000st\u0003J%\u0000t\t\u0001\u0000"+
		"\u0000\u0000uv\u00050\u0000\u0000v\u000b\u0001\u0000\u0000\u0000wx\u0005"+
		"3\u0000\u0000xy\u0005-\u0000\u0000yz\u0003:\u001d\u0000z\r\u0001\u0000"+
		"\u0000\u0000{}\u0003\u0010\b\u0000|~\u0005\u001e\u0000\u0000}|\u0001\u0000"+
		"\u0000\u0000}~\u0001\u0000\u0000\u0000~\u000f\u0001\u0000\u0000\u0000"+
		"\u007f\u0080\u0006\b\uffff\uffff\u0000\u0080\u0081\u0003\u0012\t\u0000"+
		"\u0081\u0088\u0001\u0000\u0000\u0000\u0082\u0083\n\u0001\u0000\u0000\u0083"+
		"\u0084\u0003\u0018\f\u0000\u0084\u0085\u0003\u0012\t\u0000\u0085\u0087"+
		"\u0001\u0000\u0000\u0000\u0086\u0082\u0001\u0000\u0000\u0000\u0087\u008a"+
		"\u0001\u0000\u0000\u0000\u0088\u0086\u0001\u0000\u0000\u0000\u0088\u0089"+
		"\u0001\u0000\u0000\u0000\u0089\u0011\u0001\u0000\u0000\u0000\u008a\u0088"+
		"\u0001\u0000\u0000\u0000\u008b\u008e\u00053\u0000\u0000\u008c\u008e\u0003"+
		"J%\u0000\u008d\u008b\u0001\u0000\u0000\u0000\u008d\u008c\u0001\u0000\u0000"+
		"\u0000\u008e\u0090\u0001\u0000\u0000\u0000\u008f\u0091\u0003\u0016\u000b"+
		"\u0000\u0090\u008f\u0001\u0000\u0000\u0000\u0090\u0091\u0001\u0000\u0000"+
		"\u0000\u0091\u0013\u0001\u0000\u0000\u0000\u0092\u0095\u00053\u0000\u0000"+
		"\u0093\u0095\u0003J%\u0000\u0094\u0092\u0001\u0000\u0000\u0000\u0094\u0093"+
		"\u0001\u0000\u0000\u0000\u0095\u0015\u0001\u0000\u0000\u0000\u0096\u0097"+
		"\u0006\u000b\uffff\uffff\u0000\u0097\u0098\u0003\u0014\n\u0000\u0098\u009d"+
		"\u0001\u0000\u0000\u0000\u0099\u009a\n\u0001\u0000\u0000\u009a\u009c\u0003"+
		"\u0014\n\u0000\u009b\u0099\u0001\u0000\u0000\u0000\u009c\u009f\u0001\u0000"+
		"\u0000\u0000\u009d\u009b\u0001\u0000\u0000\u0000\u009d\u009e\u0001\u0000"+
		"\u0000\u0000\u009e\u0017\u0001\u0000\u0000\u0000\u009f\u009d\u0001\u0000"+
		"\u0000\u0000\u00a0\u00a1\u0007\u0000\u0000\u0000\u00a1\u0019\u0001\u0000"+
		"\u0000\u0000\u00a2\u00a3\u00053\u0000\u0000\u00a3\u00a5\u0005#\u0000\u0000"+
		"\u00a4\u00a6\u0003>\u001f\u0000\u00a5\u00a4\u0001\u0000\u0000\u0000\u00a5"+
		"\u00a6\u0001\u0000\u0000\u0000\u00a6\u00a7\u0001\u0000\u0000\u0000\u00a7"+
		"\u00a8\u0005$\u0000\u0000\u00a8\u001b\u0001\u0000\u0000\u0000\u00a9\u00ab"+
		"\u0005\t\u0000\u0000\u00aa\u00ac\u0003:\u001d\u0000\u00ab\u00aa\u0001"+
		"\u0000\u0000\u0000\u00ab\u00ac\u0001\u0000\u0000\u0000\u00ac\u001d\u0001"+
		"\u0000\u0000\u0000\u00ad\u00ae\u0005\u0001\u0000\u0000\u00ae\u00af\u0005"+
		"3\u0000\u0000\u00af\u00b0\u0005-\u0000\u0000\u00b0\u00b1\u0003J%\u0000"+
		"\u00b1\u001f\u0001\u0000\u0000\u0000\u00b2\u00b3\u0005\u0003\u0000\u0000"+
		"\u00b3\u00b4\u00053\u0000\u0000\u00b4\u00b5\u0005-\u0000\u0000\u00b5\u00b6"+
		"\u0003:\u001d\u0000\u00b6!\u0001\u0000\u0000\u0000\u00b7\u00b8\u0007\u0001"+
		"\u0000\u0000\u00b8\u00b9\u00053\u0000\u0000\u00b9\u00ba\u0005-\u0000\u0000"+
		"\u00ba\u00bb\u0003:\u001d\u0000\u00bb#\u0001\u0000\u0000\u0000\u00bc\u00bd"+
		"\u0003&\u0013\u0000\u00bd\u00bf\u0005#\u0000\u0000\u00be\u00c0\u00032"+
		"\u0019\u0000\u00bf\u00be\u0001\u0000\u0000\u0000\u00bf\u00c0\u0001\u0000"+
		"\u0000\u0000\u00c0\u00c1\u0001\u0000\u0000\u0000\u00c1\u00c2\u0005$\u0000"+
		"\u0000\u00c2\u00c3\u0005-\u0000\u0000\u00c3\u00c4\u0003<\u001e\u0000\u00c4"+
		"%\u0001\u0000\u0000\u0000\u00c5\u00c6\u0005\u0007\u0000\u0000\u00c6\u00c7"+
		"\u00053\u0000\u0000\u00c7\'\u0001\u0000\u0000\u0000\u00c8\u00c9\u0005"+
		"\b\u0000\u0000\u00c9\u00ca\u0005\u0007\u0000\u0000\u00ca\u00cb\u00053"+
		"\u0000\u0000\u00cb\u00cd\u0005#\u0000\u0000\u00cc\u00ce\u00032\u0019\u0000"+
		"\u00cd\u00cc\u0001\u0000\u0000\u0000\u00cd\u00ce\u0001\u0000\u0000\u0000"+
		"\u00ce\u00cf\u0001\u0000\u0000\u0000\u00cf\u00d0\u0005$\u0000\u0000\u00d0"+
		")\u0001\u0000\u0000\u0000\u00d1\u00d2\u0003,\u0016\u0000\u00d2\u00d3\u0005"+
		"\u000e\u0000\u0000\u00d3\u00d4\u0003<\u001e\u0000\u00d4+\u0001\u0000\u0000"+
		"\u0000\u00d5\u00d6\u0005\r\u0000\u0000\u00d6\u00d7\u0003:\u001d\u0000"+
		"\u00d7-\u0001\u0000\u0000\u0000\u00d8\u00d9\u00030\u0018\u0000\u00d9\u00da"+
		"\u0005\u000e\u0000\u0000\u00da\u00db\u0003<\u001e\u0000\u00db/\u0001\u0000"+
		"\u0000\u0000\u00dc\u00dd\u0005\u000f\u0000\u0000\u00dd\u00de\u00053\u0000"+
		"\u0000\u00de\u00df\u0005\u0010\u0000\u0000\u00df\u00e0\u0003:\u001d\u0000"+
		"\u00e01\u0001\u0000\u0000\u0000\u00e1\u00e2\u0006\u0019\uffff\uffff\u0000"+
		"\u00e2\u00e3\u00053\u0000\u0000\u00e3\u00e9\u0001\u0000\u0000\u0000\u00e4"+
		"\u00e5\n\u0001\u0000\u0000\u00e5\u00e6\u0005*\u0000\u0000\u00e6\u00e8"+
		"\u00053\u0000\u0000\u00e7\u00e4\u0001\u0000\u0000\u0000\u00e8\u00eb\u0001"+
		"\u0000\u0000\u0000\u00e9\u00e7\u0001\u0000\u0000\u0000\u00e9\u00ea\u0001"+
		"\u0000\u0000\u0000\u00ea3\u0001\u0000\u0000\u0000\u00eb\u00e9\u0001\u0000"+
		"\u0000\u0000\u00ec\u00ed\u0005\u000b\u0000\u0000\u00ed\u00ee\u0003<\u001e"+
		"\u0000\u00ee5\u0001\u0000\u0000\u0000\u00ef\u00f0\u0005\f\u0000\u0000"+
		"\u00f0\u00f1\u0003<\u001e\u0000\u00f17\u0001\u0000\u0000\u0000\u00f2\u00f3"+
		"\u0005\n\u0000\u0000\u00f3\u00f4\u0003:\u001d\u0000\u00f4\u00f6\u0003"+
		"4\u001a\u0000\u00f5\u00f7\u00036\u001b\u0000\u00f6\u00f5\u0001\u0000\u0000"+
		"\u0000\u00f6\u00f7\u0001\u0000\u0000\u0000\u00f79\u0001\u0000\u0000\u0000"+
		"\u00f8\u00f9\u0006\u001d\uffff\uffff\u0000\u00f9\u00fa\u0007\u0002\u0000"+
		"\u0000\u00fa\u0107\u0003:\u001d\u000b\u00fb\u00fc\u0005#\u0000\u0000\u00fc"+
		"\u00fd\u0003:\u001d\u0000\u00fd\u00fe\u0005$\u0000\u0000\u00fe\u0107\u0001"+
		"\u0000\u0000\u0000\u00ff\u0107\u0003\u001a\r\u0000\u0100\u0101\u0005)"+
		"\u0000\u0000\u0101\u0102\u0003\u000e\u0007\u0000\u0102\u0103\u0005)\u0000"+
		"\u0000\u0103\u0107\u0001\u0000\u0000\u0000\u0104\u0107\u0003D\"\u0000"+
		"\u0105\u0107\u0003B!\u0000\u0106\u00f8\u0001\u0000\u0000\u0000\u0106\u00fb"+
		"\u0001\u0000\u0000\u0000\u0106\u00ff\u0001\u0000\u0000\u0000\u0106\u0100"+
		"\u0001\u0000\u0000\u0000\u0106\u0104\u0001\u0000\u0000\u0000\u0106\u0105"+
		"\u0001\u0000\u0000\u0000\u0107\u0119\u0001\u0000\u0000\u0000\u0108\u0109"+
		"\n\t\u0000\u0000\u0109\u010a\u0007\u0003\u0000\u0000\u010a\u0118\u0003"+
		":\u001d\n\u010b\u010c\n\b\u0000\u0000\u010c\u010d\u0007\u0004\u0000\u0000"+
		"\u010d\u0118\u0003:\u001d\t\u010e\u010f\n\u0007\u0000\u0000\u010f\u0110"+
		"\u0007\u0005\u0000\u0000\u0110\u0118\u0003:\u001d\b\u0111\u0112\n\u0006"+
		"\u0000\u0000\u0112\u0113\u0007\u0006\u0000\u0000\u0113\u0118\u0003:\u001d"+
		"\u0007\u0114\u0115\n\u0005\u0000\u0000\u0115\u0116\u0007\u0007\u0000\u0000"+
		"\u0116\u0118\u0003:\u001d\u0006\u0117\u0108\u0001\u0000\u0000\u0000\u0117"+
		"\u010b\u0001\u0000\u0000\u0000\u0117\u010e\u0001\u0000\u0000\u0000\u0117"+
		"\u0111\u0001\u0000\u0000\u0000\u0117\u0114\u0001\u0000\u0000\u0000\u0118"+
		"\u011b\u0001\u0000\u0000\u0000\u0119\u0117\u0001\u0000\u0000\u0000\u0119"+
		"\u011a\u0001\u0000\u0000\u0000\u011a;\u0001\u0000\u0000\u0000\u011b\u0119"+
		"\u0001\u0000\u0000\u0000\u011c\u0126\u0003\u0004\u0002\u0000\u011d\u0121"+
		"\u0005%\u0000\u0000\u011e\u0120\u0003\u0004\u0002\u0000\u011f\u011e\u0001"+
		"\u0000\u0000\u0000\u0120\u0123\u0001\u0000\u0000\u0000\u0121\u011f\u0001"+
		"\u0000\u0000\u0000\u0121\u0122\u0001\u0000\u0000\u0000\u0122\u0124\u0001"+
		"\u0000\u0000\u0000\u0123\u0121\u0001\u0000\u0000\u0000\u0124\u0126\u0005"+
		"&\u0000\u0000\u0125\u011c\u0001\u0000\u0000\u0000\u0125\u011d\u0001\u0000"+
		"\u0000\u0000\u0126=\u0001\u0000\u0000\u0000\u0127\u0128\u0006\u001f\uffff"+
		"\uffff\u0000\u0128\u0129\u0003@ \u0000\u0129\u012f\u0001\u0000\u0000\u0000"+
		"\u012a\u012b\n\u0001\u0000\u0000\u012b\u012c\u0005*\u0000\u0000\u012c"+
		"\u012e\u0003@ \u0000\u012d\u012a\u0001\u0000\u0000\u0000\u012e\u0131\u0001"+
		"\u0000\u0000\u0000\u012f\u012d\u0001\u0000\u0000\u0000\u012f\u0130\u0001"+
		"\u0000\u0000\u0000\u0130?\u0001\u0000\u0000\u0000\u0131\u012f\u0001\u0000"+
		"\u0000\u0000\u0132\u0133\u0003:\u001d\u0000\u0133A\u0001\u0000\u0000\u0000"+
		"\u0134\u0139\u0005\u0016\u0000\u0000\u0135\u0139\u0005\u0015\u0000\u0000"+
		"\u0136\u0139\u00053\u0000\u0000\u0137\u0139\u0003J%\u0000\u0138\u0134"+
		"\u0001\u0000\u0000\u0000\u0138\u0135\u0001\u0000\u0000\u0000\u0138\u0136"+
		"\u0001\u0000\u0000\u0000\u0138\u0137\u0001\u0000\u0000\u0000\u0139C\u0001"+
		"\u0000\u0000\u0000\u013a\u013b\u0005\u0011\u0000\u0000\u013b\u0141\u0005"+
		"\u0012\u0000\u0000\u013c\u013d\u0005\u0011\u0000\u0000\u013d\u013e\u0003"+
		"F#\u0000\u013e\u013f\u0005\u0012\u0000\u0000\u013f\u0141\u0001\u0000\u0000"+
		"\u0000\u0140\u013a\u0001\u0000\u0000\u0000\u0140\u013c\u0001\u0000\u0000"+
		"\u0000\u0141E\u0001\u0000\u0000\u0000\u0142\u0145\u0006#\uffff\uffff\u0000"+
		"\u0143\u0145\u0003H$\u0000\u0144\u0142\u0001\u0000\u0000\u0000\u0144\u0143"+
		"\u0001\u0000\u0000\u0000\u0145\u014b\u0001\u0000\u0000\u0000\u0146\u0147"+
		"\n\u0001\u0000\u0000\u0147\u0148\u0005*\u0000\u0000\u0148\u014a\u0003"+
		"H$\u0000\u0149\u0146\u0001\u0000\u0000\u0000\u014a\u014d\u0001\u0000\u0000"+
		"\u0000\u014b\u0149\u0001\u0000\u0000\u0000\u014b\u014c\u0001\u0000\u0000"+
		"\u0000\u014cG\u0001\u0000\u0000\u0000\u014d\u014b\u0001\u0000\u0000\u0000"+
		"\u014e\u014f\u0003:\u001d\u0000\u014fI\u0001\u0000\u0000\u0000\u0150\u0151"+
		"\u0007\b\u0000\u0000\u0151K\u0001\u0000\u0000\u0000\u0019O[m}\u0088\u008d"+
		"\u0090\u0094\u009d\u00a5\u00ab\u00bf\u00cd\u00e9\u00f6\u0106\u0117\u0119"+
		"\u0121\u0125\u012f\u0138\u0140\u0144\u014b";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}