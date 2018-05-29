package AbstractSyntaxTree.Nodes;

import ScopeCheck.Scopes.Scope;

public class IdNode extends ExprNode {
	public String id = "";
	public Scope ofScope;
}
