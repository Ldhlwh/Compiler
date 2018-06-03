package NASM;

import IR.BasicBlock;
import IR.FuncBlock;
import IR.IRGenerator;
import IR.Instructions.*;
import ScopeCheck.Instances.VariIns;

import java.io.*;
import java.util.Map;

public class NASMBuilder
{
	public IRGenerator ir;
	public PrintStream o;
	
	private String temp = "r15";
	private String temp2 = "r14";
	
	public NASMBuilder(boolean submit)
	{
		if(submit)
			o = System.out;
		else
			o = System.err;
	}
	
	public void generate()
	{
		for(FuncBlock fb : ir.funcBlock)
		{
			if(fb.funcName.equals("__init"))
				o.printf("\t\tglobal\t\tmain\n");
			else if(fb.funcName.equals("main"))
				o.printf("\t\tglobal\t\t_main\n");
			else
				o.printf("\t\tglobal\t\t%s\n", fb.funcName);
		}
		o.printf("\n\t\textern\t\tmalloc\n");
		o.printf("\t\textern\t\tputs\n");
		o.printf("\t\textern\t\tprintf\n");
		
		o.printf("\n\t\tsection\t\t.bss\n");
		
		for(Map.Entry<String, VariIns> entry : ir.topScope.variMap.entrySet())
		{
			o.printf("@" + entry.getKey() + ":");
			o.printf("\t\tresq\t\t1\n");
		}
		
		//o.printf("\n\t\tsection\t\t.data\n");
		//o.printf("_outStr:\t\tdb\t\t\"%%s\"\n");
		
		o.printf("\n\t\tsection\t\t.text\n");
		
		for(FuncBlock fb : ir.funcBlock)
		{
			allocMem(fb, fb.entry);
			/*
			o.printf("+++ Func : %s +++\n", fb.funcName);
			for(Map.Entry<String, Integer> entry : fb.memPos.entrySet())
			{
				o.printf("%s\t%d\n", entry.getKey(), entry.getValue());
			}*/
		}
		
		for(FuncBlock fb : ir.funcBlock)
		{
			generate(fb.entry);
		}
	}
	
	public void generate(BasicBlock bb)
	{
		if(bb.blockID.equals("__init"))
			o.printf("main:\n");
		else if(bb.blockID.equals("main"))
			o.printf("_main:\n");
		else
			o.printf("%s:\n", bb.blockID);
		
		if(bb.ofFunc.entry == bb)
		{
			o.printf("\t\tpush\t\trbp\n");
			o.printf("\t\tmov\t\trbp, rsp\n");
			o.printf("\t\tsub\t\trsp, %d\n", bb.ofFunc.memSize);
			
			int paramNum = bb.ofFunc.param.size();
			int up = (paramNum < 6) ? paramNum : 6;
			for(int j = 0; j < up; j++)
			{
				String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(bb.ofFunc.param.get(j)));
				String reg = null;
				if(j == 0)
					reg = "rdi";
				else if(j == 1)
					reg = "rsi";
				else if(j == 2)
					reg = "rdx";
				else if(j == 3)
					reg = "rcx";
				else if(j == 4)
					reg = "r8";
				else if(j == 5)
					reg = "r9";
				
				o.printf("\t\tmov\t\tqword[%s], %s\n", pos, reg);
			}
			if(paramNum > 6)
			{
				int nowPos = 16;
				for(int j = 6; j < paramNum; j++)
				{
					String memPos = "rbp + " + nowPos;
					o.printf("\t\tmov\t\t%s, qword[%s]\n", temp, memPos);
					String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(bb.ofFunc.param.get(j)));
					o.printf("\t\tmov\t\tqword[%s], %s\n", pos, temp);
					nowPos += 8;
				}
			}
		}
		
		String l, r;
		int max = bb.insList.size();
		for(int i = 0; i < max; i++)
		{
			Ins ins = bb.insList.get(i);
			if(ins instanceof JumpIns)
			{
				if(ins.insName.equals("ret"))
				{
					o.printf("\t\tmov\t\trax, ");
					String rtn = ((JumpIns)ins).src;
					int choice = isReg(rtn);
					if(choice == 0)
					{
						o.printf("%s\n", rtn);
					}
					else if(choice == 1)
					{
						String pos = "rbp - " + (bb.ofFunc.memPos.get(rtn) + 8);
						o.printf("qword[%s]\n", pos);
					}
					else if(choice == 2)
					{
						o.printf("qword[%s]\n", rtn);
					}
					o.printf("\t\tleave\n");
					//o.printf("\t\tpop\t\trbp\n");
					o.printf("\t\tret\n");
				}
				else if(ins.insName.equals("jump"))
				{
					o.printf("\t\tjmp\t\t%s\n", ((JumpIns)ins).target);
				}
				else if(ins.insName.equals("br"))
				{
					if(i == 0 || !(bb.insList.get(i - 1) instanceof CondSetIns))
					{
						String rtn = ((JumpIns)ins).cond;
						int choice = isReg(rtn);
						if(choice == 0)
						{
							o.printf("\t\tmov\t\t%s, %s\n", temp, rtn);
							o.printf("\t\tcmp\t\t%s, 1\n", temp);
							o.printf("\t\tje\t\t%s\n", ((JumpIns)ins).ifTrue);
							o.printf("\t\tjmp\t\t%s\n", ((JumpIns)ins).ifFalse);
						}
						else if(choice == 1)
						{
							String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(rtn));
							o.printf("\t\tcmp\t\tqword[%s], 1\n", pos);
							o.printf("\t\tje\t\t%s\n", ((JumpIns)ins).ifTrue);
							o.printf("\t\tjmp\t\t%s\n", ((JumpIns)ins).ifFalse);
						}
						else if(choice == 2)
						{
							o.printf("\t\tcmp\t\tqword[%s], 1\n", rtn);
							o.printf("\t\tje\t\t%s\n", ((JumpIns)ins).ifTrue);
							o.printf("\t\tjmp\t\t%s\n", ((JumpIns)ins).ifFalse);
						}
					}
					else
					{
						Ins lastIns = bb.insList.get(i - 1);
						if(!(((CondSetIns)lastIns).dest.equals(((JumpIns)ins).cond)))
						{
							o.println("ERROR");
							System.exit(1);
						}
						String type = lastIns.insName;
						String src1 = ((CondSetIns)lastIns).src1;
						String src2 = ((CondSetIns)lastIns).src2;
						int choice = isReg(src1);
						if(choice == 0)
						{
							o.printf("\t\tmov\t\t%s, %s\n", temp, src1);
							o.printf("\t\tcmp\t\t%s, %s\n", temp, src2);
						}
						else if(choice == 1)
						{
							String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(src1));
							o.printf("\t\tcmp\t\tqword[%s], %s\n", pos, src2);
						}
						else if(choice == 2)
						{
							o.printf("\t\tcmp\t\tqword[%s], %s\n", src1, src2);
						}
						
						if(type.equals("slt"))
							o.printf("\t\tjl\t\t%s\n", ((JumpIns)ins).ifTrue);
						else if(type.equals("sgt"))
							o.printf("\t\tjg\t\t%s\n", ((JumpIns)ins).ifTrue);
						else if(type.equals("sle"))
							o.printf("\t\tjle\t\t%s\n", ((JumpIns)ins).ifTrue);
						else if(type.equals("sge"))
							o.printf("\t\tjge\t\t%s\n", ((JumpIns)ins).ifTrue);
						else if(type.equals("seq"))
							o.printf("\t\tje\t\t%s\n", ((JumpIns)ins).ifTrue);
						else if(type.equals("sne"))
							o.printf("\t\tjne\t\t%s\n", ((JumpIns)ins).ifTrue);
						
						o.printf("\t\tjmp\t\t%s\n", ((JumpIns)ins).ifFalse);
					}
				}
			}
			else if(ins instanceof MemAccIns)
			{
				if(ins.insName.equals("alloc"))
				{
					int choice = isReg(((MemAccIns)ins).size);
					String src = null;
					if(choice == 0)
					{
						src = ((MemAccIns)ins).size;
					}
					else if(choice == 1)
					{
						String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((MemAccIns)ins).src));
						src = "qword[" + pos + "]";
					}
					else if(choice == 2)
					{
						src = "qword[" + ((MemAccIns)ins).src + "]";
					}
					
					o.printf("\t\tmov\t\trdi, %s\n", src);
					o.printf("\t\tcall\t\tmalloc\n");
					
					int c2 = isReg(((MemAccIns)ins).dest);
					String s2 = null;
					if(c2 == 1)
					{
						String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((MemAccIns)ins).dest));
						s2 = "qword[" + pos + "]";
					}
					else if(c2 == 2)
					{
						s2 = "qword[" + ((MemAccIns)ins).dest + "]";
					}
					
					o.printf("\t\tmov\t\t%s, rax\n", s2);
				}
				else if(ins.insName.equals("store"))
				{
					String size = ((MemAccIns)ins).size;
					String offset = ((MemAccIns)ins).offset + "";
					int choice = isReg(((MemAccIns)ins).addr);
					String addr = null;
					if(choice == 1)
					{
						String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((MemAccIns)ins).addr));
						addr = "qword[" + pos + "]";
					}
					o.printf("\t\tmov\t\t%s, %s\n", temp, addr);
					o.printf("\t\tadd\t\t%s, %s\n", temp, offset);
					
					int cs = isReg(((MemAccIns)ins).src);
					String src = null;
					if(cs == 0)
					{
						src = ((MemAccIns)ins).src;
					}
					else if(choice == 1)
					{
						String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((MemAccIns)ins).src));
						src = "qword[" + pos + "]";
					}
					else if(choice == 2)
					{
						src = "qword[" + ((MemAccIns)ins).src + "]";
					}
					o.printf("\t\tmov\t\t%s, %s\n", temp2, src);
					if(size.equals("1"))
						o.printf("\t\tmov\t\t[%s], %s\n", temp, temp2);
					else if(size.equals("8"))
						o.printf("\t\tmov\t\tqword[%s], %s\n", temp, temp2);
				}
			}
			else if(ins instanceof FuncCallIns)
			{
				String funcName = ((FuncCallIns)ins).funcName;
				
				if(funcName.equals("println")
						|| funcName.equals("print"))
				{
					String str = ((FuncCallIns)ins).ops.get(0);
					int choice = isReg(str);
					String op = null;
					if(choice == 0)
					{
						op = str;
					}
					else if(choice == 1)
					{
						String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(str));
						op = "qword[" + pos + "]";
					}
					else if(choice == 2)
					{
						op = "qword[" + str + "]";
					}
					o.printf("\t\tmov\t\t%s, %s\n", temp, op);
					o.printf("\t\tadd\t\t%s, 8\n", temp);
					
					
					if(funcName.equals("println"))
					{
						o.printf("\t\tmov\t\trdi, %s\n", temp);
						o.printf("\t\tcall\t\tputs\n");
					}
					else if(funcName.equals("print"))
					{
						//o.printf("\t\tmov\t\trdi, _outStr\n");
						o.printf("\t\tmov\t\trdi, %s\n", temp);
						o.printf("\t\tmov\t\trax, 0\n");
						o.printf("\t\tcall\t\tprintf\n");
					}
					
					int c2 = isReg(((FuncCallIns)ins).dest);
					String s2 = null;
					if(c2 == 1)
					{
						String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest));
						s2 = "qword[" + pos + "]";
					}
					else if(c2 == 2)
					{
						s2 = "qword[" + ((FuncCallIns)ins).dest + "]";
					}
					
					o.printf("\t\tmov\t\t%s, rax\n", s2);
				}
				
				else
				{
					FuncBlock curFB = null;
					for(FuncBlock block : ir.funcBlock)
					{
						if(block.funcName.equals(funcName))
						{
							curFB = block;
							break;
						}
					}
					int paramNum = curFB.param.size();
					int up = (paramNum < 6) ? paramNum : 6;
					for(int j = 0; j < up; j++)
					{
						String str = ((FuncCallIns)ins).ops.get(j);
						int choice = isReg(str);
						String op = null;
						if(choice == 0)
						{
							op = str;
						}
						else if(choice == 1)
						{
							String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(str));
							op = "qword[" + pos + "]";
						}
						else if(choice == 2)
						{
							op = "qword[" + str + "]";
						}
						
						String reg = null;
						if(j == 0)
							reg = "rdi";
						else if(j == 1)
							reg = "rsi";
						else if(j == 2)
							reg = "rdx";
						else if(j == 3)
							reg = "rcx";
						else if(j == 4)
							reg = "r8";
						else if(j == 5)
							reg = "r9";
						
						o.printf("\t\tmov\t\t%s, %s\n", reg, op);
					}
					if(paramNum > 6)
					{
						if(paramNum % 2 == 1)
						{
							o.printf("\t\tmov\t\t%s, 0\n", temp);
							o.printf("\t\tpush\t\t%s\n", temp);
						}
						for(int j = paramNum - 1; j >= 6; j--)
						{
							String str = ((FuncCallIns)ins).ops.get(j);
							int choice = isReg(str);
							String op = null;
							if(choice == 0)
							{
								op = str;
							}
							else if(choice == 1)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(str));
								op = "qword[" + pos + "]";
							}
							else if(choice == 2)
							{
								op = "qword[" + str + "]";
							}
							
							o.printf("\t\tmov\t\t%s, %s\n", temp, op);
							o.printf("\t\tpush\t\t%s\n", temp);
						}
					}
					if(funcName.equals("main"))
						funcName = "_main";
					o.printf("\t\tcall\t\t%s\n", funcName);
					
					int choice = isReg(((FuncCallIns)ins).dest);
					String src = null;
					if(choice == 1)
					{
						String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest));
						src = "qword[" + pos + "]";
					}
					else if(choice == 2)
					{
						src = "qword[" + ((FuncCallIns)ins).dest + "]";
					}
					o.printf("\t\tmov\t\t%s, rax\n", src);
				}
			}
			else if(ins instanceof MovIns)
			{
				int choice = isReg(((MovIns)ins).dest);
				int srcc = isReg(((MovIns)ins).src);
				String src = "";
				if(srcc == 0)
				{
					src = ((MovIns)ins).src;
				}
				else if(srcc == 1)
				{
					String spos = "rbp - " + (8 + bb.ofFunc.memPos.get(((MovIns)ins).src));
					src = "qword[" + spos + "]";
				}
				else if(srcc == 2)
				{
					src = "qword[" + ((MovIns)ins).src + "]";
				}
				if(choice == 1)
				{
					if(srcc > 0)
					{
						o.printf("\t\tmov\t\t%s, %s\n", temp, src);
						src = temp;
					}
					String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((MovIns)ins).dest));
					o.printf("\t\tmov\t\tqword[%s], %s\n", pos, src);
				}
				else if(choice == 2)
				{
					if(srcc > 0)
					{
						o.printf("\t\tmov\t\t%s, %s\n", temp, src);
						src = temp;
					}
					o.printf("\t\tmov\t\tqword[%s], %s\n", ((MovIns)ins).dest, src);
				}
			}
			else if(ins instanceof ArithIns)
			{
				if(ins.insName.equals("neg"))
				{
					int choice = isReg(((ArithIns)ins).src1);
					String pos = "";
					if(choice == 1)
					{
						pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((ArithIns)ins).src1));
					}
					else if(choice == 2)
					{
						pos = ((ArithIns)ins).src1;
					}
					o.printf("\t\tneg\t\tqword[%s]\n", pos);
				}
				else if(ins.insName.equals("add")
						|| ins.insName.equals("sub")
						|| ins.insName.equals("mul"))
				{
					int c1 = isReg(((ArithIns)ins).src1);
					int c2 = isReg(((ArithIns)ins).src2);
					String src1 = "", src2 = "";
					if(c1 == 1)
					{
						String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((ArithIns)ins).src1));
						src1 = "qword[" + pos + "]";
					}
					else if(c1 == 2)
					{
						src1 = "qword[" + ((ArithIns)ins).src1 + "]";
					}
					
					if(c2 == 0)
					{
						src2 = ((ArithIns)ins).src2;
					}
					else if(c2 == 1)
					{
						String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((ArithIns)ins).src2));
						src2 = "qword[" + pos + "]";
						
						o.printf("\t\tmov\t\t%s, %s\n", temp, src2);
						src2 = temp;
					}
					else if(c2 == 2)
					{
						src2 = "qword[" + ((ArithIns)ins).src2 + "]";
						
						o.printf("\t\tmov\t\t%s, %s\n", temp, src2);
						src2 = temp;
					}
					
					String insName = ins.insName;
					if(insName.equals("mul"))
						insName = "imul";
					o.printf("\t\t%s\t\t%s, %s\n", insName, src1, src2);
				}
				else if(ins.insName.equals("div")
						|| ins.insName.equals("rem"))
				{
					int choice = isReg(((ArithIns)ins).src1);
					String pos = "";
					String pos2 = "";
					if(choice == 0)
					{
						o.printf("\t\tmov\t\trax, %s\n", ((ArithIns)ins).src1);
					}
					else if(choice == 1)
					{
						pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((ArithIns)ins).src1));
						o.printf("\t\tmov\t\trax, qword[%s]\n", pos);
					}
					else if(choice == 2)
					{
						o.printf("\t\tmov\t\trax, qword[%s]\n", ((ArithIns)ins).src1);
					}
					o.printf("\t\tmov\t\trdx, 0\n");
					int c2 = isReg(((ArithIns)ins).src2);
					if(c2 == 0)
					{
						o.printf("\t\tdiv\t\t%s\n", ((ArithIns)ins).src2);
					}
					else if(c2 == 1)
					{
						pos2 = "rbp - " + (8 + bb.ofFunc.memPos.get(((ArithIns)ins).src2));
						o.printf("\t\tdiv\t\tqword[%s]\n", pos2);
					}
					else if(c2 == 3)
					{
						o.printf("\t\tdiv\t\tqword[%s]\n", ((ArithIns)ins).src2);
					}
					
					if(ins.insName.equals("div"))
					{
						if(choice == 1)
						{
							o.printf("\t\tmov\t\tqword[%s], rax\n", pos);
						}
						else if(choice == 2)
						{
							o.printf("\t\tmov\t\tqword[%s], rax\n", ((ArithIns)ins).src2);
						}
					}
					else if(ins.insName.equals("rem"))
					{
						if(choice == 1)
						{
							o.printf("\t\tmov\t\tqword[%s], rdx\n", pos);
						}
						else if(choice == 2)
						{
							o.printf("\t\tmov\t\tqword[%s], rdx\n", ((ArithIns)ins).src2);
						}
					}
				}
			}
			else if(ins instanceof BitIns)
			{
				if(ins.insName.equals("not"))
				{
					int choice = isReg(((BitIns)ins).src1);
					String pos = "";
					if(choice == 1)
					{
						pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((BitIns)ins).src1));
					}
					else if(choice == 2)
					{
						pos = ((BitIns)ins).src1;
					}
					o.printf("\t\tnot\t\tqword[%s]\n", pos);
				}
				else if(ins.insName.equals("shl")
						|| ins.insName.equals("shr"))
				{
					int c2 = isReg(((BitIns)ins).src2);
					String src2 = "";
					if(c2 == 0)
					{
						src2 = ((BitIns)ins).src2;
					}
					else if(c2 == 1)
					{
						String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((BitIns)ins).src2));
						src2 = "qword[" + pos + "]";
					}
					else if(c2 == 2)
					{
						src2 = "qword[" + ((BitIns)ins).src2 + "]";
					}
					o.printf("\t\tmov\t\trcx, %s\n", src2);
					
					int c1 = isReg(((BitIns)ins).src1);
					String src1 = "";
					if(c1 == 1)
					{
						String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((BitIns)ins).src1));
						src1 = "qword[" + pos + "]";
					}
					else if(c1 == 2)
					{
						src1 = "qword[" + ((BitIns)ins).src1 + "]";
					}
					
					o.printf("\t\tmov\t\t%s, %s\n", temp, src1);
					o.printf("\t\t%s\t\t%s, cl\n", ins.insName, temp);
					o.printf("\t\tmov\t\t%s, %s\n", src1, temp);
				}
				else
				{
					int c1 = isReg(((BitIns)ins).src1);
					int c2 = isReg(((BitIns)ins).src2);
					String src1 = "", src2 = "";
					if(c1 == 1)
					{
						String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((BitIns)ins).src1));
						src1 = "qword[" + pos + "]";
					}
					else if(c1 == 2)
					{
						src1 = "qword[" + ((BitIns)ins).src1 + "]";
					}
					
					if(c2 == 0)
					{
						src2 = ((BitIns)ins).src2;
					}
					else if(c2 == 1)
					{
						String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((BitIns)ins).src2));
						src2 = "qword[" + pos + "]";
						
						o.printf("\t\tmov\t\t%s, %s\n", temp, src2);
						src2 = temp;
					}
					else if(c2 == 2)
					{
						src2 = "qword[" + ((BitIns)ins).src2 + "]";
						
						o.printf("\t\tmov\t\t%s, %s\n", temp, src2);
						src2 = temp;
					}
					o.printf("\t\t%s\t\t%s, %s\n", ins.insName, src1, src2);
				}
			}
			else if(ins instanceof CondSetIns)
			{
			
			}
		}
		
		o.println();
		bb.generated = true;
		if(bb.to != null && !bb.to.generated)
		{
			generate(bb.to);
		}
		if(bb.ifFalse != null && !bb.ifFalse.generated)
		{
			generate(bb.ifFalse);
		}
		if(bb.ifTrue != null && !bb.ifTrue.generated)
		{
			generate(bb.ifTrue);
		}
	}
	
	private void allocMem(FuncBlock fb, BasicBlock bb)
	{
		for(String p : fb.param)
		{
			if(p.substring(0, 1).equals("$"))
			{
				if(!fb.memPos.containsKey(p))
				{
					fb.memPos.put(p, fb.memSize);
					fb.memSize += ir.bytes.get("addr");
				}
			}
		}
		for(Ins ins : bb.insList)
		{
			if(ins instanceof JumpIns)
			{
				if(ins.insName.equals("ret"))
				{
					if(((JumpIns)ins).src.substring(0, 1).equals("$"))
					{
						if(!fb.memPos.containsKey(((JumpIns)ins).src))
						{
							fb.memPos.put(((JumpIns)ins).src, fb.memSize);
							fb.memSize += ir.bytes.get("addr");
						}
					}
				}
				else if(ins.insName.equals("br"))
				{
					if(((JumpIns)ins).cond.substring(0, 1).equals("$"))
					{
						if(!fb.memPos.containsKey(((JumpIns)ins).cond))
						{
							fb.memPos.put(((JumpIns)ins).cond, fb.memSize);
							fb.memSize += ir.bytes.get("addr");
						}
					}
				}
			}
			else if(ins instanceof MemAccIns)
			{
				if(ins.insName.equals("store"))
				{
					if(((MemAccIns)ins).addr.substring(0, 1).equals("$"))
					{
						if(!fb.memPos.containsKey(((MemAccIns)ins).addr))
						{
							fb.memPos.put(((MemAccIns)ins).addr, fb.memSize);
							fb.memSize += ir.bytes.get("addr");
						}
					}
					if(((MemAccIns)ins).src.substring(0, 1).equals("$"))
					{
						if(!fb.memPos.containsKey(((MemAccIns)ins).src))
						{
							fb.memPos.put(((MemAccIns)ins).src, fb.memSize);
							fb.memSize += ir.bytes.get("addr");
						}
					}
					if(((MemAccIns)ins).size.substring(0, 1).equals("$"))
					{
						if(!fb.memPos.containsKey(((MemAccIns)ins).size))
						{
							fb.memPos.put(((MemAccIns)ins).size, fb.memSize);
							fb.memSize += ir.bytes.get("addr");
						}
					}
				}
				else if(ins.insName.equals("load"))
				{
					if(((MemAccIns)ins).addr.substring(0, 1).equals("$"))
					{
						if(!fb.memPos.containsKey(((MemAccIns)ins).addr))
						{
							fb.memPos.put(((MemAccIns)ins).addr, fb.memSize);
							fb.memSize += ir.bytes.get("addr");
						}
					}
					if(((MemAccIns)ins).dest.substring(0, 1).equals("$"))
					{
						if(!fb.memPos.containsKey(((MemAccIns)ins).dest))
						{
							fb.memPos.put(((MemAccIns)ins).dest, fb.memSize);
							fb.memSize += ir.bytes.get("addr");
						}
					}
					if(((MemAccIns)ins).size.substring(0, 1).equals("$"))
					{
						if(!fb.memPos.containsKey(((MemAccIns)ins).size))
						{
							fb.memPos.put(((MemAccIns)ins).size, fb.memSize);
							fb.memSize += ir.bytes.get("addr");
						}
					}
				}
				else if(ins.insName.equals("alloc"))
				{
					if(((MemAccIns)ins).size.substring(0, 1).equals("$"))
					{
						if(!fb.memPos.containsKey(((MemAccIns)ins).size))
						{
							fb.memPos.put(((MemAccIns)ins).size, fb.memSize);
							fb.memSize += ir.bytes.get("addr");
						}
					}
					if(((MemAccIns)ins).dest.substring(0, 1).equals("$"))
					{
						if(!fb.memPos.containsKey(((MemAccIns)ins).dest))
						{
							fb.memPos.put(((MemAccIns)ins).dest, fb.memSize);
							fb.memSize += ir.bytes.get("addr");
						}
					}
				}
			}
			else if(ins instanceof FuncCallIns)
			{
				if(((FuncCallIns)ins).dest.substring(0, 1).equals("$"))
				{
					if(!fb.memPos.containsKey(((FuncCallIns)ins).dest))
					{
						fb.memPos.put(((FuncCallIns)ins).dest, fb.memSize);
						fb.memSize += ir.bytes.get("addr");
					}
				}
			}
			else if(ins instanceof MovIns)
			{
				if(((MovIns)ins).dest.substring(0, 1).equals("$"))
				{
					if(!fb.memPos.containsKey(((MovIns)ins).dest))
					{
						fb.memPos.put(((MovIns)ins).dest, fb.memSize);
						fb.memSize += ir.bytes.get("addr");
					}
				}
				if(((MovIns)ins).src.substring(0, 1).equals("$"))
				{
					if(!fb.memPos.containsKey(((MovIns)ins).src))
					{
						fb.memPos.put(((MovIns)ins).src, fb.memSize);
						fb.memSize += ir.bytes.get("addr");
					}
				}
			}
			else if(ins instanceof ArithIns)
			{
				if(((ArithIns)ins).dest.substring(0, 1).equals("$"))
				{
					if(!fb.memPos.containsKey(((ArithIns)ins).dest))
					{
						fb.memPos.put(((ArithIns)ins).dest, fb.memSize);
						fb.memSize += ir.bytes.get("addr");
					}
				}
				if(((ArithIns)ins).src1.substring(0, 1).equals("$"))
				{
					if(!fb.memPos.containsKey(((ArithIns)ins).src1))
					{
						fb.memPos.put(((ArithIns)ins).src1, fb.memSize);
						fb.memSize += ir.bytes.get("addr");
					}
				}
				if(!ins.insName.equals("neg"))
				{
					if(((ArithIns)ins).src2.substring(0, 1).equals("$"))
					{
						if(!fb.memPos.containsKey(((ArithIns)ins).src2))
						{
							fb.memPos.put(((ArithIns)ins).src2, fb.memSize);
							fb.memSize += ir.bytes.get("addr");
						}
					}
				}
			}
			else if(ins instanceof BitIns)
			{
				if(((BitIns)ins).dest.substring(0, 1).equals("$"))
				{
					if(!fb.memPos.containsKey(((BitIns)ins).dest))
					{
						fb.memPos.put(((BitIns)ins).dest, fb.memSize);
						fb.memSize += ir.bytes.get("addr");
					}
				}
				if(((BitIns)ins).src1.substring(0, 1).equals("$"))
				{
					if(!fb.memPos.containsKey(((BitIns)ins).src1))
					{
						fb.memPos.put(((BitIns)ins).src1, fb.memSize);
						fb.memSize += ir.bytes.get("addr");
					}
				}
				if(!ins.insName.equals("not"))
				{
					if(((BitIns)ins).src2.substring(0, 1).equals("$"))
					{
						if(!fb.memPos.containsKey(((BitIns)ins).src2))
						{
							fb.memPos.put(((BitIns)ins).src2, fb.memSize);
							fb.memSize += ir.bytes.get("addr");
						}
					}
				}
			}
			else if(ins instanceof CondSetIns)
			{
				if(((CondSetIns)ins).dest.substring(0, 1).equals("$"))
				{
					if(!fb.memPos.containsKey(((CondSetIns)ins).dest))
					{
						fb.memPos.put(((CondSetIns)ins).dest, fb.memSize);
						fb.memSize += ir.bytes.get("addr");
					}
				}
				if(((CondSetIns)ins).src1.substring(0, 1).equals("$"))
				{
					if(!fb.memPos.containsKey(((CondSetIns)ins).src1))
					{
						fb.memPos.put(((CondSetIns)ins).src1, fb.memSize);
						fb.memSize += ir.bytes.get("addr");
					}
				}
				if(((CondSetIns)ins).src2.substring(0, 1).equals("$"))
				{
					if(!fb.memPos.containsKey(((CondSetIns)ins).src1))
					{
						fb.memPos.put(((CondSetIns)ins).src1, fb.memSize);
						fb.memSize += ir.bytes.get("addr");
					}
				}
			}
		}
		bb.allocated = true;
		if(bb.to != null && !bb.to.allocated)
		{
			allocMem(fb, bb.to);
		}
		if(bb.ifFalse != null && !bb.ifFalse.allocated)
		{
			allocMem(fb, bb.ifFalse);
		}
		if(bb.ifTrue != null && !bb.ifTrue.allocated)
		{
			allocMem(fb, bb.ifTrue);
		}
	}
	
	private int isReg(String a)
	{
		if(a.substring(0, 1).equals("@"))
			return 2;
		if(a.substring(0, 1).equals("$"))
			return 1;
		return 0;
	}
}
