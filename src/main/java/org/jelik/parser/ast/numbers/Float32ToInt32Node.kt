package org.jelik.parser.ast.numbers

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.types.JVMIntType

/**
 * @author Marcin Bukowiecki
 */
class Float32ToInt32Node(expression: Expression) : CastToNode(expression) {

    init {
        nodeContext.type = JVMIntType.INSTANCE
        nodeContext.genericType = JVMIntType.INSTANCE
    }

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
    }
}
