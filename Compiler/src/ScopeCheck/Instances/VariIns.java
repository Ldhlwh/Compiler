package ScopeCheck.Instances;

public class VariIns
{
	public String singleType;
	public int dimNum;
	public String name;
	public String initValue = null;

	public VariIns() {}
	public VariIns(String type, int num, String str)
	{
		singleType = type;
		dimNum = num;
		name = str;
		initValue = null;
	}
	public VariIns(String type, int num, String str, String init)
	{
		singleType = type;
		dimNum = num;
		name = str;
		initValue = init;
	}
}
