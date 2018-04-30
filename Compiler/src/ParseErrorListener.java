import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ParseErrorListener extends BaseErrorListener {

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer,
							Object offendingSymbol,
							int line, int charPositionInLine,
							String msg,
							RecognitionException e
							)
	{
		List<String> stack = ((Parser)recognizer).getRuleInvocationStack();
		Collections.reverse(stack);
		System.err.println("Syntax Error @ Line "+line+", Column "+charPositionInLine+": "+msg);
		System.exit(1);
	}
}
