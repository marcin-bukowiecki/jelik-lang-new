package org.jelik.parser.ast.branching

import org.jelik.parser.ast.labels.LabelNode

/**
 * @author Marcin Bukowiecki
 */
interface ConditionExpression<T : ElseNodeContext> {

    fun getFinishLabel(): LabelNode?

    fun getContext(): T

    fun setElseExpression(elseExpression: ElseExpression)
}
