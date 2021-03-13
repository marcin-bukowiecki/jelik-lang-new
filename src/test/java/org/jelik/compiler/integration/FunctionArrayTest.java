package org.jelik.compiler.integration;

import org.jelik.compiler.utils.FunctionCompiler;
import org.junit.Test;

/**
 * @author Marcin Bukowiecki
 */
public class FunctionArrayTest {

    @Test
    public void createIntArray() {
        var expr = "fun expr() -> []Int {" +
                "   ret [1,2,3,4]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new int[]{1,2,3,4});
    }

    @Test
    public void createTypedFloatArray_1() {
        var expr = "fun expr() -> []Float {" +
                "   ret [1f,2f,3f,4f]:Float" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new float[]{1,2,3,4});
    }

    @Test
    public void getIntArrayLength() {
        var expr = "fun expr() -> Int {" +
                "   ret len([1,2,3,4])" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(4);
    }

    @Test
    public void getIntFromArray() {
        var expr = "fun expr() -> Int {" +
                "   ret [1,2,3,4][3]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(4);
    }

    @Test
    public void getIntFromArrayVar() {
        var expr = "fun expr() -> Int {\n" +
                "   val a = [1,2,3,4]" +
                "   ret a[3]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(4);
    }

    @Test
    public void getShortFromArray() {
        var expr = "fun expr() -> Short {" +
                "   ret [3 as Short,4 as Short][1]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo((short) 4);
    }

    @Test
    public void createStringArray() {
        var expr = "fun expr() -> []String {" +
                "   ret [\"foo\",\"bar\"]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new String[]{"foo","bar"});
    }

    @Test
    public void getStringFromArray() {
        var expr = "fun expr() -> String {" +
                "   ret [\"foo\",\"bar\"][0]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo("foo");
    }

    @Test
    public void getStringFromArrayVar() {
        var expr = "fun expr() -> String {\n" +
                "   val a = [\"a\",\"b\",\"c\"]" +
                "   ret a[2]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo("c");
    }

    @Test
    public void storeStringInArrayVar() {
        var expr = "fun expr() -> String {\n" +
                "   val a = [\"a\",\"b\",\"c\"]\n" +
                "   a[2] = \"foo\"\n" +
                "   ret a[2]\n" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo("foo");
    }

    @Test
    public void createByteArray() {
        var expr = "fun expr() -> []Byte {" +
                "   ret [1 as Byte, 2 as Byte, 3 as Byte]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new byte[]{1,2,3});
    }

    @Test
    public void getByteArrayElement() {
        var expr = "fun expr() -> Byte {" +
                "   ret [1 as Byte, 2 as Byte, 3 as Byte][0]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo((byte) 1);
    }

    @Test
    public void storeByteElementInArray() {
        var expr = "fun expr() -> Byte {" +
                "   val a = [1 as Byte, 2 as Byte, 3 as Byte] \n a[2] = 6 as Byte \n ret a[2]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo((byte) 6);
    }

    @Test
    public void createCharArray() {
        var expr = "fun expr() -> []Char {" +
                "   ret [1 as Char, 2 as Char, 3 as Char]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new char[]{1,2,3});
    }

    @Test
    public void getCharArrayElement() {
        var expr = "fun expr() -> Char {" +
                "   ret [1 as Char, 2 as Char, 3 as Char][0]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo((char) 1);
    }

    @Test
    public void storeCharElementInArray() {
        var expr = "fun expr() -> Char {" +
                "   val a = [1 as Char, 2 as Char, 3 as Char] \n a[2] = 6 as Char \n ret a[2]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo((char) 6);
    }

    @Test
    public void createDoubleArray() {
        var expr = "fun expr() -> []Double {" +
                "   ret [1 as Double, 2 as Double, 3 as Double]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(new double[]{1,2,3});
    }

    @Test
    public void getDoubleArrayElement() {
        var expr = "fun expr() -> Double {" +
                "   ret [1 as Double, 2 as Double, 3 as Double][0]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(1d);
    }

    @Test
    public void storeDoubleElementInArray() {
        var expr = "fun expr() -> Double {" +
                "   val a = [1 as Double, 2 as Double, 3 as Double] \n a[2] = 6 as Double \n ret a[2]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo(6d);
    }

    @Test
    public void getLongFromArray() {
        var expr = "fun expr() -> Long {" +
                "   ret [3 as Long,4 as Long][1]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo((long) 4);
    }

    @Test
    public void storeLongElementInArray() {
        var expr = "fun expr() -> Long {" +
                "   val a = [1 as Long, 2 as Long, 3 as Long] \n a[2] = 6 as Long \n ret a[2]" +
                "}";
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke("expr")
                .isEqualTo((long) 6);
    }
}
