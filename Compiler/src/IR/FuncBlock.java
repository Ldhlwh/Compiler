package IR;

import java.util.ArrayList;


public class FuncBlock
{
	public String funcName;
	public BasicBlock entry;
	
	public ArrayList<String> param = new ArrayList<>();
	
	public FuncBlock(String name)
	{
		funcName = name;
	}
	
	public void print()
	{
		System.err.printf("func %s", funcName);
		for(String p : param)
		{
			System.err.printf(" %s", p);
		}
		System.err.printf(" {\n");
		entry.print();
		System.err.printf("}\n\n");
	}
}
