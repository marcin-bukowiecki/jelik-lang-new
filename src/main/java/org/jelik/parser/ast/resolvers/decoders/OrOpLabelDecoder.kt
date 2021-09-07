package org.jelik.parser.ast.resolvers.decoders

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.operators.AbstractLogicalOpExpr
import org.jelik.parser.ast.operators.AndExpr
import org.jelik.parser.ast.operators.OrExpr

/**
 * @author Marcin Bukowiecki
 */
object OrOpLabelDecoder {

    fun decode(orNode: OrExpr, ctx: CompilationContext) {
        val parent = orNode.parent

        if (parent !is AbstractLogicalOpExpr) {
            orNode.trueLabelNode = ctx.createLabel("true-label")
            orNode.falseLabelNode = ctx.createLabel("false-label")
        } else if (parent is OrExpr && orNode.isLeft) {
            orNode.trueLabelNode = parent.trueLabelNode
            orNode.falseLabelNode = parent.startLabel
        } else if (parent is OrExpr && orNode.isRight) {
            orNode.trueLabelNode = parent.trueLabelNode
            orNode.falseLabelNode = parent.falseLabelNode
        } else if (parent is AndExpr && orNode.isLeft) {
            orNode.trueLabelNode = parent.trueLabelNode
            orNode.falseLabelNode = parent.falseLabelNode
        } else if (parent is AndExpr && orNode.isRight) {
            orNode.trueLabelNode = parent.trueLabelNode
            orNode.falseLabelNode = parent.falseLabelNode
        }

        if (orNode.left is AndExpr) {
            orNode.startLabel = ctx.createLabel("and")
        }
        if (orNode.left is OrExpr) {
            orNode.startLabel = ctx.createLabel("or")
        }
    }
}
