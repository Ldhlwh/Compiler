package AbstractSyntaxTree.Nodes;

import ScopeCheck.Scopes.LocalScope;

public class ForNode extends IterStmtNode
{
	public boolean haveInit = false, haveCond = false, haveStep = false;
	public ExprNode initExprNode, condExprNode, stepExprNode;
	public StmtNode stmtNode;
	
	public LocalScope scope;
}
