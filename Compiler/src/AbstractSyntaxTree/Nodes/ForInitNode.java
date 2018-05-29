package AbstractSyntaxTree.Nodes;

import ScopeCheck.Scopes.LocalScope;

public class ForInitNode extends IterStmtNode
{
	public VariDeclNode variDeclNode;
	public boolean haveCond = false, haveStep = false;
	public ExprNode condExprNode, stepExprNode;
	public StmtNode stmtNode;
	
	public LocalScope scope;
}
