// Generated from C:/Users/qydyx/Desktop/Compiler/MxGrammar\Mx.g4 by ANTLR 4.7
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

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
		THIS=26, ADD=27, SUB=28, MUL=29, DIV=30, MOD=31, INC=32, DEC=33, ASSIGN=34, 
		LT=35, GT=36, EQ=37, NEQ=38, LEQ=39, GEQ=40, LOGICAND=41, LOGICOR=42, 
		LOGICNOT=43, LSHIFT=44, RSHIFT=45, BITNOT=46, BITOR=47, BITXOR=48, BITAND=49, 
		Brackets=50, IntegerConst=51, CharConst=52, StringConst=53, BoolConst=54, 
		Identifier=55, WS=56, COMMENT=57, LINE_COMMENT=58, NEWLINE=59;
	public static final int
		RULE_prog = 0, RULE_progSec = 1, RULE_classDecl = 2, RULE_memDecl = 3, 
		RULE_funcDecl = 4, RULE_typeSpec = 5, RULE_singleTypeSpec = 6, RULE_paramDeclList = 7, 
		RULE_paramList = 8, RULE_paramDecl = 9, RULE_variDecl = 10, RULE_variInit = 11, 
		RULE_blockStmt = 12, RULE_blockCtnt = 13, RULE_exprStmt = 14, RULE_stmt = 15, 
		RULE_slctStmt = 16, RULE_iterStmt = 17, RULE_jumpStmt = 18, RULE_expr = 19, 
		RULE_creator = 20, RULE_constant = 21;
	public static final String[] ruleNames = {
		"prog", "progSec", "classDecl", "memDecl", "funcDecl", "typeSpec", "singleTypeSpec", 
		"paramDeclList", "paramList", "paramDecl", "variDecl", "variInit", "blockStmt", 
		"blockCtnt", "exprStmt", "stmt", "slctStmt", "iterStmt", "jumpStmt", "expr", 
		"creator", "constant"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'{'", "'}'", "'('", "')'", "','", "';'", "'['", "']'", "'.'", "'bool'", 
		"'int'", "'char'", "'string'", "'null'", "'void'", "'if'", "'else if'", 
		"'else'", "'for'", "'while'", "'BREAK'", "'continue'", "'return'", "'new'", 
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
			setState(47);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL) | (1L << INT) | (1L << CHAR) | (1L << STRING) | (1L << VOID) | (1L << CLASS) | (1L << Identifier))) != 0)) {
				{
				{
				setState(44);
				progSec();
				}
				}
				setState(49);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(50);
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
			setState(55);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(52);
				classDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(53);
				funcDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(54);
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
			setState(57);
			match(CLASS);
			setState(58);
			match(Identifier);
			setState(59);
			match(T__0);
			setState(63);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL) | (1L << INT) | (1L << CHAR) | (1L << STRING) | (1L << VOID) | (1L << Identifier))) != 0)) {
				{
				{
				setState(60);
				memDecl();
				}
				}
				setState(65);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(66);
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
			setState(70);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(68);
				variDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(69);
				funcDecl();
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
		enterRule(_localctx, 8, RULE_funcDecl);
		int _la;
		try {
			_localctx = new FunctionDeclarationContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			typeSpec();
			setState(73);
			match(Identifier);
			setState(74);
			match(T__2);
			setState(76);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL) | (1L << INT) | (1L << CHAR) | (1L << STRING) | (1L << VOID) | (1L << Identifier))) != 0)) {
				{
				setState(75);
				paramDeclList();
				}
			}

			setState(78);
			match(T__3);
			setState(79);
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
		enterRule(_localctx, 10, RULE_typeSpec);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			singleTypeSpec();
			setState(85);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Brackets) {
				{
				{
				setState(82);
				match(Brackets);
				}
				}
				setState(87);
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
		enterRule(_localctx, 12, RULE_singleTypeSpec);
		try {
			setState(94);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
				enterOuterAlt(_localctx, 1);
				{
				setState(88);
				((SingleTypeSpecContext)_localctx).type = match(INT);
				}
				break;
			case CHAR:
				enterOuterAlt(_localctx, 2);
				{
				setState(89);
				((SingleTypeSpecContext)_localctx).type = match(CHAR);
				}
				break;
			case BOOL:
				enterOuterAlt(_localctx, 3);
				{
				setState(90);
				((SingleTypeSpecContext)_localctx).type = match(BOOL);
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 4);
				{
				setState(91);
				((SingleTypeSpecContext)_localctx).type = match(STRING);
				}
				break;
			case VOID:
				enterOuterAlt(_localctx, 5);
				{
				setState(92);
				((SingleTypeSpecContext)_localctx).type = match(VOID);
				}
				break;
			case Identifier:
				enterOuterAlt(_localctx, 6);
				{
				setState(93);
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
		enterRule(_localctx, 14, RULE_paramDeclList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(96);
			paramDecl();
			setState(101);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(97);
				match(T__4);
				setState(98);
				paramDecl();
				}
				}
				setState(103);
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
		enterRule(_localctx, 16, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			expr(0);
			setState(109);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(105);
				match(T__4);
				setState(106);
				expr(0);
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
		enterRule(_localctx, 18, RULE_paramDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			typeSpec();
			setState(113);
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
		enterRule(_localctx, 20, RULE_variDecl);
		int _la;
		try {
			_localctx = new VariableDeclarationContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(115);
			typeSpec();
			setState(116);
			variInit();
			setState(121);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(117);
				match(T__4);
				setState(118);
				variInit();
				}
				}
				setState(123);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(124);
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
		enterRule(_localctx, 22, RULE_variInit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			match(Identifier);
			setState(129);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(127);
				match(ASSIGN);
				setState(128);
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
		enterRule(_localctx, 24, RULE_blockStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(131);
			match(T__0);
			setState(135);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__2) | (1L << T__5) | (1L << BOOL) | (1L << INT) | (1L << CHAR) | (1L << STRING) | (1L << NULL) | (1L << VOID) | (1L << IF) | (1L << FOR) | (1L << WHILE) | (1L << BREAK) | (1L << CONTINUE) | (1L << RETURN) | (1L << NEW) | (1L << ADD) | (1L << SUB) | (1L << INC) | (1L << DEC) | (1L << LOGICNOT) | (1L << BITNOT) | (1L << IntegerConst) | (1L << CharConst) | (1L << StringConst) | (1L << BoolConst) | (1L << Identifier))) != 0)) {
				{
				{
				setState(132);
				blockCtnt();
				}
				}
				setState(137);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(138);
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
		enterRule(_localctx, 26, RULE_blockCtnt);
		try {
			setState(142);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(140);
				variDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(141);
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
		enterRule(_localctx, 28, RULE_exprStmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(145);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << NULL) | (1L << NEW) | (1L << ADD) | (1L << SUB) | (1L << INC) | (1L << DEC) | (1L << LOGICNOT) | (1L << BITNOT) | (1L << IntegerConst) | (1L << CharConst) | (1L << StringConst) | (1L << BoolConst) | (1L << Identifier))) != 0)) {
				{
				setState(144);
				expr(0);
				}
			}

			setState(147);
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
		enterRule(_localctx, 30, RULE_stmt);
		try {
			setState(154);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				_localctx = new BlockStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(149);
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
				setState(150);
				exprStmt();
				}
				break;
			case IF:
				_localctx = new SelectionStatementContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(151);
				slctStmt();
				}
				break;
			case FOR:
			case WHILE:
				_localctx = new IterationStatementContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(152);
				iterStmt();
				}
				break;
			case BREAK:
			case CONTINUE:
			case RETURN:
				_localctx = new JumpStatementContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(153);
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
		enterRule(_localctx, 32, RULE_slctStmt);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(156);
			match(IF);
			setState(157);
			match(T__2);
			setState(158);
			expr(0);
			setState(159);
			match(T__3);
			setState(160);
			stmt();
			setState(169);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(161);
					match(ELIF);
					setState(162);
					match(T__2);
					setState(163);
					expr(0);
					setState(164);
					match(T__3);
					setState(165);
					stmt();
					}
					} 
				}
				setState(171);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			}
			setState(174);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(172);
				match(ELSE);
				setState(173);
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
		enterRule(_localctx, 34, RULE_iterStmt);
		int _la;
		try {
			setState(210);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				_localctx = new ForInitContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(176);
				match(FOR);
				setState(177);
				match(T__2);
				setState(178);
				((ForInitContext)_localctx).declInit = variDecl();
				setState(180);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << NULL) | (1L << NEW) | (1L << ADD) | (1L << SUB) | (1L << INC) | (1L << DEC) | (1L << LOGICNOT) | (1L << BITNOT) | (1L << IntegerConst) | (1L << CharConst) | (1L << StringConst) | (1L << BoolConst) | (1L << Identifier))) != 0)) {
					{
					setState(179);
					((ForInitContext)_localctx).cond = expr(0);
					}
				}

				setState(182);
				match(T__5);
				setState(184);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << NULL) | (1L << NEW) | (1L << ADD) | (1L << SUB) | (1L << INC) | (1L << DEC) | (1L << LOGICNOT) | (1L << BITNOT) | (1L << IntegerConst) | (1L << CharConst) | (1L << StringConst) | (1L << BoolConst) | (1L << Identifier))) != 0)) {
					{
					setState(183);
					((ForInitContext)_localctx).step = expr(0);
					}
				}

				setState(186);
				match(T__3);
				setState(187);
				stmt();
				}
				break;
			case 2:
				_localctx = new ForContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(189);
				match(FOR);
				setState(190);
				match(T__2);
				setState(192);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << NULL) | (1L << NEW) | (1L << ADD) | (1L << SUB) | (1L << INC) | (1L << DEC) | (1L << LOGICNOT) | (1L << BITNOT) | (1L << IntegerConst) | (1L << CharConst) | (1L << StringConst) | (1L << BoolConst) | (1L << Identifier))) != 0)) {
					{
					setState(191);
					((ForContext)_localctx).init = expr(0);
					}
				}

				setState(194);
				match(T__5);
				setState(196);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << NULL) | (1L << NEW) | (1L << ADD) | (1L << SUB) | (1L << INC) | (1L << DEC) | (1L << LOGICNOT) | (1L << BITNOT) | (1L << IntegerConst) | (1L << CharConst) | (1L << StringConst) | (1L << BoolConst) | (1L << Identifier))) != 0)) {
					{
					setState(195);
					((ForContext)_localctx).cond = expr(0);
					}
				}

				setState(198);
				match(T__5);
				setState(200);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << NULL) | (1L << NEW) | (1L << ADD) | (1L << SUB) | (1L << INC) | (1L << DEC) | (1L << LOGICNOT) | (1L << BITNOT) | (1L << IntegerConst) | (1L << CharConst) | (1L << StringConst) | (1L << BoolConst) | (1L << Identifier))) != 0)) {
					{
					setState(199);
					((ForContext)_localctx).step = expr(0);
					}
				}

				setState(202);
				match(T__3);
				setState(203);
				stmt();
				}
				break;
			case 3:
				_localctx = new WhileContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(204);
				match(WHILE);
				setState(205);
				match(T__2);
				setState(206);
				expr(0);
				setState(207);
				match(T__3);
				setState(208);
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

	public final JumpStmtContext jumpStmt() throws RecognitionException {
		JumpStmtContext _localctx = new JumpStmtContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_jumpStmt);
		try {
			setState(220);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BREAK:
				_localctx = new BreakContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(212);
				match(BREAK);
				setState(213);
				match(T__5);
				}
				break;
			case CONTINUE:
				_localctx = new ContinueContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(214);
				match(CONTINUE);
				setState(215);
				match(T__5);
				}
				break;
			case RETURN:
				_localctx = new ReturnContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(216);
				match(RETURN);
				setState(217);
				expr(0);
				setState(218);
				match(T__5);
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
		int _startState = 38;
		enterRecursionRule(_localctx, 38, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(237);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INC:
			case DEC:
				{
				_localctx = new PrefixIncrementDecrementContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(223);
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
				setState(224);
				expr(19);
				}
				break;
			case ADD:
			case SUB:
				{
				_localctx = new PositiveNegativeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(225);
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
				setState(226);
				expr(18);
				}
				break;
			case LOGICNOT:
			case BITNOT:
				{
				_localctx = new NotContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(227);
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
				setState(228);
				expr(17);
				}
				break;
			case NEW:
				{
				_localctx = new NewContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(229);
				((NewContext)_localctx).op = match(NEW);
				setState(230);
				creator();
				}
				break;
			case Identifier:
				{
				_localctx = new IdentifierContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(231);
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
				setState(232);
				constant();
				}
				break;
			case T__2:
				{
				_localctx = new SubExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(233);
				match(T__2);
				setState(234);
				expr(0);
				setState(235);
				match(T__3);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(293);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,27,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(291);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(239);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(240);
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
						setState(241);
						((BinaryOperationContext)_localctx).right = expr(16);
						}
						break;
					case 2:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(242);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(243);
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
						setState(244);
						((BinaryOperationContext)_localctx).right = expr(15);
						}
						break;
					case 3:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(245);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(246);
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
						setState(247);
						((BinaryOperationContext)_localctx).right = expr(14);
						}
						break;
					case 4:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(248);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(249);
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
						setState(250);
						((BinaryOperationContext)_localctx).right = expr(13);
						}
						break;
					case 5:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(251);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(252);
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
						setState(253);
						((BinaryOperationContext)_localctx).right = expr(12);
						}
						break;
					case 6:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(254);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(255);
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
						setState(256);
						((BinaryOperationContext)_localctx).right = expr(11);
						}
						break;
					case 7:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(257);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(258);
						((BinaryOperationContext)_localctx).op = match(BITAND);
						setState(259);
						((BinaryOperationContext)_localctx).right = expr(10);
						}
						break;
					case 8:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(260);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(261);
						((BinaryOperationContext)_localctx).op = match(BITXOR);
						setState(262);
						((BinaryOperationContext)_localctx).right = expr(9);
						}
						break;
					case 9:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(263);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(264);
						((BinaryOperationContext)_localctx).op = match(BITOR);
						setState(265);
						((BinaryOperationContext)_localctx).right = expr(8);
						}
						break;
					case 10:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(266);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(267);
						((BinaryOperationContext)_localctx).op = match(LOGICAND);
						setState(268);
						((BinaryOperationContext)_localctx).right = expr(7);
						}
						break;
					case 11:
						{
						_localctx = new BinaryOperationContext(new ExprContext(_parentctx, _parentState));
						((BinaryOperationContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(269);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(270);
						((BinaryOperationContext)_localctx).op = match(LOGICOR);
						setState(271);
						((BinaryOperationContext)_localctx).right = expr(6);
						}
						break;
					case 12:
						{
						_localctx = new AssignContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(272);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(273);
						((AssignContext)_localctx).op = match(ASSIGN);
						setState(274);
						expr(4);
						}
						break;
					case 13:
						{
						_localctx = new SuffixIncrementDecrementContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(275);
						if (!(precpred(_ctx, 23))) throw new FailedPredicateException(this, "precpred(_ctx, 23)");
						setState(276);
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
						setState(277);
						if (!(precpred(_ctx, 22))) throw new FailedPredicateException(this, "precpred(_ctx, 22)");
						setState(278);
						match(T__2);
						setState(280);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << NULL) | (1L << NEW) | (1L << ADD) | (1L << SUB) | (1L << INC) | (1L << DEC) | (1L << LOGICNOT) | (1L << BITNOT) | (1L << IntegerConst) | (1L << CharConst) | (1L << StringConst) | (1L << BoolConst) | (1L << Identifier))) != 0)) {
							{
							setState(279);
							paramList();
							}
						}

						setState(282);
						match(T__3);
						}
						break;
					case 15:
						{
						_localctx = new IndexAccessContext(new ExprContext(_parentctx, _parentState));
						((IndexAccessContext)_localctx).array = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(283);
						if (!(precpred(_ctx, 21))) throw new FailedPredicateException(this, "precpred(_ctx, 21)");
						setState(284);
						match(T__6);
						setState(285);
						((IndexAccessContext)_localctx).index = expr(0);
						setState(286);
						match(T__7);
						}
						break;
					case 16:
						{
						_localctx = new MemberAccessContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(288);
						if (!(precpred(_ctx, 20))) throw new FailedPredicateException(this, "precpred(_ctx, 20)");
						setState(289);
						match(T__8);
						setState(290);
						match(Identifier);
						}
						break;
					}
					} 
				}
				setState(295);
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
		enterRule(_localctx, 40, RULE_creator);
		int _la;
		try {
			int _alt;
			setState(334);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				_localctx = new CreatorErrorContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(296);
				singleTypeSpec();
				setState(301); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(297);
					match(T__6);
					setState(298);
					expr(0);
					setState(299);
					match(T__7);
					}
					}
					setState(303); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==T__6 );
				setState(306); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(305);
					match(Brackets);
					}
					}
					setState(308); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==Brackets );
				setState(314); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(310);
						match(T__6);
						setState(311);
						expr(0);
						setState(312);
						match(T__7);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(316); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 2:
				_localctx = new CreatorArrayContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(318);
				singleTypeSpec();
				setState(323); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(319);
						match(T__6);
						setState(320);
						expr(0);
						setState(321);
						match(T__7);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(325); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				setState(330);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(327);
						match(Brackets);
						}
						} 
					}
					setState(332);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
				}
				}
				break;
			case 3:
				_localctx = new CreatorSingleContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(333);
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
		enterRule(_localctx, 42, RULE_constant);
		try {
			setState(341);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IntegerConst:
				enterOuterAlt(_localctx, 1);
				{
				setState(336);
				((ConstantContext)_localctx).type = match(IntegerConst);
				}
				break;
			case CharConst:
				enterOuterAlt(_localctx, 2);
				{
				setState(337);
				((ConstantContext)_localctx).type = match(CharConst);
				}
				break;
			case StringConst:
				enterOuterAlt(_localctx, 3);
				{
				setState(338);
				((ConstantContext)_localctx).type = match(StringConst);
				}
				break;
			case BoolConst:
				enterOuterAlt(_localctx, 4);
				{
				setState(339);
				((ConstantContext)_localctx).type = match(BoolConst);
				}
				break;
			case NULL:
				enterOuterAlt(_localctx, 5);
				{
				setState(340);
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
		case 19:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3=\u015a\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\3\2\7\2\60\n\2\f\2"+
		"\16\2\63\13\2\3\2\3\2\3\3\3\3\3\3\5\3:\n\3\3\4\3\4\3\4\3\4\7\4@\n\4\f"+
		"\4\16\4C\13\4\3\4\3\4\3\5\3\5\5\5I\n\5\3\6\3\6\3\6\3\6\5\6O\n\6\3\6\3"+
		"\6\3\6\3\7\3\7\7\7V\n\7\f\7\16\7Y\13\7\3\b\3\b\3\b\3\b\3\b\3\b\5\ba\n"+
		"\b\3\t\3\t\3\t\7\tf\n\t\f\t\16\ti\13\t\3\n\3\n\3\n\7\nn\n\n\f\n\16\nq"+
		"\13\n\3\13\3\13\3\13\3\f\3\f\3\f\3\f\7\fz\n\f\f\f\16\f}\13\f\3\f\3\f\3"+
		"\r\3\r\3\r\5\r\u0084\n\r\3\16\3\16\7\16\u0088\n\16\f\16\16\16\u008b\13"+
		"\16\3\16\3\16\3\17\3\17\5\17\u0091\n\17\3\20\5\20\u0094\n\20\3\20\3\20"+
		"\3\21\3\21\3\21\3\21\3\21\5\21\u009d\n\21\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\7\22\u00aa\n\22\f\22\16\22\u00ad\13\22\3\22"+
		"\3\22\5\22\u00b1\n\22\3\23\3\23\3\23\3\23\5\23\u00b7\n\23\3\23\3\23\5"+
		"\23\u00bb\n\23\3\23\3\23\3\23\3\23\3\23\3\23\5\23\u00c3\n\23\3\23\3\23"+
		"\5\23\u00c7\n\23\3\23\3\23\5\23\u00cb\n\23\3\23\3\23\3\23\3\23\3\23\3"+
		"\23\3\23\3\23\5\23\u00d5\n\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\5\24\u00df\n\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\5\25\u00f0\n\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u011b\n\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\7\25\u0126\n\25\f\25\16\25\u0129\13\25\3\26\3\26"+
		"\3\26\3\26\3\26\6\26\u0130\n\26\r\26\16\26\u0131\3\26\6\26\u0135\n\26"+
		"\r\26\16\26\u0136\3\26\3\26\3\26\3\26\6\26\u013d\n\26\r\26\16\26\u013e"+
		"\3\26\3\26\3\26\3\26\3\26\6\26\u0146\n\26\r\26\16\26\u0147\3\26\7\26\u014b"+
		"\n\26\f\26\16\26\u014e\13\26\3\26\5\26\u0151\n\26\3\27\3\27\3\27\3\27"+
		"\3\27\5\27\u0158\n\27\3\27\2\3(\30\2\4\6\b\n\f\16\20\22\24\26\30\32\34"+
		"\36 \"$&(*,\2\n\3\2\"#\3\2\35\36\4\2--\60\60\3\2\37!\3\2./\3\2%&\3\2)"+
		"*\3\2\'(\2\u0187\2\61\3\2\2\2\49\3\2\2\2\6;\3\2\2\2\bH\3\2\2\2\nJ\3\2"+
		"\2\2\fS\3\2\2\2\16`\3\2\2\2\20b\3\2\2\2\22j\3\2\2\2\24r\3\2\2\2\26u\3"+
		"\2\2\2\30\u0080\3\2\2\2\32\u0085\3\2\2\2\34\u0090\3\2\2\2\36\u0093\3\2"+
		"\2\2 \u009c\3\2\2\2\"\u009e\3\2\2\2$\u00d4\3\2\2\2&\u00de\3\2\2\2(\u00ef"+
		"\3\2\2\2*\u0150\3\2\2\2,\u0157\3\2\2\2.\60\5\4\3\2/.\3\2\2\2\60\63\3\2"+
		"\2\2\61/\3\2\2\2\61\62\3\2\2\2\62\64\3\2\2\2\63\61\3\2\2\2\64\65\7\2\2"+
		"\3\65\3\3\2\2\2\66:\5\6\4\2\67:\5\n\6\28:\5\26\f\29\66\3\2\2\29\67\3\2"+
		"\2\298\3\2\2\2:\5\3\2\2\2;<\7\33\2\2<=\79\2\2=A\7\3\2\2>@\5\b\5\2?>\3"+
		"\2\2\2@C\3\2\2\2A?\3\2\2\2AB\3\2\2\2BD\3\2\2\2CA\3\2\2\2DE\7\4\2\2E\7"+
		"\3\2\2\2FI\5\26\f\2GI\5\n\6\2HF\3\2\2\2HG\3\2\2\2I\t\3\2\2\2JK\5\f\7\2"+
		"KL\79\2\2LN\7\5\2\2MO\5\20\t\2NM\3\2\2\2NO\3\2\2\2OP\3\2\2\2PQ\7\6\2\2"+
		"QR\5\32\16\2R\13\3\2\2\2SW\5\16\b\2TV\7\64\2\2UT\3\2\2\2VY\3\2\2\2WU\3"+
		"\2\2\2WX\3\2\2\2X\r\3\2\2\2YW\3\2\2\2Za\7\r\2\2[a\7\16\2\2\\a\7\f\2\2"+
		"]a\7\17\2\2^a\7\21\2\2_a\79\2\2`Z\3\2\2\2`[\3\2\2\2`\\\3\2\2\2`]\3\2\2"+
		"\2`^\3\2\2\2`_\3\2\2\2a\17\3\2\2\2bg\5\24\13\2cd\7\7\2\2df\5\24\13\2e"+
		"c\3\2\2\2fi\3\2\2\2ge\3\2\2\2gh\3\2\2\2h\21\3\2\2\2ig\3\2\2\2jo\5(\25"+
		"\2kl\7\7\2\2ln\5(\25\2mk\3\2\2\2nq\3\2\2\2om\3\2\2\2op\3\2\2\2p\23\3\2"+
		"\2\2qo\3\2\2\2rs\5\f\7\2st\79\2\2t\25\3\2\2\2uv\5\f\7\2v{\5\30\r\2wx\7"+
		"\7\2\2xz\5\30\r\2yw\3\2\2\2z}\3\2\2\2{y\3\2\2\2{|\3\2\2\2|~\3\2\2\2}{"+
		"\3\2\2\2~\177\7\b\2\2\177\27\3\2\2\2\u0080\u0083\79\2\2\u0081\u0082\7"+
		"$\2\2\u0082\u0084\5(\25\2\u0083\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084"+
		"\31\3\2\2\2\u0085\u0089\7\3\2\2\u0086\u0088\5\34\17\2\u0087\u0086\3\2"+
		"\2\2\u0088\u008b\3\2\2\2\u0089\u0087\3\2\2\2\u0089\u008a\3\2\2\2\u008a"+
		"\u008c\3\2\2\2\u008b\u0089\3\2\2\2\u008c\u008d\7\4\2\2\u008d\33\3\2\2"+
		"\2\u008e\u0091\5\26\f\2\u008f\u0091\5 \21\2\u0090\u008e\3\2\2\2\u0090"+
		"\u008f\3\2\2\2\u0091\35\3\2\2\2\u0092\u0094\5(\25\2\u0093\u0092\3\2\2"+
		"\2\u0093\u0094\3\2\2\2\u0094\u0095\3\2\2\2\u0095\u0096\7\b\2\2\u0096\37"+
		"\3\2\2\2\u0097\u009d\5\32\16\2\u0098\u009d\5\36\20\2\u0099\u009d\5\"\22"+
		"\2\u009a\u009d\5$\23\2\u009b\u009d\5&\24\2\u009c\u0097\3\2\2\2\u009c\u0098"+
		"\3\2\2\2\u009c\u0099\3\2\2\2\u009c\u009a\3\2\2\2\u009c\u009b\3\2\2\2\u009d"+
		"!\3\2\2\2\u009e\u009f\7\22\2\2\u009f\u00a0\7\5\2\2\u00a0\u00a1\5(\25\2"+
		"\u00a1\u00a2\7\6\2\2\u00a2\u00ab\5 \21\2\u00a3\u00a4\7\23\2\2\u00a4\u00a5"+
		"\7\5\2\2\u00a5\u00a6\5(\25\2\u00a6\u00a7\7\6\2\2\u00a7\u00a8\5 \21\2\u00a8"+
		"\u00aa\3\2\2\2\u00a9\u00a3\3\2\2\2\u00aa\u00ad\3\2\2\2\u00ab\u00a9\3\2"+
		"\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00b0\3\2\2\2\u00ad\u00ab\3\2\2\2\u00ae"+
		"\u00af\7\24\2\2\u00af\u00b1\5 \21\2\u00b0\u00ae\3\2\2\2\u00b0\u00b1\3"+
		"\2\2\2\u00b1#\3\2\2\2\u00b2\u00b3\7\25\2\2\u00b3\u00b4\7\5\2\2\u00b4\u00b6"+
		"\5\26\f\2\u00b5\u00b7\5(\25\2\u00b6\u00b5\3\2\2\2\u00b6\u00b7\3\2\2\2"+
		"\u00b7\u00b8\3\2\2\2\u00b8\u00ba\7\b\2\2\u00b9\u00bb\5(\25\2\u00ba\u00b9"+
		"\3\2\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bc\u00bd\7\6\2\2\u00bd"+
		"\u00be\5 \21\2\u00be\u00d5\3\2\2\2\u00bf\u00c0\7\25\2\2\u00c0\u00c2\7"+
		"\5\2\2\u00c1\u00c3\5(\25\2\u00c2\u00c1\3\2\2\2\u00c2\u00c3\3\2\2\2\u00c3"+
		"\u00c4\3\2\2\2\u00c4\u00c6\7\b\2\2\u00c5\u00c7\5(\25\2\u00c6\u00c5\3\2"+
		"\2\2\u00c6\u00c7\3\2\2\2\u00c7\u00c8\3\2\2\2\u00c8\u00ca\7\b\2\2\u00c9"+
		"\u00cb\5(\25\2\u00ca\u00c9\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb\u00cc\3\2"+
		"\2\2\u00cc\u00cd\7\6\2\2\u00cd\u00d5\5 \21\2\u00ce\u00cf\7\26\2\2\u00cf"+
		"\u00d0\7\5\2\2\u00d0\u00d1\5(\25\2\u00d1\u00d2\7\6\2\2\u00d2\u00d3\5 "+
		"\21\2\u00d3\u00d5\3\2\2\2\u00d4\u00b2\3\2\2\2\u00d4\u00bf\3\2\2\2\u00d4"+
		"\u00ce\3\2\2\2\u00d5%\3\2\2\2\u00d6\u00d7\7\27\2\2\u00d7\u00df\7\b\2\2"+
		"\u00d8\u00d9\7\30\2\2\u00d9\u00df\7\b\2\2\u00da\u00db\7\31\2\2\u00db\u00dc"+
		"\5(\25\2\u00dc\u00dd\7\b\2\2\u00dd\u00df\3\2\2\2\u00de\u00d6\3\2\2\2\u00de"+
		"\u00d8\3\2\2\2\u00de\u00da\3\2\2\2\u00df\'\3\2\2\2\u00e0\u00e1\b\25\1"+
		"\2\u00e1\u00e2\t\2\2\2\u00e2\u00f0\5(\25\25\u00e3\u00e4\t\3\2\2\u00e4"+
		"\u00f0\5(\25\24\u00e5\u00e6\t\4\2\2\u00e6\u00f0\5(\25\23\u00e7\u00e8\7"+
		"\32\2\2\u00e8\u00f0\5*\26\2\u00e9\u00f0\79\2\2\u00ea\u00f0\5,\27\2\u00eb"+
		"\u00ec\7\5\2\2\u00ec\u00ed\5(\25\2\u00ed\u00ee\7\6\2\2\u00ee\u00f0\3\2"+
		"\2\2\u00ef\u00e0\3\2\2\2\u00ef\u00e3\3\2\2\2\u00ef\u00e5\3\2\2\2\u00ef"+
		"\u00e7\3\2\2\2\u00ef\u00e9\3\2\2\2\u00ef\u00ea\3\2\2\2\u00ef\u00eb\3\2"+
		"\2\2\u00f0\u0127\3\2\2\2\u00f1\u00f2\f\21\2\2\u00f2\u00f3\t\5\2\2\u00f3"+
		"\u0126\5(\25\22\u00f4\u00f5\f\20\2\2\u00f5\u00f6\t\3\2\2\u00f6\u0126\5"+
		"(\25\21\u00f7\u00f8\f\17\2\2\u00f8\u00f9\t\6\2\2\u00f9\u0126\5(\25\20"+
		"\u00fa\u00fb\f\16\2\2\u00fb\u00fc\t\7\2\2\u00fc\u0126\5(\25\17\u00fd\u00fe"+
		"\f\r\2\2\u00fe\u00ff\t\b\2\2\u00ff\u0126\5(\25\16\u0100\u0101\f\f\2\2"+
		"\u0101\u0102\t\t\2\2\u0102\u0126\5(\25\r\u0103\u0104\f\13\2\2\u0104\u0105"+
		"\7\63\2\2\u0105\u0126\5(\25\f\u0106\u0107\f\n\2\2\u0107\u0108\7\62\2\2"+
		"\u0108\u0126\5(\25\13\u0109\u010a\f\t\2\2\u010a\u010b\7\61\2\2\u010b\u0126"+
		"\5(\25\n\u010c\u010d\f\b\2\2\u010d\u010e\7+\2\2\u010e\u0126\5(\25\t\u010f"+
		"\u0110\f\7\2\2\u0110\u0111\7,\2\2\u0111\u0126\5(\25\b\u0112\u0113\f\6"+
		"\2\2\u0113\u0114\7$\2\2\u0114\u0126\5(\25\6\u0115\u0116\f\31\2\2\u0116"+
		"\u0126\t\2\2\2\u0117\u0118\f\30\2\2\u0118\u011a\7\5\2\2\u0119\u011b\5"+
		"\22\n\2\u011a\u0119\3\2\2\2\u011a\u011b\3\2\2\2\u011b\u011c\3\2\2\2\u011c"+
		"\u0126\7\6\2\2\u011d\u011e\f\27\2\2\u011e\u011f\7\t\2\2\u011f\u0120\5"+
		"(\25\2\u0120\u0121\7\n\2\2\u0121\u0126\3\2\2\2\u0122\u0123\f\26\2\2\u0123"+
		"\u0124\7\13\2\2\u0124\u0126\79\2\2\u0125\u00f1\3\2\2\2\u0125\u00f4\3\2"+
		"\2\2\u0125\u00f7\3\2\2\2\u0125\u00fa\3\2\2\2\u0125\u00fd\3\2\2\2\u0125"+
		"\u0100\3\2\2\2\u0125\u0103\3\2\2\2\u0125\u0106\3\2\2\2\u0125\u0109\3\2"+
		"\2\2\u0125\u010c\3\2\2\2\u0125\u010f\3\2\2\2\u0125\u0112\3\2\2\2\u0125"+
		"\u0115\3\2\2\2\u0125\u0117\3\2\2\2\u0125\u011d\3\2\2\2\u0125\u0122\3\2"+
		"\2\2\u0126\u0129\3\2\2\2\u0127\u0125\3\2\2\2\u0127\u0128\3\2\2\2\u0128"+
		")\3\2\2\2\u0129\u0127\3\2\2\2\u012a\u012f\5\16\b\2\u012b\u012c\7\t\2\2"+
		"\u012c\u012d\5(\25\2\u012d\u012e\7\n\2\2\u012e\u0130\3\2\2\2\u012f\u012b"+
		"\3\2\2\2\u0130\u0131\3\2\2\2\u0131\u012f\3\2\2\2\u0131\u0132\3\2\2\2\u0132"+
		"\u0134\3\2\2\2\u0133\u0135\7\64\2\2\u0134\u0133\3\2\2\2\u0135\u0136\3"+
		"\2\2\2\u0136\u0134\3\2\2\2\u0136\u0137\3\2\2\2\u0137\u013c\3\2\2\2\u0138"+
		"\u0139\7\t\2\2\u0139\u013a\5(\25\2\u013a\u013b\7\n\2\2\u013b\u013d\3\2"+
		"\2\2\u013c\u0138\3\2\2\2\u013d\u013e\3\2\2\2\u013e\u013c\3\2\2\2\u013e"+
		"\u013f\3\2\2\2\u013f\u0151\3\2\2\2\u0140\u0145\5\16\b\2\u0141\u0142\7"+
		"\t\2\2\u0142\u0143\5(\25\2\u0143\u0144\7\n\2\2\u0144\u0146\3\2\2\2\u0145"+
		"\u0141\3\2\2\2\u0146\u0147\3\2\2\2\u0147\u0145\3\2\2\2\u0147\u0148\3\2"+
		"\2\2\u0148\u014c\3\2\2\2\u0149\u014b\7\64\2\2\u014a\u0149\3\2\2\2\u014b"+
		"\u014e\3\2\2\2\u014c\u014a\3\2\2\2\u014c\u014d\3\2\2\2\u014d\u0151\3\2"+
		"\2\2\u014e\u014c\3\2\2\2\u014f\u0151\5\16\b\2\u0150\u012a\3\2\2\2\u0150"+
		"\u0140\3\2\2\2\u0150\u014f\3\2\2\2\u0151+\3\2\2\2\u0152\u0158\7\65\2\2"+
		"\u0153\u0158\7\66\2\2\u0154\u0158\7\67\2\2\u0155\u0158\78\2\2\u0156\u0158"+
		"\7\20\2\2\u0157\u0152\3\2\2\2\u0157\u0153\3\2\2\2\u0157\u0154\3\2\2\2"+
		"\u0157\u0155\3\2\2\2\u0157\u0156\3\2\2\2\u0158-\3\2\2\2%\619AHNW`go{\u0083"+
		"\u0089\u0090\u0093\u009c\u00ab\u00b0\u00b6\u00ba\u00c2\u00c6\u00ca\u00d4"+
		"\u00de\u00ef\u011a\u0125\u0127\u0131\u0136\u013e\u0147\u014c\u0150\u0157";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}