package ScopeCheck.Scopes;

import AbstractSyntaxTree.Nodes.ASTNode;

import java.util.ArrayList;

public class ParamDeclListScope extends Scope
{
	public ArrayList<Scope> paramDeclScope = new ArrayList<>();
}
