package org.jelik.parser.ast.types

import org.jelik.compiler.config.CompilationContext
import org.jelik.parser.ast.visitors.AstVisitor

/**
 * @author Marcin Bukowiecki
 */
class InferredTypeNode : TypeNode() {

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitInferredTypeNode(this, compilationContext)
    }

    override fun getEndRow(): Int {
        throw UnsupportedOperationException()
    }

    override fun getSymbol(): String {
        return "INFERRED"
    }

    override fun toString(): String {
        return "INFERRED"
    }
}
