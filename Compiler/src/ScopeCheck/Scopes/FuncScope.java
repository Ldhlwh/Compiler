package ScopeCheck.Scopes;

import ScopeCheck.Instances.ParamIns;
import ScopeCheck.Instances.VariIns;

import java.util.HashMap;
import java.util.Map;

public class FuncScope extends Scope
{
	public String name;
	public String singleRtnType;
	public int rtnDimNum = 0;
	public Map<String, ParamIns> paramMap = new HashMap<>();
}
