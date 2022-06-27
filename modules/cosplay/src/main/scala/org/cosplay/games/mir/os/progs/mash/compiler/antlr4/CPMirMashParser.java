// Generated from /Users/nivanov/cosplay/modules/cosplay/src/main/scala/org/cosplay/games/mir/os/progs/mash/compiler/antlr4/CPMirMash.g4 by ANTLR 4.10.1
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
public class CPMirMashParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, SQSTRING=5, DQSTRING=6, BQSTRING=7, BOOL=8, 
		NULL=9, EQ=10, NEQ=11, GTEQ=12, LTEQ=13, GT=14, LT=15, AND=16, OR=17, 
		VERT=18, NOT=19, LPAR=20, RPAR=21, LBRACE=22, RBRACE=23, SQUOTE=24, DQUOTE=25, 
		BQUOTE=26, TILDA=27, LBR=28, RBR=29, POUND=30, COMMA=31, COLON=32, MINUS=33, 
		DOT=34, UNDERSCORE=35, ASSIGN=36, PLUS=37, QUESTION=38, MULT=39, DIV=40, 
		MOD=41, AT=42, DOLLAR=43, INT=44, REAL=45, EXP=46, IDENT=47, COMMENT=48, 
		WS=49, ErrorChar=50;
	public static final int
		RULE_mash = 0, RULE_decls = 1, RULE_decl = 2, RULE_valDecl = 3, RULE_varDecl = 4, 
		RULE_assignDecl = 5, RULE_exportDecl = 6, RULE_unexportDecl = 7, RULE_expr = 8, 
		RULE_atom = 9, RULE_qstring = 10;
	private static String[] makeRuleNames() {
		return new String[] {
			"mash", "decls", "decl", "valDecl", "varDecl", "assignDecl", "exportDecl", 
			"unexportDecl", "expr", "atom", "qstring"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'val'", "'var'", "'export'", "'unexport'", null, null, null, null, 
			"'null'", "'=='", "'!='", "'>='", "'<='", "'>'", "'<'", "'&&'", "'||'", 
			"'|'", "'!'", "'('", "')'", "'{'", "'}'", "'''", "'\"'", "'`'", "'~'", 
			"'['", "']'", "'#'", "','", "':'", "'-'", "'.'", "'_'", "'='", "'+'", 
			"'?'", "'*'", "'/'", "'%'", "'@'", "'$'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, "SQSTRING", "DQSTRING", "BQSTRING", "BOOL", 
			"NULL", "EQ", "NEQ", "GTEQ", "LTEQ", "GT", "LT", "AND", "OR", "VERT", 
			"NOT", "LPAR", "RPAR", "LBRACE", "RBRACE", "SQUOTE", "DQUOTE", "BQUOTE", 
			"TILDA", "LBR", "RBR", "POUND", "COMMA", "COLON", "MINUS", "DOT", "UNDERSCORE", 
			"ASSIGN", "PLUS", "QUESTION", "MULT", "DIV", "MOD", "AT", "DOLLAR", "INT", 
			"REAL", "EXP", "IDENT", "COMMENT", "WS", "ErrorChar"
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
	public String getGrammarFileName() { return "CPMirMash.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CPMirMashParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class MashContext extends ParserRuleContext {
		public DeclsContext decls() {
			return getRuleContext(DeclsContext.class,0);
		}
		public TerminalNode EOF() { return getToken(CPMirMashParser.EOF, 0); }
		public MashContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mash; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).enterMash(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).exitMash(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPMirMashVisitor ) return ((CPMirMashVisitor<? extends T>)visitor).visitMash(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MashContext mash() throws RecognitionException {
		MashContext _localctx = new MashContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_mash);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(22);
			decls(0);
			setState(23);
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
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).enterDecls(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).exitDecls(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPMirMashVisitor ) return ((CPMirMashVisitor<? extends T>)visitor).visitDecls(this);
			else return visitor.visitChildren(this);
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
			setState(26);
			decl();
			}
			_ctx.stop = _input.LT(-1);
			setState(32);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new DeclsContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_decls);
					setState(28);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(29);
					decl();
					}
					} 
				}
				setState(34);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
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
		public ValDeclContext valDecl() {
			return getRuleContext(ValDeclContext.class,0);
		}
		public VarDeclContext varDecl() {
			return getRuleContext(VarDeclContext.class,0);
		}
		public AssignDeclContext assignDecl() {
			return getRuleContext(AssignDeclContext.class,0);
		}
		public ExportDeclContext exportDecl() {
			return getRuleContext(ExportDeclContext.class,0);
		}
		public UnexportDeclContext unexportDecl() {
			return getRuleContext(UnexportDeclContext.class,0);
		}
		public DeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).enterDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).exitDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPMirMashVisitor ) return ((CPMirMashVisitor<? extends T>)visitor).visitDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclContext decl() throws RecognitionException {
		DeclContext _localctx = new DeclContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_decl);
		try {
			setState(40);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				enterOuterAlt(_localctx, 1);
				{
				setState(35);
				valDecl();
				}
				break;
			case T__1:
				enterOuterAlt(_localctx, 2);
				{
				setState(36);
				varDecl();
				}
				break;
			case IDENT:
				enterOuterAlt(_localctx, 3);
				{
				setState(37);
				assignDecl();
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 4);
				{
				setState(38);
				exportDecl();
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 5);
				{
				setState(39);
				unexportDecl();
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

	public static class ValDeclContext extends ParserRuleContext {
		public AssignDeclContext assignDecl() {
			return getRuleContext(AssignDeclContext.class,0);
		}
		public ValDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).enterValDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).exitValDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPMirMashVisitor ) return ((CPMirMashVisitor<? extends T>)visitor).visitValDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValDeclContext valDecl() throws RecognitionException {
		ValDeclContext _localctx = new ValDeclContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_valDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42);
			match(T__0);
			setState(43);
			assignDecl();
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
		public AssignDeclContext assignDecl() {
			return getRuleContext(AssignDeclContext.class,0);
		}
		public VarDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).enterVarDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).exitVarDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPMirMashVisitor ) return ((CPMirMashVisitor<? extends T>)visitor).visitVarDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarDeclContext varDecl() throws RecognitionException {
		VarDeclContext _localctx = new VarDeclContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_varDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45);
			match(T__1);
			setState(46);
			assignDecl();
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
		public TerminalNode IDENT() { return getToken(CPMirMashParser.IDENT, 0); }
		public TerminalNode ASSIGN() { return getToken(CPMirMashParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public AssignDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).enterAssignDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).exitAssignDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPMirMashVisitor ) return ((CPMirMashVisitor<? extends T>)visitor).visitAssignDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignDeclContext assignDecl() throws RecognitionException {
		AssignDeclContext _localctx = new AssignDeclContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_assignDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
			match(IDENT);
			setState(49);
			match(ASSIGN);
			setState(50);
			expr();
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

	public static class ExportDeclContext extends ParserRuleContext {
		public TerminalNode IDENT() { return getToken(CPMirMashParser.IDENT, 0); }
		public ExportDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exportDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).enterExportDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).exitExportDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPMirMashVisitor ) return ((CPMirMashVisitor<? extends T>)visitor).visitExportDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExportDeclContext exportDecl() throws RecognitionException {
		ExportDeclContext _localctx = new ExportDeclContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_exportDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52);
			match(T__2);
			setState(53);
			match(IDENT);
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

	public static class UnexportDeclContext extends ParserRuleContext {
		public TerminalNode IDENT() { return getToken(CPMirMashParser.IDENT, 0); }
		public UnexportDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unexportDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).enterUnexportDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).exitUnexportDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPMirMashVisitor ) return ((CPMirMashVisitor<? extends T>)visitor).visitUnexportDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnexportDeclContext unexportDecl() throws RecognitionException {
		UnexportDeclContext _localctx = new UnexportDeclContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_unexportDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			match(T__3);
			setState(56);
			match(IDENT);
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
	public static class AtomExprContext extends ExprContext {
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public AtomExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).enterAtomExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).exitAtomExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPMirMashVisitor ) return ((CPMirMashVisitor<? extends T>)visitor).visitAtomExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_expr);
		try {
			_localctx = new AtomExprContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			atom();
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
		public TerminalNode NULL() { return getToken(CPMirMashParser.NULL, 0); }
		public TerminalNode INT() { return getToken(CPMirMashParser.INT, 0); }
		public TerminalNode REAL() { return getToken(CPMirMashParser.REAL, 0); }
		public TerminalNode EXP() { return getToken(CPMirMashParser.EXP, 0); }
		public TerminalNode BOOL() { return getToken(CPMirMashParser.BOOL, 0); }
		public QstringContext qstring() {
			return getRuleContext(QstringContext.class,0);
		}
		public AtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).enterAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).exitAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPMirMashVisitor ) return ((CPMirMashVisitor<? extends T>)visitor).visitAtom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtomContext atom() throws RecognitionException {
		AtomContext _localctx = new AtomContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_atom);
		try {
			setState(70);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NULL:
				enterOuterAlt(_localctx, 1);
				{
				setState(60);
				match(NULL);
				}
				break;
			case INT:
				enterOuterAlt(_localctx, 2);
				{
				setState(61);
				match(INT);
				setState(63);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
				case 1:
					{
					setState(62);
					match(REAL);
					}
					break;
				}
				setState(66);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
				case 1:
					{
					setState(65);
					match(EXP);
					}
					break;
				}
				}
				break;
			case BOOL:
				enterOuterAlt(_localctx, 3);
				{
				setState(68);
				match(BOOL);
				}
				break;
			case SQSTRING:
			case DQSTRING:
			case BQSTRING:
				enterOuterAlt(_localctx, 4);
				{
				setState(69);
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

	public static class QstringContext extends ParserRuleContext {
		public TerminalNode SQSTRING() { return getToken(CPMirMashParser.SQSTRING, 0); }
		public TerminalNode DQSTRING() { return getToken(CPMirMashParser.DQSTRING, 0); }
		public TerminalNode BQSTRING() { return getToken(CPMirMashParser.BQSTRING, 0); }
		public QstringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qstring; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).enterQstring(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPMirMashListener ) ((CPMirMashListener)listener).exitQstring(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPMirMashVisitor ) return ((CPMirMashVisitor<? extends T>)visitor).visitQstring(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QstringContext qstring() throws RecognitionException {
		QstringContext _localctx = new QstringContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_qstring);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SQSTRING) | (1L << DQSTRING) | (1L << BQSTRING))) != 0)) ) {
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

	public static final String _serializedATN =
		"\u0004\u00012K\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002"+
		"\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005"+
		"\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002\b\u0007"+
		"\b\u0002\t\u0007\t\u0002\n\u0007\n\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0005\u0001"+
		"\u001f\b\u0001\n\u0001\f\u0001\"\t\u0001\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0003\u0002)\b\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t"+
		"\u0003\t@\b\t\u0001\t\u0003\tC\b\t\u0001\t\u0001\t\u0003\tG\b\t\u0001"+
		"\n\u0001\n\u0001\n\u0000\u0001\u0002\u000b\u0000\u0002\u0004\u0006\b\n"+
		"\f\u000e\u0010\u0012\u0014\u0000\u0001\u0001\u0000\u0005\u0007I\u0000"+
		"\u0016\u0001\u0000\u0000\u0000\u0002\u0019\u0001\u0000\u0000\u0000\u0004"+
		"(\u0001\u0000\u0000\u0000\u0006*\u0001\u0000\u0000\u0000\b-\u0001\u0000"+
		"\u0000\u0000\n0\u0001\u0000\u0000\u0000\f4\u0001\u0000\u0000\u0000\u000e"+
		"7\u0001\u0000\u0000\u0000\u0010:\u0001\u0000\u0000\u0000\u0012F\u0001"+
		"\u0000\u0000\u0000\u0014H\u0001\u0000\u0000\u0000\u0016\u0017\u0003\u0002"+
		"\u0001\u0000\u0017\u0018\u0005\u0000\u0000\u0001\u0018\u0001\u0001\u0000"+
		"\u0000\u0000\u0019\u001a\u0006\u0001\uffff\uffff\u0000\u001a\u001b\u0003"+
		"\u0004\u0002\u0000\u001b \u0001\u0000\u0000\u0000\u001c\u001d\n\u0001"+
		"\u0000\u0000\u001d\u001f\u0003\u0004\u0002\u0000\u001e\u001c\u0001\u0000"+
		"\u0000\u0000\u001f\"\u0001\u0000\u0000\u0000 \u001e\u0001\u0000\u0000"+
		"\u0000 !\u0001\u0000\u0000\u0000!\u0003\u0001\u0000\u0000\u0000\" \u0001"+
		"\u0000\u0000\u0000#)\u0003\u0006\u0003\u0000$)\u0003\b\u0004\u0000%)\u0003"+
		"\n\u0005\u0000&)\u0003\f\u0006\u0000\')\u0003\u000e\u0007\u0000(#\u0001"+
		"\u0000\u0000\u0000($\u0001\u0000\u0000\u0000(%\u0001\u0000\u0000\u0000"+
		"(&\u0001\u0000\u0000\u0000(\'\u0001\u0000\u0000\u0000)\u0005\u0001\u0000"+
		"\u0000\u0000*+\u0005\u0001\u0000\u0000+,\u0003\n\u0005\u0000,\u0007\u0001"+
		"\u0000\u0000\u0000-.\u0005\u0002\u0000\u0000./\u0003\n\u0005\u0000/\t"+
		"\u0001\u0000\u0000\u000001\u0005/\u0000\u000012\u0005$\u0000\u000023\u0003"+
		"\u0010\b\u00003\u000b\u0001\u0000\u0000\u000045\u0005\u0003\u0000\u0000"+
		"56\u0005/\u0000\u00006\r\u0001\u0000\u0000\u000078\u0005\u0004\u0000\u0000"+
		"89\u0005/\u0000\u00009\u000f\u0001\u0000\u0000\u0000:;\u0003\u0012\t\u0000"+
		";\u0011\u0001\u0000\u0000\u0000<G\u0005\t\u0000\u0000=?\u0005,\u0000\u0000"+
		">@\u0005-\u0000\u0000?>\u0001\u0000\u0000\u0000?@\u0001\u0000\u0000\u0000"+
		"@B\u0001\u0000\u0000\u0000AC\u0005.\u0000\u0000BA\u0001\u0000\u0000\u0000"+
		"BC\u0001\u0000\u0000\u0000CG\u0001\u0000\u0000\u0000DG\u0005\b\u0000\u0000"+
		"EG\u0003\u0014\n\u0000F<\u0001\u0000\u0000\u0000F=\u0001\u0000\u0000\u0000"+
		"FD\u0001\u0000\u0000\u0000FE\u0001\u0000\u0000\u0000G\u0013\u0001\u0000"+
		"\u0000\u0000HI\u0007\u0000\u0000\u0000I\u0015\u0001\u0000\u0000\u0000"+
		"\u0005 (?BF";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}