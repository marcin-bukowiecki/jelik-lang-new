package org.jelik.parser.ast.branching;

import org.jelik.parser.ast.expression.Expression;

/**
 * @author Marcin Bukowiecki
 */
public interface IfConditionExpression extends Expression {

    Expression getExpression();
}
