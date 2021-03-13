package org.jelik.parser.ast.operators

import org.jelik.CompilationContext
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.ast.Expression
import org.jelik.parser.token.operators.NotEqualOperator

/**
 * @author Marcin Bukowiecki
 */
class NotEqualExpr(left: Expression, op: NotEqualOperator, right: Expression) : AbstractLogicalOpExpr(left, op, right) {

    override fun visit(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext);
    }
}
