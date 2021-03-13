package org.jelik.parser.ast.numbers

import org.jelik.CompilationContext
import org.jelik.parser.ast.ConsumingExpression
import org.jelik.parser.ast.Expression
import org.jelik.parser.ast.expression.ExpressionWithType
import org.jelik.parser.ast.visitors.AstVisitor

/**
 * @author Marcin Bukowiecki
 */
open class CastToNode : ExpressionWithType, ConsumingExpression {

    val subject: Expression

    constructor(subject: Expression) {
        this.subject = subject
        this.subject.parent = this;
    }

    constructor(subject: Expression, further: Expression?) {
        if (further != null) {
            this.furtherExpression = further
            this.furtherExpression.parent = this
        }
        this.subject = subject
        this.subject.parent = this
    }

    override fun visit(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
    }
}
