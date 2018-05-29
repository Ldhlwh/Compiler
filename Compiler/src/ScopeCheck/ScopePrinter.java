package ScopeCheck;

import ScopeCheck.Scopes.*;
import ScopeCheck.Instances.*;

import java.util.Map;

public class ScopePrinter
{
	public void print (Scope now)
	{
		System.err.printf("%d -> %d -> ", now.fatherScope.scopeID, now.scopeID);
		for(Scope scope : now.childScope)
			System.err.printf("%d ", scope.scopeID);
		System.err.println();

		if(now instanceof TopScope)
		{
			System.err.println("[TopScope]");
			classMapPrint(((TopScope) now).classMap);
			funcMapPrint(((TopScope) now).funcMap);
			variMapPrint(((TopScope) now).variMap);
			for(Scope scope : now.childScope)
			{
				System.err.println();
				print(scope);
			}
			System.err.println();
		}
		else if(now instanceof ClassScope)
		{
			System.err.println("[ClassScope]");
			System.err.printf("className : %s\n", ((ClassScope) now).name);
			funcMapPrint(((ClassScope) now).funcMap);
			variMapPrint(((ClassScope) now).variMap);
			for(Scope scope : now.childScope)
			{
				System.err.println();
				print(scope);
			}
			System.err.println();
		}
		else if(now instanceof FuncScope)
		{
			System.err.println("[FuncScope]");
			System.err.printf("return[dimNum] : %s[%d] funcName : %s\n", ((FuncScope) now).singleRtnType, ((FuncScope) now).rtnDimNum, ((FuncScope) now).name);
			paramMapPrint(((FuncScope) now).paramMap);
			for(Scope scope : now.childScope)
			{
				System.err.println();
				print(scope);
			}
			System.err.println();
		}
		else if(now instanceof ConstructorScope)
		{
			System.err.println("[ConstructorScope]");
			System.err.printf("return[dimNum] : %s[%d] funcName : %s\n", ((ConstructorScope)now).name, 0, ((ConstructorScope) now).name);
			for(Scope scope : now.childScope)
			{
				System.err.println();
				print(scope);
			}
			System.err.println();
		}
		else if(now instanceof LocalScope)
		{
			System.err.println("[LocalScope]");
			variMapPrint(((LocalScope) now).variMap);
			for(Scope scope : now.childScope)
			{
				System.err.println();
				print(scope);
			}
			System.err.println();
		}
	}

	public void classMapPrint (Map<String, ClassIns> now)
	{
		System.err.printf("------classMap------\n");
		for(Map.Entry<String, ClassIns> entry : now.entrySet())
		{
			System.err.printf("name : %10s \tIns: %s\n", entry.getKey(), entry.getValue().name);
		}
	}
	public void funcMapPrint (Map<String, FuncIns> now)
	{
		System.err.printf("------funcMap------\n");
		for(Map.Entry<String, FuncIns> entry : now.entrySet())
		{
			System.err.printf("name : %10s \tIns: %s[%d] %s ", entry.getKey(), entry.getValue().singleType, entry.getValue().rtnDimNum, entry.getValue().name);
			for(ParamIns nowParam : entry.getValue().param)
			{
				System.err.printf("(%s[%d] %s), ", nowParam.singleType, nowParam.dimNum, nowParam.name);
			}
			System.err.println();
		}
	}
	public void variMapPrint (Map<String, VariIns> now)
	{
		System.err.printf("------variMap------\n");
		for(Map.Entry<String, VariIns> entry : now.entrySet())
		{
			System.err.printf("name : %10s \tIns: %s[%d] %s", entry.getKey(), entry.getValue().singleType, entry.getValue().dimNum, entry.getValue().name);
			if(entry.getValue().initValue != null)
				System.err.printf(" = %s", entry.getValue().initValue);
			System.err.println();
		}
	}
	public void paramMapPrint (Map<String, ParamIns> now)
	{
		System.err.printf("------paramMap------\n");
		for(Map.Entry<String, ParamIns> entry : now.entrySet())
		{
			System.err.printf("name : %10s \tIns: %s[%d] %s", entry.getKey(), entry.getValue().singleType, entry.getValue().dimNum, entry.getValue().name);
			System.err.println();
		}
	}
}
