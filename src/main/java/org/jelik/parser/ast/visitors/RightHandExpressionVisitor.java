package org.jelik.parser.ast.visitors;

import org.jelik.compiler.exceptions.SyntaxException;
import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.keyword.ReturnKeyword;
import org.jelik.parser.token.operators.AddOperator;
import org.jelik.parser.token.operators.AndOperator;
import org.jelik.parser.token.operators.BitXorOperator;
import org.jelik.parser.token.operators.DivideOperator;
import org.jelik.parser.token.operators.GreaterOperator;
import org.jelik.parser.token.operators.MulOperator;
import org.jelik.parser.token.operators.OrOperator;
import org.jelik.parser.token.operators.RemOperator;
import org.jelik.parser.token.operators.SubtractOperator;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class RightHandExpressionVisitor extends ExpressionVisitor {

    public RightHandExpressionVisitor(Token start) {
        super(start);
    }

    @Override
    public @NotNull Expression visit(@NotNull ParseContext parseContext) {
        start.accept(this, parseContext);
        if (expression == null) {
            throw new SyntaxException("Unexpected token", start, parseContext.getCurrentFilePath());
        }
        return expression;
    }

    @Override
    public void visitAdd(@NotNull AddOperator addOperator, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitMul(@NotNull MulOperator op, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitDivide(@NotNull DivideOperator divideOperator, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitSubtract(@NotNull SubtractOperator subtractOperator, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitRem(@NotNull RemOperator remOperator, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitReturnKeyword(@NotNull ReturnKeyword returnKeyword, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitOr(@NotNull OrOperator orOperator, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitAnd(@NotNull AndOperator andOperator, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitGreater(@NotNull GreaterOperator greaterOperator, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visit(@NotNull BitXorOperator bitXorOperator, @NotNull ParseContext parseContext) {

    }
}
