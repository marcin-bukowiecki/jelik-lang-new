package org.jelik.parser.ast.arrays

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.expression.ExpressionWithType
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.LeftBracketToken
import org.jelik.parser.token.RightBracketToken

/**
 * Represents [expr, expr, expr] or
 *
 * @author Marcin Bukowiecki
 */
open class ArrayCreateExpr(protected val leftBracket: LeftBracketToken,
                           val expressions: MutableList<Expression>,
                           protected val rightBracket: RightBracketToken) : ExpressionWithType() {

    init {
        expressions.forEach { expr -> expr.parent = this }
    }

    override fun replaceWith(oldNode: Expression, newNode: Expression) {
        var pos = 0;
        for (expression in expressions) {
            if (oldNode == expression) {
                break
            }
            pos++
        }
        expressions[pos] = newNode
    }

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitArrayCreateExpr(this, compilationContext)
    }

    override fun getStartCol(): Int {
        return leftBracket.col
    }

    override fun getEndCol(): Int {
        return rightBracket.col
    }

    override fun getStartRow(): Int {
        return leftBracket.row
    }

    override fun getEndRow(): Int {
        return rightBracket.row
    }

    override fun toString(): String {
        return leftBracket.toString() +
                expressions.joinToString(separator = ",", transform = { expr -> expr.toString() }) +
                rightBracket.toString()
    }
}
