package org.jelik.parser.ast.loops

import org.jelik.parser.ast.blocks.BasicBlockImpl
import org.jelik.parser.ast.branching.WhileConditionExpression
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.labels.LabelNode

/**
 * @author Marcin Bukowiecki
 */
interface WhileLoop : Expression {
    var condition: WhileConditionExpression
    var block: BasicBlockImpl
    var loopStart: LabelNode
    var loopEnd: LabelNode
}
