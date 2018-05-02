package AbstractSyntaxTree;

import AbstractSyntaxTree.Nodes.*;
import MxGrammar.MxBaseVisitor;
import MxGrammar.MxParser;

public class ASTMaker extends MxBaseVisitor<ASTNode>
{
	public static boolean print = false;

	@Override public ASTRootNode visitProg(MxParser.ProgContext ctx)
	{
		if(print)
		{
			System.out.println("ASTRootNode");
		}
		ASTRootNode root = new ASTRootNode();
		int progNum = ctx.progSec().size();
		for(int i = 0; i < progNum; i++)
		{
			root.progSecNode.add(visit(ctx.progSec(i)));
		}
		return root;
	}

	@Override public ASTNode visitProgSec(MxParser.ProgSecContext ctx)
	{
		return visitChildren(ctx);
	}

	@Override public ClassDeclNode visitClassDeclaration(MxParser.ClassDeclarationContext ctx)
	{
		if(print)
		{
			System.out.println("ClassDeclNode : " + ctx.Identifier().getText());
		}
		ClassDeclNode classDecl = new ClassDeclNode();
		classDecl.id = ctx.Identifier().getText();
		int progNum = ctx.memDecl().size();
		for(int i = 0; i < progNum; i++)
		{
			classDecl.progSecNode.add(visit(ctx.memDecl(i)));
		}
		return classDecl;
	}

	@Override public ASTNode visitMemDecl(MxParser.MemDeclContext ctx) { return visitChildren(ctx); }

	@Override public FuncDeclNode visitFunctionDeclaration(MxParser.FunctionDeclarationContext ctx)
	{
		if(print)
		{
			System.out.println("FuncDeclNode : " + ctx.Identifier().getText());
		}
		FuncDeclNode funcDecl = new FuncDeclNode();
		funcDecl.typeNode = (TypeNode)visit(ctx.typeSpec());
		funcDecl.id = ctx.Identifier().getText();
		if(ctx.paramDeclList() != null)
		{
			funcDecl.haveParamDeclListNode = true;
			funcDecl.paramDeclListNode = (ParamDeclListNode)visit(ctx.paramDeclList());
		}
		funcDecl.blockStmtNode = (BlockStmtNode)visit(ctx.blockStmt());
		return funcDecl;
	}

	@Override public TypeNode visitTypeSpec(MxParser.TypeSpecContext ctx)
	{
		if(print)
		{
			System.out.print("TypeNode : dimNum = ");
			System.out.println(ctx.Brackets().size());
		}
		TypeNode type = new TypeNode();
		type.singleTypeNode = (SingleTypeNode) visit(ctx.singleTypeSpec());
		type.dimNum = ctx.Brackets().size();
		return type;
	}


	@Override public SingleTypeNode visitSingleTypeSpec(MxParser.SingleTypeSpecContext ctx)
	{
		if(print)
		{
			System.out.println("SingleTypeNode : " + ctx.type.getText());
		}
		SingleTypeNode singleType = new SingleTypeNode();
		singleType.type = ctx.type.getText();
		return singleType;
	}

	@Override public ParamDeclListNode visitParamDeclList(MxParser.ParamDeclListContext ctx)
	{
		if(print)
		{
			System.out.print("ParamDeclListNode : paramNum = ");
			System.out.println(ctx.paramDecl().size());
		}
		ParamDeclListNode paramDeclList = new ParamDeclListNode();
		int paramNum = ctx.paramDecl().size();
		for(int i = 0; i < paramNum; i++)
			paramDeclList.paramDeclNode.add((ParamDeclNode)visit(ctx.paramDecl(i)));
		return paramDeclList;
	}

	@Override public ParamListNode visitParamList(MxParser.ParamListContext ctx)
	{
		if(print)
		{
			System.out.print("ParamListNode : paramNum = ");
			System.out.println(ctx.expr().size());
		}
		ParamListNode paramList = new ParamListNode();
		int paramNum = ctx.expr().size();
		for(int i = 0; i < paramNum; i++)
			paramList.exprNode.add(visit(ctx.expr(i)));
		return paramList;
	}

	@Override public ParamDeclNode visitParamDecl(MxParser.ParamDeclContext ctx)
	{
		if(print)
		{
			System.out.println("ParamDeclNode : " + ctx.Identifier().getText());
		}
		ParamDeclNode paramDecl = new ParamDeclNode();
		paramDecl.typeNode = (TypeNode)visit(ctx.typeSpec());
		paramDecl.id = ctx.Identifier().getText();
		return paramDecl;
	}

	@Override public VariDeclNode visitVariableDeclaration(MxParser.VariableDeclarationContext ctx)
	{
		if(print)
		{
			System.out.print("VariDeclNode : variNum = ");
			System.out.println(ctx.variInit().size());
		}
		VariDeclNode variDecl = new VariDeclNode();
		variDecl.typeNode = (TypeNode)visit(ctx.typeSpec());
		int variNum = ctx.variInit().size();
		for(int i = 0; i < variNum; i++)
			variDecl.variInitNode.add(visit(ctx.variInit(i)));
		return variDecl;
	}

	@Override public VariInitNode visitVariInit(MxParser.VariInitContext ctx)
	{
		if(print)
		{
			System.out.println("VariInitNode : " + ctx.Identifier().getText());
		}
		VariInitNode variInit = new VariInitNode();
		variInit.id = ctx.Identifier().getText();

		if(ctx.expr() != null)
		{
			variInit.assign = true;
			variInit.exprNode = (ExprNode)visit(ctx.expr());
		}
		return variInit;
	}

	@Override public BlockStmtNode visitBlockStmt(MxParser.BlockStmtContext ctx)
	{
		if(print)
		{
			System.out.print("BlockStmtNode : blockNum = ");
			System.out.println(ctx.blockCtnt().size());
		}
		BlockStmtNode blockStmt = new BlockStmtNode();
		int blockNum = ctx.blockCtnt().size();
		for(int i = 0; i < blockNum; i++)
			blockStmt.progSecNode.add(visit(ctx.blockCtnt(i)));
		return blockStmt;
	}

	@Override public ASTNode visitBlockCtnt(MxParser.BlockCtntContext ctx) { return visitChildren(ctx); }

	@Override public ExprStmtNode visitExprStmt(MxParser.ExprStmtContext ctx)
	{
		if(print)
		{
			System.out.println("ExprStmtNode");
		}
		ExprStmtNode exprStmt = new ExprStmtNode();
		if(ctx.expr() != null)
		{
			exprStmt.empty = false;
			exprStmt.exprNode = (ExprNode)visit(ctx.expr());
		}
		return exprStmt;
	}

	@Override public ASTNode visitBlockStatement(MxParser.BlockStatementContext ctx) { return visitChildren(ctx); }

	@Override public ASTNode visitExpressionStatement(MxParser.ExpressionStatementContext ctx) { return visitChildren(ctx); }

	@Override public ASTNode visitSelectionStatement(MxParser.SelectionStatementContext ctx) { return visitChildren(ctx); }

	@Override public ASTNode visitIterationStatement(MxParser.IterationStatementContext ctx) { return visitChildren(ctx); }

	@Override public ASTNode visitJumpStatement(MxParser.JumpStatementContext ctx) { return visitChildren(ctx); }

	@Override public SlctStmtNode visitSlctStmt(MxParser.SlctStmtContext ctx)
	{
		if(print)
		{
			System.out.print("SlctStmtNode : exprNum, stmtNum = ");
			System.out.print(ctx.expr().size());
			System.out.printf(" %d\n", ctx.stmt().size());
		}
		SlctStmtNode slctStmt = new SlctStmtNode();
		slctStmt.ifExprNode = (ExprNode)visit(ctx.expr(0));
		slctStmt.ifStmtNode = (StmtNode)visit(ctx.stmt(0));
		int exprNum = ctx.expr().size();
		int stmtNum = ctx.stmt().size();
		if(exprNum + 1 == stmtNum)
		{
			slctStmt.haveElse = true;
			slctStmt.elseStmtNode = (StmtNode)visit(ctx.stmt(stmtNum - 1));
		}
		for(int i = 1; i < exprNum; i++)
		{
			slctStmt.elifExprNode.add(visit(ctx.expr(i)));
			slctStmt.elifStmtNode.add(visit(ctx.stmt(i)));
		}
		return slctStmt;
	}

	@Override public ForInitNode visitForInit(MxParser.ForInitContext ctx)
	{
		if(print)
		{
			System.out.println("ForInitNode");
		}
		ForInitNode forInit = new ForInitNode();
		forInit.variDeclNode = (VariDeclNode)visit(ctx.declInit);
		if(ctx.cond != null)
		{
			forInit.haveCond = true;
			forInit.condExprNode = (ExprNode)visit(ctx.cond);
		}
		if(ctx.step != null)
		{
			forInit.haveStep = true;
			forInit.stepExprNode = (ExprNode)visit(ctx.step);
		}
		forInit.stmtNode = (StmtNode)visit(ctx.stmt());
		return forInit;
	}

	@Override public ForNode visitFor(MxParser.ForContext ctx)
	{
		if(print)
		{
			System.out.println("ForNode");
		}
		ForNode forNode = new ForNode();
		if(ctx.init != null)
		{
			forNode.haveInit = true;
			forNode.initExprNode = (ExprNode)visit(ctx.init);
		}
		if(ctx.cond != null)
		{
			forNode.haveCond = true;
			forNode.condExprNode = (ExprNode)visit(ctx.cond);
		}
		if(ctx.step != null)
		{
			forNode.haveStep = true;
			forNode.stepExprNode = (ExprNode)visit(ctx.step);
		}
		forNode.stmtNode = (StmtNode)visit(ctx.stmt());
		return forNode;
	}

	@Override public WhileNode visitWhile(MxParser.WhileContext ctx)
	{
		if(print)
		{
			System.out.println("WhileNode");
		}
		WhileNode whileNode = new WhileNode();
		whileNode.exprNode = (ExprNode)visit(ctx.expr());
		whileNode.stmtNode = (StmtNode)visit(ctx.stmt());
		return whileNode;
	}

	@Override public BreakNode visitBreak(MxParser.BreakContext ctx)
	{
		if(print)
		{
			System.out.println("BreakNode");
		}
		BreakNode breakNode = new BreakNode();
		return breakNode;
	}

	@Override public ContinueNode visitContinue(MxParser.ContinueContext ctx)
	{
		if(print)
		{
			System.out.println("ContinueNode");
		}
		ContinueNode continueNode = new ContinueNode();
		return continueNode;
	}

	@Override public ReturnNode visitReturn(MxParser.ReturnContext ctx)
	{
		if(print)
		{
			System.out.println("ReturnNode : " + ctx.expr().getText());
		}
		ReturnNode returnNode = new ReturnNode();
		returnNode.exprNode = (ExprNode)visit(ctx.expr());
		return returnNode;
	}

	@Override public StraightReturnNode visitStrtReturn(MxParser.StrtReturnContext ctx)
	{
		StraightReturnNode straightReturnNode = new StraightReturnNode();
		return straightReturnNode;
	}

	@Override public NewNode visitNew(MxParser.NewContext ctx)
	{
		if(print)
		{
			System.out.println("NewNode");
		}
		NewNode newNode = new NewNode();
		newNode.op = "new";
		newNode.creatorNode = (CreatorNode)visit(ctx.creator());
		return newNode;
	}


	@Override public IdNode visitIdentifier(MxParser.IdentifierContext ctx)
	{
		if(print)
		{
			System.out.println("IdNode : " + ctx.Identifier().getText());
		}
		IdNode idNode = new IdNode();
		idNode.id = ctx.Identifier().getText();
		return idNode;
	}

	@Override public MemberNode visitMemberAccess(MxParser.MemberAccessContext ctx)
	{
		if(print)
		{
			System.out.println("MemberNode : " + ctx.Identifier().getText());
		}
		MemberNode member = new MemberNode();
		member.exprNode = (ExprNode)visit(ctx.expr());
		IdNode idNodeTemp = new IdNode();
		idNodeTemp.id = ctx.Identifier().getText();
		member.idNode = idNodeTemp;
		return member;
	}

	@Override public ASTNode visitConst(MxParser.ConstContext ctx) { return visitChildren(ctx); }

	@Override public SuffixIncDecNode visitSuffixIncrementDecrement(MxParser.SuffixIncrementDecrementContext ctx)
	{
		if(print)
		{
			System.out.println("SuffixIncDecNode");
		}
		SuffixIncDecNode suffix = new SuffixIncDecNode();
		suffix.op = ctx.op.getText();
		suffix.exprNode = (ExprNode)visit(ctx.expr());
		return suffix;
	}

	@Override public PrefixIncDecNode visitPrefixIncrementDecrement(MxParser.PrefixIncrementDecrementContext ctx)
	{
		if(print)
		{
			System.out.println("PrefixIncDecNode");
		}
		PrefixIncDecNode prefix = new PrefixIncDecNode();
		prefix.op = ctx.op.getText();
		prefix.exprNode = (ExprNode)visit(ctx.expr());
		return prefix;
	}

	@Override public BinaryNode visitBinaryOperation(MxParser.BinaryOperationContext ctx)
	{
		if(print)
		{
			System.out.println("BinaryNode : " + ctx.op.getText());
		}
		BinaryNode binary = new BinaryNode();
		binary.op = ctx.op.getText();
		binary.leftExprNode = (ExprNode)visit(ctx.expr(0));
		binary.rightExprNode = (ExprNode)visit(ctx.expr(1));
		return binary;
	}

	@Override public IndexNode visitIndexAccess(MxParser.IndexAccessContext ctx)
	{
		if(print)
		{
			System.out.println("IndexNode");
		}
		IndexNode index = new IndexNode();
		index.arrayExprNode = (ExprNode)visit(ctx.expr(0));
		index.indexExprNode = (ExprNode)visit(ctx.expr(1));
		return index;
	}

	@Override public NotNode visitNot(MxParser.NotContext ctx)
	{
		if(print)
		{
			System.out.println("NotNode");
		}
		NotNode not = new NotNode();
		not.op = ctx.op.getText();
		not.exprNode = (ExprNode)visit(ctx.expr());
		return not;
	}

	@Override public PosNegNode visitPositiveNegative(MxParser.PositiveNegativeContext ctx)
	{
		if(print)
		{
			System.out.println("PosNegNode");
		}
		PosNegNode posNeg = new PosNegNode();
		posNeg.op = ctx.op.getText();
		posNeg.exprNode = (ExprNode)visit(ctx.expr());
		return posNeg;
	}

	@Override public FuncCallNode visitFunctionCall(MxParser.FunctionCallContext ctx)
	{
		if(print)
		{
			System.out.println("FuncCallNode");
		}
		FuncCallNode funcCall = new FuncCallNode();
		funcCall.exprNode = (ExprNode)visit(ctx.expr());
		if(ctx.paramList() != null)
		{
			funcCall.haveParamList = true;
			funcCall.paramListNode = (ParamListNode)visit(ctx.paramList());
		}
		return funcCall;
	}

	@Override public SubExprNode visitSubExpression(MxParser.SubExpressionContext ctx)
	{
		if(print)
		{
			System.out.println("SubExprNode");
		}
		SubExprNode subExpr = new SubExprNode();
		subExpr.exprNode = (ExprNode)visit(ctx.expr());
		return subExpr;
	}

	@Override public AssignNode visitAssign(MxParser.AssignContext ctx)
	{
		if(print)
		{
			System.out.println("AssignNode");
		}
		AssignNode assign = new AssignNode();
		assign.leftExprNode = (ExprNode)visit(ctx.expr(0));
		assign.rightExprNode = (ExprNode)visit(ctx.expr(1));
		return assign;
	}

	@Override public ASTNode visitCreatorError(MxParser.CreatorErrorContext ctx)
	{
		if(print)
		{
			System.out.println("CreatorError");
		}
		throw new RuntimeException();
	}

	@Override public CreatorArrayNode visitCreatorArray(MxParser.CreatorArrayContext ctx)
	{
		if(print)
		{
			System.out.println("CreatorArrayNode");
		}
		CreatorArrayNode creatorArray = new CreatorArrayNode();
		creatorArray.singleTypeNode = (SingleTypeNode)visit(ctx.singleTypeSpec());
		int exprNum = ctx.expr().size();
		for(int i = 0; i < exprNum; i++)
			creatorArray.exprNode.add(visit(ctx.expr(i)));
		if(ctx.Brackets() != null)
			creatorArray.emptyDimNum = ctx.Brackets().size();
		return creatorArray;
	}

	@Override public CreatorSingleNode visitCreatorSingle(MxParser.CreatorSingleContext ctx)
	{
		if(print)
		{
			System.out.println("CreatorSingleNode");
		}
		CreatorSingleNode creatorSingle = new CreatorSingleNode();
		creatorSingle.singleTypeNode = (SingleTypeNode)visit(ctx.singleTypeSpec());
		return creatorSingle;
	}

	@Override public ConstNode visitConstant(MxParser.ConstantContext ctx)
	{
		if(print)
		{
			System.out.println("ConstNode : " + ctx.type.getText());
		}
		ConstNode constNode = new ConstNode();
		String mid = ctx.type.getText();
		if(mid.equals("true") || mid.equals("false")) {
			constNode.type = "bool";
		}
		else if(mid.equals("null"))
		{
			constNode.type = "null";
		}
		else if(mid.length() >= 2 && mid.substring(0,1).equals("\"")) {
			constNode.type = "string";
		}
		else {
			constNode.type = "int";
		}
		return constNode;
	}

	@Override public ConstructorNode visitConstructorDeclaration(MxParser.ConstructorDeclarationContext ctx)
	{
		ConstructorNode constructorNode = new ConstructorNode();
		constructorNode.id = ctx.Identifier().getText();
		constructorNode.blockStmtNode = (BlockStmtNode) visit(ctx.blockStmt());
		return constructorNode;
	}

}
