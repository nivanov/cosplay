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
		"\u0004\u0000\u0012\u00a4\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
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
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000N\b\u0000"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0005\u0001T\b\u0001"+
		"\n\u0001\f\u0001W\t\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004"+
		"\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007"+
		"\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0005"+
		"\nq\b\n\n\n\f\nt\t\n\u0003\nv\b\n\u0001\u000b\u0001\u000b\u0004\u000b"+
		"z\b\u000b\u000b\u000b\f\u000b{\u0001\f\u0001\f\u0003\f\u0080\b\f\u0001"+
		"\f\u0001\f\u0001\r\u0004\r\u0085\b\r\u000b\r\f\r\u0086\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0005\u000f\u008e\b\u000f\n"+
		"\u000f\f\u000f\u0091\t\u000f\u0001\u000f\u0003\u000f\u0094\b\u000f\u0001"+
		"\u000f\u0001\u000f\u0003\u000f\u0098\b\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u0010\u0004\u0010\u009d\b\u0010\u000b\u0010\f\u0010\u009e\u0001\u0010"+
		"\u0001\u0010\u0001\u0011\u0001\u0011\u0000\u0000\u0012\u0001\u0001\u0003"+
		"\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011"+
		"\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010"+
		"!\u0011#\u0012\u0001\u0000\t\u0001\u0000\"\"\u0001\u000019\u0002\u0000"+
		"09__\u0001\u000009\u0002\u0000EEee\u0002\u0000++--\u0004\u000009AZ__a"+
		"z\u0002\u0000\n\n\r\r\u0003\u0000\t\t\f\r  \u00b9\u0000\u0001\u0001\u0000"+
		"\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000"+
		"\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000"+
		"\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000"+
		"\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000"+
		"\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000"+
		"\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000"+
		"\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000"+
		"\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000"+
		"#\u0001\u0000\u0000\u0000\u0001M\u0001\u0000\u0000\u0000\u0003O\u0001"+
		"\u0000\u0000\u0000\u0005Z\u0001\u0000\u0000\u0000\u0007_\u0001\u0000\u0000"+
		"\u0000\ta\u0001\u0000\u0000\u0000\u000bc\u0001\u0000\u0000\u0000\re\u0001"+
		"\u0000\u0000\u0000\u000fg\u0001\u0000\u0000\u0000\u0011i\u0001\u0000\u0000"+
		"\u0000\u0013k\u0001\u0000\u0000\u0000\u0015u\u0001\u0000\u0000\u0000\u0017"+
		"w\u0001\u0000\u0000\u0000\u0019}\u0001\u0000\u0000\u0000\u001b\u0084\u0001"+
		"\u0000\u0000\u0000\u001d\u0088\u0001\u0000\u0000\u0000\u001f\u008b\u0001"+
		"\u0000\u0000\u0000!\u009c\u0001\u0000\u0000\u0000#\u00a2\u0001\u0000\u0000"+
		"\u0000%&\u0005m\u0000\u0000&\'\u0005o\u0000\u0000\'N\u0005v\u0000\u0000"+
		"()\u0005p\u0000\u0000)*\u0005u\u0000\u0000*+\u0005s\u0000\u0000+N\u0005"+
		"h\u0000\u0000,-\u0005p\u0000\u0000-.\u0005o\u0000\u0000.N\u0005p\u0000"+
		"\u0000/0\u0005a\u0000\u000001\u0005d\u0000\u00001N\u0005d\u0000\u0000"+
		"23\u0005m\u0000\u000034\u0005u\u0000\u00004N\u0005l\u0000\u000056\u0005"+
		"d\u0000\u000067\u0005i\u0000\u00007N\u0005v\u0000\u000089\u0005s\u0000"+
		"\u00009:\u0005u\u0000\u0000:N\u0005b\u0000\u0000;<\u0005c\u0000\u0000"+
		"<=\u0005a\u0000\u0000=>\u0005l\u0000\u0000>?\u0005l\u0000\u0000?N\u0005"+
		"n\u0000\u0000@A\u0005c\u0000\u0000AB\u0005a\u0000\u0000BC\u0005l\u0000"+
		"\u0000CN\u0005l\u0000\u0000DE\u0005j\u0000\u0000EF\u0005m\u0000\u0000"+
		"FN\u0005p\u0000\u0000GH\u0005r\u0000\u0000HI\u0005e\u0000\u0000IN\u0005"+
		"t\u0000\u0000JK\u0005l\u0000\u0000KL\u0005e\u0000\u0000LN\u0005t\u0000"+
		"\u0000M%\u0001\u0000\u0000\u0000M(\u0001\u0000\u0000\u0000M,\u0001\u0000"+
		"\u0000\u0000M/\u0001\u0000\u0000\u0000M2\u0001\u0000\u0000\u0000M5\u0001"+
		"\u0000\u0000\u0000M8\u0001\u0000\u0000\u0000M;\u0001\u0000\u0000\u0000"+
		"M@\u0001\u0000\u0000\u0000MD\u0001\u0000\u0000\u0000MG\u0001\u0000\u0000"+
		"\u0000MJ\u0001\u0000\u0000\u0000N\u0002\u0001\u0000\u0000\u0000OU\u0003"+
		"\u0007\u0003\u0000PT\b\u0000\u0000\u0000QR\u0005\\\u0000\u0000RT\u0005"+
		"\"\u0000\u0000SP\u0001\u0000\u0000\u0000SQ\u0001\u0000\u0000\u0000TW\u0001"+
		"\u0000\u0000\u0000US\u0001\u0000\u0000\u0000UV\u0001\u0000\u0000\u0000"+
		"VX\u0001\u0000\u0000\u0000WU\u0001\u0000\u0000\u0000XY\u0003\u0007\u0003"+
		"\u0000Y\u0004\u0001\u0000\u0000\u0000Z[\u0005n\u0000\u0000[\\\u0005u\u0000"+
		"\u0000\\]\u0005l\u0000\u0000]^\u0005l\u0000\u0000^\u0006\u0001\u0000\u0000"+
		"\u0000_`\u0005\"\u0000\u0000`\b\u0001\u0000\u0000\u0000ab\u0005;\u0000"+
		"\u0000b\n\u0001\u0000\u0000\u0000cd\u0005,\u0000\u0000d\f\u0001\u0000"+
		"\u0000\u0000ef\u0005\n\u0000\u0000f\u000e\u0001\u0000\u0000\u0000gh\u0005"+
		"$\u0000\u0000h\u0010\u0001\u0000\u0000\u0000ij\u0005:\u0000\u0000j\u0012"+
		"\u0001\u0000\u0000\u0000kl\u0005.\u0000\u0000l\u0014\u0001\u0000\u0000"+
		"\u0000mv\u00050\u0000\u0000nr\u0007\u0001\u0000\u0000oq\u0007\u0002\u0000"+
		"\u0000po\u0001\u0000\u0000\u0000qt\u0001\u0000\u0000\u0000rp\u0001\u0000"+
		"\u0000\u0000rs\u0001\u0000\u0000\u0000sv\u0001\u0000\u0000\u0000tr\u0001"+
		"\u0000\u0000\u0000um\u0001\u0000\u0000\u0000un\u0001\u0000\u0000\u0000"+
		"v\u0016\u0001\u0000\u0000\u0000wy\u0003\u0013\t\u0000xz\u0007\u0003\u0000"+
		"\u0000yx\u0001\u0000\u0000\u0000z{\u0001\u0000\u0000\u0000{y\u0001\u0000"+
		"\u0000\u0000{|\u0001\u0000\u0000\u0000|\u0018\u0001\u0000\u0000\u0000"+
		"}\u007f\u0007\u0004\u0000\u0000~\u0080\u0007\u0005\u0000\u0000\u007f~"+
		"\u0001\u0000\u0000\u0000\u007f\u0080\u0001\u0000\u0000\u0000\u0080\u0081"+
		"\u0001\u0000\u0000\u0000\u0081\u0082\u0003\u0015\n\u0000\u0082\u001a\u0001"+
		"\u0000\u0000\u0000\u0083\u0085\u0007\u0006\u0000\u0000\u0084\u0083\u0001"+
		"\u0000\u0000\u0000\u0085\u0086\u0001\u0000\u0000\u0000\u0086\u0084\u0001"+
		"\u0000\u0000\u0000\u0086\u0087\u0001\u0000\u0000\u0000\u0087\u001c\u0001"+
		"\u0000\u0000\u0000\u0088\u0089\u0005$\u0000\u0000\u0089\u008a\u0003\u001b"+
		"\r\u0000\u008a\u001e\u0001\u0000\u0000\u0000\u008b\u008f\u0003\t\u0004"+
		"\u0000\u008c\u008e\b\u0007\u0000\u0000\u008d\u008c\u0001\u0000\u0000\u0000"+
		"\u008e\u0091\u0001\u0000\u0000\u0000\u008f\u008d\u0001\u0000\u0000\u0000"+
		"\u008f\u0090\u0001\u0000\u0000\u0000\u0090\u0093\u0001\u0000\u0000\u0000"+
		"\u0091\u008f\u0001\u0000\u0000\u0000\u0092\u0094\u0005\r\u0000\u0000\u0093"+
		"\u0092\u0001\u0000\u0000\u0000\u0093\u0094\u0001\u0000\u0000\u0000\u0094"+
		"\u0097\u0001\u0000\u0000\u0000\u0095\u0098\u0003\r\u0006\u0000\u0096\u0098"+
		"\u0005\u0000\u0000\u0001\u0097\u0095\u0001\u0000\u0000\u0000\u0097\u0096"+
		"\u0001\u0000\u0000\u0000\u0098\u0099\u0001\u0000\u0000\u0000\u0099\u009a"+
		"\u0006\u000f\u0000\u0000\u009a \u0001\u0000\u0000\u0000\u009b\u009d\u0007"+
		"\b\u0000\u0000\u009c\u009b\u0001\u0000\u0000\u0000\u009d\u009e\u0001\u0000"+
		"\u0000\u0000\u009e\u009c\u0001\u0000\u0000\u0000\u009e\u009f\u0001\u0000"+
		"\u0000\u0000\u009f\u00a0\u0001\u0000\u0000\u0000\u00a0\u00a1\u0006\u0010"+
		"\u0000\u0000\u00a1\"\u0001\u0000\u0000\u0000\u00a2\u00a3\t\u0000\u0000"+
		"\u0000\u00a3$\u0001\u0000\u0000\u0000\r\u0000MSUru{\u007f\u0086\u008f"+
		"\u0093\u0097\u009e\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}