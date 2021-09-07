package org.jelik.parser.ast.types

import org.jelik.compiler.CompilationContext
import org.jelik.parser.ast.visitors.AstVisitor

/**
 * @author Marcin Bukowiecki
 */
class InferredTypeNode : TypeNode() {

    override fun accept(astVisitor: AstVisitor, compilationContext: CompilationContext) {
        astVisitor.visitInferredTypeNode(this, compilationContext)
    }

    override fun getStartOffset(): Int {
        return -1
    }

    override fun getEndOffset(): Int {
        return -1
    }

    override fun getSymbol(): String {
        return "INFERRED"
    }

    override fun toString(): String {
        return "INFERRED"
    }

    override fun isInferred(): Boolean {
        return true;
    }
}
