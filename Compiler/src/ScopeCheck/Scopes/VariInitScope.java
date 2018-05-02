package ScopeCheck.Scopes;

public class VariInitScope extends Scope
{
	public String name;
	public String initValue = null;
	public int kind; // 0 variable, 1 function, 2 constant, 3 instance, 4 type

	public VariInitScope() {}
	public VariInitScope(String str, String init)
	{
		name = str;
		initValue = init;
	}
}
