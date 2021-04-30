package org.jelik.parser.ast.operators

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.operators.AbstractOperator

/**
 * @author Marcin Bukowiecki
 */
class AndExpr(left: Expression,
              abstractOperator: AbstractOperator,
              right: Expression) : AbstractLogicalOpExpr(left, abstractOperator, right) {

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
    }
}
