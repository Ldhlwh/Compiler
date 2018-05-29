package AbstractSyntaxTree.Nodes;

import ScopeCheck.Scopes.*;

import java.util.ArrayList;

public class ASTRootNode extends ASTNode
{
	public ArrayList<ASTNode> progSecNode = new ArrayList<>();
	public TopScope scope;
}
