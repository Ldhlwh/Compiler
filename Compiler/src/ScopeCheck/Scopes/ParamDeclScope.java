package ScopeCheck.Scopes;

public class ParamDeclScope extends Scope
{
	public String singleType;
	public int dimNum = 0;
	public String name;

	public ParamDeclScope() {}
	public ParamDeclScope(String type, int num, String str)
	{
		singleType = type;
		dimNum = num;
		name = str;
	}
}
