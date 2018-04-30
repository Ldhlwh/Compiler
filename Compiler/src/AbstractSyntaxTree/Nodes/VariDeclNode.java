package AbstractSyntaxTree.Nodes;

import java.util.ArrayList;

public class VariDeclNode extends ProgSecNode
{
	public TypeNode typeNode;
	public ArrayList<ASTNode> variInitNode = new ArrayList<>();
}
