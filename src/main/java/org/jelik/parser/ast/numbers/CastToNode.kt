package org.jelik.parser.ast.numbers

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.ConsumingExpression
import org.jelik.parser.ast.context.TypedNodeContext
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.expression.ExpressionWrapper
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.types.Type

/**
 * @author Marcin Bukowiecki
 */
open class CastToNode(expression: Expression) : ExpressionWrapper(expression), ConsumingExpression {

    var nodeContext: TypedNodeContext = TypedNodeContext()

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
    }

    override fun getReturnType(): Type {
        return nodeContext.type
    }

    override fun getGenericReturnType(): Type {
        return nodeContext.genericType
    }

    override fun getType(): Type {
        return nodeContext.type
    }

    override fun getGenericType(): Type {
        return nodeContext.genericType
    }
}
