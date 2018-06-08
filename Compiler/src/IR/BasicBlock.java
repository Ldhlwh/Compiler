package IR;


import IR.Instructions.Ins;

import java.util.*;

public class BasicBlock
{
	public String blockID;
	public ArrayList<Ins> insList = new ArrayList<Ins>();
	
	public BasicBlock to;
	public BasicBlock ifTrue, ifFalse;
	public ArrayList<BasicBlock> from = new ArrayList<>();
	
	public static int bbNum = 0;
	
	public boolean printed = false;		// While printing the IR
	public boolean allocated = false;	// While allocating the Regs to Mem
	public boolean generated = false;	// While generating NASM code
	public boolean analyzed = false;	// While analyzing liveness
	public boolean added = false;
	
	public FuncBlock ofFunc;
	public Set<String> def = new HashSet<>();
	public Set<String> use = new HashSet<>();
	public Set<String> out = new HashSet<>();
	public Set<String> in = new HashSet<>();
	public Set<String> outp = new HashSet<>();
	public Set<String> inp = new HashSet<>();
	
	//public Map<String, String> take = new HashMap<>();
	
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
