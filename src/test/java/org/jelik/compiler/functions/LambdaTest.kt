package org.jelik.compiler.functions

import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Ignore
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class LambdaTest {

    @Test
    @Ignore
    fun lambda_test_1() {
        val expr = """
            fun expr(a Int) -> Int {
                   val l = lam b -> { ret b }
                   ret a + l(a)
                }
            """
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Int>("expr", 123)
                .isEqualTo(246)
    }

    @Test
    @Ignore
    fun lambda_test_2() {
        val expr = """
            fun toPass(fn || -> Int) -> Int {
                ret fn()
            }
            
            fun expr() -> Int {
                ret toPass { 1234 }
            }
            """.trimIndent()
        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<Int>("expr")
            .isEqualTo(1234)
    }
}
