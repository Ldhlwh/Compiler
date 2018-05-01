package ScopeCheck.Scopes;

public class TypeScope extends Scope
{
	public String singleType;
	public int dimNum = 0;

	public TypeScope() {}
	public TypeScope(String type, int num)
	{
		singleType = type;
		dimNum = num;
	}
}
