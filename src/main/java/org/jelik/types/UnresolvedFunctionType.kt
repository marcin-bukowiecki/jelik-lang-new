package org.jelik.types

import org.jelik.compiler.config.CompilationContext
import org.jelik.compiler.data.ClassData
import org.jelik.compiler.data.MethodData

/**
 * @author Marcin Bukowiecki
 */
class UnresolvedFunctionType(val functions: List<MethodData>) : FunctionType("Unresolved", "Unresolved") {

    override fun findClassData(compilationContext: CompilationContext): ClassData {
        throw IllegalAccessException("Unresolved function type")
    }
}
