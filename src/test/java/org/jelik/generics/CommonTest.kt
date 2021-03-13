package org.jelik.generics

import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class CommonTest {

    @Test
    fun shouldReturnObject() {
        val expr = "fun c<T>(arg Object) -> T {\n" +
                "ret arg as T\n" +
                "}"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("c", "foo")
                .isEqualTo("foo");
    }

    @Test
    fun shouldReturnInt() {
        val expr = "fun c<T>(arg T) -> Int {\n" +
                "ret arg.hashCode()\n" +
                "}"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("c", "foo")
                .isEqualTo(101574);
    }
}
