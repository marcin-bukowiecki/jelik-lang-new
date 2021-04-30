package org.jelik.compiler.integration;

import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionStringsTest {

    @Test
    public void testAddTwoStrings_1() {
        var expr = "fun expr(a String, b String) -> String { ret a + b }";
        FunctionCompiler
                .getInstance()
                .compile(expr)
                .invoke("expr", "foo", "bar")
                .isEqualTo("foobar");
    }

    @Test
    public void testAddTwoStrings_2() {
        var expr = "fun expr(a java.lang.String, b String) -> String { ret a + b }";
        FunctionCompiler
                .getInstance()
                .compile(expr)
                .invoke("expr", "foo", "bar")
                .isEqualTo("foobar");
    }

    @Test
    public void addIntToStrings() {
        var expr = "fun expr(a Int, b String) -> String { ret a + b }";
        FunctionCompiler
                .getInstance()
                .compile(expr)
                .invoke("expr", 1, "bar")
                .isEqualTo("1bar");
    }

    @Test
    public void addTwoStrings_failure_1() {
        var expr = "fun expr(a String, b String) -> String { ret a + b - 1 }";
        FunctionCompiler
                .getInstance()
                .compileAndExpectError(expr, "Operator '-' can't be applied to 'java.lang.String' and 'Int'");
    }
}
