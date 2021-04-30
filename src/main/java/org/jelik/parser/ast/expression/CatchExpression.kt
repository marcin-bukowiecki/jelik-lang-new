package org.jelik.parser.ast.expression

import org.jelik.parser.ast.blocks.BasicBlockImpl
import org.jelik.parser.ast.functions.FunctionParameterList
import org.jelik.parser.ast.labels.LabelNode

/**
 * @author Marcin Bukowiecki
 */
interface CatchExpression : Expression {
    var endLabel: LabelNode?
    var innerLabel: LabelNode?
    var startLabel: LabelNode?

    fun getBlock(): BasicBlockImpl
    fun getArgs(): FunctionParameterList
}
