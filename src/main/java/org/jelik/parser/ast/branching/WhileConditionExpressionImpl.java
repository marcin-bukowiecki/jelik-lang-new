package org.jelik.parser.ast.branching;

import org.jelik.parser.ast.expression.Expression;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class WhileConditionExpressionImpl extends IfConditionExpressionImpl implements WhileConditionExpression {

    public WhileConditionExpressionImpl(@NotNull Expression expression) {
        super(expression);
    }
}
