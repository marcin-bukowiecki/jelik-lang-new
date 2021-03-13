package org.jelik.compiler.integration;

import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionInt32Test {

    @Test
    public void shouldNegInt() {
        var expr = "fun expr(a Int) -> Int { val b = 10 \n ret -a * b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 123)
                .isEqualTo(-1230);
    }

    @Test
    public void shouldSubInts_1() {
        var expr = "fun expr(a Int) -> Int { val b = 10 \n ret a - b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 123)
                .isEqualTo(113);
    }

    @Test
    public void shouldSubInts_2() {
        var expr = "fun expr(a Int) -> Int { val b = 10 \n ret Integer.valueOf(a) - b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 123)
                .isEqualTo(113);
    }

    @Test
    public void shouldSubInts_3() {
        var expr = "fun expr(a Int) -> Int { val b = 10 \n ret Integer.valueOf(a) - Integer.valueOf(b) }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 123)
                .isEqualTo(113);
    }

    @Test
    public void shouldSubInts_4() {
        var expr = "fun expr(a Int) -> Int { val b = 10 \n ret a - Integer.valueOf(b) }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 123)
                .isEqualTo(113);
    }

    @Test
    public void shouldRemInts() {
        var expr = "fun expr(a Int) -> Int { val b = 10 \n ret a % b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 1230)
                .isEqualTo(0);
    }

    @Test
    public void shouldSubIntAndChar() {
        var expr = "fun expr(a Char) -> Int { val b = 10 \n ret a - b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", (char) 123)
                .isEqualTo(113);
    }

    @Test
    public void shouldReturnIntFromChar() {
        var expr = "fun expr(a Int) -> Char { ret a as Char }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12)
                .isEqualTo((char) 12);
    }

    @Test
    public void shouldReturnIntFromLong() {
        var expr = "fun expr(a Int) -> Long { ret a as Long }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12)
                .isEqualTo(12L);
    }

    @Test
    public void shouldReturnIntFromShort() {
        var expr = "fun expr(a Int) -> Short { ret a as Short }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12)
                .isEqualTo((short) 12);
    }

    @Test
    public void shouldBitwiseAndInts() {
        var expr = "fun expr(a Int) -> Short { val b = 10 \n ret (a band b) as Short }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12)
                .isEqualTo((short) 8);
    }

    @Test
    public void shouldBitwiseOrInts() {
        var expr = "fun expr(a Int) -> Short { val b = 10 \n ret (a bor b) as Short }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12)
                .isEqualTo((short) 14);
    }

    @Test
    public void shouldReturnDoubleFromInt() {
        var expr = "fun expr(a Int) -> Double { ret a as Double }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12)
                .isEqualTo((double) 12);
    }

    @Test
    public void shouldCompareInts_1() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a >= b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 11)
                .isEqualTo(true);
    }

    @Test
    public void shouldCompareInts_2() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a <= b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 11)
                .isEqualTo(false);
    }

    @Test
    public void shouldCompareInts_3() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a >= b and a >= b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 11)
                .isEqualTo(true);
    }

    @Test
    public void shouldCompareInts_4() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a > b and a > b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 11)
                .isEqualTo(true);
    }

    @Test
    public void shouldCompareInts_5() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a > b or a > b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 11)
                .isEqualTo(true);
    }

    @Test
    public void shouldCompareInts_6() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a < b and a < b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 11)
                .isEqualTo(false);
    }

    @Test
    public void shouldCompareInts_7() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a < b or a < b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 11)
                .isEqualTo(false);
    }

    @Test
    public void shouldCompareInts_8() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a < 0 or b < 0 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 11)
                .isEqualTo(false);
    }

    @Test
    public void shouldCompareInts_9() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a < 0 and b < 0 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 11)
                .isEqualTo(false);
    }

    @Test
    public void shouldCompareInts_10() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a > 0 or b > 0 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 11)
                .isEqualTo(true);
    }

    @Test
    public void shouldCompareInts_11() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a > 0 and b > 0 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 11)
                .isEqualTo(true);
    }

    @Test
    public void shouldCompareInts_12() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a >= 0 or b >= 0 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 11)
                .isEqualTo(true);
    }

    @Test
    public void shouldCompareInts_13() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a >= 0 and b >= 0 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 11)
                .isEqualTo(true);
    }

    @Test
    public void shouldXorInts_1() {
        var expr = "fun expr(a Int, b Int) -> Int { ret a xor b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 11)
                .isEqualTo(7);
    }

    @Test
    public void shouldXorInts_2() {
        var expr = "fun expr(a Int, b Int) -> Int { ret a as Short xor b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 11)
                .isEqualTo(7);
    }
}
