package ScopeCheck.Scopes;

import java.util.ArrayList;

public abstract class Scope
{
	public static int nowID = 0;
	public Scope fatherScope;
	public ArrayList<Scope> childScope = new ArrayList<>();
	public int scopeID;

	public Scope()
	{
		scopeID = nowID++;
	}

}
