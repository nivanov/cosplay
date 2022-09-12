// Generated from C:/Users/Nikita Ivanov/Documents/GitHub/cosplay/modules/cosplay/src/main/scala/org/cosplay/games/mir/os/progs/asm/compiler/antlr4\MirAsm.g4 by ANTLR 4.10.1
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
		INSRT_NAME=1, DQSTRING=2, NULL=3, DQUOTE=4, SCOLOR=5, COMMA=6, NL=7, COLON=8, 
		DOT=9, INT=10, REAL=11, EXP=12, ID=13, COMMENT=14, WS=15, ErrorChar=16;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"INSRT_NAME", "DQSTRING", "NULL", "DQUOTE", "SCOLOR", "COMMA", "NL", 
			"COLON", "DOT", "INT", "REAL", "EXP", "ID", "COMMENT", "WS", "ErrorChar"
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
		"\u0004\u0000\u0010\u009e\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000M\b\u0000\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0005\u0001S\b\u0001\n\u0001\f\u0001"+
		"V\t\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007"+
		"\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0005\tn\b\t\n\t\f\tq\t\t\u0003"+
		"\ts\b\t\u0001\n\u0001\n\u0004\nw\b\n\u000b\n\f\nx\u0001\u000b\u0001\u000b"+
		"\u0003\u000b}\b\u000b\u0001\u000b\u0001\u000b\u0001\f\u0004\f\u0082\b"+
		"\f\u000b\f\f\f\u0083\u0001\r\u0001\r\u0005\r\u0088\b\r\n\r\f\r\u008b\t"+
		"\r\u0001\r\u0003\r\u008e\b\r\u0001\r\u0001\r\u0003\r\u0092\b\r\u0001\r"+
		"\u0001\r\u0001\u000e\u0004\u000e\u0097\b\u000e\u000b\u000e\f\u000e\u0098"+
		"\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0000\u0000\u0010\u0001"+
		"\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007"+
		"\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d"+
		"\u000f\u001f\u0010\u0001\u0000\t\u0001\u0000\"\"\u0001\u000019\u0002\u0000"+
		"09__\u0001\u000009\u0002\u0000EEee\u0002\u0000++--\u0004\u000009AZ__a"+
		"z\u0002\u0000\n\n\r\r\u0003\u0000\t\t\f\r  \u00b4\u0000\u0001\u0001\u0000"+
		"\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000"+
		"\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000"+
		"\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000"+
		"\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000"+
		"\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000"+
		"\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000"+
		"\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000"+
		"\u0000\u001f\u0001\u0000\u0000\u0000\u0001L\u0001\u0000\u0000\u0000\u0003"+
		"N\u0001\u0000\u0000\u0000\u0005Y\u0001\u0000\u0000\u0000\u0007^\u0001"+
		"\u0000\u0000\u0000\t`\u0001\u0000\u0000\u0000\u000bb\u0001\u0000\u0000"+
		"\u0000\rd\u0001\u0000\u0000\u0000\u000ff\u0001\u0000\u0000\u0000\u0011"+
		"h\u0001\u0000\u0000\u0000\u0013r\u0001\u0000\u0000\u0000\u0015t\u0001"+
		"\u0000\u0000\u0000\u0017z\u0001\u0000\u0000\u0000\u0019\u0081\u0001\u0000"+
		"\u0000\u0000\u001b\u0085\u0001\u0000\u0000\u0000\u001d\u0096\u0001\u0000"+
		"\u0000\u0000\u001f\u009c\u0001\u0000\u0000\u0000!\"\u0005m\u0000\u0000"+
		"\"#\u0005o\u0000\u0000#M\u0005v\u0000\u0000$%\u0005p\u0000\u0000%&\u0005"+
		"u\u0000\u0000&\'\u0005s\u0000\u0000\'M\u0005h\u0000\u0000()\u0005p\u0000"+
		"\u0000)*\u0005o\u0000\u0000*M\u0005p\u0000\u0000+,\u0005i\u0000\u0000"+
		",-\u0005n\u0000\u0000-M\u0005c\u0000\u0000./\u0005d\u0000\u0000/0\u0005"+
		"e\u0000\u00000M\u0005c\u0000\u000012\u0005a\u0000\u000023\u0005d\u0000"+
		"\u00003M\u0005d\u0000\u000045\u0005m\u0000\u000056\u0005u\u0000\u0000"+
		"6M\u0005l\u0000\u000078\u0005d\u0000\u000089\u0005i\u0000\u00009M\u0005"+
		"v\u0000\u0000:;\u0005s\u0000\u0000;<\u0005u\u0000\u0000<M\u0005b\u0000"+
		"\u0000=>\u0005c\u0000\u0000>?\u0005a\u0000\u0000?@\u0005l\u0000\u0000"+
		"@A\u0005l\u0000\u0000AM\u0005n\u0000\u0000BC\u0005c\u0000\u0000CD\u0005"+
		"a\u0000\u0000DE\u0005l\u0000\u0000EM\u0005l\u0000\u0000FG\u0005j\u0000"+
		"\u0000GH\u0005m\u0000\u0000HM\u0005p\u0000\u0000IJ\u0005r\u0000\u0000"+
		"JK\u0005e\u0000\u0000KM\u0005t\u0000\u0000L!\u0001\u0000\u0000\u0000L"+
		"$\u0001\u0000\u0000\u0000L(\u0001\u0000\u0000\u0000L+\u0001\u0000\u0000"+
		"\u0000L.\u0001\u0000\u0000\u0000L1\u0001\u0000\u0000\u0000L4\u0001\u0000"+
		"\u0000\u0000L7\u0001\u0000\u0000\u0000L:\u0001\u0000\u0000\u0000L=\u0001"+
		"\u0000\u0000\u0000LB\u0001\u0000\u0000\u0000LF\u0001\u0000\u0000\u0000"+
		"LI\u0001\u0000\u0000\u0000M\u0002\u0001\u0000\u0000\u0000NT\u0003\u0007"+
		"\u0003\u0000OS\b\u0000\u0000\u0000PQ\u0005\\\u0000\u0000QS\u0005\"\u0000"+
		"\u0000RO\u0001\u0000\u0000\u0000RP\u0001\u0000\u0000\u0000SV\u0001\u0000"+
		"\u0000\u0000TR\u0001\u0000\u0000\u0000TU\u0001\u0000\u0000\u0000UW\u0001"+
		"\u0000\u0000\u0000VT\u0001\u0000\u0000\u0000WX\u0003\u0007\u0003\u0000"+
		"X\u0004\u0001\u0000\u0000\u0000YZ\u0005n\u0000\u0000Z[\u0005u\u0000\u0000"+
		"[\\\u0005l\u0000\u0000\\]\u0005l\u0000\u0000]\u0006\u0001\u0000\u0000"+
		"\u0000^_\u0005\"\u0000\u0000_\b\u0001\u0000\u0000\u0000`a\u0005;\u0000"+
		"\u0000a\n\u0001\u0000\u0000\u0000bc\u0005,\u0000\u0000c\f\u0001\u0000"+
		"\u0000\u0000de\u0005\n\u0000\u0000e\u000e\u0001\u0000\u0000\u0000fg\u0005"+
		":\u0000\u0000g\u0010\u0001\u0000\u0000\u0000hi\u0005.\u0000\u0000i\u0012"+
		"\u0001\u0000\u0000\u0000js\u00050\u0000\u0000ko\u0007\u0001\u0000\u0000"+
		"ln\u0007\u0002\u0000\u0000ml\u0001\u0000\u0000\u0000nq\u0001\u0000\u0000"+
		"\u0000om\u0001\u0000\u0000\u0000op\u0001\u0000\u0000\u0000ps\u0001\u0000"+
		"\u0000\u0000qo\u0001\u0000\u0000\u0000rj\u0001\u0000\u0000\u0000rk\u0001"+
		"\u0000\u0000\u0000s\u0014\u0001\u0000\u0000\u0000tv\u0003\u0011\b\u0000"+
		"uw\u0007\u0003\u0000\u0000vu\u0001\u0000\u0000\u0000wx\u0001\u0000\u0000"+
		"\u0000xv\u0001\u0000\u0000\u0000xy\u0001\u0000\u0000\u0000y\u0016\u0001"+
		"\u0000\u0000\u0000z|\u0007\u0004\u0000\u0000{}\u0007\u0005\u0000\u0000"+
		"|{\u0001\u0000\u0000\u0000|}\u0001\u0000\u0000\u0000}~\u0001\u0000\u0000"+
		"\u0000~\u007f\u0003\u0013\t\u0000\u007f\u0018\u0001\u0000\u0000\u0000"+
		"\u0080\u0082\u0007\u0006\u0000\u0000\u0081\u0080\u0001\u0000\u0000\u0000"+
		"\u0082\u0083\u0001\u0000\u0000\u0000\u0083\u0081\u0001\u0000\u0000\u0000"+
		"\u0083\u0084\u0001\u0000\u0000\u0000\u0084\u001a\u0001\u0000\u0000\u0000"+
		"\u0085\u0089\u0003\t\u0004\u0000\u0086\u0088\b\u0007\u0000\u0000\u0087"+
		"\u0086\u0001\u0000\u0000\u0000\u0088\u008b\u0001\u0000\u0000\u0000\u0089"+
		"\u0087\u0001\u0000\u0000\u0000\u0089\u008a\u0001\u0000\u0000\u0000\u008a"+
		"\u008d\u0001\u0000\u0000\u0000\u008b\u0089\u0001\u0000\u0000\u0000\u008c"+
		"\u008e\u0005\r\u0000\u0000\u008d\u008c\u0001\u0000\u0000\u0000\u008d\u008e"+
		"\u0001\u0000\u0000\u0000\u008e\u0091\u0001\u0000\u0000\u0000\u008f\u0092"+
		"\u0003\r\u0006\u0000\u0090\u0092\u0005\u0000\u0000\u0001\u0091\u008f\u0001"+
		"\u0000\u0000\u0000\u0091\u0090\u0001\u0000\u0000\u0000\u0092\u0093\u0001"+
		"\u0000\u0000\u0000\u0093\u0094\u0006\r\u0000\u0000\u0094\u001c\u0001\u0000"+
		"\u0000\u0000\u0095\u0097\u0007\b\u0000\u0000\u0096\u0095\u0001\u0000\u0000"+
		"\u0000\u0097\u0098\u0001\u0000\u0000\u0000\u0098\u0096\u0001\u0000\u0000"+
		"\u0000\u0098\u0099\u0001\u0000\u0000\u0000\u0099\u009a\u0001\u0000\u0000"+
		"\u0000\u009a\u009b\u0006\u000e\u0000\u0000\u009b\u001e\u0001\u0000\u0000"+
		"\u0000\u009c\u009d\t\u0000\u0000\u0000\u009d \u0001\u0000\u0000\u0000"+
		"\r\u0000LRTorx|\u0083\u0089\u008d\u0091\u0098\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}