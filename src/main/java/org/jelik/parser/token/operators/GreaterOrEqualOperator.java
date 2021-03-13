package org.jelik.parser.token.operators;

import org.jelik.parser.ParseContext;
import org.jelik.parser.ast.Expression;
import org.jelik.parser.ast.ParseVisitor;
import org.jelik.parser.ast.operators.AbstractOpExpr;
import org.jelik.parser.ast.operators.GreaterExpr;
import org.jelik.parser.ast.operators.GreaterOrEqualExpr;
import org.jelik.parser.token.ElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Token for '>='
 *
 * @author Marcin Bukowiecki
 */
public class GreaterOrEqualOperator extends AbstractOperator {

    public GreaterOrEqualOperator(int row, int col) {
        super(">=", row, col, ElementType.greaterOrEqualOperator);
    }

    @Override
    public void visit(@NotNull ParseVisitor<?> parseVisitor, @NotNull ParseContext parseContext) {
        parseVisitor.visit(this, parseContext);
    }

    @Override
    public AbstractOpExpr toAst(Expression left, Expression right) {
        return new GreaterOrEqualExpr(left, this, right);
    }
}