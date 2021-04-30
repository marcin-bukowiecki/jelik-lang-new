package org.jelik.parser.ast.expression

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.ASTNode
import org.jelik.parser.ast.ASTNodeImpl
import org.jelik.parser.ast.blocks.BasicBlockImpl
import org.jelik.parser.ast.labels.LabelNode
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.keyword.TryKeyword
import org.jelik.types.Type

/**
 * Expression representing try catch expression
 *
 * @author Marcin Bukowiecki
 */
class TryExpressionImpl(private val tryKeyword: TryKeyword,
                        override val block: BasicBlockImpl,
                        override val catchExpression: CatchExpression): ASTNodeImpl(), TryExpression, StackConsumer {

    override var startLabel: LabelNode? = null

    override var endLabel: LabelNode? = null

    init {
        block.parent = this
        catchExpression.parent = this
    }

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitTryExpression(this, compilationContext)
    }

    override fun getChildren(): List<ASTNode> {
        return listOf<ASTNode>(block, catchExpression)
    }

    override fun toString(): String {
        return "$tryKeyword { $block }"
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
