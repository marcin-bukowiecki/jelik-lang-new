package org.jelik.parser.ast.types

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.LeftBracketToken
import org.jelik.parser.token.RightBracketToken

/**
 * Represents array type i.e. []Int
 *
 * @author Marcin Bukowiecki
 */
class ArrayTypeNode(private val leftBracket: LeftBracketToken,
                    private val rightBracket: RightBracketToken,
                    val innerType: TypeNode) : TypeNode() {

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
    }

    override fun getStartOffset(): Int {
        return leftBracket.startOffset
    }

    override fun getEndOffset(): Int {
        return rightBracket.endOffset
    }

    override fun toString(): String {
        return "" + leftBracket + rightBracket + innerType
    }

    override fun getSymbol(): String {
        return "[]";
    }
}
