package org.jelik.parser.ast

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.expression.ExpressionWithType
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.ColonToken

/**
 * @author Marcin Bukowiecki
 */
class KeyValueExpr(var key: Expression,
                   private val colon: ColonToken,
                   var value: Expression) : ExpressionWithType() {

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

    override fun getStartCol(): Int {
        return key.startCol
    }

    override fun getEndCol(): Int {
        return value.endCol
    }

    override fun getStartRow(): Int {
        return key.startRow
    }

    override fun getEndRow(): Int {
        return value.endRow
    }

    override fun toString(): String {
        return key.toString() + colon.toString() + value.toString()
    }
}
