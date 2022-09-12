// Generated from C:/Users/Nikita Ivanov/Documents/GitHub/cosplay/modules/cosplay/src/main/scala/org/cosplay/games/mir/os/progs/asm/compiler/antlr4\MirAsm.g4 by ANTLR 4.10.1
package org.cosplay.games.mir.os.progs.asm.compiler.antlr4;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MirAsmParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		INSRT_NAME=1, DQSTRING=2, NULL=3, DQUOTE=4, SCOLOR=5, COMMA=6, NL=7, COLON=8, 
		DOT=9, INT=10, REAL=11, EXP=12, ID=13, COMMENT=14, WS=15, ErrorChar=16;
	public static final int
		RULE_asm = 0, RULE_code = 1, RULE_inst = 2, RULE_label = 3, RULE_plist = 4, 
		RULE_param = 5;
	private static String[] makeRuleNames() {
		return new String[] {
			"asm", "code", "inst", "label", "plist", "param"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'null'", "'\"'", "';'", "','", "'\\n'", "':'", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "INSRT_NAME", "DQSTRING", "NULL", "DQUOTE", "SCOLOR", "COMMA", 
			"NL", "COLON", "DOT", "INT", "REAL", "EXP", "ID", "COMMENT", "WS", "ErrorChar"
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
	public String getGrammarFileName() { return "MirAsm.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MirAsmParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class AsmContext extends ParserRuleContext {
		public CodeContext code() {
			return getRuleContext(CodeContext.class,0);
		}
		public TerminalNode EOF() { return getToken(MirAsmParser.EOF, 0); }
		public AsmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirAsmListener ) ((MirAsmListener)listener).enterAsm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirAsmListener ) ((MirAsmListener)listener).exitAsm(this);
		}
	}

	public final AsmContext asm() throws RecognitionException {
		AsmContext _localctx = new AsmContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_asm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(12);
			code(0);
			setState(13);
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

	public static class CodeContext extends ParserRuleContext {
		public InstContext inst() {
			return getRuleContext(InstContext.class,0);
		}
		public TerminalNode EOF() { return getToken(MirAsmParser.EOF, 0); }
		public List<TerminalNode> NL() { return getTokens(MirAsmParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(MirAsmParser.NL, i);
		}
		public CodeContext code() {
			return getRuleContext(CodeContext.class,0);
		}
		public CodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_code; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirAsmListener ) ((MirAsmListener)listener).enterCode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirAsmListener ) ((MirAsmListener)listener).exitCode(this);
		}
	}

	public final CodeContext code() throws RecognitionException {
		return code(0);
	}

	private CodeContext code(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		CodeContext _localctx = new CodeContext(_ctx, _parentState);
		CodeContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_code, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(19);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(16);
				match(NL);
				}
				}
				setState(21);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(22);
			inst();
			setState(29);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NL:
				{
				setState(24); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(23);
						match(NL);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(26); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case EOF:
				{
				setState(28);
				match(EOF);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
			_ctx.stop = _input.LT(-1);
			setState(44);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new CodeContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_code);
					setState(31);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(32);
					inst();
					setState(40);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
					case 1:
						{
						setState(36);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
						while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
							if ( _alt==1 ) {
								{
								{
								setState(33);
								match(NL);
								}
								} 
							}
							setState(38);
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
						}
						}
						break;
					case 2:
						{
						setState(39);
						match(EOF);
						}
						break;
					}
					}
					} 
				}
				setState(46);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
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

	public static class InstContext extends ParserRuleContext {
		public TerminalNode INSRT_NAME() { return getToken(MirAsmParser.INSRT_NAME, 0); }
		public LabelContext label() {
			return getRuleContext(LabelContext.class,0);
		}
		public PlistContext plist() {
			return getRuleContext(PlistContext.class,0);
		}
		public InstContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inst; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirAsmListener ) ((MirAsmListener)listener).enterInst(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirAsmListener ) ((MirAsmListener)listener).exitInst(this);
		}
	}

	public final InstContext inst() throws RecognitionException {
		InstContext _localctx = new InstContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_inst);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(47);
				label();
				}
			}

			setState(50);
			match(INSRT_NAME);
			setState(52);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(51);
				plist(0);
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

	public static class LabelContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(MirAsmParser.ID, 0); }
		public TerminalNode COLON() { return getToken(MirAsmParser.COLON, 0); }
		public List<TerminalNode> NL() { return getTokens(MirAsmParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(MirAsmParser.NL, i);
		}
		public LabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_label; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirAsmListener ) ((MirAsmListener)listener).enterLabel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirAsmListener ) ((MirAsmListener)listener).exitLabel(this);
		}
	}

	public final LabelContext label() throws RecognitionException {
		LabelContext _localctx = new LabelContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_label);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(54);
			match(ID);
			setState(55);
			match(COLON);
			setState(59);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NL) {
				{
				{
				setState(56);
				match(NL);
				}
				}
				setState(61);
				_errHandler.sync(this);
				_la = _input.LA(1);
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

	public static class PlistContext extends ParserRuleContext {
		public ParamContext param() {
			return getRuleContext(ParamContext.class,0);
		}
		public PlistContext plist() {
			return getRuleContext(PlistContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(MirAsmParser.COMMA, 0); }
		public PlistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_plist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirAsmListener ) ((MirAsmListener)listener).enterPlist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirAsmListener ) ((MirAsmListener)listener).exitPlist(this);
		}
	}

	public final PlistContext plist() throws RecognitionException {
		return plist(0);
	}

	private PlistContext plist(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		PlistContext _localctx = new PlistContext(_ctx, _parentState);
		PlistContext _prevctx = _localctx;
		int _startState = 8;
		enterRecursionRule(_localctx, 8, RULE_plist, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(63);
			param();
			}
			_ctx.stop = _input.LT(-1);
			setState(70);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new PlistContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_plist);
					setState(65);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(66);
					match(COMMA);
					setState(67);
					param();
					}
					} 
				}
				setState(72);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
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

	public static class ParamContext extends ParserRuleContext {
		public TerminalNode DQSTRING() { return getToken(MirAsmParser.DQSTRING, 0); }
		public TerminalNode NULL() { return getToken(MirAsmParser.NULL, 0); }
		public TerminalNode ID() { return getToken(MirAsmParser.ID, 0); }
		public TerminalNode INT() { return getToken(MirAsmParser.INT, 0); }
		public TerminalNode REAL() { return getToken(MirAsmParser.REAL, 0); }
		public TerminalNode EXP() { return getToken(MirAsmParser.EXP, 0); }
		public ParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MirAsmListener ) ((MirAsmListener)listener).enterParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MirAsmListener ) ((MirAsmListener)listener).exitParam(this);
		}
	}

	public final ParamContext param() throws RecognitionException {
		ParamContext _localctx = new ParamContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_param);
		try {
			setState(83);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DQSTRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(73);
				match(DQSTRING);
				}
				break;
			case NULL:
				enterOuterAlt(_localctx, 2);
				{
				setState(74);
				match(NULL);
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 3);
				{
				setState(75);
				match(ID);
				}
				break;
			case INT:
				enterOuterAlt(_localctx, 4);
				{
				setState(76);
				match(INT);
				setState(78);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
				case 1:
					{
					setState(77);
					match(REAL);
					}
					break;
				}
				setState(81);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
				case 1:
					{
					setState(80);
					match(EXP);
					}
					break;
				}
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1:
			return code_sempred((CodeContext)_localctx, predIndex);
		case 4:
			return plist_sempred((PlistContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean code_sempred(CodeContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean plist_sempred(PlistContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0010V\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001"+
		"\u0001\u0005\u0001\u0012\b\u0001\n\u0001\f\u0001\u0015\t\u0001\u0001\u0001"+
		"\u0001\u0001\u0004\u0001\u0019\b\u0001\u000b\u0001\f\u0001\u001a\u0001"+
		"\u0001\u0003\u0001\u001e\b\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0005"+
		"\u0001#\b\u0001\n\u0001\f\u0001&\t\u0001\u0001\u0001\u0003\u0001)\b\u0001"+
		"\u0005\u0001+\b\u0001\n\u0001\f\u0001.\t\u0001\u0001\u0002\u0003\u0002"+
		"1\b\u0002\u0001\u0002\u0001\u0002\u0003\u00025\b\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0005\u0003:\b\u0003\n\u0003\f\u0003=\t\u0003\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0005"+
		"\u0004E\b\u0004\n\u0004\f\u0004H\t\u0004\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0003\u0005O\b\u0005\u0001\u0005\u0003"+
		"\u0005R\b\u0005\u0003\u0005T\b\u0005\u0001\u0005\u0000\u0002\u0002\b\u0006"+
		"\u0000\u0002\u0004\u0006\b\n\u0000\u0000^\u0000\f\u0001\u0000\u0000\u0000"+
		"\u0002\u000f\u0001\u0000\u0000\u0000\u00040\u0001\u0000\u0000\u0000\u0006"+
		"6\u0001\u0000\u0000\u0000\b>\u0001\u0000\u0000\u0000\nS\u0001\u0000\u0000"+
		"\u0000\f\r\u0003\u0002\u0001\u0000\r\u000e\u0005\u0000\u0000\u0001\u000e"+
		"\u0001\u0001\u0000\u0000\u0000\u000f\u0013\u0006\u0001\uffff\uffff\u0000"+
		"\u0010\u0012\u0005\u0007\u0000\u0000\u0011\u0010\u0001\u0000\u0000\u0000"+
		"\u0012\u0015\u0001\u0000\u0000\u0000\u0013\u0011\u0001\u0000\u0000\u0000"+
		"\u0013\u0014\u0001\u0000\u0000\u0000\u0014\u0016\u0001\u0000\u0000\u0000"+
		"\u0015\u0013\u0001\u0000\u0000\u0000\u0016\u001d\u0003\u0004\u0002\u0000"+
		"\u0017\u0019\u0005\u0007\u0000\u0000\u0018\u0017\u0001\u0000\u0000\u0000"+
		"\u0019\u001a\u0001\u0000\u0000\u0000\u001a\u0018\u0001\u0000\u0000\u0000"+
		"\u001a\u001b\u0001\u0000\u0000\u0000\u001b\u001e\u0001\u0000\u0000\u0000"+
		"\u001c\u001e\u0005\u0000\u0000\u0001\u001d\u0018\u0001\u0000\u0000\u0000"+
		"\u001d\u001c\u0001\u0000\u0000\u0000\u001e,\u0001\u0000\u0000\u0000\u001f"+
		" \n\u0001\u0000\u0000 (\u0003\u0004\u0002\u0000!#\u0005\u0007\u0000\u0000"+
		"\"!\u0001\u0000\u0000\u0000#&\u0001\u0000\u0000\u0000$\"\u0001\u0000\u0000"+
		"\u0000$%\u0001\u0000\u0000\u0000%)\u0001\u0000\u0000\u0000&$\u0001\u0000"+
		"\u0000\u0000\')\u0005\u0000\u0000\u0001($\u0001\u0000\u0000\u0000(\'\u0001"+
		"\u0000\u0000\u0000)+\u0001\u0000\u0000\u0000*\u001f\u0001\u0000\u0000"+
		"\u0000+.\u0001\u0000\u0000\u0000,*\u0001\u0000\u0000\u0000,-\u0001\u0000"+
		"\u0000\u0000-\u0003\u0001\u0000\u0000\u0000.,\u0001\u0000\u0000\u0000"+
		"/1\u0003\u0006\u0003\u00000/\u0001\u0000\u0000\u000001\u0001\u0000\u0000"+
		"\u000012\u0001\u0000\u0000\u000024\u0005\u0001\u0000\u000035\u0003\b\u0004"+
		"\u000043\u0001\u0000\u0000\u000045\u0001\u0000\u0000\u00005\u0005\u0001"+
		"\u0000\u0000\u000067\u0005\r\u0000\u00007;\u0005\b\u0000\u00008:\u0005"+
		"\u0007\u0000\u000098\u0001\u0000\u0000\u0000:=\u0001\u0000\u0000\u0000"+
		";9\u0001\u0000\u0000\u0000;<\u0001\u0000\u0000\u0000<\u0007\u0001\u0000"+
		"\u0000\u0000=;\u0001\u0000\u0000\u0000>?\u0006\u0004\uffff\uffff\u0000"+
		"?@\u0003\n\u0005\u0000@F\u0001\u0000\u0000\u0000AB\n\u0001\u0000\u0000"+
		"BC\u0005\u0006\u0000\u0000CE\u0003\n\u0005\u0000DA\u0001\u0000\u0000\u0000"+
		"EH\u0001\u0000\u0000\u0000FD\u0001\u0000\u0000\u0000FG\u0001\u0000\u0000"+
		"\u0000G\t\u0001\u0000\u0000\u0000HF\u0001\u0000\u0000\u0000IT\u0005\u0002"+
		"\u0000\u0000JT\u0005\u0003\u0000\u0000KT\u0005\r\u0000\u0000LN\u0005\n"+
		"\u0000\u0000MO\u0005\u000b\u0000\u0000NM\u0001\u0000\u0000\u0000NO\u0001"+
		"\u0000\u0000\u0000OQ\u0001\u0000\u0000\u0000PR\u0005\f\u0000\u0000QP\u0001"+
		"\u0000\u0000\u0000QR\u0001\u0000\u0000\u0000RT\u0001\u0000\u0000\u0000"+
		"SI\u0001\u0000\u0000\u0000SJ\u0001\u0000\u0000\u0000SK\u0001\u0000\u0000"+
		"\u0000SL\u0001\u0000\u0000\u0000T\u000b\u0001\u0000\u0000\u0000\r\u0013"+
		"\u001a\u001d$(,04;FNQS";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}