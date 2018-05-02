package ScopeCheck.Instances;

import java.util.ArrayList;

public class FuncIns extends CFVIns
{
	public String singleType;
	public int rtnDimNum = 0;
	public String name;
	public ArrayList<ParamIns> param = new ArrayList<>();

	public FuncIns() {}
	public FuncIns(String type, int num, String str, int ins)
	{
		singleType = type;
		rtnDimNum = num;
		name = str;
		insID = ins;
	}
}
