package org.jelik.parser.ast.numbers

import org.jelik.CompilationContext
import org.jelik.parser.ast.Expression
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.types.JVMIntType
import org.jelik.types.jvm.JVMDoubleType
import org.jelik.types.jvm.JVMLongType

/**
 * @author Marcin Bukowiecki
 */
class Float32ToInt32Node(subject: Expression) : CastToNode(subject) {

    init {
        nodeContext.type = JVMIntType.INSTANCE
        nodeContext.genericType = JVMIntType.INSTANCE
    }

    override fun visit(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
    }
}
