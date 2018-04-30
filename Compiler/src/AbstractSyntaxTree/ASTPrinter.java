package AbstractSyntaxTree;

import AbstractSyntaxTree.Nodes.*;

import java.util.ArrayList;


public class ASTPrinter
{
	public void print(ASTNode now)
	{
		System.out.printf("ID = %d ", now.nodeID);
		if(now instanceof ASTRootNode)
		{
			System.out.println("ASTRootNode");
			System.out.print("progSecNode : ");
			for(ASTNode node : ((ASTRootNode) now).progSecNode)
				System.out.printf("%d ", node.nodeID);
			System.out.println("\n");
			for(ASTNode node : ((ASTRootNode) now).progSecNode)
				print(node);
		}
		else if(now instanceof ClassDeclNode)
		{
			System.out.println("ClassDeclNode");
			System.out.printf("idNode : %s\n", ((ClassDeclNode) now).id);
			System.out.print("progSecNode : ");
			for(ASTNode node : ((ClassDeclNode) now).progSecNode)
				System.out.printf("%d ", node.nodeID);
			System.out.println("\n");
			for(ASTNode node : ((ClassDeclNode) now).progSecNode)
				print(node);
		}
		else if(now instanceof FuncDeclNode)
		{
			System.out.println("FuncDeclNode");
			System.out.printf("typeNode : %d\n", ((FuncDeclNode) now).typeNode.nodeID);
			System.out.printf("idNode : %s\n", ((FuncDeclNode) now).id);
			if(((FuncDeclNode) now).haveParamDeclListNode)
				System.out.printf("paramDeclListNode : %d\n", ((FuncDeclNode) now).paramDeclListNode.nodeID);
			System.out.printf("blockStmtNode : %d\n", ((FuncDeclNode) now).blockStmtNode.nodeID);
			System.out.println("\n");
			print(((FuncDeclNode) now).typeNode);
			if(((FuncDeclNode) now).haveParamDeclListNode)
				print(((FuncDeclNode) now).paramDeclListNode);
			print(((FuncDeclNode) now).blockStmtNode);
		}
		else if(now instanceof VariDeclNode)
		{
			System.out.println("VariDeclNode");
			System.out.printf("typeNode : %d\n", ((VariDeclNode) now).typeNode.nodeID);
			System.out.print("variInitNode : ");
			for(ASTNode node : ((VariDeclNode) now).variInitNode)
				System.out.printf("%d ", node.nodeID);
			System.out.println("\n");
			print(((VariDeclNode) now).typeNode);
			for(int i = 0; i < ((VariDeclNode) now).variInitNode.size(); i++)
				print(((VariDeclNode) now).variInitNode.get(i));
		}
		else if(now instanceof BlockStmtNode)
		{
			System.out.println("BlockStmtNode");
			System.out.print("progSecNode : ");
			for(ASTNode node : ((BlockStmtNode) now).progSecNode)
				System.out.printf("%d ", node.nodeID);
			System.out.println("\n");
			for(ASTNode node : ((BlockStmtNode) now).progSecNode)
				print(node);
		}
		else if(now instanceof ExprStmtNode)
		{
			System.out.println("ExprStmtNode");
			System.out.printf("empty : %b\n", ((ExprStmtNode) now).empty);
			if(((ExprStmtNode) now).empty == false)
			{
				System.out.printf("exprNode : %d\n", ((ExprStmtNode) now).exprNode.nodeID);
			}
			System.out.println("\n");
			print(((ExprStmtNode) now).exprNode);
		}
		else if(now instanceof SlctStmtNode)
		{
			System.out.println("SlctStmtNode");
			System.out.printf("ifExprNode : %d\n", ((SlctStmtNode) now).ifExprNode.nodeID);
			System.out.printf("ifStmtNode : %d\n", ((SlctStmtNode) now).ifStmtNode.nodeID);
			System.out.print("elifExprNode : ");
			for(int i = 0; i < ((SlctStmtNode) now).elifExprNode.size(); i++)
				System.out.printf("%d ", ((SlctStmtNode) now).elifExprNode.get(i).nodeID);
			System.out.print("elifStmtNode : ");
			for(int i = 0; i < ((SlctStmtNode) now).elifStmtNode.size(); i++)
				System.out.printf("%d ", ((SlctStmtNode) now).elifStmtNode.get(i).nodeID);
			if(((SlctStmtNode) now).haveElse)
				System.out.printf("elseStmtNode : %d\n", ((SlctStmtNode) now).elseStmtNode.nodeID);
			System.out.println();
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
			System.out.println("ForInitNode");
			System.out.printf("variDeclNode : %d\n", ((ForInitNode) now).variDeclNode.nodeID);
			if(((ForInitNode) now).haveCond)
				System.out.printf("condExprNode : %d\n", ((ForInitNode) now).condExprNode.nodeID);
			if(((ForInitNode) now).haveStep)
				System.out.printf("stepExprNode : %d\n", ((ForInitNode) now).stepExprNode.nodeID);
			System.out.printf("stmtNode : %d\n\n", ((ForInitNode) now).stmtNode.nodeID);
			print(((ForInitNode) now).variDeclNode);
			if(((ForInitNode) now).haveCond)
				print(((ForInitNode) now).condExprNode);
			if(((ForInitNode) now).haveStep)
				print(((ForInitNode) now).stepExprNode);
			print(((ForInitNode) now).stmtNode);
		}
		else if(now instanceof ForNode)
		{
			System.out.println("ForNode");
			if(((ForNode) now).haveInit)
				System.out.printf("initExprNode : %d\n", ((ForNode) now).initExprNode.nodeID);
			if(((ForNode) now).haveCond)
				System.out.printf("condExprNode : %d\n", ((ForNode) now).condExprNode.nodeID);
			if(((ForNode) now).haveStep)
				System.out.printf("stepExprNode : %d\n", ((ForNode) now).stepExprNode.nodeID);
			System.out.printf("stmtNode : %d\n\n", ((ForNode) now).stmtNode.nodeID);
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
			System.out.println("WhileNode");
			System.out.printf("exprNode : %d\n", ((WhileNode) now).exprNode.nodeID);
			System.out.printf("stmtNode : %d\n", ((WhileNode) now).stmtNode.nodeID);
			print(((WhileNode) now).exprNode);
			print(((WhileNode) now).stmtNode);
		}
		else if(now instanceof BreakNode)
		{
			System.out.println("BreakNode");
		}
		else if(now instanceof ContinueNode)
		{
			System.out.println("ContinueNode");
		}
		else if(now instanceof ReturnNode)
		{
			System.out.println("ReturnNode");
			System.out.printf("exprNode : %d\n", ((ReturnNode) now).exprNode.nodeID);
			print(((ReturnNode) now).exprNode);
		}
		else if(now instanceof CreatorArrayNode)
		{
			System.out.println("CreatorArrayNode");
			System.out.printf("singleTypeNode : %d\n", ((CreatorArrayNode) now).singleTypeNode.nodeID);
			System.out.print("exprNode : ");
			for(int i = 0; i < ((CreatorArrayNode) now).exprNode.size(); i++)
				System.out.printf("%d ", ((CreatorArrayNode) now).exprNode.get(i).nodeID);
			System.out.printf("emptyDimNum : %d\n", ((CreatorArrayNode) now).emptyDimNum);
			print(((CreatorArrayNode) now).singleTypeNode);
			for(int i = 0; i < ((CreatorArrayNode) now).exprNode.size(); i++)
				print(((CreatorArrayNode) now).exprNode.get(i));
		}
		else if(now instanceof CreatorSingleNode)
		{
			System.out.println("CreatorSingleNode");
			System.out.printf("singleTypeNode : %d\n", ((CreatorSingleNode) now).singleTypeNode.nodeID);
			print(((CreatorSingleNode) now).singleTypeNode);
		}
		else if(now instanceof TypeNode)
		{
			System.out.println("TypeNode");
			System.out.printf("singleTypeNode : %d\n", ((TypeNode) now).singleTypeNode.nodeID);
			System.out.printf("dimNum : %d\n", ((TypeNode) now).dimNum);
			print(((TypeNode) now).singleTypeNode);
		}
		else if(now instanceof SingleTypeNode)
		{
			System.out.println("SingleTypeNode");
			System.out.printf("type : %s\n", ((SingleTypeNode) now).type);
		}
		else if(now instanceof  ParamDeclListNode)
		{
			System.out.println("ParamDeclListNode");
			System.out.print("paramDeclNode : ");
			for(ASTNode node : ((ParamDeclListNode) now).paramDeclNode)
				System.out.printf("%d ", node.nodeID);
			System.out.println();
			for(ASTNode node : ((ParamDeclListNode) now).paramDeclNode)
				print(node);
		}
		else if(now instanceof ParamDeclNode)
		{
			System.out.println("ParamDeclNode");
			System.out.printf("typeNode : %d\n", ((ParamDeclNode) now).typeNode.nodeID);
			System.out.printf("idNode : %s\n", ((ParamDeclNode) now).id);
			print(((ParamDeclNode) now).typeNode);
		}
		else if(now instanceof VariInitNode)
		{
			System.out.println("VariInitNode");
			System.out.printf("idNode : %s\n", ((VariInitNode) now).id);
			if(((VariInitNode) now).assign)
				print(((VariInitNode) now).exprNode);
		}
		else if(now instanceof ParamListNode)
		{
			System.out.println("ParamListNode");
			System.out.print("exprNode : ");
			for(ASTNode node : ((ParamListNode) now).exprNode)
				System.out.printf("%d ", node.nodeID);
			System.out.println();
			for(ASTNode node : ((ParamListNode) now).exprNode)
				print(node);
		}
		else if(now instanceof SuffixIncDecNode)
		{
			System.out.println("SuffixNode");
			System.out.printf("exprNode : %d\n", ((SuffixIncDecNode) now).exprNode.nodeID);
			System.out.printf("op : %s\n\n", ((SuffixIncDecNode) now).op);
			print(((SuffixIncDecNode) now).exprNode);
		}
		else if(now instanceof FuncCallNode)
		{
			System.out.println("FuncCallNode");
			System.out.printf("exprNode : %d\n", ((FuncCallNode) now).exprNode.nodeID);
			if(((FuncCallNode) now).haveParamList)
				System.out.printf("paramListNode : %d\n", ((FuncCallNode) now).paramListNode.nodeID);
			System.out.println();
			print(((FuncCallNode) now).exprNode);
			if(((FuncCallNode) now).haveParamList)
				print(((FuncCallNode) now).paramListNode);
		}
		else if(now instanceof IndexNode)
		{
			System.out.println("IndexNode");
			System.out.printf("arrayExpr : %d\n", ((IndexNode) now).arrayExprNode.nodeID);
			System.out.printf("indexExpr : %d\n", ((IndexNode) now).indexExprNode.nodeID);
			System.out.println();
			print(((IndexNode) now).arrayExprNode);
			print(((IndexNode) now).indexExprNode);
		}
		else if(now instanceof MemberNode)
		{
			System.out.println("MemberNode");
			System.out.printf("exprNode : %d\n", ((MemberNode) now).exprNode.nodeID);
			System.out.printf("idNode : %s\n", ((MemberNode) now).idNode.nodeID);
			System.out.println();
			print(((MemberNode) now).exprNode);
			print(((MemberNode) now).idNode);
		}
		else if(now instanceof PrefixIncDecNode)
		{
			System.out.println("PrefixNode");
			System.out.printf("op = %s\n", ((PrefixIncDecNode) now).op);
			System.out.printf("exprNode = %d\n", ((PrefixIncDecNode) now).exprNode.nodeID);
			System.out.println();
			print(((PrefixIncDecNode) now).exprNode);
		}
		else if(now instanceof PosNegNode)
		{
			System.out.println("PosNegNode");
			System.out.printf("op = %s\n", ((PosNegNode) now).op);
			System.out.printf("exprNode = %d\n", ((PosNegNode) now).exprNode.nodeID);
			System.out.println();
			print(((PosNegNode) now).exprNode);
		}
		else if(now instanceof NotNode)
		{
			System.out.println("NotNode");
			System.out.printf("op = %s\n", ((NotNode) now).op);
			System.out.printf("exprNode = %d\n", ((NotNode) now).exprNode.nodeID);
			System.out.println();
			print(((NotNode) now).exprNode);
		}
		else if(now instanceof NewNode)
		{
			System.out.println("NewNode");
			System.out.printf("op = %s\n", ((NewNode) now).op);
			System.out.printf("creatorNode = %d\n", ((NewNode) now).creatorNode.nodeID);
			System.out.println();
			print(((NewNode) now).creatorNode);
		}
		else if(now instanceof BinaryNode)
		{
			System.out.println("BinaryNode");
			System.out.printf("leftExpr : %d\n", ((BinaryNode) now).leftExprNode.nodeID);
			System.out.printf("op : %s\n", ((BinaryNode) now).op);
			System.out.printf("rightExpr : %d\n", ((BinaryNode) now).rightExprNode.nodeID);
			System.out.println();
			print(((BinaryNode) now).leftExprNode);
			print(((BinaryNode) now).rightExprNode);
		}
		else if(now instanceof AssignNode)
		{
			System.out.println("AssignNode");
			System.out.printf("left : %d\n", ((AssignNode) now).leftExprNode.nodeID);
			System.out.printf("right : %d\n", ((AssignNode) now).rightExprNode.nodeID);
			System.out.println();
			print(((AssignNode) now).leftExprNode);
			print(((AssignNode) now).rightExprNode);
		}

		else if(now instanceof IdNode)
		{
			System.out.println("IdNode");
			System.out.printf("id : %s\n", ((IdNode) now).id);
			System.out.println();
		}

		else if(now instanceof ConstNode)
		{
			System.out.println("ConstNode");
			System.out.printf("type = %s\n\n", ((ConstNode) now).type);
		}
		else if(now instanceof SubExprNode)
		{
			System.out.println("SubExprNode");
			System.out.printf("exprNode : %d\n", ((SubExprNode) now).exprNode.nodeID);
			System.out.println();
			print(((SubExprNode) now).exprNode);
		}
	}
}
