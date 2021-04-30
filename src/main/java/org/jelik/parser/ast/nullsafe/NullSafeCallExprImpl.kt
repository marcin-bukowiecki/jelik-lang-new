package org.jelik.parser.ast.nullsafe

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.ReferenceExpressionImpl
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.labels.LabelNode
import org.jelik.parser.ast.operators.BooleanExprWrapper
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
        return (reference as? BooleanExprWrapper)?.left
    }
}
