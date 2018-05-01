package ScopeCheck;

import AbstractSyntaxTree.Nodes.*;
import ScopeCheck.Instances.ClassIns;
import ScopeCheck.Instances.FuncIns;
import ScopeCheck.Instances.ParamIns;
import ScopeCheck.Instances.VariIns;
import ScopeCheck.Scopes.*;


public class ScopeChecker
{
	public Scope checkRoot(ASTNode now, Scope root)
	{
		for(ASTNode node : ((ASTRootNode)now).progSecNode)
		{
			Scope temp = check(node, root);
			if(temp instanceof ClassScope)
			{
				root.childScope.add(temp);
			}
			else if(temp instanceof FuncScope)
			{
				root.childScope.add(temp);
			}
		}
		return root;
	}
	public Scope check(ASTNode now, Scope father) {
		if (now instanceof ClassDeclNode) {
			ClassScope classScope = new ClassScope();
			classScope.fatherScope = father;
			classScope.name = ((ClassDeclNode) now).id;
			for (ASTNode node : ((ClassDeclNode) now).progSecNode) {
				Scope temp = check(node, classScope);
				classScope.childScope.add(temp);
				if (temp instanceof VariDeclScope) {
					for (Scope scope : ((VariDeclScope) temp).variInitScope) {
						classScope.variMap.put(((VariInitScope) scope).name, new VariIns(((VariDeclScope) temp).singleType, ((VariDeclScope) temp).dimNum, ((VariInitScope) scope).name, ((VariInitScope) scope).initValue));
					}
				}
			}
			return classScope;
		} else if (now instanceof FuncDeclNode) {
			FuncScope funcScope = new FuncScope();
			funcScope.fatherScope = father;
			funcScope.name = ((FuncDeclNode) now).id;
			Scope temp = check(((FuncDeclNode) now).typeNode, funcScope);
			funcScope.singleRtnType = ((TypeScope) temp).singleType;
			funcScope.rtnDimNum = ((TypeScope) temp).dimNum;
			FuncIns newIns = new FuncIns(funcScope.singleRtnType, funcScope.rtnDimNum, funcScope.name);
			if (((FuncDeclNode) now).haveParamDeclListNode) {
				temp = check(((FuncDeclNode) now).paramDeclListNode, funcScope);
				for (Scope scope : ((ParamDeclListScope) temp).paramDeclScope) {
					ParamIns newParam = new ParamIns(((ParamDeclScope) scope).singleType, ((ParamDeclScope) scope).dimNum, ((ParamDeclScope) scope).name);
					funcScope.paramMap.put(((ParamDeclScope) scope).name, newParam);
					newIns.param.add(newParam);
				}
			}
			if (funcScope.fatherScope instanceof ClassScope) {
				((ClassScope) funcScope.fatherScope).funcMap.put(funcScope.name, newIns);
			}
			temp = check(((FuncDeclNode) now).blockStmtNode, funcScope);
			funcScope.childScope.add(temp);
			return funcScope;
		} else if (now instanceof TypeNode) {
			TypeScope typeScope = new TypeScope();
			typeScope.fatherScope = father;
			Scope temp = check(((TypeNode) now).singleTypeNode, typeScope);
			typeScope.singleType = ((SingleTypeScope) temp).singleType;
			typeScope.dimNum = ((TypeNode) now).dimNum;
			return typeScope;
		} else if (now instanceof SingleTypeNode) {
			SingleTypeScope singleTypeScope = new SingleTypeScope();
			singleTypeScope.fatherScope = father;
			singleTypeScope.singleType = ((SingleTypeNode) now).type;
			return singleTypeScope;
		} else if (now instanceof VariDeclNode) {
			VariDeclScope variDeclScope = new VariDeclScope();
			variDeclScope.fatherScope = father;
			Scope temp = check(((VariDeclNode) now).typeNode, variDeclScope);
			variDeclScope.singleType = ((TypeScope) temp).singleType;
			variDeclScope.dimNum = ((TypeScope) temp).dimNum;
			for (ASTNode node : ((VariDeclNode) now).variInitNode) {
				temp = check(node, variDeclScope);
				variDeclScope.variInitScope.add(temp);
				VariIns put = new VariIns(variDeclScope.singleType, variDeclScope.dimNum, ((VariInitScope) temp).name);
				if (variDeclScope.fatherScope instanceof LocalScope) {
					((LocalScope) (variDeclScope.fatherScope)).variMap.put(((VariInitScope) temp).name, put);
				}
			}
			return variDeclScope;
		} else if (now instanceof VariInitNode) {
			VariInitScope variInitScope = new VariInitScope();
			variInitScope.fatherScope = father;
			variInitScope.name = ((VariInitNode) now).id;

			if (((VariInitNode) now).assign)
			{
				Scope temp = check(((VariInitNode) now).exprNode, variInitScope);
			}
			else
				variInitScope.initValue = null;
			return variInitScope;
		} else if (now instanceof ParamDeclListNode) {
			ParamDeclListScope paramDeclListScope = new ParamDeclListScope();
			paramDeclListScope.fatherScope = father;
			for (ASTNode node : ((ParamDeclListNode) now).paramDeclNode) {
				Scope temp = check(node, paramDeclListScope);
				paramDeclListScope.paramDeclScope.add(temp);
			}
			return paramDeclListScope;
		} else if (now instanceof ParamDeclNode) {
			ParamDeclScope paramDeclScope = new ParamDeclScope();
			paramDeclScope.fatherScope = father;
			Scope temp = check(((ParamDeclNode) now).typeNode, paramDeclScope);
			paramDeclScope.singleType = ((TypeScope) temp).singleType;
			paramDeclScope.dimNum = ((TypeScope) temp).dimNum;
			paramDeclScope.name = ((ParamDeclNode) now).id;
			return paramDeclScope;
		} else if (now instanceof BlockStmtNode) {
			LocalScope localScope = new LocalScope();
			localScope.fatherScope = father;
			for (ASTNode node : ((BlockStmtNode) now).progSecNode) {
				Scope temp = check(node, localScope);
				if (temp instanceof LocalScope) {
					localScope.childScope.add(temp);
				}
			}
			return localScope;
		} else if (now instanceof ExprStmtNode) {
			if (!((ExprStmtNode) now).empty) {
				return check(((ExprStmtNode) now).exprNode, father);
			}
			return new EmptyScope();
		} else if (now instanceof SlctStmtNode) {
			if (father instanceof LocalScope) {
				Scope temp = check(((SlctStmtNode) now).ifStmtNode, father);
				if (temp instanceof LocalScope)
					father.childScope.add(temp);
				for (ASTNode node : ((SlctStmtNode) now).elifStmtNode) {
					temp = check(node, father);
					if (temp instanceof LocalScope)
						father.childScope.add(temp);
				}
				if (((SlctStmtNode) now).haveElse) {
					temp = check(((SlctStmtNode) now).elseStmtNode, father);
					if (temp instanceof LocalScope)
						father.childScope.add(temp);
				}
			}
			return new EmptyScope();
		} else if (now instanceof ForInitNode) {
			LocalScope localScope = new LocalScope();
			localScope.fatherScope = father;
			check(((ForInitNode) now).variDeclNode, localScope);
			Scope temp = check(((ForInitNode) now).stmtNode, localScope);
			if (temp instanceof LocalScope)
				localScope.childScope.add(temp);
			return localScope;
		} else if (now instanceof ForNode) {
			if (father instanceof LocalScope) {
				Scope temp = check(((ForNode) now).stmtNode, father);
				if (temp instanceof LocalScope)
					father.childScope.add(temp);
			}
		} else if (now instanceof WhileNode) {
			if (father instanceof LocalScope) {
				Scope temp = check(((WhileNode) now).stmtNode, father);
				if (temp instanceof LocalScope)
					father.childScope.add(temp);
			}
		}
		else if(now instanceof SuffixIncDecNode)
		{
			check(((SuffixIncDecNode) now).exprNode, father);
		}
		else if(now instanceof FuncCallNode)
		{
			check(((FuncCallNode) now).exprNode, father);
		}
		else if(now instanceof IndexNode)
		{
			check(((IndexNode) now).arrayExprNode, father);
			check(((IndexNode) now).indexExprNode,father);
		}
		else if(now instanceof MemberNode)
		{
			check(((MemberNode) now).exprNode, father);
			check(((MemberNode) now).idNode, father);
		}
		else if(now instanceof PrefixIncDecNode)
		{
			check(((PrefixIncDecNode) now).exprNode, father);
		}
		else if(now instanceof PosNegNode)
		{
			check(((PosNegNode) now).exprNode, father);
		}
		else if(now instanceof NotNode)
		{
			check(((NotNode) now).exprNode, father);
		}
		else if(now instanceof NewNode)
		{
			check(((NewNode) now).creatorNode, father);
		}
		else if(now instanceof CreatorArrayNode)
		{
			for(ASTNode node : ((CreatorArrayNode) now).exprNode)
			{
				check(node, father);
			}
		}
		else if(now instanceof BinaryNode)
		{
			check(((BinaryNode) now).leftExprNode, father);
			check(((BinaryNode) now).rightExprNode, father);
		}
		else if(now instanceof AssignNode)
		{
			check(((AssignNode) now).leftExprNode, father);
			check(((AssignNode) now).rightExprNode, father);
		}
		else if(now instanceof IdNode)
		{
			String id = ((IdNode) now).id;
			boolean have = false;
			for(Scope nowScope = father; !(nowScope instanceof EmptyScope); nowScope = nowScope.fatherScope)
			{
				if(nowScope instanceof LocalScope) {
					if (((LocalScope) nowScope).variMap.containsKey(id)) {
						have = true;
						break;
					}
				}
				else if(nowScope instanceof FuncScope)
				{
					if(((FuncScope) nowScope).paramMap.containsKey(id))
					{
						have = true;
						break;
					}
				}
				else if(nowScope instanceof ClassScope)
				{
					if(((ClassScope) nowScope).variMap.containsKey(id))
					{
						have = true;
						break;
					}
					else if(((ClassScope) nowScope).funcMap.containsKey(id))
					{
						have = true;
						break;
					}
				}
				else if(nowScope instanceof TopScope)
				{
					if(((TopScope) nowScope).variMap.containsKey(id))
					{
						have = true;
						break;
					}
					else if(((TopScope) nowScope).funcMap.containsKey(id))
					{
						have = true;
						break;
					}
					else if(((TopScope) nowScope).classMap.containsKey(id))
					{
						have = true;
						break;
					}
				}
			}
			if(!have)
			{
				System.err.printf("\"%s\" is not defined.\n", id);
				System.exit(1);
			}
		}
		else if(now instanceof SubExprNode)
		{
			check(((SubExprNode) now).exprNode, father);
		}

		//System.err.printf("Unaccesible Node ID = %d\n", now.nodeID);
		//System.exit(1);
		return new EmptyScope();
	}
}
