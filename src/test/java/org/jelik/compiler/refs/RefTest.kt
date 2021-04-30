package org.jelik.compiler.refs

import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class RefTest {

    @Test
    fun test_Int_1() {
        val expr = """
            fun expr(a Integer) -> Int {
                ret a.toString().hashCode()
            }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<String>("expr", Integer.valueOf(12))
            .isEqualTo(1569)
    }

    @Test
    fun test_Int_2() {
        val expr = """
            fun expr(a Integer) -> Int {
                ret a.toString().hashCode() + 1
            }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<String>("expr", Integer.valueOf(12))
            .isEqualTo(1570)
    }

    @Test
    fun testFunRef_1() {
        val expr = """
            fun f1(a Int) -> Int {
                ret a + 10
            }
            
            fun f1(a Double) -> Double {
                ret a + 10
            }
            
            fun expr(a Int) -> Int {
                val ref = f1
                ret ref(a)
            }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<String>("expr", 12)
            .isEqualTo(22)
    }

    @Test
    fun testFunRef_2() {
        val expr = """
            fun f1(a Int) -> Int {
                ret a + 10
            }
            
            fun f1(a Double) -> Double {
                ret a + 10
            }
            
            fun expr(a String) -> Int {
                val ref = f1
                ret ref(a)
            }
        """.trimIndent()
        FunctionCompiler.getInstance()
            .compileAndExpectError(expr, "Unresolved function reference")
    }
}
