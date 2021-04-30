package org.jelik.parser.ast.loops

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.ASTNodeImpl
import org.jelik.parser.ast.LiteralExpr
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.types.Type

/**
 * @author Marcin Bukowiecki
 */
class LoopVar(expression: LiteralExpr) : ASTNodeImpl(), Expression {

    private val name: String = expression.literalToken.text

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitLoopVar(this, compilationContext)
    }

    override fun getReturnType(): Type {
        TODO("Not yet implemented")
    }

    override fun getGenericReturnType(): Type {
        TODO("Not yet implemented")
    }

    override fun getType(): Type {
        TODO("Not yet implemented")
    }

    override fun getGenericType(): Type {
        TODO("Not yet implemented")
    }

    fun getName(): String {
        return name
    }
}
