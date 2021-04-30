package org.jelik.parser.ast.types

import com.google.common.annotations.VisibleForTesting
import org.jelik.compiler.config.CompilationContext
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
                LesserOperator(-1, -1),
                types,
                types.map { CommaToken(-1, -1) },
                GreaterOperator(-1, -1)
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

    override fun getEndCol(): Int {
        return right.endCol;
    }

    override fun getStartCol(): Int {
        return left.col
    }

    override fun getEndRow(): Int {
        return right.row
    }

    override fun getStartRow(): Int {
        return left.row
    }
}
