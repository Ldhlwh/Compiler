package AbstractSyntaxTree.Nodes;

public class VariInitNode extends ASTNode
{
	public String id;
	public boolean assign = false;
	public ExprNode exprNode;
}
