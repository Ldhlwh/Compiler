package ScopeCheck.Scopes;

import ScopeCheck.Instances.ClassIns;
import ScopeCheck.Instances.FuncIns;
import ScopeCheck.Instances.VariIns;

import java.util.HashMap;
import java.util.Map;

public class TopScope extends Scope
{
	public Map<String, ClassIns> classMap = new HashMap<>();
	public Map<String, FuncIns> funcMap = new HashMap<>();
	public Map<String, VariIns> variMap = new HashMap<>();
}
