package org.jelik.parser.ast

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.expression.TypedExpression
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.NullToken
import org.jelik.types.JVMNullType

/**
 * Represents Null reference expression
 *
 * @author Marcin Bukowiecki
 */
class NullExpr(val literal: NullToken) : TypedExpression() {

    var ignore: Boolean = false

    init {
        this.nodeContext.type = JVMNullType.INSTANCE
        this.nodeContext.genericType = JVMNullType.INSTANCE
    }

    constructor(ignore: Boolean): this(NullToken()) {
        this.ignore = ignore
    }

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
    }

    override fun getStartOffset(): Int {
        return literal.startOffset
    }

    override fun getEndOffset(): Int {
        return literal.endOffset
    }

    override fun toString(): String {
        return literal.text
    }
}
