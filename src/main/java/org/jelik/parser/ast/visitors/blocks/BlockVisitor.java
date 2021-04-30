package org.jelik.parser.ast.visitors.blocks;

import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.TokenVisitor;
import org.jelik.parser.ast.blocks.BasicBlockImpl;
import org.jelik.parser.ast.branching.BreakExprImpl;
import org.jelik.parser.ast.branching.ContinueExprImpl;
import org.jelik.parser.ast.branching.IfExpressionImpl;
import org.jelik.parser.ast.expression.CatchExpressionImpl;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.expression.ThrowExpression;
import org.jelik.parser.ast.expression.TryExpressionImpl;
import org.jelik.parser.ast.functions.FunctionParameterList;
import org.jelik.parser.ast.functions.LambdaDeclarationExpression;
import org.jelik.parser.ast.visitors.ExpressionVisitor;
import org.jelik.parser.ast.visitors.ReturnExpressionVisitor;
import org.jelik.parser.ast.visitors.ValueVisitor;
import org.jelik.parser.ast.visitors.VariableVisitor;
import org.jelik.parser.ast.visitors.conditions.IfVisitor;
import org.jelik.parser.ast.visitors.functions.FunctionParameterListVisitor;
import org.jelik.parser.ast.visitors.functions.LambdaDeclarationVisitor;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.LeftParenthesisToken;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.BreakKeyword;
import org.jelik.parser.token.keyword.CatchKeyword;
import org.jelik.parser.token.keyword.ContinueKeyword;
import org.jelik.parser.token.keyword.FalseToken;
import org.jelik.parser.token.keyword.ForKeyword;
import org.jelik.parser.token.keyword.IfKeyword;
import org.jelik.parser.token.keyword.LamKeyword;
import org.jelik.parser.token.keyword.ReturnKeyword;
import org.jelik.parser.token.keyword.ThrowKeyword;
import org.jelik.parser.token.keyword.TrueToken;
import org.jelik.parser.token.keyword.TryKeyword;
import org.jelik.parser.token.keyword.ValKeyword;
import org.jelik.parser.token.keyword.VarKeyword;
import org.jelik.parser.token.keyword.WhileKeyword;
import org.jelik.parser.token.operators.DecrOperator;
import org.jelik.parser.token.operators.IncrOperator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class BlockVisitor implements TokenVisitor<BasicBlockImpl> {

    protected final Token start;

    protected final List<Expression> expressionList = new ArrayList<>();

    public BlockVisitor(@NotNull Token start) {
        this.start = start;
    }

    @Override
    public @NotNull BasicBlockImpl visit(@NotNull ParseContext parseContext) {
        final Lexer lexer = parseContext.getLexer();

        while (lexer.hasNextToken() &&
                lexer.peekNext().getTokenType() != ElementType.rightCurl &&
                lexer.peekNext().getTokenType() != ElementType.endKeyword) {

            Token nextToken = lexer.nextToken();
            nextToken.accept(this, parseContext);
        }

        var next = lexer.nextToken();

        return new BasicBlockImpl(expressionList);
    }

    @Override
    public void visitTrue(@NotNull TrueToken trueToken, @NotNull ParseContext parseContext) {
        expressionList.add(new ExpressionVisitor(trueToken).visit(parseContext));
    }

    @Override
    public void visitFalse(@NotNull FalseToken falseToken, @NotNull ParseContext parseContext) {
        expressionList.add(new ExpressionVisitor(falseToken).visit(parseContext));
    }

    @Override
    public void visitLamKeyword(@NotNull LamKeyword lamKeyword, @NotNull ParseContext parseContext) {
        expressionList.add(new LambdaDeclarationExpression(new LambdaDeclarationVisitor(lamKeyword)
                .visit(parseContext)));
    }

    @Override
    public void visitLiteral(@NotNull LiteralToken literalToken, @NotNull ParseContext parseContext) {
        expressionList.add(new ExpressionVisitor(literalToken).visit(parseContext));
    }

    @Override
    public void visitValKeyword(@NotNull ValKeyword valKeyword, @NotNull ParseContext parseContext) {
        var visit = new ValueVisitor(valKeyword).visit(parseContext);
        expressionList.add(visit);
    }

    @Override
    public void visitVarKeyword(@NotNull VarKeyword varKeyword, @NotNull ParseContext parseContext) {
        var visit = new VariableVisitor(varKeyword).visit(parseContext);
        expressionList.add(visit);
    }

    @Override
    public void visitReturnKeyword(@NotNull ReturnKeyword returnKeyword, @NotNull ParseContext parseContext) {
        Expression visit = new ReturnExpressionVisitor(returnKeyword).visit(parseContext);
        expressionList.add(visit);
    }

    @Override
    public void visit(IncrOperator incrOperator, ParseContext parseContext) {
        var visit = new ExpressionVisitor(incrOperator).visit(parseContext);
        expressionList.add(visit);
    }

    @Override
    public void visit(DecrOperator decrOperator, ParseContext parseContext) {
        var visit = new ExpressionVisitor(decrOperator).visit(parseContext);
        expressionList.add(visit);
    }

    @Override
    public void visitForKeyword(@NotNull ForKeyword forKeyword, @NotNull ParseContext parseContext) {
        var visit = new ExpressionVisitor(forKeyword).visit(parseContext);
        expressionList.add(visit);
    }

    @Override
    public void visitWhileKeyword(@NotNull WhileKeyword whileKeyword, @NotNull ParseContext parseContext) {
        var visit = new ExpressionVisitor(whileKeyword).visit(parseContext);
        expressionList.add(visit);
    }

    @Override
    public void visitIfKeyword(@NotNull IfKeyword ifKeyword, @NotNull ParseContext parseContext) {
        IfExpressionImpl visit = new IfVisitor(ifKeyword).visit(parseContext);
        expressionList.add(visit);
    }

    @Override
    public void visit(@NotNull ThrowKeyword throwKeyword, @NotNull ParseContext parseContext) {
        expressionList.add(new ThrowExpression(throwKeyword, new ExpressionVisitor(parseContext.getLexer().nextToken()).visit(parseContext)));
    }

    @Override
    public void visitBreakKeyword(@NotNull BreakKeyword breakKeyword, @NotNull ParseContext parseContext) {
        expressionList.add(new BreakExprImpl(breakKeyword));
    }

    @Override
    public void visitContinueKeyword(@NotNull ContinueKeyword continueKeyword, @NotNull ParseContext parseContext) {
        expressionList.add(new ContinueExprImpl(continueKeyword));
    }

    @Override
    public void visit(@NotNull TryKeyword tryKeyword, @NotNull ParseContext parseContext) {
        Lexer lexer = parseContext.getLexer();
        BasicBlockImpl tryBlock = new BlockVisitor(lexer.nextToken()).visit(parseContext);
        Token nextToken = lexer.nextToken();

        if (nextToken.getTokenType() != ElementType.catchKeyword) {
            throw new SyntaxException("Expected catch keyword", nextToken, parseContext.getCurrentFilePath());
        }

        CatchKeyword catchKeyword = ((CatchKeyword) nextToken);

        nextToken = lexer.nextToken();
        FunctionParameterList args;
        if (nextToken.getTokenType() == ElementType.leftParenthesis) {
            args = new FunctionParameterListVisitor(((LeftParenthesisToken) nextToken)).visit(parseContext);
        } else {
            args = FunctionParameterList.createEmpty();
        }

        nextToken = lexer.nextToken();
        BasicBlockImpl catchBlock = new BlockVisitor(nextToken).visit(parseContext);
        if (catchBlock.getExpressions().isEmpty()) {
            throw new SyntaxException("Empty catch block", catchKeyword, parseContext.getCurrentFilePath());
        }
        nextToken = lexer.getCurrent();

        TryExpressionImpl tryExpression = new TryExpressionImpl(tryKeyword, tryBlock,
                new CatchExpressionImpl(catchKeyword, args, catchBlock));
        expressionList.add(tryExpression);
    }
}
