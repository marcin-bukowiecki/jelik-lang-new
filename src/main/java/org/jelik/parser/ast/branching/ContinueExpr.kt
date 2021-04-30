package org.jelik.parser.ast.branching

import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.labels.LabelNode

/**
 * @author Marcin Bukowiecki
 */
interface ContinueExpr : Expression {

    fun setJumpTo(labelNode: LabelNode)

    fun getJumpTo(): LabelNode?
}
