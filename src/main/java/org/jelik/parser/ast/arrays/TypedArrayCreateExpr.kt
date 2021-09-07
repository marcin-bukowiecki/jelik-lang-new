package org.jelik.parser.ast.arrays

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.types.TypeNode
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.LeftBracketToken
import org.jelik.parser.token.RightBracketToken

/**
 * Represents [expr, expr, expr]Type
 *
 * @author Marcin Bukowiecki
 */
class TypedArrayCreateExpr(leftBracket: LeftBracketToken,
                           expressions: ArrayList<Expression>,
                           rightBracket: RightBracketToken,
                           val typeNode: TypeNode) : ArrayCreateExpr(leftBracket, expressions, rightBracket) {

    init {
        typeNode.parent = this
    }

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitTypedArrayCreateExpr(this, compilationContext)
    }
}
