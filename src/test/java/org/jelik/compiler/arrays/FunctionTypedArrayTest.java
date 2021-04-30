package org.jelik.compiler.arrays;

import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionTypedArrayTest {

    @Test
    public void createIntArray_1() {
        var expr = "fun expr() -> []Int {" +
                "   ret [1,2,3,4]Int" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new int[]{1,2,3,4});
    }

    @Test
    public void createIntArray_2() {
        var expr = "fun expr() -> []Int {" +
                "   ret [1]Int" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new int[]{1});
    }

    @Test
    public void createIntArray_3() {
        var expr = "fun expr() -> []Int {" +
                "   ret []Int" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new int[]{});
    }

    @Test
    public void createIntArray_4() {
        var expr = "fun expr() -> []Int {" +
                "   ret [1,]Int" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new int[]{1});
    }

    @Test
    public void createFloatArray_1() {
        var expr = "fun expr() -> []Float {" +
                "   ret [1,2,3,4]Float" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new float[]{1,2,3,4});
    }

    @Test
    public void createFloatArray_2() {
        var expr = "fun expr() -> []Float {" +
                "   ret [1]Float" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new float[]{1});
    }

    @Test
    public void createFloatArray_3() {
        var expr = "fun expr() -> []Float {" +
                "   ret []Float" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new float[]{});
    }

    @Test
    public void createDoubleArray_1() {
        var expr = "fun expr() -> []Double {" +
                "   ret [1,2,3,4]Double" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new double[]{1,2,3,4});
    }

    @Test
    public void createDoubleArray_2() {
        var expr = "fun expr() -> []Double {" +
                "   ret [1]Double" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new double[]{1});
    }

    @Test
    public void createDoubleArray_3() {
        var expr = "fun expr() -> []Double {" +
                "   ret []Double" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new double[]{});
    }

    @Test
    public void createByteArray_1() {
        var expr = "fun expr() -> []Byte {" +
                "   ret [1,2,3,4]Byte" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new byte[]{1,2,3,4});
    }

    @Test
    public void createByteArray_2() {
        var expr = "fun expr() -> []Byte {" +
                "   ret [1]Byte" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new byte[]{1});
    }

    @Test
    public void createByteArray_3() {
        var expr = "fun expr() -> []Byte {" +
                "   ret []Byte" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new byte[]{});
    }

    @Test
    public void createShortArray_1() {
        var expr = "fun expr() -> []Short {" +
                "   ret [1,2,3,4]Short" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new short[]{1,2,3,4});
    }

    @Test
    public void createShortArray_2() {
        var expr = "fun expr() -> []Short {" +
                "   ret [1]Short" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new short[]{1});
    }

    @Test
    public void createShortArray_3() {
        var expr = "fun expr() -> []Short {" +
                "   ret []Short" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new short[]{});
    }

    @Test
    public void createLongArray_1() {
        var expr = "fun expr() -> []Long {" +
                "   ret [1,2,3,4]Long" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new long[]{1,2,3,4});
    }

    @Test
    public void createLongArray_2() {
        var expr = "fun expr() -> []Long {" +
                "   ret [1]Long" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new long[]{1});
    }

    @Test
    public void createLongArray_3() {
        var expr = "fun expr() -> []Long {" +
                "   ret []Long" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new long[]{});
    }

    @Test
    public void createLongArray_4() {
        var expr = "fun expr() -> Int {" +
                "   ret len([]Long)" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(0);
    }

    @Test
    public void createStringArray_1() {
        var expr = "fun expr() -> Int {" +
                "   ret len([]String)" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(0);
    }

    @Test
    public void createStringArray_2() {
        var expr = "fun expr() -> []String {" +
                "   ret [\"foo\",\"bar\"]String)" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new String[] { "foo", "bar" });
    }

    @Test
    public void createStringArray_3() {
        var expr = "fun expr() -> []String {" +
                "   ret [\"foo\",]String)" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new String[] { "foo" });
    }

    @Test
    public void createObjectArray_1() {
        var expr = "import java.util.ArrayList\n" +
                "fun expr() -> []ArrayList<Int> {" +
                "   ret [ArrayList<Int>()]ArrayList<Int>)" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new ArrayList[] {new ArrayList<Integer>()});
    }

    @Test
    public void createObjectArray_2() {
        var expr = "import java.util.ArrayList\n" +
                "fun expr() -> Int {" +
                "   ret len([ArrayList<Int>()]ArrayList<Int>))" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(1);
    }

    @Test
    public void createObjectArray_3() {
        var expr = "import java.util.ArrayList\n" +
                "fun expr() -> Int {" +
                "   ret len([]ArrayList<Int>))" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(0);
    }

    @Test
    public void createObjectArray_4() {
        var expr = "import java.util.ArrayList\n" +
                "fun expr() -> Int {" +
                "   ret len([]ArrayList<Int>) + 2" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(2);
    }
}
