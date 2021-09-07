package org.jelik.parser.ast

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.expression.TypedExpression
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.ColonToken

/**
 * @author Marcin Bukowiecki
 */
class KeyValueExpr(var key: Expression,
                   private val colon: ColonToken,
                   var value: Expression) : TypedExpression() {

    init {
        key.parent = this
        value.parent = this
    }

    override fun replaceWith(oldNode: Expression, newNode: Expression) {
        when (oldNode) {
            key -> {
                key = newNode
            }
            value -> {
                value = newNode
            }
            else -> {
                super.replaceWith(oldNode, newNode)
            }
        }
    }

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitKeyValueExpr(this, compilationContext)
    }

    override fun getStartOffset(): Int {
        return key.startOffset
    }

    override fun getEndOffset(): Int {
        return value.endOffset
    }

    override fun toString(): String {
        return key.toString() + colon.toString() + value.toString()
    }
}
