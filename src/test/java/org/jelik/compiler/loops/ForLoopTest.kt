package org.jelik.compiler.loops

import org.assertj.core.api.Assertions
import org.jelik.compiler.utils.FunctionCompiler
import org.jelik.utils.DependencyLoader
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class ForLoopTest {

    @Test
    fun listForEach_1() {
        val expr = "import java.util.List\n" +
                "\n" +
                "fun expr(l List<Int>) -> Int {\n" +
                "   for e in l {\n" +
                "       System.out.println(e)\n" +
                "   }\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
                .getInstance()
                .compile(expr)
                .invoke<Int>("expr", listOf(1,2,3,4,5))
    }

    @Test
    fun listForEach_2() {
        val expr = "import java.util.List\n" +
                "\n" +
                "fun expr(l List<Int>) -> Int {\n" +
                "   for e in l {\n" +
                "       System.out.println(e)\n" +
                "   }\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
                .getInstance()
                .compile(expr)
                .invoke<Int>("expr", emptyList<Int>())
    }

    @Test
    fun arrayListForEach_1() {
        val expr = "import java.util.ArrayList\n" +
                "\n" +
                "fun expr(l ArrayList<Int>) -> Int {\n" +
                "   for e in l {\n" +
                "       System.out.println(e)\n" +
                "   }\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
                .getInstance()
                .compile(expr)
                .invoke<Int>("expr", arrayListOf(1,2,3,4,5))
    }

    @Test
    fun arrayListForEach_2() {
        val expr = "import java.util.ArrayList\n" +
                "\n" +
                "fun expr(l ArrayList<Int>) -> Int {\n" +
                "   for e in l {\n" +
                "       System.out.println(e)\n" +
                "   }\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
                .getInstance()
                .compile(expr)
                .invoke<Int>("expr", arrayListOf<Int>())
    }

    @Test
    fun arrayListForEach_3() {
        val expr = "import java.util.ArrayList\n" +
                "\n" +
                "fun expr(l ArrayList<Int>) -> Int {\n" +
                "   for e in l {\n" +
                "   }\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
                .getInstance()
                .compile(expr)
                .invoke<Int>("expr", arrayListOf(1,2,3,4,5))
    }

    @Test
    fun arrayListForEach_4() {
        val expr = "import java.util.ArrayList\n" +
                "\n" +
                "fun expr(l ArrayList<Int>) -> Int {\n" +
                "   for e in l {\n" +
                "       ret e" +
                "   }\n" +
                "   ret 1" +
                "}"
        Assertions.assertThatThrownBy {
            FunctionCompiler
                    .getInstance()
                    .compile(expr)
        }.hasMessage("Unexpected return statement in loop body")
    }

    @Test
    fun arrayListForEach_5() {
        val expr = "import java.util.ArrayList\n" +
                "\n" +
                "fun expr(l ArrayList<Int>) -> Int {\n" +
                "   for e in {\n" +
                "       System.out.println(1)" +
                "   }\n" +
                "   ret 1" +
                "}"
        Assertions.assertThatThrownBy {
            FunctionCompiler
                    .getInstance()
                    .compile(expr)
        }.hasMessage("Unexpected token")
    }

    @Test
    fun arrayListForEach_6() {
        val expr = "import java.util.ArrayList\n" +
                "\n" +
                "fun expr(l ArrayList<Int>) -> Int {\n" +
                "   for in l {\n" +
                "       System.out.println(1)" +
                "   }\n" +
                "   ret 1" +
                "}"
        Assertions.assertThatThrownBy {
            FunctionCompiler
                    .getInstance()
                    .compile(expr)
        }.hasMessage("Expected left expression for in operator")
    }

    @Test
    fun arrayListForEach_7() {
        val expr = "import java.util.ArrayList\n" +
                "\n" +
                "fun expr(l ArrayList<Int>) -> Int {\n" +
                "   for {\n" +
                "       System.out.println(1)" +
                "   }\n" +
                "   ret 1" +
                "}"
        Assertions.assertThatThrownBy {
            FunctionCompiler
                    .getInstance()
                    .compile(expr)
        }.hasMessage("Unexpected token")
    }

    @Test
    fun arrayListForEach_8() {
        val expr = "" +
                "import java.util.ArrayList\n" +
                "import java.util.stream.Collectors\n" +
                "\n" +
                "fun expr(l ArrayList<Int>) -> Int {\n" +
                "   for e in l.stream().collect(Collectors.toList()) {\n" +
                "       System.out.println(e)\n" +
                "   }\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
                .getInstance()
                .compile(expr)
                .invoke<Int>("expr", arrayListOf<Int>())
    }

    @Test
    fun intPrimitiveArrayForEach_2() {
        DependencyLoader.loadDefaultDependency("./sdk/jelik/lang/JelikUtils.jlk")
        val expr = "" +
                "\n" +
                "fun expr(l []Int) -> Int {\n" +
                "   for e in l {\n" +
                "       println(e)\n" +
                "   }\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
            .getInstance()
            .compile(expr)
            .invoke<Int>("expr", intArrayOf(1,2,3,4,5))
    }

    @Test
    fun intPrimitiveArrayForEach_1() {
        DependencyLoader.loadDefaultDependency("./sdk/jelik/lang/JelikUtils.jlk")
        val expr = "" +
                "\n" +
                "fun expr(l []Int) -> Int {\n" +
                "   for e in l {\n" +
                "       println(e)\n" +
                "   }\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
            .getInstance()
            .compile(expr)
            .invoke<Int>("expr", intArrayOf())
    }

    @Test
    fun longPrimitiveArrayForEach_1() {
        DependencyLoader.loadDefaultDependency("./sdk/jelik/lang/JelikUtils.jlk")
        val expr = "" +
                "\n" +
                "fun expr(l []Long) -> Int {\n" +
                "   for e in l {\n" +
                "       System.out.println(e)\n" +
                "   }\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
            .getInstance()
            .compile(expr)
            .invoke<Int>("expr", longArrayOf(1,2,3))
    }

    @Test
    fun longPrimitiveArrayForEach_2() {
        DependencyLoader.loadDefaultDependency("./sdk/jelik/lang/JelikUtils.jlk")
        val expr = "" +
                "\n" +
                "fun expr(l []Long) -> Int {\n" +
                "   for e in l {\n" +
                "       System.out.println(e)\n" +
                "   }\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
            .getInstance()
            .compile(expr)
            .invoke<Int>("expr", longArrayOf())
    }

    @Test
    fun longPrimitiveArrayForEach_3() {
        DependencyLoader.loadDefaultDependency("./sdk/jelik/lang/JelikUtils.jlk")
        val expr = "" +
                "\n" +
                "fun expr(l []Long) -> Int {\n" +
                "   for e in l {\n" +

                "   }\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
            .getInstance()
            .compile(expr)
            .invoke<Int>("expr", longArrayOf())
    }

    @Test
    fun stringArrayForEach_1() {
        DependencyLoader.loadDefaultDependency("./sdk/jelik/lang/JelikUtils.jlk")
        val expr = "" +
                "\n" +
                "fun expr(l []String) -> Int {\n" +
                "   for e in l {\n" +
                "       System.out.println(e)\n" +
                "   }\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
            .getInstance()
            .compile(expr)
            .invoke<Int>("expr", arrayOf("foo", "bar"))
    }

    @Test
    fun stringArrayForEach_2() {
        DependencyLoader.loadDefaultDependency("./sdk/jelik/lang/JelikUtils.jlk")
        val expr = "" +
                "\n" +
                "fun expr(l []String) -> Int {\n" +
                "   for e in l {\n" +
                "       System.out.println(e)\n" +
                "   }\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
            .getInstance()
            .compile(expr)
            .invoke<Int>("expr", arrayOf<String>())
    }

    @Test
    fun objectArrayForEach_1() {
        DependencyLoader.loadDefaultDependency("./sdk/jelik/lang/JelikUtils.jlk")
        val expr = "" +
                "\n" +
                "fun expr(l []Object) -> Int {\n" +
                "   for e in l {\n" +
                "       System.out.println(e)\n" +
                "   }\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
            .getInstance()
            .compile(expr)
            .invoke<Int>("expr", arrayOf("foo", "bar"))
    }

    @Test
    fun objectArrayForEach_3() {
        DependencyLoader.loadDefaultDependency("./sdk/jelik/lang/JelikUtils.jlk")
        val expr = "" +
                "\n" +
                "fun expr(l []Object) -> Int {\n" +
                "   for e in l {\n" +
                "   }\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
            .getInstance()
            .compile(expr)
            .invoke<Int>("expr", arrayOf("foo", "bar"))
    }
}
