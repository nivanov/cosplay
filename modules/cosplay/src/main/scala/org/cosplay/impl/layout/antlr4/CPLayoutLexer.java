// Generated from /Users/nivanov/cosplay/modules/cosplay/src/main/scala/org/cosplay/impl/layout/antlr4/CPLayout.g4 by ANTLR 4.12.0
package org.cosplay.impl.layout.antlr4;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class CPLayoutLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.12.0", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, EQ=11, SCOLON=12, COLON=13, COMMA=14, LPAR=15, RPAR=16, NUM=17, 
		ID=18, COMMENT=19, WS=20, ErrorChar=21;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "EQ", "SCOLON", "COLON", "COMMA", "LPAR", "RPAR", "NUM", "ID", 
			"COMMENT", "WS", "ErrorChar"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'top'", "'left'", "'bottom'", "'right'", "'vert'", "'hor'", "'pos'", 
			"'xfloat'", "'yfloat'", "'center'", "'='", "';'", "':'", "','", "'('", 
			"')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, "EQ", 
			"SCOLON", "COLON", "COMMA", "LPAR", "RPAR", "NUM", "ID", "COMMENT", "WS", 
			"ErrorChar"
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


	public CPLayoutLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CPLayout.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u0015\u00a5\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b"+
		"\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001"+
		"\u000f\u0001\u000f\u0001\u0010\u0003\u0010q\b\u0010\u0001\u0010\u0001"+
		"\u0010\u0005\u0010u\b\u0010\n\u0010\f\u0010x\t\u0010\u0001\u0011\u0004"+
		"\u0011{\b\u0011\u000b\u0011\f\u0011|\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0005\u0012\u0083\b\u0012\n\u0012\f\u0012\u0086\t\u0012\u0001"+
		"\u0012\u0003\u0012\u0089\b\u0012\u0001\u0012\u0003\u0012\u008c\b\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0005\u0012\u0092\b\u0012"+
		"\n\u0012\f\u0012\u0095\t\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u0099"+
		"\b\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0004\u0013\u009e\b\u0013"+
		"\u000b\u0013\f\u0013\u009f\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014"+
		"\u0001\u0093\u0000\u0015\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004"+
		"\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017"+
		"\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011#\u0012%\u0013\'"+
		"\u0014)\u0015\u0001\u0000\u0005\u0001\u000009\u0006\u0000$$--09AZ__az"+
		"\u0002\u0000\n\n\r\r\u0001\u0001\n\n\u0003\u0000\t\n\f\r  \u00ac\u0000"+
		"\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000"+
		"\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000"+
		"\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r"+
		"\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011"+
		"\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015"+
		"\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019"+
		"\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d"+
		"\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001"+
		"\u0000\u0000\u0000\u0000#\u0001\u0000\u0000\u0000\u0000%\u0001\u0000\u0000"+
		"\u0000\u0000\'\u0001\u0000\u0000\u0000\u0000)\u0001\u0000\u0000\u0000"+
		"\u0001+\u0001\u0000\u0000\u0000\u0003/\u0001\u0000\u0000\u0000\u00054"+
		"\u0001\u0000\u0000\u0000\u0007;\u0001\u0000\u0000\u0000\tA\u0001\u0000"+
		"\u0000\u0000\u000bF\u0001\u0000\u0000\u0000\rJ\u0001\u0000\u0000\u0000"+
		"\u000fN\u0001\u0000\u0000\u0000\u0011U\u0001\u0000\u0000\u0000\u0013\\"+
		"\u0001\u0000\u0000\u0000\u0015c\u0001\u0000\u0000\u0000\u0017e\u0001\u0000"+
		"\u0000\u0000\u0019g\u0001\u0000\u0000\u0000\u001bi\u0001\u0000\u0000\u0000"+
		"\u001dk\u0001\u0000\u0000\u0000\u001fm\u0001\u0000\u0000\u0000!p\u0001"+
		"\u0000\u0000\u0000#z\u0001\u0000\u0000\u0000%\u0098\u0001\u0000\u0000"+
		"\u0000\'\u009d\u0001\u0000\u0000\u0000)\u00a3\u0001\u0000\u0000\u0000"+
		"+,\u0005t\u0000\u0000,-\u0005o\u0000\u0000-.\u0005p\u0000\u0000.\u0002"+
		"\u0001\u0000\u0000\u0000/0\u0005l\u0000\u000001\u0005e\u0000\u000012\u0005"+
		"f\u0000\u000023\u0005t\u0000\u00003\u0004\u0001\u0000\u0000\u000045\u0005"+
		"b\u0000\u000056\u0005o\u0000\u000067\u0005t\u0000\u000078\u0005t\u0000"+
		"\u000089\u0005o\u0000\u00009:\u0005m\u0000\u0000:\u0006\u0001\u0000\u0000"+
		"\u0000;<\u0005r\u0000\u0000<=\u0005i\u0000\u0000=>\u0005g\u0000\u0000"+
		">?\u0005h\u0000\u0000?@\u0005t\u0000\u0000@\b\u0001\u0000\u0000\u0000"+
		"AB\u0005v\u0000\u0000BC\u0005e\u0000\u0000CD\u0005r\u0000\u0000DE\u0005"+
		"t\u0000\u0000E\n\u0001\u0000\u0000\u0000FG\u0005h\u0000\u0000GH\u0005"+
		"o\u0000\u0000HI\u0005r\u0000\u0000I\f\u0001\u0000\u0000\u0000JK\u0005"+
		"p\u0000\u0000KL\u0005o\u0000\u0000LM\u0005s\u0000\u0000M\u000e\u0001\u0000"+
		"\u0000\u0000NO\u0005x\u0000\u0000OP\u0005f\u0000\u0000PQ\u0005l\u0000"+
		"\u0000QR\u0005o\u0000\u0000RS\u0005a\u0000\u0000ST\u0005t\u0000\u0000"+
		"T\u0010\u0001\u0000\u0000\u0000UV\u0005y\u0000\u0000VW\u0005f\u0000\u0000"+
		"WX\u0005l\u0000\u0000XY\u0005o\u0000\u0000YZ\u0005a\u0000\u0000Z[\u0005"+
		"t\u0000\u0000[\u0012\u0001\u0000\u0000\u0000\\]\u0005c\u0000\u0000]^\u0005"+
		"e\u0000\u0000^_\u0005n\u0000\u0000_`\u0005t\u0000\u0000`a\u0005e\u0000"+
		"\u0000ab\u0005r\u0000\u0000b\u0014\u0001\u0000\u0000\u0000cd\u0005=\u0000"+
		"\u0000d\u0016\u0001\u0000\u0000\u0000ef\u0005;\u0000\u0000f\u0018\u0001"+
		"\u0000\u0000\u0000gh\u0005:\u0000\u0000h\u001a\u0001\u0000\u0000\u0000"+
		"ij\u0005,\u0000\u0000j\u001c\u0001\u0000\u0000\u0000kl\u0005(\u0000\u0000"+
		"l\u001e\u0001\u0000\u0000\u0000mn\u0005)\u0000\u0000n \u0001\u0000\u0000"+
		"\u0000oq\u0005-\u0000\u0000po\u0001\u0000\u0000\u0000pq\u0001\u0000\u0000"+
		"\u0000qr\u0001\u0000\u0000\u0000rv\u0007\u0000\u0000\u0000su\u0007\u0000"+
		"\u0000\u0000ts\u0001\u0000\u0000\u0000ux\u0001\u0000\u0000\u0000vt\u0001"+
		"\u0000\u0000\u0000vw\u0001\u0000\u0000\u0000w\"\u0001\u0000\u0000\u0000"+
		"xv\u0001\u0000\u0000\u0000y{\u0007\u0001\u0000\u0000zy\u0001\u0000\u0000"+
		"\u0000{|\u0001\u0000\u0000\u0000|z\u0001\u0000\u0000\u0000|}\u0001\u0000"+
		"\u0000\u0000}$\u0001\u0000\u0000\u0000~\u007f\u0005/\u0000\u0000\u007f"+
		"\u0080\u0005/\u0000\u0000\u0080\u0084\u0001\u0000\u0000\u0000\u0081\u0083"+
		"\b\u0002\u0000\u0000\u0082\u0081\u0001\u0000\u0000\u0000\u0083\u0086\u0001"+
		"\u0000\u0000\u0000\u0084\u0082\u0001\u0000\u0000\u0000\u0084\u0085\u0001"+
		"\u0000\u0000\u0000\u0085\u0088\u0001\u0000\u0000\u0000\u0086\u0084\u0001"+
		"\u0000\u0000\u0000\u0087\u0089\u0005\r\u0000\u0000\u0088\u0087\u0001\u0000"+
		"\u0000\u0000\u0088\u0089\u0001\u0000\u0000\u0000\u0089\u008b\u0001\u0000"+
		"\u0000\u0000\u008a\u008c\u0007\u0003\u0000\u0000\u008b\u008a\u0001\u0000"+
		"\u0000\u0000\u008c\u0099\u0001\u0000\u0000\u0000\u008d\u008e\u0005/\u0000"+
		"\u0000\u008e\u008f\u0005*\u0000\u0000\u008f\u0093\u0001\u0000\u0000\u0000"+
		"\u0090\u0092\t\u0000\u0000\u0000\u0091\u0090\u0001\u0000\u0000\u0000\u0092"+
		"\u0095\u0001\u0000\u0000\u0000\u0093\u0094\u0001\u0000\u0000\u0000\u0093"+
		"\u0091\u0001\u0000\u0000\u0000\u0094\u0096\u0001\u0000\u0000\u0000\u0095"+
		"\u0093\u0001\u0000\u0000\u0000\u0096\u0097\u0005*\u0000\u0000\u0097\u0099"+
		"\u0005/\u0000\u0000\u0098~\u0001\u0000\u0000\u0000\u0098\u008d\u0001\u0000"+
		"\u0000\u0000\u0099\u009a\u0001\u0000\u0000\u0000\u009a\u009b\u0006\u0012"+
		"\u0000\u0000\u009b&\u0001\u0000\u0000\u0000\u009c\u009e\u0007\u0004\u0000"+
		"\u0000\u009d\u009c\u0001\u0000\u0000\u0000\u009e\u009f\u0001\u0000\u0000"+
		"\u0000\u009f\u009d\u0001\u0000\u0000\u0000\u009f\u00a0\u0001\u0000\u0000"+
		"\u0000\u00a0\u00a1\u0001\u0000\u0000\u0000\u00a1\u00a2\u0006\u0013\u0000"+
		"\u0000\u00a2(\u0001\u0000\u0000\u0000\u00a3\u00a4\t\u0000\u0000\u0000"+
		"\u00a4*\u0001\u0000\u0000\u0000\n\u0000pv|\u0084\u0088\u008b\u0093\u0098"+
		"\u009f\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}