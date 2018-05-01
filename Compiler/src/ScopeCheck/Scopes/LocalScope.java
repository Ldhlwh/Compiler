package ScopeCheck.Scopes;

import ScopeCheck.Instances.VariIns;

import java.util.HashMap;
import java.util.Map;

public class LocalScope extends Scope
{
	public Map<String, VariIns> variMap = new HashMap<>();
}
