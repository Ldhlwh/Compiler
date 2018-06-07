package IR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class FuncBlock
{
	public String funcName;
	public BasicBlock entry;
	
	public ArrayList<String> param = new ArrayList<>();
	
	public Map<String, Integer> memPos = new HashMap<>();
	public int memSize = 0;
	public ArrayList<BasicBlock> blockList = new ArrayList<>();
	
	public Map<String, Set<String>> itf = new HashMap<>();
	public Map<String, Integer> deg = new HashMap<>();
	public Map<String, Boolean> inGraph = new HashMap<>();
	public Map<String, Integer> color = new HashMap<>();
	public Map<String, String> take = new HashMap<>();
	
	public boolean used = false;
	
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
