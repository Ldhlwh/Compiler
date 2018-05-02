package ScopeCheck.Scopes;

public class ExprScope extends Scope
{
	public String id;
	public String type;		// data type
	public int dimNum = 0, maxDimNum = 0;
	public int emptyDimNum = 0;
	public String source = null;	// from which class (ins.data -> source = getClass(int))
	public int kind; // 0 variable, 1 function, 2 constant, 3 instance, 4 type
}
