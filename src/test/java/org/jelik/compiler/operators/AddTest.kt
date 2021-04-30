package org.jelik.compiler.operators

import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class AddTest {

    @Test
    fun test_AddInts_1() {
        val expr = """
            fun expr() -> Int { ret 10 + 12 }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<Int>("expr")
            .isEqualTo(22)
    }

    @Test
    fun test_AddInts_2() {
        val expr = """
            fun expr(a Int) -> Int { ret a + 12 }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<Int>("expr", 10)
            .isEqualTo(22)
    }

    @Test
    fun test_AddInts_3() {
        val expr = """
            fun expr(a Int, b Int) -> Int { ret a + b }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<Int>("expr", 10, 12)
            .isEqualTo(22)
    }

    @Test
    fun test_AddInts_4() {
        val expr = """
            import java.util.List
            fun expr(list List<Int>, b Int) -> Int { ret list.size() + b }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<Int>("expr", listOf(1,2,3,4), 12)
            .isEqualTo(16)
    }

    @Test
    fun test_AddInts_5() {
        val expr = """
            import java.util.List
            fun expr(list List<Int>, b Int) -> Int { ret 10 + list.size() + b }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<Int>("expr", listOf(1,2,3,4), 12)
            .isEqualTo(26)
    }
}
