package org.jelik.parser.ast

import org.jelik.CompilationContext
import org.jelik.parser.ast.expression.ExpressionWithType
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.LeftCurlToken
import org.jelik.parser.token.RightCurlToken

/**
 * @author Marcin Bukowiecki
 */
class MapCreateExpr(private val leftCurl: LeftCurlToken,
                    val entries: List<KeyValueExpr>,
                    private val rightCurl: RightCurlToken): ExpressionWithType() {

    init {
        entries.forEach { e -> e.parent = this }
    }

    override fun getStartCol(): Int {
        return leftCurl.col
    }

    override fun getEndCol(): Int {
        return rightCurl.endCol
    }

    override fun getStartRow(): Int {
        return leftCurl.row
    }

    override fun getEndRow(): Int {
        return rightCurl.endRow
    }

    override fun visit(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitMapCreateExpr(this, compilationContext)
    }

    override fun toString(): String {
        return leftCurl.toString() + entries.joinToString(",") + rightCurl.toString()
    }
}
