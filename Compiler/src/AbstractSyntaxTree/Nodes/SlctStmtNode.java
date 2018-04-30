package AbstractSyntaxTree.Nodes;

import java.util.ArrayList;

public class SlctStmtNode extends StmtNode
{
	public boolean haveElse = false;
	public ExprNode ifExprNode;
	public StmtNode ifStmtNode, elseStmtNode;
	public ArrayList<ASTNode> elifExprNode = new ArrayList<>();
	public ArrayList<ASTNode> elifStmtNode = new ArrayList<>();
}
