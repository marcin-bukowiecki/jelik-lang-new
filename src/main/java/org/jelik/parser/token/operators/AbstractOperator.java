package org.jelik.parser.token.operators;

import org.jelik.parser.ast.expression.Expression;
import org.jelik.parser.ast.helpers.OperatorStack;
import org.jelik.parser.ast.operators.AbstractOpExpr;
import org.jelik.parser.token.Token;
import org.jelik.parser.token.ElementType;

/**
 * @author Marcin Bukowiecki
 */
public abstract class AbstractOperator extends Token {

    public AbstractOperator(String text, int offset, ElementType elementType) {
        super(text, offset, elementType);
    }

    @Override
    public boolean isOperator() {
        return true;
    }

    @Override
    public String toString() {
        return text;
    }

    public boolean isHigherPrecedence(AbstractOperator than) {
        return OperatorStack.isHigherPrecedence(this, than);
    }

    public abstract AbstractOpExpr toAst(Expression left, Expression right);
}
