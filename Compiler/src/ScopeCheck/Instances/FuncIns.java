package ScopeCheck.Instances;

import java.util.ArrayList;

public class FuncIns
{
	public String singleType;
	public int rtnDimNum = 0;
	public String name;
	public ArrayList<ParamIns> param = new ArrayList<>();

	public FuncIns() {}
	public FuncIns(String type, int num, String str)
	{
		singleType = type;
		rtnDimNum = num;
		name = str;
	}
}
