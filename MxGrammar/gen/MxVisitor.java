// Generated from C:/Users/qydyx/Desktop/Compiler/MxGrammar\Mx.g4 by ANTLR 4.7
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MxParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MxVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MxParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(MxParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#progSec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgSec(MxParser.ProgSecContext ctx);
	/**
	 * Visit a parse tree produced by the {@code classDeclaration}
	 * labeled alternative in {@link MxParser#classDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDeclaration(MxParser.ClassDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#memDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemDecl(MxParser.MemDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constructorDeclaration}
	 * labeled alternative in {@link MxParser#ctrDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorDeclaration(MxParser.ConstructorDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionDeclaration}
	 * labeled alternative in {@link MxParser#funcDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDeclaration(MxParser.FunctionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#typeSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSpec(MxParser.TypeSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#singleTypeSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleTypeSpec(MxParser.SingleTypeSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#paramDeclList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamDeclList(MxParser.ParamDeclListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#paramList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamList(MxParser.ParamListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#paramDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamDecl(MxParser.ParamDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code variableDeclaration}
	 * labeled alternative in {@link MxParser#variDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(MxParser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#variInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariInit(MxParser.VariInitContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#blockStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStmt(MxParser.BlockStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#blockCtnt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockCtnt(MxParser.BlockCtntContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#exprStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprStmt(MxParser.ExprStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code blockStatement}
	 * labeled alternative in {@link MxParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(MxParser.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expressionStatement}
	 * labeled alternative in {@link MxParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionStatement(MxParser.ExpressionStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code selectionStatement}
	 * labeled alternative in {@link MxParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectionStatement(MxParser.SelectionStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code iterationStatement}
	 * labeled alternative in {@link MxParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIterationStatement(MxParser.IterationStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code jumpStatement}
	 * labeled alternative in {@link MxParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJumpStatement(MxParser.JumpStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#slctStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSlctStmt(MxParser.SlctStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code forInit}
	 * labeled alternative in {@link MxParser#iterStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInit(MxParser.ForInitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code for}
	 * labeled alternative in {@link MxParser#iterStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor(MxParser.ForContext ctx);
	/**
	 * Visit a parse tree produced by the {@code while}
	 * labeled alternative in {@link MxParser#iterStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile(MxParser.WhileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code break}
	 * labeled alternative in {@link MxParser#jumpStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreak(MxParser.BreakContext ctx);
	/**
	 * Visit a parse tree produced by the {@code continue}
	 * labeled alternative in {@link MxParser#jumpStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinue(MxParser.ContinueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code return}
	 * labeled alternative in {@link MxParser#jumpStmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn(MxParser.ReturnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code new}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNew(MxParser.NewContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifier}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(MxParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code memberAccess}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberAccess(MxParser.MemberAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code const}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConst(MxParser.ConstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code suffixIncrementDecrement}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuffixIncrementDecrement(MxParser.SuffixIncrementDecrementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code prefixIncrementDecrement}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixIncrementDecrement(MxParser.PrefixIncrementDecrementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binaryOperation}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryOperation(MxParser.BinaryOperationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code indexAccess}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexAccess(MxParser.IndexAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code not}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot(MxParser.NotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code positiveNegative}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositiveNegative(MxParser.PositiveNegativeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionCall}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(MxParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code subExpression}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubExpression(MxParser.SubExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assign}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign(MxParser.AssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code creatorError}
	 * labeled alternative in {@link MxParser#creator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatorError(MxParser.CreatorErrorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code creatorArray}
	 * labeled alternative in {@link MxParser#creator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatorArray(MxParser.CreatorArrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code creatorSingle}
	 * labeled alternative in {@link MxParser#creator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatorSingle(MxParser.CreatorSingleContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstant(MxParser.ConstantContext ctx);
}