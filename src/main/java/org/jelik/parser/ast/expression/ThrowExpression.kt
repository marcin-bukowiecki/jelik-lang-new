package org.jelik.parser.ast.expression

import org.jelik.CompilationContext
import org.jelik.parser.ast.Expression
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.keyword.ThrowKeyword

/**
 * Expression representing throw expr or throw new Exception() etc.
 *
 * @author Marcin Bukowiecki
 */
class ThrowExpression(private val throwKeyword: ThrowKeyword, var expression: Expression): ExpressionReferencingType() {

    override fun visit(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
    }

    override fun replaceWith(oldNode: Expression, newNode: Expression) {
        if (oldNode != expression) {
            throw IllegalArgumentException()
        } else {
            expression = newNode;
        }
    }

    override fun toString(): String {
        return "$throwKeyword $expression"
    }
}
