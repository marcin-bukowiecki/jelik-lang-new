package org.jelik.parser.ast.operators

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.token.operators.NotEqualOperator

/**
 * @author Marcin Bukowiecki
 */
class NotEqualExpr(left: Expression, op: NotEqualOperator, right: Expression) : AbstractLogicalOpExpr(left, op, right) {

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
