package org.jelik.parser.interfaces

import org.assertj.core.api.Assertions
import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Ignore
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class InterfaceParserTest {

    @Test
    fun testParseInterface1() {
        val given = """
            interface Foo {
                
                met bar()
            }
        """.trimIndent()

        val result = FunctionCompiler.getInstance().compile(given)
        Assertions.assertThat(result.isInterface)
        Assertions.assertThat(result.hasMethod("bar"))
    }

    @Test
    fun testParseInterface2() {
        val given = """
            interface Foo {
                
                met bar()
                
                fun bar_2() {
                
                }
            }
        """.trimIndent()

        val result = FunctionCompiler.getInstance().compile(given)
        Assertions.assertThat(result.isInterface)
        Assertions.assertThat(result.hasMethod("bar"))
        Assertions.assertThat(result.hasMethod("bar_2"))
    }

    @Test
    fun testParseInterface3() {
        val given = """
            interface Foo {

            }
        """.trimIndent()

        val result = FunctionCompiler.getInstance().compile(given)
        Assertions.assertThat(result.isInterface)
    }

    @Test
    @Ignore
    fun testParseInterface4() {
        val given = """
            interface Foo {

            }
            
            class Bar : Foo {
            
            }
        """.trimIndent()

        val result = FunctionCompiler.getInstance().compile(given)
        Assertions.assertThat(result.isInterface)
    }
}