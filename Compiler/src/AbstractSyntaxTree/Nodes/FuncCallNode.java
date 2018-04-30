package AbstractSyntaxTree.Nodes;

public class FuncCallNode extends ExprNode
{
	public ExprNode exprNode;
	public boolean haveParamList = false;
	public ParamListNode paramListNode;
	public IdNode idNode;
}
