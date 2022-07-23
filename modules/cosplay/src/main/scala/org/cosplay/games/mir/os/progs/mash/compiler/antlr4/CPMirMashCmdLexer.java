// Generated from /Users/nivanov/cosplay/modules/cosplay/src/main/scala/org/cosplay/games/mir/os/progs/mash/compiler/antlr4/CPMirMashCmd.g4 by ANTLR 4.10.1
package org.cosplay.games.mir.os.progs.mash.compiler.antlr4;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CPMirMashCmdLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		SQUOTE=1, DQUOTE=2, SQSTRING=3, DQSTRING=4, AMP=5, PIPE_IN=6, TO_FILE=7, 
		APPEND=8, STR=9, WS=10, ErrorChar=11;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"SQUOTE", "DQUOTE", "SQSTRING", "DQSTRING", "AMP", "PIPE_IN", "TO_FILE", 
			"APPEND", "STR", "WS", "ErrorChar"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'''", "'\"'", null, null, "'&'", "'|'", "'>'", "'>>'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "SQUOTE", "DQUOTE", "SQSTRING", "DQSTRING", "AMP", "PIPE_IN", "TO_FILE", 
			"APPEND", "STR", "WS", "ErrorChar"
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


	public CPMirMashCmdLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CPMirMashCmd.g4"; }

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
		"\u0004\u0000\u000bF\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0005\u0002"+
		"\u001e\b\u0002\n\u0002\f\u0002!\t\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003)\b\u0003\n\u0003"+
		"\f\u0003,\t\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001"+
		"\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\b\u0004\b:\b\b\u000b\b\f\b;\u0001\t\u0004\t?\b\t\u000b\t"+
		"\f\t@\u0001\t\u0001\t\u0001\n\u0001\n\u0000\u0000\u000b\u0001\u0001\u0003"+
		"\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011"+
		"\t\u0013\n\u0015\u000b\u0001\u0000\u0004\u0001\u0000\'\'\u0001\u0000\""+
		"\"\u0003\u0000\t\n\r\r  \u0003\u0000\t\n\f\r  J\u0000\u0001\u0001\u0000"+
		"\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000"+
		"\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000"+
		"\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000"+
		"\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000"+
		"\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000"+
		"\u0001\u0017\u0001\u0000\u0000\u0000\u0003\u0019\u0001\u0000\u0000\u0000"+
		"\u0005\u001b\u0001\u0000\u0000\u0000\u0007$\u0001\u0000\u0000\u0000\t"+
		"/\u0001\u0000\u0000\u0000\u000b1\u0001\u0000\u0000\u0000\r3\u0001\u0000"+
		"\u0000\u0000\u000f5\u0001\u0000\u0000\u0000\u00119\u0001\u0000\u0000\u0000"+
		"\u0013>\u0001\u0000\u0000\u0000\u0015D\u0001\u0000\u0000\u0000\u0017\u0018"+
		"\u0005\'\u0000\u0000\u0018\u0002\u0001\u0000\u0000\u0000\u0019\u001a\u0005"+
		"\"\u0000\u0000\u001a\u0004\u0001\u0000\u0000\u0000\u001b\u001f\u0003\u0001"+
		"\u0000\u0000\u001c\u001e\b\u0000\u0000\u0000\u001d\u001c\u0001\u0000\u0000"+
		"\u0000\u001e!\u0001\u0000\u0000\u0000\u001f\u001d\u0001\u0000\u0000\u0000"+
		"\u001f \u0001\u0000\u0000\u0000 \"\u0001\u0000\u0000\u0000!\u001f\u0001"+
		"\u0000\u0000\u0000\"#\u0003\u0001\u0000\u0000#\u0006\u0001\u0000\u0000"+
		"\u0000$*\u0003\u0003\u0001\u0000%)\b\u0001\u0000\u0000&\'\u0005\\\u0000"+
		"\u0000\')\u0005\"\u0000\u0000(%\u0001\u0000\u0000\u0000(&\u0001\u0000"+
		"\u0000\u0000),\u0001\u0000\u0000\u0000*(\u0001\u0000\u0000\u0000*+\u0001"+
		"\u0000\u0000\u0000+-\u0001\u0000\u0000\u0000,*\u0001\u0000\u0000\u0000"+
		"-.\u0003\u0003\u0001\u0000.\b\u0001\u0000\u0000\u0000/0\u0005&\u0000\u0000"+
		"0\n\u0001\u0000\u0000\u000012\u0005|\u0000\u00002\f\u0001\u0000\u0000"+
		"\u000034\u0005>\u0000\u00004\u000e\u0001\u0000\u0000\u000056\u0005>\u0000"+
		"\u000067\u0005>\u0000\u00007\u0010\u0001\u0000\u0000\u00008:\b\u0002\u0000"+
		"\u000098\u0001\u0000\u0000\u0000:;\u0001\u0000\u0000\u0000;9\u0001\u0000"+
		"\u0000\u0000;<\u0001\u0000\u0000\u0000<\u0012\u0001\u0000\u0000\u0000"+
		"=?\u0007\u0003\u0000\u0000>=\u0001\u0000\u0000\u0000?@\u0001\u0000\u0000"+
		"\u0000@>\u0001\u0000\u0000\u0000@A\u0001\u0000\u0000\u0000AB\u0001\u0000"+
		"\u0000\u0000BC\u0006\t\u0000\u0000C\u0014\u0001\u0000\u0000\u0000DE\t"+
		"\u0000\u0000\u0000E\u0016\u0001\u0000\u0000\u0000\u0006\u0000\u001f(*"+
		";@\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}