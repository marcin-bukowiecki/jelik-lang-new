package org.jelik.compiler.operators

import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class InTest {

    @Test
    fun inListTest_1() {
        val expr = "import java.util.List\n" +
                "fun expr(a List<Int>) -> Boolean { ret 123 in a }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Boolean>("expr", listOf(1,2,3,4,5))
                .isEqualTo(false);
    }

    @Test
    fun inListTest_2() {
        val expr = "import java.util.List\n" +
                "fun expr(a List<Int>) -> Boolean { \n" +
                "if 123 in a then\n" +
                "   ret true\n" +
                "end\n" +
                "ret false\n" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Boolean>("expr", listOf(1,2,3,4,5))
                .isEqualTo(false);
    }

    @Test
    fun inListTest_3() {
        val expr = "import java.util.List\n" +
                "fun expr(a List<Int>) -> Boolean { \n" +
                "if 123 in a then\n" +
                "   ret true\n" +
                "end\n" +
                "ret false\n" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Boolean>("expr", listOf(1,2,123,4,5))
                .isEqualTo(true);
    }

    @Test
    fun inArrayTest_1() {
        val expr = "import java.util.List\n" +
                "fun expr() -> Boolean { ret 123 in [1,2,3,4,5] }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Boolean>("expr")
                .isEqualTo(false);
    }

    @Test
    fun inArrayTest_2() {
        val expr = "import java.util.List\n" +
                "fun expr(a Long) -> Boolean { ret a in [1,2,3,4,5]Long }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Boolean>("expr", 4L)
                .isEqualTo(true);
    }

    @Test
    fun inArrayTest_3() {
        val expr = "import java.util.List\n" +
                "fun expr() -> Boolean { ret 123 in [1,2,3,4,5]Double }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Boolean>("expr")
                .isEqualTo(false);
    }

    @Test
    fun inArrayTest_4() {
        val expr = "import java.util.List\n" +
                "fun expr() -> Boolean { ret \"foo\" in [\"foo\", \"bar\"] }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Boolean>("expr")
                .isEqualTo(true);
    }
}
