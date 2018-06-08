package IR.Instructions;

import IR.BasicBlock;

public class JumpIns extends Ins
{
	public String src;		// ret $src
	public String target;	// jump %target
	public String cond, ifTrue, ifFalse;		// br $cond %ifTrue %ifFalse
	public BasicBlock toBlock;
}
