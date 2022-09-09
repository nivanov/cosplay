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
		NAME=1, SQSTRING=2, DQSTRING=3, NULL=4, SQUOTE=5, DQUOTE=6, COLON=7, DOT=8, 
		INT=9, REAL=10, EXP=11, ID=12, COMMENT=13, WS=14, ErrorChar=15;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"NAME", "SQSTRING", "DQSTRING", "NULL", "SQUOTE", "DQUOTE", "COLON", 
			"DOT", "INT", "REAL", "EXP", "ID", "COMMENT", "WS", "ErrorChar"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, "'null'", "'''", "'\"'", "':'", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "NAME", "SQSTRING", "DQSTRING", "NULL", "SQUOTE", "DQUOTE", "COLON", 
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
		"\u0004\u0000\u000f{\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0003"+
		"\u0000&\b\u0000\u0001\u0001\u0001\u0001\u0005\u0001*\b\u0001\n\u0001\f"+
		"\u0001-\t\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0005\u00025\b\u0002\n\u0002\f\u00028\t\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001"+
		"\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0005\bL\b\b\n"+
		"\b\f\bO\t\b\u0003\bQ\b\b\u0001\t\u0001\t\u0004\tU\b\t\u000b\t\f\tV\u0001"+
		"\n\u0001\n\u0003\n[\b\n\u0001\n\u0001\n\u0001\u000b\u0004\u000b`\b\u000b"+
		"\u000b\u000b\f\u000ba\u0001\f\u0001\f\u0005\ff\b\f\n\f\f\fi\t\f\u0001"+
		"\f\u0003\fl\b\f\u0001\f\u0003\fo\b\f\u0001\f\u0001\f\u0001\r\u0004\rt"+
		"\b\r\u000b\r\f\ru\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0000\u0000"+
		"\u000f\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006"+
		"\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e"+
		"\u001d\u000f\u0001\u0000\u000b\u0001\u0000\'\'\u0001\u0000\"\"\u0001\u0000"+
		"19\u0002\u000009__\u0001\u000009\u0002\u0000EEee\u0002\u0000++--\u0004"+
		"\u000009AZ__az\u0002\u0000\n\n\r\r\u0001\u0001\n\n\u0003\u0000\t\n\f\r"+
		"  \u0086\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000"+
		"\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000"+
		"\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000"+
		"\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000"+
		"\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000"+
		"\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000"+
		"\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000"+
		"\u001d\u0001\u0000\u0000\u0000\u0001%\u0001\u0000\u0000\u0000\u0003\'"+
		"\u0001\u0000\u0000\u0000\u00050\u0001\u0000\u0000\u0000\u0007;\u0001\u0000"+
		"\u0000\u0000\t@\u0001\u0000\u0000\u0000\u000bB\u0001\u0000\u0000\u0000"+
		"\rD\u0001\u0000\u0000\u0000\u000fF\u0001\u0000\u0000\u0000\u0011P\u0001"+
		"\u0000\u0000\u0000\u0013R\u0001\u0000\u0000\u0000\u0015X\u0001\u0000\u0000"+
		"\u0000\u0017_\u0001\u0000\u0000\u0000\u0019c\u0001\u0000\u0000\u0000\u001b"+
		"s\u0001\u0000\u0000\u0000\u001dy\u0001\u0000\u0000\u0000\u001f \u0005"+
		"m\u0000\u0000 !\u0005o\u0000\u0000!&\u0005v\u0000\u0000\"#\u0005a\u0000"+
		"\u0000#$\u0005d\u0000\u0000$&\u0005d\u0000\u0000%\u001f\u0001\u0000\u0000"+
		"\u0000%\"\u0001\u0000\u0000\u0000&\u0002\u0001\u0000\u0000\u0000\'+\u0003"+
		"\t\u0004\u0000(*\b\u0000\u0000\u0000)(\u0001\u0000\u0000\u0000*-\u0001"+
		"\u0000\u0000\u0000+)\u0001\u0000\u0000\u0000+,\u0001\u0000\u0000\u0000"+
		",.\u0001\u0000\u0000\u0000-+\u0001\u0000\u0000\u0000./\u0003\t\u0004\u0000"+
		"/\u0004\u0001\u0000\u0000\u000006\u0003\u000b\u0005\u000015\b\u0001\u0000"+
		"\u000023\u0005\\\u0000\u000035\u0005\"\u0000\u000041\u0001\u0000\u0000"+
		"\u000042\u0001\u0000\u0000\u000058\u0001\u0000\u0000\u000064\u0001\u0000"+
		"\u0000\u000067\u0001\u0000\u0000\u000079\u0001\u0000\u0000\u000086\u0001"+
		"\u0000\u0000\u00009:\u0003\u000b\u0005\u0000:\u0006\u0001\u0000\u0000"+
		"\u0000;<\u0005n\u0000\u0000<=\u0005u\u0000\u0000=>\u0005l\u0000\u0000"+
		">?\u0005l\u0000\u0000?\b\u0001\u0000\u0000\u0000@A\u0005\'\u0000\u0000"+
		"A\n\u0001\u0000\u0000\u0000BC\u0005\"\u0000\u0000C\f\u0001\u0000\u0000"+
		"\u0000DE\u0005:\u0000\u0000E\u000e\u0001\u0000\u0000\u0000FG\u0005.\u0000"+
		"\u0000G\u0010\u0001\u0000\u0000\u0000HQ\u00050\u0000\u0000IM\u0007\u0002"+
		"\u0000\u0000JL\u0007\u0003\u0000\u0000KJ\u0001\u0000\u0000\u0000LO\u0001"+
		"\u0000\u0000\u0000MK\u0001\u0000\u0000\u0000MN\u0001\u0000\u0000\u0000"+
		"NQ\u0001\u0000\u0000\u0000OM\u0001\u0000\u0000\u0000PH\u0001\u0000\u0000"+
		"\u0000PI\u0001\u0000\u0000\u0000Q\u0012\u0001\u0000\u0000\u0000RT\u0003"+
		"\u000f\u0007\u0000SU\u0007\u0004\u0000\u0000TS\u0001\u0000\u0000\u0000"+
		"UV\u0001\u0000\u0000\u0000VT\u0001\u0000\u0000\u0000VW\u0001\u0000\u0000"+
		"\u0000W\u0014\u0001\u0000\u0000\u0000XZ\u0007\u0005\u0000\u0000Y[\u0007"+
		"\u0006\u0000\u0000ZY\u0001\u0000\u0000\u0000Z[\u0001\u0000\u0000\u0000"+
		"[\\\u0001\u0000\u0000\u0000\\]\u0003\u0011\b\u0000]\u0016\u0001\u0000"+
		"\u0000\u0000^`\u0007\u0007\u0000\u0000_^\u0001\u0000\u0000\u0000`a\u0001"+
		"\u0000\u0000\u0000a_\u0001\u0000\u0000\u0000ab\u0001\u0000\u0000\u0000"+
		"b\u0018\u0001\u0000\u0000\u0000cg\u0005;\u0000\u0000df\b\b\u0000\u0000"+
		"ed\u0001\u0000\u0000\u0000fi\u0001\u0000\u0000\u0000ge\u0001\u0000\u0000"+
		"\u0000gh\u0001\u0000\u0000\u0000hk\u0001\u0000\u0000\u0000ig\u0001\u0000"+
		"\u0000\u0000jl\u0005\r\u0000\u0000kj\u0001\u0000\u0000\u0000kl\u0001\u0000"+
		"\u0000\u0000ln\u0001\u0000\u0000\u0000mo\u0007\t\u0000\u0000nm\u0001\u0000"+
		"\u0000\u0000op\u0001\u0000\u0000\u0000pq\u0006\f\u0000\u0000q\u001a\u0001"+
		"\u0000\u0000\u0000rt\u0007\n\u0000\u0000sr\u0001\u0000\u0000\u0000tu\u0001"+
		"\u0000\u0000\u0000us\u0001\u0000\u0000\u0000uv\u0001\u0000\u0000\u0000"+
		"vw\u0001\u0000\u0000\u0000wx\u0006\r\u0000\u0000x\u001c\u0001\u0000\u0000"+
		"\u0000yz\t\u0000\u0000\u0000z\u001e\u0001\u0000\u0000\u0000\u000e\u0000"+
		"%+46MPVZagknu\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}