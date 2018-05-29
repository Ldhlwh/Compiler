package IR.Instructions;

public class MemAccIns extends Ins
{
	public String size;			// store size $addr $src offset
	public String sizeReg;		// $dest = load size $addr offset
	public String addr;			// $dest = alloc $size
	public String src;
	public String dest;
	public int offset;
}
