package org.jelik.parser.ast.types

import com.google.common.annotations.VisibleForTesting
import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.ASTNodeImpl
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.CommaToken
import org.jelik.parser.token.operators.GreaterOperator
import org.jelik.parser.token.operators.LesserOperator

/**
 * @author Marcin Bukowiecki
 */
open class TypeParameterListNode(
    val left: LesserOperator,
    val types: List<TypeNode>,
    val commas: List<CommaToken>,
    val right: GreaterOperator
) : ASTNodeImpl() {

    @VisibleForTesting
    constructor(types: List<TypeNode>) :
            this(
                LesserOperator(-1),
                types,
                types.map { CommaToken(-1) },
                GreaterOperator(-1)
            ) {
    }

    companion object {
        val EMPTY = TypeVariableListNode(emptyList())
    }

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
    }

    override fun toString(): String {
        return left.toString() + types.joinToString(separator = ",") + right.toString()
    }

    override fun getStartOffset(): Int {
        return left.startOffset
    }

    override fun getEndOffset(): Int {
        return right.endOffset
    }
}
