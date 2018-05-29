package IR.Instructions;

public class JumpIns extends Ins
{
	public String src;		// ret $src
	public String target;	// jump %target
	public String cond, ifTrue, ifFalse;		// br $cond %ifTrue %ifFalse
}
