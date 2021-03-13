package org.jelik.parser.ast.operators

import org.jelik.CompilationContext
import org.jelik.parser.ast.Expression
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.operators.AbstractOperator

/**
 * @author Marcin Bukowiecki
 */
class BitAndExpr(left: Expression, abstractOperator: AbstractOperator, right: Expression) : AbstractOpExpr(left, abstractOperator, right) {

    override fun visit(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
    }
}
