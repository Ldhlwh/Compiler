// Generated from C:/Users/qydyx/Desktop/Compiler/MxGrammar\Mx.g4 by ANTLR 4.7
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MxLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		BOOL=10, INT=11, CHAR=12, STRING=13, NULL=14, VOID=15, IF=16, ELIF=17, 
		ELSE=18, FOR=19, WHILE=20, BREAK=21, CONTINUE=22, RETURN=23, NEW=24, CLASS=25, 
		THIS=26, ADD=27, SUB=28, MUL=29, DIV=30, MOD=31, INC=32, DEC=33, ASSIGN=34, 
		LT=35, GT=36, EQ=37, NEQ=38, LEQ=39, GEQ=40, LOGICAND=41, LOGICOR=42, 
		LOGICNOT=43, LSHIFT=44, RSHIFT=45, BITNOT=46, BITOR=47, BITXOR=48, BITAND=49, 
		Brackets=50, IntegerConst=51, CharConst=52, StringConst=53, BoolConst=54, 
		Identifier=55, WS=56, COMMENT=57, LINE_COMMENT=58, NEWLINE=59;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"BOOL", "INT", "CHAR", "STRING", "NULL", "VOID", "TRUE", "FALSE", "IF", 
		"ELIF", "ELSE", "FOR", "WHILE", "BREAK", "CONTINUE", "RETURN", "NEW", 
		"CLASS", "THIS", "ADD", "SUB", "MUL", "DIV", "MOD", "INC", "DEC", "ASSIGN", 
		"LT", "GT", "EQ", "NEQ", "LEQ", "GEQ", "LOGICAND", "LOGICOR", "LOGICNOT", 
		"LSHIFT", "RSHIFT", "BITNOT", "BITOR", "BITXOR", "BITAND", "Digit", "UpperLetter", 
		"LowerLetter", "Letter", "Underscore", "CChar", "SChar", "EscapeSeq", 
		"Brackets", "IntegerConst", "CharConst", "StringConst", "BoolConst", "Identifier", 
		"WS", "COMMENT", "LINE_COMMENT", "NEWLINE"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'{'", "'}'", "'('", "')'", "','", "';'", "'['", "']'", "'.'", "'bool'", 
		"'int'", "'char'", "'string'", "'null'", "'void'", "'if'", "'else if'", 
		"'else'", "'for'", "'while'", "'break'", "'continue'", "'return'", "'new'", 
		"'class'", "'this'", "'+'", "'-'", "'*'", "'/'", "'%'", "'++'", "'--'", 
		"'='", "'<'", "'>'", "'=='", "'!='", "'<='", "'>='", "'&&'", "'||'", "'!'", 
		"'<<'", "'>>'", "'~'", "'|'", "'^'", "'&'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, "BOOL", "INT", 
		"CHAR", "STRING", "NULL", "VOID", "IF", "ELIF", "ELSE", "FOR", "WHILE", 
		"BREAK", "CONTINUE", "RETURN", "NEW", "CLASS", "THIS", "ADD", "SUB", "MUL", 
		"DIV", "MOD", "INC", "DEC", "ASSIGN", "LT", "GT", "EQ", "NEQ", "LEQ", 
		"GEQ", "LOGICAND", "LOGICOR", "LOGICNOT", "LSHIFT", "RSHIFT", "BITNOT", 
		"BITOR", "BITXOR", "BITAND", "Brackets", "IntegerConst", "CharConst", 
		"StringConst", "BoolConst", "Identifier", "WS", "COMMENT", "LINE_COMMENT", 
		"NEWLINE"
	};
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


	public MxLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Mx.g4"; }

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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2=\u01a5\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\3\2\3\2\3\3\3\3"+
		"\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13"+
		"\3\13\3\13\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\21\3\21"+
		"\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26"+
		"\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\31"+
		"\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32"+
		"\3\32\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35"+
		"\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3#\3$\3$\3$\3"+
		"%\3%\3&\3&\3\'\3\'\3(\3(\3(\3)\3)\3)\3*\3*\3*\3+\3+\3+\3,\3,\3,\3-\3-"+
		"\3-\3.\3.\3/\3/\3/\3\60\3\60\3\60\3\61\3\61\3\62\3\62\3\63\3\63\3\64\3"+
		"\64\3\65\3\65\3\66\3\66\3\67\3\67\38\38\58\u0149\n8\39\39\3:\3:\5:\u014f"+
		"\n:\3;\3;\5;\u0153\n;\3<\3<\3<\3=\3=\3=\3>\3>\7>\u015d\n>\f>\16>\u0160"+
		"\13>\3>\5>\u0163\n>\3?\3?\3?\3?\3@\3@\7@\u016b\n@\f@\16@\u016e\13@\3@"+
		"\3@\3A\3A\5A\u0174\nA\3B\3B\3B\3B\7B\u017a\nB\fB\16B\u017d\13B\3C\6C\u0180"+
		"\nC\rC\16C\u0181\3C\3C\3D\3D\3D\3D\7D\u018a\nD\fD\16D\u018d\13D\3D\3D"+
		"\3D\3D\3D\3E\3E\3E\3E\7E\u0198\nE\fE\16E\u019b\13E\3E\3E\3F\5F\u01a0\n"+
		"F\3F\3F\3F\3F\3\u018b\2G\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f"+
		"\27\r\31\16\33\17\35\20\37\21!\2#\2%\22\'\23)\24+\25-\26/\27\61\30\63"+
		"\31\65\32\67\339\34;\35=\36?\37A C!E\"G#I$K%M&O\'Q(S)U*W+Y,[-]._/a\60"+
		"c\61e\62g\63i\2k\2m\2o\2q\2s\2u\2w\2y\64{\65}\66\177\67\u00818\u00839"+
		"\u0085:\u0087;\u0089<\u008b=\3\2\13\3\2\62;\3\2C\\\3\2c|\6\2\f\f\17\17"+
		"))^^\6\2\f\f\17\17$$^^\f\2$$))AA^^cdhhppttvvxx\3\2\63;\5\2\13\f\17\17"+
		"\"\"\4\2\f\f\17\17\2\u01a8\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2"+
		"\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2"+
		"\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3"+
		"\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2"+
		"\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2"+
		";\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3"+
		"\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2"+
		"\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2"+
		"a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3"+
		"\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0085\3\2\2\2\2"+
		"\u0087\3\2\2\2\2\u0089\3\2\2\2\2\u008b\3\2\2\2\3\u008d\3\2\2\2\5\u008f"+
		"\3\2\2\2\7\u0091\3\2\2\2\t\u0093\3\2\2\2\13\u0095\3\2\2\2\r\u0097\3\2"+
		"\2\2\17\u0099\3\2\2\2\21\u009b\3\2\2\2\23\u009d\3\2\2\2\25\u009f\3\2\2"+
		"\2\27\u00a4\3\2\2\2\31\u00a8\3\2\2\2\33\u00ad\3\2\2\2\35\u00b4\3\2\2\2"+
		"\37\u00b9\3\2\2\2!\u00be\3\2\2\2#\u00c3\3\2\2\2%\u00c9\3\2\2\2\'\u00cc"+
		"\3\2\2\2)\u00d4\3\2\2\2+\u00d9\3\2\2\2-\u00dd\3\2\2\2/\u00e3\3\2\2\2\61"+
		"\u00e9\3\2\2\2\63\u00f2\3\2\2\2\65\u00f9\3\2\2\2\67\u00fd\3\2\2\29\u0103"+
		"\3\2\2\2;\u0108\3\2\2\2=\u010a\3\2\2\2?\u010c\3\2\2\2A\u010e\3\2\2\2C"+
		"\u0110\3\2\2\2E\u0112\3\2\2\2G\u0115\3\2\2\2I\u0118\3\2\2\2K\u011a\3\2"+
		"\2\2M\u011c\3\2\2\2O\u011e\3\2\2\2Q\u0121\3\2\2\2S\u0124\3\2\2\2U\u0127"+
		"\3\2\2\2W\u012a\3\2\2\2Y\u012d\3\2\2\2[\u0130\3\2\2\2]\u0132\3\2\2\2_"+
		"\u0135\3\2\2\2a\u0138\3\2\2\2c\u013a\3\2\2\2e\u013c\3\2\2\2g\u013e\3\2"+
		"\2\2i\u0140\3\2\2\2k\u0142\3\2\2\2m\u0144\3\2\2\2o\u0148\3\2\2\2q\u014a"+
		"\3\2\2\2s\u014e\3\2\2\2u\u0152\3\2\2\2w\u0154\3\2\2\2y\u0157\3\2\2\2{"+
		"\u0162\3\2\2\2}\u0164\3\2\2\2\177\u0168\3\2\2\2\u0081\u0173\3\2\2\2\u0083"+
		"\u0175\3\2\2\2\u0085\u017f\3\2\2\2\u0087\u0185\3\2\2\2\u0089\u0193\3\2"+
		"\2\2\u008b\u019f\3\2\2\2\u008d\u008e\7}\2\2\u008e\4\3\2\2\2\u008f\u0090"+
		"\7\177\2\2\u0090\6\3\2\2\2\u0091\u0092\7*\2\2\u0092\b\3\2\2\2\u0093\u0094"+
		"\7+\2\2\u0094\n\3\2\2\2\u0095\u0096\7.\2\2\u0096\f\3\2\2\2\u0097\u0098"+
		"\7=\2\2\u0098\16\3\2\2\2\u0099\u009a\7]\2\2\u009a\20\3\2\2\2\u009b\u009c"+
		"\7_\2\2\u009c\22\3\2\2\2\u009d\u009e\7\60\2\2\u009e\24\3\2\2\2\u009f\u00a0"+
		"\7d\2\2\u00a0\u00a1\7q\2\2\u00a1\u00a2\7q\2\2\u00a2\u00a3\7n\2\2\u00a3"+
		"\26\3\2\2\2\u00a4\u00a5\7k\2\2\u00a5\u00a6\7p\2\2\u00a6\u00a7\7v\2\2\u00a7"+
		"\30\3\2\2\2\u00a8\u00a9\7e\2\2\u00a9\u00aa\7j\2\2\u00aa\u00ab\7c\2\2\u00ab"+
		"\u00ac\7t\2\2\u00ac\32\3\2\2\2\u00ad\u00ae\7u\2\2\u00ae\u00af\7v\2\2\u00af"+
		"\u00b0\7t\2\2\u00b0\u00b1\7k\2\2\u00b1\u00b2\7p\2\2\u00b2\u00b3\7i\2\2"+
		"\u00b3\34\3\2\2\2\u00b4\u00b5\7p\2\2\u00b5\u00b6\7w\2\2\u00b6\u00b7\7"+
		"n\2\2\u00b7\u00b8\7n\2\2\u00b8\36\3\2\2\2\u00b9\u00ba\7x\2\2\u00ba\u00bb"+
		"\7q\2\2\u00bb\u00bc\7k\2\2\u00bc\u00bd\7f\2\2\u00bd \3\2\2\2\u00be\u00bf"+
		"\7v\2\2\u00bf\u00c0\7t\2\2\u00c0\u00c1\7w\2\2\u00c1\u00c2\7g\2\2\u00c2"+
		"\"\3\2\2\2\u00c3\u00c4\7h\2\2\u00c4\u00c5\7c\2\2\u00c5\u00c6\7n\2\2\u00c6"+
		"\u00c7\7u\2\2\u00c7\u00c8\7g\2\2\u00c8$\3\2\2\2\u00c9\u00ca\7k\2\2\u00ca"+
		"\u00cb\7h\2\2\u00cb&\3\2\2\2\u00cc\u00cd\7g\2\2\u00cd\u00ce\7n\2\2\u00ce"+
		"\u00cf\7u\2\2\u00cf\u00d0\7g\2\2\u00d0\u00d1\7\"\2\2\u00d1\u00d2\7k\2"+
		"\2\u00d2\u00d3\7h\2\2\u00d3(\3\2\2\2\u00d4\u00d5\7g\2\2\u00d5\u00d6\7"+
		"n\2\2\u00d6\u00d7\7u\2\2\u00d7\u00d8\7g\2\2\u00d8*\3\2\2\2\u00d9\u00da"+
		"\7h\2\2\u00da\u00db\7q\2\2\u00db\u00dc\7t\2\2\u00dc,\3\2\2\2\u00dd\u00de"+
		"\7y\2\2\u00de\u00df\7j\2\2\u00df\u00e0\7k\2\2\u00e0\u00e1\7n\2\2\u00e1"+
		"\u00e2\7g\2\2\u00e2.\3\2\2\2\u00e3\u00e4\7d\2\2\u00e4\u00e5\7t\2\2\u00e5"+
		"\u00e6\7g\2\2\u00e6\u00e7\7c\2\2\u00e7\u00e8\7m\2\2\u00e8\60\3\2\2\2\u00e9"+
		"\u00ea\7e\2\2\u00ea\u00eb\7q\2\2\u00eb\u00ec\7p\2\2\u00ec\u00ed\7v\2\2"+
		"\u00ed\u00ee\7k\2\2\u00ee\u00ef\7p\2\2\u00ef\u00f0\7w\2\2\u00f0\u00f1"+
		"\7g\2\2\u00f1\62\3\2\2\2\u00f2\u00f3\7t\2\2\u00f3\u00f4\7g\2\2\u00f4\u00f5"+
		"\7v\2\2\u00f5\u00f6\7w\2\2\u00f6\u00f7\7t\2\2\u00f7\u00f8\7p\2\2\u00f8"+
		"\64\3\2\2\2\u00f9\u00fa\7p\2\2\u00fa\u00fb\7g\2\2\u00fb\u00fc\7y\2\2\u00fc"+
		"\66\3\2\2\2\u00fd\u00fe\7e\2\2\u00fe\u00ff\7n\2\2\u00ff\u0100\7c\2\2\u0100"+
		"\u0101\7u\2\2\u0101\u0102\7u\2\2\u01028\3\2\2\2\u0103\u0104\7v\2\2\u0104"+
		"\u0105\7j\2\2\u0105\u0106\7k\2\2\u0106\u0107\7u\2\2\u0107:\3\2\2\2\u0108"+
		"\u0109\7-\2\2\u0109<\3\2\2\2\u010a\u010b\7/\2\2\u010b>\3\2\2\2\u010c\u010d"+
		"\7,\2\2\u010d@\3\2\2\2\u010e\u010f\7\61\2\2\u010fB\3\2\2\2\u0110\u0111"+
		"\7\'\2\2\u0111D\3\2\2\2\u0112\u0113\7-\2\2\u0113\u0114\7-\2\2\u0114F\3"+
		"\2\2\2\u0115\u0116\7/\2\2\u0116\u0117\7/\2\2\u0117H\3\2\2\2\u0118\u0119"+
		"\7?\2\2\u0119J\3\2\2\2\u011a\u011b\7>\2\2\u011bL\3\2\2\2\u011c\u011d\7"+
		"@\2\2\u011dN\3\2\2\2\u011e\u011f\7?\2\2\u011f\u0120\7?\2\2\u0120P\3\2"+
		"\2\2\u0121\u0122\7#\2\2\u0122\u0123\7?\2\2\u0123R\3\2\2\2\u0124\u0125"+
		"\7>\2\2\u0125\u0126\7?\2\2\u0126T\3\2\2\2\u0127\u0128\7@\2\2\u0128\u0129"+
		"\7?\2\2\u0129V\3\2\2\2\u012a\u012b\7(\2\2\u012b\u012c\7(\2\2\u012cX\3"+
		"\2\2\2\u012d\u012e\7~\2\2\u012e\u012f\7~\2\2\u012fZ\3\2\2\2\u0130\u0131"+
		"\7#\2\2\u0131\\\3\2\2\2\u0132\u0133\7>\2\2\u0133\u0134\7>\2\2\u0134^\3"+
		"\2\2\2\u0135\u0136\7@\2\2\u0136\u0137\7@\2\2\u0137`\3\2\2\2\u0138\u0139"+
		"\7\u0080\2\2\u0139b\3\2\2\2\u013a\u013b\7~\2\2\u013bd\3\2\2\2\u013c\u013d"+
		"\7`\2\2\u013df\3\2\2\2\u013e\u013f\7(\2\2\u013fh\3\2\2\2\u0140\u0141\t"+
		"\2\2\2\u0141j\3\2\2\2\u0142\u0143\t\3\2\2\u0143l\3\2\2\2\u0144\u0145\t"+
		"\4\2\2\u0145n\3\2\2\2\u0146\u0149\5k\66\2\u0147\u0149\5m\67\2\u0148\u0146"+
		"\3\2\2\2\u0148\u0147\3\2\2\2\u0149p\3\2\2\2\u014a\u014b\7a\2\2\u014br"+
		"\3\2\2\2\u014c\u014f\n\5\2\2\u014d\u014f\5w<\2\u014e\u014c\3\2\2\2\u014e"+
		"\u014d\3\2\2\2\u014ft\3\2\2\2\u0150\u0153\n\6\2\2\u0151\u0153\5w<\2\u0152"+
		"\u0150\3\2\2\2\u0152\u0151\3\2\2\2\u0153v\3\2\2\2\u0154\u0155\7^\2\2\u0155"+
		"\u0156\t\7\2\2\u0156x\3\2\2\2\u0157\u0158\7]\2\2\u0158\u0159\7_\2\2\u0159"+
		"z\3\2\2\2\u015a\u015e\t\b\2\2\u015b\u015d\t\2\2\2\u015c\u015b\3\2\2\2"+
		"\u015d\u0160\3\2\2\2\u015e\u015c\3\2\2\2\u015e\u015f\3\2\2\2\u015f\u0163"+
		"\3\2\2\2\u0160\u015e\3\2\2\2\u0161\u0163\7\62\2\2\u0162\u015a\3\2\2\2"+
		"\u0162\u0161\3\2\2\2\u0163|\3\2\2\2\u0164\u0165\7)\2\2\u0165\u0166\5s"+
		":\2\u0166\u0167\7)\2\2\u0167~\3\2\2\2\u0168\u016c\7$\2\2\u0169\u016b\5"+
		"u;\2\u016a\u0169\3\2\2\2\u016b\u016e\3\2\2\2\u016c\u016a\3\2\2\2\u016c"+
		"\u016d\3\2\2\2\u016d\u016f\3\2\2\2\u016e\u016c\3\2\2\2\u016f\u0170\7$"+
		"\2\2\u0170\u0080\3\2\2\2\u0171\u0174\5!\21\2\u0172\u0174\5#\22\2\u0173"+
		"\u0171\3\2\2\2\u0173\u0172\3\2\2\2\u0174\u0082\3\2\2\2\u0175\u017b\5o"+
		"8\2\u0176\u017a\5i\65\2\u0177\u017a\5o8\2\u0178\u017a\5q9\2\u0179\u0176"+
		"\3\2\2\2\u0179\u0177\3\2\2\2\u0179\u0178\3\2\2\2\u017a\u017d\3\2\2\2\u017b"+
		"\u0179\3\2\2\2\u017b\u017c\3\2\2\2\u017c\u0084\3\2\2\2\u017d\u017b\3\2"+
		"\2\2\u017e\u0180\t\t\2\2\u017f\u017e\3\2\2\2\u0180\u0181\3\2\2\2\u0181"+
		"\u017f\3\2\2\2\u0181\u0182\3\2\2\2\u0182\u0183\3\2\2\2\u0183\u0184\bC"+
		"\2\2\u0184\u0086\3\2\2\2\u0185\u0186\7\61\2\2\u0186\u0187\7,\2\2\u0187"+
		"\u018b\3\2\2\2\u0188\u018a\13\2\2\2\u0189\u0188\3\2\2\2\u018a\u018d\3"+
		"\2\2\2\u018b\u018c\3\2\2\2\u018b\u0189\3\2\2\2\u018c\u018e\3\2\2\2\u018d"+
		"\u018b\3\2\2\2\u018e\u018f\7,\2\2\u018f\u0190\7\61\2\2\u0190\u0191\3\2"+
		"\2\2\u0191\u0192\bD\2\2\u0192\u0088\3\2\2\2\u0193\u0194\7\61\2\2\u0194"+
		"\u0195\7\61\2\2\u0195\u0199\3\2\2\2\u0196\u0198\n\n\2\2\u0197\u0196\3"+
		"\2\2\2\u0198\u019b\3\2\2\2\u0199\u0197\3\2\2\2\u0199\u019a\3\2\2\2\u019a"+
		"\u019c\3\2\2\2\u019b\u0199\3\2\2\2\u019c\u019d\bE\2\2\u019d\u008a\3\2"+
		"\2\2\u019e\u01a0\7\17\2\2\u019f\u019e\3\2\2\2\u019f\u01a0\3\2\2\2\u01a0"+
		"\u01a1\3\2\2\2\u01a1\u01a2\7\f\2\2\u01a2\u01a3\3\2\2\2\u01a3\u01a4\bF"+
		"\2\2\u01a4\u008c\3\2\2\2\20\2\u0148\u014e\u0152\u015e\u0162\u016c\u0173"+
		"\u0179\u017b\u0181\u018b\u0199\u019f\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}