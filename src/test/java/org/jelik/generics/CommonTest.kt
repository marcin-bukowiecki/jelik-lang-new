package org.jelik.generics

import org.jelik.compiler.utils.FunctionCompiler
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class CommonTest {

    @Test
    fun shouldReturnObject() {
        val expr = "fun c<T>(arg Object) -> T {\n" +
                "   ret arg as T\n" +
                "}"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Any>("c", "foo")
                .isEqualTo("foo");
    }

    @Test
    fun shouldReturnSerializable_1() {
        val expr = "import java.io.Serializable\n" +
                "fun c<T : Serializable>(arg Object) -> T {\n" +
                "   ret arg as T\n" +
                "}"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Any>("c", "foo")
                .isEqualTo("foo");
    }

    @Test
    fun shouldReturnSerializable_2() {
        val expr = "import java.io.Serializable\n" +
                "fun c<T : Serializable>(arg T) -> T {\n" +
                "   ret arg as T\n" +
                "}"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Any>("c", "foo")
                .isEqualTo("foo");
    }

    @Test
    fun shouldReturnSerializable_3() {
        val expr = "import java.io.Serializable\n" +
                "fun call() -> Serializable { ret c(\"foo\") }" +
                "\n" +
                "fun c<T : Serializable>(arg T) -> T {\n" +
                "   ret arg as T\n" +
                "}"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Any>("call")
                .isEqualTo("foo");
    }

    @Test
    fun shouldReturnSerializable_4() {
        val expr = "import java.io.Serializable\n" +
                "fun call() -> String { ret c<String>(\"foo\") }" +
                "\n" +
                "fun c<T : Serializable>(arg T) -> T {\n" +
                "   ret arg as T\n" +
                "}"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Any>("call")
                .isEqualTo("foo");
    }

    @Test
    fun shouldReturnInt() {
        val expr = "fun c<T>(arg T) -> Int {\n" +
                "   ret arg.hashCode()\n" +
                "}"
        FunctionCompiler.getInstance()
                .compile(expr)
                .invoke<Any>("c", "foo")
                .isEqualTo(101574);
    }
}
