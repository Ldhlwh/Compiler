package AbstractSyntaxTree.Nodes;

import java.util.ArrayList;

public class CreatorArrayNode extends CreatorNode
{
	public SingleTypeNode singleTypeNode;
	public ArrayList<ASTNode> exprNode = new ArrayList<>();
	public int emptyDimNum = 0;
}
