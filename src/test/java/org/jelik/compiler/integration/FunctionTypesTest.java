package org.jelik.compiler.integration;

import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionTypesTest {

    @Test
    public void checkForString() {
        var expr = "fun expr(a Object) -> Boolean { ret a is String }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo")
                .isEqualTo(true);
    }

    @Test
    public void checkForInt_1() {
        var expr = "fun expr(a Object) -> Boolean { ret a is Int }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo")
                .isEqualTo(false);
    }

    @Test
    public void checkForInt_2() {
        var expr = "fun expr(a Int) -> Boolean { ret a is Int }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 1)
                .isEqualTo(true);
    }
}
