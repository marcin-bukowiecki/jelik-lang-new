package org.jelik.parser.ast.visitors;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.Expression;
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
        start.visit(this, parseContext);
        return expression;
    }

    @Override
    public void visitAdd(@NotNull AddOperator addOperator, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitMul(MulOperator op, ParseContext parseContext) {

    }

    @Override
    public void visitDivide(DivideOperator divideOperator, ParseContext parseContext) {

    }

    @Override
    public void visitSubtract(SubtractOperator subtractOperator, ParseContext parseContext) {

    }

    @Override
    public void visitRem(@NotNull RemOperator remOperator, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visitReturnKeyword(@NotNull ReturnKeyword returnKeyword, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visit(@NotNull OrOperator orOperator, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visit(@NotNull AndOperator andOperator, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visit(@NotNull GreaterOperator greaterOperator, @NotNull ParseContext parseContext) {

    }

    @Override
    public void visit(@NotNull BitXorOperator bitXorOperator, @NotNull ParseContext parseContext) {

    }
}
