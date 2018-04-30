import AbstractSyntaxTree.Nodes.*;
import MxGrammar.MxLexer;
import MxGrammar.MxParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.*;
import AbstractSyntaxTree.ASTMaker;
import AbstractSyntaxTree.ASTPrinter;


public class Compiler {
	public static void main(String[] args) throws IOException
	{
		//try {
			InputStream is = new FileInputStream("C:\\Users\\qydyx\\Desktop\\test1.mx"); // or System.in;
			ANTLRInputStream input = new ANTLRInputStream(is);
			MxLexer lexer = new MxLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);

			MxParser parser = new MxParser(tokens);
			//parser.removeErrorListeners();
			//parser.addErrorListener(new ParseErrorListener());
			ParseTree tree = parser.prog(); // calc is the starting rule

			ASTMaker maker = new ASTMaker();
			ASTNode rootNode = maker.visit(tree);


			ASTPrinter printer = new ASTPrinter();
			printer.print(rootNode);

			/*
			System.out.println("Visitor:");
			EvalVisitor evalByVisitor = new EvalVisitor();
			evalByVisitor.visit(tree);
			System.out.println();
			*/

			/*
			System.out.println("Listener:");
			ParseTreeWalker walker = new ParseTreeWalker();
			Evaluator evalByListener = new Evaluator();
			walker.walk(evalByListener, tree);
			*/
		//} catch (Exception ex) {
		//	System.exit(1);
		//}
	}
}
