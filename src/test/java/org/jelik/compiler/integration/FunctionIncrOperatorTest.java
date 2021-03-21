package org.jelik.compiler.integration;

import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionIncrOperatorTest {

    @Test
    public void returnBooleanFromSimpleExpr_1() {
        var expr = "fun expr(a Int) -> Int { ++a \n ret a }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12)
                .isEqualTo(13);
    }

    @Test
    public void returnBooleanFromSimpleExpr_2() {
        var expr = "fun expr(a Int) -> Int {  \n ret ++a }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12)
                .isEqualTo(13);
    }

    @Test
    public void intIncr_1() {
        var expr = "fun expr(a Int) -> Int {  \n ret ++a + 10 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 22)
                .isEqualTo(33);
    }

    @Test
    public void intIncr_2() {
        var expr = "fun expr(a Int) -> Int {  \n ret 12 + (++a) + 10 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 22)
                .isEqualTo(45);
    }

    @Test
    public void intIncr_3() {
        var expr = "fun expr(a Integer) -> Int { ++a \n ret a }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", Integer.valueOf(12))
                .isEqualTo(13);
    }

    @Test
    public void intIncr_4() {
        var expr = "fun expr(a Integer) -> Int { \n ret ++a }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", Integer.valueOf(12))
                .isEqualTo(13);
    }
}
