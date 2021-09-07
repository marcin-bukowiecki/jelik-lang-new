package org.jelik.parser.ast

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.expression.TypedExpression
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.LeftCurlToken
import org.jelik.parser.token.RightCurlToken

/**
 * @author Marcin Bukowiecki
 */
class MapCreateExpr(private val leftCurl: LeftCurlToken,
                    val entries: List<KeyValueExpr>,
                    private val rightCurl: RightCurlToken): TypedExpression() {

    init {
        entries.forEach { e -> e.parent = this }
    }

    override fun getStartOffset(): Int {
        return leftCurl.startOffset
    }

    override fun getEndOffset(): Int {
        return rightCurl.endOffset
    }

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitMapCreateExpr(this, compilationContext)
    }

    override fun toString(): String {
        return leftCurl.toString() + entries.joinToString(",") + rightCurl.toString()
    }
}
