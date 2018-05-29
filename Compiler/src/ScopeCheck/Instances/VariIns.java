package ScopeCheck.Instances;

public class VariIns extends CFVIns
{
	public String singleType;
	public int dimNum;
	public String name;
	public String initValue = null;
	public int offset = 0;

	public VariIns() {}
	public VariIns(String type, int num, String str)
	{
		singleType = type;
		dimNum = num;
		name = str;
		initValue = null;
	}
	public VariIns(String type, int num, String str, String init, int ins)
	{
		singleType = type;
		dimNum = num;
		name = str;
		initValue = init;
		insID = ins;
	}
}
