package ScopeCheck;

import ScopeCheck.Scopes.*;
import ScopeCheck.Instances.*;

import java.util.Map;

public class ScopePrinter
{
	public void print (Scope now)
	{
		System.out.printf("%d -> %d -> ", now.fatherScope.scopeID, now.scopeID);
		for(Scope scope : now.childScope)
			System.out.printf("%d ", scope.scopeID);
		System.out.println();

		if(now instanceof TopScope)
		{
			System.out.println("[TopScope]");
			classMapPrint(((TopScope) now).classMap);
			funcMapPrint(((TopScope) now).funcMap);
			variMapPrint(((TopScope) now).variMap);
			for(Scope scope : now.childScope)
			{
				System.out.println();
				print(scope);
			}
			System.out.println();
		}
		else if(now instanceof ClassScope)
		{
			System.out.println("[ClassScope]");
			System.out.printf("className : %s\n", ((ClassScope) now).name);
			funcMapPrint(((ClassScope) now).funcMap);
			variMapPrint(((ClassScope) now).variMap);
			for(Scope scope : now.childScope)
			{
				System.out.println();
				print(scope);
			}
			System.out.println();
		}
		else if(now instanceof FuncScope)
		{
			System.out.println("[FuncScope]");
			System.out.printf("return[dimNum] : %s[%d] funcName : %s\n", ((FuncScope) now).singleRtnType, ((FuncScope) now).rtnDimNum, ((FuncScope) now).name);
			paramMapPrint(((FuncScope) now).paramMap);
			for(Scope scope : now.childScope)
			{
				System.out.println();
				print(scope);
			}
			System.out.println();
		}
		else if(now instanceof LocalScope)
		{
			System.out.println("[LocalScope]");
			variMapPrint(((LocalScope) now).variMap);
			for(Scope scope : now.childScope)
			{
				System.out.println();
				print(scope);
			}
			System.out.println();
		}
	}

	public void classMapPrint (Map<String, ClassIns> now)
	{
		System.out.printf("------classMap------\n");
		for(Map.Entry<String, ClassIns> entry : now.entrySet())
		{
			System.out.printf("name : %10s \tIns: %s\n", entry.getKey(), entry.getValue().name);
		}
	}
	public void funcMapPrint (Map<String, FuncIns> now)
	{
		System.out.printf("------funcMap------\n");
		for(Map.Entry<String, FuncIns> entry : now.entrySet())
		{
			System.out.printf("name : %10s \tIns: %s[%d] %s ", entry.getKey(), entry.getValue().singleType, entry.getValue().rtnDimNum, entry.getValue().name);
			for(ParamIns nowParam : entry.getValue().param)
			{
				System.out.printf("(%s[%d] %s), ", nowParam.singleType, nowParam.dimNum, nowParam.name);
			}
			System.out.println();
		}
	}
	public void variMapPrint (Map<String, VariIns> now)
	{
		System.out.printf("------variMap------\n");
		for(Map.Entry<String, VariIns> entry : now.entrySet())
		{
			System.out.printf("name : %10s \tIns: %s[%d] %s", entry.getKey(), entry.getValue().singleType, entry.getValue().dimNum, entry.getValue().name);
			if(entry.getValue().initValue != null)
				System.out.printf(" = %s", entry.getValue().initValue);
			System.out.println();
		}
	}
	public void paramMapPrint (Map<String, ParamIns> now)
	{
		System.out.printf("------paramMap------\n");
		for(Map.Entry<String, ParamIns> entry : now.entrySet())
		{
			System.out.printf("name : %10s \tIns: %s[%d] %s", entry.getKey(), entry.getValue().singleType, entry.getValue().dimNum, entry.getValue().name);
			System.out.println();
		}
	}
}
