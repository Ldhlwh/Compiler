package NASM;

import IR.BasicBlock;
import IR.FuncBlock;
import IR.IRGenerator;
import IR.Instructions.*;
import ScopeCheck.Instances.VariIns;

import java.io.*;
import java.util.*;

import static java.lang.System.err;
import static java.lang.System.exit;

public class NASMBuilder
{
	public IRGenerator ir;
	public PrintStream o;
	
	private String temp = "r15";
	private String temp2 = "r14";
	
	public int regNum = 8; // MAXED = 12 (rsp, rbp, r14, r15 excluded)
	public ArrayList<String> realReg = new ArrayList<>();
	
	public Set<String> paramReg = new HashSet<>();
	
	
	public boolean printTake = false;
	public boolean printAllocMem = true;
	public boolean printInOutDefUse = true;
	public boolean printColor = true;
	public boolean printTime = false;
	
	private void regMatch()
	{
		if(regNum > 0)
			realReg.add("r10");
		if(regNum > 1)
			realReg.add("r11");
		if(regNum > 2)
			realReg.add("r12");
		if(regNum > 3)
			realReg.add("r13");
		if(regNum > 4)
			realReg.add("rdi");
		if(regNum > 5)
			realReg.add("rsi");
		if(regNum > 6)
			realReg.add("r8");
		if(regNum > 7)
			realReg.add("r9");
		
		paramReg.add("rdi");
		paramReg.add("rsi");
		paramReg.add("rdx");
		paramReg.add("rcx");
		paramReg.add("r8");
		paramReg.add("r9");
	}
	
	private boolean isParamReg(String reg)
	{
		return paramReg.contains(reg);
	}
	
	public NASMBuilder(boolean submit)
	{
		regMatch();
		
		if(submit)
		{
			o = System.out;
			printTake = printAllocMem = printColor = printInOutDefUse = false;
		}
		else
		{
			try
			{
				o = new PrintStream("C:\\Users\\qydyx\\Desktop\\my.asm");
			}
			catch(Throwable throwable)
			{
				System.err.printf("Exception thrown.\n");
				exit(1);
			}
		}
		
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
		o.printf("\t\textern\t\tscanf\n");
		o.printf("\t\textern\t\tsprintf\n");
		o.printf("\t\textern\t\tsscanf\n");
		o.printf("\t\textern\t\tstrcpy\n");
		o.printf("\t\textern\t\tstrcat\n");
		o.printf("\t\textern\t\tstrcmp\n");
		o.printf("\t\textern\t\tord\n");
		o.printf("\t\textern\t\tstrlen\n");
		
		o.printf("\n\t\tsection\t\t.data\n");
		o.printf("_getInt:\t\tdb\t\t\"%%lld\", 0\n");
		o.printf("_getStr:\t\tdb\t\t\"%%s\", 0\n");
		
		o.printf("\n\t\tsection\t\t.bss\n");
		
		for(Map.Entry<String, VariIns> entry : ir.topScope.variMap.entrySet())
		{
			o.printf("@" + entry.getKey() + ":");
			o.printf("\t\tresq\t\t1\n");
		}
		
		o.printf("\n\t\tsection\t\t.text\n");
		
		for(FuncBlock fb : ir.funcBlock)
		{
			makeBlockList(fb, fb.entry);
			makeFrom(fb);
			allocMem(fb, fb.entry);
			//blockLivenessAnalysis(fb, fb.entry);
			
			long st = System.currentTimeMillis();
			
			livenessAnalysis(fb);
			
			long mid = System.currentTimeMillis();
			
			regAlloc(fb, fb.entry);
			
			long ed = System.currentTimeMillis();
			
			if(printTime)
			{
				System.err.println("LA Time : " + (mid - st) + "ms");
				System.err.println("RA Time : " + (ed - mid) + "ms");
			}
			if(printColor)
			{
				System.err.printf("------ Colored : %s ------\n", fb.funcName);
				System.err.printf("Total : %d\nColored : %d\nUncolored : %d\n\n", fb.memSize / 8, fb.color.size(), fb.memSize / 8 - fb.color.size());
			}
			
			if(printAllocMem)
			{
				System.err.printf("------ AllocMem & RegAlloc Func : %s ------\n", fb.funcName);
				for(Map.Entry<String, Integer> entry : fb.memPos.entrySet())
				{
					if(fb.color.containsKey(entry.getKey()))
					{
						System.err.printf("%10s : %d\t\t%s\n", entry.getKey(), -entry.getValue() - 8, realReg.get(fb.color.get(entry.getKey())));
					}
					else
					{
						System.err.printf("%10s : %d\n", entry.getKey(), -entry.getValue() - 8);
					}
				}
				System.err.println();
			}
		}
		
		for(FuncBlock fb : ir.funcBlock)
		{
			if(fb.used || fb.funcName.equals("__init"))
			{
				generate(fb);
			}
		}
	}
	
	private void makeBlockList(FuncBlock fb, BasicBlock bb)
	{
		Queue<BasicBlock> queue = new LinkedList<>();
		((LinkedList<BasicBlock>)queue).push(fb.entry);
		fb.entry.added = true;
		while(!queue.isEmpty())
		{
			BasicBlock temp = queue.poll();
			fb.blockList.add(temp);
			
			int insSize = temp.insList.size();
			if(insSize > 1 && temp.insList.get(insSize - 1).insName.equals("jump")
					&& temp.insList.get(insSize - 2).insName.equals("jump"))
			{
				temp.to = ((JumpIns)temp.insList.get(insSize - 2)).toBlock;
				temp.insList.remove(insSize - 1);
			}
			if(temp.to != null && !temp.to.added)
			{
				((LinkedList<BasicBlock>)queue).push(temp.to);
				temp.to.added = true;
			}
			if(temp.ifFalse != null && !temp.ifFalse.added)
			{
				((LinkedList<BasicBlock>)queue).push(temp.ifFalse);
				temp.ifFalse.added = true;
			}
			if(temp.ifTrue != null && !temp.ifTrue.added)
			{
				((LinkedList<BasicBlock>)queue).push(temp.ifTrue);
				temp.ifTrue.added = true;
			}
		}
		/*
		fb.blockList.add(bb);
		bb.added = true;
		if(bb.to != null && !bb.to.added)
			makeBlockList(fb, bb.to);
		if(bb.ifFalse != null && !bb.ifFalse.added)
			makeBlockList(fb, bb.ifFalse);
		if(bb.ifTrue != null && !bb.ifTrue.added)
			makeBlockList(fb, bb.ifTrue);
		*/
	}
	
	private void makeFrom(FuncBlock fb)
	{
		int blockSize = fb.blockList.size();
		for(int i = 0; i < blockSize; i++)
		{
			BasicBlock bb = fb.blockList.get(i);
			if(bb.to != null)
				bb.to.from.add(bb);
			if(bb.ifFalse != null)
				bb.ifFalse.from.add(bb);
			if(bb.ifTrue != null)
				bb.ifTrue.from.add(bb);
		}
	}
	
	
	public void generate(FuncBlock fb)
	{
		//System.err.println(fb.funcName + ":");
		int blockSize = fb.blockList.size();
		for(int now = 0; now < blockSize; now++)
		{
			BasicBlock bb = fb.blockList.get(now);
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
				
				for(String p : bb.ofFunc.param)
				{
					String reg = getReg(bb.ofFunc, p);
					if(reg != null)
					{
						o.printf("\t\tmov\t\t%s, qword[%s]\n", reg, "rbp - " + (8 + bb.ofFunc.memPos.get(p)));
						fb.take.put(reg, p);
					}
				}
			}
			
			int max = bb.insList.size();
			for(int i = 0; i < max; i++)
			{
				//printTake(bb);
				Ins ins = bb.insList.get(i);
				if(ins instanceof JumpIns)
				{
					if(ins.insName.equals("ret"))
					{
						String rtn = ((JumpIns)ins).src;
						int choice = isReg(rtn);
						if(choice == 0)
						{
							o.printf("\t\tmov\t\trax, %s\n", rtn);
						}
						else if(choice == 1)
						{
							String reg = getReg(bb.ofFunc, rtn);
							if(reg == null)
							{
								String pos = "rbp - " + (bb.ofFunc.memPos.get(rtn) + 8);
								o.printf("\t\tmov\t\trax, qword[%s]\n", pos);
							}
							else
							{
								check(bb, rtn);
								if(!reg.equals("rax"))
									o.printf("\t\tmov\t\trax, %s\n", reg);
							}
						}
						else if(choice == 2)
						{
							String reg = getReg(bb.ofFunc, rtn);
							if(reg == null)
							{
								o.printf("\t\tmov\t\trax, qword[%s]\n", rtn);
							}
							else
							{
								check(bb, rtn);
								if(!reg.equals("rax"))
									o.printf("\t\tmov\t\trax, %s\n", reg);
							}
						}
						o.printf("\t\tleave\n");
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
								String reg = getReg(bb.ofFunc, rtn);
								if(reg == null)
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(rtn));
									o.printf("\t\tcmp\t\tqword[%s], 1\n", pos);
								}
								else
								{
									check(bb, rtn);
									o.printf("\t\tcmp\t\t%s, 1\n", reg);
								}
								
								o.printf("\t\tje\t\t%s\n", ((JumpIns)ins).ifTrue);
								o.printf("\t\tjmp\t\t%s\n", ((JumpIns)ins).ifFalse);
							}
							else if(choice == 2)
							{
								String reg = getReg(bb.ofFunc, rtn);
								if(reg == null)
								{
									o.printf("\t\tcmp\t\tqword[%s], 1\n", rtn);
								}
								else
								{
									check(bb, rtn);
									o.printf("\t\tcmp\t\t%s, 1\n", reg);
								}
								o.printf("\t\tje\t\t%s\n", ((JumpIns)ins).ifTrue);
								o.printf("\t\tjmp\t\t%s\n", ((JumpIns)ins).ifFalse);
							}
						}
						else
						{
							Ins lastIns = bb.insList.get(i - 1);
							if(!(((CondSetIns)lastIns).dest.equals(((JumpIns)ins).cond)))
							{
								System.err.println("ERROR");
								exit(1);
							}
							String type = lastIns.insName;
							String src1 = ((CondSetIns)lastIns).src1;
							String src2 = ((CondSetIns)lastIns).src2;
							int choice = isReg(src1);
							if(choice == 0)
							{
								o.printf("\t\tmov\t\t%s, %s\n", temp, src1);
								src1 = temp;
							}
							else if(choice == 1)
							{
								String reg = getReg(bb.ofFunc, src1);
								if(reg == null)
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(src1));
									if(lastIns.insName.length() == 5)
										o.printf("\t\tmov\t\t%sd, dword[%s]\n", temp, pos);
									else
										o.printf("\t\tmov\t\t%s, qword[%s]\n", temp, pos);
									src1 = temp;
								}
								else
								{
									check(bb, src1);
									src1 = reg;
								}
							}
							else if(choice == 2)
							{
								String reg = getReg(bb.ofFunc, src1);
								if(reg == null)
								{
									if(lastIns.insName.length() == 5)
										o.printf("\t\tmov\t\t%sd, dword[%s]\n", temp, src1);
									else
										o.printf("\t\tmov\t\t%s, qword[%s]\n", temp, src1);
									src1 = temp;
								}
								else
								{
									check(bb, src1);
									src1 = reg;
								}
							}
							
							choice = isReg(src2);
							if(choice == 0)
							{
								if(lastIns.insName.length() == 5)
									o.printf("\t\tcmp\t\t%sd, %s\n", src1, src2);
								else
									o.printf("\t\tcmp\t\t%s, %s\n", src1, src2);
							}
							else if(choice == 1)
							{
								String reg = getReg(bb.ofFunc, src2);
								if(reg == null)
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(src2));
									if(lastIns.insName.length() == 5)
										o.printf("\t\tcmp\t\t%sd, dword[%s]\n", src1, pos);
									else
										o.printf("\t\tcmp\t\t%s, qword[%s]\n", src1, pos);
								}
								else
								{
									check(bb, src2);
									o.printf("\t\tcmp\t\t%s, %s\n", src1, reg);
								}
							}
							else if(choice == 2)
							{
								String reg = getReg(bb.ofFunc, src2);
								if(reg == null)
								{
									if(lastIns.insName.length() == 5)
										o.printf("\t\tcmp\t\t%sd, dword[%s]\n", src1, src2);
									else
										o.printf("\t\tcmp\t\t%s, qword[%s]\n", src1, src2);
								}
								else
								{
									check(bb, src2);
									o.printf("\t\tcmp\t\t%s, %s\n", src1, reg);
								}
							}
							
							if(type.substring(0, 3).equals("slt"))
								o.printf("\t\tjl\t\t%s\n", ((JumpIns)ins).ifTrue);
							else if(type.substring(0, 3).equals("sgt"))
								o.printf("\t\tjg\t\t%s\n", ((JumpIns)ins).ifTrue);
							else if(type.substring(0, 3).equals("sle"))
								o.printf("\t\tjle\t\t%s\n", ((JumpIns)ins).ifTrue);
							else if(type.substring(0, 3).equals("sge"))
								o.printf("\t\tjge\t\t%s\n", ((JumpIns)ins).ifTrue);
							else if(type.substring(0, 3).equals("seq"))
								o.printf("\t\tje\t\t%s\n", ((JumpIns)ins).ifTrue);
							else if(type.substring(0, 3).equals("sne"))
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
							String reg = getReg(bb.ofFunc, ((MemAccIns)ins).size);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((MemAccIns)ins).size));
								src = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((MemAccIns)ins).size);
								src = reg;
								if(isParamReg(reg))
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((MemAccIns)ins).size));
									src = "qword[" + pos + "]";
								}
							}
						}
						else if(choice == 2)
						{
							String reg = getReg(bb.ofFunc, ((MemAccIns)ins).size);
							if(reg == null)
							{
								src = "qword[" + ((MemAccIns)ins).size + "]";
							}
							else
							{
								check(bb, ((MemAccIns)ins).size);
								src = reg;
								if(isParamReg(reg))
								{
									src = "qword[" + ((MemAccIns)ins).size + "]";
								}
							}
						}
						int c2 = isReg(((MemAccIns)ins).dest);
						String s2 = null, s2b = null;
						if(c2 == 1)
						{
							String reg = getReg(bb.ofFunc, ((MemAccIns)ins).dest);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((MemAccIns)ins).dest));
								s2b = s2 = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((MemAccIns)ins).dest);
								s2b = s2 = reg;
								if(isParamReg(reg))
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((MemAccIns)ins).dest));
									s2 = "qword[" + pos + "]";
								}
							}
						}
						else if(c2 == 2)
						{
							String reg = getReg(bb.ofFunc, ((MemAccIns)ins).dest);
							if(reg == null)
							{
								s2b = s2 = "qword[" + ((MemAccIns)ins).dest + "]";
							}
							else
							{
								check(bb, ((MemAccIns)ins).dest);
								s2b = s2 = reg;
								if(isParamReg(reg))
								{
									s2 = "qword[" + ((MemAccIns)ins).dest + "]";
								}
							}
						}
						storeAll(bb);
						o.printf("\t\tmov\t\trdi, %s\n", src);
						o.printf("\t\tcall\t\tmalloc\n");
						loadAll(bb);
						o.printf("\t\tmov\t\t%s, rax\n", s2b);
					}
					else if(ins.insName.equals("store"))
					{
						String size = ((MemAccIns)ins).size;
						String offset = ((MemAccIns)ins).offset + "";
						int choice = isReg(((MemAccIns)ins).addr);
						String addr = null;
						if(choice == 1)
						{
							String reg = getReg(bb.ofFunc, ((MemAccIns)ins).addr);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((MemAccIns)ins).addr));
								addr = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((MemAccIns)ins).addr);
								addr = reg;
							}
							o.printf("\t\tmov\t\t%s, %s\n", temp, addr);
						}
						else if(choice == 2)
						{
							String reg = getReg(bb.ofFunc, ((MemAccIns)ins).addr);
							if(reg == null)
							{
								addr = ((MemAccIns)ins).addr;
							}
							else
							{
								check(bb, ((MemAccIns)ins).addr);
								addr = reg;
							}
							
							o.printf("\t\tlea\t\t%s, [%s]\n", temp, addr);
						}
						
						
						int cs = isReg(((MemAccIns)ins).src);
						String src = null;
						if(cs == 0)
						{
							src = ((MemAccIns)ins).src;
						}
						else if(cs == 1)
						{
							String reg = getReg(bb.ofFunc, ((MemAccIns)ins).src);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((MemAccIns)ins).src));
								src = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((MemAccIns)ins).src);
								src = reg;
							}
						}
						else if(cs == 2)
						{
							String reg = getReg(bb.ofFunc, ((MemAccIns)ins).src);
							if(reg == null)
							{
								src = "qword[" + ((MemAccIns)ins).src + "]";
							}
							else
							{
								check(bb, ((MemAccIns)ins).src);
								src = reg;
							}
						}
						o.printf("\t\tmov\t\t%s, %s\n", temp2, src);
						if(size.equals("1"))
							o.printf("\t\tmov\t\t[%s + %d], %s\n", temp, ((MemAccIns)ins).offset, temp2);
						else if(size.equals("8"))
							o.printf("\t\tmov\t\tqword[%s + %d], %s\n", temp, ((MemAccIns)ins).offset, temp2);
					}
					else if(ins.insName.equals("load"))
					{
						String size = ((MemAccIns)ins).size;
						String offset = ((MemAccIns)ins).offset + "";
						int choice = isReg(((MemAccIns)ins).addr);
						String addr = null;
						if(choice == 1)
						{
							String reg = getReg(bb.ofFunc, ((MemAccIns)ins).addr);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((MemAccIns)ins).addr));
								addr = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((MemAccIns)ins).addr);
								addr = reg;
							}
						}
						else if(choice == 2)
						{
							String reg = getReg(bb.ofFunc, ((MemAccIns)ins).addr);
							if(reg == null)
							{
								addr = "qword[" + ((MemAccIns)ins).addr + "]";
							}
							else
							{
								check(bb, ((MemAccIns)ins).addr);
								addr = reg;
							}
						}
						o.printf("\t\tmov\t\t%s, %s\n", temp, addr);
						o.printf("\t\tadd\t\t%s, %s\n", temp, offset);
						
						int cs = isReg(((MemAccIns)ins).dest);
						String dest = null;
						if(cs == 0)
						{
							dest = ((MemAccIns)ins).dest;
						}
						else if(cs == 1)
						{
							String reg = getReg(bb.ofFunc, ((MemAccIns)ins).dest);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((MemAccIns)ins).dest));
								dest = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((MemAccIns)ins).dest);
								dest = reg;
							}
						}
						else if(cs == 2)
						{
							String reg = getReg(bb.ofFunc, ((MemAccIns)ins).dest);
							if(reg == null)
							{
								dest = "qword[" + ((MemAccIns)ins).dest + "]";
							}
							else
							{
								check(bb, ((MemAccIns)ins).dest);
								dest = reg;
							}
						}
						if(size.equals("1"))
							o.printf("\t\tmov\t\t%sb, [%s]\n", temp2, temp);
						else if(size.equals("8"))
							o.printf("\t\tmov\t\t%s, qword[%s]\n", temp2, temp);
						o.printf("\t\tmov\t\t%s, %s\n", dest, temp2);
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
							String reg = getReg(bb.ofFunc, str);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(str));
								op = "qword[" + pos + "]";
							}
							else
							{
								check(bb, str);
								op = reg;
								if(isParamReg(reg))
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(str));
									op = "qword[" + pos + "]";
								}
							}
						}
						else if(choice == 2)
						{
							String reg = getReg(bb.ofFunc, str);
							if(reg == null)
							{
								op = "qword[" + str + "]";
							}
							else
							{
								check(bb, str);
								op = reg;
								if(isParamReg(reg))
								{
									op = "qword[" + str + "]";
								}
							}
						}
						
						if(funcName.equals("println"))
						{
							storeAll(bb);
							o.printf("\t\tmov\t\trdi, %s\n", op);
							o.printf("\t\tcall\t\tputs\n");
							loadAll(bb);
						}
						else if(funcName.equals("print"))
						{
							storeAll(bb);
							o.printf("\t\tmov\t\trdi, _getStr\n");
							o.printf("\t\tmov\t\trsi, %s\n", op);
							o.printf("\t\tmov\t\trax, 0\n");
							o.printf("\t\tcall\t\tprintf\n");
							loadAll(bb);
						}
					}
					
					else if(funcName.equals("getString"))
					{
						int choice = isReg(((FuncCallIns)ins).dest);
						String dest = null, destb = null, desta = null;
						if(choice == 1)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).dest);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest));
								desta = destb = dest = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).dest);
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest));
								desta = "qword[" + pos + "]";
								destb = dest = reg;
								if(isParamReg(reg))
								{
									dest = desta;
								}
							}
						}
						else if(choice == 2)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).dest);
							if(reg == null)
							{
								desta = destb = dest = "qword[" + ((FuncCallIns)ins).dest + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).dest);
								desta = "qword[" + ((FuncCallIns)ins).dest + "]";
								destb = dest = reg;
								if(isParamReg(reg))
								{
									dest = desta;
								}
							}
						}
						storeAll(bb);
						o.printf("\t\tmov\t\trdi, 256\n");
						o.printf("\t\tcall\t\tmalloc\n");
						loadAll(bb);
						o.printf("\t\tmov\t\t%s, rax\n", destb);
						storeAll(bb);
						o.printf("\t\tmov\t\trdi, _getStr\n");
						o.printf("\t\tmov\t\trsi, %s\n", dest);
						o.printf("\t\tmov\t\trax, 0\n");
						o.printf("\t\tcall\t\tscanf\n");
						loadAll(bb);
						//o.printf("\t\tmov\t\t%s, %s\n", destb, desta);
					}
					
					else if(funcName.equals("getInt"))
					{
						int choice = isReg(((FuncCallIns)ins).dest);
						String dest = null;
						boolean fff = false;
						if(choice == 1)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).dest);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest));
								dest = "[" + pos + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).dest);
								String vr = bb.ofFunc.take.get(reg);
								dest = "[" + "rbp - " + (8 + bb.ofFunc.memPos.get(vr)) + "]";
								fff = true;
							}
						}
						else if(choice == 2)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).dest);
							if(reg == null)
							{
								dest = "[" + ((FuncCallIns)ins).dest + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).dest);
								String vr = bb.ofFunc.take.get(reg);
								dest = "[" + ((FuncCallIns)ins).dest + "]";
								fff = true;
							}
						}
						storeAll(bb);
						o.printf("\t\tmov\t\trdi, _getInt\n");
						o.printf("\t\tlea\t\trax, %s\n", dest);
						o.printf("\t\tmov\t\trsi, rax\n");
						o.printf("\t\tmov\t\trax, 0\n");
						o.printf("\t\tcall\t\tscanf\n");
						loadAll(bb);
						if(fff)
						{
							o.printf("\t\tmov\t\tqword%s, %s\n", dest, getReg(bb.ofFunc, ((FuncCallIns)ins).dest));
						}
					}
					
					else if(funcName.equals("toString"))
					{
						int cs = isReg(((FuncCallIns)ins).ops.get(0));
						int cd = isReg(((FuncCallIns)ins).dest);
						String src = null, dest = null, destb = null;
						if(cs == 0)
						{
							src = ((FuncCallIns)ins).ops.get(0);
						}
						else if(cs == 1)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).ops.get(0));
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).ops.get(0)));
								src = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).ops.get(0));
								src = reg;
								if(isParamReg(reg))
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).ops.get(0)));
									src = "qword[" + pos + "]";
								}
							}
						}
						else if(cs == 2)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).ops.get(0));
							if(reg == null)
							{
								src = "qword[" + ((FuncCallIns)ins).ops.get(0) + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).ops.get(0));
								src = reg;
								if(isParamReg(reg))
								{
									src = "qword[" + ((FuncCallIns)ins).ops.get(0) + "]";
								}
							}
						}
						
						if(cd == 1)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).dest);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest));
								destb = dest = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).dest);
								destb = dest = reg;
								if(isParamReg(reg))
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest));
									dest = "qword[" + pos + "]";
								}
							}
						}
						else if(cd == 2)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).dest);
							if(reg == null)
							{
								destb = dest = "qword[" + ((FuncCallIns)ins).dest + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).dest);
								destb = dest = reg;
								if(isParamReg(reg))
								{
									dest = "qword[" + ((FuncCallIns)ins).dest + "]";
								}
							}
						}
						storeAll(bb);
						o.printf("\t\tmov\t\trdi, 256\n");
						o.printf("\t\tcall\t\tmalloc\n");
						loadAll(bb);
						o.printf("\t\tmov\t\t%s, rax\n", destb);
						storeAll(bb);
						o.printf("\t\tmov\t\trdi, %s\n", dest);
						o.printf("\t\tmov\t\trsi, _getInt\n");
						o.printf("\t\tmov\t\trdx, %s\n", src);
						o.printf("\t\tmov\t\trax, 0\n");
						o.printf("\t\tcall\t\tsprintf\n");
						loadAll(bb);
					}
					
					else if(funcName.equals("string.copy"))
					{
						int cd = isReg(((FuncCallIns)ins).dest);
						int cs = isReg(((FuncCallIns)ins).ops.get(0));
						String dest = null, src = null, destb = null;
						if(cs == 1)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).ops.get(0));
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).ops.get(0)));
								src = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).ops.get(0));
								src = reg;
								if(isParamReg(reg))
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).ops.get(0)));
									src = "qword[" + pos + "]";
								}
							}
						}
						else if(cs == 2)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).ops.get(0));
							if(reg == null)
							{
								src = "qword[" + ((FuncCallIns)ins).ops.get(0) + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).ops.get(0));
								src = reg;
								if(isParamReg(reg))
								{
									src = "qword[" + ((FuncCallIns)ins).ops.get(0) + "]";
								}
							}
						}
						if(cd == 1)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).dest);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest));
								destb = dest = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).dest);
								destb = dest = reg;
								if(isParamReg(reg))
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest));
									dest = "qword[" + pos + "]";
								}
							}
						}
						else if(cd == 2)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).dest);
							if(reg == null)
							{
								destb = dest = "qword[" + ((FuncCallIns)ins).dest + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).dest);
								destb = dest = reg;
								if(isParamReg(reg))
								{
									dest = "qword[" + ((FuncCallIns)ins).dest + "]";
								}
							}
						}
						storeAll(bb);
						o.printf("\t\tmov\t\trdi, 256\n");
						o.printf("\t\tcall\t\tmalloc\n");
						loadAll(bb);
						o.printf("\t\tmov\t\t%s, rax\n", destb);
						
						storeAll(bb);
						o.printf("\t\tmov\t\trdi, rax\n");
						o.printf("\t\tmov\t\trsi, %s\n", src);
						o.printf("\t\tcall\t\tstrcpy\n");
						loadAll(bb, dest);
					}
					
					else if(funcName.equals("string.cat"))
					{
						int cd = isReg(((FuncCallIns)ins).dest);
						int cs1 = isReg(((FuncCallIns)ins).ops.get(0));
						int cs2 = isReg(((FuncCallIns)ins).ops.get(1));
						String dest = null, src1 = null, src2 = null, destb = null;
						if(cs1 == 1)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).ops.get(0));
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).ops.get(0)));
								src1 = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).ops.get(0));
								src1 = reg;
								if(isParamReg(reg))
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).ops.get(0)));
									src1 = "qword[" + pos + "]";
								}
							}
						}
						else if(cs1 == 2)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).ops.get(0));
							if(reg == null)
							{
								src1 = "qword[" + ((FuncCallIns)ins).ops.get(0) + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).ops.get(0));
								src1 = reg;
								if(isParamReg(reg))
								{
									src1 = "qword[" + ((FuncCallIns)ins).ops.get(0) + "]";
								}
							}
						}
						if(cs2 == 1)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).ops.get(1));
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).ops.get(1)));
								src2 = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).ops.get(1));
								src2 = reg;
								if(isParamReg(reg))
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).ops.get(1)));
									src2 = "qword[" + pos + "]";
								}
							}
						}
						else if(cs2 == 2)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).ops.get(1));
							if(reg == null)
							{
								src2 = "qword[" + ((FuncCallIns)ins).ops.get(1) + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).ops.get(1));
								src2 = reg;
								if(isParamReg(reg))
								{
									src2 = "qword[" + ((FuncCallIns)ins).ops.get(1) + "]";
								}
							}
						}
						if(cd == 1)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).dest);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest));
								destb = dest = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).dest);
								destb = dest = reg;
								if(isParamReg(reg))
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest));
									dest = "qword[" + pos + "]";
								}
							}
						}
						else if(cd == 2)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).dest);
							if(reg == null)
							{
								destb = dest = "qword[" + ((FuncCallIns)ins).dest + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).dest);
								destb = dest = reg;
								if(isParamReg(reg))
								{
									dest = "qword[" + ((FuncCallIns)ins).dest + "]";
								}
							}
						}
						storeAll(bb);
						o.printf("\t\tmov\t\trdi, 256\n");
						o.printf("\t\tcall\t\tmalloc\n");
						loadAll(bb);
						o.printf("\t\tmov\t\t%s, rax\n", destb);
						storeAll(bb);
						o.printf("\t\tmov\t\trdi, %s\n", dest);
						o.printf("\t\tmov\t\trsi, %s\n", src1);
						o.printf("\t\tcall\t\tstrcpy\n");
						loadAll(bb);
						storeAll(bb);
						o.printf("\t\tmov\t\trdi, %s\n", dest);
						o.printf("\t\tmov\t\trsi, %s\n", src2);
						o.printf("\t\tcall\t\tstrcat\n");
						loadAll(bb);
					}
					else if(funcName.equals("string.cmp"))
					{
						int cd = isReg(((FuncCallIns)ins).dest);
						int cs1 = isReg(((FuncCallIns)ins).ops.get(0));
						int cs2 = isReg(((FuncCallIns)ins).ops.get(1));
						String dest = null, src1 = null, src2 = null, destb = null;
						if(cs1 == 1)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).ops.get(0));
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).ops.get(0)));
								src1 = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).ops.get(0));
								src1 = reg;
								if(isParamReg(reg))
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).ops.get(0)));
									src1 = "qword[" + pos + "]";
								}
							}
						}
						else if(cs1 == 2)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).ops.get(0));
							if(reg == null)
							{
								src1 = "qword[" + ((FuncCallIns)ins).ops.get(0) + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).ops.get(0));
								src1 = reg;
								if(isParamReg(reg))
								{
									src1 = "qword[" + ((FuncCallIns)ins).ops.get(0) + "]";
								}
							}
						}
						if(cs2 == 1)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).ops.get(1));
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).ops.get(1)));
								src2 = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).ops.get(1));
								src2 = reg;
								if(isParamReg(reg))
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).ops.get(1)));
									src2 = "qword[" + pos + "]";
								}
							}
						}
						else if(cs2 == 2)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).ops.get(1));
							if(reg == null)
							{
								src2 = "qword[" + ((FuncCallIns)ins).ops.get(1) + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).ops.get(1));
								src2 = reg;
								if(isParamReg(reg))
								{
									src2 = "qword[" + ((FuncCallIns)ins).ops.get(1) + "]";
								}
							}
						}
						if(cd == 1)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).dest);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest));
								destb = dest = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).dest);
								destb = dest = reg;
								if(isParamReg(reg))
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest));
									dest = "qword[" + pos + "]";
								}
							}
						}
						else if(cd == 2)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).dest);
							if(reg == null)
							{
								destb = dest = "qword[" + ((FuncCallIns)ins).dest + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).dest);
								destb = dest = reg;
								if(isParamReg(reg))
								{
									dest = "qword[" + ((FuncCallIns)ins).dest + "]";
								}
							}
						}
						storeAll(bb);
						o.printf("\t\tmov\t\trdi, %s\n", src1);
						o.printf("\t\tmov\t\trsi, %s\n", src2);
						o.printf("\t\tcall\t\tstrcmp\n");
						loadAll(bb);
						o.printf("\t\tmov\t\t%s, rax\n", destb);
					}
					
					else if(funcName.equals("string.parseInt"))
					{
						int cs = isReg(((FuncCallIns)ins).ops.get(0));
						int cd = isReg(((FuncCallIns)ins).dest);
						String src = null, dest = null, srcb = null, destb = null;
						if(cs == 1)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).ops.get(0));
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).ops.get(0)));
								srcb = src = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).ops.get(0));
								srcb = src = reg;
								if(isParamReg(reg))
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).ops.get(0)));
									src = "qword[" + pos + "]";
								}
							}
						}
						else if(cs == 2)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).ops.get(0));
							if(reg == null)
							{
								srcb = src = "qword[" + ((FuncCallIns)ins).ops.get(0) + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).ops.get(0));
								srcb = src = reg;
								if(isParamReg(reg))
								{
									src = "qword[" + ((FuncCallIns)ins).ops.get(0) + "]";
								}
							}
						}
						
						if(cd == 1)
						{
							
							String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest));
							dest = "[" + pos + "]";
						}
						else if(cd == 2)
						{
							dest = "[" + ((FuncCallIns)ins).dest + "]";
						}
						
						
						o.printf("\t\tlea\t\t%s, %s\n", temp, dest);
						storeAll(bb);
						o.printf("\t\tmov\t\trdi, %s\n", src);
						o.printf("\t\tmov\t\trsi, _getInt\n");
						o.printf("\t\tmov\t\trdx, %s\n", temp);
						o.printf("\t\tmov\t\trax, 0\n");
						o.printf("\t\tcall\t\tsscanf\n");
						loadAll(bb);
						
						String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).dest);
						if(reg == null)
						{
							o.printf("\t\tmov\t\t%s, qword[%s]\n", temp2, temp);
							o.printf("\t\tmov\t\tqword%s, %s\n", dest, temp2);
						}
						else
						{
							check(bb, ((FuncCallIns)ins).dest);
							o.printf("\t\tmov\t\t%s, qword[%s]\n", reg, temp);
						}
					}
					
					else if(funcName.equals("string.length"))
					{
						int cs = isReg(((FuncCallIns)ins).ops.get(0));
						int cd = isReg(((FuncCallIns)ins).dest);
						
						String src = null, dest = null, destb = null;
						if(cs == 1)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).ops.get(0));
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).ops.get(0)));
								src = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).ops.get(0));
								src = reg;
								if(isParamReg(reg))
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).ops.get(0)));
									src = "qword[" + pos + "]";
								}
							}
						}
						else if(cs == 2)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).ops.get(0));
							if(reg == null)
							{
								src = "qword[" + ((FuncCallIns)ins).ops.get(0) + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).ops.get(0));
								src = reg;
								if(isParamReg(reg))
								{
									src = "qword[" + ((FuncCallIns)ins).ops.get(0) + "]";
								}
							}
						}
						if(cd == 1)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).dest);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest));
								destb = dest = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).dest);
								destb = dest = reg;
								if(isParamReg(reg))
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest));
									dest = "qword[" + pos + "]";
								}
							}
						}
						else if(cd == 2)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).dest);
							if(reg == null)
							{
								destb = dest = "qword[" + ((FuncCallIns)ins).dest + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).dest);
								destb = dest = reg;
								if(isParamReg(reg))
								{
									dest = "qword[" + ((FuncCallIns)ins).dest + "]";
								}
							}
						}
						storeAll(bb);
						o.printf("\t\tmov\t\trdi, %s\n", src);
						o.printf("\t\tcall\t\tstrlen\n");
						loadAll(bb);
						o.printf("\t\tmov\t\t%s, rax\n", destb);
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
						
						int choice = isReg(((FuncCallIns)ins).dest);
						String dest = null, destb = null;
						if(choice == 1)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).dest);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest));
								destb = dest = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).dest);
								destb = dest = reg;
								if(isParamReg(reg))
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest));
									dest = "qword[" + pos + "]";
								}
							}
						}
						else if(choice == 2)
						{
							String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).dest);
							if(reg == null)
							{
								destb = dest = "qword[" + ((FuncCallIns)ins).dest + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).dest);
								destb = dest = reg;
								if(isParamReg(reg))
								{
									dest = "qword[" + ((FuncCallIns)ins).dest + "]";
								}
							}
						}
						int up = (paramNum < 6) ? paramNum : 6;
						storeAll(bb);
						for(int j = 0; j < up; j++)
						{
							String str = ((FuncCallIns)ins).ops.get(j);
							int aac = isReg(str);
							String op = null;
							if(aac == 0)
							{
								op = str;
							}
							else if(aac == 1)
							{
								String reg = getReg(bb.ofFunc, str);
								if(reg == null)
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(str));
									op = "qword[" + pos + "]";
								}
								else
								{
									check(bb, str);
									op = reg;
									if(isParamReg(reg))
									{
										String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(str));
										op = "qword[" + pos + "]";
									}
								}
							}
							else if(aac == 2)
							{
								String reg = getReg(bb.ofFunc, str);
								if(reg == null)
								{
									op = "qword[" + str + "]";
								}
								else
								{
									check(bb, str);
									op = reg;
									if(isParamReg(reg))
									{
										op = "qword[" + str + "]";
									}
								}
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
								int ac = isReg(str);
								String op = null;
								if(ac == 0)
								{
									op = str;
								}
								else if(ac == 1)
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(str));
									op = "qword[" + pos + "]";
								}
								else if(ac == 2)
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
						loadAll(bb);
						o.printf("\t\tmov\t\t%s, rax\n", destb);
					}
				}
				else if(ins instanceof MovIns)
				{
					int choice = isReg(((MovIns)ins).dest);
					int srcc = isReg(((MovIns)ins).src);
					String src = "", dest = null;
					
					if(choice > 0 && srcc > 0)
					{
						String regd = getReg(bb.ofFunc, ((MovIns)ins).dest);
						String regs = getReg(bb.ofFunc, ((MovIns)ins).src);
						if(regd != null
								&& regd.equals(regs))
						{
							/*
							//check(bb, ((MovIns)ins).dest);
							String rR = getReg(bb.ofFunc, vr);
							String tT = getTake(bb, rR);
							if(tT == null)
							{
								load(bb, vr);
							}
							else if(!tT.equals(vr))
							{
								//store(bb, rR);
								load(bb, vr);
							}
							return;
							String nsrc = null;
							if(isReg(((MovIns)ins).src) == 1)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((MovIns)ins).src));
								nsrc = "qword[" + pos + "]";
							}
							else if(isReg(((MovIns)ins).src) == 2)
							{
								nsrc = "qword[" + ((MovIns)ins).src + "]";
							}
							o.printf("\t\tmov\t\t%s, %s\n", regd, nsrc);*/
							bb.ofFunc.take.put(regd, ((MovIns)ins).dest);
							continue;
						}
					}
					if(srcc == 0)
					{
						src = ((MovIns)ins).src;
					}
					else if(srcc == 1)
					{
						String reg = getReg(bb.ofFunc, ((MovIns)ins).src);
						if(reg == null)
						{
							String spos = "rbp - " + (8 + bb.ofFunc.memPos.get(((MovIns)ins).src));
							src = "qword[" + spos + "]";
							o.printf("\t\tmov\t\t%s, %s\n", temp, src);
							src = temp;
						}
						else
						{
							check(bb, ((MovIns)ins).src);
							src = reg;
						}
					}
					else if(srcc == 2)
					{
						String reg = getReg(bb.ofFunc, ((MovIns)ins).src);
						if(reg == null)
						{
							src = "qword[" + ((MovIns)ins).src + "]";
							o.printf("\t\tmov\t\t%s, %s\n", temp, src);
							src = temp;
						}
						else
						{
							check(bb, ((MovIns)ins).src);
							src = reg;
						}
					}
					
					if(choice == 1)
					{
						String reg = getReg(bb.ofFunc, ((MovIns)ins).dest);
						if(reg == null)
						{
							String spos = "rbp - " + (8 + bb.ofFunc.memPos.get(((MovIns)ins).dest));
							dest = "qword[" + spos + "]";
						}
						else
						{
							check(bb, ((MovIns)ins).dest);
							//System.err.printf(bb.ofFunc.take.get(getReg(bb.ofFunc, ((MovIns)ins).dest)));
							dest = reg;
						}
						o.printf("\t\tmov\t\t%s, %s\n", dest, src);
					}
					else if(choice == 2)
					{
						String reg = getReg(bb.ofFunc, ((MovIns)ins).dest);
						if(reg == null)
						{
							dest = "qword[" + ((MovIns)ins).dest + "]";
						}
						else
						{
							check(bb, ((MovIns)ins).dest);
							dest = reg;
						}
						o.printf("\t\tmov\t\t%s, %s\n", dest, src);
					}
				}
				else if(ins instanceof ArithIns)
				{
					if(!((ArithIns)ins).dest.equals(((ArithIns)ins).src1))
					{
						System.err.println("$dest is not the same as $src1");
						exit(1);
					}
					if(ins.insName.equals("neg"))
					{
						int choice = isReg(((ArithIns)ins).src1);
						String pos = "";
						String src1 = null;
						if(choice == 1)
						{
							String reg = getReg(bb.ofFunc, ((ArithIns)ins).src1);
							if(reg == null)
							{
								pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((ArithIns)ins).src1));
								src1 = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((ArithIns)ins).src1);
								src1 = reg;
							}
						}
						else if(choice == 2)
						{
							String reg = getReg(bb.ofFunc, ((ArithIns)ins).src1);
							if(reg == null)
							{
								src1 = "qword[" + ((ArithIns)ins).src1 + "]";
							}
							else
							{
								check(bb, ((ArithIns)ins).src1);
								src1 = reg;
							}
						}
						o.printf("\t\tneg\t\t%s\n", src1);
					}
					else if(ins.insName.equals("add")
							|| ins.insName.equals("sub")
							|| ins.insName.equals("mul"))
					{
						int c1 = isReg(((ArithIns)ins).src1);
						int c2 = isReg(((ArithIns)ins).src2);
						String src1 = "", src2 = "", r1 = "";
						if(c1 == 1)
						{
							String reg = getReg(bb.ofFunc, ((ArithIns)ins).src1);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((ArithIns)ins).src1));
								r1 = src1 = "qword[" + pos + "]";
								if(ins.insName.equals("mul"))
								{
									o.printf("\t\tmov\t\t%s, %s\n", temp, src1);
									r1 = temp;
								}
							}
							else
							{
								check(bb, ((ArithIns)ins).src1);
								r1 = src1 = reg;
							}
						}
						else if(c1 == 2)
						{
							String reg = getReg(bb.ofFunc, ((ArithIns)ins).src1);
							if(reg == null)
							{
								r1 = src1 = "qword[" + ((ArithIns)ins).src1 + "]";
								if(ins.insName.equals("mul"))
								{
									o.printf("\t\tmov\t\t%s, %s\n", temp, src1);
									r1 = temp;
								}
							}
							else
							{
								check(bb, ((ArithIns)ins).src1);
								r1 = src1 = reg;
							}
						}
						
						if(c2 == 0)
						{
							src2 = ((ArithIns)ins).src2;
						}
						else if(c2 == 1)
						{
							String reg = getReg(bb.ofFunc, ((ArithIns)ins).src2);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((ArithIns)ins).src2));
								src2 = "qword[" + pos + "]";
								o.printf("\t\tmov\t\t%s, %s\n", temp2, src2);
								src2 = temp2;
							}
							else
							{
								check(bb, ((ArithIns)ins).src2);
								src2 = reg;
							}
						}
						else if(c2 == 2)
						{
							String reg = getReg(bb.ofFunc, ((ArithIns)ins).src2);
							if(reg == null)
							{
								src2 = "qword[" + ((ArithIns)ins).src2 + "]";
								o.printf("\t\tmov\t\t%s, %s\n", temp2, src2);
								src2 = temp2;
							}
							else
							{
								check(bb, ((ArithIns)ins).src2);
								src2 = reg;
							}
						}
						
						String insName = ins.insName;
						if(insName.equals("mul"))
							insName = "imul";
						o.printf("\t\t%s\t\t%s, %s\n", insName, r1, src2);
						if(r1.equals(temp))
						{
							o.printf("\t\tmov\t\t%s, %s\n", src1, temp);
						}
					}
					else if(ins.insName.equals("div")
							|| ins.insName.equals("rem"))
					{
						int choice = isReg(((ArithIns)ins).src1);
						String pos = null, pos2 = null;
						String src1 = null, src2 = null;
						if(choice == 0)
						{
							o.printf("\t\tmov\t\trax, %s\n", ((ArithIns)ins).src1);
						}
						else if(choice == 1)
						{
							String reg = getReg(bb.ofFunc, ((ArithIns)ins).src1);
							if(reg == null)
							{
								pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((ArithIns)ins).src1));
								src1 = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((ArithIns)ins).src1);
								src1 = reg;
							}
							o.printf("\t\tmov\t\trax, %s\n", src1);
						}
						else if(choice == 2)
						{
							String reg = getReg(bb.ofFunc, ((ArithIns)ins).src1);
							if(reg == null)
							{
								src1 = "qword[" + ((ArithIns)ins).src1 + "]";
							}
							else
							{
								check(bb, ((ArithIns)ins).src1);
								src1 = reg;
							}
							o.printf("\t\tmov\t\trax, %s\n", src1);
						}
						o.printf("\t\tmov\t\trdx, 0\n");
						int c2 = isReg(((ArithIns)ins).src2);
						if(c2 == 0)
						{
							o.printf("\t\tmov\t\t%s, %s\n", temp, ((ArithIns)ins).src2);
							o.printf("\t\tdiv\t\t%s\n", temp);
						}
						else if(c2 == 1)
						{
							String reg = getReg(bb.ofFunc, ((ArithIns)ins).src2);
							if(reg == null)
							{
								pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((ArithIns)ins).src2));
								src2 = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((ArithIns)ins).src2);
								src2 = reg;
							}
							o.printf("\t\tdiv\t\t%s\n", src2);
						}
						else if(c2 == 2)
						{
							String reg = getReg(bb.ofFunc, ((ArithIns)ins).src2);
							if(reg == null)
							{
								src2 = "qword[" + ((ArithIns)ins).src2 + "]";
							}
							else
							{
								check(bb, ((ArithIns)ins).src2);
								src2 = reg;
							}
							o.printf("\t\tdiv\t\t%s\n", src2);
						}
						
						if(ins.insName.equals("div"))
						{
							o.printf("\t\tmov\t\t%s, rax\n", src1);
						}
						else if(ins.insName.equals("rem"))
						{
							o.printf("\t\tmov\t\t%s, rdx\n", src1);
						}
					}
				}
				else if(ins instanceof BitIns)
				{
					if(ins.insName.equals("not"))
					{
						int choice = isReg(((BitIns)ins).src1);
						String pos = "", src1 = null;
						if(choice == 1)
						{
							String reg = getReg(bb.ofFunc, ((BitIns)ins).src1);
							if(reg == null)
							{
								pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((BitIns)ins).src1));
								src1 = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((BitIns)ins).src1);
								src1 = reg;
							}
						}
						else if(choice == 2)
						{
							String reg = getReg(bb.ofFunc, ((BitIns)ins).src1);
							if(reg == null)
							{
								src1 = "qword[" + ((BitIns)ins).src1 + "]";
							}
							else
							{
								check(bb, ((BitIns)ins).src1);
								src1 = reg;
							}
						}
						o.printf("\t\tnot\t\t%s\n", src1);
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
							String reg = getReg(bb.ofFunc, ((BitIns)ins).src2);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((BitIns)ins).src2));
								src2 = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((BitIns)ins).src2);
								src2 = reg;
							}
						}
						else if(c2 == 2)
						{
							String reg = getReg(bb.ofFunc, ((BitIns)ins).src2);
							if(reg == null)
							{
								src2 = "qword[" + ((BitIns)ins).src2 + "]";
							}
							else
							{
								check(bb, ((BitIns)ins).src2);
								src2 = reg;
							}
						}
						o.printf("\t\tmov\t\trcx, %s\n", src2);
						
						int c1 = isReg(((BitIns)ins).src1);
						String src1 = "";
						if(c1 == 0)
						{
							src1 = ((BitIns)ins).src1;
						}
						else if(c1 == 1)
						{
							String reg = getReg(bb.ofFunc, ((BitIns)ins).src1);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((BitIns)ins).src1));
								src1 = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((BitIns)ins).src1);
								src1 = reg;
							}
						}
						else if(c1 == 2)
						{
							String reg = getReg(bb.ofFunc, ((BitIns)ins).src1);
							if(reg == null)
							{
								src1 = "qword[" + ((BitIns)ins).src1 + "]";
							}
							else
							{
								check(bb, ((BitIns)ins).src1);
								src1 = reg;
							}
						}
						
						o.printf("\t\t%s\t\t%s, cl\n", ins.insName, src1);
					}
					else
					{
						int c1 = isReg(((BitIns)ins).src1);
						int c2 = isReg(((BitIns)ins).src2);
						String src1 = "", src2 = "";
						if(c1 == 0)
						{
							src1 = ((BitIns)ins).src1;
						}
						else if(c1 == 1)
						{
							String reg = getReg(bb.ofFunc, ((BitIns)ins).src1);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((BitIns)ins).src1));
								src1 = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((BitIns)ins).src1);
								src1 = reg;
							}
						}
						else if(c1 == 2)
						{
							String reg = getReg(bb.ofFunc, ((BitIns)ins).src1);
							if(reg == null)
							{
								src1 = "qword[" + ((BitIns)ins).src1 + "]";
							}
							else
							{
								check(bb, ((BitIns)ins).src1);
								src1 = reg;
							}
						}
						
						if(c2 == 0)
						{
							src2 = ((BitIns)ins).src2;
						}
						else if(c2 == 1)
						{
							String reg = getReg(bb.ofFunc, ((BitIns)ins).src2);
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((BitIns)ins).src2));
								src2 = "qword[" + pos + "]";
							}
							else
							{
								check(bb, ((BitIns)ins).src2);
								src2 = reg;
							}
						}
						else if(c2 == 2)
						{
							String reg = getReg(bb.ofFunc, ((BitIns)ins).src2);
							if(reg == null)
							{
								src2 = "qword[" + ((BitIns)ins).src2 + "]";
							}
							else
							{
								check(bb, ((BitIns)ins).src2);
								src2 = reg;
							}
						}
						if(src1.substring(0, 1).equals("q")
								&& src2.substring(0, 1).equals("q"))
						{
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
			//printTake(bb);
			if(printTake)
			{
				System.err.println(bb.blockID + ":");
				for(Map.Entry<String, String> entry : bb.ofFunc.take.entrySet())
				{
					String from = entry.getKey();
					String to = entry.getValue();
					System.err.println(from + " " + to);
				}
			}
		}
	}
	
	private void allocMem(FuncBlock fb, BasicBlock bb)
	{
		//fb.blockList.add(bb);
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
		int insSize = bb.insList.size();
		for(int i = 0; i < insSize - 1; i++)
		{
			Ins ins = bb.insList.get(i);
			if(ins instanceof JumpIns)
			{
				if(bb.to != null)
					ins.succ.add(bb.to.insList.get(0));
				if(bb.ifTrue != null)
					ins.succ.add(bb.ifTrue.insList.get(0));
				if(bb.ifFalse != null)
					ins.succ.add(bb.ifFalse.insList.get(0));
			}
			else
			{
				ins.succ.add(bb.insList.get(i + 1));
			}
			
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
				String funcName = ((FuncCallIns)ins).funcName;
				
				for(FuncBlock f : ir.funcBlock)
				{
					if(f.funcName.equals(funcName))
					{
						if(!f.funcName.equals("_alloc"))
						{
							f.used = true;
							break;
						}
						else
						{
							if(!fb.funcName.equals("_alloc"))
								f.used = true;
							break;
						}
					}
				}
				
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
	
	public static boolean isSetEqual(Set set1, Set set2)
	{
		
		if (set1 == null && set2 == null)
		{
			return true; // Both are null
		}
		
		if (set1 == null || set2 == null || set1.size() != set2.size())
		{
			return false;
		}
		
		Iterator ite1 = set1.iterator();
		Iterator ite2 = set2.iterator();
		
		boolean isFullEqual = true;
		
		while (ite2.hasNext())
		{
			if (!set1.contains(ite2.next()))
			{
				isFullEqual = false;
			}
		}
		
		return isFullEqual;
	}
	
	private void blockLivenessAnalysis(FuncBlock fb, BasicBlock bb)
	{
		for(BasicBlock nowb : fb.blockList)
		{
			block_def_use(nowb);
		}
		int blockSize = fb.blockList.size();
		while(true)
		{
			for(int i = blockSize - 1; i >= 0; i--)
			{
				//System.err.println(i);
				BasicBlock curBlock = fb.blockList.get(i);
				curBlock.inp = curBlock.in;
				curBlock.outp = curBlock.out;
				curBlock.in = new HashSet<>();
				curBlock.out = new HashSet<>();
				
				if(curBlock.to != null)
					curBlock.out.addAll(curBlock.to.in);
				if(curBlock.ifTrue != null)
					curBlock.out.addAll(curBlock.ifTrue.in);
				if(curBlock.ifFalse != null)
					curBlock.out.addAll(curBlock.ifFalse.in);
				
				curBlock.in.addAll(curBlock.use);
				Iterator it = curBlock.out.iterator();
				while(it.hasNext())
				{
					Object temp = it.next();
					if(!curBlock.def.contains(temp))
					{
						curBlock.in.add(temp.toString());
					}
				}
			}
			boolean flag = true;
			for(int i = 0; i < blockSize; i++)
			{
				BasicBlock curBlock = fb.blockList.get(i);
				if(!isSetEqual(curBlock.in, curBlock.inp) || !isSetEqual(curBlock.out, curBlock.outp))
				{
					flag = false;
					break;
				}
			}
			if(flag)
			{
				break;
			}
		}
		boolean print = false;
		if(print)
		{
			for(int i = 0; i < blockSize; i++)
			{
				System.err.printf("[%s]:\n", fb.blockList.get(i).blockID);
				printSet(fb.blockList.get(i).in);
				printSet(fb.blockList.get(i).out);
				System.err.println();
			}
		}
	}
	
	private void block_def_use(BasicBlock bb)
	{
		for(Ins ins : bb.insList)
		{
			if(ins instanceof JumpIns)
			{
				if(ins.insName.equals("ret"))
				{
					if(isReg(((JumpIns)ins).src) > 0)
						bb.use.add(((JumpIns)ins).src);
				}
				else if(ins.insName.equals("br"))
				{
					if(isReg(((JumpIns)ins).cond) > 0)
						bb.use.add(((JumpIns)ins).cond);
				}
			}
			else if(ins instanceof MemAccIns)
			{
				if(ins.insName.equals("store"))
				{
					if(isReg(((MemAccIns)ins).addr) > 0)
						bb.use.add(((MemAccIns)ins).addr);
					if(isReg(((MemAccIns)ins).src) > 0)
						bb.use.add(((MemAccIns)ins).src);
				}
				else if(ins.insName.equals("load"))
				{
					if(isReg(((MemAccIns)ins).dest) > 0)
						bb.def.add(((MemAccIns)ins).dest);
					if(isReg(((MemAccIns)ins).addr) > 0)
						bb.use.add(((MemAccIns)ins).addr);
				}
				else if(ins.insName.equals("alloc"))
				{
					if(isReg(((MemAccIns)ins).dest) > 0)
						bb.def.add(((MemAccIns)ins).dest);
					if(isReg(((MemAccIns)ins).size) > 0)
						bb.use.add(((MemAccIns)ins).size);
				}
			}
			else if(ins instanceof FuncCallIns)
			{
				if(isReg(((FuncCallIns)ins).dest) > 0)
					bb.def.add(((FuncCallIns)ins).dest);
				for(String op : ((FuncCallIns)ins).ops)
				{
					if(isReg(op) > 0)
						bb.use.add(op);
				}
			}
			else if(ins instanceof MovIns)
			{
				if(isReg(((MovIns)ins).dest) > 0)
					bb.def.add(((MovIns)ins).dest);
				if(isReg(((MovIns)ins).src) > 0)
					bb.use.add(((MovIns)ins).src);
			}
			else if(ins instanceof ArithIns)
			{
				if(isReg(((ArithIns)ins).dest) > 0)
					bb.def.add(((ArithIns)ins).dest);
				if(isReg(((ArithIns)ins).src1) > 0)
					bb.use.add(((ArithIns)ins).src1);
				if(!ins.insName.equals("neg"))
					if(isReg(((ArithIns)ins).src2) > 0)
						bb.use.add(((ArithIns)ins).src2);
			}
			else if(ins instanceof BitIns)
			{
				if(isReg(((BitIns)ins).dest) > 0)
					bb.def.add(((BitIns)ins).dest);
				if(isReg(((BitIns)ins).src1) > 0)
					bb.use.add(((BitIns)ins).src1);
				if(!ins.insName.equals("not"))
					if(isReg(((BitIns)ins).src2) > 0)
						bb.use.add(((BitIns)ins).src2);
			}
			else if(ins instanceof CondSetIns)
			{
				if(isReg(((CondSetIns)ins).dest) > 0)
					bb.def.add(((CondSetIns)ins).dest);
				if(isReg(((CondSetIns)ins).src1) > 0)
					bb.use.add(((CondSetIns)ins).src1);
				if(isReg(((CondSetIns)ins).src2) > 0)
					bb.use.add(((CondSetIns)ins).src2);
			}
		}
	}
	
	private void livenessAnalysis(FuncBlock fb)
	{
		int blockSize = fb.blockList.size();
		for(int j = 0; j < blockSize; j++)
		{
			BasicBlock cur = fb.blockList.get(j);
			int insSize = cur.insList.size();
			for(int i = 0; i < insSize; i++)
			{
				Ins ins = cur.insList.get(i);
				def_use(ins);
			}
			Ins first = fb.entry.insList.get(0);
			first.def.addAll(fb.param);
		}
		while(true)
		{
			for(int j = blockSize - 1; j >= 0; j--)
			{
				//System.err.println(blockSize);
				BasicBlock cur = fb.blockList.get(j);
				int insSize = cur.insList.size();
				boolean jumped = false;
				//System.err.println(cur.blockID + "analyzed : " + cur.analyzed);
				for(int i = insSize - 1; i >= 0; i--)
				{
					Ins ins = cur.insList.get(i);
					ins.inp = ins.in;
					ins.outp = ins.out;
					ins.in = new HashSet<>();
					ins.out = new HashSet<>();
					
					if(ins instanceof JumpIns)
					{
						if(ins.insName.equals("jump"))
						{
							System.err.printf("jump to : %s\n", ((JumpIns)ins).target);
							ins.out.addAll(cur.to.insList.get(0).in);
						}
						else if(ins.insName.equals("br"))
						{
							ins.out.addAll(cur.ifTrue.insList.get(0).in);
							ins.out.addAll(cur.ifFalse.insList.get(0).in);
						}
					}
					else
					{
						Ins nextIns = cur.insList.get(i + 1);
						ins.out.addAll(nextIns.in);
					}
					
					ins.in.addAll(ins.use);
					Iterator it = ins.out.iterator();
					while(it.hasNext())
					{
						Object temp = it.next();
						if(!ins.def.contains(temp))
						{
							ins.in.add(temp.toString());
						}
					}
					
					
					if((cur.to != null || cur.ifFalse != null || cur.ifTrue != null)
							&& cur.analyzed && cur.allequal)
					{
						if(i == insSize - 2)
						{
							if(isSetEqual(ins.out, ins.outp) && isSetEqual(ins.in, ins.inp))
							{
								jumped = true;
								//System.err.println("JUMP");
								break;
							}
						}
					}
					
				}
				if(!jumped)
					cur.analyzed = true;
			}
			boolean flag = true;
			for(int j = 0; j < blockSize; j++)
			{
				BasicBlock cur = fb.blockList.get(j);
				int insSize = cur.insList.size();
				for(int i = 0; i < insSize; i++)
				{
					Ins ins = cur.insList.get(i);
					//(!isSetEqual(ins.in, ins.inp) || !isSetEqual(ins.out, ins.outp))
					if(ins.in.size() != ins.inp.size() || ins.out.size() != ins.outp.size())
					{
						//System.err.printf("Changed : %s\n", cur.blockID);
						flag = false;
						break;
					}
				}
				if(!flag)
				{
					cur.allequal = false;
					break;
				}
				cur.allequal = true;
			}
			if(flag)
				break;
		}
		
		if(printInOutDefUse)
		{
			for(int j = 0; j < blockSize; j++)
			{
				BasicBlock cur = fb.blockList.get(j);
				System.err.println("[" + cur.blockID + "]");
				int insSize = cur.insList.size();
				for(int i = 0; i < insSize; i++)
				{
					Ins ins = cur.insList.get(i);
					System.err.printf("ins[%d]:\nin: ", i);
					printSet(ins.in);
					System.err.printf("out: ");
					printSet(ins.out);
					System.err.printf("def: ");
					printSet(ins.def);
					System.err.printf("use: ");
					printSet(ins.use);
					System.err.println("\n");
				}
			}
		}
	}
	
	private void def_use(Ins ins)
	{
		if(ins instanceof JumpIns)
		{
			if(ins.insName.equals("ret"))
			{
				if(isReg(((JumpIns)ins).src) > 0)
					ins.use.add(((JumpIns)ins).src);
			}
			else if(ins.insName.equals("br"))
			{
				if(isReg(((JumpIns)ins).cond) > 0)
					ins.use.add(((JumpIns)ins).cond);
			}
		}
		else if(ins instanceof MemAccIns)
		{
			if(ins.insName.equals("store"))
			{
				if(isReg(((MemAccIns)ins).addr) > 0)
					ins.use.add(((MemAccIns)ins).addr);
				if(isReg(((MemAccIns)ins).src) > 0)
					ins.use.add(((MemAccIns)ins).src);
			}
			else if(ins.insName.equals("load"))
			{
				if(isReg(((MemAccIns)ins).dest) > 0)
					ins.def.add(((MemAccIns)ins).dest);
				if(isReg(((MemAccIns)ins).addr) > 0)
					ins.use.add(((MemAccIns)ins).addr);
			}
			else if(ins.insName.equals("alloc"))
			{
				if(isReg(((MemAccIns)ins).dest) > 0)
					ins.def.add(((MemAccIns)ins).dest);
				if(isReg(((MemAccIns)ins).size) > 0)
					ins.use.add(((MemAccIns)ins).size);
			}
		}
		else if(ins instanceof FuncCallIns)
		{
			if(isReg(((FuncCallIns)ins).dest) > 0)
			{
				ins.def.add(((FuncCallIns)ins).dest);
				ins.use.add(((FuncCallIns)ins).dest);
			}
			for(String op : ((FuncCallIns)ins).ops)
			{
				if(isReg(op) > 0)
					ins.use.add(op);
			}
		}
		else if(ins instanceof MovIns)
		{
			if(isReg(((MovIns)ins).dest) > 0)
				ins.def.add(((MovIns)ins).dest);
			if(isReg(((MovIns)ins).src) > 0)
				ins.use.add(((MovIns)ins).src);
		}
		else if(ins instanceof ArithIns)
		{
			if(isReg(((ArithIns)ins).dest) > 0)
				ins.def.add(((ArithIns)ins).dest);
			if(isReg(((ArithIns)ins).src1) > 0)
				ins.use.add(((ArithIns)ins).src1);
			if(!ins.insName.equals("neg"))
				if(isReg(((ArithIns)ins).src2) > 0)
					ins.use.add(((ArithIns)ins).src2);
		}
		else if(ins instanceof BitIns)
		{
			if(isReg(((BitIns)ins).dest) > 0)
				ins.def.add(((BitIns)ins).dest);
			if(isReg(((BitIns)ins).src1) > 0)
				ins.use.add(((BitIns)ins).src1);
			if(!ins.insName.equals("not"))
				if(isReg(((BitIns)ins).src2) > 0)
					ins.use.add(((BitIns)ins).src2);
		}
		else if(ins instanceof CondSetIns)
		{
			if(isReg(((CondSetIns)ins).dest) > 0)
				ins.def.add(((CondSetIns)ins).dest);
			if(isReg(((CondSetIns)ins).src1) > 0)
				ins.use.add(((CondSetIns)ins).src1);
			if(isReg(((CondSetIns)ins).src2) > 0)
				ins.use.add(((CondSetIns)ins).src2);
		}
	}
	
	private void printSet(Set set)
	{
		Iterator it = set.iterator();
		while(it.hasNext())
		{
			System.err.printf("%s ", it.next().toString());
		}
		System.err.println();
	}
	
	private void regAlloc(FuncBlock fb, BasicBlock bb)
	{
		for(BasicBlock curBlock : fb.blockList)
		{
			for(Ins curIns : curBlock.insList)
			{
				for(String d : curIns.def)
				{
					if(isReg(d) == 2)
						continue;
					Set<String> temp;
					if(!fb.itf.containsKey(d))
					{
						temp = new HashSet<>();
						fb.itf.put(d, temp);
					}
					else
					{
						temp = fb.itf.get(d);
					}
					for(String o : curIns.out)
					{
						if(o.equals(d)
								|| isReg(o) == 2)
							continue;
						temp.add(o);
					}
				}
				for(String o : curIns.out)
				{
					if(isReg(o) == 2)
						continue;
					Set<String> temp;
					if(!fb.itf.containsKey(o))
					{
						temp = new HashSet<>();
						fb.itf.put(o, temp);
					}
					else
						temp = fb.itf.get(o);
					for(String d : curIns.def)
					{
						if(o.equals(d)
								|| isReg(d) == 2)
							continue;
						temp.add(d);
					}
				}
			}
		}
		
		for(Map.Entry<String, Set<String>> entry : fb.itf.entrySet())
		{
			fb.deg.put(entry.getKey(), entry.getValue().size());
			fb.inGraph.put(entry.getKey(), true);
		}
		Stack<String> stack = new Stack<>();
		while(true)
		{
			boolean flag = false;
			for(Map.Entry<String, Integer> entry : fb.deg.entrySet())
			{
				if(entry.getValue() < regNum
						&& fb.inGraph.get(entry.getKey()))
				{
					stack.push(entry.getKey());
					fb.inGraph.put(entry.getKey(), false);
					Set<String> temp = fb.itf.get(entry.getKey());
					for(String str : temp)
					{
						fb.deg.put(str, fb.deg.get(str) - 1);
					}
					flag = true;
					break;
				}
			}
			if(!flag)
				break;
			
			// Have not put those with degree > regNum in stack
		}
		
		int allNum = fb.deg.size();
		int canColor = stack.size();
		int cannotColor = 0;
		for(Map.Entry<String, Boolean> entry : fb.inGraph.entrySet())
		{
			if(entry.getValue())
				cannotColor++;
		}
		for(Map.Entry<String, Integer> entry : fb.deg.entrySet())
		{
			fb.inGraph.put(entry.getKey(), false);
		}
		
		while(!stack.empty())
		{
			String now = stack.pop();
			//System.err.println("now = " + now);
			Boolean[] ok = new Boolean[regNum];
			for(int i = 0; i < regNum; i++)
				ok[i] = true;
			
			fb.deg.put(now, fb.itf.get(now).size());
			fb.inGraph.put(now, true);
			Set<String> temp = fb.itf.get(now);
			for(String adj : temp)
			{
				if(fb.inGraph.get(adj))
				{
					ok[fb.color.get(adj)] = false;
				}
			}
			
			for(int i = 0; i < regNum; i++)
			{
				if(ok[i])
				{
					fb.color.put(now, i);
					break;
				}
			}
		}
	}
	
	private String getReg(FuncBlock fb, String vr)
	{
		if(!fb.color.containsKey(vr))
		{
			return null;
		}
		return realReg.get(fb.color.get(vr));
	}
	
	private String getTake(BasicBlock bb, String reg)
	{
		if(!bb.ofFunc.take.containsKey(reg)
				|| bb.ofFunc.take.get(reg) == null)
		{
			//System.err.println(reg + " -- null");
			bb.ofFunc.take.put(reg, null);
			return null;
		}
		return bb.ofFunc.take.get(reg);
	}

	private void store(BasicBlock bb, String reg)
	{
		if(isReg(getTake(bb, reg)) == 1)
		{
			String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(getTake(bb, reg)));
			o.printf("\t\tmov\t\tqword[%s], %s\n", pos, reg);
		}
		else
		{
			o.printf("\t\tmov\t\tqword[%s], %s\n", getTake(bb, reg), reg);
		}
	}

	private void load(BasicBlock bb, String vr)
	{
		if(isReg(vr) == 1)
		{
			String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(vr));
			o.printf("\t\tmov\t\t%s, qword[%s]\n", getReg(bb.ofFunc, vr), pos);
		}
		else
		{
			o.printf("\t\tmov\t\t%s, qword[%s]\n", getReg(bb.ofFunc, vr), vr);
		}
		//System.err.println(getReg(bb.ofFunc, vr) + "--" + vr);
		bb.ofFunc.take.put(getReg(bb.ofFunc, vr), vr);
	}
	
	private void check(BasicBlock bb, String vr)
	{
		String rR = getReg(bb.ofFunc, vr);
		//String tT = getTake(bb, rR);
		bb.ofFunc.take.put(getReg(bb.ofFunc, vr), vr);
		/*if(tT == null)
		{
			//load(bb, vr);
		}
		else if(!tT.equals(vr))
		{
			//store(bb, rR);
			//load(bb, vr);
		}*/
		return;
	}
	
	private void storeAll(BasicBlock bb)
	{
		for(Map.Entry<String, String> entry : bb.ofFunc.take.entrySet())
		{
			if(entry.getValue() != null)
			{
				//System.err.printf("%s - value = %s\n", bb.blockID, entry.getValue());
				store(bb, entry.getKey());
			}
		}
		//System.err.println();
	}
	
	private void storeAll(BasicBlock bb, String ex)
	{
		for(Map.Entry<String, String> entry : bb.ofFunc.take.entrySet())
		{
			if(entry.getValue() != null && !entry.getKey().equals(ex))
			{
				//System.err.printf("%s - value = %s\n", bb.blockID, entry.getValue());
				store(bb, entry.getKey());
			}
		}
		//System.err.println();
	}
	
	private void loadAll(BasicBlock bb)
	{
		for(Map.Entry<String, String> entry : bb.ofFunc.take.entrySet())
		{
			if(entry.getValue() != null)
			{
				load(bb, entry.getValue());
			}
		}
	}
	
	private void loadAll(BasicBlock bb, String ex)
	{
		for(Map.Entry<String, String> entry : bb.ofFunc.take.entrySet())
		{
			if(entry.getValue() != null
					&& entry.getKey() != null
					&& !(entry.getKey().equals(ex)))
			{
				load(bb, entry.getValue());
			}
		}
	}
	
	private void loadAll(BasicBlock bb, String ex, String ex2)
	{
		for(Map.Entry<String, String> entry : bb.ofFunc.take.entrySet())
		{
			if(entry.getValue() != null
					&& !entry.getKey().equals(ex)
					&& !entry.getKey().equals(ex2))
			{
				load(bb, entry.getValue());
			}
		}
	}
	
	private void printTake(BasicBlock bb)
	{
		System.err.println("------ TAKE ------");
		for(Map.Entry<String, String> entry : bb.ofFunc.take.entrySet())
		{
			System.err.printf("%s -> ", entry.getKey());
			if(entry.getValue() != null)
				System.err.printf("%s", entry.getValue());
			System.err.println();
		}
		System.err.println();
	}
}
