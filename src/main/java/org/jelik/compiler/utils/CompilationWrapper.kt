package org.jelik.compiler.utils

import org.jelik.compiler.asm.visitor.ToByteCodeResult
import org.jelik.compiler.exceptions.CompileException
import java.util.function.Supplier

/**
 * @author Marcin Bukowiecki
 */
object CompilationWrapper {

    @JvmStatic
    fun wrap(f: Supplier<List<ToByteCodeResult>>): List<ToByteCodeResult> {
        return wrap(f, false)
    }

    @JvmStatic
    fun wrap(f: Supplier<List<ToByteCodeResult>>, rethrowError: Boolean): List<ToByteCodeResult> {
        return try {
            f.get()
        } catch (ex: CompileException) {
            ex.printErrorMessage()
            if (rethrowError) throw ex
            emptyList()
        }
    }
}
