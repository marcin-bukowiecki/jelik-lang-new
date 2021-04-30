package org.jelik.parser.ast.branching

import org.jelik.parser.ast.expression.Expression

/**
 * @author Marcin Bukowiecki
 */
interface IfExpression : ConditionExpression<IfNodeContext>, Expression {

    fun endsWithReturnStmt(): Boolean

    fun isLast(): Boolean
}
