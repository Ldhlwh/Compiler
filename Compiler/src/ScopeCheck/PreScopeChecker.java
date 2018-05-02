package ScopeCheck;

import AbstractSyntaxTree.Nodes.*;
import ScopeCheck.Instances.ClassIns;
import ScopeCheck.Instances.FuncIns;
import ScopeCheck.Instances.ParamIns;
import ScopeCheck.Instances.VariIns;
import ScopeCheck.Scopes.*;


public class PreScopeChecker
{
	public int insID = 0;

	public Scope check(ASTNode now, Scope father)
	{
		if(now instanceof ASTRootNode) {
			TopScope topScope = new TopScope();
			topScope.fatherScope = father;
			for (ASTNode node : ((ASTRootNode) now).progSecNode)
			{
				Scope temp = check(node, topScope);
				if (temp instanceof VariDeclScope)
				{
					for(Scope scope : ((VariDeclScope) temp).variInitScope)
					{
						if(topScope.variMap.containsKey(((VariInitScope)scope).name))
						{
							System.err.printf("Variable \"%s\" has already been defined.\n", ((VariInitScope) scope).name);
							System.exit(1);
						}
						topScope.variMap.put(((VariInitScope)scope).name, new VariIns(((VariDeclScope) temp).singleType, ((VariDeclScope) temp).dimNum, ((VariInitScope)scope).name, ((VariInitScope) scope).initValue, insID++));
					}
				}
			}
			addInternalFunc(topScope);
			if(!topScope.funcMap.containsKey("main"))
			{
				System.err.println("There should be a main function.");
				System.exit(1);
			}
			return topScope;
		}
		else if(now instanceof ClassDeclNode)
		{
			ClassScope classScope = new ClassScope();
			classScope.fatherScope = father;
			classScope.name = ((ClassDeclNode) now).id;
			if(classScope.fatherScope instanceof TopScope)
			{
				if(((TopScope) classScope.fatherScope).classMap.containsKey(classScope.name))
				{
					System.err.printf("Class \"%s\" has already been defined.\n", classScope.name);
					System.exit(1);
				}
				((TopScope) classScope.fatherScope).classMap.put(classScope.name, new ClassIns(classScope.name, insID++));
			}
			for(ASTNode node : ((ClassDeclNode) now).progSecNode)
			{
				Scope temp = check(node, classScope);
			}
			return classScope;
		}
		else if(now instanceof ConstructorNode)
		{
			ConstructorScope constructorScope = new ConstructorScope();
			constructorScope.fatherScope = father;
			constructorScope.name = ((ConstructorNode) now).id;
			if (constructorScope.fatherScope instanceof ClassScope) {
				if(((ClassScope) constructorScope.fatherScope).funcMap.containsKey(constructorScope.name))
				{
					System.err.printf("Constructor \"%s\" has already been defined.\n", constructorScope.name);
					System.exit(1);
				}
				if(!((ClassScope) constructorScope.fatherScope).name.equals(constructorScope.name))
				{
					System.err.printf("Constructor must have the same name as that of the class.\n", constructorScope.name);
					System.exit(1);
				}
				FuncIns newIns = new FuncIns();
				newIns.name = constructorScope.name;
				((ClassScope) constructorScope.fatherScope).funcMap.put(constructorScope.name, newIns);
			}
			return constructorScope;
		}
		else if(now instanceof FuncDeclNode)
		{
			FuncScope funcScope = new FuncScope();
			funcScope.fatherScope = father;
			funcScope.name = ((FuncDeclNode) now).id;
			Scope temp = check(((FuncDeclNode) now).typeNode, funcScope);
			funcScope.singleRtnType = ((TypeScope)temp).singleType;
			funcScope.rtnDimNum = ((TypeScope)temp).dimNum;
			if(funcScope.name.equals("main"))
			{
				if(!funcScope.singleRtnType.equals("int") || funcScope.rtnDimNum > 0) {
					System.err.println("The \"main\" function must return a single int.");
					System.exit(1);
				}
			}
			FuncIns newIns = new FuncIns(funcScope.singleRtnType, funcScope.rtnDimNum, funcScope.name, insID++);
			if(((FuncDeclNode) now).haveParamDeclListNode)
			{
				temp = check(((FuncDeclNode) now).paramDeclListNode, funcScope);
				for(Scope scope : ((ParamDeclListScope)temp).paramDeclScope)
				{
					ParamIns newParam = new ParamIns(((ParamDeclScope) scope).singleType, ((ParamDeclScope) scope).dimNum, ((ParamDeclScope) scope).name);
					if(funcScope.paramMap.containsKey(((ParamDeclScope) scope).name))
					{
						System.err.printf("Paramter \"%s\" has already been declared.\n", ((ParamDeclScope) scope).name);
						System.exit(1);
					}
					funcScope.paramMap.put(((ParamDeclScope) scope).name, newParam);
					newIns.param.add(newParam);
				}
			}
			if(funcScope.fatherScope instanceof TopScope)
			{
				if(((TopScope)funcScope.fatherScope).funcMap.containsKey(funcScope.name))
				{
					System.err.printf("Function \"%s\" has already been defined.\n", funcScope.name);
					System.exit(1);
				}
				((TopScope) funcScope.fatherScope).funcMap.put(funcScope.name, newIns);
			}
			if(funcScope.fatherScope instanceof ClassScope)
			{
				if(((ClassScope)funcScope.fatherScope).funcMap.containsKey(funcScope.name))
				{
					System.err.printf("Function \"%s\" has already been defined.\n", funcScope.name);
					System.exit(1);
				}
				((ClassScope) funcScope.fatherScope).funcMap.put(funcScope.name, newIns);
			}
			return funcScope;
		}
		else if(now instanceof TypeNode)
		{
			TypeScope typeScope = new TypeScope();
			typeScope.fatherScope = father;
			Scope temp = check(((TypeNode) now).singleTypeNode, typeScope);
			typeScope.singleType = ((SingleTypeScope)temp).singleType;
			typeScope.dimNum = ((TypeNode) now).dimNum;
			return typeScope;
		}
		else if(now instanceof SingleTypeNode)
		{
			SingleTypeScope singleTypeScope = new SingleTypeScope();
			singleTypeScope.fatherScope = father;
			singleTypeScope.singleType = ((SingleTypeNode) now).type;
			return singleTypeScope;
		}
		else if(now instanceof VariDeclNode)
		{
			VariDeclScope variDeclScope = new VariDeclScope();
			variDeclScope.fatherScope = father;
			Scope temp = check(((VariDeclNode) now).typeNode, variDeclScope);
			variDeclScope.singleType = ((TypeScope)temp).singleType;
			variDeclScope.dimNum = ((TypeScope)temp).dimNum;
			for(ASTNode node : ((VariDeclNode) now).variInitNode)
			{
				temp = check(node, variDeclScope);
				variDeclScope.variInitScope.add(temp);
				if(variDeclScope.fatherScope instanceof LocalScope)
				{
					VariIns put = new VariIns(variDeclScope.singleType, variDeclScope.dimNum, ((VariInitScope) temp).name);
					if(((LocalScope) variDeclScope.fatherScope).variMap.containsKey(((VariInitScope) temp).name))
					{
						System.err.printf("Variable \"%s\" has already been defined.\n", ((VariInitScope) temp).name);
						System.exit(1);
					}
					((TopScope) variDeclScope.fatherScope).variMap.put(((VariInitScope)temp).name, put);
				}
			}
			return variDeclScope;
		}
		else if(now instanceof VariInitNode)
		{
			VariInitScope variInitScope = new VariInitScope();
			variInitScope.fatherScope = father;
			variInitScope.name = ((VariInitNode) now).id;
			variInitScope.initValue = null;
			return variInitScope;
		}
		else if(now instanceof ParamDeclListNode)
		{
			ParamDeclListScope paramDeclListScope = new ParamDeclListScope();
			paramDeclListScope.fatherScope = father;
			for(ASTNode node : ((ParamDeclListNode) now).paramDeclNode)
			{
				Scope temp = check(node, paramDeclListScope);
				paramDeclListScope.paramDeclScope.add(temp);
			}
			return paramDeclListScope;
		}
		else if(now instanceof ParamDeclNode)
		{
			ParamDeclScope paramDeclScope = new ParamDeclScope();
			paramDeclScope.fatherScope = father;
			Scope temp = check(((ParamDeclNode) now).typeNode, paramDeclScope);
			paramDeclScope.singleType = ((TypeScope)temp).singleType;
			paramDeclScope.dimNum = ((TypeScope) temp).dimNum;
			paramDeclScope.name = ((ParamDeclNode) now).id;
			return paramDeclScope;
		}
		System.err.printf("Unaccesible Node ID = %d\n", now.nodeID);
		System.exit(1);
		return new EmptyScope();
	}

	void addInternalFunc(Scope root)
	{
		FuncIns printIns = new FuncIns();
		printIns.name = "print";
		printIns.singleType = "void";
		printIns.rtnDimNum = 0;
		printIns.param.add(new ParamIns("string", 0, "str"));
		((TopScope)root).funcMap.put(printIns.name, printIns);

		FuncIns printlnIns = new FuncIns();
		printlnIns.name = "println";
		printlnIns.singleType = "void";
		printlnIns.rtnDimNum = 0;
		printlnIns.param.add(new ParamIns("string", 0, "str"));
		((TopScope)root).funcMap.put(printlnIns.name, printlnIns);

		FuncIns getStringIns = new FuncIns();
		getStringIns.name = "getString";
		getStringIns.singleType = "string";
		getStringIns.rtnDimNum = 0;
		((TopScope)root).funcMap.put(getStringIns.name, getStringIns);

		FuncIns getIntIns = new FuncIns();
		getIntIns.name = "getInt";
		getIntIns.singleType = "int";
		getIntIns.rtnDimNum = 0;
		((TopScope) root).funcMap.put(getIntIns.name, getIntIns);

		FuncIns toStringIns = new FuncIns();
		toStringIns.name = "toString";
		toStringIns.singleType = "string";
		toStringIns.rtnDimNum = 0;
		toStringIns.param.add(new ParamIns("int", 0, "i"));
		((TopScope) root).funcMap.put(toStringIns.name, toStringIns);

		FuncIns lengthIns = new FuncIns();
		lengthIns.name = "length";
		lengthIns.singleType = "int";
		lengthIns.rtnDimNum = 0;
		((TopScope) root).funcMap.put(lengthIns.name, lengthIns);

		FuncIns subStringIns = new FuncIns();
		subStringIns.name = "substring";
		subStringIns.singleType = "string";
		subStringIns.rtnDimNum = 0;
		subStringIns.param.add(new ParamIns("int", 0, "left"));
		subStringIns.param.add(new ParamIns("int", 0, "right"));
		((TopScope) root).funcMap.put(subStringIns.name, subStringIns);

		FuncIns parseIntIns = new FuncIns();
		parseIntIns.name = "parseInt";
		parseIntIns.singleType = "int";
		parseIntIns.rtnDimNum = 0;
		((TopScope) root).funcMap.put(parseIntIns.name, parseIntIns);

		FuncIns ordIns = new FuncIns();
		ordIns.name = "ord";
		ordIns.singleType = "int";
		ordIns.rtnDimNum = 0;
		ordIns.param.add(new ParamIns("int", 0, "pos"));
		((TopScope) root).funcMap.put(ordIns.name, ordIns);

		FuncIns sizeIns = new FuncIns();
		sizeIns.name = "size";
		sizeIns.singleType = "int";
		sizeIns.rtnDimNum = 0;
		((TopScope) root).funcMap.put(sizeIns.name, sizeIns);
	}
}
