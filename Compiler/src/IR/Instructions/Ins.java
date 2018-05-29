package IR.Instructions;

public abstract class Ins
{
	public String insName;
	
	public void print()
	{
		if(this instanceof JumpIns)
		{
			System.err.printf("\t%s ", this.insName);
			if(this.insName.equals("ret"))
			{
				System.err.printf("%s\n", ((JumpIns)this).src);
			}
			else if(this.insName.equals("jump"))
			{
				System.err.printf("%%%s\n", ((JumpIns)this).target);
			}
			else if(this.insName.equals("br"))
			{
				System.err.printf("%s %%%s %%%s\n", ((JumpIns)this).cond, ((JumpIns)this).ifTrue, ((JumpIns)this).ifFalse);
			}
		}
		
		else if(this instanceof MemAccIns)
		{
			if(this.insName.equals("store"))
			{
				System.err.printf("\t%s %s %s %s %d\n", this.insName, ((MemAccIns)this).size, ((MemAccIns)this).addr, ((MemAccIns)this).src, ((MemAccIns)this).offset);
			}
			else if(this.insName.equals("load"))
			{
				System.err.printf("\t%s = %s %s %s %d\n", ((MemAccIns)this).dest, this.insName, ((MemAccIns)this).size, ((MemAccIns)this).addr, ((MemAccIns)this).offset);
			}
			else if(this.insName.equals("alloc"))
			{
				System.err.printf("\t%s = %s %s\n", ((MemAccIns)this).dest, this.insName, ((MemAccIns)this).size);
			}
		}
		
		else if(this instanceof FuncCallIns)
		{
			System.err.printf("\t");
			if(!((FuncCallIns)this).isVoid)
			{
				System.err.printf("%s = ", ((FuncCallIns)this).dest);
			}
			System.err.printf("%s %s", this.insName, ((FuncCallIns)this).funcName);
			for(String op : ((FuncCallIns)this).ops)
			{
				System.err.printf(" %s", op);
			}
			System.err.printf("\n");
		}
		
		else if(this instanceof MovIns)
		{
			System.err.printf("\t%s = %s %s\n", ((MovIns)this).dest, this.insName, ((MovIns)this).src);
		}
		
		else if(this instanceof ArithIns)
		{
			System.err.printf("\t%s = %s %s", ((ArithIns)this).dest, this.insName, ((ArithIns)this).src1);
			if(!(this.insName.equals("neg")))
			{
				System.err.printf(" %s", ((ArithIns)this).src2);
			}
			System.err.printf("\n");
		}
		
		else if(this instanceof BitIns)
		{
			System.err.printf("\t%s = %s %s", ((BitIns)this).dest, this.insName, ((BitIns)this).src1);
			if(!(this.insName.equals("not")))
			{
				System.err.printf(" %s", ((BitIns)this).src2);
			}
			System.err.printf("\n");
		}
		
		else if(this instanceof CondSetIns)
		{
			System.err.printf("\t%s = %s %s %s\n", ((CondSetIns)this).dest, this.insName, ((CondSetIns)this).src1, ((CondSetIns)this).src2);
		}
	}
}
