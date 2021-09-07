package org.jelik.parser.ast.nullsafe

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.ReferenceExpressionImpl
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.labels.LabelNode
import org.jelik.parser.ast.operators.NullSafeCheckExprWrapper
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.operators.NullSafeCallOperator

/**
 * Wraps expression with safe call ?.
 *
 * @author Marcin Bukowiecki
 */
class NullSafeCallExprImpl(
    expression: Expression,
    op: NullSafeCallOperator
) : ReferenceExpressionImpl(expression, op), NullSafeCallExpr {

    override var endLabel: LabelNode? = null

    override var finishLabel: LabelNode? = null

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitNullSafeCall(this, compilationContext)
    }

    override fun extractNullSubject(): Expression? {
        return (reference as? NullSafeCheckExprWrapper)?.left
    }
}
