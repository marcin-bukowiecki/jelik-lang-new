package org.jelik.compiler.exceptions

import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class BlockSyntaxTest {

    @Test
    fun testBlock_1() {
        val expr = """
            fun expr() {
                if true 
                
                }
            }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compileAndExpectError(expr, "Expected '{'")
    }

    @Test
    fun testBlock_2() {
        val expr = """
            fun expr() {
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compileAndExpectError(expr, "Expected '}'")
    }

    @Test
    fun testBlock_3() {
        val expr = """
            fun expr() {
                if true tasda
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compileAndExpectError(expr, "Expected '{'")
    }
}
