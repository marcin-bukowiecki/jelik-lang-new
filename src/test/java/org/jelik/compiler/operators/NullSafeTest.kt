package org.jelik.compiler.operators

import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Ignore
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class NullSafeTest {

    @Test
    fun testIntegerNull_1() {
        val expr = """
            fun expr(a Integer?) -> String? {
                ret a?.toString()
            }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<String>("expr", Integer.valueOf(12))
            .isEqualTo("12")
    }

    @Test
    fun testIntegerNull_2() {
        val expr = """
            fun expr() -> String? {
                val a Integer = Null
                ret a?.toString()
            }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<String>("expr")
            .isEqualTo(null)
    }

    @Test
    fun testIntegerNull_3() {
        val expr = """
            fun expr(a Integer?) -> Int? {
                ret a?.toString()?.hashCode()
            }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<String>("expr",  null as Int?)
            .isEqualTo(null)
    }

    @Test
    @Ignore
    fun testIntegerNull_4() {
        val expr = """
            fun expr() -> Int {
                val a Int = Null as Integer
                a?.toString()?.hashCode()
                return 1
            }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<String>("expr")
            .isEqualTo(1)
    }
}
