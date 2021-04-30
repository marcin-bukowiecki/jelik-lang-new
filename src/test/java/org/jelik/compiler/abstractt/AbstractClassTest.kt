package org.jelik.compiler.abstractt

import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class AbstractClassTest {

    @Test
    fun testAbstractClass_1() {
        val expr = """
            abstract class Foo {}
            fun get() -> Foo {
                ret Foo()
            }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compileAndExpectProblem(expr, "Can not instantiate abstract type")
    }

    @Test
    fun testInterface_1() {
        val expr = """
            import java.lang.Runnable
            fun get() -> Runnable {
                ret Runnable()
            }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compileAndExpectError(expr, "Can not instantiate interface type")
    }
}
