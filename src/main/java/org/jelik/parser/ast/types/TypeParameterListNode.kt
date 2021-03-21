package org.jelik.parser.ast.types

import org.jelik.CompilationContext
import org.jelik.parser.ast.ASTNode
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.CommaToken
import org.jelik.parser.token.Token
import org.jelik.parser.token.operators.GreaterOperator
import org.jelik.parser.token.operators.LesserOperator

/**
 * @author Marcin Bukowiecki
 */
class TypeParameterListNode(val left: LesserOperator,
                            val types: List<TypeNode>,
                            val commas: List<CommaToken>,
                            val right: GreaterOperator) : ASTNode() {

    companion object {
        val EMPTY = TypeParameterListNode(LesserOperator(-1,-1), emptyList(), emptyList(), GreaterOperator(-1,-1))
    }

    override fun visit(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
    }

    override fun toString(): String {
        return left.toString() + types.joinToString(separator = ",") + right.toString()
    }
}
