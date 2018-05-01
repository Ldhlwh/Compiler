package ScopeCheck.Scopes;

public class VariInitScope extends Scope
{
	public String name;
	public String initValue = null;

	public VariInitScope() {}
	public VariInitScope(String str, String init)
	{
		name = str;
		initValue = init;
	}
}
