package IR;

import AbstractSyntaxTree.Nodes.*;
import IR.Instructions.*;
import ScopeCheck.Instances.ClassIns;
import ScopeCheck.Instances.ParamIns;
import ScopeCheck.Instances.VariIns;
import ScopeCheck.Scopes.*;
import com.sun.corba.se.impl.interceptors.PICurrent;
import com.sun.org.apache.bcel.internal.generic.BIPUSH;
import com.sun.xml.internal.org.jvnet.mimepull.MIMEConfig;
import javafx.util.Pair;
import org.w3c.dom.css.CSSImportRule;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IRGenerator
{
	public TopScope topScope;
	public ASTRootNode rootNode;
	
	public ArrayList<FuncBlock> funcBlock = new ArrayList<>();
	
	public Map<String, Integer> bytes = new HashMap<>();
	
	public int tempRegNum = 0;
	
	public IRGenerator(ASTRootNode root)
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
	}
	
	public void print()
	{
		for(FuncBlock fb : funcBlock)
		{
			fb.print();
		}
	}
	
	public void add_alloc()
	{
		FuncBlock _alloc = new FuncBlock("_alloc");
		funcBlock.add(_alloc);
		BasicBlock _alloc_entry = new BasicBlock("_alloc");
		_alloc.entry = _alloc_entry;
		_alloc_entry.ofFunc = _alloc;
		
		_alloc.param.add("$_cnt");
		_alloc.param.add("$_cnt_addr");
		
		ArithIns ains = new ArithIns();
		ains.insName = "mul";
		ains.dest = "$_t" + (tempRegNum++);
		ains.src1 = "$_cnt";
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
		
		ArithIns ains3 = new ArithIns();
		ains3.insName = "add";
		ains3.dest = "$_cnt_next_addr";
		ains3.src1 = "$_cnt_addr";
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
		
		CondSetIns cins2 = new CondSetIns();
		cins2.insName = "sgt";
		cins2.dest = "$_cond2";
		cins2.src1 = "$_cnt_next";
		cins2.src2 = "0";
		_alloc_entry.insList.add(cins2);
		
		ArithIns bins = new ArithIns();
		bins.insName = "mul";
		bins.dest = "$_cond";
		bins.src1 = "$_cond1";
		bins.src2 = "$_cond2";
		_alloc_entry.insList.add(bins);
		
		BasicBlock bb = new BasicBlock();
		BasicBlock fbb = new BasicBlock();
		_alloc_entry.ifTrue = bb;
		_alloc_entry.ifFalse = fbb;
		bb.ofFunc = fbb.ofFunc = _alloc;
		
		JumpIns jins = new JumpIns();
		jins.insName = "br";
		jins.cond = "$_cond";
		jins.ifTrue = bb.blockID;
		jins.ifFalse = fbb.blockID;
		_alloc_entry.insList.add(jins);
		
		JumpIns jins2 = new JumpIns();
		jins2.insName = "ret";
		jins2.src = ins.dest;
		fbb.insList.add(jins2);
		
		ArithIns bbins = new ArithIns();
		bbins.insName = "mul";
		bbins.dest = "$_t" + (tempRegNum++);
		bbins.src1 = "$_i";
		bbins.src2 = bytes.get("addr") + "";
		bb.insList.add(bbins);
		
		ArithIns bbins2 = new ArithIns();
		bbins2.insName = "add";
		bbins2.dest = bbins2.src1 = bbins.dest;
		bbins2.src2 = bytes.get("addr") + "";
		bb.insList.add(bbins2);
		
		ArithIns bbins3 = new ArithIns();
		bbins3.insName = "add";
		bbins3.dest = "$_t_d";
		bbins3.src1 = bbins2.dest;
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
	
	public void passRoot()
	{
		FuncBlock __init = new FuncBlock("__init");
		funcBlock.add(__init);
		BasicBlock __init_entry = new BasicBlock("__init");
		__init.entry = __init_entry;
		__init_entry.ofFunc = __init;
		
		//add_alloc();
		
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
				pass(node, ((FuncDeclNode)node).scope, entry, false, true, null, null, null);
			}
			else
				pass(node, topScope, __init_entry, false, true, null, null, null);
		}
		
		FuncCallIns ins = new FuncCallIns();
		ins.insName = "call";
		ins.dest = "$_t" + (tempRegNum++) + "_" + topScope.scopeID;
		ins.funcName = "main";
		__init_entry.insList.add(ins);
		
		JumpIns jins = new JumpIns();
		jins.insName = "ret";
		jins.src = ins.dest;
		__init_entry.insList.add(jins);
	}
	
	public Pair<String, BasicBlock> pass(ASTNode now, Scope curScope, BasicBlock curBlock, boolean wantAddr, boolean checkThis, BasicBlock recHead, BasicBlock trueBlock, BasicBlock falseBlock)
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
									ins.dest = "$" + ((VariInitNode)node).id + "_" + curScope.scopeID;
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
									ins.dest = "$" + ((VariInitNode)node).id + "_" + curScope.scopeID;
								ins.insName = "move";
								if(((ConstNode)((VariInitNode)node).exprNode).text.equals("true"))
									ins.src = "1";
								else
									ins.src = "0";
								curBlock.insList.add(ins);
							}
						}
						else
						{
							MovIns ins = new MovIns();
							if(curScope == topScope)
								ins.dest = "@" + ((VariInitNode)node).id;
							else
								ins.dest = "$" + ((VariInitNode)node).id + "_" + curScope.scopeID;
							ins.insName = "move";
							Pair<String, BasicBlock> temp = pass(((VariInitNode)node).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock);
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
							ins.dest = "$" + ((VariInitNode)node).id + "_" + curScope.scopeID;
						ins.insName = "move";
						Pair<String, BasicBlock> temp = pass(((VariInitNode)node).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock);
						ins.src = temp.getKey();
						curBlock = temp.getValue();
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
			A_A.param.add("$this_" + ((ClassDeclNode)now).scope.scopeID);
			BasicBlock A_A_entry = new BasicBlock(((ClassDeclNode)now).id + "." + ((ClassDeclNode)now).id);
			A_A.entry = A_A_entry;
			A_A_entry.ofFunc = A_A;
			
			String reg = "$this_" + ((ClassDeclNode)now).scope.scopeID;
			BasicBlock temp = A_A_entry;
			
			for(ASTNode node : ((ClassDeclNode)now).progSecNode)
			{
				if(node instanceof ConstructorNode)
				{
					temp = pass(node, ((ConstructorNode)node).scope, A_A_entry, false, true, recHead, trueBlock, falseBlock).getValue();
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
					pass(node, ((FuncDeclNode)node).scope, A_entry, false, true, recHead, trueBlock, falseBlock);
				}
			}
			
			JumpIns rIns = new JumpIns();
			rIns.insName = "ret";
			rIns.src = reg;
			temp.insList.add(rIns);
		}
		
		else if(now instanceof FuncDeclNode)
		{
			BasicBlock temp = pass(((FuncDeclNode)now).blockStmtNode, ((FuncDeclNode)now).blockStmtNode.scope, curBlock, false, true, recHead, trueBlock, falseBlock).getValue();
			if(((FuncDeclNode)now).typeNode.singleTypeNode.type.equals("void"))
			{
				JumpIns ins = new JumpIns();
				ins.insName = "ret";
				ins.src = "0";
				temp.insList.add(ins);
			}
			return new Pair<>(null, temp);
		}
		
		else if(now instanceof ConstructorNode)
		{
			return pass(((ConstructorNode)now).blockStmtNode, ((ConstructorNode)now).blockStmtNode.scope, curBlock, false, true, recHead, trueBlock, falseBlock);
		}
		
		else if(now instanceof BlockStmtNode)
		{
			curScope = ((BlockStmtNode)now).scope;
			for(ASTNode node : ((BlockStmtNode)now).progSecNode)
			{
				if(node instanceof SlctStmtNode)
				{
					BasicBlock ifFalse = new BasicBlock();
					curBlock.ifFalse = ifFalse;
					ifFalse.ofFunc = curBlock.ofFunc;
					curBlock = pass(node, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getValue();
				}
				else if(node instanceof ForInitNode)
				{
					// Abandoned
				}
				else if(node instanceof ForNode)
				{
					BasicBlock ifFalse = new BasicBlock();
					curBlock.ifFalse = ifFalse;
					curBlock = pass(node, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getValue();
				}
				else if(node instanceof WhileNode)
				{
					BasicBlock ifFalse = new BasicBlock();
					curBlock.ifFalse = ifFalse;
					ifFalse.ofFunc = curBlock.ofFunc;
					curBlock = pass(node, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getValue();
				}
				else
				{
					curBlock = pass(node, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getValue();
				}
			}
			return new Pair<>(null, curBlock);
		}
		
		else if(now instanceof ExprStmtNode)
		{
			if(!((ExprStmtNode)now).empty)
			{
				Pair<String, BasicBlock> temp = pass(((ExprStmtNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock);
				curBlock = temp.getValue();
			}
			return new Pair<>(null, curBlock);
		}
		
		else if(now instanceof SlctStmtNode)
		{
			BasicBlock finalBlock = curBlock.ifFalse;
			curBlock.ifFalse = null;
			
			if(((SlctStmtNode)now).elifExprNode.size() == 0)
			{
				BasicBlock bb = new BasicBlock();
				bb.ofFunc = curBlock.ofFunc;
				BasicBlock nowBlock;
				
				JumpIns ins = new JumpIns();
				ins.insName = "br";
				Pair<String, BasicBlock> temp = pass(((SlctStmtNode)now).ifExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock);
				ins.cond = temp.getKey();
				curBlock = temp.getValue();
				ins.ifTrue = bb.blockID;
				ins.ifFalse = finalBlock.blockID;
				curBlock.ifTrue = bb;
				curBlock.ifFalse = finalBlock;
				curBlock.insList.add(ins);
				
				nowBlock = pass(((SlctStmtNode)now).ifStmtNode, curScope, bb, false, true, recHead, trueBlock, falseBlock).getValue();
				
				JumpIns ins2 = new JumpIns();
				ins2.insName = "jump";
				ins2.target = finalBlock.blockID;
				nowBlock.insList.add(ins2);
				nowBlock.to = finalBlock;
				
				if(((SlctStmtNode)now).haveElse)
				{
					BasicBlock eb = new BasicBlock();
					eb.ofFunc = curBlock.ofFunc;
					curBlock.ifFalse = eb;
					
					ins.ifFalse = eb.blockID;
					
					nowBlock = pass(((SlctStmtNode)now).elseStmtNode, curScope, eb, false, true, recHead, trueBlock, falseBlock).getValue();
					
					JumpIns ins3 = new JumpIns();
					ins3.insName = "jump";
					ins3.target = finalBlock.blockID;
					nowBlock.insList.add(ins3);
					nowBlock.to = finalBlock;
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
				Pair<String, BasicBlock> temp = pass(((SlctStmtNode)now).ifExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock);
				ains.cond = temp.getKey();
				curBlock = temp.getValue();
				ains.ifTrue = abb.blockID;
				ains.ifFalse = afbb.blockID;
				curBlock.insList.add(ains);
				curBlock.ifTrue = abb;
				curBlock.ifFalse = afbb;
				
				nowBlock = pass(((SlctStmtNode)now).ifStmtNode, curScope, abb, false, true, recHead, trueBlock, falseBlock).getValue();
				
				JumpIns ains2 = new JumpIns();
				ains2.insName = "jump";
				ains2.target = finalBlock.blockID;
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
					Pair<String, BasicBlock> temp1 = pass(((SlctStmtNode)now).elifExprNode.get(i), curScope, curBlock, false, true, recHead, trueBlock, falseBlock);
					ins.cond = temp1.getKey();
					curBlock = temp1.getValue();
					ins.ifTrue = bb.blockID;
					ins.ifFalse = fbb.blockID;
					curBlock.ifTrue = bb;
					curBlock.ifFalse = fbb;
					curBlock.insList.add(ins);
					
					nowBlock = pass(((SlctStmtNode)now).elifStmtNode.get(i), curScope, bb, false, true, recHead, trueBlock, falseBlock).getValue();
				
					if(i == maxSize - 1
							&& ((SlctStmtNode)now).haveElse)
					{
						JumpIns ins2 = new JumpIns();
						ins2.insName = "jump";
						ins2.target = finalBlock.blockID;
						nowBlock.insList.add(ins2);
						nowBlock.to = finalBlock;
						
						nowBlock = pass(((SlctStmtNode)now).elseStmtNode, curScope, fbb, false, true, recHead, trueBlock, falseBlock).getValue();
						
						JumpIns ins3 = new JumpIns();
						ins3.insName = "jump";
						ins3.target = finalBlock.blockID;
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
						nowBlock.insList.add(ins2);
						nowBlock.to = finalBlock;
					}
					else
					{
						JumpIns ins2 = new JumpIns();
						ins2.insName = "jump";
						ins2.target = fbb.blockID;
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
			BasicBlock finalBlock = curBlock.ifFalse;
			curBlock.ifFalse = null;
			
			if(((ForNode)now).haveInit)
			{
				pass(((ForNode)now).initExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock);
			}
			
			BasicBlock bb = new BasicBlock();
			bb.ofFunc = curBlock.ofFunc;
			
			if(((ForNode)now).haveCond)
			{
				JumpIns ins = new JumpIns();
				ins.insName = "br";
				Pair<String, BasicBlock> temp = pass(((ForNode)now).condExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock);
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
				curBlock.insList.add(ins);
			}
			
			BasicBlock nowBlock = pass(((ForNode)now).stmtNode, curScope, bb, false, true, curBlock, null, null).getValue();
			
			if(((ForNode)now).haveStep)
			{
				nowBlock = pass(((ForNode)now).stepExprNode, curScope, nowBlock, false, true, recHead, trueBlock, falseBlock).getValue();
			}
			
			if(((ForNode)now).haveCond)
			{
				JumpIns ins = new JumpIns();
				ins.insName = "br";
				Pair<String, BasicBlock> temp = pass(((ForNode)now).condExprNode, curScope, nowBlock, false, true, recHead, trueBlock, falseBlock);
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
				nowBlock.to = bb;
				nowBlock.insList.add(ins);
			}
			return new Pair<>(null, finalBlock);
		}
		
		else if(now instanceof WhileNode)
		{
			curScope = ((WhileNode)now).scope;
			BasicBlock finalBlock = curBlock.ifFalse;
			curBlock.ifFalse = null;
			
			BasicBlock bb = new BasicBlock();
			bb.ofFunc = curBlock.ofFunc;
			
			JumpIns ins = new JumpIns();
			ins.insName = "br";
			Pair<String, BasicBlock> temp = pass(((WhileNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock);
			ins.cond = temp.getKey();
			curBlock = temp.getValue();
			ins.ifTrue = bb.blockID;
			ins.ifFalse = finalBlock.blockID;
			curBlock.ifTrue = bb;
			curBlock.ifFalse = finalBlock;
			curBlock.insList.add(ins);
			
			BasicBlock nowBlock = pass(((WhileNode)now).stmtNode, curScope, bb, false, true, curBlock, null, null).getValue();
			
			ins = new JumpIns();
			ins.insName = "br";
			Pair<String, BasicBlock> temp1 = pass(((WhileNode)now).exprNode, curScope, nowBlock, false, true, recHead, trueBlock, falseBlock);
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
			curBlock.insList.add(ins);
			return new Pair<>(null, curBlock);
		}
		
		else if(now instanceof ContinueNode)
		{
			JumpIns ins = new JumpIns();
			ins.insName = "jump";
			ins.target = recHead.ifTrue.blockID;
			curBlock.insList.add(ins);
			return new Pair<>(null, curBlock);
		}
		
		else if(now instanceof ReturnNode)
		{
			JumpIns ins = new JumpIns();
			ins.insName = "ret";
			ins.src = pass(((ReturnNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
			curBlock.insList.add(ins);
			return new Pair<>(null, curBlock);
		}
		
		else if(now instanceof StraightReturnNode)
		{
			JumpIns ins = new JumpIns();
			ins.insName = "ret";
			ins.src = "0";
			curBlock.insList.add(ins);
			return new Pair<>(null, curBlock);
		}
		
		else if(now instanceof CreatorArrayNode)
		{
			int dim = ((CreatorArrayNode)now).exprNode.size() + 1;
			MemAccIns ins = new MemAccIns();
			ins.insName = "alloc";
			ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
			ins.size = (dim * bytes.get("addr")) + "";
			curBlock.insList.add(ins);
			
			String first = "";
			for(int i = 0 ; i < dim - 1; i++)
			{
				MemAccIns mins = new MemAccIns();
				mins.insName = "store";
				mins.size = bytes.get("addr") + "";
				mins.addr = ins.dest;
				mins.src = pass(((CreatorArrayNode)now).exprNode.get(i), curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
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
			fins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
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
			String reg = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
			ins.dest = reg;
			curBlock.insList.add(ins);
			
			String className = ((CreatorSingleNode)now).singleTypeNode.type;
			if(topScope.classMap.containsKey(className))
			{
				FuncCallIns fins = new FuncCallIns();
				fins.insName = "call";
				fins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
				fins.funcName = className + "." + className;
				fins.ops.add(reg);
				curBlock.insList.add(fins);
				return new Pair<>(fins.dest, curBlock);
			}
			return new Pair<>(reg, curBlock);
		}
		
		else if(now instanceof SuffixIncDecNode)
		{
			String outcome = pass(((SuffixIncDecNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
			MovIns ins = new MovIns();
			ins.insName = "move";
			ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
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
				ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
				ins.funcName = className + "." + funcName;
				ins.ops.add(pass(((MemberNode)((FuncCallNode)now).exprNode).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey());
				if(((FuncCallNode)now).haveParamList)
				{
					for(ASTNode node : ((FuncCallNode)now).paramListNode.exprNode)
					{
						ins.ops.add(pass(node, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey());
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
				ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
				ins.funcName = funcName + "." + funcName;
				ins.ops.add(pass(((NewNode)((FuncCallNode)now).exprNode).creatorNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey());
				curBlock.insList.add(ins);
				return new Pair<>(ins.dest, curBlock);
			}
			else
			{
				String funcName = ((IdNode)(((FuncCallNode)now).exprNode)).id;
				FuncCallIns ins = new FuncCallIns();
				ins.insName = "call";
				ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
				ins.funcName = funcName;
				if(((FuncCallNode)now).haveParamList)
				{
					for(ASTNode node : ((FuncCallNode)now).paramListNode.exprNode)
					{
						ins.ops.add(pass(node, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey());
					}
				}
				curBlock.insList.add(ins);
				return new Pair<>(ins.dest, curBlock);
			}
		}
		
		else if(now instanceof IndexNode)
		{
			ArithIns ins = new ArithIns();
			ins.insName = "mul";
			ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
			ins.src1 = pass(((IndexNode)now).indexExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
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
			ins3.src2 = pass(((IndexNode)now).arrayExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
			curBlock.insList.add(ins3);
			
			if(wantAddr)
			{
				return new Pair<>(ins.dest, curBlock);
			}
			
			MemAccIns ins4 = new MemAccIns();
			ins4.insName = "load";
			ins4.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
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
			
			ArithIns ins = new ArithIns();
			ins.insName = "add";
			ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
			ins.src1 = pass(((MemberNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
			ins.src2 = vtemp.offset + "";
			curBlock.insList.add(ins);
			
			if(wantAddr)
			{
				return new Pair<>(ins.dest, curBlock);
			}
			
			MemAccIns ins2 = new MemAccIns();
			ins2.insName = "load";
			ins2.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
			ins2.size = bytes.get("addr") + "";
			ins2.addr = ins.dest;
			ins2.offset = 0;
			curBlock.insList.add(ins2);
			
			return new Pair<>(ins2.dest, curBlock);
		}
		
		else if(now instanceof PrefixIncDecNode)
		{
			ArithIns ins = new ArithIns();
			ins.insName = "add";
			ins.dest = ins.src1 = pass(((PrefixIncDecNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
			ins.src2 = "1";
			curBlock.insList.add(ins);
			
			return new Pair<>(ins.dest, curBlock);
		}
		
		else if(now instanceof PosNegNode)
		{
			if(((PosNegNode)now).op.equals("-"))
			{
				MovIns mins = new MovIns();
				mins.insName = "move";
				mins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
				mins.src = pass(((PosNegNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
				curBlock.insList.add(mins);
				
				ArithIns ins = new ArithIns();
				ins.insName = "neg";
				ins.dest = ins.src1 = mins.dest;
				curBlock.insList.add(ins);
				return new Pair<>(ins.dest, curBlock);
			}
			else if(((PosNegNode)now).op.equals("+"))
			{
				return pass(((PosNegNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock);
			}
		}
		
		else if(now instanceof NotNode)
		{
			if(((NotNode)now).op.equals("!"))
			{
				ArithIns ins = new ArithIns();
				ins.insName = "sub";
				ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
				ins.src2 = pass(((NotNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
				ins.src1 = "1";
				curBlock.insList.add(ins);
				return new Pair<>(ins.dest, curBlock);
			}
			else if(((NotNode)now).op.equals("~"))
			{
				MovIns mins = new MovIns();
				mins.insName = "move";
				mins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
				mins.src = pass(((NotNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
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
			return pass(((NewNode)now).creatorNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock);
		}
		
		else if(now instanceof BinaryNode)
		{
			if(((BinaryNode)now).op.equals("+")
					|| ((BinaryNode)now).op.equals("-")
					|| ((BinaryNode)now).op.equals("*")
					|| ((BinaryNode)now).op.equals("/")
					|| ((BinaryNode)now).op.equals("%"))
			{
				String s1 = pass(((BinaryNode)now).leftExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
				String s2 = pass(((BinaryNode)now).rightExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
				MovIns mins = new MovIns();
				mins.insName = "move";
				mins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
				mins.src = s1;
				curBlock.insList.add(mins);
				
				ArithIns ins = new ArithIns();
				if(((BinaryNode)now).op.equals("+"))
					ins.insName = "add";
				if(((BinaryNode)now).op.equals("-"))
					ins.insName = "sub";
				if(((BinaryNode)now).op.equals("*"))
					ins.insName = "imul";
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
				String s1 = pass(((BinaryNode)now).leftExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
				String s2 = pass(((BinaryNode)now).rightExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
				MovIns mins = new MovIns();
				mins.insName = "move";
				mins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
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
				ins.src2 = pass(((BinaryNode)now).rightExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
				curBlock.insList.add(ins);
				return new Pair<>(ins.dest, curBlock);
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
				ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
				ins.src1 = pass(((BinaryNode)now).leftExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
				ins.src2 = pass(((BinaryNode)now).rightExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
				curBlock.insList.add(ins);
				return new Pair<>(ins.dest, curBlock);
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
					ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
					ins.src = "1";
					trueBlock.insList.add(ins);
					
					JumpIns jins = new JumpIns();
					jins.insName = "jump";
					jins.target = finalBlock.blockID;
					trueBlock.insList.add(jins);
					
					MovIns ins2 = new MovIns();
					ins2.insName = "move";
					ins2.dest = ins.dest;
					ins2.src = "0";
					falseBlock.insList.add(ins2);
					
					JumpIns jins2 = new JumpIns();
					jins2.insName = "jump";
					jins2.target = finalBlock.blockID;
					falseBlock.insList.add(jins2);
					
					if(((BinaryNode)now).op.equals("&&"))
					{
						BasicBlock nb = new BasicBlock();
						nb.ofFunc = curBlock.ofFunc;
						Pair<String, BasicBlock> temp = pass(((BinaryNode)now).leftExprNode, curScope, curBlock, false, true, recHead, nb, falseBlock);
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
						
						Pair<String, BasicBlock> temp1 = pass(((BinaryNode)now).rightExprNode, curScope, nb, false, true, recHead, trueBlock, falseBlock);
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
						Pair<String, BasicBlock> temp = pass(((BinaryNode)now).leftExprNode, curScope, curBlock, false, true, recHead, trueBlock, nb);
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
						
						Pair<String, BasicBlock> temp1 = pass(((BinaryNode)now).rightExprNode, curScope, nb, false, true, recHead, trueBlock, falseBlock);
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
					Pair<String, BasicBlock> temp = pass(((BinaryNode)now).leftExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock);
					String outcome = temp.getKey();
					curBlock = temp.getValue();
					BasicBlock nb = new BasicBlock();
					nb.ofFunc = curBlock.ofFunc;
					
					JumpIns ains = new JumpIns();
					ains.insName = "br";
					ains.cond = outcome;
					ains.ifFalse = falseBlock.blockID;
					ains.ifTrue = nb.blockID;
					curBlock.insList.add(ains);
					
					curBlock.ifTrue = nb;
					curBlock.ifFalse = falseBlock;
					
					return pass(((BinaryNode)now).rightExprNode, curScope, nb, false, true, recHead, trueBlock, falseBlock);
				}
				else if(((BinaryNode)now).op.equals("||"))
				{
					Pair<String, BasicBlock> temp = pass(((BinaryNode)now).leftExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock);
					String outcome = temp.getKey();
					curBlock = temp.getValue();
					BasicBlock nb = new BasicBlock();
					nb.ofFunc = curBlock.ofFunc;
					
					JumpIns ains = new JumpIns();
					ains.insName = "br";
					ains.cond = outcome;
					ains.ifFalse = nb.blockID;
					ains.ifTrue = trueBlock.blockID;
					curBlock.insList.add(ains);
					
					curBlock.ifTrue = trueBlock;
					curBlock.ifFalse = nb;
					
					return pass(((BinaryNode)now).rightExprNode, curScope, nb, false, true, recHead, trueBlock, falseBlock);
				}
				
				/*
				BitIns ins = new BitIns();
				if(((BinaryNode)now).op.equals("&&"))
					ins.insName = "and";
				if(((BinaryNode)now).op.equals("||"))
					ins.insName = "or";
				ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
				ins.src1 = pass(((BinaryNode)now).leftExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
				ins.src2 = pass(((BinaryNode)now).rightExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
				curBlock.insList.add(ins);
				return new Pair<>(ins.dest, curBlock);
				*/
			}
		}
		
		else if(now instanceof AssignNode)
		{
			if(((AssignNode)now).leftExprNode instanceof IdNode
					&& !(((IdNode)((AssignNode)now).leftExprNode).ofScope instanceof ClassScope)
					&& (((AssignNode)now).leftExprNode.ofType.equals("int")
							|| ((AssignNode)now).leftExprNode.ofType.equals("bool")))
			{
				MovIns ins = new MovIns();
				ins.insName = "move";
				ins.dest = pass(((AssignNode)now).leftExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock).getKey();
				Pair<String, BasicBlock> temp = pass(((AssignNode)now).rightExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock);
				ins.src = temp.getKey();
				temp.getValue().insList.add(ins);
				return new Pair<>(null, temp.getValue());
			}
			else
			{
				MemAccIns ins = new MemAccIns();
				ins.insName = "store";
				ins.addr = pass(((AssignNode)now).leftExprNode, curScope, curBlock, true, true, recHead, trueBlock, falseBlock).getKey();
				Pair<String, BasicBlock> temp = pass(((AssignNode)now).rightExprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock);
				ins.src = temp.getKey();
				ins.size = bytes.get("addr") + "";
				ins.offset = 0;
				temp.getValue().insList.add(ins);
				return new Pair<>(null, temp.getValue());
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
				
				ArithIns ins = new ArithIns();
				ins.insName = "add";
				ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
				ins.src1 = "$this_" + ((IdNode)now).ofScope.scopeID;
				ins.src2 = vtemp.offset + "";
				curBlock.insList.add(ins);
				
				if(wantAddr)
				{
					return new Pair<>(ins.dest, curBlock);
				}
				
				MemAccIns ins2 = new MemAccIns();
				ins2.insName = "load";
				ins2.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
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
				temp = "$" + ((IdNode)now).id + "_" + ((IdNode)now).ofScope.scopeID;
			return new Pair<>(temp, curBlock);
		}
		
		else if(now instanceof ConstNode)
		{
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
				
				MemAccIns ins = new MemAccIns();
				ins.insName = "alloc";
				ins.dest = "$_t" + (tempRegNum++) + "_" + curScope.scopeID;
				ins.size = bytes.get("addr") + len + "";
				curBlock.insList.add(ins);
				
				MemAccIns ins2 = new MemAccIns();
				ins2.insName = "store";
				ins2.size = bytes.get("addr") + "";
				ins2.addr = ins.dest;
				ins2.src = len - 1 + "";
				ins2.offset = 0;
				curBlock.insList.add(ins2);
				
				for(int i = 0; i < len; i++)
				{
					MemAccIns ins3 = new MemAccIns();
					ins3.insName = "store";
					ins3.size = bytes.get("char") + "";
					ins3.addr = ins.dest;
					ins3.src = (int)(text.charAt(i)) + "";
					ins3.offset = bytes.get("addr") + i;
					curBlock.insList.add(ins3);
				}
				return new Pair<>(ins.dest, curBlock);
			}
		}
		
		else if(now instanceof SubExprNode)
		{
			return pass(((SubExprNode)now).exprNode, curScope, curBlock, false, true, recHead, trueBlock, falseBlock);
		}
		
		return null;
	}
}
