package IR;


import IR.Instructions.Ins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BasicBlock
{
	public String blockID;
	public ArrayList<Ins> insList = new ArrayList<Ins>();
	
	public BasicBlock to;
	public BasicBlock ifTrue, ifFalse;
	
	public static int bbNum = 0;
	
	public boolean printed = false;
	
	public BasicBlock()
	{
		blockID = "bb_" + (bbNum++);
	}
	
	public BasicBlock(String name)
	{
		blockID = name;
	}
	
	public void print()
	{
		System.err.printf("%%%s:\n", blockID);
		printed = true;
		for(Ins ins : insList)
		{
			ins.print();
		}
		if(ifTrue != null && !ifTrue.printed)
		{
			System.err.printf("\n");
			ifTrue.print();
		}
		if(ifFalse != null && !ifFalse.printed)
		{
			System.err.printf("\n");
			ifFalse.print();
		}
		if(to != null && !to.printed)
		{
			System.err.printf("\n");
			to.print();
		}
	}
}
