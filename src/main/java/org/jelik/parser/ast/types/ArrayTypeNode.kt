package org.jelik.parser.ast.types

import org.jelik.compiler.config.CompilationContext
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

    override fun getEndRow(): Int {
        return innerType.endRow
    }

    override fun toString(): String {
        return "" + leftBracket + rightBracket + innerType
    }

    override fun getSymbol(): String {
        return "[]";
    }
}
