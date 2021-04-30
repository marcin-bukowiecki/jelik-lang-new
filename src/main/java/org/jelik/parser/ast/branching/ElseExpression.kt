package org.jelik.parser.ast.branching

import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.labels.LabelNode

/**
 * @author Marcin Bukowiecki
 */
interface ElseExpression : Expression {

    fun getFinishLabel(): LabelNode?

    fun isLast(): Boolean
}
