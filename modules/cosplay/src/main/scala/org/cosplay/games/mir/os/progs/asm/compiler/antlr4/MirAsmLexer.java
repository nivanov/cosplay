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
		NAME=1, SQSTRING=2, DQSTRING=3, BOOL=4, NULL=5, SQUOTE=6, DQUOTE=7, COLON=8, 
		DOT=9, INT=10, REAL=11, EXP=12, ID=13, COMMENT=14, WS=15, ErrorChar=16;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"NAME", "SQSTRING", "DQSTRING", "BOOL", "NULL", "SQUOTE", "DQUOTE", "COLON", 
			"DOT", "INT", "REAL", "EXP", "ID", "COMMENT", "WS", "ErrorChar"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, "'null'", "'''", "'\"'", "':'", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "NAME", "SQSTRING", "DQSTRING", "BOOL", "NULL", "SQUOTE", "DQUOTE", 
			"COLON", "DOT", "INT", "REAL", "EXP", "ID", "COMMENT", "WS", "ErrorChar"
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
		"\u0004\u0000\u0010\u0088\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0003\u0000(\b\u0000\u0001\u0001\u0001\u0001"+
		"\u0005\u0001,\b\u0001\n\u0001\f\u0001/\t\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u00027\b\u0002"+
		"\n\u0002\f\u0002:\t\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0003\u0003G\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0005"+
		"\tY\b\t\n\t\f\t\\\t\t\u0003\t^\b\t\u0001\n\u0001\n\u0004\nb\b\n\u000b"+
		"\n\f\nc\u0001\u000b\u0001\u000b\u0003\u000bh\b\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\f\u0004\fm\b\f\u000b\f\f\fn\u0001\r\u0001\r\u0005\rs\b\r"+
		"\n\r\f\rv\t\r\u0001\r\u0003\ry\b\r\u0001\r\u0003\r|\b\r\u0001\r\u0001"+
		"\r\u0001\u000e\u0004\u000e\u0081\b\u000e\u000b\u000e\f\u000e\u0082\u0001"+
		"\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0000\u0000\u0010\u0001\u0001"+
		"\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f"+
		"\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f"+
		"\u001f\u0010\u0001\u0000\u000b\u0001\u0000\'\'\u0001\u0000\"\"\u0001\u0000"+
		"19\u0002\u000009__\u0001\u000009\u0002\u0000EEee\u0002\u0000++--\u0004"+
		"\u000009AZ__az\u0002\u0000\n\n\r\r\u0001\u0001\n\n\u0003\u0000\t\n\f\r"+
		"  \u0094\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000"+
		"\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000"+
		"\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000"+
		"\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000"+
		"\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000"+
		"\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000"+
		"\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000"+
		"\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0001"+
		"\'\u0001\u0000\u0000\u0000\u0003)\u0001\u0000\u0000\u0000\u00052\u0001"+
		"\u0000\u0000\u0000\u0007F\u0001\u0000\u0000\u0000\tH\u0001\u0000\u0000"+
		"\u0000\u000bM\u0001\u0000\u0000\u0000\rO\u0001\u0000\u0000\u0000\u000f"+
		"Q\u0001\u0000\u0000\u0000\u0011S\u0001\u0000\u0000\u0000\u0013]\u0001"+
		"\u0000\u0000\u0000\u0015_\u0001\u0000\u0000\u0000\u0017e\u0001\u0000\u0000"+
		"\u0000\u0019l\u0001\u0000\u0000\u0000\u001bp\u0001\u0000\u0000\u0000\u001d"+
		"\u0080\u0001\u0000\u0000\u0000\u001f\u0086\u0001\u0000\u0000\u0000!\""+
		"\u0005m\u0000\u0000\"#\u0005o\u0000\u0000#(\u0005v\u0000\u0000$%\u0005"+
		"a\u0000\u0000%&\u0005d\u0000\u0000&(\u0005d\u0000\u0000\'!\u0001\u0000"+
		"\u0000\u0000\'$\u0001\u0000\u0000\u0000(\u0002\u0001\u0000\u0000\u0000"+
		")-\u0003\u000b\u0005\u0000*,\b\u0000\u0000\u0000+*\u0001\u0000\u0000\u0000"+
		",/\u0001\u0000\u0000\u0000-+\u0001\u0000\u0000\u0000-.\u0001\u0000\u0000"+
		"\u0000.0\u0001\u0000\u0000\u0000/-\u0001\u0000\u0000\u000001\u0003\u000b"+
		"\u0005\u00001\u0004\u0001\u0000\u0000\u000028\u0003\r\u0006\u000037\b"+
		"\u0001\u0000\u000045\u0005\\\u0000\u000057\u0005\"\u0000\u000063\u0001"+
		"\u0000\u0000\u000064\u0001\u0000\u0000\u00007:\u0001\u0000\u0000\u0000"+
		"86\u0001\u0000\u0000\u000089\u0001\u0000\u0000\u00009;\u0001\u0000\u0000"+
		"\u0000:8\u0001\u0000\u0000\u0000;<\u0003\r\u0006\u0000<\u0006\u0001\u0000"+
		"\u0000\u0000=>\u0005t\u0000\u0000>?\u0005r\u0000\u0000?@\u0005u\u0000"+
		"\u0000@G\u0005e\u0000\u0000AB\u0005f\u0000\u0000BC\u0005a\u0000\u0000"+
		"CD\u0005l\u0000\u0000DE\u0005s\u0000\u0000EG\u0005e\u0000\u0000F=\u0001"+
		"\u0000\u0000\u0000FA\u0001\u0000\u0000\u0000G\b\u0001\u0000\u0000\u0000"+
		"HI\u0005n\u0000\u0000IJ\u0005u\u0000\u0000JK\u0005l\u0000\u0000KL\u0005"+
		"l\u0000\u0000L\n\u0001\u0000\u0000\u0000MN\u0005\'\u0000\u0000N\f\u0001"+
		"\u0000\u0000\u0000OP\u0005\"\u0000\u0000P\u000e\u0001\u0000\u0000\u0000"+
		"QR\u0005:\u0000\u0000R\u0010\u0001\u0000\u0000\u0000ST\u0005.\u0000\u0000"+
		"T\u0012\u0001\u0000\u0000\u0000U^\u00050\u0000\u0000VZ\u0007\u0002\u0000"+
		"\u0000WY\u0007\u0003\u0000\u0000XW\u0001\u0000\u0000\u0000Y\\\u0001\u0000"+
		"\u0000\u0000ZX\u0001\u0000\u0000\u0000Z[\u0001\u0000\u0000\u0000[^\u0001"+
		"\u0000\u0000\u0000\\Z\u0001\u0000\u0000\u0000]U\u0001\u0000\u0000\u0000"+
		"]V\u0001\u0000\u0000\u0000^\u0014\u0001\u0000\u0000\u0000_a\u0003\u0011"+
		"\b\u0000`b\u0007\u0004\u0000\u0000a`\u0001\u0000\u0000\u0000bc\u0001\u0000"+
		"\u0000\u0000ca\u0001\u0000\u0000\u0000cd\u0001\u0000\u0000\u0000d\u0016"+
		"\u0001\u0000\u0000\u0000eg\u0007\u0005\u0000\u0000fh\u0007\u0006\u0000"+
		"\u0000gf\u0001\u0000\u0000\u0000gh\u0001\u0000\u0000\u0000hi\u0001\u0000"+
		"\u0000\u0000ij\u0003\u0013\t\u0000j\u0018\u0001\u0000\u0000\u0000km\u0007"+
		"\u0007\u0000\u0000lk\u0001\u0000\u0000\u0000mn\u0001\u0000\u0000\u0000"+
		"nl\u0001\u0000\u0000\u0000no\u0001\u0000\u0000\u0000o\u001a\u0001\u0000"+
		"\u0000\u0000pt\u0005;\u0000\u0000qs\b\b\u0000\u0000rq\u0001\u0000\u0000"+
		"\u0000sv\u0001\u0000\u0000\u0000tr\u0001\u0000\u0000\u0000tu\u0001\u0000"+
		"\u0000\u0000ux\u0001\u0000\u0000\u0000vt\u0001\u0000\u0000\u0000wy\u0005"+
		"\r\u0000\u0000xw\u0001\u0000\u0000\u0000xy\u0001\u0000\u0000\u0000y{\u0001"+
		"\u0000\u0000\u0000z|\u0007\t\u0000\u0000{z\u0001\u0000\u0000\u0000|}\u0001"+
		"\u0000\u0000\u0000}~\u0006\r\u0000\u0000~\u001c\u0001\u0000\u0000\u0000"+
		"\u007f\u0081\u0007\n\u0000\u0000\u0080\u007f\u0001\u0000\u0000\u0000\u0081"+
		"\u0082\u0001\u0000\u0000\u0000\u0082\u0080\u0001\u0000\u0000\u0000\u0082"+
		"\u0083\u0001\u0000\u0000\u0000\u0083\u0084\u0001\u0000\u0000\u0000\u0084"+
		"\u0085\u0006\u000e\u0000\u0000\u0085\u001e\u0001\u0000\u0000\u0000\u0086"+
		"\u0087\t\u0000\u0000\u0000\u0087 \u0001\u0000\u0000\u0000\u000f\u0000"+
		"\'-68FZ]cgntx{\u0082\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}