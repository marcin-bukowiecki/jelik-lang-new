package org.jelik.compiler.classes

import org.assertj.core.api.Assertions
import org.jelik.compiler.utils.FunctionCompiler
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

        val invoke = FunctionCompiler.getInstance().compile(expr).invoke("test").value.javaClass
        Assertions.assertThat(invoke.canonicalName)
                .isEqualTo("Test")
    }
}
