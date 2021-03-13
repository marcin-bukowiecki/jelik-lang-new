package org.jelik.parser.ast.arrays

import org.jelik.CompilationContext
import org.jelik.parser.ast.Expression
import org.jelik.parser.ast.expression.ExpressionWithType
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.LeftBracketToken
import org.jelik.parser.token.RightBracketToken

/**
 * Represents [expr, expr, expr] or
 *
 * @author Marcin Bukowiecki
 */
open class ArrayCreateExpr(private val leftBracket: LeftBracketToken,
                      val expressions: ArrayList<Expression>,
                      private val rightBracket: RightBracketToken) : ExpressionWithType() {

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

    override fun visit(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
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
                rightBracket.toString() +
                furtherExpressionOpt.map { expr -> expr.toString() }.orElse("")
    }
}
