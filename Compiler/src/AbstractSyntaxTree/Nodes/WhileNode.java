package AbstractSyntaxTree.Nodes;

import ScopeCheck.Scopes.LocalScope;

public class WhileNode extends IterStmtNode
{
	public ExprNode exprNode;
	public StmtNode stmtNode;
	
	public LocalScope scope;
}
