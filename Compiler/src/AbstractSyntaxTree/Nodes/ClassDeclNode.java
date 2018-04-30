package AbstractSyntaxTree.Nodes;

import java.util.ArrayList;

public class ClassDeclNode extends ProgSecNode
{
	public String id;
	public ArrayList<ASTNode> progSecNode = new ArrayList<>();
}
