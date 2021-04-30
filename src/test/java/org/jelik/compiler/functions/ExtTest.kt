package org.jelik.compiler.functions

import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Ignore
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class ExtTest {

    @Test
    fun testIntExt_1() {
        val expr = """
            ext fun Int getString() -> String {
                ret "foo"
            }
            
            fun expr() -> String {
                ret 123.getString()
            }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<String>("expr")
            .isEqualTo("foo")
    }

    @Test
    fun testIntExt_2() {
        val expr = """
            ext fun Int getString() -> String {
                ret this.toString()
            }
            
            fun expr() -> String {
                ret 123.getString()
            }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<String>("expr")
            .isEqualTo("123")
    }

    @Test
    @Ignore
    fun testIntExt_3() {
        val expr = """
            ext fun T getString<T>() -> String {
                ret this.toString()
            }
            
            fun expr() -> String {
                ret 123.getString()
            }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<String>("expr")
            .isEqualTo("123")
    }
}
