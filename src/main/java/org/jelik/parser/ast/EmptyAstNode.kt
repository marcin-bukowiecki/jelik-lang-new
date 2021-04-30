package org.jelik.parser.ast

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.visitors.AstVisitor

/**
 * Used for null safety
 *
 * @author Marcin Bukowiecki
 */
class EmptyAstNode : ASTNodeImpl() {

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {

    }

    companion object {
        val INSTANCE = EmptyAstNode()
    }
}
