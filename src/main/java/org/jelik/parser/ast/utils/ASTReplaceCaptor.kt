package org.jelik.parser.ast.utils

import org.jelik.parser.ast.ASTNode
import org.jelik.parser.ast.expression.EmptyExpression
import org.jelik.parser.ast.expression.Expression
import org.jelik.types.Type

/**
 * @author Marcin Bukowiecki
 */
class ASTReplaceCaptor(private val subject: Expression) : EmptyExpression() {

    var captured: Expression? = null

    override fun replaceWith(oldNode: Expression, newNode: Expression) {
        this.captured = newNode
    }

    override fun getParent(): ASTNode {
        return this
    }

    override fun getGenericReturnType(): Type {
        return subject.genericReturnType
    }

    override fun getReturnType(): Type {
        return subject.returnType
    }
}
