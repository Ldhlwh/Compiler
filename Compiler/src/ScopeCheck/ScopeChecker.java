package ScopeCheck;

import AbstractSyntaxTree.Nodes.*;
import ScopeCheck.Instances.ClassIns;
import ScopeCheck.Instances.FuncIns;
import ScopeCheck.Instances.ParamIns;
import ScopeCheck.Instances.VariIns;
import ScopeCheck.Scopes.*;
import sun.invoke.empty.Empty;


public class ScopeChecker
{
	public TopScope realRoot;

	public Scope checkRoot(ASTNode now, Scope root)
	{
		realRoot = (TopScope)root;
		Scope temp;
		for(ASTNode node : ((ASTRootNode)now).progSecNode)
		{
			if(node instanceof ClassDeclNode)
				temp = check(node, root);
		}
		for(ASTNode node : ((ASTRootNode)now).progSecNode)
		{
			if(node instanceof FuncDeclNode)
				temp = check(node, root);
		}
		for(ASTNode node : ((ASTRootNode)now).progSecNode)
		{
			if(node instanceof VariDeclNode)
				temp = check(node, root);
		}
		return root;
	}
	public Scope check(ASTNode now, Scope father)
	{
		if (now instanceof ClassDeclNode)
		{
			ClassScope classScope = new ClassScope();
			classScope.fatherScope = father;
			classScope.name = ((ClassDeclNode) now).id;
			classScope.fatherScope.childScope.add(classScope);
			for (ASTNode node : ((ClassDeclNode) now).progSecNode)
			{
				Scope temp = check(node, classScope);
				classScope.childScope.add(temp);
				if (temp instanceof VariDeclScope)
				{
					for (Scope scope : ((VariDeclScope) temp).variInitScope)
					{
						if(classScope.variMap.containsKey(((VariInitScope)scope).name))
						{
							System.err.printf("Variable \"%s\" has already been defined.\n", ((VariInitScope) scope).name);
							System.exit(1);
						}
						classScope.variMap.put(((VariInitScope) scope).name, new VariIns(((VariDeclScope) temp).singleType, ((VariDeclScope) temp).dimNum, ((VariInitScope) scope).name, ((VariInitScope) scope).initValue, 0));
					}
				}
			}
			return classScope;
		}
		else if(now instanceof ConstructorNode)
		{
			ConstructorScope constructorScope = new ConstructorScope();
			constructorScope.fatherScope = father;
			constructorScope.name = ((ConstructorNode) now).id;
			constructorScope.fatherScope.childScope.add(constructorScope);
			if (constructorScope.fatherScope instanceof ClassScope) {
				if(((ClassScope) constructorScope.fatherScope).funcMap.containsKey(constructorScope.name))
				{
					System.err.printf("Function \"%s\" has already been defined.\n", constructorScope.name);
					System.exit(1);
				}
				FuncIns newIns = new FuncIns();
				newIns.name = constructorScope.name;
				((ClassScope) constructorScope.fatherScope).funcMap.put(constructorScope.name, newIns);
			}
			Scope temp = check(((ConstructorNode) now).blockStmtNode, constructorScope);
			constructorScope.childScope.add(temp);
			return constructorScope;
		}
		else if (now instanceof FuncDeclNode) {
			FuncScope funcScope = new FuncScope();
			funcScope.fatherScope = father;
			funcScope.name = ((FuncDeclNode) now).id;
			Scope temp = check(((FuncDeclNode) now).typeNode, funcScope);
			funcScope.singleRtnType = ((TypeScope) temp).singleType;
			funcScope.rtnDimNum = ((TypeScope) temp).dimNum;
			funcScope.fatherScope.childScope.add(funcScope);
			if(!funcScope.singleRtnType.equals("int")
					&& !funcScope.singleRtnType.equals("string")
					&& !funcScope.singleRtnType.equals("bool")
					&& !funcScope.singleRtnType.equals("void"))
			{
				boolean flag = false;
				for(Scope scope : realRoot.childScope)
				{
					if((scope instanceof ClassScope) && ((ClassScope) scope).name.equals(funcScope.singleRtnType))
					{
						flag = true;
						break;
					}
				}
				if(!flag)
				{
					System.err.printf("Return type \"%s\" is undefined.", funcScope.singleRtnType);
					System.exit(1);
				}
			}
			FuncIns newIns = new FuncIns(funcScope.singleRtnType, funcScope.rtnDimNum, funcScope.name, 0);
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
			if(variDeclScope.singleType.equals("void"))
			{
				System.err.printf("Variable cannot have the type \"void\".\n");
				System.exit(1);
			}
			if(!variDeclScope.singleType.equals("int")
					&& !variDeclScope.singleType.equals("bool")
					&& !variDeclScope.singleType.equals("string"))
			{
				if(!realRoot.classMap.containsKey(variDeclScope.singleType))
				{
					System.err.printf("Type \"%s\" has not been defined.\n", variDeclScope.singleType);
					System.exit(1);
				}
			}
			variDeclScope.dimNum = ((TypeScope) temp).dimNum;
			for (ASTNode node : ((VariDeclNode) now).variInitNode) {
				temp = check(node, variDeclScope);
				if(((VariInitScope)temp).kind == 4 && (((VariInitScope) temp).initValue.equals("null")))
				{
					if(variDeclScope.singleType.equals("int")
							|| variDeclScope.singleType.equals("string")
							|| variDeclScope.singleType.equals("bool")) {
						if (variDeclScope.dimNum == 0) {
							System.err.println("NULL cannot be initiated to non-array variable.");
							System.exit(1);
						}
					}
				}
				if(((VariInitScope)temp).kind == 4 && !(((VariInitScope) temp).initValue.equals("null")))
				{
					System.err.println("Type name cannot give initial value.");
					System.exit(1);
				}
				if(((VariInitScope)temp).kind == 3)
				{
					System.err.println("Instance cannot give initial value.");
					System.exit(1);
				}
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
				variInitScope.initValue = ((ExprScope)temp).type;
				variInitScope.kind = ((ExprScope) temp).kind;
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
			if(father instanceof LocalScope) {
				localScope.jumpable = ((LocalScope) father).jumpable;
			}
			localScope.fatherScope.childScope.add(localScope);
			/*
			int s = ((BlockStmtNode) now).progSecNode.size() - 1;
			if((father instanceof FuncScope) && s == -1 && !((FuncScope)father).singleRtnType.equals("void"))
			{
				System.err.printf("Function \"%s\" should have a return statement.\n", ((FuncScope) father).name);
				System.exit(1);
			}
			*/
			for (ASTNode node : ((BlockStmtNode) now).progSecNode) {
				Scope temp = check(node, localScope);
				/*
				if(node == ((BlockStmtNode) now).progSecNode.get(s))
				{
					if(!(node instanceof ReturnNode)) {
						if (father instanceof FuncScope && !(((FuncScope) father).singleRtnType.equals("void"))) {
							System.err.printf("Function \"%s\" should have a return statement.\n", ((FuncScope) father).name);
							System.exit(1);
						}
					}
				}
				*/
			}
			return localScope;
		}
		else if(now instanceof BreakNode)
		{
			if(!((LocalScope)father).jumpable)
			{
				System.err.printf("Cannot break here.\n");
				System.exit(1);
			}
		}
		else if(now instanceof ContinueNode)
		{
			if(!((LocalScope)father).jumpable)
			{
				System.err.printf("Cannot continue here.\n");
				System.exit(1);
			}
		}
		else if (now instanceof ReturnNode)
		{
			Scope temp = check(((ReturnNode) now).exprNode, father);
			if(temp instanceof EmptyScope)
			{
				return new EmptyScope();
			}
			String rtnType = ((ExprScope) temp).type;
			int rtnDimNum = ((ExprScope) temp).dimNum;
			if(rtnType.equals("void"))
			{
				System.err.println("Cannot return void.");
				System.exit(1);
			}
			String className = "";
			if(((ExprScope)temp).id != null && ((ExprScope) temp).id.equals("this"))
			{
				for(Scope scope = father; !(scope.fatherScope instanceof EmptyScope); scope = scope.fatherScope)
				{
					if(scope instanceof ClassScope)
					{
						className = ((ClassScope) scope).name;
						break;
					}
					if(scope instanceof TopScope)
					{
						System.err.printf("Wrong returned type or dimension.\n");
						System.exit(1);
					}
				}
				return new EmptyScope();
			}
			Scope scope;
			for(scope = father;
				!(scope.fatherScope instanceof ClassScope) && !(scope.fatherScope instanceof TopScope);
				scope = scope.fatherScope);

			int expDimNum = ((FuncScope)scope).rtnDimNum;
			String expSingleType = ((FuncScope) scope).singleRtnType;
			if (!(expSingleType.equals(rtnType))
					|| expDimNum != rtnDimNum) {
				System.err.printf("Wrong returned type or dimension.\n");
				System.exit(1);
			}
			return new EmptyScope();
		}
		else if (now instanceof ExprStmtNode) {
			if (!((ExprStmtNode) now).empty) {
				return check(((ExprStmtNode) now).exprNode, father);
			}
			return new EmptyScope();
		}
		else if (now instanceof SlctStmtNode)
		{
			if (father instanceof LocalScope)
			{
				Scope temp = check(((SlctStmtNode) now).ifExprNode, father);
				if(!((ExprScope)temp).type.equals("bool"))
				{
					System.err.printf("Bool type expected in the 1condition.\n");
					System.exit(1);
				}
				check(((SlctStmtNode) now).ifStmtNode, father);
				for(ASTNode node : ((SlctStmtNode) now).elifExprNode)
				{
					temp = check(node, father);
					if(!((ExprScope)temp).type.equals("bool"))
					{
						System.err.printf("Bool type expected in the 2condition.\n");
						System.exit(1);
					}
				}
				for (ASTNode node : ((SlctStmtNode) now).elifStmtNode)
				{
					temp = check(node, father);
				}
				if (((SlctStmtNode) now).haveElse)
				{
					temp = check(((SlctStmtNode) now).elseStmtNode, father);
				}
			}
			return new EmptyScope();
		}
		else if (now instanceof ForInitNode) {
			LocalScope localScope = new LocalScope();
			localScope.fatherScope = father;
			localScope.fatherScope.childScope.add(localScope);
			localScope.jumpable = true;
			check(((ForInitNode) now).variDeclNode, localScope);
			if(((ForInitNode) now).haveCond) {
				Scope temp = check(((ForInitNode) now).condExprNode, localScope);
				if (!((ExprScope) temp).type.equals("bool")) {
					System.err.printf("Bool type expected in the 3condition.\n");
					System.exit(1);
				}
			}
			if(((ForInitNode) now).haveStep) {
				Scope temp = check(((ForInitNode) now).stepExprNode, localScope);
			}
			Scope temp = check(((ForInitNode) now).stmtNode, localScope);
			return localScope;
		}
		else if (now instanceof ForNode)
		{
			LocalScope localScope = new LocalScope();
			localScope.fatherScope = father;
			localScope.fatherScope.childScope.add(localScope);
			localScope.jumpable = true;
			if(((ForNode) now).haveInit)
			{
				Scope temp = check(((ForNode) now).initExprNode, localScope);
			}
			if(((ForNode) now).haveCond) {
				Scope temp = check(((ForNode) now).condExprNode, localScope);
				if (!((ExprScope) temp).type.equals("bool")) {
					System.err.printf("Bool type expected in the 4condition.\n");
					System.exit(1);
				}
			}
			if(((ForNode) now).haveStep) {
				Scope temp = check(((ForNode) now).stepExprNode, localScope);
			}
			Scope temp = check(((ForNode) now).stmtNode, localScope);
			return localScope;
		}
		else if (now instanceof WhileNode) {
			LocalScope localScope = new LocalScope();
			localScope.fatherScope = father;
			localScope.fatherScope.childScope.add(localScope);
			localScope.jumpable = true;
			Scope temp = check(((WhileNode) now).stmtNode, localScope);
			return localScope;
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
			System.err.printf("Current Function : %s\n", ((ExprScope)temp).id);
			if(((ExprScope)temp).kind != 1)
			{
				System.err.printf("\"%s\" is not a function.\n", ((ExprScope) temp).id);
				System.exit(1);
			}
			expr.kind = 2;
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
					System.err.printf("Type : Exp : %s \t Real : %s\n", func.param.get(i).singleType, ((ExprScope)temp).type);
					System.err.printf("DimN : Exp : %s \t Real : %s\n", func.param.get(i).dimNum, ((ExprScope)temp).dimNum);
					if(!(func.param.get(i).singleType.equals(((ExprScope)temp).type))
						|| func.param.get(i).dimNum != ((ExprScope) temp).dimNum)
					{
						if(((ExprScope) temp).type.equals("null")) {
							if (func.param.get(i).singleType.equals("int")
									|| func.param.get(i).singleType.equals("bool")
									|| func.param.get(i).singleType.equals("string")) {
								if (func.param.get(i).dimNum == 0)
								{
									System.err.printf("Wrong parameter type for function : \"%s\".\n", expr.id);
									System.exit(1);
								}
							}
						}
						else {
							System.err.printf("Wrong parameter type for function : \"%s\".\n", expr.id);
							System.exit(1);
						}
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
			if(((ExprScope)ltemp).kind != 0 && ((ExprScope) ltemp).kind != 3)
			{
				System.err.printf("\"%s\" cannot be access with index.\n", ((ExprScope) ltemp).id);
				System.exit(1);
			}

			if(((ExprScope) ltemp).dimNum == 0)
			{
				System.err.printf("\"%s\" cannot be access with index.\n", ((ExprScope) ltemp).id);
				System.exit(1);
			}
			expr.id = null;
			expr.type = ((ExprScope) ltemp).type;
			expr.source = ((ExprScope) ltemp).source;
			expr.dimNum = ((ExprScope) ltemp).dimNum - 1;
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

			if((((ExprScope)ltemp).id != null) && ((ExprScope)ltemp).id.equals("this"))
			{
				for(Scope scope = father; !(scope.fatherScope instanceof EmptyScope); scope = scope.fatherScope)
				{
					if(scope instanceof ClassScope)
					{
						return check(((MemberNode)now).idNode, father);
					}
				}
				System.err.println("\"this\" cannot be used because it is not in a class.\n");
			}
			String find = ((MemberNode) now).idNode.id;
			String type = ((ExprScope)ltemp).type;
			if(realRoot.funcMap.containsKey(find))
			{
				FuncIns ins = realRoot.funcMap.get(find);
				rtemp.id = ins.name;
				rtemp.type = ins.singleType;
				rtemp.dimNum = rtemp.maxDimNum = ins.rtnDimNum;
				rtemp.source = null;
				rtemp.kind = 1;
			}
			else {
				boolean flag = false;
				for (Scope scope : realRoot.childScope)
				{
					if (scope instanceof ClassScope)
					{
						if (((ClassScope) scope).name.equals(type))
						{
							if (((ClassScope) scope).variMap.containsKey(find))
							{
								VariIns ins = ((ClassScope) scope).variMap.get(find);
								rtemp.id = ins.name;
								rtemp.type = ins.singleType;
								rtemp.dimNum = rtemp.maxDimNum = ins.dimNum;
								rtemp.kind = 0;
								rtemp.source = ((ClassScope) scope).name;
								flag = true;
								break;
							}
							if (((ClassScope) scope).funcMap.containsKey(find)) {
								FuncIns ins = ((ClassScope) scope).funcMap.get(find);
								rtemp.id = ins.name;
								rtemp.type = ins.singleType;
								rtemp.dimNum = rtemp.maxDimNum = ins.rtnDimNum;
								rtemp.kind = 1;
								rtemp.source = ((ClassScope) scope).name;
								flag = true;
								break;
							}

							System.err.printf("Class : \"%s\" does not have a member named \"%s\".", type, find);
							System.exit(1);
						}
					}
					else if (scope instanceof FuncScope)
					{
						if (((FuncScope) scope).name.equals(find))
						{
							rtemp.id = find;
							rtemp.type = ((FuncScope) scope).singleRtnType;
							rtemp.dimNum = rtemp.maxDimNum = ((FuncScope) scope).rtnDimNum;
							rtemp.kind = 1;
							rtemp.source = null;
							break;
						}
					}
				}
				if (!flag) {
					System.err.printf("\"%s\" is not detected.\n", find);
					System.exit(1);
				}
			}


			if(((ExprScope)ltemp).type.equals("int") && ((ExprScope) ltemp).dimNum > 0)
			{
				if(!(rtemp.id.equals("size")))
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
			expr.id = rtemp.id;
			expr.type = rtemp.type;
			expr.dimNum = rtemp.dimNum;
			expr.source = rtemp.source;
			expr.kind = rtemp.kind;
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
			if(!(((ExprScope)temp).type.equals("int")) && ((NotNode) now).op.equals("~"))
			{
				System.err.printf("\"%s\" is not an int.\n", ((ExprScope) temp).id);
				System.exit(1);
			}
			if(!((ExprScope)temp).type.equals("bool") && ((NotNode) now).op.equals("!"))
			{
				System.err.printf("\"%s\" is not an bool.\n", ((ExprScope) temp).id);
				System.exit(1);
			}
			expr.id = null;
			if(((NotNode) now).op.equals("~"))
				expr.type = "int";
			else if(((NotNode)now).op.equals("!"))
				expr.type = "bool";
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
			System.err.printf("left type %s\nright type %s\n", ((ExprScope)ltemp).type, ((ExprScope)rtemp).type);
			if(((ExprScope) ltemp).type.equals("int")
					|| ((ExprScope) ltemp).type.equals("string")
					|| ((ExprScope) ltemp).type.equals("bool"))
			{
				if(((ExprScope) ltemp).dimNum == 0 && ((ExprScope) rtemp).type.equals("null"))
				{
					System.err.printf("Null cannot be initiated to non-array variable.\n");
					System.exit(1);
				}
			}
			if(!(((ExprScope)ltemp).type.equals(((ExprScope)rtemp).type)))
			{
				if(!((ExprScope) rtemp).type.equals("null"))
				{
					System.err.printf("Different types cannot do this binary operation.\n");
					System.exit(1);
				}
			}
			if(((ExprScope) ltemp).kind == 3 || ((ExprScope) rtemp).kind == 3)
			{
				System.err.printf("Only internal classes can do this operation.\n");
				System.exit(1);
			}
			if(((ExprScope) ltemp).dimNum > 0 || ((ExprScope) rtemp).dimNum > 0)
			{
				System.err.printf("Arrays cannot do this binary operation.\n");
				System.exit(1);
			}
			if(((BinaryNode) now).op.equals("+")
					|| ((BinaryNode) now).op.equals("-")
					|| ((BinaryNode) now).op.equals("*")
					|| ((BinaryNode) now).op.equals("/")
					|| ((BinaryNode) now).op.equals("%")
					|| ((BinaryNode) now).op.equals("<<")
					|| ((BinaryNode) now).op.equals(">>")
					|| ((BinaryNode) now).op.equals("~")
					|| ((BinaryNode) now).op.equals("|")
					|| ((BinaryNode) now).op.equals("^")
					|| ((BinaryNode) now).op.equals("&"))
			{
				if(!(((BinaryNode) now).op.equals("+")) && (!(((ExprScope) ltemp).type.equals("int")) || !(((ExprScope) rtemp).type.equals("int"))))
				{
					System.err.printf("Operation \"%s\" cannot be done between two \"%s\"\n", ((BinaryNode)now).op, ((ExprScope) ltemp).type);
					System.exit(1);
				}
				expr.type = ((ExprScope) ltemp).type;
			}
			else
			{
				if(((BinaryNode) now).op.equals("&&")
						|| ((BinaryNode) now).op.equals("||")
						|| ((BinaryNode) now).op.equals("!"))
				{
					if(!((ExprScope) ltemp).type.equals("int") && !(((ExprScope) ltemp).type.equals("bool")))
					{
						System.err.printf("Operation \"%s\" cannot be done between two \"%s\"\n", ((BinaryNode)now).op, ((ExprScope) ltemp).type);
						System.exit(1);
					}
				}
				expr.type = "bool";
			}
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
			if(((ExprScope) rtemp).type.equals("null"))
			{
				if(((ExprScope) ltemp).dimNum > 0)
				{
					expr.id = null;
					expr.type = null;
					expr.source = null;
					expr.dimNum = ((ExprScope) ltemp).dimNum;
					expr.maxDimNum = ((ExprScope) ltemp).maxDimNum;
					expr.kind = 2;
					return expr;
				}
				else if(!((ExprScope) ltemp).type.equals("int")
						|| !((ExprScope) ltemp).type.equals("string")
						|| !((ExprScope) ltemp).type.equals("bool"))
				{
					expr.id = null;
					expr.type = null;
					expr.source = null;
					expr.dimNum = ((ExprScope) ltemp).dimNum;
					expr.maxDimNum = ((ExprScope) ltemp).maxDimNum;
					expr.kind = 2;
					return expr;
				}
				else
				{
					System.err.printf("NULL can only be assigned to an array.\n");
					System.exit(1);
				}
			}
			if(!(((ExprScope)ltemp).type.equals(((ExprScope)rtemp).type)))
			{
				System.err.printf("Different types cannot do this assign operation.\n");
				System.exit(1);
			}
			if(((ExprScope) ltemp).kind == 2)
			{
				System.err.printf("Const cannot be the left value.\n");
				System.exit(1);
			}
			/*
			if(((ExprScope) ltemp).kind == 3 || ((ExprScope) rtemp).kind == 3)
			{
				System.err.printf("Instance of non-internal classes cannot do this operation.\n");
				System.exit(1);
			}*/
			if(((ExprScope) ltemp).kind == 4 || ((ExprScope) rtemp).kind == 4)
			{
				System.err.printf("Type cannot assign or be assigned.\n");
				System.exit(1);
			}
			expr.id = null;
			expr.type = null;
			expr.source = null;
			expr.dimNum = ((ExprScope) ltemp).dimNum;
			expr.maxDimNum = ((ExprScope) ltemp).maxDimNum;
			expr.kind = 2;
			return expr;
		}
		else if(now instanceof IdNode)
		{
			ExprScope expr = new ExprScope();
			expr.fatherScope = father;
			String id = ((IdNode) now).id;
			if(id.equals("this"))
			{
				expr.id = "this";
				return expr;
			}
			boolean have = false;
			if(id.equals("int")
					|| id.equals("string")
					|| id.equals("bool")
					|| id.equals("char"))
			{
				expr.id = expr.type = id;
				expr.dimNum = expr.maxDimNum = 0;
				expr.source = null;
				expr.kind = 4;
				return expr;
			}
			String scopeName = "";
			boolean scopeIsFunc = true;
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
						else if(ins.singleType.equals("void"))
						{
							System.err.printf("Variable cannot have the type \"void\".\n");
							System.exit(1);
						}
						else
							expr.kind = 3;
						break;
					}
				}
				else if(nowScope instanceof FuncScope)
				{
					scopeName = ((FuncScope) nowScope).name;
					scopeIsFunc = true;
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
						else if(ins.singleType.equals("void"))
						{
							System.err.printf("Variable cannot have the type \"void\".\n");
							System.exit(1);
						}
						else
							expr.kind = 3;
						break;
					}
				}
				else if(nowScope instanceof ClassScope)
				{
					scopeName = ((ClassScope) nowScope).name;
					scopeIsFunc = false;
					if(id.equals(((ClassScope) nowScope).name))
					{
						expr.id = id;
						expr.type = id;
						expr.source = null;
						expr.dimNum = expr.maxDimNum = 0;
						expr.kind = 4;
						break;
					}
					else if(((ClassScope) nowScope).variMap.containsKey(id))
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
						else if(ins.singleType.equals("void"))
						{
							System.err.printf("Variable cannot have the type \"void\".\n");
							System.exit(1);
						}
						else
							expr.kind = 3;
						break;
					}
					else if(((ClassScope) nowScope).funcMap.containsKey(id))
					{
						have = true;
						FuncIns ins = ((ClassScope) nowScope).funcMap.get(id);
						expr.id = id;
						expr.type = ins.singleType;
						expr.source = ((ClassScope) nowScope).name;
						expr.dimNum = expr.maxDimNum = ins.rtnDimNum;
						if(ins.singleType.equals("bool")
								|| ins.singleType.equals("int")
								|| ins.singleType.equals("string")
								|| ins.singleType.equals("char")
								|| ins.singleType.equals("void"))
							expr.kind = 1;
						else
							expr.kind = 1;
						break;
					}
				}
				else if(nowScope instanceof TopScope)
				{
					if(((TopScope) nowScope).variMap.containsKey(id))
					{
						have = true;
						VariIns ins = ((TopScope) nowScope).variMap.get(id);
						System.err.printf("scopeName : %s\n", scopeName);
						if(scopeIsFunc && (ins.insID > realRoot.funcMap.get(scopeName).insID))
						{
							System.err.printf("\"%s\" is defined after the current function.\n", id);
							System.exit(1);
						}
						else if(!scopeIsFunc && (ins.insID > realRoot.classMap.get(scopeName).insID))
						{
							System.err.printf("\"%s\" is defined after the current class.\n", id);
							System.exit(1);
						}
						expr.id = id;
						expr.type = ins.singleType;
						expr.source = null;
						expr.dimNum = ins.dimNum;
						expr.maxDimNum = ins.dimNum;
						if(ins.singleType.equals("bool")
								|| ins.singleType.equals("int")
								|| ins.singleType.equals("string")
								|| ins.singleType.equals("char"))
							expr.kind = 0;
						else if(ins.singleType.equals("void"))
						{
							System.err.printf("Variable cannot have the type \"void\".\n");
							System.exit(1);
						}
						else
							expr.kind = 3;
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
							expr.kind = 1;
						else
							expr.kind = 1;
						break;
					}
					else if(((TopScope) nowScope).classMap.containsKey(id))
					{
						have = true;
						expr.id = id;
						expr.type = null;
						expr.source = null;
						expr.dimNum = expr.maxDimNum = 0;
						expr.kind = 3;
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
			if(((ConstNode) now).type.equals("null"))
				expr.kind = 4;
			else
				expr.kind = 2;
			return expr;
		}
		else if(now instanceof SubExprNode)
		{
			return check(((SubExprNode) now).exprNode, father);
		}

		//System.err.printf("Unaccesible Node ID = %d\n", now.nodeID);
		//System.exit(1);
		//System.err.println(now.getClass());
		return new EmptyScope();
	}
}
