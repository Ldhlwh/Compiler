package NASM;

import IR.BasicBlock;
import IR.FuncBlock;
import IR.IRGenerator;
import IR.Instructions.*;
import ScopeCheck.Instances.VariIns;

import javax.swing.*;
import java.io.*;
import java.util.*;

import static java.lang.System.err;
import static java.lang.System.exit;

public class NASMBuilder
{
	public IRGenerator ir;
	public PrintStream o;
	
	//private String temp = "r14";	//r10, r11
	//private String temp2 = "r15";
	
	private String temp = "r10";
	private String temp2 = "r11";
	
	boolean liveOutOnly = true;
	
	public int regNum = 11; // MAXED = 12 (rsp, rbp, r14, r15 excluded)
	public ArrayList<String> realReg = new ArrayList<>();
	public Map<String, String> getD = new HashMap<>();
	public Map<String, String> getB = new HashMap<>();
	public ArrayList<String>  isCaller = new ArrayList<>();
	
	public Set<String> paramReg = new HashSet<>();
	public ArrayList<Data> data = new ArrayList<>();
	
	
	public boolean printTake = false;
	public boolean printAllocMem = true;
	public boolean printInOutDefUse = false;
	public boolean printColor = true;
	public boolean printTime = false;
	
	private void regMatch()
	{
		if(regNum > 0)
			realReg.add("r12");
		if(regNum > 1)
			realReg.add("r13");
		if(regNum > 2)
			realReg.add("r14");
		if(regNum > 3)
			realReg.add("r15");		// Safe : 4
		if(regNum > 4)
			realReg.add("rbx");		// Above are Callee-save
		if(regNum > 5)
			realReg.add("rcx");
		if(regNum > 6)
			realReg.add("rdx");
		if(regNum > 7)
			realReg.add("rdi");		// Parameter Reg : 8
		if(regNum > 8)
			realReg.add("rsi");		// Unknown Reg : 9
		if(regNum > 9)
			realReg.add("r8");
		if(regNum > 10)
			realReg.add("r9");		// shl, shr, div, rem : 11
		
		paramReg.add("rdi");
		paramReg.add("rsi");
		paramReg.add("rdx");
		paramReg.add("rcx");
		paramReg.add("r8");
		paramReg.add("r9");
		
		for(int i = 8; i < 16; i++)
		{
			getD.put("r" + i, "r" + i + "d");
			getB.put("r" + i, "r" + i + "b");
		}
		getD.put("rax", "eax");
		getD.put("rbx", "ebx");
		getD.put("rcx", "ecx");
		getD.put("rdx", "edx");
		getD.put("rsp", "esp");
		getD.put("rbp", "ebp");
		getD.put("rsi", "esi");
		getD.put("rdi", "edi");
		getB.put("rax", "al");
		getB.put("rbx", "bl");
		getB.put("rcx", "cl");
		getB.put("rdx", "dl");
		getB.put("rsp", "spl");
		getB.put("rbp", "bpl");
		getB.put("rsi", "sil");
		getB.put("rdi", "dil");
		
		//isCaller.add("rax");
		isCaller.add("rcx");
		isCaller.add("rdx");
		isCaller.add("rdi");
		isCaller.add("rsi");
		//isCaller.add("rsp");
		isCaller.add("r8");
		isCaller.add("r9");
		//isCaller.add("r10");
		//isCaller.add("r11");
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
			printTake = printAllocMem = printColor = printInOutDefUse = printTime = false;
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
		//o.printf("\t\textern\t\tsprintf\n");
		o.printf("\t\textern\t\tsscanf\n");
		//o.printf("\t\textern\t\tstrcpy\n");
		//o.printf("\t\textern\t\tstrcat\n");
		o.printf("\t\textern\t\tstrcmp\n");
		o.printf("\t\textern\t\tord\n");
		o.printf("\t\textern\t\tstrlen\n");
		o.printf("\t\textern\t\tsubstring\n");
		
		Data dt = new Data();
		dt.data += "_getInt\t\tdb\t\t\"%lld\", 0\n";
		data.add(dt);
		Data ndt = new Data();
		ndt.data += "_getStr:\t\tdb\t\t\"%s\", 0\n";
		data.add(ndt);
		
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
		
		o.printf("\n\t\tsection\t\t.data\n");
		for(Data d : data)
		{
			o.println(d.data);
		}
		
		printBuildIn();
	}
	
	private void makeBlockList(FuncBlock fb, BasicBlock bb)
	{
		//Queue<BasicBlock> queue = new LinkedList<>();
		//((LinkedList<BasicBlock>)queue).push(fb.entry);
		Stack<BasicBlock> queue = new Stack<>();
		queue.push(fb.entry);
		fb.entry.added = true;
		while(!queue.isEmpty())
		{
			//BasicBlock temp = queue.poll();
			BasicBlock temp = queue.pop();
			fb.blockList.add(temp);
			/*
			System.err.printf("%s", temp.blockID);
			if(temp.to != null)
				System.err.printf("\tto : %s", temp.to.blockID);
			if(temp.ifTrue != null)
				System.err.printf("\tifT : %s", temp.ifTrue.blockID);
			if(temp.ifFalse != null)
				System.err.printf("\tifF : %s", temp.ifFalse.blockID);
			System.err.println("\n");*/
			
			int insSize = temp.insList.size();
			if(insSize > 1 && temp.insList.get(insSize - 1).insName.equals("jump")
					&& temp.insList.get(insSize - 2).insName.equals("jump"))
			{
				temp.to = ((JumpIns)temp.insList.get(insSize - 2)).toBlock;
				temp.insList.remove(insSize - 1);
			}
			if(temp.to != null && !temp.to.added)
			{
				//((LinkedList<BasicBlock>)queue).push(temp.to);
				queue.push(temp.to);
				temp.to.added = true;
			}
			if(temp.ifTrue != null && !temp.ifTrue.added)
			{
				//((LinkedList<BasicBlock>)queue).push(temp.ifTrue);
				queue.push(temp.ifTrue);
				temp.ifTrue.added = true;
			}
			if(temp.ifFalse != null && !temp.ifFalse.added)
			{
				//((LinkedList<BasicBlock>)queue).push(temp.ifFalse);
				queue.push(temp.ifFalse);
				temp.ifFalse.added = true;
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
		int blockSize = fb.blockList.size();
		for(int now = 0; now < blockSize; now++)
		{
			BasicBlock bb = fb.blockList.get(now);
			if(bb.blockID.equals("__init"))
				o.printf("main:\n");
			else if(bb.blockID.equals("main"))
				o.printf("_main:\n");
			/*else if(now > 0
					&& bb.from.size() == 1
					&& fb.blockList.get(now - 1).insList.get(fb.blockList.get(now - 1).insList.size() - 1).insName.equals("jump")
					&& ((JumpIns)fb.blockList.get(now - 1).insList.get(fb.blockList.get(now - 1).insList.size() - 1)).target.equals(bb.blockID))
			{
				//fb.blockList.get(now - 1).insList.remove(fb.blockList.get(now - 1).insList.size() - 1);
			}*/
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
				int psize = bb.ofFunc.param.size();
				for(int i = 0; i < psize; i++)
				{
					String p = bb.ofFunc.param.get(i);
					String reg = getReg(bb.ofFunc, p);
					if(reg != null)
					{
						String to = "qword[rbp - " + (8 + bb.ofFunc.memPos.get(p)) + "]";
						o.printf("\t\tmov\t\t%s, %s\n", reg, to);
						fb.take.put(reg, p);
						//fb.dirty.put(reg, false);
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
						if(now < blockSize - 1)
						{
							BasicBlock next = fb.blockList.get(now + 1);
							if(!next.blockID.equals(((JumpIns)ins).target))
							{
								o.printf("\t\tjmp\t\t%s\n", ((JumpIns)ins).target);
							}
						}
						else
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
								if(now < blockSize - 1)
								{
									BasicBlock next = fb.blockList.get(now + 1);
									if(!next.blockID.equals(((JumpIns)ins).ifFalse))
									{
										o.printf("\t\tjmp\t\t%s\n", ((JumpIns)ins).ifFalse);
									}
								}
								else
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
								if(now < blockSize - 1)
								{
									BasicBlock next = fb.blockList.get(now + 1);
									if(!next.blockID.equals(((JumpIns)ins).ifFalse))
									{
										o.printf("\t\tjmp\t\t%s\n", ((JumpIns)ins).ifFalse);
									}
								}
								else
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
								if(now < blockSize - 1)
								{
									BasicBlock next = fb.blockList.get(now + 1);
									if(!next.blockID.equals(((JumpIns)ins).ifFalse))
									{
										o.printf("\t\tjmp\t\t%s\n", ((JumpIns)ins).ifFalse);
									}
								}
								else
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
									o.printf("\t\tcmp\t\t%s, %s\n", getD.get(src1), src2);
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
										o.printf("\t\tcmp\t\t%s, dword[%s]\n", getD.get(src1), pos);
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
										o.printf("\t\tcmp\t\t%s, dword[%s]\n", getD.get(src1), src2);
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
							if(now < blockSize - 1)
							{
								BasicBlock next = fb.blockList.get(now + 1);
								if(!next.blockID.equals(((JumpIns)ins).ifFalse))
								{
									o.printf("\t\tjmp\t\t%s\n", ((JumpIns)ins).ifFalse);
								}
							}
							else
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
									o.printf("\t\tmov\t\t%s, %s\n", src, reg);
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
									o.printf("\t\tmov\t\t%s, %s\n", src, reg);
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
						storeCaller(bb, ins);
						o.printf("\t\tmov\t\trdi, %s\n", src);
						o.printf("\t\tcall\t\tmalloc\n");
						loadCaller(bb, ins);
						o.printf("\t\tmov\t\t%s, rax\n", s2b);
					}
					else if(ins.insName.equals("storeStr"))
					{
						Data dd = new Data();
						data.add(dd);
						dd.data = dd.dataID + ":\t\tdb\t\t";
						for(int a : ((MemAccIns)ins).constStr)
						{
							dd.data += " " + a + ",";
						}
						dd.data += "\n";
						
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
								//fb.dirty.put(reg, true);
							}
							o.printf("\t\tmov\t\t%s, %s\n", addr, dd.dataID);
							
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
							
							//o.printf("\t\tlea\t\t%s, [%s]\n", temp, addr);
							o.printf("\t\tmov\t\tqword[%s], %s\n", addr, dd.dataID);
						}
						
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
						{
							o.printf("\t\tmov\t\t%s, 0\n", temp2);
							String str = getB.get(temp2);
							o.printf("\t\tmov\t\t%s, [%s]\n", str, temp);
						}
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
									o.printf("\t\tmov\t\t%s, %s\n", op, reg);
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
									o.printf("\t\tmov\t\t%s, %s\n", op, reg);
								}
							}
						}
						
						if(funcName.equals("println"))
						{
							storeCaller(bb, ins);
							o.printf("\t\tmov\t\trdi, %s\n", op);
							o.printf("\t\tcall\t\tputs\n");
							loadCaller(bb, ins);
						}
						else if(funcName.equals("print"))
						{
							storeCaller(bb, ins);
							o.printf("\t\tmov\t\trdi, _getStr\n");
							o.printf("\t\tmov\t\trsi, %s\n", op);
							o.printf("\t\tmov\t\trax, 0\n");
							o.printf("\t\tcall\t\tprintf\n");
							loadCaller(bb, ins);
						}
					}
					
					else if(funcName.equals("getString"))
					{
						int choice = isReg(((FuncCallIns)ins).dest);
						String dest = null, destb = null, desta = null;
						String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).dest);
						if(choice == 1)
						{
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
						storeCaller(bb, ins);
						o.printf("\t\tmov\t\trdi, 256\n");
						o.printf("\t\tcall\t\tmalloc\n");
						loadCaller(bb, ins);
						o.printf("\t\tmov\t\t%s, rax\n", destb);
						if(reg != null && isParamReg(reg))
						{
							o.printf("\t\tmov\t\t%s, %s\n", desta, reg);
						}
						storeCaller(bb, ins);
						o.printf("\t\tmov\t\trdi, _getStr\n");
						o.printf("\t\tmov\t\trsi, %s\n", dest);
						o.printf("\t\tmov\t\trax, 0\n");
						o.printf("\t\tcall\t\tscanf\n");
						loadCaller(bb, ins);
						if(isParamReg(reg))
						{
							o.printf("\t\tmov\t\t%s, %s\n", reg, dest);
						}
					}
					
					else if(funcName.equals("getInt"))
					{
						int choice = isReg(((FuncCallIns)ins).dest);
						String dest = null;
						boolean fff = false;
						String reg = getReg(bb.ofFunc, ((FuncCallIns)ins).dest);
						if(choice == 1)
						{
							if(reg == null)
							{
								String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest));
								dest = "[" + pos + "]";
							}
							else
							{
								check(bb, ((FuncCallIns)ins).dest);
								dest = "[" + "rbp - " + (8 + bb.ofFunc.memPos.get(((FuncCallIns)ins).dest)) + "]";
								fff = true;
							}
						}
						else if(choice == 2)
						{
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
						storeCaller(bb, ins);
						o.printf("\t\tmov\t\trdi, _getInt\n");
						o.printf("\t\tlea\t\trax, %s\n", dest);
						o.printf("\t\tmov\t\trsi, rax\n");
						o.printf("\t\tmov\t\trax, 0\n");
						o.printf("\t\tcall\t\tscanf\n");
						loadCaller(bb, ins);
						if(fff)
						{
							o.printf("\t\tmov\t\t%s, qword%S\n", reg, dest);
							//o.printf("\t\tmov\t\tqword%s, %s\n", dest, getReg(bb.ofFunc, ((FuncCallIns)ins).dest));
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
									o.printf("\t\tmov\t\t%s, %s\n", src, reg);
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
									o.printf("\t\tmov\t\t%s, %s\n", src, reg);
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
						storeCaller(bb, ins);
						o.printf("\t\tmov\t\trdi, %s\n", src);
						o.printf("\t\tcall\t\ttoString\n");
						loadCaller(bb, ins);
						o.printf("\t\tmov\t\t%s, rax\n", destb);
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
									o.printf("\t\tmov\t\t%s, %s\n", src1, reg);
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
									o.printf("\t\tmov\t\t%s, %s\n", src1, reg);
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
									o.printf("\t\tmov\t\t%s, %s\n", src2, reg);
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
									o.printf("\t\tmov\t\t%s, %s\n", src2, reg);
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
						storeCaller(bb, ins);
						o.printf("\t\tmov\t\trdi, %s\n", src1);
						o.printf("\t\tmov\t\trsi, %s\n", src2);
						o.printf("\t\tcall\t\tstring_cat\n");
						loadCaller(bb, ins);
						o.printf("\t\tmov\t\t%s, rax\n", destb);
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
									o.printf("\t\tmov\t\t%s, %s\n", src1, reg);
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
									o.printf("\t\tmov\t\t%s, %s\n", src1, reg);
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
									o.printf("\t\tmov\t\t%s, %s\n", src2, reg);
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
									o.printf("\t\tmov\t\t%s, %s\n", src2, reg);
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
						storeCaller(bb, ins);
						o.printf("\t\tmov\t\trdi, %s\n", src1);
						o.printf("\t\tmov\t\trsi, %s\n", src2);
						o.printf("\t\tcall\t\tstrcmp\n");
						loadCaller(bb, ins);
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
									o.printf("\t\tmov\t\t%s, %s\n", src, reg);
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
									o.printf("\t\tmov\t\t%s, %s\n", src, reg);
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
						o.printf("\t\tpush\t\t%s\n", temp);
						storeCaller(bb, ins);
						o.printf("\t\tmov\t\trdi, %s\n", src);
						o.printf("\t\tmov\t\trsi, _getInt\n");
						o.printf("\t\tmov\t\trdx, %s\n", temp);
						o.printf("\t\tmov\t\trax, 0\n");
						o.printf("\t\tcall\t\tsscanf\n");
						loadCaller(bb, ins);
						o.printf("\t\tpop\t\t%s\n", temp);
						
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
							//fb.dirty.put(reg, true);
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
									o.printf("\t\tmov\t\t%s, %s\n", src, reg);
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
									o.printf("\t\tmov\t\t%s, %s\n", src, reg);
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
						storeCaller(bb, ins);
						o.printf("\t\tmov\t\trdi, %s\n", src);
						o.printf("\t\tcall\t\tstrlen\n");
						loadCaller(bb, ins);
						o.printf("\t\tmov\t\t%s, rax\n", destb);
						//if(destb.substring(0, 1).equals("r"))
						//	fb.dirty.put(destb, true);
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
						String[] ops = new String[6];
						
						storeAll(bb, ins);
						int up = (paramNum < 6) ? paramNum : 6;
						for(int j = 0; j < up; j++)
						{
							String str = ((FuncCallIns)ins).ops.get(j);
							int aac = isReg(str);
							String op = null;
							if(aac == 0)
							{
								ops[j] = op = str;
							}
							else if(aac == 1)
							{
								String reg = getReg(bb.ofFunc, str);
								if(reg == null)
								{
									String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(str));
									ops[j] = op = "qword[" + pos + "]";
								}
								else
								{
									check(bb, str);
									ops[j] = op = reg;
									if(isParamReg(reg))
									{
										String pos = "rbp - " + (8 + bb.ofFunc.memPos.get(str));
										ops[j] = op = "qword[" + pos + "]";
										o.printf("\t\tmov\t\t%s, %s\n", op, reg);
									}
								}
							}
							else if(aac == 2)
							{
								String reg = getReg(bb.ofFunc, str);
								if(reg == null)
								{
									ops[j] = op = "qword[" + str + "]";
								}
								else
								{
									check(bb, str);
									ops[j] = op = reg;
									if(isParamReg(reg))
									{
										ops[j] = op = "qword[" + str + "]";
										o.printf("\t\tmov\t\t%s, %s\n", op, reg);
									}
								}
							}
						}
						for(int j = 0; j < up; j++)
						{
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
							
							o.printf("\t\tmov\t\t%s, %s\n", reg, ops[j]);
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
						loadAll(bb, ins);
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
							//fb.dirty.put(regd, true);
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
							//fb.dirty.put(dest, true);
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
							//fb.dirty.put(dest, true);
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
						//if(src1.substring(0, 1).equals("r"))
						//	fb.dirty.put(src1, true);
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
						//if(r1.substring(0, 1).equals("r"))
						//	fb.dirty.put(r1, true);
						if(r1.equals(temp))
						{
							o.printf("\t\tmov\t\t%s, %s\n", src1, temp);
							//if(src1.substring(0, 1).equals("r"))
							//	fb.dirty.put(src1, true);
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
							src1 = ((ArithIns)ins).src1;
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
						o.printf("\t\tmov\t\trax, %s\n", src1);
						o.printf("\t\tpush\t\trdx\n");
						int c2 = isReg(((ArithIns)ins).src2);
						if(c2 == 0)
						{
							o.printf("\t\tmov\t\t%s, %s\n", temp, ((ArithIns)ins).src2);
							o.printf("\t\tmov\t\trdx, 0\n");
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
								if(src2.equals("rdx"))
								{
									o.printf("\t\tmov\t\t%s, rdx\n", temp);
									src2 = temp;
								}
							}
							o.printf("\t\tmov\t\trdx, 0\n");
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
								if(src2.equals("rdx"))
								{
									o.printf("\t\tmov\t\t%s, rdx\n", temp);
									src2 = temp;
								}
							}
							o.printf("\t\tmov\t\trdx, 0\n");
							o.printf("\t\tdiv\t\t%s\n", src2);
						}
						
						if(ins.insName.equals("div"))
							o.printf("\t\tmov\t\t%s, rax\n", temp);
						else if(ins.insName.equals("rem"))
							o.printf("\t\tmov\t\t%s, rdx\n", temp);
						o.printf("\t\tpop\t\trdx\n");
						o.printf("\t\tmov\t\t%s, %s\n", src1, temp);
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
						//if(src1.substring(0, 1).equals("r"))
						//	fb.dirty.put(src1, true);
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
						if(src1.equals("rcx"))
						{
							o.printf("\t\tmov\t\t%s, rcx\n", temp);
							o.printf("\t\tmov\t\trcx, %s\n", src2);
							if(ins.insName.equals("shl"))
								o.printf("\t\tshl\t\t%s, cl\n", temp);
							else
								o.printf("\t\tsar\t\t%s, cl\n", temp);
							o.printf("\t\tmov\t\trcx, %s\n", temp);
							continue;
						}
						o.printf("\t\tpush\t\trcx\n");
						o.printf("\t\tmov\t\trcx, %s\n", src2);
						if(ins.insName.equals("shl"))
							o.printf("\t\tshl\t\t%s, cl\n", src1);
						else
							o.printf("\t\tsar\t\t%s, cl\n", src1);
						//if(src1.substring(0, 1).equals("r"))
						//	fb.dirty.put(src1, true);
						o.printf("\t\tpop\t\trcx\n");
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
						//if(src1.substring(0, 1).equals("r"))
						//	fb.dirty.put(src1, true);
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
				{
					//System.err.printf("%s -> %s\n", bb.blockID, bb.to.blockID);
					ins.succ.add(bb.to.insList.get(0));
				}
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
				else if(ins.insName.equals("storeStr"))
				{
					if(((MemAccIns)ins).addr.substring(0, 1).equals("$"))
					{
						if(!fb.memPos.containsKey(((MemAccIns)ins).addr))
						{
							fb.memPos.put(((MemAccIns)ins).addr, fb.memSize);
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
			//System.err.printf("First Error | func = %s, entry = %s\n", fb.funcName, fb.entry.blockID);
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
							//System.err.printf("jump error : %s(%d)\n", cur.blockID, i);//, cur.to.blockID, cur.to.insList.size(), cur.to.insList.get(0).in.size());
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
			else if(ins.insName.equals("storeStr"))
			{
				if(isReg(((MemAccIns)ins).addr) > 0)
					ins.def.add(((MemAccIns)ins).addr);
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
			{
				
				boolean deleteOne = false;
				for(Map.Entry<String, Integer> entry : fb.deg.entrySet())
				{
					if(fb.inGraph.get(entry.getKey()))
					{
						fb.inGraph.put(entry.getKey(), false);
						stack.push(entry.getKey());
						Set<String> temp = fb.itf.get(entry.getKey());
						for(String str : temp)
							fb.deg.put(str, fb.deg.get(str) - 1);
						deleteOne = true;
						break;
					}
				}
				if(!deleteOne)
					break;
			}
			
			// Have not put those with degree > regNum in stack
		}
		/*
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
			if(fb.inGraph.get(entry.getKey()))
				System.exit(1);
		}*/
		
		
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
					if(!fb.color.containsKey(adj))
						continue;
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
		/*
		String rR = getReg(bb.ofFunc, vr);
		String tT = getTake(bb, rR);
		bb.ofFunc.take.put(rR, vr);
		if(tT == null)
		{
			//load(bb, vr);
			//bb.ofFunc.dirty.put(rR, true);
		}
		else if(!tT.equals(vr))
		{
			//store(bb, rR);
			//load(bb, vr);
			//bb.ofFunc.dirty.put(rR, true);
		}
		return;*/
	}
	
	private void storeAll(BasicBlock bb, Ins ins)
	{
		Set<String> liveOut = new HashSet<>();
		for(String p : ins.out)
		{
			String t = getReg(bb.ofFunc, p);
			if(t != null)
				liveOut.add(t);
		}
		int s = realReg.size();
		for(int i = 0; i < s; i++)
		{
			String reg = realReg.get(i);
			if(!liveOutOnly || liveOut.contains(reg))
				o.printf("\t\tpush\t\t%s\n", reg);
		}
	}
	
	private void storeCaller(BasicBlock bb, Ins ins)
	{
		Set<String> liveOut = new HashSet<>();
		for(String p : ins.out)
		{
			String t = getReg(bb.ofFunc, p);
			if(t != null)
				liveOut.add(t);
		}
		int s = isCaller.size();
		for(int i = 0; i < s; i++)
		{
			String reg = isCaller.get(i);
			if(!liveOutOnly || liveOut.contains(reg))
				o.printf("\t\tpush\t\t%s\n", isCaller.get(i));
		}
	}
	
	private void loadAll(BasicBlock bb, Ins ins)
	{
		Set<String> liveOut = new HashSet<>();
		for(String p : ins.out)
		{
			String t = getReg(bb.ofFunc, p);
			if(t != null)
				liveOut.add(t);
		}
		int s = realReg.size();
		for(int i = s - 1; i >= 0; i--)
		{
			String reg = realReg.get(i);
			if(!liveOutOnly || liveOut.contains(reg))
				o.printf("\t\tpop\t\t%s\n", realReg.get(i));
		}
	}
	
	private void loadCaller(BasicBlock bb, Ins ins)
	{
		Set<String> liveOut = new HashSet<>();
		for(String p : ins.out)
		{
			String t = getReg(bb.ofFunc, p);
			if(t != null)
				liveOut.add(t);
		}
		int s = isCaller.size();
		for(int i = s - 1; i >= 0; i--)
		{
			String reg = isCaller.get(i);
			if(!liveOutOnly || liveOut.contains(reg))
				o.printf("\t\tpop\t\t%s\n", isCaller.get(i));
		}
	}
	
	private void printBuildIn()
	{
		String output = "global string_cat\n" +
				"global toString\n" +
				"global main\n" +
				"\n" +
				"extern puts\n" +
				"extern memcpy\n" +
				"extern malloc\n" +
				"\n" +
				"\n" +
				"SECTION .text\n" +
				"\n" +
				"string_cat:\n" +
				"        push    r14\n" +
				"        mov     rax, rdi\n" +
				"        push    r13\n" +
				"        mov     r13, rdi\n" +
				"        push    r12\n" +
				"        mov     r12, rsi\n" +
				"        push    rbp\n" +
				"        xor     ebp, ebp\n" +
				"        push    rbx\n" +
				"        jmp     L_002\n" +
				"\n" +
				"\n" +
				"\n" +
				"\n" +
				"\n" +
				"ALIGN   8\n" +
				"L_001:  add     ebp, 1\n" +
				"        add     rax, 1\n" +
				"        cmp     ebp, 256\n" +
				"        jz      L_003\n" +
				"L_002:  cmp     byte [rax], 0\n" +
				"        jnz     L_001\n" +
				"L_003:  mov     rdx, r12\n" +
				"        xor     eax, eax\n" +
				"        jmp     L_005\n" +
				"\n" +
				"\n" +
				"\n" +
				"\n" +
				"\n" +
				"ALIGN   8\n" +
				"L_004:  add     eax, 1\n" +
				"        add     rdx, 1\n" +
				"        cmp     eax, 256\n" +
				"        jz      L_006\n" +
				"L_005:  cmp     byte [rdx], 0\n" +
				"        jnz     L_004\n" +
				"L_006:  lea     ebx, [rbp+rax]\n" +
				"        lea     edi, [rbx+1H]\n" +
				"        movsxd  rdi, edi\n" +
				"        call    malloc\n" +
				"        test    ebp, ebp\n" +
				"        mov     r14, rax\n" +
				"        jz      L_009\n" +
				"        lea     eax, [rbp-1H]\n" +
				"        add     rax, 1\n" +
				"        cmp     eax, 8\n" +
				"        jnc     L_007\n" +
				"        test    al, 04H\n" +
				"        jnz     L_011\n" +
				"        test    eax, eax\n" +
				"        jz      L_008\n" +
				"        movzx   edx, byte [r13]\n" +
				"        test    al, 02H\n" +
				"        mov     byte [r14], dl\n" +
				"        jz      L_008\n" +
				"        mov     eax, eax\n" +
				"        movzx   edx, word [r13+rax-2H]\n" +
				"        mov     word [r14+rax-2H], dx\n" +
				"        jmp     L_008\n" +
				"\n" +
				"\n" +
				"\n" +
				"\n" +
				"\n" +
				"ALIGN   8\n" +
				"L_007:  mov     edx, eax\n" +
				"        mov     rdi, r14\n" +
				"        mov     rsi, r13\n" +
				"        mov     rcx, qword [r13+rdx-8H]\n" +
				"        mov     qword [r14+rdx-8H], rcx\n" +
				"        lea     ecx, [rax-1H]\n" +
				"        mov     eax, ecx\n" +
				"        shr     eax, 3\n" +
				"        mov     ecx, eax\n" +
				"        rep movsq\n" +
				"L_008:  cmp     ebx, ebp\n" +
				"        jl      L_010\n" +
				"L_009:  mov     eax, ebx\n" +
				"        movsxd  rdi, ebp\n" +
				"        mov     rsi, r12\n" +
				"        sub     eax, ebp\n" +
				"        add     rdi, r14\n" +
				"        lea     rdx, [rax+1H]\n" +
				"        call    memcpy\n" +
				"L_010:  pop     rbx\n" +
				"        mov     rax, r14\n" +
				"        pop     rbp\n" +
				"        pop     r12\n" +
				"        pop     r13\n" +
				"        pop     r14\n" +
				"        ret\n" +
				"\n" +
				"\n" +
				"L_011:\n" +
				"        mov     edx, dword [r13]\n" +
				"        mov     eax, eax\n" +
				"        mov     dword [r14], edx\n" +
				"        mov     edx, dword [r13+rax-4H]\n" +
				"        mov     dword [r14+rax-4H], edx\n" +
				"        jmp     L_008\n" +
				"\n" +
				"\n" +
				"\n" +
				"\n" +
				"\n" +
				"\n" +
				"ALIGN   16\n" +
				"\n" +
				"toString:\n" +
				"        lea     rax, [rdi+5F5E0FFH]\n" +
				"        push    rbx\n" +
				"        mov     rbx, rdi\n" +
				"        cmp     rax, 199999998\n" +
				"        ja      L_021\n" +
				"        lea     rax, [rdi+270FH]\n" +
				"        cmp     rax, 19998\n" +
				"        ja      L_014\n" +
				"        mov     edi, 6\n" +
				"        call    malloc\n" +
				"        mov     r9, rax\n" +
				"L_012:  test    rbx, rbx\n" +
				"        js      L_015\n" +
				"L_013:  jnz     L_016\n" +
				"        mov     byte [r9], 48\n" +
				"        mov     byte [r9+1H], 0\n" +
				"        mov     rax, r9\n" +
				"        pop     rbx\n" +
				"        ret\n" +
				"\n" +
				"\n" +
				"\n" +
				"\n" +
				"\n" +
				"ALIGN   16\n" +
				"L_014:  mov     edi, 11\n" +
				"        call    malloc\n" +
				"        test    rbx, rbx\n" +
				"        mov     r9, rax\n" +
				"        jns     L_013\n" +
				"L_015:  mov     byte [r9], 45\n" +
				"        neg     rbx\n" +
				"        mov     r10d, 1\n" +
				"        jmp     L_017\n" +
				"\n" +
				"\n" +
				"\n" +
				"\n" +
				"\n" +
				"ALIGN   8\n" +
				"L_016:  xor     r10d, r10d\n" +
				"L_017:  movsxd  rcx, r10d\n" +
				"        mov     edi, r10d\n" +
				"        mov     r8, qword 6666666666666667H\n" +
				"        add     rcx, r9\n" +
				"        mov     rsi, rcx\n" +
				"\n" +
				"\n" +
				"\n" +
				"\n" +
				"ALIGN   8\n" +
				"L_018:  mov     rax, rbx\n" +
				"        add     edi, 1\n" +
				"        add     rsi, 1\n" +
				"        imul    r8\n" +
				"        mov     rax, rbx\n" +
				"        sar     rax, 63\n" +
				"        sar     rdx, 2\n" +
				"        sub     rdx, rax\n" +
				"        lea     rax, [rdx+rdx*4]\n" +
				"        add     rax, rax\n" +
				"        sub     rbx, rax\n" +
				"        add     ebx, 48\n" +
				"        mov     byte [rsi-1H], bl\n" +
				"        test    rdx, rdx\n" +
				"        mov     rbx, rdx\n" +
				"        jnz     L_018\n" +
				"        mov     eax, edi\n" +
				"        movsxd  rdi, edi\n" +
				"        sub     eax, r10d\n" +
				"        sar     eax, 1\n" +
				"        mov     edx, eax\n" +
				"        jz      L_020\n" +
				"        sub     edx, 1\n" +
				"        mov     rsi, r9\n" +
				"        lea     rax, [r9+rdi-1H]\n" +
				"        sub     rsi, rdx\n" +
				"        lea     r8, [rsi+rdi-2H]\n" +
				"\n" +
				"\n" +
				"\n" +
				"\n" +
				"ALIGN   8\n" +
				"L_019:  movzx   edx, byte [rcx]\n" +
				"        movzx   esi, byte [rax]\n" +
				"        sub     rax, 1\n" +
				"        add     rcx, 1\n" +
				"        mov     byte [rcx-1H], sil\n" +
				"        mov     byte [rax+1H], dl\n" +
				"        cmp     r8, rax\n" +
				"        jnz     L_019\n" +
				"L_020:  mov     byte [r9+rdi], 0\n" +
				"        mov     rax, r9\n" +
				"        pop     rbx\n" +
				"        ret\n" +
				"\n" +
				"\n" +
				"\n" +
				"\n" +
				"\n" +
				"ALIGN   8\n" +
				"L_021:  mov     edi, 23\n" +
				"        call    malloc\n" +
				"        mov     r9, rax\n" +
				"        jmp     L_012";
		
		o.println(output);
	}
}
