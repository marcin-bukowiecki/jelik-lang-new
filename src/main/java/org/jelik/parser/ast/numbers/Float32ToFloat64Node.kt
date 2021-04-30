package org.jelik.parser.ast.numbers

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.types.jvm.JVMDoubleType

/**
 * @author Marcin Bukowiecki
 */
class Float32ToFloat64Node(subject: Expression) : CastToNode(subject) {

    init {
        nodeContext.type = JVMDoubleType.INSTANCE
        nodeContext.genericType = JVMDoubleType.INSTANCE
    }

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
    }
}
