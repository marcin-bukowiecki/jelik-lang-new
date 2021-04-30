package org.jelik.parser.ast;

import org.jelik.parser.ast.expression.Expression;

/**
 * @author Marcin Bukowiecki
 */
public interface ReferenceExpression extends Expression {

    Expression getReference();

    Expression getFurtherExpression();
}
