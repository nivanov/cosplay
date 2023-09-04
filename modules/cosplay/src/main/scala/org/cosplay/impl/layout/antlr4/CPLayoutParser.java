// Generated from /Users/nivanov/cosplay/modules/cosplay/src/main/scala/org/cosplay/impl/layout/antlr4/CPLayout.g4 by ANTLR 4.12.0
package org.cosplay.impl.layout.antlr4;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class CPLayoutParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.12.0", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, EQ=15, SCOLON=16, COLON=17, 
		COMMA=18, LPAR=19, RPAR=20, NUM=21, ID=22, COMMENT=23, WS=24, ErrorChar=25;
	public static final int
		RULE_layout = 0, RULE_decls = 1, RULE_decl = 2, RULE_specs = 3, RULE_spec = 4, 
		RULE_padSpec = 5, RULE_posSpec = 6, RULE_floatSpec = 7;
	private static String[] makeRuleNames() {
		return new String[] {
			"layout", "decls", "decl", "specs", "spec", "padSpec", "posSpec", "floatSpec"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'top'", "'left'", "'bottom'", "'right'", "'vert'", "'hor'", "'pos'", 
			"'before'", "'after'", "'above'", "'below'", "'xfloat'", "'yfloat'", 
			"'center'", "'='", "';'", "':'", "','", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, "EQ", "SCOLON", "COLON", "COMMA", "LPAR", "RPAR", "NUM", 
			"ID", "COMMENT", "WS", "ErrorChar"
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
	public String getGrammarFileName() { return "CPLayout.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CPLayoutParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LayoutContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(CPLayoutParser.EOF, 0); }
		public List<DeclsContext> decls() {
			return getRuleContexts(DeclsContext.class);
		}
		public DeclsContext decls(int i) {
			return getRuleContext(DeclsContext.class,i);
		}
		public LayoutContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_layout; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLayoutListener ) ((CPLayoutListener)listener).enterLayout(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLayoutListener ) ((CPLayoutListener)listener).exitLayout(this);
		}
	}

	public final LayoutContext layout() throws RecognitionException {
		LayoutContext _localctx = new LayoutContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_layout);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(19);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ID) {
				{
				{
				setState(16);
				decls(0);
				}
				}
				setState(21);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(22);
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

	@SuppressWarnings("CheckReturnValue")
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
			if ( listener instanceof CPLayoutListener ) ((CPLayoutListener)listener).enterDecls(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLayoutListener ) ((CPLayoutListener)listener).exitDecls(this);
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
			setState(25);
			decl();
			}
			_ctx.stop = _input.LT(-1);
			setState(31);
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
					setState(27);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(28);
					decl();
					}
					} 
				}
				setState(33);
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

	@SuppressWarnings("CheckReturnValue")
	public static class DeclContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(CPLayoutParser.ID, 0); }
		public TerminalNode EQ() { return getToken(CPLayoutParser.EQ, 0); }
		public SpecsContext specs() {
			return getRuleContext(SpecsContext.class,0);
		}
		public TerminalNode SCOLON() { return getToken(CPLayoutParser.SCOLON, 0); }
		public DeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLayoutListener ) ((CPLayoutListener)listener).enterDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLayoutListener ) ((CPLayoutListener)listener).exitDecl(this);
		}
	}

	public final DeclContext decl() throws RecognitionException {
		DeclContext _localctx = new DeclContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_decl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(34);
			match(ID);
			setState(35);
			match(EQ);
			setState(36);
			specs(0);
			setState(37);
			match(SCOLON);
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

	@SuppressWarnings("CheckReturnValue")
	public static class SpecsContext extends ParserRuleContext {
		public SpecContext spec() {
			return getRuleContext(SpecContext.class,0);
		}
		public SpecsContext specs() {
			return getRuleContext(SpecsContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(CPLayoutParser.COMMA, 0); }
		public SpecsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_specs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLayoutListener ) ((CPLayoutListener)listener).enterSpecs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLayoutListener ) ((CPLayoutListener)listener).exitSpecs(this);
		}
	}

	public final SpecsContext specs() throws RecognitionException {
		return specs(0);
	}

	private SpecsContext specs(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		SpecsContext _localctx = new SpecsContext(_ctx, _parentState);
		SpecsContext _prevctx = _localctx;
		int _startState = 6;
		enterRecursionRule(_localctx, 6, RULE_specs, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(40);
			spec();
			}
			_ctx.stop = _input.LT(-1);
			setState(47);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new SpecsContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_specs);
					setState(42);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(43);
					match(COMMA);
					setState(44);
					spec();
					}
					} 
				}
				setState(49);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
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

	@SuppressWarnings("CheckReturnValue")
	public static class SpecContext extends ParserRuleContext {
		public PadSpecContext padSpec() {
			return getRuleContext(PadSpecContext.class,0);
		}
		public PosSpecContext posSpec() {
			return getRuleContext(PosSpecContext.class,0);
		}
		public FloatSpecContext floatSpec() {
			return getRuleContext(FloatSpecContext.class,0);
		}
		public SpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_spec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLayoutListener ) ((CPLayoutListener)listener).enterSpec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLayoutListener ) ((CPLayoutListener)listener).exitSpec(this);
		}
	}

	public final SpecContext spec() throws RecognitionException {
		SpecContext _localctx = new SpecContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_spec);
		try {
			setState(53);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
			case T__1:
			case T__2:
			case T__3:
			case T__4:
			case T__5:
				enterOuterAlt(_localctx, 1);
				{
				setState(50);
				padSpec();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 2);
				{
				setState(51);
				posSpec();
				}
				break;
			case T__11:
			case T__12:
				enterOuterAlt(_localctx, 3);
				{
				setState(52);
				floatSpec();
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

	@SuppressWarnings("CheckReturnValue")
	public static class PadSpecContext extends ParserRuleContext {
		public TerminalNode COLON() { return getToken(CPLayoutParser.COLON, 0); }
		public TerminalNode NUM() { return getToken(CPLayoutParser.NUM, 0); }
		public PadSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_padSpec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLayoutListener ) ((CPLayoutListener)listener).enterPadSpec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLayoutListener ) ((CPLayoutListener)listener).exitPadSpec(this);
		}
	}

	public final PadSpecContext padSpec() throws RecognitionException {
		PadSpecContext _localctx = new PadSpecContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_padSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 126L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(56);
			match(COLON);
			setState(57);
			match(NUM);
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

	@SuppressWarnings("CheckReturnValue")
	public static class PosSpecContext extends ParserRuleContext {
		public TerminalNode COLON() { return getToken(CPLayoutParser.COLON, 0); }
		public TerminalNode LPAR() { return getToken(CPLayoutParser.LPAR, 0); }
		public TerminalNode RPAR() { return getToken(CPLayoutParser.RPAR, 0); }
		public TerminalNode ID() { return getToken(CPLayoutParser.ID, 0); }
		public PosSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_posSpec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLayoutListener ) ((CPLayoutListener)listener).enterPosSpec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLayoutListener ) ((CPLayoutListener)listener).exitPosSpec(this);
		}
	}

	public final PosSpecContext posSpec() throws RecognitionException {
		PosSpecContext _localctx = new PosSpecContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_posSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			match(T__6);
			setState(60);
			match(COLON);
			setState(61);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 3840L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(62);
			match(LPAR);
			setState(64);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(63);
				match(ID);
				}
			}

			setState(66);
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

	@SuppressWarnings("CheckReturnValue")
	public static class FloatSpecContext extends ParserRuleContext {
		public TerminalNode COLON() { return getToken(CPLayoutParser.COLON, 0); }
		public TerminalNode LPAR() { return getToken(CPLayoutParser.LPAR, 0); }
		public TerminalNode RPAR() { return getToken(CPLayoutParser.RPAR, 0); }
		public TerminalNode ID() { return getToken(CPLayoutParser.ID, 0); }
		public FloatSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_floatSpec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLayoutListener ) ((CPLayoutListener)listener).enterFloatSpec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLayoutListener ) ((CPLayoutListener)listener).exitFloatSpec(this);
		}
	}

	public final FloatSpecContext floatSpec() throws RecognitionException {
		FloatSpecContext _localctx = new FloatSpecContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_floatSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			_la = _input.LA(1);
			if ( !(_la==T__11 || _la==T__12) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(69);
			match(COLON);
			setState(70);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 16414L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(71);
			match(LPAR);
			setState(73);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(72);
				match(ID);
				}
			}

			setState(75);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1:
			return decls_sempred((DeclsContext)_localctx, predIndex);
		case 3:
			return specs_sempred((SpecsContext)_localctx, predIndex);
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
	private boolean specs_sempred(SpecsContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0019N\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0001"+
		"\u0000\u0005\u0000\u0012\b\u0000\n\u0000\f\u0000\u0015\t\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0005\u0001\u001e\b\u0001\n\u0001\f\u0001!\t\u0001\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003.\b\u0003\n\u0003"+
		"\f\u00031\t\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u00046\b\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006A\b\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0003\u0007J\b\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0000\u0002"+
		"\u0002\u0006\b\u0000\u0002\u0004\u0006\b\n\f\u000e\u0000\u0004\u0001\u0000"+
		"\u0001\u0006\u0001\u0000\b\u000b\u0001\u0000\f\r\u0002\u0000\u0001\u0004"+
		"\u000e\u000eL\u0000\u0013\u0001\u0000\u0000\u0000\u0002\u0018\u0001\u0000"+
		"\u0000\u0000\u0004\"\u0001\u0000\u0000\u0000\u0006\'\u0001\u0000\u0000"+
		"\u0000\b5\u0001\u0000\u0000\u0000\n7\u0001\u0000\u0000\u0000\f;\u0001"+
		"\u0000\u0000\u0000\u000eD\u0001\u0000\u0000\u0000\u0010\u0012\u0003\u0002"+
		"\u0001\u0000\u0011\u0010\u0001\u0000\u0000\u0000\u0012\u0015\u0001\u0000"+
		"\u0000\u0000\u0013\u0011\u0001\u0000\u0000\u0000\u0013\u0014\u0001\u0000"+
		"\u0000\u0000\u0014\u0016\u0001\u0000\u0000\u0000\u0015\u0013\u0001\u0000"+
		"\u0000\u0000\u0016\u0017\u0005\u0000\u0000\u0001\u0017\u0001\u0001\u0000"+
		"\u0000\u0000\u0018\u0019\u0006\u0001\uffff\uffff\u0000\u0019\u001a\u0003"+
		"\u0004\u0002\u0000\u001a\u001f\u0001\u0000\u0000\u0000\u001b\u001c\n\u0001"+
		"\u0000\u0000\u001c\u001e\u0003\u0004\u0002\u0000\u001d\u001b\u0001\u0000"+
		"\u0000\u0000\u001e!\u0001\u0000\u0000\u0000\u001f\u001d\u0001\u0000\u0000"+
		"\u0000\u001f \u0001\u0000\u0000\u0000 \u0003\u0001\u0000\u0000\u0000!"+
		"\u001f\u0001\u0000\u0000\u0000\"#\u0005\u0016\u0000\u0000#$\u0005\u000f"+
		"\u0000\u0000$%\u0003\u0006\u0003\u0000%&\u0005\u0010\u0000\u0000&\u0005"+
		"\u0001\u0000\u0000\u0000\'(\u0006\u0003\uffff\uffff\u0000()\u0003\b\u0004"+
		"\u0000)/\u0001\u0000\u0000\u0000*+\n\u0001\u0000\u0000+,\u0005\u0012\u0000"+
		"\u0000,.\u0003\b\u0004\u0000-*\u0001\u0000\u0000\u0000.1\u0001\u0000\u0000"+
		"\u0000/-\u0001\u0000\u0000\u0000/0\u0001\u0000\u0000\u00000\u0007\u0001"+
		"\u0000\u0000\u00001/\u0001\u0000\u0000\u000026\u0003\n\u0005\u000036\u0003"+
		"\f\u0006\u000046\u0003\u000e\u0007\u000052\u0001\u0000\u0000\u000053\u0001"+
		"\u0000\u0000\u000054\u0001\u0000\u0000\u00006\t\u0001\u0000\u0000\u0000"+
		"78\u0007\u0000\u0000\u000089\u0005\u0011\u0000\u00009:\u0005\u0015\u0000"+
		"\u0000:\u000b\u0001\u0000\u0000\u0000;<\u0005\u0007\u0000\u0000<=\u0005"+
		"\u0011\u0000\u0000=>\u0007\u0001\u0000\u0000>@\u0005\u0013\u0000\u0000"+
		"?A\u0005\u0016\u0000\u0000@?\u0001\u0000\u0000\u0000@A\u0001\u0000\u0000"+
		"\u0000AB\u0001\u0000\u0000\u0000BC\u0005\u0014\u0000\u0000C\r\u0001\u0000"+
		"\u0000\u0000DE\u0007\u0002\u0000\u0000EF\u0005\u0011\u0000\u0000FG\u0007"+
		"\u0003\u0000\u0000GI\u0005\u0013\u0000\u0000HJ\u0005\u0016\u0000\u0000"+
		"IH\u0001\u0000\u0000\u0000IJ\u0001\u0000\u0000\u0000JK\u0001\u0000\u0000"+
		"\u0000KL\u0005\u0014\u0000\u0000L\u000f\u0001\u0000\u0000\u0000\u0006"+
		"\u0013\u001f/5@I";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}