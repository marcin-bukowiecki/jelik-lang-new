package org.jelik.compiler.integration;

import org.assertj.core.api.Assertions;
import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionIntegrationTest {

    @Test
    public void shouldCompileFunctionExpression_1() {
        var expression = "fun expression(a Int, b Int) -> Int { ret a + b }";
        FunctionCompiler.getInstance()
                .compile(expression)
                .invoke("expression", 10, 22)
                .isEqualTo(32);
    }

    @Test
    public void shouldCompileFunctionExpression_2() {
        var expression = "fun expression(a Long, b Long) -> Long { ret a + b }";
        FunctionCompiler.getInstance()
                .compile(expression)
                .invoke("expression", 10L, 22L)
                .isEqualTo(32L);
    }

    @Test
    public void shouldCompileFunctionValExpression() {
        var expression = "fun expression(a Int, b Int) -> Int { " +
                "   val c = a + b\n" +
                "   ret c " +
                "}";
        FunctionCompiler.getInstance()
                .compile(expression)
                .invoke("expression",10, 22)
                .isEqualTo(32);
    }

    @Test
    public void shouldCompileFunctionMulValExpression() {
        var expression = "fun expression(a Int, b Int, c Int, d Int) -> Int { " +
                "   val e = a + b * c + d \n" +
                "   ret e " +
                "}";
        FunctionCompiler.getInstance()
                .compile(expression)
                .invoke("expression",3, 22, 10, 5)
                .isEqualTo(228);
    }

    @Test
    public void shouldReturnAForGivenGreaterValue() throws Exception {
       var expression = "fun greater(a Int, b Int) -> Int { " +
                "   if (a > b) {\n" +
                "       ret a\n" +
                "   } else\n{" +
                "       ret b\n" +
                "   }\n" +
                "}";
        var instance = FunctionCompiler.getInstance().compileAndGetInstance("Greater1", expression);
        Assertions.assertThat(instance.getClass().getMethod("greater", int.class, int.class).invoke(null, 12, 45))
                .isEqualTo(45);
    }

    @Test
    public void shouldImmediateAssignValue() throws Exception {
        final String expression = "fun assignValue(b Int) -> String { " +
                "   val a = \"-> year\" \n" +
                "   ret (a = \"foo\" + a)" +
                "}";
        final Object instance = FunctionCompiler.getInstance().compileAndGetInstance("Class2", expression);
        Assertions.assertThat(instance.getClass().getMethod("assignValue", int.class).invoke(null, 2018))
                .isEqualTo("foo-> year");
    }
}
