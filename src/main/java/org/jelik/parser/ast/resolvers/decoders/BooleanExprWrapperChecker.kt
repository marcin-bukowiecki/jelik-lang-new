package org.jelik.parser.ast.resolvers.decoders

import org.jelik.parser.ast.branching.IfConditionExpression
import org.jelik.parser.ast.common.DupNodeImpl
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.labels.LabelNode
import org.jelik.parser.ast.numbers.FalseNode
import org.jelik.parser.ast.numbers.TrueNode
import org.jelik.parser.ast.operators.AbstractLogicalOpExpr
import org.jelik.parser.ast.operators.BooleanExprWrapper
import org.jelik.parser.ast.operators.JumpInstruction
import org.jelik.parser.ast.operators.NotExpr
import org.jelik.parser.ast.operators.NullSafeCheckExprWrapper
import org.jelik.parser.token.keyword.TrueToken
import org.jelik.parser.token.operators.EqualOperator

/**
 * @author Marcin Bukowiecki
 */
object BooleanExprWrapperChecker {

    fun wrapWithIsNullCheck(caller: Expression, expression: Expression, endLabelNode: LabelNode): Expression {
        val newExpression = expression.parent.replaceWithAndReturn(expression, DupNodeImpl(expression))
        val be = NullSafeCheckExprWrapper(
            newExpression,
            EqualOperator.create(),
            endLabelNode
        )
        be.instructionToCall = JumpInstruction.isNull
        caller.replaceWith(newExpression, be)
        return be
    }

    fun checkForWrapping(expr: NotExpr): BooleanExprWrapper {
        val be = BooleanExprWrapper(expr.right, EqualOperator.create(), FalseNode(true))
        be.instructionToCall = JumpInstruction.isFalse
        expr.parent.replaceWith(expr, be)
        return be
    }

    fun checkForWrapping(expr: AbstractLogicalOpExpr) {
        if (expr.left !is AbstractLogicalOpExpr) {
            val be = BooleanExprWrapper(expr.left, EqualOperator.create(), TrueNode(true))
            be.instructionToCall = JumpInstruction.isTrue
            expr.replaceWith(expr.left, be)
        }
        if (expr.right !is AbstractLogicalOpExpr) {
            val be = BooleanExprWrapper(expr.right, EqualOperator.create(), TrueNode(true))
            be.instructionToCall = JumpInstruction.isTrue
            expr.replaceWith(expr.right, be)
        }
    }

    fun checkConditionForBooleanExpression(conditionExpression: IfConditionExpression) {
        val furtherExpression = conditionExpression.expression
        if (furtherExpression !is AbstractLogicalOpExpr) {
            val trueNode = TrueNode(TrueToken(-1))
            trueNode.isIgnore = true
            val newExpr = BooleanExprWrapper(furtherExpression, EqualOperator(-1), trueNode)
            conditionExpression.replaceWith(furtherExpression, newExpr)
            newExpr.instructionToCall = JumpInstruction.isTrue
        }
    }

    fun checkForBooleanExpression(conditionExpression: Expression) {
        if (conditionExpression !is AbstractLogicalOpExpr) {
            val trueNode = TrueNode(TrueToken(-1))
            trueNode.isIgnore = true
            val newExpr = BooleanExprWrapper(conditionExpression, EqualOperator(-1), trueNode)
            conditionExpression.parent.replaceWith(conditionExpression, newExpr)
            newExpr.instructionToCall = JumpInstruction.isTrue
        }
    }
}
