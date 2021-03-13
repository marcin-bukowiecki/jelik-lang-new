package org.jelik.parser.ast.expression

import org.jelik.CompilationContext
import org.jelik.parser.ast.BasicBlock
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.keyword.TryKeyword
import org.jelik.types.Type

/**
 * Expression representing try catch expression
 *
 * @author Marcin Bukowiecki
 */
class TryExpression(private val tryKeyword: TryKeyword, var block: BasicBlock): ExpressionReferencingType(), StackConsumer {

    init {
        block.parent = this;
    }

    override fun visit(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
    }

    override fun toString(): String {
        return "$tryKeyword { $block } ${getFurtherExpression()}"
    }

    override fun getType(): Type {
        return block.type
    }

    override fun getGenericType(): Type {
        return block.genericType
    }

    override fun getReturnType(): Type {
        return block.returnType
    }

    override fun getGenericReturnType(): Type {
        return block.genericReturnType
    }

    override fun getStartRow(): Int {
        return tryKeyword.row
    }
}
