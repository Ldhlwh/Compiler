import AbstractSyntaxTree.Nodes.*;
import IR.IRGenerator;
import MxGrammar.MxLexer;
import MxGrammar.MxParser;
import ScopeCheck.PreScopeChecker;
import ScopeCheck.ScopeChecker;
import ScopeCheck.ScopePrinter;
import ScopeCheck.Scopes.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.*;

import AbstractSyntaxTree.ASTMaker;
import AbstractSyntaxTree.ASTPrinter;


public class Compiler {
	public static void main(String[] args) throws IOException
	{
		//InputStream is = new FileInputStream("program.txt"); // or System.in;
		InputStream is = System.in;
		//InputStream is = new FileInputStream("C:\\Users\\qydyx\\Desktop\\t.txt"); // or System.in;
		
		ANTLRInputStream input = new ANTLRInputStream(is);
		MxLexer lexer = new MxLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		
		MxParser parser = new MxParser(tokens);
		lexer.removeErrorListeners();
		lexer.addErrorListener(new MyParseErrorListener());
		parser.removeErrorListeners();
		parser.addErrorListener(new MyParseErrorListener());
		ParseTree tree = parser.prog();
		
		
		ASTMaker maker = new ASTMaker();
		ASTNode rootNode = maker.visit(tree);

			/*
			ASTPrinter printer = new ASTPrinter();
			printer.print(rootNode);
			*/
		
		System.err.println("------PreScopeCheck------");
		PreScopeChecker preScopeChecker = new PreScopeChecker();
		Scope rootScope = preScopeChecker.check(rootNode, new EmptyScope());
		
		ScopePrinter scopePrinter = new ScopePrinter();
		/*
		System.err.println("------ScopePrint------");
		scopePrinter.print(rootScope);
		*/
		System.err.println("------ScopeCheck------");
		ScopeChecker scopeChecker = new ScopeChecker();
		scopeChecker.checkRoot(rootNode, rootScope);
		
		System.err.println("------ScopePrint------");
		scopePrinter = new ScopePrinter();
		scopePrinter.print(rootScope);
		
		//ASTPrinter printer = new ASTPrinter();
		//printer.print(rootNode);
		
		System.err.println("------IR Generation------");
		IRGenerator IR = new IRGenerator((ASTRootNode)rootNode);
		IR.passRoot();
		IR.print();
	}
}
