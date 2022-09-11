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
		NAME=1, DQSTRING=2, NULL=3, DQUOTE=4, SCOLOR=5, NL=6, COLON=7, DOT=8, 
		INT=9, REAL=10, EXP=11, ID=12, COMMENT=13, WS=14, ErrorChar=15;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"NAME", "DQSTRING", "NULL", "DQUOTE", "SCOLOR", "NL", "COLON", "DOT", 
			"INT", "REAL", "EXP", "ID", "COMMENT", "WS", "ErrorChar"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'null'", "'\"'", "';'", "'\\n'", "':'", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "NAME", "DQSTRING", "NULL", "DQUOTE", "SCOLOR", "NL", "COLON", 
			"DOT", "INT", "REAL", "EXP", "ID", "COMMENT", "WS", "ErrorChar"
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
		"\u0004\u0000\u000f\u009a\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0003\u0000K\b\u0000\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0005\u0001Q\b\u0001\n\u0001\f\u0001T\t\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001"+
		"\b\u0005\bj\b\b\n\b\f\bm\t\b\u0003\bo\b\b\u0001\t\u0001\t\u0004\ts\b\t"+
		"\u000b\t\f\tt\u0001\n\u0001\n\u0003\ny\b\n\u0001\n\u0001\n\u0001\u000b"+
		"\u0004\u000b~\b\u000b\u000b\u000b\f\u000b\u007f\u0001\f\u0001\f\u0005"+
		"\f\u0084\b\f\n\f\f\f\u0087\t\f\u0001\f\u0003\f\u008a\b\f\u0001\f\u0001"+
		"\f\u0003\f\u008e\b\f\u0001\f\u0001\f\u0001\r\u0004\r\u0093\b\r\u000b\r"+
		"\f\r\u0094\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0000\u0000\u000f\u0001"+
		"\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007"+
		"\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d"+
		"\u000f\u0001\u0000\t\u0001\u0000\"\"\u0001\u000019\u0002\u000009__\u0001"+
		"\u000009\u0002\u0000EEee\u0002\u0000++--\u0004\u000009AZ__az\u0002\u0000"+
		"\n\n\r\r\u0003\u0000\t\t\f\r  \u00b0\u0000\u0001\u0001\u0000\u0000\u0000"+
		"\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000"+
		"\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000"+
		"\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f"+
		"\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013"+
		"\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017"+
		"\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b"+
		"\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0001J\u0001"+
		"\u0000\u0000\u0000\u0003L\u0001\u0000\u0000\u0000\u0005W\u0001\u0000\u0000"+
		"\u0000\u0007\\\u0001\u0000\u0000\u0000\t^\u0001\u0000\u0000\u0000\u000b"+
		"`\u0001\u0000\u0000\u0000\rb\u0001\u0000\u0000\u0000\u000fd\u0001\u0000"+
		"\u0000\u0000\u0011n\u0001\u0000\u0000\u0000\u0013p\u0001\u0000\u0000\u0000"+
		"\u0015v\u0001\u0000\u0000\u0000\u0017}\u0001\u0000\u0000\u0000\u0019\u0081"+
		"\u0001\u0000\u0000\u0000\u001b\u0092\u0001\u0000\u0000\u0000\u001d\u0098"+
		"\u0001\u0000\u0000\u0000\u001f \u0005m\u0000\u0000 !\u0005o\u0000\u0000"+
		"!K\u0005v\u0000\u0000\"#\u0005p\u0000\u0000#$\u0005u\u0000\u0000$%\u0005"+
		"s\u0000\u0000%K\u0005h\u0000\u0000&\'\u0005p\u0000\u0000\'(\u0005o\u0000"+
		"\u0000(K\u0005p\u0000\u0000)*\u0005i\u0000\u0000*+\u0005n\u0000\u0000"+
		"+K\u0005c\u0000\u0000,-\u0005d\u0000\u0000-.\u0005e\u0000\u0000.K\u0005"+
		"c\u0000\u0000/0\u0005a\u0000\u000001\u0005d\u0000\u00001K\u0005d\u0000"+
		"\u000023\u0005m\u0000\u000034\u0005u\u0000\u00004K\u0005l\u0000\u0000"+
		"56\u0005d\u0000\u000067\u0005i\u0000\u00007K\u0005v\u0000\u000089\u0005"+
		"s\u0000\u00009:\u0005u\u0000\u0000:K\u0005b\u0000\u0000;<\u0005c\u0000"+
		"\u0000<=\u0005a\u0000\u0000=>\u0005l\u0000\u0000>?\u0005l\u0000\u0000"+
		"?K\u0005n\u0000\u0000@A\u0005c\u0000\u0000AB\u0005a\u0000\u0000BC\u0005"+
		"l\u0000\u0000CK\u0005l\u0000\u0000DE\u0005j\u0000\u0000EF\u0005m\u0000"+
		"\u0000FK\u0005p\u0000\u0000GH\u0005r\u0000\u0000HI\u0005e\u0000\u0000"+
		"IK\u0005t\u0000\u0000J\u001f\u0001\u0000\u0000\u0000J\"\u0001\u0000\u0000"+
		"\u0000J&\u0001\u0000\u0000\u0000J)\u0001\u0000\u0000\u0000J,\u0001\u0000"+
		"\u0000\u0000J/\u0001\u0000\u0000\u0000J2\u0001\u0000\u0000\u0000J5\u0001"+
		"\u0000\u0000\u0000J8\u0001\u0000\u0000\u0000J;\u0001\u0000\u0000\u0000"+
		"J@\u0001\u0000\u0000\u0000JD\u0001\u0000\u0000\u0000JG\u0001\u0000\u0000"+
		"\u0000K\u0002\u0001\u0000\u0000\u0000LR\u0003\u0007\u0003\u0000MQ\b\u0000"+
		"\u0000\u0000NO\u0005\\\u0000\u0000OQ\u0005\"\u0000\u0000PM\u0001\u0000"+
		"\u0000\u0000PN\u0001\u0000\u0000\u0000QT\u0001\u0000\u0000\u0000RP\u0001"+
		"\u0000\u0000\u0000RS\u0001\u0000\u0000\u0000SU\u0001\u0000\u0000\u0000"+
		"TR\u0001\u0000\u0000\u0000UV\u0003\u0007\u0003\u0000V\u0004\u0001\u0000"+
		"\u0000\u0000WX\u0005n\u0000\u0000XY\u0005u\u0000\u0000YZ\u0005l\u0000"+
		"\u0000Z[\u0005l\u0000\u0000[\u0006\u0001\u0000\u0000\u0000\\]\u0005\""+
		"\u0000\u0000]\b\u0001\u0000\u0000\u0000^_\u0005;\u0000\u0000_\n\u0001"+
		"\u0000\u0000\u0000`a\u0005\n\u0000\u0000a\f\u0001\u0000\u0000\u0000bc"+
		"\u0005:\u0000\u0000c\u000e\u0001\u0000\u0000\u0000de\u0005.\u0000\u0000"+
		"e\u0010\u0001\u0000\u0000\u0000fo\u00050\u0000\u0000gk\u0007\u0001\u0000"+
		"\u0000hj\u0007\u0002\u0000\u0000ih\u0001\u0000\u0000\u0000jm\u0001\u0000"+
		"\u0000\u0000ki\u0001\u0000\u0000\u0000kl\u0001\u0000\u0000\u0000lo\u0001"+
		"\u0000\u0000\u0000mk\u0001\u0000\u0000\u0000nf\u0001\u0000\u0000\u0000"+
		"ng\u0001\u0000\u0000\u0000o\u0012\u0001\u0000\u0000\u0000pr\u0003\u000f"+
		"\u0007\u0000qs\u0007\u0003\u0000\u0000rq\u0001\u0000\u0000\u0000st\u0001"+
		"\u0000\u0000\u0000tr\u0001\u0000\u0000\u0000tu\u0001\u0000\u0000\u0000"+
		"u\u0014\u0001\u0000\u0000\u0000vx\u0007\u0004\u0000\u0000wy\u0007\u0005"+
		"\u0000\u0000xw\u0001\u0000\u0000\u0000xy\u0001\u0000\u0000\u0000yz\u0001"+
		"\u0000\u0000\u0000z{\u0003\u0011\b\u0000{\u0016\u0001\u0000\u0000\u0000"+
		"|~\u0007\u0006\u0000\u0000}|\u0001\u0000\u0000\u0000~\u007f\u0001\u0000"+
		"\u0000\u0000\u007f}\u0001\u0000\u0000\u0000\u007f\u0080\u0001\u0000\u0000"+
		"\u0000\u0080\u0018\u0001\u0000\u0000\u0000\u0081\u0085\u0003\t\u0004\u0000"+
		"\u0082\u0084\b\u0007\u0000\u0000\u0083\u0082\u0001\u0000\u0000\u0000\u0084"+
		"\u0087\u0001\u0000\u0000\u0000\u0085\u0083\u0001\u0000\u0000\u0000\u0085"+
		"\u0086\u0001\u0000\u0000\u0000\u0086\u0089\u0001\u0000\u0000\u0000\u0087"+
		"\u0085\u0001\u0000\u0000\u0000\u0088\u008a\u0005\r\u0000\u0000\u0089\u0088"+
		"\u0001\u0000\u0000\u0000\u0089\u008a\u0001\u0000\u0000\u0000\u008a\u008d"+
		"\u0001\u0000\u0000\u0000\u008b\u008e\u0003\u000b\u0005\u0000\u008c\u008e"+
		"\u0005\u0000\u0000\u0001\u008d\u008b\u0001\u0000\u0000\u0000\u008d\u008c"+
		"\u0001\u0000\u0000\u0000\u008e\u008f\u0001\u0000\u0000\u0000\u008f\u0090"+
		"\u0006\f\u0000\u0000\u0090\u001a\u0001\u0000\u0000\u0000\u0091\u0093\u0007"+
		"\b\u0000\u0000\u0092\u0091\u0001\u0000\u0000\u0000\u0093\u0094\u0001\u0000"+
		"\u0000\u0000\u0094\u0092\u0001\u0000\u0000\u0000\u0094\u0095\u0001\u0000"+
		"\u0000\u0000\u0095\u0096\u0001\u0000\u0000\u0000\u0096\u0097\u0006\r\u0000"+
		"\u0000\u0097\u001c\u0001\u0000\u0000\u0000\u0098\u0099\t\u0000\u0000\u0000"+
		"\u0099\u001e\u0001\u0000\u0000\u0000\r\u0000JPRkntx\u007f\u0085\u0089"+
		"\u008d\u0094\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}