package ScopeCheck;

import AbstractSyntaxTree.Nodes.*;
import ScopeCheck.Instances.ClassIns;
import ScopeCheck.Instances.FuncIns;
import ScopeCheck.Instances.ParamIns;
import ScopeCheck.Instances.VariIns;
import ScopeCheck.Scopes.*;


public class ScopeChecker
{
	public TopScope realRoot;

	public Scope checkRoot(ASTNode now, Scope root)
	{
		realRoot = (TopScope)root;
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
						if(classScope.variMap.containsKey(((VariInitScope)scope).name))
						{
							System.err.printf("Variable \"%s\" has already been defined.\n", ((VariInitScope) scope).name);
							System.exit(1);
						}
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
					if(funcScope.paramMap.containsKey(((ParamDeclScope) scope).name))
					{
						System.err.printf("Parameter \"%s\" has already been declared.\n", ((ParamDeclScope) scope).name);
						System.exit(1);
					}
					funcScope.paramMap.put(((ParamDeclScope) scope).name, newParam);
					newIns.param.add(newParam);
				}
			}
			if (funcScope.fatherScope instanceof ClassScope) {
				if(((ClassScope) funcScope.fatherScope).funcMap.containsKey(funcScope.name))
				{
					System.err.printf("Function \"%s\" has already been defined.\n", funcScope.name);
					System.exit(1);
				}
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
					if(((LocalScope) variDeclScope.fatherScope).variMap.containsKey(((VariInitScope) temp).name))
					{
						System.err.printf("Variable \"%s\" has already been defined.\n", ((VariInitScope) temp).name);
						System.exit(1);
					}
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
		}
		else if (now instanceof ForNode)
		{
			if (father instanceof LocalScope) {
				Scope temp = check(((ForNode) now).stmtNode, father);
				if (temp instanceof LocalScope)
					father.childScope.add(temp);
				return temp;
			}
		} else if (now instanceof WhileNode) {
			if (father instanceof LocalScope) {
				Scope temp = check(((WhileNode) now).stmtNode, father);
				if (temp instanceof LocalScope)
					father.childScope.add(temp);
				return temp;
			}
		}
		else if(now instanceof SuffixIncDecNode)
		{
			ExprScope expr = new ExprScope();
			expr.fatherScope = father;
			Scope temp = check(((SuffixIncDecNode) now).exprNode, father);
			if(((ExprScope)temp).kind != 0 || !(((ExprScope) temp).type.equals("int")))
			{
				System.err.printf("\"%s\" cannot do the INC / DEC operation.\n", ((ExprScope) temp).id);
				System.exit(1);
			}
			expr.kind = 2;
			expr.type = "int";
			expr.source = null;
			expr.dimNum = expr.maxDimNum = 0;
			expr.id = null;
			return expr;
		}
		else if(now instanceof FuncCallNode)
		{
			ExprScope expr = new ExprScope();
			expr.fatherScope = father;
			Scope temp = check(((FuncCallNode) now).exprNode, father);
			if(((ExprScope)temp).kind != 1)
			{
				System.err.printf("\"%s\" is not a function.\n", ((ExprScope) temp).id);
				System.exit(1);
			}
			expr.kind = 1;
			expr.type = ((ExprScope) temp).type;
			expr.source = ((ExprScope) temp).source;
			expr.dimNum = ((ExprScope) temp).dimNum;
			expr.maxDimNum = ((ExprScope) temp).maxDimNum;
			expr.id = ((ExprScope) temp).id;
			if(((FuncCallNode) now).haveParamList)
			{
				FuncIns func = new FuncIns();
				if(((ExprScope) temp).source == null)
				{
					func = realRoot.funcMap.get(expr.id);
				}
				else
				{
					for(Scope scope : realRoot.childScope)
					{
						if(scope instanceof ClassScope)
						{
							if(((ClassScope) scope).name.equals(expr.source))
							{
								func = ((ClassScope) scope).funcMap.get(expr.id);
								break;
							}
						}
					}
				}
				if(func.param.size() != ((FuncCallNode) now).paramListNode.exprNode.size())
				{
					System.err.printf("Wrong number of parameters for function : \"%s\".\n", expr.id);
					System.exit(1);
				}
				for(int i = 0; i < ((FuncCallNode) now).paramListNode.exprNode.size(); i++)
				{
					temp = check(((FuncCallNode) now).paramListNode.exprNode.get(i), father);
					if(!(func.param.get(i).singleType.equals(((ExprScope)temp).type))
						|| func.param.get(i).dimNum != ((ExprScope) temp).dimNum)
					{
						System.err.printf("Wrong parameter type for function : \"%s\".\n", expr.id);
						System.exit(1);
					}
				}

			}
			return expr;
		}
		else if(now instanceof IndexNode)
		{
			ExprScope expr = new ExprScope();
			expr.fatherScope = father;
			Scope ltemp = check(((IndexNode) now).arrayExprNode, father);
			Scope Rtemp = check(((IndexNode) now).indexExprNode,father);
			if(((ExprScope)ltemp).kind != 0 && ((ExprScope) ltemp).kind != 4)
			{
				System.err.printf("\"%s\" cannot be access with index.\n", ((ExprScope) ltemp).id);
				System.exit(1);
			}
			if(((ExprScope) ltemp).dimNum >= ((ExprScope) ltemp).maxDimNum)
			{
				System.err.printf("\"%s\" cannot be access with index.\n", ((ExprScope) ltemp).id);
				System.exit(1);
			}
			expr.id = null;
			expr.type = ((ExprScope) ltemp).type;
			expr.source = ((ExprScope) ltemp).source;
			expr.dimNum = ((ExprScope) ltemp).dimNum + 1;
			expr.maxDimNum = ((ExprScope) ltemp).maxDimNum;
			expr.kind = 0;
			return expr;
		}
		else if(now instanceof MemberNode)
		{
			ExprScope expr = new ExprScope();
			expr.fatherScope = father;
			Scope ltemp = check(((MemberNode) now).exprNode, father);
			//Scope rtemp = check(((MemberNode) now).idNode, father);
			ExprScope rtemp = new ExprScope();
			String find = ((MemberNode) now).idNode.id;
			String type = ((ExprScope)ltemp).type;

			for(Scope scope : realRoot.childScope)
			{
				if(scope instanceof ClassScope)
				{
					if(((ClassScope) scope).name.equals(type))
					{
						if(((ClassScope) scope).variMap.containsKey(find))
						{
							VariIns ins = ((ClassScope) scope).variMap.get(find);
							rtemp.id = ins.name;
							rtemp.type = ins.singleType;
							rtemp.dimNum = rtemp.maxDimNum = ins.dimNum;
							rtemp.kind = 0;
							break;
						}
						for(Scope subScope : scope.childScope)
						{
							if(scope instanceof FuncScope)
							{
								if(((FuncScope)subScope).name.equals(find))
								{
									rtemp.id = find;
									rtemp.type = ((FuncScope)subScope).singleRtnType;
									rtemp.dimNum = rtemp.maxDimNum = ((FuncScope)subScope).rtnDimNum;
									rtemp.kind = 1;
									break;
								}
							}
						}
						System.err.printf("Class : \"%s\" does not have a member named \"%s\".", type, find);
						System.exit(1);
					}
				}
			}


			if(((ExprScope)ltemp).type.equals("int") && ((ExprScope) ltemp).dimNum > 0)
			{
				if(rtemp.id.equals("size"))
				{
					System.err.printf("Type \"int\" do not have a function named \"%s\".\n", rtemp.id);
					System.exit(1);
				}
			}
			else if(((ExprScope) ltemp).type.equals("string"))
			{
				if((!(rtemp.id.equals("length")))
						&& (!(rtemp.id.equals("substring")))
						&& (!(rtemp.id.equals("parseInt")))
						&& (!(((ExprScope) rtemp).id.equals("ord"))))
				{
					System.err.printf("Type \"string\" do not have a function named \"%s\".\n", rtemp.id);
					System.exit(1);
				}
			}
			if(rtemp.source != null)
			{
				if (!((ExprScope) ltemp).type.equals(rtemp.source))
				{
					System.err.printf("Type \"%s\" do not have a function named \"%s\".\n", ((ExprScope) ltemp).type, rtemp.id);
					System.exit(1);
				}
			}
			expr.id = null;
			expr.type = rtemp.type;
			expr.dimNum = rtemp.dimNum;
			expr.source = null;
			expr.kind = 2;
			return expr;
		}
		else if(now instanceof PrefixIncDecNode)
		{
			ExprScope expr = new ExprScope();
			expr.fatherScope = father;
			Scope temp = check(((PrefixIncDecNode) now).exprNode, father);
			if(((ExprScope)temp).kind != 0 || !(((ExprScope) temp).type.equals("int")))
			{
				System.err.printf("\"%s\" cannot do the INC / DEC operation.\n", ((ExprScope) temp).id);
				System.exit(1);
			}
			expr.kind = 2;
			expr.type = "int";
			expr.source = null;
			expr.dimNum = expr.maxDimNum = 0;
			expr.id = null;
			return expr;
		}
		else if(now instanceof PosNegNode)
		{
			ExprScope expr = new ExprScope();
			expr.fatherScope = father;
			Scope temp = check(((PosNegNode) now).exprNode, father);
			if(!(((ExprScope)temp).type.equals("int")))
			{
				System.err.printf("\"%s\" is not an int.\n", ((ExprScope) temp).id);
				System.exit(1);
			}
			expr.id = null;
			expr.type = "int";
			expr.source = null;
			expr.dimNum = 0;
			expr.maxDimNum = 0;
			expr.kind = 2;
			return expr;
		}
		else if(now instanceof NotNode)
		{
			ExprScope expr = new ExprScope();
			expr.fatherScope = father;
			Scope temp = check(((NotNode) now).exprNode, father);
			if(!(((ExprScope)temp).type.equals("int")))
			{
				System.err.printf("\"%s\" is not an int.\n", ((ExprScope) temp).id);
				System.exit(1);
			}
			expr.id = null;
			expr.type = "int";
			expr.source = null;
			expr.dimNum = 0;
			expr.maxDimNum = 0;
			expr.kind = 2;
			return expr;
		}
		else if(now instanceof NewNode)
		{
			ExprScope expr = new ExprScope();
			expr.fatherScope = father;
			Scope temp = check(((NewNode) now).creatorNode, father);
			expr.id = null;
			expr.type = ((ExprScope)temp).type;
			expr.dimNum = ((ExprScope) temp).dimNum;
			expr.maxDimNum = ((ExprScope) temp).maxDimNum;
			expr.source = null;
			expr.kind = 2;
			return expr;
		}
		else if(now instanceof CreatorArrayNode)
		{
			ExprScope expr = new ExprScope();
			expr.fatherScope = father;
			Scope temp = check(((CreatorArrayNode) now).singleTypeNode, father);
			expr.type = ((SingleTypeScope)temp).singleType;
			for(ASTNode node : ((CreatorArrayNode) now).exprNode)
			{
				temp = check(node, father);
				if(((ExprScope)temp).kind == 3 || !(((ExprScope) temp).type.equals("int")))
				{
					System.err.printf("Index should be int.\n");
					System.exit(1);
				}
			}
			expr.id = null;
			expr.source = null;
			expr.dimNum = ((CreatorArrayNode) now).exprNode.size() + ((CreatorArrayNode) now).emptyDimNum;
			expr.maxDimNum = expr.dimNum;
			expr.kind = 2;
			return expr;
		}
		else if(now instanceof CreatorSingleNode)
		{
			ExprScope expr = new ExprScope();
			expr.fatherScope = father;
			expr.type = ((CreatorSingleNode) now).singleTypeNode.type;
			expr.id = null;
			expr.source = null;
			expr.dimNum = expr.maxDimNum = 0;
			expr.kind = 2;
			return expr;
		}
		else if(now instanceof BinaryNode)
		{
			ExprScope expr = new ExprScope();
			expr.fatherScope = father;
			Scope ltemp = check(((BinaryNode) now).leftExprNode, father);
			Scope rtemp = check(((BinaryNode) now).rightExprNode, father);
			if(!(((ExprScope)ltemp).type.equals(((ExprScope)rtemp).type)))
			{
				System.err.printf("Different types cannot do this operation.\n");
				System.exit(1);
			}
			if(((ExprScope) ltemp).kind == 3 || ((ExprScope) rtemp).kind == 3)
			{
				System.err.printf("Only internal classes can do this operation.\n");
				System.exit(1);
			}
			if(((ExprScope) ltemp).dimNum > 1 || ((ExprScope) rtemp).dimNum > 1)
			{
				System.err.printf("Arrays cannot do this operation.\n");
				System.exit(1);
			}
			expr.id = null;
			expr.type = ((ExprScope) ltemp).type;
			expr.source = null;
			expr.dimNum = 0;
			expr.maxDimNum = 0;
			expr.kind = 2;
			return expr;
		}
		else if(now instanceof AssignNode)
		{
			ExprScope expr = new ExprScope();
			expr.fatherScope = father;
			Scope ltemp = check(((AssignNode) now).leftExprNode, father);
			Scope rtemp = check(((AssignNode) now).rightExprNode, father);
			if(!(((ExprScope)ltemp).type.equals(((ExprScope)rtemp).type)))
			{
				System.err.printf("Different types cannot do this operation.\n");
				System.exit(1);
			}
			if(((ExprScope) ltemp).kind == 3 || ((ExprScope) rtemp).kind == 3)
			{
				System.err.printf("Only internal classes can do this operation.\n");
				System.exit(1);
			}
			if(((ExprScope) ltemp).dimNum > 1 || ((ExprScope) rtemp).dimNum > 1)
			{
				System.err.printf("Arrays cannot do this operation.\n");
				System.exit(1);
			}
			expr.id = null;
			expr.type = null;
			expr.source = null;
			expr.dimNum = 0;
			expr.maxDimNum = 0;
			expr.kind = 2;
			return expr;
		}
		else if(now instanceof IdNode)
		{
			ExprScope expr = new ExprScope();
			expr.fatherScope = father;
			String id = ((IdNode) now).id;
			boolean have = false;
			for(Scope nowScope = father; !(nowScope instanceof EmptyScope); nowScope = nowScope.fatherScope)
			{
				if(nowScope instanceof LocalScope) {
					if (((LocalScope) nowScope).variMap.containsKey(id)) {
						have = true;
						VariIns ins = ((LocalScope) nowScope).variMap.get(id);
						expr.id = id;
						expr.type = ins.singleType;
						expr.source = null;
						expr.dimNum = expr.maxDimNum = ins.dimNum;
						if(ins.singleType.equals("bool")
								|| ins.singleType.equals("int")
								|| ins.singleType.equals("string")
								|| ins.singleType.equals("char"))
							expr.kind = 0;
						else
							expr.kind = 4;
						break;
					}
				}
				else if(nowScope instanceof FuncScope)
				{
					if(((FuncScope) nowScope).paramMap.containsKey(id))
					{
						have = true;
						ParamIns ins = ((FuncScope) nowScope).paramMap.get(id);
						expr.id = id;
						expr.type = ins.singleType;
						expr.source = null;
						expr.dimNum = expr.maxDimNum = ins.dimNum;
						if(ins.singleType.equals("bool")
								|| ins.singleType.equals("int")
								|| ins.singleType.equals("string")
								|| ins.singleType.equals("char"))
							expr.kind = 0;
						else
							expr.kind = 4;
						break;
					}
				}
				else if(nowScope instanceof ClassScope)
				{
					if(((ClassScope) nowScope).variMap.containsKey(id))
					{
						have = true;
						VariIns ins = ((ClassScope) nowScope).variMap.get(id);
						expr.id = id;
						expr.type = ins.singleType;
						expr.source = null;
						expr.dimNum = expr.maxDimNum = ins.dimNum;
						if(ins.singleType.equals("bool")
								|| ins.singleType.equals("int")
								|| ins.singleType.equals("string")
								|| ins.singleType.equals("char"))
							expr.kind = 0;
						else
							expr.kind = 4;
						break;
					}
					else if(((ClassScope) nowScope).funcMap.containsKey(id))
					{
						have = true;
						FuncIns ins = ((ClassScope) nowScope).funcMap.get(id);
						expr.id = id;
						expr.type = ins.singleType;
						expr.source = null;
						expr.dimNum = expr.maxDimNum = ins.rtnDimNum;
						if(ins.singleType.equals("bool")
								|| ins.singleType.equals("int")
								|| ins.singleType.equals("string")
								|| ins.singleType.equals("char")
								|| ins.singleType.equals("void"))
							expr.kind = 0;
						else
							expr.kind = 4;
						break;
					}
				}
				else if(nowScope instanceof TopScope)
				{
					if(((TopScope) nowScope).variMap.containsKey(id))
					{
						have = true;
						VariIns ins = ((TopScope) nowScope).variMap.get(id);
						expr.id = id;
						expr.type = ins.singleType;
						expr.source = null;
						expr.dimNum = expr.maxDimNum = ins.dimNum;
						if(ins.singleType.equals("bool")
								|| ins.singleType.equals("int")
								|| ins.singleType.equals("string")
								|| ins.singleType.equals("char"))
							expr.kind = 0;
						else
							expr.kind = 4;
						break;
					}
					else if(((TopScope) nowScope).funcMap.containsKey(id))
					{
						have = true;
						FuncIns ins = ((TopScope) nowScope).funcMap.get(id);
						expr.id = id;
						expr.type = ins.singleType;
						expr.source = null;
						expr.dimNum = expr.maxDimNum = ins.rtnDimNum;
						if(ins.singleType.equals("bool")
								|| ins.singleType.equals("int")
								|| ins.singleType.equals("string")
								|| ins.singleType.equals("char")
								|| ins.singleType.equals("void"))
							expr.kind = 0;
						else
							expr.kind = 4;
						break;
					}
					else if(((TopScope) nowScope).classMap.containsKey(id))
					{
						have = true;
						expr.id = id;
						expr.type = null;
						expr.source = null;
						expr.dimNum = expr.maxDimNum = 0;
						expr.kind = 4;
						break;
					}
				}
			}
			if(!have)
			{
				System.err.printf("\"%s\" is not defined.\n", id);
				System.exit(1);
			}
			return expr;
		}
		else if(now instanceof ConstNode)
		{
			ExprScope expr = new ExprScope();
			expr.fatherScope = father;
			expr.id = null;
			expr.type = ((ConstNode) now).type;
			expr.source = null;
			expr.dimNum = expr.maxDimNum = 0;
			expr.kind = 2;
			return expr;
		}
		else if(now instanceof SubExprNode)
		{
			return check(((SubExprNode) now).exprNode, father);
		}

		//System.err.printf("Unaccesible Node ID = %d\n", now.nodeID);
		//System.exit(1);
		System.err.println(now.getClass());
		return new EmptyScope();
	}
}
