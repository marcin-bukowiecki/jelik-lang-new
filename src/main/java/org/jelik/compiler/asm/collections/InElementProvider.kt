package org.jelik.compiler.asm.collections

import org.jelik.compiler.config.CompilationContext
import org.jelik.compiler.asm.visitor.ToByteCodeVisitor

/**
 * @author Marcin Bukowiecki
 */
interface InElementProvider {
    fun visit(toByteCodeVisitor: ToByteCodeVisitor, compilationContext: CompilationContext)
}
