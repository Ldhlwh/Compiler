package IR.Instructions;

import java.util.ArrayList;

public class FuncCallIns extends Ins
{
	public String funcName;
	public ArrayList<String> ops = new ArrayList<>();
	public boolean isVoid = false;
	public String dest;			// ($dest = ) call funcName $op1 $op2 ...
}
