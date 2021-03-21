package org.jelik.compiler.integration

import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class FunctionCallTest {

    @Test
    fun callFunctionRef_1() {
        val expr = "fun toCall() -> Int { ret 123 }\n" +
                "fun expr(a Int) -> Int {\n" +
                "   val b = toCall\n" +
                "   ret b() + a" +
                "}";

        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Any>("expr", 123)
                .isEqualTo(246);
    }

    @Test
    fun callFunctionRef_2() {
        val expr = "fun toCall(a Int) -> Int { ret 123 + a}\n" +
                "fun expr(a Int) -> Int {\n" +
                "   val b = toCall\n" +
                "   ret b(678) + a" +
                "}";

        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Any>("expr", 123)
                .isEqualTo(924);
    }

    @Test
    fun callFunctionRef_3() {
        val expr = "fun toCall(a Integer) -> Int { ret 123 + a }\n" +
                "fun expr(a Int) -> Int {\n" +
                "   val b = toCall\n" +
                "   ret b(678) + a" +
                "}";

        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Any>("expr", 123)
                .isEqualTo(924);
    }
}
