package org.jelik.compiler.loops

import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class WhileLoopTest {

    @Test
    fun testListWhileLoop_1() {
        val expr = "import java.util.List\n" +
                "\n" +
                "fun expr(l List<Int>) -> Int {\n" +
                "   val it = l.iterator()\n" +
                "   while it.hasNext() do\n" +
                "       System.out.println(it.next())\n" +
                "   end\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
                .getInstance()
                .compile(expr)
                .invoke<Int>("expr", listOf(1,2,3,4,5))
    }

    @Test
    fun testListWhileLoop_2() {
        val expr = "import java.util.List\n" +
                "\n" +
                "fun expr(l List<Int>) -> Int {\n" +
                "   val it = l.iterator()\n" +
                "   while it.hasNext() and l.size() > 1 do\n" +
                "       System.out.println(it.next())\n" +
                "   end\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
                .getInstance()
                .compile(expr)
                .invoke<Int>("expr", listOf(1,2,3,4,5))
    }

    @Test
    fun testBreakWhileLoop_1() {
        val expr = "import java.util.List\n" +
                "\n" +
                "fun expr(l List<Int>) -> Int {\n" +
                "   val it = l.iterator()\n" +
                "   while it.hasNext() and l.size() > 1 do\n" +
                "       System.out.println(it.next())\n" +
                "       break" +
                "   end\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
            .getInstance()
            .compile(expr)
            .invoke<Int>("expr", listOf(1,2,3,4,5))
    }

    @Test
    fun testContinueWhileLoop_1() {
        val expr = "import java.util.List\n" +
                "\n" +
                "fun expr(l List<Int>) -> Int {\n" +
                "   val it = l.iterator()\n" +
                "   while it.hasNext() and l.size() > 1 do\n" +
                "       System.out.println(it.next())\n" +
                "       continue" +
                "   end\n" +
                "   ret 1" +
                "}"
        FunctionCompiler
            .getInstance()
            .compile(expr)
            .invoke<Int>("expr", listOf(1,2,3,4,5))
    }
}
