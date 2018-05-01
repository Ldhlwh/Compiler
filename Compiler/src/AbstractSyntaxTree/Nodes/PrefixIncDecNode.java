package AbstractSyntaxTree.Nodes;

import ScopeCheck.Scopes.ExprScope;

public class PrefixIncDecNode extends ExprNode
{
	public String op = "";
	public ExprNode exprNode;
}
