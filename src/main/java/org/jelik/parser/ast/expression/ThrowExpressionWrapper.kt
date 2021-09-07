package org.jelik.parser.ast.expression

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.visitors.AstVisitor
import org.jelik.parser.token.keyword.ThrowKeyword

/**
 * Expression representing throw expr or throw new Exception() etc.
 *
 * @author Marcin Bukowiecki
 */
class ThrowExpressionWrapper(private val throwKeyword: ThrowKeyword,
                             expression: Expression): ExpressionWrapper(expression) {

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visit(this, compilationContext)
    }

    override fun toString(): String {
        return "$throwKeyword $expression"
    }
}
