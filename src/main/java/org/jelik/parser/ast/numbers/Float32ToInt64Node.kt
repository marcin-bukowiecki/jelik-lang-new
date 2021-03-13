package org.jelik.parser.ast.numbers

import org.jelik.CompilationContext
import org.jelik.parser.ast.Expression
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.types.jvm.JVMDoubleType
import org.jelik.types.jvm.JVMLongType

/**
 * @author Marcin Bukowiecki
 */
class Float32ToInt64Node(subject: Expression) : CastToNode(subject) {

    init {
        nodeContext.type = JVMLongType.INSTANCE
        nodeContext.genericType = JVMLongType.INSTANCE
    }

    override fun visit(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
    }
}
