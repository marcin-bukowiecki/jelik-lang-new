package org.jelik.parser.ast

import org.jelik.CompilationContext
import org.jelik.parser.ast.expression.ExpressionWithType
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.NullToken
import org.jelik.types.JVMNullType

/**
 * Represents Null reference expression
 *
 * @author Marcin Bukowiecki
 */
class NullExpr(val literal: NullToken) : ExpressionWithType() {

    var ignore: Boolean = false

    init {
        this.nodeContext.type = JVMNullType.INSTANCE
        this.nodeContext.genericType = JVMNullType.INSTANCE
    }

    override fun visit(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
    }

    override fun getStartCol(): Int {
        return literal.col
    }

    override fun getStartRow(): Int {
        return literal.row
    }

    override fun getEndCol(): Int {
        return literal.col + literal.text.length
    }

    override fun getEndRow(): Int {
        return literal.row
    }

    override fun toString(): String {
        return literal.text
    }
}
