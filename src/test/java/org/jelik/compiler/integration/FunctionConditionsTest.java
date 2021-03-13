package org.jelik.compiler.integration;

import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionConditionsTest {

    @Test
    public void returnBooleanFromSimpleEqualExpr() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a == b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 12)
                .isEqualTo(true);
    }

    @Test
    public void returnBooleanFromSimpleNotEqualExpr() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a != b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 12)
                .isEqualTo(false);
    }

    @Test
    public void intsGreaterExpr() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a > b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 1)
                .isEqualTo(true);
    }

    @Test
    public void intsNotEqual_1() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a != b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 1)
                .isEqualTo(true);
    }

    @Test
    public void intsNotEqual_2() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a != b and a != 100 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 1)
                .isEqualTo(true);
    }

    @Test
    public void intsNotEqual_3() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a != b or a != 100 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 1)
                .isEqualTo(true);
    }

    @Test
    public void intCompareToZero_1() {
        var expr = "fun expr(a Int) -> Boolean { ret a < 0 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", -1)
                .isEqualTo(true);
    }

    @Test
    public void intsLesserExpr() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a < b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 1)
                .isEqualTo(false);
    }

    @Test
    public void floatCompareToZero_1() {
        var expr = "fun expr(a Float) -> Boolean { ret a < 0 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12)
                .isEqualTo(false);
    }

    @Test
    public void floatCompareToZero_2() {
        var expr = "fun expr(a Float) -> Boolean { ret a < 0 and a < 123 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12)
                .isEqualTo(false);
    }

    @Test
    public void floatCompareToZero_3() {
        var expr = "fun expr(a Float) -> Boolean { ret a < 0 or a < 123 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12)
                .isEqualTo(true);
    }

    @Test
    public void longCompare_1() {
        var expr = "fun expr(a Long, b Long) -> Boolean { ret a == b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 567L, 567L)
                .isEqualTo(true);
    }

    @Test
    public void longCompare_2() {
        var expr = "fun expr(a Long, b Long) -> Boolean { ret a == b and a == b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 567L, 567L)
                .isEqualTo(true);
    }

    @Test
    public void longCompare_3() {
        var expr = "fun expr(a Long, b Long) -> Boolean { ret a == b or a == b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 567L, 567L)
                .isEqualTo(true);
    }

    @Test
    public void doubleCompare_1() {
        var expr = "fun expr(a Double, b Double) -> Boolean { ret a == b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 567.0, 567.0)
                .isEqualTo(true);
    }

    @Test
    public void returnBooleanFromSimpleOrExpr() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a == b or a == 100 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 12)
                .isEqualTo(true);
    }

    @Test
    public void checkStringNull() {
        var expr = "fun expr(a String) -> Boolean { ret a == Null }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", (String) null)
                .isEqualTo(true);
    }

    @Test
    public void checkStringNonNull() {
        var expr = "fun expr(a String) -> Boolean { ret Null != a }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", (String) null)
                .isEqualTo(false);
    }

    @Test
    public void objectsEqual_1() {
        var expr = "fun expr(a Object, b Object) -> Boolean { ret a == b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "bar")
                .isEqualTo(false);
    }

    @Test
    public void objectsEqual_2() {
        var expr = "fun expr(a Object, b Object, c Object) -> Boolean { ret a == b or a == c }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr","foo", "bar", "foo")
                .isEqualTo(true);
    }

    @Test
    public void objectsEqual_3() {
        var expr = "fun expr(a Object, b Object) -> Boolean { ret a.equals(b) }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "bar")
                .isEqualTo(false);
    }

    @Test
    public void objectsEqual_4() {
        var expr = "fun expr(a Object, b Object) -> Boolean { ret a.equals(b) }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "foo")
                .isEqualTo(true);
    }

    @Test
    public void objectsEqual_5() {
        var expr = "fun expr(a Object, b Object) -> Boolean { \n " +
                "if a == b and a == \"foo\" then \n" +
                "   ret True \n" +
                "else " +
                "   ret False\n" +
                "end" +
                " }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "foo")
                .isEqualTo(true);
    }

    @Test
    public void objectsEqual_6() {
        var expr = "fun expr(a Object, b Object) -> Boolean { \n " +
                "if a.equals(b) == True then \n" +
                "   ret True \n" +
                "else " +
                "   ret False\n" +
                "end" +
                " }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "foo")
                .isEqualTo(true);
    }

    @Test
    public void objectsEqual_7() {
        var expr = "fun expr(a Object, b Object) -> Boolean { \n " +
                "if a.equals(b) then \n" +
                "   ret True \n" +
                "else " +
                "   ret False\n" +
                "end" +
                " }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "foo")
                .isEqualTo(true);
    }
}
