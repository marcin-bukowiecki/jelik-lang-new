package org.jelik.compiler.loops

import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class ForLoopExceptionTest {

    @Test
    fun forEachExpectedCollection_1() {
        val expr = "import java.util.List\n" +
                "\n" +
                "fun expr(l Int) -> Int {\n" +
                "   for e in l do\n" +
                "       System.out.println(e)\n" +
                "   end\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
                .getInstance()
                .compileAndExpectError(expr, "Expected java.util.Collection type or array type, got: Int")
    }

    @Test
    fun forEachExpectedCollection_2() {
        val expr = "import java.util.List\n" +
                "\n" +
                "fun expr(l Int) -> Int {\n" +
                "   for e in ll do\n" +
                "       System.out.println(e)\n" +
                "   end\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
                .getInstance()
                .compileAndExpectError(expr, "Could not resolve symbol: ll")
    }
}
