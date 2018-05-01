package ScopeCheck.Scopes;

import java.util.ArrayList;

public class VariDeclScope extends Scope
{
	public String singleType;
	public int dimNum = 0;
	public ArrayList<Scope> variInitScope = new ArrayList<>();
}
