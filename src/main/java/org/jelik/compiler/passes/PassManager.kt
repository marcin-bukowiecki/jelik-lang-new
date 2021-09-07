package org.jelik.compiler.passes

import org.jelik.compiler.CompilationContext

/**
 * @author Marcin Bukowiecki
 */
class PassManager(private val compilationContext: CompilationContext) {

    private val passRegister = listOf(
        TypeChecker,
        NullChecker,
        AccessPass,
        PrimitiveTypeLifter,
    )

    fun getAllPasses(): List<BasePass> {
        return passRegister
    }

    fun <T : BasePass> getPass(type: Class<*>): T? {
        return passRegister.firstOrNull { pass -> type.isInstance(pass) } as T?
    }
}
