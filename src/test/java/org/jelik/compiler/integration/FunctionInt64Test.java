package org.jelik.compiler.integration;

import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionInt64Test {

    @Test
    public void shouldNegLong_1() {
        var expr = "fun expr(a Long) -> Long { val b = 10 \n ret -a * b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 123)
                .isEqualTo(-1230L);
    }

    @Test
    public void shouldNegLong_2() {
        var expr = "fun expr(a Long) -> Long { val b = 10L \n ret -a * b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 123)
                .isEqualTo(-1230L);
    }

    @Test
    public void shouldNegLong_3() {
        var expr = "fun expr(a Long) -> Long { val b = 1L \n ret -a * b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 123)
                .isEqualTo(-123L);
    }

    @Test
    public void shouldRemLong() {
        var expr = "fun expr(a Long) -> Long { val b = 10 \n ret -a % b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 123)
                .isEqualTo(-3L);
    }

    @Test
    public void shouldSubLongs_1() {
        var expr = "fun expr(a Long) -> Long { val b = 10 \n ret a - b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 123)
                .isEqualTo(113L);
    }

    @Test
    public void shouldSubLongs_2() {
        var expr = "fun expr(a Long) -> Long { val b = 10 as Long \n ret a - b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 123)
                .isEqualTo(113L);
    }

    @Test
    public void shouldSubLongAndChar() {
        var expr = "fun expr(a Char) -> Long { val b = 10 \n ret a - b as Long }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", (char) 123)
                .isEqualTo(113L);
    }

    @Test
    public void shouldDivLong_1() {
        var expr = "fun expr(a Long) -> Long { val b = 10 \n ret a / b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 1200L)
                .isEqualTo(10L);
    }

    @Test
    public void shouldCastDoubleFromLong() {
        var expr = "fun expr(a Long) -> Double { ret a as Double }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 123L)
                .isEqualTo((double) 123L);
    }

    @Test
    public void shouldCastFloatFromLong() {
        var expr = "fun expr(a Long) -> Float { ret a as Float }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 123L)
                .isEqualTo((float) 123L);
    }

    @Test
    public void shouldCastIntFromLong() {
        var expr = "fun expr(a Long) -> Int { ret a as Int }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 123L)
                .isEqualTo((int) 123L);
    }

    @Test
    public void shouldBandTwoLongs() {
        var expr = "fun expr(a Long, b Long) -> Long { ret a band b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 123L, 100L)
                .isEqualTo(96L);
    }

    @Test
    public void shouldIncrementLong_1() {
        var expr = "fun expr(a Long) -> Int { ret (++a) as Int }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 123L)
                .isEqualTo((int) 124L);
    }

    @Test
    public void shouldIncrementLong_2() {
        var expr = "fun expr(a Long) -> Long { ret (++a) }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 123L)
                .isEqualTo(124L);
    }

    @Test
    public void shouldIncrementLong_3() {
        var expr = "fun expr(a Long) -> Long { ret ++a }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 123L)
                .isEqualTo(124L);
    }

    @Test
    public void shouldXorLongs_1() {
        var expr = "fun expr(a Long, b Long) -> Long { ret a xor b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12L, 11L)
                .isEqualTo(7L);
    }

    @Test
    public void shouldXorLongs_2() {
        var expr = "fun expr(a Long, b Int) -> Long { ret a xor b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12L, 11)
                .isEqualTo(7L);
    }

    @Test
    public void shouldBitwiseOrLongs_1() {
        var expr = "fun expr(a Long, b Long) -> Long { ret a bor b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12L, 11L)
                .isEqualTo(8L);
    }

    @Test
    public void shouldBitwiseOrLongs_2() {
        var expr = "fun expr(a Long, b Int) -> Long { ret a bor b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12L, 11)
                .isEqualTo(8L);
    }
}
