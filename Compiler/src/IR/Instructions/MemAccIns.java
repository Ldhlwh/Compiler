package IR.Instructions;

import java.util.ArrayList;

public class MemAccIns extends Ins
{
	public String size;			// store size $addr $src offset
	public String sizeReg;		// $dest = load size $addr offset
	public String addr;			// $dest = alloc $size
	public String src;
	public String dest;
	public ArrayList<Integer> constStr = new ArrayList<>();
	public int offset;
}
