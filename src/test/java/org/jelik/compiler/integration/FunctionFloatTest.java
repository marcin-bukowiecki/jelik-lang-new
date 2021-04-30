package org.jelik.compiler.integration;

import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionFloatTest {

    @Test
    public void shouldAddTwoFloat() {
        var expr = "fun expr(a Float, b Float) -> Float { ret a + b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12f, 12f)
                .isEqualTo(24f);
    }

    @Test
    public void shouldRemTwoFloat() {
        var expr = "fun expr(a Float, b Float) -> Float { ret a % b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12f, 12f)
                .isEqualTo(0f);
    }

    @Test
    public void shouldCastFloat_1() {
        var expr = "fun expr(a Float) -> Int { ret a as Int }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 24f)
                .isEqualTo(24);
    }

    @Test
    public void shouldCastFloat_2() {
        var expr = "fun expr(a Float) -> Double { ret a as Double }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 24f)
                .isEqualTo(24.0);
    }

    @Test
    public void shouldCastFloat_3() {
        var expr = "fun expr(a Float) -> Long { ret a as Long }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 24f)
                .isEqualTo(24L);
    }

    @Test
    public void shouldDivTwoFloat() {
        var expr = "fun expr(a Float, b Float) -> Float { ret a / b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12f, 3f)
                .isEqualTo(4f);
    }

    @Test
    public void shouldMulTwoFloat() {
        var expr = "fun expr(a Float, b Float) -> Float { ret a * 4 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12f, 4f)
                .isEqualTo(48f);
    }

    @Test
    public void shouldSubTwoFloat() {
        var expr = "fun expr(a Float, b Float) -> Float { val c = a\n ret c - b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12f, 10f)
                .isEqualTo(2f);
    }

    @Test
    public void shouldNegFloat() {
        var expr = "fun expr(b Float) -> Float { ret - b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 10f)
                .isEqualTo(-10f);
    }

    @Test
    public void shouldCompareFloats_1() {
        var expr = "fun expr(b Float) -> Boolean { val a = .5f \n ret b == a }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 10f)
                .isEqualTo(false);
    }

    @Test
    public void shouldCompareFloats_2() {
        var expr = "fun expr(b Float) -> Boolean { val a = .5f \n ret b != a }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 10f)
                .isEqualTo(true);
    }

    @Test
    public void shouldCompareFloats_3() {
        var expr = "fun expr(b Float) -> Boolean { ret b > 9}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 10f)
                .isEqualTo(true);
    }
}
