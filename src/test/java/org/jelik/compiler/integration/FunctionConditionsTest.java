package org.jelik.compiler.integration;

import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionConditionsTest {

    @Test
    public void testReturnBooleanFromSimpleEqualExpr_1() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a == b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 12)
                .isEqualTo(true);
    }

    @Test
    public void testReturnBooleanFromSimpleEqualExpr_2() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret if a == b then true else false end }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 12)
                .isEqualTo(true);
    }

    @Test
    public void testReturnBooleanFromSimpleEqualExpr_3() {
        var expr = "fun expr(a Int, b Int) -> Boolean { var a = if a == b then true else false end \n ret a }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 12)
                .isEqualTo(true);
    }

    @Test
    public void testReturnBooleanFromSimpleNotEqualExpr() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a != b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 12)
                .isEqualTo(false);
    }

    @Test
    public void testIntsGreaterExpr() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a > b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 1)
                .isEqualTo(true);
    }

    @Test
    public void testIntsNotEqual_1() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a != b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 1)
                .isEqualTo(true);
    }

    @Test
    public void testIntsNotEqual_2() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a != b and a != 100 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 1)
                .isEqualTo(true);
    }

    @Test
    public void testIntsNotEqual_3() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a != b or a != 100 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 1)
                .isEqualTo(true);
    }

    @Test
    public void testIntCompareToZero_1() {
        var expr = "fun expr(a Int) -> Boolean { ret a < 0 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", -1)
                .isEqualTo(true);
    }

    @Test
    public void testIntsLesserExpr() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a < b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 1)
                .isEqualTo(false);
    }

    @Test
    public void testFloatCompareToZero_1() {
        var expr = "fun expr(a Float) -> Boolean { ret a < 0 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12)
                .isEqualTo(false);
    }

    @Test
    public void testFloatCompareToZero_2() {
        var expr = "fun expr(a Float) -> Boolean { ret a < 0 and a < 123 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12)
                .isEqualTo(false);
    }

    @Test
    public void testFloatCompareToZero_3() {
        var expr = "fun expr(a Float) -> Boolean { ret a < 0 or a < 123 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12)
                .isEqualTo(true);
    }

    @Test
    public void testLongCompare_1() {
        var expr = "fun expr(a Long, b Long) -> Boolean { ret a == b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 567L, 567L)
                .isEqualTo(true);
    }

    @Test
    public void testLongCompare_2() {
        var expr = "fun expr(a Long, b Long) -> Boolean { ret a == b and a == b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 567L, 567L)
                .isEqualTo(true);
    }

    @Test
    public void testLongCompare_3() {
        var expr = "fun expr(a Long, b Long) -> Boolean { ret a == b or a == b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 567L, 567L)
                .isEqualTo(true);
    }

    @Test
    public void testDoubleCompare_1() {
        var expr = "fun expr(a Double, b Double) -> Boolean { ret a == b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 567.0, 567.0)
                .isEqualTo(true);
    }

    @Test
    public void testReturnBooleanFromSimpleOrExpr() {
        var expr = "fun expr(a Int, b Int) -> Boolean { ret a == b or a == 100 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", 12, 12)
                .isEqualTo(true);
    }

    @Test
    public void testCheckStringNull() {
        var expr = "fun expr(a String) -> Boolean { ret a == Null }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", (String) null)
                .isEqualTo(true);
    }

    @Test
    public void testCheckStringNonNull() {
        var expr = "fun expr(a String) -> Boolean { ret Null != a }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", (String) null)
                .isEqualTo(false);
    }

    @Test
    public void testObjectsEqual_1() {
        var expr = "fun expr(a Object, b Object) -> Boolean { ret a == b }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "bar")
                .isEqualTo(false);
    }

    @Test
    public void testObjectsEqual_2() {
        var expr = "fun expr(a Object, b Object, c Object) -> Boolean { ret a == b or a == c }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr","foo", "bar", "foo")
                .isEqualTo(true);
    }

    @Test
    public void testObjectsEqual_3() {
        var expr = "fun expr(a Object, b Object) -> Boolean { ret a.equals(b) }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "bar")
                .isEqualTo(false);
    }

    @Test
    public void testObjectsEqual_4() {
        var expr = "fun expr(a Object, b Object) -> Boolean { ret a.equals(b) }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "foo")
                .isEqualTo(true);
    }

    @Test
    public void testObjectsEqual_5() {
        var expr = "fun expr(a Object, b Object) -> Boolean { \n " +
                "if a == b and a == \"foo\" then \n" +
                "   ret true \n" +
                "else " +
                "   ret false\n" +
                "end" +
                " }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "foo")
                .isEqualTo(true);
    }

    @Test
    public void testObjectsEqual_6() {
        var expr = "fun expr(a Object, b Object) -> Boolean { \n " +
                "if a.equals(b) == true then \n" +
                "   ret true \n" +
                "else " +
                "   ret false\n" +
                "end" +
                " }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "foo")
                .isEqualTo(true);
    }

    @Test
    public void testObjectsEqual_7() {
        var expr = "fun expr(a Object, b Object) -> Boolean { \n " +
                "if a.equals(b) then \n" +
                "   ret true \n" +
                "else " +
                "   ret false\n" +
                "end" +
                " }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "foo")
                .isEqualTo(true);
    }

    @Test
    public void testObjectsEqual_8() {
        var expr = "fun expr(a Object, b Object) -> Boolean { ret a.equals(b) and b.equals(a) }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "foo")
                .isEqualTo(true);
    }

    @Test
    public void testObjectsEqual_9() {
        var expr = "fun expr(a Object, b Object) -> Boolean { ret a.equals(b) or b.equals(a) }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "foo")
                .isEqualTo(true);
    }

    @Test
    public void testObjectsEqual_10() {
        var expr = "fun expr(a Object, b Object) -> Boolean { ret a.equals(a) and b.equals(a) }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "bar")
                .isEqualTo(false);
    }

    @Test
    public void testObjectsEqual_11() {
        var expr = "fun expr(a Object, b Object) -> Boolean { ret a.equals(b) or b.equals(a) }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "bar")
                .isEqualTo(false);
    }

    @Test
    public void testObjectsEqual_12() {
        var expr = "fun expr(a Object, b Object) -> Boolean { ret a.equals(b) == b.equals(a) }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "foo")
                .isEqualTo(true);
    }

    @Test
    public void testObjectsEqual_13() {
        var expr = "fun expr(a Object, b Object) -> Boolean { ret a.equals(b) == b.equals(a) }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "bar")
                .isEqualTo(true);
    }

    @Test
    public void testObjectsEqual_14() {
        var expr = "fun expr(a Object, b Object) -> Boolean { ret a.equals(b) != b.equals(b) }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "bar")
                .isEqualTo(true);
    }

    @Test
    public void testObjectsEqual_15() {
        var expr = "fun expr(a Object, b Object) -> Boolean { ret a.equals(b) != b.equals(a) }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "foo")
                .isEqualTo(false);
    }

    @Test
    public void testObjectsEqual_16() {
        var expr = "fun expr(a Object, b Object) -> Boolean { ret a.equals(b) and 2 > 1 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", "foo", "foo")
                .isEqualTo(true);
    }

    @Test
    public void testCollectionSizeTest_1() {
        var expr = "import java.util.List\n" +
                "fun expr(a List<Int>) -> Boolean { ret a.size() > 1 }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", Arrays.asList(1,2,3))
                .isEqualTo(true);
    }

    @Test
    public void testCollectionSizeTest_2() {
        var expr = "import java.util.Iterator\n" +
                "fun expr(a Iterator<Int>) -> Boolean { ret a.hasNext() }";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", Arrays.asList(1,2,3).iterator())
                .isEqualTo(true);
    }

    @Test
    public void testCollectionSizeTest_3() {
        var expr = "import java.util.Iterator\n" +
                "import java.util.List\n" +
                "fun expr(a Iterator<Int>, b List<Int>) -> Boolean { ret a.hasNext() and b.size() > 1}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr", Arrays.asList(1,2,3).iterator(), Arrays.asList(1,2,3))
                .isEqualTo(true);
    }
}
