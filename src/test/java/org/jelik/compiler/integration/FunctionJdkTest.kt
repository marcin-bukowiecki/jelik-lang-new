package org.jelik.compiler.integration

import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class FunctionJdkTest {

    @Test
    fun shouldPrintHelloWorld() {
        val expr = "fun expr() -> Int { System.out.println(\"Hello world\")\n ret 0 }"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(0)
    }
}
