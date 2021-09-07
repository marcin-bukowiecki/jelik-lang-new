package org.jelik.parser.ast.numbers

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.types.jvm.JVMLongType

/**
 * @author Marcin Bukowiecki
 */
class Float32ToInt64Node(subject: Expression) : CastToNode(subject) {

    init {
        nodeContext.type = JVMLongType.INSTANCE
        nodeContext.genericType = JVMLongType.INSTANCE
    }

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
    }
}
