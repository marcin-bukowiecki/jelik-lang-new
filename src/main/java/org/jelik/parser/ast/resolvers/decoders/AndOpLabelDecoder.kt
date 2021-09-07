package org.jelik.parser.ast.resolvers.decoders

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.operators.AbstractLogicalOpExpr
import org.jelik.parser.ast.operators.AndExpr
import org.jelik.parser.ast.operators.OrExpr

/**
 * @author Marcin Bukowiecki
 */
object AndOpLabelDecoder {

    fun decode(andNode: AndExpr, ctx: CompilationContext) {
        val parent = andNode.parent

        if (parent !is AbstractLogicalOpExpr) {
            andNode.trueLabelNode = ctx.createLabel("true-label")
            andNode.falseLabelNode = ctx.createLabel("false-label")
        } else if (parent is OrExpr && andNode.isLeft) {
            andNode.trueLabelNode = parent.trueLabelNode
            andNode.falseLabelNode = parent.startLabel
        } else if (parent is OrExpr && andNode.isRight) {
            andNode.trueLabelNode = parent.trueLabelNode
            andNode.falseLabelNode = parent.falseLabelNode
        } else if (parent is AndExpr && parent.isLeft) {
            andNode.trueLabelNode = parent.trueLabelNode
            andNode.falseLabelNode = parent.falseLabelNode
        } else if (parent is AndExpr && parent.isRight) {
            andNode.trueLabelNode = parent.startLabel
            andNode.falseLabelNode = parent.falseLabelNode
        }

        if (andNode.left is OrExpr) {
            andNode.startLabel = ctx.createLabel("or")
        }
    }
}
