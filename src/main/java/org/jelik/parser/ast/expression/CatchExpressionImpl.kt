package org.jelik.parser.ast.expression

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.ASTNode
import org.jelik.parser.ast.ASTNodeImpl
import org.jelik.parser.ast.blocks.BasicBlockImpl
import org.jelik.parser.ast.functions.FunctionParameterList
import org.jelik.parser.ast.labels.LabelNode
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.keyword.CatchKeyword
import org.jelik.types.Type

/**
 * Expression representing catch expression
 *
 * @author Marcin Bukowiecki
 */
class CatchExpressionImpl(private val catchKeyword: CatchKeyword,
                          private val args: FunctionParameterList,
                          private var block: BasicBlockImpl
): ASTNodeImpl(), CatchExpression {

    override var endLabel: LabelNode? = null

    override var innerLabel: LabelNode? = null

    override var startLabel: LabelNode? = null

    init {
        block.parent = this
    }

    override fun getBlock(): BasicBlockImpl {
       return block
    }

    override fun getArgs(): FunctionParameterList {
       return args
    }

    override fun getChildren(): List<ASTNode> {
        return listOf(args, block)
    }

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitCatchExpression(this, compilationContext)
    }

    override fun getReturnType(): Type {
        return block.returnType
    }

    override fun getGenericReturnType(): Type {
        return block.genericReturnType
    }

    override fun getType(): Type {
        return block.type
    }

    override fun getGenericType(): Type {
        return block.genericType
    }

    override fun toString(): String {
        return "$catchKeyword $args { $block }"
    }
}
