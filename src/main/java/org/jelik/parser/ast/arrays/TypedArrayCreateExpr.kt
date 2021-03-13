package org.jelik.parser.ast.arrays

import org.jelik.CompilationContext
import org.jelik.parser.ast.Expression
import org.jelik.parser.ast.types.TypeNode
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.LeftBracketToken
import org.jelik.parser.token.RightBracketToken

/**
 * Represents [expr, expr, expr]Type or
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

    override fun visit(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
    }
}
