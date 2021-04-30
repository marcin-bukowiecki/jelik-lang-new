package org.jelik.compiler.builtins

import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Test

/**
 * Tests for println function
 *
 * @author Marcin Bukowiecki
 */
class FunctionPrintlnTest {

    @Test
    fun test_println_1() {
        val expr = "fun expr() { println(\"test\") }"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Void>("expr")
    }

    @Test
    fun test_println_2() {
        val expr = "fun expr() { println() }"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Void>("expr")
    }

    @Test
    fun test_println_3() {
        val expr = "fun expr() { println(123) }"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Void>("expr")
    }

    @Test
    fun test_print_1() {
        val expr = "fun expr() { print(\"test\") }"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Void>("expr")
    }

    @Test
    fun test_print_2() {
        val expr = "fun expr() { print() }"
        FunctionCompiler.getInstance()
                .compileAndExpectError(expr, "Could not find method")
    }

    @Test
    fun test_print_3() {
        val expr = "fun expr() { print(123) }"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Void>("expr")
    }
}
