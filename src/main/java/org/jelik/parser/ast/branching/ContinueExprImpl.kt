package org.jelik.parser.ast.branching

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.ASTNodeImpl
import org.jelik.parser.ast.labels.LabelNode
import org.jelik.parser.ast.utils.ASTDataKey
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.keyword.ContinueKeyword
import org.jelik.types.JVMVoidType
import org.jelik.types.Type

/**
 * @author Marcin Bukowiecki
 */
class ContinueExprImpl(private val keyword: ContinueKeyword) : ASTNodeImpl(), ContinueExpr {

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitContinueExpression(this, compilationContext)
    }

    override fun getStartCol(): Int {
        return keyword.col
    }

    override fun getStartRow(): Int {
        return keyword.row
    }

    override fun getEndCol(): Int {
        return keyword.endCol
    }

    override fun getEndRow(): Int {
        return keyword.endRow
    }

    override fun getReturnType(): Type {
        return JVMVoidType.INSTANCE
    }

    override fun getGenericReturnType(): Type {
        return JVMVoidType.INSTANCE
    }

    override fun getType(): Type {
        return JVMVoidType.INSTANCE
    }

    override fun getGenericType(): Type {
        return JVMVoidType.INSTANCE
    }

    override fun setJumpTo(labelNode: LabelNode) {
        this.dataHolder.putData(ASTDataKey.LABEL_NODE, labelNode)
    }

    override fun getJumpTo(): LabelNode? {
        return this.dataHolder.getData(ASTDataKey.LABEL_NODE)
    }

    override fun toString(): String {
        return keyword.toString()
    }
}
