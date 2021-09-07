package org.jelik.parser.ast.branching;

import org.jelik.parser.ast.expression.Expression;
import org.jetbrains.annotations.NotNull;

/**
 * @author Marcin Bukowiecki
 */
public class WhileConditionExpressionWrapperImpl extends IfConditionExpressionWrapperImpl implements WhileConditionExpression {

    public WhileConditionExpressionWrapperImpl(@NotNull Expression expression) {
        super(expression);
    }
}
