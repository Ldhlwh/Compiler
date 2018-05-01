package ScopeCheck.Scopes;

import ScopeCheck.Instances.FuncIns;
import ScopeCheck.Instances.VariIns;

import java.util.HashMap;
import java.util.Map;

public class ClassScope extends Scope
{
	public String name;
	public Map<String, FuncIns> funcMap = new HashMap<>();
	public Map<String, VariIns> variMap = new HashMap<>();
}
