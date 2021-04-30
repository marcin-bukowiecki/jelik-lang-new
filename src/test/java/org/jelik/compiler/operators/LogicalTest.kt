package org.jelik.compiler.operators

import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class LogicalTest {

    @Test
    fun testNotEqual_1() {
        val expr = """
            fun expr()->Boolean {
                var a =!true
                ret a
            }
        """.trimIndent()
        FunctionCompiler.getInstance().compile(expr)
            .invoke<Boolean>("expr")
            .isEqualTo(false)
    }

    @Test
    fun testNotEqual_2() {
        val expr = """
            fun expr()->Boolean {
                var a = false != true
                ret a
            }
        """.trimIndent()
        FunctionCompiler.getInstance().compile(expr)
            .invoke<Boolean>("expr")
            .isEqualTo(true)
    }

    @Test
    fun testNotEqual_3() {
        val expr = """
            fun expr()->Boolean {
                var a = false == true
                ret a
            }
        """.trimIndent()
        FunctionCompiler.getInstance().compile(expr)
            .invoke<Boolean>("expr")
            .isEqualTo(false)
    }

    @Test
    fun testNotEqual_4() {
        val expr = """
            fun expr()->Boolean {
                var a =!!true
                ret a
            }
        """.trimIndent()
        FunctionCompiler.getInstance().compile(expr)
            .invoke<Boolean>("expr")
            .isEqualTo(true)
    }

    @Test
    fun testNotEqual_5() {
        val expr = """
            fun expr()->Boolean {
                var a =!!!true
                ret a
            }
        """.trimIndent()
        FunctionCompiler.getInstance().compile(expr)
            .invoke<Boolean>("expr")
            .isEqualTo(false)
    }

    @Test
    fun testNotEqual_6() {
        val expr = """
            fun expr()->Boolean {
                var a =!(12 > 5
                 )
                ret a
            }
        """.trimIndent()
        FunctionCompiler.getInstance().compile(expr)
            .invoke<Boolean>("expr")
            .isEqualTo(false)
    }
}
