package ScopeCheck.Instances;

public class ParamIns
{
	public String singleType;
	public int dimNum = 0;
	public String name;

	public ParamIns() {}
	public ParamIns(String type, int num, String str)
	{
		singleType = type;
		dimNum = num;
		name = str;
	}
}
