package org.jelik.parser.ast.expression

import org.jelik.parser.ast.blocks.BasicBlockImpl
import org.jelik.parser.ast.labels.LabelNode

/**
 * @author Marcin Bukowiecki
 */
interface TryExpression : Expression {
    val catchExpression: CatchExpression
    val block: BasicBlockImpl
    var endLabel: LabelNode?
    var startLabel: LabelNode?
}
