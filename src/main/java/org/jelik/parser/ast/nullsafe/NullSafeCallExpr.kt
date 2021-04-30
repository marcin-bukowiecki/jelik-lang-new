package org.jelik.parser.ast.nullsafe

import org.jelik.parser.ast.ReferenceExpression
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.labels.LabelNode

/**
 * Wraps expression with safe call ?.
 *
 * @author Marcin Bukowiecki
 */
interface NullSafeCallExpr : ReferenceExpression {

    /**
     * Target jump if got null
     */
    var endLabel: LabelNode?

    /**
     * Target jump for last expression
     */
    var finishLabel: LabelNode?

    fun extractNullSubject(): Expression?

    fun isLast(): Boolean {
        return parent !is NullSafeCallExpr
    }
}
