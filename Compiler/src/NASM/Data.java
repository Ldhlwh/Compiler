package NASM;

public class Data
{
	public String data = "";
	public static int dataNum = 0;
	public String dataID = null;
	
	Data()
	{
		dataID = "_D" + (dataNum++);
	}
}
