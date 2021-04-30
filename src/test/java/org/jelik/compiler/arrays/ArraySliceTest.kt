package org.jelik.compiler.arrays

import org.jelik.compiler.utils.FunctionCompiler
import org.jelik.utils.DependencyLoader.loadDefaultDependency
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class ArraySliceTest {

    @Test
    fun byteArraySlice_1() {
        loadDefaultDependency("./sdk/jelik/lang/JelikUtils.jlk")
        val expr = "fun expr() -> []Byte { \n" +
                "var foo = [1,2,3,4,5]Byte\n" +
                "ret foo[1:3] \n" +
                "}"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<ByteArray>("expr")
                .isEqualTo(byteArrayOf(2,3))
    }

    @Test
    fun charArraySlice_1() {
        loadDefaultDependency("./sdk/jelik/lang/JelikUtils.jlk")
        val expr = "fun expr() -> []Char { \n" +
                "var foo = ['a','b','c']Char\n" +
                "ret foo[0:2] \n" +
                "}"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<ByteArray>("expr")
                .isEqualTo(charArrayOf('a', 'b'))
    }

    @Test
    fun shortArraySlice_1() {
        loadDefaultDependency("./sdk/jelik/lang/JelikUtils.jlk")
        val expr = "fun expr() -> []Short { \n" +
                "var foo = [1,2,3,4,5]Short\n" +
                "ret foo[1:3] \n" +
                "}"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<ByteArray>("expr")
                .isEqualTo(shortArrayOf(2,3))
    }

    @Test
    fun intArraySlice_1() {
        loadDefaultDependency("./sdk/jelik/lang/JelikUtils.jlk")
        val expr = "fun expr() -> []Int { ret [1,2,3,4,5][1:3] }"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<IntArray>("expr")
                .isEqualTo(intArrayOf(2,3))
    }

    @Test
    fun intArraySlice_2() {
        loadDefaultDependency("./sdk/jelik/lang/JelikUtils.jlk")
        val expr = "fun expr() -> []Int { ret [1,2,3,4][0:3] }"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<IntArray>("expr")
                .isEqualTo(intArrayOf(1,2,3))
    }

    @Test
    fun objectArraySlice_1() {
        val expr = "fun calculateArrayIndex(array []Object, index Int) -> Int {\n" +
                "    val arraySize = len(array)\n" +
                "    if index < 0 then\n" +
                "        ret arraySize + index\n" +
                "    end\n" +
                "    ret index\n" +
                "}\n" +
                "\n" +
                "fun arraySlice(array []Object, start Int, stop Int) -> []Object {\n" +
                "    val newStart = calculateArrayIndex(array, start)\n" +
                "    val newStop = calculateArrayIndex(array, stop)\n" +
                "    val result = []Object(newStop - newStart)\n" +
                "    System.arraycopy(array, newStart, result, 0, newStop - newStart)\n" +
                "    ret result\n" +
                "}\n"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Array<String>>("arraySlice", arrayOf("foo", "bar", "foo", "bar"), 1, 2)
                .isEqualTo(arrayOf("bar"))
    }

    @Test
    fun objectArraySlice_2() {
        loadDefaultDependency("./sdk/jelik/lang/JelikUtils.jlk")
        val expr = "fun expr() -> []String { ret [\"foo\", \"bar\"][0:1] }"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Array<String>>("expr")
                .isEqualTo(arrayOf("foo"))
    }

    @Test
    fun objectArraySlice_3() {
        loadDefaultDependency("./sdk/jelik/lang/JelikUtils.jlk")
        val expr = "fun expr() -> []Object { ret [\"foo\", \"bar\"][0:1] }"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Array<String>>("expr")
                .isEqualTo(arrayOf("foo"))
    }

    @Test
    fun objectArraySlice_4() {
        loadDefaultDependency("./sdk/jelik/lang/JelikUtils.jlk")
        val expr = "fun expr() -> []Object { ret [\"foo\", \"bar\"]Object[0:1] }"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Array<String>>("expr")
                .isEqualTo(arrayOf("foo"))
    }
}


