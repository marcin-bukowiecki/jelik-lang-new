package org.jelik.parser.ast.visitors;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.token.keyword.ReturnKeyword;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.operators.AddOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class FurtherExpressionVisitor extends ExpressionVisitor {

    public FurtherExpressionVisitor(Expression currentExpression, Token start) {
        super(start);
        this.expression = currentExpression;
    }

    @Override
    public void visitReturnKeyword(@NotNull ReturnKeyword returnKeyword, @NotNull ParseContext parseContext) {/*
        if (current.getRow() == returnKeyword.getRow()) {
            throw new SyntaxException("Expected new line before return expression", returnKeyword, parseContext.getCurrentFilePath());
        }*/
    }

    @Override
    public void visitAdd(@NotNull AddOperator addOperator, @NotNull ParseContext parseContext) {
        var rightHand = new RightHandExpressionVisitor(parseContext.getLexer().nextToken()).visit(parseContext);

    }
}
