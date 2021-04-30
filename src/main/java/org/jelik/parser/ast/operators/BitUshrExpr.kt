package org.jelik.parser.ast.operators

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.operators.AbstractOperator

/**
 * @author Marcin Bukowiecki
 */
class BitUshrExpr(left: Expression, abstractOperator: AbstractOperator, right: Expression)
    : AbstractOpExpr(left, abstractOperator, right) {

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitBitUshrExpr(this, compilationContext)
    }
}
