package AbstractSyntaxTree;

import AbstractSyntaxTree.Nodes.*;

import java.util.ArrayList;


public class ASTPrinter
{
	public void print(ASTNode now)
	{
		System.err.printf("ID = %d ", now.nodeID);
		if(now instanceof ASTRootNode)
		{
			System.err.println("ASTRootNode");
			System.err.printf("TopScope : %d\n", ((ASTRootNode)now).scope.scopeID);
			System.err.print("progSecNode : ");
			for(ASTNode node : ((ASTRootNode) now).progSecNode)
				System.err.printf("%d ", node.nodeID);
			System.err.println("\n");
			for(ASTNode node : ((ASTRootNode) now).progSecNode)
				print(node);
		}
		else if(now instanceof ClassDeclNode)
		{
			System.err.println("ClassDeclNode");
			System.err.printf("ClassScope : %d\n", ((ClassDeclNode)now).scope.scopeID);
			System.err.printf("idNode : %s\n", ((ClassDeclNode) now).id);
			System.err.print("progSecNode : ");
			for(ASTNode node : ((ClassDeclNode) now).progSecNode)
				System.err.printf("%d ", node.nodeID);
			System.err.println("\n");
			for(ASTNode node : ((ClassDeclNode) now).progSecNode)
				print(node);
		}
		else if(now instanceof FuncDeclNode)
		{
			System.err.println("FuncDeclNode");
			System.err.printf("typeNode : %d\n", ((FuncDeclNode) now).typeNode.nodeID);
			System.err.printf("idNode : %s\n", ((FuncDeclNode) now).id);
			System.err.printf("FuncScope : %d\n", ((FuncDeclNode)now).scope.scopeID);
			if(((FuncDeclNode) now).haveParamDeclListNode)
				System.err.printf("paramDeclListNode : %d\n", ((FuncDeclNode) now).paramDeclListNode.nodeID);
			System.err.printf("blockStmtNode : %d\n", ((FuncDeclNode) now).blockStmtNode.nodeID);
			System.err.println("\n");
			print(((FuncDeclNode) now).typeNode);
			if(((FuncDeclNode) now).haveParamDeclListNode)
				print(((FuncDeclNode) now).paramDeclListNode);
			print(((FuncDeclNode) now).blockStmtNode);
		}
		else if(now instanceof ConstructorNode)
		{
			System.err.println("ConstructorNode");
			System.err.printf("ConstructorScope : %d\n", ((ConstructorNode)now).scope.scopeID);
			System.err.printf("blockStmtNode : %d\n", ((ConstructorNode) now).blockStmtNode.nodeID);
			System.err.println("\n");
			print(((ConstructorNode) now).blockStmtNode);
		}
		else if(now instanceof VariDeclNode)
		{
			System.err.println("VariDeclNode");
			System.err.printf("typeNode : %d\n", ((VariDeclNode) now).typeNode.nodeID);
			System.err.print("variInitNode : ");
			for(ASTNode node : ((VariDeclNode) now).variInitNode)
				System.err.printf("%d ", node.nodeID);
			System.err.println("\n");
			print(((VariDeclNode) now).typeNode);
			for(int i = 0; i < ((VariDeclNode) now).variInitNode.size(); i++)
				print(((VariDeclNode) now).variInitNode.get(i));
		}
		else if(now instanceof BlockStmtNode)
		{
			System.err.println("BlockStmtNode");
			System.err.printf("LocalScope : %d\n", ((BlockStmtNode)now).scope.scopeID);
			System.err.print("progSecNode : ");
			for(ASTNode node : ((BlockStmtNode) now).progSecNode)
				System.err.printf("%d ", node.nodeID);
			System.err.println("\n");
			for(ASTNode node : ((BlockStmtNode) now).progSecNode)
				print(node);
		}
		else if(now instanceof ExprStmtNode)
		{
			System.err.println("ExprStmtNode");
			System.err.printf("empty : %b\n", ((ExprStmtNode) now).empty);
			if(((ExprStmtNode) now).empty == false)
			{
				System.err.printf("exprNode : %d\n", ((ExprStmtNode) now).exprNode.nodeID);
			}
			System.err.println("\n");
			print(((ExprStmtNode) now).exprNode);
		}
		else if(now instanceof SlctStmtNode)
		{
			System.err.println("SlctStmtNode");
			System.err.printf("ifExprNode : %d\n", ((SlctStmtNode) now).ifExprNode.nodeID);
			System.err.printf("ifStmtNode : %d\n", ((SlctStmtNode) now).ifStmtNode.nodeID);
			System.err.print("elifExprNode : ");
			for(int i = 0; i < ((SlctStmtNode) now).elifExprNode.size(); i++)
				System.err.printf("%d ", ((SlctStmtNode) now).elifExprNode.get(i).nodeID);
			System.err.print("elifStmtNode : ");
			for(int i = 0; i < ((SlctStmtNode) now).elifStmtNode.size(); i++)
				System.err.printf("%d ", ((SlctStmtNode) now).elifStmtNode.get(i).nodeID);
			if(((SlctStmtNode) now).haveElse)
				System.err.printf("elseStmtNode : %d\n", ((SlctStmtNode) now).elseStmtNode.nodeID);
			System.err.println();
			print(((SlctStmtNode) now).ifExprNode);
			print(((SlctStmtNode) now).ifStmtNode);
			for(int i = 0; i < ((SlctStmtNode) now).elifExprNode.size(); i++)
				print(((SlctStmtNode) now).elifExprNode.get(i));
			for(int i = 0; i < ((SlctStmtNode) now).elifStmtNode.size(); i++)
				print(((SlctStmtNode) now).elifStmtNode.get(i));
			if(((SlctStmtNode) now).haveElse)
				print(((SlctStmtNode) now).elseStmtNode);
		}
		else if(now instanceof ForInitNode)
		{
			System.err.println("ForInitNode");
			System.err.printf("LocalScope : %d\n", ((ForInitNode)now).scope.scopeID);
			System.err.printf("variDeclNode : %d\n", ((ForInitNode) now).variDeclNode.nodeID);
			if(((ForInitNode) now).haveCond)
				System.err.printf("condExprNode : %d\n", ((ForInitNode) now).condExprNode.nodeID);
			if(((ForInitNode) now).haveStep)
				System.err.printf("stepExprNode : %d\n", ((ForInitNode) now).stepExprNode.nodeID);
			System.err.printf("stmtNode : %d\n\n", ((ForInitNode) now).stmtNode.nodeID);
			print(((ForInitNode) now).variDeclNode);
			if(((ForInitNode) now).haveCond)
				print(((ForInitNode) now).condExprNode);
			if(((ForInitNode) now).haveStep)
				print(((ForInitNode) now).stepExprNode);
			print(((ForInitNode) now).stmtNode);
		}
		else if(now instanceof ForNode)
		{
			System.err.println("ForNode");
			System.err.printf("LocalScope : %d\n", ((ForNode)now).scope.scopeID);
			if(((ForNode) now).haveInit)
				System.err.printf("initExprNode : %d\n", ((ForNode) now).initExprNode.nodeID);
			if(((ForNode) now).haveCond)
				System.err.printf("condExprNode : %d\n", ((ForNode) now).condExprNode.nodeID);
			if(((ForNode) now).haveStep)
				System.err.printf("stepExprNode : %d\n", ((ForNode) now).stepExprNode.nodeID);
			System.err.printf("stmtNode : %d\n\n", ((ForNode) now).stmtNode.nodeID);
			if(((ForNode) now).haveInit)
				print(((ForNode) now).initExprNode);
			if(((ForNode) now).haveCond)
				print(((ForNode) now).condExprNode);
			if(((ForNode) now).haveStep)
				print(((ForNode) now).stepExprNode);
			print(((ForNode) now).stmtNode);
		}
		else if(now instanceof WhileNode)
		{
			System.err.println("WhileNode");
			System.err.printf("LocalScope : %d\n", ((WhileNode)now).scope.scopeID);
			System.err.printf("exprNode : %d\n", ((WhileNode) now).exprNode.nodeID);
			System.err.printf("stmtNode : %d\n", ((WhileNode) now).stmtNode.nodeID);
			print(((WhileNode) now).exprNode);
			print(((WhileNode) now).stmtNode);
		}
		else if(now instanceof BreakNode)
		{
			System.err.println("BreakNode");
		}
		else if(now instanceof ContinueNode)
		{
			System.err.println("ContinueNode");
		}
		else if(now instanceof ReturnNode)
		{
			System.err.println("ReturnNode");
			System.err.printf("exprNode : %d\n", ((ReturnNode) now).exprNode.nodeID);
			print(((ReturnNode) now).exprNode);
		}
		else if(now instanceof CreatorArrayNode)
		{
			System.err.println("CreatorArrayNode");
			System.err.printf("singleTypeNode : %d\n", ((CreatorArrayNode) now).singleTypeNode.nodeID);
			System.err.print("exprNode : ");
			for(int i = 0; i < ((CreatorArrayNode) now).exprNode.size(); i++)
				System.err.printf("%d ", ((CreatorArrayNode) now).exprNode.get(i).nodeID);
			System.err.printf("emptyDimNum : %d\n", ((CreatorArrayNode) now).emptyDimNum);
			print(((CreatorArrayNode) now).singleTypeNode);
			for(int i = 0; i < ((CreatorArrayNode) now).exprNode.size(); i++)
				print(((CreatorArrayNode) now).exprNode.get(i));
		}
		else if(now instanceof CreatorSingleNode)
		{
			System.err.println("CreatorSingleNode");
			System.err.printf("singleTypeNode : %d\n", ((CreatorSingleNode) now).singleTypeNode.nodeID);
			print(((CreatorSingleNode) now).singleTypeNode);
		}
		else if(now instanceof TypeNode)
		{
			System.err.println("TypeNode");
			System.err.printf("singleTypeNode : %d\n", ((TypeNode) now).singleTypeNode.nodeID);
			System.err.printf("dimNum : %d\n", ((TypeNode) now).dimNum);
			print(((TypeNode) now).singleTypeNode);
		}
		else if(now instanceof SingleTypeNode)
		{
			System.err.println("SingleTypeNode");
			System.err.printf("type : %s\n", ((SingleTypeNode) now).type);
		}
		else if(now instanceof  ParamDeclListNode)
		{
			System.err.println("ParamDeclListNode");
			System.err.print("paramDeclNode : ");
			for(ASTNode node : ((ParamDeclListNode) now).paramDeclNode)
				System.err.printf("%d ", node.nodeID);
			System.err.println();
			for(ASTNode node : ((ParamDeclListNode) now).paramDeclNode)
				print(node);
		}
		else if(now instanceof ParamDeclNode)
		{
			System.err.println("ParamDeclNode");
			System.err.printf("typeNode : %d\n", ((ParamDeclNode) now).typeNode.nodeID);
			System.err.printf("idNode : %s\n", ((ParamDeclNode) now).id);
			print(((ParamDeclNode) now).typeNode);
		}
		else if(now instanceof VariInitNode)
		{
			System.err.println("VariInitNode");
			System.err.printf("idNode : %s\n", ((VariInitNode) now).id);
			if(((VariInitNode) now).assign)
				print(((VariInitNode) now).exprNode);
		}
		else if(now instanceof ParamListNode)
		{
			System.err.println("ParamListNode");
			System.err.print("exprNode : ");
			for(ASTNode node : ((ParamListNode) now).exprNode)
				System.err.printf("%d ", node.nodeID);
			System.err.println();
			for(ASTNode node : ((ParamListNode) now).exprNode)
				print(node);
		}
		else if(now instanceof SuffixIncDecNode)
		{
			System.err.println("SuffixNode");
			System.err.printf("exprNode : %d\n", ((SuffixIncDecNode) now).exprNode.nodeID);
			System.err.printf("op : %s\n\n", ((SuffixIncDecNode) now).op);
			print(((SuffixIncDecNode) now).exprNode);
		}
		else if(now instanceof FuncCallNode)
		{
			System.err.println("FuncCallNode");
			System.err.printf("exprNode : %d\n", ((FuncCallNode) now).exprNode.nodeID);
			if(((FuncCallNode) now).haveParamList)
				System.err.printf("paramListNode : %d\n", ((FuncCallNode) now).paramListNode.nodeID);
			System.err.println();
			print(((FuncCallNode) now).exprNode);
			if(((FuncCallNode) now).haveParamList)
				print(((FuncCallNode) now).paramListNode);
		}
		else if(now instanceof IndexNode)
		{
			System.err.println("IndexNode");
			System.err.printf("arrayExpr : %d\n", ((IndexNode) now).arrayExprNode.nodeID);
			System.err.printf("indexExpr : %d\n", ((IndexNode) now).indexExprNode.nodeID);
			System.err.println();
			print(((IndexNode) now).arrayExprNode);
			print(((IndexNode) now).indexExprNode);
		}
		else if(now instanceof MemberNode)
		{
			System.err.println("MemberNode");
			System.err.printf("exprNode : %d\n", ((MemberNode) now).exprNode.nodeID);
			System.err.printf("idNode : %s\n", ((MemberNode) now).idNode.nodeID);
			System.err.println();
			print(((MemberNode) now).exprNode);
			print(((MemberNode) now).idNode);
		}
		else if(now instanceof PrefixIncDecNode)
		{
			System.err.println("PrefixNode");
			System.err.printf("op = %s\n", ((PrefixIncDecNode) now).op);
			System.err.printf("exprNode = %d\n", ((PrefixIncDecNode) now).exprNode.nodeID);
			System.err.println();
			print(((PrefixIncDecNode) now).exprNode);
		}
		else if(now instanceof PosNegNode)
		{
			System.err.println("PosNegNode");
			System.err.printf("op = %s\n", ((PosNegNode) now).op);
			System.err.printf("exprNode = %d\n", ((PosNegNode) now).exprNode.nodeID);
			System.err.println();
			print(((PosNegNode) now).exprNode);
		}
		else if(now instanceof NotNode)
		{
			System.err.println("NotNode");
			System.err.printf("op = %s\n", ((NotNode) now).op);
			System.err.printf("exprNode = %d\n", ((NotNode) now).exprNode.nodeID);
			System.err.println();
			print(((NotNode) now).exprNode);
		}
		else if(now instanceof NewNode)
		{
			System.err.println("NewNode");
			System.err.printf("op = %s\n", ((NewNode) now).op);
			System.err.printf("creatorNode = %d\n", ((NewNode) now).creatorNode.nodeID);
			System.err.println();
			print(((NewNode) now).creatorNode);
		}
		else if(now instanceof BinaryNode)
		{
			System.err.println("BinaryNode");
			System.err.printf("leftExpr : %d\n", ((BinaryNode) now).leftExprNode.nodeID);
			System.err.printf("op : %s\n", ((BinaryNode) now).op);
			System.err.printf("rightExpr : %d\n", ((BinaryNode) now).rightExprNode.nodeID);
			System.err.println();
			print(((BinaryNode) now).leftExprNode);
			print(((BinaryNode) now).rightExprNode);
		}
		else if(now instanceof AssignNode)
		{
			System.err.println("AssignNode");
			System.err.printf("left : %d\n", ((AssignNode) now).leftExprNode.nodeID);
			System.err.printf("right : %d\n", ((AssignNode) now).rightExprNode.nodeID);
			System.err.println();
			print(((AssignNode) now).leftExprNode);
			print(((AssignNode) now).rightExprNode);
		}

		else if(now instanceof IdNode)
		{
			System.err.println("IdNode");
			System.err.printf("id : %s\n", ((IdNode) now).id);
			System.err.println();
		}

		else if(now instanceof ConstNode)
		{
			System.err.println("ConstNode");
			System.err.printf("type = %s\n\n", ((ConstNode) now).type);
		}
		else if(now instanceof SubExprNode)
		{
			System.err.println("SubExprNode");
			System.err.printf("exprNode : %d\n", ((SubExprNode) now).exprNode.nodeID);
			System.err.println();
			print(((SubExprNode) now).exprNode);
		}
	}
}
