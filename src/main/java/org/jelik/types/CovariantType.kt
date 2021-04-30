package org.jelik.types

import org.jelik.compiler.config.CompilationContext
import org.jelik.compiler.common.TypeEnum

/**
 * @author Marcin Bukowiecki
 */
class CovariantType(val symbol: String,
                    val upper: Type) : Type(upper.name, upper.canonicalName, TypeEnum.objectT) {

    override fun toString(): String {
        return "$symbol : $upper"
    }

    override fun isAssignableTo(type: Type, compilationContext: CompilationContext): Boolean {
        return upper.isAssignableTo(type, compilationContext)
    }
}
