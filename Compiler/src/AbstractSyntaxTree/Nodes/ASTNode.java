package AbstractSyntaxTree.Nodes;

import java.util.ArrayList;

public class ASTNode
{
	public static int nowID = 0;
	public int nodeID;
	ASTNode()
	{
		nodeID = nowID++;
	}
}
