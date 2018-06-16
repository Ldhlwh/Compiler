package IR;

import AbstractSyntaxTree.Nodes.*;
import IR.Instructions.*;
import ScopeCheck.Instances.ClassIns;
import ScopeCheck.Instances.FuncIns;
import ScopeCheck.Instances.ParamIns;
import ScopeCheck.Instances.VariIns;
import ScopeCheck.Scopes.*;
import javafx.util.Pair;

import java.io.PrintStream;
import java.util.*;

import static java.lang.System.exit;

public class IRGenerator
{
	public TopScope topScope;
	public ASTRootNode rootNode;
	
	public ArrayList<FuncBlock> funcBlock = new ArrayList<>();
	
	public Map<String, Integer> bytes = new HashMap<>();
	
	private Set<String> niFunc = new HashSet<>();
	
	public int tempRegNum = 0;
	private int inlineDepth = 1;
	//private boolean inlineOutOfMain = false;
	private int iln = 0;
	
	public IRGenerator(ASTRootNode root, boolean submit)
	{
		rootNode = root;
		topScope = root.scope;
		
		bytes.put("addr", 8);
		bytes.put("int", 8);
		bytes.put("bool", 8);
		bytes.put("char", 1);
		for(Map.Entry<String, ClassIns> entry : topScope.classMap.entrySet())
		{
			ClassIns temp = entry.getValue();
			int bytesSum = 0;
			for(Map.Entry<String, VariIns> variEntry : temp.variMap.entrySet())
			{
				VariIns variTemp = variEntry.getValue();
				variTemp.offset = bytesSum;
				if((variTemp.singleType.equals("int")
						|| variTemp.singleType.equals("bool"))
						&& variTemp.dimNum == 0)
				{
					bytesSum += bytes.get(variTemp.singleType);
				}
				else
				{
					bytesSum += bytes.get("addr");
				}
			}
			bytes.put(temp.name, bytesSum);
		}
		
		niFunc.add("getInt");
		niFunc.add("getString");
		niFunc.add("print");
		niFunc.add("println");
		niFunc.add("toString");
		niFunc.add("parseInt");
		niFunc.add("a.size");
		niFunc.add("string.length");
		niFunc.add("string.substring");
		niFunc.add("string.ord");
		//niFunc.add("");
	}
	
	public void print()
	{
		for(FuncBlock fb : funcBlock)
		{
			fb.print();
		}
	}
	
	private void add_alloc()
	{
		FuncBlock _alloc = new FuncBlock("_alloc");
		funcBlock.add(_alloc);
		BasicBlock _alloc_entry = new BasicBlock("_alloc");
		_alloc.entry = _alloc_entry;
		_alloc_entry.ofFunc = _alloc;
		
		_alloc.param.add("$_cnt");
		_alloc.param.add("$_cnt_addr");
		
		MovIns tins = new MovIns();
		tins.insName = "move";
		tins.dest = "$_t" + (tempRegNum++);
		tins.src = "$_cnt";
		_alloc_entry.insList.add(tins);
		
		ArithIns ains = new ArithIns();
		ains.insName = "mul";
		ains.dest = ains.src1 = tins.dest;
		ains.src2 = bytes.get("addr") + "";
		_alloc_entry.insList.add(ains);
		
		ArithIns ains2 = new ArithIns();
		ains2.insName = "add";
		ains2.dest = ains2.src1 = ains.dest;
		ains2.src2 = bytes.get("addr") + "";
		_alloc_entry.insList.add(ains2);
		
		MemAccIns ins = new MemAccIns();
		ins.insName = "alloc";
		ins.dest = "$_t";
		ins.size = ains2.dest;
		_alloc_entry.insList.add(ins);
		
		MemAccIns sins = new MemAccIns();
		sins.insName = "store";
		sins.size = bytes.get("addr") + "";
		sins.addr = "$_t";
		sins.src = "$_cnt";
		sins.offset = 0;
		_alloc_entry.insList.add(sins);
		
		MovIns ins2 = new MovIns();
		ins2.insName = "move";
		ins2.dest = "$_i";
		ins2.src = "0";
		_alloc_entry.insList.add(ins2);
		
		MovIns tins2 = new MovIns();
		tins2.insName = "move";
		tins2.dest = "$_cnt_next_addr";
		tins2.src = "$_cnt_addr";
		_alloc_entry.insList.add(tins2);
		
		ArithIns ains3 = new ArithIns();
		ains3.insName = "add";
		ains3.dest = ains3.src1 = tins2.dest;
		ains3.src2 = bytes.get("addr") + "";
		_alloc_entry.insList.add(ains3);
		
		MemAccIns ins3 = new MemAccIns();
		ins3.insName = "load";
		ins3.dest = "$_cnt_next";
		ins3.size = bytes.get("addr") + "";
		ins3.addr = "$_cnt_next_addr";
		ins3.offset = 0;
		_alloc_entry.insList.add(ins3);
		
		CondSetIns cins = new CondSetIns();
		cins.insName = "slt";
		cins.dest = "$_cond1";
		cins.src1 = ins2.dest;
		cins.src2 = "$_cnt";
		_alloc_entry.insList.add(cins);
		
		BasicBlock bt = new BasicBlock();
		BasicBlock bf = new BasicBlock();
		BasicBlock bm = new BasicBlock();
		BasicBlock bl = new BasicBlock();
		
		_alloc_entry.ifTrue = bm;
		_alloc_entry.ifFalse = bm.ifFalse = bf;
		bm.ifTrue = bt;
		bt.to = bf.to = bl;
		bt.ofFunc = bf.ofFunc = bm.ofFunc = bl.ofFunc = _alloc;
		
		JumpIns i1 = new JumpIns();
		i1.insName = "br";
		i1.cond = cins.dest;
		i1.ifTrue = bm.blockID;
		i1.ifFalse = bf.blockID;
		_alloc_entry.insList.add(i1);
		
		CondSetIns cins2 = new CondSetIns();
		cins2.insName = "sgt";
		cins2.dest = "$_cond2";
		cins2.src1 = "$_cnt_next";
		cins2.src2 = "0";
		bm.insList.add(cins2);
		
		JumpIns i2 = new JumpIns();
		i2.insName = "br";
		i2.cond = cins2.dest;
		i2.ifTrue = bt.blockID;
		i2.ifFalse = bf.blockID;
		bm.insList.add(i2);
		
		MovIns i3 = new MovIns();
		i3.insName = "move";
		i3.dest = "$_cond";
		i3.src = "1";
		bt.insList.add(i3);
		
		JumpIns i4 = new JumpIns();
		i4.insName = "jump";
		i4.target = bl.blockID;
		i4.toBlock = bl;
		bt.insList.add(i4);
		
		MovIns i5 = new MovIns();
		i5.insName = "move";
		i5.dest = "$_cond";
		i5.src = "0";
		bf.insList.add(i5);
		
		JumpIns i6 = new JumpIns();
		i6.insName = "jump";
		i6.target = bl.blockID;
		i6.toBlock = bl;
		bf.insList.add(i6);
		
		BasicBlock bb = new BasicBlock();
		BasicBlock fbb = new BasicBlock();
		bl.ifTrue = bb;
		bl.ifFalse = fbb;
		bb.ofFunc = fbb.ofFunc = _alloc;
		
		CondSetIns i7 = new CondSetIns();
		i7.insName = "seq";
		i7.dest = i7.src1 = "$_cond";
		i7.src2 = "1";
		bl.insList.add(i7);
		
		JumpIns jins = new JumpIns();
		jins.insName = "br";
		jins.cond = "$_cond";
		jins.ifTrue = bb.blockID;
		jins.ifFalse = fbb.blockID;
		bl.insList.add(jins);
		
		JumpIns jins2 = new JumpIns();
		jins2.insName = "ret";
		jins2.src = ins.dest;
		fbb.insList.add(jins2);
		
		MovIns tins4 = new MovIns();
		tins4.insName = "move";
		tins4.dest = "$_t" + (tempRegNum++);
		tins4.src = "$_i";
		bb.insList.add(tins4);
		
		ArithIns bbins = new ArithIns();
		bbins.insName = "mul";
		bbins.dest = bbins.src1 = tins4.dest;
		bbins.src2 = bytes.get("addr") + "";
		bb.insList.add(bbins);
		
		ArithIns bbins2 = new ArithIns();
		bbins2.insName = "add";
		bbins2.dest = bbins2.src1 = bbins.dest;
		bbins2.src2 = bytes.get("addr") + "";
		bb.insList.add(bbins2);
		
		MovIns tins5 = new MovIns();
		tins5.insName = "move";
		tins5.dest = "$_t_d";
		tins5.src = bbins2.dest;
		bb.insList.add(tins5);
		
		ArithIns bbins3 = new ArithIns();
		bbins3.insName = "add";
		bbins3.dest = bbins3.src1 = tins5.dest;
		bbins3.src2 = "$_t";
		bb.insList.add(bbins3);
		
		FuncCallIns bbins4 = new FuncCallIns();
		bbins4.insName = "call";
		bbins4.funcName = "_alloc";
		bbins4.dest = "$_t_1";
		bbins4.ops.add("$_cnt_next");
		bbins4.ops.add("$_cnt_next_addr");
		bb.insList.add(bbins4);
		
		MemAccIns bbins5 = new MemAccIns();
		bbins5.insName = "store";
		bbins5.size = bytes.get("addr") + "";
		bbins5.addr = "$_t_d";
		bbins5.src = "$_t_1";
		bbins5.offset = 0;
		bb.insList.add(bbins5);
		
		ArithIns bbins6 = new ArithIns();
		bbins6.insName = "add";
		bbins6.dest = bbins6.src1 = "$_i";
		bbins6.src2 = "1";
		bb.insList.add(bbins6);
		
		CondSetIns bbins7 = new CondSetIns();
		bbins7.insName = "slt";
		bbins7.dest = "$_condi";
		bbins7.src1 = "$_i";
		bbins7.src2 = "$_cnt";
		bb.insList.add(bbins7);
		
		JumpIns bbins8 = new JumpIns();
		bbins8.insName = "br";
		bbins8.cond = "$_condi";
		bbins8.ifTrue = bb.blockID;
		bbins8.ifFalse = fbb.blockID;
		bb.insList.add(bbins8);
		
		bb.ifTrue = bb;
		bb.ifFalse = fbb;
	}
	
	private void add_int_size()
	{
		FuncBlock int_size = new FuncBlock("int.size");
		funcBlock.add(int_size);
		BasicBlock entry = new BasicBlock("int.size");
		int_size.entry = entry;
		entry.ofFunc = int_size;
		
		int_size.param.add("$_pos");
		
		MemAccIns ins = new MemAccIns();
		ins.insName = "load";
		ins.dest = "$_t" + (tempRegNum++);
		ins.size = bytes.get("addr") + "";
		ins.offset = 0;
		ins.addr = "$_pos";
		entry.insList.add(ins);
		
		JumpIns jins = new JumpIns();
		jins.insName = "ret";
		jins.src = ins.dest;
		entry.insList.add(jins);
	}
	
	private void add_string_length()
	{
		FuncBlock length = new FuncBlock("string.length");
		funcBlock.add(length);
		BasicBlock entry = new BasicBlock("string.length");
		length.entry = entry;
		entry.ofFunc = length;
		
		length.param.add("$_pos");
		
		BasicBlock bm = new BasicBlock(), bl = new BasicBlock();
		bm.ofFunc = bl.ofFunc = length;
		entry.to = bm;
		bm.ifTrue = bl;
		bm.ifFalse = bm;
		
		MovIns i1 = new MovIns();
		i1.insName = "move";
		i1.dest = "$_cnt";
		i1.src = "-1";
		entry.insList.add(i1);
		
		JumpIns i2 = new JumpIns();
		i2.insName = "jump";
		i2.target = bm.blockID;
		i2.toBlock = bm;
		entry.insList.add(i2);
		
		MemAccIns i3 = new MemAccIns();
		i3.insName = "load";
		i3.dest = "$_t";
		i3.size = "1";
		i3.addr = "$_pos";
		i3.offset = 0;
		bm.insList.add(i3);
		
		ArithIns i4 = new ArithIns();
		i4.insName = "add";
		i4.dest = i4.src1 = "$_pos";
		i4.src2 = "1";
		bm.insList.add(i4);
		
		ArithIns i5 = new ArithIns();
		i5.insName = "add";
		i5.dest = i5.src1 = "$_cnt";
		i5.src2 = "1";
		bm.insList.add(i5);
		
		CondSetIns i6 = new CondSetIns();
		i6.insName = "seq";
		i6.dest = "$_cond";
		i6.src1 = "$_t";
		i6.src2 = "0";
		bm.insList.add(i6);
		
		JumpIns i7 = new JumpIns();
		i7.insName = "br";
		i7.cond = "$_cond";
		i7.ifTrue = bl.blockID;
		i7.ifFalse = bm.blockID;
		bm.insList.add(i7);
		
		JumpIns i8 = new JumpIns();
		i8.insName = "ret";
		i8.src = "$_cnt";
		bl.insList.add(i8);
	}
	
	private void add_string_substring()
	{
		FuncBlock f = new FuncBlock("string.substring");
		funcBlock.add(f);
		BasicBlock entry = new BasicBlock("string.substring");
		f.entry = entry;
		entry.ofFunc = f;
		
		BasicBlock bm = new BasicBlock(), bl = new BasicBlock();
		bm.ofFunc = bl.ofFunc = f;
		entry.ifTrue = bm;
		entry.ifFalse = bl;
		bm.ifTrue = bl;
		bm.ifFalse = bm;
		
		f.param.add("$_src");
		f.param.add("$_st");
		f.param.add("$_ed");
		
		MovIns tins = new MovIns();
		tins.insName = "move";
		tins.dest = "$_t" + (tempRegNum++);
		tins.src = "$_ed";
		entry.insList.add(tins);
		
		ArithIns tins2 = new ArithIns();
		tins2.insName = "sub";
		tins2.dest = tins2.src1 = tins.dest;
		tins2.src2 = "$_st";
		entry.insList.add(tins2);
		
		ArithIns tins3 = new ArithIns();
		tins3.insName = "add";
		tins3.dest = tins3.src1 = tins2.dest;
		tins3.src2 = "2";
		entry.insList.add(tins3);
		
		MemAccIns i1 = new MemAccIns();
		i1.insName = "alloc";
		i1.dest = "$_dest";
		i1.size = tins3.dest;
		entry.insList.add(i1);
		
		MovIns i2 = new MovIns();
		i2.insName = "move";
		i2.dest = "$_tdest";
		i2.src = i1.dest;
		entry.insList.add(i2);
		
		ArithIns i3 = new ArithIns();
		i3.insName = "add";
		i3.dest = i3.src1 = "$_src";
		i3.src2 = "$_st";
		entry.insList.add(i3);
		
		CondSetIns i35 = new CondSetIns();
		i35.insName = "sle";
		i35.dest = "$_cond";
		i35.src1 = "$_st";
		i35.src2 = "$_ed";
		entry.insList.add(i35);
		
		JumpIns i4 = new JumpIns();
		i4.insName = "br";
		i4.cond = "$_cond";
		i4.ifTrue = bm.blockID;
		i4.ifFalse = bl.blockID;
		entry.insList.add(i4);
		
		MemAccIns i5 = new MemAccIns();
		i5.insName = "load";
		i5.dest = "$_t";
		i5.size = "1";
		i5.addr = "$_src";
		i5.offset = 0;
		bm.insList.add(i5);
		
		MemAccIns i6 = new MemAccIns();
		i6.insName = "store";
		i6.size = "1";
		i6.addr = "$_tdest";
		i6.src = "$_t";
		i6.offset = 0;
		bm.insList.add(i6);
		
		ArithIns i7 = new ArithIns();
		i7.insName = "add";
		i7.dest = i7.src1 = "$_src";
		i7.src2 = "1";
		bm.insList.add(i7);
		
		ArithIns i8 = new ArithIns();
		i8.insName = "add";
		i8.dest = i8.src1 = "$_tdest";
		i8.src2 = "1";
		bm.insList.add(i8);
		
		ArithIns i9 = new ArithIns();
		i9.insName = "add";
		i9.dest = i9.src1 = "$_st";
		i9.src2 = "1";
		bm.insList.add(i9);
		
		CondSetIns i10 = new CondSetIns();
		i10.insName = "sgt";
		i10.dest = "$_cond";
		i10.src1 = "$_st";
		i10.src2 = "$_ed";
		bm.insList.add(i10);
		
		JumpIns i11 = new JumpIns();
		i11.insName = "br";
		i11.cond = "$_cond";
		i11.ifTrue = bl.blockID;
		i11.ifFalse = bm.blockID;
		bm.insList.add(i11);
		
		MemAccIns i12 = new MemAccIns();
		i12.insName = "store";
		i12.size = "1";
		i12.addr = "$_tdest";
		i12.src = "0";
		i12.offset = 0;
		bl.insList.add(i12);
		
		JumpIns i13 = new JumpIns();
		i13.insName = "ret";
		i13.src = "$_dest";
		bl.insList.add(i13);
	}
	
	private void add_string_ord()
	{
		FuncBlock f = new FuncBlock("string.ord");
		funcBlock.add(f);
		BasicBlock entry = new BasicBlock("string.ord");
		f.entry = entry;
		entry.ofFunc = f;
		
		f.param.add("$_src");
		f.param.add("$_pos");
		
		ArithIns i1 = new ArithIns();
		i1.insName = "add";
		i1.dest = i1.src1 = "$_src";
		i1.src2 = "$_pos";
		entry.insList.add(i1);
		
		MemAccIns i2 = new MemAccIns();
		i2.insName = "load";
		i2.dest = "$_t";
		i2.size = "1";
		i2.addr = "$_src";
		i2.offset = 0;
		entry.insList.add(i2);
		
		JumpIns i3 = new JumpIns();
		i3.insName = "ret";
		i3.src = "$_t";
		entry.insList.add(i3);
	}
	
	public void passRoot()
	{
		FuncBlock __init = new FuncBlock("__init");
		funcBlock.add(__init);
		BasicBlock __init_entry = new BasicBlock("__init");
		__init.entry = __init_entry;
		__init_entry.ofFunc = __init;
		
		
		add_alloc();
		add_int_size();
		//add_string_length();
		add_string_substring();
		add_string_ord();
		
		BasicBlock curBlock = __init_entry;
		for(ASTNode node : rootNode.progSecNode)
		{
			if(node instanceof FuncDeclNode)
			{
				FuncBlock fb = new FuncBlock(((FuncDeclNode)node).id);
				funcBlock.add(fb);
				for(ParamIns p : rootNode.scope.funcMap.get(((FuncDeclNode)node).id).param)
				{
					fb.param.add("$" + p.name + "_" + ((FuncDeclNode)node).scope.scopeID);
				}
				BasicBlock entry = new BasicBlock(((FuncDeclNode)node).id);
				fb.entry = entry;
				entry.ofFunc = fb;
				Pair<String, BasicBlock> temp = pass(node, ((FuncDeclNode)node).scope, entry, false, true,
						null, null, null, 0, null, false, null, null);
			}
			else
			{
				Pair<String, BasicBlock> temp = pass(node, topScope, curBlock, false, true,
						null, null, null, 0, null, false, null, null);
				//curBlock = temp.getValue();
			}
		}
		
		FuncCallIns ins = new FuncCallIns();
		ins.insName = "call";
		ins.dest = "$_t" + (tempRegNum++) + "_" + topScope.scopeID;
		ins.funcName = "main";
		curBlock.insList.add(ins);
		
		JumpIns jins = new JumpIns();
		jins.insName = "ret";
		jins.src = ins.dest;
		curBlock.insList.add(jins);
	}
	
	public Pair<String, BasicBlock> pass(ASTNode now, Scope curScope, BasicBlock curBlock, boolean wantAddr, boolean checkThis,
										 BasicBlock recHead, BasicBlock trueBlock, BasicBlock falseBlock,
										 int depth, Map<String, String> inlineVari, boolean il, BasicBlock rtnBlock, String rtnReg)
	{
		if(now instanceof VariDeclNode)
		{
			if((((VariDeclNode)now).typeNode.singleTypeNode.type.equals("int")
					|| ((VariDeclNode)now).typeNode.singleTypeNode.type.equals("bool"))
					&& ((VariDeclNode)now).typeNode.dimNum == 0)
			{
				for(ASTNode node : ((VariDeclNode)now).variInitNode)
				{
					if(((VariInitNode)node).assign)
					{
						if(((VariInitNode)node).exprNode instanceof ConstNode)
						{
							if(((VariDeclNode)now).typeNode.singleTypeNode.type.equals("int"))
							{
								MovIns ins = new MovIns();
								if(curScope == topScope)
									ins.dest = "@" + ((VariInitNode)node).id;
								else
									ins.dest = "$" + ((VariInitNode)node).id + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
								ins.insName = "move";
								ins.src = ((ConstNode)((VariInitNode)node).exprNode).text;
								curBlock.insList.add(ins);
							}
							if(((VariDeclNode)now).typeNode.singleTypeNode.type.equals("bool"))
							{
								MovIns ins = new MovIns();
								if(curScope == topScope)
									ins.dest = "@" + ((VariInitNode)node).id;
								else
									ins.dest = "$" + ((VariInitNode)node).id + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
								ins.insName = "move";
								if(((ConstNode)((VariInitNode)node).exprNode).text.equals("true"))
									ins.src = "1";
								else
									ins.src = "0";
								curBlock.insList.add(ins);
							}
						}
						else if(((VariInitNode)node).exprNode instanceof FuncCallNode)
						{
							MovIns ins = new MovIns();
							if(curScope == topScope)
								ins.dest = "@" + ((VariInitNode)node).id;
							else
								ins.dest = "$" + ((VariInitNode)node).id + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
							ins.insName = "move";
							Pair<String, BasicBlock> temp = pass(((VariInitNode)node).exprNode, curScope, curBlock, false, true,
									recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
							ins.src = temp.getKey();
							curBlock = temp.getValue();
							curBlock.insList.add(ins);
							//Ins last = curBlock.insList.get(curBlock.insList.size() - 1);
							//((FuncCallIns)last).dest = ins.dest;
						}
						else
						{
							MovIns ins = new MovIns();
							if(curScope == topScope)
								ins.dest = "@" + ((VariInitNode)node).id;
							else
								ins.dest = "$" + ((VariInitNode)node).id + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
							ins.insName = "move";
							Pair<String, BasicBlock> temp = pass(((VariInitNode)node).exprNode, curScope, curBlock, false, true,
									recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
							ins.src = temp.getKey();
							curBlock = temp.getValue();
							curBlock.insList.add(ins);
						}
					}
				}
			}
			else
			{
				for(ASTNode node : ((VariDeclNode)now).variInitNode)
				{
					if(((VariInitNode)node).assign)
					{
						MovIns ins = new MovIns();
						if(curScope == topScope)
							ins.dest = "@" + ((VariInitNode)node).id;
						else
							ins.dest = "$" + ((VariInitNode)node).id + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
						ins.insName = "move";
						Pair<String, BasicBlock> temp = pass(((VariInitNode)node).exprNode, curScope, curBlock, false, true,
								recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
						ins.src = temp.getKey();
						curBlock = temp.getValue();
						/*
						if(((VariInitNode)node).exprNode instanceof FuncCallNode)
						{
							Ins last = curBlock.insList.get(curBlock.insList.size() - 1);
							((FuncCallIns)last).dest = ins.dest;
							return new Pair<>(null, curBlock);
						}*/
						curBlock.insList.add(ins);
					}
				}
			}
			return new Pair<>(null, curBlock);
		}
		
		else if(now instanceof ClassDeclNode)
		{
			FuncBlock A_A = new FuncBlock(((ClassDeclNode)now).id + "." + ((ClassDeclNode)now).id);
			funcBlock.add(A_A);
			A_A.param.add("$this_" + ((ClassDeclNode)now).scope.scopeID + (il ? "_il_" + (iln): ""));
			BasicBlock A_A_entry = new BasicBlock(((ClassDeclNode)now).id + "." + ((ClassDeclNode)now).id);
			A_A.entry = A_A_entry;
			A_A_entry.ofFunc = A_A;
			
			String reg = "$this_" + ((ClassDeclNode)now).scope.scopeID + (il ? "_il_" + (iln): "");
			BasicBlock temp = A_A_entry;
			
			for(ASTNode node : ((ClassDeclNode)now).progSecNode)
			{
				if(node instanceof ConstructorNode)
				{
					temp = pass(node, ((ConstructorNode)node).scope, A_A_entry, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg).getValue();
				}
				if(node instanceof FuncDeclNode)
				{
					FuncBlock A_ = new FuncBlock(((ClassDeclNode)now).id + "." + ((FuncDeclNode)node).id);
					funcBlock.add(A_);
					A_.param.add("$this_" + ((ClassDeclNode)now).scope.scopeID);
					for(ParamIns p : ((ClassDeclNode)now).scope.funcMap.get(((FuncDeclNode)node).id).param)
					{
						A_.param.add("$" + p.name + "_" + ((FuncDeclNode)node).scope.scopeID);
					}
					BasicBlock A_entry = new BasicBlock(((ClassDeclNode)now).id + "." + ((FuncDeclNode)node).id);
					A_.entry = A_entry;
					A_entry.ofFunc = A_;
					Pair<String, BasicBlock> temp1 = pass(node, ((FuncDeclNode)node).scope, A_entry, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
					curBlock = temp1.getValue();
				}
			}
			
			JumpIns rIns = new JumpIns();
			rIns.insName = "ret";
			rIns.src = reg;
			temp.insList.add(rIns);
			
			return new Pair<>(null, curBlock);
		}
		
		else if(now instanceof FuncDeclNode)
		{
			Pair<String, BasicBlock> temp = pass(((FuncDeclNode)now).blockStmtNode, ((FuncDeclNode)now).blockStmtNode.scope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
			curBlock = temp.getValue();
			//if(il && curBlock == rtnBlock)
			//	return new Pair<>(temp.getKey(), rtnBlock);
			if(((FuncDeclNode)now).typeNode.singleTypeNode.type.equals("void"))
			{
				if(il)
				{
					MovIns mi = new MovIns();
					mi.insName = "move";
					mi.dest = rtnReg;
					mi.src = "0";
					curBlock.insList.add(mi);
					
					JumpIns ji = new JumpIns();
					ji.insName = "jump";
					ji.target = rtnBlock.blockID;
					ji.toBlock = rtnBlock;
					curBlock.insList.add(ji);
					curBlock.to = rtnBlock;
					return new Pair<>("0", rtnBlock);
				}
				JumpIns ins = new JumpIns();
				ins.insName = "ret";
				ins.src = "0";
				curBlock = temp.getValue();
				curBlock.insList.add(ins);
			}
			else if(il && (curBlock.insList.size() == 0
					|| !curBlock.insList.get(curBlock.insList.size() - 1).insName.equals("jump")))
			{
				MovIns mi = new MovIns();
				mi.insName = "move";
				mi.dest = rtnReg;
				mi.src = "0";
				curBlock.insList.add(mi);
				
				JumpIns ji = new JumpIns();
				ji.insName = "jump";
				ji.target = rtnBlock.blockID;
				ji.toBlock = rtnBlock;
				curBlock.insList.add(ji);
				curBlock.to = rtnBlock;
				return new Pair<>("0", rtnBlock);
			}
			else if(il)
			{
				//curBlock.to = rtnBlock;
				return new Pair<>(null, rtnBlock);
			}
			else if(!il && (curBlock.insList.size() == 0
					|| !curBlock.insList.get(curBlock.insList.size() - 1).insName.equals("ret")))
			{
				JumpIns ins = new JumpIns();
				ins.insName = "ret";
				ins.src = "0";
				curBlock.insList.add(ins);
			}
			return new Pair<>(temp.getKey(), curBlock);
		}
		
		else if(now instanceof ConstructorNode)
		{
			return pass(((ConstructorNode)now).blockStmtNode, ((ConstructorNode)now).blockStmtNode.scope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
		}
		
		else if(now instanceof BlockStmtNode)
		{
			curScope = ((BlockStmtNode)now).scope;
			String rtn = null;
			for(ASTNode node : ((BlockStmtNode)now).progSecNode)
			{
				if(node instanceof SlctStmtNode)
				{
					Pair<String, BasicBlock> temp = pass(node, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
					rtn = temp.getKey();
					curBlock = temp.getValue();
				}
				else if(node instanceof ForInitNode)
				{
					// Abandoned
				}
				else if(node instanceof ForNode)
				{
					Pair<String, BasicBlock> temp = pass(node, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
					rtn = temp.getKey();
					curBlock = temp.getValue();
				}
				else if(node instanceof WhileNode)
				{
					Pair<String, BasicBlock> temp = pass(node, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
					rtn = temp.getKey();
					curBlock = temp.getValue();
				}
				else
				{
					Pair<String, BasicBlock> temp = pass(node, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
					rtn = temp.getKey();
					curBlock = temp.getValue();
				}
			}
			return new Pair<>(rtn, curBlock);
		}
		
		else if(now instanceof ExprStmtNode)
		{
			if(!((ExprStmtNode)now).empty)
			{
				Pair<String, BasicBlock> temp = pass(((ExprStmtNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				curBlock = temp.getValue();
			}
			return new Pair<>(null, curBlock);
		}
		
		else if(now instanceof SlctStmtNode)
		{
			BasicBlock finalBlock = new BasicBlock();
			finalBlock.ofFunc = curBlock.ofFunc;
			
			if(((SlctStmtNode)now).elifExprNode.size() == 0)
			{
				BasicBlock bb = new BasicBlock();
				bb.ofFunc = curBlock.ofFunc;
				BasicBlock nowBlock;
				
				JumpIns ins = new JumpIns();
				ins.insName = "br";
				Pair<String, BasicBlock> temp = pass(((SlctStmtNode)now).ifExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				ins.cond = temp.getKey();
				curBlock = temp.getValue();
				ins.ifTrue = bb.blockID;
				ins.ifFalse = finalBlock.blockID;
				curBlock.ifTrue = bb;
				curBlock.ifFalse = finalBlock;
				curBlock.insList.add(ins);
				
				nowBlock = pass(((SlctStmtNode)now).ifStmtNode, curScope, bb, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg).getValue();
				
				JumpIns ins2 = new JumpIns();
				ins2.insName = "jump";
				ins2.target = finalBlock.blockID;
				ins2.toBlock = finalBlock;
				if(nowBlock.insList.size() == 0
						|| (!nowBlock.insList.get(nowBlock.insList.size() - 1).insName.equals("jump")
								&& !nowBlock.insList.get(nowBlock.insList.size() - 1).insName.equals("ret")))
				{
					nowBlock.insList.add(ins2);
					nowBlock.to = finalBlock;
				}
				if(((SlctStmtNode)now).haveElse)
				{
					BasicBlock eb = new BasicBlock();
					eb.ofFunc = curBlock.ofFunc;
					curBlock.ifFalse = eb;
					
					ins.ifFalse = eb.blockID;
					
					nowBlock = pass(((SlctStmtNode)now).elseStmtNode, curScope, eb, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg).getValue();
					
					JumpIns ins3 = new JumpIns();
					ins3.insName = "jump";
					ins3.target = finalBlock.blockID;
					ins3.toBlock = finalBlock;
					if(nowBlock.insList.size() == 0
						|| (!nowBlock.insList.get(nowBlock.insList.size() - 1).insName.equals("jump")
							&& !nowBlock.insList.get(nowBlock.insList.size() - 1).insName.equals("ret")))
					{
						//System.err.printf("%s : in\n", nowBlock.blockID);
						nowBlock.insList.add(ins3);
						nowBlock.to = finalBlock;
					}
				}
				return new Pair<>(null, finalBlock);
			}
			else
			{
				BasicBlock nowBlock;
				BasicBlock abb = new BasicBlock();
				BasicBlock afbb = new BasicBlock();
				abb.ofFunc = afbb.ofFunc = curBlock.ofFunc;
				
				JumpIns ains = new JumpIns();
				ains.insName = "br";
				Pair<String, BasicBlock> temp = pass(((SlctStmtNode)now).ifExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				ains.cond = temp.getKey();
				curBlock = temp.getValue();
				ains.ifTrue = abb.blockID;
				ains.ifFalse = afbb.blockID;
				curBlock.insList.add(ains);
				curBlock.ifTrue = abb;
				curBlock.ifFalse = afbb;
				
				nowBlock = pass(((SlctStmtNode)now).ifStmtNode, curScope, abb, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg).getValue();
				
				JumpIns ains2 = new JumpIns();
				ains2.insName = "jump";
				ains2.target = finalBlock.blockID;
				ains2.toBlock = finalBlock;
				nowBlock.insList.add(ains2);
				nowBlock.to = finalBlock;
				curBlock = afbb;
				
				int maxSize = ((SlctStmtNode)now).elifExprNode.size();
				for(int i = 0; i < maxSize; i++)
				{
					BasicBlock bb = new BasicBlock();
					BasicBlock fbb = new BasicBlock();
					bb.ofFunc = fbb.ofFunc = curBlock.ofFunc;
					
					JumpIns ins = new JumpIns();
					ins.insName = "br";
					Pair<String, BasicBlock> temp1 = pass(((SlctStmtNode)now).elifExprNode.get(i), curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
					ins.cond = temp1.getKey();
					curBlock = temp1.getValue();
					ins.ifTrue = bb.blockID;
					ins.ifFalse = fbb.blockID;
					curBlock.ifTrue = bb;
					curBlock.ifFalse = fbb;
					curBlock.insList.add(ins);
					
					nowBlock = pass(((SlctStmtNode)now).elifStmtNode.get(i), curScope, bb, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg).getValue();
					
					if(i == maxSize - 1
							&& ((SlctStmtNode)now).haveElse)
					{
						JumpIns ins2 = new JumpIns();
						ins2.insName = "jump";
						ins2.target = finalBlock.blockID;
						ins2.toBlock = finalBlock;
						nowBlock.insList.add(ins2);
						nowBlock.to = finalBlock;
						
						nowBlock = pass(((SlctStmtNode)now).elseStmtNode, curScope, fbb, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg).getValue();
						
						JumpIns ins3 = new JumpIns();
						ins3.insName = "jump";
						ins3.target = finalBlock.blockID;
						ins3.toBlock = finalBlock;
						nowBlock.insList.add(ins3);
						nowBlock.to = finalBlock;
					}
					else if(i == maxSize - 1)
					{
						ins.ifFalse = finalBlock.blockID;
						curBlock.ifFalse = finalBlock;
						
						JumpIns ins2 = new JumpIns();
						ins2.insName = "jump";
						ins2.target = finalBlock.blockID;
						ins2.toBlock = finalBlock;
						nowBlock.insList.add(ins2);
						nowBlock.to = finalBlock;
					}
					else
					{
						JumpIns ins2 = new JumpIns();
						ins2.insName = "jump";
						ins2.target = finalBlock.blockID;
						ins2.toBlock = finalBlock;
						nowBlock.insList.add(ins2);
						nowBlock.to = finalBlock;
						curBlock = fbb;
					}
				}
				return new Pair<>(null, finalBlock);
			}
			
		}
		
		else if(now instanceof ForInitNode)
		{
			// Abandoned
		}
		
		else if(now instanceof ForNode)
		{
			curScope = ((ForNode)now).scope;
			BasicBlock finalBlock = new BasicBlock();
			finalBlock.ofFunc = curBlock.ofFunc;
			
			if(((ForNode)now).haveInit)
			{
				Pair<String, BasicBlock> temp = pass(((ForNode)now).initExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				curBlock = temp.getValue();
			}
			
			BasicBlock bb = new BasicBlock();
			bb.ofFunc = curBlock.ofFunc;
			
			if(((ForNode)now).haveCond)
			{
				JumpIns ins = new JumpIns();
				ins.insName = "br";
				Pair<String, BasicBlock> temp = pass(((ForNode)now).condExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				ins.cond = temp.getKey();
				curBlock = temp.getValue();
				ins.ifTrue = bb.blockID;
				ins.ifFalse = finalBlock.blockID;
				curBlock.ifTrue = bb;
				curBlock.ifFalse = finalBlock;
				curBlock.insList.add(ins);
			}
			else
			{
				JumpIns ins = new JumpIns();
				ins.insName = "jump";
				ins.target = bb.blockID;
				ins.toBlock = bb;
				curBlock.insList.add(ins);
				curBlock.to = bb;
				curBlock.ifFalse = finalBlock;
			}
			
			BasicBlock nowBlock = pass(((ForNode)now).stmtNode, curScope, bb, false, true, curBlock, null, null, depth, inlineVari, il, rtnBlock, rtnReg).getValue();
			
			if(((ForNode)now).haveStep)
			{
				nowBlock = pass(((ForNode)now).stepExprNode, curScope, nowBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg).getValue();
			}
			
			if(((ForNode)now).haveCond)
			{
				JumpIns ins = new JumpIns();
				ins.insName = "br";
				Pair<String, BasicBlock> temp = pass(((ForNode)now).condExprNode, curScope, nowBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				ins.cond = temp.getKey();
				nowBlock = temp.getValue();
				ins.ifTrue = bb.blockID;
				ins.ifFalse = finalBlock.blockID;
				nowBlock.ifTrue = bb;
				nowBlock.ifFalse = finalBlock;
				nowBlock.insList.add(ins);
			}
			else
			{
				JumpIns ins = new JumpIns();
				ins.insName = "jump";
				ins.target = bb.blockID;
				ins.toBlock = bb;
				nowBlock.to = bb;
				nowBlock.insList.add(ins);
			}
			return new Pair<>(null, finalBlock);
		}
		
		else if(now instanceof WhileNode)
		{
			curScope = ((WhileNode)now).scope;
			BasicBlock finalBlock = new BasicBlock();
			finalBlock.ofFunc = curBlock.ofFunc;
			
			BasicBlock bb = new BasicBlock();
			bb.ofFunc = curBlock.ofFunc;
			
			JumpIns ins = new JumpIns();
			ins.insName = "br";
			Pair<String, BasicBlock> temp = pass(((WhileNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
			ins.cond = temp.getKey();
			curBlock = temp.getValue();
			ins.ifTrue = bb.blockID;
			ins.ifFalse = finalBlock.blockID;
			curBlock.ifTrue = bb;
			curBlock.ifFalse = finalBlock;
			curBlock.insList.add(ins);
			
			BasicBlock nowBlock = pass(((WhileNode)now).stmtNode, curScope, bb, false, true, curBlock, null, null, depth, inlineVari, il, rtnBlock, rtnReg).getValue();
			
			ins = new JumpIns();
			ins.insName = "br";
			Pair<String, BasicBlock> temp1 = pass(((WhileNode)now).exprNode, curScope, nowBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
			ins.cond = temp1.getKey();
			nowBlock = temp1.getValue();
			ins.ifTrue = bb.blockID;
			ins.ifFalse = finalBlock.blockID;
			nowBlock.ifTrue = bb;
			nowBlock.ifFalse = finalBlock;
			nowBlock.insList.add(ins);
			
			return new Pair<>(null, finalBlock);
		}
		
		else if(now instanceof BreakNode)
		{
			JumpIns ins = new JumpIns();
			ins.insName = "jump";
			ins.target = recHead.ifFalse.blockID;
			ins.toBlock = recHead.ifFalse;
			curBlock.insList.add(ins);
			curBlock.to = recHead.ifFalse;
			return new Pair<>(null, curBlock);
		}
		
		else if(now instanceof ContinueNode)
		{
			JumpIns ins = new JumpIns();
			ins.insName = "jump";
			ins.target = recHead.ifTrue.blockID;
			ins.toBlock = recHead.ifTrue;
			curBlock.insList.add(ins);
			curBlock.to = recHead.ifTrue;
			return new Pair<>(null, curBlock);
		}
		
		else if(now instanceof ReturnNode)
		{
			JumpIns ins = new JumpIns();
			ins.insName = "ret";
			Pair<String, BasicBlock> temp = pass(((ReturnNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
			ins.src = temp.getKey();
			curBlock = temp.getValue();
			if(il)
			{
				MovIns mi = new MovIns();
				mi.insName = "move";
				mi.dest = rtnReg;
				mi.src = ins.src;
				curBlock.insList.add(mi);
				
				JumpIns ji = new JumpIns();
				ji.insName = "jump";
				ji.target = rtnBlock.blockID;
				ji.toBlock = rtnBlock;
				curBlock.insList.add(ji);
				curBlock.to = rtnBlock;
				return new Pair<>(ins.src, curBlock);
			}
			curBlock.insList.add(ins);
			curBlock.to = null;
			return new Pair<>(null, curBlock);
		}
		
		else if(now instanceof StraightReturnNode)
		{
			JumpIns ins = new JumpIns();
			ins.insName = "ret";
			ins.src = "0";
			if(il)
			{
				MovIns mi = new MovIns();
				mi.insName = "move";
				mi.dest = rtnReg;
				mi.src = ins.src;
				curBlock.insList.add(mi);
				
				JumpIns ji = new JumpIns();
				ji.insName = "jump";
				ji.target = rtnBlock.blockID;
				ji.toBlock = rtnBlock;
				curBlock.insList.add(ji);
				curBlock.to = rtnBlock;
				return new Pair<>(ins.src, curBlock);
			}
			curBlock.insList.add(ins);
			curBlock.to = null;
			return new Pair<>(null, curBlock);
		}
		
		else if(now instanceof CreatorArrayNode)
		{
			int dim = ((CreatorArrayNode)now).exprNode.size() + 1;
			MemAccIns ins = new MemAccIns();
			ins.insName = "alloc";
			ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
			ins.size = (dim * bytes.get("addr")) + "";
			curBlock.insList.add(ins);
			
			String first = "";
			for(int i = 0 ; i < dim - 1; i++)
			{
				MemAccIns mins = new MemAccIns();
				mins.insName = "store";
				mins.size = bytes.get("addr") + "";
				mins.addr = ins.dest;
				Pair<String, BasicBlock> temp = pass(((CreatorArrayNode)now).exprNode.get(i), curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				mins.src = temp.getKey();
				curBlock = temp.getValue();
				if(i == 0)
					first = mins.src;
				mins.offset = i * bytes.get("addr");
				curBlock.insList.add(mins);
			}
			
			MemAccIns mins = new MemAccIns();
			mins.insName = "store";
			mins.size = bytes.get("addr") + "";
			mins.addr = ins.dest;
			mins.src = "0";
			mins.offset = (dim - 1) * bytes.get("addr");
			curBlock.insList.add(mins);
			
			FuncCallIns fins = new FuncCallIns();
			fins.insName = "call";
			fins.funcName = "_alloc";
			fins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
			fins.ops.add(first);
			fins.ops.add(ins.dest);
			curBlock.insList.add(fins);
			
			return new Pair<>(fins.dest, curBlock);
		}
		
		else if(now instanceof CreatorSingleNode)
		{
			int allocSize = bytes.get(((CreatorSingleNode)now).singleTypeNode.type);
			MemAccIns ins = new MemAccIns();
			ins.insName = "alloc";
			ins.size = allocSize + "";
			String reg = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
			ins.dest = reg;
			curBlock.insList.add(ins);
			
			String className = ((CreatorSingleNode)now).singleTypeNode.type;
			if(topScope.classMap.containsKey(className))
			{
				FuncCallIns fins = new FuncCallIns();
				fins.insName = "call";
				fins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
				fins.funcName = className + "." + className;
				fins.ops.add(reg);
				curBlock.insList.add(fins);
				return new Pair<>(fins.dest, curBlock);
			}
			return new Pair<>(reg, curBlock);
		}
		
		else if(now instanceof SuffixIncDecNode)
		{
			String outcome = pass(((SuffixIncDecNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg).getKey();
			MovIns ins = new MovIns();
			ins.insName = "move";
			ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
			ins.src = outcome;
			curBlock.insList.add(ins);
			
			ArithIns ains = new ArithIns();
			if(((SuffixIncDecNode)now).op.equals("++"))
				ains.insName = "add";
			else
				ains.insName = "sub";
			ains.dest = ains.src1 = outcome;
			ains.src2 = "1";
			curBlock.insList.add(ains);
			
			int ls = curBlock.insList.size();
			//System.err.printf("HHH%d\n", ls);
			if(ls >= 3)
			{
				Ins l2 = curBlock.insList.get(ls - 3);
				if(l2.insName.equals("load"))
				{
					MemAccIns mins = new MemAccIns();
					mins.insName = "store";
					mins.addr = ((MemAccIns)l2).addr;
					mins.src = ((MemAccIns)l2).dest;
					mins.size = bytes.get("addr") + "";
					mins.offset = 0;
					curBlock.insList.add(mins);
				}
			}
			
			return new Pair<>(ins.dest, curBlock);
		}
		
		else if(now instanceof FuncCallNode)
		{
			if(((FuncCallNode)now).exprNode instanceof MemberNode)
			{
				String className = ((MemberNode)((FuncCallNode)now).exprNode).exprNode.ofType;
				String funcName = ((MemberNode)((FuncCallNode)now).exprNode).idNode.id;
				FuncCallIns ins = new FuncCallIns();
				ins.insName = "call";
				ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
				ins.funcName = className + "." + funcName;
				Pair<String, BasicBlock> temp1 = pass(((MemberNode)((FuncCallNode)now).exprNode).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				ins.ops.add(temp1.getKey());
				curBlock = temp1.getValue();
				if(((FuncCallNode)now).haveParamList)
				{
					for(ASTNode node : ((FuncCallNode)now).paramListNode.exprNode)
					{
						Pair<String, BasicBlock> temp = pass(node, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
						ins.ops.add(temp.getKey());
						curBlock = temp.getValue();
					}
				}
				curBlock.insList.add(ins);
				return new Pair<>(ins.dest, curBlock);
			}
			else if(((FuncCallNode)now).exprNode instanceof NewNode)
			{
				String funcName = ((NewNode)((FuncCallNode)now).exprNode).ofType;
				FuncCallIns ins = new FuncCallIns();
				ins.insName = "call";
				ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
				ins.funcName = funcName + "." + funcName;
				Pair<String, BasicBlock> temp = pass(((NewNode)((FuncCallNode)now).exprNode).creatorNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				ins.ops.add(temp.getKey());
				curBlock = temp.getValue();
				curBlock.insList.add(ins);
				return new Pair<>(ins.dest, curBlock);
			}
			else
			{
				if(depth < inlineDepth && ((FuncCallNode)now).source == null
						&& !niFunc.contains(((IdNode)((FuncCallNode)now).exprNode).id))
				{
					iln++;
					System.err.println(((IdNode)((FuncCallNode)now).exprNode).id);
					String funcName = ((IdNode)((FuncCallNode)now).exprNode).id;
					MovIns ins = new MovIns();
					ins.insName = "move";
					ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
					
					FuncIns fi = null;
					for(Map.Entry<String, FuncIns> entry : topScope.funcMap.entrySet())
					{
						if(entry.getValue().name.equals(funcName))
						{
							fi = entry.getValue();
							break;
						}
					}
					
					BasicBlock rtnto = new BasicBlock();
					rtnto.ofFunc = curBlock.ofFunc;
					
					Map<String, String> inlineMap = new HashMap<>();
					if(((FuncCallNode)now).haveParamList)
					{
						int cnt = 0;
						for(ASTNode node : ((FuncCallNode)now).paramListNode.exprNode)
						{
							Pair<String, BasicBlock> temp = pass(node, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
							String op = temp.getKey();
							curBlock = temp.getValue();
							if(true)//!op.substring(0, 1).equals("$") && !op.substring(0, 1).equals("@"))
							{
								MovIns mi = new MovIns();
								mi.insName = "move";
								mi.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
								mi.src = op;
								curBlock.insList.add(mi);
								op = mi.dest;
							}
							String param = fi.param.get(cnt++).name;
							inlineMap.put(param, op);
						}
					}
					FuncDeclNode toil = null;
					for(ASTNode node : rootNode.progSecNode)
					{
						if(node instanceof FuncDeclNode && ((FuncDeclNode)node).id.equals(funcName))
						{
							toil = (FuncDeclNode)node;
							break;
						}
					}
					Pair<String, BasicBlock> temp = pass(toil, curScope, curBlock, false, true, recHead, trueBlock, falseBlock,
							depth + 1, inlineMap, true, rtnto, ins.dest);
					ins.src = temp.getKey();
					curBlock = temp.getValue();
					//curBlock.insList.add(ins);
					return new Pair<>(ins.dest, rtnto);
				}
				else
				{
					String funcName = ((IdNode)(((FuncCallNode)now).exprNode)).id;
					FuncCallIns ins = new FuncCallIns();
					ins.insName = "call";
					ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
					ins.funcName = funcName;
					
					if(((FuncCallNode)now).source != null)
					{
						ins.funcName = ((FuncCallNode)now).source + "." + ins.funcName;
						ins.ops.add(curBlock.ofFunc.param.get(0));
					}
					
					if(((FuncCallNode)now).haveParamList)
					{
						for(ASTNode node : ((FuncCallNode)now).paramListNode.exprNode)
						{
							Pair<String, BasicBlock> temp = pass(node, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
							ins.ops.add(temp.getKey());
							curBlock = temp.getValue();
						}
					}
					curBlock.insList.add(ins);
					return new Pair<>(ins.dest, curBlock);
				}
			}
		}
		
		else if(now instanceof IndexNode)
		{
			MovIns mmins = new MovIns();
			mmins.insName = "move";
			mmins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
			Pair<String, BasicBlock> temp = pass(((IndexNode)now).indexExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
			mmins.src = temp.getKey();
			curBlock = temp.getValue();
			
			curBlock.insList.add(mmins);
			
			ArithIns ins = new ArithIns();
			ins.insName = "mul";
			ins.dest = ins.src1 = mmins.dest;
			ins.src2 = bytes.get("addr") + "";
			curBlock.insList.add(ins);
			
			ArithIns ins2 = new ArithIns();
			ins2.insName = "add";
			ins2.dest = ins2.src1 = ins.dest;
			ins2.src2 = bytes.get("addr") + "";
			curBlock.insList.add(ins2);
			
			ArithIns ins3 = new ArithIns();
			ins3.insName = "add";
			ins3.dest = ins3.src1 = ins2.dest;
			Pair<String, BasicBlock> temp1 = pass(((IndexNode)now).arrayExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
			ins3.src2 = temp1.getKey();
			curBlock = temp1.getValue();
			
			curBlock.insList.add(ins3);
			
			if(wantAddr)
			{
				return new Pair<>(ins.dest, curBlock);
			}
			
			MemAccIns ins4 = new MemAccIns();
			ins4.insName = "load";
			ins4.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
			ins4.size = bytes.get("addr") + "";
			ins4.addr = ins3.dest;
			ins4.offset = 0;
			curBlock.insList.add(ins4);
			
			return new Pair<>(ins4.dest, curBlock);
		}
		
		else if(now instanceof MemberNode)
		{
			String className = ((MemberNode)now).exprNode.ofType;
			String memberName = ((MemberNode)now).idNode.id;
			ClassIns temp = topScope.classMap.get(className);
			VariIns vtemp = temp.variMap.get(memberName);
			
			MovIns mins = new MovIns();
			mins.insName = "move";
			mins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
			Pair<String, BasicBlock> temp1 = pass(((MemberNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
			mins.src = temp1.getKey();
			curBlock = temp1.getValue();
			curBlock.insList.add(mins);
			
			ArithIns ins = new ArithIns();
			ins.insName = "add";
			ins.dest = ins.src1 = mins.dest;
			ins.src2 = vtemp.offset + "";
			curBlock.insList.add(ins);
			
			if(wantAddr)
			{
				return new Pair<>(ins.dest, curBlock);
			}
			
			MemAccIns ins2 = new MemAccIns();
			ins2.insName = "load";
			ins2.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
			ins2.size = bytes.get("addr") + "";
			ins2.addr = ins.dest;
			ins2.offset = 0;
			curBlock.insList.add(ins2);
			
			return new Pair<>(ins2.dest, curBlock);
		}
		
		else if(now instanceof PrefixIncDecNode)
		{
			ArithIns ins = new ArithIns();
			if(((PrefixIncDecNode)now).op.equals("++"))
				ins.insName = "add";
			else
				ins.insName = "sub";
			ins.dest = ins.src1 = pass(((PrefixIncDecNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg).getKey();
			ins.src2 = "1";
			curBlock.insList.add(ins);
			
			int ls = curBlock.insList.size();
			if(ls >= 2)
			{
				Ins l2 = curBlock.insList.get(ls - 2);
				if(l2.insName.equals("load"))
				{
					MemAccIns mins = new MemAccIns();
					mins.insName = "store";
					mins.addr = ((MemAccIns)l2).addr;
					mins.src = ((MemAccIns)l2).dest;
					mins.size = bytes.get("addr") + "";
					mins.offset = 0;
					curBlock.insList.add(mins);
				}
			}
			return new Pair<>(ins.dest, curBlock);
		}
		
		else if(now instanceof PosNegNode)
		{
			if(((PosNegNode)now).op.equals("-"))
			{
				MovIns mins = new MovIns();
				mins.insName = "move";
				mins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
				Pair<String, BasicBlock> temp = pass(((PosNegNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				mins.src = temp.getKey();
				curBlock = temp.getValue();
				curBlock.insList.add(mins);
				
				ArithIns ins = new ArithIns();
				ins.insName = "neg";
				ins.dest = ins.src1 = mins.dest;
				curBlock.insList.add(ins);
				return new Pair<>(ins.dest, curBlock);
			}
			else if(((PosNegNode)now).op.equals("+"))
			{
				return pass(((PosNegNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
			}
		}
		
		else if(now instanceof NotNode)
		{
			if(((NotNode)now).op.equals("!"))
			{
				MovIns mins = new MovIns();
				mins.insName = "move";
				mins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
				mins.src = "1";
				curBlock.insList.add(mins);
				
				ArithIns ins = new ArithIns();
				ins.insName = "sub";
				ins.dest = ins.src1 = mins.dest;
				Pair<String, BasicBlock> temp = pass(((NotNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				ins.src2 = temp.getKey();
				curBlock = temp.getValue();
				curBlock.insList.add(ins);
				return new Pair<>(ins.dest, curBlock);
			}
			else if(((NotNode)now).op.equals("~"))
			{
				MovIns mins = new MovIns();
				mins.insName = "move";
				mins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
				Pair<String, BasicBlock> temp = pass(((NotNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				mins.src = temp.getKey();
				curBlock = temp.getValue();
				curBlock.insList.add(mins);
				
				BitIns ins = new BitIns();
				ins.insName = "not";
				ins.dest = ins.src1 = mins.dest;
				curBlock.insList.add(ins);
				return new Pair<>(ins.dest, curBlock);
			}
		}
		
		else if(now instanceof NewNode)
		{
			return pass(((NewNode)now).creatorNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
		}
		
		else if(now instanceof BinaryNode)
		{
			if(((BinaryNode)now).op.equals("+")
					&& ((BinaryNode)now).leftExprNode.ofType.equals("string"))
			{
				Pair<String, BasicBlock> temp1 = pass(((BinaryNode)now).leftExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				String s1 = temp1.getKey();
				curBlock = temp1.getValue();
				Pair<String, BasicBlock> temp2 = pass(((BinaryNode)now).rightExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				String s2 = temp2.getKey();
				curBlock = temp2.getValue();
				
				FuncCallIns ins = new FuncCallIns();
				ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
				ins.insName = "call";
				ins.funcName = "string.cat";
				ins.ops.add(s1);
				ins.ops.add(s2);
				curBlock.insList.add(ins);
				return new Pair<>(ins.dest, curBlock);
			}
			else if(((BinaryNode)now).op.equals("+")
					|| ((BinaryNode)now).op.equals("-")
					|| ((BinaryNode)now).op.equals("*")
					|| ((BinaryNode)now).op.equals("/")
					|| ((BinaryNode)now).op.equals("%"))
			{
				Pair<String, BasicBlock> temp1 = pass(((BinaryNode)now).leftExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				String s1 = temp1.getKey();
				curBlock = temp1.getValue();
				
				Pair<String, BasicBlock> temp2 =  pass(((BinaryNode)now).rightExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				String s2 = temp2.getKey();
				curBlock = temp2.getValue();
				
				MovIns mins = new MovIns();
				mins.insName = "move";
				mins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
				mins.src = s1;
				curBlock.insList.add(mins);
				
				ArithIns ins = new ArithIns();
				if(((BinaryNode)now).op.equals("+"))
					ins.insName = "add";
				if(((BinaryNode)now).op.equals("-"))
					ins.insName = "sub";
				if(((BinaryNode)now).op.equals("*"))
					ins.insName = "mul";
				if(((BinaryNode)now).op.equals("/"))
					ins.insName = "div";
				if(((BinaryNode)now).op.equals("%"))
					ins.insName = "rem";
				ins.dest = ins.src1 = mins.dest;
				ins.src2 = s2;
				curBlock.insList.add(ins);
				return new Pair<>(ins.dest, curBlock);
			}
			else if(((BinaryNode)now).op.equals("<<")
					|| ((BinaryNode)now).op.equals(">>")
					|| ((BinaryNode)now).op.equals("&")
					|| ((BinaryNode)now).op.equals("|")
					|| ((BinaryNode)now).op.equals("^"))
			{
				Pair<String, BasicBlock> temp1 = pass(((BinaryNode)now).leftExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				String s1 = temp1.getKey();
				curBlock = temp1.getValue();
				
				Pair<String, BasicBlock> temp2 = pass(((BinaryNode)now).rightExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				String s2 = temp2.getKey();
				curBlock = temp2.getValue();
				
				MovIns mins = new MovIns();
				mins.insName = "move";
				mins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
				mins.src = s1;
				curBlock.insList.add(mins);
				
				BitIns ins = new BitIns();
				if(((BinaryNode)now).op.equals("<<"))
					ins.insName = "shl";
				if(((BinaryNode)now).op.equals(">>"))
					ins.insName = "shr";
				if(((BinaryNode)now).op.equals("&"))
					ins.insName = "and";
				if(((BinaryNode)now).op.equals("|"))
					ins.insName = "or";
				if(((BinaryNode)now).op.equals("^"))
					ins.insName = "xor";
				ins.dest = ins.src1 = mins.dest; //"$_t" + (tempRegNum++) + "_" + curScope.scopeID;
				ins.src2 = s2;
				curBlock.insList.add(ins);
				return new Pair<>(ins.dest, curBlock);
			}
			else if(((BinaryNode)now).leftExprNode.ofType.equals("string")
					&& (((BinaryNode)now).op.equals("<")
					|| ((BinaryNode)now).op.equals(">")
					|| ((BinaryNode)now).op.equals("<=")
					|| ((BinaryNode)now).op.equals(">=")
					|| ((BinaryNode)now).op.equals("==")
					|| ((BinaryNode)now).op.equals("!=")))
			{
				Pair<String, BasicBlock> temp1 = pass(((BinaryNode)now).leftExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				String s1 = temp1.getKey();
				curBlock = temp1.getValue();
				
				Pair<String, BasicBlock> temp2 = pass(((BinaryNode)now).rightExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				String s2 = temp2.getKey();
				curBlock = temp2.getValue();
				
				FuncCallIns ins = new FuncCallIns();
				ins.insName = "call";
				ins.funcName = "string.cmp";
				ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
				ins.ops.add(s1);
				ins.ops.add(s2);
				curBlock.insList.add(ins);
				
				CondSetIns cins = new CondSetIns();
				if(((BinaryNode)now).op.equals("<"))
					cins.insName = "slt32";
				if(((BinaryNode)now).op.equals(">"))
					cins.insName = "sgt32";
				if(((BinaryNode)now).op.equals("<="))
					cins.insName = "sle32";
				if(((BinaryNode)now).op.equals(">="))
					cins.insName = "sge32";
				if(((BinaryNode)now).op.equals("=="))
					cins.insName = "seq32";
				if(((BinaryNode)now).op.equals("!="))
					cins.insName = "sne32";
				cins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
				cins.src1 = ins.dest;
				cins.src2 = "0";
				curBlock.insList.add(cins);
				
				return new Pair<>(cins.dest, curBlock);
			}
			else if(((BinaryNode)now).op.equals("<")
					|| ((BinaryNode)now).op.equals(">")
					|| ((BinaryNode)now).op.equals("<=")
					|| ((BinaryNode)now).op.equals(">=")
					|| ((BinaryNode)now).op.equals("==")
					|| ((BinaryNode)now).op.equals("!="))
			{
				CondSetIns ins = new CondSetIns();
				if(((BinaryNode)now).op.equals("<"))
					ins.insName = "slt";
				if(((BinaryNode)now).op.equals(">"))
					ins.insName = "sgt";
				if(((BinaryNode)now).op.equals("<="))
					ins.insName = "sle";
				if(((BinaryNode)now).op.equals(">="))
					ins.insName = "sge";
				if(((BinaryNode)now).op.equals("=="))
					ins.insName = "seq";
				if(((BinaryNode)now).op.equals("!="))
					ins.insName = "sne";
				ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
				Pair<String, BasicBlock> temp1 = pass(((BinaryNode)now).leftExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				ins.src1 = temp1.getKey();
				curBlock = temp1.getValue();
				
				Pair<String, BasicBlock> temp2 = pass(((BinaryNode)now).rightExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				ins.src2 = temp2.getKey();
				curBlock = temp2.getValue();
				
				curBlock.insList.add(ins);
				
				BasicBlock bt = new BasicBlock(), bf = new BasicBlock(), bl = new BasicBlock();
				bt.ofFunc = bf.ofFunc = bl.ofFunc = curBlock.ofFunc;
				curBlock.ifTrue = bt;
				curBlock.ifFalse = bf;
				bt.to = bf.to = bl;
				
				JumpIns i1 = new JumpIns();
				i1.insName = "br";
				i1.cond = ins.dest;
				i1.ifTrue = bt.blockID;
				i1.ifFalse = bf.blockID;
				curBlock.insList.add(i1);
				
				MovIns i4 = new MovIns();
				i4.insName = "move";
				i4.dest = ins.dest;
				i4.src = "1";
				bt.insList.add(i4);
				
				JumpIns i2 = new JumpIns();
				i2.insName = "jump";
				i2.target = bl.blockID;
				i2.toBlock = bl;
				bt.insList.add(i2);
				
				MovIns i5 = new MovIns();
				i5.insName = "move";
				i5.dest = ins.dest;
				i5.src = "0";
				bf.insList.add(i5);
				
				JumpIns i3 = new JumpIns();
				i3.insName = "jump";
				i3.target = bl.blockID;
				i3.toBlock = bl;
				bf.insList.add(i3);
				
				return new Pair<>(ins.dest, bl);
			}
			else if(((BinaryNode)now).op.equals("&&")
					|| ((BinaryNode)now).op.equals("||"))
			{
				
				if(trueBlock == null)
				{
					trueBlock = new BasicBlock();
					falseBlock = new BasicBlock();
					BasicBlock finalBlock = new BasicBlock();
					trueBlock.to = falseBlock.to = finalBlock;
					trueBlock.ofFunc = falseBlock.ofFunc = finalBlock.ofFunc = curBlock.ofFunc;
					
					MovIns ins = new MovIns();
					ins.insName = "move";
					ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
					ins.src = "1";
					trueBlock.insList.add(ins);
					
					JumpIns jins = new JumpIns();
					jins.insName = "jump";
					jins.target = finalBlock.blockID;
					jins.toBlock = finalBlock;
					trueBlock.insList.add(jins);
					
					MovIns ins2 = new MovIns();
					ins2.insName = "move";
					ins2.dest = ins.dest;
					ins2.src = "0";
					falseBlock.insList.add(ins2);
					
					JumpIns jins2 = new JumpIns();
					jins2.insName = "jump";
					jins2.target = finalBlock.blockID;
					jins2.toBlock = finalBlock;
					falseBlock.insList.add(jins2);
					
					if(((BinaryNode)now).op.equals("&&"))
					{
						BasicBlock nb = new BasicBlock();
						nb.ofFunc = curBlock.ofFunc;
						Pair<String, BasicBlock> temp = pass(((BinaryNode)now).leftExprNode, curScope, curBlock, false, true, recHead, nb, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
						String outcome = temp.getKey();
						curBlock = temp.getValue();
						
						JumpIns ains = new JumpIns();
						ains.insName = "br";
						ains.cond = outcome;
						ains.ifFalse = falseBlock.blockID;
						ains.ifTrue = nb.blockID;
						curBlock.insList.add(ains);
						
						curBlock.ifTrue = nb;
						curBlock.ifFalse = falseBlock;
						
						Pair<String, BasicBlock> temp1 = pass(((BinaryNode)now).rightExprNode, curScope, nb, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
						String outcome1 = temp1.getKey();
						curBlock = temp1.getValue();
						
						JumpIns ains2 = new JumpIns();
						ains2.insName = "br";
						ains2.cond = outcome1;
						ains2.ifTrue = trueBlock.blockID;
						ains2.ifFalse = falseBlock.blockID;
						curBlock.insList.add(ains2);
						
						curBlock.ifTrue = trueBlock;
						curBlock.ifFalse = falseBlock;
					}
					else if(((BinaryNode)now).op.equals("||"))
					{
						BasicBlock nb = new BasicBlock();
						nb.ofFunc = curBlock.ofFunc;
						Pair<String, BasicBlock> temp = pass(((BinaryNode)now).leftExprNode, curScope, curBlock, false, true,
								recHead, trueBlock, nb, depth, inlineVari, il, rtnBlock, rtnReg);
						String outcome = temp.getKey();
						curBlock = temp.getValue();
						
						JumpIns ains = new JumpIns();
						ains.insName = "br";
						ains.cond = outcome;
						ains.ifFalse = nb.blockID;
						ains.ifTrue = trueBlock.blockID;
						curBlock.insList.add(ains);
						
						curBlock.ifTrue = trueBlock;
						curBlock.ifFalse = nb;
						
						Pair<String, BasicBlock> temp1 = pass(((BinaryNode)now).rightExprNode, curScope, nb, false, true,
								recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
						String outcome1 = temp1.getKey();
						curBlock = temp1.getValue();
						
						JumpIns ains2 = new JumpIns();
						ains2.insName = "br";
						ains2.cond = outcome1;
						ains2.ifTrue = trueBlock.blockID;
						ains2.ifFalse = falseBlock.blockID;
						curBlock.insList.add(ains2);
						
						curBlock.ifTrue = trueBlock;
						curBlock.ifFalse = falseBlock;
					}
					return new Pair<>(ins.dest, finalBlock);
				}
				
				if(((BinaryNode)now).op.equals("&&"))
				{
					BasicBlock nb = new BasicBlock();
					nb.ofFunc = curBlock.ofFunc;
					Pair<String, BasicBlock> temp = pass(((BinaryNode)now).leftExprNode, curScope, curBlock, false, true, recHead, nb, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
					String outcome = temp.getKey();
					curBlock = temp.getValue();
					
					
					JumpIns ains = new JumpIns();
					ains.insName = "br";
					ains.cond = outcome;
					ains.ifFalse = falseBlock.blockID;
					ains.ifTrue = nb.blockID;
					curBlock.insList.add(ains);
					
					curBlock.ifTrue = nb;
					curBlock.ifFalse = falseBlock;
					
					return pass(((BinaryNode)now).rightExprNode, curScope, nb, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				}
				else if(((BinaryNode)now).op.equals("||"))
				{
					BasicBlock nb = new BasicBlock();
					nb.ofFunc = curBlock.ofFunc;
					Pair<String, BasicBlock> temp = pass(((BinaryNode)now).leftExprNode, curScope, curBlock, false, true,
							recHead, trueBlock, nb, depth, inlineVari, il, rtnBlock, rtnReg);
					String outcome = temp.getKey();
					curBlock = temp.getValue();
					
					JumpIns ains = new JumpIns();
					ains.insName = "br";
					ains.cond = outcome;
					ains.ifFalse = nb.blockID;
					ains.ifTrue = trueBlock.blockID;
					curBlock.insList.add(ains);
					
					curBlock.ifTrue = trueBlock;
					curBlock.ifFalse = nb;
					
					return pass(((BinaryNode)now).rightExprNode, curScope, nb, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				}
			}
		}
		
		else if(now instanceof AssignNode)
		{
			if(((AssignNode)now).leftExprNode instanceof IdNode
					&& !(((IdNode)((AssignNode)now).leftExprNode).ofScope instanceof ClassScope)
					&& (((AssignNode)now).leftExprNode.ofType.equals("int")
					|| ((AssignNode)now).leftExprNode.ofType.equals("bool")
					|| ((AssignNode)now).leftExprNode.ofType.equals("string")))
			{
				MovIns ins = new MovIns();
				ins.insName = "move";
				ins.dest = pass(((AssignNode)now).leftExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg).getKey();
				Pair<String, BasicBlock> temp = pass(((AssignNode)now).rightExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				ins.src = temp.getKey();
				curBlock = temp.getValue();
				curBlock.insList.add(ins);
				return new Pair<>(null, curBlock);
			}
			else if(((AssignNode)now).leftExprNode instanceof IdNode
					&& !(((IdNode)((AssignNode)now).leftExprNode).ofScope instanceof ClassScope))
			{
				MovIns ins = new MovIns();
				ins.insName = "move";
				ins.dest = pass(((AssignNode)now).leftExprNode, curScope, curBlock, true, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg).getKey();
				Pair<String, BasicBlock> temp = pass(((AssignNode)now).rightExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				ins.src = temp.getKey();
				curBlock = temp.getValue();
				curBlock.insList.add(ins);
				return new Pair<>(null, curBlock);
			}
			else
			{
				MemAccIns ins = new MemAccIns();
				ins.insName = "store";
				ins.addr = pass(((AssignNode)now).leftExprNode, curScope, curBlock, true, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg).getKey();
				Pair<String, BasicBlock> temp = pass(((AssignNode)now).rightExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
				ins.src = temp.getKey();
				curBlock = temp.getValue();
				ins.size = bytes.get("addr") + "";
				ins.offset = 0;
				curBlock.insList.add(ins);
				return new Pair<>(null, curBlock);
			}
		}
		
		else if(now instanceof IdNode)
		{
			if(((IdNode)now).ofScope instanceof ClassScope
					&& !(((IdNode)now).id.equals("this")))
			{
				String className = ((ClassScope)((IdNode)now).ofScope).name;
				String memberName = ((IdNode)now).id;
				ClassIns temp = topScope.classMap.get(className);
				VariIns vtemp = temp.variMap.get(memberName);
				
				MovIns mins = new MovIns();
				mins.insName = "move";
				mins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
				mins.src = "$this_" + ((IdNode)now).ofScope.scopeID;
				curBlock.insList.add(mins);
				
				ArithIns ins = new ArithIns();
				ins.insName = "add";
				ins.dest = ins.src1 = mins.dest;
				ins.src2 = vtemp.offset + "";
				curBlock.insList.add(ins);
				
				if(wantAddr)
				{
					return new Pair<>(ins.dest, curBlock);
				}
				
				MemAccIns ins2 = new MemAccIns();
				ins2.insName = "load";
				ins2.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID + (il ? "_il_" + (iln): "");
				ins2.size = bytes.get("addr") + "";
				ins2.addr = ins.dest;
				ins2.offset = 0;
				curBlock.insList.add(ins2);
				
				return new Pair<>(ins2.dest, curBlock);
			}
			String temp = "";
			if(((IdNode)now).ofScope == topScope)
				temp = "@" + ((IdNode)now).id;
			else
			{
				if(il && inlineVari.containsKey(((IdNode)now).id))
				{
					temp = inlineVari.get(((IdNode)now).id);
				}
				else
					temp = "$" + ((IdNode)now).id + "_" + ((IdNode)now).ofScope.scopeID + (il ? "_il_" + (iln) : "");
			}
			return new Pair<>(temp, curBlock);
		}
		
		else if(now instanceof ConstNode)
		{
			if(((ConstNode)now).text.equals("null"))
				return new Pair<>("0", curBlock);
			if(((ConstNode)now).type.equals("int"))
				return new Pair<>(((ConstNode)now).text, curBlock);
			if(((ConstNode)now).text.equals("true"))
				return new Pair<>("1", curBlock);
			if(((ConstNode)now).text.equals("false"))
				return new Pair<>("0", curBlock);
			if(((ConstNode)now).type.equals("string"))
			{
				String text = ((ConstNode)now).text.substring(1, ((ConstNode)now).text.length() - 1);
				text = text + "\0";
				int len = text.length();
				
				/*
				MemAccIns ins = new MemAccIns();
				ins.insName = "alloc";
				ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
				ins.size = "256";
				curBlock.insList.add(ins);
				*/
				
				MemAccIns ins3 = new MemAccIns();
				ins3.insName = "storeStr";
				ins3.addr = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
				for(int i = 0; i < len; i++)
				{
					ins3.constStr.add((int)(text.charAt(i)));
				}
				curBlock.insList.add(ins3);
				return new Pair<>(ins3.addr, curBlock);
			}
		}
		
		else if(now instanceof SubExprNode)
		{
			return pass(((SubExprNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock, depth, inlineVari, il, rtnBlock, rtnReg);
		}
		
		return null;
	}
}
