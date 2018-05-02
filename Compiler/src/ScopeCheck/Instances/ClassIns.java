package ScopeCheck.Instances;

import java.util.HashMap;
import java.util.Map;

public class ClassIns extends CFVIns
{
	public String name;
	public Map<String, VariIns> variMap = new HashMap<>();
	public Map<String, FuncIns> funcMap = new HashMap<>();

	public ClassIns() {}
	public ClassIns(String str, int ins) {
		name = str;
		insID = ins;
	}
}
