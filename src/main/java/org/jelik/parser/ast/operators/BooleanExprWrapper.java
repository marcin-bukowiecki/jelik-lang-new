package org.jelik.parser.ast.operators;

import org.jelik.parser.ast.ConsumingExpression;
import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.token.operators.AbstractOperator;

/**
 * Wraps expression with == True i.e foo.equals(bar) in if condition will be translated into foo.equals(bar) == True
 *
 * @author Marcin Bukowiecki
 */
public class BooleanExprWrapper extends EqualExpr implements ConsumingExpression {

    public BooleanExprWrapper(Expression left, AbstractOperator abstractOperator, Expression right) {
        super(left, abstractOperator, right);
    }

    @Override
    public int getStartOffset() {
        return left.getStartOffset();
    }

    @Override
    public int getEndOffset() {
        return left.getEndOffset();
    }

    @Override
    public String toString() {
        return left.toString() + op.toString() + right.toString();
    }
}
