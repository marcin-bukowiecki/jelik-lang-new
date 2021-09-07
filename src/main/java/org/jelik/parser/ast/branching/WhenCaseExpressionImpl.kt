package org.jelik.parser.ast.branching

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.ASTNodeImpl
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.types.Type

/**
 * @author Marcin Bukowiecki
 */
class WhenCaseExpressionImpl(private val conditionExpression: Expression,
                             private val rightExpression: Expression) : ASTNodeImpl(), WhenCaseExpression {

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        TODO("Not yet implemented")
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

    override fun toString(): String {
        return "$conditionExpression -> $rightExpression"
    }
}