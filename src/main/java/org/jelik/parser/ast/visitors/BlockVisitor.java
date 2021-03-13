package org.jelik.parser.ast.visitors;

import lombok.Getter;
import org.jelik.parser.Lexer;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.BasicBlock;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.ast.arguments.ArgumentList;
import org.jelik.parser.ast.arguments.ArgumentListVisitor;
import org.jelik.parser.ast.branching.IfExpression;
import org.jelik.parser.ast.expression.CatchExpression;
import org.jelik.parser.ast.expression.ThrowExpression;
import org.jelik.parser.ast.expression.TryExpression;
import org.jelik.parser.ast.functions.FunctionParameterList;
import org.jelik.parser.exceptions.SyntaxException;
import org.jelik.parser.token.ElementType;
import org.jelik.parser.token.LeftParenthesisToken;
import org.jelik.parser.token.LiteralToken;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.CatchKeyword;
import org.jelik.parser.token.keyword.IfKeyword;
import org.jelik.parser.token.keyword.ReturnKeyword;
import org.jelik.parser.token.keyword.ThrowKeyword;
import org.jelik.parser.token.keyword.TryKeyword;
import org.jelik.parser.token.keyword.ValKeyword;
import org.jelik.parser.token.keyword.VarKeyword;
import org.jelik.parser.token.operators.DecrOperator;
import org.jelik.parser.token.operators.IncrOperator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcin Bukowiecki
 */
public class BlockVisitor implements ParseVisitor<BasicBlock> {

    @Getter
    protected final List<Expression> expressionList = new ArrayList<>();

    public BlockVisitor() {
    }

    @Override
    public @NotNull BasicBlock visit(@NotNull ParseContext parseContext) {
        Lexer lexer = parseContext.getLexer();

        while (lexer.hasNextToken() && lexer.peekNext().getTokenType() != ElementType.rightCurl) {
            Token nextToken = lexer.nextToken();
            nextToken.visit(this, parseContext);
        }

        lexer.nextToken();

        return new BasicBlock(expressionList);
    }

    @Override
    public void visitLiteral(@NotNull LiteralToken literalToken, @NotNull ParseContext parseContext) {
        expressionList.add(new ExpressionVisitor(literalToken).visit(parseContext));
    }

    @Override
    public void visit(ValKeyword valKeyword, ParseContext parseContext) {
        var visit = new ValueVisitor(valKeyword).visit(parseContext);
        expressionList.add(visit);
    }

    @Override
    public void visit(VarKeyword varKeyword, ParseContext parseContext) {
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
    public void visit(IfKeyword ifKeyword, ParseContext parseContext) {
        IfExpression visit = new IfVisitor(ifKeyword).visit(parseContext);
        expressionList.add(visit);
    }

    @Override
    public void visit(@NotNull ThrowKeyword throwKeyword, @NotNull ParseContext parseContext) {
        expressionList.add(new ThrowExpression(throwKeyword, new ExpressionVisitor(parseContext.getLexer().nextToken()).visit(parseContext)));
    }

    @Override
    public void visit(@NotNull TryKeyword tryKeyword, @NotNull ParseContext parseContext) {
        Lexer lexer = parseContext.getLexer();
        lexer.nextToken();
        BasicBlock tryBlock = new BlockVisitor().visit(parseContext);
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
            args = FunctionParameterList.EMPTY;
        }

        nextToken = lexer.nextToken();
        BasicBlock catchBlock = new BlockVisitor().visit(parseContext);
        if (catchBlock.getExpressions().isEmpty()) {
            throw new SyntaxException("Empty try block", tryKeyword, parseContext.getCurrentFilePath());
        }
        nextToken = lexer.nextToken();

        TryExpression tryExpression = new TryExpression(tryKeyword, tryBlock);
        CatchExpression catchExpression = new CatchExpression(catchKeyword, args, catchBlock);
        tryExpression.setFurtherExpression(catchExpression);

        expressionList.add(tryExpression);
    }
}
