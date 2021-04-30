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
                if true do
                
                }
            }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compileAndExpectError(expr, "Expected then keyword")
    }

    @Test
    fun testBlock_2() {
        val expr = """
            fun expr() {
                if true then
                
                }
            }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compileAndExpectError(expr, "Unexpected token")
    }

    @Test
    fun testBlock_3() {
        val expr = """
            fun expr() {
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compileAndExpectError(expr, "Expected right curl")
    }

    @Test
    fun testBlock_4() {
        val expr = """
            fun expr() {
                if true then
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compileAndExpectError(expr, "Unexpected token")
    }
}
