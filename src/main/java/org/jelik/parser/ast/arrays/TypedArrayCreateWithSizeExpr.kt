package org.jelik.parser.ast.arrays

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.types.TypeNode
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.LeftBracketToken
import org.jelik.parser.token.RightBracketToken

/**
 * Represents []Type(5) or []Type(expr)
 *
 * @author Marcin Bukowiecki
 */
class TypedArrayCreateWithSizeExpr(leftBracket: LeftBracketToken,
                                   rightBracket: RightBracketToken,
                                   val typeNode: TypeNode,
                                   var expression: Expression
                                   ): ArrayCreateExpr(leftBracket, mutableListOf(), rightBracket) {

    init {
        typeNode.parent = this
        expression.parent = this
    }

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitTypedArrayCreateWithSizeExpr(this, compilationContext)
    }

    override fun replaceWith(oldNode: Expression, newNode: Expression) {
        expression = newNode
        newNode.parent = this
    }

    override fun toString(): String {
        return "$leftBracket$rightBracket$typeNode($expression)"
    }
}
