package MxGrammar;// Generated from C:/Users/qydyx/Desktop/Compiler/MxGrammar\Mx.g4 by ANTLR 4.7

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MxParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		BOOL=10, INT=11, CHAR=12, STRING=13, NULL=14, VOID=15, IF=16, ELIF=17, 
		ELSE=18, FOR=19, WHILE=20, BREAK=21, CONTINUE=22, RETURN=23, NEW=24, CLASS=25, 
		ADD=26, SUB=27, MUL=28, DIV=29, MOD=30, INC=31, DEC=32, ASSIGN=33, LT=34, 
		GT=35, EQ=36, NEQ=37, LEQ=38, GEQ=39, LOGICAND=40, LOGICOR=41, LOGICNOT=42, 
		LSHIFT=43, RSHIFT=44, BITNOT=45, BITOR=46, BITXOR=47, BITAND=48, Brackets=49, 
		IntegerConst=50, CharConst=51, StringConst=52, BoolConst=53, Identifier=54, 
		WS=55, COMMENT=56, LINE_COMMENT=57, NEWLINE=58;
	public static final int
		RULE_prog = 0, RULE_progSec = 1, RULE_classDecl = 2, RULE_memDecl = 3, 
		RULE_ctrDecl = 4, RULE_funcDecl = 5, RULE_typeSpec = 6, RULE_singleTypeSpec = 7, 
		RULE_paramDeclList = 8, RULE_paramList = 9, RULE_paramDecl = 10, RULE_variDecl = 11, 
		RULE_variInit = 12, RULE_blockStmt = 13, RULE_blockCtnt = 14, RULE_exprStmt = 15, 
		RULE_stmt = 16, RULE_slctStmt = 17, RULE_iterStmt = 18, RULE_jumpStmt = 19, 
		RULE_expr = 20, RULE_creator = 21, RULE_constant = 22;
	public static final String[] ruleNames = {
		"prog", "progSec", "classDecl", "memDecl", "ctrDecl", "funcDecl", "typeSpec", 
		"singleTypeSpec", "paramDeclList", "paramList", "paramDecl", "variDecl", 
		"variInit", "blockStmt", "blockCtnt", "exprStmt", "stmt", "slctStmt", 
		"iterStmt", "jumpStmt", "expr", "creator", "constant"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'{'", "'}'", "'('", "')'", "','", "';'", "'['", "']'", "'.'", "'bool'", 
		"'int'", "'char'", "'string'", "'null'", "'void'", "'if'", "'else if'", 
		"'else'", "'for'", "'while'", "'break'", "'continue'", "'return'", "'new'", 
		"'class'", "'+'", "'-'", "'*'", "'/'", "'%'", "'++'", "'--'", "'='", "'<'", 
		"'>'", "'=='", "'!='", "'<='", "'>='", "'&&'", "'||'", "'!'", "'<<'", 
		"'>>'", "'~'", "'|'", "'^'", "'&'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, "BOOL", "INT", 
		"CHAR", "STRING", "NULL", "VOID", "IF", "ELIF", "ELSE", "FOR", "WHILE", 
		"BREAK", "CONTINUE", "RETURN", "NEW", "CLASS", "ADD", "SUB", "MUL", "DIV", 
		"MOD", "INC", "DEC", "ASSIGN", "LT", "GT", "EQ", "NEQ", "LEQ", "GEQ", 
		"LOGICAND", "LOGICOR", "LOGICNOT", "LSHIFT", "RSHIFT", "BITNOT", "BITOR", 
		"BITXOR", "BITAND", "Brackets", "IntegerConst", "CharConst", "StringConst", 
		"BoolConst", "Identifier", "WS", "COMMENT", "LINE_COMMENT", "NEWLINE"
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

	@Override
	public String getGrammarFileName() { return "Mx.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MxParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(MxParser.EOF, 0); }
		public List<ProgSecContext> progSec() {
			return getRuleContexts(ProgSecContext.class);
		}
		public ProgSecContext progSec(int i) {
			return getRuleContext(ProgSecContext.class,i);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitProg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL) | (1L << INT) | (1L << CHAR) | (1L << STRING) | (1L << VOID) | (1L << CLASS) | (1L << Identifier))) != 0)) {
				{
				{
				setState(46);
				progSec();
				}
				}
				setState(51);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(52);
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

	public static class ProgSecContext extends ParserRuleContext {
		public ClassDeclContext classDecl() {
			return getRuleContext(ClassDeclContext.class,0);
		}
		public FuncDeclContext funcDecl() {
			return getRuleContext(FuncDeclContext.class,0);
		}
		public VariDeclContext variDecl() {
			return getRuleContext(VariDeclContext.class,0);
		}
		public ProgSecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_progSec; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitProgSec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgSecContext progSec() throws RecognitionException {
		ProgSecContext _localctx = new ProgSecContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_progSec);
		try {
			setState(57);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(54);
				classDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(55);
				funcDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(56);
				variDecl();
				}
				break;
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

	public static class ClassDeclContext extends ParserRuleContext {
		public ClassDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDecl; }
	 
		public ClassDeclContext() { }
		public void copyFrom(ClassDeclContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ClassDeclarationContext extends ClassDeclContext {
		public TerminalNode CLASS() { return getToken(MxParser.CLASS, 0); }
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public List<MemDeclContext> memDecl() {
			return getRuleContexts(MemDeclContext.class);
		}
		public MemDeclContext memDecl(int i) {
			return getRuleContext(MemDeclContext.class,i);
		}
		public ClassDeclarationContext(ClassDeclContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitClassDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassDeclContext classDecl() throws RecognitionException {
		ClassDeclContext _localctx = new ClassDeclContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_classDecl);
		int _la;
		try {
			_localctx = new ClassDeclarationContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			match(CLASS);
			setState(60);
			match(Identifier);
			setState(61);
			match(T__0);
			setState(65);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL) | (1L << INT) | (1L << CHAR) | (1L << STRING) | (1L << VOID) | (1L << Identifier))) != 0)) {
				{
				{
				setState(62);
				memDecl();
				}
				}
				setState(67);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(68);
			match(T__1);
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

	public static class MemDeclContext extends ParserRuleContext {
		public VariDeclContext variDecl() {
			return getRuleContext(VariDeclContext.class,0);
		}
		public FuncDeclContext funcDecl() {
			return getRuleContext(FuncDeclContext.class,0);
		}
		public CtrDeclContext ctrDecl() {
			return getRuleContext(CtrDeclContext.class,0);
		}
		public MemDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitMemDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemDeclContext memDecl() throws RecognitionException {
		MemDeclContext _localctx = new MemDeclContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_memDecl);
		try {
			setState(73);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(70);
				variDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(71);
				funcDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(72);
				ctrDecl();
				}
				break;
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

	public static class CtrDeclContext extends ParserRuleContext {
		public CtrDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ctrDecl; }
	 
		public CtrDeclContext() { }
		public void copyFrom(CtrDeclContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ConstructorDeclarationContext extends CtrDeclContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public BlockStmtContext blockStmt() {
			return getRuleContext(BlockStmtContext.class,0);
		}
		public ConstructorDeclarationContext(CtrDeclContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitConstructorDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CtrDeclContext ctrDecl() throws RecognitionException {
		CtrDeclContext _localctx = new CtrDeclContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_ctrDecl);
		try {
			_localctx = new ConstructorDeclarationContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			match(Identifier);
			setState(76);
			match(T__2);
			setState(77);
			match(T__3);
			setState(78);
			blockStmt();
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

	public static class FuncDeclContext extends ParserRuleContext {
		public FuncDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcDecl; }
	 
		public FuncDeclContext() { }
		public void copyFrom(FuncDeclContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class FunctionDeclarationContext extends FuncDeclContext {
		public TypeSpecContext typeSpec() {
			return getRuleContext(TypeSpecContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public BlockStmtContext blockStmt() {
			return getRuleContext(BlockStmtContext.class,0);
		}
		public ParamDeclListContext paramDeclList() {
			return getRuleContext(ParamDeclListContext.class,0);
		}
		public FunctionDeclarationContext(FuncDeclContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFunctionDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FuncDeclContext funcDecl() throws RecognitionException {
		FuncDeclContext _localctx = new FuncDeclContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_funcDecl);
		int _la;
		try {
			_localctx = new FunctionDeclarationContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			typeSpec();
			setState(81);
			match(Identifier);
			setState(82);
			match(T__2);
			setState(84);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL) | (1L << INT) | (1L << CHAR) | (1L << STRING) | (1L << VOID) | (1L << Identifier))) != 0)) {
				{
				setState(83);
				paramDeclList();
				}
			}

			setState(86);
			match(T__3);
			setState(87);
			blockStmt();
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

	public static class TypeSpecContext extends ParserRuleContext {
		public SingleTypeSpecContext singleTypeSpec() {
			return getRuleContext(SingleTypeSpecContext.class,0);
		}
		public List<TerminalNode> Brackets() { return getTokens(MxParser.Brackets); }
		public TerminalNode Brackets(int i) {
			return getToken(MxParser.Brackets, i);
		}
		public TypeSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeSpec; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitTypeSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeSpecContext typeSpec() throws RecognitionException {
		TypeSpecContext _localctx = new TypeSpecContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_typeSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			singleTypeSpec();
			setState(93);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Brackets) {
				{
				{
				setState(90);
				match(Brackets);
				}
				}
				setState(95);
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

	public static class SingleTypeSpecContext extends ParserRuleContext {
		public Token type;
		public TerminalNode INT() { return getToken(MxParser.INT, 0); }
		public TerminalNode CHAR() { return getToken(MxParser.CHAR, 0); }
		public TerminalNode BOOL() { return getToken(MxParser.BOOL, 0); }
		public TerminalNode STRING() { return getToken(MxParser.STRING, 0); }
		public TerminalNode VOID() { return getToken(MxParser.VOID, 0); }
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public SingleTypeSpecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_singleTypeSpec; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitSingleTypeSpec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SingleTypeSpecContext singleTypeSpec() throws RecognitionException {
		SingleTypeSpecContext _localctx = new SingleTypeSpecContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_singleTypeSpec);
		try {
			setState(102);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
				enterOuterAlt(_localctx, 1);
				{
				setState(96);
				((SingleTypeSpecContext)_localctx).type = match(INT);
				}
				break;
			case CHAR:
				enterOuterAlt(_localctx, 2);
				{
				setState(97);
				((SingleTypeSpecContext)_localctx).type = match(CHAR);
				}
				break;
			case BOOL:
				enterOuterAlt(_localctx, 3);
				{
				setState(98);
				((SingleTypeSpecContext)_localctx).type = match(BOOL);
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 4);
				{
				setState(99);
				((SingleTypeSpecContext)_localctx).type = match(STRING);
				}
				break;
			case VOID:
				enterOuterAlt(_localctx, 5);
				{
				setState(100);
				((SingleTypeSpecContext)_localctx).type = match(VOID);
				}
				break;
			case Identifier:
				enterOuterAlt(_localctx, 6);
				{
				setState(101);
				((SingleTypeSpecContext)_localctx).type = match(Identifier);
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

	public static class ParamDeclListContext extends ParserRuleContext {
		public List<ParamDeclContext> paramDecl() {
			return getRuleContexts(ParamDeclContext.class);
		}
		public ParamDeclContext paramDecl(int i) {
			return getRuleContext(ParamDeclContext.class,i);
		}
		public ParamDeclListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramDeclList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitParamDeclList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamDeclListContext paramDeclList() throws RecognitionException {
		ParamDeclListContext _localctx = new ParamDeclListContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_paramDeclList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			paramDecl();
			setState(109);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(105);
				match(T__4);
				setState(106);
				paramDecl();
				}
				}
				setState(111);
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

	public static class ParamListContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ParamListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitParamList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamListContext paramList() throws RecognitionException {
		ParamListContext _localctx = new ParamListContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			expr(0);
			setState(117);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(113);
				match(T__4);
				setState(114);
				expr(0);
				}
				}
				setState(119);
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

	public static class ParamDeclContext extends ParserRuleContext {
		public TypeSpecContext typeSpec() {
			return getRuleContext(TypeSpecContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public ParamDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramDecl; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitParamDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamDeclContext paramDecl() throws RecognitionException {
		ParamDeclContext _localctx = new ParamDeclContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_paramDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			typeSpec();
			setState(121);
			match(Identifier);
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

	public static class VariDeclContext extends ParserRuleContext {
		public VariDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variDecl; }
	 
		public VariDeclContext() { }
		public void copyFrom(VariDeclContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class VariableDeclarationContext extends VariDeclContext {
		public TypeSpecContext typeSpec() {
			return getRuleContext(TypeSpecContext.class,0);
		}
		public List<VariInitContext> variInit() {
			return getRuleContexts(VariInitContext.class);
		}
		public VariInitContext variInit(int i) {
			return getRuleContext(VariInitContext.class,i);
		}
		public VariableDeclarationContext(VariDeclContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVariableDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariDeclContext variDecl() throws RecognitionException {
		VariDeclContext _localctx = new VariDeclContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_variDecl);
		int _la;
		try {
			_localctx = new VariableDeclarationContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(123);
			typeSpec();
			setState(124);
			variInit();
			setState(129);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(125);
				match(T__4);
				setState(126);
				variInit();
				}
				}
				setState(131);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(132);
			match(T__5);
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

	public static class VariInitContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public TerminalNode ASSIGN() { return getToken(MxParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public VariInitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variInit; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVariInit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariInitContext variInit() throws RecognitionException {
		VariInitContext _localctx = new VariInitContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_variInit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(134);
			match(Identifier);
			setState(137);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(135);
				match(ASSIGN);
				setState(136);
				expr(0);
				}
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

	public static class BlockStmtContext extends ParserRuleContext {
		public List<BlockCtntContext> blockCtnt() {
			return getRuleContexts(BlockCtntContext.class);
		}
		public BlockCtntContext blockCtnt(int i) {
			return getRuleContext(BlockCtntContext.class,i);
		}
		public BlockStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBlockStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockStmtContext blockStmt() throws RecognitionException {
		BlockStmtContext _localctx = new BlockStmtContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_blockStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			match(T__0);
			setState(143);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__2) | (1L << T__5) | (1L << BOOL) | (1L << INT) | (1L << CHAR) | (1L << STRING) | (1L << NULL) | (1L << VOID) | (1L << IF) | (1L << FOR) | (1L << WHILE) | (1L << BREAK) | (1L << CONTINUE) | (1L << RETURN) | (1L << NEW) | (1L << ADD) | (1L << SUB) | (1L << INC) | (1L << DEC) | (1L << LOGICNOT) | (1L << BITNOT) | (1L << IntegerConst) | (1L << CharConst) | (1L << StringConst) | (1L << BoolConst) | (1L << Identifier))) != 0)) {
				{
				{
				setState(140);
				blockCtnt();
				}
				}
				setState(145);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(146);
			match(T__1);
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

	public static class BlockCtntContext extends ParserRuleContext {
		public VariDeclContext variDecl() {
			return getRuleContext(VariDeclContext.class,0);
		}
		public StmtContext stmt() {
			return getRuleContext(StmtContext.class,0);
		}
		public BlockCtntContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockCtnt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBlockCtnt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockCtntContext blockCtnt() throws RecognitionException {
		BlockCtntContext _localctx = new BlockCtntContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_blockCtnt);
		try {
			setState(150);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(148);
				variDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(149);
				stmt();
				}
				break;
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

	public static class ExprStmtContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitExprStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprStmtContext exprStmt() throws RecognitionException {
		ExprStmtContext _localctx = new ExprStmtContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_exprStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(153);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << NULL) | (1L << NEW) | (1L << ADD) | (1L << SUB) | (1L << INC) | (1L << DEC) | (1L << LOGICNOT) | (1L << BITNOT) | (1L << IntegerConst) | (1L << CharConst) | (1L << StringConst) | (1L << BoolConst) | (1L << Identifier))) != 0)) {
				{
				setState(152);
				expr(0);
				}
			}

			setState(155);
			match(T__5);
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

	public static class StmtContext extends ParserRuleContext {
		public StmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmt; }
	 
		public StmtContext() { }
		public void copyFrom(StmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class BlockStatementContext extends StmtContext {
		public BlockStmtContext blockStmt() {
			return getRuleContext(BlockStmtContext.class,0);
		}
		public BlockStatementContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBlockStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IterationStatementContext extends StmtContext {
		public IterStmtContext iterStmt() {
			return getRuleContext(IterStmtContext.class,0);
		}
		public IterationStatementContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitIterationStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExpressionStatementContext extends StmtContext {
		public ExprStmtContext exprStmt() {
			return getRuleContext(ExprStmtContext.class,0);
		}
		public ExpressionStatementContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitExpressionStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SelectionStatementContext extends StmtContext {
		public SlctStmtContext slctStmt() {
			return getRuleContext(SlctStmtContext.class,0);
		}
		public SelectionStatementContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitSelectionStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class JumpStatementContext extends StmtContext {
		public JumpStmtContext jumpStmt() {
			return getRuleContext(JumpStmtContext.class,0);
		}
		public JumpStatementContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitJumpStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StmtContext stmt() throws RecognitionException {
		StmtContext _localctx = new StmtContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_stmt);
		try {
			setState(162);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				_localctx = new BlockStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(157);
				blockStmt();
				}
				break;
			case T__2:
			case T__5:
			case NULL:
			case NEW:
			case ADD:
			case SUB:
			case INC:
			case DEC:
			case LOGICNOT:
			case BITNOT:
			case IntegerConst:
			case CharConst:
			case StringConst:
			case BoolConst:
			case Identifier:
				_localctx = new ExpressionStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(158);
				exprStmt();
				}
				break;
			case IF:
				_localctx = new SelectionStatementContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(159);
				slctStmt();
				}
				break;
			case FOR:
			case WHILE:
				_localctx = new IterationStatementContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(160);
				iterStmt();
				}
				break;
			case BREAK:
			case CONTINUE:
			case RETURN:
				_localctx = new JumpStatementContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(161);
				jumpStmt();
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

	public static class SlctStmtContext extends ParserRuleContext {
		public TerminalNode IF() { return getToken(MxParser.IF, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public List<TerminalNode> ELIF() { return getTokens(MxParser.ELIF); }
		public TerminalNode ELIF(int i) {
			return getToken(MxParser.ELIF, i);
		}
		public TerminalNode ELSE() { return getToken(MxParser.ELSE, 0); }
		public SlctStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_slctStmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitSlctStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SlctStmtContext slctStmt() throws RecognitionException {
		SlctStmtContext _localctx = new SlctStmtContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_slctStmt);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			match(IF);
			setState(165);
			match(T__2);
			setState(166);
			expr(0);
			setState(167);
			match(T__3);
			setState(168);
			stmt();
			setState(177);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(169);
					match(ELIF);
					setState(170);
					match(T__2);
					setState(171);
					expr(0);
					setState(172);
					match(T__3);
					setState(173);
					stmt();
					}
					}
				}
				setState(179);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			}
			setState(182);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(180);
				match(ELSE);
				setState(181);
				stmt();
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

	public static class IterStmtContext extends ParserRuleContext {
		public IterStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iterStmt; }

		public IterStmtContext() { }
		public void copyFrom(IterStmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ForInitContext extends IterStmtContext {
		public VariDeclContext declInit;
		public ExprContext cond;
		public ExprContext step;
		public TerminalNode FOR() { return getToken(MxParser.FOR, 0); }
		public StmtContext stmt() {
			return getRuleContext(StmtContext.class,0);
		}
		public VariDeclContext variDecl() {
			return getRuleContext(VariDeclContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ForInitContext(IterStmtContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitForInit(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ForContext extends IterStmtContext {
		public ExprContext init;
		public ExprContext cond;
		public ExprContext step;
		public TerminalNode FOR() { return getToken(MxParser.FOR, 0); }
		public StmtContext stmt() {
			return getRuleContext(StmtContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ForContext(IterStmtContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFor(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WhileContext extends IterStmtContext {
		public TerminalNode WHILE() { return getToken(MxParser.WHILE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public StmtContext stmt() {
			return getRuleContext(StmtContext.class,0);
		}
		public WhileContext(IterStmtContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitWhile(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IterStmtContext iterStmt() throws RecognitionException {
		IterStmtContext _localctx = new IterStmtContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_iterStmt);
		int _la;
		try {
			setState(218);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				_localctx = new ForInitContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(184);
				match(FOR);
				setState(185);
				match(T__2);
				setState(186);
				((ForInitContext)_localctx).declInit = variDecl();
				setState(188);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << NULL) | (1L << NEW) | (1L << ADD) | (1L << SUB) | (1L << INC) | (1L << DEC) | (1L << LOGICNOT) | (1L << BITNOT) | (1L << IntegerConst) | (1L << CharConst) | (1L << StringConst) | (1L << BoolConst) | (1L << Identifier))) != 0)) {
					{
					setState(187);
					((ForInitContext)_localctx).cond = expr(0);
					}
				}

				setState(190);
				match(T__5);
				setState(192);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << NULL) | (1L << NEW) | (1L << ADD) | (1L << SUB) | (1L << INC) | (1L << DEC) | (1L << LOGICNOT) | (1L << BITNOT) | (1L << IntegerConst) | (1L << CharConst) | (1L << StringConst) | (1L << BoolConst) | (1L << Identifier))) != 0)) {
					{
					setState(191);
					((ForInitContext)_localctx).step = expr(0);
					}
				}

				setState(194);
				match(T__3);
				setState(195);
				stmt();
				}
				break;
			case 2:
				_localctx = new ForContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(197);
				match(FOR);
				setState(198);
				match(T__2);
				setState(200);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << NULL) | (1L << NEW) | (1L << ADD) | (1L << SUB) | (1L << INC) | (1L << DEC) | (1L << LOGICNOT) | (1L << BITNOT) | (1L << IntegerConst) | (1L << CharConst) | (1L << StringConst) | (1L << BoolConst) | (1L << Identifier))) != 0)) {
					{
					setState(199);
					((ForContext)_localctx).init = expr(0);
					}
				}

				setState(202);
				match(T__5);
				setState(204);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << NULL) | (1L << NEW) | (1L << ADD) | (1L << SUB) | (1L << INC) | (1L << DEC) | (1L << LOGICNOT) | (1L << BITNOT) | (1L << IntegerConst) | (1L << CharConst) | (1L << StringConst) | (1L << BoolConst) | (1L << Identifier))) != 0)) {
					{
					setState(203);
					((ForContext)_localctx).cond = expr(0);
					}
				}

				setState(206);
				match(T__5);
				setState(208);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << NULL) | (1L << NEW) | (1L << ADD) | (1L << SUB) | (1L << INC) | (1L << DEC) | (1L << LOGICNOT) | (1L << BITNOT) | (1L << IntegerConst) | (1L << CharConst) | (1L << StringConst) | (1L << BoolConst) | (1L << Identifier))) != 0)) {
					{
					setState(207);
					((ForContext)_localctx).step = expr(0);
					}
				}

				setState(210);
				match(T__3);
				setState(211);
				stmt();
				}
				break;
			case 3:
				_localctx = new WhileContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(212);
				match(WHILE);
				setState(213);
				match(T__2);
				setState(214);
				expr(0);
				setState(215);
				match(T__3);
				setState(216);
				stmt();
				}
				break;
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

	public static class JumpStmtContext extends ParserRuleContext {
		public JumpStmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jumpStmt; }

		public JumpStmtContext() { }
		public void copyFrom(JumpStmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class BreakContext extends JumpStmtContext {
		public TerminalNode BREAK() { return getToken(MxParser.BREAK, 0); }
		public BreakContext(JumpStmtContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBreak(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ContinueContext extends JumpStmtContext {
		public TerminalNode CONTINUE() { return getToken(MxParser.CONTINUE, 0); }
		public ContinueContext(JumpStmtContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitContinue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ReturnContext extends JumpStmtContext {
		public TerminalNode RETURN() { return getToken(MxParser.RETURN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ReturnContext(JumpStmtContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitReturn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StrtReturnContext extends JumpStmtContext {
		public TerminalNode RETURN() { return getToken(MxParser.RETURN, 0); }
		public StrtReturnContext(JumpStmtContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitStrtReturn(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JumpStmtContext jumpStmt() throws RecognitionException {
		JumpStmtContext _localctx = new JumpStmtContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_jumpStmt);
		try {
			setState(230);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				_localctx = new BreakContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(220);
				match(BREAK);
				setState(221);
				match(T__5);
				}
				break;
			case 2:
				_localctx = new ContinueContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(222);
				match(CONTINUE);
				setState(223);
				match(T__5);
				}
				break;
			case 3:
				_localctx = new StrtReturnContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(224);
				match(RETURN);
				setState(225);
				match(T__5);
				}
				break;
			case 4:
				_localctx = new ReturnContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(226);
				match(RETURN);
				setState(227);
				expr(0);
				setState(228);
				match(T__5);
				}
				break;
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

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }

		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NewContext extends ExprContext {
		public Token op;
		public CreatorContext creator() {
			return getRuleContext(CreatorContext.class,0);
		}
		public TerminalNode NEW() { return getToken(MxParser.NEW, 0); }
		public NewContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitNew(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IdentifierContext extends ExprContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public IdentifierContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MemberAccessContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public MemberAccessContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitMemberAccess(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ConstContext extends ExprContext {
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public ConstContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitConst(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SuffixIncrementDecrementContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode INC() { return getToken(MxParser.INC, 0); }
		public TerminalNode DEC() { return getToken(MxParser.DEC, 0); }
		public SuffixIncrementDecrementContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitSuffixIncrementDecrement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PrefixIncrementDecrementContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode INC() { return getToken(MxParser.INC, 0); }
		public TerminalNode DEC() { return getToken(MxParser.DEC, 0); }
		public PrefixIncrementDecrementContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitPrefixIncrementDecrement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BinaryOperationContext extends ExprContext {
		public ExprContext left;
		public Token op;
		public ExprContext right;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode MUL() { return getToken(MxParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(MxParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(MxParser.MOD, 0); }
		public TerminalNode ADD() { return getToken(MxParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(MxParser.SUB, 0); }
		public TerminalNode LSHIFT() { return getToken(MxParser.LSHIFT, 0); }
		public TerminalNode RSHIFT() { return getToken(MxParser.RSHIFT, 0); }
		public TerminalNode LT() { return getToken(MxParser.LT, 0); }
		public TerminalNode GT() { return getToken(MxParser.GT, 0); }
		public TerminalNode LEQ() { return getToken(MxParser.LEQ, 0); }
		public TerminalNode GEQ() { return getToken(MxParser.GEQ, 0); }
		public TerminalNode EQ() { return getToken(MxParser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(MxParser.NEQ, 0); }
		public TerminalNode BITAND() { return getToken(MxParser.BITAND, 0); }
		public TerminalNode BITXOR() { return getToken(MxParser.BITXOR, 0); }
		public TerminalNode BITOR() { return getToken(MxParser.BITOR, 0); }
		public TerminalNode LOGICAND() { return getToken(MxParser.LOGICAND, 0); }
		public TerminalNode LOGICOR() { return getToken(MxParser.LOGICOR, 0); }
		public BinaryOperationContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBinaryOperation(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IndexAccessContext extends ExprContext {
		public ExprContext array;
		public ExprContext index;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public IndexAccessContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitIndexAccess(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NotContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode LOGICNOT() { return getToken(MxParser.LOGICNOT, 0); }
		public TerminalNode BITNOT() { return getToken(MxParser.BITNOT, 0); }
		public NotContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitNot(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PositiveNegativeContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode ADD() { return getToken(MxParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(MxParser.SUB, 0); }
		public PositiveNegativeContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitPositiveNegative(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctionCallContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ParamListContext paramList() {
			return getRuleContext(ParamListContext.class,0);
		}
		public FunctionCallContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SubExpressionContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public SubExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitSubExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AssignContext extends ExprContext {
		public Token op;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode ASSIGN() { return getToken(MxParser.ASSIGN, 0); }
		public AssignContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitAssign(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 40;
		enterRecursionRule(_localctx, 40, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(247);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INC:
			case DEC:
				{
				_localctx = new PrefixIncrementDecrementContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(233);
				((PrefixIncrementDecrementContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==INC || _la==DEC) ) {
					((PrefixIncrementDecrementContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(234);
				expr(19);
				}
				break;
			case ADD:
			case SUB:
				{
				_localctx = new PositiveNegativeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(235);
				((PositiveNegativeContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==ADD || _la==SUB) ) {
					((PositiveNegativeContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(236);
				expr(18);
				}
				break;
			case LOGICNOT:
			case BITNOT:
				{
				_localctx = new NotContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(237);
				((NotContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==LOGICNOT || _la==BITNOT) ) {
					((NotContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(238);
				expr(17);
				}
				break;
			case NEW:
				{
				_localctx = new NewContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(239);
				((NewContext)_localctx).op = match(NEW);
				setState(240);
				creator();
				}
				break;
			case Identifier:
				{
				_localctx = new IdentifierContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(241);
				match(Identifier);
				}
				break;
			case NULL:
			case IntegerConst:
			case CharConst:
			case StringConst:
			case BoolConst:
				{
				_localctx = new ConstContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(242);
				constant();
				}
				break;
			case T__2:
				{
				_localctx = new SubExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(243);
				match(T__2);
				setState(244);
				expr(0);
				setState(245);
				match(T__3);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(303);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(301);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(249);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(250);
						((BinaryOperationContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MUL) | (1L << DIV) | (1L << MOD))) != 0)) ) {
							((BinaryOperationContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(251);
						((BinaryOperationContext)_localctx).right = expr(16);
						}
						break;
					case 2:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(252);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(253);
						((BinaryOperationContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUB) ) {
							((BinaryOperationContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(254);
						((BinaryOperationContext)_localctx).right = expr(15);
						}
						break;
					case 3:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(255);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(256);
						((BinaryOperationContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==LSHIFT || _la==RSHIFT) ) {
							((BinaryOperationContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(257);
						((BinaryOperationContext)_localctx).right = expr(14);
						}
						break;
					case 4:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(258);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(259);
						((BinaryOperationContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==LT || _la==GT) ) {
							((BinaryOperationContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(260);
						((BinaryOperationContext)_localctx).right = expr(13);
						}
						break;
					case 5:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(261);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(262);
						((BinaryOperationContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==LEQ || _la==GEQ) ) {
							((BinaryOperationContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(263);
						((BinaryOperationContext)_localctx).right = expr(12);
						}
						break;
					case 6:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(264);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(265);
						((BinaryOperationContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==EQ || _la==NEQ) ) {
							((BinaryOperationContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(266);
						((BinaryOperationContext)_localctx).right = expr(11);
						}
						break;
					case 7:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(267);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(268);
						((BinaryOperationContext)_localctx).op = match(BITAND);
						setState(269);
						((BinaryOperationContext)_localctx).right = expr(10);
						}
						break;
					case 8:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(270);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(271);
						((BinaryOperationContext)_localctx).op = match(BITXOR);
						setState(272);
						((BinaryOperationContext)_localctx).right = expr(9);
						}
						break;
					case 9:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(273);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(274);
						((BinaryOperationContext)_localctx).op = match(BITOR);
						setState(275);
						((BinaryOperationContext)_localctx).right = expr(8);
						}
						break;
					case 10:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(276);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(277);
						((BinaryOperationContext)_localctx).op = match(LOGICAND);
						setState(278);
						((BinaryOperationContext)_localctx).right = expr(7);
						}
						break;
					case 11:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(279);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(280);
						((BinaryOperationContext)_localctx).op = match(LOGICOR);
						setState(281);
						((BinaryOperationContext)_localctx).right = expr(6);
						}
						break;
					case 12:
						{
						_localctx = new AssignContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(282);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(283);
						((AssignContext)_localctx).op = match(ASSIGN);
						setState(284);
						expr(4);
						}
						break;
					case 13:
						{
						_localctx = new SuffixIncrementDecrementContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(285);
						if (!(precpred(_ctx, 23))) throw new FailedPredicateException(this, "precpred(_ctx, 23)");
						setState(286);
						((SuffixIncrementDecrementContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==INC || _la==DEC) ) {
							((SuffixIncrementDecrementContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					case 14:
						{
						_localctx = new FunctionCallContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(287);
						if (!(precpred(_ctx, 22))) throw new FailedPredicateException(this, "precpred(_ctx, 22)");
						setState(288);
						match(T__2);
						setState(290);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << NULL) | (1L << NEW) | (1L << ADD) | (1L << SUB) | (1L << INC) | (1L << DEC) | (1L << LOGICNOT) | (1L << BITNOT) | (1L << IntegerConst) | (1L << CharConst) | (1L << StringConst) | (1L << BoolConst) | (1L << Identifier))) != 0)) {
							{
							setState(289);
							paramList();
							}
						}

						setState(292);
						match(T__3);
						}
						break;
					case 15:
						{
						_localctx = new IndexAccessContext(new ExprContext(_parentctx, _parentState));
						((IndexAccessContext)_localctx).array = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(293);
						if (!(precpred(_ctx, 21))) throw new FailedPredicateException(this, "precpred(_ctx, 21)");
						setState(294);
						match(T__6);
						setState(295);
						((IndexAccessContext)_localctx).index = expr(0);
						setState(296);
						match(T__7);
						}
						break;
					case 16:
						{
						_localctx = new MemberAccessContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(298);
						if (!(precpred(_ctx, 20))) throw new FailedPredicateException(this, "precpred(_ctx, 20)");
						setState(299);
						match(T__8);
						setState(300);
						match(Identifier);
						}
						break;
					}
					}
				}
				setState(305);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
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

	public static class CreatorContext extends ParserRuleContext {
		public CreatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_creator; }

		public CreatorContext() { }
		public void copyFrom(CreatorContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class CreatorErrorContext extends CreatorContext {
		public SingleTypeSpecContext singleTypeSpec() {
			return getRuleContext(SingleTypeSpecContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> Brackets() { return getTokens(MxParser.Brackets); }
		public TerminalNode Brackets(int i) {
			return getToken(MxParser.Brackets, i);
		}
		public CreatorErrorContext(CreatorContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitCreatorError(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreatorSingleContext extends CreatorContext {
		public SingleTypeSpecContext singleTypeSpec() {
			return getRuleContext(SingleTypeSpecContext.class,0);
		}
		public CreatorSingleContext(CreatorContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitCreatorSingle(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CreatorArrayContext extends CreatorContext {
		public SingleTypeSpecContext singleTypeSpec() {
			return getRuleContext(SingleTypeSpecContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> Brackets() { return getTokens(MxParser.Brackets); }
		public TerminalNode Brackets(int i) {
			return getToken(MxParser.Brackets, i);
		}
		public CreatorArrayContext(CreatorContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitCreatorArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreatorContext creator() throws RecognitionException {
		CreatorContext _localctx = new CreatorContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_creator);
		int _la;
		try {
			int _alt;
			setState(344);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				_localctx = new CreatorErrorContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(306);
				singleTypeSpec();
				setState(311);
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(307);
					match(T__6);
					setState(308);
					expr(0);
					setState(309);
					match(T__7);
					}
					}
					setState(313);
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==T__6 );
				setState(316);
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(315);
					match(Brackets);
					}
					}
					setState(318);
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==Brackets );
				setState(324);
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(320);
						match(T__6);
						setState(321);
						expr(0);
						setState(322);
						match(T__7);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(326);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 2:
				_localctx = new CreatorArrayContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(328);
				singleTypeSpec();
				setState(333);
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(329);
						match(T__6);
						setState(330);
						expr(0);
						setState(331);
						match(T__7);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(335);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				setState(340);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(337);
						match(Brackets);
						}
						} 
					}
					setState(342);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
				}
				}
				break;
			case 3:
				_localctx = new CreatorSingleContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(343);
				singleTypeSpec();
				}
				break;
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

	public static class ConstantContext extends ParserRuleContext {
		public Token type;
		public TerminalNode IntegerConst() { return getToken(MxParser.IntegerConst, 0); }
		public TerminalNode CharConst() { return getToken(MxParser.CharConst, 0); }
		public TerminalNode StringConst() { return getToken(MxParser.StringConst, 0); }
		public TerminalNode BoolConst() { return getToken(MxParser.BoolConst, 0); }
		public TerminalNode NULL() { return getToken(MxParser.NULL, 0); }
		public ConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitConstant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_constant);
		try {
			setState(351);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IntegerConst:
				enterOuterAlt(_localctx, 1);
				{
				setState(346);
				((ConstantContext)_localctx).type = match(IntegerConst);
				}
				break;
			case CharConst:
				enterOuterAlt(_localctx, 2);
				{
				setState(347);
				((ConstantContext)_localctx).type = match(CharConst);
				}
				break;
			case StringConst:
				enterOuterAlt(_localctx, 3);
				{
				setState(348);
				((ConstantContext)_localctx).type = match(StringConst);
				}
				break;
			case BoolConst:
				enterOuterAlt(_localctx, 4);
				{
				setState(349);
				((ConstantContext)_localctx).type = match(BoolConst);
				}
				break;
			case NULL:
				enterOuterAlt(_localctx, 5);
				{
				setState(350);
				((ConstantContext)_localctx).type = match(NULL);
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
		case 20:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 15);
		case 1:
			return precpred(_ctx, 14);
		case 2:
			return precpred(_ctx, 13);
		case 3:
			return precpred(_ctx, 12);
		case 4:
			return precpred(_ctx, 11);
		case 5:
			return precpred(_ctx, 10);
		case 6:
			return precpred(_ctx, 9);
		case 7:
			return precpred(_ctx, 8);
		case 8:
			return precpred(_ctx, 7);
		case 9:
			return precpred(_ctx, 6);
		case 10:
			return precpred(_ctx, 5);
		case 11:
			return precpred(_ctx, 4);
		case 12:
			return precpred(_ctx, 23);
		case 13:
			return precpred(_ctx, 22);
		case 14:
			return precpred(_ctx, 21);
		case 15:
			return precpred(_ctx, 20);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3<\u0164\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\3\2\7\2\62"+
		"\n\2\f\2\16\2\65\13\2\3\2\3\2\3\3\3\3\3\3\5\3<\n\3\3\4\3\4\3\4\3\4\7\4"+
		"B\n\4\f\4\16\4E\13\4\3\4\3\4\3\5\3\5\3\5\5\5L\n\5\3\6\3\6\3\6\3\6\3\6"+
		"\3\7\3\7\3\7\3\7\5\7W\n\7\3\7\3\7\3\7\3\b\3\b\7\b^\n\b\f\b\16\ba\13\b"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\5\ti\n\t\3\n\3\n\3\n\7\nn\n\n\f\n\16\nq\13\n"+
		"\3\13\3\13\3\13\7\13v\n\13\f\13\16\13y\13\13\3\f\3\f\3\f\3\r\3\r\3\r\3"+
		"\r\7\r\u0082\n\r\f\r\16\r\u0085\13\r\3\r\3\r\3\16\3\16\3\16\5\16\u008c"+
		"\n\16\3\17\3\17\7\17\u0090\n\17\f\17\16\17\u0093\13\17\3\17\3\17\3\20"+
		"\3\20\5\20\u0099\n\20\3\21\5\21\u009c\n\21\3\21\3\21\3\22\3\22\3\22\3"+
		"\22\3\22\5\22\u00a5\n\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\7\23\u00b2\n\23\f\23\16\23\u00b5\13\23\3\23\3\23\5\23\u00b9"+
		"\n\23\3\24\3\24\3\24\3\24\5\24\u00bf\n\24\3\24\3\24\5\24\u00c3\n\24\3"+
		"\24\3\24\3\24\3\24\3\24\3\24\5\24\u00cb\n\24\3\24\3\24\5\24\u00cf\n\24"+
		"\3\24\3\24\5\24\u00d3\n\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\5\24"+
		"\u00dd\n\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u00e9"+
		"\n\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\5\26\u00fa\n\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\5\26\u0125\n\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\7\26\u0130\n\26\f\26\16\26\u0133\13\26\3\27\3\27\3\27\3\27"+
		"\3\27\6\27\u013a\n\27\r\27\16\27\u013b\3\27\6\27\u013f\n\27\r\27\16\27"+
		"\u0140\3\27\3\27\3\27\3\27\6\27\u0147\n\27\r\27\16\27\u0148\3\27\3\27"+
		"\3\27\3\27\3\27\6\27\u0150\n\27\r\27\16\27\u0151\3\27\7\27\u0155\n\27"+
		"\f\27\16\27\u0158\13\27\3\27\5\27\u015b\n\27\3\30\3\30\3\30\3\30\3\30"+
		"\5\30\u0162\n\30\3\30\2\3*\31\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 "+
		"\"$&(*,.\2\n\3\2!\"\3\2\34\35\4\2,,//\3\2\36 \3\2-.\3\2$%\3\2()\3\2&\'"+
		"\2\u0192\2\63\3\2\2\2\4;\3\2\2\2\6=\3\2\2\2\bK\3\2\2\2\nM\3\2\2\2\fR\3"+
		"\2\2\2\16[\3\2\2\2\20h\3\2\2\2\22j\3\2\2\2\24r\3\2\2\2\26z\3\2\2\2\30"+
		"}\3\2\2\2\32\u0088\3\2\2\2\34\u008d\3\2\2\2\36\u0098\3\2\2\2 \u009b\3"+
		"\2\2\2\"\u00a4\3\2\2\2$\u00a6\3\2\2\2&\u00dc\3\2\2\2(\u00e8\3\2\2\2*\u00f9"+
		"\3\2\2\2,\u015a\3\2\2\2.\u0161\3\2\2\2\60\62\5\4\3\2\61\60\3\2\2\2\62"+
		"\65\3\2\2\2\63\61\3\2\2\2\63\64\3\2\2\2\64\66\3\2\2\2\65\63\3\2\2\2\66"+
		"\67\7\2\2\3\67\3\3\2\2\28<\5\6\4\29<\5\f\7\2:<\5\30\r\2;8\3\2\2\2;9\3"+
		"\2\2\2;:\3\2\2\2<\5\3\2\2\2=>\7\33\2\2>?\78\2\2?C\7\3\2\2@B\5\b\5\2A@"+
		"\3\2\2\2BE\3\2\2\2CA\3\2\2\2CD\3\2\2\2DF\3\2\2\2EC\3\2\2\2FG\7\4\2\2G"+
		"\7\3\2\2\2HL\5\30\r\2IL\5\f\7\2JL\5\n\6\2KH\3\2\2\2KI\3\2\2\2KJ\3\2\2"+
		"\2L\t\3\2\2\2MN\78\2\2NO\7\5\2\2OP\7\6\2\2PQ\5\34\17\2Q\13\3\2\2\2RS\5"+
		"\16\b\2ST\78\2\2TV\7\5\2\2UW\5\22\n\2VU\3\2\2\2VW\3\2\2\2WX\3\2\2\2XY"+
		"\7\6\2\2YZ\5\34\17\2Z\r\3\2\2\2[_\5\20\t\2\\^\7\63\2\2]\\\3\2\2\2^a\3"+
		"\2\2\2_]\3\2\2\2_`\3\2\2\2`\17\3\2\2\2a_\3\2\2\2bi\7\r\2\2ci\7\16\2\2"+
		"di\7\f\2\2ei\7\17\2\2fi\7\21\2\2gi\78\2\2hb\3\2\2\2hc\3\2\2\2hd\3\2\2"+
		"\2he\3\2\2\2hf\3\2\2\2hg\3\2\2\2i\21\3\2\2\2jo\5\26\f\2kl\7\7\2\2ln\5"+
		"\26\f\2mk\3\2\2\2nq\3\2\2\2om\3\2\2\2op\3\2\2\2p\23\3\2\2\2qo\3\2\2\2"+
		"rw\5*\26\2st\7\7\2\2tv\5*\26\2us\3\2\2\2vy\3\2\2\2wu\3\2\2\2wx\3\2\2\2"+
		"x\25\3\2\2\2yw\3\2\2\2z{\5\16\b\2{|\78\2\2|\27\3\2\2\2}~\5\16\b\2~\u0083"+
		"\5\32\16\2\177\u0080\7\7\2\2\u0080\u0082\5\32\16\2\u0081\177\3\2\2\2\u0082"+
		"\u0085\3\2\2\2\u0083\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0086\3\2"+
		"\2\2\u0085\u0083\3\2\2\2\u0086\u0087\7\b\2\2\u0087\31\3\2\2\2\u0088\u008b"+
		"\78\2\2\u0089\u008a\7#\2\2\u008a\u008c\5*\26\2\u008b\u0089\3\2\2\2\u008b"+
		"\u008c\3\2\2\2\u008c\33\3\2\2\2\u008d\u0091\7\3\2\2\u008e\u0090\5\36\20"+
		"\2\u008f\u008e\3\2\2\2\u0090\u0093\3\2\2\2\u0091\u008f\3\2\2\2\u0091\u0092"+
		"\3\2\2\2\u0092\u0094\3\2\2\2\u0093\u0091\3\2\2\2\u0094\u0095\7\4\2\2\u0095"+
		"\35\3\2\2\2\u0096\u0099\5\30\r\2\u0097\u0099\5\"\22\2\u0098\u0096\3\2"+
		"\2\2\u0098\u0097\3\2\2\2\u0099\37\3\2\2\2\u009a\u009c\5*\26\2\u009b\u009a"+
		"\3\2\2\2\u009b\u009c\3\2\2\2\u009c\u009d\3\2\2\2\u009d\u009e\7\b\2\2\u009e"+
		"!\3\2\2\2\u009f\u00a5\5\34\17\2\u00a0\u00a5\5 \21\2\u00a1\u00a5\5$\23"+
		"\2\u00a2\u00a5\5&\24\2\u00a3\u00a5\5(\25\2\u00a4\u009f\3\2\2\2\u00a4\u00a0"+
		"\3\2\2\2\u00a4\u00a1\3\2\2\2\u00a4\u00a2\3\2\2\2\u00a4\u00a3\3\2\2\2\u00a5"+
		"#\3\2\2\2\u00a6\u00a7\7\22\2\2\u00a7\u00a8\7\5\2\2\u00a8\u00a9\5*\26\2"+
		"\u00a9\u00aa\7\6\2\2\u00aa\u00b3\5\"\22\2\u00ab\u00ac\7\23\2\2\u00ac\u00ad"+
		"\7\5\2\2\u00ad\u00ae\5*\26\2\u00ae\u00af\7\6\2\2\u00af\u00b0\5\"\22\2"+
		"\u00b0\u00b2\3\2\2\2\u00b1\u00ab\3\2\2\2\u00b2\u00b5\3\2\2\2\u00b3\u00b1"+
		"\3\2\2\2\u00b3\u00b4\3\2\2\2\u00b4\u00b8\3\2\2\2\u00b5\u00b3\3\2\2\2\u00b6"+
		"\u00b7\7\24\2\2\u00b7\u00b9\5\"\22\2\u00b8\u00b6\3\2\2\2\u00b8\u00b9\3"+
		"\2\2\2\u00b9%\3\2\2\2\u00ba\u00bb\7\25\2\2\u00bb\u00bc\7\5\2\2\u00bc\u00be"+
		"\5\30\r\2\u00bd\u00bf\5*\26\2\u00be\u00bd\3\2\2\2\u00be\u00bf\3\2\2\2"+
		"\u00bf\u00c0\3\2\2\2\u00c0\u00c2\7\b\2\2\u00c1\u00c3\5*\26\2\u00c2\u00c1"+
		"\3\2\2\2\u00c2\u00c3\3\2\2\2\u00c3\u00c4\3\2\2\2\u00c4\u00c5\7\6\2\2\u00c5"+
		"\u00c6\5\"\22\2\u00c6\u00dd\3\2\2\2\u00c7\u00c8\7\25\2\2\u00c8\u00ca\7"+
		"\5\2\2\u00c9\u00cb\5*\26\2\u00ca\u00c9\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb"+
		"\u00cc\3\2\2\2\u00cc\u00ce\7\b\2\2\u00cd\u00cf\5*\26\2\u00ce\u00cd\3\2"+
		"\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00d0\3\2\2\2\u00d0\u00d2\7\b\2\2\u00d1"+
		"\u00d3\5*\26\2\u00d2\u00d1\3\2\2\2\u00d2\u00d3\3\2\2\2\u00d3\u00d4\3\2"+
		"\2\2\u00d4\u00d5\7\6\2\2\u00d5\u00dd\5\"\22\2\u00d6\u00d7\7\26\2\2\u00d7"+
		"\u00d8\7\5\2\2\u00d8\u00d9\5*\26\2\u00d9\u00da\7\6\2\2\u00da\u00db\5\""+
		"\22\2\u00db\u00dd\3\2\2\2\u00dc\u00ba\3\2\2\2\u00dc\u00c7\3\2\2\2\u00dc"+
		"\u00d6\3\2\2\2\u00dd\'\3\2\2\2\u00de\u00df\7\27\2\2\u00df\u00e9\7\b\2"+
		"\2\u00e0\u00e1\7\30\2\2\u00e1\u00e9\7\b\2\2\u00e2\u00e3\7\31\2\2\u00e3"+
		"\u00e9\7\b\2\2\u00e4\u00e5\7\31\2\2\u00e5\u00e6\5*\26\2\u00e6\u00e7\7"+
		"\b\2\2\u00e7\u00e9\3\2\2\2\u00e8\u00de\3\2\2\2\u00e8\u00e0\3\2\2\2\u00e8"+
		"\u00e2\3\2\2\2\u00e8\u00e4\3\2\2\2\u00e9)\3\2\2\2\u00ea\u00eb\b\26\1\2"+
		"\u00eb\u00ec\t\2\2\2\u00ec\u00fa\5*\26\25\u00ed\u00ee\t\3\2\2\u00ee\u00fa"+
		"\5*\26\24\u00ef\u00f0\t\4\2\2\u00f0\u00fa\5*\26\23\u00f1\u00f2\7\32\2"+
		"\2\u00f2\u00fa\5,\27\2\u00f3\u00fa\78\2\2\u00f4\u00fa\5.\30\2\u00f5\u00f6"+
		"\7\5\2\2\u00f6\u00f7\5*\26\2\u00f7\u00f8\7\6\2\2\u00f8\u00fa\3\2\2\2\u00f9"+
		"\u00ea\3\2\2\2\u00f9\u00ed\3\2\2\2\u00f9\u00ef\3\2\2\2\u00f9\u00f1\3\2"+
		"\2\2\u00f9\u00f3\3\2\2\2\u00f9\u00f4\3\2\2\2\u00f9\u00f5\3\2\2\2\u00fa"+
		"\u0131\3\2\2\2\u00fb\u00fc\f\21\2\2\u00fc\u00fd\t\5\2\2\u00fd\u0130\5"+
		"*\26\22\u00fe\u00ff\f\20\2\2\u00ff\u0100\t\3\2\2\u0100\u0130\5*\26\21"+
		"\u0101\u0102\f\17\2\2\u0102\u0103\t\6\2\2\u0103\u0130\5*\26\20\u0104\u0105"+
		"\f\16\2\2\u0105\u0106\t\7\2\2\u0106\u0130\5*\26\17\u0107\u0108\f\r\2\2"+
		"\u0108\u0109\t\b\2\2\u0109\u0130\5*\26\16\u010a\u010b\f\f\2\2\u010b\u010c"+
		"\t\t\2\2\u010c\u0130\5*\26\r\u010d\u010e\f\13\2\2\u010e\u010f\7\62\2\2"+
		"\u010f\u0130\5*\26\f\u0110\u0111\f\n\2\2\u0111\u0112\7\61\2\2\u0112\u0130"+
		"\5*\26\13\u0113\u0114\f\t\2\2\u0114\u0115\7\60\2\2\u0115\u0130\5*\26\n"+
		"\u0116\u0117\f\b\2\2\u0117\u0118\7*\2\2\u0118\u0130\5*\26\t\u0119\u011a"+
		"\f\7\2\2\u011a\u011b\7+\2\2\u011b\u0130\5*\26\b\u011c\u011d\f\6\2\2\u011d"+
		"\u011e\7#\2\2\u011e\u0130\5*\26\6\u011f\u0120\f\31\2\2\u0120\u0130\t\2"+
		"\2\2\u0121\u0122\f\30\2\2\u0122\u0124\7\5\2\2\u0123\u0125\5\24\13\2\u0124"+
		"\u0123\3\2\2\2\u0124\u0125\3\2\2\2\u0125\u0126\3\2\2\2\u0126\u0130\7\6"+
		"\2\2\u0127\u0128\f\27\2\2\u0128\u0129\7\t\2\2\u0129\u012a\5*\26\2\u012a"+
		"\u012b\7\n\2\2\u012b\u0130\3\2\2\2\u012c\u012d\f\26\2\2\u012d\u012e\7"+
		"\13\2\2\u012e\u0130\78\2\2\u012f\u00fb\3\2\2\2\u012f\u00fe\3\2\2\2\u012f"+
		"\u0101\3\2\2\2\u012f\u0104\3\2\2\2\u012f\u0107\3\2\2\2\u012f\u010a\3\2"+
		"\2\2\u012f\u010d\3\2\2\2\u012f\u0110\3\2\2\2\u012f\u0113\3\2\2\2\u012f"+
		"\u0116\3\2\2\2\u012f\u0119\3\2\2\2\u012f\u011c\3\2\2\2\u012f\u011f\3\2"+
		"\2\2\u012f\u0121\3\2\2\2\u012f\u0127\3\2\2\2\u012f\u012c\3\2\2\2\u0130"+
		"\u0133\3\2\2\2\u0131\u012f\3\2\2\2\u0131\u0132\3\2\2\2\u0132+\3\2\2\2"+
		"\u0133\u0131\3\2\2\2\u0134\u0139\5\20\t\2\u0135\u0136\7\t\2\2\u0136\u0137"+
		"\5*\26\2\u0137\u0138\7\n\2\2\u0138\u013a\3\2\2\2\u0139\u0135\3\2\2\2\u013a"+
		"\u013b\3\2\2\2\u013b\u0139\3\2\2\2\u013b\u013c\3\2\2\2\u013c\u013e\3\2"+
		"\2\2\u013d\u013f\7\63\2\2\u013e\u013d\3\2\2\2\u013f\u0140\3\2\2\2\u0140"+
		"\u013e\3\2\2\2\u0140\u0141\3\2\2\2\u0141\u0146\3\2\2\2\u0142\u0143\7\t"+
		"\2\2\u0143\u0144\5*\26\2\u0144\u0145\7\n\2\2\u0145\u0147\3\2\2\2\u0146"+
		"\u0142\3\2\2\2\u0147\u0148\3\2\2\2\u0148\u0146\3\2\2\2\u0148\u0149\3\2"+
		"\2\2\u0149\u015b\3\2\2\2\u014a\u014f\5\20\t\2\u014b\u014c\7\t\2\2\u014c"+
		"\u014d\5*\26\2\u014d\u014e\7\n\2\2\u014e\u0150\3\2\2\2\u014f\u014b\3\2"+
		"\2\2\u0150\u0151\3\2\2\2\u0151\u014f\3\2\2\2\u0151\u0152\3\2\2\2\u0152"+
		"\u0156\3\2\2\2\u0153\u0155\7\63\2\2\u0154\u0153\3\2\2\2\u0155\u0158\3"+
		"\2\2\2\u0156\u0154\3\2\2\2\u0156\u0157\3\2\2\2\u0157\u015b\3\2\2\2\u0158"+
		"\u0156\3\2\2\2\u0159\u015b\5\20\t\2\u015a\u0134\3\2\2\2\u015a\u014a\3"+
		"\2\2\2\u015a\u0159\3\2\2\2\u015b-\3\2\2\2\u015c\u0162\7\64\2\2\u015d\u0162"+
		"\7\65\2\2\u015e\u0162\7\66\2\2\u015f\u0162\7\67\2\2\u0160\u0162\7\20\2"+
		"\2\u0161\u015c\3\2\2\2\u0161\u015d\3\2\2\2\u0161\u015e\3\2\2\2\u0161\u015f"+
		"\3\2\2\2\u0161\u0160\3\2\2\2\u0162/\3\2\2\2%\63;CKV_how\u0083\u008b\u0091"+
		"\u0098\u009b\u00a4\u00b3\u00b8\u00be\u00c2\u00ca\u00ce\u00d2\u00dc\u00e8"+
		"\u00f9\u0124\u012f\u0131\u013b\u0140\u0148\u0151\u0156\u015a\u0161";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}