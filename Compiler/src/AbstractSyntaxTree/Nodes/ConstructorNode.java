package AbstractSyntaxTree.Nodes;


import ScopeCheck.Scopes.ConstructorScope;

public class ConstructorNode extends ASTNode
{
	public String id;
	public BlockStmtNode blockStmtNode;
	
	public ConstructorScope scope;
}
