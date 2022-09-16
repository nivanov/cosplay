// Generated from /Users/nivanov/cosplay/modules/cosplay/src/main/scala/org/cosplay/games/mir/os/progs/asm/compiler/antlr4/MirAsm.g4 by ANTLR 4.10.1
package org.cosplay.games.mir.os.progs.asm.compiler.antlr4;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MirAsmLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		INSRT_NAME=1, DQSTRING=2, NULL=3, DQUOTE=4, SCOLOR=5, COMMA=6, NL=7, DOLLAR=8, 
		COLON=9, DOT=10, INT=11, REAL=12, EXP=13, USR_ID=14, SYS_ID=15, COMMENT=16, 
		WS=17, ErrorChar=18;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"INSRT_NAME", "DQSTRING", "NULL", "DQUOTE", "SCOLOR", "COMMA", "NL", 
			"DOLLAR", "COLON", "DOT", "INT", "REAL", "EXP", "USR_ID", "SYS_ID", "COMMENT", 
			"WS", "ErrorChar"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'null'", "'\"'", "';'", "','", "'\\n'", "'$'", "':'", 
			"'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "INSRT_NAME", "DQSTRING", "NULL", "DQUOTE", "SCOLOR", "COMMA", 
			"NL", "DOLLAR", "COLON", "DOT", "INT", "REAL", "EXP", "USR_ID", "SYS_ID", 
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


	public MirAsmLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "MirAsm.g4"; }

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
		"\u0004\u0000\u0012\u00a7\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0003\u0000Q\b\u0000\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0005\u0001W\b\u0001\n\u0001\f\u0001Z\t\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001"+
		"\t\u0001\t\u0001\n\u0001\n\u0001\n\u0005\nt\b\n\n\n\f\nw\t\n\u0003\ny"+
		"\b\n\u0001\u000b\u0001\u000b\u0004\u000b}\b\u000b\u000b\u000b\f\u000b"+
		"~\u0001\f\u0001\f\u0003\f\u0083\b\f\u0001\f\u0001\f\u0001\r\u0004\r\u0088"+
		"\b\r\u000b\r\f\r\u0089\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f"+
		"\u0001\u000f\u0005\u000f\u0091\b\u000f\n\u000f\f\u000f\u0094\t\u000f\u0001"+
		"\u000f\u0003\u000f\u0097\b\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u009b"+
		"\b\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0004\u0010\u00a0\b\u0010"+
		"\u000b\u0010\f\u0010\u00a1\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011"+
		"\u0000\u0000\u0012\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005"+
		"\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019"+
		"\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011#\u0012\u0001\u0000\t\u0001"+
		"\u0000\"\"\u0001\u000019\u0002\u000009__\u0001\u000009\u0002\u0000EEe"+
		"e\u0002\u0000++--\u0004\u000009AZ__az\u0002\u0000\n\n\r\r\u0003\u0000"+
		"\t\t\f\r  \u00bd\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001"+
		"\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001"+
		"\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000"+
		"\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000"+
		"\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000"+
		"\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000"+
		"\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000"+
		"\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000"+
		"\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000\u0000\u0001"+
		"P\u0001\u0000\u0000\u0000\u0003R\u0001\u0000\u0000\u0000\u0005]\u0001"+
		"\u0000\u0000\u0000\u0007b\u0001\u0000\u0000\u0000\td\u0001\u0000\u0000"+
		"\u0000\u000bf\u0001\u0000\u0000\u0000\rh\u0001\u0000\u0000\u0000\u000f"+
		"j\u0001\u0000\u0000\u0000\u0011l\u0001\u0000\u0000\u0000\u0013n\u0001"+
		"\u0000\u0000\u0000\u0015x\u0001\u0000\u0000\u0000\u0017z\u0001\u0000\u0000"+
		"\u0000\u0019\u0080\u0001\u0000\u0000\u0000\u001b\u0087\u0001\u0000\u0000"+
		"\u0000\u001d\u008b\u0001\u0000\u0000\u0000\u001f\u008e\u0001\u0000\u0000"+
		"\u0000!\u009f\u0001\u0000\u0000\u0000#\u00a5\u0001\u0000\u0000\u0000%"+
		"&\u0005m\u0000\u0000&\'\u0005o\u0000\u0000\'Q\u0005v\u0000\u0000()\u0005"+
		"p\u0000\u0000)*\u0005u\u0000\u0000*+\u0005s\u0000\u0000+Q\u0005h\u0000"+
		"\u0000,-\u0005p\u0000\u0000-.\u0005o\u0000\u0000.Q\u0005p\u0000\u0000"+
		"/0\u0005a\u0000\u000001\u0005d\u0000\u00001Q\u0005d\u0000\u000023\u0005"+
		"m\u0000\u000034\u0005u\u0000\u00004Q\u0005l\u0000\u000056\u0005d\u0000"+
		"\u000067\u0005i\u0000\u00007Q\u0005v\u0000\u000089\u0005s\u0000\u0000"+
		"9:\u0005u\u0000\u0000:Q\u0005b\u0000\u0000;<\u0005c\u0000\u0000<=\u0005"+
		"a\u0000\u0000=>\u0005l\u0000\u0000>?\u0005l\u0000\u0000?Q\u0005n\u0000"+
		"\u0000@A\u0005c\u0000\u0000AB\u0005a\u0000\u0000BC\u0005l\u0000\u0000"+
		"CQ\u0005l\u0000\u0000DE\u0005j\u0000\u0000EF\u0005m\u0000\u0000FQ\u0005"+
		"p\u0000\u0000GH\u0005r\u0000\u0000HI\u0005e\u0000\u0000IQ\u0005t\u0000"+
		"\u0000JK\u0005l\u0000\u0000KL\u0005e\u0000\u0000LQ\u0005t\u0000\u0000"+
		"MN\u0005d\u0000\u0000NO\u0005u\u0000\u0000OQ\u0005p\u0000\u0000P%\u0001"+
		"\u0000\u0000\u0000P(\u0001\u0000\u0000\u0000P,\u0001\u0000\u0000\u0000"+
		"P/\u0001\u0000\u0000\u0000P2\u0001\u0000\u0000\u0000P5\u0001\u0000\u0000"+
		"\u0000P8\u0001\u0000\u0000\u0000P;\u0001\u0000\u0000\u0000P@\u0001\u0000"+
		"\u0000\u0000PD\u0001\u0000\u0000\u0000PG\u0001\u0000\u0000\u0000PJ\u0001"+
		"\u0000\u0000\u0000PM\u0001\u0000\u0000\u0000Q\u0002\u0001\u0000\u0000"+
		"\u0000RX\u0003\u0007\u0003\u0000SW\b\u0000\u0000\u0000TU\u0005\\\u0000"+
		"\u0000UW\u0005\"\u0000\u0000VS\u0001\u0000\u0000\u0000VT\u0001\u0000\u0000"+
		"\u0000WZ\u0001\u0000\u0000\u0000XV\u0001\u0000\u0000\u0000XY\u0001\u0000"+
		"\u0000\u0000Y[\u0001\u0000\u0000\u0000ZX\u0001\u0000\u0000\u0000[\\\u0003"+
		"\u0007\u0003\u0000\\\u0004\u0001\u0000\u0000\u0000]^\u0005n\u0000\u0000"+
		"^_\u0005u\u0000\u0000_`\u0005l\u0000\u0000`a\u0005l\u0000\u0000a\u0006"+
		"\u0001\u0000\u0000\u0000bc\u0005\"\u0000\u0000c\b\u0001\u0000\u0000\u0000"+
		"de\u0005;\u0000\u0000e\n\u0001\u0000\u0000\u0000fg\u0005,\u0000\u0000"+
		"g\f\u0001\u0000\u0000\u0000hi\u0005\n\u0000\u0000i\u000e\u0001\u0000\u0000"+
		"\u0000jk\u0005$\u0000\u0000k\u0010\u0001\u0000\u0000\u0000lm\u0005:\u0000"+
		"\u0000m\u0012\u0001\u0000\u0000\u0000no\u0005.\u0000\u0000o\u0014\u0001"+
		"\u0000\u0000\u0000py\u00050\u0000\u0000qu\u0007\u0001\u0000\u0000rt\u0007"+
		"\u0002\u0000\u0000sr\u0001\u0000\u0000\u0000tw\u0001\u0000\u0000\u0000"+
		"us\u0001\u0000\u0000\u0000uv\u0001\u0000\u0000\u0000vy\u0001\u0000\u0000"+
		"\u0000wu\u0001\u0000\u0000\u0000xp\u0001\u0000\u0000\u0000xq\u0001\u0000"+
		"\u0000\u0000y\u0016\u0001\u0000\u0000\u0000z|\u0003\u0013\t\u0000{}\u0007"+
		"\u0003\u0000\u0000|{\u0001\u0000\u0000\u0000}~\u0001\u0000\u0000\u0000"+
		"~|\u0001\u0000\u0000\u0000~\u007f\u0001\u0000\u0000\u0000\u007f\u0018"+
		"\u0001\u0000\u0000\u0000\u0080\u0082\u0007\u0004\u0000\u0000\u0081\u0083"+
		"\u0007\u0005\u0000\u0000\u0082\u0081\u0001\u0000\u0000\u0000\u0082\u0083"+
		"\u0001\u0000\u0000\u0000\u0083\u0084\u0001\u0000\u0000\u0000\u0084\u0085"+
		"\u0003\u0015\n\u0000\u0085\u001a\u0001\u0000\u0000\u0000\u0086\u0088\u0007"+
		"\u0006\u0000\u0000\u0087\u0086\u0001\u0000\u0000\u0000\u0088\u0089\u0001"+
		"\u0000\u0000\u0000\u0089\u0087\u0001\u0000\u0000\u0000\u0089\u008a\u0001"+
		"\u0000\u0000\u0000\u008a\u001c\u0001\u0000\u0000\u0000\u008b\u008c\u0005"+
		"$\u0000\u0000\u008c\u008d\u0003\u001b\r\u0000\u008d\u001e\u0001\u0000"+
		"\u0000\u0000\u008e\u0092\u0003\t\u0004\u0000\u008f\u0091\b\u0007\u0000"+
		"\u0000\u0090\u008f\u0001\u0000\u0000\u0000\u0091\u0094\u0001\u0000\u0000"+
		"\u0000\u0092\u0090\u0001\u0000\u0000\u0000\u0092\u0093\u0001\u0000\u0000"+
		"\u0000\u0093\u0096\u0001\u0000\u0000\u0000\u0094\u0092\u0001\u0000\u0000"+
		"\u0000\u0095\u0097\u0005\r\u0000\u0000\u0096\u0095\u0001\u0000\u0000\u0000"+
		"\u0096\u0097\u0001\u0000\u0000\u0000\u0097\u009a\u0001\u0000\u0000\u0000"+
		"\u0098\u009b\u0003\r\u0006\u0000\u0099\u009b\u0005\u0000\u0000\u0001\u009a"+
		"\u0098\u0001\u0000\u0000\u0000\u009a\u0099\u0001\u0000\u0000\u0000\u009b"+
		"\u009c\u0001\u0000\u0000\u0000\u009c\u009d\u0006\u000f\u0000\u0000\u009d"+
		" \u0001\u0000\u0000\u0000\u009e\u00a0\u0007\b\u0000\u0000\u009f\u009e"+
		"\u0001\u0000\u0000\u0000\u00a0\u00a1\u0001\u0000\u0000\u0000\u00a1\u009f"+
		"\u0001\u0000\u0000\u0000\u00a1\u00a2\u0001\u0000\u0000\u0000\u00a2\u00a3"+
		"\u0001\u0000\u0000\u0000\u00a3\u00a4\u0006\u0010\u0000\u0000\u00a4\"\u0001"+
		"\u0000\u0000\u0000\u00a5\u00a6\t\u0000\u0000\u0000\u00a6$\u0001\u0000"+
		"\u0000\u0000\r\u0000PVXux~\u0082\u0089\u0092\u0096\u009a\u00a1\u0001\u0006"+
		"\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}