package org.jelik.compiler.arrays

import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class ArrayCreateWithSizeTest {

    @Test
    fun createIntArray_1() {
        val expr = "fun expr() -> []Int {" +
                "   ret []Int(5)" +
                "}"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<IntArray>("expr")
                .isEqualTo(intArrayOf(0, 0, 0, 0, 0))
    }

    @Test
    fun createIntArray_2() {
        val expr = "fun expr() -> []Int {" +
                "   val a = []Int(5)\n" +
                "   a[2] = 123\n" +
                "   ret a" +
                "}"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<IntArray>("expr")
                .isEqualTo(intArrayOf(0, 0, 123, 0, 0))
    }

    @Test
    fun createStringArray_1() {
        val expr = "fun expr() -> []String {" +
                "   ret []String(5)" +
                "}"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Array<String>>("expr")
                .isEqualTo(arrayOf<String?>(null, null, null, null, null))
    }

    @Test
    fun createStringArray_2() {
        val expr = "fun expr() -> []String {" +
                "   ret []String(0)" +
                "}"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Array<String>>("expr")
                .isEqualTo(arrayOf<String?>())
    }
}
