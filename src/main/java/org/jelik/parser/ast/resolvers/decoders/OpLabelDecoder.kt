package org.jelik.parser.ast.resolvers.decoders

import org.jelik.CompilationContext
import org.jelik.parser.ast.operators.AbstractLogicalOpExpr
import org.jelik.parser.ast.operators.AndExpr
import org.jelik.parser.ast.operators.EqualExpr
import org.jelik.parser.ast.operators.OrExpr

/**
 * @author Marcin Bukowiecki
 */
object OpLabelDecoder {

    fun decode(op: AbstractLogicalOpExpr, ctx: CompilationContext) {
        val parent = op.parent

        if (parent is EqualExpr) {
            op.trueLabelNode = ctx.createLabel("equal-true")
            op.falseLabelNode = ctx.createLabel("equal-false")
            return
        }
/*
        if (parent is WhileNode || parent is DoNode) {
            abstractBooleanExpression.setLabelIndexes(parent.getTrueLabel(), parent.getFalseLabel())
            return
        }

        if (parent is CaseLeftNode) {
            abstractBooleanExpression.setLabelIndexes(parent.getTrueLabel(), parent.getFalseLabel())
            return
        }

        if (parent is CaseLabelExpressionNode) {

            //is last
            if (parent.getParent().getNumberOfSubNodes() - 1 === parent.getIndex()) {
                abstractBooleanExpression.setLabelIndexes(parent.getTrueLabel(), parent.getFalseLabel())
            } else {
                abstractBooleanExpression.setLabelIndexes(parent.getTrueLabel(), -1)
            }
            return
        }

        if (parent !is BooleanExpressionNode || parent is NegateNode) {
            abstractBooleanExpression.setLabelIndexes(labelCounter.getAndIncrement(), labelCounter.getAndIncrement())
            return
        }
*/
        if (parent !is AbstractLogicalOpExpr) {
            op.trueLabelNode = ctx.createLabel("true-label")
            op.falseLabelNode = ctx.createLabel("false-label")
            return
        }

        if (parent is AndExpr && op.isRight && parent.isLeft && parent.parent is AbstractLogicalOpExpr) {
            op.trueLabelNode = parent.trueLabelNode
        } else if (parent is AndExpr && op.isRight && parent.isRight &&
                parent.parent is OrExpr && (parent.parent as OrExpr).isLeft &&
                parent.parent.parent is OrExpr) {
            op.trueLabelNode = parent.trueLabelNode
        } else if (parent is AndExpr) {
            op.falseLabelNode = parent.falseLabelNode
        } else if (parent is OrExpr && op.isLeft) {
            op.trueLabelNode = parent.trueLabelNode
        } else if (parent is OrExpr && op.isRight) {
            op.falseLabelNode = parent.falseLabelNode
        } else {
            throw UnsupportedOperationException(parent.toString())
        }
    }
}
