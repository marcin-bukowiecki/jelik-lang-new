package org.jelik.compiler.integration;

import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionDoubleTest {

    @Test
    public void doubleTest_1() {
        var expr = "fun expr(a Double, b Double) -> Double {" +
                "   ret a + b" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr",2.5, 7.3)
                .isEqualTo(9.8);
    }

    @Test
    public void doubleTest_2() {
        var expr = "fun expr(a Double, b Double) -> Double {" +
                "   ret a / b" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr",9.0, 3.0)
                .isEqualTo(3.0);
    }

    @Test
    public void doubleTest_3() {
        var expr = "fun expr(a Double, b Double) -> Double {" +
                "   ret a * b" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr",9.0, 3.0)
                .isEqualTo(27.0);
    }

    @Test
    public void doubleTest_4() {
        var expr = "fun expr(a Double, b Double) -> Double {" +
                "   ret a - b" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr",9.0, 3.0)
                .isEqualTo(6.0);
    }

    @Test
    public void doubleTest_5() {
        var expr = "fun expr(a Double, b Int) -> Double {" +
                "   ret a / b" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr",9.0, 3)
                .isEqualTo(3.0);
    }

    @Test
    public void doubleTest_6() {
        var expr = "fun expr(a Double, b Float) -> Double {" +
                "   ret a / b" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr",9.0, 3.0f)
                .isEqualTo(3.0);
    }

    @Test
    public void doubleTest_7() {
        var expr = "fun expr(a Double, b Float) -> Double {\n" +
                "   val c = 11 as Double\n" +
                "   ret a / b + c" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr",9.0, 3.0f)
                .isEqualTo(14.0);
    }

    @Test
    public void doubleTest_8() {
        var expr = "fun expr(a Double, b Float) -> Double {\n" +
                "   val c = 11 as Double\n" +
                "   ret a / b + (-c)" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr",9.0, 3.0f)
                .isEqualTo(-8.0);
    }

    @Test
    public void doubleTest_9() {
        var expr = "fun expr(a Double, b Double) -> Boolean {\n" +
                "   ret a == b" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr",9.0, 3.0)
                .isEqualTo(false);
    }

    @Test
    public void doubleTest_10() {
        var expr = "fun expr(a Double, b Double) -> Boolean {\n" +
                "   ret a == b and a == b" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr",9.0d, 3.0d)
                .isEqualTo(false);
    }

    @Test
    public void doubleTest_11() {
        var expr = "fun expr(a Double, b Double) -> Boolean {\n" +
                "   ret a != b" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr",9.0, 3.0f)
                .isEqualTo(true);
    }

    @Test
    public void doubleCastTest_1() {
        var expr = "fun expr(a Double, b Float) -> Float {" +
                "   ret a as Float / b" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr",9.0, 3.0f)
                .isEqualTo(3.0f);
    }

    @Test
    public void doubleCastTest_2() {
        var expr = "fun expr(a Double, b Int) -> Int {" +
                "   ret a as Int / b" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr",9.0, 3)
                .isEqualTo(3);
    }

    @Test
    public void doubleCastTest_3() {
        var expr = "fun expr(a Double, b Long) -> Long {" +
                "   ret a as Long / b" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr",9.0, 3L)
                .isEqualTo(3L);
    }
}
