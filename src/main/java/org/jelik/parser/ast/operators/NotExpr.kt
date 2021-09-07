package org.jelik.parser.ast.operators

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.ast.expression.Expression
import org.jelik.parser.token.operators.NotOperator
import org.jelik.types.JVMBooleanType

/**
 * @author Marcin Bukowiecki
 */
class NotExpr(op: NotOperator, right: Expression) : AbstractOpExpr(op, right) {

    init {
        this.nodeContext.type = JVMBooleanType.INSTANCE
        this.nodeContext.genericType = JVMBooleanType.INSTANCE
    }

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitNotExpr(this, compilationContext);
    }

    fun getExpression(): Expression {
        return right
    }
}
