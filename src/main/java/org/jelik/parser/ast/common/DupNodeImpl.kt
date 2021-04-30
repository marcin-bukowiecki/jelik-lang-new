package org.jelik.parser.ast.common

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.ASTNodeImpl
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.types.Type

/**
 * @author Marcin Bukowiecki
 */
class DupNodeImpl(val expression: Expression) : ASTNodeImpl(), DupNode {

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitDupNode(this, compilationContext)
    }

    override fun getReturnType(): Type {
        return expression.returnType
    }

    override fun getGenericReturnType(): Type {
        return expression.genericReturnType
    }

    override fun getType(): Type {
        return expression.type
    }

    override fun getGenericType(): Type {
        return expression.genericType
    }

    override fun toString(): String {
        return expression.toString()
    }
}
