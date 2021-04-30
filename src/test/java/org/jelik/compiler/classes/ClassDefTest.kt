package org.jelik.compiler.classes

import org.assertj.core.api.Assertions
import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Ignore
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class ClassDefTest {

    @Test
    fun shouldDefineSimpleClass_1() {
        val expr = "" +
                "class Test {}\n" +
                "\n" +
                "fun test() -> Test { ret Test() }";

        val invoke = FunctionCompiler.getInstance().compile(expr).invoke<Any>("test").value.javaClass
        Assertions.assertThat(invoke.canonicalName)
                .isEqualTo("Test")
    }

    @Test
    fun shouldDefineSimpleClass_2() {
        val expr = "" +
                "class Test {}\n" +
                "\n" +
                "fun test() -> Test { ret Test() }";

        val invoke = FunctionCompiler.getInstance().compile(expr).invoke<Any>("test").value.javaClass
        Assertions.assertThat(invoke.canonicalName)
                .isEqualTo("Test")
    }

    @Test
    fun shouldDefineSimpleClass_3() {
        val expr = "" +
                "class \n" +
                "\n" +
                "fun test() -> Test { ret Test() }";
        Assertions.assertThatThrownBy {
            FunctionCompiler.getInstance().compile(expr).invoke<Any>("test").value.javaClass
        }.hasMessage("Unexpected token")
    }

    @Test
    fun shouldDefineTpl1_1() {
        val expr = "class Tuple1<T1> {\n" +
                "constructor(_1 T1) {}\n" +
                "}\n" +
                "\n" +
                "fun test() -> Tuple1<Int> { ret Tuple1<Int>(123) }"
        FunctionCompiler.getInstance().compile(expr).invoke<Any>("test")
    }
}
