package org.jelik.types

import org.jelik.compiler.CompilationContext

/**
 * Represents ? type i.e. String?, Int? etc.
 *
 * @author Marcin Bukowiecki
 */
class MaybeType(private val inner: Type) : Type(inner.name, inner.canonicalName, inner.typeEnum) {

    override fun isAssignableTo(type: Type, compilationContext: CompilationContext): Boolean {
        return inner.isAssignableTo(type, compilationContext)
    }

    override fun isNullAssignableTo(type: Type, compilationContext: CompilationContext): Boolean {
        if (type !is MaybeType) return false
        return inner.isAssignableTo(type, compilationContext)
    }

    override fun forErrorMessage(): String {
        return toString()
    }

    override fun toString(): String {
        return "$inner?"
    }
}
