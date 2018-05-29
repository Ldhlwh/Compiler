package AbstractSyntaxTree.Nodes;

import ScopeCheck.Scopes.FuncScope;

import java.util.ArrayList;

public class FuncDeclNode extends ProgSecNode
{
	public TypeNode typeNode;
	public String id;
	public ParamDeclListNode paramDeclListNode;
	public boolean haveParamDeclListNode = false;
	public BlockStmtNode blockStmtNode;
	
	public FuncScope scope;
}
