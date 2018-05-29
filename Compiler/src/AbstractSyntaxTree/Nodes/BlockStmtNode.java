package AbstractSyntaxTree.Nodes;

import ScopeCheck.Scopes.LocalScope;

import java.util.ArrayList;

public class BlockStmtNode extends StmtNode
{
	public ArrayList<ASTNode> progSecNode = new ArrayList<>();

	public LocalScope scope;
}
