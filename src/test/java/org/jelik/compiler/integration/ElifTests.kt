package org.jelik.compiler.integration

import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class ElifTests {

    @Test
    fun testElif_1() {
        val expr = """
            fun expr(a Int) -> Int {
                if a > 10 and a < 100 {
                    ret 1
                } elif a > 100 {
                    ret 2
                } else {
                    ret 0
                }    
            }
        """

        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<Int>("expr", 101)
            .isEqualTo(2)
    }

    @Test
    fun testElif_2() {
        val expr = """
            fun expr(a Int) -> Int {
                var b = 123
                if a > 10 and a < 100 {
                    b = 1
                } elif a > 100 {
                    b = 2
                } else {
                    b = 0
                }    
                ret b
            }
        """

        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<Int>("expr", 101)
            .isEqualTo(2)
    }

    @Test
    fun testElif_3() {
        val expr = """
            fun expr(a Int) -> Int {
                var b = 123
                if a > 10 and a < 100 {
                    b = 1
                } elif a > 100 {
                    b = 2
                }    
                ret b
            }
        """

        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<Int>("expr", 101)
            .isEqualTo(2)
    }

    @Test
    fun testElif_4() {
        val expr = """
            fun expr(a Int) -> Int {
                var b = 123
                if a > 10 and a < 100 {
                    b = 1
                } elif a > 100 and a <= 200 {
                    b = 2
                } elif a > 200 {
                    b = 3
                }   
                ret b
            }
        """

        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<Int>("expr", 201)
            .isEqualTo(3)
    }

    @Test
    fun testElif_5() {
        val expr = """
            fun expr(a Int) -> Int {
                var b = 123
                if a > 10 and a < 100 {
                    b = 1
                } elif a > 100 {
                    b = 2
                } elif a > 200 {
                    b = 3
                } else {
                    b = 4
                }    
                ret b
            }
        """

        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<Int>("expr", 2)
            .isEqualTo(4)
    }

    @Test
    fun testElif_6() {
        val expr = """
            fun expr(a Int) -> Int {
                var b = 123
                if a > 10 and a < 100 {
                    b = 1
                } elif a > 100 {
                    b = 2
                }    
                if a > 200 {
                    b = 3
                } else { 
                    b = 4
                }    
                ret b
            }
        """

        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<Int>("expr", 2)
            .isEqualTo(4)
    }

    @Test
    fun testElif_7() {
        val expr = """
            import java.util.List
            import java.util.Iterator

            fun expr(a Iterator<Int>, b List<Int>) -> Int {
                var c = 123
                if a.hasNext() and b.size() > 1 {
                    c = 1
                } elif b.size() > 100 {
                    c = 2
                }    
                ret c
            }
        """

        FunctionCompiler.getInstance()
            .compile(expr)
            .invoke<Int>("expr", listOf(1,2,3).iterator(), listOf(1,2,3))
            .isEqualTo(1)
    }
}
